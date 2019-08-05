package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.FornecedorModel;
import com.vandamodaintima.jfpsb.contador.model.MarcaModel;
import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;
import com.vandamodaintima.jfpsb.contador.view.produto.CadastrarProduto;

import java.util.ArrayList;

public class CadastrarProdutoController {

    CadastrarProduto view;
    ProdutoModel produtoModel;
    FornecedorModel fornecedorModel;
    MarcaModel marcaModel;
    ConexaoBanco conexaoBanco;

    public CadastrarProdutoController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = (CadastrarProduto) view;
        this.conexaoBanco = conexaoBanco;
        produtoModel = new ProdutoModel(conexaoBanco);
        fornecedorModel = new FornecedorModel(conexaoBanco);
        marcaModel = new MarcaModel(conexaoBanco);
    }

    public void cadastrar(String cod_barra, String descricao, Double preco) {
        if (cod_barra.isEmpty()) {
            view.mensagemAoUsuario("Código de Barra Não Pode Estar Vazio");
            return;
        }

        if (descricao.isEmpty()) {
            view.mensagemAoUsuario("Descrição Não Pode Estar Vazio");
            return;
        }

        if (preco == 0) {
            view.mensagemAoUsuario("Preço Não Pode Ser Zero");
            return;
        }

        produtoModel.setFornecedor(fornecedorModel);
        produtoModel.setMarca(marcaModel);

        Boolean result = produtoModel.inserir();

        if (result) {
            view.mensagemAoUsuario("Produto Cadastro Com Sucesso");
            view.aposCadastro(produtoModel.getCod_barra());
        } else {
            view.mensagemAoUsuario("Produto Não Foi Cadastrado");
        }
    }

    public void checaCodigoBarra(String codigo) {
        if (!codigo.isEmpty()) {
            ProdutoModel produto = produtoModel.listarPorId(codigo);

            if (produto != null) {
                view.bloqueiaCampos();
            } else {
                view.liberaCampos();
            }
        }
    }

    public String getCodBarra() {
        return produtoModel.getCod_barra();
    }

    public ArrayList<String> getCodBarraFornecedor() {
        return produtoModel.getCod_barra_fornecedor();
    }

    public void setCodBarraFornecedor(ArrayList<String> codigos) {
        produtoModel.setCod_barra_fornecedor(codigos);
    }

    public void addCodBarraFornecedor(String codigo) {
        produtoModel.getCod_barra_fornecedor().add(codigo);
    }

    public void carregaFornecedor(String id) {
        fornecedorModel.load(id);
    }

    public void carregaMarca(String id) {
        marcaModel.load(id);
    }

    public String getFornecedorNome() {
        return fornecedorModel.getNome();
    }

    public String getMarcaNome() {
        return marcaModel.getNome();
    }

    public void setFornecedorNull() {
        fornecedorModel = null;
    }

    public void setMarcaNull() {
        marcaModel = null;
    }

    public void resetaProduto() {
        produtoModel = new ProdutoModel(conexaoBanco);
    }
}
