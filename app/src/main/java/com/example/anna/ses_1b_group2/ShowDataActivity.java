package com.example.anna.ses_1b_group2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowDataActivity extends AppCompatActivity {


    DatabaseReference heartRateDataref;
    List<heartRateData> listHeartRate;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        listHeartRate = new ArrayList<>();
        heartRateDataref = FirebaseDatabase.getInstance().getReference("HeartRateData");
         listView = (ListView) findViewById(R.id.listViewHeartData);


    }

    @Override
    protected void onStart() {
        super.onStart();
        heartRateDataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listHeartRate.clear();
                for(DataSnapshot heartrateSnapshot: dataSnapshot.getChildren())
                {
                    heartRateData hrd = heartrateSnapshot.getValue(heartRateData.class);
                    listHeartRate.add(hrd);

                }
                heartRateList adapter = new heartRateList(ShowDataActivity.this, listHeartRate);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
