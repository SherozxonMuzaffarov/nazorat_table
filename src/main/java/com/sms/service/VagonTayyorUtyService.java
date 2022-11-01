package com.sms.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import com.sms.dto.PlanUtyDto;
import com.sms.model.PlanUty;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.sms.model.VagonTayyorUty;


public interface VagonTayyorUtyService {
	
	String getSamDate();
	String getHavDate();
	String getAndjDate();

	VagonTayyorUty saveVagon(VagonTayyorUty vagon);
	VagonTayyorUty saveVagonSam(VagonTayyorUty vagon);
	VagonTayyorUty saveVagonHav(VagonTayyorUty vagon);
	VagonTayyorUty saveVagonAndj(VagonTayyorUty vagon);
	

	VagonTayyorUty getVagonById(long id);
	
	VagonTayyorUty updateVagon(VagonTayyorUty vagon, long id);
	VagonTayyorUty updateVagonSam(VagonTayyorUty vagon, long id);
	VagonTayyorUty updateVagonHav(VagonTayyorUty vagon, long id);
	VagonTayyorUty updateVagonAndj(VagonTayyorUty vagon, long id);
	
	VagonTayyorUty updateVagonMonths(VagonTayyorUty vagon, long id);
	VagonTayyorUty updateVagonSamMonths(VagonTayyorUty vagon, long id);
	VagonTayyorUty updateVagonHavMonths(VagonTayyorUty vagon, long id);
	VagonTayyorUty updateVagonAndjMonths(VagonTayyorUty vagon, long id);
	
	void deleteVagonById(long id) throws NotFoundException;
	void deleteVagonSam(long id) throws NotFoundException;
	void deleteVagonHav(long id) throws NotFoundException;
	void deleteVagonAndj(long id) throws NotFoundException;
	
	List<VagonTayyorUty> findAll();

	List<VagonTayyorUty> findAll(String oy);
	
	void savePlan(PlanUtyDto planDto);


	int countByDepoNomiVagonTuriAndTamirTuri(String depoNomi, String vagonTuri, String tamirTuri);
	
	int countAllActiveByDepoNomiVagonTuriAndTamirTuri(String depoNomi, String vagnTuri, String tamirTuri, String oy);
	
	VagonTayyorUty findByNomer(Integer nomer);
	
	VagonTayyorUty searchByNomer(Integer nomer, String oy);

	//filterniki
	List<VagonTayyorUty> findAllByDepoNomiAndVagonTuri(String depoNomi, String vagonTuri, String oy);

	List<VagonTayyorUty> findAllByDepoNomi(String depoNomi, String oy);

	List<VagonTayyorUty> findAllByVagonTuri(String vagonTuri, String oy);
	
	// hamma oylar uchun filterniki
	
	List<VagonTayyorUty> findByDepoNomiAndVagonTuri(String depoNomi, String vagonTuri);

	List<VagonTayyorUty> findByDepoNomi(String depoNomi);

	List<VagonTayyorUty> findByVagonTuri(String vagonTuri);

	VagonTayyorUty findById(Long id);

	void createPdf(List<VagonTayyorUty> vagonsToDownload, HttpServletResponse response) throws IOException;

	PlanUty getPlanuty();

	void pdfFileTable(List<Integer> vagonsToDownloadAllTable, HttpServletResponse response) throws IOException;
}
