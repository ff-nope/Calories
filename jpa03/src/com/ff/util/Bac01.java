package com.ff.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;

import com.ff.dao.emfFac;
import com.ff.model.Item;
import com.ff.model.Usuario;


public class Bac01 {
	
    public static void main( String[] args ) throws Throwable {
		
		EntityManager em = emfFac.devolveEmf().createEntityManager();

		
		Usuario usuario = new Usuario();
		Item item = new Item();
		
		List<Item> tempItem = new ArrayList<Item>();
		
		usuario.setFName("Bobo");
		usuario.setLName("Feijo");
																//DOB
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(formatter.parse("1964/6/24"));
		usuario.setDob(calStart);
																//item
		item.setItem("Leite condensado");
		item.setCalorias(2350);
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
		System.out.println("acabou de gravar.");
		
		
		
		 List<Usuario> myList = em.createQuery( "from Usuario u", Usuario.class ).getResultList();
		  
		  for (Usuario b : myList){
			  System.out.println(b  );
		  }
		  System.out.println( "--->>>  \n \n");
		  
		  
		  em.close();
		  emfFac.closer();  
		
		
		
		
		
		

	}

		
}
