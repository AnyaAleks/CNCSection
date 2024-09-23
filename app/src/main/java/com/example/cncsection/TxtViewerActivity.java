package com.example.cncsection;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.io.InputStream;

public class TxtViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_txt_viewer);

        TextView myTextView = (TextView)findViewById(R.id.txtTextView);

        String myOutput;
        InputStream myInputStream;

        try {
            myInputStream = getAssets().open("ShaderDocumentationQt6.txt");
            int size = myInputStream.available();
            byte[] buffer = new byte[size];
            myInputStream.read(buffer);
            myInputStream.close();
            String str_data = new String(buffer);
            myTextView.setText(str_data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}