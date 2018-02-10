package com.vandamodaintima.jfpsb.contador.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String CATEGORIA = "Contador";
    private String[] scriptSQLCreate;
    private String[] scriptDeleteDB;

    public SQLiteHelper(Context context, String nomeBanco, int versaoBanco, String[] scriptSQLCreate, String[] scriptDeleteDB) {
        super(context, nomeBanco, null, versaoBanco);
        this.scriptSQLCreate = scriptSQLCreate;
        this.scriptDeleteDB = scriptDeleteDB;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(CATEGORIA, "Criando bando da aplicação");
        int qntScript = scriptSQLCreate.length;

        for(int i = 0; i < qntScript; i++) {
            String sql = scriptSQLCreate[i];
            Log.i(CATEGORIA, sql);
            sqLiteDatabase.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w(CATEGORIA, "Atualizando da versão " + i + " para versão " + i1);
        for(int p = 0; p < scriptDeleteDB.length; p++) {
            Log.i(CATEGORIA, scriptDeleteDB[p]);
            sqLiteDatabase.execSQL(scriptDeleteDB[p]);
        }

        onCreate(sqLiteDatabase);
    }
}
