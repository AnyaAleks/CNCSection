package Enter;

import static android.app.PendingIntent.getActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cncsection.MainActivityStaff;
import com.example.cncsection.R;
import com.example.cncsection.UserSettings;
import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import Manager.MainActivityManager;
import Master.MainActivityMaster;
import Operator.MainActivityOperator;
import Staff.DBStaff;


public class EntryActivity extends AppCompatActivity {

    TextView entry;
    EditText password_1, login;
    Button add_button;

    DBStaff dbStaff;
    UserSettings userSettings = new UserSettings();

    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_entry);

        dbStaff = new DBStaff(this);
        entry = findViewById(R.id.entry);
        password_1 = findViewById(R.id.textInputEditPasswordSignIn);
        login = findViewById(R.id.login);
        add_button = findViewById(R.id.add_button);

    }

    @SuppressLint("Range")
    public void goNext(View V) {
        //Сделать проверку на пароль и понять какое окно грузить
        // (всем работникам при устройстве на работу должны выдавать пароль) по типу клиента
        //У каждой роли в пароле можно сделать определённые первые цифры или буквы и делать проверку по ним
        //Toast.makeText(getBaseContext(), "Reason can not be blank", Toast.LENGTH_SHORT).show();
        String passwordMD5 = md5(password_1.getText().toString());
        boolean isValid = false;

        Cursor csr = dbStaff.getAll("Staff");
        while (csr.moveToNext()) {
            //Если Логин и Пароль есть в БД
            Log.d("DB_STAFF", csr.getString(csr.getColumnIndex("fio")));
            if (login.getText().toString().equals(csr.getString(csr.getColumnIndex("fio")).toString())
                    && passwordMD5.equals(csr.getString(csr.getColumnIndex("password")).toString())
            ) {

                //Поиск роли
                Cursor csrAccess = dbStaff.getAll("Access");
                while (csrAccess.moveToNext()) {
                    if (csr.getInt(csr.getColumnIndex("id_access")) ==
                            csrAccess.getInt(csrAccess.getColumnIndex("id_access"))) {
                        Log.d("DB_STAFF2", csr.getInt(csr.getColumnIndex("id_access"))
                                + " " + csrAccess.getInt(csrAccess.getColumnIndex("id_access")));

                        //Сохранение настроек
                        userSettings.setRoleUser(csr.getInt(csrAccess.getColumnIndex("id_access")));
                        userSettings.setIdUser(csr.getString(csr.getColumnIndex("id_staff")));
                        saveJsonData();

                        //Переход в окно
                        openActivityByAccess(csr.getInt(csrAccess.getColumnIndex("id_access")));
                    }

                }
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            login.setText("");
            password_1.setText("");
            TextView errorTextView = findViewById(R.id.error_text_view);
            errorTextView.setText("Логин или пароль введены неправильно");
        }
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
    public void openActivityByAccess(int role){
        Intent intent = null;
        switch (role){
            case 1:
                intent = new Intent(this, MainActivityManager.class);
                        //CreateOrderActivity.class);
                break;
            case 2:
                intent = new Intent(this, MainActivityMaster.class);
                break;
            case 3:
                intent = new Intent(this, MainActivityOperator.class);
                break;
            case 4:
                intent = new Intent(this, MainActivityStaff.class);
                break;
        }
        startActivity(intent);
    }

    //Сохранение в настройки
    public void saveJsonData(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userSettings);
        editor.putString("user settings", json);
        editor.apply();
    }
}