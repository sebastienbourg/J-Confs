package io.github.oliviercailloux.jconfs.location;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.locationiq.client.ApiException;

/**
 * 
 * @author Floryan
 *
 */

class TestTranslationAddress {

	/**
	 * this method test the creation of an adress
	 * 
	 * @throws ApiException
	 */

	@Test
	public final void creatInstanceTest() throws ApiException {
		TranslationAddress t = TranslationAddress.newInstance();
		assertEquals(null, t.getLatitude());
		assertEquals(0, t.getAdressInformations().size());
	}

	/**
	 * This method test the correct recovery of all the information returned by the
	 * autocomplete method of LocationIQ
	 * 
	 * @throws LocationIq.ApiException
	 * @throws InterruptedException 
	 */

	
	  @Test 
	  public final void recoveryAddressInformationsTest() throws LocationIq.ApiException, InterruptedException { 
		  TranslationAddress t =TranslationAddress.newInstance();
		  t.recoveryAddressInformations("Université paris dauphine"); 
		  TimeUnit.SECONDS.sleep(2);
		  boolean test =(t.getAdressInformations().size() > 2);
		  assertEquals(true, test); 
	  }
	 

	/**
	 * This method tests the recovery of several addresses informations associated
	 * with a search.
	 * 
	 * @throws LocationIq.ApiException
	 * @throws InterruptedException 
	 */

	@Test
	public final void recoveryAddressFoundTest() throws LocationIq.ApiException, InterruptedException {
		TranslationAddress t = TranslationAddress.newInstance();
		t.recoveryAddressInformations("1, Place du Maréchal de Lattre de Tassigny");
		TimeUnit.SECONDS.sleep(1);
		t.recoveryAddressFound();
		boolean test = (t.getAdressFound().size() > 2);
		assertEquals(true, test);
	}

	/**
	 * This method tests the builder
	 * 
	 * @throws LocationIq.ApiException
	 */

	@Test
	public final void builder() throws LocationIq.ApiException {
		TranslationAddress address = TranslationAddress.TranslationAddressBuilder.build()
				.addressInformations("Université paris dauphine").addressFound().latitude().longitude()
				.get();
	}

	/**
	 * This method tests that the latitude and longitude to retrieve are correct
	 * 
	 * @throws LocationIq.ApiException
	 */

	@Test
	public final void latitudeLongitude() throws LocationIq.ApiException {
		TranslationAddress address = TranslationAddress.TranslationAddressBuilder.build()
				.addressInformations("Universtié paris dauphine").addressFound().latitude().longitude()
				.get();
		System.out.println(address.getLatitude());
		System.out.println(address.getLongitude());
		assertEquals("48.87015115", address.getLatitude());
		assertEquals("2.2735218497104", address.getLongitude());
	}

}
