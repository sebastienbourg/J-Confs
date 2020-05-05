package io.github.oliviercailloux.conference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.validation.constraints.AssertTrue;

import io.github.oliviercailloux.jconfs.calendar.CalendarOnline;
import io.github.oliviercailloux.jconfs.conference.Conference;
import io.github.oliviercailloux.jconfs.conference.ConferenceReader;
import io.github.oliviercailloux.jconfs.conference.ConferencesFromICal;
import io.github.oliviercailloux.jconfs.conference.InvalidConferenceFormatException;
import net.fortuna.ical4j.data.ParserException;

public class TestConferenceReader {

	protected ConferenceReader Confr;
	protected ConferencesFromICal confI;
	protected Set<Conference> setOfConf;

	@Test
	public void testLocalCalendar() throws Exception {
		Confr = new ConferenceReader();
		confI = new ConferencesFromICal();
		setOfConf = new LinkedHashSet<>();
		setOfConf.addAll(confI.retrieve("Calendartest2"));
		Iterator<Conference> iteratorConf = setOfConf.iterator();
		assertEquals(setOfConf.size(), 4);
		Conference conferenceTest = iteratorConf.next();
		assertEquals(conferenceTest.getTitle(), "Java");
		assertEquals(conferenceTest.getCity(), "Toronto");
		assertEquals(conferenceTest.getCountry(), "Canada");
		assertEquals(conferenceTest.getFeeRegistration(), 22.60, 0.001);
	}

}
