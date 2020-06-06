package com.hamzaazam.fyp_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hamzaazam.fyp_frontend.Fragments.ExpensesFragment;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class AddExpenseActivity extends AppCompatActivity {
    Button btnAddItem;
    ImageButton btnCross;

    EditText expenseDate;
    EditText expenseAmount;
    EditText expenseCategory;
    EditText expenseName;
    TextView tvErrorAdding;

    DatePicker datePickerExpense;


    DatabaseReference reference;

    String fuserid;//current user logged in

    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "Novembe", "December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(1200,1500);

        setContentView(R.layout.activity_add_expense);

        expenseName = findViewById(R.id.itemName);
        expenseAmount = findViewById(R.id.itemAmount);
        expenseCategory = findViewById(R.id.itemCategory);
        //expenseDate = findViewById(R.id.itemDate);
        btnAddItem = findViewById(R.id.btnAddExpense);
        btnCross = findViewById(R.id.exitAct);
        tvErrorAdding = findViewById(R.id.tvErrorAdding);
        datePickerExpense =findViewById(R.id.datePicker1);

        fuserid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExpenseItem();
            }
        });

        btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    void addExpenseItem(){
        final ProgressDialog pd=new ProgressDialog(AddExpenseActivity.this);



        try{
            reference= FirebaseDatabase.getInstance().getReference("expenses").child(fuserid);
            HashMap<String ,Object> map2 =new HashMap<>();

            if( expenseCategory.getText().toString().matches("") || expenseAmount.getText().toString().matches("") || expenseName.getText().toString().matches("")){
                tvErrorAdding.setText("Input Fields should not be empty.");
            }
            else{
                pd.setMessage("Adding");
                pd.show();

                map2.put("expenseAmount", expenseAmount.getText().toString());
                map2.put("expenseCategory", expenseCategory.getText().toString());
                //map2.put("expenseDate", expenseDate.getText().toString());
                String dateEx = MONTHS[datePickerExpense.getMonth()]+ " " + datePickerExpense.getDayOfMonth() + ", " + datePickerExpense.getYear();
                map2.put("expenseDate", dateEx);

                map2.put("expenseItem", expenseName.getText().toString());

                reference.push().setValue(map2);
                Toasty.success(getApplicationContext(), "Item Added Successfully!", Toast.LENGTH_LONG, true).show();
                pd.dismiss();

                finish();
            }
        }
        catch (Exception e){
            tvErrorAdding.setText(e.getLocalizedMessage());
        }



    }
}
