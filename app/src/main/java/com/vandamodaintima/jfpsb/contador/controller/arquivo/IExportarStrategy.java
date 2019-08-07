package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import android.database.Cursor;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;

public interface IExportarStrategy {
    void escreveDados(XSSFWorkbook workbook, XSSFSheet sheet, Object lista);
}
