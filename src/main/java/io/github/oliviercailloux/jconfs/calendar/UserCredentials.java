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
 * Class which handle a config file. Used to give
 *         credential to Calendar Builder. Need to use the method readFile to be at the good state
 * @author machria & zanis922 
 * 
 */

public class UserCredentials {
	private String username;
	private String password;
	private String calendarId;
	private String path;

	public UserCredentials() throws IOException {
		this.path = "./src/main/resources/Config.txt";

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
	 * Parse a config file from source :
	 * https://www.programcreek.com/java-api-examples/?api=net.fortuna.ical4j.model.property.Method
	 * 
	 * @throws IOException
	 * @throws ParserException
	 */
	public void readFile() throws IOException {

		String file = "";
		final Path infile = Path.of(this.path.toString());
		try (InputStream is = Files.newInputStream(infile)) {
			is.readAllBytes();
			file = Files.readString(infile);

		}

		this.username = file.split("\n")[0].split("username = ")[1];
		this.password = file.split("\n")[1].split("password = ")[1];
		this.calendarId = file.split("\n")[2].split("calendarId = ")[1];

	}

}
