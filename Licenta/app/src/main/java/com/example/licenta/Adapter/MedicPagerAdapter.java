package com.example.licenta.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.licenta.Fragments.AppointmentsFragment;
import com.example.licenta.Fragments.MedicChatsFragment;
import com.example.licenta.Fragments.PatientsFragment;

public class MedicPagerAdapter extends FragmentPagerAdapter {
    private Fragment[] childFragments;

    public MedicPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        childFragments = new Fragment[]{
                new MedicChatsFragment(),
                new PatientsFragment(),
                new AppointmentsFragment()
        };
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return childFragments[position];
    }

    @Override
    public int getCount() {
        return childFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "CONVERSAȚII";

        if (position == 1)
            return "PACIENȚI";
        else
            return "PROGRAMĂRI";
    }
}

