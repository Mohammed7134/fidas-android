package com.example.fidas.ui.locationFinder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fidas.R;
import com.example.fidas.entity.FindMedicineLocationResponse;
import com.example.fidas.entity.MedicineLocation;
import com.example.fidas.entity.MedicineLocationReportResponse;
import com.example.fidas.network.NetworkUtils;
import com.example.fidas.utils.LocationAdapter;
import com.example.fidas.utils.MedicineLocationFinderResponseParser;
import com.example.fidas.utils.MedicineLocationReportResponseParser;
import com.example.fidas.utils.OnLocationClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class LocationFinderFragment extends Fragment implements OnLocationClickListener {
    Spinner spinner;
    SearchView searchView;
    LocationAdapter locationAdapter;
    RecyclerView locationsRecyclerView;
    private LocationFinderViewModel locationFinderViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        locationFinderViewModel = new ViewModelProvider(this).get(LocationFinderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_find_medicine, container, false);
        spinner = (Spinner) root.findViewById(R.id.spinner);
        Button findButton = root.findViewById(R.id.submit_pharmacy_button);
        CardView choosePharmacyCard = root.findViewById(R.id.choosePharmacyCard);
        searchView = root.findViewById(R.id.find_medicine_search_view);
        FloatingActionButton changePharmacyButton = root.findViewById(R.id.change_pharmacy_button);
        TextView blockView = root.findViewById(R.id.block_view);

        locationsRecyclerView = root.findViewById(R.id.locations_recycler_view);

        ArrayAdapter<String> choosePharmacyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.pharmacies));
        choosePharmacyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(choosePharmacyAdapter);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        blockView.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "Long tab to report incorrect location", Toast.LENGTH_LONG).show();

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePharmacyCard.setVisibility(View.INVISIBLE);
                Log.i(getContext().getClass().getSimpleName(), spinner.getSelectedItem().toString());
                blockView.setVisibility(View.GONE);
            }
        });
        changePharmacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePharmacyCard.setVisibility(View.VISIBLE);
                blockView.setVisibility(View.VISIBLE);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                new LocationFinderGetTask().execute();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return root;
    }

    @Override
    public void onLocationClickListener(MedicineLocation location) {
        new LocationReportsGetTask(location.getId()).execute();
    }

    class LocationFinderGetTask extends AsyncTask<Void, Integer, FindMedicineLocationResponse> {
        @Override
        protected FindMedicineLocationResponse doInBackground(Void... voids) {
            URL medicineLocationFinderUrl = NetworkUtils.buildMedicineLocationFinderUrl();
            String parameters = "hospital=farwaniya_locations&pharmacy=" + spinner.getSelectedItem().toString() + "&searchText=" + searchView.getQuery().toString();
            FindMedicineLocationResponse findMedicineLocationResponse;
            try {
                String locationJSONResponse = NetworkUtils.getResponseFromHttpUrl(medicineLocationFinderUrl, "POST", "application/x-www-form-urlencoded", parameters);
                findMedicineLocationResponse = MedicineLocationFinderResponseParser.getLocationsInfoObjectFromJson(locationJSONResponse);
                return findMedicineLocationResponse;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(FindMedicineLocationResponse s) {
            super.onPostExecute(s);
            if (s.isError()) {
                Log.i(getContext().getClass().getSimpleName(), s.getMessage());
            } else {
                Log.i(getContext().getClass().getSimpleName(), "size of the medicines array is: " + s.getMedicines().size());
                networking(s);
            }
        }
    }
    private void networking(FindMedicineLocationResponse s) {
        locationAdapter = new LocationAdapter(s.getMedicines());
        locationsRecyclerView.setAdapter(locationAdapter);
        locationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        locationAdapter.notifyDataSetChanged();
        locationAdapter.setListener(this);
    }
    class LocationReportsGetTask extends AsyncTask<Void, Integer, MedicineLocationReportResponse> {
        private int medId;
        public LocationReportsGetTask(int medId) {
            this.medId = medId;
        }

        @Override
        protected MedicineLocationReportResponse doInBackground(Void... voids) {
            URL medicineLocationReportsUrl = NetworkUtils.buildReportLocationUrl();
            JSONObject object = new JSONObject();
            JSONObject med = new JSONObject();
            try {
                med.put("id", medId);
                object.put("reviewed", "false");
                object.put("time", System.currentTimeMillis() / 1000);
                object.put("medicine", med);
                object.put("hospital", "farwaniya_locations");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MedicineLocationReportResponse locationReportResponse;
            try {
                String locationJSONResponse = NetworkUtils.getResponseFromHttpUrl(medicineLocationReportsUrl, "POST", "application/json", object.toString());
                locationReportResponse = MedicineLocationReportResponseParser.getReportsInfoObjectFromJson(locationJSONResponse);
                return locationReportResponse;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MedicineLocationReportResponse s) {
            super.onPostExecute(s);
            if (s.isError()) {
                Toast.makeText(getContext(), "Error Reporting", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Reported", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
