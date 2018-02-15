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
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarContagem extends Fragment {
    private ConexaoBanco conn;
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

    public void setConn(ConexaoBanco conn) {
        this.conn = conn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View viewInflate = inflater.inflate(R.layout.fragment_cadastrar_contagem, container, false);

        dataAtual = new Date();

        daoContagem = new DAOContagem(conn.conexao());
        daoLoja = new DAOLoja(conn.conexao());

        txtDataInicial = viewInflate.findViewById(R.id.txtDataInicio);
        txtDataInicial.setText(TestaIO.dateFormat.format(dataAtual));

        spinnerLoja = viewInflate.findViewById(R.id.spinnerLoja);

        Cursor cursorSpinner = daoLoja.selectLojas();

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(viewInflate.getContext(), android.R.layout.simple_spinner_dropdown_item, cursorSpinner, new String[] {"nome"}, new int[] {android.R.id.text1},0);
        simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLoja.setAdapter(simpleCursorAdapter);

        spinnerLoja.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                cursor.moveToPosition(i);

                loja.setIdloja(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
                loja.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnCadastrar = viewInflate.findViewById(R.id.btnCadastrar);

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

                    long id = daoContagem.inserir(contagem);

                    if(id != -1) {
                        Toast.makeText(viewInflate.getContext(), "Contagem inserida com data incial " + contagem.getDatainicio(), Toast.LENGTH_SHORT).show();

                        PesquisarContagem.populaListView();

                        txtDataInicial.setText("");
                    }
                    else {
                        Toast.makeText(viewInflate.getContext(), "Erro ao inserir contagem!", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e) {
                    Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return viewInflate;
    }

}
