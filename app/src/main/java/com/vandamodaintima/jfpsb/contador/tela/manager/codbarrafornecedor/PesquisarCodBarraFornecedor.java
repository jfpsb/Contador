package com.vandamodaintima.jfpsb.contador.tela.manager.codbarrafornecedor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.CodBarraFornecedorArrayAdapter;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.entidade.CodBarraFornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.TelaPesquisa;

public class PesquisarCodBarraFornecedor extends TelaPesquisa {
    private Produto produto;

    private ListView listView;
    private TextView txtQuantCodigos;
    private TextView txtCodBarra;
    private TextView txtDescricao;

    ArrayAdapter<CodBarraFornecedor> arrayAdapter;

    private AlertDialog.Builder alertaRemoverLista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null)
            savedInstanceState = new Bundle();

        savedInstanceState.putInt("layout", R.layout.fragment_pesquisar_cod_barra_fornecedor);

        produto = (Produto) getArguments().getSerializable("produto");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setManagers() {

    }

    @Override
    protected void setViews() {
        txtQuantCodigos = viewInflate.findViewById(R.id.txtQuantCodigos);
        txtCodBarra = viewInflate.findViewById(R.id.txtCodBarra);
        txtDescricao = viewInflate.findViewById(R.id.txtDescricao);
        listView = viewInflate.findViewById(R.id.listViewCodigos);

        txtCodBarra.setText(produto.getCod_barra());
        txtDescricao.setText(produto.getDescricao());

        setListView();
    }

    @Override
    protected void setListView() {
        try {
            arrayAdapter = new CodBarraFornecedorArrayAdapter(getContext(), produto.getCod_barra_fornecedor());
            listView.setAdapter(arrayAdapter);

            if (produto.getCod_barra_fornecedor().size() == 0) {
                Toast.makeText(getContext(), "Não Há Códigos de Barras de Fornecedor Neste Produto", Toast.LENGTH_LONG).show();
            }

            txtQuantCodigos.setText(String.valueOf(produto.getCod_barra_fornecedor().size()));
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        super.setListView();
    }

    @Override
    protected void setListOnItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CodBarraFornecedor codBarraFornecedor = (CodBarraFornecedor) parent.getItemAtPosition(position);
                setAlertaRemoverLista(codBarraFornecedor.getCodigo(), position);
            }
        });
    }

    /**
     * Popula a lista novamente
     */
    public void populaListView() {
        setListView();
    }

    public void setAlertaRemoverLista(String codigo, final int index) {
        if (alertaRemoverLista == null)
            alertaRemoverLista = new AlertDialog.Builder(getContext());

        alertaRemoverLista.setTitle("Remover Código de Listar");
        alertaRemoverLista.setMessage("Tem Certeza Que Deseja Remover o Código " + codigo + " da Lista?");

        alertaRemoverLista.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                produto.getCod_barra_fornecedor().remove(index);
                populaListView();
            }
        });

        alertaRemoverLista.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Código Não Foi Removido", Toast.LENGTH_SHORT).show();
            }
        });

        alertaRemoverLista.show();
    }
}
