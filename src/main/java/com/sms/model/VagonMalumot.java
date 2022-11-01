package com.sms.model;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

import javax.persistence.*;

@Entity
@Table(name="vagon_malumot")
public class VagonMalumot {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private Integer nomer;
	
	private String depoNomi;

	private Date oxirgiTamirKuni;

	private String remontTuri;
	
	private Integer ishlabChiqarilganYili;

	//Kirishdagi(k)
	private Integer kramaOng1;
	private Integer kramaOng1Nomeri;

	private Integer kramaOng2;
	private Integer kramaOng2Nomeri;

	private Integer kramaChap1;
	private Integer kramaChap1Nomeri;

	private Integer kramaChap2;
	private Integer kramaChap2Nomeri;

	private Integer kbalka1;
	private Integer kbalka1Nomeri;

	private Integer kbalka2;
	private Integer kbalka2Nomeri;

	private Integer kgildirak1;
	private Integer kgildirak1Nomeri;

	private Integer kgildirak2;
	private Integer kgildirak2Nomeri;

	private Integer kgildirak3;
	private Integer kgildirak3Nomeri;

	private Integer kgildirak4;
	private Integer kgildirak4Nomeri;

	//Chiqishdagi
	private Integer ramaOng1;
	private Integer ramaOng1Nomeri;

	private Integer ramaOng2;
	private Integer ramaOng2Nomeri;

	private Integer ramaChap1;
	private Integer ramaChap1Nomeri;

	private Integer ramaChap2;
	private Integer ramaChap2Nomeri;

	private Integer balka1;
	private Integer balka1Nomeri;

	private Integer balka2;
	private Integer balka2Nomeri;

	private Integer gildirak1;
	private Integer gildirak1Nomeri;

	private Integer gildirak2;
	private Integer gildirak2Nomeri;

	private Integer gildirak3;
	private Integer gildirak3Nomeri;

	private Integer gildirak4;
	private Integer gildirak4Nomeri;

	private String saqlanganVaqti;

	@ColumnDefault("0")
	private String country;
	
	private String izoh;	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public Date getOxirgiTamirKuni() {
		return oxirgiTamirKuni;
	}

	public void setOxirgiTamirKuni(Date oxirgiTamirKuni) {
		this.oxirgiTamirKuni = oxirgiTamirKuni;
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

	public void setSaqlanganVaqti(String saqlanganVaqti) {
		this.saqlanganVaqti = saqlanganVaqti;
	}

	public String getSaqlanganVaqti() {
		return saqlanganVaqti;
	}

	public Integer getRamaOng1() {
		return ramaOng1;
	}

	public void setRamaOng1(Integer ramaOng1) {
		this.ramaOng1 = ramaOng1;
	}

	public Integer getRamaOng2() {
		return ramaOng2;
	}

	public void setRamaOng2(Integer ramaOng2) {
		this.ramaOng2 = ramaOng2;
	}

	public Integer getRamaChap1() {
		return ramaChap1;
	}

	public void setRamaChap1(Integer ramaChap1) {
		this.ramaChap1 = ramaChap1;
	}

	public Integer getRamaChap2() {
		return ramaChap2;
	}

	public void setRamaChap2(Integer ramaChap2) {
		this.ramaChap2 = ramaChap2;
	}

	public Integer getBalka1() {
		return balka1;
	}

	public void setBalka1(Integer balka1) {
		this.balka1 = balka1;
	}

	public Integer getBalka2() {
		return balka2;
	}

	public void setBalka2(Integer balka2) {
		this.balka2 = balka2;
	}

	public Integer getGildirak1() {
		return gildirak1;
	}

	public void setGildirak1(Integer gildirak1) {
		this.gildirak1 = gildirak1;
	}

	public Integer getGildirak2() {
		return gildirak2;
	}

	public void setGildirak2(Integer gildirak2) {
		this.gildirak2 = gildirak2;
	}

	public Integer getGildirak3() {
		return gildirak3;
	}

	public void setGildirak3(Integer gildirak3) {
		this.gildirak3 = gildirak3;
	}

	public Integer getGildirak4() {
		return gildirak4;
	}

	public void setGildirak4(Integer gildirak4) {
		this.gildirak4 = gildirak4;
	}

	public String getIzoh() {
		return izoh;
	}

	public void setIzoh(String izoh) {
		this.izoh = izoh;
	}

	public Integer getKramaOng1() {
		return kramaOng1;
	}

	public void setKramaOng1(Integer kramaOng1) {
		this.kramaOng1 = kramaOng1;
	}

	public Integer getKramaOng1Nomeri() {
		return kramaOng1Nomeri;
	}

	public void setKramaOng1Nomeri(Integer kramaOng1Nomeri) {
		this.kramaOng1Nomeri = kramaOng1Nomeri;
	}

	public Integer getKramaOng2() {
		return kramaOng2;
	}

	public void setKramaOng2(Integer kramaOng2) {
		this.kramaOng2 = kramaOng2;
	}

	public Integer getKramaOng2Nomeri() {
		return kramaOng2Nomeri;
	}

	public void setKramaOng2Nomeri(Integer kramaOng2Nomeri) {
		this.kramaOng2Nomeri = kramaOng2Nomeri;
	}

	public Integer getKramaChap1() {
		return kramaChap1;
	}

	public void setKramaChap1(Integer kramaChap1) {
		this.kramaChap1 = kramaChap1;
	}

	public Integer getKramaChap1Nomeri() {
		return kramaChap1Nomeri;
	}

	public void setKramaChap1Nomeri(Integer kramaChap1Nomeri) {
		this.kramaChap1Nomeri = kramaChap1Nomeri;
	}

	public Integer getKramaChap2() {
		return kramaChap2;
	}

	public void setKramaChap2(Integer kramaChap2) {
		this.kramaChap2 = kramaChap2;
	}

	public Integer getKramaChap2Nomeri() {
		return kramaChap2Nomeri;
	}

	public void setKramaChap2Nomeri(Integer kramaChap2Nomeri) {
		this.kramaChap2Nomeri = kramaChap2Nomeri;
	}

	public Integer getKbalka1() {
		return kbalka1;
	}

	public void setKbalka1(Integer kbalka1) {
		this.kbalka1 = kbalka1;
	}

	public Integer getKbalka1Nomeri() {
		return kbalka1Nomeri;
	}

	public void setKbalka1Nomeri(Integer kbalka1Nomeri) {
		this.kbalka1Nomeri = kbalka1Nomeri;
	}

	public Integer getKbalka2() {
		return kbalka2;
	}

	public void setKbalka2(Integer kbalka2) {
		this.kbalka2 = kbalka2;
	}

	public Integer getKbalka2Nomeri() {
		return kbalka2Nomeri;
	}

	public void setKbalka2Nomeri(Integer kbalka2Nomeri) {
		this.kbalka2Nomeri = kbalka2Nomeri;
	}

	public Integer getKgildirak1() {
		return kgildirak1;
	}

	public void setKgildirak1(Integer kgildirak1) {
		this.kgildirak1 = kgildirak1;
	}

	public Integer getKgildirak1Nomeri() {
		return kgildirak1Nomeri;
	}

	public void setKgildirak1Nomeri(Integer kgildirak1Nomeri) {
		this.kgildirak1Nomeri = kgildirak1Nomeri;
	}

	public Integer getKgildirak2() {
		return kgildirak2;
	}

	public void setKgildirak2(Integer kgildirak2) {
		this.kgildirak2 = kgildirak2;
	}

	public Integer getKgildirak2Nomeri() {
		return kgildirak2Nomeri;
	}

	public void setKgildirak2Nomeri(Integer kgildirak2Nomeri) {
		this.kgildirak2Nomeri = kgildirak2Nomeri;
	}

	public Integer getKgildirak3() {
		return kgildirak3;
	}

	public void setKgildirak3(Integer kgildirak3) {
		this.kgildirak3 = kgildirak3;
	}

	public Integer getKgildirak3Nomeri() {
		return kgildirak3Nomeri;
	}

	public void setKgildirak3Nomeri(Integer kgildirak3Nomeri) {
		this.kgildirak3Nomeri = kgildirak3Nomeri;
	}

	public Integer getKgildirak4() {
		return kgildirak4;
	}

	public void setKgildirak4(Integer kgildirak4) {
		this.kgildirak4 = kgildirak4;
	}

	public Integer getKgildirak4Nomeri() {
		return kgildirak4Nomeri;
	}

	public void setKgildirak4Nomeri(Integer kgildirak4Nomeri) {
		this.kgildirak4Nomeri = kgildirak4Nomeri;
	}

	public Integer getRamaOng1Nomeri() {
		return ramaOng1Nomeri;
	}

	public void setRamaOng1Nomeri(Integer ramaOng1Nomeri) {
		this.ramaOng1Nomeri = ramaOng1Nomeri;
	}

	public Integer getRamaOng2Nomeri() {
		return ramaOng2Nomeri;
	}

	public void setRamaOng2Nomeri(Integer ramaOng2Nomeri) {
		this.ramaOng2Nomeri = ramaOng2Nomeri;
	}

	public Integer getRamaChap1Nomeri() {
		return ramaChap1Nomeri;
	}

	public void setRamaChap1Nomeri(Integer ramaChap1Nomeri) {
		this.ramaChap1Nomeri = ramaChap1Nomeri;
	}

	public Integer getRamaChap2Nomeri() {
		return ramaChap2Nomeri;
	}

	public void setRamaChap2Nomeri(Integer ramaChap2Nomeri) {
		this.ramaChap2Nomeri = ramaChap2Nomeri;
	}

	public Integer getBalka1Nomeri() {
		return balka1Nomeri;
	}

	public void setBalka1Nomeri(Integer balka1Nomeri) {
		this.balka1Nomeri = balka1Nomeri;
	}

	public Integer getBalka2Nomeri() {
		return balka2Nomeri;
	}

	public void setBalka2Nomeri(Integer balka2Nomeri) {
		this.balka2Nomeri = balka2Nomeri;
	}

	public Integer getGildirak1Nomeri() {
		return gildirak1Nomeri;
	}

	public void setGildirak1Nomeri(Integer gildirak1Nomeri) {
		this.gildirak1Nomeri = gildirak1Nomeri;
	}

	public Integer getGildirak2Nomeri() {
		return gildirak2Nomeri;
	}

	public void setGildirak2Nomeri(Integer gildirak2Nomeri) {
		this.gildirak2Nomeri = gildirak2Nomeri;
	}

	public Integer getGildirak3Nomeri() {
		return gildirak3Nomeri;
	}

	public void setGildirak3Nomeri(Integer gildirak3Nomeri) {
		this.gildirak3Nomeri = gildirak3Nomeri;
	}

	public Integer getGildirak4Nomeri() {
		return gildirak4Nomeri;
	}

	public void setGildirak4Nomeri(Integer gildirak4Nomeri) {
		this.gildirak4Nomeri = gildirak4Nomeri;
	}
}
