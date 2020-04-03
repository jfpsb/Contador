package com.vandamodaintima.jfpsb.contador.controller.loja;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.model.manager.LojaManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

import java.util.ArrayList;

public class CadastrarLojaController {
    private CadastrarView view;
    private LojaManager lojaManager;
    private ConexaoBanco conexaoBanco;

    public CadastrarLojaController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        lojaManager = new LojaManager(conexaoBanco);
    }

    public void cadastrar(String cnpj, String nome, String telefone, String endereco, String inscricaoEstadual) {
        if (cnpj.isEmpty()) {
            view.mensagemAoUsuario("CNPJ Não Pode Estar Vazio");
            return;
        }

        if (nome.isEmpty()) {
            view.mensagemAoUsuario("Nome Não Pode Estar Vazio");
            return;
        }

        if (lojaManager.getLoja().getMatriz().getCnpj().equals("0")) {
            lojaManager.getLoja().setMatriz(null);
        }

        lojaManager.getLoja().setCnpj(cnpj);
        lojaManager.getLoja().setNome(nome);
        lojaManager.getLoja().setTelefone(telefone);
        lojaManager.getLoja().setEndereco(endereco);
        lojaManager.getLoja().setInscricaoEstadual(inscricaoEstadual);

        Boolean result = lojaManager.salvar();

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
        matrizes.addAll(lojaManager.listarMatrizes());

        return matrizes;
    }

    public void resetaLoja() {
        lojaManager.resetaModelo();
    }

    public void carregaMatriz(Object o) {
        if (o instanceof Loja)
            lojaManager.getLoja().setMatriz((Loja) o);
    }
}
