package com.googleCalendar.calendar_app;

import Models.ScheduledEvents;
import Models.Slots;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.*;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.text.SimpleDateFormat;

import static com.fasterxml.jackson.databind.cfg.CoercionInputShape.Array;

@SpringBootApplication


public class CalendarAppApplication {

	public static void createEvents(String summary , String Location , String Description , DateTime startdate , DateTime enddate , String Timezone ) throws Exception {
		Event event = EventManagement.createEvent(summary ,Location , Description , startdate , enddate , Timezone );
		Calendar service = CalendarService.getSingletonService().service;

		String calendarId = "primary";
		event = service.events().insert(calendarId , event).execute();

		System.out.println(event.getDescription());

	}

	public static void main(String[] args) throws Exception {

		SpringApplication.run(CalendarAppApplication.class, args);
		EventManagement evm  = new EventManagement();

		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

		Calendar service = CalendarService.getSingletonService().service;
		DateTime now = new DateTime(System.currentTimeMillis());

		Events events = service.events().list("primary")
				.setMaxResults(10)
				.setTimeMin(now)
				.setOrderBy("startTime")
				.setSingleEvents(true)
				.execute();
		List<Event> items = events.getItems();
		List<ScheduledEvents> scheduledEvents = new ArrayList<>();

		ObjectMapper objectMapper = new ObjectMapper();
		for (int i = 0 ; i < items.size() ; i++){
		//	Map<String , Object> startinterval = objectMapper.readValue(items.get(i).getStart(), new TypeReference<Map<String,Object>>(){});
			DateTime startdate = new DateTime(String.valueOf(items.get(i).getStart().getDateTime()));
			DateTime enddate = new DateTime(String.valueOf(items.get(i).getEnd().getDateTime()));
			scheduledEvents.add(new ScheduledEvents(startdate , enddate , items.get(i).getSummary()));
		}

		List<Slots> t = evm.getEmptySlots(scheduledEvents);


		System.out.println("Following are the available slots for booking");

		for (Slots slots : t){
			System.out.println(slots.getStartdate() + " " + slots.getEnddate());
		}



		String summary = "Available slots can be booked";
		DateTime startdate = new DateTime("2023-10-08T12:00:00-07:00");
		DateTime enddate = new DateTime("2023-10-10T17:00:00-07:00");
		String timezone = "IST";


		createEvents(summary , "Bangalore" , "Test events for scheduling" , t.get(0).getStartdate() , t.get(0).getEnddate() , timezone);


	}



}
