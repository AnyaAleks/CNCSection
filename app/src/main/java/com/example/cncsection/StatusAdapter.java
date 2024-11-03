package com.example.cncsection;

import static android.app.PendingIntent.getActivity;
import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    private List<Status> statuses;
    private StatusAdapter.OnItemClickListener mListener;

    public StatusAdapter(List<Status> st) {
        statuses = st;
    }

    // method for filtering our recyclerview items.
    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<Status> filteredList) {
        this.statuses = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StatusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.delegate_orderlist, parent, false);
        return new ViewHolder(contactView, mListener);
    }

    public void setOnItemClickListener(StatusAdapter.OnItemClickListener onItemClickListener) {
        mListener=onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView applicationView;
        //public TextView statusView;
        ImageButton stateInformerButton;

        public ViewHolder(View itemView, StatusAdapter.OnItemClickListener listener) {
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
    public void onBindViewHolder(StatusAdapter.ViewHolder holder, int position) {
        Status status = statuses.get(position);
        TextView textView = holder.applicationView;
        textView.setText(status.getApplication());
        //TextView textView2 = holder.statusView;
        //textView2.setText(status.getStatus());
        ImageButton stateInformerButton = holder.stateInformerButton;
        stateInformerButton.setBackgroundDrawable(holder.itemView.getContext().getResources().getDrawable( getRoleColor(status.getStatus())));
        stateInformerButton.setImageResource(getRoleIcon(status.getStatus()));
    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }

    private int getRoleColor(int key)
    {
        //"@drawable/roundcorner"
        int color;
        switch (key){
            case 1: color = R.drawable.state_color_gray; break;
            case 2: color = R.drawable.state_color_green; break;
            case 3: color = R.drawable.state_color_yellow; break;
            case 4: color = R.drawable.state_color_red; break;
            case 5: color = R.drawable.state_color_red; break;
            case 6: color = R.drawable.state_color_red; break;
            case 7: color = R.drawable.state_color_blue; break;
            default: color = R.drawable.state_color_red;
        }
        return color;
    }

    private int getRoleIcon(int key)
    {
        int icon;
        switch (key){
            case 1: icon = R.drawable.null_icon; break;
            case 2: icon = R.drawable.null_icon; break;
            case 3: icon = R.drawable.null_icon; break;
            case 4: icon = R.drawable.null_icon; break;
            case 5: icon = R.drawable.osnaska; break;
            case 6: icon = R.drawable.architecture; break;
            case 7: icon = R.drawable.null_icon; break;
            default: icon = R.drawable.null_icon;
        }
        return icon;
    }
}
