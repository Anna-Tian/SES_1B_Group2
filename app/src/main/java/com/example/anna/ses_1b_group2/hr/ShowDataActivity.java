package com.example.anna.ses_1b_group2.hr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.anna.ses_1b_group2.R;
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
    private ImageView iconBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        listHeartRate = new ArrayList<>();
        heartRateDataref = FirebaseDatabase.getInstance().getReference("HeartRateData");
         listView = (ListView) findViewById(R.id.listViewHeartData);
        iconBack = (ImageView) findViewById(R.id.backArrow);

        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowDataActivity.this, HeartRateActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(getBaseContext(),HeartRateActivity.class);
        startActivity(intent);
        finish();

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
