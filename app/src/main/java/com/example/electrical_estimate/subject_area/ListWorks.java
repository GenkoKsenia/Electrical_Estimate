package com.example.electrical_estimate.subject_area;


import android.content.Context;

import com.example.electrical_estimate.DB.DataBaseHelper;

import java.util.ArrayList;
import java.util.Collections;

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
