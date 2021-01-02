package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import com.vandamodaintima.jfpsb.contador.model.Fornecedor;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

/**
 * Estratégia de exportação em Excel para Fornecedor
 */
public class ExcelFornecedorStrategy implements IExcelStrategy<Fornecedor> {

    @Override
    public void criaPlanilhas(XSSFWorkbook workbook, List<Fornecedor>... objetos) {
        CellStyle estiloCabecalho = workbook.createCellStyle();
        CellStyle estiloItens = workbook.createCellStyle();
        XSSFSheet sheet0 = workbook.createSheet("Fornecedores");
        List<Fornecedor> listagem = objetos[0];
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

        Row cabecalhoRow = sheet0.createRow(linha++);

        Cell CNPJCabecalho = cabecalhoRow.createCell(0);
        Cell NomeCabecalho = cabecalhoRow.createCell(1);
        Cell NomeFantasiaCabecalho = cabecalhoRow.createCell(2);
        Cell EmailCabecalho = cabecalhoRow.createCell(3);
        Cell TelefoneCabecalho = cabecalhoRow.createCell(4);

        CNPJCabecalho.setCellStyle(estiloCabecalho);
        NomeCabecalho.setCellStyle(estiloCabecalho);
        NomeFantasiaCabecalho.setCellStyle(estiloCabecalho);
        EmailCabecalho.setCellStyle(estiloCabecalho);
        TelefoneCabecalho.setCellStyle(estiloCabecalho);

        CNPJCabecalho.setCellValue("CNPJ");
        NomeCabecalho.setCellValue("Nome");
        NomeFantasiaCabecalho.setCellValue("Nome Fantasia");
        EmailCabecalho.setCellValue("E-mail");
        TelefoneCabecalho.setCellValue("Telefone");

        for (int i = linha; i < listagem.size() + linha; i++) {
            Row row = sheet0.createRow(linha);

            Cell CNPJ = row.createCell(0);
            Cell Nome = row.createCell(1);
            Cell NomeFantasia = row.createCell(2);
            Cell Email = row.createCell(3);
            Cell Telefone = row.createCell(4);

            CNPJ.setCellStyle(estiloItens);
            Nome.setCellStyle(estiloItens);
            NomeFantasia.setCellStyle(estiloItens);
            Email.setCellStyle(estiloItens);
            Telefone.setCellStyle(estiloItens);

            CNPJ.setCellValue(listagem.get(i - linha).getCnpj());
            Nome.setCellValue(listagem.get(i - linha).getNome());
            NomeFantasia.setCellValue(listagem.get(i - linha).getFantasia());
            Email.setCellValue(listagem.get(i - linha).getEmail());
            Telefone.setCellValue(listagem.get(i - linha).getTelefone());
        }

        sheet0.setColumnWidth(0, 30 * 256);
        sheet0.setColumnWidth(1, 60 * 256);
        sheet0.setColumnWidth(2, 60 * 256);
        sheet0.setColumnWidth(3, 30 * 256);
        sheet0.setColumnWidth(4, 30 * 256);
    }
}
