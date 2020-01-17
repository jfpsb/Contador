package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import android.content.ContentResolver;
import android.net.Uri;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutBaseView;

public class TelaFornecedorController {
    private FornecedorManager fornecedorManager;
    private TabLayoutBaseView view;
    private ConexaoBanco conexaoBanco;

    public TelaFornecedorController(TabLayoutBaseView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        fornecedorManager = new FornecedorManager(conexaoBanco);
    }

    public void exportarFornecedoresParaExcel(String dir) {
        new ExportarFornecedorParaExcel(view.getApplicationContext()).execute(dir, fornecedorManager.listar());
    }

    public void importarFornecedoresDeExcel(Uri uri, ContentResolver contentResolver) {
        new ImportarFornecedorDeExcel(view.getApplicationContext(), conexaoBanco).execute(uri, contentResolver);
    }
}
