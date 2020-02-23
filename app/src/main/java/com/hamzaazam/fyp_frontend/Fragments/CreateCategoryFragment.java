package com.hamzaazam.fyp_frontend.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.hamzaazam.fyp_frontend.R;

import java.util.ArrayList;


public class CreateCategoryFragment extends Fragment {



    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_create_category, container, false);

        ///////Add View Pager and tab layout..
        TabLayout tabLayout=view.findViewById(R.id.tabLayout1);
        ViewPager viewPager=view.findViewById(R.id.viewPager);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager());


        viewPagerAdapter.addFragment(new RequestAddCatFragment(),"Request Add");
        viewPagerAdapter.addFragment(new AdminAddCatFragment(),"Admin Add");


        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        ///////
        return view;
    }



    ////////////View Pager Adapter for TabLayout
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<String> titles;
        private ArrayList<Fragment> fragments;

        ViewPagerAdapter (FragmentManager fm){
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addFragment(Fragment frag, String title){
            fragments.add(frag);
            titles.add(title);
        }

        //ctrl + o to see functions
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    //////



}
