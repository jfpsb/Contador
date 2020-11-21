package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class Excel<T> {
    private XSSFWorkbook workbook;
    private FormulaEvaluator formulaEvaluator;
    private ContentResolver contentResolver;
    private IExcelStrategy<T> excelStrategy;

    public Excel(ContentResolver contentResolver, IExcelStrategy<T> excelStrategy) {
        workbook = new XSSFWorkbook();
        this.excelStrategy = excelStrategy;
        formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        this.contentResolver = contentResolver;
    }

    public Excel(InputStream inputStream, IExcelStrategy<T> excelStrategy) throws IOException {
        workbook = new XSSFWorkbook(inputStream);
        this.excelStrategy = excelStrategy;
        formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
    }

    @SafeVarargs
    public final Boolean exportar(Uri uri, List<T>... objetos) {
        OutputStream outputStream = null;

        excelStrategy.criaPlanilhas(workbook, objetos);

        try {
            outputStream = contentResolver.openOutputStream(uri);
            workbook.write(outputStream);
            return true;
        } catch (IOException fe) {
            Log.e("Contador", fe.getMessage(), fe);
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

        return false;
    }

    /*public Boolean importar(ConexaoBanco conexaoBanco) {
        return excelStrategy.lerInserirDados(workbook, sheet, conexaoBanco);
    }*/
}
