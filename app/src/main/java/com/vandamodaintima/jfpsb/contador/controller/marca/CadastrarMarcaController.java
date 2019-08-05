package com.vandamodaintima.jfpsb.contador.controller.marca;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.MarcaModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class CadastrarMarcaController {
    private CadastrarView view;
    private MarcaModel marcaModel;

    public CadastrarMarcaController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        marcaModel = new MarcaModel(conexaoBanco);
    }

    public Boolean cadastrar(String nome) {
        if(nome.isEmpty()) {
            view.mensagemAoUsuario("Nome da Marca Não Pode Ser Vazio");
            return false;
        }

        marcaModel.setNome(nome);

        Boolean result = marcaModel.inserir();

        if(result) {
            view.mensagemAoUsuario("Marca Cadastrada Com Sucesso");
            view.aposCadastro();
            return true;
        } else {
            view.mensagemAoUsuario("Erro ao Cadastrar Marca");
        }

        return false;
    }

    public String getNome() {
        return marcaModel.getNome();
    }
}
