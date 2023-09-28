package com.example.finalcut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.finalcut.adapter.ViewPageAdapter;
//import com.example.finalcut.fragments.ScanFragment;
import com.example.finalcut.fragments.ScanFragment;
import com.example.finalcut.fragments.StreamFragment;
import com.example.finalcut.fragments.TicketFragment;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private MenuItem prevItem;
    private ViewPageAdapter viewPageAdapter;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigationMenuView = findViewById(R.id.navigation);
        navigationMenuView.setOnNavigationItemSelectedListener(MainActivity.this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        viewPager = findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevItem!=null){
                    prevItem.setChecked(false);

                }
                navigationMenuView.getMenu().getItem(position).setChecked(true);
                prevItem = navigationMenuView.getMenu().getItem(position);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPageAdapter.addFragment(new ScanFragment());
        viewPageAdapter.addTitle("QR");
        viewPageAdapter.addFragment(new TicketFragment());
        viewPageAdapter.addTitle("Билеты");
        viewPageAdapter.addFragment(new StreamFragment());
        viewPageAdapter.addTitle("Очередь");
        viewPager.setAdapter(viewPageAdapter);
        viewPager.setCurrentItem(1);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.qrscanner){
            viewPager.setCurrentItem(0);
        }
        else if(item.getItemId()==R.id.tickets)
            viewPager.setCurrentItem(1);
        else
            viewPager.setCurrentItem(2);
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}