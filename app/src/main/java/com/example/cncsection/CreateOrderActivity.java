package com.example.cncsection;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CreateOrderActivity extends AppCompatActivity {
    TextView header, item_number, production_time,commentary,application_review,search_button;
    EditText input_production_time, item_number_entry, commentary_entry, search_input;
    Button button;
    ArrayList<Status> statuses ;//= new ArrayList<Status>();

    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_order); // начальная инициализация списка

        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.list); //создание адаптера
        statuses = Status.createStatusesList(20);
        StatusAdapter adapter = new StatusAdapter(statuses); // устанавливаем для списка адаптер
        rvContacts.setAdapter(adapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));

//        EdgeToEdge.enable(this);
        header = findViewById(R.id.header);
        item_number = findViewById(R.id.item_number);
        production_time = findViewById(R.id.production_time);
        commentary = findViewById(R.id.commentary);
        application_review = findViewById(R.id.application_review);
        search_button = findViewById(R.id.search_button);
        input_production_time = findViewById(R.id.input_production_time);
        item_number_entry = findViewById(R.id.item_number_entry);
        commentary_entry = findViewById(R.id.commentary_entry);
        search_input = findViewById(R.id.search_input);
        button = findViewById(R.id.button);

        dbManager = new DBManager(this);

        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                String number = item_number_entry.getText().toString();
                String date = input_production_time.getText().toString();
                String comments = commentary_entry.getText().toString();

                dbManager.addToRequest(number, 1, comments, "2024-10-13");

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
    }
}