package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ContagemProdutoModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

public class ExcelContagemProdutoStrategy implements IExcelStrategy<ContagemProdutoModel> {
    @Override
    public String escreveDados(XSSFWorkbook workbook, XSSFSheet sheet, Object lista) {
        CellStyle cellStyle = workbook.createCellStyle();

        Font fonte = workbook.createFont();
        fonte.setFontHeightInPoints((short) 12);

        cellStyle.setFont(fonte);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);

        ArrayList<ContagemProdutoModel> contagensProduto = (ArrayList<ContagemProdutoModel>) lista;

        Row[] rows = new Row[contagensProduto.size()];

        for (int i = 1; i <= rows.length; i++) {
            rows[i - 1] = sheet.createRow(i);
            for (int j = 0; j < ContagemProdutoModel.getHeaders().length; j++) {
                Cell cell = rows[i - 1].createCell(j);
                cell.setCellStyle(cellStyle);
            }
        }

        for (int i = 0; i < rows.length; i++) {
            ContagemProdutoModel contagemProdutoModel = contagensProduto.get(i);

            rows[i].getCell(0).setCellValue(contagemProdutoModel.getProduto().getCod_barra());
            rows[i].getCell(1).setCellValue(contagemProdutoModel.getProduto().getDescricao());
            rows[i].getCell(2).setCellValue(contagemProdutoModel.getQuant());
        }

        sheet.setColumnWidth(0, 25 * 256);
        sheet.setColumnWidth(1, 70 * 256);
        sheet.setColumnWidth(2, 20 * 256);

        return "Contagem.xlsx";
    }

    @Override
    public Boolean lerInserirDados(XSSFWorkbook workbook, XSSFSheet sheet, ConexaoBanco conexaoBanco) {
        return null;
    }

    @Override
    public String[] getHeaders() {
        return ContagemProdutoModel.getHeaders();
    }
}
