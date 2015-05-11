package com.ff.rest.inventory;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;

@Path("/helloworld")
public class HelloWorldResource {

	// This method will process HTTP GET requests
	@GET
	@Produces("text/html")
	public String returnHello() {
		
		return "<H1>Hello jpa03   from a REST resource !!!! </H1> " + new java.util.Date();
	}
}