package com.vandamodaintima.jfpsb.contador.view.produto;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
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
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.produto.TelaProdutoController;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutActivityBase;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

public class TelaProduto extends TabLayoutActivityBase {

    private SQLiteDatabase sqLiteDatabase;

    private CadastrarProduto cadastrarProduto;
    private PesquisarProduto pesquisarProduto;

    private AlphaAnimation inAnimation;
    private AlphaAnimation outAnimation;
    private LinearLayout progressBarHolder;

    private TextView txtProgressStatus;

    private TelaProdutoController telaProdutoController;

    private static final int ESCOLHER_ARQUIVO = 1;
    private static final int ESCOLHER_DIRETORIO = 2;
    private static final int PEDIDO_PERMISSAO_READ = 3;

    public TelaProduto() {
        super(new String[]{"Pesquisar", "Cadastrar"});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_tela_tablayout);
        stub.inflate();

        ViewStub stub2 = findViewById(R.id.telaLayoutViewStub);
        stub2.setLayoutResource(R.layout.activity_tela_produto);
        stub2.inflate();

        cadastrarProduto = new CadastrarProduto();
        pesquisarProduto = new PesquisarProduto();

        progressBarHolder = findViewById(R.id.progressBarHolder);
        txtProgressStatus = findViewById(R.id.txtProgressStatus);

        setViewPagerTabLayout(pesquisarProduto, cadastrarProduto);

        sqLiteDatabase = new ConexaoBanco(getApplicationContext()).conexao();
        telaProdutoController = new TelaProdutoController(this, sqLiteDatabase);
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
            case R.id.itemExportarProdutoExcel:
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
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissão Concedida para Acessar Memória Interna", Toast.LENGTH_SHORT).show();
                    AbrirEscolhaDiretorioActivity();
                }
                break;
        }
    }

    private void AbrirEscolhaDiretorioActivity() {
        Intent intentDiretorio = new Intent(this, DirectoryChooserActivity.class);

        DirectoryChooserConfig config = DirectoryChooserConfig.builder().
                newDirectoryName("Contador - Produtos Em Excel").allowReadOnlyDirectory(true).
                build();

        intentDiretorio.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);
        startActivityForResult(intentDiretorio, ESCOLHER_DIRETORIO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ESCOLHER_ARQUIVO:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    new ImportarProdutoAsyncTask(uri).execute();
                }
                break;
            case ESCOLHER_DIRETORIO:
                if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                    String diretorio = data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR);
                    new ExportarProdutoEmExcel().execute(diretorio);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    public class ImportarProdutoAsyncTask extends AsyncTask<Void, String, Boolean> {
        public class Progresso {
            private ImportarProdutoAsyncTask importarProdutoAsyncTask;

            public Progresso(ImportarProdutoAsyncTask tarefa) {
                this.importarProdutoAsyncTask = tarefa;
            }

            public void publish(String mensagem) {
                importarProdutoAsyncTask.publishProgress(mensagem);
            }
        }

        private Uri uri;
        private Progresso progresso = new Progresso(this);

        public ImportarProdutoAsyncTask(Uri uri) {
            this.uri = uri;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(300);
            inAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    progressBarHolder.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            progressBarHolder.setAnimation(inAnimation);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            txtProgressStatus.setText(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(5000);
            outAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    progressBarHolder.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            progressBarHolder.setAnimation(outAnimation);

            if (result) {
                mensagemAoUsuario("Cadastro de Produtos Por Excel Realiza Com Sucesso");
                pesquisarProduto.realizarPesquisa();
            } else {
                mensagemAoUsuario("Houve um Erro ao Cadastrar Produtos. Contate o Suporte se Problema Persistir");
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            telaProdutoController.setTask(this);
            return telaProdutoController.importarDeArquivoExcel(getContentResolver(), uri, progresso);
        }
    }

    public class ExportarProdutoEmExcel extends AsyncTask<String, String, Void> {

        public class Progresso {
            private ExportarProdutoEmExcel exportarProdutoEmExcel;

            public Progresso(ExportarProdutoEmExcel exportarProdutoEmExcel) {
                this.exportarProdutoEmExcel = exportarProdutoEmExcel;
            }

            public void publish(String mensagem) {
                exportarProdutoEmExcel.publishProgress(mensagem);
            }
        }

        private Progresso progresso = new Progresso(this);

        @Override
        protected void onProgressUpdate(String... values) {
            String mensagem = values[0];
            mensagemAoUsuario(mensagem);
        }

        @Override
        protected Void doInBackground(String... strings) {
            String diretorio = strings[0];
            telaProdutoController.exportarProdutosEmExcel(diretorio, progresso);
            return null;
        }
    }
}
