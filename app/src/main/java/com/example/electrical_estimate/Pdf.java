/*package com.example.electrical_estimate;

import android.graphics.pdf.PdfDocument;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Pdf {
    public void createPdf(String dest) throws FileNotFoundException {
        File file = new File(dest);
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Создаем шрифт
        PdfFont font = null;
        try {
            font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Создаем таблицу
        float[] columnWidths = {1, 2, 2, 2};
        Table table = new Table(columnWidths);

        // Заголовки таблицы
        table.addHeaderCell(new Cell().add(new Paragraph("Column 1").setFont(font)));
        table.addHeaderCell(new Cell().add(new Paragraph("Column 2").setFont(font)));
        table.addHeaderCell(new Cell().add(new Paragraph("Column 3").setFont(font)));
        table.addHeaderCell(new Cell().add(new Paragraph("Column 4").setFont(font)));

        // Данные таблицы
        for (int i = 0; i < 10; i++) {
            table.addCell(new Cell().add(new Paragraph("Data " + (i + 1)).setFont(font)));
            table.addCell(new Cell().add(new Paragraph("Data " + (i + 1)).setFont(font)));
            table.addCell(new Cell().add(new Paragraph("Data " + (i + 1)).setFont(font)));
            table.addCell(new Cell().add(new Paragraph("Data " + (i + 1)).setFont(font)));
        }

        document.add(table);
        document.close();
    }
}*/
