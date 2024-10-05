package com.example.cncsection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    DBManager dbManager;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Загрузка
        WebView webView = (WebView) findViewById(R.id.webvidew);
        webView.loadUrl("file:///android_asset/spinner.html");
        webView.setBackgroundColor(Color.TRANSPARENT);


        //Переход
        Intent intent = new Intent(this,
                //CreateOrderActivity.class);
                EntryActivity.class);

        CountDownTimer timer = new CountDownTimer(3000, 1000)
        {
            public void onTick(long l){}

            @Override
            public void onFinish()
            {
                startActivity(intent);
            };
        }.start();

        dbManager = new DBManager(this);

        //myDBHlpr.addToStaff(1, "Алексеева Анна Сергеевна", "1234");



//
//        Cursor csr = dbManager.getAll("Machine");
//        while(csr.moveToNext()){
//            Log.d("DB_ROWINFO",
//                    "ID is " + csr.getString(csr.getColumnIndex("id_machine"))
//                            + " TYPE is " + csr.getString((csr.getColumnIndex("type"))
//                    )
//            );
//        }
//
//        csr = myDBHlpr.sortInOrder("Request", "production_time");
//        while(csr.moveToNext()){
//            Log.d("SORT_REQUEST",
//                    "ID_REC is " + csr.getString(csr.getColumnIndex("id_request"))
//                            + " ID_PART is " + csr.getString(csr.getColumnIndex("id_part"))
//                            + " TIME is " + csr.getString((csr.getColumnIndex("production_time"))
//                    )
//            );
//        }
//
//        csr = myDBHlpr.searchItem("Osnaska", "id_osnaska", "2");
//        while(csr.moveToNext()){
//            Log.d("SEARCH",
//                    "ID_OSN is " + csr.getString(csr.getColumnIndex("id_osnaska"))
//                            + " TITLE is " + csr.getString(csr.getColumnIndex("title"))
//                            + " STOCK is " + csr.getString((csr.getColumnIndex("stock_availability"))
//                    )
//            );
//        }
//
//
//        csr.close();
    }

    //Функция создания заявки менеджером
    public void addNewRecordToRequest(DBManager db){
        db.addToStaff(1, "Алексеева Анна Сергеевна", "1234");
    }


}