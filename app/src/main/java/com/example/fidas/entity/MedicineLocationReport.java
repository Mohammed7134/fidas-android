package com.example.fidas.entity;

public class MedicineLocationReport {
    int id;
    String name;
    String pharmacy;
    Locations locations;

    public MedicineLocationReport(int id, String name, String pharmacy, Locations locations) {
        this.id = id;
        this.name = name;
        this.pharmacy = pharmacy;
        this.locations = locations;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPharmacy() {
        return pharmacy;
    }

    public Locations getLocations() {
        return locations;
    }
}
