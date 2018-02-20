package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SimpleCursorAdapter;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.DAOContagem;
import com.vandamodaintima.jfpsb.contador.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;
import com.vandamodaintima.jfpsb.contador.util.ManipulaCursor;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

public class AlterarDeletarContagem extends AlterarDeletarEntidade {

    private Contagem contagem;
    private EditText txtIDContagem;
    private EditText txtDataFinal;
    private EditText txtDataInicial;
    private EditText txtLojaAtual;
    private Spinner spinnerLoja;
    private Loja loja = new Loja();
    private CheckBox checkBoxDataFinal;
    private Button btnAdicionar;

    private DAOLoja daoLoja;
    private DAOContagem daoContagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_alterar_deletar_contagem);
        stub.inflate();

        contagem = (Contagem) getIntent().getExtras().getSerializable("contagem");

        txtIDContagem = findViewById(R.id.txtIDContagem);
        txtDataFinal = findViewById(R.id.txtDataFinal);
        txtDataInicial = findViewById(R.id.txtDataInicial);
        txtLojaAtual = findViewById(R.id.txtLojaAtual);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnDeletar = findViewById(R.id.btnDeletar);
        checkBoxDataFinal = findViewById(R.id.checkBoxDataFinal);
        btnAdicionar = findViewById(R.id.btnAdicionar);

        txtIDContagem.setText(String.valueOf(contagem.getIdcontagem()));
        txtDataFinal.setText(contagem.getDatafim());
        txtDataInicial.setText(contagem.getDatainicio());
        txtLojaAtual.setText(getIntent().getExtras().getString("lojaNome"));

        setAlertBuilder(contagem.getIdcontagem());

        spinnerLoja = findViewById(R.id.spinnerLoja);

        setDAOs();

        setSpinnerLoja();

        setBtnAtualizar();

        setBtnDeletar();

        setCheckBoxDataFinal();

        setBtnAdicionar();
    }

    @Override
    protected void setDAOs() {
        daoLoja = new DAOLoja(conn.conexao());
        daoContagem = new DAOContagem(conn.conexao());
    }

    @Override
    protected void setAlertBuilder(final Object idcontagem) {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Deletar Contagem");
        builder.setMessage("Tem certeza que deseja apagar a contagem de ID " + idcontagem + "?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                daoContagem.deletar((int) idcontagem);

                PesquisarContagem.populaListView();

                finish();
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AlterarDeletarContagem.this, "Contagem não foi deletada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSpinnerLoja() {
        Cursor spinnerCursor = null, spinnerCursor2 = null;

        try {
            spinnerCursor = daoLoja.selectLojas();

            spinnerCursor2 = ManipulaCursor.retornaCursorComHintNull(spinnerCursor, "SELECIONE A LOJA", new String[]{ "_id", "nome"});

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCursor2, new String[]{"nome"}, new int[]{android.R.id.text1}, 0);
            simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerLoja.setAdapter(simpleCursorAdapter);

            for (int i = 0; i < spinnerCursor.getCount(); i++) {
                spinnerCursor.moveToPosition(i);

                int id = spinnerCursor.getInt(spinnerCursor.getColumnIndexOrThrow("_id"));

                if (id == contagem.getLoja()) {
                    spinnerLoja.setSelection(i);
                    break;
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        spinnerLoja.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor c = (Cursor) adapterView.getItemAtPosition(i);

                c.moveToPosition(i);

                String idloja = c.getString(c.getColumnIndexOrThrow("_id"));
                String nome = c.getString(c.getColumnIndexOrThrow("nome"));

                loja.setIdloja(Integer.parseInt(idloja));
                loja.setNome(nome);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void setBtnAtualizar() {
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String data_final = txtDataFinal.getText().toString();

                    if(checkBoxDataFinal.isChecked() && !TestaIO.isValidDate(data_final))
                        throw new Exception("O campo de data final está com um valor inválido!");

                    contagem.setDatafim(data_final);
                    contagem.setLoja(loja.getIdloja());

                    int result;

                    if(checkBoxDataFinal.isChecked())
                        result = daoContagem.atualizar(contagem);
                    else
                        result = daoContagem.atualizarSemDataFinal(contagem);

                    if(result != -1) {
                        Toast.makeText(AlterarDeletarContagem.this, "A contagem com ID " + contagem.getIdcontagem() + " foi atualizada com sucesso!", Toast.LENGTH_SHORT).show();

                        PesquisarContagem.populaListView();
                    }
                } catch (Exception e) {
                    Toast.makeText(AlterarDeletarContagem.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void setCheckBoxDataFinal() {
        checkBoxDataFinal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBoxDataFinal.isChecked()) {
                    txtDataFinal.setEnabled(true);
                }
                else {
                    txtDataFinal.setEnabled(false);
                }
            }
        });
    }

    private void setBtnAdicionar() {
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pesquisaContagemProduto = new Intent(AlterarDeletarContagem.this, PesquisaContagemProduto.class);

                Bundle bundle = new Bundle();

                bundle.putSerializable("contagem",contagem);

                pesquisaContagemProduto.putExtras(bundle);

                startActivity(pesquisaContagemProduto);
            }
        });
    }
}
