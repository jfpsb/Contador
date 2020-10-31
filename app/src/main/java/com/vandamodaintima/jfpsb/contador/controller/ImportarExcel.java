package com.vandamodaintima.jfpsb.contador.controller;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.Excel;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.IExcelStrategy;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class ImportarExcel<T> extends AsyncTask<Object, Integer, Boolean> {
    private WeakReference<Context> context;
    private ConexaoBanco conexaoBanco;
    private IExcelStrategy<T> excelStrategy;

    public ImportarExcel(Context context, IExcelStrategy<T> excelStrategy, ConexaoBanco conexaoBanco) {
        this.context = new WeakReference<>(context);
        this.excelStrategy = excelStrategy;
        this.conexaoBanco = conexaoBanco;
    }

    @Override
    protected Boolean doInBackground(Object... objects) {
        /*Uri uri = (Uri) objects[0];
        ContentResolver contentResolver = (ContentResolver) objects[1];

        InputStream inputStream;

        try {
            inputStream = contentResolver.openInputStream(uri);
            return new Excel(inputStream, excelStrategy).importar(conexaoBanco);
        } catch (IOException e) {
            Log.e(ActivityBaseView.LOG, e.getMessage());
        }*/

        return false;
    }

    @Override
    public void onPreExecute() {
        Toast.makeText(context.get(), "Iniciando Importação de Excel", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostExecute(Boolean aBoolean) {
        if (aBoolean)
            Toast.makeText(context.get(), "Importação Executada Com Sucesso", Toast.LENGTH_SHORT).show();
    }
}
