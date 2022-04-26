package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.contagem.InserirContagemProdutoController;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;
import com.vandamodaintima.jfpsb.contador.view.contagem.contagemproduto.AlterarDeletarContagemProduto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;
import com.vandamodaintima.jfpsb.contador.view.produto.TelaProdutoForContagemForResult;
import com.vandamodaintima.jfpsb.contador.view.produto.grade.TelaLerCodigoBarrasCadastrarProduto;

public class InserirContagemProduto extends ActivityBaseView implements CadastrarView {
    private ConexaoBanco conexaoBanco;

    private Button btnAbrirCamera;
    private Button btnAdicionarContagem;
    private ListView listViewContagemProduto;
    private InserirContagemProdutoController controller;

    private static final int TELA_SELECIONAR_PRODUTO = 1;
    private static final int TELA_LER_CODIGO_BARRAS = 2;
    private static final int ALTERAR_DELETAR_CONTAGEM_PRODUTO = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conexaoBanco = new ConexaoBanco(this);

        stub.setLayoutResource(R.layout.activity_inserir_contagem_produto);
        stub.inflate();

        btnAbrirCamera = findViewById(R.id.btnAbrirCamera);
        btnAdicionarContagem = findViewById(R.id.btnAdicionarContagem);
        listViewContagemProduto = findViewById(R.id.listViewContagemProduto);

        btnAbrirCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TelaLerCodigoBarrasCadastrarProduto.class);
                startActivityForResult(intent, TELA_LER_CODIGO_BARRAS);
            }
        });

        btnAdicionarContagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TelaProdutoForContagemForResult.class);
                startActivityForResult(intent, TELA_SELECIONAR_PRODUTO);
            }
        });

        listViewContagemProduto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContagemProduto contagemProduto = (ContagemProduto) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext(), AlterarDeletarContagemProduto.class);
                intent.putExtra("contagemproduto", contagemProduto.getId().toString());
                startActivityForResult(intent, ALTERAR_DELETAR_CONTAGEM_PRODUTO);
            }
        });

        String idContagem = getIntent().getStringExtra("contagem");
        controller = new InserirContagemProdutoController(this, conexaoBanco, idContagem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TELA_LER_CODIGO_BARRAS:
                break;
            case TELA_SELECIONAR_PRODUTO:
                break;
            case ALTERAR_DELETAR_CONTAGEM_PRODUTO:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (conexaoBanco != null)
            conexaoBanco.close();
        super.onDestroy();
    }

    @Override
    public void limparCampos() {

    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void aposCadastro(Object... args) {

    }

    @Override
    public void focoEmViewInicial() {

    }

    @Override
    public void setListViewAdapter(ListAdapter adapter) {
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                //Move para o último item da lista sempre que o adapter for modificado
                int lastIndex = adapter.getCount() - 1;
                listViewContagemProduto.smoothScrollToPosition(lastIndex);
            }
        });
        listViewContagemProduto.setAdapter(adapter);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
