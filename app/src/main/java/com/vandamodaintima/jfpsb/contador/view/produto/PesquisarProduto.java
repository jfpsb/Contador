package com.vandamodaintima.jfpsb.contador.view.produto;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.produto.PesquisarProdutoController;
import com.vandamodaintima.jfpsb.contador.controller.produto.ProdutoAdapter;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.TelaPesquisa;

public class PesquisarProduto extends TelaPesquisa {

    protected Spinner spinnerPesquisa;
    protected EditText txtPesquisaProduto;
    protected ListView listView;
    protected TextView txtQuantProdutosCadastrados;

    private static final int DESCRICAO = 0;
    private static final int COD_DE_BARRA = 1;
    private static final int FORNECEDOR = 2;
    private static final int MARCA = 3;

    private static int PESQUISA = DESCRICAO;

    private PesquisarProdutoController pesquisarProdutoController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pesquisar_produto, container, false);

        sqLiteDatabase = new ConexaoBanco(getContext()).conexao();
        pesquisarProdutoController = new PesquisarProdutoController(this, sqLiteDatabase, getContext());

        listView = view.findViewById(R.id.listViewProduto);
        spinnerPesquisa = view.findViewById(R.id.spinnerPesquisa);
        txtPesquisaProduto = view.findViewById(R.id.txtPesquisaProduto);
        txtQuantProdutosCadastrados = view.findViewById(R.id.txtQuantProdutosCadastrados);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Produto produto = (Produto) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getContext(), AlterarDeletarProduto.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("produto", produto);

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

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
                String termo = txtPesquisaProduto.getText().toString();
                realizarPesquisa(termo);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TELA_ALTERAR_DELETAR:
                if (resultCode == Activity.RESULT_OK) {
                    String termo = txtPesquisaProduto.getText().toString();
                    realizarPesquisa(termo);
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
    public void populaLista(ArrayAdapter adapter) {
        listView.setAdapter(null);
        listView.setAdapter(adapter);

        txtQuantProdutosCadastrados.setText(String.valueOf(adapter.getCount()));
    }

    public void populaLista(ProdutoAdapter adapter) {
        listView.setAdapter(adapter);
        txtQuantProdutosCadastrados.setText(String.valueOf(adapter.getCount()));
    }

    @Override
    public void realizarPesquisa(String... termos) {
        String termo = String.valueOf(termos[0]);

        switch (PESQUISA) {
            case DESCRICAO:
                pesquisarProdutoController.pesquisarPorDescricao(termo);
                break;
            case COD_DE_BARRA:
                pesquisarProdutoController.pesquisarPorCodBarra(termo);
                break;
            case FORNECEDOR:
                pesquisarProdutoController.pesquisarPorFornecedor(termo);
                break;
            case MARCA:
                pesquisarProdutoController.pesquisarPorMarca(termo);
                break;
        }
    }
}