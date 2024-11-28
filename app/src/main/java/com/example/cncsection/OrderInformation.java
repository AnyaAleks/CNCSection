package com.example.cncsection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import Enter.EntryActivity;
import Master.MasterString;
import Staff.DBStaff;

public class OrderInformation extends AppCompatActivity {

    ImageButton stateInformerButton;
    EditText statesSpinner;
    HashMap<Integer, String> hashSates=new HashMap<Integer, String>();
    DBStaff dbStaff;
    UserSettings userSettings = new UserSettings();

    TextView idOrderTextView;
    EditText detailNumberEditText;
    EditText commentaryEntryEditText;
    EditText fioEditText;
    EditText fioRequestEditText;
    EditText dateEditText;
    EditText productionTimeEditText;
    EditText dateGenerateEditText;
    Button deleteOrderButton;
    Button openProgramButton;

    String id_current_order;
    String part_number;


//    private MasterAdapter adapter;
//    ListView lvBenches;
//    ListView lvEquipments;
//    ListView lvOperators;
//    ListView lvTools;
//
//    private ArrayList<MasterString> benches = new ArrayList<>();
//    private ArrayList<MasterString> equipments = new ArrayList<>();
//    private ArrayList<MasterString> operators = new ArrayList<>();
//    private ArrayList<MasterString> tools = new ArrayList<>();


    List<String> benches = new ArrayList<String>();
    ListView lvBenches;
    List<String> equipments = new ArrayList<String>();
    ListView lvEquipments;
    List<String> tools = new ArrayList<String>();
    ListView lvTools;
    List<String> operators = new ArrayList<String>();
    ListView lvOperators;



    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_information);

        // Настройка Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_info_order);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24);
        }


        dbStaff = new DBStaff(this);
        loadJsonData();

        deleteOrderButton = findViewById(R.id.deleteOrder);
        //Log.d("GGG", "Staff" + " "+userSettings.getIdUser() + " " + userSettings.getRoleUser());
        if(userSettings.getRoleUser() != 1){
            deleteOrderButton.setVisibility(View.GONE);
        }

        openProgramButton = findViewById(R.id.openProgramButton);
        stateInformerButton = findViewById(R.id.stateInformer);
        statesSpinner = findViewById(R.id.states_spinner);

        Intent intent = getIntent();
        id_current_order = intent.getStringExtra("id_current_order");

        idOrderTextView = findViewById(R.id.idOrderTextView);
        idOrderTextView.setText("№ " + id_current_order);

        detailNumberEditText = findViewById(R.id.detailNumberEditText);
        commentaryEntryEditText = findViewById(R.id.commentaryEntryEditText);
        fioEditText = findViewById(R.id.fioEditText);
        fioRequestEditText = findViewById(R.id.fioRequestEditText);
        dateEditText = findViewById(R.id.dateEditText);
        productionTimeEditText = findViewById(R.id.productionTimeEditText);
        dateGenerateEditText = findViewById(R.id.dateGenerateEditText);

        lvBenches = findViewById(R.id.bench_list_view);
        lvEquipments = findViewById(R.id.equipment_list_view);
        lvOperators = findViewById(R.id.operator_list_view);
        lvTools = findViewById(R.id.tool_list_view);
////////////////////////////////////////////////////////////////////////////////////////////////////

        //Заполнение списка станков
//        benches.add("Станок№1");
//        benches.add("Станок№2");
//        benches.add("Станок№3");
        ArrayList<Integer> listCurrentMachine = new ArrayList<>();
        Cursor csrOrder_and_Machine = dbStaff.getAll("Order_and_Machine");
        while (csrOrder_and_Machine.moveToNext()) {
            Log.i("HHH", String.valueOf(csrOrder_and_Machine.getInt(csrOrder_and_Machine.getColumnIndex("id_order"))));
            if(Integer.parseInt(id_current_order) == csrOrder_and_Machine.getInt(csrOrder_and_Machine.getColumnIndex("id_order"))){
                listCurrentMachine.add(csrOrder_and_Machine.getInt(csrOrder_and_Machine.getColumnIndex("id_machine")));
            }
        }
        Cursor csrMachine = dbStaff.getAll("Machine");
        while (csrMachine.moveToNext()) {
            if(listCurrentMachine.contains(csrMachine.getInt(csrMachine.getColumnIndex("id_machine")))){
                benches.add(csrMachine.getString(csrMachine.getColumnIndex("type")));
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, benches);
        lvBenches.setAdapter(adapter);
        ChangeHeight(lvBenches,adapter);

        //Заполнение списка оснасток
//        equipments.add("Оснастка№1");
//        equipments.add("Оснастка№2");
//        equipments.add("Оснастка№3");
//        equipments.add("Оснастка№4");
        ArrayList<Integer> listCurrentOsnaska = new ArrayList<>();
        Cursor csrOrder_and_Osnaska = dbStaff.getAll("Order_and_Osnaska");
        while (csrOrder_and_Osnaska.moveToNext()) {
            if(Integer.parseInt(id_current_order) == csrOrder_and_Osnaska.getInt(csrOrder_and_Osnaska.getColumnIndex("id_order"))){
                listCurrentOsnaska.add(csrOrder_and_Osnaska.getInt(csrOrder_and_Osnaska.getColumnIndex("id_osnaska")));
            }
        }
        Cursor csrOsnaska = dbStaff.getAll("Osnaska");
        while (csrOsnaska.moveToNext()) {
            if(listCurrentOsnaska.contains(csrOsnaska.getInt(csrOsnaska.getColumnIndex("id_osnaska")))){
                equipments.add(csrOsnaska.getString(csrOsnaska.getColumnIndex("title")));
            }
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, equipments);
        lvEquipments.setAdapter(adapter);
        ChangeHeight(lvEquipments,adapter);

//        tools.add("Инструмент№1");
//        tools.add("Инструмент№2");
//        tools.add("Инструмент№3");
        ArrayList<Integer> listCurrentTool = new ArrayList<>();
        Cursor csrOrder_and_Tool = dbStaff.getAll("Order_and_Tool");
        while (csrOrder_and_Tool.moveToNext()) {
            if(Integer.parseInt(id_current_order) == csrOrder_and_Tool.getInt(csrOrder_and_Tool.getColumnIndex("id_order"))){
                listCurrentTool.add(csrOrder_and_Tool.getInt(csrOrder_and_Tool.getColumnIndex("id_tool")));
            }
        }
        Cursor csrTool = dbStaff.getAll("Tool");
        while (csrTool.moveToNext()) {
            if(listCurrentTool.contains(csrTool.getInt(csrTool.getColumnIndex("id_tool")))){
                tools.add(csrTool.getString(csrTool.getColumnIndex("title")));
            }
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tools);
        lvTools.setAdapter(adapter);
        ChangeHeight(lvTools,adapter);

//        operators.add("Оператор№1");
//        operators.add("Оператор№2");
//        operators.add("Оператор№3");
//        operators.add("Оператор№4");
//        operators.add("Оператор№5");
        ArrayList<Integer> listCurrentOperator = new ArrayList<>();
        Cursor csrOrder_and_Operator = dbStaff.getAll("Order_and_Operator");
        while (csrOrder_and_Operator.moveToNext()) {
            if(Integer.parseInt(id_current_order) == csrOrder_and_Operator.getInt(csrOrder_and_Operator.getColumnIndex("id_order"))){
                listCurrentOperator.add(csrOrder_and_Operator.getInt(csrOrder_and_Operator.getColumnIndex("id_staff")));
            }
        }
        Cursor csrOperator = dbStaff.getAll("Staff");
        while (csrOperator.moveToNext()) {
            if(listCurrentOperator.contains(csrOperator.getInt(csrOperator.getColumnIndex("id_staff")))){
                operators.add(csrOperator.getString(csrOperator.getColumnIndex("fio")));
            }
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, operators);
        lvOperators.setAdapter(adapter);
        ChangeHeight(lvOperators,adapter);
////////////////////////////////////////////////////////////////////////////////////////////////////
        Cursor csrRequest = dbStaff.getAll("Request");
        int id_manager = -1;
        while (csrRequest.moveToNext()) {
            if(csrRequest.getInt(csrRequest.getColumnIndex("id_order")) == Integer.parseInt(id_current_order)){
                detailNumberEditText.setText(csrRequest.getString(csrRequest.getColumnIndex("part_number")));
                part_number = csrRequest.getString(csrRequest.getColumnIndex("part_number"));

                int id_status = csrRequest.getInt(csrRequest.getColumnIndex("id_status"));
                stateInformerButton.setBackgroundDrawable(getResources().getDrawable( getRoleColor(id_status)));
                stateInformerButton.setImageResource( getRoleIcon(id_status));

                Cursor csrStatus = dbStaff.getAll("Status");
                while(csrStatus.moveToNext()){
                    if(csrStatus.getInt(csrStatus.getColumnIndex("id_status")) == id_status){
                        statesSpinner.setText(csrStatus.getString(csrStatus.getColumnIndex("title")));
                    }
                }

                id_manager = csrRequest.getInt(csrRequest.getColumnIndex("id_staff_manager"));
                Cursor csrStaff = dbStaff.getAll("Staff");
                while (csrStaff.moveToNext()) {
                    if(csrStaff.getInt(csrStaff.getColumnIndex("id_staff")) == id_manager){
                        fioRequestEditText.setText(csrStaff.getString(csrStaff.getColumnIndex("fio")));
                    }
                }

                commentaryEntryEditText.setText(csrRequest.getString(csrRequest.getColumnIndex("comment")));
                commentaryEntryEditText.setEnabled(false);

                dateEditText.setText(csrRequest.getString(csrRequest.getColumnIndex("date")));
            }
        }

        Cursor csrOrder = dbStaff.getAllOrder();
        int id_master = -1;
        while (csrOrder.moveToNext()) {
//            Log.d("ORDERRR", String.valueOf(csrOrder.getInt(csrOrder.getColumnIndex("id_request"))));
            if(csrOrder.getInt(csrOrder.getColumnIndex("id_request")) == Integer.parseInt(id_current_order)){
                dateGenerateEditText.setText(csrOrder.getString(csrOrder.getColumnIndex("date_start")));
                productionTimeEditText.setText(csrOrder.getString(csrOrder.getColumnIndex("production_time")));
                id_master = csrOrder.getInt(csrOrder.getColumnIndex("id_staff_master"));
            }
        }

        if(id_master == -1){
            fioEditText.setText("");
        } else{
            Cursor csrStaff = dbStaff.getAll("Staff");
            while (csrStaff.moveToNext()) {
                if(csrStaff.getInt(csrStaff.getColumnIndex("id_staff")) == id_master){
                    fioEditText.setText(csrStaff.getString(csrStaff.getColumnIndex("fio")));
                }
            }
        }


        deleteOrderButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                loadConfirmationDialog(); //Удаление заявки (в 2х таблицах)
            }
        });

        Intent intentTxtViewer = new Intent(this, TxtViewerActivity.class);
        openProgramButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                //Открытие чертежа по part_number
                Cursor csrProgramm= dbStaff.getAll("Programm");
                boolean flagIsFind = false;
                while (csrProgramm.moveToNext()) {
                    if(csrProgramm.getString(csrProgramm.getColumnIndex("part_number")).equals(part_number)){
                        intentTxtViewer.putExtra("current_file_name", csrProgramm.getString(csrProgramm.getColumnIndex("title")));
                        startActivity(intentTxtViewer);
                        flagIsFind = true;
                    }
                }

                if(!flagIsFind){
                    Toast.makeText(getApplicationContext(), "Чертеж не найден", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // Возврат на предыдущий экран
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadConfirmationDialog(){
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container_orderInformation, ConfirmationDialogFragment.newInstance(id_current_order, "Order"));
        ft.commit();
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

    //Доставание из настроек
    public void loadJsonData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("user settings", null);
        userSettings = gson.fromJson(json, UserSettings.class);

        if (userSettings == null) {
            Toast.makeText(this, "Empty Settings!", Toast.LENGTH_SHORT).show();
            //userSettings = new UserSettings(userId);
        }
    }

    private void ChangeHeight(ListView listView, ArrayAdapter adapter )
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
}