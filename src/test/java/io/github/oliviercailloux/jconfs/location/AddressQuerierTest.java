package io.github.oliviercailloux.jconfs.location;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.locationiq.client.ApiException;

/**
 * 
 * @author Floryan
 *
 */

class AddressQuerierTest {

	/**
	 * This method test the correct recovery informations returned by the
	 * autocomplete method of LocationIQ. Important : A sleep of 1 second as been
	 * added due to the request limit per second of the API
	 * 
	 * @throws LocationIq.ApiException
	 * @throws InterruptedException
	 * @throws FileNotFoundException
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 */

	@Test
	public final void testRecoveryAddressInformations()
			throws ApiException, InterruptedException, JsonIOException, JsonSyntaxException, FileNotFoundException {
		AddressQuerier t = AddressQuerier.given("Université paris dauphine");
		TimeUnit.SECONDS.sleep(1);
		boolean test = false;
		for (String s : t.getAddressInformations()) {
			if (s.contains("Place du Maréchal de Lattre de Tassigny")) {
				test = true;
			}
		}
		assertTrue(test);
	}

	/**
	 * This method tests the recovery of several addresses informations associated
	 * with a search. In this case we know that Université paris dauphine might
	 * return more than 1 address. Important : A sleep of 1 second as been added due
	 * to the request limit per second of the API
	 * 
	 * @throws LocationIq.ApiException
	 * @throws InterruptedException
	 * @throws FileNotFoundException
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 */

	@Test
	public final void testRecoveryAddressFound()
			throws ApiException, InterruptedException, JsonIOException, JsonSyntaxException, FileNotFoundException {
		AddressQuerier t = AddressQuerier.given("Université paris dauphine");
		TimeUnit.SECONDS.sleep(1);
		boolean test = (t.getAddressFound().size() >= 2);
		Address test2 = t.getAddressFound().get(0);
		assertTrue(test);
		assertTrue(test2.getAddress().contains("Place du Maréchal de Lattre de Tassigny"));
		assertEquals(test2.getLatitude(), "48.87015115");
	}
}
