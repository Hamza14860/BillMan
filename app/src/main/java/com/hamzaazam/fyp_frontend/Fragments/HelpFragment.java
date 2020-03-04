package com.hamzaazam.fyp_frontend.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hamzaazam.fyp_frontend.R;


public class HelpFragment extends Fragment {

    //Variable declaration
    TextView helpRequestAddCat;
    TextView helpRequestAddCatDetails;
    int helpRACControl;

    TextView helpOcr;
    TextView helpOcrDetails;
    int helpOcrControl;

    public HelpFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_help, container, false);

        helpRequestAddCat = view.findViewById(R.id.helpRequestAddCat);
        helpRequestAddCatDetails = view.findViewById(R.id.helpRequestAddCatDetails);
        helpRACControl = 0;

        helpOcr= view.findViewById(R.id.helpOcr);
        helpOcrDetails = view.findViewById(R.id.helpOcrDetails);
        helpOcrControl = 0;



        helpRequestAddCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helpRACControl == 0){
                    helpRACControl = 1;
                    helpRequestAddCatDetails.setVisibility(View.VISIBLE);
                }else if(helpRACControl == 1){
                    helpRACControl = 0;
                    helpRequestAddCatDetails.setVisibility(View.GONE);
                }
            }
        });

        helpOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helpOcrControl == 0){
                    helpOcrControl = 1;
                    helpOcrDetails.setVisibility(View.VISIBLE);
                }else if(helpOcrControl == 1){
                    helpOcrControl = 0;
                    helpOcrDetails.setVisibility(View.GONE);
                }
            }
        });


        return view;
    }




    @Override
    public void onDetach() {
        super.onDetach();
    }


}
