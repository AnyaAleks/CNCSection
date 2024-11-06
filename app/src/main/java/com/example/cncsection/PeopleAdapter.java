package com.example.cncsection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    private List<People> peopleList;
    private OnItemClickListener mListener;

    public PeopleAdapter(List<People> people) {
        this.peopleList = people;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<People> filteredList) {
        this.peopleList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PeopleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.delegat_people, parent, false);
        return new ViewHolder(contactView, mListener);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fioView;
        ImageButton roleInformerInList;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            fioView = itemView.findViewById(R.id.input_name);
            roleInformerInList = itemView.findViewById(R.id.roleInformerInList);

            fioView.setOnClickListener(new View.OnClickListener() {
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
    public void onBindViewHolder(PeopleAdapter.ViewHolder holder, int position) {
        People person = peopleList.get(position);
        holder.fioView.setText(person.getfio());
        holder.roleInformerInList.setImageResource(getRoleIcon(person.getRole()));
    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }

    private int getRoleIcon(int key) {
        int icon;
        switch (key) {
            case 1: icon = R.drawable.baseline_person_add_24; break;
            case 2: icon = R.drawable.baseline_engineering_24; break;
            case 3: icon = R.drawable.baseline_manage_accounts_24; break;
            case 4: icon = R.drawable.baseline_how_to_reg_24; break;
            default: icon = R.drawable.baseline_groups_24;
        }
        return icon;
    }
}
