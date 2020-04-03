package com.vandamodaintima.jfpsb.contador.controller.codbarrafornecedor;

import android.widget.ArrayAdapter;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

import java.util.ArrayList;

public class ListarCodBarraFornecedorController {
    private ArrayAdapter<String> codigoAdapter;
    private PesquisarView view;
    private Produto produtoModel;

    public ListarCodBarraFornecedorController(PesquisarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        produtoModel = new Produto(conexaoBanco);
        codigoAdapter = new ArrayAdapter<>(view.getContext(), R.layout.item_lista_cod_barra_fornecedor);
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

    public void carregaProduto(String id) {
        produtoModel.load(id);
    }

    public Produto getProduto() {
        return produtoModel;
    }
}