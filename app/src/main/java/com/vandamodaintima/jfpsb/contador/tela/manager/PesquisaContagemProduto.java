package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.ProdutoCursorAdapter;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;

public class PesquisaContagemProduto extends AppCompatActivity {

    private RadioGroup radioGroup;
    private EditText txtPesquisaProduto;
    private ConexaoBanco conn;
    private static DAOProduto daoProduto;
    private ListView listView;
    private static ProdutoCursorAdapter produtoCursorAdapter;
    private static int TIPO_PESQUISA = 1;
    private Contagem contagem;
    private static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.fragment_pesquisar_produto);
        stub.inflate();

        conn = new ConexaoBanco(getApplicationContext());

        contagem = (Contagem) getIntent().getExtras().getSerializable("contagem");

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        daoProduto = new DAOProduto(conn.conexao());

        radioGroup = findViewById(R.id.radioGroupOpcao);

        txtPesquisaProduto = findViewById(R.id.txtPesquisaProduto);

        context = getApplicationContext();

        setTxtPesquisaProduto();

        setRadioGroup();

        setListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_visualizar_contagem, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.itemVerProdutos:
                Intent visualizarContagem = new Intent(PesquisaContagemProduto.this, VisualizarProdutosContagem.class);

                Bundle bundle = new Bundle();

                bundle.putSerializable("contagem", contagem);

                visualizarContagem.putExtras(bundle);

                visualizarContagem.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                startActivity(visualizarContagem);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setListViewOnItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                cursor.moveToPosition(i);

                Produto produto = new Produto();

                produto.setCod_barra(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
                produto.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                produto.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));
                produto.setFornecedor(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
                String nomeFornecedor = cursor.getString(cursor.getColumnIndexOrThrow("nome"));

                Bundle bundle = new Bundle();

                bundle.putSerializable("produto", produto);
                bundle.putSerializable("contagem", contagem);
                bundle.putString("fornecedor", nomeFornecedor);

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
        // Switch to new cursor and update contents of ListView
        Toast.makeText(context, "Pesquisando todos os produtos", Toast.LENGTH_SHORT).show();
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

    private void setListView() {
        listView = findViewById(R.id.listViewLoja);

        Cursor cursor = daoProduto.selectProdutos();

        produtoCursorAdapter = new ProdutoCursorAdapter(getApplicationContext(),cursor);

        listView.setAdapter(produtoCursorAdapter);

        setListViewOnItemClickListener();
    }

    private void setRadioGroup() {
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
                        Toast.makeText(getApplicationContext(), "Aconteceu algo de errado", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDestroy() {
        conn.fechar();
        super.onDestroy();
    }
}
