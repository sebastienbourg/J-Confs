package io.github.oliviercailloux.jconfs.conference;

import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import com.google.common.base.MoreObjects;

/**
 * @author huong,camille This class is immutable.
 *
 */
public class Conference {
	private URL url;
	private String uid;
	private String title;
	private Instant startDate;
	private Instant endDate;
	private double registrationFee;
	private String country;
	private String city;

	/**
	 * This is a constructor which initializes the conference object
	 * 
	 * @param uid
	 * @param url
	 * @param title
	 * @param startDate       not <code>null</code>
	 * @param endDate         not <code>null</code>
	 * @param registrationFee
	 * @param country
	 * @param city
	 */
	public Conference(String uid, URL url, String title, Instant startDate, Instant endDate, double registrationFee,
			String country, String city) {
		Objects.requireNonNull(endDate);
		Objects.requireNonNull(startDate);
		this.uid = uid;
		this.url = url;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.registrationFee = registrationFee;
		this.country = country;
		this.city = city;
	}

	/**
	 * This is a getter which return the URL
	 * 
	 * @return url
	 */
	public URL getUrl() {
		return url;
	}

	/**
	 * This is a getter which return the title
	 * 
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * This is a getter which return the date start
	 * 
	 * @return not <code>null</code>
	 */
	public Instant getStartDate() {
		return startDate;
	}

	/**
	 * This is a getter which return the date end
	 * 
	 * @return not <code>null</code>
	 */
	public Instant getEndDate() {
		return endDate;
	}

	/**
	 * This is a getter which return the fee of registration
	 * 
	 * @return registrationFee
	 */
	public double getFeeRegistration() {
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
	 * @return not <code>null</code>
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
					&& registrationFee == (conference2.registrationFee) && city.equals(conference2.city)
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
		return MoreObjects.toStringHelper(this).add("UID", uid).add("url", url).add("title", title)
				.add("startDate", startDate).add("endDate", endDate).add("registrationFee", registrationFee)
				.add("country", country).add("city", city).toString();
	}

}