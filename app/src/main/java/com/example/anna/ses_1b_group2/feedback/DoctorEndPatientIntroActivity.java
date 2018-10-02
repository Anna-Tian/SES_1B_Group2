
package com.example.anna.ses_1b_group2.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.anna.ses_1b_group2.R;
import com.example.anna.ses_1b_group2.models.newObject;

public class DoctorEndPatientIntroActivity extends AppCompatActivity {

    Button buttonPatientProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_patient_intro);
        Intent intent = getIntent();
       final newObject nb = (newObject) intent.getSerializableExtra("patient");
        buttonPatientProfile = (Button) findViewById(R.id.p_profile);

        setTitle(nb.getName());
        buttonPatientProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DoctorEndPatientIntroActivity.this, ActivityPatientData.class);
                i.putExtra("newobject", nb);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
