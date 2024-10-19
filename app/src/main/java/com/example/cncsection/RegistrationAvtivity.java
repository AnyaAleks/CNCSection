package com.example.cncsection;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
//SQLiteOpenHelper и SQLiteDatabase


public class RegistrationAvtivity extends AppCompatActivity {

    private String[] roles = {"Должность","Менеджер по планированию","Мастер","Оператор"};
    HashMap<Integer, String> hashRoles=new HashMap<Integer, String>();
    Spinner roles_spinner;
    TextView fio_f;
    TextView fio_i;
    TextView fio_o;
    //TextView orderNumber;

    DBStaff dbStaff;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration_avtivity);

        dbStaff = new DBStaff(this);

        roles_spinner = (Spinner) findViewById(R.id.role_spinner);
//        FillSpinners(roles, roles_spinner);

        fio_f = (TextView) findViewById(R.id.fio_f);
        fio_i = (TextView) findViewById(R.id.fio_i);
        fio_o = (TextView) findViewById(R.id.fio_o);

        //Заполнение списка для Должностей
        Cursor csr = dbStaff.getAll("Access");
        while (csr.moveToNext()) {
            hashRoles.put(csr.getInt(csr.getColumnIndex("id_access")), csr.getString(csr.getColumnIndex("title")));
        }
        Log.d("DB_STAFF",hashRoles.toString());
    }

    private  void FillSpinners(String[] arrayTypes, Spinner spinner)
    {
        ArrayAdapter<String> TypesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,arrayTypes);
        TypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(TypesAdapter);
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

    public void goRegister(View V)
    {
        String fio =fio_f.getText().toString() + " "
                + fio_i.getText().toString() + " "
                + fio_o.getText().toString();

        //Сделать проверку на пустые поля и позволять добавить если поле пустое
        //Если хоть одно поле пустое выводить toast

        //Добавить ключ из роли
        //Добавить пароль, проверки для паролей
        //дата рождения
        dbStaff.addToStaff(1, fio, "password", "13-43-23");

    }
}