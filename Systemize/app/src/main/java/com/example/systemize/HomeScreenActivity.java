package com.example.systemize;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private int previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
        previous = 2;
        fab = findViewById(R.id.fab);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

    }

    public void onNewTaskClicked(View view){
        Intent intent = new Intent(this, NewTaskActivity.class);
        startActivityForResult(intent, RESULT_FIRST_USER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment selectedFragment = null;
        switch (previous){
            case R.id.nav_calendar:
                selectedFragment = new CalendarFragment();
                previous = 1;
                fab.show();
                break;
            case R.id.nav_home:
                selectedFragment = new HomeFragment();
                previous = 2;
                fab.show();
                break;
            case R.id.nav_profile:
                selectedFragment = new ProfileFragment();
                previous = 3;
                fab.hide();
                break;
            case R.id.nav_user:
                selectedFragment = new UserFragment();
                previous = 4;
                fab.hide();
                break;
        }
        assert selectedFragment != null;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    previous = item.getItemId();
                    switch (item.getItemId()){
                        case R.id.nav_calendar:
                            selectedFragment = new CalendarFragment();
                            fab.show();
                            break;
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            fab.show();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            fab.hide();
                            break;
                        case R.id.nav_user:
                            selectedFragment = new UserFragment();
                            fab.hide();
                            break;
                    }
                    assert selectedFragment != null;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}