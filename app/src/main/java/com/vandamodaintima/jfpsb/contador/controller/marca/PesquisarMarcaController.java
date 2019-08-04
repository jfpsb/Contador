package com.vandamodaintima.jfpsb.contador.controller.marca;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.MarcaModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public class PesquisarMarcaController {
    private PesquisarView view;
    private MarcaModel marcaModel;
    private SimpleCursorAdapter marcaAdapter;

    public PesquisarMarcaController(PesquisarView view, ConexaoBanco conexaoBanco, Context context) {
        this.view = view;
        marcaModel = new MarcaModel(conexaoBanco);
        marcaAdapter = new SimpleCursorAdapter(context, R.layout.item_pesquisa_marca, null, new String[]{"nome"}, new int[]{R.id.labelMarcaNome}, 0);
        view.setListViewAdapter(marcaAdapter);
    }

    public void pesquisar(String nome) {
        Cursor cursor = marcaModel.listarPorNomeCursor(nome);

        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Marcas Não Encontradas");
        }

        marcaAdapter.changeCursor(cursor);
        marcaAdapter.notifyDataSetChanged();

        view.setTextoQuantidadeBusca(cursor.getCount());
    }
}
