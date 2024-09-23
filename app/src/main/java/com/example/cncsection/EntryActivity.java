package com.example.cncsection;

import android.annotation.SuppressLint;
import android.content.Intent;
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


public class EntryActivity extends AppCompatActivity {

    TextView entry;
    EditText num_1, num_2;
    Button add_button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_entry);

        entry = findViewById(R.id.entry);
        num_1 = findViewById(R.id.num_1);
        num_2 = findViewById(R.id.num_2);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = num_1.getText().toString();
                String password = num_2.getText().toString();
                //далее надо сделать выбор определённого интерфейса (роли)
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goNext(View V){
        //Сделать проверку на пароль и понять какое окно грузить
        // (всем работникам при устройстве на работу должны выдавать пароль) по типу клиента
        //У каждой роли в пароле можно сделать определённые первые цифры или буквы и делать проверку по ним
        //пока стоит заглушка
        Intent intent = new Intent(this, CreateOrderActivity.class);
        startActivity(intent);
    }
}