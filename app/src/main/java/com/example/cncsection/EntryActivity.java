package com.example.cncsection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class EntryActivity extends AppCompatActivity {

    TextView entry;
    EditText password_raw, login_raw;
    Button add_button;

    DBStaff dbStaff;

    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_entry);

        entry = findViewById(R.id.entry);
        password_raw = findViewById(R.id.password);
        login_raw = findViewById(R.id.login);
        add_button = findViewById(R.id.add_button);

        //Проверка по фамилии и паролю
        dbStaff = new DBStaff(this);
        boolean isAuth = false;
        String idAccess = "";

        Cursor csr = dbStaff.getAll("Staff");
        while(csr.moveToNext()){
            Log.d("DB_STAFF",
                    "ID = " + csr.getInt(csr.getColumnIndex("id_staff"))
                            + "ID_ACCESS = " + csr.getInt(csr.getColumnIndex("id_access"))
                            + " FIO = " + csr.getString(csr.getColumnIndex("fio"))
                            + " PASSWORD = " + csr.getString(csr.getColumnIndex("password"))
            );

            String login = login_raw.getText().toString();
            String password = md5(password_raw.getText().toString()); //хэширование вводимого пароля
            if (csr.getString(csr.getColumnIndex("fio")).equals(login) &&
                    csr.getString(csr.getColumnIndex("password")).equals(password)) {
                isAuth = true;
                idAccess = csr.getString(csr.getColumnIndex("id_access"));
                break;
            }
        }

        if (isAuth) {
            String role = "";
            if (idAccess.equals("1")) {
                role = "Менеджер";}
            else if (idAccess.equals("2")) {
                role = "Мастер";}
            else if (idAccess.equals("3")) {
                role = "Оператор";}
            else if (idAccess.equals("4")) {
                role = "Отделкадров";}
            switch (role) {
                    case "Менеджер":
                        Intent intentManager = new Intent(this, CreateOrderActivity.class);
                        startActivity(intentManager);
                        break;
                    case "Мастер":
                        Intent intentMaster = new Intent(this, GenerateOrderActivity.class);
                        startActivity(intentMaster);
                        break;
                    //case "Оператор":
                        //Intent intentOperator = new Intent(this, ExecuteOrderActivity.class);
                        //startActivity(intentOperator);
                        //break;
                    //case "Отделкадров":
                        //Intent intentAdmin = new Intent(this, RegistrationAvtivity.class);
                        //startActivity(intentAdmin);
                        //break;
                    default:
                        Log.d("ROLE", "Неизвестная роль");
                }
            }
        else {Log.d("AUTH", "Авторизация неуспешна");}


        //add_button.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View view) {
                //String password = password_1.getText().toString();
                //далее надо сделать выбор определённого интерфейса (роли)
            //}
        //});

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //public void goNext(View V){
        //Сделать проверку на пароль и понять какое окно грузить
        // (всем работникам при устройстве на работу должны выдавать пароль) по типу клиента
        //У каждой роли в пароле можно сделать определённые первые цифры или буквы и делать проверку по ним
        //пока стоит заглушка
        //Intent intent = new Intent(this,
                //GenerateOrderActivity.class);
                //CreateOrderActivity.class);
                //TxtViewerActivity.class);
                //PDFViewerActivity.class);
        //startActivity(intent);
    //}

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
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
}