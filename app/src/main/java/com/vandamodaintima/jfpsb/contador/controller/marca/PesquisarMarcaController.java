package com.vandamodaintima.jfpsb.contador.controller.marca;

import android.database.Cursor;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public class PesquisarMarcaController {
    private PesquisarView view;
    private Marca marcaModel;
    private SimpleCursorAdapter marcaAdapter;

    public PesquisarMarcaController(PesquisarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        marcaModel = new Marca(conexaoBanco);
        marcaAdapter = new SimpleCursorAdapter(view.getContext(), R.layout.item_pesquisa_marca, null, new String[]{"_id"}, new int[]{R.id.labelMarcaNome}, 0);
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

    public void carregaMarca(String nome) {
        marcaModel.load(nome);
    }

    public Marca getMarca() {
        return marcaModel;
    }
}
