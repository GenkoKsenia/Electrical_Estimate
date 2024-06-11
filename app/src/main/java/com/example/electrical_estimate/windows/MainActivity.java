package com.example.electrical_estimate.windows;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.electrical_estimate.DB.DataBaseHelper;
import com.example.electrical_estimate.logic.Pdf;
import com.example.electrical_estimate.R;
import com.example.electrical_estimate.logic.Table;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TableRow title;
    private TextView[] table_title = new TextView[6];
    private Button buttonAddRow;
    private Button buttonDelRow;
    private Button buttonDelAll;
    private Button buttonCalculate;
    private Button buttonSave;
    private  TableLayout tableLayout;
    private  TableLayout tableTotalLayout;
    private DataBaseHelper dbHelper;
    private TextView sum;
    private Pdf pdf;
    private static final int PERMISSION_REQUEST_CODE = 200;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DataBaseHelper(this);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("electrical_work.db", MODE_PRIVATE, null);

        // Открытие базы данных
        //SQLiteDatabase db = dbHelper.openDatabase();
        List<String> jobList = dbHelper.getAllJobs();
        // Закрытие базы данных
        //db.close();

        EdgeToEdge.enable(this);



        title = findViewById(R.id.title);
        buttonAddRow = findViewById(R.id.addRow);
        buttonDelRow = findViewById(R.id.delRow);
        buttonDelAll = findViewById(R.id.delAll);
        buttonCalculate = findViewById(R.id.calculater);
        buttonSave = findViewById(R.id.save);
        tableLayout = findViewById(R.id.maintable);
        tableTotalLayout = findViewById(R.id.totaltable);

        Pdf pdf = new Pdf();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveFileDialog.show(MainActivity.this,tableLayout, tableTotalLayout, new SaveFileDialog.SaveFileDialogListener() {

                    @Override
                    public void onSave(String fileName) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
                /*
                try {
                    pdf.createPdf("example", MainActivity.this, tableLayout, tableTotalLayout);
                } catch (FileNotFoundException e) {
                    Toast.makeText(MainActivity.this, "Ошибка сохранения: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("PDF Creation", "Ошибка сохранения", e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/
            }
        });


        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calculator calculator = new Calculator(MainActivity.this);
                Table.calculator = calculator;
                calculator.show();
            }
        });


        for (int i = 0; i < 6; i++) {
            table_title[i] = (TextView) title.getChildAt(i);
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.calculate), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new Table(table_title, this, tableLayout, title, tableTotalLayout);
        buttonAddRow.setOnClickListener(Table::addRow);
        buttonDelRow.setOnClickListener(Table::delRow);
        buttonDelAll.setOnClickListener(Table::delAll);

        Log.d("ttt","create");


        sum = findViewById(R.id.total);

        ViewTreeObserver viewTreeObserver = sum.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                sum.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                Table.setWidthTotal();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        Log.d("ttt","resume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d("ttt","restart");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Проверьте ориентацию экрана и выполните необходимые действия
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Экран перевернут в ландшафтный режим
            //Table.setColumnWidthsFromHeader();
            Toast.makeText(this, "ландшафтный режим", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Экран перевернут в портретный режим
            //Table.setColumnWidthsFromHeader();
            Toast.makeText(this, "портретный режим", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Сохраняем количество строк
        int rowCount = tableLayout.getChildCount();
        outState.putInt("rowCount", rowCount);

        for (int i = 0; i < rowCount; i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);

            // Сохраняем количество ячеек в строке
            int cellCount = row.getChildCount();
            outState.putInt("cellCount_" + i, cellCount);

            for (int j = 0; j < cellCount; j++) {
                View cell = row.getChildAt(j);

                // Сохраняем содержимое ячеек в зависимости от их типа
                if (cell instanceof TextView) {
                    outState.putString("cell_" + i + "_" + j, ((TextView) cell).getText().toString());
                } else if (cell instanceof EditText) {
                    outState.putString("cell_" + i + "_" + j, ((EditText) cell).getText().toString());
                } else if (cell instanceof Spinner) {
                    outState.putInt("spinner_pos_" + i + "_" + j, ((Spinner) cell).getSelectedItemPosition());
                }

            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Восстанавливаем состояние таблицы
        if (savedInstanceState != null) {
            restoreTableLayout(savedInstanceState);
        }
    }

    private void restoreTableLayout(Bundle savedInstanceState) {

        tableLayout.removeAllViews();
        // Восстанавливаем количество строк
        int rowCount = savedInstanceState.getInt("rowCount");

        for (int i = 0; i < rowCount; i++) {
            TableRow row = new TableRow(this);

            int cellCount = savedInstanceState.getInt("cellCount_" + i);

            for (int j = 0; j < cellCount; j++) {
                if (j == 1) { // Восстанавливаем Spinner во второй колонке
                    Spinner spinner = new Spinner(this);
                    List<String> jobList = dbHelper.getAllJobs();
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jobList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    int spinnerPosition = savedInstanceState.getInt("spinner_pos_" + i + "_" + j);
                    spinner.setSelection(spinnerPosition);

                    row.addView(spinner);
                } else {
                    String cellContent = savedInstanceState.getString("cell_" + i + "_" + j);
                    EditText cell = new EditText(this);
                    cell.setText(cellContent);
                    row.addView(cell);
                }
            }


            tableLayout.addView(row);
            Table.setColumnWidthsFromHeader();
           // tableLayout.refreshDrawableState();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ttt","destoyed");
    }*/




}