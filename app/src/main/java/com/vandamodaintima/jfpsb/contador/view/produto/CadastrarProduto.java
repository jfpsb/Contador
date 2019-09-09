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
import com.vandamodaintima.jfpsb.contador.view.codbarrafornecedor.TelaCodBarraFornecedor;
import com.vandamodaintima.jfpsb.contador.view.fornecedor.TelaFornecedorForResult;
import com.vandamodaintima.jfpsb.contador.view.marca.TelaMarcaForResult;

import java.util.ArrayList;

public class CadastrarProduto extends TelaCadastro {
    private Button btnCadastrar;
    private Button btnEscolherFornecedor;
    private Button btnEscolherMarca;
    private Button btnGerenciarCodBarraFornecedor;
    private Button btnRemoverFornecedor;
    private Button btnRemoverMarca;
    private EditText txtCodBarra;
    private EditText txtDescricao;
    private EditText txtPreco;
    private EditText txtFornecedor;
    private EditText txtMarca;
    private TextView lblCodRepetido;

    private Animation slidedown;

    protected CadastrarProdutoController controller;

    private static final int ESCOLHER_FORNECEDOR = 1;
    private static final int ESCOLHER_MARCA = 2;
    private static final int TELA_COD_BARRA_FORNECEDOR = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cadastrar_produto, container, false);

        conexaoBanco = new ConexaoBanco(getContext());
        controller = new CadastrarProdutoController(this, conexaoBanco);

        btnCadastrar = view.findViewById(R.id.btnCadastrar);
        btnEscolherFornecedor = view.findViewById(R.id.btnEscolherFornecedor);
        btnEscolherMarca = view.findViewById(R.id.btnEscolherMarca);
        btnGerenciarCodBarraFornecedor = view.findViewById(R.id.btnGerenciarCodBarraFornecedor);
        btnRemoverFornecedor = view.findViewById(R.id.btnRemoverFornecedor);
        btnRemoverMarca = view.findViewById(R.id.btnRemoverMarca);
        txtCodBarra = view.findViewById(R.id.txtCodBarra);
        txtDescricao = view.findViewById(R.id.txtDescricao);
        txtPreco = view.findViewById(R.id.txtPreco);
        txtFornecedor = view.findViewById(R.id.txtFornecedor);
        txtMarca = view.findViewById(R.id.txtMarca);
        lblCodRepetido = view.findViewById(R.id.lblCodRepetido);
        slidedown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);

        btnEscolherFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TelaFornecedorForResult.class);
                startActivityForResult(intent, ESCOLHER_FORNECEDOR);
            }
        });

        btnEscolherMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TelaMarcaForResult.class);
                startActivityForResult(intent, ESCOLHER_MARCA);
            }
        });

        btnGerenciarCodBarraFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TelaCodBarraFornecedor.class);
                intent.putExtra("codigos", controller.getProduto().getCod_barra_fornecedor());
                startActivityForResult(intent, TELA_COD_BARRA_FORNECEDOR);
            }
        });

        btnRemoverFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setFornecedorNull();
                txtFornecedor.getText().clear();
                Toast.makeText(getContext(), "Fornecedor Foi Removido Deste Produto", Toast.LENGTH_SHORT).show();
            }
        });

        btnRemoverMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setMarcaNull();
                txtMarca.getText().clear();
                Toast.makeText(getContext(), "Marca Foi Removida Deste Produto", Toast.LENGTH_SHORT).show();
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cod_barra = txtCodBarra.getText().toString();
                String descricao = txtDescricao.getText().toString();
                Double preco = Double.parseDouble(txtPreco.getText().toString());

                controller.cadastrar(cod_barra, descricao, preco);
            }
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

        return view;
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
            case TELA_COD_BARRA_FORNECEDOR:
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> codigos = (ArrayList<String>) data.getSerializableExtra("codigos");

                    if (codigos.equals(controller.getProduto().getCod_barra_fornecedor())) {
                        Toast.makeText(getContext(), "Cód. de Barras de Fornecedores Não Foram Alterados", Toast.LENGTH_SHORT).show();
                    } else {
                        controller.getProduto().setCod_barra_fornecedor(codigos);
                        Toast.makeText(getContext(), "A Lista de Códigos Será Consolidada ao Apertar em \"Cadastrar\"", Toast.LENGTH_LONG).show();
                    }
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

        controller.resetaProduto();
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