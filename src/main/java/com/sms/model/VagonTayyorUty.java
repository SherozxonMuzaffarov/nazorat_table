package com.sms.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="vagon_tayyorUty")
public class VagonTayyorUty {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private Integer nomer;

	private String depoNomi;

	private String remontTuri;
	
	private Integer ishlabChiqarilganYili;
	
	private Date chiqganVaqti;
	
	private String vagonTuri;

	private String izoh;
	
	private String country;

	private Boolean isActive;

	String createdDate ;

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setNomer(Integer nomer) {
		this.nomer = nomer;
	}

	public void setDepoNomi(String depoNomi) {
		this.depoNomi = depoNomi;
	}

	public void setRemontTuri(String remontTuri) {
		this.remontTuri = remontTuri;
	}

	public void setIshlabChiqarilganYili(Integer ishlabChiqarilganYili) {
		this.ishlabChiqarilganYili = ishlabChiqarilganYili;
	}

	public void setChiqganVaqti(Date chiqganVaqti) {
		this.chiqganVaqti = chiqganVaqti;
	}

	public void setVagonTuri(String vagonTuri) {
		this.vagonTuri = vagonTuri;
	}

	public void setIzoh(String izoh) {
		this.izoh = izoh;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setActive(Boolean active) {
		isActive = active;
	}

	public long getId() {
		return id;
	}

	public Integer getNomer() {
		return nomer;
	}

	public String getDepoNomi() {
		return depoNomi;
	}

	public String getRemontTuri() {
		return remontTuri;
	}

	public Integer getIshlabChiqarilganYili() {
		return ishlabChiqarilganYili;
	}

	public Date getChiqganVaqti() {
		return chiqganVaqti;
	}

	public String getVagonTuri() {
		return vagonTuri;
	}

	public String getIzoh() {
		return izoh;
	}

	public String getCountry() {
		return country;
	}

}
