package com.example.computacaomovel;

import static android.icu.lang.UProperty.INT_START;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.example.computacaomovel.DialogFragment;

import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


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
    private LinearLayoutCompat rootView;
    private RecyclerView list;
    private ProgressBar progressBar;
    private LinearLayoutCompat listContainer;
    private TextView progressText;
    private TextView notFoundText;
    private String searchText = "";

    private SplashScreen splashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        splashScreen = SplashScreen.installSplashScreen(this);

        setContentView(R.layout.activity_main);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        editText = (EditText) findViewById(R.id.search_bar);
        filtrosBtn = (TextView) findViewById(R.id.filtros_btn);
        spinner = (Spinner) findViewById(R.id.spinner_select);
        spinnerColumn = (Spinner) findViewById(R.id.spinner_column);
        spinnerOrder = (Spinner) findViewById(R.id.spinner_order);
        filtrosContainer = (ConstraintLayout) findViewById(R.id.filtros_container);
        rootView = (LinearLayoutCompat) findViewById(R.id.root_view);
        list = (RecyclerView) findViewById(R.id.list);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listContainer = (LinearLayoutCompat) findViewById(R.id.list_container);
        progressText = (TextView) findViewById(R.id.progress_text);
        notFoundText = (TextView) findViewById(R.id.not_found_text);

        dbWrite = new DbOpenHelper(this).getWritableDatabase();
        dbRead = new DbOpenHelper(this).getReadableDatabase();

        setSupportActionBar(myToolbar);
        Drawable menuIcon = ContextCompat.getDrawable(this, R.drawable.ic_overflow_vertical);
        myToolbar.setOverflowIcon(menuIcon);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        this.setColumnSpinnerArray(spinner, 0);

        ArrayAdapter<CharSequence> orderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_order_array, R.layout.spinner_item);
        orderSpinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerOrder.setAdapter(orderSpinnerAdapter);

        LayoutTransition autoTransition = new LayoutTransition();
        ObjectAnimator downAnimation = ObjectAnimator.ofFloat(filtrosContainer, "translationY", 0f);
        downAnimation.setDuration(750);
        ObjectAnimator upAnimation = ObjectAnimator.ofFloat(filtrosContainer, "translationY", -300f);
        upAnimation.setDuration(750);
        autoTransition.setAnimator(LayoutTransition.APPEARING, downAnimation);
        autoTransition.setAnimator(LayoutTransition.DISAPPEARING, upAnimation);
        rootView.setLayoutTransition(autoTransition);


        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchText =  v.getText().toString();
                    if (!searchText.isEmpty()) {
                        v.setCompoundDrawablesWithIntrinsicBounds(v.getCompoundDrawables()[0], null, ContextCompat.getDrawable(activity, R.drawable.custom_clear_search_bar), null);
                        switchListData();
                    } else {
                        v.setCompoundDrawablesWithIntrinsicBounds(v.getCompoundDrawables()[0], null, null, null);
                    }
                    handled = true;
                }
                return handled;
            }
        });

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(editText.getCompoundDrawables()[DRAWABLE_RIGHT] != null && event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        searchText = "";
                        editText.setText("");
                        hideKeyboard(activity);
                        editText.setCompoundDrawablesWithIntrinsicBounds(editText.getCompoundDrawables()[0], null, null, null);
                        editText.clearFocus();
                        switchListData();
                        return true;
                    }
                }
                return false;
            }
        });

        filtrosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int filtrosVisibility = filtrosContainer.getVisibility();
                if(filtrosVisibility != View.VISIBLE) {
                    filtrosContainer.setVisibility(View.VISIBLE);
                } else {
                    filtrosContainer.setVisibility(View.GONE);
                }
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

        spinnerColumn.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                activity.switchListData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerOrder.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                activity.switchListData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (!checkDatabasePopulated(dbRead)) {
            AsyncTask<String, Integer, Object> request = new QualisRequest(dbWrite, this)
                .execute(
                    "https://qualis.ic.ufmt.br/qualis_conferencias_2016.json",
                    "https://qualis.ic.ufmt.br/periodico.json",
                    "https://qualis.ic.ufmt.br/todos2.json"
                );
        } else {
            ItemsAdapter listAdapter = new ItemsAdapter(listData(), tableName());
            listAdapter.setOnItemClickListener(new ItemsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {
                    showDialog(itemView);
                }
            });
            list.setAdapter(listAdapter);
            list.setLayoutManager(new LinearLayoutManager(this));
            progressBar.setVisibility(View.GONE);
            progressText.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.sincronizar) {
            this.deleteDatabase("database.db");
            this.recreate();
            return true;
        } else if (itemId == R.id.sobre) {
            Intent secondActivity = new Intent(this, SecondActivity.class);
            startActivity(secondActivity);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    private boolean checkDatabasePopulated(SQLiteDatabase database) {
        Cursor resultConferencias = database.rawQuery("SELECT COUNT(*) FROM "+DbSchemaContract.Conferencia.TABLE_NAME+";", null);
        Cursor resultPeriodicos = database.rawQuery("SELECT COUNT(*) FROM "+DbSchemaContract.Periodicos.TABLE_NAME+";", null);
        Cursor resultOutrasAreas = database.rawQuery("SELECT COUNT(*) FROM "+DbSchemaContract.OutrasAreas.TABLE_NAME+";", null);
        resultConferencias.moveToNext();
        resultPeriodicos.moveToNext();
        resultOutrasAreas.moveToNext();

        return resultConferencias.getInt(0) > 0 || resultPeriodicos.getInt(0) > 0 || resultOutrasAreas.getInt(0) > 0;
    }

    private boolean checkDatabaseComplete(SQLiteDatabase database) {
        Cursor totalRows = database.rawQuery(String.format("SELECT %s, %s FROM %s WHERE %s = 1;", DbSchemaContract.Meta.COLUMN_NAME_TOTAL_JSON_ROWS, DbSchemaContract.Meta.COLUMN_NAME_TOTAL_JSON_ROWS, DbSchemaContract.Meta.TABLE_NAME, DbSchemaContract.Meta._ID), null);
        totalRows.moveToNext();
        return totalRows.getInt(0) == totalRows.getInt(1);
    }

    @Override
    public void onTaskCompleted() {
        progressBar.setVisibility(View.GONE);
        progressText.setVisibility(View.GONE);

        ItemsAdapter listAdapter = new ItemsAdapter(listData(), tableName());
        list.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(new ItemsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                showDialog(itemView);
            }
        });
        list.setLayoutManager(new LinearLayoutManager(this));

        list.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressUpdate(int progress) {
        String aux = "Sincronizando " + String.valueOf(progress) + "%";
        progressText.setText(aux);
    }

    private Cursor listData() {
        int selectedTab = spinner.getSelectedItemPosition();
        int selectedColumn = spinnerColumn.getSelectedItemPosition()+1;
        int selectedOrder = spinnerOrder.getSelectedItemPosition();
        String[] orderArray = {"ASC", "DESC"};
        String statement;

        if (searchText.isEmpty()) {
            if (selectedTab == 0) {
                statement = String.format(DbSchemaContract.Conferencia.SQL_SELECT_STMT, DbSchemaContract.Conferencia.COLUMNS_ARRAY[selectedColumn], orderArray[selectedOrder]);
            } else if (selectedTab == 1) {
                statement = String.format(DbSchemaContract.Periodicos.SQL_SELECT_STMT, DbSchemaContract.Periodicos.COLUMNS_ARRAY[selectedColumn], orderArray[selectedOrder]);
            } else {
                statement = String.format(DbSchemaContract.OutrasAreas.SQL_SELECT_STMT, DbSchemaContract.OutrasAreas.COLUMNS_ARRAY[selectedColumn], orderArray[selectedOrder]);
            }
        } else {
            if (selectedTab == 0) {
                statement = String.format(DbSchemaContract.Conferencia.SQL_SEARCH_STMT, DbSchemaContract.Conferencia.getLikeStmt(searchText), DbSchemaContract.Conferencia.COLUMNS_ARRAY[selectedColumn], orderArray[selectedOrder]);
            } else if (selectedTab == 1) {
                statement = String.format(DbSchemaContract.Periodicos.SQL_SEARCH_STMT, DbSchemaContract.Periodicos.getLikeStmt(searchText), DbSchemaContract.Periodicos.COLUMNS_ARRAY[selectedColumn], orderArray[selectedOrder]);
            } else {
                statement = String.format(DbSchemaContract.OutrasAreas.SQL_SEARCH_STMT, DbSchemaContract.OutrasAreas.getLikeStmt(searchText), DbSchemaContract.OutrasAreas.COLUMNS_ARRAY[selectedColumn], orderArray[selectedOrder]);
            }
        }

        return dbRead.rawQuery(statement, null);
    }

    private String tableName() {
        int selectedTab = spinner.getSelectedItemPosition();
        if (selectedTab == 0) {
            return DbSchemaContract.Conferencia.TABLE_NAME;
        } else if (selectedTab == 1) {
            return DbSchemaContract.Periodicos.TABLE_NAME;
        } else {
            return DbSchemaContract.OutrasAreas.TABLE_NAME;
        }
    }

    private void switchListData() {
        Cursor itemsCursor = listData();
        if (itemsCursor.getCount() == 0) {
            list.setVisibility(View.GONE);
            notFoundText.setVisibility(View.VISIBLE);
        } else {
            list.setVisibility(View.VISIBLE);
            notFoundText.setVisibility(View.GONE);
            ItemsAdapter listAdapter = new ItemsAdapter(itemsCursor, tableName());
            list.setAdapter(listAdapter);
            listAdapter.setOnItemClickListener(new ItemsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {
                    showDialog(itemView);
                }
            });
            list.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showDialog(View itemView) {
        String text2 = ((TextView)itemView.findViewById(R.id.item_header)).getText().toString();
        String text1 = ((TextView)itemView.findViewById(R.id.subitem_text_1)).getText().toString();
        String text3 = ((TextView)itemView.findViewById(R.id.subitem_text_2)).getText().toString();
        String text4 = ((TextView)itemView.findViewById(R.id.subitem_text_3)).getText().toString();
        String text5 = "";

        if (!text4.isEmpty()) {
            text5 = text4;
            text4 = text3.split("/")[1];
            text3 = text3.split("/")[0];
        }

        String tableName = tableName();
        SpannableStringBuilder text1Prefix;
        SpannableStringBuilder text2Prefix;
        SpannableStringBuilder text3Prefix;

        if (tableName.equals(DbSchemaContract.Conferencia.TABLE_NAME)) {
            text1Prefix = (new SpannableStringBuilder("Sigla: "));
            text1Prefix.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, ("Sigla: ").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text2Prefix = (new SpannableStringBuilder("Conferência: "));
            text2Prefix.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, ("Conferência: ").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text3Prefix = (new SpannableStringBuilder("Extrato Capes: "));
            text3Prefix.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, ("Extrato Capes: ").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (tableName.equals(DbSchemaContract.Periodicos.TABLE_NAME)) {
            text1Prefix = (new SpannableStringBuilder("ISSN: "));
            text1Prefix.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, ("ISSN: ").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text2Prefix = (new SpannableStringBuilder("Periódico: "));
            text2Prefix.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, ("Periódico: ").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text3Prefix = (new SpannableStringBuilder("Extrato Capes: "));
            text3Prefix.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, ("Extrato Capes: ").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            text1Prefix = (new SpannableStringBuilder("ISSN: "));
            text1Prefix.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, ("ISSN: ").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text2Prefix = (new SpannableStringBuilder("Periódico: "));
            text2Prefix.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, ("Periódico: ").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text3Prefix = new SpannableStringBuilder("");
        }

        FragmentManager fm = getSupportFragmentManager();
        DialogFragment dialog = DialogFragment.newInstance(text1Prefix.append(text1), text2Prefix.append(text2), text3Prefix.append(text3), text4, text5);
        dialog.show(fm, "dialog_fragment");
    }
}