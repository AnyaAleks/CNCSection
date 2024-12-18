package Staff;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cncsection.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Enter.EntryActivity;
import Staff.DBStaff;

public class RegistrationFragment extends Fragment {

    //private String[] roles = {"Должность","Менеджер по планированию","Мастер","Оператор"};
    HashMap<Integer, String> hashRoles=new HashMap<Integer, String>();
    Spinner roles_spinner;
    TextView fio_f;
    TextView fio_i;
    TextView fio_o;
    //TextView orderNumber;
    LinearLayout mainLinearLayout;

    DBStaff dbStaff;
    ImageView calendar_button_staff;
    EditText calendar_date_staff;

    EditText password_first;
    EditText password_second;
    //TextView errorTextView;

    Button add_button;

    public RegistrationFragment() {
    }
    public RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        // Настройка Toolbar
        Toolbar toolbar_entry = view.findViewById(R.id.toolbar_entry_staff);
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

        TextView errorFirstName = view.findViewById(R.id.error_first_name);
        TextView errorSecondName = view.findViewById(R.id.error_second_name);
        TextView errorThirdName = view.findViewById(R.id.error_third_name);
        TextView errorCalendarDate = view.findViewById(R.id.error_calendar_date);
        TextView errorRole = view.findViewById(R.id.error_role);
        TextView errorPassword = view.findViewById(R.id.error_password);
        TextView errorPassword_2 = view.findViewById(R.id.error_password_2);
        mainLinearLayout = view.findViewById(R.id.mainLinearLayout);


        dbStaff = new DBStaff(getActivity());

        add_button = view.findViewById(R.id.add_button);

        roles_spinner = (Spinner) view.findViewById(R.id.role_spinner);
        password_first = view.findViewById(R.id.textInputEditPasswordSignUpFirst);
        password_second = view.findViewById(R.id.textInputEditPasswordSignUpSecond);
        //        String passwordMD5 = md5(password_1.getText().toString());


        calendar_button_staff = view.findViewById(R.id.calendar_button_staff);
        calendar_date_staff = view.findViewById(R.id.calendar_date_staff);
//        FillSpinners(roles, roles_spinner);

        fio_f = (TextView) view.findViewById(R.id.fio_f_entry);
        fio_i = (TextView) view.findViewById(R.id.fio_i_entry);
        fio_o = (TextView) view.findViewById(R.id.fio_o_entry);
        //errorTextView = view.findViewById(R.id.error_text_view);

        //Заполнение списка для Должностей
        Cursor csr = dbStaff.getAll("Access");
        while (csr.moveToNext()) {
            hashRoles.put(csr.getInt(csr.getColumnIndex("id_access")), csr.getString(csr.getColumnIndex("title")));
        }
        Log.d("DB_STAFF",hashRoles.toString());

        FillRoleSpinner();

        calendar_date_staff.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting;
            private int selectionStart;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                selectionStart = calendar_date_staff.getSelectionStart();
            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isFormatting) {
                    return;
                }

                String clean = s.toString().replaceAll("\\D", ""); // Удаляем все нецифровые символы
                if (clean.length() > 8) {
                    clean = clean.substring(0, 8);
                }
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

                    if (!day.isEmpty() && !month.isEmpty() && year.length() >= 4) {
                        if (!isValidDate(day, month, year)) {
                            Toast.makeText(getActivity(), "Некорректная дата", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int yearInt = Integer.parseInt(year);
                        if (yearInt > 2100) {
                            Toast.makeText(getActivity(), "Год не может быть больше 2100", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                isFormatting = true;
                String current = formatted.toString();
                calendar_date_staff.setText(current);
                int newSelection = selectionStart + (current.length() - s.length());
                if (selectionStart > 2 && selectionStart < 5) {
                    newSelection++;
                }
                calendar_date_staff.setSelection(Math.max(0, Math.min(newSelection, current.length())));
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

        calendar_button_staff.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            String formattedDate = String.format("%02d.%02d.%d", dayOfMonth, monthOfYear + 1, year1);
                            calendar_date_staff.setText(formattedDate);
                        },
                        year, month, day);
                calendar.set(year, month, day);

                datePickerDialog.show();
            }
        });

        fio_f.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorSecondName.setText("");}
            @Override
            public void afterTextChanged(Editable s) {}
        });
        fio_i.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorFirstName.setText("");}
            @Override
            public void afterTextChanged(Editable s) {}
        });
        fio_o.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorThirdName.setText("");}
            @Override
            public void afterTextChanged(Editable s) {}
        });
        calendar_date_staff.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorCalendarDate.setText("");}
            @Override
            public void afterTextChanged(Editable s) {}
        });
        password_second.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorPassword_2.setText("");}
            @Override
            public void afterTextChanged(Editable s) {}
        });
        password_first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorPassword.setText("");}
            @Override
            public void afterTextChanged(Editable s) {}
        });
        roles_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                errorRole.setText("");
                errorRole.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                boolean flag = false;
                boolean password_wrong = false;

                String fio = "";
                String password = "";
                errorFirstName.setText("");
                errorSecondName.setText("");
                errorThirdName.setText("");
                errorCalendarDate.setText("");
                errorPassword.setText("");
                errorRole.setText("");

                if (fio_f.getText().toString().isEmpty()) {
                    errorSecondName.setText("Фамилия не может быть пустой");
                    errorSecondName.setVisibility(View.VISIBLE);
                    flag = true;
                }
                if (fio_i.getText().toString().isEmpty()) {
                    errorFirstName.setText("Имя не может быть пустым");
                    errorFirstName.setVisibility(View.VISIBLE);
                    flag = true;
                }
                if (fio_o.getText().toString().isEmpty()) {
                    errorThirdName.setText("Отчество не может быть пустым");
                    errorThirdName.setVisibility(View.VISIBLE);
                    flag = true;
                }
                if (calendar_date_staff.getText().toString().isEmpty()) {
                    errorCalendarDate.setText("Дата не может быть пустой");
                    errorCalendarDate.setVisibility(View.VISIBLE);
                    flag = true;
                } else {
                    fio = fio_f.getText().toString() + " "
                            + fio_i.getText().toString() + " "
                            + fio_o.getText().toString();
                }

                String pas1 = password_first.getText().toString();
                String pas2 = password_second.getText().toString();

                // Проверяем, совпадают ли пароли и не пустые ли они
                if (pas1.isEmpty()) {
                    errorPassword.setText("Пароль не может быть пустым");
                    errorPassword.setVisibility(View.VISIBLE);
                    password_wrong = true;
                } else if (!pas1.equals(pas2)) {
                    errorPassword_2.setText("Пароли не совпадают");
                    errorPassword_2.setVisibility(View.VISIBLE);
                    password_wrong = true;
                } else {
                    password = md5(pas1);
                }

                int roleKey = getRoleKey();
                if (roleKey == -1) {
                    errorRole.setText("Роль не выбрана");
                    errorRole.setVisibility(View.VISIBLE);
                    flag = true;
                }

                if (flag || password_wrong) {
                    return;
                }

                Toast.makeText(getActivity(), "Успешное добавление", Toast.LENGTH_SHORT).show();
                Log.i("GGGGGG", roleKey + " " + fio + " " + password + " " + calendar_date_staff.getText().toString());
                dbStaff.addToStaff(roleKey, fio, password, calendar_date_staff.getText().toString());
                fio_f.setText("");
                fio_i.setText("");
                fio_o.setText("");
                calendar_date_staff.setText("");
                password_first.setText("");
                password_second.setText("");
                roles_spinner.setSelection(0);
            }
        });

        return view;
    }

    public static RegistrationFragment newInstance(){
        return new RegistrationFragment();
    }

    private  void FillRoleSpinner()
    {
        ArrayList<String> list = new ArrayList<>(hashRoles.values());
        list.add(0,"");
        ArrayAdapter<String> TypesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);
        TypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roles_spinner.setAdapter(TypesAdapter);
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private int getRoleKey() {
        String currentRole = roles_spinner.getSelectedItem() != null ? roles_spinner.getSelectedItem().toString() : "";
        if (currentRole.isEmpty() || currentRole.equals("Выберите роль")) {
            return -1;}
        for (Map.Entry<Integer, String> entry : hashRoles.entrySet()) {
            int key = entry.getKey();
            String role = entry.getValue();

            if (role.equals(currentRole)) {
                return key;
            }
        }
        return 42;
    }
}