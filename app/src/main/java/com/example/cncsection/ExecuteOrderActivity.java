package com.example.cncsection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
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
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;


public class ExecuteOrderActivity extends AppCompatActivity {

    //private String[] statuses = {"Ожидает выполнения","В процессе выполнения","Выполнен", "Отложен", "Возникли проблемы"};
    HashMap<Integer, String> hashStatuses=new HashMap<Integer, String>();
    Spinner order_status_spinner;
    TextView orderNumber;
    DBOperator dbOperator;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_execute_order);

        order_status_spinner = (Spinner) findViewById(R.id.order_status_spinner);

        dbOperator = new DBOperator(this);

        Cursor csr = dbOperator.getAll("Status");
        while (csr.moveToNext()) {
            hashStatuses.put(csr.getInt(csr.getColumnIndex("id_status")), csr.getString(csr.getColumnIndex("title")));
        }

        FillSpinners();

        orderNumber = (TextView) findViewById(R.id.order_number);
        orderNumber.append("4315");

    }

    private  void FillSpinners()
    {
        ArrayList<String> list = new ArrayList<>(hashStatuses.values());
        list.add(0,"");
        ArrayAdapter<String> TypesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        TypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        order_status_spinner.setAdapter(TypesAdapter);
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