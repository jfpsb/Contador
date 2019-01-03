package com.vandamodaintima.jfpsb.contador.view.codbarrafornecedor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.TelaPesquisa;

public class ListarCodBarraFornecedor extends TelaPesquisa {
    private Produto produto;

    private ListView listView;
    private TextView txtQuantCodigos;
    private TextView txtCodBarra;
    private TextView txtDescricao;

    ArrayAdapter<String> arrayAdapter;

    private AlertDialog.Builder alertaRemoverLista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null)
            savedInstanceState = new Bundle();

        savedInstanceState.putInt("layout", R.layout.fragment_pesquisar_cod_barra_fornecedor);

        produto = (Produto) getArguments().getSerializable("produto");

        return super.onCreateView(inflater, container, savedInstanceState);
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
                //populaListView();
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
