package io.github.oliviercailloux.jconfs.location;

//import com.locationiq.client.Configuration;
import com.locationiq.client.api.*;

import LocationIq.ApiClient;
import LocationIq.ApiException;
import java.math.BigDecimal;
//import com.locationiq.client.model.DirectionsDirections;
//import com.locationiq.client.model.Error;
import com.locationiq.client.api.DirectionsApi;
//import com.locationiq.client.model.DirectionsDirectionsRoutes;
import org.junit.Test;
import org.junit.Ignore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import LocationIq.Configuration;
import LocationIq.auth.*;
import com.locationiq.client.model.*;


public class Direction {
          private 	String depart ; 
          private  	String arrivee;
          private 	BigDecimal duree;
          private 	BigDecimal distance; 
          private   String  steps;
          /**
           * 
           * @param dep string of format longitude,latitude example "2.287592,48.862725"
           * @param arriv string of format longitude,latitude example "2.3488,48.85341
           * steps is a string that contains all steps to go on our destination 
           * 
           */
          
          public static Direction given(String dep, String arriv) {
        	  return new Direction(dep,arriv);
          }
          
          private Direction(String dep, String arriv) {
        	  this.depart = dep;
        	  this.arrivee = arriv;
        	  this.duree = distance = BigDecimal.ZERO ; 
        	  this.steps="";
          }
          
          public BigDecimal getDuree() {
        	  return duree;
          }
          public BigDecimal getDistance() {
        	  return distance;
          }
          
          public void setDuree(BigDecimal duree) {
        	  this.duree = duree;
          }
          
          public void setDepart(String dep) {
        	  this.depart=dep;
          }
          
          public void setArrivee(String arr) {
        	  this.arrivee=arr;
          }
          
          public void setDistance(BigDecimal dist) {
        	  this.distance=dist;
          }
          
          public String getSteps() {
        	  return this.steps;
          }
          
          private String indentedStringOnIntersect(String o) {
        	    if (o == null) {
        	      return "null";
        	    }
        	    return o.replaceAll("intersections=", "\n intersections= \n");
        	  }
          
          private String indentedStringGeometry(String o) {
      	    if (o == null) {
      	      return "null";
      	    }
      	    return o.replaceAll("geometry=[^,]++,", "");
      	  }
          
          private String indentedStringOut(String o) {
        	    if (o == null) {
        	      return "null";
        	    }
        	    return o.replaceAll("out=", "\n out=");
          }
        	  
          /**
           * this method create an ApiClient and connect it to to the API with our key
           * @return ApiClient
           * @throws ApiException
           */
          
          
          
          public ApiClient connexion() throws ApiException {
        	  ApiClient defaultClient = Configuration.getDefaultApiClient();
        	  defaultClient.setBasePath("https://eu1.locationiq.com/v1");
        	  ApiKeyAuth key = (ApiKeyAuth) defaultClient.getAuthentication("key");
        	  key.setApiKey("d4b9a23eaef07d");  // here our key
        	  return defaultClient;
          }
         
          public void getDirection() throws ApiException {
        	  
        	  ApiClient defaultClient = this.connexion();

        	  DirectionsApi api = new DirectionsApi(defaultClient);
        	  /**
        	   * the format of the coordinate must be a String of { longitude,latitude; longitude,latitude}
        	   */
        	  DirectionsDirections response = api.directions(depart+";"+arrivee, null, null, null, null, null, null, "true", null, null, "simplified", null);
        	  List routes = response.getRoutes();
        	  Iterator i = routes.iterator();
        	  while(i.hasNext()){
       		  DirectionsDirectionsRoutes dr = (DirectionsDirectionsRoutes) i.next();
       		  distance = distance.add( dr.getDistance());
       		  this.duree=this.duree.add( dr.getDuration());
       		  this.steps = this.steps + dr.toString();
        	  }
        	  this.steps=indentedStringOnIntersect(this.steps);
        	  this.steps=indentedStringGeometry(this.steps);
        	  this.steps=indentedStringOut(this.steps);
         }
          
         public static void main(String argv[]) throws ApiException {
        	 Direction d = given("2.287592,48.862725","2.3488,48.85341");// 13 Rue Cloche Percé, 75004 Paris et cadéro et du 11 Novembre, 75016 Paris
        	 d.getDirection();
        	 System.out.println(d.distance+" metres et "+d.duree+ " seconds ");  
        	 System.out.println(d.getSteps());
         } 
         
}
