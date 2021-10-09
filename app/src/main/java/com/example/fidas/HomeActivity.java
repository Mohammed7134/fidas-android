package com.example.fidas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fidas.entity.LogInResponse;
import com.example.fidas.entity.User;
import com.example.fidas.network.NetworkUtils;
import com.example.fidas.utils.LogInResponseParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class HomeActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private AppBarConfiguration mAppBarConfiguration;
    private Menu menu;
    private SharedPreferences pref;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);

        intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        User user = (User) getIntent().getSerializableExtra("user");
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView usernameDisplay = headerView.findViewById(R.id.username_text_display);
        TextView hospitalDisplay = headerView.findViewById(R.id.hospital_text_display);
        TextView unitDisplay = headerView.findViewById(R.id.unit_text_display);
        usernameDisplay.setText(user.getUsername());
        hospitalDisplay.setText(user.getHospital());
        unitDisplay.setText(user.getUnit());


        Button logout = findViewById(R.id.logout_button);
        Button adminLogin = findViewById(R.id.admin_button);
        if (!user.getAdmin()) {
            adminLogin.setVisibility(View.INVISIBLE);
        }
        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AdminActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_location_finder,
                R.id.nav_home, R.id.nav_contact, R.id.nav_about)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    class LogoutDataGetTask extends AsyncTask<Void, Integer, LogInResponse> {
        @Override
        protected LogInResponse doInBackground(Void... voids) {
            URL LoginUrl = NetworkUtils.buildLogoutUrl();
            LogInResponse logInResponse;
            try {
                String loginJSONResponse = NetworkUtils.getResponseFromHttpUrl(LoginUrl, "POST", "application/x-www-form-urlencoded", "logout");
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
            Toast.makeText(HomeActivity.this, s.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        new HomeActivity.LogoutDataGetTask().execute();
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        startActivity(intent);
    }

}