package com.vandamodaintima.jfpsb.contador.view.fornecedor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.controller.fornecedor.TelaFornecedorController;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutActivityBase;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

public class TelaFornecedor extends TabLayoutActivityBase {

    protected CadastrarFornecedor cadastrarFornecedor;
    protected PesquisarFornecedor pesquisarFornecedor;

    private TelaFornecedorController telaFornecedorController;

    private static final int ESCOLHER_ARQUIVO = 1;
    private static final int ESCOLHER_DIRETORIO = 2;
    private static final int PEDIDO_PERMISSAO_READ = 3;
    private static final int CADASTRAR_MANUALMENTE = 4;

    public TelaFornecedor() {
        super(new String[]{"Pesquisar", "Cadastrar"});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_tela_tablayout);
        stub.inflate();

        setFragments();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        setViewPagerTabLayout(pesquisarFornecedor, cadastrarFornecedor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tela_fornecedor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.itemCadastrarFornecedorManualmente:
                Intent intent = new Intent(this, CadastrarFornecedorManualmente.class);
                startActivityForResult(intent, CADASTRAR_MANUALMENTE);
                return true;
            case R.id.itemExportarFornecedorExcel:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
                newDirectoryName("Contador - Fornecedor Em Excel").allowReadOnlyDirectory(true).
                build();

        intentDiretorio.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);
        startActivityForResult(intentDiretorio, ESCOLHER_DIRETORIO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ESCOLHER_DIRETORIO:
                if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                    String diretorio = data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR);

//                    ManipulaExcel manipulaExcel = new ManipulaExcel(conn);

//                    boolean result = manipulaExcel.ExportaFornecedor(diretorio);

//                    if (result) {
//                        Toast.makeText(this, "Arquivo Exportado Com Sucesso", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(this, "Erro Ao Exportar Arquivo", Toast.LENGTH_SHORT).show();
//                    }
                }
                break;

            case CADASTRAR_MANUALMENTE:
                pesquisarFornecedor.realizarPesquisa();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setFragments() {
        cadastrarFornecedor = new CadastrarFornecedor();
        pesquisarFornecedor = new PesquisarFornecedor();
    }

    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    public class ExportarFornecedorEmExcel extends AsyncTask<String, String, Void> {

        public class Progresso {
            private ExportarFornecedorEmExcel exportarFornecedorEmExcel;

            public Progresso(ExportarFornecedorEmExcel exportarFornecedorEmExcel) {
                this.exportarFornecedorEmExcel = exportarFornecedorEmExcel;
            }

            public void publish(String mensagem) {
                exportarFornecedorEmExcel.publishProgress(mensagem);
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
            //telaFornecedorController.exportarProdutosEmExcel(diretorio, progresso);
            return null;
        }
    }
}
