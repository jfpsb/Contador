package com.vandamodaintima.jfpsb.contador.tela.manager.produto;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.excel.ManipulaExcel;
import com.vandamodaintima.jfpsb.contador.tela.TabLayoutActivityBase;
import com.vandamodaintima.jfpsb.contador.R;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

public class TelaProduto extends TabLayoutActivityBase {

    private CadastrarProduto cadastrarProduto;
    private PesquisarProduto pesquisarProduto;

    private AlphaAnimation inAnimation;
    private AlphaAnimation outAnimation;
    private LinearLayout progressBarHolder;

    private static TextView txtProgressStatus;

    private static final int ESCOLHER_ARQUIVO = 1;
    private static final int ESCOLHER_DIRETORIO = 2;
    private static final int PEDIDO_PERMISSAO_READ = 3;

    public TelaProduto() {
        super(new String[] { "Pesquisar", "Cadastrar" });
    }

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
        getMenuInflater().inflate(R.menu.menu_tela_produto, menu);
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
            case R.id.itemExportarFornecedorExcel:
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PEDIDO_PERMISSAO_READ);
                } else {
                    AbrirEscolhaDiretorioActivity();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PEDIDO_PERMISSAO_READ:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissão Concedida para Acessar Memória Interna", Toast.LENGTH_SHORT).show();
                    AbrirEscolhaDiretorioActivity();
                }
                break;
        }
    }

    private void AbrirEscolhaDiretorioActivity() {
        Intent intentDiretorio = new Intent(this, DirectoryChooserActivity.class);

        DirectoryChooserConfig config = DirectoryChooserConfig.builder().
                newDirectoryName("Contador - Produto Em Excel").allowReadOnlyDirectory(true).
                build();

        intentDiretorio.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);
        startActivityForResult(intentDiretorio, ESCOLHER_DIRETORIO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Se não chamar não executa o onActivityResult das fragments em TelaFornecedorForResult
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ESCOLHER_ARQUIVO:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    new Tarefa(uri).execute();
                }
                break;
            case ESCOLHER_DIRETORIO:
                if(resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                    String diretorio = data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR);

                    ManipulaExcel manipulaExcel = new ManipulaExcel(getConn());

                    boolean result = manipulaExcel.ExportaFornecedor(diretorio);

                    if(result) {
                        Toast.makeText(this, "Arquivo Exportado Com Sucesso", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, "Erro Ao Exportar Arquivo", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public class Tarefa extends AsyncTask<Void, String, Integer> {
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
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(500);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);

            if(result == 0) {
                Toast.makeText(TelaProduto.this, "Nem Um Produto Foi Cadastrado. Talvez Todos Já Estejam No Banco de Dados", Toast.LENGTH_SHORT).show();
            }
            else if(result > 0) {
                Toast.makeText(TelaProduto.this, result + " Produtos Cadastrados com Sucesso!", Toast.LENGTH_SHORT).show();
                pesquisarProduto.populaListView();
            }
            else {
                Toast.makeText(TelaProduto.this, "Houve um Erro ao Cadastrar Produtos. Contate o Suporte se Problema Persistir", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            ManipulaExcel manipulaExcel = new ManipulaExcel(this, conn);

            int result = manipulaExcel.ImportaProduto(getContentResolver(), uri, progresso);

            return result;
        }
    }
}
