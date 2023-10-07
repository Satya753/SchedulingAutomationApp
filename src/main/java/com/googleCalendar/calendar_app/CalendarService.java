package com.googleCalendar.calendar_app;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public final class CalendarService {
    private static CalendarService calendarservice;
    public  Calendar service;
    private static final String APPLICATION_NAME = "Calendar auth";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private  static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    private static final String CREDENTIALS_DIRECTORY_PATH = "/credentials2.json";


    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws Exception {

        InputStream in;

        in = CalendarQuickStart.class.getResourceAsStream(CREDENTIALS_DIRECTORY_PATH);

        if (in==null)
            throw new FileNotFoundException("RESOURCE NOT FOUND" + CREDENTIALS_DIRECTORY_PATH);

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY , new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT , JSON_FACTORY , clientSecrets , SCOPES
        ).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow , receiver).authorize("user");


        return credential;
    }
    private CalendarService() throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        this.service = new Calendar.Builder(HTTP_TRANSPORT , JSON_FACTORY , getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

    }
    public static CalendarService getSingletonService() throws Exception {
        if (calendarservice==null)
            calendarservice = new CalendarService();

        return calendarservice;
    }
}
