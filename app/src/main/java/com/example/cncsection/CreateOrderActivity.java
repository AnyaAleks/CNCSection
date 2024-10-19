package com.example.cncsection;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;
import android.widget.DatePicker;

public class CreateOrderActivity extends AppCompatActivity {
    TextView header, item_number, production_time,commentary,application_review,search_button, calendar_date;
    EditText item_number_entry, commentary_entry;
    Button button_create;
    ImageView calendar_button;

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
        item_number_entry = findViewById(R.id.item_number_entry);
        commentary_entry = findViewById(R.id.commentary_entry);
        //search_input = findViewById(R.id.search_input);
        button_create = findViewById(R.id.button);
        calendar_button = findViewById(R.id.calendar);
        calendar_date = findViewById(R.id.calendar_date);

        commentary_entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                //инициализация новой высоты
                int newHeight = commentary_entry.getLineCount() *
                        commentary_entry.getLineHeight();

                //перевод в dp
                int minHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80,
                        getResources().getDisplayMetrics());
                int maxHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150,
                        getResources().getDisplayMetrics());

                if (newHeight < minHeight) {newHeight = minHeight;}
                if (newHeight > maxHeight) {newHeight = maxHeight;}

                // уствновка новой высоты для EditText
                ViewGroup.LayoutParams params = commentary_entry.getLayoutParams();
                params.height = newHeight;
                commentary_entry.setLayoutParams(params);
            }


        });


        calendar_button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateOrderActivity.this,
                        (view, year1, monthOfYear, dayOfMonth) ->
                                calendar_date.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year1),
                        year, month, day);
                datePickerDialog.show();
            }
        });

        dbManager = new DBManager(this);

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
    }
}