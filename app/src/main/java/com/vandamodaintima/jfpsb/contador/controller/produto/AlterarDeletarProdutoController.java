package com.vandamodaintima.jfpsb.contador.controller.produto;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.model.manager.MarcaManager;
import com.vandamodaintima.jfpsb.contador.model.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

import java.util.ArrayList;

public class AlterarDeletarProdutoController {
    private AlterarDeletarView view;
    private ConexaoBanco conexaoBanco;
    private ProdutoManager produtoManager;

    public AlterarDeletarProdutoController(AlterarDeletarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        produtoManager = new ProdutoManager(conexaoBanco);
    }

    public void atualizar(String descricao, double preco) {
        if (descricao.trim().isEmpty()) {
            view.mensagemAoUsuario("Descrição do Produto Não Pode Ser Vazia");
            return;
        }

        if (preco == 0) {
            view.mensagemAoUsuario("Preço do Produto Não Pode Ser Zero");
            return;
        }

        produtoManager.getProduto().setDescricao(descricao.trim().toUpperCase());
        produtoManager.getProduto().setPreco(preco);

        Boolean result = produtoManager.atualizar(produtoManager.getProduto().getCod_barra());

        if (result) {
            view.mensagemAoUsuario("Produto Atualizado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Produto");
        }
    }

    public void deletar() {
        Boolean result = produtoManager.deletar();

        if (result) {
            view.mensagemAoUsuario("Produto Deletado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Produto");
        }
    }

    public void carregaProduto(String id) {
        produtoManager.load(id);
    }

    public void carregaFornecedor(Fornecedor fornecedor) {
        produtoManager.getProduto().setFornecedor(fornecedor);
    }

    public void carregaMarca(Marca marca) {
        produtoManager.getProduto().setMarca(marca);
    }

    public ArrayList<String> getCodBarraFornecedor() {
        return produtoManager.getProduto().getCod_barra_fornecedor();
    }

    public void setCodBarraFornecedor(ArrayList<String> codigos) {
        produtoManager.getProduto().setCod_barra_fornecedor(codigos);
    }

    public void setFornecedorNull() {
        produtoManager.getProduto().setFornecedor(null);
    }

    public void setMarcaNull() {
        produtoManager.getProduto().setMarca(null);
    }

    public Produto getProduto() {
        return produtoManager.getProduto();
    }
}
