package com.example.computacaomovel;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class QualisRequest extends AsyncTask<String, Void, Void> {

    SQLiteDatabase database;

    QualisRequest(SQLiteDatabase db){
        super();
        database = db;
    }

    private String getJson(String string) {
        try {
            URL url = new URL(string);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            return content.toString();
        } catch (Exception ex) {
            Log.d(QualisRequest.class.getSimpleName(), Log.getStackTraceString(ex));
            System.exit(0);
        }
        return "";
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            JSONObject conferencias = new JSONObject(getJson(strings[0]).toString());
            JSONArray conferencias_data = conferencias.getJSONArray("data");
            for(int i = 0; i < conferencias_data.length(); i += 1) {
                JSONArray currentArray = conferencias_data.getJSONArray(i);
                String sanitizedColumn0 = currentArray.get(0).toString().replaceAll("'", "''");
                String sanitizedColumn1 = currentArray.get(1).toString().replaceAll("'", "''");
                String sanitizedColumn2 = currentArray.get(2).toString().replaceAll("'", "''");
                String insertStmt = String.format(DbSchemaContract.Conferencia.SQL_INSERT_STMT, sanitizedColumn0, sanitizedColumn1, sanitizedColumn2);
                database.execSQL(insertStmt);
            }

            JSONObject periodicos = new JSONObject(getJson(strings[1]).toString());
            JSONArray periodicos_data = periodicos.getJSONArray("data");
            for(int i = 0; i < periodicos_data.length(); i += 1) {
                JSONArray currentArray = periodicos_data.getJSONArray(i);
                String sanitizedColumn0 = currentArray.get(0).toString().replaceAll("'", "''");
                String sanitizedColumn1 = currentArray.get(1).toString().replaceAll("'", "''");
                String sanitizedColumn2 = currentArray.get(2).toString().replaceAll("'", "''");
                String insertStmt = String.format(DbSchemaContract.Periodicos.SQL_INSERT_STMT, sanitizedColumn0, sanitizedColumn1, sanitizedColumn2);
                database.execSQL(insertStmt);
            }

            JSONObject outrasAreas = new JSONObject(getJson(strings[2]).toString());
            JSONArray outrasAreas_data = outrasAreas.getJSONArray("data");
            for(int i = 0; i < outrasAreas_data.length(); i += 1) {
                JSONArray currentArray = outrasAreas_data.getJSONArray(i);
                String sanitizedColumn0 = currentArray.get(0).toString().replaceAll("'", "''");
                String sanitizedColumn1 = currentArray.get(1).toString().replaceAll("'", "''");
                String sanitizedColumn2 = currentArray.get(2).toString().replaceAll("'", "''");
                String sanitizedColumn3 = currentArray.get(3).toString().replaceAll("'", "''");
                String sanitizedColumn4 = currentArray.get(4).toString().replaceAll("'", "''");
                String insertStmt = String.format(DbSchemaContract.OutrasAreas.SQL_INSERT_STMT, sanitizedColumn0, sanitizedColumn1, sanitizedColumn2, sanitizedColumn3, sanitizedColumn4);
                database.execSQL(insertStmt);
            }
        } catch (Exception ex) {
            Log.d(QualisRequest.class.getSimpleName(), Log.getStackTraceString(ex));
        }

        return null;
    }
}
