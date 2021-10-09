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
import com.example.fidas.entity.Report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportsViewHolder>{
    private OnReportClickListener listener;
    ArrayList<Report> list = new ArrayList<>();
    Context mContext;
    public ReportsAdapter(ArrayList<Report> list) {
        this.list = list;
    }
    public void setListener(OnReportClickListener onReportClickListener) {
        this.listener = onReportClickListener;
    }
    @NonNull
    @Override
    public ReportsAdapter.ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.location_report_row, parent, false);
        mContext = parent.getContext();
        return new ReportsAdapter.ReportsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsAdapter.ReportsViewHolder holder, int position) {
        holder.name.setText(list.get(position).getMedicine().getName());
        holder.location.setText(list.get(position).getMedicine().getPharmacy());
        String dateAsText = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(list.get(position).getTime() * 1000L));
        holder.date.setText(dateAsText);
        holder.position = position;
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onReportClickListener(list.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ReportsViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView location;
        private TextView date;
        private LinearLayout parentLayout;
        private int position;
        ReportsViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.drug_name_report_row);
            location = itemView.findViewById(R.id.report_location_report_row);
            date = itemView.findViewById(R.id.report_date_report_row);
            parentLayout = itemView.findViewById(R.id.parent_row_report);
        }
    }
}
