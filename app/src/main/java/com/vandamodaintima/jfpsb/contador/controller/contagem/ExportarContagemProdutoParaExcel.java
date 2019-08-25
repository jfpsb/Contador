package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.controller.arquivo.Excel;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.ExcelContagemProdutoStrategy;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.ExcelStrategy;

public class ExportarContagemProdutoParaExcel extends AsyncTask<Object, Integer, Boolean> {
    private Context context;

    public ExportarContagemProdutoParaExcel(Context context) {
        this.context = context;
    }

    @Override
    public Boolean doInBackground(Object... objects) {
        String diretorio = (String) objects[0];
        Object lista = objects[1];
        return new Excel(new ExcelStrategy(new ExcelContagemProdutoStrategy())).exportar(diretorio, lista);
    }

    @Override
    public void onPreExecute() {
        Toast.makeText(context, "Iniciando Exportação para Excel", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostExecute(Boolean aBoolean) {
        if (aBoolean)
            Toast.makeText(context, "Contagem de Produtos Exportados Com Sucesso", Toast.LENGTH_SHORT).show();
    }
}
