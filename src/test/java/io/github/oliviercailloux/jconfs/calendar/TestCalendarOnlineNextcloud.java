package io.github.oliviercailloux.jconfs.calendar;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
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
import io.github.oliviercailloux.jconfs.conference.Conference.ConferenceBuilder;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.util.RandomUidGenerator;

/**
 * Unit tests for connect to a calendar on Nextcloud platform
 * 
 * @author machria & sbourg
 */

public class TestCalendarOnlineNextcloud {
	static String uidpr = new RandomUidGenerator().generateUid().getValue();

	@Test
	public void testGetOnlineConferenceFromUid() throws Exception {

		CalendarOnline instanceCalendarOnline = new CalendarOnline(new CalDavCalendarGeneric("us.cloudamo.com",
				"sebastien.bourg@dauphine.eu", "600bec84476fb1", "b", "/remote.php/dav"));
		String uidSearch = "4936861f-c1db-4059-82a2-2c1e421ad5fa";
		Optional<Conference> potentialConference;
		potentialConference = instanceCalendarOnline.getConferenceFromUid(uidSearch);
		if (potentialConference.isPresent()) {
			Conference conferenceFound = potentialConference.get();
			assertEquals("Java formation", conferenceFound.getTitle());
			assertEquals(uidSearch, conferenceFound.getUid());
			assertEquals("Paris", conferenceFound.getCity());
			assertEquals("France", conferenceFound.getCountry());
			assertEquals(Instant.parse("2019-08-06T00:00:00Z"), conferenceFound.getStartDate());
			assertEquals(136, conferenceFound.getFeeRegistration().get());
		} else {
			fail(new NullPointerException());
		}
	}

	@Test
	public void testGetAllOnlineConferences() throws Exception {

		CalendarOnline instanceCalendarOnline = new CalendarOnline(new CalDavCalendarGeneric("us.cloudamo.com",
				"sebastien.bourg@dauphine.eu", "600bec84476fb1", "b", "/remote.php/dav"));
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
		CalendarOnline instanceCalendarOnline = new CalendarOnline(new CalDavCalendarGeneric("us.cloudamo.com",
				"sebastien.bourg@dauphine.eu", "600bec84476fb1", "b", "/remote.php/dav"));
		URL url = new URL("http://fruux.com");
		String city = "Paris";
		String country = "France";
		String endDate = "08/08/2019";
		Double feeRegistration = 1.36;
		String startDate = "06/08/2019";
		String title = "Java formation";
		String uid = "4e14d618-1d93-29a3-adb3-2c21dca5ee67";
		LocalDate start = null;
		LocalDate end = null;

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			start = LocalDate.parse(startDate, formatter);
			end = LocalDate.parse(endDate, formatter);
		} catch (Exception e) {
			throw new IllegalArgumentException("Date impossible to put in the conference", e);
		}

		ConferenceBuilder theBuild = new ConferenceBuilder();
		Conference conference = theBuild.setUid(uid).setUrl(url).setTitle(title)
				.setStartDate(start.atStartOfDay(ZoneOffset.UTC).toInstant())
				.setEndDate(end.atStartOfDay(ZoneOffset.UTC).toInstant()).setRegistrationFee(feeRegistration.intValue())
				.setCity(city).setCountry(country).build();

		conferenceVEvent = instanceCalendarOnline.conferenceToVEvent(conference);

		assertEquals(conferenceVEvent.getProperty(Property.SUMMARY).getValue(), conference.getTitle());
		assertEquals(conferenceVEvent.getProperty(Property.LOCATION).getValue(),
				conference.getCity() + "," + conference.getCountry());
		assertEquals(conferenceVEvent.getProperty(Property.UID).getValue(), conference.getUid());
		assertEquals(conferenceVEvent.getProperty(Property.DESCRIPTION).getValue(),
				"Fee:" + conference.getFeeRegistration().get());
	}

	@Test
	public void testAddOnlineConference() throws Exception {
		CalendarOnline instanceCalendarOnline = new CalendarOnline(new CalDavCalendarGeneric("us.cloudamo.com",
				"sebastien.bourg@dauphine.eu", "600bec84476fb1", "b", "/remote.php/dav"));
		LocalDate start = null;
		LocalDate end = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			start = LocalDate.parse("06/08/2019", formatter);
			end = LocalDate.parse("08/08/2019", formatter);
		} catch (Exception e) {
			throw new IllegalArgumentException("Date impossible to put in the conference", e);
		}

		ConferenceBuilder theBuild = new ConferenceBuilder();
		Conference conference = theBuild.setUid(uidpr).setUrl(new URL("http://fruux.com")).setTitle("Java formation")
				.setStartDate(start.atStartOfDay(ZoneOffset.UTC).toInstant())
				.setEndDate(end.atStartOfDay(ZoneOffset.UTC).toInstant()).setRegistrationFee(136).setCity("Paris")
				.setCountry("France").build();

		instanceCalendarOnline.addOnlineConference(conference);
		Optional<Conference> confTest = instanceCalendarOnline.getConferenceFromUid(uidpr);
		assertTrue(confTest.isPresent());
	}

	@Test
	public void testDelete() throws Exception {
		CalendarOnline instanceCalendarOnline = new CalendarOnline(new CalDavCalendarGeneric("us.cloudamo.com",
				"sebastien.bourg@dauphine.eu", "600bec84476fb1", "b", "/remote.php/dav"));
		instanceCalendarOnline.deleteOnlineConference(uidpr);
		System.out.println(instanceCalendarOnline.getOnlineConferences());
		if (instanceCalendarOnline.getConferenceFromUid(uidpr).isPresent()) {
			fail();
		}
	}

}
