package com.example.anna.ses_1b_group2.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.ses_1b_group2.PatientHomeActivity;
import com.example.anna.ses_1b_group2.R;
import com.example.anna.ses_1b_group2.models.Doctor;
import com.example.anna.ses_1b_group2.models.UserProfile;
import com.example.anna.ses_1b_group2.models.UserSettings;
import com.example.anna.ses_1b_group2.utils.FirebaseMethods;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SendProfileActivity extends AppCompatActivity {
    private static final String TAG = "SendProfileActivity";
    private Context mContext = SendProfileActivity.this;
    private RecyclerView docList;
    private String topDoctorName, userID, patientName, doctorEmail, doctor_ID, doctorID;
    private ImageView iconBack;
    private DatabaseReference myRef;

    private DatabaseReference mRef;
    private FirebaseMethods mFirebaseMethods;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_send_profile);
        Log.d(TAG, "onCreate: starting");


        TextView txt = (TextView) findViewById(R.id.textView_selectDoc);
        iconBack = (ImageView) findViewById(R.id.backArrow);
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to Profile page");
                Intent intent = new Intent(mContext, ProfileActivity.class);
                startActivity(intent);
            }
        });

        myRef = FirebaseDatabase.getInstance().getReference().child(mContext.getString(R.string.dbname_doctor));
        myRef.keepSynced(true);
        mRef = FirebaseDatabase.getInstance().getReference();
        mFirebaseMethods = new FirebaseMethods(mContext);

        docList = (RecyclerView) findViewById(R.id.recyclerView);
//        docList.setHasFixedSize(true);
        docList.setLayoutManager(new LinearLayoutManager(this));


        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    //get doctor name and medical field from  Firebase
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Doctor, DocViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Doctor, DocViewHolder>
                (Doctor.class, R.layout.snippet_d_name_field, DocViewHolder.class, myRef) {
            @Override
            protected void populateViewHolder(DocViewHolder viewHolder, final Doctor model, int position) {
                viewHolder.setUsername(model.getUsername());
                viewHolder.setMedical_field(model.getMedical_field());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //retrieve patient profile information from the database
                                doctorID = model.getUser_id();
                                doctorEmail = model.getEmail();
                                sendPatientProfileDetails(mFirebaseMethods.getUserSettings(dataSnapshot));
                                Log.d(TAG, "onDataChange: send successfully");
                                Toast.makeText(mContext, "send successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(mContext, PatientHomeActivity.class);
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
        docList.setAdapter(firebaseRecyclerAdapter);
    }

    public void sendPatientProfileDetails(UserSettings userSettings) {
        Doctor doctor = userSettings.getDoctor();
        UserProfile userProfile= userSettings.getProfile();
        patientName = userProfile.getFull_name();


        Log.d(TAG, "sendPatientProfileDetails: patient name: " + patientName);
        Log.d(TAG, "sendPatientProfileDetails: userid: " + userID);
        Log.d(TAG, "sendPatientProfileDetails: doctorEmail: " + doctorEmail);
        mRef.child("send_patient_details").child(userID).child("full_name").setValue(patientName);
        mRef.child("send_patient_details").child(userID).child(doctorID).child("email").setValue(doctorEmail);
        mRef.child("send_patient_details").child(userID).child(doctorID).child("user_profile").setValue(userProfile);

    }


    public static class DocViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "DocViewHolder";
        View mView;

        public DocViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setUsername(String username){
            TextView doc_name = (TextView)mView.findViewById(R.id.doctorName);
            doc_name.setText(username);
        }

        public void setMedical_field(String medical_field){
            TextView medicalField = (TextView)mView.findViewById(R.id.tv_medical_field);
            medicalField.setText(medical_field);
        }
    }
}
