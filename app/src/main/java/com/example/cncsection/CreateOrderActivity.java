package com.example.cncsection;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateOrderActivity extends AppCompatActivity {
    TextView textView, detal, sroky,com,zayvki,poisk;
    EditText editTextDate, zayvka, write,poiskstatus;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_order);
        textView = findViewById(R.id.textView);
        detal = findViewById(R.id.detal);
        sroky = findViewById(R.id.sroky);
        com = findViewById(R.id.com);
        zayvki = findViewById(R.id.zayvki);
        poisk = findViewById(R.id.poisk);
        editTextDate = findViewById(R.id.editTextDate);
        zayvka = findViewById(R.id.zayvka);
        write = findViewById(R.id.write);
        poiskstatus = findViewById(R.id.poiskstatus);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomer = zayvka.getText().toString();
                String date = editTextDate.getText().toString();
                String coment = com.getText().toString();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}