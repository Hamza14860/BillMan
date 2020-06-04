package com.hamzaazam.fyp_frontend.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hamzaazam.fyp_frontend.Model.BillM;
import com.hamzaazam.fyp_frontend.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillImageFragment extends Fragment {

    PhotoView billImageView;

    public final static String BILLID_RECEIVE = "data_receive";
    String receivedBillId;

    View vieww;
    ////Setting & getting profile data
    DatabaseReference reference;
    String fuserid;

    public BillImageFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill_image, container, false);
        vieww=view;

        billImageView= (PhotoView) view.findViewById(R.id.billImageView);
        //billImageView.setImageResource(R.drawable.bill1);


        return view;
    }


    //In this function we receive the bill id which was clicked by the user
    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            receivedBillId=args.getString(BILLID_RECEIVE);
            //toast( receivedBillId+ " BillID");
        }
        else{
            receivedBillId="BILL ID was NULL";
            toast("Bill ID was NULL");
        }

        ////////////////////////
        fuserid=FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference = FirebaseDatabase.getInstance().getReference("bills").child(fuserid).child(receivedBillId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BillM bill = dataSnapshot.getValue(BillM.class);
                //toast(bill.getBillImageUrl()+" "+bill.getBillCustomerName());
                if(bill.getBillImageUrl().equals("None Chosen")){
                    billImageView.setImageResource(R.mipmap.bill1);
                }
                else{
                    if (isAdded()) {
                        Glide.with(getActivity().getApplicationContext()).load(bill.getBillImageUrl()).into(billImageView);
                    }
                }
                /////////
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        ////////////////////////



    }

    private void toast(String msg){
        Toast.makeText(getActivity(), msg,Toast.LENGTH_LONG).show();
    }

}
