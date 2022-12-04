package com.example.computacaomovel;

import android.provider.BaseColumns;

public final class DbSchemaContract {
    private DbSchemaContract() {};
    public static final class Conferencia implements BaseColumns {
        public static final String TABLE_NAME = "conferencia";
        public static final String COLUMN_NAME_SIGLA = "sigla";
        public static final String COLUMN_NAME_CONFERENCIA = "conferencia";
        public static final String COLUMN_NAME_EXTRATO_CAPES = "extrato_capes";
        public static final String SQL_CREATE_STMT = String.format(
            "CREATE TABLE %s (" +
                "%s INTEGER PRIMARY KEY," +
                "%s TEXT," +
                "%s TEXT," +
                "%s TEXT" +
            ");",
            Conferencia.TABLE_NAME,
            Conferencia._ID,
            Conferencia.COLUMN_NAME_SIGLA,
            Conferencia.COLUMN_NAME_CONFERENCIA,
            Conferencia.COLUMN_NAME_EXTRATO_CAPES
        );
        public static final String SQL_DELETE_STMT = "DROP TABLE IF EXISTS " + Conferencia.TABLE_NAME;
        public static final String SQL_INSERT_STMT = "INSERT INTO "+Conferencia.TABLE_NAME+" (" + Conferencia.COLUMN_NAME_SIGLA +", " + Conferencia.COLUMN_NAME_CONFERENCIA + ", " + Conferencia.COLUMN_NAME_EXTRATO_CAPES + ") VALUES ('%s', '%s', '%s');";
    }
    public static final class Periodicos implements BaseColumns {
        public static final String TABLE_NAME = "periodicos";
        public static final String COLUMN_NAME_ISSN = "issn";
        public static final String COLUMN_NAME_PERIODICOS = "periodico";
        public static final String COLUMN_NAME_EXTRATO_CAPES = "extrato_capes";
        public static final String SQL_CREATE_STMT = String.format(
            "CREATE TABLE %s (" +
                "%s INTEGER PRIMARY KEY," +
                "%s TEXT," +
                "%s TEXT," +
                "%s TEXT" +
            ");",
            Periodicos.TABLE_NAME,
            Periodicos._ID,
            Periodicos.COLUMN_NAME_ISSN,
            Periodicos.COLUMN_NAME_PERIODICOS,
            Periodicos.COLUMN_NAME_EXTRATO_CAPES
        );
        public static final String SQL_DELETE_STMT = "DROP TABLE IF EXISTS " + Periodicos.TABLE_NAME;
        public static final String SQL_INSERT_STMT = "INSERT INTO "+Periodicos.TABLE_NAME+" (" + Periodicos.COLUMN_NAME_ISSN +", " + Periodicos.COLUMN_NAME_PERIODICOS + ", " + Periodicos.COLUMN_NAME_EXTRATO_CAPES + ") VALUES ('%s', '%s', '%s');";
    }

    public static final class OutrasAreas implements BaseColumns {
        public static final String TABLE_NAME = "outras_areas";
        public static final String COLUMN_NAME_ISSN = "issn";
        public static final String COLUMN_NAME_PERIODICOS = "periodico";
        public static final String COLUMN_NAME_EXTRATO_CAPES_COMPUTACAO = "extrato_capes_computacao";
        public static final String COLUMN_NAME_EXTRATO_CAPES_OUTRA_AREA = "extrato_capes_outra_area";
        public static final String COLUMN_NAME_OUTRA_AREA = "outra_area";
        public static final String SQL_CREATE_STMT = String.format(
            "CREATE TABLE %s (" +
                "%s INTEGER PRIMARY KEY," +
                "%s TEXT," +
                "%s TEXT," +
                "%s TEXT," +
                "%s TEXT," +
                "%s TEXT" +
            ");",
            OutrasAreas.TABLE_NAME,
            OutrasAreas._ID,
            OutrasAreas.COLUMN_NAME_ISSN,
            OutrasAreas.COLUMN_NAME_PERIODICOS,
            OutrasAreas.COLUMN_NAME_EXTRATO_CAPES_COMPUTACAO,
            OutrasAreas.COLUMN_NAME_EXTRATO_CAPES_OUTRA_AREA,
            OutrasAreas.COLUMN_NAME_OUTRA_AREA
        );
        public static final String SQL_DELETE_STMT = "DROP TABLE IF EXISTS " + OutrasAreas.TABLE_NAME;
        public static final String SQL_INSERT_STMT = "INSERT INTO "+OutrasAreas.TABLE_NAME+" ("+OutrasAreas.COLUMN_NAME_ISSN+", "+OutrasAreas.COLUMN_NAME_PERIODICOS+", "+OutrasAreas.COLUMN_NAME_EXTRATO_CAPES_COMPUTACAO+", "+OutrasAreas.COLUMN_NAME_EXTRATO_CAPES_OUTRA_AREA+", "+OutrasAreas.COLUMN_NAME_OUTRA_AREA+") VALUES ('%s', '%s', '%s', '%s', '%s');";
    }
}
