package com.sms.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sms.model.VagonMalumot;

import com.sms.service.VagonMalumotService;

@Controller
public class VagonMalumotController {
	
	@Autowired
	VagonMalumotService malumotService;

	//Yuklab olish uchun Malumot yigib beradi
	List<VagonMalumot> vagonsToDownload  = new ArrayList<>();
	
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/createExcelMalumot")
	public void pdfFile(HttpServletResponse response) throws IOException {
		malumotService.createPdf(vagonsToDownload,response);
	 }
		
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/chiqishMalumot")
	public String main(Model model) {

		vagonsToDownload = malumotService.findAll();
		model.addAttribute("vagons", malumotService.findAll());

		//vaqtni olib turadi
		model.addAttribute("samDate",malumotService.getSamDate());
		model.addAttribute("havDate", malumotService.getHavDate());
		model.addAttribute("andjDate",malumotService.getAndjDate());
		
		return "chiqishMalumot";
	}
	
	 //yangi vagon qoshish uchun oyna
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/newVagonMalumot")
	public String createVagonForm(Model model) {
    	VagonMalumot vagon = new VagonMalumot();
		model.addAttribute("vagon", vagon);
		return "create_vagonMalumot";
	}
    
    //vagon qoshish
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons/saveVagonMalumot")
	public String saveVagon(@ModelAttribute("vagon") VagonMalumot vagon, HttpServletRequest request ) { 
    	if (request.isUserInRole("DIRECTOR")) {
    		malumotService.saveVagon(vagon);
        }else if (request.isUserInRole("SAM")) {
        	malumotService.saveVagonSam(vagon);
        }else if (request.isUserInRole("HAV")) {
        	malumotService.saveVagonHav(vagon);
        }else if (request.isUserInRole("ANDJ")) {
        	malumotService.saveVagonAndj(vagon);
        }
    	
		return "redirect:/vagons/chiqishMalumot";
	}
    
  //tahrirlash uchun oyna
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/editMalumot/{id}")
	public String editVagonForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("vagon",malumotService.getVagonById(id));
		return "edit_vagonMalumot";
	}

    //tahrirni saqlash
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons/updateMalumot/{id}")
	public String updateVagon(@ModelAttribute("vagon") VagonMalumot vagon,@PathVariable Long id,Model model, HttpServletRequest request) throws NotFoundException {
    	if (request.isUserInRole("DIRECTOR")) {
    		malumotService.updateVagon(vagon, id);
        }else if (request.isUserInRole("SAM")) {
        	malumotService.updateVagonSam(vagon, id);
        }else if (request.isUserInRole("HAV")) {
        	malumotService.updateVagonHav(vagon, id);
        }else if (request.isUserInRole("ANDJ")) {
        	malumotService.updateVagonAndj(vagon, id);
        }
		return "redirect:/vagons/chiqishMalumot";
	}
	
    //bazadan o'chirish
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/deleteMalumot/{id}")
	public String deleteVagonForm(@PathVariable("id") Long id, HttpServletRequest request ) throws NotFoundException {
    	if (request.isUserInRole("DIRECTOR")) {
    		malumotService.deleteVagonById(id);
        }else if (request.isUserInRole("SAM")) {
        		malumotService.deleteVagonSam(id);
        }else if (request.isUserInRole("HAV")) {
    		malumotService.deleteVagonHav(id);
        }else if (request.isUserInRole("ANDJ")) {
    		malumotService.deleteVagonAndj(id);
        }
		return "redirect:/vagons/chiqishMalumot";
	}
    
    // wagon nomer orqali qidirish 
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/searchMalumot")
	public String searchByNomer(Model model, @RequestParam(value = "participant", required = false) Integer participant) {
		if(participant==null  ) {
			model.addAttribute("vagons", malumotService.findAll());
			vagonsToDownload = malumotService.findAll();
		}else {
			model.addAttribute("vagons", malumotService.searchByNomer(participant));
			List<VagonMalumot> emptyList = new ArrayList<>();
			vagonsToDownload=emptyList;
			vagonsToDownload.add(malumotService.searchByNomer(participant)) ;
		}
		//vaqtni olib turadi
		model.addAttribute("samDate",malumotService.getSamDate());
		model.addAttribute("havDate", malumotService.getHavDate());
		model.addAttribute("andjDate",malumotService.getAndjDate());
		return "chiqishMalumot";
    }

	//Filter qilish
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/filterMalumot")
	public String filterByDate(Model model,  @RequestParam(value = "depoNomi") String depoNomi,
							   					@RequestParam(value = "country", required = false) String country,
							   					@RequestParam(value = "saqlanganVaqti", defaultValue = "2200-01-01") String saqlanganVaqti){
		String saqlanganVaqt = saqlanganVaqti.substring(8) + "-" + saqlanganVaqti.substring(5,7) + "-" + saqlanganVaqti.substring(0,4);

		if(depoNomi.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi") && !saqlanganVaqt.equalsIgnoreCase("01-01-2200")){
			model.addAttribute("vagons", malumotService.filterByDate(saqlanganVaqt));
		}else if(depoNomi.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi") && saqlanganVaqt.equalsIgnoreCase("01-01-2200")){
			model.addAttribute("vagons", malumotService.filterByCountry(country));
		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi") && saqlanganVaqt.equalsIgnoreCase("01-01-2200")){
			model.addAttribute("vagons", malumotService.filterByDepoNomi(depoNomi));
		}else if(depoNomi.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi") && !saqlanganVaqt.equalsIgnoreCase("01-01-2200")){
			model.addAttribute("vagons", malumotService.filterByCountryAndDate(country, saqlanganVaqt));
		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi") && saqlanganVaqt.equalsIgnoreCase("01-01-2200")){
			model.addAttribute("vagons", malumotService.filterByDepoNomiAndCountry(depoNomi, country));
		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi") && !saqlanganVaqt.equalsIgnoreCase("01-01-2200")){
			model.addAttribute("vagons", malumotService.filterByDepoNomiAndDate(depoNomi, saqlanganVaqt));
		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi") && !saqlanganVaqt.equalsIgnoreCase("01-01-2200")){
			model.addAttribute("vagons", malumotService.filterByDepoNomiCountryAndDate(depoNomi, country, saqlanganVaqt));
		}else {
			model.addAttribute("vagons", malumotService.findAll());
			vagonsToDownload = malumotService.findAll();
		}




//		if (!saqlanganVaqt.equals("01-01-2200") && !depoNomi.equalsIgnoreCase("Hammasi")) {
//			vagonsToDownload =   malumotService.filterByDateAndDepoNomi(saqlanganVaqt,depoNomi);
//			model.addAttribute("vagons", malumotService.filterByDateAndDepoNomi(saqlanganVaqt,depoNomi));
//		}else if (saqlanganVaqt.equals("01-01-2200") && !depoNomi.equalsIgnoreCase("Hammasi")) {
//			model.addAttribute("vagons", malumotService.findAllByDepoNomi(depoNomi));
//			vagonsToDownload =  malumotService.findAllByDepoNomi(depoNomi);
//		}else if (!saqlanganVaqt.equals("01-01-2200") && depoNomi.equalsIgnoreCase("Hammasi")) {
//			vagonsToDownload = malumotService.findAllByDate(saqlanganVaqt);
//			model.addAttribute("vagons", malumotService.findAllByDate(saqlanganVaqt));
//		}else{
//			model.addAttribute("vagons", malumotService.findAll());
//			vagonsToDownload = malumotService.findAll();
//		}

		//vaqtni olib turadi
		model.addAttribute("samDate",malumotService.getSamDate());
		model.addAttribute("havDate", malumotService.getHavDate());
		model.addAttribute("andjDate",malumotService.getAndjDate());

		return "chiqishMalumot";
	}
}
