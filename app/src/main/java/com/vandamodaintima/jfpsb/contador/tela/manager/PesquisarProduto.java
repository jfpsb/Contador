package com.vandamodaintima.jfpsb.contador.tela.manager;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.ProdutoCursorAdapter;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarProduto extends Fragment {
    private RadioGroup radioGroup;
    private EditText txtPesquisaProduto;
    private ConexaoBanco conn;
    private static DAOProduto daoProduto;
    private ListView listView;
    private static ProdutoCursorAdapter produtoCursorAdapter;
    private static int TIPO_PESQUISA = 1;

    public PesquisarProduto() {
        // Required empty public constructor
    }

    public void setConn(ConexaoBanco conn) {
        this.conn = conn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewInflate = inflater.inflate(R.layout.fragment_pesquisar_produto, container, false);

        daoProduto = new DAOProduto(conn.conexao());

        radioGroup = viewInflate.findViewById(R.id.radioGroupOpcao);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selecao = radioGroup.getCheckedRadioButtonId();

                switch (selecao) {
                    case (R.id.rbCodBarra):
                        txtPesquisaProduto.setHint(R.string.hintRadioButtonCodBarra);
                        TIPO_PESQUISA = 1;
                        break;
                    case (R.id.rbDescricao):
                        txtPesquisaProduto.setHint(R.string.hintRadioButtonDescricao);
                        TIPO_PESQUISA = 2;
                        break;
                    case (R.id.rbFornecedor):
                        txtPesquisaProduto.setHint(R.string.hintRadioButtonNomeFornecedor);
                        TIPO_PESQUISA = 3;
                        break;
                    default:
                        Toast.makeText(getActivity(), "Aconteceu algo de errado", Toast.LENGTH_SHORT).show();
                        TIPO_PESQUISA = 1;
                        break;
                }
            }
        });

        listView = viewInflate.findViewById(R.id.listViewLoja);

        Cursor cursor = daoProduto.selectProdutos();

        produtoCursorAdapter = new ProdutoCursorAdapter(viewInflate.getContext(),cursor);

        listView.setAdapter(produtoCursorAdapter);

        txtPesquisaProduto = viewInflate.findViewById(R.id.txtPesquisaProduto);

        txtPesquisaProduto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                populaListView(txtPesquisaProduto.getText().toString());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                cursor.moveToPosition(i);

                Produto produto = new Produto();

                produto.setCod_barra(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
                produto.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                produto.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

                //TODO: Criar tela de alterar e deletar loja
            }
        });

        return viewInflate;
    }

    /**
     * Popula a lista novamente
     */
    public static void populaListView() {
        // Switch to new cursor and update contents of ListView
        Cursor cursor = daoProduto.selectProdutos();
        produtoCursorAdapter.changeCursor(cursor);
    }

    public static void populaListView(String termo) {
        // Switch to new cursor and update contents of ListView
        Cursor cursor = null;

        switch(TIPO_PESQUISA) {
            case 1:
                cursor = daoProduto.selectProdutosCodBarra(termo);
                break;
            case 2:
                cursor = daoProduto.selectProdutosDescricao(termo);
                break;
            case 3:
                cursor = daoProduto.selectProdutosFornecedor(termo);
                break;
        }

        produtoCursorAdapter.changeCursor(cursor);
    }
}
