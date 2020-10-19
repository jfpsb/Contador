package com.vandamodaintima.jfpsb.contador.view.produto;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.produto.CadastrarProdutoController;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.view.TelaCadastro;
import com.vandamodaintima.jfpsb.contador.view.fornecedor.TelaFornecedorForResult;
import com.vandamodaintima.jfpsb.contador.view.marca.TelaMarcaForResult;
import com.vandamodaintima.jfpsb.contador.view.produto.grade.GerenciarGrades;

public class CadastrarProduto extends TelaCadastro {
    private Button btnCadastrar;
    private Button btnEscolherFornecedor;
    private Button btnEscolherMarca;
    private Button btnRemoverFornecedor;
    private Button btnRemoverMarca;
    private Button btnGerenciarGrades;
    protected EditText txtCodBarra;
    private EditText txtDescricao;
    private EditText txtPreco;
    private EditText txtFornecedor;
    private EditText txtMarca;
    private EditText txtNcm;
    private TextView lblCodRepetido;

    private Animation slidedown;

    protected CadastrarProdutoController controller;

    private static final int ESCOLHER_FORNECEDOR = 1;
    private static final int ESCOLHER_MARCA = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        telaCadastroView = inflater.inflate(R.layout.fragment_cadastrar_produto, container, false);

        conexaoBanco = new ConexaoBanco(getContext());
        controller = new CadastrarProdutoController(this, conexaoBanco);

        btnCadastrar = telaCadastroView.findViewById(R.id.btnCadastrar);
        btnEscolherFornecedor = telaCadastroView.findViewById(R.id.btnEscolherFornecedor);
        btnEscolherMarca = telaCadastroView.findViewById(R.id.btnEscolherMarca);
        btnRemoverFornecedor = telaCadastroView.findViewById(R.id.btnRemoverFornecedor);
        btnRemoverMarca = telaCadastroView.findViewById(R.id.btnRemoverMarca);
        btnGerenciarGrades = telaCadastroView.findViewById(R.id.btnGerenciarGrades);
        txtCodBarra = telaCadastroView.findViewById(R.id.txtCodBarra);
        txtDescricao = telaCadastroView.findViewById(R.id.txtDescricao);
        txtPreco = telaCadastroView.findViewById(R.id.txtPreco);
        txtFornecedor = telaCadastroView.findViewById(R.id.txtFornecedor);
        txtMarca = telaCadastroView.findViewById(R.id.txtMarca);
        txtNcm = telaCadastroView.findViewById(R.id.txtNcm);
        lblCodRepetido = telaCadastroView.findViewById(R.id.lblCodRepetido);
        slidedown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);

        btnGerenciarGrades.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GerenciarGrades.class);
            startActivity(intent);
        });

        btnEscolherFornecedor.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TelaFornecedorForResult.class);
            startActivityForResult(intent, ESCOLHER_FORNECEDOR);
        });

        btnEscolherMarca.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TelaMarcaForResult.class);
            startActivityForResult(intent, ESCOLHER_MARCA);
        });

        btnRemoverFornecedor.setOnClickListener(v -> {
            controller.setFornecedorNull();
            txtFornecedor.getText().clear();
            Toast.makeText(getContext(), "Fornecedor Foi Removido Deste Produto", Toast.LENGTH_SHORT).show();
        });

        btnRemoverMarca.setOnClickListener(v -> {
            controller.setMarcaNull();
            txtMarca.getText().clear();
            Toast.makeText(getContext(), "Marca Foi Removida Deste Produto", Toast.LENGTH_SHORT).show();
        });

        btnCadastrar.setOnClickListener(view -> {
            controller.getProduto().setCodBarra(txtCodBarra.getText().toString());
            controller.getProduto().setDescricao(txtDescricao.getText().toString().toUpperCase());
            controller.getProduto().setNcm(txtNcm.getText().toString());
            boolean precoResult = controller.setPreco(txtPreco.getText().toString());

            if (!precoResult)
                txtPreco.setText("0.0");

            controller.cadastrar();
        });

        txtCodBarra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                controller.checaCodigoBarra(txtCodBarra.getText().toString());
            }
        });

        return telaCadastroView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ESCOLHER_FORNECEDOR:
                if (resultCode == Activity.RESULT_OK) {
                    Fornecedor fornecedor = (Fornecedor) data.getSerializableExtra("fornecedor");
                    controller.carregaFornecedor(fornecedor);
                    txtFornecedor.setText(controller.getProduto().getFornecedor().getNome());
                }
                break;
            case ESCOLHER_MARCA:
                if (resultCode == Activity.RESULT_OK) {
                    Marca marca = (Marca) data.getSerializableExtra("marca");
                    controller.carregaMarca(marca);
                    txtMarca.setText(controller.getProduto().getMarca().getNome());
                }
                break;
        }
    }

    @Override
    public void limparCampos() {
        txtCodBarra.getText().clear();
        txtDescricao.getText().clear();
        txtPreco.getText().clear();
        txtMarca.getText().clear();
        txtFornecedor.getText().clear();
        controller.reset();
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void focoEmViewInicial() {
        txtCodBarra.requestFocus();
    }

    public void bloqueiaCampos() {
        txtDescricao.setEnabled(false);
        txtPreco.setEnabled(false);
        btnCadastrar.setEnabled(false);
        btnEscolherFornecedor.setEnabled(false);
        lblCodRepetido.setVisibility(View.VISIBLE);
        lblCodRepetido.startAnimation(slidedown);
    }

    public void liberaCampos() {
        txtDescricao.setEnabled(true);
        txtPreco.setEnabled(true);
        btnCadastrar.setEnabled(true);
        btnEscolherFornecedor.setEnabled(true);
        lblCodRepetido.setVisibility(View.GONE);
    }
}