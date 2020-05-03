package io.github.oliviercailloux.jconfs.calendar;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.io.FileInputStream;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import io.github.oliviercailloux.jconfs.conference.Conference;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;

/**
 * This class allows to read and iCalelndar file and creates a conference object
 * from a parsed iCalendar file
 * 
 * @author berkani-mustapha
 *
 */
public class ReadCalendarFiles {

	/**
	 * Parse an iCalendar object with ical4j API function took from source :
	 * https://www.programcreek.com/java-api-examples/?api=net.fortuna.ical4j.model.property.Method
	 * 
	 * @param filePath
	 * @throws IOException
	 * @throws ParserException
	 */
	public static void ReadCalendarFile(String filePath) throws IOException, ParserException {

		try (FileInputStream fin1 = new FileInputStream(filePath)) {

			CalendarBuilder builder = new CalendarBuilder();

			Calendar calendar = builder.build(fin1);

			// Iterating over the calendar component
			for (Iterator<CalendarComponent> i = calendar.getComponents().iterator(); i.hasNext();) {
				Component component = i.next();
				System.out.println("Component [" + component.getName() + "]");
				// Iterating over the component property
				for (Iterator<Property> j = component.getProperties().iterator(); j.hasNext();) {
					Property property = j.next();
					System.out.println("Property [" + property.getName() + ", " + property.getValue() + "]");
				}
			}
		}
	}

	/**
	 * generate a localDate from string parametre This function is needed since the
	 * ical4j property DTSTART is a string so we have to convert it into a localDate
	 * format
	 * 
	 * @param date
	 * @return Instant
	 */
	public static Instant stringToInstant(String date) {
		return Instant.parse(date);
	}

	/**
	 * Creates conference from ics file, function inspired by function
	 * readCalendarFile
	 * 
	 * @param filepath
	 * @return
	 * @throws IOException
	 * @throws ParserException
	 * @throws ValidationException
	 */

	public static Conference createConference(String filepath) throws IOException, ParserException {

		Conference conf = null;
		try (FileInputStream fin2 = new FileInputStream(filepath)) {

			CalendarBuilder builder = new CalendarBuilder();
			Calendar calendar = builder.build(fin2);
			Component confCompo = calendar.getComponent("CONFERENCE");

			// the url is the primary key of a conference
			URL confURL = new URL(confCompo.getProperty("URL").getValue());

			// add the others attributes
			String title = confCompo.getProperty("SUMMARY").getValue();
			String country = confCompo.getProperty("COUNTRY").getValue();
			Double feeRegistration = Double.parseDouble(confCompo.getProperty("FEE").getValue());
			String startDate = confCompo.getProperty("DTSTART").getValue();
			String endDate = confCompo.getProperty("DTEND").getValue();
			String city = confCompo.getProperty("CITY").getValue();
			Instant start = null;
			Instant end = null;

			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				start = Instant.parse(startDate.replace('/', '-'));
				end = Instant.parse(endDate.replace('/', '-'));
			} catch (Exception e) {
				throw new IllegalArgumentException("Date impossible to put in the conference", e);
			}

			conf = new Conference(null, confURL, title, start, end, feeRegistration, country, city);

		}
		return conf;

	}
}
