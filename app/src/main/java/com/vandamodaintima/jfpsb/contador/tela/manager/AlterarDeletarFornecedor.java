package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

public class AlterarDeletarFornecedor extends AlterarDeletarEntidade {

    private EditText txtCnpj;
    private EditText txtNome;
    private Fornecedor fornecedor;
    private AlertDialog.Builder builder;
    private DAOFornecedor daoFornecedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_alterar_deletar_fornecedor);
        stub.inflate();

        fornecedor = (Fornecedor) getIntent().getExtras().getSerializable("fornecedor");

        txtCnpj = findViewById(R.id.txtCnpj);
        txtNome = findViewById(R.id.txtNome);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnDeletar = findViewById(R.id.btnDeletar);

        txtCnpj.setText(fornecedor.getCnpj());
        txtNome.setText(fornecedor.getNome());

        daoFornecedor = new DAOFornecedor(conn.conexao());

        setAlertBuilder(fornecedor.getCnpj());

        setBtnAtualizar();

        setBtnDeletar();
    }

    @Override
    protected void setAlertBuilder(final Object idfornecedor) {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Deletar Fornecedor");
        builder.setMessage("Tem certeza que deseja apagar o fornecedor de CNPJ " + idfornecedor + "?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                daoFornecedor.deletar((String) idfornecedor);

                PesquisarFornecedor.populaListView();

                finish();
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AlterarDeletarFornecedor.this, "Fornecedor não foi deletado", Toast.LENGTH_SHORT).show();
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
                        throw new Exception("O campo de nome não pode ficar vazio!");

                    fornecedor.setNome(nome);

                    int result = daoFornecedor.atualizar(fornecedor);

                    if(result != -1) {
                        Toast.makeText(AlterarDeletarFornecedor.this, "O fornecedor de CNPJ " + fornecedor.getCnpj() + " foi atualizado com sucesso!", Toast.LENGTH_SHORT).show();

                        PesquisarFornecedor.populaListView();
                    }
                } catch (Exception e) {
                    Toast.makeText(AlterarDeletarFornecedor.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
