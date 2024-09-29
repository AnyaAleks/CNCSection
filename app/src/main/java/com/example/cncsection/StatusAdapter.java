package com.example.cncsection;

import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private final List<Status> statuses;

    public StatusAdapter(Context context, List<Status> statuses) {
        this.inflater = LayoutInflater.from(context);
        this.statuses = statuses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.delegate_orderlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StatusAdapter.ViewHolder holder, int position) {
        Status status = statuses.get(position);
        holder.applicationView.setText(status.getApplication());
        holder.statusView.setText(status.getStatus());
    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView applicationView, statusView;
        public ViewHolder(View view) {
            super(view);
            applicationView = view.findViewById(R.id.input_number);
            statusView = view.findViewById(R.id.input_status);
        }
    }
}
