package com.vandamodaintima.jfpsb.contador.tela.manager.contagem;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.contagem.PesquisarContagemController;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.view.TelaPesquisa;

import java.util.Date;

public class PesquisarContagem extends TelaPesquisa {

    private EditText txtData;
    private Spinner spinnerLoja;
    private Button btnPesquisar;
    private Loja loja;

    private PesquisarContagemController pesquisarContagemController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pesquisar_contagem, container, false);

        sqLiteDatabase = new ConexaoBanco(getContext()).conexao();
        pesquisarContagemController = new PesquisarContagemController(this, sqLiteDatabase, getContext());

        txtData = view.findViewById(R.id.txtData);
        listView = view.findViewById(R.id.listViewLoja);
        btnPesquisar = view.findViewById(R.id.btnPesquisar);
        spinnerLoja = view.findViewById(R.id.spinnerLoja);

        txtData.setText(Contagem.getDataString(new Date()));
        txtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contagem contagem = (Contagem) adapterView.getItemAtPosition(i);

                Intent alterarContagem = new Intent(getContext(), AlterarDeletarContagem.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("contagem", contagem);

                alterarContagem.putExtras(bundle);

                startActivityForResult(alterarContagem, TELA_ALTERAR_DELETAR);
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

        btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = txtData.getText().toString();
                pesquisarContagemController.pesquisar(loja.getCnpj(), Contagem.getData(data), Contagem.getData(data));
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TELA_ALTERAR_DELETAR:
                if (resultCode == Activity.RESULT_OK) {
                    btnPesquisar.performClick();
                } else {
                    mensagemAoUsuario("A Contagem Não Foi Alterada");
                }
                break;
        }
    }
}