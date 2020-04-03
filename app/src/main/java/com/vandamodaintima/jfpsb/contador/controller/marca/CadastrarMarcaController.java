package com.vandamodaintima.jfpsb.contador.controller.marca;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class CadastrarMarcaController {
    private CadastrarView view;
    private Marca marcaModel;

    public CadastrarMarcaController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        marcaModel = new Marca(conexaoBanco);
    }

    public Boolean cadastrar() {
        if (marcaModel.getNome().isEmpty()) {
            view.mensagemAoUsuario("Nome da Marca Não Pode Ser Vazio");
            return false;
        }

        Boolean result = marcaModel.salvar();

        if (result) {
            view.mensagemAoUsuario("Marca Cadastrada Com Sucesso");
            view.aposCadastro();
            return true;
        } else {
            view.mensagemAoUsuario("Erro ao Cadastrar Marca");
        }

        return false;
    }

    public Marca getMarca() {
        return marcaModel;
    }
}
