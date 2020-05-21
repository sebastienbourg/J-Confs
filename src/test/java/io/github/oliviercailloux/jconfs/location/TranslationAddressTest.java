package io.github.oliviercailloux.jconfs.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.locationiq.client.ApiException;

/**
 * 
 * @author Floryan
 *
 */

class TranslationAddressTest {

	/**
	 * Test of the creation of an newInstance of TranslationAddress
	 */

	@Test
	public final void testCreateInstance() {
		TranslationAddress t = TranslationAddress.newInstance();
		assertEquals(null, t.getLatitude());
		assertEquals(0, t.getAddressInformations().size());
	}

	/**
	 * This method test the correct recovery informations returned by the
	 * autocomplete method of LocationIQ
	 * 
	 * @throws LocationIq.ApiException
	 * @throws InterruptedException
	 */

	@Test
	public final void testRecoveryAddressInformations() throws ApiException, InterruptedException {
		TranslationAddress t = TranslationAddress.newInstance();
		TimeUnit.SECONDS.sleep(1);
		t.recoveryAddressInformations("Université paris dauphine");
		boolean test = (t.getAddressInformations().size() > 2);
		assertEquals(true, test);
	}

	/**
	 * This method tests the recovery of several addresses informations associated
	 * with a search. In this case we know that Université paris dauphine might
	 * return more than 1 address.
	 * 
	 * @throws LocationIq.ApiException
	 * @throws InterruptedException
	 */

	@Test
	public final void testRecoveryAddressFound() throws ApiException, InterruptedException {
		TranslationAddress t = TranslationAddress.newInstance();
		t.recoveryAddressInformations("Université paris dauphine");
		TimeUnit.SECONDS.sleep(1);
		t.recoveryAddressFound();
		boolean test = (t.getAddressFound().size() > 2);
		assertEquals(true, test);
	}

	/**
	 * This method tests the builder of TranslationAddress
	 * 
	 * @throws LocationIq.ApiException
	 */

	@Test
	public final void testBuilder() throws ApiException {
		TranslationAddress address = TranslationAddress.TranslationAddressBuilder.build()
				.addressInformations("Avenue jean rostand domont 95330").addressFound().latitude().longitude().get();
		assertTrue(address.getAddressFound().contains("Avenue Jean Rostand, Domont, Ile-de-France, 95330, France"));
		assertFalse(address.getLatitude().isEmpty());
		assertFalse(address.getLongitude().isEmpty());
	}

}
