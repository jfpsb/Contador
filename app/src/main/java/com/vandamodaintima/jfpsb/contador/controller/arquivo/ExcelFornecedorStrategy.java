package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.FornecedorModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

public class ExcelFornecedorStrategy implements IExcelStrategy<FornecedorModel> {
    @Override
    public String escreveDados(XSSFWorkbook workbook, XSSFSheet sheet, Object lista) {
        CellStyle cellStyle = workbook.createCellStyle();

        Font fonte = workbook.createFont();
        fonte.setFontHeightInPoints((short) 12);

        cellStyle.setFont(fonte);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);

        ArrayList<FornecedorModel> fornecedores = (ArrayList<FornecedorModel>) lista;

        Row[] rows = new Row[fornecedores.size()];

        for (int i = 1; i <= rows.length; i++) {
            rows[i - 1] = sheet.createRow(i);
            for (int j = 0; j < FornecedorModel.getHeaders().length; j++) {
                Cell cell = rows[i - 1].createCell(j);
                cell.setCellStyle(cellStyle);
            }
        }

        for (int i = 0; i < rows.length; i++) {
            FornecedorModel f = fornecedores.get(i);

            rows[i].getCell(0).setCellValue(f.getCnpj());
            rows[i].getCell(1).setCellValue(f.getNome());
            rows[i].getCell(2).setCellValue(f.getFantasia());
            rows[i].getCell(3).setCellValue(f.getEmail());
        }

        sheet.setColumnWidth(0, 30 * 256);
        sheet.setColumnWidth(1, 60 * 256);
        sheet.setColumnWidth(2, 60 * 256);
        sheet.setColumnWidth(3, 40 * 256);

        return "Fornecedores.xlsx";
    }

    @Override
    public Boolean lerInserirDados(XSSFWorkbook workbook, XSSFSheet sheet, ConexaoBanco conexaoBanco) {
        return null;
    }

    @Override
    public String[] getHeaders() {
        return FornecedorModel.getHeaders();
    }
}
