package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagem;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

import java.util.ArrayList;
import java.util.Date;

public class PesquisarContagemController {
    private PesquisarView view;
    private DAOContagem daoContagem;
    private ContagemAdapter contagemAdapter;
    private Context context;

    public PesquisarContagemController(PesquisarView view, SQLiteDatabase sqLiteDatabase, Context context) {
        this.view = view;
        this.context = context;
        daoContagem = new DAOContagem(sqLiteDatabase);
    }

    public void pesquisar(String loja, Date dataInicial, Date dataFinal) {
        if (loja == "0" || loja.isEmpty()) {
            view.mensagemAoUsuario("Loja Inválida");
            return;
        }

        ArrayList<Contagem> contagens = daoContagem.listarPorLojaPeriodo(loja, dataInicial, dataFinal);

        if (contagens.size() == 0) {
            view.mensagemAoUsuario("Contagens Não Encontradas");
        }

        contagemAdapter = new ContagemAdapter(context, R.layout.item_pesquisa_id_nome, contagens);

        view.populaLista(contagemAdapter);
    }
}
