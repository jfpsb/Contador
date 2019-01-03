package com.vandamodaintima.jfpsb.contador.view.fornecedor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.tela.manager.AlterarDeletarEntidade;

public class AlterarDeletarFornecedor extends AlterarDeletarEntidade<Fornecedor> {

    private EditText txtCnpj;
    private EditText txtNome;
    private EditText txtFantasia;
    private Fornecedor fornecedor;
//    private FornecedorManager fornecedorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null)
            savedInstanceState = new Bundle();

        savedInstanceState.putInt("layout", R.layout.content_alterar_deletar_fornecedor);

        fornecedor = (Fornecedor) getIntent().getExtras().getSerializable("fornecedor");

        savedInstanceState.putString("entidade", "fornecedor");
        savedInstanceState.putSerializable("fornecedor", fornecedor);

        super.onCreate(savedInstanceState);

        txtCnpj = findViewById(R.id.txtCnpj);
        txtNome = findViewById(R.id.txtNome);
        txtFantasia = findViewById(R.id.txtFantasia);

        txtCnpj.setText(fornecedor.getCnpj());
        txtNome.setText(fornecedor.getNome());

        if (!fornecedor.getFantasia().isEmpty()) {
            txtFantasia.setText(fornecedor.getFantasia());
        }
    }

    @Override
    protected void setManagers() {
//        fornecedorManager = new FornecedorManager(conn);
    }

    @Override
    protected void setAlertBuilderAtualizar(final Fornecedor entidade) {
        alertBuilderAtualizar = new AlertDialog.Builder(this);
        alertBuilderAtualizar.setTitle("Atualizar Fornecedor");
        alertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Atualizar o Fornecedor " + entidade.getCnpj() + " - " + entidade.getNome() + "?");

        alertBuilderAtualizar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String cnpj = txtCnpj.getText().toString();
                    String nome = txtNome.getText().toString();

                    if (cnpj.isEmpty())
                        throw new Exception("O campo de cnpj não pode ficar vazio!");

                    if (nome.isEmpty())
                        throw new Exception("O campo de nome não pode ficar vazio!");

                    Fornecedor toUpdate = new Fornecedor();
                    toUpdate.setNome(nome.toUpperCase());

//                    boolean result = fornecedorManager.atualizar(toUpdate, entidade.getCnpj());

//                    if(result) {
//                        Toast.makeText(AlterarDeletarFornecedor.this, "O Fornecedor Foi Atualizado com Sucesso!", Toast.LENGTH_SHORT).show();
//                        setResult(RESULT_OK);
//                        finish();
//                    }
//                    else {
//                        Toast.makeText(AlterarDeletarFornecedor.this, "Erro ao Atualizar Fornecedor", Toast.LENGTH_SHORT).show();
//                    }
                } catch (Exception e) {
                    Toast.makeText(AlterarDeletarFornecedor.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertBuilderAtualizar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlterarDeletarFornecedor.this, "Fornecedor Não Foi Atualizado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void setAlertBuilderDeletar(final Fornecedor entidade) {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Fornecedor");
        alertBuilderDeletar.setMessage("Tem certeza que deseja delete o fornecedor " + entidade.getCnpj() + " - " + entidade.getNome() + "?");

        alertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                boolean result = fornecedorManager.deletar(entidade.getCnpj());

//                if(result) {
//                    Toast.makeText(AlterarDeletarFornecedor.this, "Fornecedor Deletado Com Sucesso", Toast.LENGTH_SHORT).show();
//                    setResult(RESULT_OK);
//                    finish();
//                }
//                else {
//                    Toast.makeText(AlterarDeletarFornecedor.this, "Erro ao Deletar Fornecedor", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        alertBuilderDeletar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AlterarDeletarFornecedor.this, "Fornecedor Não Foi Deletado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
