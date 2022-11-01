package com.sms.service;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.sms.model.VagonMalumot;

public interface VagonMalumotService {

	VagonMalumot saveVagon(VagonMalumot vagon);
	VagonMalumot saveVagonSam(VagonMalumot vagon);
	VagonMalumot saveVagonHav(VagonMalumot vagon);
	VagonMalumot saveVagonAndj(VagonMalumot vagon);
	
	List<VagonMalumot> findAll();
	VagonMalumot getVagonById(long id);
	
	VagonMalumot updateVagon(VagonMalumot vagon, long id);
	VagonMalumot updateVagonSam(VagonMalumot vagon, long id);
	VagonMalumot updateVagonHav(VagonMalumot vagon, long id);
	VagonMalumot updateVagonAndj(VagonMalumot vagon, long id);
	
	void deleteVagonById(long id) throws NotFoundException;
	void deleteVagonSam(long id) throws NotFoundException;
	void deleteVagonHav(long id) throws NotFoundException;
	void deleteVagonAndj(long id) throws NotFoundException;
	

	VagonMalumot searchByNomer(Integer nomer);
	
	String getSamDate();
	String getHavDate();
	String getAndjDate();
	
	void createPdf(List<VagonMalumot> vagonsToDownload, HttpServletResponse response) throws IOException;

	//	Filterniki
	List<VagonMalumot> filterByDate(String saqlanganVaqt);

	List<VagonMalumot> filterByCountry(String country);

	List<VagonMalumot> filterByDepoNomi(String depoNomi);

	List<VagonMalumot> filterByCountryAndDate(String country, String saqlanganVaqt);

	List<VagonMalumot> filterByDepoNomiAndCountry(String depoNomi, String country);

	List<VagonMalumot> filterByDepoNomiAndDate(String depoNomi, String saqlanganVaqt);

	List<VagonMalumot> filterByDepoNomiCountryAndDate(String depoNomi, String country, String saqlanganVaqt);

}
