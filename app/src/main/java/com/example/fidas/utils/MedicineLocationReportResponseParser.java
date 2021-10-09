package com.example.fidas.utils;

import com.example.fidas.entity.FindMedicineLocationResponse;
import com.example.fidas.entity.Locations;
import com.example.fidas.entity.MedicineLocation;
import com.example.fidas.entity.MedicineLocationReport;
import com.example.fidas.entity.MedicineLocationReportResponse;
import com.example.fidas.entity.Report;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MedicineLocationReportResponseParser {
    public static MedicineLocationReportResponse getReportsInfoObjectFromJson(String reportsJsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(reportsJsonString);
        ArrayList<Report> list = new ArrayList<>();

        JSONArray reportsJsonObject = jsonObject.optJSONArray("reports");
        String message = jsonObject.optString("message");

        if (reportsJsonObject != null) {
            for(int i = 0; i < reportsJsonObject.length(); i++) {
                int reportId = reportsJsonObject.getJSONObject(i).optInt("id");
                boolean reviewed = reportsJsonObject.getJSONObject(i).optBoolean("reviewed");
                int time = reportsJsonObject.getJSONObject(i).optInt("time");
                String hospital = reportsJsonObject.getJSONObject(i).optString("hospital");

                JSONObject medicineObject = reportsJsonObject.getJSONObject(i).optJSONObject("medicine");
                int medId = medicineObject.optInt("id");
                String name = medicineObject.optString("name");
                String pharmacy = medicineObject.optString("pharmacy");
                JSONObject locations = medicineObject.optJSONObject("locations");
                String location1 = locations.optString("location1");
                String location2 = locations.optString("location2");
                String location3 = locations.optString("location3");
                String location4 = locations.optString("location4");

                Locations locationsFinal = new Locations(location1, location2, location3, location4);
                MedicineLocation med = new MedicineLocation(medId, name, pharmacy, locationsFinal);

                Report report = new Report(reportId, reviewed, time, hospital, med);
                list.add(report);

            }
        }

        boolean error = jsonObject.getBoolean("error");

        MedicineLocationReportResponse medicineLocationReportResponse = new MedicineLocationReportResponse(error, message, list);
        return medicineLocationReportResponse;
    }
}
