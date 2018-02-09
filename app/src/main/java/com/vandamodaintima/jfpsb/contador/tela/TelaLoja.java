package com.vandamodaintima.jfpsb.contador.tela;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.style.BulletSpan;
import android.view.ViewStub;
import android.view.WindowManager;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.tela.manager.CadastrarFornecedor;
import com.vandamodaintima.jfpsb.contador.MyPagerAdapter;
import com.vandamodaintima.jfpsb.contador.tela.manager.CadastrarLoja;
import com.vandamodaintima.jfpsb.contador.tela.manager.PesquisarFornecedor;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.tela.manager.PesquisarLoja;

public class TelaLoja extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ConexaoBanco conn;
    private CadastrarLoja telaCadastrarLoja;
    private PesquisarLoja telaPesquisarLoja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_tela_loja);
        stub.inflate();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        conn = new ConexaoBanco(this.getApplicationContext());

        //PesquisaTab
        TabLayout.Tab pesquisaTab = tabLayout.newTab();
        pesquisaTab.setText("Pesquisar");
        tabLayout.addTab(pesquisaTab);

        //CadastraTab
        TabLayout.Tab cadastraTab = tabLayout.newTab();
        cadastraTab.setText("Cadastrar");
        tabLayout.addTab(cadastraTab);

        MyPagerAdapter adapter = new MyPagerAdapter (getSupportFragmentManager());

        telaCadastrarLoja = new CadastrarLoja();
        telaPesquisarLoja = new PesquisarLoja();

        telaCadastrarLoja.setConn(conn);
        telaPesquisarLoja.setConn(conn);

        adapter.addFragment(telaPesquisarLoja, "Pesquisar");
        adapter.addFragment(telaCadastrarLoja, "Cadastrar");

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

}
