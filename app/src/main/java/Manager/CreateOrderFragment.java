package Manager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cncsection.R;
import com.example.cncsection.UserSettings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.Calendar;

import Enter.EntryActivity;

public class CreateOrderFragment extends Fragment {

    TextView item_number, production_time,commentary;
    EditText item_number_entry, commentary_entry, calendar_date;
    Button button_create;
    ImageView calendar_button;

    UserSettings userSettings = new UserSettings();

    DBManager dbManager;

    public CreateOrderFragment() {
        // Required empty public constructor
    }
    public static CreateOrderFragment newInstance(String param1, String param2) {
        CreateOrderFragment fragment = new CreateOrderFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_order, container, false);

        // Настройка Toolbar
        Toolbar toolbar_entry = view.findViewById(R.id.toolbar_entry_manager);
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
                // Переход к EntryActivity
                Intent intent = new Intent(getActivity(), EntryActivity.class);
                startActivity(intent);
                getActivity().finish(); // Закрываем текущую активность
            }
        });

        TextView errorNumber = view.findViewById(R.id.error_number);
//        TextView errorDate = view.findViewById(R.id.error_calendar);

        //Доставание из настроек
        loadJsonData();

        item_number = view.findViewById(R.id.item_number);
        production_time = view.findViewById(R.id.production_time);
        commentary = view.findViewById(R.id.commentary);
        item_number_entry = view.findViewById(R.id.item_number_entry);
        commentary_entry = view.findViewById(R.id.commentary_entry);
        button_create = view.findViewById(R.id.button);
//        calendar_button = view.findViewById(R.id.calendar);
        calendar_date = view.findViewById(R.id.calendar_date);

        calendar_date.setFocusable(false);
        calendar_date.setClickable(false);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String todayDate = String.format("%02d.%02d.%04d", day, month + 1, year);
        calendar_date.setText(todayDate);
        //boolean isValid = false;

        commentary_entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                int lineHeight = commentary_entry.getLineHeight();
                int padding = commentary_entry.getPaddingTop() + commentary_entry.getPaddingBottom();

                int newHeight = (commentary_entry.getLineCount() * lineHeight) + padding;

                // Перевод в dp
                int minHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
                        getResources().getDisplayMetrics());
                int maxHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300,
                        getResources().getDisplayMetrics());

                if (newHeight < minHeight) {
                    newHeight = minHeight;
                } else if (newHeight > maxHeight) {
                    newHeight = maxHeight;
                }

                ViewGroup.LayoutParams params = commentary_entry.getLayoutParams();
                params.height = newHeight;
                commentary_entry.setLayoutParams(params);
                commentary_entry.requestLayout();
            }

        });

//        calendar_button.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                Calendar calendar = Calendar.getInstance();
//                int year = calendar.get(Calendar.YEAR);
//                int month = calendar.get(Calendar.MONTH);
//                int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(
//                        getActivity(),
//                        (view, year1, monthOfYear, dayOfMonth) ->
//                                calendar_date.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year1),
//                        year, month, day);
//
//                // Установите минимальную дату (например, сегодня)
//                calendar.set(year, month, day);
//                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
//
//                datePickerDialog.show();
//            }
//        });

        dbManager = new DBManager(getActivity());


        item_number_entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorNumber.setText("");}
            @Override
            public void afterTextChanged(Editable s) {}
        });

//        calendar_date.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                errorDate.setText("");}
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });


        button_create.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                String number = item_number_entry.getText().toString().trim();
                String date = calendar_date.getText().toString().trim();
                String comments = commentary_entry.getText().toString().trim();

                boolean itIsError = false;
                if (number.isEmpty()) {
                    errorNumber.setText("Необходимо заполнить поле номера");
                    itIsError = true;
                }
//                if (date.isEmpty()) {
//                    errorDate.setText("Необходимо заполнить поле даты");
//                    itIsError = true;
//                }
                if (itIsError) {
                    return;
                }

                dbManager.addToRequest(number, 1, comments, date, userSettings.getIdUser());
                Toast.makeText(getActivity(), "Успешное добавление", Toast.LENGTH_SHORT).show();

                item_number_entry.setText("");
                calendar_date.setText("");
                commentary_entry.setText("");

                Cursor csr = dbManager.getAll("Request");
                while (csr.moveToNext()) {
                    Log.d("DB_Manager",
                            "ID = " + csr.getInt(csr.getColumnIndex("id_order"))
                                    + " PART_NUMBER = " + csr.getInt(csr.getColumnIndex("part_number"))
                                    + " ID_STATUS = " + csr.getInt(csr.getColumnIndex("id_status"))
                                    + " COMMENT = " + csr.getString(csr.getColumnIndex("comment"))
                                    + " DATE = " + csr.getString(csr.getColumnIndex("date"))
                    );
                }
            }
        });

        return view;

    }

    public static CreateOrderFragment newInstance(){
        return new CreateOrderFragment();
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
}



