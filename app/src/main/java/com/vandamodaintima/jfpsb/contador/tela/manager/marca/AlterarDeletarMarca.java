package com.vandamodaintima.jfpsb.contador.tela.manager.marca;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.manager.MarcaManager;
import com.vandamodaintima.jfpsb.contador.entidade.Marca;
import com.vandamodaintima.jfpsb.contador.tela.manager.AlterarDeletarEntidade;

public class AlterarDeletarMarca extends AlterarDeletarEntidade<Marca> {
    private EditText txtNome;

    private Marca marca;

    private MarcaManager marcaManager;

    @Override
    protected void onCreate(Bundle bundle) {
        if(bundle == null)
            bundle = new Bundle();

        bundle.putInt("layout", R.layout.content_alterar_deletar_marca);

        marca = (Marca) getIntent().getExtras().getSerializable("marca");

        bundle.putString("entidade", "marca");
        bundle.putSerializable("marca", marca);

        super.onCreate(bundle);

        txtNome = findViewById(R.id.txtNome);

        txtNome.setText(marca.getNome());
    }

    @Override
    protected void setManagers() {
        marcaManager = new MarcaManager(conn);
    }

    @Override
    protected void setAlertBuilderDeletar(Marca entidade) {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Marca");

        alertBuilderDeletar.setMessage("Tem Certeza que Deseja Deletar a Marca?");

        alertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean result = marcaManager.deletar(marca.getId());

                if (result) {
                    Toast.makeText(getApplicationContext(), "Marca Deletada Com Sucesso", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(AlterarDeletarMarca.this, "Erro ao Deletar Marca", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertBuilderDeletar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlterarDeletarMarca.this, "Marca Não foi Deletada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void setAlertBuilderAtualizar(Marca entidade) {
        alertBuilderAtualizar = new AlertDialog.Builder(this);
        alertBuilderAtualizar.setTitle("Atualizar Marca");
        alertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Atualizar a Marca?");

        alertBuilderAtualizar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String nome = txtNome.getText().toString();

                    if(nome.isEmpty())
                        throw new Exception("O Nome Não Pode Ficar Vazio!");

                    Marca toUpdate = new Marca();
                    toUpdate.setId(marca.getId());
                    toUpdate.setNome(nome);

                    boolean result = marcaManager.atualizar(toUpdate, marca.getId());

                    if(result) {
                        Toast.makeText(AlterarDeletarMarca.this, "Marca Atualizada Com Sucesso", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                    else {
                        Toast.makeText(AlterarDeletarMarca.this, "Erro ao Atualizar Marca", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Log.e(LOG, e.getMessage(), e);
                    Toast.makeText(AlterarDeletarMarca.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertBuilderAtualizar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlterarDeletarMarca.this, "Marca Não Foi Alterada", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
