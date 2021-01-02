package com.vandamodaintima.jfpsb.contador.controller.arquivo;

import com.vandamodaintima.jfpsb.contador.model.Produto;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

/**
 * Estratégia de exportação em Excel para Produto
 */
public class ExcelProdutoStrategy implements IExcelStrategy<Produto> {

    @SafeVarargs
    @Override
    public final void criaPlanilhas(XSSFWorkbook workbook, List<Produto>... objetos) {

    }
}
