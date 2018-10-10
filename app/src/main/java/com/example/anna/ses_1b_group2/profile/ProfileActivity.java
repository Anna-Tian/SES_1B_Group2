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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anna.ses_1b_group2.PatientHomeActivity;
import com.example.anna.ses_1b_group2.R;
import com.example.anna.ses_1b_group2.login.PatientLoginActivity;
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

public class ProfileActivity extends AppCompatActivity{
    private static final String TAG = "ProfileActivity";

    private Context mContext = ProfileActivity.this;
    private ImageView iconHome;
    private TextView dFullName, dGender, dDOB, dHeight, dWeight, dMedicalConditon;
    private Button btnEdit, btnSend;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mFirebaseMethods = new FirebaseMethods(mContext);
        iconHome = (ImageView) findViewById(R.id.icHome);
        dFullName = (TextView) findViewById(R.id.display_fullname);
        dGender = (TextView) findViewById(R.id.display_gender);
        dDOB = (TextView) findViewById(R.id.display_dob);
        dHeight = (TextView) findViewById(R.id.display_height);
        dWeight = (TextView) findViewById(R.id.display_weight);
        dMedicalConditon = (TextView) findViewById(R.id.display_condition);
        btnEdit = (Button) findViewById(R.id.edit_profile);
        btnSend = (Button) findViewById(R.id.btn_send);

        Log.d(TAG, "onCreate: started");

        iconHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to HomePage");
                Intent intent = new Intent(mContext, PatientHomeActivity.class);
                startActivity(intent);
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to EditProfile page");
                Intent intent = new Intent(mContext, EditProfileActivity.class);
                startActivity(intent);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to SendProfile page");
                Intent intent = new Intent(mContext, SendProfileActivity.class);
                startActivity(intent);
            }
        });
        setupFirebaseAuth();

    }

    private void setProfileWidgets(UserSettings userSettings){
        Log.d(TAG, "setProfileWidgets: "+ userSettings.toString());
        //User user = userSettings.getUser();
        UserProfile userProfile = userSettings.getProfile();

        dFullName.setText(userProfile.getFull_name());
        dGender.setText(userProfile.getGender());
        dDOB.setText(userProfile.getDob());
        dHeight.setText(String.valueOf(userProfile.getHeight()));
        dWeight.setText(String.valueOf(userProfile.getWeight()));
        dMedicalConditon.setText(userProfile.getMedical_condition());


    }







    /**
     * -------------------------------Firebase---------------------------------------
     */

    //set up the firebase auth object
    private void setupFirebaseAuth(){

        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                // check if the user is logged in
                //checkCurrentUser(user);

                if (user != null){
                    // user is signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());

                } else {
                    // user is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");

                    Intent intent = new Intent(mContext,PatientLoginActivity.class);
                    startActivity(intent);
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //retrieve user information from the database
                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
    /**
     * -------------------------------Firebase---------------------------------------
     */
}
