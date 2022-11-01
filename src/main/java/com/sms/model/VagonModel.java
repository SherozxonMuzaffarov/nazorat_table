package com.sms.model;


import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;


import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name="vagons")
public class VagonModel {
	

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	private Integer nomer;

	private String depoNomi;

	private String remontTuri;
	
	private Integer ishlabChiqarilganYili;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date kelganVaqti;
	
	private String vagonTuri;

	private String izoh;
	
	private String country;

	String createdDate ;

	public VagonModel() {
		
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getNomer() {
		return nomer;
	}

	public void setNomer(Integer nomer) {
		this.nomer = nomer;
	}

	public String getDepoNomi() {
		return depoNomi;
	}

	public void setDepoNomi(String depoNomi) {
		this.depoNomi = depoNomi;
	}

	public String getRemontTuri() {
		return remontTuri;
	}

	public void setRemontTuri(String remontTuri) {
		this.remontTuri = remontTuri;
	}

	public Integer getIshlabChiqarilganYili() {
		return ishlabChiqarilganYili;
	}

	public void setIshlabChiqarilganYili(Integer ishlabChiqarilganYili) {
		this.ishlabChiqarilganYili = ishlabChiqarilganYili;
	}

	public String getVagonTuri() {
		return vagonTuri;
	}

	public void setVagonTuri(String vagonTuri) {
		this.vagonTuri = vagonTuri;
	}

	public String getIzoh() {
		return izoh;
	}

	public void setIzoh(String izoh) {
		this.izoh = izoh;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getKelganVaqti() {
		return kelganVaqti;
	}

	public void setKelganVaqti(Date kelganVaqti) {
		this.kelganVaqti = kelganVaqti;
	}

	
}
