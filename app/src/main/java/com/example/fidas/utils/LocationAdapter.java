package com.example.fidas.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fidas.R;

import java.util.ArrayList;
import com.example.fidas.entity.MedicineLocation;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    private OnLocationClickListener listener;
    private ArrayList<MedicineLocation> list = new ArrayList<>();
    private Context mContext;
    public void setListener(OnLocationClickListener onLocationClickListener) {
        this.listener = onLocationClickListener;
    }
    public LocationAdapter(ArrayList<MedicineLocation> list) {
        this.list = list;
    }
    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.medicine_location_row, parent, false);
        mContext = parent.getContext();
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        holder.location.setText(list.get(position).getLocations().getLocation1());
        holder.position = position;
        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    listener.onLocationClickListener(list.get(position));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView location;
        private LinearLayout container;
        private int position;
        LocationViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.drug_name_location_row);
            location = itemView.findViewById(R.id.drug_first_location_row);
            container = itemView.findViewById(R.id.parent_row);
        }
    }
}
