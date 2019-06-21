package io.github.oliviercailloux.y2018.jconfs;

import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import com.google.common.base.MoreObjects;

import net.fortuna.ical4j.data.ParserException;

/**
 * @author huong,camille	
 * @author Alison This class is immutable.
 *
 */
public class Conference {
	private URL url;
	private String uid;
	private String title;
	private LocalDate startDate;
	private LocalDate endDate;
	private Double registrationFee;
	private String country;
	private String city;

	/**
	 * This is a constructor which initializes the conference object
	 * 
	 * @param uid
	 * 			not <code>null</code>
	 * @param url
	 * @param title
	 * @param startDate
	 *            not <code>null</code>
	 * @param endDate
	 *            not <code>null</code>
	 * @param registrationFee
	 * @param country
	 * @param city
	 */
	public Conference(String uid,URL url, String title, String startDate, String endDate, Double registrationFee, String country,
			String city) {
		this.uid=uid;
		Objects.requireNonNull(uid);
		Objects.requireNonNull(endDate);
		Objects.requireNonNull(startDate);
		this.url = url;
		this.title = title;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			this.startDate = LocalDate.parse(startDate, formatter);
			this.endDate = LocalDate.parse(endDate, formatter);
		} catch (Exception e) {
			throw new IllegalArgumentException("Date impossible to put in the conference", e);
		}
		this.registrationFee = registrationFee;
		this.country = country;
		this.city = city;
	}

	/**
	 * This is a constructor which initializes the conference object without
	 * optional data
	 * @param uid
	 * 	          not <code>null</code>
	 * @param startDate
	 *            not <code>null</code>
	 * @param endDate
	 *            not <code>null</code>
	 */
	public Conference(String uid,LocalDate startDate, LocalDate endDate) {
		Objects.requireNonNull(uid);
		Objects.requireNonNull(endDate);
		Objects.requireNonNull(startDate);
		this.uid=uid;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/**
	 * This is a constructor which initializes the conference object for classes without caldav4j
	 * @param url
	 * @param title
	 * @param startDate
	 *            not <code>null</code>
	 * @param endDate
	 *            not <code>null</code>
	 * @param registrationFee
	 * @param country
	 * @param city
	 */
	public Conference(URL url, String title, String startDate, String endDate, Double registrationFee, String country,
			String city) {
		Objects.requireNonNull(endDate);
		Objects.requireNonNull(startDate);
		this.url = url;
		this.title = title;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			this.startDate = LocalDate.parse(startDate, formatter);
			this.endDate = LocalDate.parse(endDate, formatter);
		} catch (Exception e) {
			throw new IllegalArgumentException("Date impossible to put in the conference", e);
		}
		this.registrationFee = registrationFee;
		this.country = country;
		this.city = city;
	}

	/**
	 * This is a getter which return the URL
	 * 
	 * @return not <code>null</code>
	 */
	public URL getUrl() {
		return url;
	}

	/**
	 * This is a getter which return the title
	 * 
	 * @return not <code>null</code>
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * This is a getter which return the date start
	 * 
	 * @return not <code>null</code>
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * This is a getter which return the date end
	 * 
	 * @return endDate
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * This is a getter which return the fee of registration
	 * 
	 * @return registrationFee
	 */
	public Double getFeeRegistration() {
		return registrationFee;
	}

	/**
	 * This is a getter which return the country
	 * 
	 * @return country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * This is a getter which return the city
	 * 
	 * @return city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * This is a getter which return the iud
	 * 
	 * @return city
	 */
	public String getUid() {
		return this.uid;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Conference) {
			Conference conference2 = (Conference) obj;

			if (title.equals(conference2.title) && url.equals(conference2.url)
					&& startDate.equals(conference2.startDate) && endDate.equals(conference2.endDate)
					&& registrationFee.equals(conference2.registrationFee) && city.equals(conference2.city)
					&& country.equals(conference2.country)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(url, title, registrationFee, startDate, endDate, country, city);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("UID", uid)
				.add("url", url)
				.add("title", title)
				.add("startDate", startDate)
				.add("endDate", endDate)
				.add("registrationFee", registrationFee)
				.add("country", country)
				.add("city", city)
				.toString();
	}

}