package com.company;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Booking {
    private String meetigTimeInMinutes;
    private Calendar calendar1;
    private Calendar calendar2;

    public Booking(String meetigTimeInMinutes, Calendar calendar1, Calendar calendar2) {
        this.meetigTimeInMinutes = meetigTimeInMinutes;
        this.calendar1 = calendar1;
        this.calendar2 = calendar2;
    }

    public String getMeetigTimeInMinutes() {
        return meetigTimeInMinutes;
    }

    public Calendar getCalendar1() {
        return calendar1;
    }

    public Calendar getCalendar2() {
        return calendar2;
    }

    //aceasta functie ia 2 liste de booked meetings de la 2 persoane diferite, le face append si returneaza lista sortata in functie de start hour
    public static List<Meeting> sortMeetings(Calendar calendar1, Calendar calendar2) throws ParseException {

        List<Meeting> sortedMeetings = new ArrayList<>();
        int limit = calendar1.getBookedMeetings().size();

        if (calendar1.getBookedMeetings().size() <= calendar2.getBookedMeetings().size()) {
            limit = calendar2.getBookedMeetings().size();
        }

        for (int i = 0; i < limit; i++) {
            Meeting meeting1 = null;
            Meeting meeting2 = null;
            if (i < calendar1.getBookedMeetings().size()) {
                meeting1 = calendar1.getBookedMeetings().get(i);
            }

            if (i < calendar2.getBookedMeetings().size()) {
                meeting2 = calendar2.getBookedMeetings().get(i);
            }

            if (meeting1 != null && meeting2 != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("h:mm");
                Date d1 = formatter.parse(meeting1.getStartTime());
                Date d2 = formatter.parse(meeting2.getStartTime());
                long timeDiff = d2.getTime() - d1.getTime();

                if (timeDiff > 0 ) {
                    sortedMeetings.add(meeting1);
                    sortedMeetings.add(meeting2);
                } else {
                    sortedMeetings.add(meeting2);
                    sortedMeetings.add(meeting1);
                }

            } else if (meeting1 == null && meeting2 != null) {
                sortedMeetings.add(meeting2);

            } else if (meeting1 != null && meeting2 == null) {
                sortedMeetings.add(meeting1);
            }
        }

        return sortedMeetings;
    }

    public static List<Meeting> computeFreeTimes(Calendar calendar1, Calendar calendar2) throws ParseException {
        //folosesc acest formatter, deoarece startTime si endTime le-am transmis ca si stringuri, si acestea nu ar putea fi comparate cu -, >, < etc
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm");
        List<Meeting> freeIntervals = new ArrayList<Meeting>();
        List<Meeting> sortedMeetings = sortMeetings(calendar1,calendar2);

        //verfic daca in lista de sortedMeetings primul meeting are start time > minRange al calendarului 1 sau calendarului 2; daca da, adaug in lista de freeIntervals
        Date startTimeFirstMeetingFormatted = formatter.parse(sortedMeetings.get(0).getStartTime());
        Date minRangeCalendar1Formatted = formatter.parse(calendar1.getMinRange());
        Date minRangeCalendar2Formatted = formatter.parse(calendar2.getMinRange());
        Date maxDate;
        String max="";
        if( minRangeCalendar2Formatted.getTime() > minRangeCalendar1Formatted.getTime())
        {
            maxDate=minRangeCalendar2Formatted;
            max=calendar2.getMinRange();
        }
        else
        {
            maxDate=minRangeCalendar1Formatted;
            max=calendar1.getMinRange();
        }
        if(startTimeFirstMeetingFormatted.getTime()>maxDate.getTime())
        {

            Meeting m=new Meeting(max,sortedMeetings.get(0).getStartTime());
            freeIntervals.add(m);
        }


        for (int i = 0; i < sortedMeetings.size(); i++) {
            Meeting meeting1 = null;
            Meeting meeting2 = null;
            if (i < sortedMeetings.size() - 1) {
                meeting1 = sortedMeetings.get(i);
                meeting2 = sortedMeetings.get(i + 1);

                Date meet1EndTimeFormatted = formatter.parse(meeting1.getEndTime());
                Date meet2StartTimeFormatted = formatter.parse(meeting2.getStartTime());
                if ( meet1EndTimeFormatted.getTime() <= meet2StartTimeFormatted.getTime()   ){
                    freeIntervals.add(new Meeting(meeting1.getEndTime(), meeting2.getStartTime()));
                }
            }
        }
        //fac min dintre maxRange ale celor 2 calendare si verific daca in lista de sorted meetings, ultimul meeting are end hour< min
        //daca da, adaug in lista de freeIntervals un nou meeting care are ca startHour, endtime-ul ultimului meeting din lista sortata si end time minimul calculat
        Date endTimeLastMeetingFormatted = formatter.parse(sortedMeetings.get(sortedMeetings.size()-1).getEndTime());
        Date maxRangeCalendar1Formatted = formatter.parse(calendar1.getMaxRange());
        Date maxRangeCalendar2Formatted = formatter.parse(calendar2.getMaxRange());
        Date minDate;
        String minEndTime="";
        if( maxRangeCalendar2Formatted.getTime() < maxRangeCalendar1Formatted.getTime())
        {
            minDate=maxRangeCalendar2Formatted;
            minEndTime=calendar2.getMaxRange();
        }
        else
        {
            minDate=minRangeCalendar1Formatted;
            minEndTime=calendar1.getMaxRange();
        }

        if(endTimeLastMeetingFormatted.getTime() < minDate.getTime())
        {

            Collections.sort(sortedMeetings); //sortez meetingurile in functie de endTime, pentru a putea dac noului meeting startTime-ul corect
            Meeting m=new Meeting(sortedMeetings.get(sortedMeetings.size()-1).getEndTime(),minEndTime);
            freeIntervals.add(m);
        }
        return freeIntervals;
    }




    public List<Meeting> validateFreeIntervals(List<Meeting> freeIntervals, Calendar calendar1, Calendar calendar2, String meetingTimeInMinutes) throws ParseException {
         for (int i=0; i<freeIntervals.size(); i++) {
             //pentru fiecare inetrval din lista de freeIntervals, verific daca depasesc sau nu min.maxRange de la fiecare persoana
             //si daca durata intervalului este >= cu meetingTimeInMinutes(intervalul de timp al meetingului pe care dorim sa il programam, dat ca input)
            Meeting m=freeIntervals.get(i);
            SimpleDateFormat formatter = new SimpleDateFormat("h:mm");
            Date startMeetingFormatted = formatter.parse(m.getStartTime());
            Date endMeetingFormatted = formatter.parse(m.getEndTime());

            Date minRangeCalendar1Formatted=formatter.parse(calendar1.getMinRange());
            Date maxRangeCalendar1Formatted=formatter.parse(calendar1.getMaxRange());

            Date minRangeCalendar2Formatted=formatter.parse(calendar2.getMinRange());
            Date maxRangeCalendar2Formatted=formatter.parse(calendar2.getMaxRange());




            long timeDiff=endMeetingFormatted.getTime()-startMeetingFormatted.getTime();

            long diff1=startMeetingFormatted.getTime() -minRangeCalendar1Formatted.getTime();
            long diff2=startMeetingFormatted.getTime() -minRangeCalendar2Formatted.getTime();
            long diff3=endMeetingFormatted.getTime() -maxRangeCalendar1Formatted.getTime();
            long diff4=endMeetingFormatted.getTime() - maxRangeCalendar2Formatted.getTime();

            if(diff1<0 || diff2<0 || diff3>0 || diff4>0)
            {
                freeIntervals.remove(i);
            }
           else
            {
                if(timeDiff < Integer.parseInt(meetigTimeInMinutes))
                {
                    freeIntervals.remove(i);
                }
            }

        }
        return freeIntervals;

    }

    // metoda care afiseaza o lista de meetings dupa formatul precizat in cerinta
    public static void printIntervalsList(List<Meeting> durationList) {
        System.out.print("[");
        String s="";
        for (Meeting duration : durationList) {
            s=s+duration +",";
        }
        s = s.substring(0, s.length() - 1);

        System.out.print(s);
        System.out.print("]");
    }



}
