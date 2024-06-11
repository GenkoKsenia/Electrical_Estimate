package com.example.electrical_estimate.logic;



import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.electrical_estimate.R;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

public class Pdf {
    public void createPdf(String fileName, Context context, TableLayout tableLayout, TableLayout tableTotalLayout) throws IOException {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (!directory.exists()) {
            boolean dirCreated = directory.mkdirs();
            if (!dirCreated) {
                throw new FileNotFoundException("Не удалось создать директорию: " + directory.getPath());
            }
        }

        String filePath = new File(directory, fileName + ".pdf").getPath();
        Log.d("PDF Path", "Сохранение PDF по пути: " + filePath);
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        File fontFile = getFontFile(context);
        PdfFont font = PdfFontFactory.createFont(fontFile.getAbsolutePath(), PdfEncodings.IDENTITY_H);
        document.setFont(font);

        String headerText = "Предварительный расчёт стоимости электромонтажных работ для объекта " + fileName;
        Paragraph headerParagraph = new Paragraph(headerText)
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER);

        document.add(headerParagraph);


        float[] pointColumnWidths = {30F, 300F, 70F, 80F, 100F, 120F};
        Table table = new Table(pointColumnWidths);


        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            View child = tableLayout.getChildAt(i);
            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;

                // Проверка соответствия количества столбцов в TableRow и pointColumnWidths
                if (row.getChildCount() != pointColumnWidths.length) {
                    throw new IllegalArgumentException("Количество столбцов в TableRow не соответствует количеству ширин столбцов");
                }

                // Перебор всех дочерних элементов TableRow (TextView или Spinner)
                for (int j = 0; j < row.getChildCount(); j++) {
                    View cell = row.getChildAt(j);
                    String cellTextString = null;
                    if (cell instanceof TextView) {
                        cellTextString = ((TextView) cell).getText().toString();
                    } else if (cell instanceof Spinner) {
                        Spinner spinner = (Spinner) cell;
                        cellTextString = spinner.getSelectedItem().toString();
                    } else if (cell instanceof EditText) {
                        EditText editText = (EditText) cell;
                        cellTextString = editText.getText().toString();
                    }
                    Cell cellTab = new Cell();
                    cellTab.setTextAlignment(TextAlignment.CENTER);
                    cellTab.add(new Paragraph(cellTextString));
                    table.addCell(cellTab);
                }
            } else {
                throw new IllegalArgumentException("Дочерний элемент TableLayout не является TableRow");
            }
        }

        Cell cellTab = new Cell();
        cellTab.setTextAlignment(TextAlignment.CENTER);
        cellTab.add(new Paragraph(" "));
        table.addCell(cellTab);

        Cell cellTab1 = new Cell();
        cellTab1.setTextAlignment(TextAlignment.CENTER);
        cellTab1.add(new Paragraph(" "));
        table.addCell(cellTab1);

        Cell cellTab2 = new Cell();
        cellTab2.setTextAlignment(TextAlignment.CENTER);
        cellTab2.add(new Paragraph(" "));
        table.addCell(cellTab2);

        Cell cellTab3 = new Cell();
        cellTab3.setTextAlignment(TextAlignment.CENTER);
        cellTab3.add(new Paragraph(" "));
        table.addCell(cellTab3);

        View child = tableTotalLayout.getChildAt(0);
        if (child instanceof TableRow) {
            TableRow row = (TableRow) child;
            for (int j = 0; j < row.getChildCount(); j++) {
                View cell = row.getChildAt(j);
                String cellTextString;
                cellTextString = ((TextView) cell).getText().toString();

                Cell cellTab4 = new Cell();
                cellTab4.setTextAlignment(TextAlignment.CENTER);
                cellTab4.add(new Paragraph(cellTextString));
                table.addCell(cellTab4);
            }
        }

        document.add(table);
        document.close();

        String message = "PDF файл сохранен по пути: " + filePath;
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    private File getFontFile(Context context) throws IOException {
        @SuppressLint("ResourceType") InputStream fontStream = context.getResources().openRawResource(R.font.chocolate);
        File tempFile = File.createTempFile("tempfont", ".ttf", context.getCacheDir());
        try (OutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fontStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }
        return tempFile;
    }
}
