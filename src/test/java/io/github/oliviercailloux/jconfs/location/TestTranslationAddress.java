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
	 * this method test the creation of an address
	 */

	@Test
	public final void creatInstanceTest() {
		TranslationAddress t = TranslationAddress.newInstance();
		assertEquals(null, t.getLatitude());
		assertEquals(0, t.getAddressInformations().size());
	}

	/**
	 * This method test the correct recovery of all the information returned by the
	 * autocomplete method of LocationIQ
	 * 
	 * @throws LocationIq.ApiException
	 * @throws InterruptedException
	 */

	@Test
	public final void recoveryAddressInformationsTest() throws ApiException, InterruptedException {
		TranslationAddress t = TranslationAddress.newInstance();
		TimeUnit.SECONDS.sleep(1);
		t.recoveryAddressInformations("Université paris dauphine");
		boolean test = (t.getAddressInformations().size() > 2);
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
	public final void recoveryAddressFoundTest() throws ApiException, InterruptedException {
		TranslationAddress t = TranslationAddress.newInstance();
		t.recoveryAddressInformations("Université paris dauphine");
		TimeUnit.SECONDS.sleep(1);
		t.recoveryAddressFound();
		boolean test = (t.getAddressFound().size() > 2);
		assertEquals(true, test);
	}

	/**
	 * This method tests the builder
	 * 
	 * @throws LocationIq.ApiException
	 */

	@Test
	public final void builderTest() throws ApiException {
		TranslationAddress address = TranslationAddress.TranslationAddressBuilder.build()
				.addressInformations("Avenue jean rostand domont 95330").addressFound().latitude().longitude().get();
	}

	/**
	 * This method tests that the latitude and longitude to retrieve are correct
	 * 
	 * @throws LocationIq.ApiException
	 */

	@Test
	public final void latitudeLongitudeTest() throws ApiException {
		TranslationAddress address = TranslationAddress.TranslationAddressBuilder.build()
				.addressInformations("Avenue jean rostand domont 95330").addressFound().latitude().longitude().get();
		System.out.println(address.getLatitude());
		System.out.println(address.getLongitude());
		assertEquals("49.0327146", address.getLatitude());
		assertEquals("2.3425254", address.getLongitude());
	}

}
