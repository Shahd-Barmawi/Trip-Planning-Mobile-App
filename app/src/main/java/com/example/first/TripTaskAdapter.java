package com.example.first;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TripTaskAdapter extends RecyclerView.Adapter<TripTaskAdapter.Holder> {

    ArrayList<TripTask> taskList;
    ArrayList<TripTask> fullList;
    OnItemClick listener;

    public interface OnItemClick {
        void onClick(int pos);
    }

    public TripTaskAdapter(ArrayList<TripTask> taskList, OnItemClick listener) {
        this.taskList = taskList;
        this.listener = listener;
        this.fullList = new ArrayList<>(taskList);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip_task, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        TripTask t = taskList.get(position);

        h.tvTitle.setText(t.getTitle());
        h.tvCity.setText(t.getCity());
        h.tvDateTime.setText(t.getDate() + " • " + t.getTimeOfDay());
        h.checkDone.setChecked(t.isDone());

        // ICONS
        switch (t.getType()) {
            case "Flight": h.imgTask.setImageResource(R.drawable.ic_flight); break;
            case "Hotel": h.imgTask.setImageResource(R.drawable.ic_hotel); break;
            case "Beach": h.imgTask.setImageResource(R.drawable.ic_beach); break;
            case "Food": h.imgTask.setImageResource(R.drawable.ic_food); break;
            case "Shopping": h.imgTask.setImageResource(R.drawable.ic_shopping); break;
            default: h.imgTask.setImageResource(R.drawable.ic_task); break;
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void update(ArrayList<TripTask> list) {
        this.taskList = list;
        this.fullList = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    public void filter(String q) {
        q = q.toLowerCase();
        taskList.clear();

        if (q.isEmpty()) taskList.addAll(fullList);
        else
            for (TripTask t : fullList)
                if (t.getTitle().toLowerCase().contains(q) ||
                        t.getCity().toLowerCase().contains(q) ||
                        t.getType().toLowerCase().contains(q))
                    taskList.add(t);

        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView imgTask;
        TextView tvTitle, tvCity, tvDateTime;
        CheckBox checkDone;

        Holder(@NonNull View v) {
            super(v);
            imgTask = v.findViewById(R.id.imgTask);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvCity = v.findViewById(R.id.tvCity);
            tvDateTime = v.findViewById(R.id.tvDateTime);
            checkDone = v.findViewById(R.id.checkDone);

            v.setOnClickListener(view -> {
                if (listener != null) listener.onClick(getAdapterPosition());
            });
        }
    }
}
