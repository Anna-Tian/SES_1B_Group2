package com.example.anna.ses_1b_group2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.anna.ses_1b_group2.feedback.DoctorEndPatientIntroActivity;
import com.example.anna.ses_1b_group2.hr.patientWithDoctor;
import com.example.anna.ses_1b_group2.login.PatientLoginActivity;
import com.example.anna.ses_1b_group2.camera.MediaActivity;
import com.example.anna.ses_1b_group2.login.SignOutActivity;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.anna.ses_1b_group2.camera.VideoListActivity;
import com.example.anna.ses_1b_group2.hr.HeartRateActivity;
import com.example.anna.ses_1b_group2.login.SignOutActivity;
import com.example.anna.ses_1b_group2.models.Doctor;
import com.example.anna.ses_1b_group2.map.MapActivity;
import com.example.anna.ses_1b_group2.models.UserProfile;
import com.example.anna.ses_1b_group2.models.UserSettings;
import com.example.anna.ses_1b_group2.models.newObject;
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

import java.util.ArrayList;
import java.util.List;

public class DoctorHomeActivity extends AppCompatActivity {
    private static final String TAG = "DoctorHomeActivity";
    private Context mContext = DoctorHomeActivity.this;
    private TextView mDoctorName, linkSignOut, mMedicalField;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    DatabaseReference doctorname;
    DatabaseReference patientlist;
    List<Doctor> listdotor;
    List<patientWithDoctor> patientDoctor;
    TextView textViewDoctorName;
    ListView patient_listview;
    EditText editTextMedicalField;
    Button edit;
    Doctor updateDoctor;


    private UserSettings mUserSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_homepage);
        Log.d(TAG, "onCreate: starting");

        mDoctorName = (TextView)findViewById(R.id.doctorName);
        mMedicalField = (TextView)findViewById(R.id.et_medical_field);
        linkSignOut = (TextView) findViewById(R.id.profile_SignOut);


        textViewDoctorName = (TextView) findViewById(R.id.doctorName) ;
        patient_listview = (ListView) findViewById(R.id.ListViewPatient);
        editTextMedicalField = (EditText) findViewById(R.id.et_medical_field);
        edit = (Button) findViewById(R.id.btn_edit);
        doctorname =  FirebaseDatabase.getInstance().getReference("doctor");
        patientlist = FirebaseDatabase.getInstance().getReference("patientAndDoctor");
        listdotor = new ArrayList<>();
        patientDoctor= new ArrayList<>();


        linkSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to SignOutActivity");
                Intent intent = new Intent(mContext, SignOutActivity.class);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseReference newdoctor =  FirebaseDatabase.getInstance().getReference("doctor").child(updateDoctor.getUser_id());
                Doctor d = new Doctor(updateDoctor.getUsername(),updateDoctor.getUser_id(),editTextMedicalField.getText().toString().trim(),updateDoctor.getEmail());
                newdoctor.setValue(d);
                Toast.makeText(mContext,"Medical Field updated successfully", Toast.LENGTH_LONG).show();

            }
        });

        patient_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: 1");
                patientWithDoctor obj = patientDoctor.get(i);
                Log.d(TAG, "onItemClick: 2");
                newObject nb = new newObject(obj, updateDoctor, patient_listview.getItemAtPosition(i).toString().trim());
                Log.d(TAG, "onItemClick: 3");
                Intent intent = new Intent(mContext, DoctorEndPatientIntroActivity.class);
                Log.d(TAG, "onItemClick: 4");
                intent.putExtra("patient", nb);
                startActivity(intent);
                Log.d(TAG, "onItemClick: 5");
                finish();

            }
        });

        setupFirebaseAuth();
    }

    private void setProfileWidgets(UserSettings userSettings){

        mUserSettings = userSettings;
        Doctor doctor = userSettings.getDoctor();
        mDoctorName.setText(doctor.getUsername());
        mMedicalField.setText(doctor.getMedical_field());
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

                //retrieve doctor information from the database
                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        doctorname.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listdotor.clear();

                for(DataSnapshot doctor_list: dataSnapshot.getChildren())
                {
                    Doctor d = doctor_list.getValue(Doctor.class);
                    d.user_id = doctor_list.getKey();
                    listdotor.add(d);
                }
                if(!listdotor.isEmpty()) {
                    textViewDoctorName.setText(listdotor.get(0).getUsername());
                    editTextMedicalField.setText(listdotor.get(0).getMedical_field());

                    updateDoctor = new Doctor(listdotor.get(0).getUsername(),listdotor.get(0).getUser_id().toString().trim(),listdotor.get(0).getMedical_field(),listdotor.get(0).getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            
        });

        patientlist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                patientDoctor.clear();

                for(DataSnapshot patientlist: dataSnapshot.getChildren())
                {

                    patientWithDoctor pd = patientlist.getValue(patientWithDoctor.class);
                    if(pd.getDoctorName().contentEquals(listdotor.get(0).getUsername())) {
                        patientDoctor.add(pd);
                    }
                }
                final ArrayList<String> listItems=new ArrayList<String>();
                int i=1;
                for(patientWithDoctor pd : patientDoctor)
                {
                    if(pd.toString().contentEquals(listdotor.get(0).getUsername()))
                    {
                        listItems.add("Patient "+(i++)+"");
                    }
                }
                final ArrayAdapter < String > adapter = new ArrayAdapter < String >
                        (mContext, android.R.layout.simple_list_item_1,
                                listItems);
                patient_listview.setAdapter(adapter);
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
