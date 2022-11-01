package com.sms.model;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PlanBiznes {
    @Id
    private int id;

    private int samDtKritiPlanBiznes;
    private int samDtPlatformaPlanBiznes;
    private int samDtPoluvagonPlanBiznes;
    private int samDtSisternaPlanBiznes;
    private int samDtBoshqaPlanBiznes;

    private int havDtKritiPlanBiznes;
    private int havDtPlatformaPlanBiznes;
    private int havDtPoluvagonPlanBiznes;
    private int havDtSisternaPlanBiznes;
    private int havDtBoshqaPlanBiznes;

    private int andjDtKritiPlanBiznes;
    private int andjDtPlatformaPlanBiznes;
    private int andjDtPoluvagonPlanBiznes;
    private int andjDtSisternaPlanBiznes;
    private int andjDtBoshqaPlanBiznes;

//    @Column(columnDefinition = "int default 0")
    @ColumnDefault("0")
    private int andjDtYolovchiPlanBiznes;
//    @Column(columnDefinition = "int default 0")
    @ColumnDefault("0")
    private int andjTYolovchiPlanBiznes;


    private int samKtKritiPlanBiznes;
    private int samKtPlatformaPlanBiznes;
    private int samKtPoluvagonPlanBiznes;
    private int samKtSisternaPlanBiznes;
    private int samKtBoshqaPlanBiznes;

    private int havKtKritiPlanBiznes;
    private int havKtPlatformaPlanBiznes;
    private int havKtPoluvagonPlanBiznes;
    private int havKtSisternaPlanBiznes;
    private int havKtBoshqaPlanBiznes;

    private int andjKtKritiPlanBiznes;
    private int andjKtPlatformaPlanBiznes;
    private int andjKtPoluvagonPlanBiznes;
    private int andjKtSisternaPlanBiznes;
    private int andjKtBoshqaPlanBiznes;

    private int samKrpKritiPlanBiznes;
    private int samKrpPlatformaPlanBiznes;
    private int samKrpPoluvagonPlanBiznes;
    private int samKrpSisternaPlanBiznes;
    private int samKrpBoshqaPlanBiznes;

    private int havKrpKritiPlanBiznes;
    private int havKrpPlatformaPlanBiznes;
    private int havKrpPoluvagonPlanBiznes;
    private int havKrpSisternaPlanBiznes;
    private int havKrpBoshqaPlanBiznes;

    private int andjKrpKritiPlanBiznes;
    private int andjKrpPlatformaPlanBiznes;
    private int andjKrpPoluvagonPlanBiznes;
    private int andjKrpSisternaPlanBiznes;
    private int andjKrpBoshqaPlanBiznes;





    //*******************



    private int samDtKritiPlanBiznesMonths;
    private int samDtPlatformaPlanBiznesMonths;
    private int samDtPoluvagonPlanBiznesMonths;
    private int samDtSisternaPlanBiznesMonths;
    private int samDtBoshqaPlanBiznesMonths;

    private int havDtKritiPlanBiznesMonths;
    private int havDtPlatformaPlanBiznesMonths;
    private int havDtPoluvagonPlanBiznesMonths;
    private int havDtSisternaPlanBiznesMonths;
    private int havDtBoshqaPlanBiznesMonths;

    private int andjDtKritiPlanBiznesMonths;
    private int andjDtPlatformaPlanBiznesMonths;
    private int andjDtPoluvagonPlanBiznesMonths;
    private int andjDtSisternaPlanBiznesMonths;
    private int andjDtBoshqaPlanBiznesMonths;

    @ColumnDefault("0")
    private int andjDtYolovchiPlanBiznesMonths;
    @ColumnDefault("0")
    private int andjTYolovchiPlanBiznesMonths;

    private int samKtKritiPlanBiznesMonths;
    private int samKtPlatformaPlanBiznesMonths;
    private int samKtPoluvagonPlanBiznesMonths;
    private int samKtSisternaPlanBiznesMonths;
    private int samKtBoshqaPlanBiznesMonths;

    private int havKtKritiPlanBiznesMonths;
    private int havKtPlatformaPlanBiznesMonths;
    private int havKtPoluvagonPlanBiznesMonths;
    private int havKtSisternaPlanBiznesMonths;
    private int havKtBoshqaPlanBiznesMonths;

    private int andjKtKritiPlanBiznesMonths;
    private int andjKtPlatformaPlanBiznesMonths;
    private int andjKtPoluvagonPlanBiznesMonths;
    private int andjKtSisternaPlanBiznesMonths;
    private int andjKtBoshqaPlanBiznesMonths;

    private int samKrpKritiPlanBiznesMonths;
    private int samKrpPlatformaPlanBiznesMonths;
    private int samKrpPoluvagonPlanBiznesMonths;
    private int samKrpSisternaPlanBiznesMonths;
    private int samKrpBoshqaPlanBiznesMonths;

    private int havKrpKritiPlanBiznesMonths;
    private int havKrpPlatformaPlanBiznesMonths;
    private int havKrpPoluvagonPlanBiznesMonths;
    private int havKrpSisternaPlanBiznesMonths;
    private int havKrpBoshqaPlanBiznesMonths;

    private int andjKrpKritiPlanBiznesMonths;
    private int andjKrpPlatformaPlanBiznesMonths;
    private int andjKrpPoluvagonPlanBiznesMonths;
    private int andjKrpSisternaPlanBiznesMonths;
    private int andjKrpBoshqaPlanBiznesMonths;


    public int getId() {
        return id;
    }

    public int getSamDtKritiPlanBiznes() {
        return samDtKritiPlanBiznes;
    }

    public int getSamDtPlatformaPlanBiznes() {
        return samDtPlatformaPlanBiznes;
    }

    public int getSamDtPoluvagonPlanBiznes() {
        return samDtPoluvagonPlanBiznes;
    }

    public int getSamDtSisternaPlanBiznes() {
        return samDtSisternaPlanBiznes;
    }

    public int getSamDtBoshqaPlanBiznes() {
        return samDtBoshqaPlanBiznes;
    }

    public int getHavDtKritiPlanBiznes() {
        return havDtKritiPlanBiznes;
    }

    public int getHavDtPlatformaPlanBiznes() {
        return havDtPlatformaPlanBiznes;
    }

    public int getHavDtPoluvagonPlanBiznes() {
        return havDtPoluvagonPlanBiznes;
    }

    public int getHavDtSisternaPlanBiznes() {
        return havDtSisternaPlanBiznes;
    }

    public int getHavDtBoshqaPlanBiznes() {
        return havDtBoshqaPlanBiznes;
    }

    public int getAndjDtKritiPlanBiznes() {
        return andjDtKritiPlanBiznes;
    }

    public int getAndjDtPlatformaPlanBiznes() {
        return andjDtPlatformaPlanBiznes;
    }

    public int getAndjDtPoluvagonPlanBiznes() {
        return andjDtPoluvagonPlanBiznes;
    }

    public int getAndjDtSisternaPlanBiznes() {
        return andjDtSisternaPlanBiznes;
    }

    public int getAndjDtBoshqaPlanBiznes() {
        return andjDtBoshqaPlanBiznes;
    }

    public int getSamKtKritiPlanBiznes() {
        return samKtKritiPlanBiznes;
    }

    public int getSamKtPlatformaPlanBiznes() {
        return samKtPlatformaPlanBiznes;
    }

    public int getSamKtPoluvagonPlanBiznes() {
        return samKtPoluvagonPlanBiznes;
    }

    public int getSamKtSisternaPlanBiznes() {
        return samKtSisternaPlanBiznes;
    }

    public int getSamKtBoshqaPlanBiznes() {
        return samKtBoshqaPlanBiznes;
    }

    public int getHavKtKritiPlanBiznes() {
        return havKtKritiPlanBiznes;
    }

    public int getHavKtPlatformaPlanBiznes() {
        return havKtPlatformaPlanBiznes;
    }

    public int getHavKtPoluvagonPlanBiznes() {
        return havKtPoluvagonPlanBiznes;
    }

    public int getHavKtSisternaPlanBiznes() {
        return havKtSisternaPlanBiznes;
    }

    public int getHavKtBoshqaPlanBiznes() {
        return havKtBoshqaPlanBiznes;
    }

    public int getAndjKtKritiPlanBiznes() {
        return andjKtKritiPlanBiznes;
    }

    public int getAndjKtPlatformaPlanBiznes() {
        return andjKtPlatformaPlanBiznes;
    }

    public int getAndjKtPoluvagonPlanBiznes() {
        return andjKtPoluvagonPlanBiznes;
    }

    public int getAndjKtSisternaPlanBiznes() {
        return andjKtSisternaPlanBiznes;
    }

    public int getAndjKtBoshqaPlanBiznes() {
        return andjKtBoshqaPlanBiznes;
    }

    public int getSamKrpKritiPlanBiznes() {
        return samKrpKritiPlanBiznes;
    }

    public int getSamKrpPlatformaPlanBiznes() {
        return samKrpPlatformaPlanBiznes;
    }

    public int getSamKrpPoluvagonPlanBiznes() {
        return samKrpPoluvagonPlanBiznes;
    }

    public int getSamKrpSisternaPlanBiznes() {
        return samKrpSisternaPlanBiznes;
    }

    public int getSamKrpBoshqaPlanBiznes() {
        return samKrpBoshqaPlanBiznes;
    }

    public int getHavKrpKritiPlanBiznes() {
        return havKrpKritiPlanBiznes;
    }

    public int getHavKrpPlatformaPlanBiznes() {
        return havKrpPlatformaPlanBiznes;
    }

    public int getHavKrpPoluvagonPlanBiznes() {
        return havKrpPoluvagonPlanBiznes;
    }

    public int getHavKrpSisternaPlanBiznes() {
        return havKrpSisternaPlanBiznes;
    }

    public int getHavKrpBoshqaPlanBiznes() {
        return havKrpBoshqaPlanBiznes;
    }

    public int getAndjKrpKritiPlanBiznes() {
        return andjKrpKritiPlanBiznes;
    }

    public int getAndjKrpPlatformaPlanBiznes() {
        return andjKrpPlatformaPlanBiznes;
    }

    public int getAndjKrpPoluvagonPlanBiznes() {
        return andjKrpPoluvagonPlanBiznes;
    }

    public int getAndjKrpSisternaPlanBiznes() {
        return andjKrpSisternaPlanBiznes;
    }

    public int getAndjKrpBoshqaPlanBiznes() {
        return andjKrpBoshqaPlanBiznes;
    }

    public int getSamDtKritiPlanBiznesMonths() {
        return samDtKritiPlanBiznesMonths;
    }

    public int getSamDtPlatformaPlanBiznesMonths() {
        return samDtPlatformaPlanBiznesMonths;
    }

    public int getSamDtPoluvagonPlanBiznesMonths() {
        return samDtPoluvagonPlanBiznesMonths;
    }

    public int getSamDtSisternaPlanBiznesMonths() {
        return samDtSisternaPlanBiznesMonths;
    }

    public int getSamDtBoshqaPlanBiznesMonths() {
        return samDtBoshqaPlanBiznesMonths;
    }

    public int getHavDtKritiPlanBiznesMonths() {
        return havDtKritiPlanBiznesMonths;
    }

    public int getHavDtPlatformaPlanBiznesMonths() {
        return havDtPlatformaPlanBiznesMonths;
    }

    public int getHavDtPoluvagonPlanBiznesMonths() {
        return havDtPoluvagonPlanBiznesMonths;
    }

    public int getHavDtSisternaPlanBiznesMonths() {
        return havDtSisternaPlanBiznesMonths;
    }

    public int getHavDtBoshqaPlanBiznesMonths() {
        return havDtBoshqaPlanBiznesMonths;
    }

    public int getAndjDtKritiPlanBiznesMonths() {
        return andjDtKritiPlanBiznesMonths;
    }

    public int getAndjDtPlatformaPlanBiznesMonths() {
        return andjDtPlatformaPlanBiznesMonths;
    }

    public int getAndjDtPoluvagonPlanBiznesMonths() {
        return andjDtPoluvagonPlanBiznesMonths;
    }

    public int getAndjDtSisternaPlanBiznesMonths() {
        return andjDtSisternaPlanBiznesMonths;
    }

    public int getAndjDtBoshqaPlanBiznesMonths() {
        return andjDtBoshqaPlanBiznesMonths;
    }

    public int getSamKtKritiPlanBiznesMonths() {
        return samKtKritiPlanBiznesMonths;
    }

    public int getSamKtPlatformaPlanBiznesMonths() {
        return samKtPlatformaPlanBiznesMonths;
    }

    public int getSamKtPoluvagonPlanBiznesMonths() {
        return samKtPoluvagonPlanBiznesMonths;
    }

    public int getSamKtSisternaPlanBiznesMonths() {
        return samKtSisternaPlanBiznesMonths;
    }

    public int getSamKtBoshqaPlanBiznesMonths() {
        return samKtBoshqaPlanBiznesMonths;
    }

    public int getHavKtKritiPlanBiznesMonths() {
        return havKtKritiPlanBiznesMonths;
    }

    public int getHavKtPlatformaPlanBiznesMonths() {
        return havKtPlatformaPlanBiznesMonths;
    }

    public int getHavKtPoluvagonPlanBiznesMonths() {
        return havKtPoluvagonPlanBiznesMonths;
    }

    public int getHavKtSisternaPlanBiznesMonths() {
        return havKtSisternaPlanBiznesMonths;
    }

    public int getHavKtBoshqaPlanBiznesMonths() {
        return havKtBoshqaPlanBiznesMonths;
    }

    public int getAndjKtKritiPlanBiznesMonths() {
        return andjKtKritiPlanBiznesMonths;
    }

    public int getAndjKtPlatformaPlanBiznesMonths() {
        return andjKtPlatformaPlanBiznesMonths;
    }

    public int getAndjKtPoluvagonPlanBiznesMonths() {
        return andjKtPoluvagonPlanBiznesMonths;
    }

    public int getAndjKtSisternaPlanBiznesMonths() {
        return andjKtSisternaPlanBiznesMonths;
    }

    public int getAndjKtBoshqaPlanBiznesMonths() {
        return andjKtBoshqaPlanBiznesMonths;
    }

    public int getSamKrpKritiPlanBiznesMonths() {
        return samKrpKritiPlanBiznesMonths;
    }

    public int getSamKrpPlatformaPlanBiznesMonths() {
        return samKrpPlatformaPlanBiznesMonths;
    }

    public int getSamKrpPoluvagonPlanBiznesMonths() {
        return samKrpPoluvagonPlanBiznesMonths;
    }

    public int getSamKrpSisternaPlanBiznesMonths() {
        return samKrpSisternaPlanBiznesMonths;
    }

    public int getSamKrpBoshqaPlanBiznesMonths() {
        return samKrpBoshqaPlanBiznesMonths;
    }

    public int getHavKrpKritiPlanBiznesMonths() {
        return havKrpKritiPlanBiznesMonths;
    }

    public int getHavKrpPlatformaPlanBiznesMonths() {
        return havKrpPlatformaPlanBiznesMonths;
    }

    public int getHavKrpPoluvagonPlanBiznesMonths() {
        return havKrpPoluvagonPlanBiznesMonths;
    }

    public int getHavKrpSisternaPlanBiznesMonths() {
        return havKrpSisternaPlanBiznesMonths;
    }

    public int getHavKrpBoshqaPlanBiznesMonths() {
        return havKrpBoshqaPlanBiznesMonths;
    }

    public int getAndjKrpKritiPlanBiznesMonths() {
        return andjKrpKritiPlanBiznesMonths;
    }

    public int getAndjKrpPlatformaPlanBiznesMonths() {
        return andjKrpPlatformaPlanBiznesMonths;
    }

    public int getAndjKrpPoluvagonPlanBiznesMonths() {
        return andjKrpPoluvagonPlanBiznesMonths;
    }

    public int getAndjKrpSisternaPlanBiznesMonths() {
        return andjKrpSisternaPlanBiznesMonths;
    }

    public int getAndjKrpBoshqaPlanBiznesMonths() {
        return andjKrpBoshqaPlanBiznesMonths;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSamDtKritiPlanBiznes(int samDtKritiPlanBiznes) {
        this.samDtKritiPlanBiznes = samDtKritiPlanBiznes;
    }

    public void setSamDtPlatformaPlanBiznes(int samDtPlatformaPlanBiznes) {
        this.samDtPlatformaPlanBiznes = samDtPlatformaPlanBiznes;
    }

    public void setSamDtPoluvagonPlanBiznes(int samDtPoluvagonPlanBiznes) {
        this.samDtPoluvagonPlanBiznes = samDtPoluvagonPlanBiznes;
    }

    public void setSamDtSisternaPlanBiznes(int samDtSisternaPlanBiznes) {
        this.samDtSisternaPlanBiznes = samDtSisternaPlanBiznes;
    }

    public void setSamDtBoshqaPlanBiznes(int samDtBoshqaPlanBiznes) {
        this.samDtBoshqaPlanBiznes = samDtBoshqaPlanBiznes;
    }

    public void setHavDtKritiPlanBiznes(int havDtKritiPlanBiznes) {
        this.havDtKritiPlanBiznes = havDtKritiPlanBiznes;
    }

    public void setHavDtPlatformaPlanBiznes(int havDtPlatformaPlanBiznes) {
        this.havDtPlatformaPlanBiznes = havDtPlatformaPlanBiznes;
    }

    public void setHavDtPoluvagonPlanBiznes(int havDtPoluvagonPlanBiznes) {
        this.havDtPoluvagonPlanBiznes = havDtPoluvagonPlanBiznes;
    }

    public void setHavDtSisternaPlanBiznes(int havDtSisternaPlanBiznes) {
        this.havDtSisternaPlanBiznes = havDtSisternaPlanBiznes;
    }

    public void setHavDtBoshqaPlanBiznes(int havDtBoshqaPlanBiznes) {
        this.havDtBoshqaPlanBiznes = havDtBoshqaPlanBiznes;
    }

    public void setAndjDtKritiPlanBiznes(int andjDtKritiPlanBiznes) {
        this.andjDtKritiPlanBiznes = andjDtKritiPlanBiznes;
    }

    public void setAndjDtPlatformaPlanBiznes(int andjDtPlatformaPlanBiznes) {
        this.andjDtPlatformaPlanBiznes = andjDtPlatformaPlanBiznes;
    }

    public void setAndjDtPoluvagonPlanBiznes(int andjDtPoluvagonPlanBiznes) {
        this.andjDtPoluvagonPlanBiznes = andjDtPoluvagonPlanBiznes;
    }

    public void setAndjDtSisternaPlanBiznes(int andjDtSisternaPlanBiznes) {
        this.andjDtSisternaPlanBiznes = andjDtSisternaPlanBiznes;
    }

    public void setAndjDtBoshqaPlanBiznes(int andjDtBoshqaPlanBiznes) {
        this.andjDtBoshqaPlanBiznes = andjDtBoshqaPlanBiznes;
    }

    public void setSamKtKritiPlanBiznes(int samKtKritiPlanBiznes) {
        this.samKtKritiPlanBiznes = samKtKritiPlanBiznes;
    }

    public void setSamKtPlatformaPlanBiznes(int samKtPlatformaPlanBiznes) {
        this.samKtPlatformaPlanBiznes = samKtPlatformaPlanBiznes;
    }

    public void setSamKtPoluvagonPlanBiznes(int samKtPoluvagonPlanBiznes) {
        this.samKtPoluvagonPlanBiznes = samKtPoluvagonPlanBiznes;
    }

    public void setSamKtSisternaPlanBiznes(int samKtSisternaPlanBiznes) {
        this.samKtSisternaPlanBiznes = samKtSisternaPlanBiznes;
    }

    public void setSamKtBoshqaPlanBiznes(int samKtBoshqaPlanBiznes) {
        this.samKtBoshqaPlanBiznes = samKtBoshqaPlanBiznes;
    }

    public void setHavKtKritiPlanBiznes(int havKtKritiPlanBiznes) {
        this.havKtKritiPlanBiznes = havKtKritiPlanBiznes;
    }

    public void setHavKtPlatformaPlanBiznes(int havKtPlatformaPlanBiznes) {
        this.havKtPlatformaPlanBiznes = havKtPlatformaPlanBiznes;
    }

    public void setHavKtPoluvagonPlanBiznes(int havKtPoluvagonPlanBiznes) {
        this.havKtPoluvagonPlanBiznes = havKtPoluvagonPlanBiznes;
    }

    public void setHavKtSisternaPlanBiznes(int havKtSisternaPlanBiznes) {
        this.havKtSisternaPlanBiznes = havKtSisternaPlanBiznes;
    }

    public void setHavKtBoshqaPlanBiznes(int havKtBoshqaPlanBiznes) {
        this.havKtBoshqaPlanBiznes = havKtBoshqaPlanBiznes;
    }

    public void setAndjKtKritiPlanBiznes(int andjKtKritiPlanBiznes) {
        this.andjKtKritiPlanBiznes = andjKtKritiPlanBiznes;
    }

    public void setAndjKtPlatformaPlanBiznes(int andjKtPlatformaPlanBiznes) {
        this.andjKtPlatformaPlanBiznes = andjKtPlatformaPlanBiznes;
    }

    public void setAndjKtPoluvagonPlanBiznes(int andjKtPoluvagonPlanBiznes) {
        this.andjKtPoluvagonPlanBiznes = andjKtPoluvagonPlanBiznes;
    }

    public void setAndjKtSisternaPlanBiznes(int andjKtSisternaPlanBiznes) {
        this.andjKtSisternaPlanBiznes = andjKtSisternaPlanBiznes;
    }

    public void setAndjKtBoshqaPlanBiznes(int andjKtBoshqaPlanBiznes) {
        this.andjKtBoshqaPlanBiznes = andjKtBoshqaPlanBiznes;
    }

    public void setSamKrpKritiPlanBiznes(int samKrpKritiPlanBiznes) {
        this.samKrpKritiPlanBiznes = samKrpKritiPlanBiznes;
    }

    public void setSamKrpPlatformaPlanBiznes(int samKrpPlatformaPlanBiznes) {
        this.samKrpPlatformaPlanBiznes = samKrpPlatformaPlanBiznes;
    }

    public void setSamKrpPoluvagonPlanBiznes(int samKrpPoluvagonPlanBiznes) {
        this.samKrpPoluvagonPlanBiznes = samKrpPoluvagonPlanBiznes;
    }

    public void setSamKrpSisternaPlanBiznes(int samKrpSisternaPlanBiznes) {
        this.samKrpSisternaPlanBiznes = samKrpSisternaPlanBiznes;
    }

    public void setSamKrpBoshqaPlanBiznes(int samKrpBoshqaPlanBiznes) {
        this.samKrpBoshqaPlanBiznes = samKrpBoshqaPlanBiznes;
    }

    public void setHavKrpKritiPlanBiznes(int havKrpKritiPlanBiznes) {
        this.havKrpKritiPlanBiznes = havKrpKritiPlanBiznes;
    }

    public void setHavKrpPlatformaPlanBiznes(int havKrpPlatformaPlanBiznes) {
        this.havKrpPlatformaPlanBiznes = havKrpPlatformaPlanBiznes;
    }

    public void setHavKrpPoluvagonPlanBiznes(int havKrpPoluvagonPlanBiznes) {
        this.havKrpPoluvagonPlanBiznes = havKrpPoluvagonPlanBiznes;
    }

    public void setHavKrpSisternaPlanBiznes(int havKrpSisternaPlanBiznes) {
        this.havKrpSisternaPlanBiznes = havKrpSisternaPlanBiznes;
    }

    public void setHavKrpBoshqaPlanBiznes(int havKrpBoshqaPlanBiznes) {
        this.havKrpBoshqaPlanBiznes = havKrpBoshqaPlanBiznes;
    }

    public void setAndjKrpKritiPlanBiznes(int andjKrpKritiPlanBiznes) {
        this.andjKrpKritiPlanBiznes = andjKrpKritiPlanBiznes;
    }

    public void setAndjKrpPlatformaPlanBiznes(int andjKrpPlatformaPlanBiznes) {
        this.andjKrpPlatformaPlanBiznes = andjKrpPlatformaPlanBiznes;
    }

    public void setAndjKrpPoluvagonPlanBiznes(int andjKrpPoluvagonPlanBiznes) {
        this.andjKrpPoluvagonPlanBiznes = andjKrpPoluvagonPlanBiznes;
    }

    public void setAndjKrpSisternaPlanBiznes(int andjKrpSisternaPlanBiznes) {
        this.andjKrpSisternaPlanBiznes = andjKrpSisternaPlanBiznes;
    }

    public void setAndjKrpBoshqaPlanBiznes(int andjKrpBoshqaPlanBiznes) {
        this.andjKrpBoshqaPlanBiznes = andjKrpBoshqaPlanBiznes;
    }

    public void setSamDtKritiPlanBiznesMonths(int samDtKritiPlanBiznesMonths) {
        this.samDtKritiPlanBiznesMonths = samDtKritiPlanBiznesMonths;
    }

    public void setSamDtPlatformaPlanBiznesMonths(int samDtPlatformaPlanBiznesMonths) {
        this.samDtPlatformaPlanBiznesMonths = samDtPlatformaPlanBiznesMonths;
    }

    public void setSamDtPoluvagonPlanBiznesMonths(int samDtPoluvagonPlanBiznesMonths) {
        this.samDtPoluvagonPlanBiznesMonths = samDtPoluvagonPlanBiznesMonths;
    }

    public void setSamDtSisternaPlanBiznesMonths(int samDtSisternaPlanBiznesMonths) {
        this.samDtSisternaPlanBiznesMonths = samDtSisternaPlanBiznesMonths;
    }

    public void setSamDtBoshqaPlanBiznesMonths(int samDtBoshqaPlanBiznesMonths) {
        this.samDtBoshqaPlanBiznesMonths = samDtBoshqaPlanBiznesMonths;
    }

    public void setHavDtKritiPlanBiznesMonths(int havDtKritiPlanBiznesMonths) {
        this.havDtKritiPlanBiznesMonths = havDtKritiPlanBiznesMonths;
    }

    public void setHavDtPlatformaPlanBiznesMonths(int havDtPlatformaPlanBiznesMonths) {
        this.havDtPlatformaPlanBiznesMonths = havDtPlatformaPlanBiznesMonths;
    }

    public void setHavDtPoluvagonPlanBiznesMonths(int havDtPoluvagonPlanBiznesMonths) {
        this.havDtPoluvagonPlanBiznesMonths = havDtPoluvagonPlanBiznesMonths;
    }

    public void setHavDtSisternaPlanBiznesMonths(int havDtSisternaPlanBiznesMonths) {
        this.havDtSisternaPlanBiznesMonths = havDtSisternaPlanBiznesMonths;
    }

    public void setHavDtBoshqaPlanBiznesMonths(int havDtBoshqaPlanBiznesMonths) {
        this.havDtBoshqaPlanBiznesMonths = havDtBoshqaPlanBiznesMonths;
    }

    public void setAndjDtKritiPlanBiznesMonths(int andjDtKritiPlanBiznesMonths) {
        this.andjDtKritiPlanBiznesMonths = andjDtKritiPlanBiznesMonths;
    }

    public void setAndjDtPlatformaPlanBiznesMonths(int andjDtPlatformaPlanBiznesMonths) {
        this.andjDtPlatformaPlanBiznesMonths = andjDtPlatformaPlanBiznesMonths;
    }

    public void setAndjDtPoluvagonPlanBiznesMonths(int andjDtPoluvagonPlanBiznesMonths) {
        this.andjDtPoluvagonPlanBiznesMonths = andjDtPoluvagonPlanBiznesMonths;
    }

    public void setAndjDtSisternaPlanBiznesMonths(int andjDtSisternaPlanBiznesMonths) {
        this.andjDtSisternaPlanBiznesMonths = andjDtSisternaPlanBiznesMonths;
    }

    public void setAndjDtBoshqaPlanBiznesMonths(int andjDtBoshqaPlanBiznesMonths) {
        this.andjDtBoshqaPlanBiznesMonths = andjDtBoshqaPlanBiznesMonths;
    }

    public void setSamKtKritiPlanBiznesMonths(int samKtKritiPlanBiznesMonths) {
        this.samKtKritiPlanBiznesMonths = samKtKritiPlanBiznesMonths;
    }

    public void setSamKtPlatformaPlanBiznesMonths(int samKtPlatformaPlanBiznesMonths) {
        this.samKtPlatformaPlanBiznesMonths = samKtPlatformaPlanBiznesMonths;
    }

    public void setSamKtPoluvagonPlanBiznesMonths(int samKtPoluvagonPlanBiznesMonths) {
        this.samKtPoluvagonPlanBiznesMonths = samKtPoluvagonPlanBiznesMonths;
    }

    public void setSamKtSisternaPlanBiznesMonths(int samKtSisternaPlanBiznesMonths) {
        this.samKtSisternaPlanBiznesMonths = samKtSisternaPlanBiznesMonths;
    }

    public void setSamKtBoshqaPlanBiznesMonths(int samKtBoshqaPlanBiznesMonths) {
        this.samKtBoshqaPlanBiznesMonths = samKtBoshqaPlanBiznesMonths;
    }

    public void setHavKtKritiPlanBiznesMonths(int havKtKritiPlanBiznesMonths) {
        this.havKtKritiPlanBiznesMonths = havKtKritiPlanBiznesMonths;
    }

    public void setHavKtPlatformaPlanBiznesMonths(int havKtPlatformaPlanBiznesMonths) {
        this.havKtPlatformaPlanBiznesMonths = havKtPlatformaPlanBiznesMonths;
    }

    public void setHavKtPoluvagonPlanBiznesMonths(int havKtPoluvagonPlanBiznesMonths) {
        this.havKtPoluvagonPlanBiznesMonths = havKtPoluvagonPlanBiznesMonths;
    }

    public void setHavKtSisternaPlanBiznesMonths(int havKtSisternaPlanBiznesMonths) {
        this.havKtSisternaPlanBiznesMonths = havKtSisternaPlanBiznesMonths;
    }

    public void setHavKtBoshqaPlanBiznesMonths(int havKtBoshqaPlanBiznesMonths) {
        this.havKtBoshqaPlanBiznesMonths = havKtBoshqaPlanBiznesMonths;
    }

    public void setAndjKtKritiPlanBiznesMonths(int andjKtKritiPlanBiznesMonths) {
        this.andjKtKritiPlanBiznesMonths = andjKtKritiPlanBiznesMonths;
    }

    public void setAndjKtPlatformaPlanBiznesMonths(int andjKtPlatformaPlanBiznesMonths) {
        this.andjKtPlatformaPlanBiznesMonths = andjKtPlatformaPlanBiznesMonths;
    }

    public void setAndjKtPoluvagonPlanBiznesMonths(int andjKtPoluvagonPlanBiznesMonths) {
        this.andjKtPoluvagonPlanBiznesMonths = andjKtPoluvagonPlanBiznesMonths;
    }

    public void setAndjKtSisternaPlanBiznesMonths(int andjKtSisternaPlanBiznesMonths) {
        this.andjKtSisternaPlanBiznesMonths = andjKtSisternaPlanBiznesMonths;
    }

    public void setAndjKtBoshqaPlanBiznesMonths(int andjKtBoshqaPlanBiznesMonths) {
        this.andjKtBoshqaPlanBiznesMonths = andjKtBoshqaPlanBiznesMonths;
    }

    public void setSamKrpKritiPlanBiznesMonths(int samKrpKritiPlanBiznesMonths) {
        this.samKrpKritiPlanBiznesMonths = samKrpKritiPlanBiznesMonths;
    }

    public void setSamKrpPlatformaPlanBiznesMonths(int samKrpPlatformaPlanBiznesMonths) {
        this.samKrpPlatformaPlanBiznesMonths = samKrpPlatformaPlanBiznesMonths;
    }

    public void setSamKrpPoluvagonPlanBiznesMonths(int samKrpPoluvagonPlanBiznesMonths) {
        this.samKrpPoluvagonPlanBiznesMonths = samKrpPoluvagonPlanBiznesMonths;
    }

    public void setSamKrpSisternaPlanBiznesMonths(int samKrpSisternaPlanBiznesMonths) {
        this.samKrpSisternaPlanBiznesMonths = samKrpSisternaPlanBiznesMonths;
    }

    public void setSamKrpBoshqaPlanBiznesMonths(int samKrpBoshqaPlanBiznesMonths) {
        this.samKrpBoshqaPlanBiznesMonths = samKrpBoshqaPlanBiznesMonths;
    }

    public void setHavKrpKritiPlanBiznesMonths(int havKrpKritiPlanBiznesMonths) {
        this.havKrpKritiPlanBiznesMonths = havKrpKritiPlanBiznesMonths;
    }

    public void setHavKrpPlatformaPlanBiznesMonths(int havKrpPlatformaPlanBiznesMonths) {
        this.havKrpPlatformaPlanBiznesMonths = havKrpPlatformaPlanBiznesMonths;
    }

    public void setHavKrpPoluvagonPlanBiznesMonths(int havKrpPoluvagonPlanBiznesMonths) {
        this.havKrpPoluvagonPlanBiznesMonths = havKrpPoluvagonPlanBiznesMonths;
    }

    public void setHavKrpSisternaPlanBiznesMonths(int havKrpSisternaPlanBiznesMonths) {
        this.havKrpSisternaPlanBiznesMonths = havKrpSisternaPlanBiznesMonths;
    }

    public void setHavKrpBoshqaPlanBiznesMonths(int havKrpBoshqaPlanBiznesMonths) {
        this.havKrpBoshqaPlanBiznesMonths = havKrpBoshqaPlanBiznesMonths;
    }

    public void setAndjKrpKritiPlanBiznesMonths(int andjKrpKritiPlanBiznesMonths) {
        this.andjKrpKritiPlanBiznesMonths = andjKrpKritiPlanBiznesMonths;
    }

    public void setAndjKrpPlatformaPlanBiznesMonths(int andjKrpPlatformaPlanBiznesMonths) {
        this.andjKrpPlatformaPlanBiznesMonths = andjKrpPlatformaPlanBiznesMonths;
    }

    public void setAndjKrpPoluvagonPlanBiznesMonths(int andjKrpPoluvagonPlanBiznesMonths) {
        this.andjKrpPoluvagonPlanBiznesMonths = andjKrpPoluvagonPlanBiznesMonths;
    }

    public void setAndjKrpSisternaPlanBiznesMonths(int andjKrpSisternaPlanBiznesMonths) {
        this.andjKrpSisternaPlanBiznesMonths = andjKrpSisternaPlanBiznesMonths;
    }

    public void setAndjKrpBoshqaPlanBiznesMonths(int andjKrpBoshqaPlanBiznesMonths) {
        this.andjKrpBoshqaPlanBiznesMonths = andjKrpBoshqaPlanBiznesMonths;
    }

    public int getAndjDtYolovchiPlanBiznes() {
        return andjDtYolovchiPlanBiznes;
    }

    public void setAndjDtYolovchiPlanBiznes(int andjDtYolovchiPlanBiznes) {
        this.andjDtYolovchiPlanBiznes = andjDtYolovchiPlanBiznes;
    }

    public int getAndjDtYolovchiPlanBiznesMonths() {
        return andjDtYolovchiPlanBiznesMonths;
    }

    public void setAndjDtYolovchiPlanBiznesMonths(int andjDtYolovchiPlanBiznesMonths) {
        this.andjDtYolovchiPlanBiznesMonths = andjDtYolovchiPlanBiznesMonths;
    }

    public int getAndjTYolovchiPlanBiznes() {
        return andjTYolovchiPlanBiznes;
    }

    public void setAndjTYolovchiPlanBiznes(int andjTYolovchiPlanBiznes) {
        this.andjTYolovchiPlanBiznes = andjTYolovchiPlanBiznes;
    }

    public int getAndjTYolovchiPlanBiznesMonths() {
        return andjTYolovchiPlanBiznesMonths;
    }

    public void setAndjTYolovchiPlanBiznesMonths(int andjTYolovchiPlanBiznesMonths) {
        this.andjTYolovchiPlanBiznesMonths = andjTYolovchiPlanBiznesMonths;
    }
}
