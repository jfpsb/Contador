package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.ProdutoCursorAdapter;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.dao.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;

public class PesquisaContagemProduto extends ActivityBase {

    private RadioGroup radioGroup;
    private EditText txtPesquisaProduto;
    private static TextView txtQuantProdutosCadastrados;
    private static ProdutoManager produtoManager;
    private ListView listView;
    private static ProdutoCursorAdapter produtoCursorAdapter;
    private static int TIPO_PESQUISA = 1;
    private Contagem contagem;
    private static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.fragment_pesquisar_produto);
        stub.inflate();

        contagem = (Contagem) getIntent().getExtras().getSerializable("contagem");

        radioGroup = findViewById(R.id.radioGroupOpcao);

        txtPesquisaProduto = findViewById(R.id.txtPesquisaProduto);
        txtQuantProdutosCadastrados = findViewById(R.id.txtQuantProdutosCadastrados);

        context = getApplicationContext();

        setManagers();

        setTxtPesquisaProduto();

        setRadioGroup();

        setListView();
    }

    @Override
    protected void setManagers() {
        produtoManager = new ProdutoManager(conn);
    }

    private void setListViewOnItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                cursor.moveToPosition(i);

                // Já que produto possui um Fornecedor em recupero logo do banco pq o Fornecedor já vem iniciado se existir
                Produto produto = produtoManager.listarPorChave(cursor.getString(cursor.getColumnIndexOrThrow("_id")));

                Bundle bundle = new Bundle();

                bundle.putSerializable("produto", produto);
                bundle.putSerializable("contagem", contagem);

                Intent adicionarProduto = new Intent(getApplicationContext(), AdicionarContagemProduto.class);

                adicionarProduto.putExtras(bundle);
                startActivity(adicionarProduto);
            }
        });
    }

    /**
     * Popula a lista novamente
     */
    public static void populaListView() {
        if(cursorLista != null)
            cursorLista.close();

        try {
            cursorLista = produtoManager.listarCursor();

            txtQuantProdutosCadastrados.setText(String.valueOf(cursorLista.getCount()));

            produtoCursorAdapter.changeCursor(cursorLista);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void populaListView(String termo) {
        if(cursorLista != null)
            cursorLista.close();

        try {
            switch (TIPO_PESQUISA) {
                case 1:
                    cursorLista = produtoManager.listarCursorPorCodBarra(termo);
                    break;
                case 2:
                    cursorLista = produtoManager.listarCursorPorDescricao(termo);
                    break;
                case 3:
                    cursorLista = produtoManager.listarCursorPorFornecedor(termo);
                    break;
            }

            txtQuantProdutosCadastrados.setText(String.valueOf(cursorLista.getCount()));

            produtoCursorAdapter.changeCursor(cursorLista);
        } catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setListView() {
        listView = findViewById(R.id.listViewProduto);

        if(cursorLista != null)
            cursorLista.close();

        try {
            cursorLista = produtoManager.listarCursor();

            txtQuantProdutosCadastrados.setText(String.valueOf(cursorLista.getCount()));

            produtoCursorAdapter = new ProdutoCursorAdapter(getApplicationContext(), cursorLista);

            listView.setAdapter(produtoCursorAdapter);

            setListViewOnItemClickListener();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setRadioGroup() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selecao = radioGroup.getCheckedRadioButtonId();

                switch (selecao) {
                    case (R.id.rbCodBarra):
                        txtPesquisaProduto.setHint(R.string.hintRadioButtonCodBarra);
                        txtPesquisaProduto.setInputType(InputType.TYPE_CLASS_NUMBER);
                        TIPO_PESQUISA = 1;
                        break;
                    case (R.id.rbDescricao):
                        txtPesquisaProduto.setHint(R.string.hintRadioButtonDescricao);
                        txtPesquisaProduto.setInputType(InputType.TYPE_CLASS_TEXT);
                        TIPO_PESQUISA = 2;
                        break;
                    case (R.id.rbFornecedor):
                        txtPesquisaProduto.setHint(R.string.hintRadioButtonNomeFornecedor);
                        txtPesquisaProduto.setInputType(InputType.TYPE_CLASS_TEXT);
                        TIPO_PESQUISA = 3;
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Aconteceu algo de errado", Toast.LENGTH_SHORT).show();
                        txtPesquisaProduto.setInputType(InputType.TYPE_CLASS_NUMBER);
                        TIPO_PESQUISA = 1;
                        break;
                }
            }
        });
    }

    private void setTxtPesquisaProduto(){
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
    }
}
