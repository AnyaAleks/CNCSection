package Staff;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cncsection.ListOfPeopleFragment;
import com.example.cncsection.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivityStaff extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_staff);
        //getWindow().setNavigationBarColor(getResources().getColor(R.color.dark_blue));

        BottomNavigationView navigation=(BottomNavigationView) findViewById(R.id.bottom_navigation_staff);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);

        //Появление пункта меню messages
        loadFragment(ListOfPeopleFragment.newInstance());
        navigation.setSelectedItemId(R.id.pageOrderList);
    }

    //Выбор пункта меню NavigationBarView
    private NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.pageStaffList:
                    loadFragment(RegistrationFragment.newInstance());
                    //getSupportActionBar().setTitle(R.string.contacts_caps);
                    return true;
                case R.id.pageOrderStaff:
                    loadFragment(ListOfPeopleFragment.newInstance());
                    //getSupportActionBar().setTitle(R.string.messages_caps);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment){
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_operator, fragment);
        ft.commit();
    }
}