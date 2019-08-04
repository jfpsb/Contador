package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ContagemModel;
import com.vandamodaintima.jfpsb.contador.model.LojaModel;
import com.vandamodaintima.jfpsb.contador.view.contagem.CadastrarContagem;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class InserirContagemController {
    private CadastrarView view;
    private LojaModel lojaModel;
    private SimpleCursorAdapter spinnerLojaAdapter;

    public InserirContagemController(CadastrarView view, ConexaoBanco conexaoBanco, Context context) {
        this.view = view;
        lojaModel = new LojaModel(conexaoBanco);
        spinnerLojaAdapter = new SimpleCursorAdapter(context, android.R.layout.simple_spinner_dropdown_item, null, new String[]{"nome"}, new int[]{android.R.id.text1}, 0);
        ((CadastrarContagem) view).setSpinnerLojaAdapter(spinnerLojaAdapter);
    }

    public void cadastrar(ContagemModel contagem) {
        if (contagem.getLoja() == null) {
            view.mensagemAoUsuario("Selecione Uma Loja Válida");
            return;
        }

        Boolean result = contagem.inserir();

        if (result) {
            view.mensagemAoUsuario("Contagem Cadastrada Com Sucesso");
            view.aposCadastro();
            view.limparCampos();
        } else {
            view.mensagemAoUsuario("Erro Ao Cadastrar ContagemModel");
        }
    }

    public void popularSpinnerLoja() {
        Cursor cursor = lojaModel.listarCursor();

        spinnerLojaAdapter.changeCursor(cursor);
        spinnerLojaAdapter.notifyDataSetChanged();
    }

    public LojaModel retornaLojaEscolhidaSpinner(String cnpj) {
        return lojaModel.listarPorId(cnpj);
    }
}
