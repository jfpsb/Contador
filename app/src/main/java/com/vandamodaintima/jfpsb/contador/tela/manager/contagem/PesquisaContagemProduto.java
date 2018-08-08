package com.vandamodaintima.jfpsb.contador.tela.manager.contagem;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.manager.contagem.AdicionarContagemProduto;
import com.vandamodaintima.jfpsb.contador.tela.manager.produto.PesquisarProduto;

public class PesquisaContagemProduto extends PesquisarProduto {
    private Contagem contagem;

    public PesquisaContagemProduto() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        contagem = (Contagem) bundle.getSerializable("contagem");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setListOnItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                    if(cursor == null)
                        Log.i("Contador", "Cursor está fechado");

                    cursor.moveToPosition(i);

                    // Já que produto possui um Fornecedor em recupero logo do banco pq o Fornecedor já vem iniciado se existir
                    Produto produto = produtoManager.listarPorChave(cursor.getString(cursor.getColumnIndexOrThrow("_id")));

                    Bundle bundle = new Bundle();

                    bundle.putSerializable("produto", produto);
                    bundle.putSerializable("contagem", contagem);

                    Intent adicionarProduto = new Intent(viewInflate.getContext(), AdicionarContagemProduto.class);

                    adicionarProduto.putExtras(bundle);

                    startActivity(adicionarProduto);
                }
                catch (Exception e) {
                    Log.e("Contador", e.getMessage(), e);
                }
            }
        });
    }
}
