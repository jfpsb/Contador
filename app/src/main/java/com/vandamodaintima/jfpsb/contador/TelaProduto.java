package com.vandamodaintima.jfpsb.contador;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class TelaProduto extends Fragment {

    MyAdapter adapter;

    private void setupViewPager(ViewPager viewPager) {
        adapter = new MyAdapter(getChildFragmentManager());
        adapter.addFragment(new CadastroProduto(), "Cadastro");
        adapter.addFragment(new PesquisaProduto(), "Pesquisa");
        viewPager.setAdapter(adapter);
    }

    public TelaProduto() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tela_produto, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        setupViewPager(viewPager);

        TabLayout tabs = (TabLayout) view.findViewById(R.id.sliding_tabs);

        tabs.setupWithViewPager(viewPager);

        return view;
    }
}
