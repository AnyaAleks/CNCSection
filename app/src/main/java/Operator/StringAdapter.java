package Operator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cncsection.R;

import java.util.List;

public class StringAdapter extends ArrayAdapter<String> {

    public StringAdapter(Context context, List<String> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_operator_order, parent, false);
        }

        String currentItem = getItem(position);

        TextView textView = convertView.findViewById(R.id.bench_list);
        textView.setText(currentItem);

        return convertView;
    }
}