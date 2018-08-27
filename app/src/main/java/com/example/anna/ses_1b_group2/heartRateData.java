package com.example.anna.ses_1b_group2;

public class heartRateData {


    String min, max, Value_date, heartrate, speed, distance;

    public heartRateData()
    {

    }

    public heartRateData(String min, String max, String dtvalue, String heartrate, String speed, String distance) {
        this.min = min;
        this.max = max;
        Value_date = dtvalue;
        this.heartrate = heartrate;
        this.speed = speed;
        this.distance = distance;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }


    public String getValueDate() {
        return Value_date;
    }


    public String getHeartrate() {
        return heartrate;
    }


}
