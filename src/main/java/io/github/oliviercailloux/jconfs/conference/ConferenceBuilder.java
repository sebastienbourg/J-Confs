package io.github.oliviercailloux.jconfs.conference;

import java.net.URL;
import java.time.Instant;
import java.util.Objects;

import io.github.oliviercailloux.jconfs.calendar.CalendarBuilder;
import io.github.oliviercailloux.jconfs.calendar.UserCredentials;

public class ConferenceBuilder {

	private URL url;
	private String uid;
	private String title;
	private Instant startDate;
	private Instant endDate;
	private double registrationFee;
	private String country;
	private String city;
	public static ConferenceBuilder given(String url, String postUrl, UserCredentials user) {
		return new ConferenceBuilder(url, user.getUsername(), user.getPassword(), user.getCalendarId(), postUrl) ;
		
	}
	
	private ConferenceBuilder(String uid, URL url, String title, Instant startDate, Instant endDate, double registrationFee,
			String country, String city) {
		Objects.requireNonNull(endDate);
		Objects.requireNonNull(startDate);
		this.uid = uid;
		this.url = url;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.registrationFee = registrationFee;
		this.country = country;
		this.city = city;
	}

	public ConferenceBuilder(String url2, String username, String password, String calendarId, String postUrl) {
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	

}
