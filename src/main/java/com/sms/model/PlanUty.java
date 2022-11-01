package com.sms.model;

import javax.persistence.*;

@Entity
public class PlanUty {

	@Id
	private int id;

	private int samDtKritiPlanUty;
	private int samDtPlatformaPlanUty;
	private int samDtPoluvagonPlanUty;
	private int samDtSisternaPlanUty;
	private int samDtBoshqaPlanUty;
	
	private int havDtKritiPlanUty;
	private int havDtPlatformaPlanUty;
	private int havDtPoluvagonPlanUty;
	private int havDtSisternaPlanUty;
	private int havDtBoshqaPlanUty;
	
	private int andjDtKritiPlanUty;
	private int andjDtPlatformaPlanUty;
	private int andjDtPoluvagonPlanUty;
	private int andjDtSisternaPlanUty;
	private int andjDtBoshqaPlanUty;
	
	private int samKtKritiPlanUty;
	private int samKtPlatformaPlanUty;
	private int samKtPoluvagonPlanUty;
	private int samKtSisternaPlanUty;
	private int samKtBoshqaPlanUty;
	
	private int havKtKritiPlanUty;
	private int havKtPlatformaPlanUty;
	private int havKtPoluvagonPlanUty;
	private int havKtSisternaPlanUty;
	private int havKtBoshqaPlanUty;
	
	private int andjKtKritiPlanUty;
	private int andjKtPlatformaPlanUty;
	private int andjKtPoluvagonPlanUty;
	private int andjKtSisternaPlanUty;
	private int andjKtBoshqaPlanUty;
	
	private int samKrpKritiPlanUty;
	private int samKrpPlatformaPlanUty;
	private int samKrpPoluvagonPlanUty;
	private int samKrpSisternaPlanUty;
	private int samKrpBoshqaPlanUty;
	
	private int havKrpKritiPlanUty;
	private int havKrpPlatformaPlanUty;
	private int havKrpPoluvagonPlanUty;
	private int havKrpSisternaPlanUty;
	private int havKrpBoshqaPlanUty;
	
	private int andjKrpKritiPlanUty;
	private int andjKrpPlatformaPlanUty;
	private int andjKrpPoluvagonPlanUty;
	private int andjKrpSisternaPlanUty;
	private int andjKrpBoshqaPlanUty;





	//*******************



	private int samDtKritiPlanUtyMonths;
	private int samDtPlatformaPlanUtyMonths;
	private int samDtPoluvagonPlanUtyMonths;
	private int samDtSisternaPlanUtyMonths;
	private int samDtBoshqaPlanUtyMonths;

	private int havDtKritiPlanUtyMonths;
	private int havDtPlatformaPlanUtyMonths;
	private int havDtPoluvagonPlanUtyMonths;
	private int havDtSisternaPlanUtyMonths;
	private int havDtBoshqaPlanUtyMonths;

	private int andjDtKritiPlanUtyMonths;
	private int andjDtPlatformaPlanUtyMonths;
	private int andjDtPoluvagonPlanUtyMonths;
	private int andjDtSisternaPlanUtyMonths;
	private int andjDtBoshqaPlanUtyMonths;

	private int samKtKritiPlanUtyMonths;
	private int samKtPlatformaPlanUtyMonths;
	private int samKtPoluvagonPlanUtyMonths;
	private int samKtSisternaPlanUtyMonths;
	private int samKtBoshqaPlanUtyMonths;

	private int havKtKritiPlanUtyMonths;
	private int havKtPlatformaPlanUtyMonths;
	private int havKtPoluvagonPlanUtyMonths;
	private int havKtSisternaPlanUtyMonths;
	private int havKtBoshqaPlanUtyMonths;

	private int andjKtKritiPlanUtyMonths;
	private int andjKtPlatformaPlanUtyMonths;
	private int andjKtPoluvagonPlanUtyMonths;
	private int andjKtSisternaPlanUtyMonths;
	private int andjKtBoshqaPlanUtyMonths;

	private int samKrpKritiPlanUtyMonths;
	private int samKrpPlatformaPlanUtyMonths;
	private int samKrpPoluvagonPlanUtyMonths;
	private int samKrpSisternaPlanUtyMonths;
	private int samKrpBoshqaPlanUtyMonths;

	private int havKrpKritiPlanUtyMonths;
	private int havKrpPlatformaPlanUtyMonths;
	private int havKrpPoluvagonPlanUtyMonths;
	private int havKrpSisternaPlanUtyMonths;
	private int havKrpBoshqaPlanUtyMonths;

	private int andjKrpKritiPlanUtyMonths;
	private int andjKrpPlatformaPlanUtyMonths;
	private int andjKrpPoluvagonPlanUtyMonths;
	private int andjKrpSisternaPlanUtyMonths;
	private int andjKrpBoshqaPlanUtyMonths;


	public int getId() {
		return id;
	}

	public int getSamDtKritiPlanUty() {
		return samDtKritiPlanUty;
	}

	public int getSamDtPlatformaPlanUty() {
		return samDtPlatformaPlanUty;
	}

	public int getSamDtPoluvagonPlanUty() {
		return samDtPoluvagonPlanUty;
	}

	public int getSamDtSisternaPlanUty() {
		return samDtSisternaPlanUty;
	}

	public int getSamDtBoshqaPlanUty() {
		return samDtBoshqaPlanUty;
	}

	public int getHavDtKritiPlanUty() {
		return havDtKritiPlanUty;
	}

	public int getHavDtPlatformaPlanUty() {
		return havDtPlatformaPlanUty;
	}

	public int getHavDtPoluvagonPlanUty() {
		return havDtPoluvagonPlanUty;
	}

	public int getHavDtSisternaPlanUty() {
		return havDtSisternaPlanUty;
	}

	public int getHavDtBoshqaPlanUty() {
		return havDtBoshqaPlanUty;
	}

	public int getAndjDtKritiPlanUty() {
		return andjDtKritiPlanUty;
	}

	public int getAndjDtPlatformaPlanUty() {
		return andjDtPlatformaPlanUty;
	}

	public int getAndjDtPoluvagonPlanUty() {
		return andjDtPoluvagonPlanUty;
	}

	public int getAndjDtSisternaPlanUty() {
		return andjDtSisternaPlanUty;
	}

	public int getAndjDtBoshqaPlanUty() {
		return andjDtBoshqaPlanUty;
	}

	public int getSamKtKritiPlanUty() {
		return samKtKritiPlanUty;
	}

	public int getSamKtPlatformaPlanUty() {
		return samKtPlatformaPlanUty;
	}

	public int getSamKtPoluvagonPlanUty() {
		return samKtPoluvagonPlanUty;
	}

	public int getSamKtSisternaPlanUty() {
		return samKtSisternaPlanUty;
	}

	public int getSamKtBoshqaPlanUty() {
		return samKtBoshqaPlanUty;
	}

	public int getHavKtKritiPlanUty() {
		return havKtKritiPlanUty;
	}

	public int getHavKtPlatformaPlanUty() {
		return havKtPlatformaPlanUty;
	}

	public int getHavKtPoluvagonPlanUty() {
		return havKtPoluvagonPlanUty;
	}

	public int getHavKtSisternaPlanUty() {
		return havKtSisternaPlanUty;
	}

	public int getHavKtBoshqaPlanUty() {
		return havKtBoshqaPlanUty;
	}

	public int getAndjKtKritiPlanUty() {
		return andjKtKritiPlanUty;
	}

	public int getAndjKtPlatformaPlanUty() {
		return andjKtPlatformaPlanUty;
	}

	public int getAndjKtPoluvagonPlanUty() {
		return andjKtPoluvagonPlanUty;
	}

	public int getAndjKtSisternaPlanUty() {
		return andjKtSisternaPlanUty;
	}

	public int getAndjKtBoshqaPlanUty() {
		return andjKtBoshqaPlanUty;
	}

	public int getSamKrpKritiPlanUty() {
		return samKrpKritiPlanUty;
	}

	public int getSamKrpPlatformaPlanUty() {
		return samKrpPlatformaPlanUty;
	}

	public int getSamKrpPoluvagonPlanUty() {
		return samKrpPoluvagonPlanUty;
	}

	public int getSamKrpSisternaPlanUty() {
		return samKrpSisternaPlanUty;
	}

	public int getSamKrpBoshqaPlanUty() {
		return samKrpBoshqaPlanUty;
	}

	public int getHavKrpKritiPlanUty() {
		return havKrpKritiPlanUty;
	}

	public int getHavKrpPlatformaPlanUty() {
		return havKrpPlatformaPlanUty;
	}

	public int getHavKrpPoluvagonPlanUty() {
		return havKrpPoluvagonPlanUty;
	}

	public int getHavKrpSisternaPlanUty() {
		return havKrpSisternaPlanUty;
	}

	public int getHavKrpBoshqaPlanUty() {
		return havKrpBoshqaPlanUty;
	}

	public int getAndjKrpKritiPlanUty() {
		return andjKrpKritiPlanUty;
	}

	public int getAndjKrpPlatformaPlanUty() {
		return andjKrpPlatformaPlanUty;
	}

	public int getAndjKrpPoluvagonPlanUty() {
		return andjKrpPoluvagonPlanUty;
	}

	public int getAndjKrpSisternaPlanUty() {
		return andjKrpSisternaPlanUty;
	}

	public int getAndjKrpBoshqaPlanUty() {
		return andjKrpBoshqaPlanUty;
	}

	public int getSamDtKritiPlanUtyMonths() {
		return samDtKritiPlanUtyMonths;
	}

	public int getSamDtPlatformaPlanUtyMonths() {
		return samDtPlatformaPlanUtyMonths;
	}

	public int getSamDtPoluvagonPlanUtyMonths() {
		return samDtPoluvagonPlanUtyMonths;
	}

	public int getSamDtSisternaPlanUtyMonths() {
		return samDtSisternaPlanUtyMonths;
	}

	public int getSamDtBoshqaPlanUtyMonths() {
		return samDtBoshqaPlanUtyMonths;
	}

	public int getHavDtKritiPlanUtyMonths() {
		return havDtKritiPlanUtyMonths;
	}

	public int getHavDtPlatformaPlanUtyMonths() {
		return havDtPlatformaPlanUtyMonths;
	}

	public int getHavDtPoluvagonPlanUtyMonths() {
		return havDtPoluvagonPlanUtyMonths;
	}

	public int getHavDtSisternaPlanUtyMonths() {
		return havDtSisternaPlanUtyMonths;
	}

	public int getHavDtBoshqaPlanUtyMonths() {
		return havDtBoshqaPlanUtyMonths;
	}

	public int getAndjDtKritiPlanUtyMonths() {
		return andjDtKritiPlanUtyMonths;
	}

	public int getAndjDtPlatformaPlanUtyMonths() {
		return andjDtPlatformaPlanUtyMonths;
	}

	public int getAndjDtPoluvagonPlanUtyMonths() {
		return andjDtPoluvagonPlanUtyMonths;
	}

	public int getAndjDtSisternaPlanUtyMonths() {
		return andjDtSisternaPlanUtyMonths;
	}

	public int getAndjDtBoshqaPlanUtyMonths() {
		return andjDtBoshqaPlanUtyMonths;
	}

	public int getSamKtKritiPlanUtyMonths() {
		return samKtKritiPlanUtyMonths;
	}

	public int getSamKtPlatformaPlanUtyMonths() {
		return samKtPlatformaPlanUtyMonths;
	}

	public int getSamKtPoluvagonPlanUtyMonths() {
		return samKtPoluvagonPlanUtyMonths;
	}

	public int getSamKtSisternaPlanUtyMonths() {
		return samKtSisternaPlanUtyMonths;
	}

	public int getSamKtBoshqaPlanUtyMonths() {
		return samKtBoshqaPlanUtyMonths;
	}

	public int getHavKtKritiPlanUtyMonths() {
		return havKtKritiPlanUtyMonths;
	}

	public int getHavKtPlatformaPlanUtyMonths() {
		return havKtPlatformaPlanUtyMonths;
	}

	public int getHavKtPoluvagonPlanUtyMonths() {
		return havKtPoluvagonPlanUtyMonths;
	}

	public int getHavKtSisternaPlanUtyMonths() {
		return havKtSisternaPlanUtyMonths;
	}

	public int getHavKtBoshqaPlanUtyMonths() {
		return havKtBoshqaPlanUtyMonths;
	}

	public int getAndjKtKritiPlanUtyMonths() {
		return andjKtKritiPlanUtyMonths;
	}

	public int getAndjKtPlatformaPlanUtyMonths() {
		return andjKtPlatformaPlanUtyMonths;
	}

	public int getAndjKtPoluvagonPlanUtyMonths() {
		return andjKtPoluvagonPlanUtyMonths;
	}

	public int getAndjKtSisternaPlanUtyMonths() {
		return andjKtSisternaPlanUtyMonths;
	}

	public int getAndjKtBoshqaPlanUtyMonths() {
		return andjKtBoshqaPlanUtyMonths;
	}

	public int getSamKrpKritiPlanUtyMonths() {
		return samKrpKritiPlanUtyMonths;
	}

	public int getSamKrpPlatformaPlanUtyMonths() {
		return samKrpPlatformaPlanUtyMonths;
	}

	public int getSamKrpPoluvagonPlanUtyMonths() {
		return samKrpPoluvagonPlanUtyMonths;
	}

	public int getSamKrpSisternaPlanUtyMonths() {
		return samKrpSisternaPlanUtyMonths;
	}

	public int getSamKrpBoshqaPlanUtyMonths() {
		return samKrpBoshqaPlanUtyMonths;
	}

	public int getHavKrpKritiPlanUtyMonths() {
		return havKrpKritiPlanUtyMonths;
	}

	public int getHavKrpPlatformaPlanUtyMonths() {
		return havKrpPlatformaPlanUtyMonths;
	}

	public int getHavKrpPoluvagonPlanUtyMonths() {
		return havKrpPoluvagonPlanUtyMonths;
	}

	public int getHavKrpSisternaPlanUtyMonths() {
		return havKrpSisternaPlanUtyMonths;
	}

	public int getHavKrpBoshqaPlanUtyMonths() {
		return havKrpBoshqaPlanUtyMonths;
	}

	public int getAndjKrpKritiPlanUtyMonths() {
		return andjKrpKritiPlanUtyMonths;
	}

	public int getAndjKrpPlatformaPlanUtyMonths() {
		return andjKrpPlatformaPlanUtyMonths;
	}

	public int getAndjKrpPoluvagonPlanUtyMonths() {
		return andjKrpPoluvagonPlanUtyMonths;
	}

	public int getAndjKrpSisternaPlanUtyMonths() {
		return andjKrpSisternaPlanUtyMonths;
	}

	public int getAndjKrpBoshqaPlanUtyMonths() {
		return andjKrpBoshqaPlanUtyMonths;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSamDtKritiPlanUty(int samDtKritiPlanUty) {
		this.samDtKritiPlanUty = samDtKritiPlanUty;
	}

	public void setSamDtPlatformaPlanUty(int samDtPlatformaPlanUty) {
		this.samDtPlatformaPlanUty = samDtPlatformaPlanUty;
	}

	public void setSamDtPoluvagonPlanUty(int samDtPoluvagonPlanUty) {
		this.samDtPoluvagonPlanUty = samDtPoluvagonPlanUty;
	}

	public void setSamDtSisternaPlanUty(int samDtSisternaPlanUty) {
		this.samDtSisternaPlanUty = samDtSisternaPlanUty;
	}

	public void setSamDtBoshqaPlanUty(int samDtBoshqaPlanUty) {
		this.samDtBoshqaPlanUty = samDtBoshqaPlanUty;
	}

	public void setHavDtKritiPlanUty(int havDtKritiPlanUty) {
		this.havDtKritiPlanUty = havDtKritiPlanUty;
	}

	public void setHavDtPlatformaPlanUty(int havDtPlatformaPlanUty) {
		this.havDtPlatformaPlanUty = havDtPlatformaPlanUty;
	}

	public void setHavDtPoluvagonPlanUty(int havDtPoluvagonPlanUty) {
		this.havDtPoluvagonPlanUty = havDtPoluvagonPlanUty;
	}

	public void setHavDtSisternaPlanUty(int havDtSisternaPlanUty) {
		this.havDtSisternaPlanUty = havDtSisternaPlanUty;
	}

	public void setHavDtBoshqaPlanUty(int havDtBoshqaPlanUty) {
		this.havDtBoshqaPlanUty = havDtBoshqaPlanUty;
	}

	public void setAndjDtKritiPlanUty(int andjDtKritiPlanUty) {
		this.andjDtKritiPlanUty = andjDtKritiPlanUty;
	}

	public void setAndjDtPlatformaPlanUty(int andjDtPlatformaPlanUty) {
		this.andjDtPlatformaPlanUty = andjDtPlatformaPlanUty;
	}

	public void setAndjDtPoluvagonPlanUty(int andjDtPoluvagonPlanUty) {
		this.andjDtPoluvagonPlanUty = andjDtPoluvagonPlanUty;
	}

	public void setAndjDtSisternaPlanUty(int andjDtSisternaPlanUty) {
		this.andjDtSisternaPlanUty = andjDtSisternaPlanUty;
	}

	public void setAndjDtBoshqaPlanUty(int andjDtBoshqaPlanUty) {
		this.andjDtBoshqaPlanUty = andjDtBoshqaPlanUty;
	}

	public void setSamKtKritiPlanUty(int samKtKritiPlanUty) {
		this.samKtKritiPlanUty = samKtKritiPlanUty;
	}

	public void setSamKtPlatformaPlanUty(int samKtPlatformaPlanUty) {
		this.samKtPlatformaPlanUty = samKtPlatformaPlanUty;
	}

	public void setSamKtPoluvagonPlanUty(int samKtPoluvagonPlanUty) {
		this.samKtPoluvagonPlanUty = samKtPoluvagonPlanUty;
	}

	public void setSamKtSisternaPlanUty(int samKtSisternaPlanUty) {
		this.samKtSisternaPlanUty = samKtSisternaPlanUty;
	}

	public void setSamKtBoshqaPlanUty(int samKtBoshqaPlanUty) {
		this.samKtBoshqaPlanUty = samKtBoshqaPlanUty;
	}

	public void setHavKtKritiPlanUty(int havKtKritiPlanUty) {
		this.havKtKritiPlanUty = havKtKritiPlanUty;
	}

	public void setHavKtPlatformaPlanUty(int havKtPlatformaPlanUty) {
		this.havKtPlatformaPlanUty = havKtPlatformaPlanUty;
	}

	public void setHavKtPoluvagonPlanUty(int havKtPoluvagonPlanUty) {
		this.havKtPoluvagonPlanUty = havKtPoluvagonPlanUty;
	}

	public void setHavKtSisternaPlanUty(int havKtSisternaPlanUty) {
		this.havKtSisternaPlanUty = havKtSisternaPlanUty;
	}

	public void setHavKtBoshqaPlanUty(int havKtBoshqaPlanUty) {
		this.havKtBoshqaPlanUty = havKtBoshqaPlanUty;
	}

	public void setAndjKtKritiPlanUty(int andjKtKritiPlanUty) {
		this.andjKtKritiPlanUty = andjKtKritiPlanUty;
	}

	public void setAndjKtPlatformaPlanUty(int andjKtPlatformaPlanUty) {
		this.andjKtPlatformaPlanUty = andjKtPlatformaPlanUty;
	}

	public void setAndjKtPoluvagonPlanUty(int andjKtPoluvagonPlanUty) {
		this.andjKtPoluvagonPlanUty = andjKtPoluvagonPlanUty;
	}

	public void setAndjKtSisternaPlanUty(int andjKtSisternaPlanUty) {
		this.andjKtSisternaPlanUty = andjKtSisternaPlanUty;
	}

	public void setAndjKtBoshqaPlanUty(int andjKtBoshqaPlanUty) {
		this.andjKtBoshqaPlanUty = andjKtBoshqaPlanUty;
	}

	public void setSamKrpKritiPlanUty(int samKrpKritiPlanUty) {
		this.samKrpKritiPlanUty = samKrpKritiPlanUty;
	}

	public void setSamKrpPlatformaPlanUty(int samKrpPlatformaPlanUty) {
		this.samKrpPlatformaPlanUty = samKrpPlatformaPlanUty;
	}

	public void setSamKrpPoluvagonPlanUty(int samKrpPoluvagonPlanUty) {
		this.samKrpPoluvagonPlanUty = samKrpPoluvagonPlanUty;
	}

	public void setSamKrpSisternaPlanUty(int samKrpSisternaPlanUty) {
		this.samKrpSisternaPlanUty = samKrpSisternaPlanUty;
	}

	public void setSamKrpBoshqaPlanUty(int samKrpBoshqaPlanUty) {
		this.samKrpBoshqaPlanUty = samKrpBoshqaPlanUty;
	}

	public void setHavKrpKritiPlanUty(int havKrpKritiPlanUty) {
		this.havKrpKritiPlanUty = havKrpKritiPlanUty;
	}

	public void setHavKrpPlatformaPlanUty(int havKrpPlatformaPlanUty) {
		this.havKrpPlatformaPlanUty = havKrpPlatformaPlanUty;
	}

	public void setHavKrpPoluvagonPlanUty(int havKrpPoluvagonPlanUty) {
		this.havKrpPoluvagonPlanUty = havKrpPoluvagonPlanUty;
	}

	public void setHavKrpSisternaPlanUty(int havKrpSisternaPlanUty) {
		this.havKrpSisternaPlanUty = havKrpSisternaPlanUty;
	}

	public void setHavKrpBoshqaPlanUty(int havKrpBoshqaPlanUty) {
		this.havKrpBoshqaPlanUty = havKrpBoshqaPlanUty;
	}

	public void setAndjKrpKritiPlanUty(int andjKrpKritiPlanUty) {
		this.andjKrpKritiPlanUty = andjKrpKritiPlanUty;
	}

	public void setAndjKrpPlatformaPlanUty(int andjKrpPlatformaPlanUty) {
		this.andjKrpPlatformaPlanUty = andjKrpPlatformaPlanUty;
	}

	public void setAndjKrpPoluvagonPlanUty(int andjKrpPoluvagonPlanUty) {
		this.andjKrpPoluvagonPlanUty = andjKrpPoluvagonPlanUty;
	}

	public void setAndjKrpSisternaPlanUty(int andjKrpSisternaPlanUty) {
		this.andjKrpSisternaPlanUty = andjKrpSisternaPlanUty;
	}

	public void setAndjKrpBoshqaPlanUty(int andjKrpBoshqaPlanUty) {
		this.andjKrpBoshqaPlanUty = andjKrpBoshqaPlanUty;
	}

	public void setSamDtKritiPlanUtyMonths(int samDtKritiPlanUtyMonths) {
		this.samDtKritiPlanUtyMonths = samDtKritiPlanUtyMonths;
	}

	public void setSamDtPlatformaPlanUtyMonths(int samDtPlatformaPlanUtyMonths) {
		this.samDtPlatformaPlanUtyMonths = samDtPlatformaPlanUtyMonths;
	}

	public void setSamDtPoluvagonPlanUtyMonths(int samDtPoluvagonPlanUtyMonths) {
		this.samDtPoluvagonPlanUtyMonths = samDtPoluvagonPlanUtyMonths;
	}

	public void setSamDtSisternaPlanUtyMonths(int samDtSisternaPlanUtyMonths) {
		this.samDtSisternaPlanUtyMonths = samDtSisternaPlanUtyMonths;
	}

	public void setSamDtBoshqaPlanUtyMonths(int samDtBoshqaPlanUtyMonths) {
		this.samDtBoshqaPlanUtyMonths = samDtBoshqaPlanUtyMonths;
	}

	public void setHavDtKritiPlanUtyMonths(int havDtKritiPlanUtyMonths) {
		this.havDtKritiPlanUtyMonths = havDtKritiPlanUtyMonths;
	}

	public void setHavDtPlatformaPlanUtyMonths(int havDtPlatformaPlanUtyMonths) {
		this.havDtPlatformaPlanUtyMonths = havDtPlatformaPlanUtyMonths;
	}

	public void setHavDtPoluvagonPlanUtyMonths(int havDtPoluvagonPlanUtyMonths) {
		this.havDtPoluvagonPlanUtyMonths = havDtPoluvagonPlanUtyMonths;
	}

	public void setHavDtSisternaPlanUtyMonths(int havDtSisternaPlanUtyMonths) {
		this.havDtSisternaPlanUtyMonths = havDtSisternaPlanUtyMonths;
	}

	public void setHavDtBoshqaPlanUtyMonths(int havDtBoshqaPlanUtyMonths) {
		this.havDtBoshqaPlanUtyMonths = havDtBoshqaPlanUtyMonths;
	}

	public void setAndjDtKritiPlanUtyMonths(int andjDtKritiPlanUtyMonths) {
		this.andjDtKritiPlanUtyMonths = andjDtKritiPlanUtyMonths;
	}

	public void setAndjDtPlatformaPlanUtyMonths(int andjDtPlatformaPlanUtyMonths) {
		this.andjDtPlatformaPlanUtyMonths = andjDtPlatformaPlanUtyMonths;
	}

	public void setAndjDtPoluvagonPlanUtyMonths(int andjDtPoluvagonPlanUtyMonths) {
		this.andjDtPoluvagonPlanUtyMonths = andjDtPoluvagonPlanUtyMonths;
	}

	public void setAndjDtSisternaPlanUtyMonths(int andjDtSisternaPlanUtyMonths) {
		this.andjDtSisternaPlanUtyMonths = andjDtSisternaPlanUtyMonths;
	}

	public void setAndjDtBoshqaPlanUtyMonths(int andjDtBoshqaPlanUtyMonths) {
		this.andjDtBoshqaPlanUtyMonths = andjDtBoshqaPlanUtyMonths;
	}

	public void setSamKtKritiPlanUtyMonths(int samKtKritiPlanUtyMonths) {
		this.samKtKritiPlanUtyMonths = samKtKritiPlanUtyMonths;
	}

	public void setSamKtPlatformaPlanUtyMonths(int samKtPlatformaPlanUtyMonths) {
		this.samKtPlatformaPlanUtyMonths = samKtPlatformaPlanUtyMonths;
	}

	public void setSamKtPoluvagonPlanUtyMonths(int samKtPoluvagonPlanUtyMonths) {
		this.samKtPoluvagonPlanUtyMonths = samKtPoluvagonPlanUtyMonths;
	}

	public void setSamKtSisternaPlanUtyMonths(int samKtSisternaPlanUtyMonths) {
		this.samKtSisternaPlanUtyMonths = samKtSisternaPlanUtyMonths;
	}

	public void setSamKtBoshqaPlanUtyMonths(int samKtBoshqaPlanUtyMonths) {
		this.samKtBoshqaPlanUtyMonths = samKtBoshqaPlanUtyMonths;
	}

	public void setHavKtKritiPlanUtyMonths(int havKtKritiPlanUtyMonths) {
		this.havKtKritiPlanUtyMonths = havKtKritiPlanUtyMonths;
	}

	public void setHavKtPlatformaPlanUtyMonths(int havKtPlatformaPlanUtyMonths) {
		this.havKtPlatformaPlanUtyMonths = havKtPlatformaPlanUtyMonths;
	}

	public void setHavKtPoluvagonPlanUtyMonths(int havKtPoluvagonPlanUtyMonths) {
		this.havKtPoluvagonPlanUtyMonths = havKtPoluvagonPlanUtyMonths;
	}

	public void setHavKtSisternaPlanUtyMonths(int havKtSisternaPlanUtyMonths) {
		this.havKtSisternaPlanUtyMonths = havKtSisternaPlanUtyMonths;
	}

	public void setHavKtBoshqaPlanUtyMonths(int havKtBoshqaPlanUtyMonths) {
		this.havKtBoshqaPlanUtyMonths = havKtBoshqaPlanUtyMonths;
	}

	public void setAndjKtKritiPlanUtyMonths(int andjKtKritiPlanUtyMonths) {
		this.andjKtKritiPlanUtyMonths = andjKtKritiPlanUtyMonths;
	}

	public void setAndjKtPlatformaPlanUtyMonths(int andjKtPlatformaPlanUtyMonths) {
		this.andjKtPlatformaPlanUtyMonths = andjKtPlatformaPlanUtyMonths;
	}

	public void setAndjKtPoluvagonPlanUtyMonths(int andjKtPoluvagonPlanUtyMonths) {
		this.andjKtPoluvagonPlanUtyMonths = andjKtPoluvagonPlanUtyMonths;
	}

	public void setAndjKtSisternaPlanUtyMonths(int andjKtSisternaPlanUtyMonths) {
		this.andjKtSisternaPlanUtyMonths = andjKtSisternaPlanUtyMonths;
	}

	public void setAndjKtBoshqaPlanUtyMonths(int andjKtBoshqaPlanUtyMonths) {
		this.andjKtBoshqaPlanUtyMonths = andjKtBoshqaPlanUtyMonths;
	}

	public void setSamKrpKritiPlanUtyMonths(int samKrpKritiPlanUtyMonths) {
		this.samKrpKritiPlanUtyMonths = samKrpKritiPlanUtyMonths;
	}

	public void setSamKrpPlatformaPlanUtyMonths(int samKrpPlatformaPlanUtyMonths) {
		this.samKrpPlatformaPlanUtyMonths = samKrpPlatformaPlanUtyMonths;
	}

	public void setSamKrpPoluvagonPlanUtyMonths(int samKrpPoluvagonPlanUtyMonths) {
		this.samKrpPoluvagonPlanUtyMonths = samKrpPoluvagonPlanUtyMonths;
	}

	public void setSamKrpSisternaPlanUtyMonths(int samKrpSisternaPlanUtyMonths) {
		this.samKrpSisternaPlanUtyMonths = samKrpSisternaPlanUtyMonths;
	}

	public void setSamKrpBoshqaPlanUtyMonths(int samKrpBoshqaPlanUtyMonths) {
		this.samKrpBoshqaPlanUtyMonths = samKrpBoshqaPlanUtyMonths;
	}

	public void setHavKrpKritiPlanUtyMonths(int havKrpKritiPlanUtyMonths) {
		this.havKrpKritiPlanUtyMonths = havKrpKritiPlanUtyMonths;
	}

	public void setHavKrpPlatformaPlanUtyMonths(int havKrpPlatformaPlanUtyMonths) {
		this.havKrpPlatformaPlanUtyMonths = havKrpPlatformaPlanUtyMonths;
	}

	public void setHavKrpPoluvagonPlanUtyMonths(int havKrpPoluvagonPlanUtyMonths) {
		this.havKrpPoluvagonPlanUtyMonths = havKrpPoluvagonPlanUtyMonths;
	}

	public void setHavKrpSisternaPlanUtyMonths(int havKrpSisternaPlanUtyMonths) {
		this.havKrpSisternaPlanUtyMonths = havKrpSisternaPlanUtyMonths;
	}

	public void setHavKrpBoshqaPlanUtyMonths(int havKrpBoshqaPlanUtyMonths) {
		this.havKrpBoshqaPlanUtyMonths = havKrpBoshqaPlanUtyMonths;
	}

	public void setAndjKrpKritiPlanUtyMonths(int andjKrpKritiPlanUtyMonths) {
		this.andjKrpKritiPlanUtyMonths = andjKrpKritiPlanUtyMonths;
	}

	public void setAndjKrpPlatformaPlanUtyMonths(int andjKrpPlatformaPlanUtyMonths) {
		this.andjKrpPlatformaPlanUtyMonths = andjKrpPlatformaPlanUtyMonths;
	}

	public void setAndjKrpPoluvagonPlanUtyMonths(int andjKrpPoluvagonPlanUtyMonths) {
		this.andjKrpPoluvagonPlanUtyMonths = andjKrpPoluvagonPlanUtyMonths;
	}

	public void setAndjKrpSisternaPlanUtyMonths(int andjKrpSisternaPlanUtyMonths) {
		this.andjKrpSisternaPlanUtyMonths = andjKrpSisternaPlanUtyMonths;
	}

	public void setAndjKrpBoshqaPlanUtyMonths(int andjKrpBoshqaPlanUtyMonths) {
		this.andjKrpBoshqaPlanUtyMonths = andjKrpBoshqaPlanUtyMonths;
	}
}
