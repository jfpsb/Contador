package com.vandamodaintima.jfpsb.contador.tela.manager.marca;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.manager.MarcaManager;
import com.vandamodaintima.jfpsb.contador.entidade.Marca;
import com.vandamodaintima.jfpsb.contador.tela.manager.AlterarDeletarEntidade;

import org.apache.poi.ss.formula.eval.NotImplementedException;

public class AlterarDeletarMarca extends AlterarDeletarEntidade {
    private Button btnAtualizar;
    private Button btnDeletar;
    private EditText txtNome;

    private Marca marca;

    private MarcaManager marcaManager;

    @Override
    protected void onCreate(Bundle bundle) {
        if(bundle == null)
            bundle = new Bundle();

        bundle.putInt("layout", R.layout.content_alterar_deletar_marca);

        super.onCreate(bundle);

        marca = (Marca) getIntent().getExtras().getSerializable("marca");

        txtNome = findViewById(R.id.txtNome);

        txtNome.setText(marca.getNome());
    }

    @Override
    protected void setManagers() {
        marcaManager = new MarcaManager(conn);
    }

    @Override
    protected void setAlertBuilderDeletar() {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Marca");

        alertBuilderDeletar.setMessage("Tem Certeza que Deseja Deletar a Marca?");

        alertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean result = marcaManager.deletar(marca.getNome());

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
    protected void setAlertBuilderAtualizar() {
        throw new NotImplementedException("Não Tem Como Atualizar Marca");
    }
}
