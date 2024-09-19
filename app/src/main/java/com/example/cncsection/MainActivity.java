package com.example.cncsection;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    DataBaseHelper myDBHlpr;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        myDBHlpr = new DataBaseHelper(this);

        Cursor csr = myDBHlpr.getAll("Machine");
        while(csr.moveToNext()){
            Log.d("DB_ROWINFO",
                    "ID is " + csr.getString(csr.getColumnIndex("id_machine"))
                            + " TYPE is " + csr.getString((csr.getColumnIndex("type"))
                    )
            );
        }

        csr = myDBHlpr.sortInOrder("Request", "production_time");
        while(csr.moveToNext()){
            Log.d("SORT_REQUEST",
                    "ID_REC is " + csr.getString(csr.getColumnIndex("id_request"))
                            + " ID_PART is " + csr.getString(csr.getColumnIndex("id_part"))
                            + " TIME is " + csr.getString((csr.getColumnIndex("production_time"))
                    )
            );
        }

        csr = myDBHlpr.searchItem("Osnaska", "id_osnaska", "2");
        while(csr.moveToNext()){
            Log.d("SEARCH",
                    "ID_OSN is " + csr.getString(csr.getColumnIndex("id_osnaska"))
                            + " TITLE is " + csr.getString(csr.getColumnIndex("title"))
                            + " STOCK is " + csr.getString((csr.getColumnIndex("stock_availability"))
                    )
            );
        }


        csr.close();
    }
}