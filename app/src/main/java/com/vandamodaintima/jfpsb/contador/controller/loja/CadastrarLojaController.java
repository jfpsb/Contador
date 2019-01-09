package com.vandamodaintima.jfpsb.contador.controller.loja;

import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

import java.util.ArrayList;

public class CadastrarLojaController {
    private CadastrarView view;
    private DAOLoja daoLoja;

    public CadastrarLojaController(CadastrarView view, SQLiteDatabase sqLiteDatabase) {
        this.view = view;
        daoLoja = new DAOLoja(sqLiteDatabase);
    }

    public void cadastrar(Loja loja) {
        if (loja.getCnpj().isEmpty()) {
            view.mensagemAoUsuario("CNPJ Não Pode Estar Vazio");
            return;
        }

        if (loja.getNome().isEmpty()) {
            view.mensagemAoUsuario("Nome Não Pode Estar Vazio");
            return;
        }

        Boolean result = daoLoja.inserir(loja);

        if (result) {
            view.mensagemAoUsuario("Loja Cadastrada Com Sucesso!");
            view.aposCadastro();
            view.limparCampos();
        } else {
            view.mensagemAoUsuario("Erro Ao Cadastrar Loja");
        }
    }

    public ArrayList<Loja> getMatrizes() {
        return daoLoja.listarMatrizes();
    }
}
