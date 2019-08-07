package com.vandamodaintima.jfpsb.contador.controller.loja;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.LojaModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarLojaController {
    private AlterarDeletarView view;
    private ConexaoBanco conexaoBanco;
    private LojaModel lojaModel;

    public AlterarDeletarLojaController(AlterarDeletarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        lojaModel = new LojaModel(conexaoBanco);
    }

    public void atualizar(String nome) {
        if(nome.trim().isEmpty()) {
            view.mensagemAoUsuario("Nome Não Pode Ser Vazio");
            return;
        }

        lojaModel.setNome(nome);

        Boolean result = lojaModel.atualizar();

        if(result) {
            view.mensagemAoUsuario("Loja Deletada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Loja");
        }
    }

    public void deletar() {
        Boolean result = lojaModel.deletar();

        if(result) {
            view.mensagemAoUsuario("Loja Deletada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Loja");
        }
    }

    public void carregaLoja(String id) {
        lojaModel.load(id);
    }

    public String getCnpj() {
        return lojaModel.getCnpj();
    }

    public String getNome() {
        return lojaModel.getNome();
    }
}
