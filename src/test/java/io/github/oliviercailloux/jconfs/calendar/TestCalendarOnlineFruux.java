package io.github.oliviercailloux.jconfs.calendar;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.github.caldav4j.exceptions.CalDAV4JException;

import io.github.oliviercailloux.jconfs.calendar.CalendarOnline;
import io.github.oliviercailloux.jconfs.conference.Conference;
import io.github.oliviercailloux.jconfs.conference.InvalidConferenceFormatException;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;

/**
 *  @author machria & sbourg
 *  Unit tests for connect to a calendar on Fruux cloud platform
 */

public class TestCalendarOnlineFruux {

	@Test
	public void testGetOnlineConferenceFromUid()
			throws Exception {

		UserCredentials userFruux = new UserCredentials();
		userFruux.setPath("./src/main/resources/ConfigTestFruux.txt");
		userFruux.readFile();
		CalendarOnline instanceCalendarOnline = new CalendarOnline(CalendarBuilder.given("dav.fruux.com", "", userFruux));
		String uidSearch = "b8e5f0dc-5a69-4fd5-bde3-f38e0f986085";
		Optional<Conference> potentialConference;
		potentialConference = instanceCalendarOnline.getConferenceFromUid(uidSearch);
		if (potentialConference.isPresent()) {
			Conference conferenceFound = potentialConference.get();
			assertEquals("Java presentation",conferenceFound.getTitle());
			assertEquals(uidSearch,conferenceFound.getUid());
			assertEquals("Paris",conferenceFound.getCity());
			assertEquals("France",conferenceFound.getCountry());
			assertEquals("2019-07-01",conferenceFound.getStartDate().toString());
			assertEquals("1.36",conferenceFound.getFeeRegistration()+"");
		}
		else {
			fail(new NullPointerException());
		}
	}

	@Test
	public void testGetAllOnlineConferences()
			throws Exception {

		UserCredentials userFruux = new UserCredentials();
		userFruux.setPath("./src/main/resources/ConfigTestFruux.txt");
		userFruux.readFile();
		CalendarOnline instanceCalendarOnline = new CalendarOnline(CalendarBuilder.given("dav.fruux.com", "", userFruux));
		Set<Conference> collectionConferences = instanceCalendarOnline.getOnlineConferences();
		Iterator<Conference> iteratorConf = collectionConferences.iterator();
		while (iteratorConf.hasNext()) {
			Conference conferenceOnline = iteratorConf.next();
			System.out.println(conferenceOnline.toString());
		}
	}

	@Test
	public void testConferenceToVEvent() throws Exception {
		VEvent conferenceVEvent;
		UserCredentials userFruux = new UserCredentials();
		userFruux.setPath("./src/main/resources/ConfigTestFruux.txt");
		userFruux.readFile();
		CalendarOnline instanceCalendarOnline = new CalendarOnline(CalendarBuilder.given("dav.fruux.com", "", userFruux));
		URL url = new URL("http://fruux.com");
		String city = "Paris";
		String country = "France";
		String endDate = "08/08/2019";
		double feeRegistration = 1.36;
		String startDate = "06/08/2019";
		String title = "Java formation";
		String uid = "4e14d618-1d93-29a3-adb3-2c21dca5ee67";
		Instant start_ = null;
		Instant end_ = null;

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			start_ = Instant.parse(startDate);
			end_ = Instant.parse(endDate);
		} catch (Exception e) {
			throw new IllegalArgumentException("Date impossible to put in the conference", e);
		}

		Conference conference = new Conference(uid, url, title, start_, end_, feeRegistration, country, city);
		
		conferenceVEvent = instanceCalendarOnline.conferenceToVEvent(conference);

		assertEquals(conferenceVEvent.getProperty(Property.SUMMARY).getValue(), conference.getTitle());
		assertEquals(conferenceVEvent.getProperty(Property.LOCATION).getValue(),
				conference.getCity() + "," + conference.getCountry());
		assertEquals(conferenceVEvent.getProperty(Property.UID).getValue(), conference.getUid());
		assertEquals(conferenceVEvent.getProperty(Property.DESCRIPTION).getValue(),
				"Fee:" + conference.getFeeRegistration());
	}

	@Test
	public void testAddOnlineConference() throws Exception {
		UserCredentials userFruux = new UserCredentials();
		userFruux.setPath("./src/main/resources/ConfigTestFruux.txt");
		userFruux.readFile();
		CalendarOnline instanceCalendarOnline = new CalendarOnline(CalendarBuilder.given("dav.fruux.com", "", userFruux));
		Instant start_ = null;
		Instant end_ = null;
		String uid = "4e14d618-1d93-29a3-adb3-2c21dca5ee68";
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			start_ = Instant.parse("2020-05-25T16:22:52.966Z");
			end_ = Instant.parse("2020-07-25T16:22:52.966Z");
		} catch (Exception e) {
			throw new IllegalArgumentException("Date impossible to put in the conference", e);
		}
		Conference conference = new Conference(uid, new URL("http://fruux.com"), "Java formation", start_, end_, 1.36,
				"France", "Paris");
		instanceCalendarOnline.addOnlineConference(conference);
		Optional<Conference> confTest = instanceCalendarOnline.getConferenceFromUid(uid);
		if(!confTest.isPresent()) {
			fail();
		}
	}

	@Test
	public void testDelete() throws Exception {
		String uid = "4e14d618-1d93-29a3-adb3-2c21dca5ee68";
		UserCredentials userFruux = new UserCredentials();
		userFruux.setPath("./src/main/resources/ConfigTestFruux.txt");
		userFruux.readFile();
		CalendarOnline instanceCalendarOnline = new CalendarOnline(CalendarBuilder.given("dav.fruux.com", "", userFruux));
		instanceCalendarOnline.deleteOnlineConference(uid);
		System.out.println(instanceCalendarOnline.getOnlineConferences());
		if(instanceCalendarOnline.getConferenceFromUid(uid).isPresent()) {
			fail();
		}
	}

}