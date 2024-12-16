package com.example.cs360_app;

public class WeightRecordModel {
    private int id;
    private String date;
    private String weight;
    int image;

    // Constructor for WeightRecordModel
    public WeightRecordModel(int id, String date, String weight, int image) {
        this.id = id;
        this.date = date;
        this.weight = weight;
        this.image = image;
    }
    // == Getters and Setters == //
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
