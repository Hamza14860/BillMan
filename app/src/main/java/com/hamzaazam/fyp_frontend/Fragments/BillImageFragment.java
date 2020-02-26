package com.hamzaazam.fyp_frontend.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.hamzaazam.fyp_frontend.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillImageFragment extends Fragment {

    PhotoView billImageView;

    public BillImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill_image, container, false);


        billImageView= (PhotoView) view.findViewById(R.id.billImageView);
        billImageView.setImageResource(R.drawable.bill1);


        return view;
    }

}
