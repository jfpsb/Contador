package com.vandamodaintima.jfpsb.contador.tela;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.R;

public class FragmentBase extends Fragment {

    protected static View viewInflate;


    public FragmentBase() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return viewInflate;
    }

    protected void setDAOs() {

    }

    @Override
    public void onResume() {
        setDAOs();
        super.onResume();
    }
}
