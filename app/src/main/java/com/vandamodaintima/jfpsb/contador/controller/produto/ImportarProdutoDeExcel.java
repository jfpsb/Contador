package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.ExcelProdutoStrategy;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.Excel;
import com.vandamodaintima.jfpsb.contador.model.FornecedorModel;
import com.vandamodaintima.jfpsb.contador.model.MarcaModel;
import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImportarProdutoDeExcel extends AsyncTask<Object, Integer, Boolean> {
    private Context context;
    private ConexaoBanco conexaoBanco;
    private ProdutoModel produtoModel;
    private FornecedorModel fornecedorModel;
    private MarcaModel marcaModel;

    public ImportarProdutoDeExcel(Context context, ConexaoBanco conexaoBanco) {
        this.context = context;
        this.conexaoBanco = conexaoBanco;
        produtoModel = new ProdutoModel(conexaoBanco);
        fornecedorModel = new FornecedorModel(conexaoBanco);
        marcaModel = new MarcaModel(conexaoBanco);
    }

    @Override
    protected Boolean doInBackground(Object... objects) {
        Uri uri = (Uri) objects[0];
        ContentResolver contentResolver = (ContentResolver) objects[1];

        InputStream inputStream;

        try {
            inputStream = contentResolver.openInputStream(uri);
        } catch (FileNotFoundException e) {
            Log.e("Contador", e.getMessage());
            return false;
        }

        try {
            return new Excel(new ExcelProdutoStrategy(), inputStream).importar(conexaoBanco);
        } catch (IOException e) {
            Log.e("Contador", e.getMessage());
        }

        return false;
    }

    @Override
    public void onPreExecute() {
        Toast.makeText(context, "Iniciando Importação de Excel", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostExecute(Boolean aBoolean) {
        if (aBoolean)
            Toast.makeText(context, "Produtos Importados Com Sucesso", Toast.LENGTH_SHORT).show();
    }
}
