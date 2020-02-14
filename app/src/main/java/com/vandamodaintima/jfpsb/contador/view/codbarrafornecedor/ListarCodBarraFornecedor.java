package com.vandamodaintima.jfpsb.contador.view.codbarrafornecedor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.codbarrafornecedor.ListarCodBarraFornecedorController;
import com.vandamodaintima.jfpsb.contador.view.TelaPesquisa;

import java.util.ArrayList;

public class ListarCodBarraFornecedor extends TelaPesquisa {
    private TextView txtQuantCodigos;
    private TextView txtCodBarra;
    private TextView txtDescricao;
    private ArrayList<String> codigos;

    private ListarCodBarraFornecedorController controller;

    private AlertDialog.Builder alertaRemoverLista;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        telaPesquisaView = inflater.inflate(R.layout.fragment_pesquisar_cod_barra_fornecedor, container, false);

        listView = telaPesquisaView.findViewById(R.id.listViewCodigos);
        txtQuantCodigos = telaPesquisaView.findViewById(R.id.txtQuantCodigos);

        codigos = (ArrayList<String>) getArguments().getSerializable("codigos");

        alertaRemoverLista = new AlertDialog.Builder(getContext());

        conexaoBanco = new ConexaoBanco(getContext());
        controller = new ListarCodBarraFornecedorController(this, conexaoBanco);
        String id = getArguments().getString("produto");
        controller.carregaProduto(id);

        if (controller.getProduto() != null && controller.getProduto() != null) {
            ViewStub stub = telaPesquisaView.findViewById(R.id.dados_produto_cod_barra_fornecedor_stub);
            stub.setLayoutResource(R.layout.dados_produto_cod_barra_fornecedor);
            stub.inflate();

            txtCodBarra = telaPesquisaView.findViewById(R.id.txtCodBarraProduto);
            txtDescricao = telaPesquisaView.findViewById(R.id.txtDescricao);

            txtCodBarra.setText(controller.getProduto().getCod_barra());
            txtDescricao.setText(controller.getProduto().getDescricao());
        }

        realizarPesquisa();

        listView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int i, KeyEvent event) {
                return i == KeyEvent.KEYCODE_ENTER;
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setAlertaRemoverLista(String codigo, final int index) {
        alertaRemoverLista.setTitle("Remover Código de Listar");
        alertaRemoverLista.setMessage("Tem Certeza Que Deseja Remover o Código " + codigo + " da Lista?");

        alertaRemoverLista.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                codigos.remove(index);

                Fragment fragment = getActivity().getSupportFragmentManager().getFragments().get(1);
                if (fragment instanceof InserirCodBarraFornecedor) {
                    ((InserirCodBarraFornecedor) fragment).focoEmViewInicial();
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
        controller.pesquisar(codigos);
    }

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
        String codigo = (String) adapterView.getItemAtPosition(i);
        setAlertaRemoverLista(codigo, i);
    }

    @Override
    public void setTextoQuantidadeBusca(int quantidade) {
        txtQuantCodigos.setText(String.valueOf(quantidade));
    }
}
