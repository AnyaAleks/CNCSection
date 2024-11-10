package com.example.cncsection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import Staff.DBStaff;

public class ConfirmationDialogFragment extends Fragment {
    static String _delete_id;
    static String _table_name;

    DBStaff dbStaff;

    public ConfirmationDialogFragment() {
        // Required empty public constructor
    }

    public static ConfirmationDialogFragment newInstance(String del_id, String table_name) {
        _delete_id = del_id;
        _table_name = table_name;
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_confirmation_dialog, container, false);

        dbStaff = new DBStaff(getActivity());

        //Кнопка Yes
        Chip chipYes = (Chip) view.findViewById(R.id.chip_Yes);
        chipYes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (_table_name){//переход осуществлен из окна
                    case "Staff":
                        dbStaff.deleteStaffById(_delete_id);
                        getActivity().finish();
                        break;
                    case "Order":
                        dbStaff.deleteOrderByIdInRequest(_delete_id);
                        //dbStaff.deleteOrderByIdInOrder(id_current_order)
                        getActivity().finish();
                        break;
                }

//                Intent intent = new Intent(getActivity(), SignInActivity.class);
//                startActivity(intent);
            }
        });

        //Кнопка No
        Chip chipNo = (Chip) view.findViewById(R.id.chip_No);
        chipNo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().remove(ConfirmationDialogFragment.this).commit();
            }
        });

        return view;
    }
}