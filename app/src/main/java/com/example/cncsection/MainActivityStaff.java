package com.example.cncsection;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

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

import Staff.RegistrationFragment;

public class MainActivityStaff extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_staff);
        //getWindow().setNavigationBarColor(getResources().getColor(R.color.dark_blue));

        BottomNavigationView navigation=(BottomNavigationView) findViewById(R.id.bottom_navigation_staff);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
        View fragmentContainer = findViewById(R.id.fragment_container_staff);

        //Появление пункта меню messages
        loadFragment(ListOfPeopleFragment.newInstance());
        navigation.setSelectedItemId(R.id.pageOrderList);

        fragmentContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = fragmentContainer.getRootView().getHeight() - fragmentContainer.getHeight();
                if (heightDiff > 300) { // Если высота больше чем 300, клавиатура открыта
                    navigation.setVisibility(View.GONE); // Скрываем BottomNavigationView
                } else {
                    navigation.setVisibility(View.VISIBLE); // Показываем BottomNavigationView
                }
            }
        });
    }

    //Выбор пункта меню NavigationBarView
    private NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.pageStaffList:
                    loadFragment(ListOfPeopleFragment.newInstance());
                    //getSupportActionBar().setTitle(R.string.contacts_caps);
                    return true;
                case R.id.pageOrderStaff:
                    loadFragment(RegistrationFragment.newInstance());
                    //getSupportActionBar().setTitle(R.string.messages_caps);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment){
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_staff, fragment);
        ft.commit();
    }
}