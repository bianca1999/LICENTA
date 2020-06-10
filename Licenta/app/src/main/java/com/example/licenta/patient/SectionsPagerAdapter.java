package com.example.licenta.patient;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

class SectionsPagerAdapter extends FragmentPagerAdapter {
    private Fragment[] childFragments;
    public SectionsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        childFragments=new Fragment[]{
                new ChatsFragment(),
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
            return "Conversations";
        else
            return "Doctors";
    }
}
