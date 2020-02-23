package com.hamzaazam.fyp_frontend.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hamzaazam.fyp_frontend.R;


public class AllBillsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_bills, container, false);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
