package com.example.cncsection;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmationDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmationDialogFragment extends Fragment {
    static String _delete_id;
    static String _table_name;

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

        //Кнопка Yes
        Chip chipYes = (Chip) view.findViewById(R.id.chip_Yes);
        chipYes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (_table_name){
                    case "Staff": Log.d("GGG", "Staff"); break;
                    case "Order": Log.d("GGG", "Order"); break;
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