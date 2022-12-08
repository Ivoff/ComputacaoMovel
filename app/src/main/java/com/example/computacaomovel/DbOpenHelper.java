package com.example.computacaomovel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {

    public DbOpenHelper(Context context) {
        super(context, "database.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DbSchemaContract.Conferencia.SQL_CREATE_STMT);
        sqLiteDatabase.execSQL(DbSchemaContract.Periodicos.SQL_CREATE_STMT);
        sqLiteDatabase.execSQL(DbSchemaContract.OutrasAreas.SQL_CREATE_STMT);
//        sqLiteDatabase.execSQL(DbSchemaContract.Meta.SQL_CREATE_STMT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DbSchemaContract.Conferencia.SQL_DELETE_STMT);
        sqLiteDatabase.execSQL(DbSchemaContract.Periodicos.SQL_DELETE_STMT);
        sqLiteDatabase.execSQL(DbSchemaContract.OutrasAreas.SQL_DELETE_STMT);
//        sqLiteDatabase.execSQL(DbSchemaContract.Meta.SQL_DELETE_STMT);
        onCreate(sqLiteDatabase);
    }
}
