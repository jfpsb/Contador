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
    private EditText txtId;
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

        txtId = findViewById(R.id.txtIDFornecedor);
        txtCnpj = findViewById(R.id.txtCnpj);
        txtNome = findViewById(R.id.txtNome);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnDeletar = findViewById(R.id.btnDeletar);

        txtId.setText(Integer.toString(fornecedor.getId()));
        txtCnpj.setText(fornecedor.getCnpj());
        txtNome.setText(fornecedor.getNome());

        setDAOs();

        setAlertBuilder(fornecedor);

        setBtnAtualizar();

        setBtnDeletar();
    }

    @Override
    protected void setDAOs() {
        daoFornecedor = new DAOFornecedor(conn.conexao());
    }

    @Override
    protected void setAlertBuilder(final Object entidade) {

        final Fornecedor fornecedor = (Fornecedor)entidade;

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Deletar Fornecedor");
        builder.setMessage("Tem certeza que deseja apagar o fornecedor de CNPJ " + fornecedor.getCnpj() + "?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                daoFornecedor.deletar(fornecedor);

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
                    String cnpj = txtCnpj.getText().toString();
                    String nome = txtNome.getText().toString();

                    if(TestaIO.isStringEmpty(cnpj))
                        throw new Exception("O campo de cnpj não pode ficar vazio!");

                    if(TestaIO.isStringEmpty(nome))
                        throw new Exception("O campo de nome não pode ficar vazio!");

                    fornecedor.setCnpj(cnpj);
                    fornecedor.setNome(nome.toUpperCase());

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
