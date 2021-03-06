package com.example.anna.ses_1b_group2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.ses_1b_group2.camera.MediaActivity;
import com.example.anna.ses_1b_group2.hr.HeartRateActivity;
import com.example.anna.ses_1b_group2.login.PatientLoginActivity;
import com.example.anna.ses_1b_group2.login.SignOutActivity;
import com.example.anna.ses_1b_group2.map.MapActivity;
import com.example.anna.ses_1b_group2.models.UserProfile;
import com.example.anna.ses_1b_group2.models.UserSettings;
import com.example.anna.ses_1b_group2.profile.ProfileActivity;
import com.example.anna.ses_1b_group2.utils.FirebaseMethods;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientHomeActivity extends AppCompatActivity {
    private static final String TAG = "PatientHomeActivity";
    private Context mContext = PatientHomeActivity.this;
    private TextView mPatientName, linkSignOut;
    private Button btnProfile, btnHR, btnMap, btnVs;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    private UserSettings mUserSettings;
    private static final int ERROR_DIALOG_REQUEST = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_homepage);
        Log.d(TAG, "onCreate: starting");

        mPatientName = (TextView)findViewById(R.id.patientName);
        linkSignOut = (TextView) findViewById(R.id.profile_SignOut);
        btnProfile = (Button) findViewById(R.id.btn_profile);
        btnHR = (Button) findViewById(R.id.btn_hr);
        btnMap = (Button) findViewById(R.id.btn_map);
        btnVs = (Button) findViewById(R.id.btn_vs);

        linkSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to SignOutActivity");
                Intent intent = new Intent(mContext, SignOutActivity.class);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to ProfileActivity");
                Intent intent = new Intent(mContext, ProfileActivity.class);
                startActivity(intent);
            }
        });

        btnHR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to HeartRateActivity");
                Intent intent = new Intent(mContext, HeartRateActivity.class);
                startActivity(intent);
            }
        });
        btnVs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to MediaActivity");
                Intent intent = new Intent(mContext, MediaActivity.class);
                startActivity(intent);
            }
        });

        //map function
        if (isServicesOK()){
            initMap();
        }
        setupFirebaseAuth();
    }

    private void setProfileWidgets(UserSettings userSettings){

        mUserSettings = userSettings;
        UserProfile profile = userSettings.getProfile();
        mPatientName.setText(profile.getFull_name());
    }

    /**
     * testing if map function is work or not
     */
    private void initMap(){
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to MapActivity");
                Intent intent = new Intent(mContext, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(PatientHomeActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured by we can resolve it
            Log.d(TAG, "isServicesOK: an error occured by we can resolve it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(PatientHomeActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else {
            Toast.makeText(this, "You can't make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    /**
     * -------------------------------Firebase---------------------------------------
     */
    /**
     * checks to see if the @param 'user' is logged in
     * @param user
     */
    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");
        if (user == null){
            Intent intent = new Intent(mContext, PatientLoginActivity.class);
            startActivity(intent);
        }
    }
    /**
     * setup the Firebase auth object
     */
    
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mFirebaseMethods = new FirebaseMethods(mContext);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                checkCurrentUser(user);

                if (user != null){
                    // user is signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());

                } else {
                    // user is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));

                //retrieve images for the user in question

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
        checkCurrentUser(mAuth.getCurrentUser());
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
