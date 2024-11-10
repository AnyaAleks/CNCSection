package com.example.cncsection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;

import Staff.DBStaff;

public class PeopleInformation extends AppCompatActivity {

    TextView idStaffTextView;
    TextView fioTextView;
    DBStaff dbStaff;
    ImageView staffImageView;
    TextView dateOfBirthTextView;
    String id_current_person;
    Button deleteStaff;

    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_people_information);

        dbStaff = new DBStaff(this);

        deleteStaff = findViewById(R.id.deleteStaff);
        fioTextView = findViewById(R.id.fioTextView);
        dateOfBirthTextView = findViewById(R.id.dateOfBirthTextView);
        idStaffTextView = findViewById(R.id.idStaffTextView);
        staffImageView = findViewById(R.id.staffImageView);

        Intent intent = getIntent();
        id_current_person = intent.getStringExtra("id_current_person");
        idStaffTextView.setText("№ " + id_current_person);

        Cursor csrStaff = dbStaff.getAll("Staff");
        while (csrStaff.moveToNext()) {
            if(csrStaff.getInt(csrStaff.getColumnIndex("id_staff")) == Integer.parseInt(id_current_person)){
                staffImageView.setImageResource(getRoleIcon(csrStaff.getInt(csrStaff.getColumnIndex("id_access")) ));
                fioTextView.setText(csrStaff.getString(csrStaff.getColumnIndex("fio")));
                dateOfBirthTextView.setText(csrStaff.getString(csrStaff.getColumnIndex("date_of_birth")));
            }
        }

        deleteStaff.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                loadConfirmationDialog(); //Удаление сотрудника
            }
        });
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

    private void loadConfirmationDialog(){
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container_peopleInformation, ConfirmationDialogFragment.newInstance(id_current_person, "Staff"));
        ft.commit();
    }
}