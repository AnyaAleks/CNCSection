package com.example.cncsection;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GenerateOrderActivity extends AppCompatActivity {
    TextView master_header;
    Spinner bench_spinner;
    private String[] bench_types = {"Токарный", "Фрезерный", "Расточный"};

    Spinner equipment_spinner;
    private String[] equipment_types = {"7100-0009П", "7100-0002П", "7100-0035П","7100-0005П",};

    Spinner operator_spinner;
    private String[] operator_types = {"Алексеева А.С.", "Гуляева А.Д.","Леонов А.Я.", "Бэтмен Д.С."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_generate_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //master_header = findViewById(R.id.master_header);
        bench_spinner = findViewById(R.id.bench_spinner);

        ArrayAdapter<String> benchTypesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,bench_types);
        benchTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerBenchType = (Spinner) findViewById(R.id.bench_spinner);
        spinnerBenchType.setAdapter(benchTypesAdapter);


        equipment_spinner = findViewById(R.id.equipment_spinner);

        ArrayAdapter<String> equipmentTypesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,equipment_types);
        equipmentTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerEquipmentType = (Spinner) findViewById(R.id.equipment_spinner);
        spinnerEquipmentType.setAdapter(equipmentTypesAdapter);


        operator_spinner = findViewById(R.id.operator_spinner);

        ArrayAdapter<String> operatorTypesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,operator_types);
        equipmentTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerOperatorType = (Spinner) findViewById(R.id.operator_spinner);
        spinnerOperatorType.setAdapter(operatorTypesAdapter);
    }
}