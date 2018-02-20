package com.vandamodaintima.jfpsb.contador.tela;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;

import com.vandamodaintima.jfpsb.contador.MyPagerAdapter;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;

public class ActivityBase extends AppCompatActivity {

    protected ConexaoBanco conn;
    private ViewPager viewPager;
    private TabLayout tabLayout;

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

        setDAOs();
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

    protected void setDAOs() {

    }

    public ConexaoBanco getConn() {
        return conn;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDestroy() {
        conn.fechar();
        Log.i("Contador", "ONDESTROY -> " + this.getComponentName().toShortString());
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        if(!conn.conexao().isOpen()) {
            conn = new ConexaoBanco(getApplicationContext());
            setDAOs();
        }
        Log.i("Contador", "ONRESTART -> " + this.getComponentName().toShortString());
        super.onRestart();
    }

    @Override
    protected void onResume() {
        if(!conn.conexao().isOpen()) {
            conn = new ConexaoBanco(getApplicationContext());
            setDAOs();
        }
        Log.i("Contador", "ONRESUME -> " + this.getComponentName().toShortString());
        super.onResume();
    }

    @Override
    protected void onStop() {
//        conn.fechar();
        Log.i("Contador", "ONSTOP -> " + this.getComponentName().toShortString());
        super.onStop();
    }

    @Override
    protected void onPause() {
//        conn.fechar();
        Log.i("Contador", "ONPAUSE -> " + this.getComponentName().toShortString());
        super.onPause();
    }
}