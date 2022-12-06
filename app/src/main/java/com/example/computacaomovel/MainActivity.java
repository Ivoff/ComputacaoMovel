package com.example.computacaomovel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {

    private SQLiteDatabase dbWrite;
    private SQLiteDatabase dbRead;
    private final MainActivity activity = this;
    private Toolbar myToolbar;
    private EditText editText;
    private TextView filtrosBtn;
    private Spinner spinner;
    private Spinner spinnerColumn;
    private Spinner spinnerOrder;
    private ConstraintLayout filtrosContainer;
    private ConstraintLayout rootView;
    private RecyclerView list;
    private ProgressBar progressBar;
    private LinearLayoutCompat listContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        editText = (EditText) findViewById(R.id.search_bar);
        filtrosBtn = (TextView) findViewById(R.id.filtros_btn);
        spinner = (Spinner) findViewById(R.id.spinner_select);
        spinnerColumn = (Spinner) findViewById(R.id.spinner_column);
        spinnerOrder = (Spinner) findViewById(R.id.spinner_order);
        filtrosContainer = (ConstraintLayout) findViewById(R.id.filtros_container);
        rootView = (ConstraintLayout) findViewById(R.id.root_view);
        list = (RecyclerView) findViewById(R.id.list);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listContainer = (LinearLayoutCompat) findViewById(R.id.list_container);

        dbWrite = new DbOpenHelper(this).getWritableDatabase();
        dbRead = new DbOpenHelper(this).getReadableDatabase();


//        if (checkDatabaseCreated(dbRead)) {
//            AsyncTask<String, Void, Object> request = new QualisRequest(dbWrite, this)
//                .execute(
//                    "https://qualis.ic.ufmt.br/qualis_conferencias_2016.json",
//                    "https://qualis.ic.ufmt.br/periodico.json",
//                    "https://qualis.ic.ufmt.br/todos2.json"
//                );
//        }

        setSupportActionBar(myToolbar);
        Drawable menuIcon = getDrawable(R.drawable.ic_overflow_vertical);
        myToolbar.setOverflowIcon(menuIcon);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        this.setColumnSpinnerArray(spinner, 0);

        ArrayAdapter<CharSequence> orderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_order_array, R.layout.spinner_item);
        orderSpinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerOrder.setAdapter(orderSpinnerAdapter);

        filtrosContainer.setVisibility(View.GONE);

        LayoutTransition autoTransition = new LayoutTransition();
        ObjectAnimator downAnimation = ObjectAnimator.ofFloat(filtrosContainer, "translationY", 0f);
        downAnimation.setDuration(750);
        ObjectAnimator upAnimation = ObjectAnimator.ofFloat(filtrosContainer, "translationY", -300f);
        upAnimation.setDuration(750);
        autoTransition.setAnimator(LayoutTransition.APPEARING, downAnimation);
        autoTransition.setAnimator(LayoutTransition.DISAPPEARING, upAnimation);
        rootView.setLayoutTransition(autoTransition);

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
                int filtrosVisibility = filtrosContainer.getVisibility();
                if(filtrosVisibility != View.VISIBLE) {
                    filtrosContainer.setVisibility(View.VISIBLE);
                    changeListContraint(filtrosContainer.getId());
                } else {
                    filtrosContainer.setVisibility(View.GONE);
                    changeListContraint(filtrosBtn.getId());
                }
                Toast.makeText(MainActivity.this, "Tocou no filtro", Toast.LENGTH_SHORT).show();
            }
        });

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                activity.setColumnSpinnerArray(spinner, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);

        return true;
    }

    private void setColumnSpinnerArray(Spinner spinner, int index){
        int itemArray = R.array.spinner_conferencias_columns_array;

        if (index == 1) {
            itemArray = R.array.spinner_periodicos_columns_array;
        } else if (index == 2) {
            itemArray = R.array.spinner_outras_areas_columns_array;
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, itemArray, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerColumn.setAdapter(adapter);
    }

    private boolean checkDatabaseCreated(SQLiteDatabase database) {
        Cursor resultConferencias = dbRead.rawQuery("SELECT COUNT(*) FROM "+DbSchemaContract.Conferencia.TABLE_NAME+";", null);
        Cursor resultPeriodicos = dbRead.rawQuery("SELECT COUNT(*) FROM "+DbSchemaContract.Periodicos.TABLE_NAME+";", null);
        Cursor resultOutrasAreas = dbRead.rawQuery("SELECT COUNT(*) FROM "+DbSchemaContract.OutrasAreas.TABLE_NAME+";", null);
        resultConferencias.moveToNext();
        resultPeriodicos.moveToNext();
        resultOutrasAreas.moveToNext();

        return resultConferencias.getInt(0) > 0 || resultPeriodicos.getInt(0) > 0 || resultOutrasAreas.getInt(0) > 0;
    }

    @Override
    public void onTaskCompleted() {
        progressBar.setVisibility(View.GONE);
        list.setVisibility(View.VISIBLE);
    }

    private void changeListContraint(int topId) {
//        ConstraintSet constraintSet = new ConstraintSet();
//        constraintSet.clear(R.id.);
        ConstraintLayout.LayoutParams listParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        listParams.topToBottom = topId;
        listParams.leftToLeft = rootView.getId();
        listParams.rightToRight = rootView.getId();
        listParams.bottomToBottom = rootView.getId();

        listContainer.setLayoutParams(listParams);
    }
}