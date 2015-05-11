package com.ff.rest.inventory;

import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ff.dao.emfFac;
import com.ff.model.Usuario;

@Path("/Inv01")
public class Inv01 {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
    public String retornaInv() throws Throwable {
		
		EntityManager em = emfFac.devolveEmf().createEntityManager();
		String myRetorno = "";

		
		 List<Usuario> myList = em.createQuery( "from Usuario u", Usuario.class ).getResultList();
		  
		  for (Usuario b : myList){
			  System.out.println(b  );
			  myRetorno += "<BR>";
			  myRetorno += b;
			  myRetorno += "<BR><BR>";
		  }
		  return myRetorno;
		
		
	}

		
}
