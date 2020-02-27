package com.vandamodaintima.jfpsb.contador.view.contagem;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.contagem.PesquisarContagemController;
import com.vandamodaintima.jfpsb.contador.controller.loja.SpinnerLojaAdapter;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.view.TelaPesquisa;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.vandamodaintima.jfpsb.contador.view.ActivityBaseView.LOG;

public class PesquisarContagem extends TelaPesquisa {

    private EditText txtDataInicial;
    private EditText txtDataFinal;
    private Spinner spinnerLoja;
    private Button btnPesquisar;

    private PesquisarContagemController controller;

    private SimpleDateFormat txtDataParaCalendar = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        telaPesquisaView = inflater.inflate(R.layout.fragment_pesquisar_contagem, container, false);

        txtDataInicial = telaPesquisaView.findViewById(R.id.txtDataInicial);
        txtDataFinal = telaPesquisaView.findViewById(R.id.txtDataFinal);
        listView = telaPesquisaView.findViewById(R.id.listViewContagem);
        btnPesquisar = telaPesquisaView.findViewById(R.id.btnPesquisar);
        spinnerLoja = telaPesquisaView.findViewById(R.id.spinnerLoja);

        conexaoBanco = new ConexaoBanco(getContext());
        controller = new PesquisarContagemController(this, conexaoBanco);

        txtDataInicial.setText(txtDataParaCalendar.format(new Date()));
        txtDataInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });

        txtDataFinal.setText(txtDataParaCalendar.format(new Date()));
        txtDataFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });

        spinnerLoja.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = spinnerLoja.getSelectedItem();
                controller.carregaLoja(o);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realizarPesquisa();
            }
        });

        ArrayAdapter spinnerAdapter = new SpinnerLojaAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, controller.getLojas());
        spinnerLoja.setAdapter(spinnerAdapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TELA_ALTERAR_DELETAR:
                if (resultCode == Activity.RESULT_OK) {
                    realizarPesquisa();
                } else {
                    mensagemAoUsuario("A Contagem Não Foi Alterada");
                }
                break;
        }
    }

    @Override
    public void setTextoQuantidadeBusca(int quantidade) {

    }

    @Override
    public void realizarPesquisa() {
        LocalDateTime dataInicial = LocalDateTime.parse(txtDataInicial.getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDateTime dataFinal = LocalDateTime.parse(txtDataFinal.getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        controller.pesquisar(dataInicial, dataFinal);
    }

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
        Contagem contagem = (Contagem) adapterView.getItemAtPosition(i);

        Intent alterarContagem = new Intent(getContext(), AlterarDeletarContagem.class);
        alterarContagem.putExtra("loja", contagem.getLoja().getCnpj());
        alterarContagem.putExtra("data", contagem.getDataParaSQLite());
        startActivityForResult(alterarContagem, TELA_ALTERAR_DELETAR);
    }
}