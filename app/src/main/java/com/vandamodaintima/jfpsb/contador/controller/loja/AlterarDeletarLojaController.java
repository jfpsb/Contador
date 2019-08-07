package com.vandamodaintima.jfpsb.contador.controller.loja;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.LojaModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

import java.util.ArrayList;

public class AlterarDeletarLojaController {
    private AlterarDeletarView view;
    private ConexaoBanco conexaoBanco;
    private LojaModel lojaModel;
    private LojaModel matriz;

    public AlterarDeletarLojaController(AlterarDeletarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        lojaModel = new LojaModel(conexaoBanco);
        matriz = new LojaModel(conexaoBanco);
    }

    public void atualizar(String nome) {
        if(nome.trim().isEmpty()) {
            view.mensagemAoUsuario("Nome Não Pode Ser Vazio");
            return;
        }

        if(matriz.getCnpj().equals("0")) {
            lojaModel.setMatriz(null);
        }
        else {
            lojaModel.setMatriz(matriz);
        }

        lojaModel.setNome(nome);

        Boolean result = lojaModel.atualizar();

        if(result) {
            view.mensagemAoUsuario("Loja Atualizada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Loja");
        }
    }

    public void deletar() {
        Boolean result = lojaModel.deletar();

        if(result) {
            view.mensagemAoUsuario("Loja Deletada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Loja");
        }
    }

    public ArrayList<LojaModel> getMatrizes() {
        ArrayList<LojaModel> matrizes = new ArrayList<>();
        LojaModel loja = new LojaModel(conexaoBanco);
        loja.setCnpj("0");
        loja.setNome("SEM MATRIZ");

        matrizes.add(loja);
        matrizes.addAll(lojaModel.listarMatrizes());

        return matrizes;
    }

    public void carregaMatriz(Object o) {
        if(o instanceof LojaModel)
            matriz = (LojaModel)o;
    }

    public int getMatrizIndex(String cnpj) {
        int index = 0, aux = 1;

        for (LojaModel loja : lojaModel.listarMatrizes()) {
            if(loja.getCnpj().equals(cnpj)) {
                index = aux;
                break;
            }
            aux++;
        }

        return index;
    }

    public void carregaLoja(String id) {
        lojaModel.load(id);
        if(lojaModel.getMatriz() != null)
            matriz = lojaModel.getMatriz();
    }

    public LojaModel getMatriz() {
        return matriz;
    }

    public String getCnpj() {
        return lojaModel.getCnpj();
    }

    public String getNome() {
        return lojaModel.getNome();
    }
}
