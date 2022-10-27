package com.mdodot.android_blood_pressure_log.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.mdodot.android_blood_pressure_log.R;
import com.mdodot.android_blood_pressure_log.adapter.SectionsStateAdapter;
import com.mdodot.android_blood_pressure_log.databinding.MeasurementEntriesActivityBinding;

public class MeasurementEntriesActivity extends AppCompatActivity {

    private MeasurementEntriesActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.measurement_entries_activity);
        FragmentManager fragmentManager = getSupportFragmentManager();
        SectionsStateAdapter sectionsStateAdapter = new SectionsStateAdapter(fragmentManager, getLifecycle());
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tabs);

        viewPager.setAdapter(sectionsStateAdapter);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_add));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_board));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}