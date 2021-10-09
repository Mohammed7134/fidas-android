package com.example.fidas.utils;

import com.example.fidas.entity.FindMedicineLocationResponse;
import com.example.fidas.entity.Locations;
import com.example.fidas.entity.LogInResponse;
import com.example.fidas.entity.MedicineLocation;
import com.example.fidas.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MedicineLocationFinderResponseParser {
    public static FindMedicineLocationResponse getLocationsInfoObjectFromJson(String locationJsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(locationJsonString);
        ArrayList<MedicineLocation> list = new ArrayList<>();

        JSONArray medicinesJsonObject = jsonObject.optJSONArray("medicines");
        String message = jsonObject.optString("message");

        if (medicinesJsonObject != null) {
            for(int i = 0; i < medicinesJsonObject.length(); i++) {
                int id = medicinesJsonObject.getJSONObject(i).optInt("id");
                String name = medicinesJsonObject.getJSONObject(i).optString("name");
                String pharmacy = medicinesJsonObject.getJSONObject(i).optString("pharmacy");
                JSONObject locations = medicinesJsonObject.getJSONObject(i).optJSONObject("locations");
                String location1 = locations.optString("location1");
                String location2 = locations.optString("location2");
                String location3 = locations.optString("location3");
                String location4 = locations.optString("location4");
                Locations locationsFinal = new Locations(location1, location2, location3, location4);
                MedicineLocation medLoc = new MedicineLocation(id, name, pharmacy, locationsFinal);
                list.add(medLoc);
            }
        }

        boolean error = jsonObject.getBoolean("error");

        FindMedicineLocationResponse findMedicineLocationResponse = new FindMedicineLocationResponse(error, message, list);
        return findMedicineLocationResponse;
    }

}
