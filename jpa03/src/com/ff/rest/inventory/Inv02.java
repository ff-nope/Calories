package com.ff.rest.inventory;

import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;

import com.ff.dao.emfFac;
import com.ff.model.Usuario;
import com.ff.model.printUsuarioJSON;

@Path("/Inv02")
public class Inv02 {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
    public String retornaInv() throws Throwable {
		
		EntityManager em = emfFac.devolveEmf().createEntityManager();
		String myRetorno = "\n\n";
		JSONArray jsonArray = new JSONArray();

		
		 List<Usuario> myList = em.createQuery( "from Usuario u", Usuario.class ).getResultList();
		  
		  for (Usuario b : myList){
			  
			  Usuario bPobre = b;
			  bPobre.setItems(null);
			  printUsuarioJSON p = new printUsuarioJSON();
//			  myRetorno += p.retornaJason(bPobre);
//			  myRetorno += "\n\n";
			  jsonArray.put(p.retornaJason(bPobre)); 
		  }
//		  return myRetorno;
		  return jsonArray.toString();
		
		
	}

		
}
