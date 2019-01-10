package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.contagem.AdicionarContagemProdutoController;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.Produto;
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

    private SQLiteDatabase sqLiteDatabase;
    private AdicionarContagemProdutoController adicionarContagemProdutoController;

    private Contagem contagem;
    private Produto produto;
    private String cod_barra = null;

    private static final int TELA_SELECIONAR_PRODUTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_adicionar_contagem_produto);
        stub.inflate();

        contagem = (Contagem) getIntent().getExtras().getSerializable("contagem");

        txtCodBarra = findViewById(R.id.txtCodigoBarra);
        listViewContagemProduto = findViewById(R.id.listViewAdicionarContagem);

        setProdutoNaoEncontradoDialog();
        setEscolhaProdutoDialog();
        setDeletarContagemProdutoDialog();

        sqLiteDatabase = new ConexaoBanco(getApplicationContext()).conexao();
        adicionarContagemProdutoController = new AdicionarContagemProdutoController(this, sqLiteDatabase, getApplicationContext());

        realizarPesquisa();

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
                    adicionarContagemProdutoController.pesquisarProduto(cod_barra);
                    return true;
                }

                return false;
            }
        });

        txtCodBarra.setShowSoftInputOnFocus(false);
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
        adicionarContagemProdutoController.pesquisar(contagem);
    }

    @Override
    public void limparCampos() {
        txtCodBarra.getText().clear();
    }

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

        long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));

        ContagemProduto contagemProduto = adicionarContagemProdutoController.retornarContagemProduto(String.valueOf(id));

        if (contagemProduto != null) {
            abrirDeletarContagemProdutoDialog(contagemProduto);
        }
    }

    private void abrirTelaProdutoForResult() {
        Intent intent = new Intent(this, TelaProdutoForContagemForResult.class);
        intent.putExtra("codigo", cod_barra);
        startActivityForResult(intent, TELA_SELECIONAR_PRODUTO);
    }

    @Override
    public void abrirTelaEscolhaProdutoDialog(ListAdapter adapter) {
        escolhaProdutoDialog.setSingleChoiceItems(adapter, 0, null);
        escolhaProdutoDialog.show();
    }

    @Override
    public void retornarProdutoEncontrado(Produto produto) {
        ContagemProduto contagemProduto = new ContagemProduto();

        contagemProduto.setId(new Date().getTime());
        contagemProduto.setProduto(produto);
        contagemProduto.setContagem(contagem);
        contagemProduto.setQuant(1);

        adicionarContagemProdutoController.cadastrar(contagemProduto);
    }

    private void setEscolhaProdutoDialog() {
        escolhaProdutoDialog = new AlertDialog.Builder(AdicionarContagemProduto.this);
        escolhaProdutoDialog.setTitle("Selecione Abaixo");

        escolhaProdutoDialog.setNegativeButton("O Produto Não Está Lista", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                produto = null;
                abrirTelaProdutoForResult();
            }
        });

        escolhaProdutoDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ListView lw = ((AlertDialog) dialogInterface).getListView();
                produto = (Produto) lw.getAdapter().getItem(lw.getCheckedItemPosition());

                ContagemProduto contagemProduto = new ContagemProduto();

                contagemProduto.setId(new Date().getTime());
                contagemProduto.setProduto(produto);
                contagemProduto.setContagem(contagem);
                contagemProduto.setQuant(1);

                adicionarContagemProdutoController.cadastrar(contagemProduto);
            }
        });

        escolhaProdutoDialog.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                produto = null;
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
                produto = null;
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

    private void abrirDeletarContagemProdutoDialog(final ContagemProduto contagemProduto) {
        deletarContagemProdutoDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adicionarContagemProdutoController.deletar(String.valueOf(contagemProduto.getId()));
            }
        });

        deletarContagemProdutoDialog.show();
    }

    @Override
    public void abreProdutoNaoEncontradoDialog() {
        produtoNaoEncontradoDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case TELA_SELECIONAR_PRODUTO:
                if (resultCode == RESULT_OK) {
                    produto = (Produto) data.getSerializableExtra("produto");
                    int quantidade = (int) data.getSerializableExtra("quantidade");

                    ContagemProduto contagemProduto = new ContagemProduto();

                    contagemProduto.setId(new Date().getTime());
                    contagemProduto.setProduto(produto);
                    contagemProduto.setContagem(contagem);
                    contagemProduto.setQuant(quantidade);

                    adicionarContagemProdutoController.cadastrar(contagemProduto);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
