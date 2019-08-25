package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.controller.arquivo.ExcelProdutoStrategy;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.Excel;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.ExcelStrategy;

public class ExportarProdutoParaExcel extends AsyncTask<Object, Integer, Boolean> {
    private Toast toast;

    ExportarProdutoParaExcel(Context context) {
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    @Override
    public Boolean doInBackground(Object... objects) {
        String diretorio = (String) objects[0];
        Object lista = objects[1];
        return new Excel(new ExcelStrategy(new ExcelProdutoStrategy())).exportar(diretorio, lista);
    }

    @Override
    public void onPreExecute() {
        toast.setText("Iniciando Exportação para Excel");
        toast.show();
    }

    @Override
    public void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            toast.setText("Produtos Exportados Com Sucesso");
            toast.show();
        }
    }
}
