package io.github.oliviercailloux.jconfs.location;


import org.junit.jupiter.api.Test;

import LocationIq.ApiException;

class TestDirection {

	@Test
	void test() throws ApiException {

		Direction d = Direction.given("2.287592,48.862725","2.3488,48.85341");// 13 Rue Cloche Percé, 75004 Paris et cadéro et du 11 Novembre, 75016 Paris  	
		d.getDirection();		   		
		System.out.println(d.getDistance()+" metres et "+d.getDuree()+ " seconds ");  
		System.out.println(d.getSteps());


	}
}
