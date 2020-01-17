package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.model.manager.MarcaManager;
import com.vandamodaintima.jfpsb.contador.model.manager.ProdutoManager;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.Locale;

public class ExcelProdutoStrategy implements IExcelStrategy<Produto> {
    @Override
    public String escreveDados(XSSFWorkbook workbook, XSSFSheet sheet, Object lista) {
        CellStyle cellStyle = workbook.createCellStyle();

        Font fonte = workbook.createFont();
        fonte.setFontHeightInPoints((short) 12);

        cellStyle.setFont(fonte);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);

        ArrayList<Produto> produtos = (ArrayList<Produto>) lista;

        Row[] rows = new Row[produtos.size()];

        for (int i = 1; i <= rows.length; i++) {
            rows[i - 1] = sheet.createRow(i);
            for (int j = 0; j < Produto.getHeaders().length; j++) {
                Cell cell = rows[i - 1].createCell(j);
                cell.setCellStyle(cellStyle);
            }
        }

        for (int i = 0; i < rows.length; i++) {
            Produto p = produtos.get(i);

            rows[i].getCell(0).setCellValue(p.getCod_barra());
            rows[i].getCell(1).setCellValue(p.getDescricao());
            rows[i].getCell(2).setCellValue(p.getPreco());
            rows[i].getCell(3).setCellValue(p.getFornecedor() == null ? "NÃO POSSUI" : p.getFornecedor().getNome());
            rows[i].getCell(4).setCellValue(p.getMarca() == null ? "NÃO POSSUI" : p.getMarca().getNome());

            String codigos = "";

            for (int j = 0; j < p.getCod_barra_fornecedor().size(); j++) {
                codigos += p.getCod_barra_fornecedor().get(j);

                if (j != p.getCod_barra_fornecedor().size() - 1)
                    codigos += ",";
            }

            rows[i].getCell(5).setCellValue(codigos);
        }

        sheet.setColumnWidth(0, 25 * 256);
        sheet.setColumnWidth(1, 70 * 256);
        sheet.setColumnWidth(2, 40 * 256);
        sheet.setColumnWidth(3, 40 * 256);
        sheet.setColumnWidth(4, 40 * 256);
        sheet.setColumnWidth(5, 40 * 256);

        return "Produtos.xlsx";
    }

    @Override
    public Boolean lerInserirDados(XSSFWorkbook workbook, XSSFSheet sheet, ConexaoBanco conexaoBanco) {
        ArrayList<Produto> produtos = new ArrayList<>();
        ProdutoManager produtoManager = new ProdutoManager(conexaoBanco);
        FornecedorManager fornecedorManager = new FornecedorManager(conexaoBanco);
        MarcaManager marcaManager = new MarcaManager(conexaoBanco);

        Row cabecalho = sheet.getRow(0);
        int numCols = cabecalho.getPhysicalNumberOfCells();
        if (numCols != Produto.getHeaders().length) {
            Log.e("Contador", "O Número de Colunas Está Errado");
            return false;
        }

        int rows = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i < rows; i++) {
            Row row = sheet.getRow(i);

            Produto p = new Produto();

            Cell cod_barra = row.getCell(0);
            Cell descricao = row.getCell(1);
            Cell preco = row.getCell(2);
            Cell fornecedor = row.getCell(3);
            Cell marca = row.getCell(4);
            Cell codbarrafornecedor = row.getCell(5);

            if (cod_barra != null && cod_barra.getCellType() != Cell.CELL_TYPE_BLANK) {
                if (cod_barra.getCellType() == Cell.CELL_TYPE_STRING) {
                    p.setCod_barra(cod_barra.getStringCellValue());
                } else if (cod_barra.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    p.setCod_barra(String.format(Locale.ENGLISH, "%.0f", cod_barra.getNumericCellValue()));
                } else {
                    Log.i("Contador", "Código de Barra Vazio");
                    continue;
                }
            }

            if (descricao != null && descricao.getCellType() != Cell.CELL_TYPE_BLANK) {
                if (descricao.getCellType() == Cell.CELL_TYPE_STRING) {
                    p.setDescricao(descricao.getStringCellValue());
                } else {
                    Log.i("Contador", "Descrição Vazia");
                    continue;
                }
            }

            if (preco != null && preco.getCellType() != Cell.CELL_TYPE_BLANK) {
                if (preco.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    p.setPreco(preco.getNumericCellValue());
                } else {
                    Log.i("Contador", "Preço Vazio");
                    continue;
                }
            }

            if (fornecedor != null && fornecedor.getCellType() != Cell.CELL_TYPE_BLANK) {
                if (fornecedor.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    //O Excel apaga zeros iniciais de células configuradas como números então aqui eu os coloco de volta
                    String cnpj = String.format(Locale.ENGLISH, "%.0f", fornecedor.getNumericCellValue());

                    if (cnpj.length() != 14) {
                        int diff = 14 - cnpj.length();

                        String append = "";

                        for (int j = 0; j < diff; j++) {
                            append += "0";
                        }

                        append += cnpj;
                        cnpj = append;
                    }
                    p.setFornecedor(fornecedorManager.listarPorId(cnpj));
                } else if (fornecedor.getCellType() == Cell.CELL_TYPE_STRING) {
                    p.setFornecedor(fornecedorManager.listarPorIdOuNome(fornecedor.getStringCellValue()));
                } else {
                    Log.i("Contador", "Fornecedor Não Encontrado ou Vazio");
                    p.setFornecedor(null);
                }
            }

            if (marca != null && marca.getCellType() != Cell.CELL_TYPE_BLANK) {
                if (marca.getCellType() == Cell.CELL_TYPE_STRING) {
                    p.setMarca(marcaManager.listarPorId(marca.getStringCellValue()));
                } else {
                    Log.i("Contador", "Fornecedor Não Encontrada ou Vazia");
                    p.setMarca(null);
                }
            }

            if (codbarrafornecedor != null && codbarrafornecedor.getCellType() != Cell.CELL_TYPE_BLANK) {
                if (marca.getCellType() == Cell.CELL_TYPE_STRING) {
                    String conteudo = codbarrafornecedor.getStringCellValue();
                    String[] partes = null;
                    if (conteudo.contains(",")) {
                        partes = conteudo.split(",");

                        for (String s : partes) {
                            p.getCod_barra_fornecedor().add(s);
                        }
                    } else {
                        p.getCod_barra_fornecedor().add(conteudo);
                    }
                } else {
                    Log.i("Contador", "Cód. de Barras de Fornecedor em Formato Errado");
                    continue;
                }
            }

            produtos.add(p);
        }

        return produtoManager.salvar(produtos);
    }

    @Override
    public String[] getHeaders() {
        return Produto.getHeaders();
    }
}
