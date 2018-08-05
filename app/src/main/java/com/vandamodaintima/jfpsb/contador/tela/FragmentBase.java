package com.vandamodaintima.jfpsb.contador.tela;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment base para todas as fragments do app
 */
public abstract class FragmentBase extends Fragment {
    protected static View viewInflate;

    public FragmentBase() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return viewInflate;
    }

    /**
     * Mostra fragment para escolher data. Comum em algumas fragments então foi colocada aqui.
     * onClick é atribuído no layout xml
     *
     * @param v
     */
    public void showDatePicker(View v) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setView(v);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    protected abstract void setManagers();

    @Override
    public void onResume() {
        setManagers();
        super.onResume();
    }
}
