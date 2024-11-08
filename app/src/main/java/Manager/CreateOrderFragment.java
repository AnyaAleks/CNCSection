package Manager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;

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

import java.util.Calendar;

public class CreateOrderFragment extends Fragment {

    TextView item_number, production_time,commentary;
    EditText item_number_entry, commentary_entry, calendar_date;
    Button button_create;
    ImageView calendar_button;



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

        item_number = view.findViewById(R.id.item_number);
        production_time = view.findViewById(R.id.production_time);
        commentary = view.findViewById(R.id.commentary);
        item_number_entry = view.findViewById(R.id.item_number_entry);
        commentary_entry = view.findViewById(R.id.commentary_entry);
        button_create = view.findViewById(R.id.button);
        calendar_button = view.findViewById(R.id.calendar);
        calendar_date = view.findViewById(R.id.calendar_date);


        commentary_entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                int newHeight = commentary_entry.getLineCount() *
                        commentary_entry.getLineHeight();

                //перевод в dp
                int minHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
                        getResources().getDisplayMetrics());
                int maxHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250,
                        getResources().getDisplayMetrics());

                if (newHeight < minHeight) {newHeight = minHeight;}
                if (newHeight > maxHeight) {newHeight = maxHeight;}


                ViewGroup.LayoutParams params = commentary_entry.getLayoutParams();
                params.height = newHeight;
                commentary_entry.setLayoutParams(params);
                commentary_entry.requestLayout();
            }


        });

        calendar_date.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting;
            private int selectionStart;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                selectionStart = calendar_date.getSelectionStart();
            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isFormatting) {
                    return;
                }

                String clean = s.toString().replaceAll("\\D", ""); // Удаляем все нецифровые символы
                StringBuilder formatted = new StringBuilder();

                if (clean.length() > 0) {
                    formatted.append(clean.substring(0, Math.min(2, clean.length()))); // ДД
                    if (clean.length() >= 3) {
                        formatted.append(".").append(clean.substring(2, Math.min(4, clean.length()))); // ММ
                    }
                    if (clean.length() >= 5) {
                        formatted.append(".").append(clean.substring(4)); // ГГГГ
                    }
                }

                // Проверка на количество символов
                if (clean.length() >= 2) {
                    String day = clean.length() >= 2 ? clean.substring(0, 2) : clean;

                    if (Integer.parseInt(day) > 31) {
                        Toast.makeText(getActivity(), "Не бывает больше 31 дня в месяце", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (clean.length() >= 4) {
                    String day = clean.length() >= 2 ? clean.substring(0, 2) : clean;
                    String month = clean.length() >= 4 ? clean.substring(2, 4) : "";
                    String year = clean.length() > 4 ? clean.substring(4) : "";

                    if (Integer.parseInt(month) > 12) {
                        Toast.makeText(getActivity(), "Месяц не может быть больше 12", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!isValidDate(day, month, year)) {
                        Toast.makeText(getActivity(), "Некорректная дата", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                isFormatting = true;
                String current = formatted.toString();
                calendar_date.setText(current);
                int newSelection = selectionStart + (current.length() - s.length());
                if (selectionStart > 2 && selectionStart < 5) {
                    newSelection++;
                }
                calendar_date.setSelection(Math.max(0, Math.min(newSelection, current.length())));
                isFormatting = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            // Метод для проверки корректности даты
            private boolean isValidDate(String day, String month, String year) {
                int dayInt = Integer.parseInt(day);
                int monthInt = Integer.parseInt(month);
                int yearInt = year.isEmpty() ? 0 : Integer.parseInt(year);

                int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

                // Проверка на високосный год
                if (monthInt == 2 && isLeapYear(yearInt)) {
                    daysInMonth[2] = 29;
                }
                return dayInt <= daysInMonth[monthInt];
            }

            // Метод для проверки високосного года
            private boolean isLeapYear(int year) {
                return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
            }
        });


        calendar_button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        (view, year1, monthOfYear, dayOfMonth) ->
                                calendar_date.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year1),
                        year, month, day);

                // Установите минимальную дату (например, сегодня)
                calendar.set(year, month, day);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

                datePickerDialog.show();
            }
        });

        dbManager = new DBManager(getActivity());

        button_create.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                String number = item_number_entry.getText().toString();
                //String calendar = calendar_button.getText().toString();
                String comments = commentary_entry.getText().toString();

                dbManager.addToRequest(number, 1, comments, calendar_date.getText().toString());

                Toast.makeText(getActivity(), "Успешное добавление", Toast.LENGTH_SHORT).show();
                Cursor csr = dbManager.getAll("Request");
                while(csr.moveToNext()) {
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

}



