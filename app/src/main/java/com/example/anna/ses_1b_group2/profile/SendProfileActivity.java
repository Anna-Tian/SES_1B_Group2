package com.example.anna.ses_1b_group2.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.anna.ses_1b_group2.R;
import com.example.anna.ses_1b_group2.models.UserProfile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendProfileActivity extends AppCompatActivity {
    private static final String TAG = "SendProfileActivity";
    private Context mContext = SendProfileActivity.this;
    private Spinner docList;
    private Button btnSend;
    private UserProfile userProfile;
    private ImageView btnClose;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_send_profile);
        Log.d(TAG, "onCreate: starting");

        docList = (Spinner) findViewById(R.id.spinnerDoctorList);
        btnSend = (Button) findViewById(R.id.button_SendDoctor);
        TextView txt = (TextView) findViewById(R.id.textView_selectDoc);
        btnClose = (ImageButton) findViewById(R.id.imageButtonClose);
        myRef = FirebaseDatabase.getInstance().getReference();



    }

}
