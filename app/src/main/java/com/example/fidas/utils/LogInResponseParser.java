package com.example.fidas.utils;

import android.util.Log;

import com.example.fidas.entity.LogInResponse;
import com.example.fidas.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

public class LogInResponseParser {
    public static LogInResponse getLogInInfoObjectFromJson(String logInJsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(logInJsonString);
        User user = null;
        JSONObject userJsonObject = jsonObject.optJSONObject("user");
        String message = jsonObject.optString("message");
        if (userJsonObject != null) {
            int id = userJsonObject.optInt("id");
            String username = userJsonObject.optString("username");
            String email = userJsonObject.optString("email");
            String hospital = userJsonObject.optString("hospital");
            String unit = userJsonObject.optString("unit");
            boolean admin = userJsonObject.optBoolean("admin");
            user = new User(id, username, email, hospital, unit, admin);
        }

        boolean error = jsonObject.getBoolean("error");

        LogInResponse logInResponse = new LogInResponse(error, message, user);
        return logInResponse;
    }
}
