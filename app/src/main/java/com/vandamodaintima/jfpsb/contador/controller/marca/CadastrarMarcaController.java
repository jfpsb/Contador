package com.vandamodaintima.jfpsb.contador.controller.marca;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.model.manager.MarcaManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class CadastrarMarcaController {
    private CadastrarView view;
    private MarcaManager marcaManager;

    public CadastrarMarcaController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        marcaManager = new MarcaManager(conexaoBanco);
    }

    public Boolean cadastrar(String nome) {
        if (nome.isEmpty()) {
            view.mensagemAoUsuario("Nome da Marca Não Pode Ser Vazio");
            return false;
        }

        marcaManager.getMarca().setNome(nome);

        Boolean result = marcaManager.salvar();

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
        return marcaManager.getMarca();
    }
}
