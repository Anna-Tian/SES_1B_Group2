package com.example.anna.ses_1b_group2.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.ses_1b_group2.R;
import com.example.anna.ses_1b_group2.models.Doctor;
import com.example.anna.ses_1b_group2.models.User;
import com.example.anna.ses_1b_group2.models.UserProfile;
import com.example.anna.ses_1b_group2.models.UserSettings;
import com.example.anna.ses_1b_group2.utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OneDoctor extends AppCompatActivity {
    private static final String TAG = "OneDoctor";
    private Context mContext = OneDoctor.this;
    private String topDoctorName, userID, patientName, doctorName, doctor_ID, doctorID;
    private Button cancel, send;
    private TextView mDoctorName;

    private DatabaseReference myRef;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods mFirebaseMethods;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_show_one_doctor);
        Log.d(TAG, "onCreate: starting");

        myRef = FirebaseDatabase.getInstance().getReference().child(mContext.getString(R.string.dbname_doctor));
        myRef.keepSynced(true);
        mFirebaseMethods = new FirebaseMethods(mContext);
        mRef = FirebaseDatabase.getInstance().getReference();

        topDoctorName = getIntent().getStringExtra("topDoctorName");
        doctor_ID = getIntent().getStringExtra("doctorID");
        
        cancel = (Button) findViewById(R.id.btn_cancel);
        send = (Button) findViewById(R.id.btn_send);
        mDoctorName = (TextView)findViewById(R.id.doctorName);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to SendProfileActivity page");
                Intent intent = new Intent(mContext, SendProfileActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){
                    // user is signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());
                    userID = user.getUid();

                } else {
                    // user is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                    userID = "";

                }
            }
        };

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDoctorName.setText(topDoctorName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: attempting to send profile");

                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //retrieve patient profile information from the database
                        sendPatientProfileDetails(mFirebaseMethods.getUserSettings(dataSnapshot));
                        Log.d(TAG, "onDataChange: send successfully");
                        Toast.makeText(mContext, "send successfully", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                
                
            }
        });
    }

    public void sendPatientProfileDetails(UserSettings userSettings) {
        Doctor doctor = userSettings.getDoctor();
        UserProfile userProfile= userSettings.getProfile();
        patientName = userProfile.getFull_name();
        doctorName = topDoctorName;
        doctorID = doctor_ID;

        Log.d(TAG, "sendPatientProfileDetails: patient name: " + patientName);
        mRef.child("send_patient_details").child(userID).child("patient_name").setValue(patientName);
        mRef.child("send_patient_details").child(userID).child(doctorID).child("doctor_name").setValue(doctorName);
        mRef.child("send_patient_details").child(userID).child(doctorID).child("patient_profile").setValue(userProfile);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onStop(){
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
