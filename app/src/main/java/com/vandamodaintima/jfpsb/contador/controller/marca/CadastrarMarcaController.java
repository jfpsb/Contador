package com.vandamodaintima.jfpsb.contador.controller.marca;

import com.vandamodaintima.jfpsb.contador.model.MarcaModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class CadastrarMarcaController {
    private CadastrarView view;

    public CadastrarMarcaController(CadastrarView view) {
        this.view = view;
    }

    public Boolean cadastrar(MarcaModel marca) {
        if(marca.getNome().isEmpty()) {
            view.mensagemAoUsuario("Nome da Marca Não Pode Ser Vazio");
            return false;
        }

        Boolean result = marca.inserir();

        if(result) {
            view.mensagemAoUsuario("Marca Cadastrada Com Sucesso");
            view.aposCadastro();
            view.limparCampos();
            return true;
        } else {
            view.mensagemAoUsuario("Erro ao Cadastrar Marca");
        }

        return false;
    }
}
