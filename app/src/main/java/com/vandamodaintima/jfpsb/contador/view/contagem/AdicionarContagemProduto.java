package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.contagem.AdicionarContagemProdutoController;
import com.vandamodaintima.jfpsb.contador.model.ContagemModel;
import com.vandamodaintima.jfpsb.contador.model.ContagemProdutoModel;
import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AdicionarContagemProdutoView;
import com.vandamodaintima.jfpsb.contador.view.produto.TelaProdutoForContagemForResult;

import java.util.Date;

public class AdicionarContagemProduto extends ActivityBaseView implements AdicionarContagemProdutoView {

    private EditText txtCodBarra;
    private ListView listViewContagemProduto;
    private AlertDialog.Builder escolhaProdutoDialog;
    private AlertDialog.Builder produtoNaoEncontradoDialog;
    private AlertDialog.Builder deletarContagemProdutoDialog;
    private MediaPlayer mediaPlayer;

    private ConexaoBanco conexaoBanco;
    private AdicionarContagemProdutoController controller;

    private String cod_barra = null;

    private static final int TELA_SELECIONAR_PRODUTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_adicionar_contagem_produto);
        stub.inflate();

        txtCodBarra = findViewById(R.id.txtCodigoBarra);
        listViewContagemProduto = findViewById(R.id.listViewAdicionarContagem);

        conexaoBanco = new ConexaoBanco(getApplicationContext());
        controller = new AdicionarContagemProdutoController(this, conexaoBanco);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.buzzer);

        String loja = getIntent().getStringExtra("loja");
        String data = getIntent().getStringExtra("data");
        controller.carregaContagem(loja, data);

        setProdutoNaoEncontradoDialog();
        setEscolhaProdutoDialog();
        setDeletarContagemProdutoDialog();

        listViewContagemProduto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cliqueEmItemLista(adapterView, i);
            }
        });

        txtCodBarra.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_ENTER) {
                    cod_barra = txtCodBarra.getText().toString().replace("\n", "");
                    controller.pesquisarProduto(cod_barra);
                    return true;
                }

                return false;
            }
        });

        txtCodBarra.setShowSoftInputOnFocus(false);

        realizarPesquisa();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tela_adicionar_contagem, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.itemAdicionarManualmente:
                abrirTelaProdutoForResult();
                return true;
            case R.id.itemHabilitarCampo:
                if (item.isChecked()) {
                    item.setChecked(false);
                    txtCodBarra.setInputType(InputType.TYPE_NULL);
                    txtCodBarra.setLongClickable(false);
                    txtCodBarra.setHint(getString(R.string.hint_leia_cod_de_barra));
                    txtCodBarra.setShowSoftInputOnFocus(false);
                } else {
                    item.setChecked(true);
                    txtCodBarra.setInputType(InputType.TYPE_CLASS_TEXT);
                    txtCodBarra.setLongClickable(true);
                    txtCodBarra.setHint(getString(R.string.hint_leia_ou_escreva_cod_de_barra));
                    txtCodBarra.setShowSoftInputOnFocus(true);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setListViewAdapter(final ListAdapter adapter) {
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
    public void realizarPesquisa() {
        controller.pesquisar();
    }

    @Override
    public void limparCampos() {
        txtCodBarra.getText().clear();
    }

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
        long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
        controller.carregaContagemProduto(id);
        abrirDeletarContagemProdutoDialog();
    }

    private void abrirTelaProdutoForResult() {
        Intent intent = new Intent(this, TelaProdutoForContagemForResult.class);
        intent.putExtra("codigo", cod_barra);
        startActivityForResult(intent, TELA_SELECIONAR_PRODUTO);
    }

    @Override
    public void abrirTelaEscolhaProdutoDialog(ListAdapter adapter) {
        mediaPlayer.start();
        escolhaProdutoDialog.setSingleChoiceItems(adapter, 0, null);
        escolhaProdutoDialog.show();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    private void setEscolhaProdutoDialog() {
        escolhaProdutoDialog = new AlertDialog.Builder(AdicionarContagemProduto.this);
        escolhaProdutoDialog.setTitle("Selecione Abaixo");

        escolhaProdutoDialog.setNegativeButton("O Produto Não Está Lista", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                abrirTelaProdutoForResult();
            }
        });

        escolhaProdutoDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ListView lw = ((AlertDialog) dialogInterface).getListView();
                Object model = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                controller.carregaProduto(model);
                controller.cadastrar();
            }
        });

        escolhaProdutoDialog.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mensagemAoUsuario("Nenhuma Contagem De Produto Foi Adicionada");
            }
        });
    }

    private void setProdutoNaoEncontradoDialog() {
        produtoNaoEncontradoDialog = new AlertDialog.Builder(AdicionarContagemProduto.this);
        produtoNaoEncontradoDialog.setTitle("Produto Não Encontrado");
        produtoNaoEncontradoDialog.setMessage("Nenhum Produto Foi Encontrado Com o Código Informado. Deseja Pesquisar na Tela de Produtos ou Cadastrar Um Novo Produto?");

        produtoNaoEncontradoDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                abrirTelaProdutoForResult();
            }
        });

        produtoNaoEncontradoDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mensagemAoUsuario("Nenhuma Contagem De Produto Foi Adicionada");
            }
        });
    }

    private void setDeletarContagemProdutoDialog() {
        deletarContagemProdutoDialog = new AlertDialog.Builder(AdicionarContagemProduto.this);
        deletarContagemProdutoDialog.setTitle("Deletar Contagem de Produto");
        deletarContagemProdutoDialog.setMessage("Deletar Esta Contagem de Produto?");

        deletarContagemProdutoDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mensagemAoUsuario("A Contagem de Produto Não Foi Deletada");
            }
        });
    }

    private void abrirDeletarContagemProdutoDialog() {
        deletarContagemProdutoDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                controller.deletar();
            }
        });

        deletarContagemProdutoDialog.show();
    }

    @Override
    public void abreProdutoNaoEncontradoDialog() {
        mediaPlayer.start();
        produtoNaoEncontradoDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TELA_SELECIONAR_PRODUTO) {
            if (resultCode == RESULT_OK) {
                String id = data.getStringExtra("produto");
                controller.carregaProduto(id);
                int quantidade = (int) data.getSerializableExtra("quantidade");
                controller.cadastrar(quantidade);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
