package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

public class ProdutoStrategy implements IExportarStrategy {
    @Override
    public void escreveDados(XSSFWorkbook workbook, XSSFSheet sheet, Object lista) {
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
        //sheet.setColumnWidth(5, 40 * 256);
    }
}
