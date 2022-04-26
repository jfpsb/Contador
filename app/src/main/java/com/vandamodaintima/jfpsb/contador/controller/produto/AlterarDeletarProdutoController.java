package com.vandamodaintima.jfpsb.contador.controller.produto;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarProdutoController {
    private AlterarDeletarView view;
    private ConexaoBanco conexaoBanco;
    private Produto model;

    public AlterarDeletarProdutoController(AlterarDeletarView view, ConexaoBanco conexaoBanco, String idProduto) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        model = new Produto(conexaoBanco);
        model.load(idProduto);
    }

    public void atualizar() {
        if (model.getDescricao().trim().isEmpty()) {
            view.mensagemAoUsuario("Descrição do Produto Não Pode Ser Vazia");
            return;
        }

        Boolean result = model.atualizar();

        if (result) {
            view.mensagemAoUsuario("Produto Atualizado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Produto");
        }
    }

    public void deletar() {
        Boolean result = model.deletar();
        if (result) {
            view.mensagemAoUsuario("Produto Deletado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Produto");
        }
    }

    public void setFornecedor(Fornecedor fornecedor) {
        model.setFornecedor(fornecedor);
    }

    public void setMarca(Marca marca) {
        model.setMarca(marca);
    }

    public void setFornecedorNull() {
        model.setFornecedor(null);
    }

    public void setMarcaNull() {
        model.setMarca(null);
    }

    public Produto getProduto() {
        return model;
    }
}
