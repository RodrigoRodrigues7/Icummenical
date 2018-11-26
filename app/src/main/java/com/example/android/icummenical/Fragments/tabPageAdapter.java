package com.example.android.icummenical.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robso on 25/11/2018.
 */

public class tabPageAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> lisfragment = new ArrayList<>();
    private final List<String> listtitles = new ArrayList<>();

    public void AddFragment(Fragment fragment, String title){

        lisfragment.add(fragment);
        listtitles.add(title);

    }
    public tabPageAdapter(FragmentManager fm)
    {
        super(fm);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return listtitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return lisfragment.get(position);

    }
    @Override
    public int getCount()
    {
        return listtitles.size();
    }

}