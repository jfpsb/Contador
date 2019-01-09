package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.content.DialogInterface;
import android.database.Cursor;
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

        adicionarContagemProdutoController.pesquisar(contagem);

        txtCodBarra.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_ENTER) {
                    String cod_barra = txtCodBarra.getText().toString().replace("\n", "");
                    adicionarContagemProdutoController.pesquisarProduto(cod_barra);
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setListViewAdapter(ListAdapter adapter) {
        listViewContagemProduto.setAdapter(adapter);
    }

    @Override
    public void realizarPesquisa() {

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
            abreDeletarContagemProdutoDialog(contagemProduto);
        }
    }

    @Override
    public void abrirTelaProdutoForResult() {

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

    public void setProdutoNaoEncontradoDialog() {
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

    private void abreDeletarContagemProdutoDialog(final ContagemProduto contagemProduto) {
        deletarContagemProdutoDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adicionarContagemProdutoController.deletar(String.valueOf(contagemProduto.getId()));
            }
        });

        deletarContagemProdutoDialog.show();
    }
}
