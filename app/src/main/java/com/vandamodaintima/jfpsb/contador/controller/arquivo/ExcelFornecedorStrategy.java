package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.manager.FornecedorManager;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.Locale;

public class ExcelFornecedorStrategy implements IExcelStrategy<Fornecedor> {
    @Override
    public String escreveDados(XSSFWorkbook workbook, XSSFSheet sheet, Object lista) {
        CellStyle cellStyle = workbook.createCellStyle();

        Font fonte = workbook.createFont();
        fonte.setFontHeightInPoints((short) 12);

        cellStyle.setFont(fonte);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);

        ArrayList<Fornecedor> fornecedores = (ArrayList<Fornecedor>) lista;

        Row[] rows = new Row[fornecedores.size()];

        for (int i = 1; i <= rows.length; i++) {
            rows[i - 1] = sheet.createRow(i);
            for (int j = 0; j < Fornecedor.getHeaders().length; j++) {
                Cell cell = rows[i - 1].createCell(j);
                cell.setCellStyle(cellStyle);
            }
        }

        for (int i = 0; i < rows.length; i++) {
            Fornecedor f = fornecedores.get(i);

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
        ArrayList<Fornecedor> fornecedores = new ArrayList<>();
        FornecedorManager fornecedorManager = new FornecedorManager(conexaoBanco);

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

            if (cnpj != null && cnpj.getCellType() != Cell.CELL_TYPE_BLANK) {
                if (cnpj.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    //O Excel apaga zeros iniciais de células configuradas como números então aqui eu os coloco de volta
                    String cnpjValue = String.format(Locale.ENGLISH, "%.0f", cnpj.getNumericCellValue());

                    if (cnpjValue.length() != 14) {
                        int diff = 14 - cnpjValue.length();

                        String append = "";

                        for (int j = 0; j < diff; j++) {
                            append += "0";
                        }

                        append += cnpj;
                        cnpjValue = append;
                    }
                    f.setCnpj(cnpjValue);
                } else if (cnpj.getCellType() == Cell.CELL_TYPE_STRING) {
                    f.setCnpj(cnpj.getStringCellValue());
                } else {
                    Log.i("Contador", "Campo de CNPJ está Vazio");
                }
            }

            if(nome != null && nome.getCellType() != Cell.CELL_TYPE_BLANK) {
                if (nome.getCellType() == Cell.CELL_TYPE_STRING) {
                    f.setNome(nome.getStringCellValue());
                } else {
                    Log.i("Contador", "Nome de Fornecedor Vazio");
                    continue;
                }
            }

            if(fantasia != null) {
                f.setFantasia(fantasia.getStringCellValue());
            }

            if(email != null) {
                f.setEmail(email.getStringCellValue());
            }

            fornecedores.add(f);
        }

        return fornecedorManager.salvar(fornecedores);
    }

    @Override
    public String[] getHeaders() {
        return Fornecedor.getHeaders();
    }
}