package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface IExcelStrategy<T> {
    String escreveDados(XSSFWorkbook workbook, XSSFSheet sheet, Object lista, int linhaConteudo);
    Boolean lerInserirDados(XSSFWorkbook workbook, XSSFSheet sheet, ConexaoBanco conexaoBanco);
    String[] getHeaders();
    int escreveAntesCabecalho(XSSFWorkbook workbook, XSSFSheet sheet, Object lista);
}
