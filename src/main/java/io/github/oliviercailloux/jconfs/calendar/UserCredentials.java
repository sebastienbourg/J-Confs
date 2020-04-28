package io.github.oliviercailloux.jconfs.calendar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class UserCredentials 
{
	private String username;
	private String password;
	private String calendarId;

	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getCalendarId() {
		return calendarId;
	}

	public void readFile() throws IOException {	    

		final Path infile = Path.of("./src/main/resources/Config.txt");
		try (InputStream is = Files.newInputStream(infile)) {
			is.readAllBytes();
			System.out.println(Files.readString(infile)); 
		}

		this.username = "9392@yopmail.com";
		this.password = "Loscincos9392" ;
		this.calendarId = "a" ;
	}


}
