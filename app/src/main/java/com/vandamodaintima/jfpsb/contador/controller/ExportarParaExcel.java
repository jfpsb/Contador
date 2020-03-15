package com.vandamodaintima.jfpsb.contador.controller;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.controller.arquivo.Excel;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.ExcelProdutoStrategy;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.ExcelStrategy;

import java.lang.ref.WeakReference;

public class ExportarParaExcel <T> extends AsyncTask<Object, Integer, Boolean> {
    private Toast toast;
    private ExcelStrategy<T> excelStrategy;
    private WeakReference<Context> context;

    public ExportarParaExcel(Context context, ExcelStrategy<T> excelStrategy) {
        this.context = new WeakReference<>(context);
        this.excelStrategy = excelStrategy;
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    @Override
    protected Boolean doInBackground(Object... objects) {
        Uri uri = (Uri) objects[0];
        Object lista = objects[1];
        return new Excel(context.get().getContentResolver(), excelStrategy).exportar(uri, lista);
    }

    @Override
    public void onPreExecute() {
        toast.setText("Iniciando Exportação Para Excel");
        toast.show();
    }

    @Override
    public void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            toast.setText("Exportação Realizada Com Sucesso");
            toast.show();
        }
    }
}
