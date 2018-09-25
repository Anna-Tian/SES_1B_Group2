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
import android.widget.EditText;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.ses_1b_group2.R;
import com.example.anna.ses_1b_group2.models.User;
import com.example.anna.ses_1b_group2.utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity{
    private static final String TAG = "RegisterActivity";

    private Context mContext;
    private String email, username, password, phone, profile_photo, description, address;
    private EditText mEmail, mPassword, mUsername;
    private Button btnRegister;
    private String append = "";
    private ProgressBar mProgressBar;
    private TextView loadingPleaseWait;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = RegisterActivity.this;
        firebaseMethods = new FirebaseMethods(mContext);
        Log.d(TAG, "onCreate: start");

        initWidgets();
        setupFirebaseAuth();
        init();
    }

    private void init(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString();
                username = mUsername.getText().toString();
                password = mPassword.getText().toString();

                if (checkInputs(email,username,password)){
                    mProgressBar.setVisibility(View.VISIBLE);
                    loadingPleaseWait.setVisibility(View.VISIBLE);

                    firebaseMethods.registerNewEmail(email, username, password);
                }
            }
        });
    }

    private boolean checkInputs (String email, String username, String password){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if (email.equals("") || username.equals("") || password.equals("")){
            Toast.makeText(mContext, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //initialise the activity widgets
    private void initWidgets(){
        Log.d(TAG, "intWidgets: Initializing Widgets.");
        mUsername = (EditText) findViewById(R.id.input_username);
        btnRegister = (Button) findViewById(R.id.btn_register);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        loadingPleaseWait = (TextView) findViewById(R.id.pleaseWait);

        mProgressBar.setVisibility(View.GONE);
        loadingPleaseWait.setVisibility(View.GONE);
        mContext = RegisterActivity.this;
    }

    private boolean isStringNull(String string){
        Log.d(TAG, "isStringNull: checking string if null.");

        if (string.equals("")){
            return true;
        } else {
            return false;
        }
    }
    /*
    -----------Firebase-----------------
     */

    /**
     * check is @param username already exists in the database
     * @param username
     */
    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkIfUsernameExists: checking if " + username + " already exists");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_user))
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //add new user to the database
                firebaseMethods.addNewUser(email,username);
                Toast.makeText(mContext,"Welcome! " + username, Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

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

                if (user != null){
                    // user is signed in
                    Log.d(TAG, "onAuthStateChanged: sined_in: " + user.getUid());

                    //get database from firebase
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            checkIfUsernameExists(username);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    // user is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");

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

    public void onStop(){
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    /*
    -----------Firebase-----------------
     */
}
