package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.contagem.AdicionarContagemProdutoController;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutBaseView;
import com.vandamodaintima.jfpsb.contador.view.interfaces.IAdicionarContagemProduto;
import com.vandamodaintima.jfpsb.contador.view.produto.TelaProdutoForContagemForResult;

public class AdicionarContagemProduto extends TabLayoutBaseView implements IAdicionarContagemProduto {

    private ConexaoBanco conexaoBanco;
    private AdicionarContagemProdutoController controller;

    private static final int TELA_SELECIONAR_PRODUTO = 1;

    private TelaLerCodigoDeBarra telaLerCodigo;
    private TelaVerProdutoContado telaVerProdutoContado;

    public AdicionarContagemProduto() {
        super(new String[]{"Ler Cód. De Barras", "Produtos Contados"});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_tela_tablayout);
        stub.inflate();

        String loja = getIntent().getStringExtra("loja");
        String data = getIntent().getStringExtra("data");

        conexaoBanco = new ConexaoBanco(getApplicationContext());
        controller = new AdicionarContagemProdutoController(this, conexaoBanco);
        controller.carregaContagem(loja, data);

        Bundle bundle = new Bundle();
        bundle.putString("loja", loja);
        bundle.putString("data", data);

        telaLerCodigo = new TelaLerCodigoDeBarra();
        telaVerProdutoContado = new TelaVerProdutoContado();

        telaLerCodigo.setArguments(bundle);
        telaVerProdutoContado.setArguments(bundle);

        setViewPagerTabLayout(telaLerCodigo, telaVerProdutoContado);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tela_adicionar_contagem, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.itemAdicionarManualmente) {
            abrirTelaProdutoForResult();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void abrirTelaProdutoForResult(String... codigo) {
        Intent intent = new Intent(this, TelaProdutoForContagemForResult.class);
        if (codigo.length > 0)
            intent.putExtra("codigo", codigo[0]);
        startActivityForResult(intent, TELA_SELECIONAR_PRODUTO);
    }

    @Override
    public ConexaoBanco getConexaoBanco() {
        return conexaoBanco;
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TELA_SELECIONAR_PRODUTO) {
            if (resultCode == RESULT_OK) {
                Produto produto = (Produto) data.getSerializableExtra("produto");
                controller.carregaProduto(produto);
                int quantidade = data.getIntExtra("quantidade", 1);
                controller.cadastrar(quantidade);
                telaVerProdutoContado.realizarPesquisa();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        conexaoBanco.close();
        super.onDestroy();
    }
}
