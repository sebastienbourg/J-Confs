package io.github.oliviercailloux.jconfs.conference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.google.common.base.Preconditions;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.XComponent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Url;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.model.property.XProperty;
import net.fortuna.ical4j.validate.ValidationException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Property;

public class ConferenceWriter {

	/**
	 * Open the specified ics file as an Calendar object
	 * 
	 * @param calFile not <code>null</code>.
	 * @return not <code>null</code>.
	 * @throws IOException
	 * @throws ParserException
	 */
	private static Calendar openCalendar(String calFile) throws IOException, ParserException {
		Objects.requireNonNull(calFile);
		URL urlcalendar = ConferenceWriter.class.getResource(calFile + ".ics");
		try (FileReader reader = new FileReader(new File(urlcalendar.getFile()))) {
			CalendarBuilder builder = new CalendarBuilder();
			Calendar calendar = builder.build(reader);
			return calendar;
		}
	}

	/**
	 * Delete the conference of the ics file (if it exists)
	 * 
	 * @param calFile    not <code>null</code>.
	 * @param conference not <code>null</code>.
	 * @throws IOException
	 * @throws ParserException
	 * @throws URISyntaxException
	 * @throws InvalidConferenceFormatException
	 */
	public static void deleteConference(String calFile, Conference conference)
			throws IOException, ParserException, URISyntaxException, InvalidConferenceFormatException {
		Objects.requireNonNull(calFile);
		Objects.requireNonNull(conference);

		Calendar calendar = new Calendar();
		Calendar newCalendar;

		Conference tempConf;
		calendar = openCalendar(calFile);

		ComponentList<CalendarComponent> confList = calendar.getComponents("X-CONFERENCE");
		for (int i = 0; i < confList.size(); i++) {
			tempConf = ConferenceReader.createConference(confList.get(i));
			if (tempConf.equals(conference)) {
				confList.remove(i);
			}
		}

		newCalendar = new Calendar(confList);
		saveIcsFile(newCalendar, calFile);
	}

	/**
	 * Add the conference in the ics file file (if it exists)
	 * 
	 * @param calFile    not <code>null</code>.
	 * @param conference not <code>null</code>.
	 * @throws IOException
	 * @throws ParserException
	 * @throws ValidationException
	 * @throws URISyntaxException
	 */
	public static void addConference(String calFile, Conference conference)
			throws IOException, ParserException, ValidationException, URISyntaxException {
		Objects.requireNonNull(calFile);
		Objects.requireNonNull(conference);

		Calendar calendar = new Calendar();
		calendar = openCalendar(calFile);

		// Creating an event
		PropertyList<Property> propertyList = new PropertyList<>();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		propertyList.add(new XProperty("X-DTSTART", formatter.format(conference.getStartDate())));
		propertyList.add(new XProperty("X-DTEND", formatter.format(conference.getEndDate())));

		propertyList.add(new Summary(conference.getTitle()));
		propertyList.add(new XProperty("X-COUNTRY", conference.getCountry().toString()));
		propertyList.add(new XProperty("X-CITY", conference.getCity().toString()));
		propertyList.add(new Url(conference.getUrl().toURI()));
		propertyList.add(new XProperty("X-FEE", conference.getFeeRegistration()+""));

		XComponent meeting = new XComponent("X-CONFERENCE", propertyList);
		// add event to the calendar
		calendar.getComponents().add(meeting);

		// Saving an iCalendar file
		saveIcsFile(calendar, calFile);
	}

	/**
	 * Save the given conference in the ics File
	 * 
	 * @param cal     not <code>null</code>.
	 * @param calFile not <code>null</code>.
	 * @throws URISyntaxException
	 * @throws ValidationException
	 * @throws IOException
	 */
	private static void saveIcsFile(Calendar cal, String calFile)
			throws URISyntaxException, ValidationException, IOException {
		Objects.requireNonNull(calFile);
		Objects.requireNonNull(cal);

		URL resourceUrl = ConferenceWriter.class.getResource(calFile + ".ics");
		File file = new File(resourceUrl.toURI());
		try (FileOutputStream fout = new FileOutputStream(file)) {
			CalendarOutputter outputter = new CalendarOutputter();
			outputter.setValidating(false);
			outputter.output(cal, fout);
		}
	}
}
