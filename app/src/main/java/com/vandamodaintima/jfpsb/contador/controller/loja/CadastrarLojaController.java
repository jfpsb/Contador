package com.vandamodaintima.jfpsb.contador.controller.loja;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.LojaModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

import java.util.ArrayList;

public class CadastrarLojaController {
    private CadastrarView view;
    private LojaModel lojaModel;
    private LojaModel matriz = null;
    private ConexaoBanco conexaoBanco;

    public CadastrarLojaController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        lojaModel = new LojaModel(conexaoBanco);
    }

    public void cadastrar(String cnpj, String nome) {
        if (cnpj.isEmpty()) {
            view.mensagemAoUsuario("CNPJ Não Pode Estar Vazio");
            return;
        }

        if (nome.isEmpty()) {
            view.mensagemAoUsuario("Nome Não Pode Estar Vazio");
            return;
        }

        lojaModel.setMatriz(matriz);

        Boolean result = lojaModel.inserir();

        if (result) {
            view.mensagemAoUsuario("Loja Cadastrada Com Sucesso!");
            view.aposCadastro();
            view.limparCampos();
        } else {
            view.mensagemAoUsuario("Erro Ao Cadastrar Loja");
        }
    }

    public void setMatriz(LojaModel matriz) {
        this.matriz = matriz;
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

    public void resetaLoja() {
        lojaModel = new LojaModel(conexaoBanco);
        matriz = null;
    }
}
