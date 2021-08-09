package com.vandamodaintima.jfpsb.contador.model;

import org.threeten.bp.ZonedDateTime;

public class AModel {
    private ZonedDateTime criadoEm;
    private ZonedDateTime modificadoEm;
    private ZonedDateTime deletadoEm;
    private boolean deletado;

    public ZonedDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(ZonedDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public ZonedDateTime getModificadoEm() {
        return modificadoEm;
    }

    public void setModificadoEm(ZonedDateTime modificadoEm) {
        this.modificadoEm = modificadoEm;
    }

    public ZonedDateTime getDeletadoEm() {
        return deletadoEm;
    }

    public void setDeletadoEm(ZonedDateTime deletadoEm) {
        this.deletadoEm = deletadoEm;
    }

    public boolean isDeletado() {
        return deletado;
    }

    public void setDeletado(boolean deletado) {
        this.deletado = deletado;
    }
}
