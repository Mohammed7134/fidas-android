package com.example.fidas.entity;

import java.util.ArrayList;

public class FindMedicineLocationResponse {
    private boolean error;
    private String message = "";
    private ArrayList<MedicineLocation> medicines = null;

    public FindMedicineLocationResponse(boolean error, String message, ArrayList<MedicineLocation> medicines) {
        this.error = error;
        this.message = message;
        this.medicines = medicines;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<MedicineLocation> getMedicines() {
        return medicines;
    }
}
