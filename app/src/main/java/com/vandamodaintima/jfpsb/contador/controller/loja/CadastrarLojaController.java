package com.vandamodaintima.jfpsb.contador.controller.loja;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

import java.util.ArrayList;

public class CadastrarLojaController {
    private CadastrarView view;
    private Loja lojaModel;
    private ConexaoBanco conexaoBanco;

    public CadastrarLojaController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        lojaModel = new Loja(conexaoBanco);
    }

    public void cadastrar() {
        if (lojaModel.getCnpj().isEmpty()) {
            view.mensagemAoUsuario("CNPJ Não Pode Estar Vazio");
            return;
        }

        if (lojaModel.getNome().isEmpty()) {
            view.mensagemAoUsuario("Nome Não Pode Estar Vazio");
            return;
        }

        if (lojaModel.getMatriz().getCnpj().equals("0")) {
            lojaModel.setMatriz(null);
        }

        Boolean result = lojaModel.salvar();

        if (result) {
            view.mensagemAoUsuario("Loja Cadastrada Com Sucesso!");
            view.aposCadastro();
            view.limparCampos();
        } else {
            view.mensagemAoUsuario("Erro Ao Cadastrar Loja");
        }
    }

    public ArrayList<Loja> getMatrizes() {
        ArrayList<Loja> matrizes = new ArrayList<>();
        Loja loja = new Loja();
        loja.setCnpj("0");
        loja.setNome("SEM MATRIZ");

        matrizes.add(loja);
        matrizes.addAll(lojaModel.listarMatrizes());

        return matrizes;
    }

    public void resetaLoja() {
        lojaModel = new Loja(conexaoBanco);
    }

    public void carregaMatriz(Object o) {
        if (o instanceof Loja)
            lojaModel.setMatriz((Loja) o);
    }

    public Loja getLoja() {
        return lojaModel;
    }
}
