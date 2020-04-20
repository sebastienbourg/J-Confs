package io.github.oliviercailloux.jconfs.conference;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashSet;
import java.util.Set;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;

/**
 * This class allows to read and iCalelndar file and creates a conference object
 * from a parsed iCalendar file
 * 
 */
public class ConferenceReader {

	/**
	 * Parse an iCalendar object with ical4j API function took from source :
	 * https://www.programcreek.com/java-api-examples/?api=net.fortuna.ical4j.model.property.Method
	 * 
	 * @param filePath
	 * @throws IOException
	 * @throws ParserException
	 */

	public static void readCalendarFile(Reader read) throws IOException, ParserException, NumberFormatException {
		CalendarBuilder builder = new CalendarBuilder();
		Calendar calendar = builder.build(read);
		for (Component component : calendar.getComponents()) {
			System.out.println("Component [" + component.getName() + "]");
			for (Property property : component.getProperties()) {
				System.out.println("Property [" + property.getName() + ", " + property.getValue() + "]");
			}
		}
	}

	/**
	 * We parse a calendar component to create a conference
	 * 
	 * @param confCompo it's a calendar component that contains the data of one
	 *                  conference
	 * @return a conference
	 * @throws InvalidConferenceFormatException
	 * @throws IOException
	 * @throws ParserException
	 * @throws NumberFormatException
	 */
	public static Conference createConference(Component confCompo) throws InvalidConferenceFormatException {
		Conference conf = null;
		URL confURL;
		String[] location;
		String[] description;
		Double feeRegistration = null;

		try {
			confURL = new URL(confCompo.getProperty("URL").getValue());
		} catch (MalformedURLException e1) {
			throw new InvalidConferenceFormatException("URL malformated, impossible to put in a conference", e1);
		}

		location = confCompo.getProperty("LOCATION").getValue().split(",");
		description = confCompo.getProperty("DESCRIPTION").getValue().split("/");

		for (String ele : description) {
			if (ele.contains("Fee")) {
				feeRegistration = Double.parseDouble(ele.substring(ele.indexOf(":") + 1));
			}
		}
		
		String title = confCompo.getProperty("SUMMARY").getValue();
		String city = location[0];
		String country = location[1];
		String stringDTSTART = convertDate(confCompo.getProperty("DTSTART").getValue());
		String stringDTEND = convertDate(confCompo.getProperty("DTEND").getValue());
		String uid = confCompo.getProperty("UID").getValue();
		LocalDate start = null;
		LocalDate end = null;

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			start = LocalDate.parse(stringDTSTART, formatter);
			end = LocalDate.parse(stringDTEND, formatter);
		} catch (Exception e) {
			throw new IllegalArgumentException("Date impossible to put in the conference", e);
		}

		conf = new Conference(uid, confURL, title, start, end, feeRegistration, country, city);
		return conf;
	}

	/**
	 * We will import a set of conferences contain in a ical
	 * 
	 * @param read contain data of the user's calendar
	 * @return a list of the conferences of the user
	 * @throws IOException
	 * @throws ParserException
	 * @throws NumberFormatException
	 * @throws InvalidConferenceFormatException
	 */
	public static Set<Conference> readConferences(Reader reader)
			throws InvalidConferenceFormatException, IOException, ParserException {
		CalendarBuilder builder = new CalendarBuilder();
		Calendar calendar = builder.build(reader);
		Set<Conference> listeconfuser = new LinkedHashSet<>();
		ComponentList<CalendarComponent> conflist = calendar.getComponents(Component.VEVENT);
		for (int i = 0; i < conflist.size(); i++) {
			listeconfuser.add(createConference(conflist.get(i)));
		}
		return listeconfuser;

	}

	/**
	 * this function transform a date to an another pattern of date
	 * 
	 * @param date Not <code>null</code> it's the date that we want to change its
	 *             pattern.
	 * 
	 * @return dateformated it's the date with the good pattern
	 */
	public static String convertDate(String date) {
		DateTimeFormatter formatBefore = DateTimeFormatter.ofPattern("yyyyMMdd");
		DateTimeFormatter formatAfter = DateTimeFormatter.ofPattern("dd/MM/yyy");
		String dateformated = LocalDate.parse(date, formatBefore).format(formatAfter);
		return dateformated;
	}

}
