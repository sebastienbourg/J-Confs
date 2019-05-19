package io.github.oliviercailloux.y2018.jconfs;

import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;


/*
 * An object of PathStep represents a path step of a conference with parametres
 * (type of transport, departure and arrival place) that describe this path
 * 
 */
public class PathStep {
	

	/**
	 * Not <code> null</code>, noTransport if unknown
	 */
	private TransportType type = TransportType.NOTRANSPORT;

	/**
	 * Not <code> null</code>, "" if unknown
	 */
	private String startingPoint = "";

	/**
	 * Not <code> null</code>, "" if unknown
	 */
	private String arrivalPoint = "";

	/**
	 * this is a constructor which initializes the PathStep object The TransporType
	 * is noTranport by default
	 * 
	 * @param startingPoint
	 *            not <code>null</code>.
	 * @param arrivalPoint
	 *            not <code>null</code>.
	 */

	public PathStep(String startingPoint, String arrivalPoint) {
		this.startingPoint = Preconditions.checkNotNull(startingPoint,"The path must have a start location");
		this.arrivalPoint = Preconditions.checkNotNull(arrivalPoint,"The path must have an end location");
	}

	/**
	 * this is a constructor which initializes the PathStep object
	 * 
	 * @param startingPoint
	 *            not <code>null</code>.
	 * @param arrivalPoint
	 *            not <code>null</code>.
	 * @param type
	 *            not <code>null</code>.
	 */
	public PathStep(String startingPoint, String arrivalPoint, TransportType type) {
		this(startingPoint, arrivalPoint);
		this.type = Preconditions.checkNotNull(type,"The path must have a transport type");
	}

	/**
	 * this is a getter which return the type
	 * 
	 * @return not <code>null</code>.
	 */
	public TransportType getType() {
		return type;
	}

	/**
	 * this is a getter which return the startingPoint
	 * 
	 * @return not <code>null</code>.
	 */
	public String getStartingPoint() {
		return startingPoint;
	}

	/**
	 * This is a getter which return the arrivalPoint
	 * 
	 * @returnnot <code>null</code>.
	 */

	public String getArrivalPoint() {
		return arrivalPoint;
	}

	/**
	 * This is a setter to modify the type
	 * 
	 * @param type
	 *            can't be null
	 */
	public void setType(TransportType type) {
		this.type = Preconditions.checkNotNull(type,"The path must have a transport type");
	}

	/**
	 * a setter to modify the startingPoint
	 * 
	 * @param startingPoint
	 *            not <code>null</code>
	 */
	public void setStartingPoint(String startingPoint) {
		this.startingPoint = Preconditions.checkNotNull(startingPoint,"The path must have a start location");
	}

	/**
	 * a setter to modify the arrivalPoint
	 * 
	 * @param arrivalPoint
	 *            not <code>null</code>.
	 */
	public void setArrivalPoint(String arrivalPoint) {
		this.arrivalPoint = Preconditions.checkNotNull(arrivalPoint,"The path must have an end location");
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("StartingPoint", startingPoint)
				.add("ArrivalPoint", arrivalPoint)
				.add("Type", type)
				.toString();
	}

}
// end of class PathStep
