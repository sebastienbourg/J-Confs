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

	public static void main(String[] args) throws ApiException {
		Translation t = given();
		t.TransalteAdresse("Université paris dauphine");
		Iterator i = t.getAdressInformations().iterator();
		while(i.hasNext()) {
			System.out.println(i.next());
		}
		
	}

}
