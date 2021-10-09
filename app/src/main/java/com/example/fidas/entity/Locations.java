package com.example.fidas.entity;

import java.io.Serializable;

public class Locations implements Serializable {
    String location1;
    String location2;

    public String getLocation1() {
        return location1;
    }

    public String getLocation2() {
        return location2;
    }

    public String getLocation3() {
        return location3;
    }

    public String getLocation4() {
        return location4;
    }

    String location3;
    String location4;

    public Locations(String location1, String location2, String location3, String location4) {
        this.location1 = location1;
        this.location2 = location2;
        this.location3 = location3;
        this.location4 = location4;
    }
}
