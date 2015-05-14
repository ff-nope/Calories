package com.ff.rest.crud;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

import com.ff.util.*;
import com.ff.dao.DbCalorias;
import com.ff.model.Usuario;
import com.ff.util.printUsuarioJSON;

@Path("/v2/crud")
public class V2_crud {

	
	@GET
	@Produces(MediaType.TEXT_HTML)										// Default URL has no job, so getting there
	public String returnReadme() { 										// points to the readme page
		return "<p>This is the backend server.</p> <p> Please see <a href='http://localhost:7001/jpa03/readme.html'> the readme file  </a> for usage details. ";
	}

	
	
	@Path("/chk_user")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response chk_user(String incomingData) throws Exception { 
		
		String MSG = null;												// Receives a last and first name and returns the row if user exists.
		Usuario usuario = new Usuario();								// Expects a string similar to this in JSON:
		printUsuarioJSON p = new printUsuarioJSON();					// {"fname":"Isadora","lname":"Duncan"}
		int http_code = 0;
		DbCalorias dao = new DbCalorias();
		Response_util response_util = new Response_util();

		try {
			JSONObject partsData = new JSONObject(incomingData);
			String bacFName = partsData.optString("fname");				// convert user data from JSON
			String bacLName = partsData.optString("lname");			
			
			usuario = dao.fetch_user_by_name ( bacFName,  bacLName); 	// call to model to check if user exists 

			if (usuario ==  null) {
				usuario = new Usuario();
				usuario.setFName(bacFName);
				usuario.setLName(bacLName);
				http_code = 206; //
				MSG = "User created in memory.";
			} else {
				http_code = 200;
				MSG = "User exists.";
			}
			Usuario bPobre = usuario; 									// needs to get rid of the Items
																		// collection, to avoid recursive trap  when Item maps back. 
																		// Jackson annotations (@JsonManagedReference, @JsonBackReference)
																		// are blowing, so we go around.
			bPobre.setItems(null);
			return response_util.response_Gen( MSG,  http_code,p.retornaJason(bPobre) );
		} catch (Exception e) {
			e.printStackTrace();
			MSG = "Server was not able to process your request.";
			return response_util.response_Gen( MSG,  500);
		}
	} // end of chk_user

	
	
	
	@Path("/rec_user")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response rec_user(String incomingData) throws Exception { 	// Records a brand new user, without items.
																		// Should receive something like this -
		String MSG = null;												// {"fname":"Isadora","lname":"Duncan","dob":"1929/01/22"}
		int http_code = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		Calendar calStart = Calendar.getInstance();
		DbCalorias dao = new DbCalorias();
		Response_util response_util = new Response_util();
		
		try {
			JSONObject partsData = new JSONObject(incomingData);
			String bacFName = partsData.optString("fname");				// Translate from JSON
			String bacLName = partsData.optString("lname");
			String bacdob = partsData.optString("dob");
			calStart.setTime(formatter.parse(bacdob));
			
			http_code = dao.rec_user_by_name ( bacFName,  bacLName, calStart); 	// call model for database record creation. Result comes back on http_code.

			if (http_code == 406) {  									// creation not sucessfull
				MSG = "User exists. It was not supposed to exist, so you are in trouble.";
				return response_util.response_Gen( MSG,  http_code);
			}
			MSG = "User created.";
			return response_util.response_Gen( MSG,  http_code);
		} catch (Exception e) {
			e.printStackTrace();
			MSG = "Server was not able to process your request.";
			return response_util.response_Gen( MSG,  500);
		}

	} // end of rec_user

	
	
	
	@Path("/rec_item")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response rec_item(String incomingData) throws Exception { 	// Receives user & item from view in JSON, calls dao in model to persist item
		 																// Expects a JSON string similar to this -
		String MSG = null;												// {"fname":"Isadora","lname":"Duncan","dob":"1929/01/22","memitem":"Apple Pie","calorias":600,"quando":1431101164}
		int http_code = 0;
		DbCalorias dao = new DbCalorias();
		Response_util response_util = new Response_util();

		try {
			JSONObject partsData = new JSONObject(incomingData);
																					
			String bacFName = partsData.optString("fname");				// Grab user data from JSON, to query the entity. First user,
			String bacLName = partsData.optString("lname");				//  then item, to prepare for persistence.
																					
			String bacItem = partsData.optString("memitem");
			int bacCalorias = partsData.optInt("calorias");
			long bacQuando = partsData.optLong("quando");
			
			http_code = dao.rec_item_by_user_name(bacFName, bacLName,  bacItem,bacCalorias, bacQuando); // Check consistency then if OK persist item.
			
			if (http_code == 406) {  									// could not find an user to whom attach my item
				MSG = "User does not exist. It was supposed to exist, so you are in trouble.";
				return response_util.response_Gen( MSG,  http_code);
			}
																		// item persistence went well. Time to return.
			MSG = "Item persisted.";
			return response_util.response_Gen( MSG,  http_code);
		} catch (Exception e) {
			e.printStackTrace();
			MSG = "Server was not able to process your request.";
			return response_util.response_Gen( MSG,  500);
		}
	} // fim do metodo rec_item




	@Path("/del_user")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response del_user(String incomingData) throws Exception { 
																		// Deletes user by name
		String MSG = null;												// expects name in JSON, thusly -
		int http_code = 0;												// {"fname":"Isadora","lname":"Duncan"}
		DbCalorias dao = new DbCalorias();								// Possible remaining items go as well, thanks to
		Response_util response_util = new Response_util();				// CascadeType set to ALL Note that orphanremoval=true has no effect here.

		try {
			JSONObject partsData = new JSONObject(incomingData);
			String bacFName = partsData.optString("fname");
			String bacLName = partsData.optString("lname");
			http_code = dao.del_user_by_name(bacFName, bacLName);		// calls model to delete user
			if (http_code == 500) {  									// could not find an user
				MSG = "User does not exist. I can't kill what is not there.";
				return response_util.response_Gen( MSG,  http_code);
			}
			MSG = "User deleted.";
			return response_util.response_Gen( MSG,  http_code);
		} catch (Exception e) {
			e.printStackTrace();
			MSG = "BLEW! Exception. Happy troubleshooting.";
			return response_util.response_Gen( MSG,  500);
		}
	} // end of del_user



	@Path("/del_item")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response del_item(String incomingData) throws Exception { 

		String MSG = null;												// Deletes an item by user name and item index.
		int http_code = 0;												// Expect 3 fields on JSON, like this -
		DbCalorias dao = new DbCalorias();								// {"fname":"Isadora","lname":"Duncan","idxToKill":0}	
		Response_util response_util = new Response_util();

		try {
			JSONObject partsData = new JSONObject(incomingData);
			String bacFName = partsData.optString("fname");
			String bacLName = partsData.optString("lname");
			int bacidxToKill = partsData.optInt("idxToKill");
			
			http_code = dao.del_item_by_idx(bacFName,bacLName,bacidxToKill) ;

			if (http_code == 406 || http_code == 500) {
				MSG = "Could not delete item";
				return response_util.response_Gen( MSG,  http_code);
			}
			MSG = "Item deleted."; 
			return response_util.response_Gen( MSG,  http_code);
			
		} catch (Exception e) {
			e.printStackTrace();
			MSG =  "BLEW! Exception. Happy troubleshooting.";
			return response_util.response_Gen( MSG,  500);
		}

	} // end of del_item





} // end of  V2_crud
