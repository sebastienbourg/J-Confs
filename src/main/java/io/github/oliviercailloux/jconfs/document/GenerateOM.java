package io.github.oliviercailloux.jconfs.document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;

import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Cell;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

import io.github.oliviercailloux.jconfs.conference.Conference;
import io.github.oliviercailloux.jconfs.researcher.Researcher;

/**
 * This class fills a searcher Mission Order
 * 
 * @author huong
 *
 */
public class GenerateOM {
	final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(GenerateOM.class);
	private static String target = FileSystems.getDefault().getPath("").toAbsolutePath() + "/ordre_de_mission_test.ods";

	public static String getTarget() {
		return target;
	}

	/**
	 * Use the conference to fill the Spreadsheet
	 * 
	 * @param conference
	 * @throws Exception
	 */
	public static void generateOM(Conference conference, Researcher researcher) throws Exception {

		try (InputStream inputStream = MissionOrder.class.getResourceAsStream("ordre_de_mission.ods");
				SpreadsheetDocument spreadsheetDoc = SpreadsheetDocument.loadDocument(inputStream)) {

			Cell surNameCell = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("F8");
			surNameCell.setStringValue(researcher.getLastname());

			Cell firstNameCell = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("Y8");
			firstNameCell.setStringValue(researcher.getFirstname());

			Cell mailCell = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("F11");
			mailCell.setStringValue(researcher.getMail());

			Cell comeCell = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("B31");
			comeCell.setStringValue("Paris, France");
			
			if(conference.isConf()) {
				Cell titleCell = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("B15");
				titleCell.setStringValue(conference.getTitle());
				
				Cell startCell = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("M22");
				startCell.setStringValue(conference.getStartDate().toString());

				Cell endCell = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("Z22");
				endCell.setStringValue(conference.getEndDate().toString());
				
				if (!((conference.getCity().isEmpty()) && (conference.getCity().isEmpty()))) {

				Cell returnCell = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("B37");
				returnCell.setStringValue(conference.getCity().get() + " ," + conference.getCountry().get());
				}

				
			}
			
			

			spreadsheetDoc.save(target);
			saveOrderMissionToHistory(target, conference.getCity().get(), conference.getCountry().get(),
					conference.getStartDate().toString());
		}

	}

	private static void saveOrderMissionToHistory(String fileToCopy, String city, String country, String startDate)
			throws IOException {
		File filesource = new File(fileToCopy);
		String filename = new String("OM_" + city + "-" + country + "_" + startDate + ".ods");
		File targetfile = new File(filename);
		Files.copy(filesource, targetfile);
	}

}
