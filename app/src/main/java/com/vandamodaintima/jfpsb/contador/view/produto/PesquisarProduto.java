package com.vandamodaintima.jfpsb.contador.view.produto;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.produto.PesquisarProdutoController;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public class PesquisarProduto extends Fragment implements PesquisarView {

    protected Spinner spinnerPesquisa;
    protected EditText txtPesquisaProduto;
    protected ListView listView;
    protected TextView txtQuantProdutosCadastrados;

    private static int PESQUISA = 0;
    private static final int DESCRICAO = 0;
    private static final int COD_DE_BARRA = 1;
    private static final int FORNECEDOR = 2;
    private static final int MARCA = 3;

    private SQLiteDatabase sqLiteDatabase;
    private PesquisarProdutoController pesquisarProdutoController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewInflate = inflater.inflate(R.layout.fragment_pesquisar_produto, container, false);

        sqLiteDatabase = new ConexaoBanco(getContext()).conexao();
        pesquisarProdutoController = new PesquisarProdutoController(this, sqLiteDatabase, getContext());

        listView = viewInflate.findViewById(R.id.listViewProduto);
        spinnerPesquisa = viewInflate.findViewById(R.id.spinnerPesquisa);
        txtPesquisaProduto = viewInflate.findViewById(R.id.txtPesquisaProduto);

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
        });

        return viewInflate;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void populaLista(ArrayAdapter adapter) {
        listView.setAdapter(null);
        listView.setAdapter(adapter);
    }
}