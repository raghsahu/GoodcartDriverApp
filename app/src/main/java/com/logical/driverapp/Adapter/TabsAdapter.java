package com.logical.driverapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.logical.driverapp.Fragment.ListFragment;
import com.logical.driverapp.Fragment.MapFragment;

/**
 * Created by Ravindra Birla on 11/11/2019.
 */
public class TabsAdapter extends FragmentStatePagerAdapter {
    int tabCount;
    public TabsAdapter( FragmentManager supportFragmentManager,int tabCount) {

        super(supportFragmentManager);
        this.tabCount=tabCount;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ListFragment maplistfragment =  new ListFragment();
                return maplistfragment;
            case 1:
                MapFragment listFragment = new MapFragment();
                return listFragment;
            default:
                return null;
        }
    }}


