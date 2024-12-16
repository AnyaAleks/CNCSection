package com.example.cncsection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cncsection.R;
import com.example.cncsection.Status;
import com.example.cncsection.StatusAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Enter.EntryActivity;
import Master.GenerateOrderFragment;
import Operator.OperatorOrderFragment;
import Staff.DBStaff;

public class ListOrderFragment extends Fragment {

    DBStaff dbStaff;
    UserSettings userSettings = new UserSettings();

    private StatusAdapter adapter;
    private SearchView searchView;
    ArrayList<Status> statuses = new ArrayList<Status>();
    //ArrayList<Status> baseStatusList = new ArrayList<Status>();
    RecyclerView rvContacts;
    private CheckBox filterStatusCheckbox;
    LinearLayout mainLinearLayout;

    public ListOrderFragment() {
        // Required empty public constructor
    }


    public static ListOrderFragment newInstance(String param1, String param2) {
        ListOrderFragment fragment = new ListOrderFragment();

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_orderxml, container, false);

        // Настройка Toolbar
        Toolbar toolbarentry = view.findViewById(R.id.toolbar_entry_list_order);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarentry);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24);

        }
        // Установка слушателя для обработки нажатий на элементы меню
        toolbarentry.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход к EntryActivity
                Intent intent = new Intent(getActivity(), EntryActivity.class);
                startActivity(intent);
                getActivity().finish(); // Закрываем текущую активность
            }
        });

        searchView = view.findViewById(R.id.searchView);
        rvContacts = view.findViewById(R.id.list);
        filterStatusCheckbox = view.findViewById(R.id.filter_status_checkbox);


//        //Сохранение настроек
//        userSettings.setRoleUser(csr.getInt(csrAccess.getColumnIndex("id_access")));
//        userSettings.setIdUser(csr.getString(csr.getColumnIndex("id_staff")));
//        saveJsonData();

        //Доставание из настроек
        loadJsonData();

        //Только у менеджера будет нижнее меню => убрать отступ
        mainLinearLayout = view.findViewById(R.id.mainLinearLayout);
        if(userSettings.getRoleUser() != 1){
             fixLayoutDelMargin();
        }

        // Инициализация списка статусов
        dbStaff = new DBStaff(getActivity());
        Cursor csr = dbStaff.getAll("Request");
        statuses.clear();
        while (csr.moveToNext()) {
            statuses.add(new Status(csr.getString(csr.getColumnIndex("part_number"))
                    , csr.getInt(csr.getColumnIndex("id_status"))
                    , csr.getInt(csr.getColumnIndex("id_order"))
            ));
        }

        setupAdapter(statuses);
        //statuses = Status.createStatusesList(20);
//        adapter = new StatusAdapter(statuses);
//        rvContacts.setAdapter(adapter);
//        rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
//        adapter.setOnItemClickListener(new StatusAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
////                Log.d("GGG",""+statuses.get(position).getIdOrder());
//                Intent intent = new Intent(getActivity(), OrderInformation.class);
//                intent.putExtra("id_current_order", ""+statuses.get(position).getIdOrder());
//                startActivity(intent);
//            }
//        });


        // Установка слушателя для SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //newText - название детали
                filter(newText, filterStatusCheckbox.isChecked());
                return true;
            }
        });
        //Фокус на поиске - убираем отступ
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userSettings.getRoleUser() == 1){
                    fixLayoutDelMargin();
                }
            }
        });
        //Фокус скрылся - добавляем отступ
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if(userSettings.getRoleUser() == 1) {
                    fixLayoutAddMargin();
                }
                return false;
            }
        });

        filterStatusCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            filter(searchView.getQuery().toString(), isChecked);
        });

        //Проверка на обновление адаптера
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                Cursor csrCount = dbStaff.getAll("Request");
                if(statuses.size() != csrCount.getCount()){
                    Log.d("CountR", String.valueOf(statuses.size()) + "--" + csrCount.getCount());

                    statuses.clear();
                    ArrayList<Status> statuses2 = new ArrayList<Status>();
                    while (csrCount.moveToNext()) {
                        statuses2.add(new Status(csrCount.getString(csrCount.getColumnIndex("part_number"))
                                , csrCount.getInt(csrCount.getColumnIndex("id_status"))
                                , csrCount.getInt(csrCount.getColumnIndex("id_order"))
                        ));
                    }
                    statuses.addAll(statuses2);
                    adapter.notifyDataSetChanged();
                }
            }

            public void onFinish() {
                //mTextField.setText("done!");
            }

        }.start();

        return view;
    }

    @SuppressLint("Range")
    private void setupAdapter(ArrayList<Status> statusList) {
        adapter = new StatusAdapter(statusList);
        rvContacts.setAdapter(adapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(getActivity(), OrderInformation.class);
            intent.putExtra("id_current_order", "" + statusList.get(position).getIdOrder());
            startActivity(intent);
        });

        adapter.setOnItemLongClickListener(position -> {
            //Проверка на роль в настройках

            //Изменение статуса у Оператора
            if(userSettings.getRoleUser() == 3){
                // Переход к фрагменту OperatorOrderFragment
                OperatorOrderFragment operatorOrderFragment = OperatorOrderFragment.newInstance();

                if(statusList.get(position).getStatus() == 1){
                    Toast.makeText(getActivity(), "Заявка еще не сформирована!", Toast.LENGTH_SHORT).show();
                }
                else if(statusList.get(position).getStatus() == 7){
                    Toast.makeText(getActivity(), "Заявка уже выполнена!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Bundle bundle = new Bundle();
                    bundle.putInt("idOrder", statusList.get(position).getIdOrder());
                    operatorOrderFragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_operator, operatorOrderFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
            //Оформление заявки мастером
            if(userSettings.getRoleUser() == 2){
                GenerateOrderFragment generateOrderFragment = GenerateOrderFragment.newInstance();

                int id_current_order = statusList.get(position).getIdOrder();
                Cursor csrCount = dbStaff.getAllOrder();
                boolean flagOrderAlreadyExist = false;
                while (csrCount.moveToNext()) {
                    if(id_current_order == csrCount.getInt(csrCount.getColumnIndex("id_request"))){
                        flagOrderAlreadyExist = true;
                    }
                }

//                if(flagOrderAlreadyExist){
//                    Toast.makeText(getActivity(), "Заявка уже сформирована!", Toast.LENGTH_SHORT).show();
//                } else{
                    if(statusList.get(position).getStatus() == 3
                            || statusList.get(position).getStatus() == 4
                            || statusList.get(position).getStatus() == 5
                            || statusList.get(position).getStatus() == 6){
                        Toast.makeText(getActivity(), "Заявка находится в работе!", Toast.LENGTH_SHORT).show();
                    }
                    else if(statusList.get(position).getStatus() == 2){
                        Toast.makeText(getActivity(), "Заявка уже сформирована!", Toast.LENGTH_SHORT).show();
                    }
                    else if(statusList.get(position).getStatus() == 7){
                        Toast.makeText(getActivity(), "Заявка уже выполнена!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Bundle bundle = new Bundle();
                        bundle.putInt("idOrder", statusList.get(position).getIdOrder());
                        generateOrderFragment.setArguments(bundle);

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container_master, generateOrderFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                //}
            }
        });
    }

//    private void filter(String text,  boolean isFilterByStatus) {
//        baseStatusList = statuses;
//        Collections.sort(statuses, new statusesComparator());
//
//        if(isFilterByStatus){
//            adapter = new StatusAdapter(statuses);
//            rvContacts.setAdapter(adapter);
//            rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
//            adapter.setOnItemClickListener(new StatusAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(int position) {
//                    Intent intent = new Intent(getActivity(), OrderInformation.class);
//                    intent.putExtra("id_current_order", ""+statuses.get(position).getIdOrder());
//                    startActivity(intent);
//                }
//            });
//        } else{
//            adapter = new StatusAdapter(baseStatusList);
//            rvContacts.setAdapter(adapter);
//            rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
//            adapter.setOnItemClickListener(new StatusAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(int position) {
//                    Intent intent = new Intent(getActivity(), OrderInformation.class);
//                    intent.putExtra("id_current_order", ""+baseStatusList.get(position).getIdOrder());
//                    startActivity(intent);
//                }
//            });
//        }
//    }

    private void filter(String text, boolean isFilterByStatus) {
        ArrayList<Status> filteredList = new ArrayList<>();
        for (Status status : statuses) {
            if (status.getApplication().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(status);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(getActivity(), "Данные не найдены", Toast.LENGTH_SHORT).show();
        }
        if (isFilterByStatus) {
            Collections.sort(filteredList, new statusesComparator());
        }
        setupAdapter(filteredList);
    }

    public class statusesComparator implements Comparator<Status>
    {
        @Override
        public int compare(Status st1, Status st2) {
            int a = st1.getStatus();
            int b = st2.getStatus();
            return a > b ? 1 : (a == b ? 0 : -1);
        }
    }

    public static ListOrderFragment newInstance(){
        return new ListOrderFragment();
    }

    //Сохранение в настройки
    public void saveJsonData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userSettings);
        editor.putString("user settings", json);
        editor.apply();
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

    public void fixLayoutAddMargin(){
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mainLinearLayout
                .getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 250);
    }

    public void fixLayoutDelMargin(){
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mainLinearLayout
                .getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
    }
}