package com.example.fidas.entity;

import java.util.ArrayList;

public class MedicineLocationReportResponse {
    private boolean error;
    private String message = "";
    private ArrayList<Report> reports = null;

    public MedicineLocationReportResponse(boolean error, String message, ArrayList<Report> reports) {
        this.error = error;
        this.message = message;
        this.reports = reports;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<Report> getReports() {
        return reports;
    }
}
