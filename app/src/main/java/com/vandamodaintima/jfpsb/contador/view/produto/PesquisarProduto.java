package com.vandamodaintima.jfpsb.contador.view.produto;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.produto.PesquisarProdutoController;
import com.vandamodaintima.jfpsb.contador.view.TelaPesquisa;

public class PesquisarProduto extends TelaPesquisa {

    protected Spinner spinnerPesquisa;
    protected EditText txtPesquisaProduto;
    protected TextView txtQuantProdutosCadastrados;

    private static final int DESCRICAO = 0;
    private static final int COD_DE_BARRA = 1;
    private static final int FORNECEDOR = 2;
    private static final int MARCA = 3;

    private static int PESQUISA = DESCRICAO;

    protected PesquisarProdutoController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pesquisar_produto, container, false);

        listView = view.findViewById(R.id.listViewProduto);
        spinnerPesquisa = view.findViewById(R.id.spinnerPesquisa);
        txtPesquisaProduto = view.findViewById(R.id.txtPesquisaProduto);
        txtQuantProdutosCadastrados = view.findViewById(R.id.txtQuantProdutosCadastrados);

        // Não pode ser antes de instanciar as views
        conexaoBanco = new ConexaoBanco(getContext());
        controller = new PesquisarProdutoController(this, conexaoBanco);

        spinnerPesquisa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case DESCRICAO:
                        txtPesquisaProduto.setHint("Digite a Descrição do Produto");
                        txtPesquisaProduto.setInputType(InputType.TYPE_CLASS_TEXT);
                        PESQUISA = DESCRICAO;
                        break;
                    case COD_DE_BARRA:
                        txtPesquisaProduto.setHint("Digite o Código de Barra");
                        txtPesquisaProduto.setInputType(InputType.TYPE_CLASS_NUMBER);
                        PESQUISA = COD_DE_BARRA;
                        break;
                    case FORNECEDOR:
                        txtPesquisaProduto.setHint("Digite o Nome do Fornecedor");
                        txtPesquisaProduto.setInputType(InputType.TYPE_CLASS_TEXT);
                        PESQUISA = FORNECEDOR;
                        break;
                    case MARCA:
                        txtPesquisaProduto.setHint("Digite o Nome da Marca");
                        txtPesquisaProduto.setInputType(InputType.TYPE_CLASS_TEXT);
                        PESQUISA = MARCA;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txtPesquisaProduto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                realizarPesquisa();
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TELA_ALTERAR_DELETAR:
                if (resultCode == Activity.RESULT_OK) {
                    realizarPesquisa();
                } else {
                    mensagemAoUsuario("Produto Não Foi Alterado");
                }

                break;
        }
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setTextoQuantidadeBusca(int quantidade) {
        txtQuantProdutosCadastrados.setText(String.valueOf(quantidade));
    }

    @Override
    public void realizarPesquisa() {
        String termo = txtPesquisaProduto.getText().toString();

        switch (PESQUISA) {
            case DESCRICAO:
                controller.pesquisarPorDescricao(termo);
                break;
            case COD_DE_BARRA:
                controller.pesquisarPorCodBarra(termo);
                break;
            case FORNECEDOR:
                controller.pesquisarPorFornecedor(termo);
                break;
            case MARCA:
                controller.pesquisarPorMarca(termo);
                break;
        }
    }

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

        String cod_barra = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

        Bundle bundle = new Bundle();
        bundle.putString("produto", cod_barra);

        Intent alterarProduto = new Intent(getContext(), AlterarDeletarProduto.class);
        alterarProduto.putExtras(bundle);

        startActivityForResult(alterarProduto, TELA_ALTERAR_DELETAR);
    }
}