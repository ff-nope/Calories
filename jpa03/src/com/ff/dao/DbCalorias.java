package com.ff.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import com.ff.model.Item;
import com.ff.model.Usuario;

public class DbCalorias {
	
	
	
	public List<Usuario> fetch_list_by_name(String fname, String lname, EntityManager em){
		TypedQuery<Usuario> usuarioExiste = em.createQuery(
				"SELECT u FROM Usuario u WHERE u.FName = ?1 and u.LName=?2",
				Usuario.class);															// getSingleResult() blows when not found; getResultList() doesn't,
		List<Usuario> usuariosComEsseNome = usuarioExiste								// so lets use it.
				.setParameter(1, fname).setParameter(2, lname).getResultList();
		return usuariosComEsseNome;
	}
	
	
	

	public Usuario fetch_user_by_name(String fname, String lname) {
		Usuario usuario = new Usuario();
		usuario = null;
		

		EntityManager em = emfFac.devolveEmf().createEntityManager();
		List<Usuario> usuariosComEsseNome = fetch_list_by_name( fname,  lname,  em);

		if (!usuariosComEsseNome.isEmpty()) {
			usuario = usuariosComEsseNome.get(0);
		}
		return usuario;
	} // end fetch_user_by_name
	

	public int rec_user_by_name(String fname, String lname, Calendar calStart) {
		int http_code = 0;
		Usuario usuario = new Usuario();

		EntityManager em = emfFac.devolveEmf().createEntityManager();

		// Last chance for DB integrity. If I am recording this user, then
		// it should not exist in the database, so we verify.
		List<Usuario> usuariosComEsseNome = fetch_list_by_name( fname,  lname,  em);

		if (usuariosComEsseNome.isEmpty()) {
			http_code = 201; //
		} else {
			http_code = 406;
			return http_code;
		}
																			// Let's persist the user, shall we?
		usuario.setFName(fname);
		usuario.setLName(lname);
		usuario.setDob(calStart);
		em.getTransaction().begin();
		em.persist(usuario); 												// Persisted here.
		em.getTransaction().commit();
		em.close();
		return http_code;
	} 																		// end rec_user_by_name

	
	
	
	public int rec_item_by_user_name(String fname, String lname, String memitem, int calorias, long quando) {
		int http_code = 0;
		Usuario usuario = new Usuario();
		Item item = new Item();
		List<Item> tempItem = new ArrayList<Item>();
																				// record REST data item in entity
		item.setItem(memitem);
		item.setCalorias(calorias);
		item.setQuando(quando);

		EntityManager em = emfFac.devolveEmf().createEntityManager();
																				// Last chance for DB integrity. If I am recording this item, then
																				// the corresponding user should exist in the database, so we
																				// verify.
		List<Usuario> usuariosComEsseNome = fetch_list_by_name( fname,  lname,  em);
		if (!usuariosComEsseNome.isEmpty()) {
			usuario = usuariosComEsseNome.get(0); 								 
			http_code = 201; 
		} else {
			http_code = 406;
			return http_code;
		}
																				// ready to record item. We do that, then establish ManyToOne
																				// association with usuario.
		em.getTransaction().begin();
		em.persist(usuario); 													// usuario to DB
		em.persist(item); 														// item to DB

		item.setUsuario(usuario); 												// here we persist user in item, so far without the items list inside the user 
		tempItem = usuario.getItems();
		if (tempItem == null) {
			tempItem = new ArrayList<Item>();
		}
		tempItem.add(item);
		usuario.setItems(tempItem); 											// persisted item list in user

		em.getTransaction().commit();
		em.close();
		
		return http_code;
	} 																			// end rec_item_by_user_name


	public int del_user_by_name(String fname, String lname) {
		int http_code = 0;
		Usuario usuario = new Usuario();

		EntityManager em = emfFac.devolveEmf().createEntityManager();

		List<Usuario> usuariosComEsseNome = fetch_list_by_name( fname,  lname,  em);
		if (usuariosComEsseNome.isEmpty()) {									
			System.out.println("user not found, we're blown"); 							// bad vodoo. This user should have been found.
			http_code = 500; //
			return http_code;
		} 
		System.out.println("Found the user - ready to delete it");
		usuario = usuariosComEsseNome.get(0);
																				// deletes user. Note that CascadeType is set to ALL,  
		em.getTransaction().begin();											// so the items will be deleted as well.
		em.remove(usuario);
		em.getTransaction().commit();
		em.close();
		http_code = 200;
		return http_code;
	} 																			// del_user_by_name
	
	
	

	public int del_item_by_idx(String fname, String lname, int idxToKill) {
		int http_code = 0;
		Usuario usuario = new Usuario();
		EntityManager em = emfFac.devolveEmf().createEntityManager();
		List<Usuario> usuariosComEsseNome = fetch_list_by_name( fname,  lname,  em);
		if (usuariosComEsseNome.isEmpty()) {									
			http_code = 500; //
			return http_code;
		}
		usuario = usuariosComEsseNome.get(0);
		if (usuario.getItems().size() <= idxToKill || idxToKill < 0  ){			//Checks if bacidxToKill makes sense
			http_code = 406; //
			return http_code;
		}
		System.out.println("Found the user - ready to delete item");
																				// deletes item. Note that CascadeType is set to ALL, 
																				// and orphanremoval=true, so the item row will be deleted  
		em.getTransaction().begin();											// in both user and item tables.
		usuario.getItems().remove(idxToKill);
		em.getTransaction().commit();
		em.close();
		http_code = 200;
		return http_code;
	} 																			// del_user_by_name
	
	
	
	
}// end class DbCalorias

