package com.sms.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sms.dto.PlanUtyDto;
import com.sms.model.PlanUty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sms.model.VagonTayyorUty;
import com.sms.service.VagonTayyorUtyService;

@Controller
public class VagonTayyorUtyController {
	
	@Autowired
	private VagonTayyorUtyService vagonTayyorUtyService;

	LocalDate today = LocalDate.now();
	int month = today.getMonthValue();
	
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/tablesUty")
	public String tables(Model model) {

		String oy = null;
		switch (month) {
			case 1: 
				oy = "Yanvar";
				break;
			case 2: 
				oy = "Fevral";
				break;
			case 3: 
				oy = "Mart";
				break;
			case 4: 
				oy = "Aprel";
				break;
			case 5: 
				oy = "May";
				break;
			case 6: 
				oy = "Iyun";
				break;
			case 7: 
				oy = "Iyul";
				break;
			case 8: 
				oy = "Avgust";
				break;
			case 9: 
				oy = "Sentabr";
				break;
			case 10: 
				oy = "Oktabr";
				break;
			case 11: 
				oy = "Noyabr";
				break;
			case 12: 
				oy = "Dekabr";
				break;
		}
		
		model.addAttribute("month", oy);
		model.addAttribute("year", month + " oylik");
		return "tablesUty";
	}
	
	//Yuklab olish uchun Malumot yigib beradi
	List<VagonTayyorUty> vagonsToDownload  = new ArrayList<>();
	List<Integer> vagonsToDownloadAllTable  = new ArrayList<>();

	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/createExcelUty")
	public void pdfFile(Model model, HttpServletResponse response) throws IOException {
		vagonTayyorUtyService.createPdf(vagonsToDownload, response);
		model.addAttribute("vagons",vagonsToDownload);
	 }

	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/createExcelUtyAllMonth")
	public void createPdf(Model model,HttpServletResponse response) throws IOException {
		vagonTayyorUtyService.createPdf(vagonsToDownload, response);
		model.addAttribute("vagons",vagonsToDownload);
	 }


	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/createExcelTableUty")
	public void pdfFileTable(Model model, HttpServletResponse response) throws IOException {
		vagonTayyorUtyService.pdfFileTable(vagonsToDownloadAllTable, response);
		model.addAttribute("vagons",vagonsToDownloadAllTable);
	}
	
    //Tayyor yangi vagon qoshish uchun oyna
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/newTayyorUty")
	public String createVagonForm(Model model) {
		VagonTayyorUty vagonTayyor = new VagonTayyorUty();
		model.addAttribute("vagon", vagonTayyor);
		return "create_tayyorvagonUty";
	}
    
    //TAyyor vagon qoshish
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons/saveTayyorUty")
	public String saveVagon(@ModelAttribute("vagon") VagonTayyorUty vagon, HttpServletRequest request ) { 
    	if (request.isUserInRole("DIRECTOR")) {
    		vagonTayyorUtyService.saveVagon(vagon);
        }else if (request.isUserInRole("SAM")) {
        	vagonTayyorUtyService.saveVagonSam(vagon);
        }else if (request.isUserInRole("HAV")) {
        	vagonTayyorUtyService.saveVagonHav(vagon);
        }else if (request.isUserInRole("ANDJ")) {
        	vagonTayyorUtyService.saveVagonAndj(vagon);
        }
		return "redirect:/vagons/AllPlanTableUty";
	}
    
    //tahrirlash uchun oyna bir oy
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/editTayyorUty/{id}")
	public String editVagonForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("vagon",vagonTayyorUtyService.getVagonById(id));
		return "edit_tayyorvagonUty";
	}

    //tahrirni saqlash bir oy
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons/updateTayyorUty/{id}")
	public String updateVagon(@ModelAttribute("vagon") VagonTayyorUty vagon, @PathVariable Long id, HttpServletRequest request) throws NotFoundException {
    	if (request.isUserInRole("DIRECTOR")) {
    		vagonTayyorUtyService.updateVagon(vagon, id);
        }else if (request.isUserInRole("SAM")) {
        	vagonTayyorUtyService.updateVagonSam(vagon, id);
        }else if (request.isUserInRole("HAV")) {
        	vagonTayyorUtyService.updateVagonHav(vagon, id);
        }else if (request.isUserInRole("ANDJ")) {
        	vagonTayyorUtyService.updateVagonAndj(vagon, id);
        }
		return "redirect:/vagons/AllPlanTableUty";
	}
    
    //** oylar uchun update
    //tahrirlash uchun oyna 
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/editTayyorUtyMonths/{id}")
	public String editVagonFormMonths(@PathVariable("id") Long id, Model model) {
		model.addAttribute("vagon",vagonTayyorUtyService.getVagonById(id));
		return "edit_tayyorvagonUtyMonths";
	}

    //tahrirni saqlash 
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons/updateTayyorUtyMonths/{id}")
	public String updateVagonMonths(@ModelAttribute("vagon") VagonTayyorUty vagon, @PathVariable Long id, HttpServletRequest request) throws NotFoundException {
    	if (request.isUserInRole("DIRECTOR")) {
    		vagonTayyorUtyService.updateVagonMonths(vagon, id);
        }else if (request.isUserInRole("SAM")) {
        	vagonTayyorUtyService.updateVagonSamMonths(vagon, id);
        }else if (request.isUserInRole("HAV")) {
        	vagonTayyorUtyService.updateVagonHavMonths(vagon, id);
        }else if (request.isUserInRole("ANDJ")) {
        	vagonTayyorUtyService.updateVagonAndjMonths(vagon, id);
        }
    	return "redirect:/vagons/planTableForMonthsUty";
	}

    //bazadan o'chirish
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/deleteTayyorUty/{id}")
	public String deleteVagonForm(@PathVariable("id") Long id, HttpServletRequest request ) throws NotFoundException {
    	if (request.isUserInRole("DIRECTOR")) {
    		vagonTayyorUtyService.deleteVagonById(id);
        }else if (request.isUserInRole("SAM")) {
        		vagonTayyorUtyService.deleteVagonSam(id);
        }else if (request.isUserInRole("HAV")) {
    		vagonTayyorUtyService.deleteVagonHav(id);
        }else if (request.isUserInRole("ANDJ")) {
    		vagonTayyorUtyService.deleteVagonAndj(id);
        }
		return "redirect:/vagons/AllPlanTableUty";
	}

    //All planlar uchun 
    @PreAuthorize(value = "hasRole('DIRECTOR')")
   	@GetMapping("/vagons/newPlanUty")
   	public String addPlan(Model model) {
   		PlanUtyDto planDto = new PlanUtyDto();
   		model.addAttribute("planDto", planDto);
   		return "add_planUty";
   	}
    
    //Plan qoshish
    @PreAuthorize(value = "hasRole('DIRECTOR')")
	@PostMapping("/vagons/savePlanUty")
	public String savePlan(@ModelAttribute("planDto") PlanUtyDto planDto) {
    	vagonTayyorUtyService.savePlan(planDto);
    	return "redirect:/vagons/AllPlanTableUty";
	}
    
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/AllPlanTableUty")
	public String getAllPlan(Model model) {

		String oy=null;
		switch (month) {
			case 1:
				oy = "-01";
				break;
			case 2:
				oy = "-02";
				break;
			case 3:
				oy = "-03";
				break;
			case 4:
				oy = "-04";
				break;
			case 5:
				oy = "-05";
				break;
			case 6:
				oy = "-06";
				break;
			case 7:
				oy = "-07";
				break;
			case 8:
				oy = "-08";
				break;
			case 9:
				oy = "-09";
				break;
			case 10:
				oy = "-10";
				break;
			case 11:
				oy = "-11";
				break;
			case 12:
				oy = "-12";
				break;
		}
    	
    	//listni toldirish uchun
    	vagonsToDownload = vagonTayyorUtyService.findAll(oy);
    	model.addAttribute("vagons", vagonTayyorUtyService.findAll(oy));

    	//vaqtni olib turadi
		model.addAttribute("samDate",vagonTayyorUtyService.getSamDate());
		model.addAttribute("havDate", vagonTayyorUtyService.getHavDate());
		model.addAttribute("andjDate",vagonTayyorUtyService.getAndjDate());
    	
    	PlanUty planDto = vagonTayyorUtyService.getPlanuty();
    	//planlar kiritish

    	//havos hamma plan
    	int HavDtHammaPlan = planDto.getHavDtKritiPlanUty() + planDto.getHavDtPlatformaPlanUty() + planDto.getHavDtPoluvagonPlanUty() + planDto.getHavDtSisternaPlanUty() + planDto.getHavDtBoshqaPlanUty();
    	int HavDtKritiPlan = planDto.getHavDtKritiPlanUty();
    	int HavDtPlatformaPlan = planDto.getHavDtPlatformaPlanUty();
    	int HavDtPoluvagonPlan = planDto.getHavDtPoluvagonPlanUty();
    	int HavDtSisternaPlan = planDto.getHavDtSisternaPlanUty();
    	int HavDtBoshqaPlan = planDto.getHavDtBoshqaPlanUty();

		
		model.addAttribute("HavDtHammaPlan", HavDtHammaPlan);
    	model.addAttribute("HavDtKritiPlan", HavDtKritiPlan);
    	model.addAttribute("HavDtPlatformaPlan", HavDtPlatformaPlan);
    	model.addAttribute("HavDtPoluvagonPlan", HavDtPoluvagonPlan);
    	model.addAttribute("HavDtSisternaPlan", HavDtSisternaPlan);
    	model.addAttribute("HavDtBoshqaPlan", HavDtBoshqaPlan);
    	
    	//andijon hamma plan depo tamir
    	int AndjDtHammaPlan = planDto.getAndjDtKritiPlanUty() + planDto.getAndjDtPlatformaPlanUty() + planDto.getAndjDtPoluvagonPlanUty() + planDto.getAndjDtSisternaPlanUty() + planDto.getAndjDtBoshqaPlanUty();
		int AndjDtKritiPlan =  planDto.getAndjDtKritiPlanUty();
		int AndjDtPlatformaPlan =  planDto.getAndjDtPlatformaPlanUty();
		int AndjDtPoluvagonPlan =  planDto.getAndjDtPoluvagonPlanUty();
		int AndjDtSisternaPlan =  planDto.getAndjDtSisternaPlanUty();
		int AndjDtBoshqaPlan =  planDto.getAndjDtBoshqaPlanUty();
		
		model.addAttribute("AndjDtHammaPlan", AndjDtHammaPlan);
    	model.addAttribute("AndjDtKritiPlan", AndjDtKritiPlan);
    	model.addAttribute("AndjDtPlatformaPlan", AndjDtPlatformaPlan);
    	model.addAttribute("AndjDtPoluvagonPlan", AndjDtPoluvagonPlan);
    	model.addAttribute("AndjDtSisternaPlan", AndjDtSisternaPlan);
    	model.addAttribute("AndjDtBoshqaPlan", AndjDtBoshqaPlan);

		//samarqand depo tamir
		int SamDtHammaPlan=planDto.getSamDtKritiPlanUty() + planDto.getSamDtPlatformaPlanUty() + planDto.getSamDtPoluvagonPlanUty() + planDto.getSamDtSisternaPlanUty() + planDto.getSamDtBoshqaPlanUty();
		int SamDtKritiPlan =  planDto.getSamDtKritiPlanUty();
		int SamDtPlatformaPlan =  planDto.getSamDtPlatformaPlanUty();
		int SamDtPoluvagonPlan =  planDto.getSamDtPoluvagonPlanUty();
		int SamDtSisternaPlan =  planDto.getSamDtSisternaPlanUty();
		int SamDtBoshqaPlan =  planDto.getSamDtBoshqaPlanUty();
		
		model.addAttribute("SamDtHammaPlan",SamDtHammaPlan);
		model.addAttribute("SamDtKritiPlan", SamDtKritiPlan);
		model.addAttribute("SamDtPlatformaPlan", SamDtPlatformaPlan);
		model.addAttribute("SamDtPoluvagonPlan", SamDtPoluvagonPlan);
		model.addAttribute("SamDtSisternaPlan", SamDtSisternaPlan);
		model.addAttribute("SamDtBoshqaPlan", SamDtBoshqaPlan);

		// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = planDto.getAndjDtKritiPlanUty() + planDto.getHavDtKritiPlanUty() + planDto.getSamDtKritiPlanUty();
		int DtPlatformaPlan = planDto.getAndjDtPlatformaPlanUty() + planDto.getHavDtPlatformaPlanUty() + planDto.getSamDtPlatformaPlanUty();
		int DtPoluvagonPlan = planDto.getAndjDtPoluvagonPlanUty() + planDto.getHavDtPoluvagonPlanUty() + planDto.getSamDtPoluvagonPlanUty();
		int DtSisternaPlan = planDto.getAndjDtSisternaPlanUty() + planDto.getHavDtSisternaPlanUty() + planDto.getSamDtSisternaPlanUty();
		int DtBoshqaPlan = planDto.getAndjDtBoshqaPlanUty() + planDto.getHavDtBoshqaPlanUty() + planDto.getSamDtBoshqaPlanUty();

    	model.addAttribute("DtHammaPlan", DtHammaPlan);
    	model.addAttribute("DtKritiPlan", DtKritiPlan);
    	model.addAttribute("DtPlatformaPlan", DtPlatformaPlan);
    	model.addAttribute("DtPoluvagonPlan", DtPoluvagonPlan);
    	model.addAttribute("DtSisternaPlan", DtSisternaPlan);
    	model.addAttribute("DtBoshqaPlan", DtBoshqaPlan);
    	
    	
    	//havos kapital tamir uchun plan
    	int HavKtHammaPlan = planDto.getHavKtKritiPlanUty() + planDto.getHavKtPlatformaPlanUty() + planDto.getHavKtPoluvagonPlanUty() + planDto.getHavKtSisternaPlanUty() + planDto.getHavKtBoshqaPlanUty();
		int HavKtKritiPlan = planDto.getHavKtKritiPlanUty();
		int HavKtPlatformaPlan = planDto.getHavKtPlatformaPlanUty();
		int HavKtPoluvagonPlan = planDto.getHavKtPoluvagonPlanUty();
		int HavKtSisternaPlan = planDto.getHavKtSisternaPlanUty();
		int HavKtBoshqaPlan = planDto.getHavKtBoshqaPlanUty();

		model.addAttribute("HavKtHammaPlan", HavKtHammaPlan);
		model.addAttribute("HavKtKritiPlan", HavKtKritiPlan);
		model.addAttribute("HavKtPlatformaPlan", HavKtPlatformaPlan);
		model.addAttribute("HavKtPoluvagonPlan", HavKtPoluvagonPlan);
		model.addAttribute("HavKtSisternaPlan", HavKtSisternaPlan);
		model.addAttribute("HavKtBoshqaPlan", HavKtBoshqaPlan);
		
    	//VCHD-5 kapital tamir uchun plan
    	int AndjKtHammaPlan = planDto.getAndjKtKritiPlanUty() + planDto.getAndjKtPlatformaPlanUty() + planDto.getAndjKtPoluvagonPlanUty() + planDto.getAndjKtSisternaPlanUty() + planDto.getAndjKtBoshqaPlanUty();
		int AndjKtKritiPlan =  planDto.getAndjKtKritiPlanUty();
		int AndjKtPlatformaPlan =  planDto.getAndjKtPlatformaPlanUty();
		int AndjKtPoluvagonPlan =  planDto.getAndjKtPoluvagonPlanUty();
		int AndjKtSisternaPlan =  planDto.getAndjKtSisternaPlanUty();
		int AndjKtBoshqaPlan =  planDto.getAndjKtBoshqaPlanUty();

		model.addAttribute("AndjKtHammaPlan", AndjKtHammaPlan);
		model.addAttribute("AndjKtKritiPlan", AndjKtKritiPlan);
		model.addAttribute("AndjKtPlatformaPlan", AndjKtPlatformaPlan);
		model.addAttribute("AndjKtPoluvagonPlan", AndjKtPoluvagonPlan);
		model.addAttribute("AndjKtSisternaPlan", AndjKtSisternaPlan);
		model.addAttribute("AndjKtBoshqaPlan", AndjKtBoshqaPlan);
		
		
		//VCHD-6 kapital tamir uchun plan
		int SamKtHammaPlan =  planDto.getSamKtKritiPlanUty() + planDto.getSamKtPlatformaPlanUty() + planDto.getSamKtPoluvagonPlanUty() + planDto.getSamKtSisternaPlanUty() + planDto.getSamKtBoshqaPlanUty();
		int SamKtKritiPlan =  planDto.getSamKtKritiPlanUty();
		int SamKtPlatformaPlan =  planDto.getSamKtPlatformaPlanUty();
		int SamKtPoluvagonPlan =  planDto.getSamKtPoluvagonPlanUty();
		int SamKtSisternaPlan =  planDto.getSamKtSisternaPlanUty();
		int SamKtBoshqaPlan =  planDto.getSamKtBoshqaPlanUty();

		model.addAttribute("SamKtHammaPlan", SamKtHammaPlan);
		model.addAttribute("SamKtKritiPlan", SamKtKritiPlan);
		model.addAttribute("SamKtPlatformaPlan", SamKtPlatformaPlan);
		model.addAttribute("SamKtPoluvagonPlan", SamKtPoluvagonPlan);
		model.addAttribute("SamKtSisternaPlan", SamKtSisternaPlan);
		model.addAttribute("SamKtBoshqaPlan", SamKtBoshqaPlan);

		//kapital itogo
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = planDto.getAndjKtKritiPlanUty() + planDto.getHavKtKritiPlanUty() + planDto.getSamKtKritiPlanUty();
		int KtPlatformaPlan = planDto.getAndjKtPlatformaPlanUty() + planDto.getHavKtPlatformaPlanUty() + planDto.getSamKtPlatformaPlanUty();
		int KtPoluvagonPlan = planDto.getAndjKtPoluvagonPlanUty() + planDto.getHavKtPoluvagonPlanUty() + planDto.getSamKtPoluvagonPlanUty();
		int KtSisternaPlan = planDto.getAndjKtSisternaPlanUty() + planDto.getHavKtSisternaPlanUty() + planDto.getSamKtSisternaPlanUty();
		int KtBoshqaPlan = planDto.getAndjKtBoshqaPlanUty() + planDto.getHavKtBoshqaPlanUty() + planDto.getSamKtBoshqaPlanUty();

		model.addAttribute("KtHammaPlan", KtHammaPlan);
		model.addAttribute("KtKritiPlan", KtKritiPlan);
		model.addAttribute("KtPlatformaPlan", KtPlatformaPlan);
		model.addAttribute("KtPoluvagonPlan", KtPoluvagonPlan);
		model.addAttribute("KtSisternaPlan", KtSisternaPlan);
		model.addAttribute("KtBoshqaPlan", KtBoshqaPlan);
    	
    	//VCHD-3 KRP plan
    	int HavKrpHammaPlan =  planDto.getHavKrpKritiPlanUty() + planDto.getHavKrpPlatformaPlanUty() + planDto.getHavKrpPoluvagonPlanUty() + planDto.getHavKrpSisternaPlanUty() + planDto.getHavKrpBoshqaPlanUty();
		int HavKrpKritiPlan = planDto.getHavKrpKritiPlanUty();
		int HavKrpPlatformaPlan = planDto.getHavKrpPlatformaPlanUty();
		int HavKrpPoluvagonPlan = planDto.getHavKrpPoluvagonPlanUty();
		int HavKrpSisternaPlan = planDto.getHavKrpSisternaPlanUty();
		int HavKrpBoshqaPlan = planDto.getHavKrpBoshqaPlanUty();

		model.addAttribute("HavKrpHammaPlan", HavKrpHammaPlan);
		model.addAttribute("HavKrpKritiPlan", HavKrpKritiPlan);
		model.addAttribute("HavKrpPlatformaPlan", HavKrpPlatformaPlan);
		model.addAttribute("HavKrpPoluvagonPlan", HavKrpPoluvagonPlan);
		model.addAttribute("HavKrpSisternaPlan", HavKrpSisternaPlan);
		model.addAttribute("HavKrpBoshqaPlan", HavKrpBoshqaPlan);

    	//VCHD-5 Krp plan
    	int AndjKrpHammaPlan =  planDto.getAndjKrpKritiPlanUty() + planDto.getAndjKrpPlatformaPlanUty() + planDto.getAndjKrpPoluvagonPlanUty() + planDto.getAndjKrpSisternaPlanUty() + planDto.getAndjKrpBoshqaPlanUty();
		int AndjKrpKritiPlan = planDto.getAndjKrpKritiPlanUty();
		int AndjKrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanUty();
		int AndjKrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanUty();
		int AndjKrpSisternaPlan = planDto.getAndjKrpSisternaPlanUty();
		int AndjKrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanUty();

		model.addAttribute("AndjKrpHammaPlan", AndjKrpHammaPlan);
		model.addAttribute("AndjKrpKritiPlan", AndjKrpKritiPlan);
		model.addAttribute("AndjKrpPlatformaPlan", AndjKrpPlatformaPlan);
		model.addAttribute("AndjKrpPoluvagonPlan", AndjKrpPoluvagonPlan);
		model.addAttribute("AndjKrpSisternaPlan", AndjKrpSisternaPlan);
		model.addAttribute("AndjKrpBoshqaPlan", AndjKrpBoshqaPlan);
		
		//samarqand KRP plan
		int SamKrpHammaPlan = planDto.getSamKrpKritiPlanUty() + planDto.getSamKrpPlatformaPlanUty() + planDto.getSamKrpPoluvagonPlanUty() + planDto.getSamKrpSisternaPlanUty() + planDto.getSamKrpBoshqaPlanUty();
		int SamKrpKritiPlan = planDto.getSamKrpKritiPlanUty();
		int SamKrpPlatformaPlan = planDto.getSamKrpPlatformaPlanUty();
		int SamKrpPoluvagonPlan = planDto.getSamKrpPoluvagonPlanUty();
		int SamKrpSisternaPlan = planDto.getSamKrpSisternaPlanUty();
		int SamKrpBoshqaPlan = planDto.getSamKrpBoshqaPlanUty();

		model.addAttribute("SamKrpHammaPlan", SamKrpHammaPlan);
		model.addAttribute("SamKrpKritiPlan", SamKrpKritiPlan);
		model.addAttribute("SamKrpPlatformaPlan", SamKrpPlatformaPlan);
		model.addAttribute("SamKrpPoluvagonPlan", SamKrpPoluvagonPlan);
		model.addAttribute("SamKrpSisternaPlan", SamKrpSisternaPlan);
		model.addAttribute("SamKrpBoshqaPlan", SamKrpBoshqaPlan);
		
    	//Krp itogo plan
		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = planDto.getAndjKrpKritiPlanUty() + planDto.getHavKrpKritiPlanUty() + planDto.getSamKrpKritiPlanUty();
		int KrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanUty() + planDto.getHavKrpPlatformaPlanUty() + planDto.getSamKrpPlatformaPlanUty();
		int KrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanUty() + planDto.getHavKrpPoluvagonPlanUty() + planDto.getSamKrpPoluvagonPlanUty();
		int KrpSisternaPlan = planDto.getAndjKrpSisternaPlanUty() + planDto.getHavKrpSisternaPlanUty() + planDto.getSamKrpSisternaPlanUty();
		int KrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanUty() + planDto.getHavKrpBoshqaPlanUty() + planDto.getSamKrpBoshqaPlanUty();

		model.addAttribute("KrpHammaPlan", KrpHammaPlan);
		model.addAttribute("KrpKritiPlan", KrpKritiPlan);
		model.addAttribute("KrpPlatformaPlan", KrpPlatformaPlan);
		model.addAttribute("KrpPoluvagonPlan", KrpPoluvagonPlan);
		model.addAttribute("KrpSisternaPlan", KrpSisternaPlan);
		model.addAttribute("KrpBoshqaPlan", KrpBoshqaPlan);


 // factlar 

    	//VCHD-3 uchun depli tamir
		int hdHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		int hdKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy);
		int hdPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)", oy);
		int hdPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy);
		int hdSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)", oy);
		int hdBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		model.addAttribute("hdHamma",hdHamma);
		model.addAttribute("hdKriti", hdKriti);
		model.addAttribute("hdPlatforma", hdPlatforma);
		model.addAttribute("hdPoluvagon", hdPoluvagon);
		model.addAttribute("hdSisterna", hdSisterna);
		model.addAttribute("hdBoshqa", hdBoshqa);
		
		
    	//VCHD-5 uchun depli tamir
		int adHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		int adKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy);
		int adPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)", oy);
		int adPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy);
		int adSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)", oy);
		int adBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		model.addAttribute("adHamma",adHamma);
		model.addAttribute("adKriti", adKriti);
		model.addAttribute("adPlatforma", adPlatforma);
		model.addAttribute("adPoluvagon", adPoluvagon);
		model.addAttribute("adSisterna", adSisterna);
		model.addAttribute("adBoshqa", adBoshqa);

		//samarqand uchun depli tamir
		int sdHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		int sdKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy);
		int sdPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)", oy);
		int sdPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy);
		int sdSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)", oy);
		int sdBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		model.addAttribute("sdHamma",sdHamma);
		model.addAttribute("sdKriti", sdKriti);
		model.addAttribute("sdPlatforma", sdPlatforma);
		model.addAttribute("sdPoluvagon", sdPoluvagon);
		model.addAttribute("sdSisterna", sdSisterna);
		model.addAttribute("sdBoshqa", sdBoshqa);


		// itogo Fact uchun depli tamir
    	 int uvtdhamma = sdHamma + hdHamma + adHamma;
		 int uvtdKriti = sdKriti + hdKriti + adKriti;
		 int uvtdPlatforma = sdPlatforma + adPlatforma + hdPlatforma;
		 int uvtdPoluvagon = sdPoluvagon + hdPoluvagon + adPoluvagon;
		 int uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
		 int uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

		 model.addAttribute("uvtdhamma",uvtdhamma);
    	 model.addAttribute("uvtdKriti",uvtdKriti);
    	 model.addAttribute("uvtdPlatforma",uvtdPlatforma);
    	 model.addAttribute("uvtdPoluvagon",uvtdPoluvagon);
    	 model.addAttribute("uvtdSisterna",uvtdSisterna);
    	 model.addAttribute("uvtdBoshqa",uvtdBoshqa);

     	
     	//VCHD-3 uchun kapital tamir
		int hkHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		int hkKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)"," Kapital ta’mir(КР)", oy);
		int hkPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)", oy);
		int hkPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy);
		int hkSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)", oy);
		int hkBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		model.addAttribute("hkHamma",hkHamma);
		model.addAttribute("hkKriti", hkKriti);
		model.addAttribute("hkPlatforma", hkPlatforma);
		model.addAttribute("hkPoluvagon", hkPoluvagon);
		model.addAttribute("hkSisterna", hkSisterna);
		model.addAttribute("hkBoshqa", hkBoshqa);
		

     	//VCHD-5 uchun kapital tamir
		int akHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		int akKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy);
		int akPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)", oy);
		int akPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy);
		int akSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)", oy);
		int akBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		model.addAttribute("akHamma",akHamma);
		model.addAttribute("akKriti", akKriti);
		model.addAttribute("akPlatforma", akPlatforma);
		model.addAttribute("akPoluvagon", akPoluvagon);
		model.addAttribute("akSisterna", akSisterna);
		model.addAttribute("akBoshqa", akBoshqa);
		
		//samarqand uchun Kapital tamir
		int skHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		int skKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy);
		int skPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)", oy);
		int skPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy);
		int skSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)", oy);
		int skBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		model.addAttribute("skHamma",skHamma);
		model.addAttribute("skKriti", skKriti);
		model.addAttribute("skPlatforma", skPlatforma);
		model.addAttribute("skPoluvagon", skPoluvagon);
		model.addAttribute("skSisterna", skSisterna);
		model.addAttribute("skBoshqa", skBoshqa);

		// itogo Fact uchun kapital tamir
		int uvtkhamma = skHamma + hkHamma + akHamma;
		int uvtkKriti = skKriti + hkKriti + akKriti;
		int uvtkPlatforma = skPlatforma + akPlatforma + hkPlatforma;
		int uvtkPoluvagon = skPoluvagon + hkPoluvagon + akPoluvagon;
		int uvtkSisterna = akSisterna + hkSisterna + skSisterna;
		int uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

		model.addAttribute("uvtkhamma",uvtkhamma);
		model.addAttribute("uvtkKriti",uvtkKriti);
		model.addAttribute("uvtkPlatforma",uvtkPlatforma);
		model.addAttribute("uvtkPoluvagon",uvtkPoluvagon);
		model.addAttribute("uvtkSisterna",uvtkSisterna);
		model.addAttribute("uvtkBoshqa",uvtkBoshqa);

      	//VCHD-3 uchun KRP
		int hkrHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)", oy);

		int hkrKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)"," KRP(КРП)", oy);
		int hkrPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)", oy);
		int hkrPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)", oy);
		int hkrSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)", oy);
		int hkrBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)", oy);

		model.addAttribute("hkrHamma",hkrHamma);
		model.addAttribute("hkrKriti", hkrKriti);
		model.addAttribute("hkrPlatforma", hkrPlatforma);
		model.addAttribute("hkrPoluvagon", hkrPoluvagon);
		model.addAttribute("hkrSisterna", hkrSisterna);
		model.addAttribute("hkrBoshqa", hkrBoshqa);

		//VCHD-5 uchun KRP
		int akrHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)", oy);

		int akrKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)", oy);
		int akrPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)", oy);
		int akrPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)", oy);
		int akrSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)", oy);
		int akrBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)", oy);

		model.addAttribute("akrHamma",akrHamma);
		model.addAttribute("akrKriti", akrKriti);
		model.addAttribute("akrPlatforma", akrPlatforma);
		model.addAttribute("akrPoluvagon", akrPoluvagon);
		model.addAttribute("akrSisterna", akrSisterna);
		model.addAttribute("akrBoshqa", akrBoshqa);

		//samarqand uchun KRP tamir
		int skrHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)", oy);

		int skrKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)", oy);
		int skrPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)", oy);
		int skrPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)", oy);
		int skrSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)", oy);
		int skrBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)", oy);

		model.addAttribute("skrHamma",skrHamma);
		model.addAttribute("skrKriti", skrKriti);
		model.addAttribute("skrPlatforma", skrPlatforma);
		model.addAttribute("skrPoluvagon", skrPoluvagon);
		model.addAttribute("skrSisterna", skrSisterna);
		model.addAttribute("skrBoshqa", skrBoshqa);
		
		// itogo Fact uchun KRP
		int uvtkrhamma = skrHamma + hkrHamma + akrHamma;
		int uvtkrKriti = skrKriti + hkrKriti + akrKriti;
		int uvtkrPlatforma = skrPlatforma + akrPlatforma + hkrPlatforma;
		int uvtkrPoluvagon = skrPoluvagon + hkrPoluvagon + akrPoluvagon;
		int uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
		int uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

		model.addAttribute("uvtkrhamma",uvtkrhamma);
		model.addAttribute("uvtkrKriti",uvtkrKriti);
		model.addAttribute("uvtkrPlatforma",uvtkrPlatforma);
		model.addAttribute("uvtkrPoluvagon",uvtkrPoluvagon);
		model.addAttribute("uvtkrSisterna",uvtkrSisterna);
		model.addAttribute("uvtkrBoshqa",uvtkrBoshqa);


		//yuklab olish uchun list
		List<Integer> vagonsToDownloadTable = new ArrayList<>();
//Depoli tamir
		vagonsToDownloadTable.add(HavDtHammaPlan);
		vagonsToDownloadTable.add(hdHamma);
		vagonsToDownloadTable.add(HavDtKritiPlan);
		vagonsToDownloadTable.add(hdKriti);
		vagonsToDownloadTable.add(HavDtPlatformaPlan);
		vagonsToDownloadTable.add(hdPlatforma);
		vagonsToDownloadTable.add(HavDtPoluvagonPlan);
		vagonsToDownloadTable.add(hdPoluvagon);
		vagonsToDownloadTable.add(HavDtSisternaPlan);
		vagonsToDownloadTable.add(hdSisterna);
		vagonsToDownloadTable.add(HavDtBoshqaPlan);
		vagonsToDownloadTable.add(hdBoshqa);
		
		vagonsToDownloadTable.add(AndjDtHammaPlan);
		vagonsToDownloadTable.add(adHamma);
		vagonsToDownloadTable.add(AndjDtKritiPlan);
		vagonsToDownloadTable.add(adKriti);
		vagonsToDownloadTable.add(AndjDtPlatformaPlan);
		vagonsToDownloadTable.add(adPlatforma);
		vagonsToDownloadTable.add(AndjDtPoluvagonPlan);
		vagonsToDownloadTable.add(adPoluvagon);
		vagonsToDownloadTable.add(AndjDtSisternaPlan);
		vagonsToDownloadTable.add(adSisterna);
		vagonsToDownloadTable.add(AndjDtBoshqaPlan);
		vagonsToDownloadTable.add(adBoshqa);
		
		vagonsToDownloadTable.add(SamDtHammaPlan);
		vagonsToDownloadTable.add(sdHamma);
		vagonsToDownloadTable.add(SamDtKritiPlan);
		vagonsToDownloadTable.add(sdKriti);
		vagonsToDownloadTable.add(SamDtPlatformaPlan);
		vagonsToDownloadTable.add(sdPlatforma);
		vagonsToDownloadTable.add(SamDtPoluvagonPlan);
		vagonsToDownloadTable.add(sdPoluvagon);
		vagonsToDownloadTable.add(SamDtSisternaPlan);
		vagonsToDownloadTable.add(sdSisterna);
		vagonsToDownloadTable.add(SamDtBoshqaPlan);
		vagonsToDownloadTable.add(sdBoshqa);
		
		vagonsToDownloadTable.add(DtHammaPlan);
		vagonsToDownloadTable.add(uvtdhamma);
		vagonsToDownloadTable.add(DtKritiPlan);
		vagonsToDownloadTable.add(uvtdKriti);
		vagonsToDownloadTable.add(DtPlatformaPlan);
		vagonsToDownloadTable.add(uvtdPlatforma);
		vagonsToDownloadTable.add(DtPoluvagonPlan);
		vagonsToDownloadTable.add(uvtdPoluvagon);
		vagonsToDownloadTable.add(DtSisternaPlan);
		vagonsToDownloadTable.add(uvtdSisterna);
		vagonsToDownloadTable.add(DtBoshqaPlan);
		vagonsToDownloadTable.add(uvtdBoshqa);
		
//kapital tamir
		vagonsToDownloadTable.add(HavKtHammaPlan);
		vagonsToDownloadTable.add(hkHamma);
		vagonsToDownloadTable.add(HavKtKritiPlan);
		vagonsToDownloadTable.add(hkKriti);
		vagonsToDownloadTable.add(HavKtPlatformaPlan);
		vagonsToDownloadTable.add(hkPlatforma);
		vagonsToDownloadTable.add(HavKtPoluvagonPlan);
		vagonsToDownloadTable.add(hkPoluvagon);
		vagonsToDownloadTable.add(HavKtSisternaPlan);
		vagonsToDownloadTable.add(hkSisterna);
		vagonsToDownloadTable.add(HavKtBoshqaPlan);
		vagonsToDownloadTable.add(hkBoshqa);
		
		vagonsToDownloadTable.add(AndjKtHammaPlan);
		vagonsToDownloadTable.add(akHamma);
		vagonsToDownloadTable.add(AndjKtKritiPlan);
		vagonsToDownloadTable.add(akKriti);
		vagonsToDownloadTable.add(AndjKtPlatformaPlan);
		vagonsToDownloadTable.add(akPlatforma);
		vagonsToDownloadTable.add(AndjKtPoluvagonPlan);
		vagonsToDownloadTable.add(akPoluvagon);
		vagonsToDownloadTable.add(AndjKtSisternaPlan);
		vagonsToDownloadTable.add(akSisterna);
		vagonsToDownloadTable.add(AndjKtBoshqaPlan);
		vagonsToDownloadTable.add(akBoshqa);
		
		vagonsToDownloadTable.add(SamKtHammaPlan);
		vagonsToDownloadTable.add(skHamma);
		vagonsToDownloadTable.add(SamKtKritiPlan);
		vagonsToDownloadTable.add(skKriti);
		vagonsToDownloadTable.add(SamKtPlatformaPlan);
		vagonsToDownloadTable.add(skPlatforma);
		vagonsToDownloadTable.add(SamKtPoluvagonPlan);
		vagonsToDownloadTable.add(skPoluvagon);
		vagonsToDownloadTable.add(SamKtSisternaPlan);
		vagonsToDownloadTable.add(skSisterna);
		vagonsToDownloadTable.add(SamKtBoshqaPlan);
		vagonsToDownloadTable.add(skBoshqa);
		
		vagonsToDownloadTable.add(KtHammaPlan);
		vagonsToDownloadTable.add(uvtkhamma);
		vagonsToDownloadTable.add(KtKritiPlan);
		vagonsToDownloadTable.add(uvtkKriti);
		vagonsToDownloadTable.add(KtPlatformaPlan);
		vagonsToDownloadTable.add(uvtkPlatforma);
		vagonsToDownloadTable.add(KtPoluvagonPlan);
		vagonsToDownloadTable.add(uvtkPoluvagon);
		vagonsToDownloadTable.add(KtSisternaPlan);
		vagonsToDownloadTable.add(uvtkSisterna);
		vagonsToDownloadTable.add(KtBoshqaPlan);
		vagonsToDownloadTable.add(uvtkBoshqa);
//krp
		vagonsToDownloadTable.add(HavKrpHammaPlan);
		vagonsToDownloadTable.add(hkrHamma);
		vagonsToDownloadTable.add(HavKrpKritiPlan);
		vagonsToDownloadTable.add(hkrKriti);
		vagonsToDownloadTable.add(HavKrpPlatformaPlan);
		vagonsToDownloadTable.add(hkrPlatforma);
		vagonsToDownloadTable.add(HavKrpPoluvagonPlan);
		vagonsToDownloadTable.add(hkrPoluvagon);
		vagonsToDownloadTable.add(HavKrpSisternaPlan);
		vagonsToDownloadTable.add(hkrSisterna);
		vagonsToDownloadTable.add(HavKrpBoshqaPlan);
		vagonsToDownloadTable.add(hkrBoshqa);
		
		vagonsToDownloadTable.add(AndjKrpHammaPlan);
		vagonsToDownloadTable.add(akrHamma);
		vagonsToDownloadTable.add(AndjKrpKritiPlan);
		vagonsToDownloadTable.add(akrKriti);
		vagonsToDownloadTable.add(AndjKrpPlatformaPlan);
		vagonsToDownloadTable.add(akrPlatforma);
		vagonsToDownloadTable.add(AndjKrpPoluvagonPlan);
		vagonsToDownloadTable.add(akrPoluvagon);
		vagonsToDownloadTable.add(AndjKrpSisternaPlan);
		vagonsToDownloadTable.add(akrSisterna);
		vagonsToDownloadTable.add(AndjKrpBoshqaPlan);
		vagonsToDownloadTable.add(akrBoshqa);
		
		vagonsToDownloadTable.add(SamKrpHammaPlan);
		vagonsToDownloadTable.add(skrHamma);
		vagonsToDownloadTable.add(SamKrpKritiPlan);
		vagonsToDownloadTable.add(skrKriti);
		vagonsToDownloadTable.add(SamKrpPlatformaPlan);
		vagonsToDownloadTable.add(skrPlatforma);
		vagonsToDownloadTable.add(SamKrpPoluvagonPlan);
		vagonsToDownloadTable.add(skrPoluvagon);
		vagonsToDownloadTable.add(SamKrpSisternaPlan);
		vagonsToDownloadTable.add(skrSisterna);
		vagonsToDownloadTable.add(SamKrpBoshqaPlan);
		vagonsToDownloadTable.add(skrBoshqa);

		vagonsToDownloadTable.add(KrpHammaPlan);
		vagonsToDownloadTable.add(uvtkrhamma);
		vagonsToDownloadTable.add(KrpKritiPlan);
		vagonsToDownloadTable.add(uvtkrKriti);
		vagonsToDownloadTable.add(KrpPlatformaPlan);
		vagonsToDownloadTable.add(uvtkrPlatforma);
		vagonsToDownloadTable.add(KrpPoluvagonPlan);
		vagonsToDownloadTable.add(uvtkrPoluvagon);
		vagonsToDownloadTable.add(KrpSisternaPlan);
		vagonsToDownloadTable.add(uvtkrSisterna);
		vagonsToDownloadTable.add(KrpBoshqaPlan);
		vagonsToDownloadTable.add(uvtkrBoshqa);

		vagonsToDownloadAllTable = vagonsToDownloadTable;

		return "AllPlanTableUty";
	}

	
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
   	@GetMapping("/vagons/planTableForMonthsUty")
   	public String getPlanForAllMonths(Model model) {

		PlanUty planDto = vagonTayyorUtyService.getPlanuty();
    	//planlar kiritish
	    
    	//samarqand depo tamir plan
    	int sdKriti = planDto.getSamDtKritiPlanUtyMonths();
		int sdPlatforma=planDto.getSamDtPlatformaPlanUtyMonths();
		int sdPoluvagon=planDto.getSamDtPoluvagonPlanUtyMonths();
		int sdSisterna=planDto.getSamDtSisternaPlanUtyMonths();
		int sdBoshqa=planDto.getSamDtBoshqaPlanUtyMonths();
		int SamDtHammaPlan=sdKriti+sdPlatforma+sdPoluvagon+sdSisterna+sdBoshqa;

    	model.addAttribute("SamDtHammaPlan",SamDtHammaPlan);
    	model.addAttribute("SamDtKritiPlan", sdKriti);
    	model.addAttribute("SamDtPlatformaPlan", sdPlatforma);
    	model.addAttribute("SamDtPoluvagonPlan", sdPoluvagon);
    	model.addAttribute("SamDtSisternaPlan", sdSisterna);
    	model.addAttribute("SamDtBoshqaPlan", sdBoshqa);
    	
    	//havos depo tamir hamma plan
		int hdKriti = planDto.getHavDtKritiPlanUtyMonths();
		int hdPlatforma=planDto.getHavDtPlatformaPlanUtyMonths();
		int hdPoluvagon=planDto.getHavDtPoluvagonPlanUtyMonths();
		int hdSisterna=planDto.getHavDtSisternaPlanUtyMonths();
		int hdBoshqa=planDto.getHavDtBoshqaPlanUtyMonths();
		int HavDtHammaPlan = hdKriti + hdPlatforma + hdPoluvagon + hdSisterna + hdBoshqa;
    	
    	model.addAttribute("HavDtHammaPlan", HavDtHammaPlan);
    	model.addAttribute("HavDtKritiPlan", hdKriti);
    	model.addAttribute("HavDtPlatformaPlan", hdPlatforma);
    	model.addAttribute("HavDtPoluvagonPlan", hdPoluvagon);
    	model.addAttribute("HavDtSisternaPlan", hdSisterna);
    	model.addAttribute("HavDtBoshqaPlan", hdBoshqa);


    	
    	//VCHD-5 depo tamir plan
		int adKriti = planDto.getAndjDtKritiPlanUtyMonths();
		int adPlatforma=planDto.getAndjDtPlatformaPlanUtyMonths();
		int adPoluvagon=planDto.getAndjDtPoluvagonPlanUtyMonths();
		int adSisterna=planDto.getAndjDtSisternaPlanUtyMonths();
		int adBoshqa=planDto.getAndjDtBoshqaPlanUtyMonths();
		int AndjDtHammaPlan = adKriti + adPlatforma + adPoluvagon + adSisterna + adBoshqa;
    	
    	model.addAttribute("AndjDtHammaPlan", AndjDtHammaPlan);
    	model.addAttribute("AndjDtKritiPlan", adKriti);
    	model.addAttribute("AndjDtPlatformaPlan",adPlatforma);
    	model.addAttribute("AndjDtPoluvagonPlan", adPoluvagon);
    	model.addAttribute("AndjDtSisternaPlan", adSisterna);
    	model.addAttribute("AndjDtBoshqaPlan", adBoshqa);
    	
    	// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = sdKriti + hdKriti + adKriti;
		int DtPlatformaPlan = sdPlatforma + hdPlatforma + adPlatforma;
		int DtPoluvagonPlan = sdPoluvagon + hdPoluvagon + adPoluvagon;
		int DtSisternaPlan = sdSisterna + hdSisterna + adSisterna;
		int DtBoshqaPlan = sdBoshqa + hdBoshqa + adBoshqa;

    	model.addAttribute("DtHammaPlan", DtHammaPlan);
    	model.addAttribute("DtKritiPlan", DtKritiPlan);
    	model.addAttribute("DtPlatformaPlan", DtPlatformaPlan);
    	model.addAttribute("DtPoluvagonPlan", DtPoluvagonPlan);
    	model.addAttribute("DtSisternaPlan", DtSisternaPlan);
    	model.addAttribute("DtBoshqaPlan", DtBoshqaPlan);
    	
    	//Samrqand kapital plan
		int skKriti = planDto.getSamKtKritiPlanUtyMonths();
		int skPlatforma=planDto.getSamKtPlatformaPlanUtyMonths();
		int skPoluvagon=planDto.getSamKtPoluvagonPlanUtyMonths();
		int skSisterna=planDto.getSamKtSisternaPlanUtyMonths();
		int skBoshqa=planDto.getSamKtBoshqaPlanUtyMonths();
		int SamKtHammaPlan=skKriti+skPlatforma+skPoluvagon+skSisterna+skBoshqa;
    	
    	model.addAttribute("SamKtHammaPlan",SamKtHammaPlan);
    	model.addAttribute("SamKtKritiPlan", skKriti);
    	model.addAttribute("SamKtPlatformaPlan", skPlatforma);
    	model.addAttribute("SamKtPoluvagonPlan", skPoluvagon);
    	model.addAttribute("SamKtSisternaPlan", skSisterna);
    	model.addAttribute("SamKtBoshqaPlan", skBoshqa);
    	
    	//hovos kapital plan
    	int hkKriti = planDto.getHavKtKritiPlanUtyMonths();
		int hkPlatforma=planDto.getHavKtPlatformaPlanUtyMonths();
		int hkPoluvagon=planDto.getHavKtPoluvagonPlanUtyMonths();
		int hkSisterna=planDto.getHavKtSisternaPlanUtyMonths();
		int hkBoshqa=planDto.getHavKtBoshqaPlanUtyMonths();
		int HavKtHammaPlan = hkKriti + hkPlatforma + hkPoluvagon + hkSisterna + hkBoshqa;
    	
    	model.addAttribute("HavKtHammaPlan", HavKtHammaPlan);
    	model.addAttribute("HavKtKritiPlan", hkKriti);
    	model.addAttribute("HavKtPlatformaPlan", hkPlatforma);
    	model.addAttribute("HavKtPoluvagonPlan", hkPoluvagon);
    	model.addAttribute("HavKtSisternaPlan", hkSisterna);
    	model.addAttribute("HavKtBoshqaPlan", hkBoshqa);
    	
    	//ANDIJON kapital plan
		int akKriti = planDto.getAndjKtKritiPlanUtyMonths();
		int akPlatforma=planDto.getAndjKtPlatformaPlanUtyMonths();
		int akPoluvagon=planDto.getAndjKtPoluvagonPlanUtyMonths();
		int akSisterna=planDto.getAndjKtSisternaPlanUtyMonths();
		int akBoshqa=planDto.getAndjKtBoshqaPlanUtyMonths();
		int AndjKtHammaPlan = akKriti + akPlatforma + akPoluvagon + akSisterna + akBoshqa;
    	
    	
    	model.addAttribute("AndjKtHammaPlan", AndjKtHammaPlan);
    	model.addAttribute("AndjKtKritiPlan", akKriti);
    	model.addAttribute("AndjKtPlatformaPlan", akPlatforma);
    	model.addAttribute("AndjKtPoluvagonPlan", akPoluvagon);
    	model.addAttribute("AndjKtSisternaPlan", akSisterna);
    	model.addAttribute("AndjKtBoshqaPlan", akBoshqa);
    	
    	//Itogo kapital plan
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = skKriti + hkKriti + akKriti;
		int KtPlatformaPlan = skPlatforma + hkPlatforma + akPlatforma;
		int KtPoluvagonPlan =skPoluvagon + hkPoluvagon + akPoluvagon;
		int KtSisternaPlan = skSisterna + hkSisterna + akSisterna;
		int KtBoshqaPlan = skBoshqa + hkBoshqa + akBoshqa;

    	model.addAttribute("KtHammaPlan", KtHammaPlan);
    	model.addAttribute("KtKritiPlan", KtKritiPlan);
    	model.addAttribute("KtPlatformaPlan", KtPlatformaPlan);
    	model.addAttribute("KtPoluvagonPlan", KtPoluvagonPlan);
    	model.addAttribute("KtSisternaPlan", KtSisternaPlan);
    	model.addAttribute("KtBoshqaPlan", KtBoshqaPlan);
  
    	//Samarqankr Krp plan
		int skrKriti = planDto.getSamKrpKritiPlanUtyMonths();
		int skrPlatforma=planDto.getSamKrpPlatformaPlanUtyMonths();
		int skrPoluvagon=planDto.getSamKrpPoluvagonPlanUtyMonths();
		int skrSisterna=planDto.getSamKrpSisternaPlanUtyMonths();
		int skrBoshqa=planDto.getSamKrpBoshqaPlanUtyMonths();
		int SamKrpHammaPlan=skrKriti+skrPlatforma+skrPoluvagon+skrSisterna+skrBoshqa;
    	
    	model.addAttribute("SamKrpHammaPlan", SamKrpHammaPlan);
    	model.addAttribute("SamKrpKritiPlan", skrKriti);
    	model.addAttribute("SamKrpPlatformaPlan", skrPlatforma);
    	model.addAttribute("SamKrpPoluvagonPlan", skrPoluvagon);
    	model.addAttribute("SamKrpSisternaPlan", skrSisterna);
    	model.addAttribute("SamKrpBoshqaPlan", skrBoshqa);
    	
    	//Hovos krp plan
    	int hkrKriti = planDto.getHavKrpKritiPlanUtyMonths();
		int hkrPlatforma=planDto.getHavKrpPlatformaPlanUtyMonths();
		int hkrPoluvagon=planDto.getHavKrpPoluvagonPlanUtyMonths();
		int hkrSisterna=planDto.getHavKrpSisternaPlanUtyMonths();
		int hkrBoshqa=planDto.getHavKrpBoshqaPlanUtyMonths();
		int HavKrpHammaPlan = hkrKriti + hkrPlatforma + hkrPoluvagon + hkrSisterna + hkrBoshqa;
    	
    	model.addAttribute("HavKrpHammaPlan",HavKrpHammaPlan);
    	model.addAttribute("HavKrpKritiPlan", hkrKriti);
    	model.addAttribute("HavKrpPlatformaPlan", hkrPlatforma);
    	model.addAttribute("HavKrpPoluvagonPlan", hkrPoluvagon);
    	model.addAttribute("HavKrpSisternaPlan", hkrSisterna);
    	model.addAttribute("HavKrpBoshqaPlan", hkrBoshqa);
    	
    	//andijon krp plan
		int akrKriti = planDto.getAndjKrpKritiPlanUtyMonths();
		int akrPlatforma=planDto.getAndjKrpPlatformaPlanUtyMonths();
		int akrPoluvagon=planDto.getAndjKrpPoluvagonPlanUtyMonths();
		int akrSisterna=planDto.getAndjKrpSisternaPlanUtyMonths();
		int akrBoshqa=planDto.getAndjKrpBoshqaPlanUtyMonths();
		int AndjKrpHammaPlan = akrKriti + akrPlatforma + akrPoluvagon + akrSisterna + akrBoshqa;
    	
    	model.addAttribute("AndjKrpHammaPlan",AndjKrpHammaPlan);
    	model.addAttribute("AndjKrpKritiPlan", akrKriti);
    	model.addAttribute("AndjKrpPlatformaPlan", akrPlatforma);
    	model.addAttribute("AndjKrpPoluvagonPlan", akrPoluvagon);
    	model.addAttribute("AndjKrpSisternaPlan", akrSisterna);
    	model.addAttribute("AndjKrpBoshqaPlan", akrBoshqa);
    	
    	//itogo krp
		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = skrKriti + hkrKriti + akrKriti;
		int KrpPlatformaPlan = skrPlatforma + hkrPlatforma + akrPlatforma;
		int KrpPoluvagonPlan = akrPoluvagon + hkrPoluvagon + skrPoluvagon;
		int KrpSisternaPlan = skrSisterna + hkrSisterna + akrSisterna;
		int KrpBoshqaPlan = skrBoshqa + hkrBoshqa + akrBoshqa;

    	model.addAttribute("KrpHammaPlan", KrpHammaPlan);
    	model.addAttribute("KrpKritiPlan", KrpKritiPlan);
    	model.addAttribute("KrpPlatformaPlan", KrpPlatformaPlan);
    	model.addAttribute("KrpPoluvagonPlan",KrpPoluvagonPlan);
    	model.addAttribute("KrpSisternaPlan", KrpSisternaPlan);
    	model.addAttribute("KrpBoshqaPlan", KrpBoshqaPlan);

    	//**//

    	// VCHD-3 depo tamir hamma false vagonlar soni
    	int hdKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)");
    	int hdPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)");
    	int hdPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)");
    	int hdSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)");
    	int hdBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)");
    	int hdHammaFalse = hdKritiFalse + hdPlatformaFalse+ hdPoluvagonFalse+ hdSisternaFalse + hdBoshqaFalse;
    	
    	model.addAttribute("hdHammaFalse",hdHammaFalse+455);
    	model.addAttribute("hdKritiFalse",hdKritiFalse);
    	model.addAttribute("hdPlatformaFalse",hdPlatformaFalse+26);
    	model.addAttribute("hdPoluvagonFalse",hdPoluvagonFalse+109);
    	model.addAttribute("hdSisternaFalse",hdSisternaFalse+4);
    	model.addAttribute("hdBoshqaFalse",hdBoshqaFalse+316);
    	
    	// VCHD-5 depo tamir hamma false vagonlar soni
    	int adKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)");
    	int adPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)");
    	int adPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)");
    	int adSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)");
    	int adBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)");
    	int adHammaFalse = adKritiFalse + adPlatformaFalse+ adPoluvagonFalse+ adSisternaFalse + adBoshqaFalse;
    	
    	model.addAttribute("adHammaFalse",adHammaFalse+443);
    	model.addAttribute("adKritiFalse",adKritiFalse+224);
    	model.addAttribute("adPlatformaFalse",adPlatformaFalse+3);
    	model.addAttribute("adPoluvagonFalse",adPoluvagonFalse+103);
    	model.addAttribute("adSisternaFalse",adSisternaFalse);
    	model.addAttribute("adBoshqaFalse",adBoshqaFalse+113);

		// samarqand depo tamir hamma false vagonlar soni
		int sdKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)");
		int sdPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)");
		int sdPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)");
		int sdSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)");
		int sdBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)");
		int sdHammaFalse = sdKritiFalse + sdPlatformaFalse+ sdPoluvagonFalse+ sdSisternaFalse + sdBoshqaFalse;

		model.addAttribute("sdHammaFalse",sdHammaFalse+334);
		model.addAttribute("sdKritiFalse",sdKritiFalse+134);
		model.addAttribute("sdPlatformaFalse",sdPlatformaFalse+6);
		model.addAttribute("sdPoluvagonFalse",sdPoluvagonFalse+65);
		model.addAttribute("sdSisternaFalse",sdSisternaFalse);
		model.addAttribute("sdBoshqaFalse",sdBoshqaFalse+129);

		// depoli tamir itogo uchun
    	int dHammaFalse =  adHammaFalse + hdHammaFalse+sdHammaFalse;
    	int dKritiFalse = sdKritiFalse + hdKritiFalse + adKritiFalse;
    	int dPlatforma = adPlatformaFalse + sdPlatformaFalse + hdPlatformaFalse;
    	int dPoluvagon  = adPoluvagonFalse + sdPoluvagonFalse + hdPoluvagonFalse;
    	int dSisterna = adSisternaFalse + hdSisternaFalse + sdSisternaFalse;
    	int dBoshqa = adBoshqaFalse + hdBoshqaFalse + sdBoshqaFalse;
    	
    	model.addAttribute("dHammaFalse",dHammaFalse+1232);
    	model.addAttribute("dKritiFalse",dKritiFalse+358);
    	model.addAttribute("dPlatforma",dPlatforma+35);
    	model.addAttribute("dPoluvagon",dPoluvagon+277);
    	model.addAttribute("dSisterna",dSisterna+4);
    	model.addAttribute("dBoshqa",dBoshqa+558);
    	
    	
    	// samarqand KApital tamir hamma false vagonlar soni
    	int skKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)");
    	int skPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)");
    	int skPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)");
    	int skSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)");
    	int skBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)");
    	int skHammaFalse = skKritiFalse + skPlatformaFalse+ skPoluvagonFalse+ skSisternaFalse + skBoshqaFalse;
    	
    	model.addAttribute("skHammaFalse",skHammaFalse+16);
    	model.addAttribute("skKritiFalse",skKritiFalse+16);
    	model.addAttribute("skPlatformaFalse",skPlatformaFalse);
    	model.addAttribute("skPoluvagonFalse",skPoluvagonFalse);
    	model.addAttribute("skSisternaFalse",skSisternaFalse);
    	model.addAttribute("skBoshqaFalse",skBoshqaFalse);
    	
    	// VCHD-3 kapital tamir hamma false vagonlar soni
    	int hkKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Kapital ta’mir(КР)");
    	int hkPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)");
    	int hkPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)");
    	int hkSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)");
    	int hkBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)");
    	int hkHammaFalse = hkKritiFalse + hkPlatformaFalse+ hkPoluvagonFalse+ hkSisternaFalse + hkBoshqaFalse;
    	
    	model.addAttribute("hkHammaFalse",hkHammaFalse+7);
    	model.addAttribute("hkKritiFalse",hkKritiFalse);
    	model.addAttribute("hkPlatformaFalse",hkPlatformaFalse);
    	model.addAttribute("hkPoluvagonFalse",hkPoluvagonFalse+4);
    	model.addAttribute("hkSisternaFalse",hkSisternaFalse);
    	model.addAttribute("hkBoshqaFalse",hkBoshqaFalse+3);
    	
    	// VCHD-5 kapital tamir hamma false vagonlar soni
    	int akKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)");
    	int akPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)");
    	int akPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)");
    	int akSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)");
    	int akBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)");
    	int akHammaFalse = akKritiFalse + akPlatformaFalse+ akPoluvagonFalse+ akSisternaFalse + akBoshqaFalse;
    	
    	model.addAttribute("akHammaFalse",akHammaFalse+28);
    	model.addAttribute("akKritiFalse",akKritiFalse+26);
    	model.addAttribute("akPlatformaFalse",akPlatformaFalse);
    	model.addAttribute("akPoluvagonFalse",akPoluvagonFalse+2);
    	model.addAttribute("akSisternaFalse",akSisternaFalse);
    	model.addAttribute("akBoshqaFalse",akBoshqaFalse);
    	
    	// Kapital tamir itogo uchun
    	int kHammaFalse =  akHammaFalse + hkHammaFalse+skHammaFalse;
    	int kKritiFalse = skKritiFalse + hkKritiFalse + akKritiFalse;
    	int kPlatforma = akPlatformaFalse + skPlatformaFalse + hkPlatformaFalse;
    	int kPoluvagon  = akPoluvagonFalse + skPoluvagonFalse + hkPoluvagonFalse;
    	int kSisterna = akSisternaFalse + hkSisternaFalse + skSisternaFalse;
    	int kBoshqa = akBoshqaFalse + hkBoshqaFalse + skBoshqaFalse;
    	
    	model.addAttribute("kHammaFalse",kHammaFalse+51);
    	model.addAttribute("kKritiFalse",kKritiFalse+42);
    	model.addAttribute("kPlatforma",kPlatforma);
    	model.addAttribute("kPoluvagon",kPoluvagon+6);
    	model.addAttribute("kSisterna",kSisterna);
    	model.addAttribute("kBoshqa",kBoshqa+3);
    	
    	//**
    	// samarqand KRP tamir hamma false vagonlar soni
    	int skrKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)");
    	int skrPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)");
    	int skrPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)");
    	int skrSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)");
    	int skrBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)");
    	int skrHammaFalse = skrKritiFalse + skrPlatformaFalse+ skrPoluvagonFalse+ skrSisternaFalse + skrBoshqaFalse;
    	
    	model.addAttribute("skrHammaFalse",skrHammaFalse+89);
    	model.addAttribute("skrKritiFalse",skrKritiFalse);
    	model.addAttribute("skrPlatformaFalse",skrPlatformaFalse);
    	model.addAttribute("skrPoluvagonFalse",skrPoluvagonFalse+88);
    	model.addAttribute("skrSisternaFalse",skrSisternaFalse+1);
    	model.addAttribute("skrBoshqaFalse",skrBoshqaFalse);
    	
    	// VCHD-3 KRP tamir hamma false vagonlar soni
    	int hkrKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","KRP(КРП)");
    	int hkrPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)");
    	int hkrPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)");
    	int hkrSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)");
    	int hkrBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)");
    	int hkrHammaFalse = hkrKritiFalse + hkrPlatformaFalse+ hkrPoluvagonFalse+ hkrSisternaFalse + hkrBoshqaFalse;
    	
    	model.addAttribute("hkrHammaFalse",hkrHammaFalse+83);
    	model.addAttribute("hkrKritiFalse",hkrKritiFalse);
    	model.addAttribute("hkrPlatformaFalse",hkrPlatformaFalse);
    	model.addAttribute("hkrPoluvagonFalse",hkrPoluvagonFalse+83);
    	model.addAttribute("hkrSisternaFalse",hkrSisternaFalse);
    	model.addAttribute("hkrBoshqaFalse",hkrBoshqaFalse);
    	
    	// VCHD-5 KRP tamir hamma false vagonlar soni
    	int akrKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)");
    	int akrPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)");
    	int akrPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)");
    	int akrSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)");
    	int akrBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)");
    	int akrHammaFalse = akrKritiFalse + akrPlatformaFalse+ akrPoluvagonFalse+ akrSisternaFalse + akrBoshqaFalse;
    	
    	model.addAttribute("akrHammaFalse",akrHammaFalse+61);
    	model.addAttribute("akrKritiFalse",akrKritiFalse);
    	model.addAttribute("akrPlatformaFalse",akrPlatformaFalse);
    	model.addAttribute("akrPoluvagonFalse",akrPoluvagonFalse+61);
    	model.addAttribute("akrSisternaFalse",akrSisternaFalse);
    	model.addAttribute("akBoshqaFalse",akBoshqaFalse);
    	
    	// Krp itogo uchun
    	int krHammaFalse =  akrHammaFalse + hkrHammaFalse+skrHammaFalse;
    	int krKritiFalse = skrKritiFalse + hkrKritiFalse + akrKritiFalse;
    	int krPlatforma = akrPlatformaFalse + skrPlatformaFalse + hkrPlatformaFalse;
    	int krPoluvagon  = akrPoluvagonFalse + skrPoluvagonFalse + hkrPoluvagonFalse;
    	int krSisterna = akrSisternaFalse + hkrSisternaFalse + skrSisternaFalse;
    	int krBoshqa = akrBoshqaFalse + hkrBoshqaFalse + skrBoshqaFalse;
    	
    	model.addAttribute("krHammaFalse",krHammaFalse+233);
    	model.addAttribute("krKritiFalse",krKritiFalse);
    	model.addAttribute("krPlatforma",krPlatforma);
    	model.addAttribute("krPoluvagon",krPoluvagon+232);
    	model.addAttribute("krSisterna",krSisterna+1);
    	model.addAttribute("krBoshqa",krBoshqa);
    	
    	// hamma false vagonlarni list qilib chiqarish
    	vagonsToDownload = vagonTayyorUtyService.findAll();
    	model.addAttribute("vagons", vagonTayyorUtyService.findAll());

		//yuklab olish uchun list
		List<Integer> vagonsToDownloadTable = new ArrayList<>();

		//Depoli tamir
		vagonsToDownloadTable.add(HavDtHammaPlan);
		vagonsToDownloadTable.add(hdHammaFalse + 455);
		vagonsToDownloadTable.add(hdKriti);
		vagonsToDownloadTable.add(hdKritiFalse);
		vagonsToDownloadTable.add(hdPlatforma);
		vagonsToDownloadTable.add(hdPlatformaFalse + 26);
		vagonsToDownloadTable.add(hdPoluvagon);
		vagonsToDownloadTable.add(hdPoluvagonFalse + 109);
		vagonsToDownloadTable.add(hdSisterna);
		vagonsToDownloadTable.add(hdSisternaFalse + 4);
		vagonsToDownloadTable.add(hdBoshqa);
		vagonsToDownloadTable.add(hdBoshqaFalse + 316);
		
		vagonsToDownloadTable.add(AndjDtHammaPlan);
		vagonsToDownloadTable.add(adHammaFalse + 443);
		vagonsToDownloadTable.add(adKriti);
		vagonsToDownloadTable.add(adKritiFalse + 224);
		vagonsToDownloadTable.add(adPlatforma);
		vagonsToDownloadTable.add(adPlatformaFalse + 3);
		vagonsToDownloadTable.add(adPoluvagon);
		vagonsToDownloadTable.add(adPoluvagonFalse + 103);
		vagonsToDownloadTable.add(adSisterna);
		vagonsToDownloadTable.add(adSisternaFalse);
		vagonsToDownloadTable.add(adBoshqa);
		vagonsToDownloadTable.add(adBoshqaFalse + 113);
		
		vagonsToDownloadTable.add(SamDtHammaPlan);
		vagonsToDownloadTable.add(sdHammaFalse + 334);
		vagonsToDownloadTable.add(sdKriti);
		vagonsToDownloadTable.add(sdKritiFalse + 134);
		vagonsToDownloadTable.add(sdPlatforma);
		vagonsToDownloadTable.add(sdPlatformaFalse + 6);
		vagonsToDownloadTable.add(sdPoluvagon);
		vagonsToDownloadTable.add(sdPoluvagonFalse + 65);
		vagonsToDownloadTable.add(sdSisterna);
		vagonsToDownloadTable.add(sdSisternaFalse );
		vagonsToDownloadTable.add(sdBoshqa);
		vagonsToDownloadTable.add(sdBoshqaFalse + 129);;
		
		vagonsToDownloadTable.add(DtHammaPlan);
		vagonsToDownloadTable.add(dHammaFalse+1232);
		vagonsToDownloadTable.add(DtKritiPlan);
		vagonsToDownloadTable.add(dKritiFalse+358);
		vagonsToDownloadTable.add(DtPlatformaPlan);
		vagonsToDownloadTable.add(dPlatforma+35);
		vagonsToDownloadTable.add(DtPoluvagonPlan);
		vagonsToDownloadTable.add(dPoluvagon+277);
		vagonsToDownloadTable.add(DtSisternaPlan);
		vagonsToDownloadTable.add(dSisterna+4 );
		vagonsToDownloadTable.add(DtBoshqaPlan);
		vagonsToDownloadTable.add(dBoshqa+558);
		
		//kapital tamir
		vagonsToDownloadTable.add(HavKtHammaPlan);
		vagonsToDownloadTable.add(hkHammaFalse + 7);
		vagonsToDownloadTable.add(hkKriti);
		vagonsToDownloadTable.add(hkKritiFalse);
		vagonsToDownloadTable.add(hkPlatforma);
		vagonsToDownloadTable.add(hkPlatformaFalse);
		vagonsToDownloadTable.add(hkPoluvagon);
		vagonsToDownloadTable.add(hkPoluvagonFalse + 4);
		vagonsToDownloadTable.add(hkSisterna);
		vagonsToDownloadTable.add(hkSisternaFalse);
		vagonsToDownloadTable.add(hkBoshqa);
		vagonsToDownloadTable.add(hkBoshqaFalse + 3);

		vagonsToDownloadTable.add(AndjKtHammaPlan);
		vagonsToDownloadTable.add(akHammaFalse + 28);
		vagonsToDownloadTable.add(akKriti);
		vagonsToDownloadTable.add(akKritiFalse + 26);
		vagonsToDownloadTable.add(akPlatforma);
		vagonsToDownloadTable.add(akPlatformaFalse);
		vagonsToDownloadTable.add(akPoluvagon);
		vagonsToDownloadTable.add(akPoluvagonFalse + 2);
		vagonsToDownloadTable.add(akSisterna);
		vagonsToDownloadTable.add(akSisternaFalse);
		vagonsToDownloadTable.add(akBoshqa);
		vagonsToDownloadTable.add(akBoshqaFalse);

		vagonsToDownloadTable.add(SamKtHammaPlan);
		vagonsToDownloadTable.add(skHammaFalse + 16);
		vagonsToDownloadTable.add(skKriti);
		vagonsToDownloadTable.add(skKritiFalse + 16);
		vagonsToDownloadTable.add(skPlatforma);
		vagonsToDownloadTable.add(skPlatformaFalse);
		vagonsToDownloadTable.add(skPoluvagon);
		vagonsToDownloadTable.add(skPoluvagonFalse);
		vagonsToDownloadTable.add(skSisterna);
		vagonsToDownloadTable.add(skSisternaFalse );
		vagonsToDownloadTable.add(skBoshqa);
		vagonsToDownloadTable.add(skBoshqaFalse);

		vagonsToDownloadTable.add(KtHammaPlan);
		vagonsToDownloadTable.add(kHammaFalse + 51);
		vagonsToDownloadTable.add(KtKritiPlan);
		vagonsToDownloadTable.add(kKritiFalse + 42);
		vagonsToDownloadTable.add(KtPlatformaPlan);
		vagonsToDownloadTable.add(kPlatforma);
		vagonsToDownloadTable.add(KtPoluvagonPlan);
		vagonsToDownloadTable.add(kPoluvagon + 6);
		vagonsToDownloadTable.add(KtSisternaPlan);
		vagonsToDownloadTable.add(kSisterna);
		vagonsToDownloadTable.add(KtBoshqaPlan);
		vagonsToDownloadTable.add(kBoshqa + 3);
		
		//KRPP
		vagonsToDownloadTable.add(HavKrpHammaPlan);
		vagonsToDownloadTable.add(hkrHammaFalse + 83);
		vagonsToDownloadTable.add(hkrKriti);
		vagonsToDownloadTable.add(hkrKritiFalse);
		vagonsToDownloadTable.add(hkrPlatforma);
		vagonsToDownloadTable.add(hkrPlatformaFalse);
		vagonsToDownloadTable.add(hkrPoluvagon);
		vagonsToDownloadTable.add(hkrPoluvagonFalse + 83);
		vagonsToDownloadTable.add(hkrSisterna);
		vagonsToDownloadTable.add(hkrSisternaFalse);
		vagonsToDownloadTable.add(hkrBoshqa);
		vagonsToDownloadTable.add(hkrBoshqaFalse);

		vagonsToDownloadTable.add(AndjKrpHammaPlan);
		vagonsToDownloadTable.add(akrHammaFalse + 61);
		vagonsToDownloadTable.add(akrKriti);
		vagonsToDownloadTable.add(akrKritiFalse);
		vagonsToDownloadTable.add(akrPlatforma);
		vagonsToDownloadTable.add(akrPlatformaFalse);
		vagonsToDownloadTable.add(akrPoluvagon);
		vagonsToDownloadTable.add(akrPoluvagonFalse + 61);
		vagonsToDownloadTable.add(akrSisterna);
		vagonsToDownloadTable.add(akrSisternaFalse);
		vagonsToDownloadTable.add(akrBoshqa);
		vagonsToDownloadTable.add(akrBoshqaFalse);

		vagonsToDownloadTable.add(SamKrpHammaPlan);
		vagonsToDownloadTable.add(skrHammaFalse + 89);
		vagonsToDownloadTable.add(skrKriti);
		vagonsToDownloadTable.add(skrKritiFalse);
		vagonsToDownloadTable.add(skrPlatforma);
		vagonsToDownloadTable.add(skrPlatformaFalse);
		vagonsToDownloadTable.add(skrPoluvagon);
		vagonsToDownloadTable.add(skrPoluvagonFalse + 88);
		vagonsToDownloadTable.add(skrSisterna);
		vagonsToDownloadTable.add(skrSisternaFalse + 1);
		vagonsToDownloadTable.add(skrBoshqa);
		vagonsToDownloadTable.add(skrBoshqaFalse);

		vagonsToDownloadTable.add(KrpHammaPlan);
		vagonsToDownloadTable.add(krHammaFalse + 233);
		vagonsToDownloadTable.add(KrpKritiPlan);
		vagonsToDownloadTable.add(krKritiFalse);
		vagonsToDownloadTable.add(KrpPlatformaPlan);
		vagonsToDownloadTable.add(krPlatforma);
		vagonsToDownloadTable.add(KrpPoluvagonPlan);
		vagonsToDownloadTable.add(krPoluvagon + 232);
		vagonsToDownloadTable.add(KrpSisternaPlan);
		vagonsToDownloadTable.add(krSisterna + 1);
		vagonsToDownloadTable.add(KrpBoshqaPlan);
		vagonsToDownloadTable.add(krBoshqa);

		vagonsToDownloadAllTable = vagonsToDownloadTable;

    	return "planTableForMonthsUty";
    }

    // wagon nomer orqali qidirish 1 oylida
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/searchTayyorUty")
	public String searchByNomer(Model model,  @RequestParam(value = "participant", required = false) Integer participant) {

		String oy=null;
		switch (month) {
			case 1:
				oy = "-01";
				break;
			case 2:
				oy = "-02";
				break;
			case 3:
				oy = "-03";
				break;
			case 4:
				oy = "-04";
				break;
			case 5:
				oy = "-05";
				break;
			case 6:
				oy = "-06";
				break;
			case 7:
				oy = "-07";
				break;
			case 8:
				oy = "-08";
				break;
			case 9:
				oy = "-09";
				break;
			case 10:
				oy = "-10";
				break;
			case 11:
				oy = "-11";
				break;
			case 12:
				oy = "-12";
				break;
		}

		if(participant==null  ) {
	    	vagonsToDownload = vagonTayyorUtyService.findAll(oy);
			model.addAttribute("vagons", vagonTayyorUtyService.findAll(oy));
		}else {
			List<VagonTayyorUty> emptyList = new ArrayList<>();
			vagonsToDownload = emptyList;
			vagonsToDownload.add( vagonTayyorUtyService.searchByNomer(participant, oy));
			model.addAttribute("vagons", vagonTayyorUtyService.searchByNomer(participant, oy));
		}

		//vaqtni olib turadi
		model.addAttribute("samDate",vagonTayyorUtyService.getSamDate());
		model.addAttribute("havDate", vagonTayyorUtyService.getHavDate());
		model.addAttribute("andjDate",vagonTayyorUtyService.getAndjDate());

		PlanUty planDto = vagonTayyorUtyService.getPlanuty();
		//planlar kiritish

		//havos hamma plan
		int HavDtHammaPlan = planDto.getHavDtKritiPlanUty() + planDto.getHavDtPlatformaPlanUty() + planDto.getHavDtPoluvagonPlanUty() + planDto.getHavDtSisternaPlanUty() + planDto.getHavDtBoshqaPlanUty();
		int HavDtKritiPlan =  planDto.getHavDtKritiPlanUty();
		int HavDtPlatformaPlan =  planDto.getHavDtPlatformaPlanUty();
		int HavDtPoluvagonPlan =  planDto.getHavDtPoluvagonPlanUty();
		int HavDtSisternaPlan =  planDto.getHavDtSisternaPlanUty();
		int HavDtBoshqaPlan =  planDto.getHavDtBoshqaPlanUty();


		model.addAttribute("HavDtHammaPlan", HavDtHammaPlan);
		model.addAttribute("HavDtKritiPlan", HavDtKritiPlan);
		model.addAttribute("HavDtPlatformaPlan", HavDtPlatformaPlan);
		model.addAttribute("HavDtPoluvagonPlan", HavDtPoluvagonPlan);
		model.addAttribute("HavDtSisternaPlan", HavDtSisternaPlan);
		model.addAttribute("HavDtBoshqaPlan", HavDtBoshqaPlan);

		//andijon hamma plan depo tamir
		int AndjDtHammaPlan = planDto.getAndjDtKritiPlanUty() + planDto.getAndjDtPlatformaPlanUty() + planDto.getAndjDtPoluvagonPlanUty() + planDto.getAndjDtSisternaPlanUty() + planDto.getAndjDtBoshqaPlanUty();
		int AndjDtKritiPlan =  planDto.getAndjDtKritiPlanUty();
		int AndjDtPlatformaPlan =  planDto.getAndjDtPlatformaPlanUty();
		int AndjDtPoluvagonPlan =  planDto.getAndjDtPoluvagonPlanUty();
		int AndjDtSisternaPlan =  planDto.getAndjDtSisternaPlanUty();
		int AndjDtBoshqaPlan =  planDto.getAndjDtBoshqaPlanUty();

		model.addAttribute("AndjDtHammaPlan", AndjDtHammaPlan);
		model.addAttribute("AndjDtKritiPlan", AndjDtKritiPlan);
		model.addAttribute("AndjDtPlatformaPlan", AndjDtPlatformaPlan);
		model.addAttribute("AndjDtPoluvagonPlan", AndjDtPoluvagonPlan);
		model.addAttribute("AndjDtSisternaPlan", AndjDtSisternaPlan);
		model.addAttribute("AndjDtBoshqaPlan", AndjDtBoshqaPlan);

		//samarqand depo tamir
		int SamDtHammaPlan=planDto.getSamDtKritiPlanUty() + planDto.getSamDtPlatformaPlanUty() + planDto.getSamDtPoluvagonPlanUty() + planDto.getSamDtSisternaPlanUty() + planDto.getSamDtBoshqaPlanUty();
		int SamDtKritiPlan =  planDto.getSamDtKritiPlanUty();
		int SamDtPlatformaPlan =  planDto.getSamDtPlatformaPlanUty();
		int SamDtPoluvagonPlan =  planDto.getSamDtPoluvagonPlanUty();
		int SamDtSisternaPlan =  planDto.getSamDtSisternaPlanUty();
		int SamDtBoshqaPlan =  planDto.getSamDtBoshqaPlanUty();

		model.addAttribute("SamDtHammaPlan",SamDtHammaPlan);
		model.addAttribute("SamDtKritiPlan", SamDtKritiPlan);
		model.addAttribute("SamDtPlatformaPlan", SamDtPlatformaPlan);
		model.addAttribute("SamDtPoluvagonPlan", SamDtPoluvagonPlan);
		model.addAttribute("SamDtSisternaPlan", SamDtSisternaPlan);
		model.addAttribute("SamDtBoshqaPlan", SamDtBoshqaPlan);

		// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = planDto.getAndjDtKritiPlanUty() + planDto.getHavDtKritiPlanUty() + planDto.getSamDtKritiPlanUty();
		int DtPlatformaPlan = planDto.getAndjDtPlatformaPlanUty() + planDto.getHavDtPlatformaPlanUty() + planDto.getSamDtPlatformaPlanUty();
		int DtPoluvagonPlan = planDto.getAndjDtPoluvagonPlanUty() + planDto.getHavDtPoluvagonPlanUty() + planDto.getSamDtPoluvagonPlanUty();
		int DtSisternaPlan = planDto.getAndjDtSisternaPlanUty() + planDto.getHavDtSisternaPlanUty() + planDto.getSamDtSisternaPlanUty();
		int DtBoshqaPlan = planDto.getAndjDtBoshqaPlanUty() + planDto.getHavDtBoshqaPlanUty() + planDto.getSamDtBoshqaPlanUty();

		model.addAttribute("DtHammaPlan", DtHammaPlan);
		model.addAttribute("DtKritiPlan", DtKritiPlan);
		model.addAttribute("DtPlatformaPlan", DtPlatformaPlan);
		model.addAttribute("DtPoluvagonPlan", DtPoluvagonPlan);
		model.addAttribute("DtSisternaPlan", DtSisternaPlan);
		model.addAttribute("DtBoshqaPlan", DtBoshqaPlan);


		//havos kapital tamir uchun plan
		int HavKtHammaPlan = planDto.getHavKtKritiPlanUty() + planDto.getHavKtPlatformaPlanUty() + planDto.getHavKtPoluvagonPlanUty() + planDto.getHavKtSisternaPlanUty() + planDto.getHavKtBoshqaPlanUty();
		int HavKtKritiPlan = planDto.getHavKtKritiPlanUty();
		int HavKtPlatformaPlan = planDto.getHavKtPlatformaPlanUty();
		int HavKtPoluvagonPlan = planDto.getHavKtPoluvagonPlanUty();
		int HavKtSisternaPlan = planDto.getHavKtSisternaPlanUty();
		int HavKtBoshqaPlan = planDto.getHavKtBoshqaPlanUty();

		model.addAttribute("HavKtHammaPlan", HavKtHammaPlan);
		model.addAttribute("HavKtKritiPlan", HavKtKritiPlan);
		model.addAttribute("HavKtPlatformaPlan", HavKtPlatformaPlan);
		model.addAttribute("HavKtPoluvagonPlan", HavKtPoluvagonPlan);
		model.addAttribute("HavKtSisternaPlan", HavKtSisternaPlan);
		model.addAttribute("HavKtBoshqaPlan", HavKtBoshqaPlan);

		//VCHD-5 kapital tamir uchun plan
		int AndjKtHammaPlan = planDto.getAndjKtKritiPlanUty() + planDto.getAndjKtPlatformaPlanUty() + planDto.getAndjKtPoluvagonPlanUty() + planDto.getAndjKtSisternaPlanUty() + planDto.getAndjKtBoshqaPlanUty();
		int AndjKtKritiPlan =  planDto.getAndjKtKritiPlanUty();
		int AndjKtPlatformaPlan =  planDto.getAndjKtPlatformaPlanUty();
		int AndjKtPoluvagonPlan =  planDto.getAndjKtPoluvagonPlanUty();
		int AndjKtSisternaPlan =  planDto.getAndjKtSisternaPlanUty();
		int AndjKtBoshqaPlan =  planDto.getAndjKtBoshqaPlanUty();

		model.addAttribute("AndjKtHammaPlan", AndjKtHammaPlan);
		model.addAttribute("AndjKtKritiPlan", AndjKtKritiPlan);
		model.addAttribute("AndjKtPlatformaPlan", AndjKtPlatformaPlan);
		model.addAttribute("AndjKtPoluvagonPlan", AndjKtPoluvagonPlan);
		model.addAttribute("AndjKtSisternaPlan", AndjKtSisternaPlan);
		model.addAttribute("AndjKtBoshqaPlan", AndjKtBoshqaPlan);


		//VCHD-6 kapital tamir uchun plan
		int SamKtHammaPlan =  planDto.getSamKtKritiPlanUty() + planDto.getSamKtPlatformaPlanUty() + planDto.getSamKtPoluvagonPlanUty() + planDto.getSamKtSisternaPlanUty() + planDto.getSamKtBoshqaPlanUty();
		int SamKtKritiPlan =  planDto.getSamKtKritiPlanUty();
		int SamKtPlatformaPlan =  planDto.getSamKtPlatformaPlanUty();
		int SamKtPoluvagonPlan =  planDto.getSamKtPoluvagonPlanUty();
		int SamKtSisternaPlan =  planDto.getSamKtSisternaPlanUty();
		int SamKtBoshqaPlan =  planDto.getSamKtBoshqaPlanUty();

		model.addAttribute("SamKtHammaPlan", SamKtHammaPlan);
		model.addAttribute("SamKtKritiPlan", SamKtKritiPlan);
		model.addAttribute("SamKtPlatformaPlan", SamKtPlatformaPlan);
		model.addAttribute("SamKtPoluvagonPlan", SamKtPoluvagonPlan);
		model.addAttribute("SamKtSisternaPlan", SamKtSisternaPlan);
		model.addAttribute("SamKtBoshqaPlan", SamKtBoshqaPlan);

		//kapital itogo
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = planDto.getAndjKtKritiPlanUty() + planDto.getHavKtKritiPlanUty() + planDto.getSamKtKritiPlanUty();
		int KtPlatformaPlan = planDto.getAndjKtPlatformaPlanUty() + planDto.getHavKtPlatformaPlanUty() + planDto.getSamKtPlatformaPlanUty();
		int KtPoluvagonPlan = planDto.getAndjKtPoluvagonPlanUty() + planDto.getHavKtPoluvagonPlanUty() + planDto.getSamKtPoluvagonPlanUty();
		int KtSisternaPlan = planDto.getAndjKtSisternaPlanUty() + planDto.getHavKtSisternaPlanUty() + planDto.getSamKtSisternaPlanUty();
		int KtBoshqaPlan = planDto.getAndjKtBoshqaPlanUty() + planDto.getHavKtBoshqaPlanUty() + planDto.getSamKtBoshqaPlanUty();

		model.addAttribute("KtHammaPlan", KtHammaPlan);
		model.addAttribute("KtKritiPlan", KtKritiPlan);
		model.addAttribute("KtPlatformaPlan", KtPlatformaPlan);
		model.addAttribute("KtPoluvagonPlan", KtPoluvagonPlan);
		model.addAttribute("KtSisternaPlan", KtSisternaPlan);
		model.addAttribute("KtBoshqaPlan", KtBoshqaPlan);

		//VCHD-3 KRP plan
		int HavKrpHammaPlan =  planDto.getHavKrpKritiPlanUty() + planDto.getHavKrpPlatformaPlanUty() + planDto.getHavKrpPoluvagonPlanUty() + planDto.getHavKrpSisternaPlanUty() + planDto.getHavKrpBoshqaPlanUty();
		int HavKrpKritiPlan = planDto.getHavKrpKritiPlanUty();
		int HavKrpPlatformaPlan = planDto.getHavKrpPlatformaPlanUty();
		int HavKrpPoluvagonPlan = planDto.getHavKrpPoluvagonPlanUty();
		int HavKrpSisternaPlan = planDto.getHavKrpSisternaPlanUty();
		int HavKrpBoshqaPlan = planDto.getHavKrpBoshqaPlanUty();

		model.addAttribute("HavKrpHammaPlan", HavKrpHammaPlan);
		model.addAttribute("HavKrpKritiPlan", HavKrpKritiPlan);
		model.addAttribute("HavKrpPlatformaPlan", HavKrpPlatformaPlan);
		model.addAttribute("HavKrpPoluvagonPlan", HavKrpPoluvagonPlan);
		model.addAttribute("HavKrpSisternaPlan", HavKrpSisternaPlan);
		model.addAttribute("HavKrpBoshqaPlan", HavKrpBoshqaPlan);

		//VCHD-5 Krp plan
		int AndjKrpHammaPlan =  planDto.getAndjKrpKritiPlanUty() + planDto.getAndjKrpPlatformaPlanUty() + planDto.getAndjKrpPoluvagonPlanUty() + planDto.getAndjKrpSisternaPlanUty() + planDto.getAndjKrpBoshqaPlanUty();
		int AndjKrpKritiPlan = planDto.getAndjKrpKritiPlanUty();
		int AndjKrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanUty();
		int AndjKrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanUty();
		int AndjKrpSisternaPlan = planDto.getAndjKrpSisternaPlanUty();
		int AndjKrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanUty();

		model.addAttribute("AndjKrpHammaPlan", AndjKrpHammaPlan);
		model.addAttribute("AndjKrpKritiPlan", AndjKrpKritiPlan);
		model.addAttribute("AndjKrpPlatformaPlan", AndjKrpPlatformaPlan);
		model.addAttribute("AndjKrpPoluvagonPlan", AndjKrpPoluvagonPlan);
		model.addAttribute("AndjKrpSisternaPlan", AndjKrpSisternaPlan);
		model.addAttribute("AndjKrpBoshqaPlan", AndjKrpBoshqaPlan);

		//samarqand KRP plan
		int SamKrpHammaPlan = planDto.getSamKrpKritiPlanUty() + planDto.getSamKrpPlatformaPlanUty() + planDto.getSamKrpPoluvagonPlanUty() + planDto.getSamKrpSisternaPlanUty() + planDto.getSamKrpBoshqaPlanUty();
		int SamKrpKritiPlan = planDto.getSamKrpKritiPlanUty();
		int SamKrpPlatformaPlan = planDto.getSamKrpPlatformaPlanUty();
		int SamKrpPoluvagonPlan = planDto.getSamKrpPoluvagonPlanUty();
		int SamKrpSisternaPlan = planDto.getSamKrpSisternaPlanUty();
		int SamKrpBoshqaPlan = planDto.getSamKrpBoshqaPlanUty();

		model.addAttribute("SamKrpHammaPlan", SamKrpHammaPlan);
		model.addAttribute("SamKrpKritiPlan", SamKrpKritiPlan);
		model.addAttribute("SamKrpPlatformaPlan", SamKrpPlatformaPlan);
		model.addAttribute("SamKrpPoluvagonPlan", SamKrpPoluvagonPlan);
		model.addAttribute("SamKrpSisternaPlan", SamKrpSisternaPlan);
		model.addAttribute("SamKrpBoshqaPlan", SamKrpBoshqaPlan);

		//Krp itogo plan
		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = planDto.getAndjKrpKritiPlanUty() + planDto.getHavKrpKritiPlanUty() + planDto.getSamKrpKritiPlanUty();
		int KrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanUty() + planDto.getHavKrpPlatformaPlanUty() + planDto.getSamKrpPlatformaPlanUty();
		int KrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanUty() + planDto.getHavKrpPoluvagonPlanUty() + planDto.getSamKrpPoluvagonPlanUty();
		int KrpSisternaPlan = planDto.getAndjKrpSisternaPlanUty() + planDto.getHavKrpSisternaPlanUty() + planDto.getSamKrpSisternaPlanUty();
		int KrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanUty() + planDto.getHavKrpBoshqaPlanUty() + planDto.getSamKrpBoshqaPlanUty();

		model.addAttribute("KrpHammaPlan", KrpHammaPlan);
		model.addAttribute("KrpKritiPlan", KrpKritiPlan);
		model.addAttribute("KrpPlatformaPlan", KrpPlatformaPlan);
		model.addAttribute("KrpPoluvagonPlan", KrpPoluvagonPlan);
		model.addAttribute("KrpSisternaPlan", KrpSisternaPlan);
		model.addAttribute("KrpBoshqaPlan", KrpBoshqaPlan);


		// factlar

		//VCHD-3 uchun depli tamir
		int hdHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		int hdKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy);
		int hdPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)", oy);
		int hdPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy);
		int hdSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)", oy);
		int hdBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		model.addAttribute("hdHamma",hdHamma);
		model.addAttribute("hdKriti", hdKriti);
		model.addAttribute("hdPlatforma", hdPlatforma);
		model.addAttribute("hdPoluvagon", hdPoluvagon);
		model.addAttribute("hdSisterna", hdSisterna);
		model.addAttribute("hdBoshqa", hdBoshqa);


		//VCHD-5 uchun depli tamir
		int adHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		int adKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy);
		int adPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)", oy);
		int adPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy);
		int adSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)", oy);
		int adBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		model.addAttribute("adHamma",adHamma);
		model.addAttribute("adKriti", adKriti);
		model.addAttribute("adPlatforma", adPlatforma);
		model.addAttribute("adPoluvagon", adPoluvagon);
		model.addAttribute("adSisterna", adSisterna);
		model.addAttribute("adBoshqa", adBoshqa);

		//samarqand uchun depli tamir
		int sdHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		int sdKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy);
		int sdPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)", oy);
		int sdPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy);
		int sdSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)", oy);
		int sdBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		model.addAttribute("sdHamma",sdHamma);
		model.addAttribute("sdKriti", sdKriti);
		model.addAttribute("sdPlatforma", sdPlatforma);
		model.addAttribute("sdPoluvagon", sdPoluvagon);
		model.addAttribute("sdSisterna", sdSisterna);
		model.addAttribute("sdBoshqa", sdBoshqa);


		// itogo Fact uchun depli tamir
		int uvtdhamma = sdHamma + hdHamma + adHamma;
		int uvtdKriti = sdKriti + hdKriti + adKriti;
		int uvtdPlatforma = sdPlatforma + adPlatforma + hdPlatforma;
		int uvtdPoluvagon = sdPoluvagon + hdPoluvagon + adPoluvagon;
		int uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
		int uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

		model.addAttribute("uvtdhamma",uvtdhamma);
		model.addAttribute("uvtdKriti",uvtdKriti);
		model.addAttribute("uvtdPlatforma",uvtdPlatforma);
		model.addAttribute("uvtdPoluvagon",uvtdPoluvagon);
		model.addAttribute("uvtdSisterna",uvtdSisterna);
		model.addAttribute("uvtdBoshqa",uvtdBoshqa);


		//VCHD-3 uchun kapital tamir
		int hkHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		int hkKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)"," Kapital ta’mir(КР)", oy);
		int hkPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)", oy);
		int hkPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy);
		int hkSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)", oy);
		int hkBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		model.addAttribute("hkHamma",hkHamma);
		model.addAttribute("hkKriti", hkKriti);
		model.addAttribute("hkPlatforma", hkPlatforma);
		model.addAttribute("hkPoluvagon", hkPoluvagon);
		model.addAttribute("hkSisterna", hkSisterna);
		model.addAttribute("hkBoshqa", hkBoshqa);

		//VCHD-5 uchun kapital tamir
		int akHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		int akKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy);
		int akPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)", oy);
		int akPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy);
		int akSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)", oy);
		int akBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		model.addAttribute("akHamma",akHamma);
		model.addAttribute("akKriti", akKriti);
		model.addAttribute("akPlatforma", akPlatforma);
		model.addAttribute("akPoluvagon", akPoluvagon);
		model.addAttribute("akSisterna", akSisterna);
		model.addAttribute("akBoshqa", akBoshqa);

		//samarqand uchun Kapital tamir
		int skHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		int skKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy);
		int skPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)", oy);
		int skPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy);
		int skSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)", oy);
		int skBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		model.addAttribute("skHamma",skHamma);
		model.addAttribute("skKriti", skKriti);
		model.addAttribute("skPlatforma", skPlatforma);
		model.addAttribute("skPoluvagon", skPoluvagon);
		model.addAttribute("skSisterna", skSisterna);
		model.addAttribute("skBoshqa", skBoshqa);

		// itogo Fact uchun kapital tamir
		int uvtkhamma = skHamma + hkHamma + akHamma;
		int uvtkKriti = skKriti + hkKriti + akKriti;
		int uvtkPlatforma = skPlatforma + akPlatforma + hkPlatforma;
		int uvtkPoluvagon = skPoluvagon + hkPoluvagon + akPoluvagon;
		int uvtkSisterna = akSisterna + hkSisterna + skSisterna;
		int uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

		model.addAttribute("uvtkhamma",uvtkhamma);
		model.addAttribute("uvtkKriti",uvtkKriti);
		model.addAttribute("uvtkPlatforma",uvtkPlatforma);
		model.addAttribute("uvtkPoluvagon",uvtkPoluvagon);
		model.addAttribute("uvtkSisterna",uvtkSisterna);
		model.addAttribute("uvtkBoshqa",uvtkBoshqa);

		//VCHD-3 uchun KRP
		int hkrHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)", oy);

		int hkrKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)"," KRP(КРП)", oy);
		int hkrPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)", oy);
		int hkrPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)", oy);
		int hkrSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)", oy);
		int hkrBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)", oy);

		model.addAttribute("hkrHamma",hkrHamma);
		model.addAttribute("hkrKriti", hkrKriti);
		model.addAttribute("hkrPlatforma", hkrPlatforma);
		model.addAttribute("hkrPoluvagon", hkrPoluvagon);
		model.addAttribute("hkrSisterna", hkrSisterna);
		model.addAttribute("hkrBoshqa", hkrBoshqa);

  		//VCHD-5 uchun KRP
		int akrHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)", oy);

		int akrKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)", oy);
		int akrPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)", oy);
		int akrPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)", oy);
		int akrSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)", oy);
		int akrBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)", oy);

		model.addAttribute("akrHamma",akrHamma);
		model.addAttribute("akrKriti", akrKriti);
		model.addAttribute("akrPlatforma", akrPlatforma);
		model.addAttribute("akrPoluvagon", akrPoluvagon);
		model.addAttribute("akrSisterna", akrSisterna);
		model.addAttribute("akrBoshqa", akrBoshqa);

		//samarqand uchun KRP tamir
		int skrHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)", oy);

		int skrKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)", oy);
		int skrPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)", oy);
		int skrPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)", oy);
		int skrSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)", oy);
		int skrBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)", oy);

		model.addAttribute("skrHamma",skrHamma);
		model.addAttribute("skrKriti", skrKriti);
		model.addAttribute("skrPlatforma", skrPlatforma);
		model.addAttribute("skrPoluvagon", skrPoluvagon);
		model.addAttribute("skrSisterna", skrSisterna);
		model.addAttribute("skrBoshqa", skrBoshqa);


		// itogo Fact uchun KRP
		int uvtkrhamma = skrHamma + hkrHamma + akrHamma;
		int uvtkrKriti = skrKriti + hkrKriti + akrKriti;
		int uvtkrPlatforma = skrPlatforma + akrPlatforma + hkrPlatforma;
		int uvtkrPoluvagon = skrPoluvagon + hkrPoluvagon + akrPoluvagon;
		int uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
		int uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

		model.addAttribute("uvtkrhamma",uvtkrhamma);
		model.addAttribute("uvtkrKriti",uvtkrKriti);
		model.addAttribute("uvtkrPlatforma",uvtkrPlatforma);
		model.addAttribute("uvtkrPoluvagon",uvtkrPoluvagon);
		model.addAttribute("uvtkrSisterna",uvtkrSisterna);
		model.addAttribute("uvtkrBoshqa",uvtkrBoshqa);

		//yuklab olish uchun list
		List<Integer> vagonsToDownloadTable = new ArrayList<>();
//Depoli tamir
		vagonsToDownloadTable.add(HavDtHammaPlan);
		vagonsToDownloadTable.add(hdHamma);
		vagonsToDownloadTable.add(HavDtKritiPlan);
		vagonsToDownloadTable.add(hdKriti);
		vagonsToDownloadTable.add(HavDtPlatformaPlan);
		vagonsToDownloadTable.add(hdPlatforma);
		vagonsToDownloadTable.add(HavDtPoluvagonPlan);
		vagonsToDownloadTable.add(hdPoluvagon);
		vagonsToDownloadTable.add(HavDtSisternaPlan);
		vagonsToDownloadTable.add(hdSisterna);
		vagonsToDownloadTable.add(HavDtBoshqaPlan);
		vagonsToDownloadTable.add(hdBoshqa);

		vagonsToDownloadTable.add(AndjDtHammaPlan);
		vagonsToDownloadTable.add(adHamma);
		vagonsToDownloadTable.add(AndjDtKritiPlan);
		vagonsToDownloadTable.add(adKriti);
		vagonsToDownloadTable.add(AndjDtPlatformaPlan);
		vagonsToDownloadTable.add(adPlatforma);
		vagonsToDownloadTable.add(AndjDtPoluvagonPlan);
		vagonsToDownloadTable.add(adPoluvagon);
		vagonsToDownloadTable.add(AndjDtSisternaPlan);
		vagonsToDownloadTable.add(adSisterna);
		vagonsToDownloadTable.add(AndjDtBoshqaPlan);
		vagonsToDownloadTable.add(adBoshqa);

		vagonsToDownloadTable.add(SamDtHammaPlan);
		vagonsToDownloadTable.add(sdHamma);
		vagonsToDownloadTable.add(SamDtKritiPlan);
		vagonsToDownloadTable.add(sdKriti);
		vagonsToDownloadTable.add(SamDtPlatformaPlan);
		vagonsToDownloadTable.add(sdPlatforma);
		vagonsToDownloadTable.add(SamDtPoluvagonPlan);
		vagonsToDownloadTable.add(sdPoluvagon);
		vagonsToDownloadTable.add(SamDtSisternaPlan);
		vagonsToDownloadTable.add(sdSisterna);
		vagonsToDownloadTable.add(SamDtBoshqaPlan);
		vagonsToDownloadTable.add(sdBoshqa);

		vagonsToDownloadTable.add(DtHammaPlan);
		vagonsToDownloadTable.add(uvtdhamma);
		vagonsToDownloadTable.add(DtKritiPlan);
		vagonsToDownloadTable.add(uvtdKriti);
		vagonsToDownloadTable.add(DtPlatformaPlan);
		vagonsToDownloadTable.add(uvtdPlatforma);
		vagonsToDownloadTable.add(DtPoluvagonPlan);
		vagonsToDownloadTable.add(uvtdPoluvagon);
		vagonsToDownloadTable.add(DtSisternaPlan);
		vagonsToDownloadTable.add(uvtdSisterna);
		vagonsToDownloadTable.add(DtBoshqaPlan);
		vagonsToDownloadTable.add(uvtdBoshqa);

//kapital tamir
		vagonsToDownloadTable.add(HavKtHammaPlan);
		vagonsToDownloadTable.add(hkHamma);
		vagonsToDownloadTable.add(HavKtKritiPlan);
		vagonsToDownloadTable.add(hkKriti);
		vagonsToDownloadTable.add(HavKtPlatformaPlan);
		vagonsToDownloadTable.add(hkPlatforma);
		vagonsToDownloadTable.add(HavKtPoluvagonPlan);
		vagonsToDownloadTable.add(hkPoluvagon);
		vagonsToDownloadTable.add(HavKtSisternaPlan);
		vagonsToDownloadTable.add(hkSisterna);
		vagonsToDownloadTable.add(HavKtBoshqaPlan);
		vagonsToDownloadTable.add(hkBoshqa);

		vagonsToDownloadTable.add(AndjKtHammaPlan);
		vagonsToDownloadTable.add(akHamma);
		vagonsToDownloadTable.add(AndjKtKritiPlan);
		vagonsToDownloadTable.add(akKriti);
		vagonsToDownloadTable.add(AndjKtPlatformaPlan);
		vagonsToDownloadTable.add(akPlatforma);
		vagonsToDownloadTable.add(AndjKtPoluvagonPlan);
		vagonsToDownloadTable.add(akPoluvagon);
		vagonsToDownloadTable.add(AndjKtSisternaPlan);
		vagonsToDownloadTable.add(akSisterna);
		vagonsToDownloadTable.add(AndjKtBoshqaPlan);
		vagonsToDownloadTable.add(akBoshqa);

		vagonsToDownloadTable.add(SamKtHammaPlan);
		vagonsToDownloadTable.add(skHamma);
		vagonsToDownloadTable.add(SamKtKritiPlan);
		vagonsToDownloadTable.add(skKriti);
		vagonsToDownloadTable.add(SamKtPlatformaPlan);
		vagonsToDownloadTable.add(skPlatforma);
		vagonsToDownloadTable.add(SamKtPoluvagonPlan);
		vagonsToDownloadTable.add(skPoluvagon);
		vagonsToDownloadTable.add(SamKtSisternaPlan);
		vagonsToDownloadTable.add(skSisterna);
		vagonsToDownloadTable.add(SamKtBoshqaPlan);
		vagonsToDownloadTable.add(skBoshqa);

		vagonsToDownloadTable.add(KtHammaPlan);
		vagonsToDownloadTable.add(uvtkhamma);
		vagonsToDownloadTable.add(KtKritiPlan);
		vagonsToDownloadTable.add(uvtkKriti);
		vagonsToDownloadTable.add(KtPlatformaPlan);
		vagonsToDownloadTable.add(uvtkPlatforma);
		vagonsToDownloadTable.add(KtPoluvagonPlan);
		vagonsToDownloadTable.add(uvtkPoluvagon);
		vagonsToDownloadTable.add(KtSisternaPlan);
		vagonsToDownloadTable.add(uvtkSisterna);
		vagonsToDownloadTable.add(KtBoshqaPlan);
		vagonsToDownloadTable.add(uvtkBoshqa);
//krp
		vagonsToDownloadTable.add(HavKrpHammaPlan);
		vagonsToDownloadTable.add(hkrHamma);
		vagonsToDownloadTable.add(HavKrpKritiPlan);
		vagonsToDownloadTable.add(hkrKriti);
		vagonsToDownloadTable.add(HavKrpPlatformaPlan);
		vagonsToDownloadTable.add(hkrPlatforma);
		vagonsToDownloadTable.add(HavKrpPoluvagonPlan);
		vagonsToDownloadTable.add(hkrPoluvagon);
		vagonsToDownloadTable.add(HavKrpSisternaPlan);
		vagonsToDownloadTable.add(hkrSisterna);
		vagonsToDownloadTable.add(HavKrpBoshqaPlan);
		vagonsToDownloadTable.add(hkrBoshqa);

		vagonsToDownloadTable.add(AndjKrpHammaPlan);
		vagonsToDownloadTable.add(akrHamma);
		vagonsToDownloadTable.add(AndjKrpKritiPlan);
		vagonsToDownloadTable.add(akrKriti);
		vagonsToDownloadTable.add(AndjKrpPlatformaPlan);
		vagonsToDownloadTable.add(akrPlatforma);
		vagonsToDownloadTable.add(AndjKrpPoluvagonPlan);
		vagonsToDownloadTable.add(akrPoluvagon);
		vagonsToDownloadTable.add(AndjKrpSisternaPlan);
		vagonsToDownloadTable.add(akrSisterna);
		vagonsToDownloadTable.add(AndjKrpBoshqaPlan);
		vagonsToDownloadTable.add(akrBoshqa);

		vagonsToDownloadTable.add(SamKrpHammaPlan);
		vagonsToDownloadTable.add(skrHamma);
		vagonsToDownloadTable.add(SamKrpKritiPlan);
		vagonsToDownloadTable.add(skrKriti);
		vagonsToDownloadTable.add(SamKrpPlatformaPlan);
		vagonsToDownloadTable.add(skrPlatforma);
		vagonsToDownloadTable.add(SamKrpPoluvagonPlan);
		vagonsToDownloadTable.add(skrPoluvagon);
		vagonsToDownloadTable.add(SamKrpSisternaPlan);
		vagonsToDownloadTable.add(skrSisterna);
		vagonsToDownloadTable.add(SamKrpBoshqaPlan);
		vagonsToDownloadTable.add(skrBoshqa);

		vagonsToDownloadTable.add(KrpHammaPlan);
		vagonsToDownloadTable.add(uvtkrhamma);
		vagonsToDownloadTable.add(KrpKritiPlan);
		vagonsToDownloadTable.add(uvtkrKriti);
		vagonsToDownloadTable.add(KrpPlatformaPlan);
		vagonsToDownloadTable.add(uvtkrPlatforma);
		vagonsToDownloadTable.add(KrpPoluvagonPlan);
		vagonsToDownloadTable.add(uvtkrPoluvagon);
		vagonsToDownloadTable.add(KrpSisternaPlan);
		vagonsToDownloadTable.add(uvtkrSisterna);
		vagonsToDownloadTable.add(KrpBoshqaPlan);
		vagonsToDownloadTable.add(uvtkrBoshqa);

		vagonsToDownloadAllTable = vagonsToDownloadTable;

		return "AllPlanTableUty";
    }
    
    // wagon nomer orqali qidirish shu oygacha hammasidan
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/searchTayyorAllMonthsUty")
	public String search(Model model,  @RequestParam(value = "participant", required = false) Integer participant) {
		if(participant==null  ) {
			vagonsToDownload = vagonTayyorUtyService.findAll();
			model.addAttribute("vagons", vagonTayyorUtyService.findAll());
		}else {
			List<VagonTayyorUty> emptyList = new ArrayList<>();
			vagonsToDownload = emptyList;
			vagonsToDownload.add( vagonTayyorUtyService.findByNomer(participant));
			model.addAttribute("vagons", vagonTayyorUtyService.findByNomer(participant));
		}

		//planlar kiritish
		PlanUty planDto = vagonTayyorUtyService.getPlanuty();

		//samarqand depo tamir plan
		int sdKriti = planDto.getSamDtKritiPlanUtyMonths();
		int sdPlatforma=planDto.getSamDtPlatformaPlanUtyMonths();
		int sdPoluvagon=planDto.getSamDtPoluvagonPlanUtyMonths();
		int sdSisterna=planDto.getSamDtSisternaPlanUtyMonths();
		int sdBoshqa=planDto.getSamDtBoshqaPlanUtyMonths();
		int SamDtHammaPlan=sdKriti+sdPlatforma+sdPoluvagon+sdSisterna+sdBoshqa;

		model.addAttribute("SamDtHammaPlan",SamDtHammaPlan);
		model.addAttribute("SamDtKritiPlan", sdKriti);
		model.addAttribute("SamDtPlatformaPlan", sdPlatforma);
		model.addAttribute("SamDtPoluvagonPlan", sdPoluvagon);
		model.addAttribute("SamDtSisternaPlan", sdSisterna);
		model.addAttribute("SamDtBoshqaPlan", sdBoshqa);

		//havos depo tamir hamma plan
		int hdKriti = planDto.getHavDtKritiPlanUtyMonths();
		int hdPlatforma=planDto.getHavDtPlatformaPlanUtyMonths();
		int hdPoluvagon=planDto.getHavDtPoluvagonPlanUtyMonths();
		int hdSisterna=planDto.getHavDtSisternaPlanUtyMonths();
		int hdBoshqa=planDto.getHavDtBoshqaPlanUtyMonths();
		int HavDtHammaPlan = hdKriti + hdPlatforma + hdPoluvagon + hdSisterna + hdBoshqa;

		model.addAttribute("HavDtHammaPlan", HavDtHammaPlan);
		model.addAttribute("HavDtKritiPlan", hdKriti);
		model.addAttribute("HavDtPlatformaPlan", hdPlatforma);
		model.addAttribute("HavDtPoluvagonPlan", hdPoluvagon);
		model.addAttribute("HavDtSisternaPlan", hdSisterna);
		model.addAttribute("HavDtBoshqaPlan", hdBoshqa);

		//VCHD-5 depo tamir plan
		int adKriti = planDto.getAndjDtKritiPlanUtyMonths();
		int adPlatforma=planDto.getAndjDtPlatformaPlanUtyMonths();
		int adPoluvagon=planDto.getAndjDtPoluvagonPlanUtyMonths();
		int adSisterna=planDto.getAndjDtSisternaPlanUtyMonths();
		int adBoshqa=planDto.getAndjDtBoshqaPlanUtyMonths();
		int AndjDtHammaPlan = adKriti + adPlatforma + adPoluvagon + adSisterna + adBoshqa;

		model.addAttribute("AndjDtHammaPlan", AndjDtHammaPlan);
		model.addAttribute("AndjDtKritiPlan", adKriti);
		model.addAttribute("AndjDtPlatformaPlan",adPlatforma);
		model.addAttribute("AndjDtPoluvagonPlan", adPoluvagon);
		model.addAttribute("AndjDtSisternaPlan", adSisterna);
		model.addAttribute("AndjDtBoshqaPlan", adBoshqa);

		// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = sdKriti + hdKriti + adKriti;
		int DtPlatformaPlan = sdPlatforma + hdPlatforma + adPlatforma;
		int DtPoluvagonPlan = sdPoluvagon + hdPoluvagon + adPoluvagon;
		int DtSisternaPlan = sdSisterna + hdSisterna + adSisterna;
		int DtBoshqaPlan = sdBoshqa + hdBoshqa + adBoshqa;

		model.addAttribute("DtHammaPlan", DtHammaPlan);
		model.addAttribute("DtKritiPlan", DtKritiPlan);
		model.addAttribute("DtPlatformaPlan", DtPlatformaPlan);
		model.addAttribute("DtPoluvagonPlan", DtPoluvagonPlan);
		model.addAttribute("DtSisternaPlan", DtSisternaPlan);
		model.addAttribute("DtBoshqaPlan", DtBoshqaPlan);

		//Samrqand kapital plan
		int skKriti = planDto.getSamKtKritiPlanUtyMonths();
		int skPlatforma=planDto.getSamKtPlatformaPlanUtyMonths();
		int skPoluvagon=planDto.getSamKtPoluvagonPlanUtyMonths();
		int skSisterna=planDto.getSamKtSisternaPlanUtyMonths();
		int skBoshqa=planDto.getSamKtBoshqaPlanUtyMonths();
		int SamKtHammaPlan=skKriti+skPlatforma+skPoluvagon+skSisterna+skBoshqa;

		model.addAttribute("SamKtHammaPlan",SamKtHammaPlan);
		model.addAttribute("SamKtKritiPlan", skKriti);
		model.addAttribute("SamKtPlatformaPlan", skPlatforma);
		model.addAttribute("SamKtPoluvagonPlan", skPoluvagon);
		model.addAttribute("SamKtSisternaPlan", skSisterna);
		model.addAttribute("SamKtBoshqaPlan", skBoshqa);

		//hovos kapital plan
		int hkKriti = planDto.getHavKtKritiPlanUtyMonths();
		int hkPlatforma=planDto.getHavKtPlatformaPlanUtyMonths();
		int hkPoluvagon=planDto.getHavKtPoluvagonPlanUtyMonths();
		int hkSisterna=planDto.getHavKtSisternaPlanUtyMonths();
		int hkBoshqa=planDto.getHavKtBoshqaPlanUtyMonths();
		int HavKtHammaPlan = hkKriti + hkPlatforma + hkPoluvagon + hkSisterna + hkBoshqa;

		model.addAttribute("HavKtHammaPlan", HavKtHammaPlan);
		model.addAttribute("HavKtKritiPlan", hkKriti);
		model.addAttribute("HavKtPlatformaPlan", hkPlatforma);
		model.addAttribute("HavKtPoluvagonPlan", hkPoluvagon);
		model.addAttribute("HavKtSisternaPlan", hkSisterna);
		model.addAttribute("HavKtBoshqaPlan", hkBoshqa);

		//ANDIJON kapital plan
		int akKriti = planDto.getAndjKtKritiPlanUtyMonths();
		int akPlatforma=planDto.getAndjKtPlatformaPlanUtyMonths();
		int akPoluvagon=planDto.getAndjKtPoluvagonPlanUtyMonths();
		int akSisterna=planDto.getAndjKtSisternaPlanUtyMonths();
		int akBoshqa=planDto.getAndjKtBoshqaPlanUtyMonths();
		int AndjKtHammaPlan = akKriti + akPlatforma + akPoluvagon + akSisterna + akBoshqa;


		model.addAttribute("AndjKtHammaPlan", AndjKtHammaPlan);
		model.addAttribute("AndjKtKritiPlan", akKriti);
		model.addAttribute("AndjKtPlatformaPlan", akPlatforma);
		model.addAttribute("AndjKtPoluvagonPlan", akPoluvagon);
		model.addAttribute("AndjKtSisternaPlan", akSisterna);
		model.addAttribute("AndjKtBoshqaPlan", akBoshqa);

		//Itogo kapital plan
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = skKriti + hkKriti + akKriti;
		int KtPlatformaPlan = skPlatforma + hkPlatforma + akPlatforma;
		int KtPoluvagonPlan =skPoluvagon + hkPoluvagon + akPoluvagon;
		int KtSisternaPlan = skSisterna + hkSisterna + akSisterna;
		int KtBoshqaPlan = skBoshqa + hkBoshqa + akBoshqa;

		model.addAttribute("KtHammaPlan", KtHammaPlan);
		model.addAttribute("KtKritiPlan", KtKritiPlan);
		model.addAttribute("KtPlatformaPlan", KtPlatformaPlan);
		model.addAttribute("KtPoluvagonPlan", KtPoluvagonPlan);
		model.addAttribute("KtSisternaPlan", KtSisternaPlan);
		model.addAttribute("KtBoshqaPlan", KtBoshqaPlan);

		//Samarqankr Krp plan
		int skrKriti = planDto.getSamKrpKritiPlanUtyMonths();
		int skrPlatforma=planDto.getSamKrpPlatformaPlanUtyMonths();
		int skrPoluvagon=planDto.getSamKrpPoluvagonPlanUtyMonths();
		int skrSisterna=planDto.getSamKrpSisternaPlanUtyMonths();
		int skrBoshqa=planDto.getSamKrpBoshqaPlanUtyMonths();
		int SamKrpHammaPlan=skrKriti+skrPlatforma+skrPoluvagon+skrSisterna+skrBoshqa;

		model.addAttribute("SamKrpHammaPlan", SamKrpHammaPlan);
		model.addAttribute("SamKrpKritiPlan", skrKriti);
		model.addAttribute("SamKrpPlatformaPlan", skrPlatforma);
		model.addAttribute("SamKrpPoluvagonPlan", skrPoluvagon);
		model.addAttribute("SamKrpSisternaPlan", skrSisterna);
		model.addAttribute("SamKrpBoshqaPlan", skrBoshqa);

		//Hovos krp plan
		int hkrKriti = planDto.getHavKrpKritiPlanUtyMonths();
		int hkrPlatforma=planDto.getHavKrpPlatformaPlanUtyMonths();
		int hkrPoluvagon=planDto.getHavKrpPoluvagonPlanUtyMonths();
		int hkrSisterna=planDto.getHavKrpSisternaPlanUtyMonths();
		int hkrBoshqa=planDto.getHavKrpBoshqaPlanUtyMonths();
		int HavKrpHammaPlan = hkrKriti + hkrPlatforma + hkrPoluvagon + hkrSisterna + hkrBoshqa;

		model.addAttribute("HavKrpHammaPlan",HavKrpHammaPlan);
		model.addAttribute("HavKrpKritiPlan", hkrKriti);
		model.addAttribute("HavKrpPlatformaPlan", hkrPlatforma);
		model.addAttribute("HavKrpPoluvagonPlan", hkrPoluvagon);
		model.addAttribute("HavKrpSisternaPlan", hkrSisterna);
		model.addAttribute("HavKrpBoshqaPlan", hkrBoshqa);

		//andijon krp plan
		int akrKriti = planDto.getAndjKrpKritiPlanUtyMonths();
		int akrPlatforma=planDto.getAndjKrpPlatformaPlanUtyMonths();
		int akrPoluvagon=planDto.getAndjKrpPoluvagonPlanUtyMonths();
		int akrSisterna=planDto.getAndjKrpSisternaPlanUtyMonths();
		int akrBoshqa=planDto.getAndjKrpBoshqaPlanUtyMonths();
		int AndjKrpHammaPlan = akrKriti + akrPlatforma + akrPoluvagon + akrSisterna + akrBoshqa;

		model.addAttribute("AndjKrpHammaPlan",AndjKrpHammaPlan);
		model.addAttribute("AndjKrpKritiPlan", akrKriti);
		model.addAttribute("AndjKrpPlatformaPlan", akrPlatforma);
		model.addAttribute("AndjKrpPoluvagonPlan", akrPoluvagon);
		model.addAttribute("AndjKrpSisternaPlan", akrSisterna);
		model.addAttribute("AndjKrpBoshqaPlan", akrBoshqa);

		//itogo krp
		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = skrKriti + hkrKriti + akrKriti;
		int KrpPlatformaPlan = skrPlatforma + hkrPlatforma + akrPlatforma;
		int KrpPoluvagonPlan = akrPoluvagon + hkrPoluvagon + skrPoluvagon;
		int KrpSisternaPlan = skrSisterna + hkrSisterna + akrSisterna;
		int KrpBoshqaPlan = skrBoshqa + hkrBoshqa + akrBoshqa;

		model.addAttribute("KrpHammaPlan", KrpHammaPlan);
		model.addAttribute("KrpKritiPlan", KrpKritiPlan);
		model.addAttribute("KrpPlatformaPlan", KrpPlatformaPlan);
		model.addAttribute("KrpPoluvagonPlan",KrpPoluvagonPlan);
		model.addAttribute("KrpSisternaPlan", KrpSisternaPlan);
		model.addAttribute("KrpBoshqaPlan", KrpBoshqaPlan);

		//**//
		// samarqand depo tamir hamma false vagonlar soni
		int sdKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)");
		int sdPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)");
		int sdPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)");
		int sdSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)");
		int sdBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)");
		int sdHammaFalse = sdKritiFalse + sdPlatformaFalse+ sdPoluvagonFalse+ sdSisternaFalse + sdBoshqaFalse;

		model.addAttribute("sdHammaFalse",sdHammaFalse+334);
		model.addAttribute("sdKritiFalse",sdKritiFalse+134);
		model.addAttribute("sdPlatformaFalse",sdPlatformaFalse+6);
		model.addAttribute("sdPoluvagonFalse",sdPoluvagonFalse+65);
		model.addAttribute("sdSisternaFalse",sdSisternaFalse);
		model.addAttribute("sdBoshqaFalse",sdBoshqaFalse+129);

		// VCHD-3 depo tamir hamma false vagonlar soni
		int hdKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)");
		int hdPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)");
		int hdPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)");
		int hdSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)");
		int hdBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)");
		int hdHammaFalse = hdKritiFalse + hdPlatformaFalse+ hdPoluvagonFalse+ hdSisternaFalse + hdBoshqaFalse;

		model.addAttribute("hdHammaFalse",hdHammaFalse+455);
		model.addAttribute("hdKritiFalse",hdKritiFalse);
		model.addAttribute("hdPlatformaFalse",hdPlatformaFalse+26);
		model.addAttribute("hdPoluvagonFalse",hdPoluvagonFalse+109);
		model.addAttribute("hdSisternaFalse",hdSisternaFalse+4);
		model.addAttribute("hdBoshqaFalse",hdBoshqaFalse+316);

		// VCHD-5 depo tamir hamma false vagonlar soni
		int adKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)");
		int adPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)");
		int adPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)");
		int adSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)");
		int adBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)");
		int adHammaFalse = adKritiFalse + adPlatformaFalse+ adPoluvagonFalse+ adSisternaFalse + adBoshqaFalse;

		model.addAttribute("adHammaFalse",adHammaFalse+443);
		model.addAttribute("adKritiFalse",adKritiFalse+224);
		model.addAttribute("adPlatformaFalse",adPlatformaFalse+3);
		model.addAttribute("adPoluvagonFalse",adPoluvagonFalse+103);
		model.addAttribute("adSisternaFalse",adSisternaFalse);
		model.addAttribute("adBoshqaFalse",adBoshqaFalse+113);

		// depoli tamir itogo uchun
		int dHammaFalse =  adHammaFalse + hdHammaFalse+sdHammaFalse;
		int dKritiFalse = sdKritiFalse + hdKritiFalse + adKritiFalse;
		int dPlatforma = adPlatformaFalse + sdPlatformaFalse + hdPlatformaFalse;
		int dPoluvagon  = adPoluvagonFalse + sdPoluvagonFalse + hdPoluvagonFalse;
		int dSisterna = adSisternaFalse + hdSisternaFalse + sdSisternaFalse;
		int dBoshqa = adBoshqaFalse + hdBoshqaFalse + sdBoshqaFalse;

		model.addAttribute("dHammaFalse",dHammaFalse+1232);
		model.addAttribute("dKritiFalse",dKritiFalse+358);
		model.addAttribute("dPlatforma",dPlatforma+35);
		model.addAttribute("dPoluvagon",dPoluvagon+277);
		model.addAttribute("dSisterna",dSisterna+4);
		model.addAttribute("dBoshqa",dBoshqa+558);


		// samarqand KApital tamir hamma false vagonlar soni
		int skKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)");
		int skPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)");
		int skPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)");
		int skSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)");
		int skBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)");
		int skHammaFalse = skKritiFalse + skPlatformaFalse+ skPoluvagonFalse+ skSisternaFalse + skBoshqaFalse;

		model.addAttribute("skHammaFalse",skHammaFalse+16);
		model.addAttribute("skKritiFalse",skKritiFalse+16);
		model.addAttribute("skPlatformaFalse",skPlatformaFalse);
		model.addAttribute("skPoluvagonFalse",skPoluvagonFalse);
		model.addAttribute("skSisternaFalse",skSisternaFalse);
		model.addAttribute("skBoshqaFalse",skBoshqaFalse);

		// VCHD-3 kapital tamir hamma false vagonlar soni
		int hkKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Kapital ta’mir(КР)");
		int hkPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)");
		int hkPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)");
		int hkSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)");
		int hkBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)");
		int hkHammaFalse = hkKritiFalse + hkPlatformaFalse+ hkPoluvagonFalse+ hkSisternaFalse + hkBoshqaFalse;

		model.addAttribute("hkHammaFalse",hkHammaFalse+7);
		model.addAttribute("hkKritiFalse",hkKritiFalse);
		model.addAttribute("hkPlatformaFalse",hkPlatformaFalse);
		model.addAttribute("hkPoluvagonFalse",hkPoluvagonFalse+4);
		model.addAttribute("hkSisternaFalse",hkSisternaFalse);
		model.addAttribute("hkBoshqaFalse",hkBoshqaFalse+3);

		// VCHD-5 kapital tamir hamma false vagonlar soni
		int akKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)");
		int akPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)");
		int akPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)");
		int akSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)");
		int akBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)");
		int akHammaFalse = akKritiFalse + akPlatformaFalse+ akPoluvagonFalse+ akSisternaFalse + akBoshqaFalse;

		model.addAttribute("akHammaFalse",akHammaFalse+28);
		model.addAttribute("akKritiFalse",akKritiFalse+26);
		model.addAttribute("akPlatformaFalse",akPlatformaFalse);
		model.addAttribute("akPoluvagonFalse",akPoluvagonFalse+2);
		model.addAttribute("akSisternaFalse",akSisternaFalse);
		model.addAttribute("akBoshqaFalse",akBoshqaFalse);

		// Kapital tamir itogo uchun
		int kHammaFalse =  akHammaFalse + hkHammaFalse+skHammaFalse;
		int kKritiFalse = skKritiFalse + hkKritiFalse + akKritiFalse;
		int kPlatforma = akPlatformaFalse + skPlatformaFalse + hkPlatformaFalse;
		int kPoluvagon  = akPoluvagonFalse + skPoluvagonFalse + hkPoluvagonFalse;
		int kSisterna = akSisternaFalse + hkSisternaFalse + skSisternaFalse;
		int kBoshqa = akBoshqaFalse + hkBoshqaFalse + skBoshqaFalse;

		model.addAttribute("kHammaFalse",kHammaFalse+51);
		model.addAttribute("kKritiFalse",kKritiFalse+42);
		model.addAttribute("kPlatforma",kPlatforma);
		model.addAttribute("kPoluvagon",kPoluvagon+6);
		model.addAttribute("kSisterna",kSisterna);
		model.addAttribute("kBoshqa",kBoshqa+3);

		//**
		// samarqand KRP tamir hamma false vagonlar soni
		int skrKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)");
		int skrPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)");
		int skrPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)");
		int skrSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)");
		int skrBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)");
		int skrHammaFalse = skrKritiFalse + skrPlatformaFalse+ skrPoluvagonFalse+ skrSisternaFalse + skrBoshqaFalse;

		model.addAttribute("skrHammaFalse",skrHammaFalse+89);
		model.addAttribute("skrKritiFalse",skrKritiFalse);
		model.addAttribute("skrPlatformaFalse",skrPlatformaFalse);
		model.addAttribute("skrPoluvagonFalse",skrPoluvagonFalse+88);
		model.addAttribute("skrSisternaFalse",skrSisternaFalse+1);
		model.addAttribute("skrBoshqaFalse",skrBoshqaFalse);

		// VCHD-3 KRP tamir hamma false vagonlar soni
		int hkrKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","KRP(КРП)");
		int hkrPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)");
		int hkrPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)");
		int hkrSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)");
		int hkrBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)");
		int hkrHammaFalse = hkrKritiFalse + hkrPlatformaFalse+ hkrPoluvagonFalse+ hkrSisternaFalse + hkrBoshqaFalse;

		model.addAttribute("hkrHammaFalse",hkrHammaFalse+83);
		model.addAttribute("hkrKritiFalse",hkrKritiFalse);
		model.addAttribute("hkrPlatformaFalse",hkrPlatformaFalse);
		model.addAttribute("hkrPoluvagonFalse",hkrPoluvagonFalse+83);
		model.addAttribute("hkrSisternaFalse",hkrSisternaFalse);
		model.addAttribute("hkrBoshqaFalse",hkrBoshqaFalse);

		// VCHD-5 KRP tamir hamma false vagonlar soni
		int akrKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)");
		int akrPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)");
		int akrPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)");
		int akrSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)");
		int akrBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)");
		int akrHammaFalse = akrKritiFalse + akrPlatformaFalse+ akrPoluvagonFalse+ akrSisternaFalse + akrBoshqaFalse;

		model.addAttribute("akrHammaFalse",akrHammaFalse+61);
		model.addAttribute("akrKritiFalse",akrKritiFalse);
		model.addAttribute("akrPlatformaFalse",akrPlatformaFalse);
		model.addAttribute("akrPoluvagonFalse",akrPoluvagonFalse+61);
		model.addAttribute("akrSisternaFalse",akrSisternaFalse);
		model.addAttribute("akBoshqaFalse",akBoshqaFalse);

		// Krp itogo uchun
		int krHammaFalse =  akrHammaFalse + hkrHammaFalse+skrHammaFalse;
		int krKritiFalse = skrKritiFalse + hkrKritiFalse + akrKritiFalse;
		int krPlatforma = akrPlatformaFalse + skrPlatformaFalse + hkrPlatformaFalse;
		int krPoluvagon  = akrPoluvagonFalse + skrPoluvagonFalse + hkrPoluvagonFalse;
		int krSisterna = akrSisternaFalse + hkrSisternaFalse + skrSisternaFalse;
		int krBoshqa = akrBoshqaFalse + hkrBoshqaFalse + skrBoshqaFalse;

		model.addAttribute("krHammaFalse",krHammaFalse+233);
		model.addAttribute("krKritiFalse",krKritiFalse);
		model.addAttribute("krPlatforma",krPlatforma);
		model.addAttribute("krPoluvagon",krPoluvagon+232);
		model.addAttribute("krSisterna",krSisterna+1);
		model.addAttribute("krBoshqa",krBoshqa);

		//yuklab olish uchun list
		List<Integer> vagonsToDownloadTable = new ArrayList<>();

		//Depoli tamir
		vagonsToDownloadTable.add(HavDtHammaPlan);
		vagonsToDownloadTable.add(hdHammaFalse + 455);
		vagonsToDownloadTable.add(hdKriti);
		vagonsToDownloadTable.add(hdKritiFalse);
		vagonsToDownloadTable.add(hdPlatforma);
		vagonsToDownloadTable.add(hdPlatformaFalse + 26);
		vagonsToDownloadTable.add(hdPoluvagon);
		vagonsToDownloadTable.add(hdPoluvagonFalse + 109);
		vagonsToDownloadTable.add(hdSisterna);
		vagonsToDownloadTable.add(hdSisternaFalse + 4);
		vagonsToDownloadTable.add(hdBoshqa);
		vagonsToDownloadTable.add(hdBoshqaFalse + 316);

		vagonsToDownloadTable.add(AndjDtHammaPlan);
		vagonsToDownloadTable.add(adHammaFalse + 443);
		vagonsToDownloadTable.add(adKriti);
		vagonsToDownloadTable.add(adKritiFalse + 224);
		vagonsToDownloadTable.add(adPlatforma);
		vagonsToDownloadTable.add(adPlatformaFalse + 3);
		vagonsToDownloadTable.add(adPoluvagon);
		vagonsToDownloadTable.add(adPoluvagonFalse + 103);
		vagonsToDownloadTable.add(adSisterna);
		vagonsToDownloadTable.add(adSisternaFalse);
		vagonsToDownloadTable.add(adBoshqa);
		vagonsToDownloadTable.add(adBoshqaFalse + 113);

		vagonsToDownloadTable.add(SamDtHammaPlan);
		vagonsToDownloadTable.add(sdHammaFalse + 334);
		vagonsToDownloadTable.add(sdKriti);
		vagonsToDownloadTable.add(sdKritiFalse + 134);
		vagonsToDownloadTable.add(sdPlatforma);
		vagonsToDownloadTable.add(sdPlatformaFalse + 6);
		vagonsToDownloadTable.add(sdPoluvagon);
		vagonsToDownloadTable.add(sdPoluvagonFalse + 65);
		vagonsToDownloadTable.add(sdSisterna);
		vagonsToDownloadTable.add(sdSisternaFalse );
		vagonsToDownloadTable.add(sdBoshqa);
		vagonsToDownloadTable.add(sdBoshqaFalse + 129);;

		vagonsToDownloadTable.add(DtHammaPlan);
		vagonsToDownloadTable.add(dHammaFalse+1232);
		vagonsToDownloadTable.add(DtKritiPlan);
		vagonsToDownloadTable.add(dKritiFalse+358);
		vagonsToDownloadTable.add(DtPlatformaPlan);
		vagonsToDownloadTable.add(dPlatforma+35);
		vagonsToDownloadTable.add(DtPoluvagonPlan);
		vagonsToDownloadTable.add(dPoluvagon+277);
		vagonsToDownloadTable.add(DtSisternaPlan);
		vagonsToDownloadTable.add(dSisterna+4 );
		vagonsToDownloadTable.add(DtBoshqaPlan);
		vagonsToDownloadTable.add(dBoshqa+558);

		//kapital tamir
		vagonsToDownloadTable.add(HavKtHammaPlan);
		vagonsToDownloadTable.add(hkHammaFalse + 7);
		vagonsToDownloadTable.add(hkKriti);
		vagonsToDownloadTable.add(hkKritiFalse);
		vagonsToDownloadTable.add(hkPlatforma);
		vagonsToDownloadTable.add(hkPlatformaFalse);
		vagonsToDownloadTable.add(hkPoluvagon);
		vagonsToDownloadTable.add(hkPoluvagonFalse + 4);
		vagonsToDownloadTable.add(hkSisterna);
		vagonsToDownloadTable.add(hkSisternaFalse);
		vagonsToDownloadTable.add(hkBoshqa);
		vagonsToDownloadTable.add(hkBoshqaFalse + 3);

		vagonsToDownloadTable.add(AndjKtHammaPlan);
		vagonsToDownloadTable.add(akHammaFalse + 28);
		vagonsToDownloadTable.add(akKriti);
		vagonsToDownloadTable.add(akKritiFalse + 26);
		vagonsToDownloadTable.add(akPlatforma);
		vagonsToDownloadTable.add(akPlatformaFalse);
		vagonsToDownloadTable.add(akPoluvagon);
		vagonsToDownloadTable.add(akPoluvagonFalse + 2);
		vagonsToDownloadTable.add(akSisterna);
		vagonsToDownloadTable.add(akSisternaFalse);
		vagonsToDownloadTable.add(akBoshqa);
		vagonsToDownloadTable.add(akBoshqaFalse);

		vagonsToDownloadTable.add(SamKtHammaPlan);
		vagonsToDownloadTable.add(skHammaFalse + 16);
		vagonsToDownloadTable.add(skKriti);
		vagonsToDownloadTable.add(skKritiFalse + 16);
		vagonsToDownloadTable.add(skPlatforma);
		vagonsToDownloadTable.add(skPlatformaFalse);
		vagonsToDownloadTable.add(skPoluvagon);
		vagonsToDownloadTable.add(skPoluvagonFalse);
		vagonsToDownloadTable.add(skSisterna);
		vagonsToDownloadTable.add(skSisternaFalse );
		vagonsToDownloadTable.add(skBoshqa);
		vagonsToDownloadTable.add(skBoshqaFalse);

		vagonsToDownloadTable.add(KtHammaPlan);
		vagonsToDownloadTable.add(kHammaFalse + 51);
		vagonsToDownloadTable.add(KtKritiPlan);
		vagonsToDownloadTable.add(kKritiFalse + 42);
		vagonsToDownloadTable.add(KtPlatformaPlan);
		vagonsToDownloadTable.add(kPlatforma);
		vagonsToDownloadTable.add(KtPoluvagonPlan);
		vagonsToDownloadTable.add(kPoluvagon + 6);
		vagonsToDownloadTable.add(KtSisternaPlan);
		vagonsToDownloadTable.add(kSisterna);
		vagonsToDownloadTable.add(KtBoshqaPlan);
		vagonsToDownloadTable.add(kBoshqa + 3);

		//KRPP
		vagonsToDownloadTable.add(HavKrpHammaPlan);
		vagonsToDownloadTable.add(hkrHammaFalse + 83);
		vagonsToDownloadTable.add(hkrKriti);
		vagonsToDownloadTable.add(hkrKritiFalse);
		vagonsToDownloadTable.add(hkrPlatforma);
		vagonsToDownloadTable.add(hkrPlatformaFalse);
		vagonsToDownloadTable.add(hkrPoluvagon);
		vagonsToDownloadTable.add(hkrPoluvagonFalse + 83);
		vagonsToDownloadTable.add(hkrSisterna);
		vagonsToDownloadTable.add(hkrSisternaFalse);
		vagonsToDownloadTable.add(hkrBoshqa);
		vagonsToDownloadTable.add(hkrBoshqaFalse);

		vagonsToDownloadTable.add(AndjKrpHammaPlan);
		vagonsToDownloadTable.add(akrHammaFalse + 61);
		vagonsToDownloadTable.add(akrKriti);
		vagonsToDownloadTable.add(akrKritiFalse);
		vagonsToDownloadTable.add(akrPlatforma);
		vagonsToDownloadTable.add(akrPlatformaFalse);
		vagonsToDownloadTable.add(akrPoluvagon);
		vagonsToDownloadTable.add(akrPoluvagonFalse + 61);
		vagonsToDownloadTable.add(akrSisterna);
		vagonsToDownloadTable.add(akrSisternaFalse);
		vagonsToDownloadTable.add(akrBoshqa);
		vagonsToDownloadTable.add(akrBoshqaFalse);

		vagonsToDownloadTable.add(SamKrpHammaPlan);
		vagonsToDownloadTable.add(skrHammaFalse + 89);
		vagonsToDownloadTable.add(skrKriti);
		vagonsToDownloadTable.add(skrKritiFalse);
		vagonsToDownloadTable.add(skrPlatforma);
		vagonsToDownloadTable.add(skrPlatformaFalse);
		vagonsToDownloadTable.add(skrPoluvagon);
		vagonsToDownloadTable.add(skrPoluvagonFalse + 88);
		vagonsToDownloadTable.add(skrSisterna);
		vagonsToDownloadTable.add(skrSisternaFalse + 1);
		vagonsToDownloadTable.add(skrBoshqa);
		vagonsToDownloadTable.add(skrBoshqaFalse);

		vagonsToDownloadTable.add(KrpHammaPlan);
		vagonsToDownloadTable.add(krHammaFalse + 233);
		vagonsToDownloadTable.add(KrpKritiPlan);
		vagonsToDownloadTable.add(krKritiFalse);
		vagonsToDownloadTable.add(KrpPlatformaPlan);
		vagonsToDownloadTable.add(krPlatforma);
		vagonsToDownloadTable.add(KrpPoluvagonPlan);
		vagonsToDownloadTable.add(krPoluvagon + 232);
		vagonsToDownloadTable.add(KrpSisternaPlan);
		vagonsToDownloadTable.add(krSisterna + 1);
		vagonsToDownloadTable.add(KrpBoshqaPlan);
		vagonsToDownloadTable.add(krBoshqa);

		vagonsToDownloadAllTable = vagonsToDownloadTable;
    	
    	return "planTableForMonthsUty";
    }
    
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
   	@GetMapping("/vagons/filterOneMonthUty")
   	public String filterByDepoNomi(Model model,  @RequestParam(value = "depoNomi", required = false) String depoNomi,
   												@RequestParam(value = "vagonTuri", required = false) String vagonTuri) {
		String oy=null;
		switch (month) {
			case 1:
				oy = "-01";
				break;
			case 2:
				oy = "-02";
				break;
			case 3:
				oy = "-03";
				break;
			case 4:
				oy = "-04";
				break;
			case 5:
				oy = "-05";
				break;
			case 6:
				oy = "-06";
				break;
			case 7:
				oy = "-07";
				break;
			case 8:
				oy = "-08";
				break;
			case 9:
				oy = "-09";
				break;
			case 10:
				oy = "-10";
				break;
			case 11:
				oy = "-11";
				break;
			case 12:
				oy = "-12";
				break;
		}

   		if(!depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") ) {
   			vagonsToDownload = vagonTayyorUtyService.findAllByDepoNomiAndVagonTuri(depoNomi, vagonTuri, oy);
   			model.addAttribute("vagons", vagonTayyorUtyService.findAllByDepoNomiAndVagonTuri(depoNomi, vagonTuri, oy));
   		}else if(depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") ){
   			vagonsToDownload = vagonTayyorUtyService.findAllByVagonTuri(vagonTuri, oy);
   			model.addAttribute("vagons", vagonTayyorUtyService.findAllByVagonTuri(vagonTuri, oy));
   		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi") ){
   			vagonsToDownload = vagonTayyorUtyService.findAllByDepoNomi(depoNomi, oy );
   			model.addAttribute("vagons", vagonTayyorUtyService.findAllByDepoNomi(depoNomi , oy));
   		}else {
   			vagonsToDownload = vagonTayyorUtyService.findAll(oy);
   			model.addAttribute("vagons", vagonTayyorUtyService.findAll(oy));
   		}

		//vaqtni olib turadi
		model.addAttribute("samDate",vagonTayyorUtyService.getSamDate());
		model.addAttribute("havDate", vagonTayyorUtyService.getHavDate());
		model.addAttribute("andjDate",vagonTayyorUtyService.getAndjDate());

		PlanUty planDto = vagonTayyorUtyService.getPlanuty();
		//planlar kiritish

		//havos hamma plan
		int HavDtHammaPlan = planDto.getHavDtKritiPlanUty() + planDto.getHavDtPlatformaPlanUty() + planDto.getHavDtPoluvagonPlanUty() + planDto.getHavDtSisternaPlanUty() + planDto.getHavDtBoshqaPlanUty();
		int HavDtKritiPlan =  planDto.getHavDtKritiPlanUty();
		int HavDtPlatformaPlan =  planDto.getHavDtPlatformaPlanUty();
		int HavDtPoluvagonPlan =  planDto.getHavDtPoluvagonPlanUty();
		int HavDtSisternaPlan =  planDto.getHavDtSisternaPlanUty();
		int HavDtBoshqaPlan =  planDto.getHavDtBoshqaPlanUty();


		model.addAttribute("HavDtHammaPlan", HavDtHammaPlan);
		model.addAttribute("HavDtKritiPlan", HavDtKritiPlan);
		model.addAttribute("HavDtPlatformaPlan", HavDtPlatformaPlan);
		model.addAttribute("HavDtPoluvagonPlan", HavDtPoluvagonPlan);
		model.addAttribute("HavDtSisternaPlan", HavDtSisternaPlan);
		model.addAttribute("HavDtBoshqaPlan", HavDtBoshqaPlan);

		//andijon hamma plan depo tamir
		int AndjDtHammaPlan = planDto.getAndjDtKritiPlanUty() + planDto.getAndjDtPlatformaPlanUty() + planDto.getAndjDtPoluvagonPlanUty() + planDto.getAndjDtSisternaPlanUty() + planDto.getAndjDtBoshqaPlanUty();
		int AndjDtKritiPlan =  planDto.getAndjDtKritiPlanUty();
		int AndjDtPlatformaPlan =  planDto.getAndjDtPlatformaPlanUty();
		int AndjDtPoluvagonPlan =  planDto.getAndjDtPoluvagonPlanUty();
		int AndjDtSisternaPlan =  planDto.getAndjDtSisternaPlanUty();
		int AndjDtBoshqaPlan =  planDto.getAndjDtBoshqaPlanUty();

		model.addAttribute("AndjDtHammaPlan", AndjDtHammaPlan);
		model.addAttribute("AndjDtKritiPlan", AndjDtKritiPlan);
		model.addAttribute("AndjDtPlatformaPlan", AndjDtPlatformaPlan);
		model.addAttribute("AndjDtPoluvagonPlan", AndjDtPoluvagonPlan);
		model.addAttribute("AndjDtSisternaPlan", AndjDtSisternaPlan);
		model.addAttribute("AndjDtBoshqaPlan", AndjDtBoshqaPlan);

		//samarqand depo tamir
		int SamDtHammaPlan=planDto.getSamDtKritiPlanUty() + planDto.getSamDtPlatformaPlanUty() + planDto.getSamDtPoluvagonPlanUty() + planDto.getSamDtSisternaPlanUty() + planDto.getSamDtBoshqaPlanUty();
		int SamDtKritiPlan =  planDto.getSamDtKritiPlanUty();
		int SamDtPlatformaPlan =  planDto.getSamDtPlatformaPlanUty();
		int SamDtPoluvagonPlan =  planDto.getSamDtPoluvagonPlanUty();
		int SamDtSisternaPlan =  planDto.getSamDtSisternaPlanUty();
		int SamDtBoshqaPlan =  planDto.getSamDtBoshqaPlanUty();

		model.addAttribute("SamDtHammaPlan",SamDtHammaPlan);
		model.addAttribute("SamDtKritiPlan", SamDtKritiPlan);
		model.addAttribute("SamDtPlatformaPlan", SamDtPlatformaPlan);
		model.addAttribute("SamDtPoluvagonPlan", SamDtPoluvagonPlan);
		model.addAttribute("SamDtSisternaPlan", SamDtSisternaPlan);
		model.addAttribute("SamDtBoshqaPlan", SamDtBoshqaPlan);

		// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = planDto.getAndjDtKritiPlanUty() + planDto.getHavDtKritiPlanUty() + planDto.getSamDtKritiPlanUty();
		int DtPlatformaPlan = planDto.getAndjDtPlatformaPlanUty() + planDto.getHavDtPlatformaPlanUty() + planDto.getSamDtPlatformaPlanUty();
		int DtPoluvagonPlan = planDto.getAndjDtPoluvagonPlanUty() + planDto.getHavDtPoluvagonPlanUty() + planDto.getSamDtPoluvagonPlanUty();
		int DtSisternaPlan = planDto.getAndjDtSisternaPlanUty() + planDto.getHavDtSisternaPlanUty() + planDto.getSamDtSisternaPlanUty();
		int DtBoshqaPlan = planDto.getAndjDtBoshqaPlanUty() + planDto.getHavDtBoshqaPlanUty() + planDto.getSamDtBoshqaPlanUty();

		model.addAttribute("DtHammaPlan", DtHammaPlan);
		model.addAttribute("DtKritiPlan", DtKritiPlan);
		model.addAttribute("DtPlatformaPlan", DtPlatformaPlan);
		model.addAttribute("DtPoluvagonPlan", DtPoluvagonPlan);
		model.addAttribute("DtSisternaPlan", DtSisternaPlan);
		model.addAttribute("DtBoshqaPlan", DtBoshqaPlan);


		//havos kapital tamir uchun plan
		int HavKtHammaPlan = planDto.getHavKtKritiPlanUty() + planDto.getHavKtPlatformaPlanUty() + planDto.getHavKtPoluvagonPlanUty() + planDto.getHavKtSisternaPlanUty() + planDto.getHavKtBoshqaPlanUty();
		int HavKtKritiPlan = planDto.getHavKtKritiPlanUty();
		int HavKtPlatformaPlan = planDto.getHavKtPlatformaPlanUty();
		int HavKtPoluvagonPlan = planDto.getHavKtPoluvagonPlanUty();
		int HavKtSisternaPlan = planDto.getHavKtSisternaPlanUty();
		int HavKtBoshqaPlan = planDto.getHavKtBoshqaPlanUty();

		model.addAttribute("HavKtHammaPlan", HavKtHammaPlan);
		model.addAttribute("HavKtKritiPlan", HavKtKritiPlan);
		model.addAttribute("HavKtPlatformaPlan", HavKtPlatformaPlan);
		model.addAttribute("HavKtPoluvagonPlan", HavKtPoluvagonPlan);
		model.addAttribute("HavKtSisternaPlan", HavKtSisternaPlan);
		model.addAttribute("HavKtBoshqaPlan", HavKtBoshqaPlan);

		//VCHD-5 kapital tamir uchun plan
		int AndjKtHammaPlan = planDto.getAndjKtKritiPlanUty() + planDto.getAndjKtPlatformaPlanUty() + planDto.getAndjKtPoluvagonPlanUty() + planDto.getAndjKtSisternaPlanUty() + planDto.getAndjKtBoshqaPlanUty();
		int AndjKtKritiPlan =  planDto.getAndjKtKritiPlanUty();
		int AndjKtPlatformaPlan =  planDto.getAndjKtPlatformaPlanUty();
		int AndjKtPoluvagonPlan =  planDto.getAndjKtPoluvagonPlanUty();
		int AndjKtSisternaPlan =  planDto.getAndjKtSisternaPlanUty();
		int AndjKtBoshqaPlan =  planDto.getAndjKtBoshqaPlanUty();

		model.addAttribute("AndjKtHammaPlan", AndjKtHammaPlan);
		model.addAttribute("AndjKtKritiPlan", AndjKtKritiPlan);
		model.addAttribute("AndjKtPlatformaPlan", AndjKtPlatformaPlan);
		model.addAttribute("AndjKtPoluvagonPlan", AndjKtPoluvagonPlan);
		model.addAttribute("AndjKtSisternaPlan", AndjKtSisternaPlan);
		model.addAttribute("AndjKtBoshqaPlan", AndjKtBoshqaPlan);


		//VCHD-6 kapital tamir uchun plan
		int SamKtHammaPlan =  planDto.getSamKtKritiPlanUty() + planDto.getSamKtPlatformaPlanUty() + planDto.getSamKtPoluvagonPlanUty() + planDto.getSamKtSisternaPlanUty() + planDto.getSamKtBoshqaPlanUty();
		int SamKtKritiPlan =  planDto.getSamKtKritiPlanUty();
		int SamKtPlatformaPlan =  planDto.getSamKtPlatformaPlanUty();
		int SamKtPoluvagonPlan =  planDto.getSamKtPoluvagonPlanUty();
		int SamKtSisternaPlan =  planDto.getSamKtSisternaPlanUty();
		int SamKtBoshqaPlan =  planDto.getSamKtBoshqaPlanUty();

		model.addAttribute("SamKtHammaPlan", SamKtHammaPlan);
		model.addAttribute("SamKtKritiPlan", SamKtKritiPlan);
		model.addAttribute("SamKtPlatformaPlan", SamKtPlatformaPlan);
		model.addAttribute("SamKtPoluvagonPlan", SamKtPoluvagonPlan);
		model.addAttribute("SamKtSisternaPlan", SamKtSisternaPlan);
		model.addAttribute("SamKtBoshqaPlan", SamKtBoshqaPlan);

		//kapital itogo
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = planDto.getAndjKtKritiPlanUty() + planDto.getHavKtKritiPlanUty() + planDto.getSamKtKritiPlanUty();
		int KtPlatformaPlan = planDto.getAndjKtPlatformaPlanUty() + planDto.getHavKtPlatformaPlanUty() + planDto.getSamKtPlatformaPlanUty();
		int KtPoluvagonPlan = planDto.getAndjKtPoluvagonPlanUty() + planDto.getHavKtPoluvagonPlanUty() + planDto.getSamKtPoluvagonPlanUty();
		int KtSisternaPlan = planDto.getAndjKtSisternaPlanUty() + planDto.getHavKtSisternaPlanUty() + planDto.getSamKtSisternaPlanUty();
		int KtBoshqaPlan = planDto.getAndjKtBoshqaPlanUty() + planDto.getHavKtBoshqaPlanUty() + planDto.getSamKtBoshqaPlanUty();

		model.addAttribute("KtHammaPlan", KtHammaPlan);
		model.addAttribute("KtKritiPlan", KtKritiPlan);
		model.addAttribute("KtPlatformaPlan", KtPlatformaPlan);
		model.addAttribute("KtPoluvagonPlan", KtPoluvagonPlan);
		model.addAttribute("KtSisternaPlan", KtSisternaPlan);
		model.addAttribute("KtBoshqaPlan", KtBoshqaPlan);

		//VCHD-3 KRP plan
		int HavKrpHammaPlan =  planDto.getHavKrpKritiPlanUty() + planDto.getHavKrpPlatformaPlanUty() + planDto.getHavKrpPoluvagonPlanUty() + planDto.getHavKrpSisternaPlanUty() + planDto.getHavKrpBoshqaPlanUty();
		int HavKrpKritiPlan = planDto.getHavKrpKritiPlanUty();
		int HavKrpPlatformaPlan = planDto.getHavKrpPlatformaPlanUty();
		int HavKrpPoluvagonPlan = planDto.getHavKrpPoluvagonPlanUty();
		int HavKrpSisternaPlan = planDto.getHavKrpSisternaPlanUty();
		int HavKrpBoshqaPlan = planDto.getHavKrpBoshqaPlanUty();

		model.addAttribute("HavKrpHammaPlan", HavKrpHammaPlan);
		model.addAttribute("HavKrpKritiPlan", HavKrpKritiPlan);
		model.addAttribute("HavKrpPlatformaPlan", HavKrpPlatformaPlan);
		model.addAttribute("HavKrpPoluvagonPlan", HavKrpPoluvagonPlan);
		model.addAttribute("HavKrpSisternaPlan", HavKrpSisternaPlan);
		model.addAttribute("HavKrpBoshqaPlan", HavKrpBoshqaPlan);

		//VCHD-5 Krp plan
		int AndjKrpHammaPlan =  planDto.getAndjKrpKritiPlanUty() + planDto.getAndjKrpPlatformaPlanUty() + planDto.getAndjKrpPoluvagonPlanUty() + planDto.getAndjKrpSisternaPlanUty() + planDto.getAndjKrpBoshqaPlanUty();
		int AndjKrpKritiPlan = planDto.getAndjKrpKritiPlanUty();
		int AndjKrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanUty();
		int AndjKrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanUty();
		int AndjKrpSisternaPlan = planDto.getAndjKrpSisternaPlanUty();
		int AndjKrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanUty();

		model.addAttribute("AndjKrpHammaPlan", AndjKrpHammaPlan);
		model.addAttribute("AndjKrpKritiPlan", AndjKrpKritiPlan);
		model.addAttribute("AndjKrpPlatformaPlan", AndjKrpPlatformaPlan);
		model.addAttribute("AndjKrpPoluvagonPlan", AndjKrpPoluvagonPlan);
		model.addAttribute("AndjKrpSisternaPlan", AndjKrpSisternaPlan);
		model.addAttribute("AndjKrpBoshqaPlan", AndjKrpBoshqaPlan);

		//samarqand KRP plan
		int SamKrpHammaPlan = planDto.getSamKrpKritiPlanUty() + planDto.getSamKrpPlatformaPlanUty() + planDto.getSamKrpPoluvagonPlanUty() + planDto.getSamKrpSisternaPlanUty() + planDto.getSamKrpBoshqaPlanUty();
		int SamKrpKritiPlan = planDto.getSamKrpKritiPlanUty();
		int SamKrpPlatformaPlan = planDto.getSamKrpPlatformaPlanUty();
		int SamKrpPoluvagonPlan = planDto.getSamKrpPoluvagonPlanUty();
		int SamKrpSisternaPlan = planDto.getSamKrpSisternaPlanUty();
		int SamKrpBoshqaPlan = planDto.getSamKrpBoshqaPlanUty();

		model.addAttribute("SamKrpHammaPlan", SamKrpHammaPlan);
		model.addAttribute("SamKrpKritiPlan", SamKrpKritiPlan);
		model.addAttribute("SamKrpPlatformaPlan", SamKrpPlatformaPlan);
		model.addAttribute("SamKrpPoluvagonPlan", SamKrpPoluvagonPlan);
		model.addAttribute("SamKrpSisternaPlan", SamKrpSisternaPlan);
		model.addAttribute("SamKrpBoshqaPlan", SamKrpBoshqaPlan);

		//Krp itogo plan
		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = planDto.getAndjKrpKritiPlanUty() + planDto.getHavKrpKritiPlanUty() + planDto.getSamKrpKritiPlanUty();
		int KrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanUty() + planDto.getHavKrpPlatformaPlanUty() + planDto.getSamKrpPlatformaPlanUty();
		int KrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanUty() + planDto.getHavKrpPoluvagonPlanUty() + planDto.getSamKrpPoluvagonPlanUty();
		int KrpSisternaPlan = planDto.getAndjKrpSisternaPlanUty() + planDto.getHavKrpSisternaPlanUty() + planDto.getSamKrpSisternaPlanUty();
		int KrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanUty() + planDto.getHavKrpBoshqaPlanUty() + planDto.getSamKrpBoshqaPlanUty();

		model.addAttribute("KrpHammaPlan", KrpHammaPlan);
		model.addAttribute("KrpKritiPlan", KrpKritiPlan);
		model.addAttribute("KrpPlatformaPlan", KrpPlatformaPlan);
		model.addAttribute("KrpPoluvagonPlan", KrpPoluvagonPlan);
		model.addAttribute("KrpSisternaPlan", KrpSisternaPlan);
		model.addAttribute("KrpBoshqaPlan", KrpBoshqaPlan);


		// factlar

		//VCHD-3 uchun depli tamir
		int hdHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		int hdKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy);
		int hdPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)", oy);
		int hdPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy);
		int hdSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)", oy);
		int hdBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		model.addAttribute("hdHamma",hdHamma);
		model.addAttribute("hdKriti", hdKriti);
		model.addAttribute("hdPlatforma", hdPlatforma);
		model.addAttribute("hdPoluvagon", hdPoluvagon);
		model.addAttribute("hdSisterna", hdSisterna);
		model.addAttribute("hdBoshqa", hdBoshqa);


		//VCHD-5 uchun depli tamir
		int adHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		int adKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy);
		int adPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)", oy);
		int adPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy);
		int adSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)", oy);
		int adBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		model.addAttribute("adHamma",adHamma);
		model.addAttribute("adKriti", adKriti);
		model.addAttribute("adPlatforma", adPlatforma);
		model.addAttribute("adPoluvagon", adPoluvagon);
		model.addAttribute("adSisterna", adSisterna);
		model.addAttribute("adBoshqa", adBoshqa);

		//samarqand uchun depli tamir
		int sdHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		int sdKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy);
		int sdPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)", oy);
		int sdPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy);
		int sdSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)", oy);
		int sdBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		model.addAttribute("sdHamma",sdHamma);
		model.addAttribute("sdKriti", sdKriti);
		model.addAttribute("sdPlatforma", sdPlatforma);
		model.addAttribute("sdPoluvagon", sdPoluvagon);
		model.addAttribute("sdSisterna", sdSisterna);
		model.addAttribute("sdBoshqa", sdBoshqa);


		// itogo Fact uchun depli tamir
		int uvtdhamma = sdHamma + hdHamma + adHamma;
		int uvtdKriti = sdKriti + hdKriti + adKriti;
		int uvtdPlatforma = sdPlatforma + adPlatforma + hdPlatforma;
		int uvtdPoluvagon = sdPoluvagon + hdPoluvagon + adPoluvagon;
		int uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
		int uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

		model.addAttribute("uvtdhamma",uvtdhamma);
		model.addAttribute("uvtdKriti",uvtdKriti);
		model.addAttribute("uvtdPlatforma",uvtdPlatforma);
		model.addAttribute("uvtdPoluvagon",uvtdPoluvagon);
		model.addAttribute("uvtdSisterna",uvtdSisterna);
		model.addAttribute("uvtdBoshqa",uvtdBoshqa);


		//VCHD-3 uchun kapital tamir
		int hkHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		int hkKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)"," Kapital ta’mir(КР)", oy);
		int hkPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)", oy);
		int hkPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy);
		int hkSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)", oy);
		int hkBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		model.addAttribute("hkHamma",hkHamma);
		model.addAttribute("hkKriti", hkKriti);
		model.addAttribute("hkPlatforma", hkPlatforma);
		model.addAttribute("hkPoluvagon", hkPoluvagon);
		model.addAttribute("hkSisterna", hkSisterna);
		model.addAttribute("hkBoshqa", hkBoshqa);

		//VCHD-5 uchun kapital tamir
		int akHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		int akKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy);
		int akPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)", oy);
		int akPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy);
		int akSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)", oy);
		int akBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		model.addAttribute("akHamma",akHamma);
		model.addAttribute("akKriti", akKriti);
		model.addAttribute("akPlatforma", akPlatforma);
		model.addAttribute("akPoluvagon", akPoluvagon);
		model.addAttribute("akSisterna", akSisterna);
		model.addAttribute("akBoshqa", akBoshqa);

		//samarqand uchun Kapital tamir
		int skHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		int skKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy);
		int skPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)", oy);
		int skPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy);
		int skSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)", oy);
		int skBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		model.addAttribute("skHamma",skHamma);
		model.addAttribute("skKriti", skKriti);
		model.addAttribute("skPlatforma", skPlatforma);
		model.addAttribute("skPoluvagon", skPoluvagon);
		model.addAttribute("skSisterna", skSisterna);
		model.addAttribute("skBoshqa", skBoshqa);

		// itogo Fact uchun kapital tamir
		int uvtkhamma = skHamma + hkHamma + akHamma;
		int uvtkKriti = skKriti + hkKriti + akKriti;
		int uvtkPlatforma = skPlatforma + akPlatforma + hkPlatforma;
		int uvtkPoluvagon = skPoluvagon + hkPoluvagon + akPoluvagon;
		int uvtkSisterna = akSisterna + hkSisterna + skSisterna;
		int uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

		model.addAttribute("uvtkhamma",uvtkhamma);
		model.addAttribute("uvtkKriti",uvtkKriti);
		model.addAttribute("uvtkPlatforma",uvtkPlatforma);
		model.addAttribute("uvtkPoluvagon",uvtkPoluvagon);
		model.addAttribute("uvtkSisterna",uvtkSisterna);
		model.addAttribute("uvtkBoshqa",uvtkBoshqa);

		//VCHD-3 uchun KRP
		int hkrHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)", oy);

		int hkrKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)"," KRP(КРП)", oy);
		int hkrPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)", oy);
		int hkrPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)", oy);
		int hkrSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)", oy);
		int hkrBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)", oy);

		model.addAttribute("hkrHamma",hkrHamma);
		model.addAttribute("hkrKriti", hkrKriti);
		model.addAttribute("hkrPlatforma", hkrPlatforma);
		model.addAttribute("hkrPoluvagon", hkrPoluvagon);
		model.addAttribute("hkrSisterna", hkrSisterna);
		model.addAttribute("hkrBoshqa", hkrBoshqa);

		//VCHD-5 uchun KRP
		int akrHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)", oy);

		int akrKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)", oy);
		int akrPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)", oy);
		int akrPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)", oy);
		int akrSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)", oy);
		int akrBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)", oy);

		model.addAttribute("akrHamma",akrHamma);
		model.addAttribute("akrKriti", akrKriti);
		model.addAttribute("akrPlatforma", akrPlatforma);
		model.addAttribute("akrPoluvagon", akrPoluvagon);
		model.addAttribute("akrSisterna", akrSisterna);
		model.addAttribute("akrBoshqa", akrBoshqa);

		//samarqand uchun KRP tamir
		int skrHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)", oy) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)", oy);

		int skrKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)", oy);
		int skrPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)", oy);
		int skrPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)", oy);
		int skrSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)", oy);
		int skrBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)", oy);

		model.addAttribute("skrHamma",skrHamma);
		model.addAttribute("skrKriti", skrKriti);
		model.addAttribute("skrPlatforma", skrPlatforma);
		model.addAttribute("skrPoluvagon", skrPoluvagon);
		model.addAttribute("skrSisterna", skrSisterna);
		model.addAttribute("skrBoshqa", skrBoshqa);


		// itogo Fact uchun KRP
		int uvtkrhamma = skrHamma + hkrHamma + akrHamma;
		int uvtkrKriti = skrKriti + hkrKriti + akrKriti;
		int uvtkrPlatforma = skrPlatforma + akrPlatforma + hkrPlatforma;
		int uvtkrPoluvagon = skrPoluvagon + hkrPoluvagon + akrPoluvagon;
		int uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
		int uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

		model.addAttribute("uvtkrhamma",uvtkrhamma);
		model.addAttribute("uvtkrKriti",uvtkrKriti);
		model.addAttribute("uvtkrPlatforma",uvtkrPlatforma);
		model.addAttribute("uvtkrPoluvagon",uvtkrPoluvagon);
		model.addAttribute("uvtkrSisterna",uvtkrSisterna);
		model.addAttribute("uvtkrBoshqa",uvtkrBoshqa);

		//yuklab olish uchun list
		List<Integer> vagonsToDownloadTable = new ArrayList<>();
//Depoli tamir
		vagonsToDownloadTable.add(HavDtHammaPlan);
		vagonsToDownloadTable.add(hdHamma);
		vagonsToDownloadTable.add(HavDtKritiPlan);
		vagonsToDownloadTable.add(hdKriti);
		vagonsToDownloadTable.add(HavDtPlatformaPlan);
		vagonsToDownloadTable.add(hdPlatforma);
		vagonsToDownloadTable.add(HavDtPoluvagonPlan);
		vagonsToDownloadTable.add(hdPoluvagon);
		vagonsToDownloadTable.add(HavDtSisternaPlan);
		vagonsToDownloadTable.add(hdSisterna);
		vagonsToDownloadTable.add(HavDtBoshqaPlan);
		vagonsToDownloadTable.add(hdBoshqa);

		vagonsToDownloadTable.add(AndjDtHammaPlan);
		vagonsToDownloadTable.add(adHamma);
		vagonsToDownloadTable.add(AndjDtKritiPlan);
		vagonsToDownloadTable.add(adKriti);
		vagonsToDownloadTable.add(AndjDtPlatformaPlan);
		vagonsToDownloadTable.add(adPlatforma);
		vagonsToDownloadTable.add(AndjDtPoluvagonPlan);
		vagonsToDownloadTable.add(adPoluvagon);
		vagonsToDownloadTable.add(AndjDtSisternaPlan);
		vagonsToDownloadTable.add(adSisterna);
		vagonsToDownloadTable.add(AndjDtBoshqaPlan);
		vagonsToDownloadTable.add(adBoshqa);

		vagonsToDownloadTable.add(SamDtHammaPlan);
		vagonsToDownloadTable.add(sdHamma);
		vagonsToDownloadTable.add(SamDtKritiPlan);
		vagonsToDownloadTable.add(sdKriti);
		vagonsToDownloadTable.add(SamDtPlatformaPlan);
		vagonsToDownloadTable.add(sdPlatforma);
		vagonsToDownloadTable.add(SamDtPoluvagonPlan);
		vagonsToDownloadTable.add(sdPoluvagon);
		vagonsToDownloadTable.add(SamDtSisternaPlan);
		vagonsToDownloadTable.add(sdSisterna);
		vagonsToDownloadTable.add(SamDtBoshqaPlan);
		vagonsToDownloadTable.add(sdBoshqa);

		vagonsToDownloadTable.add(DtHammaPlan);
		vagonsToDownloadTable.add(uvtdhamma);
		vagonsToDownloadTable.add(DtKritiPlan);
		vagonsToDownloadTable.add(uvtdKriti);
		vagonsToDownloadTable.add(DtPlatformaPlan);
		vagonsToDownloadTable.add(uvtdPlatforma);
		vagonsToDownloadTable.add(DtPoluvagonPlan);
		vagonsToDownloadTable.add(uvtdPoluvagon);
		vagonsToDownloadTable.add(DtSisternaPlan);
		vagonsToDownloadTable.add(uvtdSisterna);
		vagonsToDownloadTable.add(DtBoshqaPlan);
		vagonsToDownloadTable.add(uvtdBoshqa);

//kapital tamir
		vagonsToDownloadTable.add(HavKtHammaPlan);
		vagonsToDownloadTable.add(hkHamma);
		vagonsToDownloadTable.add(HavKtKritiPlan);
		vagonsToDownloadTable.add(hkKriti);
		vagonsToDownloadTable.add(HavKtPlatformaPlan);
		vagonsToDownloadTable.add(hkPlatforma);
		vagonsToDownloadTable.add(HavKtPoluvagonPlan);
		vagonsToDownloadTable.add(hkPoluvagon);
		vagonsToDownloadTable.add(HavKtSisternaPlan);
		vagonsToDownloadTable.add(hkSisterna);
		vagonsToDownloadTable.add(HavKtBoshqaPlan);
		vagonsToDownloadTable.add(hkBoshqa);

		vagonsToDownloadTable.add(AndjKtHammaPlan);
		vagonsToDownloadTable.add(akHamma);
		vagonsToDownloadTable.add(AndjKtKritiPlan);
		vagonsToDownloadTable.add(akKriti);
		vagonsToDownloadTable.add(AndjKtPlatformaPlan);
		vagonsToDownloadTable.add(akPlatforma);
		vagonsToDownloadTable.add(AndjKtPoluvagonPlan);
		vagonsToDownloadTable.add(akPoluvagon);
		vagonsToDownloadTable.add(AndjKtSisternaPlan);
		vagonsToDownloadTable.add(akSisterna);
		vagonsToDownloadTable.add(AndjKtBoshqaPlan);
		vagonsToDownloadTable.add(akBoshqa);

		vagonsToDownloadTable.add(SamKtHammaPlan);
		vagonsToDownloadTable.add(skHamma);
		vagonsToDownloadTable.add(SamKtKritiPlan);
		vagonsToDownloadTable.add(skKriti);
		vagonsToDownloadTable.add(SamKtPlatformaPlan);
		vagonsToDownloadTable.add(skPlatforma);
		vagonsToDownloadTable.add(SamKtPoluvagonPlan);
		vagonsToDownloadTable.add(skPoluvagon);
		vagonsToDownloadTable.add(SamKtSisternaPlan);
		vagonsToDownloadTable.add(skSisterna);
		vagonsToDownloadTable.add(SamKtBoshqaPlan);
		vagonsToDownloadTable.add(skBoshqa);

		vagonsToDownloadTable.add(KtHammaPlan);
		vagonsToDownloadTable.add(uvtkhamma);
		vagonsToDownloadTable.add(KtKritiPlan);
		vagonsToDownloadTable.add(uvtkKriti);
		vagonsToDownloadTable.add(KtPlatformaPlan);
		vagonsToDownloadTable.add(uvtkPlatforma);
		vagonsToDownloadTable.add(KtPoluvagonPlan);
		vagonsToDownloadTable.add(uvtkPoluvagon);
		vagonsToDownloadTable.add(KtSisternaPlan);
		vagonsToDownloadTable.add(uvtkSisterna);
		vagonsToDownloadTable.add(KtBoshqaPlan);
		vagonsToDownloadTable.add(uvtkBoshqa);
//krp
		vagonsToDownloadTable.add(HavKrpHammaPlan);
		vagonsToDownloadTable.add(hkrHamma);
		vagonsToDownloadTable.add(HavKrpKritiPlan);
		vagonsToDownloadTable.add(hkrKriti);
		vagonsToDownloadTable.add(HavKrpPlatformaPlan);
		vagonsToDownloadTable.add(hkrPlatforma);
		vagonsToDownloadTable.add(HavKrpPoluvagonPlan);
		vagonsToDownloadTable.add(hkrPoluvagon);
		vagonsToDownloadTable.add(HavKrpSisternaPlan);
		vagonsToDownloadTable.add(hkrSisterna);
		vagonsToDownloadTable.add(HavKrpBoshqaPlan);
		vagonsToDownloadTable.add(hkrBoshqa);

		vagonsToDownloadTable.add(AndjKrpHammaPlan);
		vagonsToDownloadTable.add(akrHamma);
		vagonsToDownloadTable.add(AndjKrpKritiPlan);
		vagonsToDownloadTable.add(akrKriti);
		vagonsToDownloadTable.add(AndjKrpPlatformaPlan);
		vagonsToDownloadTable.add(akrPlatforma);
		vagonsToDownloadTable.add(AndjKrpPoluvagonPlan);
		vagonsToDownloadTable.add(akrPoluvagon);
		vagonsToDownloadTable.add(AndjKrpSisternaPlan);
		vagonsToDownloadTable.add(akrSisterna);
		vagonsToDownloadTable.add(AndjKrpBoshqaPlan);
		vagonsToDownloadTable.add(akrBoshqa);

		vagonsToDownloadTable.add(SamKrpHammaPlan);
		vagonsToDownloadTable.add(skrHamma);
		vagonsToDownloadTable.add(SamKrpKritiPlan);
		vagonsToDownloadTable.add(skrKriti);
		vagonsToDownloadTable.add(SamKrpPlatformaPlan);
		vagonsToDownloadTable.add(skrPlatforma);
		vagonsToDownloadTable.add(SamKrpPoluvagonPlan);
		vagonsToDownloadTable.add(skrPoluvagon);
		vagonsToDownloadTable.add(SamKrpSisternaPlan);
		vagonsToDownloadTable.add(skrSisterna);
		vagonsToDownloadTable.add(SamKrpBoshqaPlan);
		vagonsToDownloadTable.add(skrBoshqa);

		vagonsToDownloadTable.add(KrpHammaPlan);
		vagonsToDownloadTable.add(uvtkrhamma);
		vagonsToDownloadTable.add(KrpKritiPlan);
		vagonsToDownloadTable.add(uvtkrKriti);
		vagonsToDownloadTable.add(KrpPlatformaPlan);
		vagonsToDownloadTable.add(uvtkrPlatforma);
		vagonsToDownloadTable.add(KrpPoluvagonPlan);
		vagonsToDownloadTable.add(uvtkrPoluvagon);
		vagonsToDownloadTable.add(KrpSisternaPlan);
		vagonsToDownloadTable.add(uvtkrSisterna);
		vagonsToDownloadTable.add(KrpBoshqaPlan);
		vagonsToDownloadTable.add(uvtkrBoshqa);

		vagonsToDownloadAllTable = vagonsToDownloadTable;

	   	 return "AllPlanTableUty";
    }
    
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
   	@GetMapping("/vagons/filterAllMonthUty")
   	public String filterByDepoNomiForAllMonths(Model model,  @RequestParam(value = "depoNomi", required = false) String depoNomi,
   												@RequestParam(value = "vagonTuri", required = false) String vagonTuri) {
   		if(!depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi")){
   			vagonsToDownload = vagonTayyorUtyService.findByDepoNomiAndVagonTuri(depoNomi, vagonTuri);
   			model.addAttribute("vagons", vagonTayyorUtyService.findByDepoNomiAndVagonTuri(depoNomi, vagonTuri));
   		}else if(depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi")){
   			vagonsToDownload = vagonTayyorUtyService.findByVagonTuri(vagonTuri);
   			model.addAttribute("vagons", vagonTayyorUtyService.findByVagonTuri(vagonTuri));
   		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi")){
   			vagonsToDownload = vagonTayyorUtyService.findByDepoNomi(depoNomi );
   			model.addAttribute("vagons", vagonTayyorUtyService.findByDepoNomi(depoNomi ));
   		}else {
   			vagonsToDownload = vagonTayyorUtyService.findAll();
   			model.addAttribute("vagons", vagonTayyorUtyService.findAll());
   		}

		//planlar kiritish
		PlanUty planDto = vagonTayyorUtyService.getPlanuty();

		//samarqand depo tamir plan
		int sdKriti = planDto.getSamDtKritiPlanUtyMonths();
		int sdPlatforma=planDto.getSamDtPlatformaPlanUtyMonths();
		int sdPoluvagon=planDto.getSamDtPoluvagonPlanUtyMonths();
		int sdSisterna=planDto.getSamDtSisternaPlanUtyMonths();
		int sdBoshqa=planDto.getSamDtBoshqaPlanUtyMonths();
		int SamDtHammaPlan=sdKriti+sdPlatforma+sdPoluvagon+sdSisterna+sdBoshqa;

		model.addAttribute("SamDtHammaPlan",SamDtHammaPlan);
		model.addAttribute("SamDtKritiPlan", sdKriti);
		model.addAttribute("SamDtPlatformaPlan", sdPlatforma);
		model.addAttribute("SamDtPoluvagonPlan", sdPoluvagon);
		model.addAttribute("SamDtSisternaPlan", sdSisterna);
		model.addAttribute("SamDtBoshqaPlan", sdBoshqa);

		//havos depo tamir hamma plan
		int hdKriti = planDto.getHavDtKritiPlanUtyMonths();
		int hdPlatforma=planDto.getHavDtPlatformaPlanUtyMonths();
		int hdPoluvagon=planDto.getHavDtPoluvagonPlanUtyMonths();
		int hdSisterna=planDto.getHavDtSisternaPlanUtyMonths();
		int hdBoshqa=planDto.getHavDtBoshqaPlanUtyMonths();
		int HavDtHammaPlan = hdKriti + hdPlatforma + hdPoluvagon + hdSisterna + hdBoshqa;

		model.addAttribute("HavDtHammaPlan", HavDtHammaPlan);
		model.addAttribute("HavDtKritiPlan", hdKriti);
		model.addAttribute("HavDtPlatformaPlan", hdPlatforma);
		model.addAttribute("HavDtPoluvagonPlan", hdPoluvagon);
		model.addAttribute("HavDtSisternaPlan", hdSisterna);
		model.addAttribute("HavDtBoshqaPlan", hdBoshqa);

		//VCHD-5 depo tamir plan
		int adKriti = planDto.getAndjDtKritiPlanUtyMonths();
		int adPlatforma=planDto.getAndjDtPlatformaPlanUtyMonths();
		int adPoluvagon=planDto.getAndjDtPoluvagonPlanUtyMonths();
		int adSisterna=planDto.getAndjDtSisternaPlanUtyMonths();
		int adBoshqa=planDto.getAndjDtBoshqaPlanUtyMonths();
		int AndjDtHammaPlan = adKriti + adPlatforma + adPoluvagon + adSisterna + adBoshqa;

		model.addAttribute("AndjDtHammaPlan", AndjDtHammaPlan);
		model.addAttribute("AndjDtKritiPlan", adKriti);
		model.addAttribute("AndjDtPlatformaPlan",adPlatforma);
		model.addAttribute("AndjDtPoluvagonPlan", adPoluvagon);
		model.addAttribute("AndjDtSisternaPlan", adSisterna);
		model.addAttribute("AndjDtBoshqaPlan", adBoshqa);

		// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = sdKriti + hdKriti + adKriti;
		int DtPlatformaPlan = sdPlatforma + hdPlatforma + adPlatforma;
		int DtPoluvagonPlan = sdPoluvagon + hdPoluvagon + adPoluvagon;
		int DtSisternaPlan = sdSisterna + hdSisterna + adSisterna;
		int DtBoshqaPlan = sdBoshqa + hdBoshqa + adBoshqa;

		model.addAttribute("DtHammaPlan", DtHammaPlan);
		model.addAttribute("DtKritiPlan", DtKritiPlan);
		model.addAttribute("DtPlatformaPlan", DtPlatformaPlan);
		model.addAttribute("DtPoluvagonPlan", DtPoluvagonPlan);
		model.addAttribute("DtSisternaPlan", DtSisternaPlan);
		model.addAttribute("DtBoshqaPlan", DtBoshqaPlan);

		//Samrqand kapital plan
		int skKriti = planDto.getSamKtKritiPlanUtyMonths();
		int skPlatforma=planDto.getSamKtPlatformaPlanUtyMonths();
		int skPoluvagon=planDto.getSamKtPoluvagonPlanUtyMonths();
		int skSisterna=planDto.getSamKtSisternaPlanUtyMonths();
		int skBoshqa=planDto.getSamKtBoshqaPlanUtyMonths();
		int SamKtHammaPlan=skKriti+skPlatforma+skPoluvagon+skSisterna+skBoshqa;

		model.addAttribute("SamKtHammaPlan",SamKtHammaPlan);
		model.addAttribute("SamKtKritiPlan", skKriti);
		model.addAttribute("SamKtPlatformaPlan", skPlatforma);
		model.addAttribute("SamKtPoluvagonPlan", skPoluvagon);
		model.addAttribute("SamKtSisternaPlan", skSisterna);
		model.addAttribute("SamKtBoshqaPlan", skBoshqa);

		//hovos kapital plan
		int hkKriti = planDto.getHavKtKritiPlanUtyMonths();
		int hkPlatforma=planDto.getHavKtPlatformaPlanUtyMonths();
		int hkPoluvagon=planDto.getHavKtPoluvagonPlanUtyMonths();
		int hkSisterna=planDto.getHavKtSisternaPlanUtyMonths();
		int hkBoshqa=planDto.getHavKtBoshqaPlanUtyMonths();
		int HavKtHammaPlan = hkKriti + hkPlatforma + hkPoluvagon + hkSisterna + hkBoshqa;

		model.addAttribute("HavKtHammaPlan", HavKtHammaPlan);
		model.addAttribute("HavKtKritiPlan", hkKriti);
		model.addAttribute("HavKtPlatformaPlan", hkPlatforma);
		model.addAttribute("HavKtPoluvagonPlan", hkPoluvagon);
		model.addAttribute("HavKtSisternaPlan", hkSisterna);
		model.addAttribute("HavKtBoshqaPlan", hkBoshqa);

		//ANDIJON kapital plan
		int akKriti = planDto.getAndjKtKritiPlanUtyMonths();
		int akPlatforma=planDto.getAndjKtPlatformaPlanUtyMonths();
		int akPoluvagon=planDto.getAndjKtPoluvagonPlanUtyMonths();
		int akSisterna=planDto.getAndjKtSisternaPlanUtyMonths();
		int akBoshqa=planDto.getAndjKtBoshqaPlanUtyMonths();
		int AndjKtHammaPlan = akKriti + akPlatforma + akPoluvagon + akSisterna + akBoshqa;


		model.addAttribute("AndjKtHammaPlan", AndjKtHammaPlan);
		model.addAttribute("AndjKtKritiPlan", akKriti);
		model.addAttribute("AndjKtPlatformaPlan", akPlatforma);
		model.addAttribute("AndjKtPoluvagonPlan", akPoluvagon);
		model.addAttribute("AndjKtSisternaPlan", akSisterna);
		model.addAttribute("AndjKtBoshqaPlan", akBoshqa);

		//Itogo kapital plan
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = skKriti + hkKriti + akKriti;
		int KtPlatformaPlan = skPlatforma + hkPlatforma + akPlatforma;
		int KtPoluvagonPlan =skPoluvagon + hkPoluvagon + akPoluvagon;
		int KtSisternaPlan = skSisterna + hkSisterna + akSisterna;
		int KtBoshqaPlan = skBoshqa + hkBoshqa + akBoshqa;

		model.addAttribute("KtHammaPlan", KtHammaPlan);
		model.addAttribute("KtKritiPlan", KtKritiPlan);
		model.addAttribute("KtPlatformaPlan", KtPlatformaPlan);
		model.addAttribute("KtPoluvagonPlan", KtPoluvagonPlan);
		model.addAttribute("KtSisternaPlan", KtSisternaPlan);
		model.addAttribute("KtBoshqaPlan", KtBoshqaPlan);

		//Samarqankr Krp plan
		int skrKriti = planDto.getSamKrpKritiPlanUtyMonths();
		int skrPlatforma=planDto.getSamKrpPlatformaPlanUtyMonths();
		int skrPoluvagon=planDto.getSamKrpPoluvagonPlanUtyMonths();
		int skrSisterna=planDto.getSamKrpSisternaPlanUtyMonths();
		int skrBoshqa=planDto.getSamKrpBoshqaPlanUtyMonths();
		int SamKrpHammaPlan=skrKriti+skrPlatforma+skrPoluvagon+skrSisterna+skrBoshqa;

		model.addAttribute("SamKrpHammaPlan", SamKrpHammaPlan);
		model.addAttribute("SamKrpKritiPlan", skrKriti);
		model.addAttribute("SamKrpPlatformaPlan", skrPlatforma);
		model.addAttribute("SamKrpPoluvagonPlan", skrPoluvagon);
		model.addAttribute("SamKrpSisternaPlan", skrSisterna);
		model.addAttribute("SamKrpBoshqaPlan", skrBoshqa);

		//Hovos krp plan
		int hkrKriti = planDto.getHavKrpKritiPlanUtyMonths();
		int hkrPlatforma=planDto.getHavKrpPlatformaPlanUtyMonths();
		int hkrPoluvagon=planDto.getHavKrpPoluvagonPlanUtyMonths();
		int hkrSisterna=planDto.getHavKrpSisternaPlanUtyMonths();
		int hkrBoshqa=planDto.getHavKrpBoshqaPlanUtyMonths();
		int HavKrpHammaPlan = hkrKriti + hkrPlatforma + hkrPoluvagon + hkrSisterna + hkrBoshqa;

		model.addAttribute("HavKrpHammaPlan",HavKrpHammaPlan);
		model.addAttribute("HavKrpKritiPlan", hkrKriti);
		model.addAttribute("HavKrpPlatformaPlan", hkrPlatforma);
		model.addAttribute("HavKrpPoluvagonPlan", hkrPoluvagon);
		model.addAttribute("HavKrpSisternaPlan", hkrSisterna);
		model.addAttribute("HavKrpBoshqaPlan", hkrBoshqa);

		//andijon krp plan
		int akrKriti = planDto.getAndjKrpKritiPlanUtyMonths();
		int akrPlatforma=planDto.getAndjKrpPlatformaPlanUtyMonths();
		int akrPoluvagon=planDto.getAndjKrpPoluvagonPlanUtyMonths();
		int akrSisterna=planDto.getAndjKrpSisternaPlanUtyMonths();
		int akrBoshqa=planDto.getAndjKrpBoshqaPlanUtyMonths();
		int AndjKrpHammaPlan = akrKriti + akrPlatforma + akrPoluvagon + akrSisterna + akrBoshqa;

		model.addAttribute("AndjKrpHammaPlan",AndjKrpHammaPlan);
		model.addAttribute("AndjKrpKritiPlan", akrKriti);
		model.addAttribute("AndjKrpPlatformaPlan", akrPlatforma);
		model.addAttribute("AndjKrpPoluvagonPlan", akrPoluvagon);
		model.addAttribute("AndjKrpSisternaPlan", akrSisterna);
		model.addAttribute("AndjKrpBoshqaPlan", akrBoshqa);

		//itogo krp
		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = skrKriti + hkrKriti + akrKriti;
		int KrpPlatformaPlan = skrPlatforma + hkrPlatforma + akrPlatforma;
		int KrpPoluvagonPlan = akrPoluvagon + hkrPoluvagon + skrPoluvagon;
		int KrpSisternaPlan = skrSisterna + hkrSisterna + akrSisterna;
		int KrpBoshqaPlan = skrBoshqa + hkrBoshqa + akrBoshqa;

		model.addAttribute("KrpHammaPlan", KrpHammaPlan);
		model.addAttribute("KrpKritiPlan", KrpKritiPlan);
		model.addAttribute("KrpPlatformaPlan", KrpPlatformaPlan);
		model.addAttribute("KrpPoluvagonPlan",KrpPoluvagonPlan);
		model.addAttribute("KrpSisternaPlan", KrpSisternaPlan);
		model.addAttribute("KrpBoshqaPlan", KrpBoshqaPlan);

		//**//
		// samarqand depo tamir hamma false vagonlar soni
		int sdKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)");
		int sdPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)");
		int sdPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)");
		int sdSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)");
		int sdBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)");
		int sdHammaFalse = sdKritiFalse + sdPlatformaFalse+ sdPoluvagonFalse+ sdSisternaFalse + sdBoshqaFalse;

		model.addAttribute("sdHammaFalse",sdHammaFalse+334);
		model.addAttribute("sdKritiFalse",sdKritiFalse+134);
		model.addAttribute("sdPlatformaFalse",sdPlatformaFalse+6);
		model.addAttribute("sdPoluvagonFalse",sdPoluvagonFalse+65);
		model.addAttribute("sdSisternaFalse",sdSisternaFalse);
		model.addAttribute("sdBoshqaFalse",sdBoshqaFalse+129);

		// VCHD-3 depo tamir hamma false vagonlar soni
		int hdKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)");
		int hdPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)");
		int hdPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)");
		int hdSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)");
		int hdBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)");
		int hdHammaFalse = hdKritiFalse + hdPlatformaFalse+ hdPoluvagonFalse+ hdSisternaFalse + hdBoshqaFalse;

		model.addAttribute("hdHammaFalse",hdHammaFalse+455);
		model.addAttribute("hdKritiFalse",hdKritiFalse);
		model.addAttribute("hdPlatformaFalse",hdPlatformaFalse+26);
		model.addAttribute("hdPoluvagonFalse",hdPoluvagonFalse+109);
		model.addAttribute("hdSisternaFalse",hdSisternaFalse+4);
		model.addAttribute("hdBoshqaFalse",hdBoshqaFalse+316);

		// VCHD-5 depo tamir hamma false vagonlar soni
		int adKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)");
		int adPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)");
		int adPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)");
		int adSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)");
		int adBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)");
		int adHammaFalse = adKritiFalse + adPlatformaFalse+ adPoluvagonFalse+ adSisternaFalse + adBoshqaFalse;

		model.addAttribute("adHammaFalse",adHammaFalse+443);
		model.addAttribute("adKritiFalse",adKritiFalse+224);
		model.addAttribute("adPlatformaFalse",adPlatformaFalse+3);
		model.addAttribute("adPoluvagonFalse",adPoluvagonFalse+103);
		model.addAttribute("adSisternaFalse",adSisternaFalse);
		model.addAttribute("adBoshqaFalse",adBoshqaFalse+113);

		// depoli tamir itogo uchun
		int dHammaFalse =  adHammaFalse + hdHammaFalse+sdHammaFalse;
		int dKritiFalse = sdKritiFalse + hdKritiFalse + adKritiFalse;
		int dPlatforma = adPlatformaFalse + sdPlatformaFalse + hdPlatformaFalse;
		int dPoluvagon  = adPoluvagonFalse + sdPoluvagonFalse + hdPoluvagonFalse;
		int dSisterna = adSisternaFalse + hdSisternaFalse + sdSisternaFalse;
		int dBoshqa = adBoshqaFalse + hdBoshqaFalse + sdBoshqaFalse;

		model.addAttribute("dHammaFalse",dHammaFalse+1232);
		model.addAttribute("dKritiFalse",dKritiFalse+358);
		model.addAttribute("dPlatforma",dPlatforma+35);
		model.addAttribute("dPoluvagon",dPoluvagon+277);
		model.addAttribute("dSisterna",dSisterna+4);
		model.addAttribute("dBoshqa",dBoshqa+558);


		// samarqand KApital tamir hamma false vagonlar soni
		int skKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)");
		int skPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)");
		int skPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)");
		int skSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)");
		int skBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)");
		int skHammaFalse = skKritiFalse + skPlatformaFalse+ skPoluvagonFalse+ skSisternaFalse + skBoshqaFalse;

		model.addAttribute("skHammaFalse",skHammaFalse+16);
		model.addAttribute("skKritiFalse",skKritiFalse+16);
		model.addAttribute("skPlatformaFalse",skPlatformaFalse);
		model.addAttribute("skPoluvagonFalse",skPoluvagonFalse);
		model.addAttribute("skSisternaFalse",skSisternaFalse);
		model.addAttribute("skBoshqaFalse",skBoshqaFalse);

		// VCHD-3 kapital tamir hamma false vagonlar soni
		int hkKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Kapital ta’mir(КР)");
		int hkPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)");
		int hkPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)");
		int hkSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)");
		int hkBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)");
		int hkHammaFalse = hkKritiFalse + hkPlatformaFalse+ hkPoluvagonFalse+ hkSisternaFalse + hkBoshqaFalse;

		model.addAttribute("hkHammaFalse",hkHammaFalse+7);
		model.addAttribute("hkKritiFalse",hkKritiFalse);
		model.addAttribute("hkPlatformaFalse",hkPlatformaFalse);
		model.addAttribute("hkPoluvagonFalse",hkPoluvagonFalse+4);
		model.addAttribute("hkSisternaFalse",hkSisternaFalse);
		model.addAttribute("hkBoshqaFalse",hkBoshqaFalse+3);

		// VCHD-5 kapital tamir hamma false vagonlar soni
		int akKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)");
		int akPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)");
		int akPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)");
		int akSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)");
		int akBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)");
		int akHammaFalse = akKritiFalse + akPlatformaFalse+ akPoluvagonFalse+ akSisternaFalse + akBoshqaFalse;

		model.addAttribute("akHammaFalse",akHammaFalse+28);
		model.addAttribute("akKritiFalse",akKritiFalse+26);
		model.addAttribute("akPlatformaFalse",akPlatformaFalse);
		model.addAttribute("akPoluvagonFalse",akPoluvagonFalse+2);
		model.addAttribute("akSisternaFalse",akSisternaFalse);
		model.addAttribute("akBoshqaFalse",akBoshqaFalse);

		// Kapital tamir itogo uchun
		int kHammaFalse =  akHammaFalse + hkHammaFalse+skHammaFalse;
		int kKritiFalse = skKritiFalse + hkKritiFalse + akKritiFalse;
		int kPlatforma = akPlatformaFalse + skPlatformaFalse + hkPlatformaFalse;
		int kPoluvagon  = akPoluvagonFalse + skPoluvagonFalse + hkPoluvagonFalse;
		int kSisterna = akSisternaFalse + hkSisternaFalse + skSisternaFalse;
		int kBoshqa = akBoshqaFalse + hkBoshqaFalse + skBoshqaFalse;

		model.addAttribute("kHammaFalse",kHammaFalse+51);
		model.addAttribute("kKritiFalse",kKritiFalse+42);
		model.addAttribute("kPlatforma",kPlatforma);
		model.addAttribute("kPoluvagon",kPoluvagon+6);
		model.addAttribute("kSisterna",kSisterna);
		model.addAttribute("kBoshqa",kBoshqa+3);

		//**
		// samarqand KRP tamir hamma false vagonlar soni
		int skrKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)");
		int skrPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)");
		int skrPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)");
		int skrSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)");
		int skrBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)");
		int skrHammaFalse = skrKritiFalse + skrPlatformaFalse+ skrPoluvagonFalse+ skrSisternaFalse + skrBoshqaFalse;

		model.addAttribute("skrHammaFalse",skrHammaFalse+89);
		model.addAttribute("skrKritiFalse",skrKritiFalse);
		model.addAttribute("skrPlatformaFalse",skrPlatformaFalse);
		model.addAttribute("skrPoluvagonFalse",skrPoluvagonFalse+88);
		model.addAttribute("skrSisternaFalse",skrSisternaFalse+1);
		model.addAttribute("skrBoshqaFalse",skrBoshqaFalse);

		// VCHD-3 KRP tamir hamma false vagonlar soni
		int hkrKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","KRP(КРП)");
		int hkrPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)");
		int hkrPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)");
		int hkrSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)");
		int hkrBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)");
		int hkrHammaFalse = hkrKritiFalse + hkrPlatformaFalse+ hkrPoluvagonFalse+ hkrSisternaFalse + hkrBoshqaFalse;

		model.addAttribute("hkrHammaFalse",hkrHammaFalse+83);
		model.addAttribute("hkrKritiFalse",hkrKritiFalse);
		model.addAttribute("hkrPlatformaFalse",hkrPlatformaFalse);
		model.addAttribute("hkrPoluvagonFalse",hkrPoluvagonFalse+83);
		model.addAttribute("hkrSisternaFalse",hkrSisternaFalse);
		model.addAttribute("hkrBoshqaFalse",hkrBoshqaFalse);

		// VCHD-5 KRP tamir hamma false vagonlar soni
		int akrKritiFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)");
		int akrPlatformaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)");
		int akrPoluvagonFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)");
		int akrSisternaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)");
		int akrBoshqaFalse=vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)");
		int akrHammaFalse = akrKritiFalse + akrPlatformaFalse+ akrPoluvagonFalse+ akrSisternaFalse + akrBoshqaFalse;

		model.addAttribute("akrHammaFalse",akrHammaFalse+61);
		model.addAttribute("akrKritiFalse",akrKritiFalse);
		model.addAttribute("akrPlatformaFalse",akrPlatformaFalse);
		model.addAttribute("akrPoluvagonFalse",akrPoluvagonFalse+61);
		model.addAttribute("akrSisternaFalse",akrSisternaFalse);
		model.addAttribute("akBoshqaFalse",akBoshqaFalse);

		// Krp itogo uchun
		int krHammaFalse =  akrHammaFalse + hkrHammaFalse+skrHammaFalse;
		int krKritiFalse = skrKritiFalse + hkrKritiFalse + akrKritiFalse;
		int krPlatforma = akrPlatformaFalse + skrPlatformaFalse + hkrPlatformaFalse;
		int krPoluvagon  = akrPoluvagonFalse + skrPoluvagonFalse + hkrPoluvagonFalse;
		int krSisterna = akrSisternaFalse + hkrSisternaFalse + skrSisternaFalse;
		int krBoshqa = akrBoshqaFalse + hkrBoshqaFalse + skrBoshqaFalse;

		model.addAttribute("krHammaFalse",krHammaFalse+233);
		model.addAttribute("krKritiFalse",krKritiFalse);
		model.addAttribute("krPlatforma",krPlatforma);
		model.addAttribute("krPoluvagon",krPoluvagon+232);
		model.addAttribute("krSisterna",krSisterna+1);
		model.addAttribute("krBoshqa",krBoshqa);

		//yuklab olish uchun list
		List<Integer> vagonsToDownloadTable = new ArrayList<>();

		//Depoli tamir
		vagonsToDownloadTable.add(HavDtHammaPlan);
		vagonsToDownloadTable.add(hdHammaFalse + 455);
		vagonsToDownloadTable.add(hdKriti);
		vagonsToDownloadTable.add(hdKritiFalse);
		vagonsToDownloadTable.add(hdPlatforma);
		vagonsToDownloadTable.add(hdPlatformaFalse + 26);
		vagonsToDownloadTable.add(hdPoluvagon);
		vagonsToDownloadTable.add(hdPoluvagonFalse + 109);
		vagonsToDownloadTable.add(hdSisterna);
		vagonsToDownloadTable.add(hdSisternaFalse + 4);
		vagonsToDownloadTable.add(hdBoshqa);
		vagonsToDownloadTable.add(hdBoshqaFalse + 316);

		vagonsToDownloadTable.add(AndjDtHammaPlan);
		vagonsToDownloadTable.add(adHammaFalse + 443);
		vagonsToDownloadTable.add(adKriti);
		vagonsToDownloadTable.add(adKritiFalse + 224);
		vagonsToDownloadTable.add(adPlatforma);
		vagonsToDownloadTable.add(adPlatformaFalse + 3);
		vagonsToDownloadTable.add(adPoluvagon);
		vagonsToDownloadTable.add(adPoluvagonFalse + 103);
		vagonsToDownloadTable.add(adSisterna);
		vagonsToDownloadTable.add(adSisternaFalse);
		vagonsToDownloadTable.add(adBoshqa);
		vagonsToDownloadTable.add(adBoshqaFalse + 113);

		vagonsToDownloadTable.add(SamDtHammaPlan);
		vagonsToDownloadTable.add(sdHammaFalse + 334);
		vagonsToDownloadTable.add(sdKriti);
		vagonsToDownloadTable.add(sdKritiFalse + 134);
		vagonsToDownloadTable.add(sdPlatforma);
		vagonsToDownloadTable.add(sdPlatformaFalse + 6);
		vagonsToDownloadTable.add(sdPoluvagon);
		vagonsToDownloadTable.add(sdPoluvagonFalse + 65);
		vagonsToDownloadTable.add(sdSisterna);
		vagonsToDownloadTable.add(sdSisternaFalse );
		vagonsToDownloadTable.add(sdBoshqa);
		vagonsToDownloadTable.add(sdBoshqaFalse + 129);;

		vagonsToDownloadTable.add(DtHammaPlan);
		vagonsToDownloadTable.add(dHammaFalse+1232);
		vagonsToDownloadTable.add(DtKritiPlan);
		vagonsToDownloadTable.add(dKritiFalse+358);
		vagonsToDownloadTable.add(DtPlatformaPlan);
		vagonsToDownloadTable.add(dPlatforma+35);
		vagonsToDownloadTable.add(DtPoluvagonPlan);
		vagonsToDownloadTable.add(dPoluvagon+277);
		vagonsToDownloadTable.add(DtSisternaPlan);
		vagonsToDownloadTable.add(dSisterna+4 );
		vagonsToDownloadTable.add(DtBoshqaPlan);
		vagonsToDownloadTable.add(dBoshqa+558);

		//kapital tamir
		vagonsToDownloadTable.add(HavKtHammaPlan);
		vagonsToDownloadTable.add(hkHammaFalse + 7);
		vagonsToDownloadTable.add(hkKriti);
		vagonsToDownloadTable.add(hkKritiFalse);
		vagonsToDownloadTable.add(hkPlatforma);
		vagonsToDownloadTable.add(hkPlatformaFalse);
		vagonsToDownloadTable.add(hkPoluvagon);
		vagonsToDownloadTable.add(hkPoluvagonFalse + 4);
		vagonsToDownloadTable.add(hkSisterna);
		vagonsToDownloadTable.add(hkSisternaFalse);
		vagonsToDownloadTable.add(hkBoshqa);
		vagonsToDownloadTable.add(hkBoshqaFalse + 3);

		vagonsToDownloadTable.add(AndjKtHammaPlan);
		vagonsToDownloadTable.add(akHammaFalse + 28);
		vagonsToDownloadTable.add(akKriti);
		vagonsToDownloadTable.add(akKritiFalse + 26);
		vagonsToDownloadTable.add(akPlatforma);
		vagonsToDownloadTable.add(akPlatformaFalse);
		vagonsToDownloadTable.add(akPoluvagon);
		vagonsToDownloadTable.add(akPoluvagonFalse + 2);
		vagonsToDownloadTable.add(akSisterna);
		vagonsToDownloadTable.add(akSisternaFalse);
		vagonsToDownloadTable.add(akBoshqa);
		vagonsToDownloadTable.add(akBoshqaFalse);

		vagonsToDownloadTable.add(SamKtHammaPlan);
		vagonsToDownloadTable.add(skHammaFalse + 16);
		vagonsToDownloadTable.add(skKriti);
		vagonsToDownloadTable.add(skKritiFalse + 16);
		vagonsToDownloadTable.add(skPlatforma);
		vagonsToDownloadTable.add(skPlatformaFalse);
		vagonsToDownloadTable.add(skPoluvagon);
		vagonsToDownloadTable.add(skPoluvagonFalse);
		vagonsToDownloadTable.add(skSisterna);
		vagonsToDownloadTable.add(skSisternaFalse );
		vagonsToDownloadTable.add(skBoshqa);
		vagonsToDownloadTable.add(skBoshqaFalse);

		vagonsToDownloadTable.add(KtHammaPlan);
		vagonsToDownloadTable.add(kHammaFalse + 51);
		vagonsToDownloadTable.add(KtKritiPlan);
		vagonsToDownloadTable.add(kKritiFalse + 42);
		vagonsToDownloadTable.add(KtPlatformaPlan);
		vagonsToDownloadTable.add(kPlatforma);
		vagonsToDownloadTable.add(KtPoluvagonPlan);
		vagonsToDownloadTable.add(kPoluvagon + 6);
		vagonsToDownloadTable.add(KtSisternaPlan);
		vagonsToDownloadTable.add(kSisterna);
		vagonsToDownloadTable.add(KtBoshqaPlan);
		vagonsToDownloadTable.add(kBoshqa + 3);

		//KRPP
		vagonsToDownloadTable.add(HavKrpHammaPlan);
		vagonsToDownloadTable.add(hkrHammaFalse + 83);
		vagonsToDownloadTable.add(hkrKriti);
		vagonsToDownloadTable.add(hkrKritiFalse);
		vagonsToDownloadTable.add(hkrPlatforma);
		vagonsToDownloadTable.add(hkrPlatformaFalse);
		vagonsToDownloadTable.add(hkrPoluvagon);
		vagonsToDownloadTable.add(hkrPoluvagonFalse + 83);
		vagonsToDownloadTable.add(hkrSisterna);
		vagonsToDownloadTable.add(hkrSisternaFalse);
		vagonsToDownloadTable.add(hkrBoshqa);
		vagonsToDownloadTable.add(hkrBoshqaFalse);

		vagonsToDownloadTable.add(AndjKrpHammaPlan);
		vagonsToDownloadTable.add(akrHammaFalse + 61);
		vagonsToDownloadTable.add(akrKriti);
		vagonsToDownloadTable.add(akrKritiFalse);
		vagonsToDownloadTable.add(akrPlatforma);
		vagonsToDownloadTable.add(akrPlatformaFalse);
		vagonsToDownloadTable.add(akrPoluvagon);
		vagonsToDownloadTable.add(akrPoluvagonFalse + 61);
		vagonsToDownloadTable.add(akrSisterna);
		vagonsToDownloadTable.add(akrSisternaFalse);
		vagonsToDownloadTable.add(akrBoshqa);
		vagonsToDownloadTable.add(akrBoshqaFalse);

		vagonsToDownloadTable.add(SamKrpHammaPlan);
		vagonsToDownloadTable.add(skrHammaFalse + 89);
		vagonsToDownloadTable.add(skrKriti);
		vagonsToDownloadTable.add(skrKritiFalse);
		vagonsToDownloadTable.add(skrPlatforma);
		vagonsToDownloadTable.add(skrPlatformaFalse);
		vagonsToDownloadTable.add(skrPoluvagon);
		vagonsToDownloadTable.add(skrPoluvagonFalse + 88);
		vagonsToDownloadTable.add(skrSisterna);
		vagonsToDownloadTable.add(skrSisternaFalse + 1);
		vagonsToDownloadTable.add(skrBoshqa);
		vagonsToDownloadTable.add(skrBoshqaFalse);

		vagonsToDownloadTable.add(KrpHammaPlan);
		vagonsToDownloadTable.add(krHammaFalse + 233);
		vagonsToDownloadTable.add(KrpKritiPlan);
		vagonsToDownloadTable.add(krKritiFalse);
		vagonsToDownloadTable.add(KrpPlatformaPlan);
		vagonsToDownloadTable.add(krPlatforma);
		vagonsToDownloadTable.add(KrpPoluvagonPlan);
		vagonsToDownloadTable.add(krPoluvagon + 232);
		vagonsToDownloadTable.add(KrpSisternaPlan);
		vagonsToDownloadTable.add(krSisterna + 1);
		vagonsToDownloadTable.add(KrpBoshqaPlan);
		vagonsToDownloadTable.add(krBoshqa);

		vagonsToDownloadAllTable = vagonsToDownloadTable;

		return "planTableForMonthsUty";
    }
	
}
