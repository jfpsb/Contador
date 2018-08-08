package com.vandamodaintima.jfpsb.contador.tela.manager.contagem;


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
    private EditText txtData;
    private Spinner spinnerLoja;
    private Button btnPesquisar;
    private Loja loja;
    private Date dataAtual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState == null)
            savedInstanceState = new Bundle();

        savedInstanceState.putInt("layout", R.layout.fragment_pesquisar_contagem);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setManagers() {
        contagemManager = new ContagemManager(((ActivityBase) getActivity()).getConn());
        lojaManager = new LojaManager(((ActivityBase) getActivity()).getConn());
    }

    @Override
    protected void setViews() {
        setTxtData();
        setListView();
        setBtnPesquisar();
        setSpinnerLoja();
    }

    private void setTxtData() {
        dataAtual = new Date();

        txtData = viewInflate.findViewById(R.id.txtData);
        txtData.setText(TestaIO.dateFormat.format(dataAtual));

        txtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });
    }

    @Override
    protected void setListView() {
        listView = viewInflate.findViewById(R.id.listViewLoja);

        if (cursorPesquisa != null)
            cursorPesquisa.close();

        try {
            contagemCursorAdapter = new ContagemCursorAdapter(viewInflate.getContext(), null);
            listView.setAdapter(contagemCursorAdapter);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        super.setListView();
    }

    @Override
    protected void setListOnItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor c = (Cursor) adapterView.getItemAtPosition(i);

                Loja loja = new Loja();
                String cnpj = c.getString(c.getColumnIndexOrThrow("loja"));
                loja.setCnpj(cnpj);

                Date data = TrataDisplayData.getDataBD(c.getString(c.getColumnIndexOrThrow("data")));

                // Contagem possui Loja então usando o Manager a Loja já vem iniciada
                Contagem contagem = contagemManager.listarPorChave(loja, data);

                Bundle bundle = new Bundle();

                bundle.putSerializable("contagem", contagem);

                Intent alterarContagem = new Intent(viewInflate.getContext(), AlterarDeletarContagem.class);
                alterarContagem.putExtras(bundle);

                startActivityForResult(alterarContagem, TELA_ALTERAR_DELETAR);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TELA_ALTERAR_DELETAR:
                if (resultCode == Activity.RESULT_OK) {
                    btnPesquisar.performClick();
                } else {
                    Toast.makeText(viewInflate.getContext(), "A Contagem Não Foi Alterada", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setSpinnerLoja() {
        spinnerLoja = viewInflate.findViewById(R.id.spinnerLoja);

        Cursor cursorSpinner = null, cursorSpinner2 = null;

        try {
            cursorSpinner = lojaManager.listarCursor();
            cursorSpinner2 = ManipulaCursor.retornaCursorComHintNull(cursorSpinner, "SELECIONE A LOJA", new String[]{"_id", "nome"});

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(viewInflate.getContext(), android.R.layout.simple_spinner_dropdown_item, cursorSpinner2, new String[]{"nome"}, new int[]{android.R.id.text1}, 0);
            simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerLoja.setAdapter(simpleCursorAdapter);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursorSpinner != null)
                cursorSpinner.close();

            if (cursorSpinner2 != null)
                cursorSpinner2.close();
        }

        spinnerLoja.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
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
        btnPesquisar = viewInflate.findViewById(R.id.btnPesquisar);

        btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = txtData.getText().toString();

                try {
                    if (loja == null) {
                        throw new Exception("Loja Inválida");
                    }

                    if (data.isEmpty())
                        throw new Exception("Campo de data inicial não pode estar vazio!");

                    if (!TestaIO.isValidDate(data))
                        throw new Exception("A data inicial digitada é inválida!");

                    populaListView(TrataDisplayData.getDataDisplay(data), loja.getCnpj());

                } catch (Exception e) {
                    Log.e("Contador", e.getMessage(), e);
                    Toast.makeText(getContext(), "Erro ao Pesquisar Contagens: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void populaListView(Date data, String cnpj) {
        try {
            cursorPesquisa = contagemManager.listarPorDataELoja(data, cnpj);

            if (cursorPesquisa.getCount() == 0) {
                Toast.makeText(viewInflate.getContext(), "A Pesquisa Não Retornou Dados", Toast.LENGTH_SHORT).show();
            }

            contagemCursorAdapter.changeCursor(cursorPesquisa);
        } catch (Exception e) {
            Log.i("Contador", e.getMessage());
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}