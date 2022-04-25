package com.vandamodaintima.jfpsb.contador.model;

import com.google.gson.annotations.SerializedName;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagem;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Contagem extends AModel implements Serializable, IModel<Contagem> {
    private transient DAOContagem daoContagem;

    public Contagem() {
    }

    public Contagem(ConexaoBanco conexaoBanco) {
        daoContagem = new DAOContagem(conexaoBanco);
    }

    @SerializedName(value = "Id")
    private UUID id;
    @SerializedName(value = "Loja")
    private Loja loja;
    @SerializedName(value = "Data")
    private ZonedDateTime data;
    @SerializedName(value = "Finalizada")
    private Boolean finalizada;
    @SerializedName(value = "TipoContagem")
    private TipoContagem tipoContagem;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
        return new String[]{"uuid as _id", "loja", "data", "finalizada", "tipo", "deletado"};
    }

    public static String[] getHeaders() {
        return new String[0];
    }

    public static String getDataSQLite(ZonedDateTime data) {
        return data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public Object getIdentifier() {
        return id;
    }

    @Override
    public String getDeleteWhereClause() {
        return "uuid = ?";
    }

    @Override
    public Boolean salvar() {
        return daoContagem.inserir(this);
    }

    @Override
    public Boolean salvar(List<Contagem> lista) {
        return daoContagem.inserir(lista);
    }

    @Override
    public Boolean atualizar() {
        return daoContagem.atualizar(this);
    }

    @Override
    public Boolean deletar() {
        return daoContagem.deletar(this);
    }

    @Override
    public List<Contagem> listar() {
        return daoContagem.listar();
    }

    @Override
    public Contagem listarPorId(Object... ids) {
        return daoContagem.listarPorId(ids);
    }

    @Override
    public void load(Object... ids) {
        Contagem contagem = listarPorId(ids);
        if (contagem != null) {
            setId(contagem.getId());
            setLoja(contagem.getLoja());
            setData(contagem.getData());
            setFinalizada(contagem.getFinalizada());
            setTipoContagem(contagem.getTipoContagem());
        }
    }

    public ArrayList<Contagem> listarPorLojaPeriodo(String cnpj, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return daoContagem.listarPorLojaPeriodo(cnpj, dataInicial, dataFinal);
    }
}
