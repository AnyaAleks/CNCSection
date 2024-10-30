package com.example.cncsection;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class GenerateOrderFragment extends Fragment {

    TextView bench_list, equipment_list, operator_list, input_estimated_production_time;

    ImageView calendar_button;


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

    public GenerateOrderFragment() {
        // Required empty public constructor
    }
    public static GenerateOrderFragment newInstance(String param1, String param2) {
        return new GenerateOrderFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generate_order, container, false);

        dbMaster = new DBMaster(getActivity());



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
        bench_spinner = (Spinner) view.findViewById(R.id.bench_spinner);
        FillSpinners(hashBench, bench_spinner);
        equipment_spinner = view.findViewById(R.id.equipment_spinner);
        FillSpinners(hashEquipment, equipment_spinner);
        operator_spinner = view.findViewById(R.id.operator_spinner);
        FillSpinners(hashOperator, operator_spinner);

        bench_list = view.findViewById(R.id.bench_list);
        equipment_list = view.findViewById(R.id.equipment_list);
        operator_list = view.findViewById(R.id.operator_list);

        benches[0] = bench_list.getText().toString();
        equipments[0] = equipment_list.getText().toString();
        operators[0] = operator_list.getText().toString();

        calendar_button = view.findViewById(R.id.calendar);
        input_estimated_production_time = view.findViewById(R.id.input_estimated_production_time);

        calendar_button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        (view, year1, monthOfYear, dayOfMonth) ->
                                input_estimated_production_time.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year1),
                        year, month, day);
                datePickerDialog.show();
            }
        });


        return view;
    }

    public static GenerateOrderFragment newInstance(){
        return new GenerateOrderFragment();
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
        ArrayAdapter<String> TypesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);
        TypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(TypesAdapter);
    }

}