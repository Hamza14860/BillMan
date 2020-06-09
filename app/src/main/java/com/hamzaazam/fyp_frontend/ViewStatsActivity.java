package com.hamzaazam.fyp_frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hamzaazam.fyp_frontend.Model.BillM;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

public class ViewStatsActivity extends AppCompatActivity {

    DatabaseReference reference;
    String fuserid;

    String receivedBillCategory;

    GraphView graph;

    TextView tvBillCat;

    public Toolbar toolbar;


    List<String> dates = new ArrayList<String>();
    List<Integer> billAmounts = new ArrayList<Integer>();


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stats);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" Bills Stats");
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tvBillCat=findViewById(R.id.tvBillCat);
        graph = (GraphView) findViewById(R.id.graph);
        // activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScalableY(true);

        Intent intent=getIntent();
        receivedBillCategory= intent.getStringExtra("billcat");

        tvBillCat.setText(receivedBillCategory);

        fuserid= FirebaseAuth.getInstance().getCurrentUser().getUid();

//        Query query= FirebaseDatabase.getInstance().getReference("bills").child(fuserid).orderByChild("billDate");
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for(DataSnapshot bill : dataSnapshot.getChildren()){
//                    BillM e = bill.getValue(BillM.class);
//                    if(e.getBillCategory().equals(receivedBillCategory)){
//                        dates.add(e.getBillDate());
//                        billAmounts.add(Integer.parseInt(e.getBillAmount()));
//                    }
//                }
//
//                DataPoint[] dataPoints = new DataPoint[billAmounts.size()]; // declare an array of DataPoint objects with the same size as your list
//                for (int i = 0; i < billAmounts.size(); i++) {
//                    // add new DataPoint object to the array for each of your list entries
//                    //dataPoints[i] = new DataPoint(billAmounts.get(i),dates.get(i)); // not sure but I think the second argument should be of type double
//                    dataPoints[i] = new DataPoint(i,billAmounts.get(i)); // not sure but I think the second argument should be of type double
//                    LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(dataPoints);
//                    graph.addSeries(series2);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });





        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 2690),
                new DataPoint(1, 3400),
                new DataPoint(2, 3000),
                new DataPoint(3, 1890),
                new DataPoint(4, 2850)
        });

        series.setTitle("Curve 1");
        series.setColor(Color.GREEN);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);
        graph.addSeries(series);

    }
}
