package com.example.computacaomovel;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase db_write = new DbOpenHelper(this).getWritableDatabase();
        AsyncTask<String, Void, Void> request = new QualisRequest(db_write)
            .execute(
                "https://qualis.ic.ufmt.br/qualis_conferencias_2016.json",
                "https://qualis.ic.ufmt.br/periodico.json",
                "https://qualis.ic.ufmt.br/todos2.json"
            );

    }
}