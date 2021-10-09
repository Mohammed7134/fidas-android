package com.example.fidas.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class MedicineLocation implements Serializable {
    int id;
    String name;
    String pharmacy;
    Locations locations;

    public MedicineLocation(int id, String name, String pharmacy, Locations locations) {
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

