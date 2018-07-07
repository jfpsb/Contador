package com.vandamodaintima.jfpsb.contador.tela.manager;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOContagem;
import com.vandamodaintima.jfpsb.contador.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;
import com.vandamodaintima.jfpsb.contador.util.ManipulaCursor;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;
import com.vandamodaintima.jfpsb.contador.util.TratamentoMensagensSQLite;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarContagem extends FragmentBase {

    private Button btnCadastrar;
    private DAOContagem daoContagem;
    private DAOLoja daoLoja;
    private Spinner spinnerLoja;
    private EditText txtDataInicial;
    private Date dataAtual;

    private Loja loja = new Loja();

    public CadastrarContagem() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.fragment_cadastrar_contagem, container, false);

        dataAtual = new Date();

        setDAOs();

        txtDataInicial = viewInflate.findViewById(R.id.txtDataInicio);
        txtDataInicial.setText(TestaIO.dateFormat.format(dataAtual));

        spinnerLoja = viewInflate.findViewById(R.id.spinnerLoja);

        btnCadastrar = viewInflate.findViewById(R.id.btnCadastrar);

        setBtnCadastrar();

        setSpinnerLoja();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setDAOs() {
        daoContagem = new DAOContagem(((ActivityBase)getActivity()).getConn().conexao());
        daoLoja = new DAOLoja(((ActivityBase)getActivity()).getConn().conexao());
    }

    private void setBtnCadastrar() {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contagem contagem = new Contagem();

                try {
                    String dataInicial = txtDataInicial.getText().toString();

                    if(TestaIO.isStringEmpty(dataInicial))
                        throw new Exception("O campo de data inicial não pode estar vazio!");

                    if(!TestaIO.isValidDate(dataInicial))
                        throw new Exception("A data digitada é inválida! Formato correto é 'aaaa-mm-dd'");

                    contagem.setDatainicio(dataInicial);
                    contagem.setLoja(loja.getIdloja());

                    long result[] = daoContagem.inserir(contagem);

                    if(result[0] != -1) {
                        Toast.makeText(viewInflate.getContext(), "Contagem inserida com data incial " + contagem.getDatainicio(), Toast.LENGTH_SHORT).show();

                        PesquisarContagem.populaListView();

                        txtDataInicial.setText(TestaIO.dateFormat.format(dataAtual));
                    }
                    else {
                        TratamentoMensagensSQLite.trataErroEmInsert(viewInflate.getContext(), result[1]);
                    }

                }catch (Exception e) {
                    Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setSpinnerLoja() {
        Cursor cursorSpinner = null, cursorSpinner2 = null;

        try {
            cursorSpinner = daoLoja.selectLojas();

            cursorSpinner2 = ManipulaCursor.retornaCursorComHintNull(cursorSpinner, "SELECIONE A LOJA", new String[]{"_id", "nome"});

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(viewInflate.getContext(), android.R.layout.simple_spinner_dropdown_item, cursorSpinner2, new String[]{"nome"}, new int[]{android.R.id.text1}, 0);
            simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerLoja.setAdapter(simpleCursorAdapter);
        }
        catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            cursorSpinner.close();
            cursorSpinner2.close();
        }

        spinnerLoja.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0) {
                    Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                    cursor.moveToPosition(i);

                    loja.setIdloja(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
                    loja.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                } else {
                    loja.setIdloja(-1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
