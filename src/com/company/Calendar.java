package com.company;

import java.util.List;

public class Calendar {
    private List<Meeting> bookedMeetings;
    private String minRange;
    private String maxRange;

    public Calendar(List<Meeting> bookedMeetings, String minRange, String endHour) {
        this.bookedMeetings = bookedMeetings;
        this.minRange = minRange;
        this.maxRange = endHour;
    }

    public List<Meeting> getBookedMeetings() {
        return bookedMeetings;
    }

    public String getMinRange() {
        return minRange;
    }

    public String getMaxRange() {
        return maxRange;
    }




}
