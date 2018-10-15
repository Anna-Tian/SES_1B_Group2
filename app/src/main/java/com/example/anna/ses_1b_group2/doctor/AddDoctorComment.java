package com.example.anna.ses_1b_group2.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.anna.ses_1b_group2.R;
import com.example.anna.ses_1b_group2.utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDoctorComment extends AppCompatActivity {
    private static final String TAG = "PatientDetails";
    private Context mContext = AddDoctorComment.this;
    private ImageView btnBack;
    private EditText eFeedback;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    private DatabaseReference myRefPatient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_feedback);
        Log.d(TAG, "onCreate: starting");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mFirebaseMethods = new FirebaseMethods(mContext);
        myRefPatient = mFirebaseDatabase.getReference().child("send_patient_details");
        myRefPatient.keepSynced(true);

        btnBack = (ImageView)findViewById(R.id.icBack);
        eFeedback = (EditText)findViewById(R.id.add_feedback);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to SignOutActivity");
                Intent intent = new Intent(mContext, PatientDetails.class);
                startActivity(intent);
            }
        });


    }
}
