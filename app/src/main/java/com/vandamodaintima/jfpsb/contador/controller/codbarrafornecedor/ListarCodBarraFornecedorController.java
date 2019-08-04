package com.vandamodaintima.jfpsb.contador.controller.codbarrafornecedor;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

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

    public void pesquisar(ProdutoModel produtoModel) {
        codigoAdapter.clear();
        codigoAdapter.addAll(produtoModel.getCod_barra_fornecedor());
        codigoAdapter.notifyDataSetChanged();

        if (produtoModel.getCod_barra_fornecedor().size() == 0) {
            view.mensagemAoUsuario("Não Há Códigos De FornecedorModel No ProdutoModel");
        }

        view.setTextoQuantidadeBusca(produtoModel.getCod_barra_fornecedor().size());
    }
}
