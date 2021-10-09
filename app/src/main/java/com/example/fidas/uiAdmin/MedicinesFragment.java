package com.example.fidas.uiAdmin;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.fidas.R;
import com.example.fidas.entity.FindMedicineLocationResponse;
import com.example.fidas.entity.MedicineLocation;
import com.example.fidas.entity.MedicineLocationReport;
import com.example.fidas.entity.MedicineLocationReportResponse;
import com.example.fidas.entity.Report;
import com.example.fidas.network.NetworkUtils;
import com.example.fidas.utils.LocationAdapter;
import com.example.fidas.utils.MedicineLocationFinderResponseParser;
import com.example.fidas.utils.MedicineLocationReportResponseParser;
import com.example.fidas.utils.OnReportClickListener;
import com.example.fidas.utils.ReportsAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class MedicinesFragment extends Fragment implements OnReportClickListener {
    ArrayList<MedicineLocationReport> list = new ArrayList<>();
    ReportsAdapter reportsAdapter;
    RecyclerView reportsRecyclerView;
    TextView loadingText;

    public MedicinesFragment() {
        // Required empty public constructor
    }


    public static MedicinesFragment newInstance() {
        MedicinesFragment fragment = new MedicinesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_medicines, container, false);
        reportsRecyclerView = root.findViewById(R.id.medicines_reports_recycler_view);
        loadingText = root.findViewById(R.id.loading_text);
        new LocationReportsGetTask().execute();
        return root;
    }

    @Override
    public void onReportClickListener(Report report) {
        Fragment fragment = LocationEditFragment.newInstance(report);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.parent_admin_med, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    class LocationReportsGetTask extends AsyncTask<Void, Integer, MedicineLocationReportResponse> {
        @Override
        protected MedicineLocationReportResponse doInBackground(Void... voids) {
            URL medicineLocationReportsUrl = NetworkUtils.buildMedicineLocationReportsFinderUrl();
            String parameters = "hospital=farwaniya_locations";
            MedicineLocationReportResponse locationReportResponse;
            try {
                String locationJSONResponse = NetworkUtils.getResponseFromHttpUrl(medicineLocationReportsUrl, "POST", "application/x-www-form-urlencoded", parameters);
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
                Log.i(getContext().getClass().getSimpleName(), s.getMessage());
            } else {
                loadingText.setVisibility(View.GONE);
                networking(s);
            }
        }
    }
    private void networking(MedicineLocationReportResponse s) {
        reportsAdapter = new ReportsAdapter(s.getReports());
        reportsRecyclerView.setAdapter(reportsAdapter);
        reportsAdapter.setListener(this);
        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reportsAdapter.notifyDataSetChanged();
    }
}