package com.example.anna.ses_1b_group2.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.anna.ses_1b_group2.R;
import com.example.anna.ses_1b_group2.models.User;
import com.example.anna.ses_1b_group2.models.UserProfile;
import com.example.anna.ses_1b_group2.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseMethods {
    private static final String TAG = "FirebaseMethods";


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private Context mContext;
    private String userID;
    private String resID;

    public FirebaseMethods (Context context){
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        if (mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }

    }

    /**
     * register a new email and password to firebase authentication
     * @param email
     * @param password
     * @param username
     */
    public void registerNewEmail (final String email, final String username, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(mContext, "Register success. ",Toast.LENGTH_SHORT).show();
                            userID = mAuth.getCurrentUser().getUid();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * add New User to the database
     * @param email
     * @param username
     */
    public void addNewUser(String email, String username){
        User user = new User(
                userID,
                email,
                StringManipulation.condenseUsername(username)
        );

        myRef.child(mContext.getString(R.string.dbname_user)).child(userID).setValue(user);

        UserProfile profile = new UserProfile(
                username,
                "",
                "",
                0,
                0,
                ""
        );

        myRef.child(mContext.getString(R.string.dbname_user_profile)).child(userID).setValue(profile);
    }

    /**
     * retrives the profile fro the user currently logged in
     * @param dataSnapshot
     * @return
     */
    public UserSettings getUserSettings(DataSnapshot dataSnapshot){
        Log.d(TAG, "getUserSettings: retrieving user profile from firebase.");

        UserProfile profile = new UserProfile();
        User user = new User();

        for (DataSnapshot ds: dataSnapshot.getChildren()){

            //user_profile node
            if (ds.getKey().equals(mContext.getString(R.string.dbname_user_profile))){
                Log.d(TAG, "getUserSettings: snapshot key: " + ds.getKey());
                try{
                    profile.setFull_name(ds.child(userID).getValue(UserProfile.class).getFull_name());
                    profile.setGender(ds.child(userID).getValue(UserProfile.class).getGender());
                    profile.setDob(ds.child(userID).getValue(UserProfile.class).getDob());
                    profile.setHeight(ds.child(userID).getValue(UserProfile.class).getHeight());
                    profile.setWeight(ds.child(userID).getValue(UserProfile.class).getWeight());
                    profile.setMedical_condition(ds.child(userID).getValue(UserProfile.class).getMedical_condition());
                    Log.d(TAG, "getUserSettings: retrieved users information: " + user.toString());
                }catch (NullPointerException e) {
                    Log.d(TAG, "getUserSettings: NullPointerException2: " + e.getMessage());
                }
            }

            //user node
            if (ds.getKey().equals(mContext.getString(R.string.dbname_user))){
                Log.d(TAG, "getUserSettings: snapshot key: " + ds.getKey());
                try{
                    user.setUser_id(ds.child(userID).getValue(User.class).getUser_id());
                    user.setEmail(ds.child(userID).getValue(User.class).getEmail());
                    user.setUsername(ds.child(userID).getValue(User.class).getUsername());
                }catch (NullPointerException e) {
                    Log.d(TAG, "getUserSettings: NullPointerException2: " + e.getMessage());
                }
            }
        }
        return new UserSettings(user,profile);
    }
}
