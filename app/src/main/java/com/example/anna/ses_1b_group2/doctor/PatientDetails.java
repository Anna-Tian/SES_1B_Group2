package com.example.anna.ses_1b_group2.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.anna.ses_1b_group2.DoctorHomeActivity;
import com.example.anna.ses_1b_group2.R;
import com.example.anna.ses_1b_group2.models.Doctor;
import com.example.anna.ses_1b_group2.models.User;
import com.example.anna.ses_1b_group2.models.UserProfile;
import com.example.anna.ses_1b_group2.models.UserSettings;
import com.example.anna.ses_1b_group2.utils.FirebaseMethods;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientDetails extends AppCompatActivity {
    private static final String TAG = "PatientDetails";
    private Context mContext = PatientDetails.this;
    private ImageView btnBack;
    private TextView mPatientName, patientname, dName, dob_t, gender_t, height_t, condition_t, weight_t;
    private RecyclerView patientList;
    private String patientName,g_dob, g_gender, g_condition ;
    private Button btnFB;
    private Integer g_height, g_weight;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    private DatabaseReference myRefPatient;

    private UserSettings mUserSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_show_one_patient);
        Log.d(TAG, "onCreate: starting");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mFirebaseMethods = new FirebaseMethods(mContext);
        myRefPatient = mFirebaseDatabase.getReference().child("send_patient_details");
        myRefPatient.keepSynced(true);

        btnBack = (ImageView)findViewById(R.id.backArrow);
        mPatientName = (TextView)findViewById(R.id.patientName);
        dob_t = (TextView) findViewById(R.id.display_dob);
        gender_t = (TextView) findViewById(R.id.display_gender);
        height_t = (TextView)findViewById(R.id.display_height);
        condition_t = (TextView) findViewById(R.id.tv_medical_field);
        weight_t = (TextView)findViewById(R.id.display_weight);
        patientname = (TextView)findViewById(R.id.display_patientname);
//
//        patientList = (RecyclerView) findViewById(R.id.recyclerView);
////        docList.setHasFixedSize(true);
//        patientList.setLayoutManager(new LinearLayoutManager(this));



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to SignOutActivity");
                Intent intent = new Intent(mContext, DoctorHomeActivity.class);
                startActivity(intent);
            }
        });
        getIncomingIntent();

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
        UserProfile profile = userSettings.getProfile();
        mPatientName.setText(patientName);
        patientname.setText(patientName);
        dob_t.setText(g_dob);
        gender_t.setText(g_gender);
        height_t.setText(String.valueOf(g_height));
        condition_t.setText(g_condition);
        weight_t.setText(String.valueOf(g_weight));

    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: check for incoming intent");

        if (getIntent().hasExtra("patient_name")){
            patientName = getIntent().getStringExtra("patient_name");
        }
        if (getIntent().hasExtra("dob_i")){
            g_dob = getIntent().getStringExtra("dob_i");
            Log.d(TAG, "getIncomingIntent: test: " + g_dob);
        }
        g_gender = getIntent().getStringExtra("gender_i");
        g_height = getIntent().getIntExtra("height_i", 1);
        g_condition = getIntent().getStringExtra("condition_i");
        g_weight = getIntent().getIntExtra("weight_i",1);

//        showData(patientName, getApplicationContext());

    }

//    private void showData(final String patientName, final Context ctx) {
//
//        myRef.child("User_profile").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
//                    String name = ds.child("full_name").getValue(String.class);
//                    Log.d(TAG, "onDataChange: test " + name + " " + patientName);
//                    if (areSame(patientName, name)) {
//                        Log.d(TAG, "setRestaurant: setting profile photo name and address");
//
//                        TextView dName = (TextView) findViewById(R.id.patientName);
//                        dName.setText(ds.child("full_name").getValue(String.class));
//
//                        TextView dob_t = (TextView) findViewById(R.id.display_dob);
//                        dob_t.setText(ds.child("dob").getValue(String.class));
//
//                        TextView gender_t = findViewById(R.id.display_gender);
//                        gender_t.setText(ds.child("gender").getValue(String.class));
//
//                        TextView height_t = findViewById(R.id.display_height);
//                        height_t.setText(ds.child("height").getValue(String.class));
//
//                        TextView condition_t = findViewById(R.id.tv_medical_field);
//                        condition_t.setText(ds.child("medical_condition").getValue(String.class));
//                    } else {
//                        Log.d(TAG, "onDataChange: no restaurant found ");
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }


    @Override
    public void onStart() {
        super.onStart();

//
//        FirebaseRecyclerAdapter<UserProfile, PatientViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserProfile, PatientViewHolder>
//                (UserProfile.class, R.layout.snippet_p_profile_summary, PatientViewHolder.class, myRefPatient) {
//
//            @Override
//            protected void populateViewHolder(PatientViewHolder viewHolder, final UserProfile model, int position) {
//                viewHolder.setFull_name(model.getFull_name());
//                viewHolder.setGender(model.getGender());
//                viewHolder.setDob(model.getDob());
//                viewHolder.setHeight(model.getHeight());
//                viewHolder.setWeight(model.getWeight());
//                viewHolder.setMedical_condition(model.getMedical_condition());
//
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        myRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                //retrieve patient profile information from the database
//                                Intent intent = new Intent(mContext, AddDoctorComment.class);
//                                startActivity(intent);
//                            }
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                            }
//                        });
//                    }
//                });
//            }
//        };
//        patientList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "PatientViewHolder";
        View mView;

        public PatientViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setFull_name(String full_name) {
            TextView patient_name_s = (TextView)mView.findViewById(R.id.patientName);
            patient_name_s.setText(full_name);
            Log.d(TAG, "setFull_name: " + full_name);
        }

        public void setGender(String gender) {
            TextView gender_s = (TextView)mView.findViewById(R.id.display_gender);
            gender_s.setText(gender);
            Log.d(TAG, "setGender: " + gender);
        }

        public void setDob(String dob) {
            TextView dob_s = (TextView)mView.findViewById(R.id.display_dob);
            dob_s.setText(dob);
        }

        public void setHeight(int height) {
            TextView height_s = (TextView)mView.findViewById(R.id.display_height);
            height_s.setText(String.valueOf(height));
        }

        public void setWeight(int weight) {
            TextView weight_s = (TextView)mView.findViewById(R.id.display_weight);
            weight_s.setText(String.valueOf(weight));
        }

        public void setMedical_condition(String medical_condition) {
            TextView condition_s = (TextView)mView.findViewById(R.id.tv_medical_field);
            condition_s.setText(medical_condition);
        }



    }

    private boolean areSame(String string1, String string2) {
        return string1.equals(string2);
    }

}
