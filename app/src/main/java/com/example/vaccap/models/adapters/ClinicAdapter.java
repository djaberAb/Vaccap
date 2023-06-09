package com.example.vaccap.models.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccap.R;
import com.example.vaccap.models.Clinic;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class ClinicAdapter extends RecyclerView.Adapter<ClinicAdapter.ViewHolder> {
    private Context context;
    private List<Clinic> clinicList;

    public ClinicAdapter(Context context, List<Clinic> clinicList) {
        this.context = context;
        this.clinicList = clinicList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.clinic_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Clinic clinic = clinicList.get(position);

        // Set the clinic name and address in the TextViews
        holder.clinicNameTextView.setText(clinic.getClinicName());
        holder.clinicAddressTextView.setText(clinic.getPhoneNumber());

    }

    @Override
    public int getItemCount() {
        return clinicList.size();
    }

    public void updateClinicList(List<Clinic> newClinicList) {
        clinicList.clear();
        clinicList.addAll(newClinicList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView clinicNameTextView;
        TextView clinicAddressTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            clinicNameTextView = itemView.findViewById(R.id.clinic_name);
            clinicAddressTextView = itemView.findViewById(R.id.clinic_address);
        }
    }
}