package Master;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cncsection.MasterAdapter;
import com.example.cncsection.OrderInformation;
import com.example.cncsection.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import Enter.EntryActivity;

public class GenerateOrderFragment extends Fragment {

    TextView bench_list, equipment_list, operator_list, tool_list, input_estimated_production_time;

    private MasterAdapter adapter;
    ImageView calendar_button;
    ListView lvBenches;
    ListView lvEquipments;
    ListView lvOperators;
    ListView lvTools;

    ImageButton add_button_bench;
    ImageButton update_button_bench;
    ImageButton add_button_equipment;
    ImageButton update_button_equipment;
    ImageButton add_button_operator;
    ImageButton update_button_operator;
    ImageButton add_button_tool;
    ImageButton update_button_tool;

    private ArrayList<MasterString> benches = new ArrayList<>();
    private ArrayList<MasterString> equipments = new ArrayList<>();
    private ArrayList<MasterString> operators = new ArrayList<>();
    private ArrayList<MasterString> tools = new ArrayList<>();


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
        setHasOptionsMenu(true);

    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.back_menu, menu); // Замените на ваш файл меню
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Переход к EntryActivity
                Intent intent = new Intent(getActivity(), EntryActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generate_order, container, false);

        // Настройка Toolbar
        Toolbar toolbar_entry = view.findViewById(R.id.toolbar_entry_master);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar_entry);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24);
        }

        dbMaster = new DBMaster(getActivity());

        lvBenches = view.findViewById(R.id.bench_list_view);
        lvEquipments = view.findViewById(R.id.equipment_list_view);
        lvOperators = view.findViewById(R.id.operator_list_view);
        lvTools = view.findViewById(R.id.tool_list_view);

        add_button_bench =  view.findViewById(R.id.add_button_bench);
        update_button_bench =  view.findViewById(R.id.update_button_bench);

        add_button_equipment =  view.findViewById(R.id.add_button_equipment);
        update_button_equipment =  view.findViewById(R.id.update_button_equipment);

        add_button_operator =  view.findViewById(R.id.add_button_operator);
        update_button_operator =  view.findViewById(R.id.update_button_operator);

        add_button_tool =  view.findViewById(R.id.add_button_tool);
        update_button_tool =  view.findViewById(R.id.update_button_tool);

        ArrayList<MasterString> list = new ArrayList<>();
        adapter = new MasterAdapter(getActivity(),list);

        lvBenches.setAdapter(adapter);
        lvEquipments.setAdapter(adapter);
        lvOperators.setAdapter(adapter);
        lvTools.setAdapter(adapter);
        //rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));


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
                //AddBench(view);
                String newBench=bench_spinner.getSelectedItem().toString();
                boolean flag = false;
                if(!flag)
                {
                    benches.add(new MasterString(newBench));
                    adapter = new MasterAdapter(getActivity(),benches);
                    lvBenches.setAdapter(adapter);
                }
            }
        });
        update_button_bench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AddBench(view);

                int i=0;
                while (i<benches.size())
                {
                    if(Objects.equals(benches.get(i).getName(), "empty"))
                    {
                        benches.remove(i);
                    }
                    else
                        i++;
                }

                adapter = new MasterAdapter(getActivity(),benches);
                lvBenches.setAdapter(adapter);
            }
        });
        add_button_equipment.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                //AddBench(view);
                String newBench=equipment_spinner.getSelectedItem().toString();
                boolean flag = false;
                if(!flag)
                {
                    equipments.add(new MasterString(newBench));
                    adapter = new MasterAdapter(getActivity(),equipments);
                    lvEquipments.setAdapter(adapter);
                }
            }
        });
        update_button_equipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AddBench(view);

                int i=0;
                while (i<equipments.size())
                {
                    if(Objects.equals(equipments.get(i).getName(), "empty"))
                    {
                        equipments.remove(i);
                    }
                    else
                        i++;
                }

                adapter = new MasterAdapter(getActivity(),equipments);
                lvEquipments.setAdapter(adapter);
            }
        });
        add_button_operator.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                //AddBench(view);
                String newBench=operator_spinner.getSelectedItem().toString();
                boolean flag = false;
                if(!flag)
                {
                    operators.add(new MasterString(newBench));
                    adapter = new MasterAdapter(getActivity(),operators);
                    lvOperators.setAdapter(adapter);
                }
            }
        });
        update_button_operator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AddBench(view);

                int i=0;
                while (i<operators.size())
                {
                    if(Objects.equals(operators.get(i).getName(), "empty"))
                    {
                        operators.remove(i);
                    }
                    else
                        i++;
                }

                adapter = new MasterAdapter(getActivity(),operators);
                lvOperators.setAdapter(adapter);
            }
        });
        add_button_tool.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                //AddBench(view);
                String newBench=tool_spinner.getSelectedItem().toString();
                boolean flag = false;
                if(!flag)
                {
                    tools.add(new MasterString(newBench));
                    adapter = new MasterAdapter(getActivity(),tools);
                    lvTools.setAdapter(adapter);
                }
            }
        });
        update_button_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AddBench(view);

                int i=0;
                while (i<tools.size())
                {
                    if(Objects.equals(tools.get(i).getName(), "empty"))
                    {
                        tools.remove(i);
                    }
                    else
                        i++;
                }

                adapter = new MasterAdapter(getActivity(),tools);
                lvTools.setAdapter(adapter);
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

}