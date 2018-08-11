package com.vandamodaintima.jfpsb.contador.tela.manager.fornecedor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.tela.TabLayoutActivityBase;

/**
 * Pesquisa e cadastra fornecedor e ao final retorna o fornecedor cadastrado ou escolhido
 */
public class TelaFornecedorForResult extends TabLayoutActivityBase {
    private CadastrarFornecedor cadastrarFornecedor;
    private PesquisarFornecedor pesquisarFornecedor;

    private static final int CADASTRAR_SEM_INTERNET = 3;

    public TelaFornecedorForResult() {
        super(new String[]{"Pesquisar", "Cadastrar"});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_tela_fornecedor);
        stub.inflate();

        cadastrarFornecedor = new CadastrarFornecedorEmProduto();
        pesquisarFornecedor = new PesquisarFornecedorEmProduto();

        setViewPagerTabLayout(pesquisarFornecedor, cadastrarFornecedor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tela_fornecedor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.itemCadastrarFornecedorSemInternet:
                Intent intent = new Intent(this, CadastrarFornecedorSemInternetContainer.class);
                startActivityForResult(intent, CADASTRAR_SEM_INTERNET);
                return true;
            default:
                Toast.makeText(this, "Opção Indisponível Nesta Tela", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CADASTRAR_SEM_INTERNET:
                if (resultCode == Activity.RESULT_OK) {
                    Fornecedor fornecedor = (Fornecedor) data.getSerializableExtra("fornecedor");
                    setResultado(fornecedor);
                }
                break;
        }
    }

    public void setResultado(Fornecedor fornecedor) {
        Intent intent = new Intent();
        intent.putExtra("fornecedor", fornecedor);
        setResult(RESULT_OK, intent);
        finish();
    }
}
