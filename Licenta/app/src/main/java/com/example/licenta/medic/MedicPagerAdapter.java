package com.example.licenta.medic;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.licenta.patient.PatientChatsFragment;
import com.example.licenta.patient.DoctorsFragment;

public class MedicPagerAdapter extends FragmentPagerAdapter {
    private Fragment[] childFragments;
    public MedicPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        childFragments=new Fragment[]{
                new MedicRequestsFragment(),
                new MedicChatsFragment(),
                new PatientsFragment()
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
        if(position==0)
            return "Requests";
        else {
            if (position == 1)
                return "Conversations";

            else
                return "My patients";
        }
    }
}

