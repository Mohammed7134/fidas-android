package com.example.fidas.network;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

    private static String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "https://www.farwaniyapharmacist.online/FidasDevelopment/v1/";
    private static final String USERNAME_PARAM = "username";
    private static final String PASSWORD_PARAM = "password";
    static final String COOKIES_HEADER = "Set-Cookie";
    static java.net.CookieManager msCookieManager = new java.net.CookieManager();


    public static String getResponseFromHttpUrl(URL url, String method, String contentType, String params) throws IOException {
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        httpsURLConnection.getDoOutput();
        httpsURLConnection.setRequestMethod(method);
        httpsURLConnection.setRequestProperty("Content-Type", contentType + "; charset=UTF-8");
        httpsURLConnection.setRequestProperty("Accept", contentType);
        if (!params.contains(USERNAME_PARAM)) {
            if (msCookieManager.getCookieStore().getCookies().get(0).toString() != null) {
                httpsURLConnection.setRequestProperty("Cookie", msCookieManager.getCookieStore().getCookies().get(0).toString());
            } else {
                Log.i("Cookie", "problem");
            }
        }

        DataOutputStream wr = new DataOutputStream(httpsURLConnection.getOutputStream());
        wr.writeBytes(params);

        wr.flush();
        wr.close();

        httpsURLConnection.connect();

        try {
            InputStream inputStream = httpsURLConnection.getInputStream();

            getSession(httpsURLConnection);

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            Log.i(TAG, "Response: " + response);
            return response;
        } finally {
            httpsURLConnection.disconnect();
        }
    }
    public static URL buildLogoutUrl() {
        try {
            return new URL(Uri.parse(BASE_URL + "endSession.php").buildUpon().build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static URL buildLoginUrl() {
        try {
            return new URL(Uri.parse(BASE_URL + "login.php").buildUpon().build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL buildMedicineLocationFinderUrl() {
        try {
            return new URL(Uri.parse(BASE_URL + "searchMedicineLocation.php").buildUpon().build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static URL buildMedicineLocationReportsFinderUrl() {
        try {
            return new URL(Uri.parse(BASE_URL + "loadReports.php").buildUpon().build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static  URL buildDeleteReportUrl() {
        try {
            return new URL(Uri.parse(BASE_URL + "adminPage.php").buildUpon().build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static  URL buildReportLocationUrl() {
        try {
            return new URL(Uri.parse(BASE_URL + "reportLocation.php").buildUpon().build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }







    public static String loginParams(String username, String password) {
        return USERNAME_PARAM + "=" + username + "&" + PASSWORD_PARAM + "=" + password;
    }

    private static void getSession(HttpsURLConnection httpsURLConnection) {
        Map<String, List<String>> headerFields = httpsURLConnection.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
        if (cookiesHeader != null) {
            for (String cookieHeader : cookiesHeader) {
                List<HttpCookie> cookies;
                try {
                    cookies = HttpCookie.parse(cookieHeader);
                } catch (NullPointerException e) {
                    continue;
                }

                if (cookies != null) {
                    if (cookies.size() > 0) {
                        msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookieHeader).get(0));
                        Log.i(TAG, msCookieManager.getCookieStore().getCookies().get(0).toString());
                    }
                }
            }
        }
    }
}
