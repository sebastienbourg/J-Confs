package io.github.oliviercailloux.jconfs.location;

import LocationIq.ApiClient;
import LocationIq.ApiException;
import java.math.BigDecimal;
import com.locationiq.client.api.DirectionsApi;
import java.util.Iterator;
import java.util.List;
import LocationIq.Configuration;
import LocationIq.auth.*;
import com.locationiq.client.model.*;

/**
 * 
 * @author Anis HAMOUNI this class allows to calculate the distance and the
 *         duration between two places defined by (longitude, latitude) and also
 *         the steps to go to the destination. the units are meter and second,
 *         the steps are a string
 */
public class Direction {
	private String addressDeparture;
	private String addressArrival;
	private BigDecimal duration;
	private BigDecimal distance;
	private String steps;
	private TranslationAddress firstAddress;
	private TranslationAddress secondAddress;

	/**
	 * 
	 * @param dep
	 *            string of format longitude,latitude example "2.287592,48.862725"
	 * @param arriv
	 *            string of format longitude,latitude example "2.3488,48.85341 steps
	 *            is a string that contains all steps to go on our destination
	 * @throws ApiException 
	 * 
	 */

	public static Direction given(String dep, String arriv) throws ApiException {
		return new Direction(dep, arriv);
	}

	private Direction(String dep, String arriv) throws ApiException {
		this.addressDeparture = dep;
		this.addressArrival = arriv;
		this.duration = distance = BigDecimal.ZERO;
		this.steps = "";
		this.firstAddress = TranslationAddress.TranslationAddressBuilder.build()
							.addressInformations(this.addressDeparture).addressFound().latitude().longitude()
							.get();
		this.secondAddress = TranslationAddress.TranslationAddressBuilder.build()
				.addressInformations(this.addressArrival).addressFound().latitude().longitude()
				.get();
	}

	public BigDecimal getDuration() {
		return duration;
	}

	public BigDecimal getDistance() {
		return distance;
	}

	public void setDuration(BigDecimal duree) {
		this.duration = duree;
	}

	public void setDistance(BigDecimal dist) {
		this.distance = dist;
	}

	public String getDeparture() {
		return this.addressDeparture;
	}

	public String getArrival() {
		return this.addressArrival;
	}

	public void setDeparture(String dep) {
		this.addressDeparture = dep;
	}

	public void setArrivQL(String arr) {
		this.addressArrival = arr;
	}

	public String getSteps() {
		return this.steps;
	}

	/**
	 * 
	 * @param o
	 *            String this function takes a string and returns it with a line
	 *            break at each "intersections" word. it used when we calculate the
	 *            steps
	 */
	private String indentedStringOnIntersect(String o) {
		if (o == null) {
			return "null";
		}
		return o.replaceAll("intersections=", "\n intersections= \n");
	}

	/**
	 * 
	 * @param o
	 *            String this function removes the geometry information in the steps
	 *            because it is useless
	 */
	private String indentedStringGeometry(String o) {
		if (o == null) {
			return "null";
		}
		return o.replaceAll(
				"geometry=[^,]++,|out=[^,]++,|in=[^,]++,|entry=\\[[^\\]]++\\],|bearings=\\[[^\\]]++\\]}?\\]?,", "");
	}

	/**
	 * 
	 * @param o
	 * @this function takes a string and returns it with a line break at each "out"
	 *       word.
	 */
	private String indentedStringOut(String o) {
		if (o == null) {
			return "null";
		}
		return o.replaceAll("out=", "\n out=");
	}

	/**
	 * this method create an ApiClient and connect it to to the API with our key
	 * 
	 * @return ApiClient
	 */

	public ApiClient connexion() {
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		defaultClient.setBasePath("https://eu1.locationiq.com/v1");
		ApiKeyAuth key = (ApiKeyAuth) defaultClient.getAuthentication("key");
		key.setApiKey("d4b9a23eaef07d"); // here our key
		return defaultClient;
	}

	/**
	 * this function calculates the time and distance as well as the steps to move
	 * between the two attributes of the class which are coordinates of localisation
	 * 
	 * @throws ApiException
	 */
	public void getDirection() throws ApiException {

		ApiClient defaultClient = this.connexion();
		
		String latLonAddressDeparture = firstAddress.getLongitude() + "," + firstAddress.getLatitude();
		String latLonAddressArrival = secondAddress.getLongitude() + "," + secondAddress.getLatitude();
		

		DirectionsApi api = new DirectionsApi(defaultClient);
		/**
		 * the format of the coordinate must be a String of { longitude,latitude;
		 * longitude,latitude}
		 */
		DirectionsDirections response = api.directions(latLonAddressDeparture + ";" + latLonAddressArrival, null, null, null, null, null, null,
				"true", null, null, "simplified", null);
		List<DirectionsDirectionsRoutes> routes = response.getRoutes();
		Iterator<DirectionsDirectionsRoutes> i = routes.iterator();
		while (i.hasNext()) {
			DirectionsDirectionsRoutes dr = i.next();
			distance = distance.add(dr.getDistance());
			this.duration = this.duration.add(dr.getDuration());
			this.steps = this.steps + dr.toString();
		}
		this.steps = indentedStringOnIntersect(this.steps);
		this.steps = indentedStringGeometry(this.steps);
		this.steps = indentedStringOut(this.steps);
	}

}
