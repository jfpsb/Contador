package com.vandamodaintima.jfpsb.contador.tela;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.vandamodaintima.jfpsb.contador.MyPagerAdapter;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;

public class ActivityBase extends AppCompatActivity {

    protected ConexaoBanco conn;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    protected Cursor cursorLista = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    protected void setViewPagerTabLayout(Fragment telaPesquisar, Fragment telaCadastrar) {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        MyPagerAdapter adapter = new MyPagerAdapter (getSupportFragmentManager());

        //PesquisaTab
        TabLayout.Tab pesquisaTab = tabLayout.newTab();
        pesquisaTab.setText("Pesquisar");
        tabLayout.addTab(pesquisaTab);

        //CadastraTab
        TabLayout.Tab cadastraTab = tabLayout.newTab();
        cadastraTab.setText("Cadastrar");
        tabLayout.addTab(cadastraTab);

        adapter.addFragment(telaPesquisar, "Pesquisar");
        adapter.addFragment(telaCadastrar, "Cadastrar");

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    protected void setManagers() {

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

        if(cursorLista != null)
            cursorLista.close();

        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        if(!conn.conexao().isOpen()) {
            conn = new ConexaoBanco(getApplicationContext());
            setManagers();
        }

        super.onRestart();
    }

    @Override
    protected void onResume() {
        if(!conn.conexao().isOpen()) {
            conn = new ConexaoBanco(getApplicationContext());
            setManagers();
        }

        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}