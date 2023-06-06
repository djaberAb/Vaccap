package com.example.vaccap.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccap.R;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class AppointmentAdapterPatient extends RecyclerView.Adapter<AppointmentAdapterPatient.AppointmentViewHolder> {

    private List<Appointment> appointments;

    public AppointmentAdapterPatient(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_list_item_patient, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.clinicNameTextView.setText(appointment.getClinic());
        holder.dateTextView.setText(appointment.getDate());
        holder.timeTextView.setText(appointment.getTime());
        holder.typeTextView.setText(appointment.getType());
        holder.status.setText(appointment.getStatus());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {

        public TextView clinicNameTextView;
        public TextView dateTextView;
        public TextView timeTextView;
        public TextView typeTextView, status;
        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            clinicNameTextView = itemView.findViewById(R.id.appointment_clinic_name);
            dateTextView = itemView.findViewById(R.id.appointment_date);
            timeTextView = itemView.findViewById(R.id.appointment_time);
            typeTextView = itemView.findViewById(R.id.appointment_type);
            status = itemView.findViewById(R.id.appointment_status);
        }
    }
}
