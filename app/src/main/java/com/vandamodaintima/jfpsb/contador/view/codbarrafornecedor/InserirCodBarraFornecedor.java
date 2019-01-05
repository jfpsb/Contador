package com.vandamodaintima.jfpsb.contador.view.codbarrafornecedor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.controller.codbarrafornecedor.InserirCodBarraFornecedorController;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class InserirCodBarraFornecedor extends Fragment implements CadastrarView {
    private Produto produto;

    private EditText txtCodBarra;
    private Button btnInserir;

    private InserirCodBarraFornecedorController inserirCodBarraFornecedorController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inserir_cod_barra_fornecedor, container, false);

        txtCodBarra = view.findViewById(R.id.txtCodBarra);
        btnInserir = view.findViewById(R.id.btnInserir);

        inserirCodBarraFornecedorController = new InserirCodBarraFornecedorController(this);

        produto = (Produto) getArguments().getSerializable("produto");

        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cod_barra_fornecedor = txtCodBarra.getText().toString();
                inserirCodBarraFornecedorController.inserir(cod_barra_fornecedor, produto);
            }
        });

        txtCodBarra.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (getUserVisibleHint()) {
                    if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_ENTER) {
                        String cod_barra_fornecedor = txtCodBarra.getText().toString().replace("\n", "");
                        inserirCodBarraFornecedorController.inserir(cod_barra_fornecedor, produto);
                        return true;
                    }
                } else {
                    txtCodBarra.getText().clear();
                }

                return false;
            }
        });

        return view;
    }

    @Override
    public void limparCampos() {
        txtCodBarra.getText().clear();
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void aposCadastro() {
        Fragment fragment = getActivity().getSupportFragmentManager().getFragments().get(0);

        if (fragment instanceof ListarCodBarraFornecedor)
            ((ListarCodBarraFornecedor) fragment).realizarPesquisa();
    }

    @Override
    public void focoEmTxt() {
        txtCodBarra.clearFocus();
        txtCodBarra.requestFocus();
    }

    @Override
    public void onResume() {
        txtCodBarra.requestFocus();
        super.onResume();
    }
}
