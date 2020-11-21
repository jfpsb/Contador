package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;

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
import java.util.Locale;

public class ExcelFornecedorStrategy implements IExcelStrategy<Fornecedor> {

    @Override
    public Boolean lerInserirDados(XSSFWorkbook workbook, XSSFSheet sheet, ConexaoBanco conexaoBanco) {
        ArrayList<Fornecedor> fornecedores = new ArrayList<>();
        Fornecedor fornecedorManager = new Fornecedor(conexaoBanco);

        Row cabecalho = sheet.getRow(0);
        int numCols = cabecalho.getPhysicalNumberOfCells();
        if (numCols != Fornecedor.getHeaders().length) {
            Log.e("Contador", "O Número de Colunas Está Errado");
            return false;
        }

        int rows = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i < rows; i++) {
            Row row = sheet.getRow(i);

            Fornecedor f = new Fornecedor();

            Cell cnpj = row.getCell(0);
            Cell nome = row.getCell(1);
            Cell fantasia = row.getCell(2);
            Cell email = row.getCell(3);

            if (cnpj != null && cnpj.getCellTypeEnum() != CellType.BLANK) {
                if (cnpj.getCellTypeEnum() == CellType.NUMERIC) {
                    //O Excel apaga zeros iniciais de células configuradas como números então aqui eu os coloco de volta
                    String cnpjValue = String.format(Locale.ENGLISH, "%.0f", cnpj.getNumericCellValue());

                    if (cnpjValue.length() != 14) {
                        int diff = 14 - cnpjValue.length();

                        StringBuilder append = new StringBuilder();

                        for (int j = 0; j < diff; j++) {
                            append.append("0");
                        }

                        append.append(cnpj);
                        cnpjValue = append.toString();
                    }
                    f.setCnpj(cnpjValue);
                } else if (cnpj.getCellTypeEnum() == CellType.STRING) {
                    f.setCnpj(cnpj.getStringCellValue());
                } else {
                    Log.i("Contador", "Campo de CNPJ está Vazio");
                }
            }

            if (nome != null && nome.getCellTypeEnum() != CellType.BLANK) {
                if (nome.getCellTypeEnum() == CellType.STRING) {
                    f.setNome(nome.getStringCellValue());
                } else {
                    Log.i("Contador", "Nome de Fornecedor Vazio");
                    continue;
                }
            }

            if (fantasia != null) {
                f.setFantasia(fantasia.getStringCellValue());
            }

            if (email != null) {
                f.setEmail(email.getStringCellValue());
            }

            fornecedores.add(f);
        }

        return fornecedorManager.salvar(fornecedores);
    }

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
