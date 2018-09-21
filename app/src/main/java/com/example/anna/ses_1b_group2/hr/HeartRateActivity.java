package com.example.anna.ses_1b_group2.hr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.anna.ses_1b_group2.PatientHomeActivity;
import com.example.anna.ses_1b_group2.R;

public class HeartRateActivity extends AppCompatActivity {
    private static final String TAG = "HeartRateActivity";
    private ImageView iconHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);
        iconHome = (ImageView) findViewById(R.id.icHome);
        Button measureNow = (Button) findViewById(R.id.buttonMeasureNow);
        Button ShowallData = (Button) findViewById(R.id.buttonShowAllData);
        iconHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to HomePage");
                Intent intent = new Intent(HeartRateActivity.this, PatientHomeActivity.class);
                startActivity(intent);
            }
        });
        measureNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HeartRateActivity.this,HearRateDataActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ShowallData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HeartRateActivity.this,ShowDataActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
