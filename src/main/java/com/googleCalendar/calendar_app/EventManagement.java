package com.googleCalendar.calendar_app;

import Models.ScheduledEvents;
import Models.Slots;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.stereotype.Component;
import com.google.api.services.calendar.model.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;

public final class EventManagement {

    public static Event createEvent(String summary , String Location , String Description , DateTime startdate , DateTime enddate , String Timezone){
        Event event = new Event()
                .setSummary(summary)
                .setLocation(Location)
                .setDescription(Description);

        EventDateTime start = new EventDateTime()
                .setDateTime(startdate)
                .setTimeZone(Timezone);

        EventDateTime end = new EventDateTime()
                .setDateTime(enddate)
                .setTimeZone(Timezone);

        event.setStart(start);
        event.setEnd(end);
        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
        event.setRecurrence(Arrays.asList(recurrence));
        return event;

    }


    class DateComparator implements Comparator<ScheduledEvents>{
        @Override
        public int compare(ScheduledEvents o1, ScheduledEvents o2) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            try {
                return dateFormat.parse(o1.getEnddate().toString()).compareTo(dateFormat.parse(o2.getEnddate().toString()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }


    }

    // Sort all the events based on end time of all the events;
    public List<Slots> getEmptySlots(List<ScheduledEvents> scheduledEvents) throws ParseException {
        List<Slots> emptyslots= new ArrayList<>();
        Comparator<ScheduledEvents> datecomparator = new DateComparator();

        Collections.sort(scheduledEvents ,datecomparator);

        // considering the case of overlapping intervals as well


        DateTime starttime = scheduledEvents.get(0).getEnddate();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        for (int i = 1 ; i < scheduledEvents.size() ; i++){
            if (simpleDateFormat.parse(scheduledEvents.get(i).getStartdate().toString())!=simpleDateFormat.parse(starttime.toString())){
                emptyslots.add(new Slots(starttime , scheduledEvents.get(i).getStartdate()));
                starttime = scheduledEvents.get(i).getEnddate();
            }
            System.out.println(scheduledEvents.get(i).getStartdate() + " " + scheduledEvents.get(i).getEnddate());
        }


        return emptyslots;

    }
}
