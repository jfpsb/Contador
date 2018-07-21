package com.vandamodaintima.jfpsb.contador.tela;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.excel.ManipulaExcel;
import com.vandamodaintima.jfpsb.contador.tela.manager.CadastrarProduto;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.tela.manager.PesquisarProduto;

public class TelaProduto extends ActivityBase {

    private CadastrarProduto cadastrarProduto;
    private PesquisarProduto pesquisarProduto;

    private AlphaAnimation inAnimation;
    private AlphaAnimation outAnimation;
    private LinearLayout progressBarHolder;

    private static TextView txtProgressStatus;

    String myLog = "Contador";

    private static final int ESCOLHER_ARQUIVO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_tela_produto);
        stub.inflate();

        cadastrarProduto = new CadastrarProduto();
        pesquisarProduto = new PesquisarProduto();

        progressBarHolder = findViewById(R.id.progressBarHolder);
        txtProgressStatus = findViewById(R.id.txtProgressStatus);

        setViewPagerTabLayout(pesquisarProduto, cadastrarProduto);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastrar_produto_batch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.itemCadastrarProdutosBatch:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                startActivityForResult(Intent.createChooser(intent, "Selecione o Arquivo Excel"), ESCOLHER_ARQUIVO);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ESCOLHER_ARQUIVO) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                new Tarefa(uri).execute();
            }
        }
    }

    public class Tarefa extends AsyncTask<Void, String, String> {
        public class Progresso {
            private Tarefa tarefa;

            public Progresso(Tarefa tarefa) {
                this.tarefa = tarefa;
            }

            public void publish(String mensagem) {
                tarefa.publishProgress(mensagem);
            }
        }
        private Uri uri;
        private Progresso progresso = new Progresso(this);

        public Tarefa(Uri uri) {
            this.uri = uri;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(300);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            txtProgressStatus.setText(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(500);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);

            Toast.makeText(TelaProduto.this, result, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            ManipulaExcel manipulaExcel = new ManipulaExcel(this, conn);

            int result = manipulaExcel.ImportaProduto(getContentResolver(), uri, progresso);

            if(result == 0) {
                return "Nem Um Produto Foi Cadastrado. Talvez Todos Já Estejam No Banco de Dados.";
            }
            else if(result > 0) {
                return result + " Produtos Cadastrados com Sucesso!";
            }

            return "Houve um Erro ao Cadastrar Produtos. Contate o Suporte se Problema Persistir";
        }
    }
}
