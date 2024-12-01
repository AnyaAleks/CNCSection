package Operator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cncsection.ListOrderFragment;
import com.example.cncsection.OrderInformation;
import com.example.cncsection.R;
import com.example.cncsection.Status;
import com.example.cncsection.StatusAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import Enter.EntryActivity;
import Staff.DBStaff;

public class OperatorOrderFragment extends Fragment {

    ImageButton stateInformerButton;
    Spinner statesSpinner;
    HashMap<Integer, String> hashSates=new HashMap<Integer, String>();
    DBStaff dbStaff;
    TextView idOrderTextView;
    Button saveButton;

    List<String> benches = new ArrayList<String>();
    ListView lvBenches;
    List<String> equipments = new ArrayList<String>();
    ListView lvEquipments;
    List<String> tools = new ArrayList<String>();
    ListView lvTools;

    int idOrder;
    int selectedNewRole;

    public OperatorOrderFragment() {
        // Required empty public constructor
    }
    public static OperatorOrderFragment newInstance(String param1, String param2) {
        OperatorOrderFragment fragment = new OperatorOrderFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operator_order, container, false);

        int backStackEntryCount = requireActivity().getSupportFragmentManager().getBackStackEntryCount();
        Log.d("FragmentBackStack", "Back stack entry count: " + backStackEntryCount);

        // Настройка Toolbar
        Toolbar toolbar_entry = view.findViewById(R.id.toolbar_operator);
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

                // Создаем Bundle и передаем idOrder
                Bundle bundle = new Bundle();
                bundle.putInt("idOrder", idOrder); // Передаем idOrder, если нужно
                listOrderFragment.setArguments(bundle);

                // Заменяем текущий фрагмент на ListOrderFragment
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_operator, listOrderFragment); // Убедитесь, что ID контейнера правильный
                transaction.addToBackStack(null); // Добавляем в стек возврата
                transaction.commit();
            }
        });



        dbStaff = new DBStaff(getActivity());

        stateInformerButton = view.findViewById(R.id.stateInformer);
        statesSpinner = (Spinner) view.findViewById(R.id.states_spinner);
        idOrderTextView = view.findViewById(R.id.idOrderTextView);
        saveButton = view.findViewById(R.id.saveButton);

        lvBenches=view.findViewById((R.id.bench_listView));
        lvEquipments=view.findViewById((R.id.equipment_listview));
        lvTools=view.findViewById((R.id.tools_listview));


        //Получение id
        Bundle bundle = this.getArguments();
        idOrder = bundle.getInt("idOrder");
        idOrderTextView.setText("№ " + idOrder);



        //Заполнение списка для состояний индикатора
        Cursor csr = dbStaff.getAll("Status");
        while (csr.moveToNext()) {
//            if(csr.getInt(csr.getColumnIndex("id_status")) == 1 || csr.getInt(csr.getColumnIndex("id_status")) == 2){
//
//            } else{
                hashSates.put(csr.getInt(csr.getColumnIndex("id_status")), csr.getString(csr.getColumnIndex("title")));
           // }
        }
//        hashSates.remove(0);
//        hashSates.remove(1);
//        hashSates.remove(2);
//        Log.d("DB_STAFF",hashSates.toString());

        //Заполнение списка станков
//        benches.add("Станок№1");
//        benches.add("Станок№2");
//        benches.add("Станок№3");
        ArrayList<Integer> listCurrentMachine = new ArrayList<>();
        Cursor csrOrder_and_Machine = dbStaff.getAll("Order_and_Machine");
        while (csrOrder_and_Machine.moveToNext()) {
            if(idOrder == csrOrder_and_Machine.getInt(csrOrder_and_Machine.getColumnIndex("id_order"))){
                listCurrentMachine.add(csrOrder_and_Machine.getInt(csrOrder_and_Machine.getColumnIndex("id_machine")));
            }
        }
        Cursor csrMachine = dbStaff.getAll("Machine");
        while (csrMachine.moveToNext()) {
            if(listCurrentMachine.contains(csrMachine.getInt(csrMachine.getColumnIndex("id_machine")))){
                benches.add(csrMachine.getString(csrMachine.getColumnIndex("type")));
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, benches);
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
            if(idOrder == csrOrder_and_Osnaska.getInt(csrOrder_and_Osnaska.getColumnIndex("id_order"))){
                listCurrentOsnaska.add(csrOrder_and_Osnaska.getInt(csrOrder_and_Osnaska.getColumnIndex("id_osnaska")));
            }
        }
        Cursor csrOsnaska = dbStaff.getAll("Osnaska");
        while (csrOsnaska.moveToNext()) {
            if(listCurrentOsnaska.contains(csrOsnaska.getInt(csrOsnaska.getColumnIndex("id_osnaska")))){
                equipments.add(csrOsnaska.getString(csrOsnaska.getColumnIndex("title")));
            }
        }
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, equipments);
        lvEquipments.setAdapter(adapter);
        ChangeHeight(lvEquipments,adapter);

//        tools.add("Инструмент№1");
//        tools.add("Инструмент№2");
//        tools.add("Инструмент№3");
        ArrayList<Integer> listCurrentTool = new ArrayList<>();
        Cursor csrOrder_and_Tool = dbStaff.getAll("Order_and_Tool");
        while (csrOrder_and_Tool.moveToNext()) {
            if(idOrder == csrOrder_and_Tool.getInt(csrOrder_and_Tool.getColumnIndex("id_order"))){
                listCurrentTool.add(csrOrder_and_Tool.getInt(csrOrder_and_Tool.getColumnIndex("id_tool")));
            }
        }
        Cursor csrTool = dbStaff.getAll("Tool");
        while (csrTool.moveToNext()) {
            if(listCurrentTool.contains(csrTool.getInt(csrTool.getColumnIndex("id_tool")))){
                tools.add(csrTool.getString(csrTool.getColumnIndex("title")));
            }
        }
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tools);
        lvTools.setAdapter(adapter);
        ChangeHeight(lvTools,adapter);

        statesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                stateInformerButton.setBackgroundDrawable(getResources().getDrawable( getRoleColor(position+3)));
                stateInformerButton.setImageResource( getRoleIcon(position+3));
                selectedNewRole = position+3;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        fillStatesSpinner();
        //Установка значений при входе в выпадающий список
        Cursor csrRequest = dbStaff.getAll("Request");
        while (csrRequest.moveToNext()) {
            if(csrRequest.getInt(csrRequest.getColumnIndex("id_order")) == idOrder){

                int _id_status = csrRequest.getInt(csrRequest.getColumnIndex("id_status"));
                statesSpinner.setSelection(_id_status-3);
            }
        }

        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //String table_name, String update_idField, String update_id,
                // String update_field, String update_value
                dbStaff.updateValueById("Request", "id_order"
                        , idOrder,"id_status", String.valueOf(selectedNewRole));
                Toast.makeText(getActivity(), "Данные обновлены", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    public static OperatorOrderFragment newInstance(){
        return new OperatorOrderFragment();
    }

    private  void fillStatesSpinner()
    {
        ArrayList<String> list = new ArrayList<>(hashSates.values());
        list.remove(0);
        list.remove(0);
        ArrayAdapter<String> TypesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);
        TypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statesSpinner.setAdapter(TypesAdapter);
    }

//    private void fillListview(List<String> list, ListView lv) {
//
//        // Найдите ListView
//
//        // Используйте ArrayAdapter для подключения списка
//        //ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_expandable_list_item_1, items);
//
//        //listView.setAdapter(adapter);//---------------------------------------
//
//
////        // Создаем адаптер
////
////        adapter = new StringAdapter(getActivity(), list);
////
////        // Устанавливаем адаптер для ListView
////        lv.setAdapter(adapter);
//
//
//    }
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

    private int getRoleColor(int key)
    {
        //"@drawable/roundcorner"
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
        //"@drawable/roundcorner"
        int icon;
        switch (key){
//            case 1: icon = R.drawable.null_icon; break;
//            case 2: icon = R.drawable.null_icon; break;
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