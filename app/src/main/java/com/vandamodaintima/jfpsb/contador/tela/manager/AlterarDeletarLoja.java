package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

public class AlterarDeletarLoja extends AlterarDeletarEntidade {

    private EditText txtIDLoja;
    private EditText txtNome;
    private Loja loja;
    private DAOLoja daoLoja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_alterar_deletar_loja);
        stub.inflate();

        setDAOs();

        loja = (Loja) getIntent().getExtras().getSerializable("loja");

        txtIDLoja = findViewById(R.id.txtIDLoja);
        txtNome = findViewById(R.id.txtNome);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnDeletar = findViewById(R.id.btnDeletar);

        txtIDLoja.setText(String.valueOf(loja.getIdloja()));
        txtNome.setText(loja.getNome());

        setAlertBuilder(loja);

        setBtnAtualizar();

        setBtnDeletar();
    }

    @Override
    protected void setDAOs() {
        daoLoja = new DAOLoja(conn.conexao());
    }

    @Override
    protected void setAlertBuilder(final Object loja) {
        final Loja objLoja = (Loja) loja;

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Deletar Loja");
        builder.setMessage("Tem certeza que deseja apagar a loja " + objLoja.getNome() + "?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                daoLoja.deletar(objLoja.getIdloja());

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

    @Override
    protected void setBtnAtualizar() {
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String nome = txtNome.getText().toString();

                    if(TestaIO.isStringEmpty(nome))
                        throw new Exception("O campo nome não pode estar vazio!");

                    loja.setNome(nome.toUpperCase());

                    int result = daoLoja.atualizar(loja);

                    if(result != -1) {
                        Toast.makeText(AlterarDeletarLoja.this, "A loja " + loja.getNome() + " foi atualizada com sucesso!", Toast.LENGTH_SHORT).show();

                        PesquisarLoja.populaListView();
                    }
                } catch (Exception e) {
                    Toast.makeText(AlterarDeletarLoja.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void setBtnDeletar() {
        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

}
