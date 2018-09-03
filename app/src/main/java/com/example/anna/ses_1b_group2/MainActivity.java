package com.example.anna.ses_1b_group2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button measureNow = (Button) findViewById(R.id.buttonMeasureNow);
        Button ShowallData = (Button) findViewById(R.id.buttonShowAllData);
        measureNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,HearRateDataActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ShowallData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ShowDataActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
