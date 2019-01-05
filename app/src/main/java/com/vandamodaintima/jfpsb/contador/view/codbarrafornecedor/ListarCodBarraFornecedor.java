package com.vandamodaintima.jfpsb.contador.view.codbarrafornecedor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.controller.codbarrafornecedor.ListarCodBarraFornecedorController;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.TelaPesquisa;

public class ListarCodBarraFornecedor extends TelaPesquisa {
    private Produto produto;

    private TextView txtQuantCodigos;
    private TextView txtCodBarra;
    private TextView txtDescricao;

    private ListarCodBarraFornecedorController listarCodBarraFornecedorController;

    private AlertDialog.Builder alertaRemoverLista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pesquisar_cod_barra_fornecedor, container, false);

        listView = view.findViewById(R.id.listViewCodigos);
        txtQuantCodigos = view.findViewById(R.id.txtQuantCodigos);

        alertaRemoverLista = new AlertDialog.Builder(getContext());

        produto = (Produto) getArguments().getSerializable("produto");

        if (produto.getCod_barra() != null && produto.getDescricao() != null) {
            ViewStub stub = view.findViewById(R.id.dados_produto_cod_barra_fornecedor_stub);
            stub.setLayoutResource(R.layout.dados_produto_cod_barra_fornecedor);
            stub.inflate();

            txtCodBarra = view.findViewById(R.id.txtCodBarraProduto);
            txtDescricao = view.findViewById(R.id.txtDescricao);

            txtCodBarra.setText(produto.getCod_barra());
            txtDescricao.setText(produto.getDescricao());
        }

        listarCodBarraFornecedorController = new ListarCodBarraFornecedorController(this, getContext());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String codigo = (String) parent.getItemAtPosition(position);
                setAlertaRemoverLista(codigo, position);
            }
        });

        realizarPesquisa();

        listView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int i, KeyEvent event) {
                if (i == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }

                return false;
            }
        });

        return view;
    }

    public void setAlertaRemoverLista(String codigo, final int index) {
        alertaRemoverLista.setTitle("Remover Código de Listar");
        alertaRemoverLista.setMessage("Tem Certeza Que Deseja Remover o Código " + codigo + " da Lista?");

        alertaRemoverLista.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                produto.getCod_barra_fornecedor().remove(index);

                Fragment fragment = getActivity().getSupportFragmentManager().getFragments().get(1);
                if (fragment instanceof InserirCodBarraFornecedor) {
                    ((InserirCodBarraFornecedor) fragment).focoEmTxt();
                }

                realizarPesquisa();
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

    @Override
    public void realizarPesquisa() {
        listarCodBarraFornecedorController.pesquisar(produto);
    }

    @Override
    public void setTextoQuantidadeBusca(int quantidade) {
        txtQuantCodigos.setText(String.valueOf(quantidade));
    }
}
