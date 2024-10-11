package com.example.cncsection;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GenerateOrderActivity extends AppCompatActivity {
    TextView bench_list;
    private String[] benches = new String[1];
    Spinner bench_spinner;
    private String[] bench_types = {"Токарный", "Фрезерный", "Расточный"};

    Spinner equipment_spinner;
    private String[] equipment_types = {"7100-0009П", "7100-0002П", "7100-0035П","7100-0005П",};

    Spinner operator_spinner;
    private String[] operator_types = {"Бэтмен Б.У.", "Алексеева А.С.", "Гуляева А.Д.","Леонов А.Я."};

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
        //bench_spinner = findViewById(R.id.bench_spinner);
        bench_spinner = (Spinner) findViewById(R.id.bench_spinner);
        FillSpinners(bench_types, bench_spinner);
        equipment_spinner = findViewById(R.id.equipment_spinner);
        FillSpinners(equipment_types, equipment_spinner);
        operator_spinner = findViewById(R.id.operator_spinner);
        FillSpinners(operator_types, operator_spinner);

        bench_list = findViewById(R.id.bench_list);


        benches[0] = bench_list.getText().toString();
        /*
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
        */
    }

    public void DeleteBench(View view)
    {
        String newBench=bench_spinner.getSelectedItem().toString();
        int delId = 0;
        //if(benches.length>1)
        for (int i=1;i<benches.length;i++)
        {
            if(newBench==benches[i])
                delId = i;
        }
        if(delId >0)
        {
            String[] newBenches = new String[benches.length-1];
            newBenches[0]=benches[0];

            boolean flag=false;
            for (int i=1;i<benches.length-1;i++)
            {
                if(i==delId)
                    flag = true;

                if(!flag)
                {
                    newBenches[i]=benches[i];
                }
                else
                {
                    newBenches[i]=benches[i+1];
                }
            }
            benches=newBenches;
            OutPutBenchList();
        }
    }
    public void AddBench(View view)
    {
        String newBench=bench_spinner.getSelectedItem().toString();
        boolean flag = false;
        //if(benches.length>1)
        for (String bench : benches)
        {
            if(newBench==bench)
                flag = true;
        }
        if(!flag)
        {
            String[] newBenches = new String[benches.length+1];
            for (int i=0;i<benches.length;i++)
            {
                newBenches[i]=benches[i];
            }
            newBenches[newBenches.length-1]=newBench;
            benches=newBenches;
            OutPutBenchList();
        }
    }
    private void OutPutBenchList()
    {
        bench_list.setText("");
        //bench_list.clearComposingText();
        bench_list.append(benches[0]+" ");
        for (int i=1;i<benches.length;i++)
        {
            bench_list.append(benches[i]+"; ");
        }
    }
    private  void FillSpinners(String[] arrayTypes, Spinner spinner)
    {
        ArrayAdapter<String> TypesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,arrayTypes);
        TypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(TypesAdapter);
    }
}