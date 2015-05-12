package com.ff.model;



import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class printUsuarioJSON {
	ObjectMapper mapper = new ObjectMapper();
	Usuario meImprime = null;
	String jsonString = null;
	
	
	public  void doIt(Usuario foo){
		this.meImprime = foo;
		
		try {
			this.jsonString = mapper.writeValueAsString(meImprime);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}catch (Throwable e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println(jsonString);
		
		
	}
	public  String retornaJason(Usuario foo){
		this.meImprime = foo;
		
		try {
			this.jsonString = mapper.writeValueAsString(meImprime);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}catch (Throwable e) {
			e.printStackTrace();
			System.exit(0);
		}
		return jsonString;
		
		
	}

}
