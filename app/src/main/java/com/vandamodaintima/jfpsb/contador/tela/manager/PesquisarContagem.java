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
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;
import com.vandamodaintima.jfpsb.contador.util.ManipulaCursor;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarContagem extends FragmentBase {

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

    public PesquisarContagem() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.fragment_pesquisar_contagem, container, false);

        dataAtual = new Date();

        setDAOs();

        listView = viewInflate.findViewById(R.id.listViewLoja);
        txtDataInicial = viewInflate.findViewById(R.id.txtDataInicial);
        txtDataFinal = viewInflate.findViewById(R.id.txtDataFinal);
        txtDataInicial.setText(TestaIO.dateFormat.format(dataAtual));
        txtDataFinal.setText(TestaIO.dateFormat.format(dataAtual));

        spinnerLoja = viewInflate.findViewById(R.id.spinnerLoja);

        btnPesquisar = viewInflate.findViewById(R.id.btnPesquisar);

        btnPesquisarTodos = viewInflate.findViewById(R.id.btnPesquisarTodos);

        setListView();
        setBtnPesquisar();
        setBtnPesquisarTodos();
        setSpinnerLoja();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setDAOs() {
        daoContagem = new DAOContagem(((ActivityBase) getActivity()).getConn().conexao());
        daoLoja = new DAOLoja(((ActivityBase) getActivity()).getConn().conexao());
    }

    private void setListView() {
        if(cursorLista != null)
            cursorLista.close();

        try {
            cursorLista = daoContagem.selectContagens();

            contagemCursorAdapter = new ContagemCursorAdapter(viewInflate.getContext(), cursorLista);

            listView.setAdapter(contagemCursorAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cursor cursorItem = (Cursor) adapterView.getItemAtPosition(i);

                    cursorItem.moveToPosition(i);

                    Contagem contagem = new Contagem();

                    String id = cursorItem.getString(cursorItem.getColumnIndexOrThrow("_id"));
                    String idloja = cursorItem.getString(cursorItem.getColumnIndexOrThrow("loja"));
                    String nomeLoja = cursorItem.getString(cursorItem.getColumnIndexOrThrow("nome"));
                    String dataInicio = cursorItem.getString(cursorItem.getColumnIndexOrThrow("datainicio"));
                    String dataFinal = cursorItem.getString(cursorItem.getColumnIndexOrThrow("datafinal"));

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
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setSpinnerLoja() {
        Cursor cursorSpinner = null, cursorSpinner2 = null;

        try {
            cursorSpinner = daoLoja.selectLojas();

            cursorSpinner2 = ManipulaCursor.retornaCursorComHintNull(cursorSpinner, "SELECIONE A LOJA", new String[]{"_id", "nome"});

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(viewInflate.getContext(), android.R.layout.simple_spinner_dropdown_item, cursorSpinner2, new String[]{"nome"}, new int[]{android.R.id.text1}, 0);
            simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerLoja.setAdapter(simpleCursorAdapter);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            if(cursorSpinner != null)
                cursorSpinner.close();

            if(cursorSpinner2 != null)
                cursorSpinner2.close();
        }

        spinnerLoja.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0) {
                    Cursor innerCursor = (Cursor) adapterView.getItemAtPosition(i);

                    innerCursor.moveToPosition(i);

                    loja.setIdloja(innerCursor.getInt(innerCursor.getColumnIndexOrThrow("_id")));
                    loja.setNome(innerCursor.getString(innerCursor.getColumnIndexOrThrow("nome")));
                } else {
                    loja.setIdloja(-1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setBtnPesquisar() {
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
    }

    private void setBtnPesquisarTodos() {
        btnPesquisarTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populaListView();
            }
        });
    }

    /**
     * Popula a lista novamente
     */

    public static void populaListView() {
        Toast.makeText(viewInflate.getContext(), "Pesquisando todos as contagens em todas as lojas", Toast.LENGTH_SHORT).show();

        if(cursorLista != null)
            cursorLista.close();

        try {
            cursorLista = daoContagem.selectContagens();
            contagemCursorAdapter.changeCursor(cursorLista);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void populaListView(Contagem contagem, Loja loja) {
        Toast.makeText(viewInflate.getContext(), "Pesquisando contagens na loja " + loja.getNome() + " no intervalo " + contagem.getDatainicio() + " a " + contagem.getDatafim(), Toast.LENGTH_SHORT).show();

        if(cursorLista != null)
            cursorLista.close();

        try {
            cursorLista = daoContagem.selectContagens(contagem.getDatainicio(), contagem.getDatafim(), Integer.toString(contagem.getLoja()));
            contagemCursorAdapter.changeCursor(cursorLista);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}