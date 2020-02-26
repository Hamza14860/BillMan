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
import com.hamzaazam.fyp_frontend.Fragments.BillStatsFragment;
import com.hamzaazam.fyp_frontend.Fragments.BillTextFragment;

import java.util.ArrayList;

public class BillViewActivity extends AppCompatActivity {

    public Toolbar toolbar;
    private String billId;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" Bill View");
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        billId=intent.getStringExtra("billid");
        toast(billId);


        ///////Add View Pager and tab layout..
        TabLayout tabLayout=findViewById(R.id.tabLayoutbill);
        ViewPager viewPager=findViewById(R.id.viewPagerbill);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());


        viewPagerAdapter.addFragment(new BillImageFragment(),"Image");
        viewPagerAdapter.addFragment(new BillTextFragment(),"Text");
        viewPagerAdapter.addFragment(new BillStatsFragment(),"Stats");

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


    private void toast(String msg){
        Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_LONG).show();
    }

}
