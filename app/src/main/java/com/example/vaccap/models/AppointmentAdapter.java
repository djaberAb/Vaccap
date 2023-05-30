package com.example.vaccap.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccap.R;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> appointments;

    public AppointmentAdapter(List<Appointment> appointments) {
        this.appointments = appointments;
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
        holder.userNameTextView.setText(appointment.getPatient());
        holder.dateTextView.setText(appointment.getDate());
        holder.timeTextView.setText(appointment.getTime());
        holder.typeTextView.setText(appointment.getType());

        // Set up the accept and decline button listeners
        holder.acceptButton.setOnClickListener(v -> {
            // Update the appointment status to "accepted"
            appointment.setStatus("accepted");
            // TODO: Save the updated appointment to a database or other data store

            // Notify the adapter that the data set has changed
            notifyDataSetChanged();
            });

        holder.declineButton.setOnClickListener(v -> { // Update the appointment status to "declined"
            appointment.setStatus("declined");

            // TODO: Save the updated appointment to a database or other data store

            // Notify the adapter that the data set has changed
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {

        public TextView userNameTextView;
        public TextView dateTextView;
        public TextView timeTextView;
        public TextView typeTextView;
        public Button acceptButton;
        public Button declineButton;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.appointment_user_name);
            dateTextView = itemView.findViewById(R.id.appointment_date);
            timeTextView = itemView.findViewById(R.id.appointment_time);
            typeTextView = itemView.findViewById(R.id.appointment_type);
            acceptButton = itemView.findViewById(R.id.accept_button);
            declineButton = itemView.findViewById(R.id.decline_button);
        }
    }
}
