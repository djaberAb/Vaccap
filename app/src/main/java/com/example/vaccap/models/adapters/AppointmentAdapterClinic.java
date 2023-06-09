package com.example.vaccap.models.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccap.R;
import com.example.vaccap.models.Appointment;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class AppointmentAdapterClinic extends RecyclerView.Adapter<AppointmentAdapterClinic.AppointmentViewHolder> {
    private List<Appointment> appointments;
    private OnAppointmentClickListener onAppointmentClickListener;

    public AppointmentAdapterClinic(List<Appointment> appointments, OnAppointmentClickListener onAppointmentClickListener) {
        this.appointments = appointments;
        this.onAppointmentClickListener = onAppointmentClickListener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_list_item, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);

        holder.patientNameTextView.setText(appointment.getPatient());
        holder.dateTextView.setText(appointment.getDate());
        holder.timeTextView.setText(appointment.getTime());
        holder.typeTextView.setText(appointment.getType());

        holder.acceptButton.setOnClickListener(v -> onAppointmentClickListener.onAcceptButtonClick(appointment));
        holder.declineButton.setOnClickListener(v -> onAppointmentClickListener.onDeclineButtonClick(appointment));
        holder.editButton.setOnClickListener(v -> onAppointmentClickListener.onEditButtonClick(appointment));
        holder.cancelButton.setOnClickListener(v -> onAppointmentClickListener.onCancelButtonClick(appointment));
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private TextView patientNameTextView;
        private TextView dateTextView;
        private TextView timeTextView;
        private TextView typeTextView;
        private Button acceptButton;
        private Button declineButton;
        private Button editButton;
        private Button cancelButton;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            patientNameTextView = itemView.findViewById(R.id.patient_name);
            dateTextView = itemView.findViewById(R.id.appointment_date);
            timeTextView = itemView.findViewById(R.id.appointment_time);
            typeTextView = itemView.findViewById(R.id.appointment_type);
            acceptButton = itemView.findViewById(R.id.accept_button);
            declineButton = itemView.findViewById(R.id.decline_button);
            editButton = itemView.findViewById(R.id.edit_button);
            cancelButton = itemView.findViewById(R.id.cancel_button);
        }
    }

    public interface OnAppointmentClickListener {
        void onAcceptButtonClick(Appointment appointment);
        void onDeclineButtonClick(Appointment appointment);
        void onEditButtonClick(Appointment appointment);
        void onCancelButtonClick(Appointment appointment);
    }
}