package io.github.oliviercailloux.jconfs.location;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.locationiq.client.ApiException;

class DistanceDurationTests {

	/**
	 * 
	 * Unit test in order to verify the good get of distance and duration. Carreful
	 * this test depend on API calculation so it might fail in the future. Important
	 * : A sleep of 1 second as been added due to the request limit per second of
	 * the API free use.
	 * 
	 * @throws ApiException
	 * @throws InterruptedException
	 * @throws FileNotFoundException
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 */

	@Test
	void testGetDistanceDuration()
			throws ApiException, InterruptedException, JsonIOException, JsonSyntaxException, FileNotFoundException {
		Address departure = Address.given("13 Rue Cloche Percé, 75004 Paris", "48.8564037", "2.3572215");
		Address arrival = Address.given("15 Avenue du général de gaulle, 92800 puteaux", "48.8851553", "2.2269999");
		DistanceDuration result = DistanceDuration.newDistanceDuration(departure, arrival);
		TimeUnit.SECONDS.sleep(1);
		assertEquals(11005, result.getDistance());
		assertEquals(1027, result.getDuration());
		assertEquals(132, result.getAllSteps().size());
	}

}
