package com.vandamodaintima.jfpsb.contador.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Representante extends AModel implements Serializable, IModel<Representante> {
    private UUID id;
    private String nome;
    private String whatsapp;
    private String cidadeestado;
    private String email;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getCidadeestado() {
        return cidadeestado;
    }

    public void setCidadeestado(String cidadeestado) {
        this.cidadeestado = cidadeestado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Object getIdentifier() {
        return id;
    }

    @Override
    public String getDeleteWhereClause() {
        return "id = ?";
    }

    @Override
    public Boolean salvar() {
        return null;
    }

    @Override
    public Boolean salvar(List<Representante> lista) {
        return null;
    }

    @Override
    public Boolean atualizar() {
        return null;
    }

    @Override
    public Boolean deletar() {
        return null;
    }

    @Override
    public List<Representante> listar() {
        return null;
    }

    @Override
    public Representante listarPorId(Object... ids) {
        return null;
    }

    @Override
    public void load(Object... ids) {

    }
}
