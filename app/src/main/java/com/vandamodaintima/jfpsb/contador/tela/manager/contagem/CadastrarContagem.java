package com.vandamodaintima.jfpsb.contador.tela.manager.contagem;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.view.TelaCadastro;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarContagem extends TelaCadastro {

    private Button btnCadastrar;
    private Spinner spinnerLoja;
    private EditText txtData;

    private Loja loja;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cadastrar_contagem, container, false);

        txtData = view.findViewById(R.id.txtDataInicio);
        btnCadastrar = view.findViewById(R.id.btnCadastrar);
        spinnerLoja = view.findViewById(R.id.spinnerLoja);

        txtData.setText(Contagem.getDataString(new Date()));
        txtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contagem contagem = new Contagem();

                try {
                    String dataInicial = txtData.getText().toString();

                    if (loja == null)
                        throw new Exception("Loja Inválida");

                    if (dataInicial.isEmpty())
                        throw new Exception("O campo de data inicial não pode estar vazio!");

                    contagem.setData(Contagem.getData(dataInicial));
                    contagem.setLoja(loja);

//                    boolean result = contagemManager.inserir(contagem);
//
//                    if(result) {
//                        Toast.makeText(view.getContext(), "Contagem Inserida Com Data " + TrataDisplayData.getDataFormatoDisplay(contagem.getData()), Toast.LENGTH_SHORT).show();
//                        txtData.setText(TrataDisplayData.getDataFormatoDisplay(new Date()));
//                    }
//                    else {
//                        Toast.makeText(view.getContext(), "Erro ao Inserir Contagem", Toast.LENGTH_SHORT).show();
//                    }
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), "Erro ao Inserir Contagem: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinnerLoja.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    loja = (Loja) adapterView.getItemAtPosition(i);
                } else {
                    loja = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }
}
