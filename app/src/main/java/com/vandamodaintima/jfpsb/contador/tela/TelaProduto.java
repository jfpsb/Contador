package com.vandamodaintima.jfpsb.contador.tela;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class TelaProduto extends ActivityBase {

    private CadastrarProduto cadastrarProduto;
    private PesquisarProduto pesquisarProduto;

    private AlphaAnimation inAnimation;
    private AlphaAnimation outAnimation;
    private LinearLayout progressBarHolder;

    private static TextView txtProgressStatus;

    String myLog = "Contador";

    private static final int EscolherArquivo = 1;

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
                startActivityForResult(Intent.createChooser(intent, "Selecione o Arquivo Excel"), EscolherArquivo);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EscolherArquivo) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Log.i("Contador", "Path: " + uri.getPath());
                Log.i("Contador", "Autoridade:" + uri.getAuthority());
                Log.i("Contador", "Environment.getExternalStorageDirectory(): " + Environment.getExternalStorageDirectory().getPath());
                new Tarefa(uri).execute();
            }
        }
    }

    private class Tarefa extends AsyncTask<Void, Void,Void> {

        private Uri filepath;

        public Tarefa(Uri filepath) {
            this.filepath = filepath;
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(800);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ManipulaExcel.adicionaProdutosDePlanilhaParaBD(TelaProduto.this, conn, filepath);
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                Log.i("Contador", e.getMessage());
            }
            return null;
        }
    }

    public static Runnable msgTxtProgressStatus(final String msg) {
        return new Runnable() {
            @Override
            public void run() {
                txtProgressStatus.setText(msg);
            }
        };
    }
}
