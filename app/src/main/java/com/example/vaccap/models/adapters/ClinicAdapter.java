package com.example.vaccap.models.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccap.R;
import com.example.vaccap.models.Clinic;

import java.util.List;

public class ClinicAdapter extends RecyclerView.Adapter<ClinicAdapter.ViewHolder> {
    private Context context;
    private final List<Clinic> items;
    private OnItemClickListener mListener;

    public ClinicAdapter(List<Clinic> items) {
        this.items = items;
    }


    public interface OnItemClickListener {
        void onItemClick(int position, String clinicName);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = (OnItemClickListener) listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView clinicNameTextView;
        TextView clinicAddressTextView;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            clinicNameTextView = itemView.findViewById(R.id.clinic_name);
            clinicAddressTextView = itemView.findViewById(R.id.clinic_address);

            String clinicName = clinicNameTextView.getText().toString();
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position, clinicName);
                    }
                }
            });
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.clinic_item, parent, false);
        return new ViewHolder(itemView, mListener);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        Clinic clinic = items.get(position);

        // Set the clinic name and address in the TextViews
        holder.clinicNameTextView.setText(clinic.getClinicName());
        holder.clinicAddressTextView.setText(clinic.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateClinicList(List<Clinic> newClinicList) {
        items.clear();
        items.addAll(newClinicList);
        notifyDataSetChanged();
    }

}
