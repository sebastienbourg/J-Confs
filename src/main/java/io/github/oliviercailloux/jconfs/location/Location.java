package io.github.oliviercailloux.jconfs.location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Location {
	
	/**
     * 
     * @param citydDep string of format
     * @param cityArr string of format
     * @param longitudeDep double format which represent longitude of cityDep
     * @param latitudeDep double format which represent latitude of cityDep
     * @param longitudeArr double format which represent longitude of cityArr
     * @param latitudeArr double format which represent latitude of cityArr
     * 
     */
	
	double longitudeDep;
	double latitudeDep;
	double longitudeArr;
	double latitudeArr;
	String cityDep;
	String cityArr;
	
	public static Location given() {
		return new Location();
	}
	
	private Location() {
		this.longitudeDep=0.0;
		this.latitudeDep=0.0;
		this.longitudeArr=0.0;
		this.latitudeArr=0.0;
		this.cityArr=null;
		this.cityDep=null;
	}
	
	
	public double getLongitudeDep() {
		return longitudeDep;
	}

	public void setLongitudeDep(double longitudeDep) {
		this.longitudeDep = longitudeDep;
	}

	public double getLatitudeDep() {
		return latitudeDep;
	}

	public void setLatitudeDep(double latitudeDep) {
		this.latitudeDep = latitudeDep;
	}

	public double getLongitudeArr() {
		return longitudeArr;
	}

	public void setLongitudeArr(double longitudeArr) {
		this.longitudeArr = longitudeArr;
	}

	public double getLatitudeArr() {
		return latitudeArr;
	}

	public void setLatitudeArr(double latitudeArr) {
		this.latitudeArr = latitudeArr;
	}

	public String getCityDep() {
		return cityDep;
	}

	public void setCityDep(String cityDep) {
		this.cityDep = cityDep;
	}

	public String getCityArr() {
		return cityArr;
	}

	public void setCityArr(String cityArr) {
		this.cityArr = cityArr;
	}

	private void readCoordinateFileByCityName(Path path, String CityDep,String CityArr) throws IOException {
		ArrayList<String>coordonnee = new ArrayList<String>();
		try (BufferedReader is = Files.newBufferedReader(path)) {
			while (true) {
				final String read = is.readLine();
				if(read == null) {
					break;
				}
				String [] orderedArray = read.split(" ");
				for(int i = 0 ; i < orderedArray.length ; i++) {
					coordonnee.add(orderedArray[i]);
				}
			}
		}
		int cpt=0;
		for (int j = 0; j<coordonnee.size(); j = j+3) {
			if(cpt==2) {
				j=coordonnee.size();
			}
			else {
				if(coordonnee.get(j).equals(CityDep)) {
					this.latitudeDep=Double.parseDouble(coordonnee.get(j+1));
					this.longitudeDep=Double.parseDouble(coordonnee.get(j+2));
					cpt++;
				}
				else if (coordonnee.get(j).contentEquals(CityArr)) {
					this.latitudeArr=Double.parseDouble(coordonnee.get(j+1));
					this.longitudeArr=Double.parseDouble(coordonnee.get(j+2));
					cpt++;
				}
			}
		}
	}
	
	
	
	
	public static void main(String[] args) throws IOException {
		Location a = Location.given();
		Path path =Path.of("/Users/floryan/Documents/city.txt");
		Location P_M = given();
		P_M.setCityArr("Marseille");
		P_M.setCityDep("Paris");
		a.readCoordinateFileByCityName(path,P_M.cityDep , P_M.cityArr);
		System.out.println("Coordonnées de la ville de depart "+a.latitudeDep+ " "+a.longitudeDep);
		System.out.println("Coordonnées de la ville d'arrivé "+a.latitudeArr+ " "+a.longitudeArr);
	}
}

