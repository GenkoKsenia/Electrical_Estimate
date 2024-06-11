package com.example.electrical_estimate;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import javax.xml.parsers.SAXParser;

public class Calculator extends Dialog {
    private static Context context;
    private Button buttonOk;
    private Button buttonCancel;
    private Button areaPlus;
    private Button socketsPlus;
    private Button switchesPlus;
    private Button areaMinus;
    private Button socketsMinus;
    private Button switchesMinus;

    private EditText areaValue; //площадь
    private EditText socketsValue; //розетки
    private EditText switchesValue; //выключатели

    private Spinner materialValue; //материал

    private static ListWorks works;

    private String choiceMaterial;


    @SuppressLint("MissingInflatedId")
    public Calculator(Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.calculator);

        areaValue = findViewById(R.id.areaValue);
        socketsValue = findViewById(R.id.socketsValue);
        switchesValue = findViewById(R.id.switchesValue);

        buttonOk = findViewById(R.id.buttonOk);
        buttonCancel = findViewById(R.id.buttonCancel);

        materialValue = findViewById(R.id.materialValue);

        areaPlus = findViewById(R.id.areaPlus);
        socketsPlus = findViewById(R.id.socketsPlus);
        switchesPlus = findViewById(R.id.switchesPlus);
        areaMinus = findViewById(R.id.areaMinus);
        socketsMinus = findViewById(R.id.socketsMinus);
        switchesMinus = findViewById(R.id.switchesMinus);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item,
                new String[]{"Кирпич", "Бетон"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        materialValue.setAdapter(adapter);

        materialValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                switch (selectedItem) {
                    case "Кирпич":
                        choiceMaterial = "Кирпич";
                        break;
                    case "Бетон":
                        choiceMaterial = "Бетон";
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Действия, если ничего не выбрано
            }
        });

        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Table.fillingCalculator()==1) {
                    dismiss();  // Закрывает диалог
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        areaPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int number = Integer.parseInt(areaValue.getText().toString());
                    number += 10;
                    areaValue.setText(String.valueOf(number));
                }catch (NumberFormatException e){}
            }
        });

        socketsPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int number = Integer.parseInt(socketsValue.getText().toString());
                    number += 1;
                    socketsValue.setText(String.valueOf(number));
                }catch (NumberFormatException e){}
            }
        });

        switchesPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int number = Integer.parseInt(switchesValue.getText().toString());
                    number += 1;
                    switchesValue.setText(String.valueOf(number));
                }catch (NumberFormatException e){}
            }
        });

        areaMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int number = Integer.parseInt(areaValue.getText().toString());
                    if (number <= 10) {
                        number = 0;
                    } else number -= 10;
                    areaValue.setText(String.valueOf(number));
                }catch (NumberFormatException e){}
            }
        });

        socketsMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int number = Integer.parseInt(socketsValue.getText().toString());
                    if (number <= 0) {
                        number = 0;
                    } else number -= 1;
                    socketsValue.setText(String.valueOf(number));
                }catch (NumberFormatException e){}
            }
        });

        switchesMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int number = Integer.parseInt(switchesValue.getText().toString());
                    if (number <= 0) {
                        number = 0;
                    } else number -= 1;
                    switchesValue.setText(String.valueOf(number));
                }catch (NumberFormatException e){}
            }
        });
    }

    public ListWorks calculationVolume(){
        works = new ListWorks(context);
        if (Objects.equals(choiceMaterial, "Кирпич")){
            ArrayList<Integer> indexesToRemove = new ArrayList<>();
            indexesToRemove.add(1);
            indexesToRemove.add(3);
            indexesToRemove.add(4);
            works.ListWorksBrickCompose(indexesToRemove);
        }else if(Objects.equals(choiceMaterial, "Бетон")){
            ArrayList<Integer> indexesToRemove = new ArrayList<>();
            indexesToRemove.add(0);
            indexesToRemove.add(2);
            indexesToRemove.add(5);
            works.ListWorksBrickCompose(indexesToRemove);
        }

        if (areaValue.getText().toString().isEmpty()) {
            Toast.makeText(context, "Поле 'площадь' не должно быть пустым", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (socketsValue.getText().toString().isEmpty()) {
            Toast.makeText(context, "Поле 'розетки' не должно быть пустым", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (switchesValue.getText().toString().isEmpty()) {
            Toast.makeText(context, "Поле 'выключатели' не должно быть пустым", Toast.LENGTH_SHORT).show();
            return null;
        }

        int numberTens = Integer.parseInt(areaValue.getText().toString()) / 10;
        int numberSockets = Integer.parseInt(socketsValue.getText().toString());
        int numberSwitches = Integer.parseInt(switchesValue.getText().toString());

        if(numberTens == 0){
            numberTens = 1;
        }
        int volume = 0;
        for(int i = 0; i<works.size(); i++){
            switch (i){
                case 0:
                    volume = (numberSockets+numberSwitches)*3*numberTens;
                    works.get(i).setVolume(volume);
                    break;
                case 1:
                    volume = (numberSockets+numberSwitches)*numberTens;
                    works.get(i).setVolume(volume);
                    break;
                case 2:
                    volume = 3*numberTens;
                    works.get(i).setVolume(volume);
                    break;
                case 3:
                    volume = (numberSockets+numberSwitches)*3*numberTens;
                    works.get(i).setVolume(volume);
                    break;
                case 4:
                    volume = ((numberSockets+numberSwitches)*10 - (numberSockets+numberSwitches)*3)*numberTens;
                    works.get(i).setVolume(volume);
                    break;
                case 5:
                    volume = 3*numberTens;
                    works.get(i).setVolume(volume);
                    break;
                case 6:
                    volume = (numberSockets+numberSwitches)*numberTens;
                    works.get(i).setVolume(volume);
                    break;
                case 7:
                    volume = 1;
                    works.get(i).setVolume(volume);
                    break;
                case 8:
                    volume = 2*numberTens;
                    works.get(i).setVolume(volume);
                    break;
                case 9:
                    volume = 2*numberTens;
                    works.get(i).setVolume(volume);
                    break;
                default:
                    works.get(i).setVolume(0);
                    break;
            }
        }
        return works;
    }

}
