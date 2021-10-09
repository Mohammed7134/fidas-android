package com.example.fidas.uiAdmin;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fidas.R;
import com.example.fidas.entity.FindMedicineLocationResponse;
import com.example.fidas.entity.Locations;
import com.example.fidas.entity.Report;
import com.example.fidas.network.NetworkUtils;
import com.example.fidas.utils.LocationAdapter;
import com.example.fidas.utils.MedicineLocationFinderResponseParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationEditFragment extends Fragment {
    private static final String ARG_REPORT = "report";
    private Spinner spinnerEdit;
    private Spinner code1;
    private Spinner code2;
    private Spinner code3;
    private Button submitButton;
    private Button deleteButton;
    private Report reportArg;
    private TextView location1;
    private TextView location2;
    private TextView location3;
    private TextView location4;
    public LocationEditFragment() {}

    public static LocationEditFragment newInstance(Report report) {
        LocationEditFragment fragment = new LocationEditFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_REPORT, report);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reportArg = (Report) getArguments().getSerializable(ARG_REPORT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_location_edit, container, false);
        //textView
        TextView medName = root.findViewById(R.id.medicine_name_in_edit_location);
        medName.setText(reportArg.getMedicine().getName());
        location1 = root.findViewById(R.id.drug_first_location_row_edit);
        location2 = root.findViewById(R.id.drug_second_location_row_edit);
        location3 = root.findViewById(R.id.drug_third_location_row_edit);
        location4 = root.findViewById(R.id.drug_forth_location_row_edit);
        location1.setText(reportArg.getMedicine().getLocations().getLocation1());
        location2.setText(reportArg.getMedicine().getLocations().getLocation2());
        location3.setText(reportArg.getMedicine().getLocations().getLocation3());
        location4.setText(reportArg.getMedicine().getLocations().getLocation4());

        //spinners
        spinnerEdit = root.findViewById(R.id.spinner_edit);
        code1 = root.findViewById(R.id.spinner_code1);
        code2 = root.findViewById(R.id.spinner_code2);
        code3 = root.findViewById(R.id.spinner_code3);
        spinnerEdit.setAdapter(chooseLocationAdapter(R.array.locations));
        code1.setAdapter(chooseLocationAdapter(R.array.code1));
        code2.setAdapter(chooseLocationAdapter(R.array.code2));
        code3.setAdapter(chooseLocationAdapter(R.array.code3));
        if (spinnerEdit.getSelectedItemPosition() == 0) {
            code1.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation1().substring(0, 2), R.array.code1));
            code2.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation1().substring(2, 4), R.array.code2));
            code3.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation1().substring(4, 5), R.array.code3));
        } else if (spinnerEdit.getSelectedItemPosition() == 1) {
            code1.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation2().substring(0, 2), R.array.code1));
            code2.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation2().substring(2, 4), R.array.code2));
            code3.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation2().substring(4, 5), R.array.code3));
        } else if (spinnerEdit.getSelectedItemPosition() == 2) {
            code1.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation3().substring(0, 2), R.array.code1));
            code2.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation3().substring(2, 4), R.array.code2));
            code3.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation3().substring(4, 5), R.array.code3));
        } else if (spinnerEdit.getSelectedItemPosition() == 3) {
            code1.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation4().substring(0, 2), R.array.code1));
            code2.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation4().substring(2, 4), R.array.code2));
            code3.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation4().substring(4, 5), R.array.code3));
        }
        spinnerEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spinnerEdit.getSelectedItemPosition() == 0 && reportArg.getMedicine().getLocations().getLocation1().length() == 5) {
                    code1.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation1().substring(0, 2), R.array.code1));
                    code2.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation1().substring(2, 4), R.array.code2));
                    code3.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation1().substring(4, 5), R.array.code3));
                } else if (spinnerEdit.getSelectedItemPosition() == 1 && reportArg.getMedicine().getLocations().getLocation2().length() == 5) {
                    code1.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation2().substring(0, 2), R.array.code1));
                    code2.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation2().substring(2, 4), R.array.code2));
                    code3.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation2().substring(4, 5), R.array.code3));
                } else if (spinnerEdit.getSelectedItemPosition() == 2 && reportArg.getMedicine().getLocations().getLocation3().length() == 5) {
                    code1.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation3().substring(0, 2), R.array.code1));
                    code2.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation3().substring(2, 4), R.array.code2));
                    code3.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation3().substring(4, 5), R.array.code3));
                } else if (spinnerEdit.getSelectedItemPosition() == 3 && reportArg.getMedicine().getLocations().getLocation4().length() == 5) {
                    code1.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation4().substring(0, 2), R.array.code1));
                    code2.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation4().substring(2, 4), R.array.code2));
                    code3.setSelection(getPositionFromList(reportArg.getMedicine().getLocations().getLocation4().substring(4, 5), R.array.code3));
                } else {
                    code1.setSelection(0);
                    code2.setSelection(0);
                    code3.setSelection(0);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        //buttons
        deleteButton = root.findViewById(R.id.delete_report_button);
        submitButton = root.findViewById(R.id.submit_edit_button);

        deleteButton.setOnClickListener(actionReport("true"));
        submitButton.setOnClickListener(actionReport("false"));
        return root;
    }
    private ArrayAdapter<String> chooseLocationAdapter(int array) {
        ArrayAdapter<String> chooseLocationAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(array));
        chooseLocationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return chooseLocationAdapter;
    }

    View.OnClickListener actionReport(String reject) {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LocationFinderGetTask(reject).execute();
            }
        };
        return clickListener;
    }
    private int getPositionFromList(String str, int array) {
        int position = 0;
        String[] list = getResources().getStringArray(array);
        for (int pos = 0; pos < list.length; pos++) {
            if (list[pos].equals(str)) {
                position = pos;
                Log.i("initial code", str);
            }
        }
        return position;
    }
    private String getLocationNumber(String locationN) {
        Locations locs = reportArg.getMedicine().getLocations();
        switch (locationN) {
            case "location1":
                return locs.getLocation1();
            case "location2":
                return locs.getLocation2();
            case "location3":
                return locs.getLocation3();
            case "location4":
                return locs.getLocation4();
        }
        return locs.getLocation1();
    }

    class LocationFinderGetTask extends AsyncTask<Void, Integer, FindMedicineLocationResponse> {
        private String reject;
        public LocationFinderGetTask(String reject) {
            this.reject = reject;
        }
        @Override
        protected FindMedicineLocationResponse doInBackground(Void... voids) {
            URL medicineLocationFinderUrl = NetworkUtils.buildDeleteReportUrl();
            JSONObject reportObject = new JSONObject();
            JSONObject locations = new JSONObject();
            JSONObject locs = new JSONObject();

            try {
                for (int i = 0; i <= 3; i++) {
                    if (!spinnerEdit.getSelectedItem().toString().equals("location" + (i+1))) {
                        locations.put(("location" + (i+1)), getLocationNumber("location" + (i+1)));
                    } else {
                        if (code1.getSelectedItem().toString().equals("")) {
                            locations.put(spinnerEdit.getSelectedItem().toString(), "");
                        } else {
                            locations.put(spinnerEdit.getSelectedItem().toString(), code1.getSelectedItem().toString() + code2.getSelectedItem().toString() + code3.getSelectedItem().toString());
                        }
                    }
                }
                locs.put("locations", locations);
                locs.put("id", reportArg.getMedicine().getId());
                reportObject.put("reject", reject);
                reportObject.put("reportId", reportArg.getId());
                reportObject.put("medicineLocations", locs);
                reportObject.put("place", reportArg.getHospital());
                Log.i("locations", reportObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();;
            }
            FindMedicineLocationResponse findMedicineLocationResponse;
            try {
                String locationJSONResponse = NetworkUtils.getResponseFromHttpUrl(medicineLocationFinderUrl, "POST", "application/json", reportObject.toString());
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
            if (!s.isError()) {
                if (reject == "true") {
                    getFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), spinnerEdit.getSelectedItem().toString() + " change submitted", Toast.LENGTH_SHORT).show();
                    if (spinnerEdit.getSelectedItem().toString().equals("location1")) {
                        location1.setText(code1.getSelectedItem().toString() + code2.getSelectedItem().toString() + code3.getSelectedItem().toString());
                    } else if (spinnerEdit.getSelectedItem().toString().equals("location2")) {
                        location2.setText(code1.getSelectedItem().toString() + code2.getSelectedItem().toString() + code3.getSelectedItem().toString());
                    } else if (spinnerEdit.getSelectedItem().toString().equals("location3")) {
                        location3.setText(code1.getSelectedItem().toString() + code2.getSelectedItem().toString() + code3.getSelectedItem().toString());
                    } else if (spinnerEdit.getSelectedItem().toString().equals("location4")) {
                        location4.setText(code1.getSelectedItem().toString() + code2.getSelectedItem().toString() + code3.getSelectedItem().toString());
                    }
                }
            } else {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}