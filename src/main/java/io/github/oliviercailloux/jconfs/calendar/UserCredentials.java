package io.github.oliviercailloux.jconfs.calendar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import net.fortuna.ical4j.data.ParserException;

/**
 * Class which handle a config file. 
 * Used to give credential to Calendar Builder. 
 * Need to use the method readFile to be usable in CalendarBuilder.
 * Please note that the calendarId is link to a credential in all calendar cloud web-host.
 * @author machria & zanis922 
 * 
 */

public class UserCredentials {
	private String username;
	private String password;
	private String calendarId;
	private String path;

	public UserCredentials() throws Exception {
		this.path = "./src/main/resources/Config.txt";
		readFile();
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getCalendarId() {
		return calendarId;
	}

	public void setPath(String a) {
		this.path = a;
	}

	/**
	 * Parse a config file from source
	 * 
	 * @throws IOException
	 * @throws ParserException
	 * @throws ConfigFileError
	 */
	public void readFile() throws Exception {

		String file = "";
		final Path infile = Path.of(this.path.toString());
		try (InputStream is = Files.newInputStream(infile)) {
			is.readAllBytes();
			file = Files.readString(infile);

		}
		try {
			this.username = file.split("\n")[0].split("username = ")[1];
			this.password = file.split("\n")[1].split("password = ")[1];
			this.calendarId = file.split("\n")[2].split("calendarId = ")[1];
		}catch(Exception e) {
			throw new ConfigFileError();
		}
	}

}
