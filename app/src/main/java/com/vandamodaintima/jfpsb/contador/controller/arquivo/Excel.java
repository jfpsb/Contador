package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Excel {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private FormulaEvaluator formulaEvaluator;
    private ExcelStrategy excelStrategy;
    private ContentResolver contentResolver;

    public Excel(ContentResolver contentResolver, ExcelStrategy excelStrategy) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet();
        formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        this.excelStrategy = excelStrategy;
        this.contentResolver = contentResolver;
    }

    public Excel(ExcelStrategy excelStrategy, InputStream inputStream) throws IOException {
        workbook = new XSSFWorkbook(inputStream);
        sheet = workbook.getSheetAt(0);
        formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        this.excelStrategy = excelStrategy;
    }

    public Boolean exportar(Uri uri, Object lista) {
        OutputStream outputStream = null;

        excelStrategy.escreveDados(workbook, sheet, lista);

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

    public Boolean importar(ConexaoBanco conexaoBanco) {
        return excelStrategy.lerInserirDados(workbook, sheet, conexaoBanco);
    }
}
