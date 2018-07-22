package com.vandamodaintima.jfpsb.contador.util;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

/**
 * Created by jfpsb on 17/02/2018.
 */

public class ManipulaCursor {
    /**
     * Retorna um cursor com instrução ao usuário no topo
     * @param cursor Cursor original com dados do banco de dados
     * @param msg Mensagem a ser mostrada como instrução
     * @param camposEntidade Array String especificando as colunas presentes no Cursor
     * @return Cursor com mensagem específica no topo
     */
    public static Cursor retornaCursorComHintNull(Cursor cursor, String msg, String[] camposEntidade) {
        MatrixCursor matrixCursor = new MatrixCursor(camposEntidade);

        String[] placeholder = new String[camposEntidade.length];

        //Atribui ID
        placeholder[0] = "-1";
        //Atribui texto que será mostrado
        placeholder[1] = msg;

        // Coloca outros placeholders se houver
        for(int i = 2; i < camposEntidade.length - 1; i++) {
            placeholder[i] = "placeholder";
        }

        matrixCursor.addRow(placeholder);

        try {
            while (cursor.moveToNext()) {
                Object[] object = new Object[camposEntidade.length];

                object[0] = cursor.getString(cursor.getColumnIndexOrThrow(camposEntidade[0]));
                object[1] = cursor.getString(cursor.getColumnIndexOrThrow(camposEntidade[1]));

                for(int i = 2; i < camposEntidade.length - 1; i++) {
                    object[i] = cursor.getString(cursor.getColumnIndexOrThrow(camposEntidade[i]));
                }

                matrixCursor.addRow(object);
            }
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return matrixCursor;
    }
}
