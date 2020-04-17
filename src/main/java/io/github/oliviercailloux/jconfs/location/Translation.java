package io.github.oliviercailloux.jconfs.location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.locationiq.client.Configuration;
import com.locationiq.client.api.*;
import com.locationiq.client.model.*;

import LocationIq.ApiClient;
import LocationIq.ApiException;
import LocationIq.Configuration;
import LocationIq.auth.ApiKeyAuth;

/**
 * 
 * @author Floryan Kieffer
 * This class allows you to translate an address into several pieces of informations.
 * It therefore allows, from imprecise information, to propose several addresses or a single address if the information entered is precise.
 * 
 */

public class Translation {
	
	private String latitude;
	private String longitude;
	private ArrayList <String> adressInformations;
	
	
	/**
	 * 
	 * Factory method which creates a Translation instance
	 *
	 */
	
	public static Translation given() {
		return new Translation();
	}
	
	/**
	 * Private constructor used by Factory method given()
	 */
	
	private Translation() {
		this.longitude=null;
		this.latitude=null;
		this.adressInformations = new ArrayList<String>();
	}
	
	
	/**
	 * This methode return the latitude of the instance
	 * @return latitude
	 */
	
	public String getLatitude() {
		return latitude;
	}
	
	/**
	 * Setter for latitude
	 * @param latitude
	 */
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * This method return the longitude of the instance
	 * @return longitude
	 */
	
	public String getLongitude() {
		return longitude;
	}
	
	/**
	 * Setter for longitude
	 * @param longitude
	 */
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * This method return a list with a lot of informations about an address research
	 * @return adressInformations
	 */
	
	public List<String> getAdressInformations() {
		return adressInformations;
	}
	
	/**
	 * Setter for adressInformations
	 * @param adressInformations
	 */
	
	public void setAdressInformations(ArrayList<String> adressInformations) {
		this.adressInformations = adressInformations;
	}
	
	/**
	 * This method allows connection to LocationIQ and its database.
	 * @return ApiClient defaultClient
	 * @throws ApiException
	 */
	
	public ApiClient connexion() throws ApiException {
  	  ApiClient defaultClient = Configuration.getDefaultApiClient();
  	  defaultClient.setBasePath("https://eu1.locationiq.com/v1");
  	  ApiKeyAuth key = (ApiKeyAuth) defaultClient.getAuthentication("key");
  	  key.setApiKey("d4b9a23eaef07d");
  	  return defaultClient;
    }
	
	/**
	 * 
	 * This method allows to create the ArrayList adressInformations making a precise or not address search.
	 * The ArrayList is filled according to the different elements to find, that is to say that if for an imprecise 
	 * search it is possible to assign several addresses to it, all these addresses are entered in the ArrayList.
	 * @param adresse
	 * @throws ApiException
	 */
	
	public void TransalteAdresse(String adresse) throws ApiException{
  	  
  	  ApiClient defaultClient = this.connexion();
  	  AutocompleteApi api = new AutocompleteApi(defaultClient);
  	  List tmp = api.autocomplete(adresse, 1, null, null, null, null, null, null);
  	  Iterator i = tmp.iterator();
  	  while(i.hasNext()) {
  		  this.adressInformations.add(i.next().toString());
  	  }
    }
	
	
	/**
	 * This method compares all addresses found in an array containing only addresses.
	 * It returns false if the addresses are different and true otherwise.
	 * @param selection
	 * @return boolean res 
	 */
	
	public boolean addressComparison(ArrayList <String> selection) {
		boolean res=false;
		for(int i = 0 ; i< selection.size()-1;i++) {
			if(selection.get(i).equals(selection.get(i+1)) && i!=selection.size()-1) {
				res = true;
			}
			
			else {
				return false;
			}
		}
		return res;
	}
	
	/**
	 * This method displays all the addresses if they are different and only one if not.
	 * @param selection
	 */
	
	public void displaySelectionAddress(ArrayList<String>selection) {
		if(!this.addressComparison(selection)) {
			for (int i = 0; i<selection.size();i++) {
				System.out.println(selection.get(i));
			}
		}
		else {
			System.out.println(selection.get(0));
		}
		
	}
	
	
	/**
	 * This method modifies the contents of the ArrayList adressInformations to make it more readable and 
	 * to be able to apply different methods more easily.
	 * It allows the selection of the address that the user wants.
	 */
	
	public void selectionAddressInformation() {
		ArrayList<String> tmp = new ArrayList<String>();
		for(int i = 0; i<this.adressInformations.size(); i++) {
			String contenu = this.adressInformations.get(i);
			String hash = contenu.substring(1, contenu.length()-2);
			this.adressInformations.set(i, hash);
		}
		ArrayList<String> selection = new ArrayList<String>();
		for(int i = 0 ; i<this.adressInformations.size();i++) {
			String search = "display_address=";
			int posDep = this.adressInformations.get(i).indexOf(search);
			int posArr = this.adressInformations.get(i).indexOf(", address={");
			String add = this.adressInformations.get(i).substring(posDep+search.length(), posArr);
			selection.add(add);
		}
		this.displaySelectionAddress(selection);
		
	}

	public static void main(String[] args) throws ApiException {
		Translation t = given();
		t.TransalteAdresse("Université paris dauphine");
		t.selectionAddressInformation();
		
	}

}
