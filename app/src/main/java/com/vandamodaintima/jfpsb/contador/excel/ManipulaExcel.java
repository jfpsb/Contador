package com.vandamodaintima.jfpsb.contador.excel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.entidade.Produto;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jfpsb on 16/02/2018.
 */

public class ManipulaExcel {
    public static List<Produto> getProdutosEmPlanilha(Context context, String filename) {
        List<Produto> produtos = new ArrayList<Produto>();

        try {
            File file = new File("/data/data/com.vandamodaintima.jfpsb.contador/files/produtos2.xlsx");

            // Create a workbook using the File System
            Workbook myWorkBook = WorkbookFactory.create(file);
            Sheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells.**/
            Iterator<Row> rowIter = mySheet.rowIterator();

            while(rowIter.hasNext()){
                HSSFRow myRow = (HSSFRow) rowIter.next();

                Produto produto = new Produto();

                Iterator cellIter = myRow.cellIterator();
                while(cellIter.hasNext()){
                    HSSFCell myCell = (HSSFCell) cellIter.next();

                    Log.i("Contador", "Index Coluna: " + myCell.getColumnIndex() + " - Conteúdo: " + myCell.getStringCellValue());
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("Contador", e.getMessage());
        }

        return produtos;
    }
}
