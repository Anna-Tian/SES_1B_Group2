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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.ses_1b_group2.HomeActivity;
import com.example.anna.ses_1b_group2.R;
import com.example.anna.ses_1b_group2.login.LoginActivity;
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

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";

    private Context mContext = EditProfileActivity.this;
    private ImageView iconBack, iconSave;
    private EditText eFullName, eGender, eDOB, eHeight, eWeight, eMedicalConditon;

    //variables
    private UserSettings mUserSettings;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_edit);
        Log.d(TAG, "onCreate: started");

        mFirebaseMethods = new FirebaseMethods(mContext);
        iconBack = (ImageView) findViewById(R.id.backArrow);
        iconSave = (ImageView) findViewById(R.id.saveChanges);
        eFullName = (EditText) findViewById(R.id.input_fullname);
        eGender = (EditText) findViewById(R.id.input_gender);
        eDOB = (EditText) findViewById(R.id.input_dob);
        eHeight = (EditText) findViewById(R.id.input_height);
        eWeight = (EditText) findViewById(R.id.input_weight);
        eMedicalConditon = (EditText) findViewById(R.id.input_condition);

        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to Profile page");
                Intent intent = new Intent(mContext, ProfileActivity.class);
                startActivity(intent);
            }
        });
        iconSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: attempting to save changes");
                saveProfileSettings();
            }
        });

        setupFirebaseAuth();
    }

    private void setProfileWidgets(UserSettings userSettings){
        Log.d(TAG, "setProfileWidgets: "+ userSettings.toString());
        //User user = userSettings.getUser();
        UserProfile userProfile = userSettings.getProfile();

        mUserSettings = userSettings;
        eFullName.setText(userProfile.getFull_name());
        eGender.setText(userProfile.getGender());
        eDOB.setText(userProfile.getDob());
        eHeight.setText(String.valueOf(userProfile.getHeight()));
        eWeight.setText(String.valueOf(userProfile.getWeight()));
        eMedicalConditon.setText(userProfile.getMedical_condition());
    }

    private void saveProfileSettings(){
        final String fullName = eFullName.getText().toString();
        final String gender = eGender.getText().toString();
        final String dob = eDOB.getText().toString();
        final int height = Integer.parseInt(eHeight.getText().toString());
        final int weight = Integer.parseInt(eWeight.getText().toString());
        final String medicalCondition = eMedicalConditon.getText().toString();

        if (!mUserSettings.getProfile().getFull_name().equals(fullName)){
            mFirebaseMethods.updateUserSettings(fullName,null,null,0,0,null);
            Toast.makeText(mContext, "Changes Saved", Toast.LENGTH_SHORT).show();
        }
        if (!mUserSettings.getProfile().getFull_name().equals(gender)){
            mFirebaseMethods.updateUserSettings(null,gender,null,0,0,null);
            Toast.makeText(mContext, "Changes Saved", Toast.LENGTH_SHORT).show();
        }
        if (!mUserSettings.getProfile().getFull_name().equals(dob)){
            mFirebaseMethods.updateUserSettings(null,null,dob,0,0,null);
            Toast.makeText(mContext, "Changes Saved", Toast.LENGTH_SHORT).show();
        }
        if (!mUserSettings.getProfile().getFull_name().equals(height)){
            mFirebaseMethods.updateUserSettings(null,null,null,height,0,null);
            Toast.makeText(mContext, "Changes Saved", Toast.LENGTH_SHORT).show();
        }
        if (!mUserSettings.getProfile().getFull_name().equals(weight)){
            mFirebaseMethods.updateUserSettings(null,null,null,0,weight,null);
            Toast.makeText(mContext, "Changes Saved", Toast.LENGTH_SHORT).show();
        }
        if (!mUserSettings.getProfile().getFull_name().equals(medicalCondition)){
            mFirebaseMethods.updateUserSettings(null,null,null,0,0,medicalCondition);
            Toast.makeText(mContext, "Changes Saved", Toast.LENGTH_SHORT).show();
        }
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

                    Intent intent = new Intent(mContext,LoginActivity.class);
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
