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
 * are a string containing all the routes indication.
 * 
 * @author Anis HAMOUNI & Sébastien BOURG
 */
public class DistanceDuration {
	private BigDecimal duration;
	private BigDecimal distance;
	private String steps;
	private TranslationAddress addressDeparture;
	private TranslationAddress addressArrival;

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
	public static DistanceDuration newDistanceDuration(String dep, String arriv) throws ApiException {
		return new DistanceDuration(dep, arriv);
	}

	private DistanceDuration(String dep, String arriv) throws ApiException {
		this.duration = this.distance = BigDecimal.ZERO;
		this.steps = "";
		this.addressDeparture = TranslationAddress.TranslationAddressBuilder.build().addressInformations(dep)
				.addressFound().latitude().longitude().get();
		this.addressArrival = TranslationAddress.TranslationAddressBuilder.build().addressInformations(arriv)
				.addressFound().latitude().longitude().get();
	}

	/**
	 * Return the duration in second
	 * 
	 * @return duration
	 */
	public BigDecimal getDuration() {
		return this.duration;
	}

	/**
	 * Return the distance in meters
	 * 
	 * @return distance
	 */
	public BigDecimal getDistance() {
		return this.distance;
	}

	/**
	 * Return the departure address
	 * 
	 * @return addressDeparture
	 */
	public TranslationAddress getDeparture() {
		return this.addressDeparture;
	}

	/**
	 * Return the arrival address
	 * 
	 * @return addressArrival
	 */
	public TranslationAddress getArrival() {
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
	 * This function calculates the time, the distance and the steps to move between
	 * two address (converted to latitude and longitude using TranslationAddress
	 * class)
	 * 
	 * @throws ApiException
	 */
	public void getDirection() throws ApiException {

		ApiClient defaultClient = TranslationAddress.connexion();

		String latLonAddressDeparture = this.addressDeparture.getLongitude() + ","
				+ this.addressDeparture.getLatitude();
		String latLonAddressArrival = this.addressArrival.getLongitude() + "," + this.addressArrival.getLatitude();

		DirectionsApi api = new DirectionsApi(defaultClient);

		DirectionsDirections response = api.directions(latLonAddressDeparture + ";" + latLonAddressArrival, null, null,
				null, null, null, null, "true", null, null, "simplified", null);
		List<DirectionsDirectionsRoutes> routes = response.getRoutes();
		Iterator<DirectionsDirectionsRoutes> ite = routes.iterator();
		while (ite.hasNext()) {
			DirectionsDirectionsRoutes oneDirection = ite.next();
			this.distance = this.distance.add(oneDirection.getDistance());
			this.duration = this.duration.add(oneDirection.getDuration());
			this.steps = this.steps + oneDirection.toString();

		}

	}

}
