package com.vandamodaintima.jfpsb.contador.tela.manager;


import android.content.Context;
import android.content.Intent;
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
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private Button btnPesquisarTodos;
    private Loja loja = new Loja();
    private Date dataAtual;
    private static View viewInflate;

    public PesquisarContagem() {
        // Required empty public constructor
    }

    public void setConn(ConexaoBanco conn) {
        this.conn = conn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflate =inflater.inflate(R.layout.fragment_pesquisar_contagem, container, false);

        dataAtual = new Date();

        daoContagem = new DAOContagem(conn.conexao());
        daoLoja = new DAOLoja(conn.conexao());

        Cursor cursor = daoContagem.selectContagens();

        listView = viewInflate.findViewById(R.id.listViewLoja);

        contagemCursorAdapter = new ContagemCursorAdapter(viewInflate.getContext(), cursor);

        listView.setAdapter(contagemCursorAdapter);

        txtDataInicial = viewInflate.findViewById(R.id.txtDataInicial);
        txtDataFinal = viewInflate.findViewById(R.id.txtDataFinal);
        txtDataInicial.setText(TestaIO.dateFormat.format(dataAtual));
        txtDataFinal.setText(TestaIO.dateFormat.format(dataAtual));

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

                    if(TestaIO.isStringEmpty(data_inicial))
                        throw new Exception("Campo de data inicial não pode estar vazio!");

                    if(!TestaIO.isValidDate(data_inicial))
                        throw new Exception("A data inicial digitada é inválida!");

                    if(TestaIO.isStringEmpty(data_final))
                        throw new Exception("Campo de data final não pode estar vazio!");

                    if(!TestaIO.isValidDate(data_final))
                        throw new Exception("A data final digitada é inválida!");

                    contagem.setDatainicio(data_inicial);
                    contagem.setDatafim(data_final);
                    contagem.setLoja(loja.getIdloja());

                    populaListView(contagem, loja);
                }catch (Exception e) {
                    Toast.makeText(viewInflate.getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPesquisarTodos = viewInflate.findViewById(R.id.btnPesquisarTodos);

        btnPesquisarTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populaListView();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                cursor.moveToPosition(i);

                Contagem contagem = new Contagem();

                String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String idloja = cursor.getString(cursor.getColumnIndexOrThrow("loja"));
                String nomeLoja = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
                String dataInicio = cursor.getString(cursor.getColumnIndexOrThrow("datainicio"));
                String dataFinal = cursor.getString(cursor.getColumnIndexOrThrow("datafinal"));

                contagem.setIdcontagem(Integer.parseInt(id));
                contagem.setLoja(Integer.parseInt(idloja));
                contagem.setDatainicio(dataInicio);
                contagem.setDatafim(dataFinal);

                Bundle bundle = new Bundle();

                bundle.putSerializable("contagem", contagem);
                bundle.putString("lojaNome", nomeLoja);

                Intent alterarContagem = new Intent(viewInflate.getContext(), AlterarDeletarContagem.class);
                alterarContagem.putExtras(bundle);

                startActivity(alterarContagem);
            }
        });

        return viewInflate;
    }

    /**
     * Popula a lista novamente
     */
    public static void populaListView() {
        // Switch to new cursor and update contents of ListView
        Toast.makeText(viewInflate.getContext(), "Pesquisando todos as contagens em todas as lojas", Toast.LENGTH_SHORT).show();
        Cursor cursor = daoContagem.selectContagens();
        contagemCursorAdapter.changeCursor(cursor);
    }

    public static void populaListView(Contagem contagem, Loja loja) {
        Toast.makeText(viewInflate.getContext(), "Pesquisando contagens na loja " + loja.getNome() + " no intervalo " + contagem.getDatainicio() + " a " + contagem.getDatafim(), Toast.LENGTH_SHORT).show();
        Cursor cursor = daoContagem.selectContagens(contagem.getDatainicio(), contagem.getDatafim(), Integer.toString(contagem.getLoja()));
        contagemCursorAdapter.changeCursor(cursor);
    }
}