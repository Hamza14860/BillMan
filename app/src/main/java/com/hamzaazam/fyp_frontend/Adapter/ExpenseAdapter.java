package com.hamzaazam.fyp_frontend.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hamzaazam.fyp_frontend.BillViewActivity;
import com.hamzaazam.fyp_frontend.Model.ExpenseM;
import com.hamzaazam.fyp_frontend.R;

import java.io.Console;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ExpenseM> mExpenses;
    DatabaseReference reference;

    String fuserid;

    private Fragment frag;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public ExpenseAdapter(Context context, List<ExpenseM>  expensesList){
        this.mContext=context;
        this.mExpenses=expensesList;
        fuserid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        //frag=cont;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view= LayoutInflater.from(mContext).inflate(R.layout.rv_expense_item,parent, false);
//        return new ExpenseAdapter.ExpenseViewHolder(view);

        if (viewType == TYPE_HEADER) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_expense_header, parent, false);
            return new HeaderViewHolder(layoutView);
        } else if (viewType == TYPE_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_expense_item, parent, false);
            return new ExpenseViewHolder(layoutView);
        }
        throw new RuntimeException("No match for " + viewType + ".");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ExpenseM expense = mExpenses.get(position);
        if(holder instanceof HeaderViewHolder){
            //Log.e("ADAPTER BIND","HEADER");

            ((HeaderViewHolder) holder).headerTitle.setText(expense.getExpenseCategory());
            //expense.setHeader(true);
        }
        else if(holder instanceof ExpenseViewHolder){

            ((ExpenseViewHolder) holder).expenseItem.setText(expense.getExpenseItem());
            ((ExpenseViewHolder) holder).expenseCategory.setText(expense.getExpenseCategory());
            ((ExpenseViewHolder) holder).expenseDate.setText(expense.getExpenseDate());
            ((ExpenseViewHolder) holder).expenseAmount.setText(expense.getExpenseAmount());

            if (expense.getExpenseCategory().equals("Food")){
                ((ExpenseViewHolder) holder).expenseCategory.setBackgroundColor(mContext.getResources().getColor(R.color.dkgreen));
            }
            else if (expense.getExpenseCategory().equals("Shopping")){
                ((ExpenseViewHolder) holder).expenseCategory.setBackgroundColor(mContext.getResources().getColor(R.color.lightred));
            }
            else if (expense.getExpenseCategory().equals("Grocery")){
                ((ExpenseViewHolder) holder).expenseCategory.setBackgroundColor(mContext.getResources().getColor(R.color.dkyellow));
            }
            else {
                ((ExpenseViewHolder) holder).expenseCategory.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            }



            ((ExpenseViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    try{
                                        reference = FirebaseDatabase.getInstance().getReference("expenses").child(fuserid);
                                        reference.child(expense.getExpenseId()).removeValue();
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
                }
            });
        }

        Animation animation= AnimationUtils.loadAnimation(mContext,android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);


    }
    private ExpenseM getItem(int position) {
        return mExpenses.get(position);
    }
    @Override
    public int getItemCount() {
        return mExpenses.size();
    }

    @Override
    public int getItemViewType(int position) {
//        if (isPositionHeader(position))
//            return TYPE_HEADER;
//        return TYPE_ITEM;

        //Log.e("ADAPTER",mExpenses.get(position).getHeader().toString());
        if(mExpenses.get(position).getHeader() == true){
            return TYPE_ITEM;
        }
        else {
            //mExpenses.get(position).setHeader(true);
            return TYPE_HEADER;
        }


    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder{
        public TextView expenseItem;
        public TextView expenseDate;
        public TextView expenseAmount;
        public TextView expenseCategory;





        public ExpenseViewHolder( View itemView) {
            super(itemView);

            expenseItem = itemView.findViewById(R.id.expenseNameItem);
            expenseDate = itemView.findViewById(R.id.expenseDateItem);
            expenseAmount = itemView.findViewById(R.id.expenseAmountItem);
            expenseCategory = itemView.findViewById(R.id.expenseCategoryItem);


        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        public TextView headerTitle;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTitle = (TextView)itemView.findViewById(R.id.header_id);
        }
    }
}

