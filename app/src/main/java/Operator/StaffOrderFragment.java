package Operator;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.cncsection.R;

import java.util.ArrayList;
import java.util.HashMap;

import Staff.DBStaff;

public class StaffOrderFragment extends Fragment {

    ImageButton stateInformerButton;
    Spinner statesSpinner;
    HashMap<Integer, String> hashSates=new HashMap<Integer, String>();
    DBStaff dbStaff;

    public StaffOrderFragment() {
        // Required empty public constructor
    }
    public static StaffOrderFragment newInstance(String param1, String param2) {
        StaffOrderFragment fragment = new StaffOrderFragment();
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
        View view = inflater.inflate(R.layout.fragment_staff_order, container, false);

        dbStaff = new DBStaff(getActivity());

        stateInformerButton = view.findViewById(R.id.stateInformer);
        statesSpinner = (Spinner) view.findViewById(R.id.states_spinner);

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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