package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.LojaModel;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

public class ExcelLojaStrategy implements IExcelStrategy<LojaModel> {
    @Override
    public String escreveDados(XSSFWorkbook workbook, XSSFSheet sheet, Object lista) {
        CellStyle cellStyle = workbook.createCellStyle();

        Font fonte = workbook.createFont();
        fonte.setFontHeightInPoints((short) 12);

        cellStyle.setFont(fonte);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);

        ArrayList<LojaModel> lojas = (ArrayList<LojaModel>) lista;

        Row[] rows = new Row[lojas.size()];

        for (int i = 1; i <= rows.length; i++) {
            rows[i - 1] = sheet.createRow(i);
            for (int j = 0; j < LojaModel.getColunas().length; j++) {
                Cell cell = rows[i - 1].createCell(j);
                cell.setCellStyle(cellStyle);
            }
        }

        for (int i = 0; i < rows.length; i++) {
            LojaModel l = lojas.get(i);

            rows[i].getCell(0).setCellValue(l.getCnpj());
            rows[i].getCell(1).setCellValue(l.getNome());
            rows[i].getCell(2).setCellValue(l.getMatriz().getNome());
            rows[i].getCell(3).setCellValue(l.getTelefone());
        }

        sheet.setColumnWidth(0, 25 * 256);
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        sheet.setColumnWidth(3, 30 * 256);

        return "Lojas.xlsx";
    }

    @Override
    public Boolean lerInserirDados(XSSFWorkbook workbook, XSSFSheet sheet, ConexaoBanco conexaoBanco) {
        return false;
    }

    @Override
    public String[] getHeaders() {
        return LojaModel.getHeaders();
    }
}
