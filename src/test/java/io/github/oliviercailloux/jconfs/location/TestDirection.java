package io.github.oliviercailloux.jconfs.location;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.locationiq.client.ApiException;

class TestDirection {

	/**
	 * 
	 * Unit test in order to verify the good get of distance and duration Carreful
	 * this test depend on API calculation so it might fail in the future
	 * 
	 * @throws ApiException
	 * @throws InterruptedException
	 */

	@Test
	void testGetDistanceDuration() throws ApiException, InterruptedException {
		BigDecimal distance = new BigDecimal("10537.2");
		BigDecimal duration = new BigDecimal("997");
		Direction d = Direction.given("13 Rue Cloche Percé, 75004 Paris", "Avenue du général de gaulle, 92800 puteaux");
		TimeUnit.SECONDS.sleep(1);
		d.getDirection();
		System.out.println(d.getDistance() + " metres et " + d.getDuration() + " seconds ");

		assertEquals(d.getDistance(), distance);
		assertEquals(d.getDuration(), duration);

	}
}
