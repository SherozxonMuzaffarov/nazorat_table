package com.sms.model;

import javax.persistence.*;

@Entity
@Table(name="metrologiya_sklad")
public class MetrologiyaSkladModel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String nomi;
    private int soni;
    private int ishlabChiqarilganYili;
    private String raqami;
    private String turi;
    private String ishi;
    private String izoh;
    private String depoNomi;


    public String getDepoNomi() {
        return depoNomi;
    }

    public void setDepoNomi(String depoNomi) {
        this.depoNomi = depoNomi;
    }

    public String getIzoh() {
        return izoh;
    }

    public void setIzoh(String izoh) {
        this.izoh = izoh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomi() {
        return nomi;
    }

    public void setNomi(String nomi) {
        this.nomi = nomi;
    }

    public int getSoni() {
        return soni;
    }

    public void setSoni(int soni) {
        this.soni = soni;
    }

    public int getIshlabChiqarilganYili() {
        return ishlabChiqarilganYili;
    }

    public void setIshlabChiqarilganYili(int ishlabChiqarilganYili) {
        this.ishlabChiqarilganYili = ishlabChiqarilganYili;
    }

    public String getRaqami() {
        return raqami;
    }

    public void setRaqami(String raqami) {
        this.raqami = raqami;
    }

    public String getTuri() {
        return turi;
    }

    public void setTuri(String turi) {
        this.turi = turi;
    }

    public String getIshi() {
        return ishi;
    }

    public void setIshi(String ishi) {
        this.ishi = ishi;
    }

}

