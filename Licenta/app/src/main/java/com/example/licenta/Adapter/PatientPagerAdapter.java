package com.example.licenta.Adapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.licenta.Fragments.DoctorsFragment;
import com.example.licenta.Fragments.PatientChatsFragment;


public class PatientPagerAdapter extends FragmentPagerAdapter {
    private Fragment[] childFragments;
    public PatientPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        childFragments=new Fragment[]{
                new PatientChatsFragment(),
                new DoctorsFragment()
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
            return "CONVERSAȚII";
        else
            return "MEDICI";
    }
}
