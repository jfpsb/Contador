package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.FornecedorModel;
import com.vandamodaintima.jfpsb.contador.model.MarcaModel;
import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

public class ExcelProdutoStrategy implements IExcelStrategy<ProdutoModel> {
    @Override
    public String escreveDados(XSSFWorkbook workbook, XSSFSheet sheet, Object lista) {
        //Estilo da primeira linha
        CellStyle cellStyle = workbook.createCellStyle();

        Font fonte = workbook.createFont();
        fonte.setFontName("Arial");
        fonte.setFontHeightInPoints((short) 16);
        fonte.setBold(true);

        cellStyle.setFont(fonte);
        cellStyle.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);

        Row cabecalho = sheet.createRow(0);

        Cell codBarra = cabecalho.createCell(0);
        codBarra.setCellValue("Cód. De Barra");

        Cell descricao = cabecalho.createCell(1);
        descricao.setCellValue("Descrição");

        Cell preco = cabecalho.createCell(2);
        preco.setCellValue("Preço");

        Cell fornecedor = cabecalho.createCell(3);
        fornecedor.setCellValue("Fornecedor");

        Cell marca = cabecalho.createCell(4);
        marca.setCellValue("Marca");

        codBarra.setCellStyle(cellStyle);
        descricao.setCellStyle(cellStyle);
        preco.setCellStyle(cellStyle);
        fornecedor.setCellStyle(cellStyle);
        marca.setCellStyle(cellStyle);

        //Estilo para restante das células
        cellStyle = workbook.createCellStyle();

        fonte = workbook.createFont();
        fonte.setFontHeightInPoints((short) 12);

        cellStyle.setFont(fonte);

        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);

        ArrayList<ProdutoModel> produtos = (ArrayList<ProdutoModel>) lista;

        Row[] rows = new Row[produtos.size()];

        for (int i = 1; i <= rows.length; i++) {
            rows[i - 1] = sheet.createRow(i);
            for (int j = 0; j < 5; j++) {
                Cell cell = rows[i - 1].createCell(j);
                cell.setCellStyle(cellStyle);
            }
        }

        for (int i = 0; i < rows.length; i++) {
            ProdutoModel p = produtos.get(i);

            rows[i].getCell(0).setCellValue(p.getCod_barra());
            rows[i].getCell(1).setCellValue(p.getDescricao());
            rows[i].getCell(2).setCellValue(p.getPreco());
            rows[i].getCell(3).setCellValue(p.getFornecedor() == null ? "NÃO POSSUI" : p.getFornecedor().getNome());
            rows[i].getCell(4).setCellValue(p.getMarca() == null ? "NÃO POSSUI" : p.getMarca().getNome());
        }

        sheet.setColumnWidth(0, 25 * 256);
        sheet.setColumnWidth(1, 70 * 256);
        sheet.setColumnWidth(2, 40 * 256);
        sheet.setColumnWidth(3, 40 * 256);
        sheet.setColumnWidth(4, 40 * 256);

        return "Produtos.xlsx";
    }

    @Override
    public Boolean lerInserirDados(XSSFWorkbook workbook, XSSFSheet sheet, ConexaoBanco conexaoBanco) {
        ArrayList<ProdutoModel> produtos = new ArrayList<>();
        ProdutoModel produtoModel = new ProdutoModel(conexaoBanco);
        FornecedorModel fornecedorModel = new FornecedorModel(conexaoBanco);
        MarcaModel marcaModel = new MarcaModel(conexaoBanco);

        Row cabecalho = sheet.getRow(0);
        int numCols = cabecalho.getPhysicalNumberOfCells();
        if (numCols != 5) {
            Log.e("Contador", "O Número de Colunas Está Errado");
            return false;
        }

        int rows = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i < rows; i++) {
            Row row = sheet.getRow(i);

            ProdutoModel p = new ProdutoModel(conexaoBanco);

            Cell cod_barra = row.getCell(0);
            Cell descricao = row.getCell(1);
            Cell preco = row.getCell(2);
            Cell fornecedor = row.getCell(3);
            Cell marca = row.getCell(4);

            if (cod_barra != null && cod_barra.getCellType() != Cell.CELL_TYPE_BLANK) {
                if (cod_barra.getCellType() == Cell.CELL_TYPE_STRING) {
                    p.setCod_barra(cod_barra.getStringCellValue());
                } else if (cod_barra.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    p.setCod_barra(String.valueOf(cod_barra.getNumericCellValue()));
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
                    p.setFornecedor(fornecedorModel.listarPorId(fornecedor.getNumericCellValue()));
                } else if (fornecedor.getCellType() == Cell.CELL_TYPE_STRING) {
                    p.setFornecedor(fornecedorModel.listarPorId(fornecedor.getStringCellValue()));
                } else {
                    Log.i("Contador", "Fornecedor Não Encontrado ou Vazio");
                    p.setFornecedor(null);
                }
            }

            if (marca != null && marca.getCellType() != Cell.CELL_TYPE_BLANK) {
                if (marca.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    p.setMarca(marcaModel.listarPorId(marca.getNumericCellValue()));
                } else if (fornecedor.getCellType() == Cell.CELL_TYPE_STRING) {
                    p.setMarca(marcaModel.listarPorId(marca.getStringCellValue()));
                } else {
                    Log.i("Contador", "Fornecedor Não Encontrada ou Vazia");
                    p.setMarca(null);
                }
            }

            produtos.add(p);
        }

        return produtoModel.inserir(produtos);
    }
}
