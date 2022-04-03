package com.company;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {
    @Test
    void testBookings() throws ParseException {
        List<Meeting> list1 = new ArrayList<Meeting>();
        list1.add(new Meeting("9:00", "10:30"));
        list1.add(new Meeting("12:00", "13:00"));
        list1.add(new Meeting("16:00", "18:00"));
        Calendar calendar1=new Calendar(list1,"7:00","20:00");


        List<Meeting> list2 = new ArrayList<Meeting>();
        list2.add(new Meeting("10:00", "11:30"));
        list2.add(new Meeting("12:30", "14:30"));
        list2.add(new Meeting("14:30", "15:00"));
        list2.add(new Meeting("16:00", "17:00"));
        Calendar calendar2=new Calendar(list2,"7:00","18:30");




        Booking c=new Booking("30",calendar1,calendar2);
        List<Meeting> freeTimesVerified=c.validateFreeIntervals(c.computeFreeTimes(calendar1, calendar2),c.getCalendar1(),c.getCalendar2(), c.getMeetigTimeInMinutes());;
        List<Meeting> expectedOutput = new ArrayList<Meeting>();
        expectedOutput.add(new Meeting("7:00", "9:00"));
        expectedOutput.add(new Meeting("15:00", "16:00"));
        expectedOutput.add(new Meeting("18:00", "18:30"));
        assertEquals(freeTimesVerified.size(), expectedOutput.size());
        for (int i = 0; i < freeTimesVerified.size(); i++)
            assertEquals(freeTimesVerified.get(i).toString(), expectedOutput.get(i).toString());
    }
}