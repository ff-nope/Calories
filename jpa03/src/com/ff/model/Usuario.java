package com.ff.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.Calendar;
import java.util.List;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@Table(name = "usuario", schema = "calories")
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "idusuario", unique = true, nullable = false)
	private int idusuario;

	@Temporal(TemporalType.DATE)
	private Calendar dob;

	private String FName;

	private String LName;
	
	@OneToMany(mappedBy="usuario", cascade=CascadeType.ALL, fetch=FetchType.EAGER,orphanRemoval = true)
	@OrderBy("quando ASC")
	@JsonManagedReference
	private List<Item> items;
	
	
	
	
	

	public Usuario() {
	}

	public int getIdusuario() {
		return this.idusuario;
	}

	public void setIdusuario(int idusuario) {
		this.idusuario = idusuario;
	}

	public Calendar getDob() {
		return this.dob;
	}

	public void setDob(Calendar calStart) {
		this.dob = calStart;
	}

	public String getFName() {
		return this.FName;
	}

	public void setFName(String FName) {
		this.FName = FName;
	}

	public String getLName() {
		return this.LName;
	}

	public void setLName(String LName) {
		this.LName = LName;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	public String toString() {
        return "\nUSUARIO Id: " + getIdusuario() + " name: " + getFName() + " " + getLName()+ 
               " DOB: " + new java.sql.Date(getDob().getTimeInMillis()) + "\n Items: \n"  + getItems() + "\n"   ;
    }

	
	
	
}