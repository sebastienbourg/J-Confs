package io.github.oliviercailloux.jconfs.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.time.temporal.ChronoUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.io.Files;

import io.github.oliviercailloux.jconfs.conference.Conference;
import io.github.oliviercailloux.jconfs.map.setCoordinates;
import io.github.oliviercailloux.jconfs.researcher.Researcher;

/**
 * 
 * @author huong
 *
 */
public class GenerateOMYS {

	private static String target = FileSystems.getDefault().getPath("").toAbsolutePath()
			+ "/demande_de_mission_jeune_chercheur.fodt";

	public static String getTarget() {
		return target;
	}

	/**
	 * 
	 * 
	 * @param user
	 * @param conf
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IllegalArgumentException
	 */
	public static void fillYSOrderMission(Researcher user, Conference conf, String fileDestination)
			throws IOException, SAXException, ParserConfigurationException, IllegalArgumentException {
		if (!fileDestination.isEmpty())
			target = fileDestination;
		conf.isConf();
		ClassLoader classLoader = setCoordinates.class.getClassLoader();
		String source = classLoader
				.getResource("io/github/oliviercailloux/y2018/jconfs/demande_de_mission_jeune_chercheur.fodt")
				.getPath();

		File demand = new File(source);

		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		org.w3c.dom.Document doc = null;

		try (InputStream xmlText = new FileInputStream(demand)) {
			// InputStream transformed in DOM Document
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(xmlText));
		}

		NodeList span = doc.getElementsByTagName("text:span");

		// Search every information that have to be completed and complete them
		// using the UserDetails and the Conference information
		for (int i = 0; i < span.getLength(); i++) {
			if (span.item(i).getTextContent().contains("NOM") && !span.item(i).getTextContent().contains("PRENOM")) {
				span.item(i).setTextContent(user.getLastname());
			}

			if (span.item(i).getTextContent().contains("PRENOM")) {
				span.item(i).setTextContent(user.getFirstname());
			}

			if (span.item(i).getTextContent().contains("POLE")) {
				span.item(i).setTextContent(user.getGroup());
			}
			if (span.item(i).getTextContent().contains("INTITULEMISSION")) {
				span.item(i).setTextContent(conf.getTitle());
			}
			if(!((conf.getCity().isEmpty()) && (conf.getCity().isEmpty()))) {
			if (span.item(i).getTextContent().contains("VILLE - PAYS")) {
				span.item(i).setTextContent(conf.getCity() + " - " + conf.getCountry());
			}
			}
			if (span.item(i).getTextContent().contains("DATE")) {
				span.item(i).setTextContent(conf.getStartDate().toString());
			}

			if (span.item(i).getTextContent().contains("DUREE")) {
				Long period = ChronoUnit.DAYS.between(conf.getStartDate(), conf.getEndDate());
				span.item(i).setTextContent(String.valueOf(period + " jour(s)"));
			}

			saveYSOrderMission(doc);
			saveYSOrderMissionToHistory(target, conf.getCity().get(), conf.getCountry().get(), conf.getStartDate().toString());
		}
	}

	private static void saveYSOrderMissionToHistory(String fileToCopy, String city, String country, String startDate)
			throws IOException {
		File filesource = new File(fileToCopy);
		String filename = new String(city + "-" + country + "_" + startDate + ".fodt");
		File targetfile = new File(filename);
		Files.copy(filesource, targetfile);
	}

	private static void saveYSOrderMission(org.w3c.dom.Document doc) throws IOException {

		DOMSource source = new DOMSource(doc);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try (FileWriter writer = new FileWriter(new File(target))) {
			StreamResult result = new StreamResult(writer);
			transformer = transformerFactory.newTransformer();
			transformer.transform(source, result);
		} catch (TransformerException e1) {
			throw new IllegalStateException(e1);
		}
	}

}
