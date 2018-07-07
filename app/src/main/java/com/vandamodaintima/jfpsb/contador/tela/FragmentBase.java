package com.vandamodaintima.jfpsb.contador.tela;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.R;

public class FragmentBase extends Fragment {

    protected static View viewInflate;
    protected static Cursor cursorLista = null;

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

    @Override
    public void onDestroy() {
        if(cursorLista != null)
            cursorLista.close();

        super.onDestroy();
    }
}
