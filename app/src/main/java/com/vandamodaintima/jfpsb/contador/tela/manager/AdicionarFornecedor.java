package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.util.ManipulaCursor;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;
import com.vandamodaintima.jfpsb.contador.util.TratamentoMensagensSQLite;

public class AdicionarFornecedor extends ActivityBase {

    private Spinner spinnerFornecedor;
    private Button btnSelecionar;
    private Button btnCadastrar;
    private EditText txtCnpj;
    private EditText txtNome;

    private DAOFornecedor daoFornecedor;

    private Fornecedor fornecedor = new Fornecedor();

    private TextWatcher textWatcher;
    private AdapterView.OnItemSelectedListener onItemSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_adicionar_fornecedor);
        stub.inflate();

        spinnerFornecedor = findViewById(R.id.spinnerFornecedor);
        btnSelecionar = findViewById(R.id.btnSelecionar);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        txtCnpj = findViewById(R.id.txtCnpj);
        txtNome = findViewById(R.id.txtNome);

        setDAOs();

        setTextChangedEmCampos();

        setSpinnerFornecedor();

        setBtnCadastrar();

        setBtnSelecionar();
    }

    @Override
    protected void setDAOs() {
        daoFornecedor = new DAOFornecedor(conn.conexao());
    }

    private void setBtnSelecionar() {
        btnSelecionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(fornecedor.getCnpj() ==  null)
                        throw new Exception("Escolha o fornecedor no menu acima!");

                    Intent intentResult = new Intent();

                    intentResult.putExtra("fornecedor", fornecedor);

                    setResult(Activity.RESULT_OK, intentResult);

                    Toast.makeText(getApplicationContext(), "Fornecedor " + fornecedor.getNome() + " escolhido. Alteração será salva ao adicionar quantidade", Toast.LENGTH_SHORT).show();

                    finish();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    setResult(Activity.RESULT_CANCELED);
                }
            }
        });
    }

    private void setBtnCadastrar() {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String cnpj = txtCnpj.getText().toString();
                    String nome = txtNome.getText().toString();

                    if(TestaIO.isStringEmpty(cnpj))
                        throw new Exception("Campo de CNPJ não pode ficar vazio!");

                    if(TestaIO.isStringEmpty(nome))
                        throw new Exception("Campo de nome não pode ficar vazio!");

                    fornecedor.setCnpj(cnpj);
                    fornecedor.setNome(nome.toUpperCase());

                    long result[] = daoFornecedor.inserir(fornecedor);

                    if(result[0] != -1) {
                        Toast.makeText(getApplicationContext(), "Inserção de fornecedor " + fornecedor.getNome() + " efetuada com sucesso.", Toast.LENGTH_SHORT).show();

                        Intent addFornecedor = new Intent();

                        addFornecedor.putExtra("fornecedor", fornecedor);

                        setResult(Activity.RESULT_OK, addFornecedor);

                        finish();
                    }
                    else {
                        TratamentoMensagensSQLite.trataErroEmInsert(getApplicationContext(), result[1]);

                        setResult(Activity.RESULT_CANCELED);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setSpinnerFornecedor() {
        Cursor cursor = null, cursor2 = null;

        try {
            cursor = daoFornecedor.selectFornecedores();

            cursor2 = ManipulaCursor.retornaCursorComHintNull(cursor, "SELECIONE O FORNECEDOR", new String[]{"_id", "nome"});

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, cursor2, new String[]{"nome"}, new int[]{android.R.id.text1}, 0);
            simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerFornecedor.setAdapter(simpleCursorAdapter);

            onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    txtNome.removeTextChangedListener(textWatcher);
                    txtCnpj.removeTextChangedListener(textWatcher);

                    if (position != 0) {
                        Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                        cursor.moveToPosition(position);

                        fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                        fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                    } else {
                        fornecedor.setCnpj(null);
                    }

                    txtCnpj.setText("");
                    txtNome.setText("");

                    txtCnpj.addTextChangedListener(textWatcher);
                    txtNome.addTextChangedListener(textWatcher);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(AdicionarFornecedor.this, "Nenhum fornecedor foi escolhido", Toast.LENGTH_SHORT).show();
                }
            };

            spinnerFornecedor.setOnItemSelectedListener(onItemSelectedListener);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            if(cursor != null)
                cursor.close();

            if(cursor2 != null)
                cursor2.close();
        }
    }

    private void setTextChangedEmCampos() {
        textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                spinnerFornecedor.setOnItemSelectedListener(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                spinnerFornecedor.setSelection(0);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                spinnerFornecedor.setOnItemSelectedListener(onItemSelectedListener);
            }
        };

        txtNome.addTextChangedListener(textWatcher);
        txtCnpj.addTextChangedListener(textWatcher);
    }
}
