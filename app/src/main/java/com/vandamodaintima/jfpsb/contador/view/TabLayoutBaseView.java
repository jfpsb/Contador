package com.vandamodaintima.jfpsb.contador.view;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.vandamodaintima.jfpsb.contador.MyPagerAdapter2;
import com.vandamodaintima.jfpsb.contador.R;

public class TabLayoutBaseView extends ActivityBaseView {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private MyPagerAdapter2 adapter;

    private String[] headers;

    public TabLayoutBaseView() {
        //default constructor
    }

    /**
     * Construtor para activities que possuem abas (TabLayout)
     *
     * @param headers Nomes das abas, na ordem em que devem aparecer
     */
    public TabLayoutBaseView(String[] headers) {
        this.headers = headers;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Método que deve ser chamado na classe filha para inserir as fragments que ficarão nas abas
     *
     * @param fragments Fragments, na ordem que devem ser inseridas no tablayout
     */
    protected void setViewPagerTabLayout(Fragment... fragments) {
        try {
            if (headers == null)
                throw new Exception("Informe no Construtor os Nomes da Abas!");

            if (headers.length != fragments.length)
                throw new Exception("O Número de Fragments a Serem Adicionadas Não Correspondem ao Número Informado de Nomes de Abas");

            viewPager = findViewById(R.id.viewPager);
            tabLayout = findViewById(R.id.tabLayout);

            adapter = new MyPagerAdapter2(getSupportFragmentManager(), getLifecycle());

            for (int i = 0; i < headers.length; i++) {
                TabLayout.Tab tab = tabLayout.newTab();
                tab.setText(headers[i]);
                tabLayout.addTab(tab);
                adapter.addFragment(fragments[i]);
            }

            viewPager.setAdapter(adapter);

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

            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, true, (tab, position) -> {
                tab.setText(headers[position]);
                viewPager.setCurrentItem(position, true);
            });

            tabLayoutMediator.attach();

            viewPager.setCurrentItem(0);

        } catch (Exception e) {
            Log.e(LOG, e.getMessage(), e);
        }
    }

    public MyPagerAdapter2 getPagerAdapter() {
        return adapter;
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }
}