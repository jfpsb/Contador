package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;

import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagem;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.view.contagem.CadastrarContagem;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class CadastrarContagemController {
    private CadastrarView view;
    private DAOContagem daoContagem;
    private DAOLoja daoLoja;
    private SimpleCursorAdapter spinnerLojaAdapter;

    public CadastrarContagemController(CadastrarView view, SQLiteDatabase sqLiteDatabase, Context context) {
        this.view = view;
        daoContagem = new DAOContagem(sqLiteDatabase);
        daoLoja = new DAOLoja(sqLiteDatabase);
        spinnerLojaAdapter = new SimpleCursorAdapter(context, android.R.layout.simple_spinner_dropdown_item, null, new String[]{"nome"}, new int[]{android.R.id.text1}, 0);
        ((CadastrarContagem) view).setSpinnerLojaAdapter(spinnerLojaAdapter);
    }

    public void cadastrar(Contagem contagem) {
        if (contagem.getLoja() == null) {
            view.mensagemAoUsuario("Selecione Uma Loja Válida");
            return;
        }

        Boolean result = daoContagem.inserir(contagem);

        if (result) {
            view.mensagemAoUsuario("Contagem Cadastrada Com Sucesso");
            view.limparCampos();
            view.aposCadastro();
        } else {
            view.mensagemAoUsuario("Erro Ao Cadastrar Contagem");
        }
    }

    public void popularSpinnerLoja() {
        Cursor cursor = daoLoja.listarCursor();

        spinnerLojaAdapter.changeCursor(cursor);
        spinnerLojaAdapter.notifyDataSetChanged();
    }

    public Loja retornaLojaEscolhidaSpinner(String cnpj) {
        return daoLoja.listarPorId(cnpj);
    }
}
