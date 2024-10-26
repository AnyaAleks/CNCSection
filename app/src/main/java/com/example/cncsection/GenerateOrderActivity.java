package com.example.cncsection;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.ArrayList;

public class GenerateOrderActivity extends AppCompatActivity {
    TextView bench_list;
    TextView equipment_list;
    TextView operator_list;


    private String[] benches = new String[1];
    private String[] equipments = new String[1];
    private String[] operators = new String[1];

    HashMap<Integer, String> hashRoles=new HashMap<Integer, String>();

    Spinner bench_spinner;
    //private String[] bench_types = {"","Токарный", "Фрезерный", "Расточный"};
    HashMap<Integer, String> hashBench=new HashMap<Integer, String>();

    Spinner equipment_spinner;
    //private String[] equipment_types = {"","7100-0009П", "7100-0002П", "7100-0035П","7100-0005П",};
    HashMap<Integer, String> hashEquipment=new HashMap<Integer, String>();

    Spinner operator_spinner;
    //private String[] operator_types = {"","Бэтмен Б.У.", "Алексеева А.С.", "Гуляева А.Д.","Леонов А.Я."};
    HashMap<Integer, String> hashOperator=new HashMap<Integer, String>();

    DBMaster dbMaster;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generate_order);

        ScrollView scrollView = findViewById(R.id.scroll_view);

        if (scrollView != null) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        }

        dbMaster = new DBMaster(this);

        Cursor csrb = dbMaster.getAll("Machine");
        while (csrb.moveToNext()) {
            hashBench.put(csrb.getInt(csrb.getColumnIndex("id_machine")), csrb.getString(csrb.getColumnIndex("type")));
        }

        Cursor csre = dbMaster.getAll("Osnaska");
        while (csre.moveToNext()) {
            hashEquipment.put(csre.getInt(csre.getColumnIndex("id_osnaska")), csre.getString(csre.getColumnIndex("title")));
        }

        Cursor csro = dbMaster.getAll("Staff");
        while (csro.moveToNext()) {
            hashOperator.put(csro.getInt(csro.getColumnIndex("id_staff")), csro.getString(csro.getColumnIndex("fio")));
        }

        //hashEquipment.put(csr.getInt(csr.getColumnIndex("id_osnaska")), csr.getString(csr.getColumnIndex("title")));
        //hashOperator.put(csr.getInt(csr.getColumnIndex("id_osnaska")), csr.getString(csr.getColumnIndex("title")));

        //EdgeToEdge.enable(this);


        //master_header = findViewById(R.id.master_header);
        //bench_spinner = findViewById(R.id.bench_spinner);
        bench_spinner = (Spinner) findViewById(R.id.bench_spinner);
        FillSpinners(hashBench, bench_spinner);
        equipment_spinner = findViewById(R.id.equipment_spinner);
        FillSpinners(hashEquipment, equipment_spinner);
        operator_spinner = findViewById(R.id.operator_spinner);
        FillSpinners(hashOperator, operator_spinner);

        bench_list = findViewById(R.id.bench_list);
        equipment_list = findViewById(R.id.equipment_list);
        operator_list = findViewById(R.id.operator_list);

        benches[0] = bench_list.getText().toString();
        equipments[0] = equipment_list.getText().toString();
        operators[0] = operator_list.getText().toString();
    }

    public void AddBench(View view)
    {
        String newBench=bench_spinner.getSelectedItem().toString();
        boolean flag = false;
        //if(benches.length>1)
        if(newBench=="")
            flag = true;
        for (String element : benches)
        {
            if(newBench==element)
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
            OutPutList(benches,bench_list);
        }
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
            OutPutList(benches,bench_list);
        }
    }


    public void AddEquipment(View view)
    {
        String newEquipment=equipment_spinner.getSelectedItem().toString();
        boolean flag = false;
        //if(benches.length>1)
        for (String element : equipments)
        {
            if(newEquipment==element)
                flag = true;
        }
        if(!flag)
        {
            String[] newEquipmentes = new String[equipments.length+1];
            for (int i=0;i<equipments.length;i++)
            {
                newEquipmentes[i]=equipments[i];
            }
            newEquipmentes[newEquipmentes.length-1]=newEquipment;
            equipments=newEquipmentes;
            OutPutList(equipments,equipment_list);
        }
    }

    public void DeleteEquipment(View view)
    {
        String newEquipment=bench_spinner.getSelectedItem().toString();
        int delId = 0;
        for (int i=1;i<equipments.length;i++)
        {
            if(newEquipment==equipments[i])
                delId = i;
        }
        if(delId >0)
        {
            String[] newBenches = new String[equipments.length-1];
            newBenches[0]=equipments[0];

            boolean flag=false;
            for (int i=1;i<equipments.length-1;i++)
            {
                if(i==delId)
                    flag = true;

                if(!flag)
                {
                    newBenches[i]=equipments[i];
                }
                else
                {
                    newBenches[i]=equipments[i+1];
                }
            }
            equipments=newBenches;
            OutPutList(equipments,equipment_list);
        }
    }

    public void AddOperator(View view)
    {
        String newOperator=operator_spinner.getSelectedItem().toString();
        boolean flag = false;
        //if(benches.length>1)
        for (String element : operators)
        {
            if(newOperator==element)
                flag = true;
        }
        if(!flag)
        {
            String[] newOperators = new String[operators.length+1];
            for (int i=0;i<operators.length;i++)
            {
                newOperators[i]=operators[i];
            }
            newOperators[newOperators.length-1]=newOperator;
            operators=newOperators;
            OutPutList(operators,operator_list);
        }
    }

    public void DeleteOperator(View view)
    {
        String newOperator=operator_spinner.getSelectedItem().toString();
        int delId = 0;
        for (int i=1;i<operators.length;i++)
        {
            if(newOperator==operators[i])
                delId = i;
        }
        if(delId >0)
        {
            String[] newBenches = new String[operators.length-1];
            newBenches[0]=operators[0];

            boolean flag=false;
            for (int i=1;i<operators.length-1;i++)
            {
                if(i==delId)
                    flag = true;

                if(!flag)
                {
                    newBenches[i]=operators[i];
                }
                else
                {
                    newBenches[i]=operators[i+1];
                }
            }
            operators=newBenches;
            OutPutList(operators,operator_list);
        }
    }
    private void OutPutList(String[] list, TextView listTextView)
    {
        listTextView.setText("");
        //bench_list.clearComposingText();
        listTextView.append(list[0]+" ");
        for (int i=1;i<list.length;i++)
        {
            listTextView.append(list[i]+"; ");
        }
    }
    private  void FillSpinners(HashMap<Integer,String> hashMap, Spinner spinner)
    {
        ArrayList<String> list = new ArrayList<>(hashMap.values());
        list.add(0,"");
        ArrayAdapter<String> TypesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        TypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(TypesAdapter);
    }
}
