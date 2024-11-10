package com.example.cncsection;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MasterAdapter extends RecyclerView.Adapter<MasterAdapter.ViewHolder> {

    public List<String> statuses;
    private MasterAdapter.OnItemClickListener mListener;

    public MasterAdapter(List<String> st) {
        statuses = st;
    }

    // method for filtering our recyclerview items.
    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<String> filteredList) {
        this.statuses = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MasterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.fragment_generate_order, parent, false);
        return new ViewHolder(contactView, mListener);
    }

    public void setOnItemClickListener(MasterAdapter.OnItemClickListener onItemClickListener) {
        mListener=onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView applicationView;
        //public TextView statusView;
        ImageButton stateInformerButton;

        public ViewHolder(View itemView, MasterAdapter.OnItemClickListener listener) {
            super(itemView);
            applicationView = (TextView) itemView.findViewById(R.id.input_number);
            //statusView = (TextView) itemView.findViewById(R.id.input_status);
            stateInformerButton = (ImageButton) itemView.findViewById(R.id.stateInformerInList);

            applicationView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(MasterAdapter.ViewHolder holder, int position) {
        String status = statuses.get(position);
        TextView textView = holder.applicationView;
        textView.setText(status);
        //TextView textView2 = holder.statusView;
        //textView2.setText(status.getStatus());
        ImageButton stateInformerButton = holder.stateInformerButton;
    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }

    public void add(String s) {
    }
}
