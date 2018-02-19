package com.vandamodaintima.jfpsb.contador.util;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
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
        matrixCursor.addRow(new String[] { "-1", msg });
        Cursor[] cursors = {matrixCursor, cursor};

        return new MergeCursor(cursors);
    }
}
