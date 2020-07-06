package io.github.oliviercailloux.jconfs.location;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.locationiq.client.ApiClient;
import com.locationiq.client.ApiException;
import com.locationiq.client.api.DirectionsApi;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.locationiq.client.model.*;

/**
 * This class allows to get a path and informations about it. A trajet is define
 * beetween two addresses (see Address class). Using LocationIQ "DirectionsAPI"
 * you can get the total distance (in meters) and duration (in seconds) for the
 * journey. The global informations about the path (stored in a Step object) can
 * be get using getGeneralStep method. You can also get all the steps of the
 * journey (stored as a List<Step>) using getAllSteps method.
 * 
 * @author Anis HAMOUNI & sbourg & ZOUARI Anis
 */
public class DistanceDuration {

	private List<Step> allSteps;
	private Step generalStep;

	/**
	 * 
	 * Static factory method to build a DistanceDuration instance.
	 * 
	 * @param dep   Address
	 * @param arriv Address
	 * 
	 */
	public static DistanceDuration newDistanceDuration(Address dep, Address arriv) {
		return new DistanceDuration(dep, arriv);
	}

	/**
	 * Distance and Duration of generalStep are temporally set to -1 and will be
	 * calculate by calling locationIQ API
	 * 
	 */
	private DistanceDuration(Address dep, Address arriv) {
		this.allSteps = new ArrayList<>();
		this.generalStep = Step.newStep(dep, arriv, -1, -1);
	}

	/**
	 * Return the duration in seconds
	 * 
	 * @return duration
	 * @throws ApiException
	 * @throws FileNotFoundException
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 */
	public int getDuration() throws ApiException, JsonIOException, JsonSyntaxException, FileNotFoundException {
		if (this.generalStep.getDuration() == -1)
			calculateDistanceDuration();
		return this.generalStep.getDuration();
	}

	/**
	 * Return the distance in meters
	 * 
	 * @return distance
	 * @throws ApiException
	 * @throws FileNotFoundException
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 */
	public int getDistance() throws ApiException, JsonIOException, JsonSyntaxException, FileNotFoundException {
		if (this.generalStep.getDistance() == -1)
			calculateDistanceDuration();
		return this.generalStep.getDistance();
	}

	/**
	 * Return the departure address
	 * 
	 * @return addressDeparture
	 */
	public Address getDeparture() {
		return this.generalStep.getDepartureAddress();
	}

	/**
	 * Return the arrival address
	 * 
	 * @return addressArrival
	 */
	public Address getArrival() {
		return this.generalStep.getArrivalAddress();
	}

	/**
	 * Return all the detailed steps of the path
	 * 
	 * @return allSteps
	 * @throws ApiException
	 * @throws FileNotFoundException
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 */
	public List<Step> getAllSteps() throws ApiException, JsonIOException, JsonSyntaxException, FileNotFoundException {
		if (this.allSteps.isEmpty())
			calculateDistanceDuration();

		return this.allSteps;
	}

	/**
	 * Return the generalStep composed of the departure and arrival address and the
	 * global distance and duration. It verified if the distance and the duration
	 * had been calculated and otherwise it use locationIQ to calculate it.
	 * 
	 * 
	 * @return generalStep
	 * @throws ApiException
	 * @throws FileNotFoundException
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 */
	public Step getGeneralStep() throws ApiException, JsonIOException, JsonSyntaxException, FileNotFoundException {
		if (this.generalStep.getDistance() == -1 || this.generalStep.getDuration() == -1)
			calculateDistanceDuration();

		return this.generalStep;
	}

	/**
	 * This function use the LocataionIQ "DirectionsAPI" to calculates the duration,
	 * the distance and all the informations about the path between the two
	 * addresses of the class.
	 * 
	 * @throws ApiException
	 * @throws FileNotFoundException
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 */
	private void calculateDistanceDuration()
			throws ApiException, JsonIOException, JsonSyntaxException, FileNotFoundException {
		String latLonAddressDeparture = this.generalStep.getDepartureAddress().getLongitude() + ","
				+ this.generalStep.getDepartureAddress().getLatitude();
		String latLonAddressArrival = this.generalStep.getArrivalAddress().getLongitude() + ","
				+ this.generalStep.getArrivalAddress().getLatitude();

		ApiClient defaultClient = AddressQuerier.connexion();
		DirectionsApi api = new DirectionsApi(defaultClient);
		DirectionsDirections response = api.directions(latLonAddressDeparture + ";" + latLonAddressArrival, null, null,
				null, null, null, null, "true", null, null, "simplified", null);

		DirectionsDirectionsRoutes trip = response.getRoutes().get(0);
		int distance = trip.getDistance().intValue();
		int duration = trip.getDuration().intValue();
		this.generalStep = Step.newStep(this.generalStep.getDepartureAddress(), this.generalStep.getArrivalAddress(),
				distance, duration);
		String[] tabToParse = trip.getLegs().get(0).toString().split("location=\\[");
		List<String> listToParse = new ArrayList<>(Arrays.asList(tabToParse));
		listToParse.remove(0);
		listToParse.remove(0);
		listToParse.remove(listToParse.size() - 1);
		for (int i = 0; i < listToParse.size() - 1; i++) {
			String departureLatitude = listToParse.get(i).split("]")[0].split(",")[1];
			String departureLongitude = listToParse.get(i).split("]")[0].split(",")[0];
			String arrivalLatitude = listToParse.get(i + 1).split("]")[0].split(",")[1];
			String arrivalLongitude = listToParse.get(i + 1).split("]")[0].split(",")[0];
			Address departureStep = Address.given(null, departureLatitude, departureLongitude);
			Address arrivalStep = Address.given(null, arrivalLatitude, arrivalLongitude);
			Step oneNewStep = Step.newStep(departureStep, arrivalStep);
			this.allSteps.add(oneNewStep);
		}
	}
}
