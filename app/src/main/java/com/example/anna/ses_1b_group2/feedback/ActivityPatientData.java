package com.example.anna.ses_1b_group2.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.anna.ses_1b_group2.R;
import com.example.anna.ses_1b_group2.hr.heartRateData;
import com.example.anna.ses_1b_group2.hr.patientWithDoctor;
import com.example.anna.ses_1b_group2.models.Doctor;
import com.example.anna.ses_1b_group2.models.newObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ActivityPatientData extends AppCompatActivity {


    DatabaseReference heartRateDataref;
    List<heartRateData> listHeartRate;
    ListView listView;
    Doctor d;
    newObject nb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_hr_patient_data);
        listHeartRate = new ArrayList<>();
        Intent intent = getIntent();
        nb = (newObject) intent.getSerializableExtra("newobject");
        //we have three objects here doctor | patientwithdoctor | patient number (1)
         d = nb.getD();
        heartRateDataref = FirebaseDatabase.getInstance().getReference("patientAndDoctor");
       listView = (ListView) findViewById(R.id.ListViewPatientData);
    }

    @Override
    protected void onStart() {
        super.onStart();
        heartRateDataref.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listHeartRate.clear();
                for(DataSnapshot heartrateSnapshot: dataSnapshot.getChildren())
                {

                    patientWithDoctor hrd = heartrateSnapshot.getValue(patientWithDoctor.class);
                    if(hrd.getDoctorName().equalsIgnoreCase(d.getUsername()))
                    listHeartRate.add((heartRateData) hrd.getHrd());

                }
                customeListView adapter = new customeListView(ActivityPatientData.this, listHeartRate);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
