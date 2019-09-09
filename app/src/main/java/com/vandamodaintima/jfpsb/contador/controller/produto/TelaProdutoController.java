package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.content.ContentResolver;
import android.net.Uri;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutBaseView;

public class TelaProdutoController {
    private ProdutoManager produtoManager;
    private TabLayoutBaseView view;
    private ConexaoBanco conexaoBanco;

    public TelaProdutoController(TabLayoutBaseView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        produtoManager = new ProdutoManager(conexaoBanco);
    }

    public void exportarProdutosParaExcel(String diretorio) {
        new ExportarProdutoParaExcel(view.getApplicationContext()).execute(diretorio, produtoManager.listar());
    }

    public void importarProdutosDeExcel(Uri uri, ContentResolver contentResolver) {
        new ImportarProdutoDeExcel(view.getApplicationContext(), conexaoBanco).execute(uri, contentResolver);
    }
}
