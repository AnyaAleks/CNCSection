package com.example.cncsection;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.io.InputStream;


//import com.github.barteksc.pdfviewer.PDFView;
public class PDFViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pdfviewer);

        Log.d("PDF","fffffffffffffffffffffffffff");

//        PDFView pdfView=(PDFView)findViewById(R.id.pdfView);
//        pdfView.fromAsset("rub.pdf") .load();

//        WebView webView = (WebView) findViewById(R.id.pdfWebView);
//        webView.loadUrl("file:///android_asset/PRS_1.html");
        //webView.setBackgroundColor(Color.TRANSPARENT);

//        Log.d("PDF","fffffffffffffffffffffffffff");
//        WebView webView = (WebView)findViewById(R.id.pdfWebView);
//        webView.getSettings().setAllowContentAccess(true);
//        webView.getSettings().setAllowFileAccess(true);
//        //webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + "C:/Users/Anya/AndroidStudioProjects/CNCSection/app/src/main/assets/PRS.1.18.0002.000.pdf");
//        webView.loadUrl("file:///android_asset/PRS.1.18.0002.000.pdf");

//        WebView webView = findViewById(R.id.pdfWebView);
//        webView.setWebViewClient(new WebViewClient());
//        webView.getSettings().setSupportZoom(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + "https://pdfurl.com/file.pdf");


    }
}