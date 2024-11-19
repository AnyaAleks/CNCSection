package com.example.cncsection;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

import Master.MasterString;

public class MasterAdapter extends ArrayAdapter<MasterString> {

    public MasterAdapter(@NonNull Context context, ArrayList<MasterString> arrayList) {

        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.delegate_master, parent, false);
        }

        MasterString currentNumberPosition = getItem(position);


        TextView itemText = currentItemView.findViewById(R.id.input_name_item);

        String s=currentNumberPosition.getName()+currentNumberPosition.getId();
        itemText.setText(s);
        //itemText.setText(currentNumberPosition.getName());

        ImageButton deleteImage = currentItemView.findViewById(R.id.delete_button);
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MasterString currentNumberPosition = getItem(position);
                remove(currentNumberPosition);
                notifyDataSetChanged();
            }
        });

        // then return the recyclable view
        return currentItemView;
    }
}
