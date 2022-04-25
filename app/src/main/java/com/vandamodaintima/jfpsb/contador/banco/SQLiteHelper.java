package com.vandamodaintima.jfpsb.contador.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class SQLiteHelper extends SQLiteAssetHelper {
    public SQLiteHelper(Context context, String nomeBanco, int versaoBanco) {
        super(context, nomeBanco, null, versaoBanco);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }
}
