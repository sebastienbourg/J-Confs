package io.github.oliviercailloux.jconfs.calendar;


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
public class CalDavCalendarGeneric {
	protected String url;
	protected String userName;
	protected String password;
	protected String calendarID;
	protected CredentialsProvider credsProvider = new BasicCredentialsProvider();
	protected CloseableHttpClient httpclient;
	protected HttpHost hostTarget;	
	protected CalDAVCollection collectionCalendarsOnline;
	protected int port;
	protected String posturl;
	
	
	/**
	 * Constructor for a generic calendar object 
	 * @param url
	 * @param userName
	 * @param password
	 * @param calendarID
	 * @param port
	 * @param postUrl
	 */
	public CalDavCalendarGeneric(String url, String userName, String password, String calendarID,int port,String postUrl) {
		this.url = url;
		this.userName = userName;
		this.password = password;
		this.calendarID = calendarID;
		this.port=port;
		this.posturl=postUrl;
		this.credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(this.url, 443),
				new UsernamePasswordCredentials(this.userName, this.password));
		httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		hostTarget = new HttpHost(this.url, 443, "https");
		collectionCalendarsOnline = new CalDAVCollection(this.posturl+"/calendars/" + this.userName + "/" + this.calendarID,
				hostTarget, new CalDAV4JMethodFactory(), null);
	}
	

	/**
	 * Set the credentials to connect to the calendar on cloud
	 */
	public void setCredentials() {
		credsProvider.setCredentials(new AuthScope(this.url, this.port), new UsernamePasswordCredentials(this.userName, this.password));
	}

}
