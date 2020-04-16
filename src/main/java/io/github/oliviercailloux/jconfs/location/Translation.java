package io.github.oliviercailloux.jconfs.location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import com.locationiq.client.Configuration;
import com.locationiq.client.api.*;
import com.locationiq.client.model.*;

import LocationIq.ApiClient;
import LocationIq.ApiException;
import LocationIq.Configuration;
import LocationIq.auth.ApiKeyAuth;

public class Translation {
	
	private String latitude;
	private String longitude;
	private ArrayList <String> adressInformations;
	
	public static Translation given() {
		return new Translation();
	}
	
	private Translation() {
		this.longitude=null;
		this.latitude=null;
		this.adressInformations = new ArrayList<String>();
	}
	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public List<String> getAdressInformations() {
		return adressInformations;
	}

	public void setAdressInformations(ArrayList<String> adressInformations) {
		this.adressInformations = adressInformations;
	}

	public ApiClient connexion() throws ApiException {
  	  ApiClient defaultClient = Configuration.getDefaultApiClient();
  	  defaultClient.setBasePath("https://eu1.locationiq.com/v1");
  	  ApiKeyAuth key = (ApiKeyAuth) defaultClient.getAuthentication("key");
  	  key.setApiKey("d4b9a23eaef07d");
  	  return defaultClient;
    }
	
	public void TransalteAdresse(String adresse) throws ApiException{
  	  
  	  ApiClient defaultClient = this.connexion();
  	  AutocompleteApi api = new AutocompleteApi(defaultClient);
  	  List tmp = api.autocomplete(adresse, 1, null, null, null, null, null, null);
  	  Iterator i = tmp.iterator();
  	  while(i.hasNext()) {
  		  this.adressInformations.add(i.next().toString());
  	  }
    }
	
	public void transformationAdressInformation() {
		ArrayList<String> tmp = new ArrayList<String>();
		for(int i = 0; i<this.adressInformations.size(); i++) {
			String contenu = this.adressInformations.get(i);
			String hash = contenu.substring(1, contenu.length()-1);
			this.adressInformations.set(i, hash);
			System.out.println(this.adressInformations.get(i));
		}
		
		for(int i=0;i<this.adressInformations.size();i++) {
			String [] spliting = this.adressInformations.get(i).split(",");
			String splitingsplit[spliting.length]
			for(int j = 0; j<spliting.length;j++) {
				String
			}
		}
	}

	public static void main(String[] args) throws ApiException {
		Translation t = given();
		t.TransalteAdresse("Université paris dauphine");
		t.transformationAdressInformation();
		
	}

}
