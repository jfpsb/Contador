package com.vandamodaintima.jfpsb.contador.tela.manager.contagem;


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
import com.vandamodaintima.jfpsb.contador.dao.manager.ContagemManager;
import com.vandamodaintima.jfpsb.contador.dao.manager.LojaManager;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;
import com.vandamodaintima.jfpsb.contador.tela.TabLayoutActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;
import com.vandamodaintima.jfpsb.contador.util.ManipulaCursor;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;
import com.vandamodaintima.jfpsb.contador.util.TrataDisplayData;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarContagem extends FragmentBase {

    private Button btnCadastrar;
    private LojaManager lojaManager;
    private Spinner spinnerLoja;
    private EditText txtData;
    private Date dataAtual = new Date();

    private ContagemManager contagemManager;

    private Loja loja;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState == null)
            savedInstanceState = new Bundle();

        savedInstanceState.putInt("layout", R.layout.fragment_cadastrar_contagem);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setManagers() {
        contagemManager = new ContagemManager(((TabLayoutActivityBase)getActivity()).getConn());
        lojaManager = new LojaManager(((TabLayoutActivityBase)getActivity()).getConn());
    }

    @Override
    protected void setViews() {
        setTxtData();
        setBtnCadastrar();
        setSpinnerLoja();
    }

    private void setTxtData() {
        txtData = viewInflate.findViewById(R.id.txtDataInicio);
        txtData.setText(TestaIO.dateFormat.format(dataAtual));

        txtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });
    }

    private void setBtnCadastrar() {
        btnCadastrar = viewInflate.findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contagem contagem = new Contagem();

                try {
                    String dataInicial = txtData.getText().toString();

                    if(loja == null)
                        throw new Exception("Loja Inválida");

                    if(dataInicial.isEmpty())
                        throw new Exception("O campo de data inicial não pode estar vazio!");

                    contagem.setData(TrataDisplayData.getDataDisplay(dataInicial));
                    contagem.setLoja(loja);

                    boolean result = contagemManager.inserir(contagem);

                    if(result) {
                        Toast.makeText(viewInflate.getContext(), "Contagem Inserida Com Data " + TrataDisplayData.getDataFormatoDisplay(contagem.getData()), Toast.LENGTH_SHORT).show();
                        txtData.setText(TrataDisplayData.getDataFormatoDisplay(dataAtual));
                    }
                    else {
                        Toast.makeText(viewInflate.getContext(), "Erro ao Inserir Contagem", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e) {
                    Toast.makeText(viewInflate.getContext(), "Erro ao Inserir Contagem: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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

                    loja = new Loja();
                    loja.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                    loja.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                } else {
                    loja = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
