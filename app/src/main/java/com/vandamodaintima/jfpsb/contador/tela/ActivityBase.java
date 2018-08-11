package com.vandamodaintima.jfpsb.contador.tela;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;

import org.apache.poi.ss.formula.eval.NotImplementedException;

public class ActivityBase extends AppCompatActivity {
    protected ConexaoBanco conn;
    public static final String LOG = "Contador";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tela);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        conn = new ConexaoBanco(getApplicationContext());

        setManagers();
    }

    protected void setManagers() {
        throw new NotImplementedException("Sobrescreva setManagers nesta Activity!");
    }

    public ConexaoBanco getConn() {
        return conn;
    }

    public void showDatePicker(View v) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setView(v);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDestroy() {
        conn.fechar();
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        if (!conn.conexao().isOpen()) {
            conn = new ConexaoBanco(getApplicationContext());
            setManagers();
        }

        super.onRestart();
    }

    @Override
    protected void onResume() {
        if (!conn.conexao().isOpen()) {
            conn = new ConexaoBanco(getApplicationContext());
            setManagers();
        }

        super.onResume();
    }
}
