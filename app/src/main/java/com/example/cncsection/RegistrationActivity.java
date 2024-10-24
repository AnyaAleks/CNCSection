package com.example.cncsection;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
//SQLiteOpenHelper и SQLiteDatabase


public class RegistrationActivity extends AppCompatActivity {

    //private String[] roles = {"Должность","Менеджер по планированию","Мастер","Оператор"};
    HashMap<Integer, String> hashRoles=new HashMap<Integer, String>();
    Spinner roles_spinner;
    TextView fio_f;
    TextView fio_i;
    TextView fio_o;
    //TextView orderNumber;

    DBStaff dbStaff;
    ImageView calendar_button;
    EditText calendar_date;

    EditText password_first;
    EditText password_second;
    TextView errorTextView;

    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        dbStaff = new DBStaff(this);

        roles_spinner = (Spinner) findViewById(R.id.role_spinner);
        password_first = findViewById(R.id.textInputEditPasswordSignUpFirst);
        password_second = findViewById(R.id.textInputEditPasswordSignUpSecond);
        //        String passwordMD5 = md5(password_1.getText().toString());


        calendar_button = findViewById(R.id.calendar);
        calendar_date = findViewById(R.id.calendar_date);
//        FillSpinners(roles, roles_spinner);

        fio_f = (TextView) findViewById(R.id.fio_f_entry);
        fio_i = (TextView) findViewById(R.id.fio_i_entry);
        fio_o = (TextView) findViewById(R.id.fio_o_entry);
        errorTextView = findViewById(R.id.error_text_view);

        //Заполнение списка для Должностей
        Cursor csr = dbStaff.getAll("Access");
        while (csr.moveToNext()) {
            hashRoles.put(csr.getInt(csr.getColumnIndex("id_access")), csr.getString(csr.getColumnIndex("title")));
        }
        Log.d("DB_STAFF",hashRoles.toString());

        FillRoleSpinner();

        calendar_button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        RegistrationActivity.this,
                        (view, year1, monthOfYear, dayOfMonth) ->
                                calendar_date.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year1),
                        year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private  void FillRoleSpinner()
    {
        ArrayList<String> list = new ArrayList<>(hashRoles.values());
        list.add(0,"");
        ArrayAdapter<String> TypesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
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

    private int getRoleKey()
    {
        int currentKey=42;
        String currentRole = roles_spinner.getSelectedItem().toString();
        for (Map.Entry<Integer, String> entry : hashRoles.entrySet())
        {
            int key = entry.getKey();
            String role = entry.getValue();

            if(role.equals(currentRole))
                currentKey = key;
            break;
        }
        return(currentKey);
    }


    public void goRegistToDB(View V)
    {
        boolean fio_wrong=false;

        boolean password_wrong=false;

        //boolean sthEmpty = false;
        //if(fio_f.getHint().toString().equals("") || fio_i.getHint().toString().equals("") || fio_o.getHint().toString().equals("") || calendar_date.getHint().toString().equals("")
        //||fio_f.getHint().toString().equals("Заполните поле") || fio_i.getHint().toString().equals("Заполните поле") || fio_o.getHint().toString().equals("Заполните поле"))
        String fio="";
        String password="";
        if(fio_f.getText().toString().equals("") || fio_i.getText().toString().equals("") || fio_o.getText().toString().equals("")|| calendar_date.getHint().toString().equals(""))
        {
            fio_wrong=true;
        }
        else
        {
            fio =fio_f.getText().toString() + " "
                    + fio_i.getText().toString() + " "
                    + fio_o.getText().toString();
        }
        String pas1,pas2;
        pas1=password_first.getText().toString();
        pas2=password_second.getText().toString();

        if(!pas1.equals(pas2) || pas1.isEmpty())//password_second.getText().toString().equals(password_first.getText().toString()))
        {
            password_wrong=true;
        }
        else
        {
            password=md5(password_first.getText().toString());
        }

        if(fio_wrong || password_wrong)
        {
            if(fio_wrong && password_wrong)
            {
                errorTextView.setText("Не все поля заполнены и пароли не совпадают");
            }
            else
            {
                if(fio_wrong)
                    errorTextView.setText("Не все поля заполнены");

                if(password_wrong)
                    errorTextView.setText("Пароли не совпадают");
            }
        }
        else
        {
            errorTextView.setText("");
            dbStaff.addToStaff(getRoleKey(), fio, password, calendar_date.getText().toString());
        }

    }
}