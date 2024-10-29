package com.example.cncsection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderInformation extends AppCompatActivity {

    ImageButton stateInformerButton;
    EditText statesSpinner;
    HashMap<Integer, String> hashSates=new HashMap<Integer, String>();
    DBStaff dbStaff;

    TextView idOrderTextView;
    EditText detailNumberEditText;
    EditText commentaryEntryEditText;
    EditText dateEditText;
    EditText productionTimeEditText;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_information);

        dbStaff = new DBStaff(this);

        stateInformerButton = findViewById(R.id.stateInformer);
        statesSpinner = findViewById(R.id.states_spinner);

        Intent intent = getIntent();
        String id_current_order = intent.getStringExtra("id_current_order");

        idOrderTextView = findViewById(R.id.idOrderTextView);
        idOrderTextView.setText(id_current_order);

        detailNumberEditText = findViewById(R.id.detailNumberEditText);
        commentaryEntryEditText = findViewById(R.id.commentaryEntryEditText);
        dateEditText = findViewById(R.id.dateEditText);
        productionTimeEditText = findViewById(R.id.productionTimeEditText);

        Cursor csrRequest = dbStaff.getAll("Request");
        while (csrRequest.moveToNext()) {
            if(csrRequest.getInt(csrRequest.getColumnIndex("id_order")) == Integer.parseInt(id_current_order)){
                detailNumberEditText.setText(csrRequest.getString(csrRequest.getColumnIndex("part_number")));

                int id_status = csrRequest.getInt(csrRequest.getColumnIndex("id_status"));
                stateInformerButton.setBackgroundDrawable(getResources().getDrawable( getRoleColor(id_status)));
                stateInformerButton.setImageResource( getRoleIcon(id_status));

                Cursor csrStatus = dbStaff.getAll("Status");
                while(csrStatus.moveToNext()){
                    if(csrStatus.getInt(csrStatus.getColumnIndex("id_status")) == id_status){
                        statesSpinner.setText(csrStatus.getString(csrStatus.getColumnIndex("title")));
                    }
                }

                commentaryEntryEditText.setText(csrRequest.getString(csrRequest.getColumnIndex("comment")));

                dateEditText.setText(csrRequest.getString(csrRequest.getColumnIndex("date")));
            }
        }

        Cursor csrOrder = dbStaff.getAllOrder();
        while (csrOrder.moveToNext()) {
//            Log.d("ORDERRR", String.valueOf(csrOrder.getInt(csrOrder.getColumnIndex("id_request"))));
            if(csrOrder.getInt(csrOrder.getColumnIndex("id_request")) == Integer.parseInt(id_current_order)){
                productionTimeEditText.setText(csrOrder.getString(csrOrder.getColumnIndex("production_time")));
            }
        }









    }

    private int getRoleColor(int key)
    {
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