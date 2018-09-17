package com.example.anna.ses_1b_group2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivities;

public class heartRateList extends ArrayAdapter<heartRateData>{

    private Activity context;
    private List<heartRateData> heartlist;

    public heartRateList(Activity context, List<heartRateData> heartlist)
    {
        super(context,R.layout.custom_data_view,heartlist);
        this.context =context;
        this.heartlist=heartlist;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem=inflater.inflate(R.layout.custom_data_view,null, true);
        TextView date = (TextView) listViewItem.findViewById(R.id.textViewDate);
        TextView time = (TextView) listViewItem.findViewById(R.id.textViewTime);
        TextView HeartRate = (TextView) listViewItem.findViewById(R.id.textViewHeartRate);
        TextView minValue = (TextView) listViewItem.findViewById(R.id.textViewMinValue);
        TextView maxValue = (TextView) listViewItem.findViewById(R.id.textViewMaxValue);
        Button send = (Button) listViewItem.findViewById(R.id.buttonSend);
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
        date.setText(date2);
        time.setText(time1 );
        HeartRate.setText(hrd.getHeartrate());
        minValue.setText(hrd.getMin());
        maxValue.setText(hrd.getMax());

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   Intent i = new Intent(context, ActivitySelectDoctor.class);

                   i.putExtra("patient", hrd);

                    context.startActivity(i);
                    context.finish();

                }
            });
        return listViewItem;
    }
}
