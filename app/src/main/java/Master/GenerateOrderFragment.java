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

import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.cncsection.TxtViewerActivity;
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
    String part_number;
    TextView idRequestTextView;
    EditText productionTimeInput;
    ImageButton stateInformerButton;
    EditText statesSpinner;
    Button openProgramButton;

    UserSettings userSettings = new UserSettings();

    LinearLayout mainLinearLayout;

    TextView bench_list, equipment_list, operator_list, tool_list;
    EditText input_estimated_production_time;

    private MasterAdapter adapter;
    ImageView calendar_button;
    ListView lvBenches;
    ListView lvEquipments;
    ListView lvOperators;
    ListView lvTools;

    ImageButton add_button_bench;
//    ImageButton update_button_bench;
    ImageButton add_button_equipment;
//    ImageButton update_button_equipment;
    ImageButton add_button_operator;
//    ImageButton update_button_operator;
    ImageButton add_button_tool;
//    ImageButton update_button_tool;

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

        TextView errorDay = view.findViewById(R.id.error_calendar_master);
        TextView errorTime = view.findViewById(R.id.error_time_master);
        TextView errorBench = view.findViewById(R.id.error_bench_master);
        TextView errorEquipment = view.findViewById(R.id.error_equipment_master);
        TextView errorOperator = view.findViewById(R.id.error_operator_master);
        TextView errorTool = view.findViewById(R.id.error_tool_master);

        lvBenches = view.findViewById(R.id.bench_list_view);
        lvEquipments = view.findViewById(R.id.equipment_list_view);
        lvOperators = view.findViewById(R.id.operator_list_view);
        lvTools = view.findViewById(R.id.tool_list_view);

        add_button_bench =  view.findViewById(R.id.add_button_bench);
//        update_button_bench =  view.findViewById(R.id.update_button_bench);

        add_button_equipment =  view.findViewById(R.id.add_button_equipment);
//        update_button_equipment =  view.findViewById(R.id.update_button_equipment);

        add_button_operator =  view.findViewById(R.id.add_button_operator);
//        update_button_operator =  view.findViewById(R.id.update_button_operator);

        add_button_tool =  view.findViewById(R.id.add_button_tool);
//        update_button_tool =  view.findViewById(R.id.update_button_tool);

        addOrderButton = view.findViewById(R.id.addOrderButton);
        productionTimeInput = view.findViewById(R.id.productionTimeInput);

        openProgramButton = view.findViewById(R.id.openProgramButton);

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
            int id=csro.getInt(csro.getColumnIndex("id_staff"));
            int role=csro.getInt(csro.getColumnIndex("id_access"));
            String name =csro.getString(csro.getColumnIndex("fio"));
            if(role==3)
                hashOperator.put(id, name);
        }



        Cursor csrt = dbMaster.getAll("Tool");
        while (csrt.moveToNext()) {
            hashTool.put(csrt.getInt(csrt.getColumnIndex("id_tool")), csrt.getString(csrt.getColumnIndex("title")));
        }



        //Выставление статуса
        stateInformerButton = view.findViewById(R.id.stateInformer);
        statesSpinner = view.findViewById(R.id.states_spinner);
        Cursor csrRequest = dbMaster.getAll("Request");
        while (csrRequest.moveToNext()) {
            if(csrRequest.getInt(csrRequest.getColumnIndex("id_order")) == idOrder){
                part_number = csrRequest.getString(csrRequest.getColumnIndex("part_number"));

                int id_status = csrRequest.getInt(csrRequest.getColumnIndex("id_status"));
                stateInformerButton.setBackgroundDrawable(getResources().getDrawable( getRoleColor(id_status)));
                stateInformerButton.setImageResource( getRoleIcon(id_status));

                Cursor csrStatus = dbMaster.getAll("Status");
                while(csrStatus.moveToNext()){
                    if(csrStatus.getInt(csrStatus.getColumnIndex("id_status")) == id_status){
                        statesSpinner.setText(csrStatus.getString(csrStatus.getColumnIndex("title")));
                    }
                }
            }
        }

        //Выставление чертежа
        Intent intentTxtViewer = new Intent(getActivity(), TxtViewerActivity.class);
        openProgramButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                //Открытие чертежа по part_number
                Cursor csrProgramm= dbMaster.getAll("Programm");
                boolean flagIsFind = false;
                while (csrProgramm.moveToNext()) {
                    if(csrProgramm.getString(csrProgramm.getColumnIndex("part_number")).equals(part_number)){
                        intentTxtViewer.putExtra("current_file_name", csrProgramm.getString(csrProgramm.getColumnIndex("title")));
                        startActivity(intentTxtViewer);
                        flagIsFind = true;
                    }
                }

                if(!flagIsFind){
                    Toast.makeText(getActivity(), "Чертеж не найден", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                calendar.set(year, month, day);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        input_estimated_production_time.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting_master;
            private int selectionStart_Master;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                selectionStart_Master = input_estimated_production_time.getSelectionStart();
            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isFormatting_master) {
                    return;
                }

                String clean = s.toString().replaceAll("\\D", ""); // Удаляем все нецифровые символы
                if (clean.length() > 8) {
                    clean = clean.substring(0, 8);
                }
                StringBuilder formatted = new StringBuilder();

                if (clean.length() > 0) {
                    formatted.append(clean.substring(0, Math.min(2, clean.length()))); // ДД
                    if (clean.length() >= 3) {
                        formatted.append(".").append(clean.substring(2, Math.min(4, clean.length()))); // ММ
                    }
                    if (clean.length() >= 5) {
                        formatted.append(".").append(clean.substring(4)); // ГГГГ
                    }
                }

                // Проверка на количество символов
                if (clean.length() >= 2) {
                    String day = clean.length() >= 2 ? clean.substring(0, 2) : clean;

                    if (Integer.parseInt(day) > 31 || Integer.parseInt(day) <1) {
                        Toast.makeText(getActivity(), "Такого дня нет", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (clean.length() >= 4) {
                    String day = clean.length() >= 2 ? clean.substring(0, 2) : clean;
                    String month = clean.length() >= 4 ? clean.substring(2, 4) : "";
                    String year = clean.length() > 4 ? clean.substring(4) : "";

                    if (Integer.parseInt(month) > 12 || Integer.parseInt(month) < 1) {
                        Toast.makeText(getActivity(), "Такого месяца нет", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Проверка корректности даты только если все части введены
                    if (!day.isEmpty() && !month.isEmpty() && year.length() >= 4) {
                        if (!isValidDate_Master(day, month, year)) {
                            Toast.makeText(getActivity(), "Некорректная дата", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!isDateNotInPast(day, month, year)) {
                            Toast.makeText(getActivity(), "Дата не может быть раньше текущего дня", Toast.LENGTH_SHORT).show();
                            input_estimated_production_time.setText("");
                            return;
                        }

                        int yearInt = Integer.parseInt(year);
                        if (yearInt > 2100) {
                            Toast.makeText(getActivity(), "Год не может быть больше 2100", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                isFormatting_master = true;
                String current = formatted.toString();
                input_estimated_production_time.setText(current);
                int newSelection = selectionStart_Master + (current.length() - s.length());
                if (selectionStart_Master > 2 && selectionStart_Master < 5) {
                    newSelection++;
                }
                input_estimated_production_time.setSelection(Math.max(0, Math.min(newSelection, current.length())));
                isFormatting_master = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            // Метод для проверки корректности даты
            private boolean isValidDate_Master(String day, String month, String year) {
                int dayInt_master = Integer.parseInt(day);
                int monthInt_master = Integer.parseInt(month);
                int yearInt_master = year.isEmpty() ? 0 : Integer.parseInt(year);

                int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

                // Проверка на високосный год
                if (monthInt_master == 2 && isLeapYear(yearInt_master)) {
                    daysInMonth[2] = 29;
                }
                return dayInt_master <= daysInMonth[monthInt_master];
            }

            // Метод для проверки високосного года
            private boolean isLeapYear(int year) {
                return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
            }

            // Метод для проверки, что дата не раньше текущего дня
            private boolean isDateNotInPast(String day, String month, String year) {
                Calendar currentDate = Calendar.getInstance();
                int currentYear = currentDate.get(Calendar.YEAR);
                int currentMonth = currentDate.get(Calendar.MONTH) + 1;
                int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

                int dayInt = Integer.parseInt(day);
                int monthInt = Integer.parseInt(month);
                int yearInt = year.isEmpty() ? 0 : Integer.parseInt(year);

                if (yearInt < currentYear || (yearInt == currentYear && monthInt < currentMonth) ||
                        (yearInt == currentYear && monthInt == currentMonth && dayInt < currentDay)) {
                    return false;
                }
                return true;
            }
        });

        add_button_bench.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                //AddBench(view);
                if (hashBench.size() > 0) {
                    String newBench = bench_spinner.getSelectedItem().toString();
                    int id = 42;

                    for (Entry<Integer, String> entry : hashBench.entrySet()) {
                        if (entry.getValue().equals(newBench)) {
                            id = entry.getKey();
                            break;
                        }
                    }

                    boolean flag = true;
                    for (int i = 0; i < benches.size(); i++) {
                        if(Objects.equals(benches.get(i).getName(), newBench)){
                            flag = false;
                        }
                    }

                    if (flag) {
                        errorBench.setText("");
                        benches.add(new MasterString(newBench, id));
                        adapter = new MasterAdapter(getActivity(), benches);
                        lvBenches.setAdapter(adapter);
                        ChangeHeight(lvBenches);
                    }
                }
            }
        });

        add_button_equipment.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                //AddBench(view);
                if (hashEquipment.size() > 0) {
                    String newBench = equipment_spinner.getSelectedItem().toString();
                    int id = 42;

                    for (Entry<Integer, String> entry : hashEquipment.entrySet()) {
                        if (entry.getValue().equals(newBench)) {
                            id = entry.getKey();
                            break;
                        }
                    }
                    boolean flag = true;
                    for (int i = 0; i < equipments.size(); i++) {
                        if(Objects.equals(equipments.get(i).getName(), newBench)){
                            flag = false;
                        }
                    }

                    if (flag) {
                        errorEquipment.setText("");
                        equipments.add(new MasterString(newBench, id));
                        adapter = new MasterAdapter(getActivity(), equipments);
                        lvEquipments.setAdapter(adapter);
                        ChangeHeight(lvEquipments);
                    }
                }
            }
        });

        add_button_operator.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                //AddBench(view);
                if (hashOperator.size() > 0) {
                    String newBench = operator_spinner.getSelectedItem().toString();
                    int id = 42;
                    for (Entry<Integer, String> entry : hashOperator.entrySet()) {
                        if (entry.getValue().equals(newBench)) {
                            id = entry.getKey();
                            break;
                        }
                    }
                    boolean flag = true;
                    for (int i = 0; i < operators.size(); i++) {
                        if(Objects.equals(operators.get(i).getName(), newBench)){
                            flag = false;
                        }
                    }

                    if (flag) {
                        errorOperator.setText("");
                        operators.add(new MasterString(newBench, id));
                        adapter = new MasterAdapter(getActivity(), operators);
                        lvOperators.setAdapter(adapter);
                        ChangeHeight(lvOperators);
                    }
                }
            }
        });

        add_button_tool.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                //AddBench(view);
                if (hashTool.size() > 0) {
                    String newBench=tool_spinner.getSelectedItem().toString();
                    int id=42;

                    for (Entry<Integer, String> entry : hashTool.entrySet()) {
                        if (entry.getValue().equals(newBench)) {
                            id=entry.getKey();
                            break;
                        }
                    }
                    boolean flag = true;
                    for (int i = 0; i < tools.size(); i++) {
                        if(Objects.equals(tools.get(i).getName(), newBench)){
                            flag = false;
                        }
                    }

                    if (flag) {
                        errorTool.setText("");
                        tools.add(new MasterString(newBench, id));
                        adapter = new MasterAdapter(getActivity(), tools);
                        lvTools.setAdapter(adapter);
                        ChangeHeight(lvTools);
                    }
                }
            }
        });

        input_estimated_production_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorDay.setText("");}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        productionTimeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorTime.setText("");}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        addOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String estimatedProductionTime = String.valueOf(productionTimeInput.getText());
                String calendar_date_master = String.valueOf(input_estimated_production_time.getText());
                int benchSize = benches.size();
                int equipmentSize = equipments.size();
                int operatorSize = operators.size();
                int toolSize = tools.size();

                boolean itIsError = false;
                if (calendar_date_master.isEmpty()) {
                    errorDay.setText("Необходимо заполнить поле выбора даты");
                    itIsError = true;
                }
                if (estimatedProductionTime.isEmpty()) {
                    errorTime.setText("Необходимо заполнить поле время производства");
                    itIsError = true;
                }
                if (benchSize<1) {
                    errorBench.setText("Необходимо указать требуемые станки");
                    itIsError = true;
                }
                if (toolSize<1) {
                    errorTool.setText("Необходимо указать требуемые инструменты");
                    itIsError = true;
                }
                if (equipmentSize<1) {
                    errorEquipment.setText("Необходимо указать требуемые оснастки");
                    itIsError = true;
                }
                if (operatorSize<1) {
                    errorOperator.setText("Необходимо указать требуемых операторов");
                    itIsError = true;
                }


                if (itIsError) {
                    return;
                }

                try {
                    // Создали заявку
                    dbMaster.addToOrder(userSettings.getIdUser(), estimatedProductionTime, idOrder, calendar_date_master);

                    productionTimeInput.setText("");
                    input_estimated_production_time.setText("");

                    // Забрали id у новой заявки
                    Cursor csrOrder = dbMaster.getAllOrder();
                    int id_current_order = idOrder;
//                    while (csrOrder.moveToNext()) {
//                        if (idOrder == csrOrder.getInt(csrOrder.getColumnIndex("id_request"))) {
//                            id_current_order = csrOrder.getInt(csrOrder.getColumnIndex("id_order"));
//                        }
//                    }

                    // Machine
                    if (benches != null) {
                        for (int i = 0; i < benches.size(); i++) {
                            dbMaster.addToOrder_and_Machine(id_current_order, benches.get(i).getId());
                        }
                    }

                    // Osnaska
                    if (equipments != null) {
                        for (int i = 0; i < equipments.size(); i++) {
                            dbMaster.addToOrder_and_Osnaska(id_current_order, equipments.get(i).getId());
                        }
                    }

                    // Operator
                    if (operators != null) {
                        for (int i = 0; i < operators.size(); i++) {
                            dbMaster.addToOrder_and_Operator(id_current_order, operators.get(i).getId());
                        }
                    }

                    // Tool
                    if (tools != null) {
                        for (int i = 0; i < tools.size(); i++) {
                            dbMaster.addToOrder_and_Tool(id_current_order, tools.get(i).getId());
                        }
                    }

                    // Изменение статуса заявки
                    dbMaster.updateValueById("Request", "id_order", idOrder, "id_status", 2);

                    Toast.makeText(getActivity(), "Данные успешно добавлены", Toast.LENGTH_SHORT).show();
                    ListOrderFragment listOrderFragment = new ListOrderFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_master, listOrderFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } catch (Exception e) {
                    Log.e("OrderError", "Ошибка при оформлении заявки: " + e.getMessage());
                    Toast.makeText(getActivity(), "Произошла ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
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


    private void ChangeHeight(ListView listView)
    {
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private int getRoleColor(int key)
    {
        int color;
        switch (key){
            case 1: color = R.drawable.state_color_gray; break;
            case 2: color = R.drawable.state_color_green; break;
            case 3: color = R.drawable.state_color_yellow; break;
            case 4: color = R.drawable.state_color_red; break;
            case 5: color = R.drawable.state_color_red; break;
            case 6: color = R.drawable.state_color_red; break;
            case 7: color = R.drawable.state_color_blue; break;
            default: color = R.drawable.state_color_red;
        }
        return color;
    }

    private int getRoleIcon(int key)
    {
        int icon;
        switch (key){
            case 1: icon = R.drawable.null_icon; break;
            case 2: icon = R.drawable.null_icon; break;
            case 3: icon = R.drawable.null_icon; break;
            case 4: icon = R.drawable.null_icon; break;
            case 5: icon = R.drawable.osnaska; break;
            case 6: icon = R.drawable.architecture; break;
            case 7: icon = R.drawable.null_icon; break;
            default: icon = R.drawable.null_icon;
        }
        return icon;
    }

}