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
    Spinner statesSpinner;
    HashMap<Integer, String> hashSates=new HashMap<Integer, String>();
    DBStaff dbStaff;

    TextView idOrderTextView;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_information);

//        Bundle arguments = getIntent().getExtras();
//        Status status;
//        status = (Status) arguments.getSerializable(Status.class.getSimpleName());
        Intent intent = getIntent();
        String str = intent.getStringExtra("id_current_order");

        idOrderTextView = findViewById(R.id.idOrderTextView);
        idOrderTextView.setText(str);


        //ВЫСТАВЛЕНИЕ СТАТУСА
        stateInformerButton = findViewById(R.id.stateInformer);
        statesSpinner = (Spinner) findViewById(R.id.states_spinner);
        //Заполнение списка для состояний индикатора
        dbStaff = new DBStaff(this);
        Cursor csr = dbStaff.getAll("Status");
        while (csr.moveToNext()) {
            hashSates.put(csr.getInt(csr.getColumnIndex("id_status")), csr.getString(csr.getColumnIndex("title")));
        }
//        Log.d("DB_STAFF",hashSates.toString());
        fillStatesSpinner();
        statesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                stateInformerButton.setBackgroundDrawable(getResources().getDrawable( getRoleColor(position+1)));
                stateInformerButton.setImageResource( getRoleIcon(position+1));
                        //getRoleColor(position+1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private  void fillStatesSpinner()
    {
        ArrayList<String> list = new ArrayList<>(hashSates.values());
        ArrayAdapter<String> TypesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        TypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statesSpinner.setAdapter(TypesAdapter);
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
        //"@drawable/roundcorner"
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