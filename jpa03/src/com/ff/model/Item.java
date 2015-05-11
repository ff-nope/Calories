package com.ff.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;




/**
 * The persistent class for the item database table.
 * 
 */

@Entity
@Table(name = "item", schema = "calories")
@NamedQuery(name="Item.findAll", query="SELECT i FROM Item i")
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	private int calorias;

	

	private String item;

	private Long quando;

	@ManyToOne()
	@JoinColumn(name="idusuario")
	@JsonBackReference
	private Usuario usuario;
	
	
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Item() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCalorias() {
		return this.calorias;
	}

	public void setCalorias(int calorias) {
		this.calorias = calorias;
	}

	public String getItem() {
		return this.item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Long getQuando() {
		return this.quando;
	}

	public void setQuando(Long quando) {
		this.quando = quando;
	}
	public String toString() {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		format.setTimeZone(TimeZone.getTimeZone("US/Central"));
		String formatted = format.format( new Date(getQuando() * 1000));
		
        return "   Item's Id: " + getId() + " -->item: " + getItem() +  
               " -->calorias: " + getCalorias() + "\n Quando: "  +formatted +  " Quando em millis: "  +getQuando() + "\n"   ;
    }


}