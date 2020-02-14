package com.vandamodaintima.jfpsb.contador.util.sincronizacao.xmlstrategy;

import com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql.EntidadeMySQL;
import com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql.EntidadeMySQLContagem;

import org.simpleframework.xml.Serializer;

import java.io.File;

public class XmlContagemStrategy implements IXmlStrategy{
    @Override
    public void write(String path, Serializer serializer, EntidadeMySQL entidadeMySQL) throws Exception {
        EntidadeMySQLContagem entidadeMySQLContagem = (EntidadeMySQLContagem)entidadeMySQL;
        File result = new File(path, entidadeMySQLContagem.getEntidadeSalva().getIdentificador() + ".xml");
        serializer.write(entidadeMySQL, result);
    }

    @Override
    public EntidadeMySQL read(Serializer serializer, File arquivo) throws Exception {
        return serializer.read(EntidadeMySQLContagem.class, arquivo);
    }
}
