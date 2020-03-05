package com.hamzaazam.fyp_frontend.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hamzaazam.fyp_frontend.BillViewActivity;
import com.hamzaazam.fyp_frontend.Model.ReceiptM;
import com.hamzaazam.fyp_frontend.R;
import com.hamzaazam.fyp_frontend.ReceiptViewActivity;

import java.util.List;


public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.BillsViewHolder> {

    private Context mContext;
    private List<ReceiptM> mRecs;

    String recImageurl;

    public ReceiptsAdapter(Context context, List<ReceiptM>  billsList){
        this.mContext=context;
        this.mRecs=billsList;

    }

    @NonNull
    @Override
    public BillsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.rv_receipt_item ,parent, false);
        return new BillsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillsViewHolder holder, final int position) {

        final ReceiptM rec = mRecs.get(position);

        holder.recAmount.setText(String.valueOf(rec.getRecAmount()));

        holder.recDate.setText(rec.getRecDate());

        holder.recName.setText(rec.getRecName());

        recImageurl=rec.getRecImageUrl();

        if(recImageurl ==null || recImageurl.equals("None Chosen")){
            holder.recImage.setImageResource(R.drawable.receipt1);
        }
        else{
            Glide.with(mContext).load(recImageurl).into(holder.recImage);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                FragmentTransaction ft=getChildFragmentManager().beginTransaction();
//                CreateCategoryFragment ccf=new CreateCategoryFragment();
//                ft.replace(R.id.child_fragment_container,ccf);
//                //ft.addToBackStack(null);
//                ft.commit();


//                ((AppCompatActivity) mContext).getSupportActionBar().setTitle(category.getCatName()+" OCR Bills");
//
//                ((Fragment) frag).getChildFragmentManager().beginTransaction()
//                        .replace(R.id.child_fragment_container, new AllBillsFragment())
//                        .commit();

                Intent intent=new Intent(mContext, ReceiptViewActivity.class);
                    intent.putExtra("recid", mRecs.get(position).getRecId());
                    mContext.startActivity(intent);

            }
        });

        Animation animation= AnimationUtils.loadAnimation(mContext,android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);


    }

    @Override
    public int getItemCount() {
        return mRecs.size();
    }

    public class BillsViewHolder extends RecyclerView.ViewHolder{
        public ImageView recImage;
        public TextView recName;
        public TextView recDescription;
        public TextView recDate;
        public TextView recAmount;



        public BillsViewHolder( View itemView) {
            super(itemView);

            recImage = itemView.findViewById(R.id.imgRecImage);
            recName = itemView.findViewById(R.id.lblRecName);
            recAmount = itemView.findViewById(R.id.lblRecAmount);

            recDescription = itemView.findViewById(R.id.lblRecCategory);

            recDate = itemView.findViewById(R.id.lblRecDate);



        }
    }
}

