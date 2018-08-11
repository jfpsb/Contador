package com.vandamodaintima.jfpsb.contador.tela.manager.loja;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.manager.LojaManager;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;
import com.vandamodaintima.jfpsb.contador.tela.manager.AlterarDeletarEntidade;

public class AlterarDeletarLoja extends AlterarDeletarEntidade<Loja> {

    private EditText txtCnpj;
    private EditText txtNome;
    private Loja loja;
    private LojaManager lojaManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState == null)
            savedInstanceState = new Bundle();

        savedInstanceState.putInt("layout", R.layout.content_alterar_deletar_loja);

        loja = (Loja) getIntent().getExtras().getSerializable("loja");

        savedInstanceState.putString("entidade", "loja");
        savedInstanceState.putSerializable("loja", loja);

        super.onCreate(savedInstanceState);

        txtCnpj = findViewById(R.id.txtCnpj);
        txtNome = findViewById(R.id.txtNome);

        txtCnpj.setText(String.valueOf(loja.getCnpj()));
        txtNome.setText(loja.getNome());
    }

    @Override
    protected void setManagers() {
        lojaManager = new LojaManager(conn);
    }

    @Override
    protected void setAlertBuilderDeletar(Loja entidade) {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Loja");
        alertBuilderDeletar.setMessage("Tem Certeza Que Deseja Apagar a Loja " + entidade.getNome() + "?");

        alertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean result = lojaManager.deletar(loja.getCnpj());

                if(result) {
                    Toast.makeText(AlterarDeletarLoja.this, "Loja Deletada Com Sucesso", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK, null);
                    finish();
                }
                else {
                    Toast.makeText(AlterarDeletarLoja.this, "Erro ao Deletar Loja", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertBuilderDeletar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AlterarDeletarLoja.this, "Loja Não Foi Deletada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void setAlertBuilderAtualizar(Loja entidade) {
        alertBuilderAtualizar = new AlertDialog.Builder(this);
        alertBuilderAtualizar.setTitle("Atualizar Loja");
        alertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Atualizar a Loja " + entidade.getNome() + "?");

        alertBuilderAtualizar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String cnpj = txtCnpj.getText().toString();
                    String nome = txtNome.getText().toString();

                    if(cnpj.isEmpty())
                        throw new Exception("O Campo Cnpj Não Pode Estar Vazio!");

                    if(nome.isEmpty())
                        throw new Exception("O Campo Nome Não Pode Estar Vazio!");

                    Loja toUpdate = new Loja();

                    toUpdate.setCnpj(cnpj);
                    toUpdate.setNome(nome);

                    boolean result = lojaManager.atualizar(toUpdate, loja.getCnpj());

                    if(result) {
                        Toast.makeText(AlterarDeletarLoja.this, "A Loja Foi Atualizada Com Sucesso!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                    else {
                        Toast.makeText(AlterarDeletarLoja.this, "Erro ao Atualizar Loja", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(AlterarDeletarLoja.this, "Erro ao Atualizar Loja: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(LOG, e.getMessage(), e);
                }
            }
        });

        alertBuilderAtualizar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlterarDeletarLoja.this, "A Loja Não Foi Alterada", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
