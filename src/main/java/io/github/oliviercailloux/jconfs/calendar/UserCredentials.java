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
	private String path;
	
	public UserCredentials() throws IOException {
		this.path="./src/main/resources/Config.txt";

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
		this.path=a;
	}
	public void readFile() throws IOException {	  
		
		String a = "";
		final Path infile = Path.of(this.path.toString());
		try (InputStream is = Files.newInputStream(infile)) {
			is.readAllBytes();
			a = Files.readString(infile); 
			

		}

		this.username = a.split("\n")[0].split("username = ")[1].substring(0, a.split("\n")[0].split("username = ")[1].length()-1);
		this.password = a.split("\n")[1].split("password = ")[1].substring(0, a.split("\n")[1].split("password = ")[1].length()-1);
		this.calendarId = a.split("\n")[2].split("calendarId = ")[1].substring(0, a.split("\n")[2].split("calendarId = ")[1].length()-1);
	}


}
