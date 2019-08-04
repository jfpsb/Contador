package com.vandamodaintima.jfpsb.contador.controller.codbarrafornecedor;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

import java.util.ArrayList;

public class ListarCodBarraFornecedorController {
    private ArrayAdapter<String> codigoAdapter;
    private PesquisarView view;
    private Context context;

    public ListarCodBarraFornecedorController(PesquisarView view, Context context) {
        this.view = view;
        this.context = context;
        codigoAdapter = new ArrayAdapter<>(context, R.layout.item_lista_cod_barra_fornecedor);
        view.setListViewAdapter(codigoAdapter);
    }

    public void pesquisar(ArrayList<String> codigos) {
        codigoAdapter.clear();
        codigoAdapter.addAll(codigos);
        codigoAdapter.notifyDataSetChanged();

        if (codigos.size() == 0) {
            view.mensagemAoUsuario("Não Há Códigos De Fornecedor No Produto");
        }

        view.setTextoQuantidadeBusca(codigos.size());
    }
}
