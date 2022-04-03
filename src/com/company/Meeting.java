package com.company;

import java.text.ParseException;
import java.text.SimpleDateFormat;

class Meeting implements Comparable{
    private String startTime;
    private String endTime;

    public Meeting(String start, String end) {
        this.startTime = start;
        this.endTime = end;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }


    public String toString() {
        return "[ " + startTime + "," + endTime + " ]";
    }



    @Override
    public int compareTo(Object  o) {
        String compareEndTime=((Meeting)o).getEndTime();
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm");
        try {
            return ((int)formatter.parse(this.endTime).getTime()-(int)formatter.parse(compareEndTime).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
