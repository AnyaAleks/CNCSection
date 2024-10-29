package com.example.cncsection;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivityOperator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_manager);
        //getWindow().setNavigationBarColor(getResources().getColor(R.color.dark_blue));

        BottomNavigationView navigation=(BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);

        //Появление пункта меню messages
        loadFragment(CreateOrderFragment.newInstance());
        navigation.setSelectedItemId(R.id.pageOrder);
    }

    //Выбор пункта меню NavigationBarView
    private NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.pageOrder:
                    loadFragment(CreateOrderFragment.newInstance());
                    //getSupportActionBar().setTitle(R.string.contacts_caps);
                    return true;
                case R.id.pageOrderList:
                    loadFragment(ListOrderFragment.newInstance());
                    //getSupportActionBar().setTitle(R.string.messages_caps);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment){
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }
}