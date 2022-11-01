package com.sms.model;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Table(name="metrologiya")
public class MetrologiyaModel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String nomi;
    private int soni;
    private int ishlabChiqarilganYili;
    private String raqami;
    private String turi;
    private String ishi;
    private String saqlanishJoyi;
    private String serRaqamiSanasi;
    private String serBerganKorxona;
    private String sarflanganMablag;
    private Date serKeyingiSanasi;
    private String serDavriyligi;
    private String shartnomaRaqamiSanasi;
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

    public String getSaqlanishJoyi() {
        return saqlanishJoyi;
    }

    public void setSaqlanishJoyi(String saqlanishJoyi) {
        this.saqlanishJoyi = saqlanishJoyi;
    }

    public String getSerRaqamiSanasi() {
        return serRaqamiSanasi;
    }

    public void setSerRaqamiSanasi(String serRaqamiSanasi) {
        this.serRaqamiSanasi = serRaqamiSanasi;
    }

    public String getSerBerganKorxona() {
        return serBerganKorxona;
    }

    public void setSerBerganKorxona(String serBerganKorxona) {
        this.serBerganKorxona = serBerganKorxona;
    }

    public String getSarflanganMablag() {
        return sarflanganMablag;
    }

    public void setSarflanganMablag(String sarflanganMablag) {
        this.sarflanganMablag = sarflanganMablag;
    }

    public Date getSerKeyingiSanasi() {
        return serKeyingiSanasi;
    }

    public void setSerKeyingiSanasi(Date serKeyingiSanasi) {
        this.serKeyingiSanasi = serKeyingiSanasi;
    }

    public String getSerDavriyligi() {
        return serDavriyligi;
    }

    public void setSerDavriyligi(String serDavriyligi) {
        this.serDavriyligi = serDavriyligi;
    }

    public String getShartnomaRaqamiSanasi() {
        return shartnomaRaqamiSanasi;
    }

    public void setShartnomaRaqamiSanasi(String shartnomaRaqamiSanasi) {
        this.shartnomaRaqamiSanasi = shartnomaRaqamiSanasi;
    }
}
