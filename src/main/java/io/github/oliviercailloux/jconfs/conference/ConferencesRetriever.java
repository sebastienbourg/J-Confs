package io.github.oliviercailloux.jconfs.conference;

import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

import net.fortuna.ical4j.data.ParserException;

/**
 * this interface read conference data and return a set of conference
 * 
 * @author stanislas
 *
 */
public interface ConferencesRetriever {

	/**
	 * this method read conference data and return a set of conference
	 * 
	 * @return Set<Conference>
	 * @throws InvalidConferenceFormatException
	 */

	public Set<Conference> retrieve() throws IOException, ParserException, InvalidConferenceFormatException;

	/**
	 * this method search an .ics file located in classpath and return a set of
	 * conferences contain in this file.
	 * 
	 * @param fileName Not <code> null</code>
	 * @return
	 * @throws InvalidConferenceFormatException
	 * @throws ParseException
	 * @throws ParserException
	 */

	public Set<Conference> retrieve(String fileName)
			throws IOException, ParserException, InvalidConferenceFormatException;

	/**
	 * this method take a date interval and return a restricted set of conference
	 * 
	 * @param maxDate not <code> null</code>
	 * @param minDate Not <code> null</code>
	 * @return Set<Conference>,Not <code> null</code>, return empty set if no data
	 *         found
	 * @throws InvalidConferenceFormatException
	 */
	Set<Conference> retrieve(Instant minDate, Instant maxDate)
			throws IOException, ParserException, InvalidConferenceFormatException;

}
