package com.example.cncsection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListOrderFragment extends Fragment {

    Button button_openInfo;

    TextView search_button;
    private StatusAdapter adapter;
    private SearchView searchView;
    private ArrayList<Status> statuses;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListOrderManagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListOrderFragment newInstance(String param1, String param2) {
        ListOrderFragment fragment = new ListOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_orderxml, container, false);

        searchView = view.findViewById(R.id.searchView);
        RecyclerView rvContacts = view.findViewById(R.id.list);

        // Инициализация списка статусов
        statuses = Status.createStatusesList(20);
        adapter = new StatusAdapter(statuses);
        rvContacts.setAdapter(adapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));

        // Установка слушателя для SearchView
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

        button_openInfo = view.findViewById(R.id.buttonOpenInfo);
        button_openInfo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(getActivity(), OrderInformation.class));
                //intent.putExtra(Contacts.class.getSimpleName(), contactsList.get(position));
                startActivity(intent);
            }
        });

        return view;
    }

    private void filter(String text) {
        ArrayList<Status> filteredList = new ArrayList<>();
        for (Status st : statuses) {
            if (st.getApplication().toLowerCase().contains(text.toLowerCase()) ||
                    st.getStatus().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(st);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            adapter.filterList(filteredList);
        }
    }

    public static ListOrderFragment newInstance(){
        return new ListOrderFragment();
    }
}