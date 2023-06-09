package com.example.vaccap.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccap.R;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class AppointmentAdapterPatient extends RecyclerView.Adapter<AppointmentAdapterPatient.AppointmentViewHolder> {

    private List<Appointment> appointments;
    private OnItemClickListener listener;

    public AppointmentAdapterPatient(List<Appointment> appointments, OnItemClickListener listener) {
        this.appointments = appointments;
        this.listener = listener;
    }

    // Getter method for the appointments list
    public List<Appointment> getAppointments() {
        return appointments;
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
        holder.editButton.setOnClickListener(v -> listener.onEditClick(appointment));
        holder.cancelButton.setOnClickListener(v -> listener.onCancelClick(appointment));
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public interface OnItemClickListener {
        void onEditClick(Appointment appointment);
        void onCancelClick(Appointment appointment);
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {

        public TextView clinicNameTextView;
        public TextView dateTextView;
        public TextView timeTextView;
        public TextView typeTextView;
        public Button editButton;
        public Button cancelButton;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            clinicNameTextView = itemView.findViewById(R.id.clinic_name);
            dateTextView = itemView.findViewById(R.id.appointment_date_patient);
            timeTextView = itemView.findViewById(R.id.appointment_time_patient);
            typeTextView = itemView.findViewById(R.id.appointment_type_patient);
            editButton = itemView.findViewById(R.id.edit_button);
            cancelButton = itemView.findViewById(R.id.cancel_button);
        }
    }
}