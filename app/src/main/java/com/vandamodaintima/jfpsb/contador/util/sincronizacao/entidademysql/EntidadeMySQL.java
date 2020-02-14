package com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "EntidadeMySQL")
public class EntidadeMySQL {
    @Element(name = "OperacaoMySql")
    private String operacaoMySql;

    public String getOperacaoMySql() {
        return operacaoMySql;
    }

    public void setOperacaoMySql(String operacaoMySql) {
        this.operacaoMySql = operacaoMySql;
    }
}
