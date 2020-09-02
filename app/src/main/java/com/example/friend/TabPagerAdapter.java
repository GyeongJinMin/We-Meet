package com.example.friend;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private int tabCount;

    public TabPagerAdapter(@NonNull FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                MainActivity Fragment1 = new MainActivity();
                return Fragment1;
            case 1:
                Calendar_main Fragment3 = new Calendar_main();
                return Fragment3;
            case 2:
                ScheduleMainActivity Fragment2 = new ScheduleMainActivity();
                return Fragment2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
