package com.vandamodaintima.jfpsb.contador.controller;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.ExcelProdutoStrategy;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.Excel;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.ExcelStrategy;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class ImportarExcel<T> extends AsyncTask<Object, Integer, Boolean> {
    private WeakReference<Context> context;
    private ConexaoBanco conexaoBanco;
    private ExcelStrategy<T> excelStrategy;

    public ImportarExcel(Context context, ExcelStrategy<T> excelStrategy, ConexaoBanco conexaoBanco) {
        this.context = new WeakReference<>(context);
        this.excelStrategy = excelStrategy;
        this.conexaoBanco = conexaoBanco;
    }

    @Override
    protected Boolean doInBackground(Object... objects) {
        Uri uri = (Uri) objects[0];
        ContentResolver contentResolver = (ContentResolver) objects[1];

        InputStream inputStream;

        try {
            inputStream = contentResolver.openInputStream(uri);
            return new Excel(excelStrategy, inputStream).importar(conexaoBanco);
        } catch (IOException e) {
            Log.e(ActivityBaseView.LOG, e.getMessage());
        }

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
