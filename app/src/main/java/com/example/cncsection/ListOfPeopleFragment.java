package com.example.cncsection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Enter.EntryActivity;
import Staff.DBStaff;

public class ListOfPeopleFragment extends Fragment {
    private DBStaff dbStaff;
    private PeopleAdapter adapter;
    private SearchView searchView;
    private ArrayList<People> peopleList = new ArrayList<>();
    private RecyclerView rvContacts;
    private CheckBox filterPeopleCheckbox;

    public ListOfPeopleFragment() {
    }

    public static ListOfPeopleFragment newInstance(String param1, String param2) {
        ListOfPeopleFragment fragment = new ListOfPeopleFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_people, container, false);

        // Настройка Toolbar
        Toolbar toolbarentry = view.findViewById(R.id.toolbar_entry_list_staff);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarentry);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24);
        }

        searchView = view.findViewById(R.id.searchView);
        rvContacts = view.findViewById(R.id.list);
        filterPeopleCheckbox = view.findViewById(R.id.filter_people_checkbox);

        dbStaff = new DBStaff(getActivity());
        Cursor csr = dbStaff.getAll("Staff");
        while (csr.moveToNext()) {
            peopleList.add(
                    new People(csr.getString(csr.getColumnIndex("fio"))
                            , csr.getInt(csr.getColumnIndex("id_access"))
                            , csr.getInt(csr.getColumnIndex("id_staff"))
            ));
        }
        setupPeopleAdapter(peopleList);
//        adapter = new PeopleAdapter(peopleList);
//        rvContacts.setAdapter(adapter);
//        rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
//        adapter.setOnItemClickListener(new PeopleAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
////                Log.d("GGG",""+statuses.get(position).getIdOrder());
//                Intent intent = new Intent(getActivity(), PeopleInformation.class);
//                intent.putExtra("id_current_person", ""+peopleList.get(position).getId());
//                startActivity(intent);
//            }
//        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText, filterPeopleCheckbox.isChecked());
                return true;
            }
        });

        filterPeopleCheckbox.setOnCheckedChangeListener((buttonView, isCheck) -> {
            filter(searchView.getQuery().toString(), isCheck);
        });

        //Проверка на обновление адаптера
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                Cursor csrCount = dbStaff.getAll("Staff");
                if(peopleList.size() != csrCount.getCount()){
                    Log.d("CountR", String.valueOf(peopleList.size()) + "--" + csrCount.getCount());

                    peopleList.clear();
                    while (csrCount.moveToNext()) {
                        peopleList.add(
                                new People(csrCount.getString(csrCount.getColumnIndex("fio"))
                                        , csrCount.getInt(csrCount.getColumnIndex("id_access"))
                                        , csrCount.getInt(csrCount.getColumnIndex("id_staff"))
                                ));
                    }

                    adapter = new PeopleAdapter(peopleList);
                    rvContacts.setAdapter(adapter);
                    rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            public void onFinish() {
                //mTextField.setText("done!");
            }

        }.start();

        return view;
    }
    private void setupPeopleAdapter(ArrayList<People> peopleList) {
        adapter = new PeopleAdapter(peopleList);
        rvContacts.setAdapter(adapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setOnItemClickListener(new PeopleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Log.d("GGG",""+statuses.get(position).getIdOrder());
                Intent intent = new Intent(getActivity(), PeopleInformation.class);
                intent.putExtra("id_current_person", ""+peopleList.get(position).getId());
                startActivity(intent);
            }
        });
    }
    private void filter(String text, boolean isFilterByRole) {
        ArrayList<People> filteredList = new ArrayList<>();
        for (People person : peopleList) {
            if (person.getfio().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(person);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
        }
        if (isFilterByRole) {
            Collections.sort(filteredList, new ListOfPeopleFragment.staffFioComparator());
        }
        setupPeopleAdapter(filteredList);
    }

//    private void filter(String text) {
//        ArrayList<People> filteredList = new ArrayList<>();
//        for (People person : peopleList) {
//            if (person.getfio().toLowerCase().contains(text.toLowerCase())) {
//                filteredList.add(person);
//            }
//        }
//        if (filteredList.isEmpty()) {
//            Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
//        }
//        adapter.notifyDataSetChanged();
////            adapter = new PeopleAdapter(filteredList);
////            rvContacts.setAdapter(adapter);
////            rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
//    }

    public class staffFioComparator implements Comparator<People>
    {
        @Override
        public int compare(People p1, People p2) {
            return p1.getfio().compareTo(p2.getfio());
        }
    }

    public static ListOfPeopleFragment newInstance() {
        return new ListOfPeopleFragment();
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
}
