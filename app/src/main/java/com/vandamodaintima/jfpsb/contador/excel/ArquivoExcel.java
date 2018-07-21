package com.vandamodaintima.jfpsb.contador.excel;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

public class ArquivoExcel {
    private XSSFWorkbook PastaTrabalho;
    private XSSFSheet Planilha;
    private FormulaEvaluator formulaEvaluator;

    private static final String app = "Contador";

    public ArquivoExcel() {
        PastaTrabalho = new XSSFWorkbook();
        Planilha = PastaTrabalho.createSheet();
        formulaEvaluator = PastaTrabalho.getCreationHelper().createFormulaEvaluator();
    }

    public ArquivoExcel(InputStream inputStream) throws IOException {
        PastaTrabalho = new XSSFWorkbook(inputStream);
        Planilha = PastaTrabalho.getSheetAt(0);
        formulaEvaluator = PastaTrabalho.getCreationHelper().createFormulaEvaluator();
    }

    public XSSFWorkbook getPastaTrabalho() {
        return PastaTrabalho;
    }

    public XSSFSheet getPlanilha() {
        return Planilha;
    }

    public FormulaEvaluator getFormulaEvaluator() {
        return formulaEvaluator;
    }
}
