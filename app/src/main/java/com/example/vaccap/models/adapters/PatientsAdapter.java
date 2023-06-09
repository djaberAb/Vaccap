package com.example.vaccap.models.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccap.R;
import com.example.vaccap.models.User;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.ViewHolder> {
    private Context context;
    private List<User> patientList;

    public PatientsAdapter(Context context, List<User> patientList) {
        this.context = context;
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.patient_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User patient = patientList.get(position);

        // Set the patient name, phone number, and baby name in the TextViews
        holder.momNameTextView.setText(patient.getUserName());
        holder.momPhoneTextView.setText(patient.getPhoneNumber());
        holder.babyAgeTextView.setText(patient.getNameBaby());

    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public void updatePatientList(List<User> newPatientList) {
        patientList.clear();
        patientList.addAll(newPatientList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView momNameTextView;
        TextView momPhoneTextView;
        TextView babyAgeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            momNameTextView = itemView.findViewById(R.id.mom_name);
            momPhoneTextView = itemView.findViewById(R.id.mom_phone);
            babyAgeTextView = itemView.findViewById(R.id.baby_age);
        }
    }
}