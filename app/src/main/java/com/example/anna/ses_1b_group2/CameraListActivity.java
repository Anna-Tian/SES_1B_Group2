package com.example.anna.ses_1b_group2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CameraListActivity extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);



    }
    public void onClick(View view){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }
}
