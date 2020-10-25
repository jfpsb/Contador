package com.vandamodaintima.jfpsb.contador.controller.marca;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.IController;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class CadastrarMarcaController implements IController {
    private ConexaoBanco conexaoBanco;
    private CadastrarView view;
    private Marca marcaModel;

    public CadastrarMarcaController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        marcaModel = new Marca(conexaoBanco);
    }

    public void salvar() {
        if (marcaModel.getNome().isEmpty()) {
            view.mensagemAoUsuario("Nome da Marca Não Pode Ser Vazio");
            return;
        }

        Boolean result = marcaModel.salvar();

        if (result) {
            view.mensagemAoUsuario("Marca Cadastrada Com Sucesso");
            view.aposCadastro();
        } else {
            view.mensagemAoUsuario("Erro ao Cadastrar Marca");
        }
    }

    public Marca getMarca() {
        return marcaModel;
    }

    public void setFornecedorNull() {
        marcaModel.setFornecedor(null);
    }

    public void carregaFornecedor(Fornecedor fornecedor) {
        marcaModel.setFornecedor(fornecedor);
    }

    @Override
    public void reset() {
        marcaModel = new Marca(conexaoBanco);
    }
}
