package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Loja;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

public class ExcelLojaStrategy implements IExcelStrategy<Loja> {
    /*@Override
    public String escreveArquivoExcel(XSSFWorkbook workbook, XSSFSheet sheet, Object lista, int linhaConteudo) {
        CellStyle cellStyle = workbook.createCellStyle();

        Font fonte = workbook.createFont();
        fonte.setFontHeightInPoints((short) 12);

        cellStyle.setFont(fonte);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        ArrayList<Loja> lojas = (ArrayList<Loja>) lista;

        Row[] rows = new Row[lojas.size()];

        for (int i = linhaConteudo; i < rows.length + linhaConteudo; i++) {
            rows[i - linhaConteudo] = sheet.createRow(i);
            for (int j = 0; j < Loja.getColunas().length; j++) {
                Cell cell = rows[i - linhaConteudo].createCell(j);
                cell.setCellStyle(cellStyle);
            }
        }

        for (int i = linhaConteudo; i < rows.length + linhaConteudo; i++) {
            Loja l = lojas.get(i);

            rows[i - linhaConteudo].getCell(0).setCellValue(l.getCnpj());
            rows[i - linhaConteudo].getCell(1).setCellValue(l.getNome());
            rows[i - linhaConteudo].getCell(2).setCellValue(l.getMatriz().getNome());
            rows[i - linhaConteudo].getCell(3).setCellValue(l.getTelefone());
        }

        sheet.setColumnWidth(0, 25 * 256);
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        sheet.setColumnWidth(3, 30 * 256);

        return "Lojas.xlsx";
    }*/

    @Override
    public Boolean lerInserirDados(XSSFWorkbook workbook, XSSFSheet sheet, ConexaoBanco conexaoBanco) {
        return false;
    }

    @SafeVarargs
    @Override
    public final void criaPlanilhas(XSSFWorkbook workbook, List<Loja>... objetos) {

    }
}
