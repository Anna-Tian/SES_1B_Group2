package com.example.anna.ses_1b_group2.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.anna.ses_1b_group2.R;
import com.example.anna.ses_1b_group2.models.Doctor;
import com.example.anna.ses_1b_group2.models.UserProfile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendProfileActivity extends AppCompatActivity {
    private static final String TAG = "SendProfileActivity";
    private Context mContext = SendProfileActivity.this;
    private RecyclerView docList;
    private ImageView iconBack;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_send_profile);
        Log.d(TAG, "onCreate: starting");


        TextView txt = (TextView) findViewById(R.id.textView_selectDoc);
        iconBack = (ImageView) findViewById(R.id.backArrow);
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to Profile page");
                Intent intent = new Intent(mContext, ProfileActivity.class);
                startActivity(intent);
            }
        });

        myRef = FirebaseDatabase.getInstance().getReference().child(mContext.getString(R.string.dbname_doctor));
        myRef.keepSynced(true);

        docList = (RecyclerView) findViewById(R.id.recyclerView);
//        docList.setHasFixedSize(true);
        docList.setLayoutManager(new LinearLayoutManager(this));
    }


    //get doctor name and medical field from  Firebase
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Doctor, DocViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Doctor, DocViewHolder>
                (Doctor.class, R.layout.snippet_d_name_field, DocViewHolder.class, myRef) {
            @Override
            protected void populateViewHolder(DocViewHolder viewHolder, final Doctor model, int position) {
                viewHolder.setUsername(model.getUsername());
                viewHolder.setMedical_field(model.getMedical_field());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, OneDoctor.class);
                        intent.putExtra("topDoctorName", model.getUsername());
                        intent.putExtra("doctorID", model.getUser_id());
                        startActivity(intent);
                    }
                });
            }
        };
        docList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class DocViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "DocViewHolder";
        View mView;

        public DocViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setUsername(String username){
            TextView doc_name = (TextView)mView.findViewById(R.id.doctorName);
            doc_name.setText(username);
        }

        public void setMedical_field(String medical_field){
            TextView medicalField = (TextView)mView.findViewById(R.id.tv_medical_field);
            medicalField.setText(medical_field);
        }
    }
}
