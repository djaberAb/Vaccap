package com.example.vaccap.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccap.MainActivity;
import com.example.vaccap.R;

import java.util.List;

public class VaccineAdapter extends RecyclerView.Adapter<VaccineAdapter.MyViewHolder> {
    private List<VaccineInfo> items;
    private OnItemClickListener mListener;

    public VaccineAdapter(List<VaccineInfo> items) {
        this.items = items;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ageRange, names;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            ageRange = itemView.findViewById(R.id.tvAgeRange);
            names = itemView.findViewById(R.id.tvVaccineNames);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vaccine_card, parent, false);
        return new MyViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        VaccineInfo vaccine = items.get(position);
        holder.ageRange.setText(vaccine.getAgeRange());
        holder.names.setText(vaccine.getVaccineNames());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
