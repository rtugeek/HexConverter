package com.example.jzzh;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Bundle;

import com.rey.material.widget.TabPageIndicator;


import java.lang.reflect.Field;
import java.util.ArrayList;


public class MainActivity extends FragmentActivity {
    private Tab[] mItems = new Tab[]{Tab.DOC, Tab.MAIN, Tab.ABOUT};
    private TabPageIndicator tpi;
    private PagerAdapter mPagerAdapter;
    private String[] tabTitles;
    private CustomViewPager vp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tpi = (TabPageIndicator)findViewById(R.id.main_tpi);
        vp = (CustomViewPager)findViewById(R.id.main_vp);

        tabTitles =getResources().getStringArray(R.array.titles);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), mItems,tabTitles);
        vp.setAdapter(mPagerAdapter);
        tpi.setViewPager(vp);
        vp.setCurrentItem(1);
        tpi.setCurrentItem(1);


    }


    public enum Tab {
        DOC("doc"),
        MAIN("main"),
        ABOUT("about");
        private final String name;

        private Tab(String s) {
            name = s;
        }

        public boolean equalsName(String otherName){
            return (otherName != null) && name.equals(otherName);
        }

        public String toString(){
            return name;
        }

    }

    private static class PagerAdapter extends FragmentStatePagerAdapter {

        Fragment[] mFragments;
        Tab[] mTabs;
        String[] tabTitles;
        private static final Field sActiveField;

        static {
            Field f = null;
            try {
                Class<?> c = Class.forName("android.support.v4.app.FragmentManagerImpl");
                f = c.getDeclaredField("mActive");
                f.setAccessible(true);
            } catch (Exception e) {}

            sActiveField = f;
        }


    public PagerAdapter(FragmentManager fm, Tab[] tabs,String[] string) {
        super(fm);
        mTabs = tabs;
        mFragments = new Fragment[mTabs.length];
        tabTitles = string;

        //dirty way to get reference of cached fragment
        try{
            ArrayList<Fragment> mActive = (ArrayList<Fragment>)sActiveField.get(fm);
            if(mActive != null){
                for(Fragment fragment : mActive){
                    if(fragment instanceof DocFragment)
                        setFragment(Tab.DOC, fragment);
                    else if(fragment instanceof MainFragment)
                        setFragment(Tab.MAIN, fragment);
                    else if(fragment instanceof AboutFragment)
                        setFragment(Tab.ABOUT, fragment);

                }
            }
        }
        catch(Exception e){}
    }

    private void setFragment(Tab tab, Fragment f){
        for(int i = 0; i < mTabs.length; i++)
            if(mTabs[i] == tab){
                mFragments[i] = f;
                break;
            }
    }

    @Override
    public Fragment getItem(int position) {
        if(mFragments[position] == null){
            switch (mTabs[position]) {
                case DOC:
                    mFragments[position] = DocFragment.newInstance();
                    break;
                case MAIN:
                    mFragments[position] = MainFragment.newInstance();
                    break;
                case ABOUT:
                    mFragments[position] = AboutFragment.newInstance();
                    break;

            }
        }

        return mFragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }


}


}
