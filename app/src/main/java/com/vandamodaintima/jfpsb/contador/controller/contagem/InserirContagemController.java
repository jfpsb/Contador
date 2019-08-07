package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ContagemModel;
import com.vandamodaintima.jfpsb.contador.model.LojaModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

import java.util.ArrayList;
import java.util.Date;

public class InserirContagemController {
    private CadastrarView view;
    private ContagemModel contagemModel;
    private LojaModel lojaModel;

    public InserirContagemController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        contagemModel = new ContagemModel(conexaoBanco);
        lojaModel = new LojaModel(conexaoBanco);
    }

    public void cadastrar() {
        contagemModel.setData(new Date());
        contagemModel.setLoja(lojaModel);
        contagemModel.setFinalizada(false);

        Boolean result = contagemModel.inserir();

        if (result) {
            view.mensagemAoUsuario("Contagem Cadastrada Com Sucesso");
            view.aposCadastro();
        } else {
            view.mensagemAoUsuario("Erro Ao Cadastrar Contagem");
        }
    }

    public void carregaLoja(Object o) {
        if (o instanceof LojaModel)
            lojaModel = (LojaModel) o;
    }

    public ArrayList<LojaModel> getLojas() {
        return lojaModel.listar();
    }
}
