package com.example.cncsection;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class ExecuteOrderActivity extends AppCompatActivity {

    private String[] statuses = {"Ожидает выполнения","В процессе выполнения","Выполнен", "Отложен", "Возникли проблемы"};
    Spinner order_status_spinner;
    TextView orderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_execute_order);

        order_status_spinner = (Spinner) findViewById(R.id.order_status_spinner);
        FillSpinners(statuses, order_status_spinner);

        orderNumber = (TextView) findViewById(R.id.order_number);
        orderNumber.append("4315");

    }

    private  void FillSpinners(String[] arrayTypes, Spinner spinner)
    {
        ArrayAdapter<String> TypesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,arrayTypes);
        TypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(TypesAdapter);
    }

    /*
    public void OpenDrawing(View view)
    {
        Intent intent = new Intent(this, PDFViewerActivity.class);
        //Intent intent = new Intent(this, TxtViewerActivity.class);
    }
    public void OpenTechProcess(View view)
    {
        //Intent intent = new Intent(this, PDFViewerActivity.class);
        Intent intent = new Intent(this, TxtViewerActivity.class);
    }

     */
}