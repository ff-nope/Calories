package com.ff.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.ff.dao.emfFac;


public class Bac02 {
	
    public static void main( String[] args ) throws Throwable {
		
		EntityManager em = emfFac.devolveEmf().createEntityManager();

		
		Usuario usuario = new Usuario();
		Item item = new Item();
		
		List<Item> tempItem = new ArrayList<Item>();
		
		
		String bacFName = "Fernando";
		String bacLName = "Feijo";
		
		TypedQuery<Usuario> usuarioExiste = em.createQuery("SELECT u FROM Usuario u WHERE u.FName = ?1 and u.LName=?2",Usuario.class);
		List<Usuario> usuariosComEsseNome =   usuarioExiste.setParameter(1, bacFName).setParameter(2, bacLName).getResultList();


		
		if (usuariosComEsseNome.isEmpty()){
			System.out.println("vazio");                           	// vou criar o registro
			usuario.setFName(bacFName);
			usuario.setLName(bacLName);
																	//DOB
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			Calendar calStart = Calendar.getInstance();
			calStart.setTime(formatter.parse("1994/4/5"));
			usuario.setDob(calStart);

		}
		else
			{
			System.out.println("tem gente");
			usuario = usuariosComEsseNome.get(0);
			}

		
		System.out.println(usuariosComEsseNome);
		//System.exit(0);
		
		
		//item
		item.setItem("Torta Apricot");
		item.setCalorias(1000);
		item.setQuando(System.currentTimeMillis()/1000);

		

																//pega uma transaction, grava usuario& item
		em.getTransaction().begin();
		em.persist(usuario);  									// gravei usuario 
		em.persist(item);										// gravei item
		
		item.setUsuario(usuario);								// gravei o usuario no item
		tempItem = usuario.getItems();
		if (tempItem == null){
			tempItem = new ArrayList<Item>();
		}
		tempItem.add(item);
		usuario.setItems(tempItem);								// gravei o item no usuario
		
		em.getTransaction().commit();
		System.out.println(">>>>>>>>>>>>>>>>>>                       acabou de gravar.        <<<<<<<<<<<<<<<<<");
		
		
		
		 List<Usuario> myList = em.createQuery( "from Usuario u", Usuario.class ).getResultList();
		  
		  for (Usuario b : myList){
//			  System.out.println(b  );
			  Usuario bPobre = b;
			  bPobre.setItems(null);
			  printUsuarioJSON p = new printUsuarioJSON();
			  p.doIt(bPobre);
//			  System.exit(0);
		  }
		  System.out.println( "--->>>  \n \n");
		  
		  
//		  TypedQuery<Item> queryD = em.createQuery("SELECT  department FROM Item department",Item.class);
//		  
//		  List<Item> myListD = queryD.getResultList();
//		  for (Item b : myListD){
//			  System.out.println(b + " ID: " + b.getId()  );
//			  System.out.println("Name -> " + b.getName() );
//			  System.out.println("Usuarios : " +b.getUsuarios() + "\n ");
//		  }
//		  
		  em.close();
		  emfFac.closer();  
		
		
		
		
		
		

	}

		
}
