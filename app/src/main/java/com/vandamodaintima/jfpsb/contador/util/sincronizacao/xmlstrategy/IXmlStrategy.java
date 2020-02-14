package com.vandamodaintima.jfpsb.contador.util.sincronizacao.xmlstrategy;

import com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql.EntidadeMySQL;

import org.simpleframework.xml.Serializer;

import java.io.File;

public interface IXmlStrategy {
    void write(String path, Serializer serializer, EntidadeMySQL entidadeMySQL) throws Exception;
    EntidadeMySQL read(Serializer serializer, File arquivo) throws Exception;
}
