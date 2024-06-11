package com.example.electrical_estimate.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.electrical_estimate.subject_area.Work;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "electrical_work.db";
    private static final int DATABASE_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //создание таблиц
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создаем таблицы
        String createTableQuery = "CREATE TABLE IF NOT EXISTS types_of_jobs ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name_job TEXT, "
                + "measure TEXT, "
                + "price REAL);";
        db.execSQL(createTableQuery);
        insertData(db);
        Log.d("Database", "Database created successfully");
    }

    //обновление таблиц
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Обновление схемы базы данных
        db.execSQL("DROP TABLE IF EXISTS types_of_jobs");
        onCreate(db);
    }

    public SQLiteDatabase openDatabase() {
        return this.getWritableDatabase();
    }

    //заполнение таблицы
    private void insertData(SQLiteDatabase db) {
        // Вставка данных в таблицу types_of_jobs
        String insertQuery1 = "INSERT INTO types_of_jobs (name_job, measure, price) VALUES ('Штроба в кирпиче', 'метр', 300.0);";
        String insertQuery2 = "INSERT INTO types_of_jobs (name_job, measure, price) VALUES ('Штроба в бетоне', 'метр', 500.0);";
        String insertQuery3 = "INSERT INTO types_of_jobs (name_job, measure, price) VALUES ('Коронкование в кирпиче', 'штука', 200.0);";
        String insertQuery4 = "INSERT INTO types_of_jobs (name_job, measure, price) VALUES ('Коронкование в бетоне', 'штука', 500.0);";
        String insertQuery5 = "INSERT INTO types_of_jobs (name_job, measure, price) VALUES ('Сверление сквозных отверстий в бетоне', 'см', 30.0);";
        String insertQuery6 = "INSERT INTO types_of_jobs (name_job, measure, price) VALUES ('Сверление сквозных отверстий в кирпиче', 'см', 10.0);";
        String insertQuery7 = "INSERT INTO types_of_jobs (name_job, measure, price) VALUES ('Прокладка кабеля открытым способом в штробе без гофры', 'метр', 100.0);";
        String insertQuery8 = "INSERT INTO types_of_jobs (name_job, measure, price) VALUES ('Прокладка кабеля в гофре', 'метр', 150.0);";
        String insertQuery9 = "INSERT INTO types_of_jobs (name_job, measure, price) VALUES ('Монтаж и распайка коробок', 'штука', 500.0);";
        String insertQuery10 = "INSERT INTO types_of_jobs (name_job, measure, price) VALUES ('Монтаж подрозетника', 'штука', 200.0);";
        String insertQuery11 = "INSERT INTO types_of_jobs (name_job, measure, price) VALUES ('Монтаж щита', 'штука', 3000.0);";
        String insertQuery12 = "INSERT INTO types_of_jobs (name_job, measure, price) VALUES ('Сборка щита', 'штука', 500.0);";
        String insertQuery13 = "INSERT INTO types_of_jobs (name_job, measure, price) VALUES ('Подключение кабелей в щите', 'штука', 300.0);";

        db.execSQL(insertQuery1);
        db.execSQL(insertQuery2);
        db.execSQL(insertQuery3);
        db.execSQL(insertQuery4);
        db.execSQL(insertQuery5);
        db.execSQL(insertQuery6);
        db.execSQL(insertQuery7);
        db.execSQL(insertQuery8);
        db.execSQL(insertQuery9);
        db.execSQL(insertQuery10);
        db.execSQL(insertQuery11);
        db.execSQL(insertQuery12);
        db.execSQL(insertQuery13);
    }

    // Метод для получения всех значений столбца name_job
    public ArrayList<String> getAllJobs() {
        ArrayList<String> jobList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name_job FROM types_of_jobs", null);

        if (cursor.moveToFirst()) {
            do {
                String nameJob = cursor.getString(cursor.getColumnIndexOrThrow("name_job"));
                jobList.add(nameJob);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return jobList;
    }

    public ArrayList<Work> getAllWorks() {
        ArrayList<Work> workList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name_job, measure, price FROM types_of_jobs", null);

        if (cursor.moveToFirst()) {
            do {
                Work work = new Work();
                String nameJob = cursor.getString(cursor.getColumnIndexOrThrow("name_job"));
                String measure = cursor.getString(cursor.getColumnIndexOrThrow("measure"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));

                // Инициализация объекта Work
                work.setName(nameJob);
                work.setMeasure(measure);
                work.setPrice(price);

                // Добавление объекта Work в список
                workList.add(work);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return workList;
    }

    // Метод для получения цены
    public double getPriceForJob(String jobName) {
        SQLiteDatabase db = this.getReadableDatabase();
        double price = 0;
        Cursor cursor = db.rawQuery("SELECT price FROM types_of_jobs WHERE name_job = ?", new String[]{jobName});
        if (cursor.moveToFirst()) {
            price = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return price;
    }

    public String getMeasureForJob(String jobName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String measure = "";
        Cursor cursor = db.rawQuery("SELECT measure FROM types_of_jobs WHERE name_job = ?", new String[]{jobName});
        if (cursor.moveToFirst()) {
            measure = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return measure;
    }
}
