package com.example.cncsection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;

import Staff.DBStaff;

public class ListOfPeopleFragment extends Fragment {
    private DBStaff dbStaff;
    private PeopleAdapter adapter;
    private SearchView searchView;
    private ArrayList<People> peopleList = new ArrayList<>();
    private RecyclerView rvContacts;

    public ListOfPeopleFragment() {
    }

    public static ListOfPeopleFragment newInstance(String param1, String param2) {
        ListOfPeopleFragment fragment = new ListOfPeopleFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_people, container, false);

        searchView = view.findViewById(R.id.searchView);
        rvContacts = view.findViewById(R.id.list);

        dbStaff = new DBStaff(getActivity());
        Cursor csr = dbStaff.getAll("Staff");
        while (csr.moveToNext()) {
            peopleList.add(
                    new People(csr.getString(csr.getColumnIndex("fio"))
                            , csr.getInt(csr.getColumnIndex("id_access"))
                            , csr.getInt(csr.getColumnIndex("id_staff"))
            ));
        }

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


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        return view;
    }

    private void filter(String text) {
        ArrayList<People> filteredList = new ArrayList<>();
        for (People person : peopleList) {
            if (person.getfio().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(person);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new PeopleAdapter(filteredList);
            rvContacts.setAdapter(adapter);
            rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

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
}
