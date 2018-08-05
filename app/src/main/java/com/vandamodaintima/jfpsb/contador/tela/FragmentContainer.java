package com.vandamodaintima.jfpsb.contador.tela;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.R;

/**
 * Serve como classe base para as Activities containers de Fragments
 */
public abstract class FragmentContainer extends ActivityBase {
    private Fragment fragment; //Fragment que será hosteada pela activity

    public FragmentContainer(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_fragment_container_for_activity);
        stub.inflate();

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Adiciona fragment na activity
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void setManagers() {

    }

    public abstract void setResultCadastro(Object object);

    protected Fragment getFragment() {
        return fragment;
    }
}
