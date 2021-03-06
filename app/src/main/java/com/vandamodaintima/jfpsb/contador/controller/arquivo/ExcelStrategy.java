package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelStrategy<T> {
    private IExcelStrategy excelStrategy;

    public ExcelStrategy(IExcelStrategy excelStrategy) {
        this.excelStrategy = excelStrategy;
    }

    String escreveDados(XSSFWorkbook workbook, XSSFSheet sheet, Object lista) {
        //Retorna linha onde próximo conteúdo começa
        int linha = excelStrategy.escreveAntesCabecalho(workbook, sheet, lista);

        //Estilo da primeira linha
        CellStyle cellStyle = workbook.createCellStyle();

        Font fonte = workbook.createFont();
        fonte.setFontName("Arial");
        fonte.setFontHeightInPoints((short) 16);
        fonte.setBold(true);

        cellStyle.setFont(fonte);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Row cabecalho = sheet.createRow(linha);

        for(int i = 0; i < excelStrategy.getHeaders().length; i++) {
            Cell cell = cabecalho.createCell(i);
            cell.setCellValue(excelStrategy.getHeaders()[i]);
            cell.setCellStyle(cellStyle);
        }

        return excelStrategy.escreveDados(workbook, sheet, lista, ++linha);
    }

    Boolean lerInserirDados(XSSFWorkbook workbook, XSSFSheet sheet, ConexaoBanco conexaoBanco) {
        return excelStrategy.lerInserirDados(workbook, sheet, conexaoBanco);
    }
}
