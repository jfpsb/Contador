package com.vandamodaintima.jfpsb.contador.tela.manager;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.vandamodaintima.jfpsb.contador.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.dao.manager.ContagemManager;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;
import com.vandamodaintima.jfpsb.contador.util.ManipulaCursor;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;
import com.vandamodaintima.jfpsb.contador.util.TrataDisplayData;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarContagem extends FragmentBase {

    private static ContagemManager contagemManager;
    private DAOLoja daoLoja;
    private ListView listView;
    private static ContagemCursorAdapter contagemCursorAdapter;
    private EditText txtDataInicial;
    private EditText txtDataFinal;
    private Spinner spinnerLoja;
    private Button btnPesquisar;
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

        setListView();
        setBtnPesquisar();
        setSpinnerLoja();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setDAOs() {
        contagemManager = new ContagemManager(((ActivityBase) getActivity()).getConn());
        daoLoja = new DAOLoja(((ActivityBase) getActivity()).getConn().conexao());
    }

    private void setListView() {
        if(cursorLista != null)
            cursorLista.close();

        try {
            //cursorLista = contagemManager.listarCursor();

            contagemCursorAdapter = new ContagemCursorAdapter(viewInflate.getContext(), null);

            listView.setAdapter(contagemCursorAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cursor cursorItem = (Cursor) adapterView.getItemAtPosition(i);

                    cursorItem.moveToPosition(i);

                    Contagem contagem = contagemManager.listarPorId(cursorItem.getInt(cursorItem.getColumnIndexOrThrow("_id")));

                    Bundle bundle = new Bundle();

                    bundle.putSerializable("contagem", contagem);

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

                String data_inicial = txtDataInicial.getText().toString();
                String data_final = txtDataFinal.getText().toString();

                try {
                    if(TestaIO.isStringEmpty(data_inicial))
                        throw new Exception("Campo de data inicial não pode estar vazio!");

                    if(!TestaIO.isValidDate(data_inicial))
                        throw new Exception("A data inicial digitada é inválida!");

                    if(TestaIO.isStringEmpty(data_final))
                        throw new Exception("Campo de data final não pode estar vazio!");

                    if(!TestaIO.isValidDate(data_final))
                        throw new Exception("A data final digitada é inválida!");

                    contagem.setDatainicio(TrataDisplayData.getDataDisplay(data_inicial));
                    contagem.setDatafinal(TrataDisplayData.getDataDisplay(data_final));
                    contagem.setLoja(loja);

                    populaListView(contagem);

                }catch (Exception e) {
                    Log.i("Contador", e.getMessage());
                    Toast.makeText(viewInflate.getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void populaListView(Contagem contagem) {
        Toast.makeText(viewInflate.getContext(), "Pesquisando contagens na loja " + contagem.getLoja().getNome() + " no intervalo " +
                TrataDisplayData.getDataEmStringDisplay(contagem.getDatainicio()) + " a " + TrataDisplayData.getDataEmStringDisplay(contagem.getDatafinal()), Toast.LENGTH_SHORT).show();
        try {
            cursorLista = contagemManager.listarPorPeriodoELoja(contagem.getDatainicio(), contagem.getDatafinal(), contagem.getLoja());
            contagemCursorAdapter.changeCursor(cursorLista);
        } catch (Exception e) {
            Log.i("Contador", e.getMessage());
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}