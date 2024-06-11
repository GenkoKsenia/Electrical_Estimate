package com.example.electrical_estimate;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.ArrayRes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListWorks {
    private ArrayList<Work> works = new ArrayList<>();

    public ListWorks(Context context){
        DataBaseHelper dbHelper = new DataBaseHelper(context);
        this.works = dbHelper.getAllWorks();
    }

    //удаляем не нужные работы
    public void ListWorksBrickCompose(ArrayList<Integer> indexesToRemove) {
        // Сортируем индексы в обратном порядке, чтобы избежать ошибок при удалении
        Collections.sort(indexesToRemove, Collections.reverseOrder());

        for (int index : indexesToRemove) {
            if (index >= 0 && index < works.size()) {
                works.remove(index);
            }
        }
    }

    public Work get(int index) {
        return works.get(index);
    }

    public int size() {
        return works.size();
    }
}
