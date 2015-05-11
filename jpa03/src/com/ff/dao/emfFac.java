package com.ff.dao;


import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class emfFac  {
	
	private static EntityManagerFactory emf = null;
	
	
	
	public static EntityManagerFactory devolveEmf()
	{
	  emf = Persistence.createEntityManagerFactory("jpa03");
	  return emf;
	}
	
	
	public static void closer()
	{
		emf.close();
	}
	

}


