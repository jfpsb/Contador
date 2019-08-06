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

import java.util.Date;

public class InserirContagemController {
    private CadastrarView view;
    private ContagemModel contagemModel;
    private LojaModel lojaModel;
    private SimpleCursorAdapter spinnerLojaAdapter;

    public InserirContagemController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        contagemModel = new ContagemModel(conexaoBanco);
        lojaModel = new LojaModel(conexaoBanco);
        spinnerLojaAdapter = new SimpleCursorAdapter(view.getContext(), android.R.layout.simple_spinner_dropdown_item, null, new String[]{"nome"}, new int[]{android.R.id.text1}, 0);
        ((CadastrarContagem) view).setSpinnerLojaAdapter(spinnerLojaAdapter);
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

    public void carregaSpinnerLoja() {
        Cursor cursor = lojaModel.listarCursor();
        spinnerLojaAdapter.changeCursor(cursor);
        spinnerLojaAdapter.notifyDataSetChanged();
    }

    public void carregaLoja(String id) {
        lojaModel.load(id);
    }
}
