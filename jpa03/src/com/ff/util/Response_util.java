package com.ff.util;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Response_util {

	public Response response_Gen(String MSG,  int http_code) {
		String returnString = null;				
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		
		try {
			jsonObject.put("HTTP_CODE", http_code);
			jsonObject.put("MSG", MSG);
			jsonArray.put(jsonObject);
			returnString = jsonArray.toString();
		} 
		catch (JSONException e) {
			e.printStackTrace();
			return Response.status(500)
					.entity("Server was not able to process your request.")
					.build();
		}
		return Response.status(http_code).entity(returnString).build();
	} // end of response_Gen
	
	public Response response_Gen(String MSG,  int http_code, String more) {
		String returnString = null;				
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		
		try {
			jsonObject.put("HTTP_CODE", http_code);
			jsonObject.put("MSG", MSG);
			jsonArray.put(jsonObject);
			jsonArray.put(more);
			returnString = jsonArray.toString();
		} 
		catch (JSONException e) {
			e.printStackTrace();
			return Response.status(500)
					.entity("Server was not able to process your request.")
					.build();
		}
		return Response.status(http_code).entity(returnString).build();
	} // end of response_Gen
	
}
