package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import com.vandamodaintima.jfpsb.contador.model.LojaModel;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

public class LojaStrategy implements IExportarStrategy {
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

        Cell cnpj = cabecalho.createCell(0);
        cnpj.setCellValue("CNPJ");

        Cell nome = cabecalho.createCell(1);
        nome.setCellValue("Nome");

        Cell matriz = cabecalho.createCell(2);
        matriz.setCellValue("Matriz");

        cnpj.setCellStyle(cellStyle);
        nome.setCellStyle(cellStyle);
        matriz.setCellStyle(cellStyle);

        //Estilo para restante das células
        cellStyle = workbook.createCellStyle();

        fonte = workbook.createFont();
        fonte.setFontHeightInPoints((short) 12);

        cellStyle.setFont(fonte);

        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);

        ArrayList<LojaModel> lojas = (ArrayList<LojaModel>) lista;

        Row[] rows = new Row[lojas.size()];

        for (int i = 1; i <= rows.length; i++) {
            rows[i - 1] = sheet.createRow(i);
            for (int j = 0; j < 3; j++) {
                Cell cell = rows[i - 1].createCell(j);
                cell.setCellStyle(cellStyle);
            }
        }

        for (int i = 0; i < rows.length; i++) {
            LojaModel l = lojas.get(i);

            rows[i].getCell(0).setCellValue(l.getCnpj());
            rows[i].getCell(1).setCellValue(l.getNome());
            rows[i].getCell(2).setCellValue(l.getMatriz().getNome());
        }

        sheet.setColumnWidth(0, 25 * 256);
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        //sheet.setColumnWidth(5, 40 * 256);
    }
}
