package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

public class ExcelContagemProdutoStrategy implements IExcelStrategy<ContagemProduto> {
    @Override
    public String escreveDados(XSSFWorkbook workbook, XSSFSheet sheet, Object lista, int linhaConteudo) {
        CellStyle estiloCelula = workbook.createCellStyle();

        Font fonte = workbook.createFont();
        fonte.setFontHeightInPoints((short) 12);

        estiloCelula.setFont(fonte);
        estiloCelula.setAlignment(HorizontalAlignment.CENTER);
        estiloCelula.setVerticalAlignment(VerticalAlignment.CENTER);

        ArrayList<ContagemProduto> contagensProduto = (ArrayList<ContagemProduto>) lista;

        Row[] dataRows = new Row[contagensProduto.size()];

        CellStyle estiloCelulaMonetario = workbook.createCellStyle();
        estiloCelulaMonetario.cloneStyleFrom(estiloCelula);
        estiloCelulaMonetario.setDataFormat((short) 8);

        //Configurando os tipos de célula
        for (int i = linhaConteudo; i < dataRows.length + linhaConteudo; i++) {
            dataRows[i - linhaConteudo] = sheet.createRow(i);
            for (int j = 0; j < ContagemProduto.getHeaders().length; j++) {
                Cell cell = dataRows[i - linhaConteudo].createCell(j);
                if (j == 2) {
                    cell.setCellStyle(estiloCelulaMonetario);
                } else {
                    cell.setCellStyle(estiloCelula);
                }
            }
        }

        //Configurando coluna de valor total
        for (int i = linhaConteudo; i < dataRows.length + linhaConteudo; i++) {
            Cell cell = dataRows[i - linhaConteudo].createCell(4);
            cell.setCellStyle(estiloCelulaMonetario);
            cell.setCellType(CellType.FORMULA);
            String output = String.format("%s*%s", "C" + (i + 1), "D" + (i + 1));
            cell.setCellFormula(output);
        }

        linhaConteudo++;

        //Colocando valores em células
        for (int i = linhaConteudo; i < dataRows.length + linhaConteudo; i++) {
            ContagemProduto contagemProduto = contagensProduto.get(i - linhaConteudo);

            dataRows[i - linhaConteudo].getCell(0).setCellValue(contagemProduto.getProduto().getCod_barra());
            dataRows[i - linhaConteudo].getCell(1).setCellValue(contagemProduto.getProduto().getDescricao());
            dataRows[i - linhaConteudo].getCell(2).setCellValue(contagemProduto.getProduto().getPreco());
            dataRows[i - linhaConteudo].getCell(3).setCellValue(contagemProduto.getQuant());
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

    @Override
    public int escreveAntesCabecalho(XSSFWorkbook workbook, XSSFSheet sheet, Object lista) {
        CellStyle cellStyle = workbook.createCellStyle();
        int linha = 0;
        ArrayList<ContagemProduto> contagensProduto = (ArrayList<ContagemProduto>) lista;

        Font fonte = workbook.createFont();
        fonte.setFontName("Arial");
        fonte.setFontHeightInPoints((short) 16);
        fonte.setBold(true);

        cellStyle.setFont(fonte);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Row cabecalho = sheet.createRow(linha++);
        Cell cellLoja = cabecalho.createCell(0);
        cellLoja.setCellStyle(cellStyle);
        cellLoja.setCellValue("Loja: " + contagensProduto.get(0).getContagem().getLoja().getNome());

        return linha;
    }
}
