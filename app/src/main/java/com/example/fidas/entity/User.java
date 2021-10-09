package com.example.fidas.entity;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String username;
    private String email;
    private String hospital;
    private String unit;
    private boolean admin;

    public User(int id, String username, String email, String hospital, String unit, boolean admin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.hospital = hospital;
        this.unit = unit;
        this.admin = admin;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getHospital() {
        return hospital;
    }

    public String getUnit() {
        return unit;
    }

    public boolean getAdmin() {
        return admin;
    }
}
