package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

public class AlterarDeletarLoja extends AppCompatActivity {

    private ConexaoBanco conn;
    private EditText txtIDLoja;
    private EditText txtNome;
    private Loja loja;
    private AlertDialog.Builder builder;
    private Button btnAtualizar;
    private Button btnDeletar;
    private DAOLoja daoLoja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_alterar_deletar_loja);
        stub.inflate();

        loja = (Loja) getIntent().getExtras().getSerializable("loja");

        txtIDLoja = findViewById(R.id.txtIDLoja);
        txtNome = findViewById(R.id.txtNome);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnDeletar = findViewById(R.id.btnDeletar);

        txtIDLoja.setText(String.valueOf(loja.getIdloja()));
        txtNome.setText(loja.getNome());

        setAlertBuilder(loja);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        conn = new ConexaoBanco(getApplicationContext());

        daoLoja = new DAOLoja(conn.conexao());

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String nome = txtNome.getText().toString();

                    if(TestaIO.isStringEmpty(nome))
                        throw new Exception("O campo nome não pode estar vazio!");

                    loja.setNome(nome);

                    int result = daoLoja.atualizar(loja);

                    if(result != -1) {
                        Toast.makeText(AlterarDeletarLoja.this, "A loja " + loja.getNome() + " foi adicionada com sucesso!", Toast.LENGTH_SHORT).show();

                        PesquisarLoja.populaListView();
                    }
                } catch (Exception e) {
                    Toast.makeText(AlterarDeletarLoja.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDestroy() {
        conn.fechar();
        super.onDestroy();
    }

    private void setAlertBuilder(final Loja loja) {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Deletar Loja");
        builder.setMessage("Tem certeza que deseja apagar a loja " + loja.getNome() + "?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                daoLoja.deletar(loja.getIdloja());

                PesquisarLoja.populaListView();

                finish();
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AlterarDeletarLoja.this, "Loja não foi deletada", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
