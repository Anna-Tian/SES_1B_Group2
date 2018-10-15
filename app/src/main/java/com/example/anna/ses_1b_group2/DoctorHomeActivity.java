package com.example.anna.ses_1b_group2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.anna.ses_1b_group2.doctor.PatientDetails;
import com.example.anna.ses_1b_group2.login.PatientLoginActivity;
import com.example.anna.ses_1b_group2.camera.MediaActivity;
import com.example.anna.ses_1b_group2.login.SignOutActivity;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.anna.ses_1b_group2.hr.HeartRateActivity;
import com.example.anna.ses_1b_group2.login.SignOutActivity;
import com.example.anna.ses_1b_group2.models.Doctor;
import com.example.anna.ses_1b_group2.map.MapActivity;
import com.example.anna.ses_1b_group2.models.User;
import com.example.anna.ses_1b_group2.models.UserProfile;
import com.example.anna.ses_1b_group2.models.UserSettings;
import com.example.anna.ses_1b_group2.utils.FirebaseMethods;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorHomeActivity extends AppCompatActivity {
    private static final String TAG = "DoctorHomeActivity";
    private Context mContext = DoctorHomeActivity.this;
    private TextView mDoctorName, linkSignOut, mMedicalField;
    private RecyclerView patientList;
    private String userID, patientNameinSend, patientnameinProfile, doctorName, patientID;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    private DatabaseReference myRefPatient;

    private UserSettings mUserSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_homepage);
        Log.d(TAG, "onCreate: starting");

        mDoctorName = (TextView)findViewById(R.id.doctorName);
        mMedicalField = (TextView)findViewById(R.id.tv_medical_field);
        linkSignOut = (TextView) findViewById(R.id.profile_SignOut);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mFirebaseMethods = new FirebaseMethods(mContext);
        myRefPatient = mFirebaseDatabase.getReference().child("user_profile");
        myRefPatient.keepSynced(true);


        linkSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to SignOutActivity");
                Intent intent = new Intent(mContext, SignOutActivity.class);
                startActivity(intent);
            }
        });

        patientList = (RecyclerView) findViewById(R.id.recyclerView);
//        docList.setHasFixedSize(true);
        patientList.setLayoutManager(new LinearLayoutManager(this));



//        patientNameinSend = myRefPatient.child("full_name").toString();
//        patientnameinProfile = myRef.child("send_Patient_details").child("full_name").toString();
        Log.d(TAG, "onCreate: patientNameinSend: " + patientNameinSend);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "onCreate: singed in: " + userID);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve doctor information from the database
                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setProfileWidgets(UserSettings userSettings){

        mUserSettings = userSettings;
        Doctor doctor = userSettings.getDoctor();
        User user = userSettings.getUser();
        mDoctorName.setText(doctor.getUsername());
        mMedicalField.setText(doctor.getMedical_field());
    }


    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<UserProfile, PatientViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserProfile, PatientViewHolder>
                (UserProfile.class, R.layout.snippet_p_name, PatientViewHolder.class, myRefPatient) {
            @Override
            protected void populateViewHolder(PatientViewHolder viewHolder, final UserProfile model, int position) {

                viewHolder.setFull_name(model.getFull_name());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //retrieve patient profile information from the database
                                Intent intent = new Intent(mContext, PatientDetails.class);
                                intent.putExtra("patient_name", model.getFull_name());
                                intent.putExtra("dob_i", model.getDob());
                                intent.putExtra("gender_i", model.getGender());
                                intent.putExtra("height_i",model.getGender());
                                intent.putExtra("condition_i", model.getMedical_condition());
                                intent.putExtra("weight_i", model.getWeight());
                                startActivity(intent);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                });
            }
        };
        patientList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "PatientViewHolder";
        View mView;

        public PatientViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setFull_name(String full_name){
            TextView patient_name = (TextView)mView.findViewById(R.id.patientName);
            patient_name.setText(full_name);
            Log.d(TAG, "setFull_name: "+ full_name);
        }

    }



}
