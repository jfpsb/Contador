package com.vandamodaintima.jfpsb.contador.tela.manager;


import android.app.Activity;
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
import com.vandamodaintima.jfpsb.contador.dao.manager.ContagemManager;
import com.vandamodaintima.jfpsb.contador.dao.manager.LojaManager;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;
import com.vandamodaintima.jfpsb.contador.tela.TelaPesquisa;
import com.vandamodaintima.jfpsb.contador.util.ManipulaCursor;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;
import com.vandamodaintima.jfpsb.contador.util.TrataDisplayData;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarContagem extends TelaPesquisa {

    private static ContagemManager contagemManager;
    private LojaManager lojaManager;
    private ListView listView;
    private ContagemCursorAdapter contagemCursorAdapter;
    private EditText txtDataInicial;
    private EditText txtDataFinal;
    private Spinner spinnerLoja;
    private Button btnPesquisar;
    private Loja loja;
    private Date dataAtual;

    public PesquisarContagem() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.fragment_pesquisar_contagem, container, false);

        dataAtual = new Date();

        setManagers();

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
    protected void setManagers() {
        contagemManager = new ContagemManager(((ActivityBase) getActivity()).getConn());
        lojaManager = new LojaManager(((ActivityBase) getActivity()).getConn());
    }

    private void setListView() {
        if(cursorPesquisa != null)
            cursorPesquisa.close();

        try {
            contagemCursorAdapter = new ContagemCursorAdapter(viewInflate.getContext(), null);

            listView.setAdapter(contagemCursorAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cursor cursorItem = (Cursor) adapterView.getItemAtPosition(i);

                    cursorItem.moveToPosition(i);

                    // Contagem possui Loja então usando o Manager a Loja já vem iniciada
                    Contagem contagem = contagemManager.listarPorChave(cursorItem.getInt(cursorItem.getColumnIndexOrThrow("_id")));

                    Bundle bundle = new Bundle();

                    bundle.putSerializable("contagem", contagem);

                    Intent alterarContagem = new Intent(viewInflate.getContext(), AlterarDeletarContagem.class);
                    alterarContagem.putExtras(bundle);

                    startActivityForResult(alterarContagem, TELA_ALTERAR_DELETAR);
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TELA_ALTERAR_DELETAR:
                if(resultCode == Activity.RESULT_OK) {
                    btnPesquisar.performClick();
                }
                else {
                    Toast.makeText(viewInflate.getContext(), "A Contagem Não Foi Alterada", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setSpinnerLoja() {
        Cursor cursorSpinner = null, cursorSpinner2 = null;

        try {
            cursorSpinner = lojaManager.listarCursor();
            cursorSpinner2 = ManipulaCursor.retornaCursorComHintNull(cursorSpinner, "SELECIONE A LOJA", new String[]{"_id", "nome" });

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

                    loja = new Loja();
                    loja.setCnpj(innerCursor.getString(innerCursor.getColumnIndexOrThrow("_id")));
                    loja.setNome(innerCursor.getString(innerCursor.getColumnIndexOrThrow("nome")));
                } else {
                    loja = null;
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
                String data_inicial = txtDataInicial.getText().toString();
                String data_final = txtDataFinal.getText().toString();

                try {
                    if(loja.getCnpj().equals("-1")) {
                        throw new Exception("Loja Inválida");
                    }

                    if(TestaIO.isStringEmpty(data_inicial))
                        throw new Exception("Campo de data inicial não pode estar vazio!");

                    if(!TestaIO.isValidDate(data_inicial))
                        throw new Exception("A data inicial digitada é inválida!");

                    if(TestaIO.isStringEmpty(data_final))
                        throw new Exception("Campo de data final não pode estar vazio!");

                    if(!TestaIO.isValidDate(data_final))
                        throw new Exception("A data final digitada é inválida!");

                    populaListView(TrataDisplayData.getDataDisplay(data_inicial), TrataDisplayData.getDataDisplay(data_final), loja.getCnpj());

                }catch (Exception e) {
                    Log.e("Contador", e.getMessage(), e);
                    Toast.makeText(viewInflate.getContext(), "Erro ao Pesquisar Contagens: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void populaListView(Date datainicial, Date datafinal, String cnpj) {
        try {
            cursorPesquisa = contagemManager.listarPorPeriodoELoja(datainicial, datafinal, cnpj);

            if(cursorPesquisa.getCount() == 0) {
                Toast.makeText(viewInflate.getContext(), "A Pesquisa Não Retornou Dados", Toast.LENGTH_SHORT).show();
            }

            contagemCursorAdapter.changeCursor(cursorPesquisa);
        } catch (Exception e) {
            Log.i("Contador", e.getMessage());
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}