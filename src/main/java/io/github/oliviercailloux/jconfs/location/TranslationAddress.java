package io.github.oliviercailloux.jconfs.location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.locationiq.client.api.*;

import com.locationiq.client.ApiClient;
import com.locationiq.client.ApiException;
import com.locationiq.client.Configuration;
import com.locationiq.client.auth.ApiKeyAuth;

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

	public static TranslationAddress newInstance() {
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

		/**
		 * This method initialize state
		 * 
		 * @param translationAddress
		 */

		private TranslationAddressBuilder(final TranslationAddress translationAddress) {
			this.translationAddress = translationAddress;
		}

		/**
		 * This method start building a TranslationAddress
		 * 
		 * @return new TranslationAddressBuilder(new TranslationAddress())
		 */

		public static TranslationAddressBuilder build() {
			return new TranslationAddressBuilder(new TranslationAddress());
		}

		/**
		 * Instantiate attribute latitude
		 * 
		 * @param lat
		 * @return this
		 */

		public TranslationAddressBuilder latitude() {
			this.translationAddress.recoveryLatitude();
			return this;
		}

		/**
		 * Instantiate attribute longitude
		 * 
		 * @param lon
		 * @return this
		 */

		public TranslationAddressBuilder longitude() {
			this.translationAddress.recoveryLongitude();
			return this;
		}

		/**
		 * Instantiate attribute addressInformations
		 * 
		 * @param address
		 * @return this
		 * @throws ApiException
		 */

		public TranslationAddressBuilder addressInformations(final String address) throws ApiException {
			this.translationAddress.recoveryAddressInformations(address);
			return this;
		}

		/**
		 * Instantiate attribute addressFound
		 * 
		 * @return this
		 */

		public TranslationAddressBuilder addressFound() {
			this.translationAddress.recoveryAddressFound();
			this.translationAddress.addressProposal();
			return this;
		}

		/**
		 * Return the unique object
		 * 
		 * @return ret
		 */

		public TranslationAddress get() {
			final TranslationAddress ret = TranslationAddress.newInstance();
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

	public ArrayList<String> getAddressInformations() {
		return addressInformations;
	}

	/**
	 * This method return all address found after a researcher address
	 * 
	 * @return adressFound
	 */

	public ArrayList<String> getAddressFound() {
		return addressFound;
	}

	/**
	 * This method allows connection to LocationIQ and its database.
	 * 
	 * @return ApiClient defaultClient
	 */

	public static ApiClient connexion() {
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		defaultClient.setBasePath("https://eu1.locationiq.com/v1");
		ApiKeyAuth key = (ApiKeyAuth) defaultClient.getAuthentication("key");
		key.setApiKey("d4b9a23eaef07d");
		return defaultClient;
	}

	/**
	 * 
	 * This method allows to create the ArrayList adressInformations making a
	 * precise or not address search (precise is better). The ArrayList is filled
	 * according to the different elements to find, that is to say that if for an
	 * imprecise search it is possible to assign several addresses to it, all these
	 * addresses are entered in the ArrayList.
	 * 
	 * @param adresse
	 * @throws ApiException
	 */

	public void recoveryAddressInformations(String address) throws ApiException {
		if (address == "" || address == null || address.isEmpty()) {
			throw new IllegalArgumentException("Address error");
		}
		ApiClient defaultClient = TranslationAddress.connexion();
		AutocompleteApi api = new AutocompleteApi(defaultClient);
		List<Object> tmp = api.autocomplete(address, 1, null, null, null, null, null, null);
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

	public boolean displayFoundAddress(ArrayList<String> selection) {
		Set<String> tmp = new HashSet<String>(selection);
		Iterator<String> i = tmp.iterator();
		int cpt = 1;
		if (tmp.size() > 1) {
			while (i.hasNext()) {
				System.out.println(cpt + ") - " + i.next());
				cpt++;
			}
		} else {
			System.out.println("The only address found is: " + i.next());
			return true;
		}
		return false;

	}

	/**
	 * This method modifies the contents of the ArrayList addressInformations to
	 * make it more readable and to be able to apply different methods more easily.
	 * It also makes it possible to retrieve all the addresses found by
	 * autocomplete, to store them in addressFound. It allows the selection of the
	 * address that the user wants.
	 */

	public void recoveryAddressFound() {
		for (int i = 0; i < this.addressInformations.size(); i++) {
			String contenu = this.addressInformations.get(i);
			String hash = contenu.substring(1, contenu.length() - 2);
			this.addressInformations.set(i, hash);
		}
		ArrayList<String> selection = new ArrayList<String>();
		for (int i = 0; i < this.addressInformations.size(); i++) {
			String search = "display_name=";
			int posDep = this.addressInformations.get(i).indexOf(search);
			int posArr = this.addressInformations.get(i).indexOf(", display_place=");
			String add = this.addressInformations.get(i).substring(posDep + search.length(), posArr);
			selection.add(add);
		}
		this.addressFound = selection;
	}

	/**
	 * This method display all address found by autocomplete and ask the user to
	 * select the one of his choice. After the user has chosen all the unnecessary
	 * addresses in the addressFound and addressInformations ArrayList are deleted.
	 * If only one address is found interaction with the user does not take place
	 */

	public void addressProposal() {
		boolean displayall = this.displayFoundAddress(this.getAddressFound());
		if (displayall == false) {
			String address = this.selectionAddressProposal();
			for (int i = 0; i < this.addressFound.size(); i++) {
				if (!address.equals(this.addressFound.get(i))) {
					this.addressFound.remove(i);
					this.addressInformations.remove(i);
					i--;
				}
			}
		} else {
			for (int i = 1; i < this.addressFound.size(); i++) {
				this.addressFound.remove(i);
				this.addressInformations.remove(i);
				i--;
			}
		}

	}

	/**
	 * This method allows to return the good address.
	 * 
	 * @return numberAddress
	 */

	public String selectionAddressProposal() {
		System.out.println("Enter address of your choice (exactly the same proposed): ");
		Scanner sc = new Scanner(System.in);
		String address = sc.nextLine();
		sc.close();
		return address;
	}

	/**
	 * This method retrieves the latitude for an address and instantiates the
	 * latitude attribute
	 */

	public void recoveryLatitude() {
		String search = "lat=";
		int posDep = this.addressInformations.get(0).indexOf(search);
		int posArr = this.addressInformations.get(0).indexOf(", lon=");
		String add = this.addressInformations.get(0).substring(posDep + search.length(), posArr);
		this.latitude = add;
	}

	/**
	 * This method retrieves the longitude for an address and instantiates the
	 * longitude attribute
	 */

	public void recoveryLongitude() {
		String search = "lon=";
		int posDep = this.addressInformations.get(0).indexOf(search);
		int posArr = this.addressInformations.get(0).indexOf(", boundingbox=");
		String add = this.addressInformations.get(0).substring(posDep + search.length(), posArr);
		this.longitude = add;
	}
}
