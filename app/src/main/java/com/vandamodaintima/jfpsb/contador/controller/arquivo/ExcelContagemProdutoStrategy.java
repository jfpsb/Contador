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
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

public class ExcelContagemProdutoStrategy implements IExcelStrategy<ContagemProduto> {

    @SafeVarargs
    @Override
    public final void criaPlanilhas(XSSFWorkbook workbook, List<ContagemProduto>... objetos) {
        CellStyle estiloCabecalho = workbook.createCellStyle();
        CellStyle estiloItens = workbook.createCellStyle();
        XSSFSheet sheet0 = workbook.createSheet("Contagem Por Produto");
        XSSFSheet sheet1 = workbook.createSheet("Contagem Por Grade");
        List<ContagemProduto> listagemPorProduto = objetos[0];
        List<ContagemProduto> listagemPorGrade = objetos[1];
        int linha = 0;

        Font fonteCabecalho = workbook.createFont();
        fonteCabecalho.setFontName("Arial");
        fonteCabecalho.setFontHeightInPoints((short) 16);
        fonteCabecalho.setBold(true);
        estiloCabecalho.setFont(fonteCabecalho);
        estiloCabecalho.setAlignment(HorizontalAlignment.CENTER);
        estiloCabecalho.setVerticalAlignment(VerticalAlignment.CENTER);

        Font fonteItens = workbook.createFont();
        fonteItens.setFontName("Arial");
        fonteItens.setFontHeightInPoints((short) 12);
        estiloItens.setFont(fonteItens);
        estiloItens.setAlignment(HorizontalAlignment.CENTER);
        estiloItens.setVerticalAlignment(VerticalAlignment.CENTER);

        Row cabecalhoSheet0 = sheet0.createRow(linha++);
        Cell CodProdutoCabecalhoSheet0 = cabecalhoSheet0.createCell(0);
        Cell DescricaoCabecalhoSheet0 = cabecalhoSheet0.createCell(1);
        Cell QuantidadeCabecalhoSheet0 = cabecalhoSheet0.createCell(2);

        CodProdutoCabecalhoSheet0.setCellStyle(estiloCabecalho);
        DescricaoCabecalhoSheet0.setCellStyle(estiloCabecalho);
        QuantidadeCabecalhoSheet0.setCellStyle(estiloCabecalho);

        CodProdutoCabecalhoSheet0.setCellValue("Código de Produto");
        DescricaoCabecalhoSheet0.setCellValue("Descrição");
        QuantidadeCabecalhoSheet0.setCellValue("Quantidade");

        for (int i = linha; i < listagemPorProduto.size() + linha; i++) {
            Row row = sheet0.createRow(i);
            Cell CodProdutoCellSheet0 = row.createCell(0);
            Cell DescricaoCellSheet0 = row.createCell(1);
            Cell QuantidadeCellSheet0 = row.createCell(2);

            CodProdutoCellSheet0.setCellStyle(estiloItens);
            DescricaoCellSheet0.setCellStyle(estiloItens);
            QuantidadeCellSheet0.setCellStyle(estiloItens);

            CodProdutoCellSheet0.setCellValue(listagemPorProduto.get(i - linha).getProduto().getCodBarra());
            DescricaoCellSheet0.setCellValue(listagemPorProduto.get(i - linha).getProduto().getDescricao());
            QuantidadeCellSheet0.setCellValue(listagemPorProduto.get(i - linha).getQuant());
        }

        sheet0.setColumnWidth(0, 25 * 256);
        sheet0.setColumnWidth(1, 60 * 256);
        sheet0.setColumnWidth(2, 25 * 256);

        linha = 0;

        Row cabecalhoSheet1 = sheet1.createRow(linha++);
        Cell CodProdutoCabecalhoSheet1 = cabecalhoSheet1.createCell(0);
        Cell DescricaoCabecalhoSheet1 = cabecalhoSheet1.createCell(1);
        Cell DescricaoGradeCabecalhoSheet1 = cabecalhoSheet1.createCell(2);
        Cell CodBarraCabecalhoSheet1 = cabecalhoSheet1.createCell(3);
        Cell QuantidadeCabecalhoSheet1 = cabecalhoSheet1.createCell(4);

        CodProdutoCabecalhoSheet1.setCellStyle(estiloCabecalho);
        DescricaoCabecalhoSheet1.setCellStyle(estiloCabecalho);
        DescricaoGradeCabecalhoSheet1.setCellStyle(estiloCabecalho);
        CodBarraCabecalhoSheet1.setCellStyle(estiloCabecalho);
        QuantidadeCabecalhoSheet1.setCellStyle(estiloCabecalho);

        CodProdutoCabecalhoSheet1.setCellValue("Código de Produto");
        DescricaoCabecalhoSheet1.setCellValue("Descrição");
        DescricaoGradeCabecalhoSheet1.setCellValue("Descrição da Grade");
        CodBarraCabecalhoSheet1.setCellValue("Cód. de Barras");
        QuantidadeCabecalhoSheet1.setCellValue("Quantidade");

        for (int i = linha; i < listagemPorGrade.size() + linha; i++) {
            Row row = sheet1.createRow(i);

            Cell CodProdutoCellSheet1 = row.createCell(0);
            Cell DescricaoCellSheet1 = row.createCell(1);
            Cell DescricaoGradeCellSheet1 = row.createCell(2);
            Cell CodBarraCellSheet1 = row.createCell(3);
            Cell QuantidadeCellSheet1 = row.createCell(4);

            CodProdutoCellSheet1.setCellStyle(estiloItens);
            DescricaoCellSheet1.setCellStyle(estiloItens);
            DescricaoGradeCellSheet1.setCellStyle(estiloItens);
            CodBarraCellSheet1.setCellStyle(estiloItens);
            QuantidadeCellSheet1.setCellStyle(estiloItens);

            CodProdutoCellSheet1.setCellValue(listagemPorGrade.get(i - linha).getProduto().getCodBarra());
            DescricaoCellSheet1.setCellValue(listagemPorGrade.get(i - linha).getProduto().getDescricao());

            if (listagemPorGrade.get(i - linha).getProdutoGrade() != null) {
                DescricaoGradeCellSheet1.setCellValue(listagemPorGrade.get(i - linha).getProdutoGrade().getGradesToString());
                CodBarraCellSheet1.setCellValue(listagemPorGrade.get(i - linha).getProdutoGrade().getCodBarra());
            } else {
                DescricaoGradeCellSheet1.setCellValue("INSERIDO SEM GRADE");
                CodBarraCellSheet1.setCellValue("INSERIDO SEM GRADE");
            }

            QuantidadeCellSheet1.setCellValue(listagemPorGrade.get(i - linha).getQuant());
        }

        sheet1.setColumnWidth(0, 25 * 256);
        sheet1.setColumnWidth(1, 60 * 256);
        sheet1.setColumnWidth(2, 30 * 256);
        sheet1.setColumnWidth(3, 25 * 256);
        sheet1.setColumnWidth(4, 25 * 256);
    }
}
