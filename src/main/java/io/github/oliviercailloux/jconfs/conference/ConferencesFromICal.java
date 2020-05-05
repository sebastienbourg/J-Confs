package io.github.oliviercailloux.jconfs.conference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import com.google.common.base.Preconditions;

import net.fortuna.ical4j.data.ParserException;

public class ConferencesFromICal implements ConferencesRetriever {

	@Override
	public Set<Conference> retrieve() throws IOException, ParserException, InvalidConferenceFormatException {
		String filePath = ConferencesFromICal.class.getClassLoader().getResource("icaldata").getFile();
		File ressourcesDirectory = new File(filePath);
		File[] fileList = ressourcesDirectory.listFiles();
		Set<Conference> setOfConf = new LinkedHashSet<>();
		for (File file : fileList) {
			if (file.getName().endsWith(".ics")) {
				try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
					setOfConf.addAll(ConferenceReader.readConferences(reader));
				}
			}
		}
		return setOfConf;
	}

	@Override
	public Set<Conference> retrieve(Instant minDate, Instant maxDate)
			throws IOException, ParserException, InvalidConferenceFormatException {
		Set<Conference> setOfAllConf = retrieve();
		Set<Conference> setOfConfFiltred = new LinkedHashSet<>();
		for (Conference conf : setOfAllConf) {
			if (conf.getStartDate().isAfter(minDate) && conf.getEndDate().isBefore(maxDate)) {
				setOfConfFiltred.add(conf);
			}
		}
		return setOfConfFiltred;
	}

	@Override
	public Set<Conference> retrieve(String fileName)
			throws InvalidConferenceFormatException, IOException, ParserException {
		Preconditions.checkNotNull(fileName);
		URL urlcalendar = ConferenceReader.class.getResource(fileName + ".ics");
		try (FileReader reader = new FileReader(new File(urlcalendar.getFile()))) {
			return ConferenceReader.readConferences(reader);
		}

	}

}
