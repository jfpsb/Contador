package com.vandamodaintima.jfpsb.contador.controller.produto;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;
import com.vandamodaintima.jfpsb.contador.view.produto.CadastrarProduto;

public class CadastrarProdutoController {

    private CadastrarProduto view;
    private ProdutoManager produtoManager;
    ConexaoBanco conexaoBanco;

    public CadastrarProdutoController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = (CadastrarProduto) view;
        this.conexaoBanco = conexaoBanco;
        produtoManager = new ProdutoManager(conexaoBanco);
    }

    public void cadastrar(String cod_barra, String descricao, Double preco) {
        if (cod_barra.trim().isEmpty()) {
            view.mensagemAoUsuario("Código de Barra Não Pode Estar Vazio");
            return;
        }

        if (descricao.trim().isEmpty()) {
            view.mensagemAoUsuario("Descrição Não Pode Estar Vazio");
            return;
        }

        if (preco == 0) {
            view.mensagemAoUsuario("Preço Não Pode Ser Zero");
            return;
        }

        produtoManager.getProduto().setCod_barra(cod_barra);
        produtoManager.getProduto().setDescricao(descricao.trim().toUpperCase());
        produtoManager.getProduto().setPreco(preco);

        Boolean result = produtoManager.salvar();

        if (result) {
            view.mensagemAoUsuario("Produto Cadastro Com Sucesso");
            view.aposCadastro(produtoManager.getProduto().getCod_barra());
        } else {
            view.mensagemAoUsuario("Produto Não Foi Cadastrado");
        }
    }

    public void checaCodigoBarra(String codigo) {
        if (!codigo.isEmpty()) {
            Produto produto = produtoManager.listarPorId(codigo);

            if (produto != null) {
                view.bloqueiaCampos();
            } else {
                view.liberaCampos();
            }
        }
    }

    public void carregaFornecedor(Fornecedor fornecedor) {
        produtoManager.getProduto().setFornecedor(fornecedor);
    }

    public void carregaMarca(Marca marca) {
        produtoManager.getProduto().setMarca(marca);
    }

    public void setFornecedorNull() {
        produtoManager.getProduto().setFornecedor(null);
    }

    public void setMarcaNull() {
        produtoManager.getProduto().setMarca(null);
    }

    public void resetaProduto() {
        produtoManager.resetaModelo();
    }

    public Produto getProduto() {
        return produtoManager.getProduto();
    }
}
