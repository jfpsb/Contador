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
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.contagem.PesquisarContagemController;
import com.vandamodaintima.jfpsb.contador.model.ContagemModel;
import com.vandamodaintima.jfpsb.contador.view.TelaPesquisa;

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
    private Loja loja;

    private PesquisarContagemController pesquisarContagemController;

    private SimpleDateFormat txtDataParaCalendar = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pesquisar_contagem, container, false);

        txtDataInicial = view.findViewById(R.id.txtDataInicial);
        txtDataFinal = view.findViewById(R.id.txtDataFinal);
        listView = view.findViewById(R.id.listViewLoja);
        btnPesquisar = view.findViewById(R.id.btnPesquisar);
        spinnerLoja = view.findViewById(R.id.spinnerLoja);

        sqLiteDatabase = new ConexaoBanco(getContext()).conexao();
        pesquisarContagemController = new PesquisarContagemController(this, sqLiteDatabase, getContext());

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
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                String cnpj = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

                loja = pesquisarContagemController.retornaLojaEscolhidaSpinner(cnpj);
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

        pesquisarContagemController.popularSpinnerLoja();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TELA_ALTERAR_DELETAR:
                if (resultCode == Activity.RESULT_OK) {
                    realizarPesquisa();
                } else {
                    mensagemAoUsuario("A ContagemModel Não Foi Alterada");
                }
                break;
        }
    }

    @Override
    public void setTextoQuantidadeBusca(int quantidade) {

    }

    @Override
    public void realizarPesquisa() {
        try {
            Calendar dataInicial = Calendar.getInstance();
            Calendar dataFinal = Calendar.getInstance();

            dataInicial.setTime(txtDataParaCalendar.parse(txtDataInicial.getText().toString()));
            dataFinal.setTime(txtDataParaCalendar.parse(txtDataFinal.getText().toString()));

            pesquisarContagemController.pesquisar(loja.getCnpj(), dataInicial, dataFinal);
        } catch (ParseException e) {
            Log.e(LOG, e.getMessage(), e);
        }
    }

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

        String loja = cursor.getString(cursor.getColumnIndexOrThrow("loja"));
        String data = cursor.getString(cursor.getColumnIndexOrThrow("data"));

        ContagemModel contagem = pesquisarContagemController.retornaContagemEscolhidaListView(loja, data);

        Intent alterarContagem = new Intent(getContext(), AlterarDeletarContagem.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("contagem", contagem);

        alterarContagem.putExtras(bundle);

        startActivityForResult(alterarContagem, TELA_ALTERAR_DELETAR);
    }

    public void setSpinnerLojaAdapter(SpinnerAdapter adapter) {
        spinnerLoja.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        SimpleCursorAdapter spinnerAdapter = (SimpleCursorAdapter) spinnerLoja.getAdapter();

        if (spinnerAdapter != null) {
            Cursor cursor = spinnerAdapter.getCursor();

            if (cursor != null)
                cursor.close();
        }

        super.onDestroy();
    }
}