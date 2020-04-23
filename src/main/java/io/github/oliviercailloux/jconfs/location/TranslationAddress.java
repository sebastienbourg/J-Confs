package io.github.oliviercailloux.jconfs.location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

//import com.locationiq.client.Configuration;
import com.locationiq.client.api.*;

import LocationIq.ApiClient;
import LocationIq.ApiException;
import LocationIq.Configuration;
import LocationIq.auth.ApiKeyAuth;

/**
 * 
 * @author Floryan Kieffer This class allows you to translate an address into
 *         several pieces of informations. It therefore allows, from imprecise
 *         information, to propose several addresses or a single address if the
 *         information entered is precise.
 * 
 */

public class TranslationAddress {

	private String latitude;
	private String longitude;
	private ArrayList<String> addressInformations;
	private ArrayList<String> addressFound;

	/**
	 * 
	 * Factory method which creates a Translation instance
	 *
	 */

	public static TranslationAddress givenEmptyInstance() {
		return new TranslationAddress();
	}

	/**
	 * Private constructor
	 */

	private TranslationAddress() {
		this.addressFound = new ArrayList<String>();
		this.addressInformations = new ArrayList<String>();
	}

	/**
	 * This class is a builder, it's allows to make the object TranslsationAddress
	 * immutable. It builds all the attributes of the class using a
	 * TranslationAddress object.
	 * 
	 * @author floryan
	 *
	 */

	public static class TranslationAddressBuilder {

		private TranslationAddress translationAddress;

		private TranslationAddressBuilder(final TranslationAddress translationAddress) {
			this.translationAddress = translationAddress;
		}

		public static TranslationAddressBuilder build() {
			return new TranslationAddressBuilder(new TranslationAddress());
		}

		// Instantiate attribute latitude

		public TranslationAddressBuilder latitude(final String lat) {
			this.translationAddress.latitude = lat;
			return this;
		}

		// Instantiate attribute longitude

		public TranslationAddressBuilder longitude(final String lon) {
			this.translationAddress.longitude = lon;
			return this;
		}

		// Instantiate attribute addressInformations

		public TranslationAddressBuilder addressInformations(final String address) throws ApiException {
			this.translationAddress.recoveryAdresseInformations(address);
			return this;
		}

		// Instantiate attribute addressFound

		public TranslationAddressBuilder addressFound() throws ApiException {
			this.translationAddress.addressFound();
			this.translationAddress.addressProposal();
			return this;
		}

		// Return the unique object

		public TranslationAddress get() {
			final TranslationAddress ret = TranslationAddress.givenEmptyInstance();
			ret.latitude = translationAddress.latitude;
			ret.longitude = translationAddress.longitude;
			ret.addressFound = translationAddress.addressFound;
			ret.addressInformations = translationAddress.addressInformations;
			return ret;
		}
	}

	/**
	 * This method return the latitude of the instance
	 * 
	 * @return latitude
	 */

	public String getLatitude() {
		return latitude;
	}

	/**
	 * This method return the longitude of the instance
	 * 
	 * @return longitude
	 */

	public String getLongitude() {
		return longitude;
	}

	/**
	 * This method return a list with a lot of informations about an address
	 * research
	 * 
	 * @return adressInformations
	 */

	public ArrayList<String> getAdressInformations() {
		return addressInformations;
	}

	/**
	 * This method return all address found after a researcher address
	 * 
	 * @return adressFound
	 */

	public ArrayList<String> getAdressFound() {
		return addressFound;
	}

	/**
	 * This method allows connection to LocationIQ and its database.
	 * 
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
	 * This method allows to create the ArrayList adressInformations making a
	 * precise or not address search. The ArrayList is filled according to the
	 * different elements to find, that is to say that if for an imprecise search it
	 * is possible to assign several addresses to it, all these addresses are
	 * entered in the ArrayList.
	 * 
	 * @param adresse
	 * @throws ApiException
	 */

	public void recoveryAdresseInformations(String adresse) throws ApiException {
		if (adresse == "" || adresse == null || adresse.isEmpty()) {
			throw new IllegalArgumentException("Address error");
		}
		ApiClient defaultClient = this.connexion();
		AutocompleteApi api = new AutocompleteApi(defaultClient);
		List<Object> tmp = api.autocomplete(adresse, 1, null, null, null, null, null, null);
		Iterator<Object> i = tmp.iterator();
		while (i.hasNext()) {
			this.addressInformations.add(i.next().toString());
		}
	}

	/**
	 * This method displays all the addresses if they are different and only one if
	 * not.
	 * 
	 * @param selection
	 */

	public void displayFoundAddress(ArrayList<String> selection) {
		Set<String> tmp = new HashSet<String>(selection);
		Iterator<String> i = tmp.iterator();
		int cpt = 1;
		if (tmp.size() > 1) {
			while (i.hasNext()) {
				System.out.println(cpt + ") - " + i.next());
				cpt++;
			}
		} else {
			System.out.println(1 + ") - " + i.next());
		}

	}

	/**
	 * This method modifies the contents of the ArrayList adressInformations to make
	 * it more readable and to be able to apply different methods more easily. It
	 * also makes it possible to retrieve all the addresses found by autocomplete,
	 * to store them in adressFound. It allows the selection of the address that the
	 * user wants.
	 */

	public void addressFound() {
		for (int i = 0; i < this.addressInformations.size(); i++) {
			String contenu = this.addressInformations.get(i);
			String hash = contenu.substring(1, contenu.length() - 2);
			this.addressInformations.set(i, hash);
		}
		ArrayList<String> selection = new ArrayList<String>();
		for (int i = 0; i < this.addressInformations.size(); i++) {
			String search = "display_address=";
			int posDep = this.addressInformations.get(i).indexOf(search);
			int posArr = this.addressInformations.get(i).indexOf(", address={");
			String add = this.addressInformations.get(i).substring(posDep + search.length(), posArr);
			selection.add(add);
		}
		this.addressFound = selection;
	}

	/**
	 * This method display all address found by autocomplete and ask the user to
	 * select the one of his choice. After the user has chosen all the unnecessary
	 * addresses in the addressFound and addressInformations ArrayList are deleted.
	 */

	public void addressProposal() {
		this.displayFoundAddress(this.getAdressFound());
		int numberAddress = this.selectionAddressProposal();
		for (int i = 0; i < this.addressInformations.size(); i++) {
			if (i != numberAddress - 1) {
				this.addressFound.remove(i);
				this.addressInformations.remove(i);
				i--;
			}
		}
	}

	/**
	 * This method allows to return the choice of the user.
	 * 
	 * @return numberAddress
	 */

	public int selectionAddressProposal() {
		System.out.print("Saisir le numéro associé à l'adresse de votre choix: ");
		Scanner sc = new Scanner(System.in);
		int numberAddress = sc.nextInt();
		sc.close();
		return numberAddress;
	}

	public static void main(String[] args) throws ApiException {
		TranslationAddress address = TranslationAddress.TranslationAddressBuilder.build()
				.addressInformations("Université paris dauphine").addressFound().latitude("2,4").longitude("3,345")
				.get();
	}
}
