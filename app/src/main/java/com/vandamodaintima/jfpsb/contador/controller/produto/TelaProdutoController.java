package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.arquivo.Arquivo;
import com.vandamodaintima.jfpsb.contador.excel.ArquivoExcel;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOMarca;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.view.produto.TelaProduto;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class TelaProdutoController {
    private TelaProduto telaProduto;
    private SQLiteDatabase sqLiteDatabase;
    private DAOProduto daoProduto;
    private DAOFornecedor daoFornecedor;
    private DAOMarca daoMarca;
    private AsyncTask task;

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

    public TelaProdutoController(TelaProduto telaProduto, SQLiteDatabase sqLiteDatabase) {
        this.telaProduto = telaProduto;
        this.sqLiteDatabase = sqLiteDatabase;
        daoProduto = new DAOProduto(sqLiteDatabase);
        daoFornecedor = new DAOFornecedor(sqLiteDatabase);
        daoMarca = new DAOMarca(sqLiteDatabase);
    }

    public Boolean importarDeArquivoExcel(final ContentResolver contentResolver, Uri filepath, TelaProduto.ImportarProdutoAsyncTask.Progresso progresso) {
        ArrayList<Produto> produtos = new ArrayList<>();

        InputStream inputStream = null;

        progresso.publish("Iniciando Cadastro");

        try {
            if (task == null) {
                throw new Exception("Você precisa passar setar o AsyncTask!");
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

            if (!headerRow.getCell(Headers.COD_BARRA.ordinal()).getStringCellValue().equals(Headers.COD_BARRA.texto)) {
                throw new Exception("Cabeçalho da Primeira Coluna Está Errado");
            }

            if (!headerRow.getCell(Headers.FORNECEDOR.ordinal()).getStringCellValue().equals(Headers.FORNECEDOR.texto)) {
                throw new Exception("Cabeçalho da Segunda Coluna Está Errado");
            }

            if (!headerRow.getCell(Headers.COD_BARRA_FORNECEDOR.ordinal()).getStringCellValue().equals(Headers.COD_BARRA_FORNECEDOR.texto)) {
                throw new Exception("Cabeçalho da Terceira Coluna Está Errado");
            }

            if (!headerRow.getCell(Headers.DESCRICAO.ordinal()).getStringCellValue().equals(Headers.DESCRICAO.texto)) {
                throw new Exception("Cabeçalho da Quarta Coluna Está Errado");
            }

            if (!headerRow.getCell(Headers.MARCA.ordinal()).getStringCellValue().equals(Headers.MARCA.texto)) {
                throw new Exception("Cabeçalho da Quinta Coluna Está Errado");
            }

            if (!headerRow.getCell(Headers.PRECO.ordinal()).getStringCellValue().equals(Headers.PRECO.texto)) {
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

                if (!isCellEmpty(cod_barra)) {
                    if (cod_barra.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        produto.setCod_barra(String.format("%.0f", cod_barra.getNumericCellValue())); //Retirando o .0
                    } else if (cod_barra.getCellType() == Cell.CELL_TYPE_STRING) {
                        produto.setCod_barra(cod_barra.getStringCellValue());
                    } else {
                        throw new Exception("Formato de Código de Barras Errado. A Célula Precisa Ser do Tipo \"Texto\" ou \"Número\"");
                    }
                } else {
                    throw new Exception("A Célula de Código de Barras Não Pode Estar Vazia");
                }

                if (!isCellEmpty(fornecedor)) {
                    if (fornecedor.getCellType() == Cell.CELL_TYPE_STRING) {
                        Fornecedor f = daoFornecedor.listarPorId(fornecedor.getStringCellValue());

                        if (f != null) {
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

                if (!isCellEmpty(cod_barra_fornecedor)) {
                    String codigosCell = null;

                    if (cod_barra_fornecedor.getCellType() == Cell.CELL_TYPE_STRING) {
                        codigosCell = cod_barra_fornecedor.getStringCellValue().replace(" ", "");
                    }

                    if (cod_barra_fornecedor.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        codigosCell = String.valueOf(cod_barra_fornecedor.getNumericCellValue()).replace(" ", "");
                    }

                    String[] codigos = codigosCell.split(",");

                    produto.setCod_barra_fornecedor(new ArrayList<>(Arrays.asList(codigos)));
                }

                if (!isCellEmpty(descricao)) {
                    if (descricao.getCellType() == Cell.CELL_TYPE_STRING) {
                        produto.setDescricao(descricao.getStringCellValue());
                    } else {
                        throw new Exception("Formato de Descrição do Produto Está Errado. A Célula Precisa ser do Tipo \"Texto\"");
                    }
                } else {
                    throw new Exception("A Célula de Descrição Não Pode Estar Vazia");
                }

                if (!isCellEmpty(marca)) {
                    if (marca.getCellType() == Cell.CELL_TYPE_STRING) {
                        Marca m = daoMarca.listarPorNome(marca.getStringCellValue()).get(0);

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

                if (!isCellEmpty(preco)) {
                    if (preco.getCellType() == Cell.CELL_TYPE_NUMERIC) {
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

            Boolean result = daoProduto.importarDeExcel(produtos, progresso);

            if (result) {
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

    public void exportarProdutosEmExcel(String diretorio, TelaProduto.ExportarProdutoEmExcel.Progresso progresso) {
        progresso.publish("Iniciando Exportação. Aguarde");

        Date dataAtual = new Date();
        ArquivoExcel arquivoExcel = new ArquivoExcel();
        int rowIndex = 1; //Inicia após cabeçalho
        OutputStream outputStream = null;

        setCabecalhoProduto(arquivoExcel);

        CellStyle cellStyle = CellStylePadrao(arquivoExcel);

        ArrayList<Produto> produtos = daoProduto.listar();

        try {
            if (produtos.size() == 0)
                throw new Exception("Não Há Produtos Cadastrados");

            Row[] rows = new Row[produtos.size()];

            for (int i = rowIndex; i <= rows.length; i++) {
                Row row = arquivoExcel.getPlanilha().createRow(i);

                for (int j = 0; j < Produto.getHeaders().length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                }

                rows[i - 1] = row;
            }

            for (int i = 0; i < rows.length; i++) {
                Produto produto = produtos.get(i);

                rows[i].getCell(0).setCellValue(produto.getCod_barra());

                String codbarrafornecedor = "";

                for (int j = 0; j < produto.getCod_barra_fornecedor().size(); j++) {
                    codbarrafornecedor += produto.getCod_barra_fornecedor().get(j);

                    if (j != produto.getCod_barra_fornecedor().size() - 1) {
                        codbarrafornecedor += ", ";
                    }
                }

                rows[i].getCell(1).setCellValue(codbarrafornecedor);
                if (produto.getFornecedor() != null) {
                    rows[i].getCell(2).setCellValue(produto.getFornecedor().getNome());
                } else {
                    rows[i].getCell(2).setCellValue("Não Possui");
                }

                if (produto.getMarca() != null) {
                    rows[i].getCell(3).setCellValue(produto.getMarca().getNome());
                } else {
                    rows[i].getCell(3).setCellValue("Não Possui");
                }

                rows[i].getCell(4).setCellValue(produto.getDescricao());
                rows[i].getCell(5).setCellValue(produto.getPreco());

                if (i % 100 == 0 && i > 99) {
                    progresso.publish(i + " Produtos Já Listados");
                }
            }

            arquivoExcel.getPlanilha().setColumnWidth(0, 25 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(1, 70 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(2, 40 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(3, 40 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(4, 40 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(5, 40 * 256);

            String nomeArquivo = "Produtos " + new SimpleDateFormat("yyyy-MM-dd HHmmss").format(dataAtual) + ".xlsx";

            File arquivo = new File(diretorio, nomeArquivo);

            outputStream = new FileOutputStream(arquivo.getAbsolutePath());

            progresso.publish("Escrevendo Arquivo Em Disco");
            arquivoExcel.getPastaTrabalho().write(outputStream);

            progresso.publish("Produtos Exportados Com Sucesso");

            return;
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

        progresso.publish("Erro ao Exportar Produtos");
    }

    private boolean isCellEmpty(Cell cell) {
        if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return true;
        }

        if (cell.getCellType() == Cell.CELL_TYPE_STRING && cell.getStringCellValue().isEmpty()) {
            return true;
        }

        return false;
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

    public void setTask(AsyncTask task) {
        this.task = task;
    }
}
