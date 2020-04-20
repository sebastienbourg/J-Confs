package io.github.oliviercailloux.jconfs.location;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.locationiq.client.ApiException;

class TranslationTest {
	
	/**
	 * this method test the creation of an adress
	 * @throws ApiException
	 */
	
	@Test
	public final void creatInstanceTest() throws ApiException{
		Translation t = Translation.given();
		String lon = t.getLongitude();
		assertEquals(null,lon);
		assertEquals(null,t.getLatitude());
		assertEquals(0,t.getAdressInformations().size());
	}
	
	/**
	 * This method test the correct recovery of all the information returned by the autocomplete method of LocationIQ
	 * @throws LocationIq.ApiException
	 * @throws IllegalArgumentException
	 */
	
	@Test
	public final void  addressInformationsTest() throws LocationIq.ApiException, IllegalArgumentException {
		Translation t = Translation.given();
		t.TransalteAdresse("Université paris dauphine");
		assertEquals(5,t.getAdressInformations().size());
	}
	
	/**
	 * This method test the correct processing of data with the recovery only of the addresses 
	 * which will then be offered to the user so that he selects the correct one.
	 * @throws LocationIq.ApiException
	 * @throws IllegalArgumentException
	 */
	
	@Test
	public final void treatmentAdressInformationsTest() throws LocationIq.ApiException, IllegalArgumentException {
		Translation t = Translation.given();
		t.TransalteAdresse("Université paris dauphine");
		t.selectionAddressInformation();
		boolean test = t.addressComparison(t.getAdressFound());
		assertEquals(true,test);
	}

}
