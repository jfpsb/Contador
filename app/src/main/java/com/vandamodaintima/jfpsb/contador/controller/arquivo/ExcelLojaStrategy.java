package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import com.vandamodaintima.jfpsb.contador.model.Loja;

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
 * Estratégia de exportação em Excel para Loja
 */
public class ExcelLojaStrategy implements IExcelStrategy<Loja> {

    @SafeVarargs
    @Override
    public final void criaPlanilhas(XSSFWorkbook workbook, List<Loja>... objetos) {
        CellStyle estiloCabecalho = workbook.createCellStyle();
        CellStyle estiloItens = workbook.createCellStyle();
        XSSFSheet sheet0 = workbook.createSheet("Fornecedores");
        List<Loja> listagem = objetos[0];
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
        Cell MatrizCabecalho = cabecalhoRow.createCell(2);
        Cell TelefoneCabecalho = cabecalhoRow.createCell(3);
        Cell EnderecoCabecalho = cabecalhoRow.createCell(4);
        Cell InscricaoEstadualCabecalho = cabecalhoRow.createCell(5);

        CNPJCabecalho.setCellStyle(estiloCabecalho);
        NomeCabecalho.setCellStyle(estiloCabecalho);
        MatrizCabecalho.setCellStyle(estiloCabecalho);
        EnderecoCabecalho.setCellStyle(estiloCabecalho);
        TelefoneCabecalho.setCellStyle(estiloCabecalho);
        InscricaoEstadualCabecalho.setCellStyle(estiloCabecalho);

        CNPJCabecalho.setCellValue("CNPJ");
        NomeCabecalho.setCellValue("Nome");
        MatrizCabecalho.setCellValue("Matriz");
        EnderecoCabecalho.setCellValue("Endereço");
        TelefoneCabecalho.setCellValue("Telefone");
        InscricaoEstadualCabecalho.setCellValue("Inscrição Estadual");

        for (int i = linha; i < listagem.size() + linha; i++) {
            Row row = sheet0.createRow(linha);

            Cell CNPJ = row.createCell(0);
            Cell Nome = row.createCell(1);
            Cell Matriz = row.createCell(2);
            Cell Endereco = row.createCell(3);
            Cell Telefone = row.createCell(4);
            Cell InscricaoEstadual = row.createCell(5);

            CNPJ.setCellStyle(estiloItens);
            Nome.setCellStyle(estiloItens);
            Matriz.setCellStyle(estiloItens);
            Endereco.setCellStyle(estiloItens);
            Telefone.setCellStyle(estiloItens);
            InscricaoEstadual.setCellStyle(estiloItens);

            CNPJ.setCellValue(listagem.get(i - linha).getCnpj());
            Nome.setCellValue(listagem.get(i - linha).getNome());
            Endereco.setCellValue(listagem.get(i - linha).getEndereco());
            Telefone.setCellValue(listagem.get(i - linha).getTelefone());
            InscricaoEstadual.setCellValue(listagem.get(i - linha).getInscricaoEstadual());

            if (listagem.get(i - linha).getMatriz() != null) {
                Matriz.setCellValue(listagem.get(i - linha).getMatriz().getNome());
            } else {
                Matriz.setCellValue("SEM MATRIZ");
            }
        }

        sheet0.setColumnWidth(0, 25 * 256);
        sheet0.setColumnWidth(1, 50 * 256);
        sheet0.setColumnWidth(2, 40 * 256);
        sheet0.setColumnWidth(3, 60 * 256);
        sheet0.setColumnWidth(4, 30 * 256);
        sheet0.setColumnWidth(5, 30 * 256);
    }
}
