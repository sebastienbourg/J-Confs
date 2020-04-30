package io.github.oliviercailloux.jconfs.calendar;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.github.caldav4j.CalDAVCollection;
import com.github.caldav4j.CalDAVConstants;
import com.github.caldav4j.exceptions.CalDAV4JException;
import com.github.caldav4j.methods.CalDAV4JMethodFactory;
import com.github.caldav4j.model.request.CalendarQuery;
import com.github.caldav4j.util.GenerateQuery;
import com.github.caldav4j.util.ICalendarUtils;

import io.github.oliviercailloux.jconfs.conference.Conference;
import io.github.oliviercailloux.jconfs.conference.ConferenceReader;
import io.github.oliviercailloux.jconfs.conference.InvalidConferenceFormatException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.Created;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStamp;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.LastModified;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Sequence;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Url;

/**
 *  @author machria & sbourg
 *  Builder which create the connection with online calendar
 */
public class CalendarBuilder {
	protected String url;
	protected String username;
	protected String password;
	protected String calendarId;
	protected CredentialsProvider credsProvider = new BasicCredentialsProvider();
	protected CloseableHttpClient httpclient;
	protected HttpHost hostTarget;	
	protected CalDAVCollection collectionCalendarsOnline;
	protected static int port=443;
	protected String postUrl;
	public static CalendarBuilder given(String url, String postUrl, UserCredentials user) {
		return new CalendarBuilder(url, user.getUsername(), user.getPassword(), user.getCalendarId(), postUrl) ;
	}

	/**
	 * Constructor for a generic calendar object 
	 * @param url
	 * @param userName
	 * @param password
	 * @param calendarID
	 * @param port
	 * @param postUrl
	 */
	private CalendarBuilder(String url, String userName, String password, String calendarID,String postUrl) {
		this.url = url;
		this.username = userName;
		this.password = password;
		this.calendarId = calendarID;
		this.postUrl=postUrl;
		this.credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(this.url, port),
				new UsernamePasswordCredentials(this.username, this.password));
		httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		hostTarget = new HttpHost(this.url, port, "https");
		collectionCalendarsOnline = new CalDAVCollection(this.postUrl+"/calendars/" + this.username + "/" + this.calendarId,
				hostTarget, new CalDAV4JMethodFactory(), null);
	}
	
	
	public static void main(String args[]) throws CalDAV4JException, URISyntaxException, ParseException, InvalidConferenceFormatException, IOException {
		UserCredentials a = new UserCredentials();
		a.readFile();
		CalendarBuilder b = given("ppp.woelkli.com", "/remote.php/dav", a) ;
		CalendarOnline c = new CalendarOnline(b) ;
		LocalDate start = null;

		 LocalDate end = null;

		 String uid = "4e14d618-1d93-29a3-adb3-2c21dca5ee67";

		 try {

		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		 start = LocalDate.parse("28/04/2020", formatter);

		 end = LocalDate.parse("29/04/2020", formatter);

		 } catch (Exception e) {

		 throw new IllegalArgumentException("Date impossible to put in the conference", e);

		}

		 Conference conference = new Conference(uid, new URL("http://fruux.com"), "Java formation", start, end, 1.36,

		 "France", "Paris");

		 c.addOnlineConference(conference);
		 
		 
		 Set<Conference> var = c.getOnlineConferences();
		 for (Conference calendar : var) {

			 System.out.println(calendar.toString());

			}
	}




}
