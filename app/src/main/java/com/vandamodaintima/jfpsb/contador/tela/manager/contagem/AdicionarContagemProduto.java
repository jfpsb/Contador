package com.vandamodaintima.jfpsb.contador.tela.manager.contagem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.manager.ContagemProdutoManager;
import com.vandamodaintima.jfpsb.contador.dao.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.TabLayoutActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.manager.fornecedor.TelaFornecedorForResult;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

import java.util.Date;

public class AdicionarContagemProduto extends ActivityBase {

    private ProdutoManager produtoManager;
    private ContagemProdutoManager contagemProdutoManager;
    private EditText txtCodBarra;
    private EditText txtFornecedor;
    private EditText txtQuantidade;
    private EditText txtDescricao;
    private Button btnAdicionar;
    private Button btnAlterarFornecedor;

    private Fornecedor fornecedor = new Fornecedor();
    private Produto produto;
    private Contagem contagem;
    private ContagemProduto contagem_produto = new ContagemProduto();
    private Boolean addFornecedorFlag = false;

    private static final int ALTERAR_FORNECEDOR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_adicionar_contagem_produto);
        stub.inflate();

        setManagers();

        contagem = (Contagem) getIntent().getExtras().getSerializable("contagem");
        produto = (Produto) getIntent().getExtras().getSerializable("produto");

        txtCodBarra = findViewById(R.id.txtCodBarra);
        txtFornecedor = findViewById(R.id.txtFornecedor);
        txtQuantidade = findViewById(R.id.txtQuantidade);
        txtDescricao = findViewById(R.id.txtDescricao);
        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnAlterarFornecedor = findViewById(R.id.btnEscolherFornecedor);

        txtCodBarra.setText(String.valueOf(produto.getCod_barra()));

        if(produto.getFornecedor() != null) {
            txtFornecedor.setText(produto.getFornecedor().getNome());
        } else {
            txtFornecedor.setText("Não Possui");
        }

        txtDescricao.setText(produto.getDescricao());

        setBtnAdicionar();
        setBtnAlterarFornecedor();
    }

    @Override
    protected void setManagers() {
        contagemProdutoManager = new ContagemProdutoManager(conn);
        produtoManager = new ProdutoManager(conn);
    }

    private void setBtnAdicionar() {
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String quant = txtQuantidade.getText().toString();

                    if(quant.isEmpty())
                        throw new Exception("O Campo de Quantidade Não Pode Estar Vazio!");

                    if(! TestaIO.isValidInt(quant))
                        throw new Exception("O Valor em Quantidade é Inválido!");

                    contagem_produto.setId(new Date().getTime());
                    contagem_produto.setContagem(contagem);
                    contagem_produto.setProduto(produto);
                    contagem_produto.setQuant(Integer.parseInt(quant));

                    boolean result = contagemProdutoManager.inserir(contagem_produto);

                    if(result) {
                        Toast.makeText(AdicionarContagemProduto.this, "Contagem de Produto Inserida com Sucesso!", Toast.LENGTH_SHORT).show();

                        if(addFornecedorFlag) {

                            produto.setFornecedor(fornecedor);

                            boolean resultProduto = produtoManager.atualizar(produto, produto.getCod_barra());

                            if(resultProduto) {
                                Toast.makeText(AdicionarContagemProduto.this, "Fornecedor de Produto Atualizado com Sucesso!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AdicionarContagemProduto.this, "Houve um Erro ao Atualizar Fornecedor de Produto", Toast.LENGTH_SHORT).show();
                            }
                        }

                        finish();
                    }
                    else {
                        Toast.makeText(AdicionarContagemProduto.this, "Erro ao Inserir Contagem!", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e) {
                    Toast.makeText(AdicionarContagemProduto.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setBtnAlterarFornecedor() {
        btnAlterarFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(produto.getFornecedor() == null) {
                    Intent intent = new Intent(AdicionarContagemProduto.this, TelaFornecedorForResult.class);
                    startActivityForResult(intent, ALTERAR_FORNECEDOR);
                }
                else {
                    Toast.makeText(AdicionarContagemProduto.this, "Somente Altere o Fornecedor Nesta Tela Se O Produto Não Possuir Um", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ALTERAR_FORNECEDOR:
                if(resultCode == RESULT_OK) {
                    fornecedor = (Fornecedor) data.getSerializableExtra("fornecedor");

                    if(fornecedor != null) {
                        txtFornecedor.setText(fornecedor.getNome());
                        addFornecedorFlag = true;
                        Toast.makeText(this, "Fornecedor Escolhido. Dados Serão Salvos ao Adicionar Contagem de Produto", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(this, "O Fornecedor Não Foi Escolhido", Toast.LENGTH_SHORT).show();
                    fornecedor = null;
                    addFornecedorFlag = false;
                }
                break;
        }
    }
}
