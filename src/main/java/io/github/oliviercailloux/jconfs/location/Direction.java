package io.github.oliviercailloux.jconfs.location;

import com.locationiq.client.ApiClient;
import com.locationiq.client.ApiException;
import java.math.BigDecimal;
import com.locationiq.client.api.DirectionsApi;
import java.util.Iterator;
import java.util.List;
import com.locationiq.client.model.*;

/**
 * This class allows to calculate the distance and the duration between two
 * places defined by address (convert to lattitude, longitude). It define the
 * steps to go to the destination. The units are meter and second, and the steps
 * are a string
 * 
 * @author Anis HAMOUNI & Sébastien BOURG
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
	 * static factory method to build a direction instance
	 * 
	 * @param dep   string address, example : "13 Rue Cloche Percé, 75004 Paris"
	 * @param arriv string address, example : "Avenue du général de gaulle, 92800
	 *              puteaux"
	 * 
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
				.addressInformations(this.addressDeparture).addressFound().latitude().longitude().get();
		this.secondAddress = TranslationAddress.TranslationAddressBuilder.build()
				.addressInformations(this.addressArrival).addressFound().latitude().longitude().get();
	}

	/**
	 * Return the duration in second
	 * 
	 * @return duration
	 */
	public BigDecimal getDuration() {
		return duration;
	}

	/**
	 * Return the distance in meters
	 * 
	 * @return distance
	 */
	public BigDecimal getDistance() {
		return distance;
	}

	/**
	 * Return the departure address
	 * 
	 * @return addressDeparture
	 */
	public String getDeparture() {
		return this.addressDeparture;
	}

	/**
	 * Return the departure address
	 * 
	 * @return addressDeparture
	 */
	public String getArrival() {
		return this.addressArrival;
	}

	/**
	 * Return the steps
	 * 
	 * @return steps
	 */
	public String getSteps() {
		return this.steps;
	}

	/**
	 * This function takes a string and returns it with a line break at each
	 * "intersections" word. It's use when we calculate the steps.
	 * 
	 * @param s String
	 * 
	 */
	private String indentedStringOnIntersect(String s) {
		if (s == null) {
			return "null";
		}
		return s.replaceAll("intersections=", "\n intersections= \n");
	}

	/**
	 * This function removes the geometry information in the steps because it's not
	 * currently use.
	 * 
	 * @param s String
	 * 
	 */
	private String indentedStringGeometry(String s) {
		if (s == null) {
			return "null";
		}
		return s.replaceAll(
				"geometry=[^,]++,|out=[^,]++,|in=[^,]++,|entry=\\[[^\\]]++\\],|bearings=\\[[^\\]]++\\]}?\\]?,", "");
	}

	/**
	 * This function takes a string and returns it with a line break at each "out"
	 * word.
	 * 
	 * @param s String
	 *
	 */
	private String indentedStringOut(String o) {
		if (o == null) {
			return "null";
		}
		return o.replaceAll("out=", "\n out=");
	}

	/**
	 * This function calculates the time and distance as well as the steps to move
	 * between the two departure and arrival address converted to latitude and
	 * longitude
	 * 
	 * @throws ApiException
	 */
	public void getDirection() throws ApiException {

		ApiClient defaultClient = TranslationAddress.connexion();

		String latLonAddressDeparture = firstAddress.getLongitude() + "," + firstAddress.getLatitude();
		String latLonAddressArrival = secondAddress.getLongitude() + "," + secondAddress.getLatitude();

		DirectionsApi api = new DirectionsApi(defaultClient);

		DirectionsDirections response = api.directions(latLonAddressDeparture + ";" + latLonAddressArrival, null, null,
				null, null, null, null, "true", null, null, "simplified", null);
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
