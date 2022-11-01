package com.sms.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.sms.dto.VagonDto;
import com.sms.model.VagonModel;

public interface VagonService {
	
	List<VagonModel> findAll();

	VagonModel saveVagon(VagonDto vagon);
	VagonModel saveVagonSam(VagonDto vagon);
	VagonModel saveVagonHav(VagonDto vagon);
	VagonModel saveVagonAndj(VagonDto vagon);


	VagonModel updateVagon(VagonDto vagon, long id);
	VagonModel updateVagonSam(VagonDto vagon, long id);
	VagonModel updateVagonHav(VagonDto vagon, long id);
	VagonModel updateVagonAndj(VagonDto vagon, long id);
	
	String getCurrentDate();
	String getSamDate();
	String getHavDate();
	String getAndjDate();
	
	VagonModel getVagonById(long id);
	
	void deleteVagonById(long id) throws NotFoundException;
	void deleteVagonSam(long id) throws NotFoundException;
	void deleteVagonHav(long id) throws NotFoundException;
	void deleteVagonAndj(long id) throws NotFoundException;

	VagonModel findByKeyword(Integer participant);
	//filterniki
	Integer getCount(String string);

	Integer getVagonsCount(String kriti, String depoNomi);

	//filterniki OTY
	Integer getCount(String string,String country);

	Integer getVagonsCount(String kriti, String depoNomi,String country);


	List<VagonModel> findAllByDepoNomiVagonTuriAndCountry(String depoNomi, String vagonTuri, String country);

	List<VagonModel> findAllByDepoNomiAndVagonTuri(String depoNomi, String vagonTuri);

	List<VagonModel> findAllByDepoNomiAndCountry(String depoNomi, String country);

	List<VagonModel> findAllByDepoNomi(String depoNomi);
	
	List<VagonModel> findAllByVagonTuriAndCountry(String vagonTuri, String country);

	List<VagonModel> findAllBycountry(String country);

	List<VagonModel> findAllByVagonTuri(String vagonTuri);

	void createPdf(List<VagonModel> vagons, HttpServletResponse response) throws IOException;


	void pdfTableFile(List<Integer> vagonsToDownloadTables, HttpServletResponse response) throws IOException;
}
