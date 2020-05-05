package io.github.oliviercailloux.jconfs.calendar;

public class ConfigFileError extends Exception{
	
	/**
	 * New exception for our config file
	 */
	private static final long serialVersionUID = -1535226959813756779L;

	public ConfigFileError() {
		System.out.println("The config file doesn't respect the usual format.");
	}
}
