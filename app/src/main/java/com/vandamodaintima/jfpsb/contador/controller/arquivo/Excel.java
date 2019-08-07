package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import android.util.Log;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Excel {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private FormulaEvaluator formulaEvaluator;
    private IExportarStrategy exportarStrategy;

    public Excel(IExportarStrategy exportarStrategy) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet();
        formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        this.exportarStrategy = exportarStrategy;
    }

    public void exportar(String dir, Object lista) {
        OutputStream outputStream = null;

        exportarStrategy.escreveDados(workbook, sheet, lista);

        String nomeArquivo = "Produtos.xlsx";
        File arquivo = new File(dir, nomeArquivo);

        try {
            outputStream = new FileOutputStream(arquivo.getAbsolutePath());
            workbook.write(outputStream);
        } catch (FileNotFoundException fe) {
            Log.e("Contador", fe.getMessage(), fe);
        } catch (IOException ie) {
            Log.e("Contador", ie.getMessage(), ie);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void importar(String dir) {

    }
}
