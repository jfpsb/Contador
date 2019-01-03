package com.vandamodaintima.jfpsb.contador.view.codbarrafornecedor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Produto;

public class CadastrarCodBarraFornecedor extends Fragment {
    private Produto produto;

    private EditText txtCodBarraProduto;
    private EditText txtDescricao;
    private EditText txtCodBarra;
    private Button btnInserir;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState == null)
            savedInstanceState = new Bundle();

        savedInstanceState.putInt("layout", R.layout.fragment_inserir_cod_barra_fornecedor);

        produto = (Produto) getArguments().getSerializable("produto");

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
