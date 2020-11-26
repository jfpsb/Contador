package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutBaseView;

public class AdicionarContagemProduto extends TabLayoutBaseView {

    private ConexaoBanco conexaoBanco;

    private TelaLerCodigoDeBarraContagemProduto telaLerCodigo;
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

        Bundle bundle = new Bundle();
        bundle.putString("loja", loja);
        bundle.putString("data", data);

        telaLerCodigo = new TelaLerCodigoDeBarraContagemProduto();
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

        if (id == R.id.itemHabilitarCampoQuant) {
            if (item.isChecked()) {
                item.setChecked(false);
            } else {
                item.setChecked(true);
            }
            telaLerCodigo.setCampoQuantidadeMarcado(item.isChecked());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ConexaoBanco getConexaoBanco() {
        return conexaoBanco;
    }

    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        conexaoBanco.close();
        super.onDestroy();
    }
}
