package com.example.fidas.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Report implements Serializable {
    private int id;
    private boolean reviewed;
    private int time;
    private String hospital;
    private MedicineLocation medicine;

    public int getId() {
        return id;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public int getTime() {
        return time;
    }

    public String getHospital() {
        return hospital;
    }

    public MedicineLocation getMedicine() {
        return medicine;
    }

    public Report(int id, boolean reviewed, int time, String hospital, MedicineLocation medicine) {
        this.id = id;
        this.reviewed = reviewed;
        this.time = time;
        this.hospital = hospital;
        this.medicine = medicine;
    }


}
