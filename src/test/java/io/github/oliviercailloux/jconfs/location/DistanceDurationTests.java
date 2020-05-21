package io.github.oliviercailloux.jconfs.location;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.locationiq.client.ApiException;

class DistanceDurationTests {

	/**
	 * 
	 * Unit test in order to verify the good get of distance and duration. Carreful
	 * this test depend on API calculation so it might fail in the future
	 * 
	 * @throws ApiException
	 * @throws InterruptedException
	 */

	@Test
	void testGetDistanceDuration() throws ApiException, InterruptedException {
		BigDecimal distance = new BigDecimal("11168.5");
		BigDecimal duration = new BigDecimal("1048.9");
		DistanceDuration result = DistanceDuration.newDistanceDuration("13 Rue Cloche Percé, 75004 Paris",
				"15 Avenue du général de gaulle, 92800 puteaux");
		TimeUnit.SECONDS.sleep(1);
		result.getDirection();
		assertEquals(distance,result.getDistance());
		assertEquals(duration,result.getDuration());

	}
}
