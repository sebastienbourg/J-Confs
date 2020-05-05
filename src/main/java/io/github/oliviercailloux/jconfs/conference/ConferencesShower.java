package io.github.oliviercailloux.jconfs.conference;

import java.io.IOException;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.EmptyListException;

import net.fortuna.ical4j.data.ParserException;

/**
 * this class show Conferences data
 * 
 * @author stanislas
 *
 */
public class ConferencesShower {
	ConferencesRetriever retriever;

	/**
	 * this is a constructor to initialize a ConferencesShower
	 * 
	 * @param retriever
	 */
	public ConferencesShower(ConferencesRetriever retriever) {
		this.retriever = retriever;

	}

	/**
	 * 
	 * take a filename and search every conference with the title given in arg
	 * 
	 * @param title not <code>null<code>
	 * @return Set<Conference> Not <code>null<code>
	 * 
	 * @throws Exception
	 */
	public Set<Conference> searchConferenceByTitle(String title) throws Exception {
		if (Objects.isNull(title))
			throw new IllegalArgumentException("title is null");
		Conference conf = null;
		Set<Conference> setOfConfFiltred = new LinkedHashSet<>();
		try (Scanner sc = new Scanner(System.in)) {
			System.out.println("please enter a file name");
			String fileName = sc.nextLine();
			Set<Conference> set = retriever.retrieve(Objects.requireNonNull(fileName));

			while (set.iterator().hasNext()) {
				conf = set.iterator().next();
				if (conf.getTitle() == title) {
					setOfConfFiltred.add(conf);
				}
			}

		}
		if (setOfConfFiltred.isEmpty())
			throw new EmptyListException("no conferences found");
		return setOfConfFiltred;
	}

	/**
	 * 
	 * read a conference file with nameFile name
	 * 
	 * @return null if conference is not find
	 * 
	 * @throws ParseException
	 * @throws ParserException
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws InvalidConferenceFormatException
	 */
	public Set<Conference> searchConferenceInFile(String fileName)
			throws IOException, ParserException, InvalidConferenceFormatException {
		Set<Conference> set = retriever.retrieve(Objects.requireNonNull(fileName));
		return set;

	}

	/**
	 * this method return a set of Conference with every conferences data in
	 * icaldata directory
	 * 
	 * @return Set<Conference> not <code>null</code>
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws ParserException
	 * @throws ParseException
	 * @throws InvalidConferenceFormatException
	 */
	public Set<Conference> allConferences() throws IOException, ParserException, InvalidConferenceFormatException {
		return retriever.retrieve();
	}

	public Set<Conference> conferencesFiltredByDate()
			throws InvalidConferenceFormatException, IOException, ParserException {
		LocalDate minDate;
		LocalDate maxDate;
		try (Scanner sc = new Scanner(System.in)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			System.out.println("Please enter the minimale date dd/MM/yyyy format");
			minDate = LocalDate.parse(sc.nextLine(), formatter);
			System.out.println("Please enter the maximale date dd/MM/yyyy format ");
			maxDate = LocalDate.parse(sc.nextLine(), formatter);

			if (!minDate.isBefore(maxDate))
				throw new IllegalArgumentException("minDate must be before maxDate");

		}
		return retriever.retrieve(minDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
				maxDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

	}
}
