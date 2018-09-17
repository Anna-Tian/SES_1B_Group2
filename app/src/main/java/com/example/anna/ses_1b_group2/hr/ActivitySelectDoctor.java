package com.example.anna.ses_1b_group2.hr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.ses_1b_group2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivitySelectDoctor extends AppCompatActivity {

    Spinner docList;
    Button send;
    heartRateData hrd;
    ImageButton close;
    DatabaseReference heartRateDataref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_doctor);

         docList = (Spinner) findViewById(R.id.spinnerDoctorList);
         send = (Button) findViewById(R.id.button_SendDoctor);
        TextView txt = (TextView) findViewById(R.id.textView_selectDoc);
        close = (ImageButton) findViewById(R.id.imageButtonClose);
        heartRateDataref = FirebaseDatabase.getInstance().getReference("patientAndDoctor");

        Intent intent = getIntent();
         hrd = (heartRateData)intent.getSerializableExtra("patient");
       // txt.setText(hrd.getHeartrate());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name= null;
                if(docList != null && docList.getSelectedItem() !=null ) {
                    name = (String)docList.getSelectedItem();
                    String id=heartRateDataref.push().getKey();
                    patientWithDoctor pd = new patientWithDoctor(hrd,name);
                    heartRateDataref.child(id).setValue(pd);
                    Toast.makeText(getBaseContext(), "Data Added!", Toast.LENGTH_LONG).show();
                } else  {

                    Toast.makeText(getBaseContext(), "You have to choose a doctor name!", Toast.LENGTH_LONG).show();

                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ActivitySelectDoctor.this, ShowDataActivity.class);
                startActivity(intent1);
                finish();
            }
        });


    }
}
