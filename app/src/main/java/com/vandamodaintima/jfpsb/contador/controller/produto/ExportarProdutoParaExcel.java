package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.controller.arquivo.ExcelProdutoStrategy;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.Excel;

public class ExportarProdutoParaExcel extends AsyncTask<Object, Integer, Boolean> {
    private Context context;

    public ExportarProdutoParaExcel(Context context) {
        this.context = context;
    }

    @Override
    public Boolean doInBackground(Object... objects) {
        String diretorio = (String) objects[0];
        Object lista = objects[1];
        return new Excel(new ExcelProdutoStrategy()).exportar(diretorio, lista);
    }

    @Override
    public void onPreExecute() {
        Toast.makeText(context, "Iniciando Exportação para Excel", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostExecute(Boolean aBoolean) {
        if (aBoolean)
            Toast.makeText(context, "Produtos Exportados Com Sucesso", Toast.LENGTH_SHORT).show();
    }
}
