package com.example.electrical_estimate.windows;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.electrical_estimate.logic.Pdf;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SaveFileDialog {
    public interface SaveFileDialogListener {
        void onSave(String fileName);
        void onCancel();
    }

    public static void show(Context context, TableLayout tableLayout, TableLayout tableTotalLayout, SaveFileDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Введите название файла");

        // Создание поля для ввода
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Кнопка "Сохранить"
        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String fileName = input.getText().toString().trim();
                if (!fileName.isEmpty()) {
                    Pdf pdf = new Pdf();
                    try {
                        pdf.createPdf(fileName, context, tableLayout, tableTotalLayout);
                    } catch (FileNotFoundException e) {
                        Toast.makeText(context, "Ошибка сохранения: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else Toast.makeText(context, "Введите название", Toast.LENGTH_SHORT).show();
            }
        });

        // Кнопка "Отмена"
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                listener.onCancel();
            }
        });

        builder.show();
    }
}
