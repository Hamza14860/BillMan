package com.hamzaazam.fyp_frontend;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.hamzaazam.fyp_frontend.Fragments.BillImageFragment;
import com.hamzaazam.fyp_frontend.Fragments.BillExportFragment;
import com.hamzaazam.fyp_frontend.Fragments.BillTextFragment;
import com.hamzaazam.fyp_frontend.Fragments.ZoomHack;

import java.util.ArrayList;

public class BillViewActivity extends AppCompatActivity {

    public Toolbar toolbar;
    private String billId;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void setTitleActionBar() {
        getSupportActionBar().setTitle(" Bill View");
    }


    void getContentFromIntent() {
        Intent intent = getIntent();
        billId=intent.getStringExtra("billid");
        //toast(billId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitleActionBar();
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getContentFromIntent();


        ///////Add View Pager and tab layout..

        populateViewPagerAdapter();



    }

    void populateViewPagerAdapter() {


        TabLayout tabLayout=findViewById(R.id.tabLayoutbill);
        ViewPager viewPager=findViewById(R.id.viewPagerbill);



        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new BillImageFragment(),"Image");
        viewPagerAdapter.addFragment(new BillTextFragment(),"Text");
        viewPagerAdapter.addFragment(new BillExportFragment(),"Export");

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
            if(title.equals("Image")){args.putString(BillImageFragment.BILLID_RECEIVE, billId);}
            else if(title.equals("Text")){args.putString(BillTextFragment.BILLID_RECEIVE, billId);}
            else{args.putString(BillExportFragment.BILLID_RECEIVE, billId);}

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

    //////


    protected void toast(String msg){
        Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_LONG).show();
    }




}
