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
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        myDBHlpr = new DataBaseHelper(this);

        // Get some data from the database using your method
        Cursor csr = myDBHlpr.getDataDB_TableItemNamesByItemType("type2");
        while(csr.moveToNext()){
            Log.d("DB_ROWINFO",
                    "ITEM NAME is " + csr.getString(csr.getColumnIndex(DataBaseHelper.ITEM_NAME))
                            + "ITEM TYPE is "
                            + csr.getString((csr.getColumnIndex(DataBaseHelper.ITEM_TYPE))
                    )
            );
        }
        // ========================================
        // ALWAYS CLOSE CURSORS WHEN DONE WITH THEM
        // ========================================
        csr.close();
    }
}