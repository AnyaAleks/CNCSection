package com.example.cncsection;

import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView applicationView;
        public TextView statusView;

        public ViewHolder(View itemView) {
            super(itemView);

            applicationView = (TextView) itemView.findViewById(R.id.input_number);
            statusView = (TextView) itemView.findViewById(R.id.input_status);
        }
    }
    private List<Status> statuses;

    public StatusAdapter(List<Status> st) {
        statuses = st;
    }

    @Override
    public StatusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.delegate_orderlist, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
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
