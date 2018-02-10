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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.ContagemCursorAdapter;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOContagem;
import com.vandamodaintima.jfpsb.contador.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarContagem extends Fragment {
    private ConexaoBanco conn;
    private static DAOContagem daoContagem;
    private DAOLoja daoLoja;
    private ListView listView;
    private static ContagemCursorAdapter contagemCursorAdapter;
    private EditText txtDataInicial;
    private EditText txtDataFinal;
    private Spinner spinnerLoja;
    private Button btnPesquisar;
    private Loja loja = new Loja();

    public PesquisarContagem() {
        // Required empty public constructor
    }

    public void setConn(ConexaoBanco conn) {
        this.conn = conn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View viewInflate =inflater.inflate(R.layout.fragment_pesquisar_contagem, container, false);

        daoContagem = new DAOContagem(conn.conexao());
        daoLoja = new DAOLoja(conn.conexao());

        Cursor cursor = daoContagem.selectContagens();

        listView = viewInflate.findViewById(R.id.listViewLoja);

        contagemCursorAdapter = new ContagemCursorAdapter(viewInflate.getContext(), cursor);

        listView.setAdapter(contagemCursorAdapter);

        txtDataInicial = viewInflate.findViewById(R.id.txtDataInicial);
        txtDataFinal = viewInflate.findViewById(R.id.txtDataFinal);

        spinnerLoja = viewInflate.findViewById(R.id.spinnerLoja);

        Cursor cursorSpinner = daoLoja.selectLojas();

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(viewInflate.getContext(), android.R.layout.simple_spinner_dropdown_item, cursorSpinner, new String[] {"nome"}, new int[] {android.R.id.text1},0);
        simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLoja.setAdapter(simpleCursorAdapter);

        spinnerLoja.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor innerCursor = (Cursor) adapterView.getItemAtPosition(i);

                innerCursor.moveToPosition(i);

                loja.setIdloja(innerCursor.getInt(innerCursor.getColumnIndexOrThrow("_id")));
                loja.setNome(innerCursor.getString(innerCursor.getColumnIndexOrThrow("nome")));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnPesquisar = viewInflate.findViewById(R.id.btnPesquisar);

        btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contagem contagem = new Contagem();

                try {
                    String data_inicial = txtDataInicial.getText().toString();
                    String data_final = txtDataFinal.getText().toString();

                    if(data_inicial.isEmpty())
                        throw new Exception("Campo de data inicial não pode estar vazio!");

                    if(data_final.isEmpty())
                        throw new Exception("Campo de data final não pode estar vazio!");

                    //TODO: Testar formato digitado

                    contagem.setDatainicio(data_inicial);
                    contagem.setDatafim(data_final);
                    contagem.setLoja(loja.getIdloja());

                    populaListView(contagem);
                }catch (Exception e) {
                    Toast.makeText(viewInflate.getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return viewInflate;
    }

    /**
     * Popula a lista novamente
     */
    public static void populaListView() {
        // Switch to new cursor and update contents of ListView
        Cursor cursor = daoContagem.selectContagens();
        contagemCursorAdapter.changeCursor(cursor);
    }

    public static void populaListView(Contagem contagem) {
        Cursor cursor = daoContagem.selectContagens(contagem.getDatainicio(), contagem.getDatafim(), String.valueOf(contagem.getLoja()));
        contagemCursorAdapter.changeCursor(cursor);
    }

}
