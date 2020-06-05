package com.hamzaazam.fyp_frontend.Adapter;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hamzaazam.fyp_frontend.BillViewActivity;
import com.hamzaazam.fyp_frontend.Model.BillM;
import com.hamzaazam.fyp_frontend.R;

import java.util.List;


public class BillsAdapter extends RecyclerView.Adapter<BillsAdapter.BillsViewHolder> {

    private Context mContext;
    private List<BillM> mBills;

    String billImageurl;

    DatabaseReference reference;

    String fuserid;

    public BillsAdapter(Context context, List<BillM>  billsList){
        this.mContext=context;
        this.mBills=billsList;
        fuserid= FirebaseAuth.getInstance().getCurrentUser().getUid();


    }

    @NonNull
    @Override
    public BillsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.rv_bill_item,parent, false);
        return new BillsAdapter.BillsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillsViewHolder holder, final int position) {

        final BillM category=mBills.get(position);
        holder.billCategory.setText(category.getBillCategory());

        holder.billDate.setText(category.getBillDate());

        holder.billCustomerName.setText(category.getBillCustomerName());

        holder.billAmount.setText(category.getBillAmount());

        billImageurl=category.getBillImageUrl();

        if(billImageurl == null || billImageurl.equals("None Chosen")){
            holder.billImage.setImageResource(R.mipmap.bill1);
        }
        else{
            Glide.with(mContext).load(billImageurl).into(holder.billImage);
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

                Intent intent=new Intent(mContext, BillViewActivity.class);
                    intent.putExtra("billid",mBills.get(position).getBillId());
                    mContext.startActivity(intent);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete Bill")
                        .setMessage("Are you sure you want to delete this bill?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                try{
                                    reference = FirebaseDatabase.getInstance().getReference("bills").child(fuserid);
                                    reference.child(category.getBillId()).removeValue();
                                }
                                catch (Exception e){
                                    System.out.println(e.getLocalizedMessage());
                                }
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });

        Animation animation= AnimationUtils.loadAnimation(mContext,android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);


    }

    @Override
    public int getItemCount() {
        return mBills.size();
    }

    public class BillsViewHolder extends RecyclerView.ViewHolder{
        public ImageView billImage;
        public TextView billCategory;
        public TextView billCustomerName;
        public TextView billDate;
        public TextView billAmount;



        public BillsViewHolder( View itemView) {
            super(itemView);

            billImage = itemView.findViewById(R.id.billImageItem);
            billCategory = itemView.findViewById(R.id.billCatItem);
            billAmount = itemView.findViewById(R.id.billAmountItem);

            billCustomerName = itemView.findViewById(R.id.billCustomerNameItem);

            billDate = itemView.findViewById(R.id.billDateItem);



        }
    }
}

