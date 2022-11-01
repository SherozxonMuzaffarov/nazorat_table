package com.sms.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LastActionTimes {
    @Id
    private int id;

    String samQoldiqDate ;

    String havQoldiqDate ;

    String andjQoldiqDate ;

    String samMalumotDate ;

    String havMalumotDate ;

    String andjMalumotDate ;

    String samUtyDate ;

    String havUtyDate ;

    String andjUtyDate ;

    String samBiznesDate ;

    String havBiznesDate ;

    String andjBiznesDate ;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSamQoldiqDate() {
        return samQoldiqDate;
    }

    public void setSamQoldiqDate(String samQoldiqDate) {
        this.samQoldiqDate = samQoldiqDate;
    }

    public String getHavQoldiqDate() {
        return havQoldiqDate;
    }

    public void setHavQoldiqDate(String havQoldiqDate) {
        this.havQoldiqDate = havQoldiqDate;
    }

    public String getAndjQoldiqDate() {
        return andjQoldiqDate;
    }

    public void setAndjQoldiqDate(String andjQoldiqDate) {
        this.andjQoldiqDate = andjQoldiqDate;
    }

    public String getSamMalumotDate() {
        return samMalumotDate;
    }

    public void setSamMalumotDate(String samMalumotDate) {
        this.samMalumotDate = samMalumotDate;
    }

    public String getHavMalumotDate() {
        return havMalumotDate;
    }

    public void setHavMalumotDate(String havMalumotDate) {
        this.havMalumotDate = havMalumotDate;
    }

    public String getAndjMalumotDate() {
        return andjMalumotDate;
    }

    public void setAndjMalumotDate(String andjMalumotDate) {
        this.andjMalumotDate = andjMalumotDate;
    }

    public String getSamUtyDate() {
        return samUtyDate;
    }

    public void setSamUtyDate(String samUtyDate) {
        this.samUtyDate = samUtyDate;
    }

    public String getHavUtyDate() {
        return havUtyDate;
    }

    public void setHavUtyDate(String havUtyDate) {
        this.havUtyDate = havUtyDate;
    }

    public String getAndjUtyDate() {
        return andjUtyDate;
    }

    public void setAndjUtyDate(String andjUtyDate) {
        this.andjUtyDate = andjUtyDate;
    }

    public String getSamBiznesDate() {
        return samBiznesDate;
    }

    public void setSamBiznesDate(String samBiznesDate) {
        this.samBiznesDate = samBiznesDate;
    }

    public String getHavBiznesDate() {
        return havBiznesDate;
    }

    public void setHavBiznesDate(String havBiznesDate) {
        this.havBiznesDate = havBiznesDate;
    }

    public String getAndjBiznesDate() {
        return andjBiznesDate;
    }

    public void setAndjBiznesDate(String andjBiznesDate) {
        this.andjBiznesDate = andjBiznesDate;
    }
}
