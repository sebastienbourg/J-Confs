package io.github.oliviercailloux.conference;

import static org.junit.jupiter.api.Assertions.assertEquals;

//import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.jconfs.conference.Conference;
import io.github.oliviercailloux.jconfs.conference.ConferencesFromICal;
import io.github.oliviercailloux.jconfs.conference.InvalidConferenceFormatException;
import net.fortuna.ical4j.data.ParserException;

public class ConferencesFromICalTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConferencesFromICalTest.class);

	/**
	 * this method test retrive method in ConferencesFromIcal
	 * 
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws ParserException
	 * @throws InvalidConferenceFormatException
	 */

	public final void retriveTest() throws Exception {
		ConferencesFromICal testConfFromIcal = new ConferencesFromICal();

		// TEST 1
		Set<Conference> setConfTest1 = testConfFromIcal.retrieve("Calendartest2");

		assertEquals(4, setConfTest1.size());
		LOGGER.debug("setConfTest1 size is 4");

		Conference confTest1 = setConfTest1.iterator().next();
		assertEquals("Java", confTest1.getTitle());
		LOGGER.debug("test 1 done");

	}

	public final void retriveTest2() throws Exception {
		ConferencesFromICal testConfFromIcal = new ConferencesFromICal();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		// TEST 2
		Set<Conference> setConfTest2 = testConfFromIcal.retrieve(Instant.parse("2020-07-25T16:22:52.966Z"),
				Instant.parse("2020-07-25T16:22:52.966Z"));
		assertEquals(2, setConfTest2.size());
		LOGGER.debug("setConfTest2 size is 2");
		Iterator<Conference> iteratorTest = setConfTest2.iterator();

		Conference confTest21 = iteratorTest.next();
		assertEquals("Chine", confTest21.getCountry());
		Conference confTest22 = iteratorTest.next();

		assertEquals("Japon", confTest22.getCountry());

	}
}
