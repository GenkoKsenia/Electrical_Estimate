package com.example.electrical_estimate.logic;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.electrical_estimate.DB.DataBaseHelper;
import com.example.electrical_estimate.subject_area.ListWorks;
import com.example.electrical_estimate.windows.Calculator;


import java.util.ArrayList;
import java.util.List;

public class Table {
    private static TextView[] table_title; //элементы заголовка таблицы
    private static Context context;
    private static TableLayout tableLayout; //основная таблица
    private static TableLayout tableTotalLayout; //таблица с итогом
    private static TableRow title; //заголовок таблицы
    private static DataBaseHelper dbHelper;
    private static ArrayList<TableRow> selectedRow = new ArrayList<>(); //список выделенных строк
    private static ListWorks works;
    public static Calculator calculator;
    public Table(TextView[] table_title, Context context, TableLayout tableLayout, TableRow title, TableLayout tableTotalLayout){
        this.table_title = table_title;
        this.context = context;
        this.tableLayout = tableLayout;
        this.title = title;
        this.dbHelper = new DataBaseHelper(context);
        this.tableTotalLayout = tableTotalLayout;
    }

    //добавить строку в таблицу
    public static void addRow(View view) {
        TableRow tableRow = new TableRow(context);
        tableRow.setBackgroundColor(Color.BLACK);

        tableRow.setPadding(
                title.getPaddingLeft(),
                0,
                title.getPaddingRight(),
                title.getPaddingBottom()
        );
        for (int i = 0; i < 6; i++) {
            TextView titleView = table_title[i];
            View textView;

            switch (i){
                case 0:
                    textView = new TextView(context);
                    int rowNumber = tableLayout.getChildCount();
                    ((TextView) textView).setText(String.valueOf(rowNumber));
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Получение родительской строки и её удаление
                            TableRow parentRow = (TableRow) v.getParent();

                            boolean contains = selectedRow.contains(parentRow);
                            if(!contains){
                                selectedRow.add(parentRow);
                                recolorRow(parentRow, 135, 206, 235);
                            }
                            else{
                                selectedRow.remove(parentRow);
                                recolorRow(parentRow, 255, 255, 255);
                            }
                        }
                    });
                    break;
                case 1:
                    textView = new Spinner(context);
                    List<String> jobList = dbHelper.getAllJobs();
                    jobList.add(0, "Выбрать");
                    ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, jobList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ((Spinner) textView).setAdapter(adapter);
                    ((Spinner) textView).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            if (position > 0) {
                                // Получаем выбранный элемент из Spinner
                                String selectedJob = (String) parentView.getItemAtPosition(position);
                                // Получаем цену для выбранного вида работы из базы данных
                                String measure = dbHelper.getMeasureForJob(selectedJob);
                                double price = dbHelper.getPriceForJob(selectedJob);
                                // Устанавливаем цену в четвёртый столбец таблицы
                                TextView priceEditText = (TextView) tableRow.getChildAt(4);
                                priceEditText.setText(String.valueOf(price));

                                TextView measureEditText = (TextView) tableRow.getChildAt(2);
                                measureEditText.setText(String.valueOf(measure));
                                updateSum(tableRow);
                            }
                            else {
                                // Если выбран первый элемент ("Выбрать"), устанавливаем цену "0"
                                TextView priceTextView = (TextView) tableRow.getChildAt(4);
                                priceTextView.setText("0.0");
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // Обработка события, если ничего не выбрано в Spinner
                        }
                    });
                    break;
                case 2:
                    textView = new TextView(context);
                    break;
                case 3:
                    textView = new EditText(context);
                    ((EditText)textView).setText("");
                    ((EditText)textView).setMaxLines(1);
                    ((EditText)textView).setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                    ((EditText)textView).addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                            // Вызывается до того, как текст в EditText изменится
                        }
                        @Override
                        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                            //
                        }
                        @Override
                        public void afterTextChanged(Editable editable) {
                            // Вызывается после изменения текста в EditText
                            updateSum(tableRow);
                        }
                    });
                    break;
                case 5:
                    textView = new TextView(context);
                    ((TextView) textView).setText("0.0");
                    break;
                default:
                    textView = new TextView(context);
                    ((TextView) textView).setText("");
                    break;
            }


            // Устанавливаем ширину ячейки по ширине соответствующего заголовка
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    titleView.getWidth(), // Ширина как у соответствующего заголовка
                    titleView.getHeight(),
                    TableRow.LayoutParams.WRAP_CONTENT
            );

            // Копируем внутренние отступы (padding)
            textView.setPadding(
                    titleView.getPaddingLeft(),
                    titleView.getPaddingTop(),
                    titleView.getPaddingRight(),
                    titleView.getPaddingBottom()
            );

            // Копируем внешние отступы (margin)
            ViewGroup.MarginLayoutParams titleLayoutParams = (ViewGroup.MarginLayoutParams) titleView.getLayoutParams();
            params.setMargins(
                    titleLayoutParams.leftMargin,
                    titleLayoutParams.topMargin,
                    titleLayoutParams.rightMargin,
                    titleLayoutParams.bottomMargin
            );

            // Копируем цвет фона
            textView.setBackgroundColor(Color.WHITE);

            // Устанавливаем параметры макета
            textView.setLayoutParams(params);



            if (textView instanceof TextView) {
                float textSizePx = titleView.getTextSize();
                float textSizeSp = textSizePx / context.getResources().getDisplayMetrics().scaledDensity;
                ((TextView) textView).setTextSize(textSizeSp);
                ((TextView) textView).setGravity(titleView.getGravity());
                ((TextView) textView).setEllipsize(TextUtils.TruncateAt.END);
                ((TextView) textView).setMaxLines(1);
            } else if (textView instanceof EditText) {
                float textSizePx = titleView.getTextSize();
                float textSizeSp = textSizePx / context.getResources().getDisplayMetrics().scaledDensity;
                ((EditText) textView).setTextSize(textSizeSp);
                ((EditText) textView).setGravity(titleView.getGravity());
                ((EditText) textView).setEllipsize(TextUtils.TruncateAt.END);
                ((EditText) textView).setMaxLines(1);
            } else if (textView instanceof Spinner) {
                ((Spinner) textView).setGravity(titleView.getGravity());
                ((Spinner)textView).setLayoutParams(params);
            }

            // Добавляем TextView в TableRow
            tableRow.addView(textView);
        }
        tableLayout.addView(tableRow);
        //setColumnWidthsFromHeader();
    }

    //подсчет суммы
    public static void updateSum(TableRow tableRow){
        TextView volume = (TextView) tableRow.getChildAt(3);
        TextView price = (TextView) tableRow.getChildAt(4);
        TextView total = (TextView) tableRow.getChildAt(5);
        try {
            String volumeStr = volume.getText().toString();
            String priceStr = price.getText().toString();

            double volumeValue = Double.parseDouble(volumeStr);
            double priceValue = Double.parseDouble(priceStr);

            double totalValue = volumeValue * priceValue;
            total.setText(String.valueOf(totalValue));
        }catch (Exception e){
            total.setText("0.0");
        }
        updateTotal();
    }

    //обновить номера строк (при удалении)
    public static void updateNumberRow(){
        int rowCount = tableLayout.getChildCount();
        for (int i = 1; i < rowCount; i++) {
            View rowView = tableLayout.getChildAt(i);
            if (rowView instanceof TableRow) {
                TableRow tableRow = (TableRow) rowView;
                View cellView = tableRow.getChildAt(0);
                if (cellView instanceof TextView) {
                    TextView textView = (TextView) cellView;
                    textView.setText(String.valueOf(i)); // Нумерация начинается с 1
                }
            }
        }
    }

    //перекрасить строку (при выделении)
    public static void recolorRow(TableRow tableRow, int red, int green, int blue){
        for (int i = 0; i < tableRow.getChildCount(); i++) {
            View selectedCell = tableRow.getChildAt(i);
            if (selectedCell instanceof TextView) {
                ((TextView) selectedCell).setBackgroundColor(Color.rgb(red, green, blue));
            } else if (selectedCell instanceof Spinner) {
                selectedCell.setBackgroundColor(Color.rgb(red, green, blue));
            }
        }
    }

    //удалить строку
    public static void delRow(View view) {
        if(selectedRow.isEmpty()){
            Toast.makeText(context, "Выделите строку", Toast.LENGTH_SHORT).show();
        }else{
            for(int i = 0; i<selectedRow.size(); i++){
                tableLayout.removeView(selectedRow.get(i));
            }
            updateNumberRow();
            updateTotal();
        }
    }

    //подсчет итого
    public static void updateTotal() {
        double totalValue = 0;

        // Перебираем строки в tableLayout
        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            View rowView = tableLayout.getChildAt(i);
            if (rowView instanceof TableRow) {
                TableRow tableRow = (TableRow) rowView;
                TextView sum = (TextView) tableRow.getChildAt(5); // Получаем TextView с индексом 5
                String sumStr = sum.getText().toString();
                double sumValue = Double.parseDouble(sumStr);
                totalValue += sumValue;
            }
        }

        // Обновляем ячейки в tableTotalLayout
        View totalRow = tableTotalLayout.getChildAt(0);
        if (totalRow instanceof TableRow) {
            TableRow total = (TableRow) totalRow;
            TextView totalTextView1 = (TextView) total.getChildAt(1);
            totalTextView1.setText(String.valueOf(totalValue));
        }
    }

    //очистка
    public static void delAll(View view) {
        for (int i = tableLayout.getChildCount() - 1; i >=1; i--) {
            tableLayout.removeView(tableLayout.getChildAt(i));
        }
        updateTotal();
    }

    //установить ширину ячеек как у заголовка
    public static void setColumnWidthsFromHeader() {
        if (tableLayout.getChildCount() == 0) return;

        Toast.makeText(context, "setColumnWidthsFromHeader", Toast.LENGTH_SHORT).show();
        TableRow headerRow = (TableRow) tableLayout.getChildAt(0);
        int columnCount = headerRow.getChildCount();
        int[] columnWidths = new int[columnCount];

        // Получаем ширину каждой ячейки заголовка
        for (int i = 0; i < columnCount; i++) {
            View headerCell = headerRow.getChildAt(i);
            columnWidths[i] = headerCell.getWidth(); // Получаем фактическую ширину ячейки
        }

        // Применяем ширину каждой ячейки заголовка к остальным строкам таблицы
        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < columnCount; j++) {
                View cell = row.getChildAt(j);
                TableRow.LayoutParams params = (TableRow.LayoutParams) cell.getLayoutParams();
                params.width = columnWidths[j];
                cell.setLayoutParams(params);
            }
        }
    }

    //установить ширину ячейки итого
    public static void setWidthTotal(){
        TableRow mainTableRow = (TableRow) tableLayout.getChildAt(0);
        TableRow totalTableRow = (TableRow) tableTotalLayout.getChildAt(0);

        View mainTableCell = mainTableRow.getChildAt(5);
        View totalTableCell = totalTableRow.getChildAt(1);

        int widthMainCell = mainTableCell.getWidth();
        TableRow.LayoutParams params = (TableRow.LayoutParams) totalTableCell.getLayoutParams();
        params.width = widthMainCell;
        totalTableCell.setLayoutParams(params);
    }

    public static int fillingCalculator() {
        works = new ListWorks(context);
        if(calculator.calculationVolume()==null){
            return 0;
        }else{
        works = calculator.calculationVolume();}
        for (int j = 0; j < works.size(); j++) {
            TableRow tableRow = new TableRow(context);
            tableRow.setBackgroundColor(Color.BLACK);

            tableRow.setPadding(
                    title.getPaddingLeft(),
                    0,
                    title.getPaddingRight(),
                    title.getPaddingBottom()
            );


            for (int i = 0; i < 6; i++) {
                TextView titleView = table_title[i];
                View textView;

                String initialValue;
                switch (i) {
                    case 0:
                        textView = new TextView(context);
                        int rowNumber = tableLayout.getChildCount();
                        ((TextView) textView).setText(String.valueOf(rowNumber));
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Получение родительской строки и её удаление
                                TableRow parentRow = (TableRow) v.getParent();

                                boolean contains = selectedRow.contains(parentRow);
                                if (!contains) {
                                    selectedRow.add(parentRow);
                                    recolorRow(parentRow, 135, 206, 235);
                                } else {
                                    selectedRow.remove(parentRow);
                                    recolorRow(parentRow, 255, 255, 255);
                                }
                            }
                        });
                        break;
                    case 1:
                        textView = new Spinner(context);
                        List<String> jobList = dbHelper.getAllJobs();
                        jobList.add(0, "Выбрать");
                        ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, jobList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        ((Spinner) textView).setAdapter(adapter);

                        initialValue = works.get(j).getName();
                        int initialPosition = adapter.getPosition(initialValue);
                        if (initialPosition >= 0) {
                            ((Spinner) textView).setSelection(initialPosition);
                        }


                        ((Spinner) textView).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                if (position > 0) {
                                    // Получаем выбранный элемент из Spinner
                                    String selectedJob = (String) parentView.getItemAtPosition(position);
                                    // Получаем цену для выбранного вида работы из базы данных
                                    String measure = dbHelper.getMeasureForJob(selectedJob);
                                    double price = dbHelper.getPriceForJob(selectedJob);
                                    // Устанавливаем цену в четвёртый столбец таблицы
                                    TextView priceEditText = (TextView) tableRow.getChildAt(4);
                                    priceEditText.setText(String.valueOf(price));

                                    TextView measureEditText = (TextView) tableRow.getChildAt(2);
                                    measureEditText.setText(String.valueOf(measure));
                                    updateSum(tableRow);
                                } else {
                                    // Если выбран первый элемент ("Выбрать"), устанавливаем цену "0"
                                    TextView priceTextView = (TextView) tableRow.getChildAt(4);
                                    priceTextView.setText("0.0");
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                                // Обработка события, если ничего не выбрано в Spinner
                            }
                        });
                        break;
                    case 2:
                        textView = new TextView(context);
                        break;
                    case 3:
                        textView = new EditText(context);
                        initialValue = String.valueOf(works.get(j).getVolume());
                        ((EditText) textView).setText(initialValue);
                        ((EditText) textView).setMaxLines(1);
                        ((EditText) textView).setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                        ((EditText) textView).addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                                // Вызывается до того, как текст в EditText изменится
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                                //
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                // Вызывается после изменения текста в EditText
                                updateSum(tableRow);
                            }
                        });
                        break;
                    case 5:
                        textView = new TextView(context);
                        ((TextView) textView).setText("0.0");
                        break;
                    default:
                        textView = new TextView(context);
                        ((TextView) textView).setText("5");
                        break;
                }


                // Устанавливаем ширину ячейки по ширине соответствующего заголовка
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        titleView.getWidth(), // Ширина как у соответствующего заголовка
                        titleView.getHeight(),
                        TableRow.LayoutParams.WRAP_CONTENT
                );

                // Копируем внутренние отступы (padding)
                textView.setPadding(
                        titleView.getPaddingLeft(),
                        titleView.getPaddingTop(),
                        titleView.getPaddingRight(),
                        titleView.getPaddingBottom()
                );

                // Копируем внешние отступы (margin)
                ViewGroup.MarginLayoutParams titleLayoutParams = (ViewGroup.MarginLayoutParams) titleView.getLayoutParams();
                params.setMargins(
                        titleLayoutParams.leftMargin,
                        titleLayoutParams.topMargin,
                        titleLayoutParams.rightMargin,
                        titleLayoutParams.bottomMargin
                );

                // Копируем цвет фона
                textView.setBackgroundColor(Color.WHITE);

                // Устанавливаем параметры макета
                textView.setLayoutParams(params);


                if (textView instanceof TextView) {
                    float textSizePx = titleView.getTextSize();
                    float textSizeSp = textSizePx / context.getResources().getDisplayMetrics().scaledDensity;
                    ((TextView) textView).setTextSize(textSizeSp);
                    ((TextView) textView).setGravity(titleView.getGravity());
                    ((TextView) textView).setEllipsize(TextUtils.TruncateAt.END);
                    ((TextView) textView).setMaxLines(1);
                } else if (textView instanceof EditText) {
                    float textSizePx = titleView.getTextSize();
                    float textSizeSp = textSizePx / context.getResources().getDisplayMetrics().scaledDensity;
                    ((EditText) textView).setTextSize(textSizeSp);
                    ((EditText) textView).setGravity(titleView.getGravity());
                    ((EditText) textView).setEllipsize(TextUtils.TruncateAt.END);
                    ((EditText) textView).setMaxLines(1);
                } else if (textView instanceof Spinner) {
                    ((Spinner) textView).setGravity(titleView.getGravity());
                    ((Spinner) textView).setLayoutParams(params);
                }

                // Добавляем TextView в TableRow
                tableRow.addView(textView);
            }

            tableLayout.addView(tableRow);
            //setColumnWidthsFromHeader();
        }
        return 1;
    }
}
