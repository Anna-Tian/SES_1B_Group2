package com.example.anna.ses_1b_group2.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.ses_1b_group2.DoctorHomeActivity;
import com.example.anna.ses_1b_group2.PatientHomeActivity;
import com.example.anna.ses_1b_group2.R;
import com.example.anna.ses_1b_group2.models.Doctor;
import com.example.anna.ses_1b_group2.models.User;
import com.example.anna.ses_1b_group2.models.UserSettings;
import com.example.anna.ses_1b_group2.utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginSelectActivity extends AppCompatActivity {
    private static final String TAG = "LoginSelectActivity";
    private Context mContext = LoginSelectActivity.this;
    private Button btnLoginPatient, btnLoginDoctor;
    private ProgressBar mProgressBar;
    private TextView mPleaseWait;
    private UserSettings mUserSettings;
    private String doctorUID, userUID;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_select);

        btnLoginPatient = (Button)findViewById(R.id.btn_login_patient);
        btnLoginDoctor = (Button)findViewById(R.id.btn_login_doctor);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mPleaseWait = (TextView) findViewById(R.id.pleaseWait);

        btnLoginPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PatientLoginActivity.class);
                startActivity(intent);
            }
        });
        btnLoginDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DoctorLoginActivity.class);
                startActivity(intent);
            }
        });

        mPleaseWait.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

        setupFirebaseAuth();
    }

    private void changeHomepage(UserSettings userSettings){

        mUserSettings = userSettings;
        Doctor doctor = userSettings.getDoctor();
        User user = userSettings.getUser();
        doctorUID = doctor.getUser_id();
        Log.d(TAG, "setProfileWidgets: doctor uid: " + doctorUID);
        userUID = user.getUser_id();
        Log.d(TAG, "setProfileWidgets: user uid: " + userUID);
        if (doctorUID != null){
            Intent intent = new Intent(mContext, DoctorHomeActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(mContext, PatientHomeActivity.class);
            startActivity(intent);
        }
    }


    /*
    -----------Firebase-----------------
     */

    //set up the firebase auth object
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mFirebaseMethods = new FirebaseMethods(mContext);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // user is signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //retrieve doctor information from the database
                            changeHomepage(mFirebaseMethods.getUserSettings(dataSnapshot));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                    mAuth.signOut();

                } else {
                    // user is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                    mPleaseWait.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "You are currently signed out!", Toast.LENGTH_LONG).show();
                }
            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /*
    -----------Firebase-----------------
     */
}
