package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.content.ContentResolver;
import android.net.Uri;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.ExportarParaExcel;
import com.vandamodaintima.jfpsb.contador.controller.ImportarExcel;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.ExcelProdutoStrategy;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.ExcelStrategy;
import com.vandamodaintima.jfpsb.contador.model.Produto;
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

    public void exportarParaExcel(Uri uri) {
        new ExportarParaExcel<Produto>(view.getApplicationContext(), new ExcelStrategy<>(new ExcelProdutoStrategy())).execute(uri, produtoManager.listar());
    }

    public void importarDeExcel(Uri uri, ContentResolver contentResolver) {
        new ImportarExcel<Produto>(view.getApplicationContext(), new ExcelStrategy<>(new ExcelProdutoStrategy()), conexaoBanco).execute(uri, contentResolver);
    }
}
