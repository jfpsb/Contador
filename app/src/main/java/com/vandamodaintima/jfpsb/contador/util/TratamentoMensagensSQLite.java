package com.vandamodaintima.jfpsb.contador.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by jfpsb on 18/02/2018.
 */


public class TratamentoMensagensSQLite {
    /**
     * Trata erros no insert de banco de dados mostrando a mensagem de erro correspondente
     * @param context Contexto da activity
     * @param codigo Código retornado como erro no insert
     */
    public static void trataErroEmInsert(Context context, long codigo) {
        switch ((int) codigo) {
            case 787:
                Toast.makeText(context, "Erro de chave estrangeira! Algum item selecionado para cadastro não existe no banco de dados (cheque escolha de loja) ou você tentou deletar um item que está sendo referenciado em outro item (Como loja em contagem)", Toast.LENGTH_LONG).show();
                break;
            case 1555:
                Toast.makeText(context, "Erro de chave primária! Já existe um item cadastrado com o código especificado (Cheque código de barras ou CNPJ)", Toast.LENGTH_SHORT).show();
                break;
            case 2067:
                Toast.makeText(context, "Erro de campo único! Já existe um item cadastrado com o campo especificado (Cheque o nome ou outro campo)", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, "Erro ao cadastrar! Cheque LogCat para mais detalhes", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public static long retornaCodigoErro(String mensagem) {

        if(mensagem.contains("code 787"))
            return 787;

        if(mensagem.contains("code 1555"))
            return 1555;

        if(mensagem.contains("code 2067"))
            return 2067;

        return -1;
    }
}
