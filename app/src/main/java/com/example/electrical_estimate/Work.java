package com.example.electrical_estimate;

public class Work {
    private String name; //название
    private String measure; //ед измерения
    private double price; //цена
    private int volume; //объем

    Work(){

    }

    Work(String name, String measure,double price){
        this.name = name;
        this.measure = measure;
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setMeasure(String measure){
        this.measure = measure;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getName() {
        return this.name;
    }
    public int getVolume() {
        return this.volume;
    }
}
