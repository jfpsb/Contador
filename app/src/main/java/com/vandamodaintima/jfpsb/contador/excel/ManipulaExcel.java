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
import com.vandamodaintima.jfpsb.contador.dao.manager.MarcaManager;
import com.vandamodaintima.jfpsb.contador.dao.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Marca;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.manager.produto.TelaProduto;
import com.vandamodaintima.jfpsb.contador.util.TrataDisplayData;

import org.apache.poi.hssf.record.HeaderRecord;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

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
    private ProdutoManager produtoManager;
    private MarcaManager marcaManager;
    private AsyncTask task;
    private ConexaoBanco conexaoBanco;

    private enum Headers {
        COD_BARRA("Código de Barra"),
        FORNECEDOR("Fornecedor (CNPJ)"),
        COD_BARRA_FORNECEDOR("Códigos de Barra de Fornecedor"),
        DESCRICAO("Descrição"),
        MARCA("Marca (Nome)"),
        PRECO("Preço");

        public String texto;

        Headers(String texto) {
            this.texto = texto;
        }
    }



    public ManipulaExcel(ConexaoBanco conexao) {
        conexaoBanco = conexao;
    }

    public ManipulaExcel(AsyncTask task, ConexaoBanco conexao) {
        this.task = task;
        conexaoBanco = conexao;
    }

    public boolean ImportaProduto(final ContentResolver contentResolver, Uri filepath, TelaProduto.Tarefa.Progresso progresso) {
        daoProduto = new DAOProduto(conexaoBanco.conexao());
        marcaManager = new MarcaManager(conexaoBanco);
        fornecedorManager = new FornecedorManager(conexaoBanco);

        ArrayList<Produto> produtos = new ArrayList<>();

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

            Row headerRow = arquivoExcel.getPlanilha().getRow(0);

            if (headerRow.getPhysicalNumberOfCells() != 6) {
                throw new Exception("Planilha Configurada de Forma Errada");
            }

            if(! headerRow.getCell(Headers.COD_BARRA.ordinal()).getStringCellValue().equals(Headers.COD_BARRA.texto)) {
                throw new Exception("Cabeçalho da Primeira Coluna Está Errado");
            }

            if(! headerRow.getCell(Headers.FORNECEDOR.ordinal()).getStringCellValue().equals(Headers.FORNECEDOR.texto)) {
                throw new Exception("Cabeçalho da Segunda Coluna Está Errado");
            }

            if(! headerRow.getCell(Headers.COD_BARRA_FORNECEDOR.ordinal()).getStringCellValue().equals(Headers.COD_BARRA_FORNECEDOR.texto)) {
                throw new Exception("Cabeçalho da Terceira Coluna Está Errado");
            }

            if(! headerRow.getCell(Headers.DESCRICAO.ordinal()).getStringCellValue().equals(Headers.DESCRICAO.texto)) {
                throw new Exception("Cabeçalho da Quarta Coluna Está Errado");
            }

            if(! headerRow.getCell(Headers.MARCA.ordinal()).getStringCellValue().equals(Headers.MARCA.texto)) {
                throw new Exception("Cabeçalho da Quinta Coluna Está Errado");
            }

            if(! headerRow.getCell(Headers.PRECO.ordinal()).getStringCellValue().equals(Headers.PRECO.texto)) {
                throw new Exception("Cabeçalho da Sexta Coluna Está Errado");
            }

            int rows = arquivoExcel.getPlanilha().getPhysicalNumberOfRows();

            progresso.publish(rows + " Produto(s) Encontrado(s)");

            for (int i = 1; i < rows; i++) {
                Row row = arquivoExcel.getPlanilha().getRow(i);

                Produto produto = new Produto();

                Cell cod_barra = row.getCell(Headers.COD_BARRA.ordinal());
                Cell fornecedor = row.getCell(Headers.FORNECEDOR.ordinal());
                Cell cod_barra_fornecedor = row.getCell(Headers.COD_BARRA_FORNECEDOR.ordinal());
                Cell descricao = row.getCell(Headers.DESCRICAO.ordinal());
                Cell marca = row.getCell(Headers.MARCA.ordinal());
                Cell preco = row.getCell(Headers.PRECO.ordinal());

                if(! isCellEmpty(cod_barra)) {
                    if(cod_barra.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        produto.setCod_barra(String.format("%.0f", cod_barra.getNumericCellValue())); //Retirando o .0
                    } else if(cod_barra.getCellType() == Cell.CELL_TYPE_STRING) {
                        produto.setCod_barra(cod_barra.getStringCellValue());
                    } else {
                        throw new Exception("Formato de Código de Barras Errado. A Célula Precisa Ser do Tipo \"Texto\" ou \"Número\"");
                    }
                } else {
                    throw new Exception("A Célula de Código de Barras Não Pode Estar Vazia");
                }

                if(! isCellEmpty(fornecedor)) {
                    if(fornecedor.getCellType() == Cell.CELL_TYPE_STRING) {
                        Fornecedor f = fornecedorManager.listarPorChave(fornecedor.getStringCellValue());

                        if(f != null) {
                            produto.setFornecedor(f);
                        } else {
                            throw new Exception("Fornecedor " + fornecedor.getStringCellValue() + "Informado Não Encontrado");
                        }
                    } else {
                        throw new Exception("Formato de CNPJ de Fornecedor Está Errado. A Célula Precisa ser do Tipo \"Texto\"");
                    }
                } else {
                    produto.setFornecedor(null);
                }

                if(! isCellEmpty(cod_barra_fornecedor)) {
                    if(cod_barra_fornecedor.getCellType() == Cell.CELL_TYPE_STRING) {
                        //TODO: Separar códigos e inserir
                    } else {
                        throw new Exception("Formato de Código de Barras de Fornecedor Está Errado. A Célula Precisa ser do Tipo \"Texto\"");
                    }
                }

                if(! isCellEmpty(descricao)) {
                    if(descricao.getCellType() == Cell.CELL_TYPE_STRING) {
                        produto.setDescricao(descricao.getStringCellValue());
                    } else {
                        throw new Exception("Formato de Descrição do Produto Está Errado. A Célula Precisa ser do Tipo \"Texto\"");
                    }
                } else {
                    throw new Exception("A Célula de Descrição Não Pode Estar Vazia");
                }

                if(! isCellEmpty(marca)) {
                    if (marca.getCellType() == Cell.CELL_TYPE_STRING) {
                        Marca m = marcaManager.listarPorNome(marca.getStringCellValue());

                        if (m != null) {
                            produto.setMarca(m);
                        } else {
                            throw new Exception("Marca " + marca.getStringCellValue() + " Não Encontrada");
                        }
                    } else {
                        throw new Exception("Formato de Marca do Produto Está Errado. A Célula Precisa ser do Tipo \"Texto\"");
                    }
                } else {
                    produto.setMarca(null);
                }

                if(! isCellEmpty(preco)) {
                    if(preco.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        produto.setPreco(preco.getNumericCellValue());
                    } else {
                        throw new Exception("Formato de Preço do Produto Está Errado. A Célula Precisa ser do Tipo \"Número\"");
                    }
                } else {
                    throw new Exception("A Célula de Preço Não Pode Estar Vazia");
                }

                if (produto != null && produto.getDescricao() != null && produto.getCod_barra() != null && produto.getPreco() != null) {
                    produtos.add(produto);
                }
            }

            long result = daoProduto.inserirVarios(produtos, progresso);

            if(result == 1) {
                progresso.publish("Produtos Cadastrados com Sucesso");
                return true;
            }
        } catch (Exception e) {
            Log.i("Contador", e.getMessage());
            progresso.publish(e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e("Contador", "Ao tentar fechar InputStream: " + e.getMessage(), e);
                }
            }
        }

        return false;
    }

    private boolean isCellEmpty(Cell cell) {
        if(cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return true;
        }

        if (cell.getCellType() == Cell.CELL_TYPE_STRING && cell.getStringCellValue().isEmpty()) {
            return true;
        }

        return false;
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

        ArrayList<ContagemProduto> contagemproduto = contagemProdutoManager.listarPorContagem(contagem);

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

            String nomeArquivo = "Contagem - " + contagem.getLoja().getNome() + " - " + TrataDisplayData.getDataFormatoBD(dataAtual) + ".xlsx";

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

            String nomeArquivo = "Fornecedores " + TrataDisplayData.getDataFormatoBD(dataAtual) + ".xlsx";

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
    public boolean ExportaProduto(String diretorio) {
        produtoManager = new ProdutoManager(conexaoBanco);

        Date dataAtual = new Date();
        ArquivoExcel arquivoExcel = new ArquivoExcel();
        int rowIndex = 1; //Inicia após cabeçalho
        OutputStream outputStream = null;

        setCabecalhoProduto(arquivoExcel);

        CellStyle cellStyle = CellStylePadrao(arquivoExcel);

        ArrayList<Produto> produtos = produtoManager.listar();

        try {
            if (produtos.size() == 0)
                throw new Exception("Não Há Produtos Cadastrados");

            Row[] rows = new Row[produtos.size()];

            for(int i = rowIndex; i <= rows.length; i++) {
                Row row = arquivoExcel.getPlanilha().createRow(i);

                for(int j = 0; j < Produto.getHeaders().length; i++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                }

                rows[i - 1] = row;
            }

            for (int i = 0; i < rows.length; i++) {
                Produto produto = produtos.get(i);

                rows[i].getCell(0).setCellValue(produto.getCod_barra());
                //TODO: lista de codigos
                rows[i].getCell(2).setCellValue(produto.getFornecedor().getNome());
                rows[i].getCell(3).setCellValue(produto.getMarca().getNome());
                rows[i].getCell(4).setCellValue(produto.getDescricao());
                rows[i].getCell(5).setCellValue(produto.getPreco());
            }

            arquivoExcel.getPlanilha().setColumnWidth(0, 25 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(1, 70 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(2, 40 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(3, 40 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(4, 40 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(5, 40 * 256);

            String nomeArquivo = "Produtos " + TrataDisplayData.getDataFormatoBD(dataAtual) + ".xlsx";

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

    private void setCabecalhoProduto(ArquivoExcel arquivoExcel) {
        CellStyle cabecalhoStyle = CellStylePadrao(arquivoExcel);

        Font font = arquivoExcel.getPastaTrabalho().createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);

        cabecalhoStyle.setFont(font);
        cabecalhoStyle.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);

        Row cabecalho = arquivoExcel.getPlanilha().createRow(0);

        for (int i = 0; i < Produto.getHeaders().length; i++) {
            Cell header = cabecalho.createCell(i);
            header.setCellValue(Produto.getHeaders()[i]);
            header.setCellStyle(cabecalhoStyle);
        }
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