package com.example.cncsection;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateOrderFragment extends Fragment {

    TextView item_number, production_time,commentary, calendar_date;
    EditText item_number_entry, commentary_entry;
    Button button_create;
    ImageView calendar_button;



    DBManager dbManager;

    public CreateOrderFragment() {
        // Required empty public constructor
    }
    public static CreateOrderFragment newInstance(String param1, String param2) {
        CreateOrderFragment fragment = new CreateOrderFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_order, container, false);

        item_number = view.findViewById(R.id.item_number);
        production_time = view.findViewById(R.id.production_time);
        commentary = view.findViewById(R.id.commentary);
        item_number_entry = view.findViewById(R.id.item_number_entry);
        commentary_entry = view.findViewById(R.id.commentary_entry);
        button_create = view.findViewById(R.id.button);
        calendar_button = view.findViewById(R.id.calendar);
        calendar_date = view.findViewById(R.id.calendar_date);


        commentary_entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                int newHeight = commentary_entry.getLineCount() *
                        commentary_entry.getLineHeight();

                //перевод в dp
                int minHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
                        getResources().getDisplayMetrics());
                int maxHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250,
                        getResources().getDisplayMetrics());

                if (newHeight < minHeight) {newHeight = minHeight;}
                if (newHeight > maxHeight) {newHeight = maxHeight;}


                ViewGroup.LayoutParams params = commentary_entry.getLayoutParams();
                params.height = newHeight;
                commentary_entry.setLayoutParams(params);
                commentary_entry.requestLayout();
            }


        });


        calendar_button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        (view, year1, monthOfYear, dayOfMonth) ->
                                calendar_date.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year1),
                        year, month, day);
                datePickerDialog.show();
            }
        });

        dbManager = new DBManager(getActivity());

        button_create.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                String number = item_number_entry.getText().toString();
                //String calendar = calendar_button.getText().toString();
                String comments = commentary_entry.getText().toString();

                dbManager.addToRequest(number, 1, comments, calendar_date.getText().toString());

                Cursor csr = dbManager.getAll("Request");
                while(csr.moveToNext()) {
                    Log.d("DB_Manager",
                            "ID = " + csr.getInt(csr.getColumnIndex("id_order"))
                                    + " PART_NUMBER = " + csr.getInt(csr.getColumnIndex("part_number"))
                                    + " ID_STATUS = " + csr.getInt(csr.getColumnIndex("id_status"))
                                    + " COMMENT = " + csr.getString(csr.getColumnIndex("comment"))
                                    + " DATE = " + csr.getString(csr.getColumnIndex("date"))
                    );
                }
            }
        });

        return view;
    }

    public static CreateOrderFragment newInstance(){
        return new CreateOrderFragment();
    }
}