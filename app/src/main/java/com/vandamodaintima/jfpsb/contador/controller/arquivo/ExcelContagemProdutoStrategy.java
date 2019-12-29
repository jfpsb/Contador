package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

public class ExcelContagemProdutoStrategy implements IExcelStrategy<ContagemProduto> {
    @Override
    public String escreveDados(XSSFWorkbook workbook, XSSFSheet sheet, Object lista) {
        CellStyle estiloCelula = workbook.createCellStyle();

        Font fonte = workbook.createFont();
        fonte.setFontHeightInPoints((short) 12);

        estiloCelula.setFont(fonte);
        estiloCelula.setAlignment(CellStyle.ALIGN_CENTER);
        estiloCelula.setVerticalAlignment(CellStyle.ALIGN_CENTER);

        ArrayList<ContagemProduto> contagensProduto = (ArrayList<ContagemProduto>) lista;

        Row[] rows = new Row[contagensProduto.size()];

        CellStyle estiloCelulaMonetario = workbook.createCellStyle();
        estiloCelulaMonetario.cloneStyleFrom(estiloCelula);
        estiloCelulaMonetario.setDataFormat((short) 8);

        for (int i = 1; i <= rows.length; i++) {
            rows[i - 1] = sheet.createRow(i);
            for (int j = 0; j < ContagemProduto.getHeaders().length; j++) {
                Cell cell = rows[i - 1].createCell(j);
                if (j == 2) {
                    cell.setCellStyle(estiloCelulaMonetario);
                } else {
                    cell.setCellStyle(estiloCelula);
                }
            }
        }

        for (int i = 1; i <= rows.length; i++) {
            Cell cell = rows[i - 1].createCell(4);
            cell.setCellStyle(estiloCelulaMonetario);
            cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
            String output = String.format("%s*%s", "C" + (i + 1), "D" + (i + 1));
            cell.setCellFormula(output);
        }

        for (int i = 0; i < rows.length; i++) {
            ContagemProduto contagemProduto = contagensProduto.get(i);

            rows[i].getCell(0).setCellValue(contagemProduto.getProduto().getCod_barra());
            rows[i].getCell(1).setCellValue(contagemProduto.getProduto().getDescricao());
            rows[i].getCell(2).setCellValue(contagemProduto.getProduto().getPreco());
            rows[i].getCell(3).setCellValue(contagemProduto.getQuant());
        }

        sheet.setColumnWidth(0, 25 * 256);
        sheet.setColumnWidth(1, 70 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 30 * 256);

        return "Contagem.xlsx";
    }

    @Override
    public Boolean lerInserirDados(XSSFWorkbook workbook, XSSFSheet sheet, ConexaoBanco conexaoBanco) {
        return null;
    }

    @Override
    public String[] getHeaders() {
        return ContagemProduto.getHeaders();
    }
}
