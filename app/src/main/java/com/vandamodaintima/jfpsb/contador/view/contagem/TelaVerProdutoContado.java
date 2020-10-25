package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.controller.contagem.TelaVerProdutoContadoController;
import com.vandamodaintima.jfpsb.contador.view.interfaces.IAdicionarContagemProduto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ITelaVerProdutoContado;

public class TelaVerProdutoContado extends Fragment implements ITelaVerProdutoContado {
    private ListView listViewContagemProduto;
    private AlertDialog.Builder deletarContagemProdutoDialog;
    private TelaVerProdutoContadoController controller;

    private IAdicionarContagemProduto owner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_itens_contagem_produto, container, false);

        listViewContagemProduto = view.findViewById(R.id.listViewAdicionarContagem);

        owner = (IAdicionarContagemProduto) getActivity();
        controller = new TelaVerProdutoContadoController(this, owner.getConexaoBanco());

        Bundle bundle = getArguments();
        String loja = bundle.getString("loja");
        String data = bundle.getString("data");
        controller.carregaContagem(loja, data);

        listViewContagemProduto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cliqueEmItemLista(adapterView, i);
            }
        });

        setDeletarContagemProdutoDialog();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        controller.pesquisar();
    }

    @Override
    public void setListViewAdapter(final ListAdapter adapter) {
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                //Move para o último item da lista sempre que o adapter for modificado
                int lastIndex = adapter.getCount() - 1;
                listViewContagemProduto.smoothScrollToPosition(lastIndex);
            }
        });

        listViewContagemProduto.setAdapter(adapter);
    }

    @Override
    public void realizarPesquisa() {
        controller.pesquisar();
    }

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
        long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
        controller.carregaContagemProduto(id);
        deletarContagemProdutoDialog.show();
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    private void setDeletarContagemProdutoDialog() {
        deletarContagemProdutoDialog = new AlertDialog.Builder(getContext());
        deletarContagemProdutoDialog.setTitle("Deletar Contagem de Produto");
        deletarContagemProdutoDialog.setMessage("Deletar Esta Contagem de Produto?");

        deletarContagemProdutoDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mensagemAoUsuario("A Contagem de Produto Não Foi Deletada");
            }
        });

        deletarContagemProdutoDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                controller.deletar();
            }
        });
    }
}
