package com.hamzaazam.fyp_frontend;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.hamzaazam.fyp_frontend.Fragments.BillExportFragment;
import com.hamzaazam.fyp_frontend.Fragments.BillImageFragment;
import com.hamzaazam.fyp_frontend.Fragments.BillTextFragment;
import com.hamzaazam.fyp_frontend.Fragments.ReceiptTextFragment;

import java.util.ArrayList;

public class ReceiptViewActivity extends BillViewActivity {


    private ViewPagerAdapter viewPagerAdapter;

    private String recId;

    void setTitleActionBar() {
        getSupportActionBar().setTitle(" Receipt View");
    }


    void getContentFromIntent() {
        Intent intent = getIntent();
        recId=intent.getStringExtra("recid");
        toast(recId);
    }


    void populateViewPagerAdapter() {


        TabLayout tabLayout=findViewById(R.id.tabLayoutbill);
        ViewPager viewPager=findViewById(R.id.viewPagerbill);



        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

//        viewPagerAdapter.addFragment(new BillImageFragment(),"Image");
        viewPagerAdapter.addFragment(new ReceiptTextFragment(),"Text");
//        viewPagerAdapter.addFragment(new BillExportFragment(),"Export");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);



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

            Bundle args = new Bundle();
            if(title.equals("Image")){args.putString(ReceiptTextFragment.RECID_RECEIVE, recId);}
            else if(title.equals("Text")){args.putString(ReceiptTextFragment.RECID_RECEIVE, recId);}
            else{args.putString(BillExportFragment.BILLID_RECEIVE, recId);}

            frag.setArguments(args);

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

}
