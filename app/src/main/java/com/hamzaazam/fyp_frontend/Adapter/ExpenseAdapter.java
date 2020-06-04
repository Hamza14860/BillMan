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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hamzaazam.fyp_frontend.BillViewActivity;
import com.hamzaazam.fyp_frontend.Model.ExpenseM;
import com.hamzaazam.fyp_frontend.R;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

private Context mContext;
private List<ExpenseM> mExpenses;


private Fragment frag;

public ExpenseAdapter(Context context, List<ExpenseM>  expensesList){
        this.mContext=context;
        this.mExpenses=expensesList;

        //frag=cont;
}

@NonNull
@Override
public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(mContext).inflate(R.layout.rv_expense_item,parent, false);
    return new ExpenseAdapter.ExpenseViewHolder(view);
}

@Override
public void onBindViewHolder(@NonNull ExpenseViewHolder holder, final int position) {

    final ExpenseM expense = mExpenses.get(position);
    holder.expenseItem.setText(expense.getExpenseItem());
    holder.expenseCategory.setText(expense.getExpenseCategory());
    holder.expenseDate.setText(expense.getExpenseDate());
    holder.expenseAmount.setText(expense.getExpenseAmount());

    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

//            Intent intent=new Intent(mContext, BillViewActivity.class);
//            intent.putExtra("billid",mBills.get(position).getBillId());
//            mContext.startActivity(intent);

        }
    });

    Animation animation= AnimationUtils.loadAnimation(mContext,android.R.anim.slide_in_left);
    holder.itemView.startAnimation(animation);


}

@Override
public int getItemCount() {
        return mExpenses.size();
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
}

