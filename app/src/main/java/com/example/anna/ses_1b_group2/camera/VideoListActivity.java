package com.example.anna.ses_1b_group2.camera;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.anna.ses_1b_group2.R;

import static android.support.constraint.Constraints.TAG;

public class VideoListActivity extends Activity{
        private ListView listView;
        private Button btnAdd;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_video_list);
            listView = (ListView) findViewById(R.id.listView1);
            btnAdd = findViewById(R.id.add);


            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: navigating to VideoListActivity");
                    Intent intent = new Intent(VideoListActivity.this, CameraActivity.class);
                    startActivity(intent);
                }
            });
/*
            //步骤1 一个列表项的内容，就是一个item
            Map<String, Object> item1 = new HashMap<String, Object>();
            item1.put("image", R.drawable.vae);
            item1.put("name", "许嵩");
            //步骤1：一个列表项的内容，就是一个item，即一个Map
            Map<String, Object> item2 = new HashMap<String, Object>();
            item2.put("image", R.drawable.smyh);
            item2.put("name", "生命壹号");

            //步骤2：把这些Map放到List当中
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
            data.add(item1);
            data.add(item2);

            //注意：第四个参数和第五个参数要一一对应
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, data,
                    R.layout.list_item, new String[] { "image", "name" },
                    new int[] { R.id.imageView1, R.id.textView1 });

            //步骤3：将List中的内容填充到listView里面去
            listView.setAdapter(simpleAdapter);
        }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }*/
    }
}
