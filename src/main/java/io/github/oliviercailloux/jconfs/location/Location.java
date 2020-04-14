package io.github.oliviercailloux.jconfs.location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Location {
	
	double longitudeDep;
	double latitudeDep;
	double longitudeArr;
	double latitudeArr;
	String villeDep;
	String villeArr;
	
	public static Location given() {
		return new Location();
	}
	
	private Location() {
		this.longitudeDep=0.0;
		this.latitudeDep=0.0;
		this.longitudeArr=0.0;
		this.latitudeArr=0.0;
		this.villeArr=null;
		this.villeDep=null;
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
				System.out.println(read);
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
		String CityDep = "Paris";
		String CityArr = "Marseille";
		a.readCoordinateFileByCityName(path,CityDep , CityArr);
		System.out.println("Coordonnées de la ville de depart "+a.latitudeDep+ " "+a.longitudeDep);
		System.out.println("Coordonnées de la ville d'arrivé "+a.latitudeArr+ " "+a.longitudeArr);
	}
}

