package com.vandamodaintima.jfpsb.contador;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class MyPagerAdapter2 extends FragmentStateAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();

    public MyPagerAdapter2(@NonNull FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}
