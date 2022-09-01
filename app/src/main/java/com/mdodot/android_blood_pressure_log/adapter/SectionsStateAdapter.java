package com.mdodot.android_blood_pressure_log.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.mdodot.android_blood_pressure_log.fragment.MeasurementsListFragment;
import com.mdodot.android_blood_pressure_log.fragment.NewEntryFragment;

public class SectionsStateAdapter extends FragmentStateAdapter {

    public SectionsStateAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new NewEntryFragment();
            case 1:
                return new MeasurementsListFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}