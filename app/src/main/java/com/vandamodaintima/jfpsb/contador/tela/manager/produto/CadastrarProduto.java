package com.vandamodaintima.jfpsb.contador.tela.manager.produto;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.dao.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;
import com.vandamodaintima.jfpsb.contador.tela.manager.fornecedor.TelaFornecedorForResult;
import com.vandamodaintima.jfpsb.contador.util.ManipulaCursor;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarProduto extends FragmentBase {

    private Button btnCadastrar;
    private Button btnEscolherFornecedor;
    private EditText txtCodBarra;
    private EditText txtDescricao;
    private EditText txtPreco;
    private EditText txtFornecedor;
    private TextView lblCodRepetido;

    private ProdutoManager produtoManager;

    private Fornecedor fornecedor;

    private Animation slidedown;

    private static final int ESCOLHER_FORNECEDOR = 1;

    public CadastrarProduto() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.fragment_cadastrar_produto, container, false);

        btnCadastrar = viewInflate.findViewById(R.id.btnCadastrar);
        txtCodBarra = viewInflate.findViewById(R.id.txtCodBarra);
        txtDescricao = viewInflate.findViewById(R.id.txtDescricao);
        txtPreco = viewInflate.findViewById(R.id.txtPreco);
        txtFornecedor = viewInflate.findViewById(R.id.txtFornecedor);
        btnEscolherFornecedor = viewInflate.findViewById(R.id.btnEscolherFornecedor);
        lblCodRepetido = viewInflate.findViewById(R.id.lblCnpjRepetido);

        setManagers();
        setBtnEscolherFornecedor();
        setBtnCadastrar();
        setTxtCodBarra();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setManagers() {
        produtoManager = new ProdutoManager(((ActivityBase)getActivity()).getConn());
    }

    private void setBtnEscolherFornecedor() {
        btnEscolherFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TelaFornecedorForResult.class);
                startActivityForResult(intent, ESCOLHER_FORNECEDOR);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ESCOLHER_FORNECEDOR:
                if(resultCode == Activity.RESULT_OK) {
                    fornecedor = (Fornecedor) data.getSerializableExtra("fornecedor");
                    txtFornecedor.setText(fornecedor.getNome());
                }
                break;
        }
    }

    private void setBtnCadastrar() {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Produto produto = new Produto();

                try {
                    String cod_barra = txtCodBarra.getText().toString();
                    String descricao = txtDescricao.getText().toString();
                    String preco = txtPreco.getText().toString();

                    if(TestaIO.isStringEmpty(cod_barra))
                        throw new Exception("Código de barras não pode estar vazio!");

                    if(TestaIO.isStringEmpty(descricao))
                        throw new Exception("A descrição do produto não pode estar vazia!");

                    if(TestaIO.isStringEmpty(preco))
                        throw new Exception("O preço do produto não pode estar vazio!");

                    produto.setCod_barra(cod_barra);
                    produto.setPreco(Double.parseDouble(preco));
                    produto.setDescricao(descricao.toUpperCase());
                    produto.setFornecedor(fornecedor);

                    boolean result = produtoManager.inserir(produto);

                    if(result) {
                        Toast.makeText(viewInflate.getContext(), "O produto " + produto.getDescricao() + " foi inserido com sucesso!" , Toast.LENGTH_SHORT).show();

                        Fragment fragment = ((ActivityBase)getActivity()).getAdapter().getItem(0);
                        ((PesquisarProduto)fragment).populaListView();

                        txtPreco.setText("");
                        txtDescricao.setText("");
                        txtCodBarra.setText("");
                    }
                    else {
                        Toast.makeText(viewInflate.getContext(), "Erro ao Cadastrar Produto.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Log.e("Contador", e.getMessage(), e);
                    Toast.makeText(viewInflate.getContext(), "Erro ao Cadastrar Produto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setTxtCodBarra() {
        slidedown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);

        txtCodBarra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String texto = txtCodBarra.getText().toString();

                if(! texto.isEmpty()) {
                    Produto produto = produtoManager.listarPorChave(texto);

                    if(produto != null) {
                        txtDescricao.setEnabled(false);
                        txtPreco.setEnabled(false);
                        btnCadastrar.setEnabled(false);
                        btnEscolherFornecedor.setEnabled(false);

                        lblCodRepetido.setVisibility(View.VISIBLE);
                        lblCodRepetido.startAnimation(slidedown);
                    }
                    else {
                        txtDescricao.setEnabled(true);
                        txtPreco.setEnabled(true);
                        btnCadastrar.setEnabled(true);
                        btnEscolherFornecedor.setEnabled(true);

                        lblCodRepetido.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
}
