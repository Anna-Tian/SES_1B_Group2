package com.example.anna.ses_1b_group2.feedback;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.anna.ses_1b_group2.R;
import com.example.anna.ses_1b_group2.hr.heartRateData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class customeListView extends ArrayAdapter<heartRateData> {
    private Activity context;
    private List<heartRateData> heartlist;
    public customeListView(Activity context, List<heartRateData> heartlist) {
        super(context, R.layout.d_patient_detail);
        this.context =context;
        this.heartlist=heartlist;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem=inflater.inflate(R.layout.d_patient_detail,null, true);
        Button feedback = (Button) listViewItem.findViewById(R.id.buttonFeedback);
        TextView dateValue= (TextView) listViewItem.findViewById(R.id.textViewDate);
        EditText patientData = (EditText) listViewItem.findViewById(R.id.textViewPatientDetails);

        final heartRateData hrd = heartlist.get(position);

        final int item = position;
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(hrd.getValueDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String time1 = new SimpleDateFormat("H:mm").format(date1);
        String date2 = new SimpleDateFormat("yyyy-MM-dd").format(date1);
        dateValue.setText("ON "+date2+" AT: "+time1+" DATA RECIVED:");
        patientData.setText("Normal Heart Rate:" + hrd.getHeartrate()+ "\nMin Value: "+hrd.getMin()+ "\nMax Value: "+hrd.getMax());


        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, ActivityAddViewFeedBack.class);

                i.putExtra("patientwithdoctor", hrd);

                context.startActivity(i);
                context.finish();

            }
        });
        return listViewItem;

    }
}
