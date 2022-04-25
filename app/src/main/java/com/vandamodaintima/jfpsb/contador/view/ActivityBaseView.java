package com.vandamodaintima.jfpsb.contador.view;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;

import com.vandamodaintima.jfpsb.contador.R;

public abstract class ActivityBaseView extends AppCompatActivity {
    public static final String LOG = "Contador";
    protected ViewStub stub;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tela);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        stub = findViewById(R.id.layoutStub);
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
}
