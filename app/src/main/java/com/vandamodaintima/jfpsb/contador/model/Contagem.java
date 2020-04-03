package com.vandamodaintima.jfpsb.contador.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.io.Serializable;
import java.util.List;

public class Contagem implements Serializable, IModel<Contagem> {
    @SerializedName(value = "Loja")
    private Loja loja;
    @SerializedName(value = "Data")
    private ZonedDateTime data;
    @SerializedName(value = "Finalizada")
    private Boolean finalizada;
    @SerializedName(value = "TipoContagem")
    private TipoContagem tipoContagem;

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public String getFullDataString() {
        return data.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    public String getFullDataStringForFileName() {
        return data.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH.mm.ss"));
    }

    public String getShortDataString() {
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getDataParaSQLite() {
        return data.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static ZonedDateTime convertStringToDate(String s) {
        LocalDateTime localDateTime = LocalDateTime.parse(s, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return localDateTime.atZone(ZoneId.systemDefault());
    }

    public ZonedDateTime getData() {
        return data;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public Boolean getFinalizada() {
        return finalizada;
    }

    public void setFinalizada(Boolean finalizada) {
        this.finalizada = finalizada;
    }

    public TipoContagem getTipoContagem() {
        return tipoContagem;
    }

    public void setTipoContagem(TipoContagem tipoContagem) {
        this.tipoContagem = tipoContagem;
    }

    public static String[] getColunas() {
        return new String[]{"ROWID as _id", "loja", "data", "finalizada", "tipo"};
    }

    public static String getDataSQLite(ZonedDateTime data) {
        return data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public Object getIdentifier() {
        return new String[] { loja.getCnpj(), getDataParaSQLite()};
    }

    @Override
    public String getDeleteWhereClause() {
        return "loja = ? AND data = ?";
    }

    @Override
    public Boolean salvar() {
        return null;
    }

    @Override
    public Boolean salvar(List<Contagem> lista) {
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
    public List<Contagem> listar() {
        return null;
    }

    @Override
    public Contagem listarPorId(Object... ids) {
        return null;
    }

    @Override
    public void load(Object... ids) {

    }
}
