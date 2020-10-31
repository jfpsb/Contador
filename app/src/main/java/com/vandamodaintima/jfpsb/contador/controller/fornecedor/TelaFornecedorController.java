package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import android.content.ContentResolver;
import android.net.Uri;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.ExportarParaExcel;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.ExcelFornecedorStrategy;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutBaseView;

public class TelaFornecedorController {
    private Fornecedor fornecedorModel;
    private TabLayoutBaseView view;
    private ConexaoBanco conexaoBanco;

    public TelaFornecedorController(TabLayoutBaseView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        fornecedorModel = new Fornecedor(conexaoBanco);
    }

    public void exportarParaExcel(Uri uri) {
        new ExportarParaExcel<>(view.getApplicationContext(), new ExcelFornecedorStrategy()).execute(uri, fornecedorModel.listar());
    }

    public void importarFornecedoresDeExcel(Uri uri, ContentResolver contentResolver) {
        new ImportarFornecedorDeExcel(view.getApplicationContext(), conexaoBanco).execute(uri, contentResolver);
    }
}
