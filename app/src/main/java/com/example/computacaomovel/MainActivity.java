package com.example.computacaomovel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        EditText editText = (EditText) findViewById(R.id.search_bar);
        TextView filtrosBtn = (TextView) findViewById(R.id.filtros_btn);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_select);

        setSupportActionBar(myToolbar);
        Drawable menuIcon = getDrawable(R.drawable.ic_overflow_vertical);
        myToolbar.setOverflowIcon(menuIcon);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(adapter);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Toast placeholder = Toast.makeText(MainActivity.this, "funcionou", Toast.LENGTH_SHORT);
                    placeholder.show();
                    handled = true;
                }
                return handled;
            }
        });

        filtrosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MainActivity.class.getSimpleName(), "Tocou no filtro");
                Toast.makeText(MainActivity.this, "Tocou no filtro", Toast.LENGTH_SHORT).show();
            }
        });

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        SQLiteDatabase db_write = new DbOpenHelper(this).getWritableDatabase();
//        AsyncTask<String, Void, Void> request = new QualisRequest(db_write)
//            .execute(
//                "https://qualis.ic.ufmt.br/qualis_conferencias_2016.json",
//                "https://qualis.ic.ufmt.br/periodico.json",
//                "https://qualis.ic.ufmt.br/todos2.json"
//            );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);

        return true;
    }
}