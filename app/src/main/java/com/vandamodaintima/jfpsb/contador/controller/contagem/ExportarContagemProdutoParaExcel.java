package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.controller.arquivo.Excel;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.ExcelContagemProdutoStrategy;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.ExcelStrategy;

public class ExportarContagemProdutoParaExcel extends AsyncTask<Object, Integer, Boolean> {
    private Toast toast;

    ExportarContagemProdutoParaExcel(Context context) {
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    @Override
    public Boolean doInBackground(Object... objects) {
        String diretorio = (String) objects[0];
        Object lista = objects[1];
        return new Excel(new ExcelStrategy(new ExcelContagemProdutoStrategy())).exportar(diretorio, lista);
    }

    @Override
    public void onPreExecute() {
        toast.setText("Iniciando Exportação para Excel");
        toast.show();
    }

    @Override
    public void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            toast.setText("Contagem de Produtos Exportada Com Sucesso");
            toast.show();
        }
    }
}
