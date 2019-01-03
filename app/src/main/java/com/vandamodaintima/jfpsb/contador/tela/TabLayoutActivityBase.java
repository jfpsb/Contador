package com.vandamodaintima.jfpsb.contador.tela;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.MyPagerAdapter;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

public class TabLayoutActivityBase extends ActivityBaseView {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MyPagerAdapter adapter;

    private String[] headers;

    /**
     * Construtor para activities que possuem abas (TabLayout)
     *
     * @param headers Nomes das abas, na ordem em que devem aparecer
     */
    public TabLayoutActivityBase(String[] headers) {
        this.headers = headers;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setViewPagerTabLayout(Fragment... fragments) {
        try {
            if (headers == null)
                throw new Exception("Informe no Construtor os Nomes da Abas!");

            if (headers.length != fragments.length)
                throw new Exception("O Número de Fragments a Serem Adicionadas Não Correspondem ao Número Informado de Nomes de Abas");

            viewPager = findViewById(R.id.viewPager);
            tabLayout = findViewById(R.id.tabLayout);

            adapter = new MyPagerAdapter(getSupportFragmentManager());

            for (int i = 0; i < headers.length; i++) {
                TabLayout.Tab tab = tabLayout.newTab();
                tab.setText(headers[i]);
                tabLayout.addTab(tab);

                adapter.addFragment(fragments[i], headers[i]);
            }

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
        } catch (Exception e) {
            Log.e(LOG, e.getMessage(), e);
        }
    }

    public MyPagerAdapter getPagerAdapter() {
        return adapter;
    }
}