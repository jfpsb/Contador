package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentContainer;

import org.apache.poi.ss.formula.eval.NotImplementedException;

/**
 * PesquisaContagemProduto é uma Fragment filha de PesquisarProduto. Esta
 * activity serve como container de PesquisaContagemProduto
 */
public class PesquisaContagemProdutoContainer extends FragmentContainer {

    public PesquisaContagemProdutoContainer() {
        super(new PesquisaContagemProduto());
    }

    @Override
    public void setResultCadastro(Object object) {
        throw new NotImplementedException("Você Não Devia Usar Esse Método");
    }
}
