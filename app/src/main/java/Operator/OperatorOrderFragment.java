package Operator;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cncsection.ListOrderFragment;
import com.example.cncsection.R;

import java.util.ArrayList;
import java.util.HashMap;

import Staff.DBStaff;

public class OperatorOrderFragment extends Fragment {

    ImageButton stateInformerButton;
    Spinner statesSpinner;
    HashMap<Integer, String> hashSates=new HashMap<Integer, String>();
    DBStaff dbStaff;
    TextView idOrderTextView;
    Button saveButton;

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

        // Настройка Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24);
        }

        dbStaff = new DBStaff(getActivity());

        stateInformerButton = view.findViewById(R.id.stateInformer);
        statesSpinner = (Spinner) view.findViewById(R.id.states_spinner);
        idOrderTextView = view.findViewById(R.id.idOrderTextView);
        saveButton = view.findViewById(R.id.saveButton);

        //Получение id
        Bundle bundle = this.getArguments();
        idOrder = bundle.getInt("idOrder");
        idOrderTextView.setText("№ " + idOrder);

        //Заполнение списка для состояний индикатора
        Cursor csr = dbStaff.getAll("Status");
        while (csr.moveToNext()) {
            hashSates.put(csr.getInt(csr.getColumnIndex("id_status")), csr.getString(csr.getColumnIndex("title")));
        }
//        Log.d("DB_STAFF",hashSates.toString());
        fillStatesSpinner();
        statesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                stateInformerButton.setBackgroundDrawable(getResources().getDrawable( getRoleColor(position+1)));
                stateInformerButton.setImageResource( getRoleIcon(position+1));
                selectedNewRole = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

    //не работает логика перехода назад к фрагменту со списком заявок
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().onBackPressed();
            Log.d("OperatorOrderFragment", "Back button pressed");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static OperatorOrderFragment newInstance(){
        return new OperatorOrderFragment();
    }

    private  void fillStatesSpinner()
    {
        ArrayList<String> list = new ArrayList<>(hashSates.values());
        ArrayAdapter<String> TypesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);
        TypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statesSpinner.setAdapter(TypesAdapter);
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