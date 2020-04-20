package io.github.oliviercailloux.jconfs.location;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.locationiq.client.ApiException;

class TranslationTest {

	@Test
	public final void creatInstanceTest() throws ApiException{
		Translation t = Translation.given();
		String lon = t.getLongitude();
		assertEquals(null,lon);
		assertEquals(null,t.getLatitude());
		assertEquals(0,t.getAdressInformations().size());
	}
	
	@Test
	public final void  addressInformationsTest() throws LocationIq.ApiException {
		Translation t = Translation.given();
		t.TransalteAdresse("Université paris dauphine");
		assertEquals(5,t.getAdressInformations().size());
	}
	
	@Test
	public final void treatmentAdressInformationsTest() throws LocationIq.ApiException {
		Translation t = Translation.given();
		t.TransalteAdresse("Université paris dauphine");
		t.selectionAddressInformation();
		boolean test = t.addressComparison(t.getAdressFound());
		assertEquals(true,test);
	}

}
