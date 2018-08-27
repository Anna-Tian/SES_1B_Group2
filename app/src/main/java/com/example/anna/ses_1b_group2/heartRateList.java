package com.example.anna.ses_1b_group2;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class heartRateList extends ArrayAdapter<heartRateData>{

    private Activity context;
    private List<heartRateData> heartlist;

    public heartRateList(Activity context, List<heartRateData> heartlist)
    {
        super(context, R.layout.custom_data_view,heartlist);
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
        heartRateData hrd = heartlist.get(position);

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
        return listViewItem;
    }
}
