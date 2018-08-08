package com.vandamodaintima.jfpsb.contador.tela;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vandamodaintima.jfpsb.contador.R;

/**
 * Fragment base para todas as fragments do app
 */
public abstract class FragmentBase extends Fragment {
    protected static View viewInflate;

    public FragmentBase() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layout = savedInstanceState.getInt("layout");

        viewInflate = inflater.inflate(layout, container, false);

        setManagers();
        setViews();

        return viewInflate;
    }

    /**
     * Mostra fragment para escolher data. Comum em algumas fragments então foi colocada aqui.
     *
     * @param v View que ativa método
     */
    public void showDatePicker(View v) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setView(v);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    protected abstract void setManagers();
    protected abstract void setViews();

    @Override
    public void onResume() {
        setManagers();
        super.onResume();
    }
}
