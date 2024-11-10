package Master;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cncsection.MasterAdapter;
import com.example.cncsection.OrderInformation;
import com.example.cncsection.R;
import com.example.cncsection.StatusAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class GenerateOrderFragment extends Fragment {

    TextView bench_list, equipment_list, operator_list, tool_list, input_estimated_production_time;

    private MasterAdapter adapter;
    ImageView calendar_button;
    RecyclerView rvContacts;


    ImageButton add_button_bench;

    private String[] benches = new String[1];
    private String[] equipments = new String[1];
    private String[] operators = new String[1];
    private String[] tools = new String[1];

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

    Spinner tool_spinner;
    HashMap<Integer, String> hashTool=new HashMap<Integer, String>();

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

        rvContacts = view.findViewById(R.id.bench_list_view);

        add_button_bench =  view.findViewById(R.id.add_button_bench);

        List<String> list = new ArrayList<>();
        adapter = new MasterAdapter(list);

        rvContacts.setAdapter(adapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));


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

        Cursor csrt = dbMaster.getAll("Tool");
        while (csrt.moveToNext()) {
            hashOperator.put(csrt.getInt(csrt.getColumnIndex("id_tool")), csrt.getString(csrt.getColumnIndex("title")));
        }

        //hashEquipment.put(csr.getInt(csr.getColumnIndex("id_osnaska")), csr.getString(csr.getColumnIndex("title")));
        //hashOperator.put(csr.getInt(csr.getColumnIndex("id_osnaska")), csr.getString(csr.getColumnIndex("title")));

        //EdgeToEdge.enable(this);
//

        //master_header = findViewById(R.id.master_header);
        //bench_spinner = findViewById(R.id.bench_spinner);
        bench_spinner = (Spinner) view.findViewById(R.id.bench_spinner);
        FillSpinners(hashBench, bench_spinner);
        equipment_spinner = view.findViewById(R.id.equipment_spinner);
        FillSpinners(hashEquipment, equipment_spinner);
        operator_spinner = view.findViewById(R.id.operator_spinner);
        FillSpinners(hashOperator, operator_spinner);
        tool_spinner = view.findViewById(R.id.tool_spinner);
        FillSpinners(hashTool, tool_spinner);

        bench_list = view.findViewById(R.id.bench_list);
        equipment_list = view.findViewById(R.id.equipment_list);
        operator_list = view.findViewById(R.id.operator_list);
        tool_list = view.findViewById(R.id.tool_list);

//        benches[0] = "";
//        equipments[0] = "";
//        operators[0] = "";
////        tools[0] = "";ч
//        benches[0] = bench_list.getText().toString();
//        equipments[0] = equipment_list.getText().toString();
//        operators[0] = operator_list.getText().toString();
//        tools[0] = tool_list.getText().toString();

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

        add_button_bench.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                AddBench(view);
            }
        });

        return view;
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

    public static GenerateOrderFragment newInstance(){
        return new GenerateOrderFragment();
    }

    public void AddBench(View view)
    {
        String newBench=bench_spinner.getSelectedItem().toString();
        boolean flag = false;
        if(!flag)
        {
            List<String> list = new ArrayList<>();
            list.add(newBench);
            //adapter.statuses.add(newBench);
            adapter.add(newBench);
            rvContacts.setAdapter(adapter);
            rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
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

}