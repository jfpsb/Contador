package com.vandamodaintima.jfpsb.contador.excel;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.arquivo.Arquivo;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.dao.manager.ContagemProdutoManager;
import com.vandamodaintima.jfpsb.contador.dao.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.manager.produto.TelaProduto;
import com.vandamodaintima.jfpsb.contador.util.TrataDisplayData;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jfpsb on 16/02/2018.
 */

public class ManipulaExcel {
    private static DAOProduto daoProduto;
    private ContagemProdutoManager contagemProdutoManager;
    private FornecedorManager fornecedorManager;
    private AsyncTask task;
    private int ProdutosCadastrados = 0;
    private ConexaoBanco conexaoBanco;

    public ManipulaExcel(ConexaoBanco conexao) {
        conexaoBanco = conexao;
    }

    public ManipulaExcel(AsyncTask task, ConexaoBanco conexao) {
        this.task = task;
        conexaoBanco = conexao;
    }

    public int ImportaProduto(final ContentResolver contentResolver, Uri filepath, TelaProduto.Tarefa.Progresso progresso) {
        daoProduto = new DAOProduto(conexaoBanco.conexao());

        Produto[] produtos = null;

        InputStream inputStream = null;

        progresso.publish("Iniciando Cadastro");

        try {
            if (task == null) {
                throw new Exception("Você precisa passar um objeto AsyncTask no construtor!");
            }

            inputStream = Arquivo.getInputStreamFromUri(contentResolver, filepath);

            if (inputStream == null) {
                throw new Exception("InputStream de arquivo Excel escolhido voltou nula");
            }

            ArquivoExcel arquivoExcel = new ArquivoExcel(inputStream);

            Row auxRow = arquivoExcel.getPlanilha().getRow(0);

            if (auxRow.getPhysicalNumberOfCells() != 3) {
                throw new Exception("Planilha Configurada de Forma Errada. A Planilha Deve Conter Três Colunas contendo Cód. de Barras do Produto, Descrição e Preço.");
            }

            int rows = arquivoExcel.getPlanilha().getPhysicalNumberOfRows();

            progresso.publish(rows + " Produto(s) Encontrado(s)");

            produtos = new Produto[rows - 1];

            try {

                for (int i = 1; i < rows; i++) {
                    Row row = arquivoExcel.getPlanilha().getRow(i);

                    int cells = row.getPhysicalNumberOfCells();

                    Produto produto = new Produto();

                    for (int j = 0; j < cells; j++) {
                        Cell cell = row.getCell(j);

                        CellValue cellValue = arquivoExcel.getFormulaEvaluator().evaluate(cell);

                        if (cellValue == null)
                            continue;

                        switch (cellValue.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                produto.setPreco(cell.getNumericCellValue());
                                break;
                            case Cell.CELL_TYPE_STRING:
                                if (cell.getColumnIndex() == 0) {
                                    produto.setCod_barra(cell.getStringCellValue());
                                } else if (cell.getColumnIndex() == 1) {
                                    produto.setDescricao(cell.getStringCellValue());
                                }
                                break;
                        }

                        produto.setFornecedor(null);

                        if (produto != null && produto.getDescricao() != null && produto.getCod_barra() != null) {
                            produtos[i - 1] = produto;
                        }
                    }
                }

                for (int i = 0; i < produtos.length; i++) {
                    long result = daoProduto.inserirBulk(produtos[i]);

                    if (result != -1) {
                        progresso.publish("Produto com Cód. " + produtos[i].getCod_barra() + " Cadastrado");
                        ProdutosCadastrados++;
                    }
                }
            } catch (Exception e) {
                Log.e("Contador", e.getMessage(), e);
            }

            return ProdutosCadastrados;
        } catch (Exception e) {
            Log.i("Contador", e.getMessage());
            progresso.publish(e.getMessage());
            return -1;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e("Contador", "Ao tentar fechar InputStream: " + e.getMessage(), e);
                }
            }
        }
    }

    //TODO: Tentar simplificar. Estilizar planilha
    public boolean ExportaContagem(Contagem contagem, String diretorio) {
        contagemProdutoManager = new ContagemProdutoManager(conexaoBanco);

        Date dataAtual = new Date();
        ArquivoExcel arquivoExcel = new ArquivoExcel();
        int rowIndex = 1; //Inicia após cabeçalho
        OutputStream outputStream = null;

        setCabecalhoContagem(arquivoExcel);

        CellStyle cellStyle = CellStylePadrao(arquivoExcel);

        ArrayList<ContagemProduto> contagemproduto = contagemProdutoManager.listarPorContagem(contagem.getIdcontagem());

        try {
            if (contagemproduto.size() == 0)
                throw new Exception("Não Há Produtos na Contagem");

            for (int i = rowIndex; i <= contagemproduto.size(); i++) {
                Row row = arquivoExcel.getPlanilha().createRow(i);

                ContagemProduto contagemProduto = contagemproduto.get(i - 1);

                Cell cod_barra = row.createCell(0);
                cod_barra.setCellValue(contagemProduto.getProduto().getCod_barra());

                Cell descricao = row.createCell(1);
                descricao.setCellValue(contagemProduto.getProduto().getDescricao());

                Cell quant = row.createCell(2);
                quant.setCellValue(contagemProduto.getQuant());

                cod_barra.setCellStyle(cellStyle);
                descricao.setCellStyle(cellStyle);
                quant.setCellStyle(cellStyle);
            }

            arquivoExcel.getPlanilha().setColumnWidth(0, 23 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(1, 65 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(2, 18 * 256);

            String nomeArquivo = "Contagem - " + contagem.getLoja().getNome() + " - " + TrataDisplayData.getDataEmString(dataAtual) + ".xlsx";

            File arquivo = new File(diretorio, nomeArquivo);

            outputStream = new FileOutputStream(arquivo.getAbsolutePath());

            arquivoExcel.getPastaTrabalho().write(outputStream);

            return true;
        } catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    //TODO: Tentar simplificar. Estilizar planilha
    public boolean ExportaFornecedor(String diretorio) {
        fornecedorManager = new FornecedorManager(conexaoBanco);

        Date dataAtual = new Date();
        ArquivoExcel arquivoExcel = new ArquivoExcel();
        int rowIndex = 1; //Inicia após cabeçalho
        OutputStream outputStream = null;

        setCabecalhoFornecedor(arquivoExcel);

        CellStyle cellStyle = CellStylePadrao(arquivoExcel);

        ArrayList<Fornecedor> fornecedores = fornecedorManager.listar();

        try {
            if (fornecedores.size() == 0)
                throw new Exception("Não Há Fornecedores Cadastrados");

            for (int i = rowIndex; i <= fornecedores.size(); i++) {
                Row row = arquivoExcel.getPlanilha().createRow(i);

                Fornecedor fornecedor = fornecedores.get(i - 1);

                Cell cnpj = row.createCell(0);
                cnpj.setCellValue(fornecedor.getCnpj());

                Cell nome = row.createCell(1);
                nome.setCellValue(fornecedor.getNome());

                Cell fantasia = row.createCell(2);
                fantasia.setCellValue(fornecedor.getFantasia());

                cnpj.setCellStyle(cellStyle);
                nome.setCellStyle(cellStyle);
                fantasia.setCellStyle(cellStyle);
            }

            arquivoExcel.getPlanilha().setColumnWidth(0, 25 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(1, 70 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(2, 40 * 256);

            String nomeArquivo = "Fornecedores " + TrataDisplayData.getDataEmString(dataAtual) + ".xlsx";

            File arquivo = new File(diretorio, nomeArquivo);

            outputStream = new FileOutputStream(arquivo.getAbsolutePath());

            arquivoExcel.getPastaTrabalho().write(outputStream);

            return true;
        } catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private void setCabecalhoContagem(ArquivoExcel arquivoExcel) {
        CellStyle cabecalhoStyle = CellStylePadrao(arquivoExcel);

        Font font = arquivoExcel.getPastaTrabalho().createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);

        cabecalhoStyle.setFont(font);
        cabecalhoStyle.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);

        Row cabecalho = arquivoExcel.getPlanilha().createRow(0);

        Cell cab1 = cabecalho.createCell(0);
        cab1.setCellValue("Cód. de Barra");

        Cell cab2 = cabecalho.createCell(1);
        cab2.setCellValue("Descrição");

        Cell cab3 = cabecalho.createCell(2);
        cab3.setCellValue("Quantidade");

        cab1.setCellStyle(cabecalhoStyle);
        cab2.setCellStyle(cabecalhoStyle);
        cab3.setCellStyle(cabecalhoStyle);
    }

    private void setCabecalhoFornecedor(ArquivoExcel arquivoExcel) {
        CellStyle cabecalhoStyle = CellStylePadrao(arquivoExcel);

        Font font = arquivoExcel.getPastaTrabalho().createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);

        cabecalhoStyle.setFont(font);
        cabecalhoStyle.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);

        Row cabecalho = arquivoExcel.getPlanilha().createRow(0);

        Cell cab1 = cabecalho.createCell(0);
        cab1.setCellValue("CNPJ");

        Cell cab2 = cabecalho.createCell(1);
        cab2.setCellValue("Nome");

        Cell cab3 = cabecalho.createCell(2);
        cab3.setCellValue("Nome Fantasia");

        cab1.setCellStyle(cabecalhoStyle);
        cab2.setCellStyle(cabecalhoStyle);
        cab3.setCellStyle(cabecalhoStyle);
    }

    private CellStyle CellStylePadrao(ArquivoExcel arquivoExcel) {
        CellStyle cellStyle = arquivoExcel.getPastaTrabalho().createCellStyle();

        Font font = arquivoExcel.getPastaTrabalho().createFont();
        font.setFontHeightInPoints((short) 12);

        cellStyle.setFont(font);

        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);

        return cellStyle;
    }
}