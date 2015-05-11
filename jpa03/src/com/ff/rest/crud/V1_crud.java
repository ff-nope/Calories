package com.ff.rest.crud;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.ff.dao.emfFac;
import com.ff.model.Item;
import com.ff.model.Usuario;
import com.ff.model.printUsuarioJSON;
import com.sun.jersey.api.Responses;

@Path("/v1/crud")
public class V1_crud {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnTest() { // >>>>> returnTest
		return "<p>This is the backend server.</p> <p> Please see <a href='http://192.168.1.81:7001/jpa03/readme.html'> the readme file  </a> for usage details. ";
	}

	@Path("/chk_user")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response chk_user(String incomingData) throws Exception { // >>>>>
		
		// Receives a last and first name and returns the row if user exists.
		
		String returnString = null;
		String MSG = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		Usuario usuario = new Usuario();
		printUsuarioJSON p = new printUsuarioJSON();
		int http_code = 0;

		try {
			System.out.println(">>>      incomingData : " + incomingData);
			JSONObject partsData = new JSONObject(incomingData);
			System.out.println("partsData   : " + partsData.toString());

			String bacFName = partsData.optString("fname");
			String bacLName = partsData.optString("lname");
			System.out.println("\n\nfname : " + bacFName + "\nlname : "
					+ bacLName + "\n\n");

			EntityManager em = emfFac.devolveEmf().createEntityManager();

			TypedQuery<Usuario> usuarioExiste = em
					.createQuery(
							"SELECT u FROM Usuario u WHERE u.FName = ?1 and u.LName=?2",
							Usuario.class);
			// getSingleResult() blows when not found; getResultList() doesn't,
			// so lets use it.
			List<Usuario> usuariosComEsseNome = usuarioExiste
					.setParameter(1, bacFName).setParameter(2, bacLName)
					.getResultList();

			if (usuariosComEsseNome.isEmpty()) {
				System.out.println("vazio"); // vou criar o registro
				usuario.setFName(bacFName);
				usuario.setLName(bacLName);
				http_code = 206; //
				MSG = "User created.";

			} else {
				System.out.println("tem gente");
				usuario = usuariosComEsseNome.get(0);
				http_code = 200;
				MSG = "User exists.";
			}

			jsonObject.put("HTTP_CODE", http_code);
			jsonObject.put("MSG", MSG);

			Usuario bPobre = usuario; // needs to get rid of the Items
										// collection, to avoid recursive trap
										// when Item maps back.
			bPobre.setItems(null);
			jsonArray.put(jsonObject);
			jsonArray.put(p.retornaJason(bPobre));
			returnString = jsonArray.toString();
			System.out.println("returnString: " + returnString);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500)
					.entity("Server was not able to process your request.")
					.build();
		}

		return Response.ok(returnString).build();
	} // fim do metodo chk_user

	@Path("/rec_user")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response rec_user(String incomingData) throws Exception { // >>>>>
																		// chk_user
		String returnString = null;
		String MSG = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		Usuario usuario = new Usuario();
		printUsuarioJSON p = new printUsuarioJSON();
		int http_code = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		Calendar calStart = Calendar.getInstance();

		try {
			System.out.println(">>>      incomingData : " + incomingData);
			JSONObject partsData = new JSONObject(incomingData);
			System.out.println("partsData   : " + partsData.toString());

			String bacFName = partsData.optString("fname");
			String bacLName = partsData.optString("lname");
			String bacdob = partsData.optString("dob");
			System.out.println("dob   : " + bacdob);
			calStart.setTime(formatter.parse(bacdob));

			EntityManager em = emfFac.devolveEmf().createEntityManager();

			// Last chance for DB integrity. If I am recording this user, then
			// it should not exist in the database, so we verify.
			TypedQuery<Usuario> usuarioExiste = em
					.createQuery(
							"SELECT u FROM Usuario u WHERE u.FName = ?1 and u.LName=?2",
							Usuario.class);
			// getSingleResult() blows when not found; getResultList() doesn't,
			// so lets use it.
			List<Usuario> usuariosComEsseNome = usuarioExiste
					.setParameter(1, bacFName).setParameter(2, bacLName)
					.getResultList();

			if (usuariosComEsseNome.isEmpty()) {
				System.out.println("vazio"); // vou criar o registro
				usuario.setFName(bacFName);
				usuario.setLName(bacLName);
				http_code = 201; //
				MSG = "User created.";

			} else {
				System.out.println("Oooops.");
				// usuario = usuariosComEsseNome.get(0);
				http_code = 406;
				MSG = "User exists. It was not supposed to exist, so you are in trouble.";

				jsonObject.put("HTTP_CODE", http_code);
				jsonObject.put("MSG", MSG);
				jsonArray.put(jsonObject);
				System.out.println(jsonArray.toString());
				// return Response.notModified(jsonArray.toString()).build();
				return Response.status(Responses.NOT_ACCEPTABLE)
						.entity(jsonArray.toString()).build();
			}

			jsonObject.put("HTTP_CODE", http_code);
			jsonObject.put("MSG", MSG);

			// Let's persist the user, shall we?
			usuario.setFName(bacFName);
			usuario.setLName(bacLName);
			usuario.setDob(calStart);

			em.getTransaction().begin();
			em.persist(usuario); // gravei usuario
			em.getTransaction().commit();
			em.close();

			jsonArray.put(jsonObject);

			returnString = jsonArray.toString();
			System.out.println("returnString: " + returnString);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500)
					.entity("Server was not able to process your request.")
					.build();
		}

		// return Response.ok(returnString).build();

		return Response.status(http_code).entity(returnString).build();
	} // fim do metodo rec_user

	
	
	
	
	
	
	
	
	
	@Path("/rec_item")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response rec_item(String incomingData) throws Exception { // >>>>>
																		// chk_user
		String returnString = null;
		String MSG = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		int http_code = 0;
		List<Item> tempItem = new ArrayList<Item>();
		Usuario usuario = new Usuario();
		Item item = new Item();

		try {
			System.out.println(">>>      incomingData : " + incomingData);
			JSONObject partsData = new JSONObject(incomingData);
			System.out.println("partsData   : " + partsData.toString());

			// Grab user data from JSON, to query the entity.
			String bacFName = partsData.optString("fname");
			String bacLName = partsData.optString("lname");

			// Item data, to prepare for persistence. Here we extract from JSON
			String bacItem = partsData.optString("memitem");
			int bacCalorias = partsData.optInt("calorias");
			long bacQuando = partsData.optLong("quando");

			
			System.out.println(">>>      aqui vai : " + bacItem + " " + bacCalorias+ "   " + bacQuando);
			
			
			// record REST data item in entity
			item.setItem(bacItem);
			item.setCalorias(bacCalorias);
			item.setQuando(bacQuando);

			EntityManager em = emfFac.devolveEmf().createEntityManager();

			// Last chance for DB integrity. If I am recording this item, then
			// the corresponding user should exist in the database, so we
			// verify.
			TypedQuery<Usuario> usuarioExiste = em
					.createQuery(
							"SELECT u FROM Usuario u WHERE u.FName = ?1 and u.LName=?2",
							Usuario.class);
			// getSingleResult() blows when not found; getResultList() doesn't,
			// so lets use it.
			List<Usuario> usuariosComEsseNome = usuarioExiste
					.setParameter(1, bacFName).setParameter(2, bacLName)
					.getResultList();

			if (!usuariosComEsseNome.isEmpty()) {
				System.out.println("User exists, we are good so far."); // vou
																		// criar
																		// o
																		// registro
				usuario = usuariosComEsseNome.get(0); // We fetch the usuario
														// entity here, ready to
														// be added a new item
				// http_code = 201; //
				// MSG = "User created.";

			} else {
				System.out.println("Oooops.");

				http_code = 406;
				MSG = "User does not exist. It was supposed to exist, so you are in trouble.";

				jsonObject.put("HTTP_CODE", http_code);
				jsonObject.put("MSG", MSG);
				jsonArray.put(jsonObject);
				System.out.println(jsonArray.toString());
				// return Response.notModified(jsonArray.toString()).build();
				return Response.status(Responses.NOT_ACCEPTABLE)
						.entity(jsonArray.toString()).build();
			}

			// ready to record item. We do that, then establish ManyToOne
			// association with usuario.
			em.getTransaction().begin();
			em.persist(usuario); // usuario to DB
			em.persist(item); // item to DB

			item.setUsuario(usuario); // persisted usuario no item
			tempItem = usuario.getItems();
			if (tempItem == null) {
				tempItem = new ArrayList<Item>();
			}
			tempItem.add(item);
			usuario.setItems(tempItem); // persisted item no usuario

			em.getTransaction().commit();
			em.close();
			System.out.println("acabou de gravar.");

			// item persistence went well. Time to return a cheerful message, code
			 http_code = 201; //
			 MSG = "Item persisted.";

			jsonObject.put("HTTP_CODE", http_code);
			jsonObject.put("MSG", MSG);

			jsonArray.put(jsonObject);

			returnString = jsonArray.toString();
			System.out.println("returnString: " + returnString);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500)
					.entity("Server was not able to process your request.")
					.build();
		}

		// return Response.ok(returnString).build();

		return Response.status(http_code).entity(returnString).build();
	} // fim do metodo rec_item











	@Path("/del_user")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response del_user(String incomingData) throws Exception { // >>>>>
																		// chk_user
		String returnString = null;
		String MSG = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		Usuario usuario = new Usuario();
		printUsuarioJSON p = new printUsuarioJSON();
		int http_code = 0;

		try {
			System.out.println(">>>      incomingData : " + incomingData);
			JSONObject partsData = new JSONObject(incomingData);
			System.out.println("partsData   : " + partsData.toString());

			String bacFName = partsData.optString("fname");
			String bacLName = partsData.optString("lname");
			System.out.println("\n\nfname : " + bacFName + "\nlname : "
					+ bacLName + "\n\n");

			EntityManager em = emfFac.devolveEmf().createEntityManager();

			TypedQuery<Usuario> usuarioExiste = em
					.createQuery(
							"SELECT u FROM Usuario u WHERE u.FName = ?1 and u.LName=?2",
							Usuario.class);
			// getSingleResult() blows when not found; getResultList() doesn't,
			// so lets use it.
			List<Usuario> usuariosComEsseNome = usuarioExiste
					.setParameter(1, bacFName).setParameter(2, bacLName)
					.getResultList();

			if (usuariosComEsseNome.isEmpty()) {
				System.out.println("vazio"); // bad vodoo. This user should have been found.
				http_code = 500; //
				MSG = "User does not exist. I can't kill what is not there.";
				jsonObject.put("HTTP_CODE", http_code);
				jsonObject.put("MSG", MSG);
				jsonArray.put(jsonObject);
				System.out.println(jsonArray.toString());
				// return Response.notModified(jsonArray.toString()).build();
				return Response.status(Responses.NOT_ACCEPTABLE)
						.entity(jsonArray.toString()).build();

			} else {
				System.out.println("Found the user - ready to delete it");
				usuario = usuariosComEsseNome.get(0);
			}
			
			
			
			// deletes user. Note that CascadeType is set to ALL, so the items will be deleted as well. 
			em.getTransaction().begin();
			em.remove(usuario);
			em.getTransaction().commit();
			em.close();

			http_code = 200;
			MSG = "User deleted.";
			jsonObject.put("HTTP_CODE", http_code);
			jsonObject.put("MSG", MSG);
			
			
			jsonArray.put(jsonObject);
			
			returnString = jsonArray.toString();
			System.out.println("returnString: " + returnString);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500)
					.entity("Server was not able to process your request.")
					.build();
		}

		return Response.ok(returnString).build();
	} // fim do metodo del_user





	@Path("/del_item")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response del_item(String incomingData) throws Exception { // >>>>>
																		// 
		String returnString = null;
		String MSG = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		Usuario usuario = new Usuario();
		printUsuarioJSON p = new printUsuarioJSON();
		int http_code = 0;

		try {
			System.out.println(">>>      incomingData : " + incomingData);
			JSONObject partsData = new JSONObject(incomingData);
			System.out.println("partsData   : " + partsData.toString());

			String bacFName = partsData.optString("fname");
			String bacLName = partsData.optString("lname");
			int bacidxToKill = partsData.optInt("idxToKill");
			
			
			
			System.out.println("\n\nfname : " + bacFName + "\nlname : "
					+ bacLName + "\nidxToKill: " + bacidxToKill  + "\n\n"    );
			

			EntityManager em = emfFac.devolveEmf().createEntityManager();

			TypedQuery<Usuario> usuarioExiste = em
					.createQuery(
							"SELECT u FROM Usuario u WHERE u.FName = ?1 and u.LName=?2",
							Usuario.class);
			// getSingleResult() blows when not found; getResultList() doesn't,
			// so lets use it.
			List<Usuario> usuariosComEsseNome = usuarioExiste
					.setParameter(1, bacFName).setParameter(2, bacLName)
					.getResultList();

			if (usuariosComEsseNome.isEmpty()) {
				System.out.println("vazio"); // bad vodoo. This user should have been found.
				http_code = 406; //
				MSG = "User does not exist. I can't kill an item if the user is not there.";
				jsonObject.put("HTTP_CODE", http_code);
				jsonObject.put("MSG", MSG);
				jsonArray.put(jsonObject);
				System.out.println(jsonArray.toString());
				// return Response.notModified(jsonArray.toString()).build();
				return Response.status(Responses.NOT_ACCEPTABLE)
						.entity(jsonArray.toString()).build();

			} else {
				System.out.println("Found the user - ready to delete item");
				usuario = usuariosComEsseNome.get(0);
			}
			
			
			//Checks if bacidxToKill makes sense
			if (usuario.getItems().size() < bacidxToKill || bacidxToKill < 0  ){
				http_code = 406; //
				MSG = "Nothing to remove! There is no item with that index.";
				jsonObject.put("HTTP_CODE", http_code);
				jsonObject.put("MSG", MSG);
				jsonArray.put(jsonObject);
				System.out.println(jsonArray.toString());
				// return Response.notModified(jsonArray.toString()).build();
				return Response.status(Responses.NOT_ACCEPTABLE)
						.entity(jsonArray.toString()).build();

			}
			
			
			// deletes item. Note that CascadeType is set to ALL, and orphanremoval=true, so the item row will be deleted as well in the item table 
			em.getTransaction().begin();
			usuario.getItems().remove(bacidxToKill);
			em.getTransaction().commit();
			em.close();

			http_code = 200;
			MSG = "Item deleted."; 												
			jsonObject.put("HTTP_CODE", http_code);
			jsonObject.put("MSG", MSG);
			
			
			jsonArray.put(jsonObject);
			
			returnString = jsonArray.toString();
			System.out.println("returnString: " + returnString);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500)
					.entity("Server was not able to process your request.")
					.build();
		}

		return Response.ok(returnString).build();
	} // fim do metodo del_item











}
