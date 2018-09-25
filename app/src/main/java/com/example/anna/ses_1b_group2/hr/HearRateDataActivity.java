package com.example.anna.ses_1b_group2.hr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.anna.ses_1b_group2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class HearRateDataActivity extends AppCompatActivity {

    EditText min;
    EditText max;
    EditText _time;
    EditText heartRate;
    EditText speed;
    EditText distance;
    Button stop, now;
    ImageButton _exit;
    private ImageView iconBack;

    DatabaseReference heartRateDataref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_data);
         min = (EditText) findViewById(R.id.editTextMin);
         max = (EditText) findViewById(R.id.editTextMax);
         _time = (EditText) findViewById(R.id.editTextTime);
         heartRate = (EditText) findViewById(R.id.editTextHeartRate);
         speed = (EditText) findViewById(R.id.editTextSpeed);
         distance = (EditText) findViewById(R.id.editTextDistance);
         stop = (Button) findViewById(R.id.buttonStop);
//         _exit = (ImageButton) findViewById(R.id.imageButtonexit);
         now = (Button) findViewById(R.id.buttonNow);
        iconBack = (ImageView) findViewById(R.id.backArrow);

        heartRateDataref = FirebaseDatabase.getInstance().getReference("HeartRateData");
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHeartData();
            }
        });
//        _exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(HearRateDataActivity.this,HeartRateActivity.class);
//
//                startActivity(intent);
//                finish();
//            }
//        });
        now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long date = System.currentTimeMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = sdf.format(date);
                _time.setText(dateString);
            }
        });
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HearRateDataActivity.this, HeartRateActivity.class);
                startActivity(intent);
            }
        });
    }
    private void addHeartData()
    {
         min = (EditText) findViewById(R.id.editTextMin);
         max = (EditText) findViewById(R.id.editTextMax);
         _time = (EditText) findViewById(R.id.editTextTime);
         heartRate = (EditText) findViewById(R.id.editTextHeartRate);
         speed = (EditText) findViewById(R.id.editTextSpeed);
         distance = (EditText) findViewById(R.id.editTextDistance);

        String txtMinVale= min.getText().toString().trim();
        String txtMaxVale= max.getText().toString().trim();
        String txttime= _time.getText().toString().trim();
        String txtHeartRate= heartRate.getText().toString().trim();
        String txtSpeed= speed.getText().toString().trim();
        String txtDistance= distance.getText().toString().trim();

            if(!TextUtils.isEmpty(txtMinVale) && !TextUtils.isEmpty(txtMaxVale) && !TextUtils.isEmpty(txttime) && !TextUtils.isEmpty(txtHeartRate)
        && !TextUtils.isEmpty(txtSpeed) && !TextUtils.isEmpty(txtDistance))
            {
                String id=heartRateDataref.push().getKey();
                heartRateData hrd = new heartRateData(txtMinVale,txtMaxVale,txttime,txtHeartRate,txtSpeed,txtDistance);
                heartRateDataref.child(id).setValue(hrd);
                Toast.makeText(this, "Data Added!", Toast.LENGTH_LONG).show();
                min.getText().clear();
                max.getText().clear();
                _time.getText().clear();
                heartRate.getText().clear();
                speed.getText().clear();
                distance.getText().clear();

            }
            else
            {
                Toast.makeText(this, "You should enter all values", Toast.LENGTH_LONG).show();
            }
    }
}
