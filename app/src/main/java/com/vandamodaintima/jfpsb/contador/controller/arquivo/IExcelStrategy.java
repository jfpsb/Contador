package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public interface IExcelStrategy<T> {
    void criaPlanilhas(XSSFWorkbook workbook, List<T>... objetos);
}
