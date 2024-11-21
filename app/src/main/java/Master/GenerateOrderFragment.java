package Master;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cncsection.ListOrderFragment;
import com.example.cncsection.MasterAdapter;
import com.example.cncsection.OrderInformation;
import com.example.cncsection.R;
import com.example.cncsection.UserSettings;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.Map.Entry;

import Enter.EntryActivity;

public class GenerateOrderFragment extends Fragment {

    int idOrder;
    TextView idRequestTextView;
    EditText productionTimeInput;

    UserSettings userSettings = new UserSettings();

    LinearLayout mainLinearLayout;

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

    Button addOrderButton;

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

        // Установка слушателя для обработки нажатий на элементы меню
        toolbar_entry.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход к ListOrderFragment
                ListOrderFragment listOrderFragment = new ListOrderFragment();

//                // Создаем Bundle и передаем idOrder
//                Bundle bundle = new Bundle();
//                bundle.putInt("idOrder", idOrder); // Передаем idOrder, если нужно
//                listOrderFragment.setArguments(bundle);

                // Заменяем текущий фрагмент на ListOrderFragment
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_master, listOrderFragment); // Убедитесь, что ID контейнера правильный
                transaction.addToBackStack(null); // Добавляем в стек возврата
                transaction.commit();
            }
        });

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

        addOrderButton = view.findViewById(R.id.addOrderButton);
        productionTimeInput = view.findViewById(R.id.productionTimeInput);

        ArrayList<MasterString> list = new ArrayList<>();
        adapter = new MasterAdapter(getActivity(),list);

        lvBenches.setAdapter(adapter);
        lvEquipments.setAdapter(adapter);
        lvOperators.setAdapter(adapter);
        lvTools.setAdapter(adapter);
        //rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));

        //Доставание из настроек
        loadJsonData();

        //Только у менеджера будет нижнее меню => убрать отступ
        mainLinearLayout = view.findViewById(R.id.mainLinearLayout);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mainLinearLayout
                .getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);


        idRequestTextView = view.findViewById(R.id.idRequestTextView);
        //Получение id
        Bundle bundle = this.getArguments();
        idOrder = bundle.getInt("idOrder");
        idRequestTextView.setText("№ " + idOrder);

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
                int id=42;

                for (Entry<Integer, String> entry : hashBench.entrySet()) {
                    if (entry.getValue().equals(newBench)) {
                        id=entry.getKey();
                        break;
                    }
                }

                boolean flag = false;
                if(!flag)
                {
                    benches.add(new MasterString(newBench,id));
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
                int id=42;

                for (Entry<Integer, String> entry : hashBench.entrySet()) {
                    if (entry.getValue().equals(newBench)) {
                        id=entry.getKey();
                        break;
                    }
                }
                if(!flag)
                {
                    equipments.add(new MasterString(newBench,id));
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
                int id=42;

                for (Entry<Integer, String> entry : hashBench.entrySet()) {
                    if (entry.getValue().equals(newBench)) {
                        id=entry.getKey();
                        break;
                    }
                }
                if(!flag)
                {
                    operators.add(new MasterString(newBench,id));
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
                int id=42;

                for (Entry<Integer, String> entry : hashBench.entrySet()) {
                    if (entry.getValue().equals(newBench)) {
                        id=entry.getKey();
                        break;
                    }
                }
                if(!flag)
                {
                    tools.add(new MasterString(newBench,id));
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

        addOrderButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //ПРОВЕРКУ НА НЕПУСТОЕ ВРЕМЯ
                //Сделать календарь с датой
                //создали заявку
                dbMaster.addToOrder(userSettings.getIdUser(),
                        String.valueOf(productionTimeInput.getText()),
                        idOrder,
                        "18.11.2024");

                //забрали id у новой заявки
                Cursor csrOrder = dbMaster.getAll("Order");
                int id_current_order = 0;
                while (csrOrder.moveToNext()) {
                    if(idOrder == csrOrder.getInt(csrOrder.getColumnIndex("id_request"))){
                        id_current_order = csrOrder.getInt(csrOrder.getColumnIndex("id_order"));
                    }
                }

                //Machine
                for(int i=0; i<benches.size(); i++){
                    dbMaster.addToOrder_and_Machine(id_current_order, benches.get(i).getId());
                }

                //Osnaska
                for(int i=0; i<equipments.size(); i++){
                    dbMaster.addToOrder_and_Osnaska(id_current_order, equipments.get(i).getId());
                }

                //Operator
                for(int i=0; i<operators.size(); i++){
                    dbMaster.addToOrder_and_Operator(id_current_order, operators.get(i).getId());
                }

                //Tool addToOrder_and_Tool
                for(int i=0; i<tools.size(); i++){
                    dbMaster.addToOrder_and_Tool(id_current_order, tools.get(i).getId());
                }


                Toast.makeText(getActivity(), "Данные успешно добавлены", Toast.LENGTH_SHORT).show();
                ListOrderFragment listOrderFragment = new ListOrderFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_master, listOrderFragment); // Убедитесь, что ID контейнера правильный
                transaction.addToBackStack(null); // Добавляем в стек возврата
                transaction.commit();
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

    //Доставание из настроек
    public void loadJsonData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("user settings", null);
        userSettings = gson.fromJson(json, UserSettings.class);

        if (userSettings == null) {
            Toast.makeText(getActivity(), "Empty Settings!", Toast.LENGTH_SHORT).show();
            //userSettings = new UserSettings(userId);
        }
    }
}