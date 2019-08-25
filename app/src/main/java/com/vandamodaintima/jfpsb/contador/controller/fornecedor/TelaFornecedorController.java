package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.FornecedorModel;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutBaseView;

public class TelaFornecedorController {
    private FornecedorModel fornecedorModel;
    private TabLayoutBaseView view;

    public TelaFornecedorController(TabLayoutBaseView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        fornecedorModel = new FornecedorModel(conexaoBanco);
    }

    public void exportarFornecedoresParaExcel(String dir) {
        new ExportarFornecedorParaExcel(view.getApplicationContext()).execute(dir, fornecedorModel.listar());
    }
}
