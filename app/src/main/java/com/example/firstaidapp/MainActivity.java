package com.example.firstaidapp;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.firstaidapp.fragments.HomeFragment;
import com.example.firstaidapp.fragments.MapFragment;
import com.example.firstaidapp.fragments.OtherAilmentsFragment;
import com.example.firstaidapp.fragments.PhoneFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private HomeFragment mHomeFragment = new HomeFragment();
    private MapFragment mMapFragment = new MapFragment();
    private PhoneFragment mPhoneFragment = new PhoneFragment();
    private OtherAilmentsFragment mOtherAilmentsFragment = new OtherAilmentsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mBottomNavigationView.setOnItemSelectedListener(navListener);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                updateBottomNavigationIcon();
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, mHomeFragment).commit();
    }

    private BottomNavigationView.OnItemSelectedListener navListener =
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            replaceFragment(mHomeFragment);
                            break;
                        case R.id.navigation_map:
                            replaceFragment(mMapFragment);
                            break;
                        case R.id.navigation_phone:
                            replaceFragment(mPhoneFragment);
                            break;
                        case R.id.navigation_bandage:
                            replaceFragment(mOtherAilmentsFragment);
                            break;
                    }
                    return true;
                }
            };

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .commit();
    }

    private void updateBottomNavigationIcon() {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);

        if (currentFragment instanceof HomeFragment) {
            mBottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
        } else if (currentFragment instanceof MapFragment) {
            mBottomNavigationView.getMenu().findItem(R.id.navigation_map).setChecked(true);
        } else if (currentFragment instanceof PhoneFragment) {
            mBottomNavigationView.getMenu().findItem(R.id.navigation_phone).setChecked(true);
        } else if (currentFragment instanceof OtherAilmentsFragment){
            mBottomNavigationView.getMenu().findItem(R.id.navigation_bandage).setChecked(true);
        }
    }
}

