package com.vandamodaintima.jfpsb.contador.tela;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
