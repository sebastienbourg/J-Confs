package io.github.oliviercailloux.jconfs.conference;

import java.net.URL;
import java.time.Instant;
import java.util.Objects;

public class ConferenceBuilder {
	URL url;
	String uid;
	String title;
	Instant startDate;
	Instant endDate;
	double registrationFee;
	String country;
	String city;

	public static ConferenceBuilder given(String uid, URL url, String title, Instant startDate, Instant endDate,
			double registrationFee, String country, String city) {
		return new ConferenceBuilder(uid, url, title, startDate, endDate, registrationFee, country, city);

	}

	private ConferenceBuilder(String uid, URL url, String title, Instant startDate, Instant endDate,
			double registrationFee, String country, String city) {
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
}
