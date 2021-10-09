package com.example.fidas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fidas.entity.LogInResponse;
import com.example.fidas.network.NetworkUtils;
import com.example.fidas.utils.LogInResponseParser;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    EditText usernameTextEdit;
    EditText passwordTextEdit;
    Button signInButton;

    String username;
    String password;

    SharedPreferences pref;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameTextEdit = findViewById(R.id.username_text_edit);
        passwordTextEdit = findViewById(R.id.password_text_edit);
        signInButton = findViewById(R.id.sign_in_button);

        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        intent = new Intent(MainActivity.this, HomeActivity.class);
        if(pref.contains("username") && pref.contains("password")){
            username = pref.getString("username", "");
            password = pref.getString("password", "");
            login();
        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameTextEdit.getText().toString();
                password = passwordTextEdit.getText().toString();
                signIn();
            }
        });
    }
    class LoginDataGetTask extends AsyncTask<Void, Integer, LogInResponse> {
        @Override
        protected LogInResponse doInBackground(Void... voids) {
            URL LoginUrl = NetworkUtils.buildLoginUrl();
            String parameters = NetworkUtils.loginParams(username, password);
            LogInResponse logInResponse;
            try {
                String loginJSONResponse = NetworkUtils.getResponseFromHttpUrl(LoginUrl, "POST", "application/x-www-form-urlencoded", parameters);
                logInResponse = LogInResponseParser.getLogInInfoObjectFromJson(loginJSONResponse);
                return logInResponse;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(LogInResponse s) {
            super.onPostExecute(s);
            if (s.isError()) {
                Toast.makeText(MainActivity.this, s.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Welcome " + s.getUser().getUsername(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("user", s.getUser());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }

    private void signIn() {
        if (!username.isEmpty() && !password.isEmpty()) {
            login();
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("username",username);
            editor.putString("password",password);
            editor.commit();
        } else {
            Log.i(TAG, "fields not filled");
        }
    }
    private void login() {
        new LoginDataGetTask().execute();
        signInButton.setText("Loading...");
        signInButton.setEnabled(false);
    }
}