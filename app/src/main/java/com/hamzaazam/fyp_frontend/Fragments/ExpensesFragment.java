package com.hamzaazam.fyp_frontend.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hamzaazam.fyp_frontend.Adapter.BillsAdapter;
import com.hamzaazam.fyp_frontend.Adapter.ExpenseAdapter;
import com.hamzaazam.fyp_frontend.AddExpenseActivity;
import com.hamzaazam.fyp_frontend.Model.BillM;
import com.hamzaazam.fyp_frontend.Model.ExpenseM;
import com.hamzaazam.fyp_frontend.R;
import com.hamzaazam.fyp_frontend.ocrActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpensesFragment extends Fragment {

    RecyclerView recyclerViewExpense;
    ExpenseAdapter expenseAdapter;
    protected List<ExpenseM> expenseMList;
    FloatingActionButton addExpensebutton;

    EditText search_bar;
    ProgressDialog pDialog;

    DatabaseReference reference;

    String fuserid;//current user logged in

    List expenseCategories = new ArrayList<String>();

    public ExpensesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_docs, container, false);


        ////////////

        addExpensebutton = view.findViewById(R.id.create_expense_button);

        search_bar = view.findViewById(R.id.searchExpenses);

        recyclerViewExpense = view.findViewById(R.id.recyclerViewExpenses);
        recyclerViewExpense.setHasFixedSize(true);
        recyclerViewExpense.setLayoutManager(new LinearLayoutManager(getContext()));

        expenseMList=new ArrayList<>();


        //////////////
        fuserid= FirebaseAuth.getInstance().getCurrentUser().getUid();



        //Populating Category list
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading Expenses");
        pDialog.show();
        Query myTopPostsQuery = FirebaseDatabase.getInstance().getReference("expenses").child(fuserid).orderByChild("expenseCategory");
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expenseMList.clear();
                expenseCategories.clear();

                for(DataSnapshot expense : dataSnapshot.getChildren()){
                    ExpenseM e = expense.getValue(ExpenseM.class);

                    if (!expenseCategories.contains(e.getExpenseCategory())){
                        Log.e("ERROR EXPENSE",e.getExpenseCategory());
                        e.setHeader(false);
                        expenseCategories.add(e.getExpenseCategory());
                        expenseMList.add(e);
                        expenseAdapter.notifyDataSetChanged();


                    }
//                    e.setHeader(true);
                    e.setExpenseId(expense.getKey());
                    expenseMList.add(e);
                    expenseAdapter.notifyDataSetChanged();


                    pDialog.hide();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        expenseAdapter=new ExpenseAdapter(getContext(),expenseMList);
        recyclerViewExpense.setAdapter(expenseAdapter);



        //ADD BILL FAB ONCLICK
        addExpensebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddExpenseActivity.class);
                startActivity(intent);
            }

        });



        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchExpenses(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





        recyclerViewExpense.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_MOVE){

                    // Do what you want
                    addExpensebutton.hide();
                }
                if(event.getAction() == MotionEvent.ACTION_UP){

                    // Do what you want
                    addExpensebutton.show();
                }
                v.onTouchEvent(event);
                return true;
            }
        });



        return view;
    }


    private void searchExpenses(String s){
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Query query= FirebaseDatabase.getInstance().getReference("expenses").child(firebaseUser.getUid()).orderByChild("expenseDate")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expenseMList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final ExpenseM bill = snapshot.getValue(ExpenseM.class);
                    assert expenseMList != null;

                        expenseMList.add(bill);

                }
                expenseAdapter = new ExpenseAdapter(getContext(), expenseMList);
                recyclerViewExpense.setAdapter(expenseAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
        pDialog.dismiss();
    }

}
