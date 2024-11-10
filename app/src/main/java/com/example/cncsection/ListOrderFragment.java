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
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cncsection.R;
import com.example.cncsection.Status;
import com.example.cncsection.StatusAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Staff.DBStaff;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListOrderFragment extends Fragment {

    DBStaff dbStaff;

    private StatusAdapter adapter;
    private SearchView searchView;
    ArrayList<Status> statuses = new ArrayList<Status>();
    ArrayList<Status> baseStatusList = new ArrayList<Status>();
    RecyclerView rvContacts;
    private CheckBox filterStatusCheckbox;

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

    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_orderxml, container, false);

        searchView = view.findViewById(R.id.searchView);
        rvContacts = view.findViewById(R.id.list);
        filterStatusCheckbox = view.findViewById(R.id.filter_status_checkbox);


        // Инициализация списка статусов
        dbStaff = new DBStaff(getActivity());
        Cursor csr = dbStaff.getAll("Request");
        while (csr.moveToNext()) {
            statuses.add(new Status(csr.getString(csr.getColumnIndex("part_number"))
                    , csr.getInt(csr.getColumnIndex("id_status"))
                    , csr.getInt(csr.getColumnIndex("id_order"))
            ));
        }
        //statuses = Status.createStatusesList(20);
        adapter = new StatusAdapter(statuses);
        rvContacts.setAdapter(adapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setOnItemClickListener(new StatusAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Log.d("GGG",""+statuses.get(position).getIdOrder());
                Intent intent = new Intent(getActivity(), OrderInformation.class);
                intent.putExtra("id_current_order", ""+statuses.get(position).getIdOrder());
                startActivity(intent);
            }
        });


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

        filterStatusCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            filter(searchView.getQuery().toString(), isChecked);
        });

        return view;
    }

    private void filter(String text,  boolean isFilterByStatus) {
        baseStatusList = statuses;
        Collections.sort(statuses, new statusesComparator());

        if(isFilterByStatus){
            adapter = new StatusAdapter(statuses);
            rvContacts.setAdapter(adapter);
            rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter.setOnItemClickListener(new StatusAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(getActivity(), OrderInformation.class);
                    intent.putExtra("id_current_order", ""+statuses.get(position).getIdOrder());
                    startActivity(intent);
                }
            });
        } else{
            adapter = new StatusAdapter(baseStatusList);
            rvContacts.setAdapter(adapter);
            rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter.setOnItemClickListener(new StatusAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(getActivity(), OrderInformation.class);
                    intent.putExtra("id_current_order", ""+baseStatusList.get(position).getIdOrder());
                    startActivity(intent);
                }
            });
        }
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
}