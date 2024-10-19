package com.example.cncsection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    private List<Status> statuses;

    public StatusAdapter(List<Status> st) {
        statuses = st;
    }

    // method for filtering our recyclerview items.
    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<Status> filterList) {
        statuses = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StatusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.delegate_orderlist, parent, false);
        return new ViewHolder(contactView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView applicationView;
        public TextView statusView;

        public ViewHolder(View itemView) {
            super(itemView);
            applicationView = (TextView) itemView.findViewById(R.id.input_number);
            statusView = (TextView) itemView.findViewById(R.id.input_status);
        }
    }

    @Override
    public void onBindViewHolder(StatusAdapter.ViewHolder holder, int position) {
        Status status = statuses.get(position);
        TextView textView = holder.applicationView;
        textView.setText(status.getApplication());
        TextView textView2 = holder.statusView;
        textView2.setText(status.getStatus());
    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }
}
