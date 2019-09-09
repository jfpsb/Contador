package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutBaseView;

public class TelaFornecedorController {
    private FornecedorManager fornecedorManager;
    private TabLayoutBaseView view;

    public TelaFornecedorController(TabLayoutBaseView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        fornecedorManager = new FornecedorManager(conexaoBanco);
    }

    public void exportarFornecedoresParaExcel(String dir) {
        new ExportarFornecedorParaExcel(view.getApplicationContext()).execute(dir, fornecedorManager.listar());
    }
}
