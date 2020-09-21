package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.model.Produto;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
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
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

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
            rows[i].getCell(1).setCellValue(p.getCod_barra_fornecedor());
            rows[i].getCell(2).setCellValue(p.getDescricao());
            rows[i].getCell(3).setCellValue(p.getPreco());
            rows[i].getCell(4).setCellValue(p.getFornecedor() == null ? "NÃO POSSUI" : p.getFornecedor().getNome());
            rows[i].getCell(5).setCellValue(p.getMarca() == null ? "NÃO POSSUI" : p.getMarca().getNome());
            rows[i].getCell(6).setCellValue((p.getNcm() == null || p.getNcm().length() == 0) ? "NÃO POSSUI" : p.getNcm());
        }

        sheet.setColumnWidth(0, 25 * 256);
        sheet.setColumnWidth(1, 70 * 256);
        sheet.setColumnWidth(2, 40 * 256);
        sheet.setColumnWidth(3, 40 * 256);
        sheet.setColumnWidth(4, 40 * 256);
        sheet.setColumnWidth(5, 40 * 256);
        sheet.setColumnWidth(6, 50 * 256);

        return "Produtos.xlsx";
    }

    @Override
    public Boolean lerInserirDados(XSSFWorkbook workbook, XSSFSheet sheet, ConexaoBanco conexaoBanco) {
        ArrayList<Produto> produtos = new ArrayList<>();
        Produto produtoModel = new Produto(conexaoBanco);
        Fornecedor fornecedorModel = new Fornecedor(conexaoBanco);
        Marca marcaModel = new Marca(conexaoBanco);

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
            Cell codbarrafornecedor = row.getCell(1);
            Cell descricao = row.getCell(2);
            Cell preco = row.getCell(3);
            Cell fornecedor = row.getCell(4);
            Cell marca = row.getCell(5);

            if (cod_barra != null && cod_barra.getCellTypeEnum() != CellType.BLANK) {
                if (cod_barra.getCellTypeEnum() == CellType.STRING) {
                    p.setCod_barra(cod_barra.getStringCellValue());
                } else if (cod_barra.getCellTypeEnum() == CellType.NUMERIC) {
                    p.setCod_barra(String.format(Locale.ENGLISH, "%.0f", cod_barra.getNumericCellValue()));
                } else {
                    Log.i("Contador", "Código de Barra Vazio");
                    continue;
                }
            }

            if (codbarrafornecedor != null && codbarrafornecedor.getCellTypeEnum() != CellType.BLANK) {
                if (codbarrafornecedor.getCellTypeEnum() == CellType.STRING) {
                    p.setCod_barra(codbarrafornecedor.getStringCellValue());
                } else if (codbarrafornecedor.getCellTypeEnum() == CellType.NUMERIC) {
                    p.setCod_barra(String.format(Locale.ENGLISH, "%.0f", codbarrafornecedor.getNumericCellValue()));
                }
            }

            if (descricao != null && descricao.getCellTypeEnum() != CellType.BLANK) {
                if (descricao.getCellTypeEnum() == CellType.STRING) {
                    p.setDescricao(descricao.getStringCellValue());
                } else {
                    Log.i("Contador", "Descrição Vazia");
                    continue;
                }
            }

            if (preco != null && preco.getCellTypeEnum() != CellType.BLANK) {
                if (preco.getCellTypeEnum() == CellType.NUMERIC) {
                    p.setPreco(preco.getNumericCellValue());
                } else {
                    Log.i("Contador", "Preço Vazio");
                    continue;
                }
            }

            if (fornecedor != null && fornecedor.getCellTypeEnum() != CellType.BLANK) {
                if (fornecedor.getCellTypeEnum() == CellType.NUMERIC) {
                    //O Excel apaga zeros iniciais de células configuradas como números então aqui eu os coloco de volta
                    String cnpj = String.format(Locale.ENGLISH, "%.0f", fornecedor.getNumericCellValue());

                    if (cnpj.length() != 14) {
                        int diff = 14 - cnpj.length();

                        StringBuilder stringBuilder = new StringBuilder();

                        for (int j = 0; j < diff; j++) {
                            stringBuilder.append("0");
                        }

                        stringBuilder.append(cnpj);
                        cnpj = stringBuilder.toString();
                    }
                    p.setFornecedor(fornecedorModel.listarPorId(cnpj));
                } else if (fornecedor.getCellTypeEnum() == CellType.STRING) {
                    p.setFornecedor(fornecedorModel.listarPorIdOuNome(fornecedor.getStringCellValue()));
                } else {
                    Log.i("Contador", "Fornecedor Não Encontrado ou Vazio");
                    p.setFornecedor(null);
                }
            }

            if (marca != null && marca.getCellTypeEnum() != CellType.BLANK) {
                if (marca.getCellTypeEnum() == CellType.STRING) {
                    p.setMarca(marcaModel.listarPorId(marca.getStringCellValue()));
                } else {
                    Log.i("Contador", "Marca Não Encontrada ou Vazia");
                    p.setMarca(null);
                }
            }

            produtos.add(p);
        }

        return produtoModel.salvar(produtos);
    }

    @Override
    public String[] getHeaders() {
        return Produto.getHeaders();
    }
}
