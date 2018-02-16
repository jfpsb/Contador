package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOContagemProduto;
import com.vandamodaintima.jfpsb.contador.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem_Produto;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

public class AdicionarContagemProduto extends AppCompatActivity {

    private ConexaoBanco conn;
    DAOProduto daoProduto;
    DAOContagemProduto daoContagemProduto;
    private EditText txtCodBarra;
    private EditText txtFornecedor;
    private EditText txtPreco;
    private EditText txtQuantidade;
    private EditText txtDescricao;
    private Button btnAdicionar;

    private Produto produto;
    private Contagem contagem;
    private Contagem_Produto contagem_produto = new Contagem_Produto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_adicionar_contagem_produto);
        stub.inflate();

        conn = new ConexaoBanco(getApplicationContext());

        daoContagemProduto = new DAOContagemProduto(conn.conexao());
        daoProduto = new DAOProduto(conn.conexao());

        contagem = (Contagem) getIntent().getExtras().getSerializable("contagem");
        produto = (Produto) getIntent().getExtras().getSerializable("produto");
        String nomeFornecedor = getIntent().getExtras().getString("fornecedor");

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtCodBarra = findViewById(R.id.txtCodBarra);
        txtFornecedor = findViewById(R.id.txtFornecedor);
        txtPreco = findViewById(R.id.txtPreco);
        txtQuantidade = findViewById(R.id.txtQuantidade);
        txtDescricao = findViewById(R.id.txtDescricao);
        btnAdicionar = findViewById(R.id.btnAdicionar);

        txtCodBarra.setText(String.valueOf(produto.getCod_barra()));
        txtFornecedor.setText(nomeFornecedor);
        txtPreco.setText(String.valueOf(produto.getPreco()));
        txtDescricao.setText(produto.getDescricao());

        setBtnAdicionar();
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

    private void setBtnAdicionar() {
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String quant = txtQuantidade.getText().toString();

                    if(TestaIO.isStringEmpty(quant))
                        throw new Exception("O campo de quantidade não pode estar vazio!");

                    if(!TestaIO.isValidInt(quant))
                        throw new Exception("O valor em quantidade é inválido!");

                    contagem_produto.setContagem(contagem.getIdcontagem());
                    contagem_produto.setProduto(produto.getCod_barra());
                    contagem_produto.setQuant(Integer.parseInt(quant));

                    long result = daoContagemProduto.inserir(contagem_produto);

                    if(result != -1) {
                        Toast.makeText(AdicionarContagemProduto.this, "Contagem de produto inserida com sucesso!", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                    else {
                        Toast.makeText(AdicionarContagemProduto.this, "Erro ao inserir contagem de produto!", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e) {
                    Toast.makeText(AdicionarContagemProduto.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
