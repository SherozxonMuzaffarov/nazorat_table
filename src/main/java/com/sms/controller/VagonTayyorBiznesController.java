package com.sms.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sms.dto.PlanBiznesDto;
import com.sms.model.PlanBiznes;
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

import com.sms.model.VagonTayyor;
import com.sms.service.VagonTayyorBiznesService;

@Controller
public class VagonTayyorBiznesController {

	@Autowired
	private VagonTayyorBiznesService vagonTayyorService;


	LocalDate today = LocalDate.now();
	int month = today.getMonthValue();

	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ' , 'SAMMETROLOGIYA','HAVMETROLOGIYA','ANDJMETROLOGIYA')")
	@GetMapping("")
	public String main(Model model) {
		return "main";
	}

	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/rejalar")
	public String rejalar(Model model) {
		return "rejalar";
	}

	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/tables")
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
		return "tables";
	}

	//Yuklab olish uchun Malumot yigib beradi
	List<VagonTayyor> vagonsToDownload  = new ArrayList<>();
	//Yuklab olish uchun Malumot yigib beradi JAdval uchun
	List<Integer> vagonsToDownloadAllTable  = new ArrayList<>();

	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/createExcelBiznes")
	public void pdfFile(Model model,HttpServletResponse response) throws IOException {
		vagonTayyorService.createPdf(vagonsToDownload,response);
//		model.addAttribute("vagons",vagonsToDownload);
	 }

	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/createExcelBiznesAllMonth")
	public void createPdfFile(Model model,HttpServletResponse response) throws IOException {
		vagonTayyorService.createPdf(vagonsToDownload,response);
//		model.addAttribute("vagons",vagonsToDownload);
	 }

	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/createExcelTableBiznes")
	public void pdfFileTable(Model model, HttpServletResponse response) throws IOException {
		vagonTayyorService.pdfFileTable(vagonsToDownloadAllTable, response);
//		model.addAttribute("vagons",vagonsToDownloadAllTable);
	}

    //Tayyor yangi vagon qoshish uchun oyna
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/newTayyor")
	public String createVagonForm(Model model) {
		VagonTayyor vagonTayyor = new VagonTayyor();
		model.addAttribute("vagon", vagonTayyor);
		return "create_tayyorvagon";
	}

    //TAyyor vagon qoshish
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons/saveTayyor")
	public String saveVagon(@ModelAttribute("vagon") VagonTayyor vagon, HttpServletRequest request ) {
    	if (request.isUserInRole("DIRECTOR")) {
    		vagonTayyorService.saveVagon(vagon);
        }else if (request.isUserInRole("SAM")) {
        	vagonTayyorService.saveVagonSam(vagon);
        }else if (request.isUserInRole("HAV")) {
        	vagonTayyorService.saveVagonHav(vagon);
        }else if (request.isUserInRole("ANDJ")) {
        	vagonTayyorService.saveVagonAndj(vagon);
        }
		return "redirect:/vagons/AllPlanTable";
	}

    //tahrirlash uchun oyna
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/editTayyor/{id}")
	public String editVagonForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("vagon",vagonTayyorService.getVagonById(id));
		return "edit_tayyorvagon";
	}

    //tahrirni saqlash
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons/updateTayyor/{id}")
	public String updateVagon(@ModelAttribute("vagon") VagonTayyor vagon, @PathVariable Long id,Model model, HttpServletRequest request) throws NotFoundException {
    	if (request.isUserInRole("DIRECTOR")) {
    		vagonTayyorService.updateVagon(vagon, id);
        }else if (request.isUserInRole("SAM")) {
        	vagonTayyorService.updateVagonSam(vagon, id);
        }else if (request.isUserInRole("HAV")) {
        	vagonTayyorService.updateVagonHav(vagon, id);
        }else if (request.isUserInRole("ANDJ")) {
        	vagonTayyorService.updateVagonAndj(vagon, id);
        }
    	return "redirect:/vagons/AllPlanTable";
	}

    //tahrirlash jami oylar uchun
  //tahrirlash uchun oyna
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/editTayyorMonths/{id}")
	public String editVagonFormMonths(@PathVariable("id") Long id, Model model) {
		model.addAttribute("vagon",vagonTayyorService.getVagonById(id));
		return "edit_tayyorvagonMonths";
	}

    //tahrirni saqlash
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons/updateTayyorMonths/{id}")
	public String updateVagonMonths(@ModelAttribute("vagon") VagonTayyor vagon, @PathVariable Long id,Model model, HttpServletRequest request) throws NotFoundException {
    	if (request.isUserInRole("DIRECTOR")) {
    		vagonTayyorService.updateVagonMonths(vagon, id);
        }else if (request.isUserInRole("SAM")) {
        	vagonTayyorService.updateVagonSamMonths(vagon, id);
        }else if (request.isUserInRole("HAV")) {
        	vagonTayyorService.updateVagonHavMonths(vagon, id);
        }else if (request.isUserInRole("ANDJ")) {
        	vagonTayyorService.updateVagonAndjMonths(vagon, id);
        }
    	return "redirect:/vagons/planTableForMonths";
	}

    //bazadan o'chirish
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/deleteTayyor/{id}")
	public String deleteVagonForm(@PathVariable("id") Long id, HttpServletRequest request ) throws NotFoundException {
    	if (request.isUserInRole("DIRECTOR")) {
    		vagonTayyorService.deleteVagonById(id);
        }else if (request.isUserInRole("SAM")) {
        		vagonTayyorService.deleteVagonSam(id);
        }else if (request.isUserInRole("HAV")) {
    		vagonTayyorService.deleteVagonHav(id);
        }else if (request.isUserInRole("ANDJ")) {
    		vagonTayyorService.deleteVagonAndj(id);
        }
		return "redirect:/vagons/AllPlanTable";
	}

    //All planlar uchun
    @PreAuthorize(value = "hasRole('DIRECTOR')")
   	@GetMapping("/vagons/newPlan")
   	public String addPlan(Model model) {
   		PlanBiznesDto planDto = new PlanBiznesDto();
   		model.addAttribute("planDto", planDto);
   		return "add_plan";
   	}

    //Plan qoshish
    @PreAuthorize(value = "hasAnyRole('DIRECTOR')")
	@PostMapping("/vagons/savePlan")
	public String savePlan(@ModelAttribute("planDto") PlanBiznesDto planDto) {
    	vagonTayyorService.savePlan(planDto);
    	return "redirect:/vagons/AllPlanTable";
	}

    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/AllPlanTable")
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
    	vagonsToDownload = vagonTayyorService.findAll(oy);
    	model.addAttribute("vagons", vagonTayyorService.findAll(oy));

    	//vaqtni olib turadi
		model.addAttribute("samDate",vagonTayyorService.getSamDate());
		model.addAttribute("havDate", vagonTayyorService.getHavDate());
		model.addAttribute("andjDate",vagonTayyorService.getAndjDate());

    	PlanBiznes planDto = vagonTayyorService.getPlanBiznes();
		//planlar kiritish

		//havos hamma plan
		int HavDtHammaPlan = planDto.getHavDtKritiPlanBiznes() + planDto.getHavDtPlatformaPlanBiznes() + planDto.getHavDtPoluvagonPlanBiznes() + planDto.getHavDtSisternaPlanBiznes() + planDto.getHavDtBoshqaPlanBiznes();
		int HavDtKritiPlan = planDto.getHavDtKritiPlanBiznes();
		int HavDtPlatformaPlan = planDto.getHavDtPlatformaPlanBiznes();
		int HavDtPoluvagonPlan = planDto.getHavDtPoluvagonPlanBiznes();
		int HavDtSisternaPlan = planDto.getHavDtSisternaPlanBiznes();
		int HavDtBoshqaPlan = planDto.getHavDtBoshqaPlanBiznes();

		model.addAttribute("HavDtHammaPlan", HavDtHammaPlan);
		model.addAttribute("HavDtKritiPlan", HavDtKritiPlan);
		model.addAttribute("HavDtPlatformaPlan", HavDtPlatformaPlan);
		model.addAttribute("HavDtPoluvagonPlan", HavDtPoluvagonPlan);
		model.addAttribute("HavDtSisternaPlan", HavDtSisternaPlan);
		model.addAttribute("HavDtBoshqaPlan", HavDtBoshqaPlan);

		//andijon hamma plan depo tamir
		int AndjDtHammaPlan = planDto.getAndjDtKritiPlanBiznes() + planDto.getAndjDtPlatformaPlanBiznes() + planDto.getAndjDtPoluvagonPlanBiznes() + planDto.getAndjDtSisternaPlanBiznes() + planDto.getAndjDtBoshqaPlanBiznes();
		int AndjDtKritiPlan =  planDto.getAndjDtKritiPlanBiznes();
		int AndjDtPlatformaPlan =  planDto.getAndjDtPlatformaPlanBiznes();
		int AndjDtPoluvagonPlan =  planDto.getAndjDtPoluvagonPlanBiznes();
		int AndjDtSisternaPlan =  planDto.getAndjDtSisternaPlanBiznes();
		int AndjDtBoshqaPlan =  planDto.getAndjDtBoshqaPlanBiznes();

		model.addAttribute("AndjDtHammaPlan", AndjDtHammaPlan);
		model.addAttribute("AndjDtKritiPlan", AndjDtKritiPlan);
		model.addAttribute("AndjDtPlatformaPlan", AndjDtPlatformaPlan);
		model.addAttribute("AndjDtPoluvagonPlan", AndjDtPoluvagonPlan);
		model.addAttribute("AndjDtSisternaPlan", AndjDtSisternaPlan);
		model.addAttribute("AndjDtBoshqaPlan", AndjDtBoshqaPlan);
	
		//samarqand depo tamir
		int SamDtHammaPlan=planDto.getSamDtKritiPlanBiznes() + planDto.getSamDtPlatformaPlanBiznes() + planDto.getSamDtPoluvagonPlanBiznes() + planDto.getSamDtSisternaPlanBiznes() + planDto.getSamDtBoshqaPlanBiznes();
		int SamDtKritiPlan =  planDto.getSamDtKritiPlanBiznes();
		int SamDtPlatformaPlan =  planDto.getSamDtPlatformaPlanBiznes();
		int SamDtPoluvagonPlan =  planDto.getSamDtPoluvagonPlanBiznes();
		int SamDtSisternaPlan =  planDto.getSamDtSisternaPlanBiznes();
		int SamDtBoshqaPlan =  planDto.getSamDtBoshqaPlanBiznes();

		model.addAttribute("SamDtHammaPlan",SamDtHammaPlan);
		model.addAttribute("SamDtKritiPlan", SamDtKritiPlan);
		model.addAttribute("SamDtPlatformaPlan", SamDtPlatformaPlan);
		model.addAttribute("SamDtPoluvagonPlan", SamDtPoluvagonPlan);
		model.addAttribute("SamDtSisternaPlan", SamDtSisternaPlan);
		model.addAttribute("SamDtBoshqaPlan", SamDtBoshqaPlan);

		// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = planDto.getAndjDtKritiPlanBiznes() + planDto.getHavDtKritiPlanBiznes() + planDto.getSamDtKritiPlanBiznes();
		int DtPlatformaPlan = planDto.getAndjDtPlatformaPlanBiznes() + planDto.getHavDtPlatformaPlanBiznes() + planDto.getSamDtPlatformaPlanBiznes();
		int DtPoluvagonPlan = planDto.getAndjDtPoluvagonPlanBiznes() + planDto.getHavDtPoluvagonPlanBiznes() + planDto.getSamDtPoluvagonPlanBiznes();
		int DtSisternaPlan = planDto.getAndjDtSisternaPlanBiznes() + planDto.getHavDtSisternaPlanBiznes() + planDto.getSamDtSisternaPlanBiznes();
		int DtBoshqaPlan = planDto.getAndjDtBoshqaPlanBiznes() + planDto.getHavDtBoshqaPlanBiznes() + planDto.getSamDtBoshqaPlanBiznes();

		model.addAttribute("DtHammaPlan", DtHammaPlan);
		model.addAttribute("DtKritiPlan", DtKritiPlan);
		model.addAttribute("DtPlatformaPlan", DtPlatformaPlan);
		model.addAttribute("DtPoluvagonPlan", DtPoluvagonPlan);
		model.addAttribute("DtSisternaPlan", DtSisternaPlan);
		model.addAttribute("DtBoshqaPlan", DtBoshqaPlan);

		//yolovchi vagonlar plan
		int AndjToYolovchiPlan = planDto.getAndjTYolovchiPlanBiznes();
		int AndjDtYolovchiPlan = planDto.getAndjDtYolovchiPlanBiznes();
		
		model.addAttribute("AndjToYolovchiPlan", AndjToYolovchiPlan);
		model.addAttribute("AndjDtYolovchiPlan", AndjDtYolovchiPlan);


		//havos kapital tamir uchun plan
		int HavKtHammaPlan = planDto.getHavKtKritiPlanBiznes() + planDto.getHavKtPlatformaPlanBiznes() + planDto.getHavKtPoluvagonPlanBiznes() + planDto.getHavKtSisternaPlanBiznes() + planDto.getHavKtBoshqaPlanBiznes();
		int HavKtKritiPlan = planDto.getHavKtKritiPlanBiznes();
		int HavKtPlatformaPlan = planDto.getHavKtPlatformaPlanBiznes();
		int HavKtPoluvagonPlan = planDto.getHavKtPoluvagonPlanBiznes();
		int HavKtSisternaPlan = planDto.getHavKtSisternaPlanBiznes();
		int HavKtBoshqaPlan = planDto.getHavKtBoshqaPlanBiznes();

		model.addAttribute("HavKtHammaPlan", HavKtHammaPlan);
		model.addAttribute("HavKtKritiPlan", HavKtKritiPlan);
		model.addAttribute("HavKtPlatformaPlan", HavKtPlatformaPlan);
		model.addAttribute("HavKtPoluvagonPlan", HavKtPoluvagonPlan);
		model.addAttribute("HavKtSisternaPlan", HavKtSisternaPlan);
		model.addAttribute("HavKtBoshqaPlan", HavKtBoshqaPlan);

		//VCHD-5 kapital tamir uchun plan
		int AndjKtHammaPlan = planDto.getAndjKtKritiPlanBiznes() + planDto.getAndjKtPlatformaPlanBiznes() + planDto.getAndjKtPoluvagonPlanBiznes() + planDto.getAndjKtSisternaPlanBiznes() + planDto.getAndjKtBoshqaPlanBiznes();
		int AndjKtKritiPlan = planDto.getAndjKtKritiPlanBiznes();
		int AndjKtPlatformaPlan = planDto.getAndjKtPlatformaPlanBiznes();
		int AndjKtPoluvagonPlan = planDto.getAndjKtPoluvagonPlanBiznes();
		int AndjKtSisternaPlan = planDto.getAndjKtSisternaPlanBiznes();
		int AndjKtBoshqaPlan = planDto.getAndjKtBoshqaPlanBiznes();

		model.addAttribute("AndjKtHammaPlan", AndjKtHammaPlan);
		model.addAttribute("AndjKtKritiPlan", AndjKtKritiPlan);
		model.addAttribute("AndjKtPlatformaPlan", AndjKtPlatformaPlan);
		model.addAttribute("AndjKtPoluvagonPlan", AndjKtPoluvagonPlan);
		model.addAttribute("AndjKtSisternaPlan", AndjKtSisternaPlan);
		model.addAttribute("AndjKtBoshqaPlan", AndjKtBoshqaPlan);

		//VCHD-6 kapital tamir uchun plan
		int SamKtHammaPlan = planDto.getSamKtKritiPlanBiznes() + planDto.getSamKtPlatformaPlanBiznes() + planDto.getSamKtPoluvagonPlanBiznes() + planDto.getSamKtSisternaPlanBiznes() + planDto.getSamKtBoshqaPlanBiznes();
		int SamKtKritiPlan = planDto.getSamKtKritiPlanBiznes();
		int SamKtPlatformaPlan = planDto.getSamKtPlatformaPlanBiznes();
		int SamKtPoluvagonPlan = planDto.getSamKtPoluvagonPlanBiznes();
		int SamKtSisternaPlan = planDto.getSamKtSisternaPlanBiznes();
		int SamKtBoshqaPlan = planDto.getSamKtBoshqaPlanBiznes();

		model.addAttribute("SamKtHammaPlan", SamKtHammaPlan);
		model.addAttribute("SamKtKritiPlan", SamKtKritiPlan);
		model.addAttribute("SamKtPlatformaPlan", SamKtPlatformaPlan);
		model.addAttribute("SamKtPoluvagonPlan", SamKtPoluvagonPlan);
		model.addAttribute("SamKtSisternaPlan", SamKtSisternaPlan);
		model.addAttribute("SamKtBoshqaPlan", SamKtBoshqaPlan);

		//kapital itogo
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = planDto.getAndjKtKritiPlanBiznes() + planDto.getHavKtKritiPlanBiznes() + planDto.getSamKtKritiPlanBiznes();
		int KtPlatformaPlan = planDto.getAndjKtPlatformaPlanBiznes() + planDto.getHavKtPlatformaPlanBiznes() + planDto.getSamKtPlatformaPlanBiznes();
		int KtPoluvagonPlan = planDto.getAndjKtPoluvagonPlanBiznes() + planDto.getHavKtPoluvagonPlanBiznes() + planDto.getSamKtPoluvagonPlanBiznes();
		int KtSisternaPlan = planDto.getAndjKtSisternaPlanBiznes() + planDto.getHavKtSisternaPlanBiznes() + planDto.getSamKtSisternaPlanBiznes();
		int KtBoshqaPlan = planDto.getAndjKtBoshqaPlanBiznes() + planDto.getHavKtBoshqaPlanBiznes() + planDto.getSamKtBoshqaPlanBiznes();

		model.addAttribute("KtHammaPlan", KtHammaPlan);
		model.addAttribute("KtKritiPlan", KtKritiPlan);
		model.addAttribute("KtPlatformaPlan", KtPlatformaPlan);
		model.addAttribute("KtPoluvagonPlan", KtPoluvagonPlan);
		model.addAttribute("KtSisternaPlan", KtSisternaPlan);
		model.addAttribute("KtBoshqaPlan", KtBoshqaPlan);

		//VCHD-3 KRP plan
		int HavKrpHammaPlan =  planDto.getHavKrpKritiPlanBiznes() + planDto.getHavKrpPlatformaPlanBiznes() + planDto.getHavKrpPoluvagonPlanBiznes() + planDto.getHavKrpSisternaPlanBiznes() + planDto.getHavKrpBoshqaPlanBiznes();
		int HavKrpKritiPlan = planDto.getHavKrpKritiPlanBiznes();
		int HavKrpPlatformaPlan = planDto.getHavKrpPlatformaPlanBiznes();
		int HavKrpPoluvagonPlan = planDto.getHavKrpPoluvagonPlanBiznes();
		int HavKrpSisternaPlan = planDto.getHavKrpSisternaPlanBiznes();
		int HavKrpBoshqaPlan = planDto.getHavKrpBoshqaPlanBiznes();
		
		model.addAttribute("HavKrpHammaPlan", HavKrpHammaPlan);
		model.addAttribute("HavKrpKritiPlan", HavKrpKritiPlan);
		model.addAttribute("HavKrpPlatformaPlan", HavKrpPlatformaPlan);
		model.addAttribute("HavKrpPoluvagonPlan", HavKrpPoluvagonPlan);
		model.addAttribute("HavKrpSisternaPlan", HavKrpSisternaPlan);
		model.addAttribute("HavKrpBoshqaPlan", HavKrpBoshqaPlan);

		//VCHD-5 Krp plan
		int AndjKrpHammaPlan =  planDto.getAndjKrpKritiPlanBiznes() + planDto.getAndjKrpPlatformaPlanBiznes() + planDto.getAndjKrpPoluvagonPlanBiznes() + planDto.getAndjKrpSisternaPlanBiznes() + planDto.getAndjKrpBoshqaPlanBiznes();
		int AndjKrpKritiPlan = planDto.getAndjKrpKritiPlanBiznes();
		int AndjKrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanBiznes();
		int AndjKrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanBiznes();
		int AndjKrpSisternaPlan = planDto.getAndjKrpSisternaPlanBiznes();
		int AndjKrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanBiznes();

		model.addAttribute("AndjKrpHammaPlan", AndjKrpHammaPlan);
		model.addAttribute("AndjKrpKritiPlan", AndjKrpKritiPlan);
		model.addAttribute("AndjKrpPlatformaPlan", AndjKrpPlatformaPlan);
		model.addAttribute("AndjKrpPoluvagonPlan", AndjKrpPoluvagonPlan);
		model.addAttribute("AndjKrpSisternaPlan", AndjKrpSisternaPlan);
		model.addAttribute("AndjKrpBoshqaPlan", AndjKrpBoshqaPlan);


		//samarqand KRP plan
		int SamKrpHammaPlan = planDto.getSamKrpKritiPlanBiznes() + planDto.getSamKrpPlatformaPlanBiznes() + planDto.getSamKrpPoluvagonPlanBiznes() + planDto.getSamKrpSisternaPlanBiznes() + planDto.getSamKrpBoshqaPlanBiznes();
		int SamKrpKritiPlan = planDto.getSamKrpKritiPlanBiznes();
		int SamKrpPlatformaPlan = planDto.getSamKrpPlatformaPlanBiznes();
		int SamKrpPoluvagonPlan = planDto.getSamKrpPoluvagonPlanBiznes();
		int SamKrpSisternaPlan = planDto.getSamKrpSisternaPlanBiznes();
		int SamKrpBoshqaPlan = planDto.getSamKrpBoshqaPlanBiznes();

		model.addAttribute("SamKrpHammaPlan", SamKrpHammaPlan);
		model.addAttribute("SamKrpKritiPlan", SamKrpKritiPlan);
		model.addAttribute("SamKrpPlatformaPlan", SamKrpPlatformaPlan);
		model.addAttribute("SamKrpPoluvagonPlan", SamKrpPoluvagonPlan);
		model.addAttribute("SamKrpSisternaPlan", SamKrpSisternaPlan);
		model.addAttribute("SamKrpBoshqaPlan", SamKrpBoshqaPlan);
		
		//Krp itogo plan
		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = planDto.getAndjKrpKritiPlanBiznes() + planDto.getHavKrpKritiPlanBiznes() + planDto.getSamKrpKritiPlanBiznes();
		int KrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanBiznes() + planDto.getHavKrpPlatformaPlanBiznes() + planDto.getSamKrpPlatformaPlanBiznes();
		int KrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanBiznes() + planDto.getHavKrpPoluvagonPlanBiznes() + planDto.getSamKrpPoluvagonPlanBiznes();
		int KrpSisternaPlan = planDto.getAndjKrpSisternaPlanBiznes() + planDto.getHavKrpSisternaPlanBiznes() + planDto.getSamKrpSisternaPlanBiznes();
		int KrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanBiznes() + planDto.getHavKrpBoshqaPlanBiznes() + planDto.getSamKrpBoshqaPlanBiznes();

		model.addAttribute("KrpHammaPlan", KrpHammaPlan);
		model.addAttribute("KrpKritiPlan", KrpKritiPlan);
		model.addAttribute("KrpPlatformaPlan", KrpPlatformaPlan);
		model.addAttribute("KrpPoluvagonPlan", KrpPoluvagonPlan);
		model.addAttribute("KrpSisternaPlan", KrpSisternaPlan);
		model.addAttribute("KrpBoshqaPlan", KrpBoshqaPlan);

		// factlar

		//VCHD-3 uchun depli tamir
		int hdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);
		int hdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy);
		int hdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)", oy);
		int hdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy);
		int hdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)", oy);
		int hdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		model.addAttribute("hdHamma",hdHamma);
		model.addAttribute("hdKriti", hdKriti);
		model.addAttribute("hdPlatforma", hdPlatforma);
		model.addAttribute("hdPoluvagon", hdPoluvagon);
		model.addAttribute("hdSisterna", hdSisterna);
		model.addAttribute("hdBoshqa", hdBoshqa);

		//VCHD-5 uchun depli tamir
		int adHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)", oy);
		int adKriti =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy);
		int adPlatforma =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)", oy);
		int adPoluvagon =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy);
		int adSisterna =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)", oy);
		int adBoshqa =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		model.addAttribute("adHamma",adHamma);
		model.addAttribute("adKriti", adKriti);
		model.addAttribute("adPlatforma", adPlatforma);
		model.addAttribute("adPoluvagon", adPoluvagon);
		model.addAttribute("adSisterna", adSisterna);
		model.addAttribute("adBoshqa", adBoshqa);

		//samarqand uchun depli tamir
		int sdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);
		int sdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy);
		int sdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)", oy);
		int sdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy);
		int sdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)", oy);
		int sdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		model.addAttribute("sdHamma",sdHamma);
		model.addAttribute("sdKriti", sdKriti);
		model.addAttribute("sdPlatforma", sdPlatforma);
		model.addAttribute("sdPoluvagon", sdPoluvagon);
		model.addAttribute("sdSisterna", sdSisterna);
		model.addAttribute("sdBoshqa", sdBoshqa);

		// itogo Fact uchun depli tamir
		int uvtdHamma = sdHamma + hdHamma + adHamma;
		int uvtdKriti = sdKriti + hdKriti + adKriti;
		int uvtdPlatforma = sdPlatforma + adPlatforma + hdPlatforma;
		int uvtdPoluvagon = sdPoluvagon + hdPoluvagon + adPoluvagon;
		int uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
		int uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

		model.addAttribute("uvtdHamma",uvtdHamma);
		model.addAttribute("uvtdKriti",uvtdKriti);
		model.addAttribute("uvtdPlatforma",uvtdPlatforma);
		model.addAttribute("uvtdPoluvagon",uvtdPoluvagon);
		model.addAttribute("uvtdSisterna",uvtdSisterna);
		model.addAttribute("uvtdBoshqa",uvtdBoshqa);


		//Yolovchi vagon Fact
		int atYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yo'lovchi vagon(пассжир)","TO-3", oy);
		int adYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yo'lovchi vagon(пассжир)","Depoli ta’mir(ДР)", oy);

		model.addAttribute("atYolovchi", atYolovchi);
		model.addAttribute("adYolovchi", adYolovchi);
		
		//VCHD-3 uchun kapital tamir
		int hkHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);
		int hkKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy);
		int hkPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)", oy);
		int hkPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy);
		int hkSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)", oy);
		int hkBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		model.addAttribute("hkHamma",hkHamma);
		model.addAttribute("hkKriti", hkKriti);
		model.addAttribute("hkPlatforma", hkPlatforma);
		model.addAttribute("hkPoluvagon", hkPoluvagon);
		model.addAttribute("hkSisterna", hkSisterna);
		model.addAttribute("hkBoshqa", hkBoshqa);
		
		//VCHD-3 uchun kapital tamir
		int akHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)", oy);
		int akKriti =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy);
		int akPlatforma =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)", oy);
		int akPoluvagon =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy);
		int akSisterna =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)", oy);
		int akBoshqa =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		model.addAttribute( "akHamma", akHamma);
		model.addAttribute( "akKriti", akKriti);
		model.addAttribute( "akPlatforma", akPlatforma);
		model.addAttribute( "akPoluvagon", akPoluvagon);
		model.addAttribute( "akSisterna", akSisterna);
		model.addAttribute( "akBoshqa", akBoshqa);

		//samarqand uchun Kapital tamir
		int skHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);
		int skKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy);
		int skPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)", oy);
		int skPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy);
		int skSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)", oy);
		int skBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

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
		

		//VCHD-3 uchun kapital tamir
		int hkrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)", oy);
		int hkrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","KRP(КРП)", oy);
		int hkrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)", oy);
		int hkrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)", oy);
		int hkrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)", oy);
		int hkrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)", oy);

		model.addAttribute("hkrHamma",hkrHamma);
		model.addAttribute("hkrKriti", hkrKriti);
		model.addAttribute("hkrPlatforma", hkrPlatforma);
		model.addAttribute("hkrPoluvagon", hkrPoluvagon);
		model.addAttribute("hkrSisterna", hkrSisterna);
		model.addAttribute("hkrBoshqa", hkrBoshqa);

		//VCHD-3 uchun kapital tamir
		int akrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)", oy);
		int akrKriti =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)", oy);
		int akrPlatforma =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)", oy);
		int akrPoluvagon =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)", oy);
		int akrSisterna =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)", oy);
		int akrBoshqa =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)", oy);

		model.addAttribute( "akrHamma", akrHamma);
		model.addAttribute( "akrKriti", akrKriti);
		model.addAttribute( "akrPlatforma", akrPlatforma);
		model.addAttribute( "akrPoluvagon", akrPoluvagon);
		model.addAttribute( "akrSisterna", akrSisterna);
		model.addAttribute( "akrBoshqa", akrBoshqa);

		//samarqand uchun Kapital tamir
		int skrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)", oy);
		int skrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)", oy);
		int skrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)", oy);
		int skrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)", oy);
		int skrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)", oy);
		int skrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)", oy);

		model.addAttribute("skrHamma",skrHamma);
		model.addAttribute("skrKriti", skrKriti);
		model.addAttribute("skrPlatforma", skrPlatforma);
		model.addAttribute("skrPoluvagon", skrPoluvagon);
		model.addAttribute("skrSisterna", skrSisterna);
		model.addAttribute("skrBoshqa", skrBoshqa);

		// itogo Fact uchun KRP tamir
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
		vagonsToDownloadTable.add(uvtdHamma);
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

		vagonsToDownloadTable.add(AndjToYolovchiPlan);
		vagonsToDownloadTable.add(atYolovchi);
		vagonsToDownloadTable.add(AndjDtYolovchiPlan);
		vagonsToDownloadTable.add(adYolovchi);


		vagonsToDownloadAllTable = vagonsToDownloadTable;

    	 return "AllPlanTable";
	}



    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
   	@GetMapping("/vagons/planTableForMonths")
   	public String getPlanForAllMonths(Model model) {

		PlanBiznes planDto = vagonTayyorService.getPlanBiznes();
		//planlar kiritish

		//havos depo tamir hamma plan
		int HavDtKritiPlan = planDto.getHavDtKritiPlanBiznesMonths();
		int HavDtPlatformaPlan = planDto.getHavDtPlatformaPlanBiznesMonths();
		int HavDtPoluvagonPlan = planDto.getHavDtPoluvagonPlanBiznesMonths();
		int HavDtSisternaPlan = planDto.getHavDtSisternaPlanBiznesMonths();
		int HavDtBoshqaPlan = planDto.getHavDtBoshqaPlanBiznesMonths();
		int HavDtHammaPlan = HavDtKritiPlan + HavDtPlatformaPlan + HavDtPoluvagonPlan + HavDtSisternaPlan + HavDtBoshqaPlan;

		model.addAttribute("HavDtHammaPlan", HavDtHammaPlan);
		model.addAttribute("HavDtKritiPlan", HavDtKritiPlan);
		model.addAttribute("HavDtPlatformaPlan", HavDtPlatformaPlan);
		model.addAttribute("HavDtPoluvagonPlan", HavDtPoluvagonPlan);
		model.addAttribute("HavDtSisternaPlan", HavDtSisternaPlan);
		model.addAttribute("HavDtBoshqaPlan", HavDtBoshqaPlan);
		

		//VCHD-5 depo tamir plan
		int AndjDtKritiPlan = planDto.getAndjDtKritiPlanBiznesMonths();
		int AndjDtPlatformaPlan =planDto.getAndjDtPlatformaPlanBiznesMonths();
		int AndjDtPoluvagonPlan =planDto.getAndjDtPoluvagonPlanBiznesMonths();
		int AndjDtSisternaPlan =planDto.getAndjDtSisternaPlanBiznesMonths();
		int AndjDtBoshqaPlan=planDto.getAndjDtBoshqaPlanBiznesMonths();
		int AndjDtHammaPlan = AndjDtKritiPlan + AndjDtPlatformaPlan + AndjDtPoluvagonPlan + AndjDtSisternaPlan + AndjDtBoshqaPlan;

		model.addAttribute("AndjDtHammaPlan", AndjDtHammaPlan);
		model.addAttribute("AndjDtKritiPlan", AndjDtKritiPlan);
		model.addAttribute("AndjDtPlatformaPlan",AndjDtPlatformaPlan);
		model.addAttribute("AndjDtPoluvagonPlan", AndjDtPoluvagonPlan);
		model.addAttribute("AndjDtSisternaPlan", AndjDtSisternaPlan);
		model.addAttribute("AndjDtBoshqaPlan", AndjDtBoshqaPlan);

		//samarqand depo tamir plan
		int SamDtKritiPlan = planDto.getSamDtKritiPlanBiznesMonths();
		int SamDtPlatformaPlan = planDto.getSamDtPlatformaPlanBiznesMonths();
		int SamDtPoluvagonPlan =  planDto.getSamDtPoluvagonPlanBiznesMonths();
		int SamDtSisternaPlan = planDto.getSamDtSisternaPlanBiznesMonths();
		int SamDtBoshqaPlan = planDto.getSamDtBoshqaPlanBiznesMonths();
		int SamDtHammaPlan=SamDtKritiPlan + SamDtPlatformaPlan + SamDtPoluvagonPlan + SamDtSisternaPlan + SamDtBoshqaPlan;

		model.addAttribute("SamDtHammaPlan",SamDtHammaPlan);
		model.addAttribute("SamDtKritiPlan", SamDtKritiPlan);
		model.addAttribute("SamDtPlatformaPlan", SamDtPlatformaPlan);
		model.addAttribute("SamDtPoluvagonPlan", SamDtPoluvagonPlan);
		model.addAttribute("SamDtSisternaPlan", SamDtSisternaPlan);
		model.addAttribute("SamDtBoshqaPlan", SamDtBoshqaPlan);


		// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = SamDtKritiPlan + HavDtKritiPlan + AndjDtKritiPlan;
		int DtPlatformaPlan = SamDtPlatformaPlan + HavDtPlatformaPlan + AndjDtPlatformaPlan;
		int DtPoluvagonPlan = SamDtPoluvagonPlan + HavDtPoluvagonPlan + AndjDtPoluvagonPlan;
		int DtSisternaPlan = SamDtSisternaPlan + HavDtSisternaPlan + AndjDtSisternaPlan;
		int DtBoshqaPlan = SamDtBoshqaPlan + HavDtBoshqaPlan + AndjDtBoshqaPlan;
		
		model.addAttribute("DtHammaPlan", DtHammaPlan);
		model.addAttribute("DtKritiPlan", DtKritiPlan);
		model.addAttribute("DtPlatformaPlan", DtPlatformaPlan);
		model.addAttribute("DtPoluvagonPlan",DtPoluvagonPlan);
		model.addAttribute("DtSisternaPlan", DtSisternaPlan);
		model.addAttribute("DtBoshqaPlan", DtBoshqaPlan);
		

		//Yolovchi vagon Plan
		int AndjToYolovchiPlan = planDto.getAndjTYolovchiPlanBiznesMonths();
		int AndjDtYolovchiPlan = planDto.getAndjDtYolovchiPlanBiznesMonths();

		model.addAttribute("AndjToYolovchiPlan", AndjToYolovchiPlan);
		model.addAttribute("AndjDtYolovchiPlan", AndjDtYolovchiPlan);

		//hovos kapital plan
		int HavKtKritiPlan = planDto.getHavKtKritiPlanBiznesMonths();
		int HavKtPlatformaPlan = planDto.getHavKtPlatformaPlanBiznesMonths();
		int HavKtPoluvagonPlan = planDto.getHavKtPoluvagonPlanBiznesMonths();
		int HavKtSisternaPlan = planDto.getHavKtSisternaPlanBiznesMonths();
		int HavKtBoshqaPlan = planDto.getHavKtBoshqaPlanBiznesMonths();
		int HavKtHammaPlan = HavKtKritiPlan + HavKtPlatformaPlan + HavKtPoluvagonPlan + HavKtSisternaPlan + HavKtBoshqaPlan;

		model.addAttribute("HavKtHammaPlan", HavKtHammaPlan);
		model.addAttribute("HavKtKritiPlan", HavKtKritiPlan);
		model.addAttribute("HavKtPlatformaPlan", HavKtPlatformaPlan);
		model.addAttribute("HavKtPoluvagonPlan", HavKtPoluvagonPlan);
		model.addAttribute("HavKtSisternaPlan", HavKtSisternaPlan);
		model.addAttribute("HavKtBoshqaPlan", HavKtBoshqaPlan);

		//ANDIJON kapital plan
		int AndjKtKritiPlan = planDto.getAndjKtKritiPlanBiznesMonths();
		int AndjKtPlatformaPlan=planDto.getAndjKtPlatformaPlanBiznesMonths();
		int AndjKtPoluvagonPlan=planDto.getAndjKtPoluvagonPlanBiznesMonths();
		int AndjKtSisternaPlan=planDto.getAndjKtSisternaPlanBiznesMonths();
		int AndjKtBoshqaPlan=planDto.getAndjKtBoshqaPlanBiznesMonths();
		int AndjKtHammaPlan = AndjKtKritiPlan + AndjKtPlatformaPlan + AndjKtPoluvagonPlan + AndjKtSisternaPlan + AndjKtBoshqaPlan;
		
		model.addAttribute("AndjKtHammaPlan", AndjKtHammaPlan);
		model.addAttribute("AndjKtKritiPlan", AndjKtKritiPlan);
		model.addAttribute("AndjKtPlatformaPlan", AndjKtPlatformaPlan);
		model.addAttribute("AndjKtPoluvagonPlan", AndjKtPoluvagonPlan);
		model.addAttribute("AndjKtSisternaPlan", AndjKtSisternaPlan);
		model.addAttribute("AndjKtBoshqaPlan", AndjKtBoshqaPlan);

		//Samrqand kapital plan
		int SamKtKritiPlan = planDto.getSamKtKritiPlanBiznesMonths();
		int SamKtPlatformaPlan = planDto.getSamKtPlatformaPlanBiznesMonths();
		int SamKtPoluvagonPlan = planDto.getSamKtPoluvagonPlanBiznesMonths();
		int SamKtSisternaPlan = planDto.getSamKtSisternaPlanBiznesMonths();
		int SamKtBoshqaPlan = planDto.getSamKtBoshqaPlanBiznesMonths();
		int SamKtHammaPlan = SamKtKritiPlan + SamKtPlatformaPlan + SamKtPoluvagonPlan + SamKtSisternaPlan +SamKtBoshqaPlan;

		model.addAttribute("SamKtHammaPlan",SamKtHammaPlan);
		model.addAttribute("SamKtKritiPlan", SamKtKritiPlan);
		model.addAttribute("SamKtPlatformaPlan", SamKtPlatformaPlan);
		model.addAttribute("SamKtPoluvagonPlan", SamKtPoluvagonPlan);
		model.addAttribute("SamKtSisternaPlan", SamKtSisternaPlan);
		model.addAttribute("SamKtBoshqaPlan", SamKtBoshqaPlan);


		//Itogo kapital plan
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = SamKtKritiPlan + HavKtKritiPlan + AndjKtKritiPlan;
		int KtPlatformaPlan = SamKtPlatformaPlan + HavKtPlatformaPlan + AndjKtPlatformaPlan;
		int KtPoluvagonPlan = SamKtPoluvagonPlan + HavKtPoluvagonPlan + AndjKtPoluvagonPlan;
		int KtSisternaPlan = SamKtSisternaPlan + HavKtSisternaPlan + AndjKtSisternaPlan;
		int KtBoshqaPlan = SamKtBoshqaPlan + HavKtBoshqaPlan + AndjKtBoshqaPlan;

		model.addAttribute("KtHammaPlan", KtHammaPlan);
		model.addAttribute("KtKritiPlan", KtKritiPlan);
		model.addAttribute("KtPlatformaPlan", KtPlatformaPlan);
		model.addAttribute("KtPoluvagonPlan",KtPoluvagonPlan);
		model.addAttribute("KtSisternaPlan", KtSisternaPlan);
		model.addAttribute("KtBoshqaPlan", KtBoshqaPlan);

		
		//Hovos krp plan
		int HavKrpKritiPlan = planDto.getHavKrpKritiPlanBiznesMonths();
		int HavKrpPlatformaPlan = planDto.getHavKrpPlatformaPlanBiznesMonths();
		int HavKrpPoluvagonPlan = planDto.getHavKrpPoluvagonPlanBiznesMonths();
		int HavKrpSisternaPlan = planDto.getHavKrpSisternaPlanBiznesMonths();
		int HavKrpBoshqaPlan = planDto.getHavKrpBoshqaPlanBiznesMonths();
		int HavKrpHammaPlan = HavKrpKritiPlan + HavKrpPlatformaPlan + HavKrpPoluvagonPlan + HavKrpSisternaPlan + HavKrpBoshqaPlan;

		model.addAttribute("HavKrpHammaPlan",HavKrpHammaPlan);
		model.addAttribute("HavKrpKritiPlan", HavKrpKritiPlan);
		model.addAttribute("HavKrpPlatformaPlan", HavKrpPlatformaPlan);
		model.addAttribute("HavKrpPoluvagonPlan", HavKrpPoluvagonPlan);
		model.addAttribute("HavKrpSisternaPlan", HavKrpSisternaPlan);
		model.addAttribute("HavKrpBoshqaPlan", HavKrpBoshqaPlan);

		//andijon krp plan
		int AndjKrpKritiPlan = planDto.getAndjKrpKritiPlanBiznesMonths();
		int AndjKrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanBiznesMonths();
		int AndjKrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanBiznesMonths();
		int AndjKrpSisternaPlan = planDto.getAndjKrpSisternaPlanBiznesMonths();
		int AndjKrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanBiznesMonths();
		int AndjKrpHammaPlan = AndjKrpKritiPlan + AndjKrpPlatformaPlan + AndjKrpPoluvagonPlan + AndjKrpSisternaPlan + AndjKrpBoshqaPlan;

		model.addAttribute("AndjKrpHammaPlan",AndjKrpHammaPlan);
		model.addAttribute("AndjKrpKritiPlan", AndjKrpKritiPlan);
		model.addAttribute("AndjKrpPlatformaPlan", AndjKrpPlatformaPlan);
		model.addAttribute("AndjKrpPoluvagonPlan", AndjKrpPoluvagonPlan);
		model.addAttribute("AndjKrpSisternaPlan", AndjKrpSisternaPlan);
		model.addAttribute("AndjKrpBoshqaPlan", AndjKrpBoshqaPlan);

		//Samarqankr Krp plan
		int SamKrpKritiPlan = planDto.getSamKrpKritiPlanBiznesMonths();
		int SamKrpPlatformaPlan = planDto.getSamKrpPlatformaPlanBiznesMonths();
		int SamKrpPoluvagonPlan = planDto.getSamKrpPoluvagonPlanBiznesMonths();
		int SamKrpSisternaPlan = planDto.getSamKrpSisternaPlanBiznesMonths();
		int SamKrpBoshqaPlan = planDto.getSamKrpBoshqaPlanBiznesMonths();
		int SamKrpHammaPlan = SamKrpKritiPlan + SamKrpPlatformaPlan + SamKrpPoluvagonPlan + SamKrpSisternaPlan + SamKrpBoshqaPlan;

		model.addAttribute("SamKrpHammaPlan", SamKrpHammaPlan);
		model.addAttribute("SamKrpKritiPlan", SamKrpKritiPlan);
		model.addAttribute("SamKrpPlatformaPlan", SamKrpPlatformaPlan);
		model.addAttribute("SamKrpPoluvagonPlan", SamKrpPoluvagonPlan);
		model.addAttribute("SamKrpSisternaPlan", SamKrpSisternaPlan);
		model.addAttribute("SamKrpBoshqaPlan", SamKrpBoshqaPlan);


		//itogo krp

		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = SamKrpKritiPlan + HavKrpKritiPlan + AndjKrpKritiPlan;
		int KrpPlatformaPlan = SamKrpPlatformaPlan + HavKrpPlatformaPlan + AndjKrpPlatformaPlan;
		int KrpPoluvagonPlan = SamKrpPoluvagonPlan + HavKrpPoluvagonPlan + AndjKrpPoluvagonPlan;
		int KrpSisternaPlan = SamKrpSisternaPlan + HavKrpSisternaPlan + AndjKrpSisternaPlan;
		int KrpBoshqaPlan = SamKrpBoshqaPlan + HavKrpBoshqaPlan + AndjKrpBoshqaPlan;

		model.addAttribute("KrpHammaPlan", KrpHammaPlan);
		model.addAttribute("KrpKritiPlan", KrpKritiPlan);
		model.addAttribute("KrpPlatformaPlan", KrpPlatformaPlan);
		model.addAttribute("KrpPoluvagonPlan",KrpPoluvagonPlan);
		model.addAttribute("KrpSisternaPlan", KrpSisternaPlan);
		model.addAttribute("KrpBoshqaPlan", KrpBoshqaPlan);
		
		//**//
		// VCHD-3 depo tamir hamma false vagonlar soni
		int hdKriti=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)");
		int hdPlatforma=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)");
		int hdPoluvagon=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)");
		int hdSisterna=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)");
		int hdBoshqa=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)");
		int hdHamma = hdKriti + hdPlatforma + hdPoluvagon + hdSisterna + hdBoshqa;

		model.addAttribute("hdHamma",hdHamma + 651);
		model.addAttribute("hdKriti",hdKriti + 35);
		model.addAttribute("hdPlatforma",hdPlatforma + 45);
		model.addAttribute("hdPoluvagon",hdPoluvagon + 109);
		model.addAttribute("hdSisterna",hdSisterna + 35);
		model.addAttribute("hdBoshqa",hdBoshqa + 427);

		// VCHD-5 depo tamir hamma false vagonlar soni
		int adKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)");
		int adPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)");
		int adPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)");
		int adSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)");
		int adBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)");
		int adHamma = adKriti + adPlatforma+ adPoluvagon+ adSisterna + adBoshqa;

		model.addAttribute("adHamma",adHamma + 443);
		model.addAttribute("adKriti",adKriti + 224);
		model.addAttribute("adPlatforma",adPlatforma + 3);
		model.addAttribute("adPoluvagon",adPoluvagon + 103);
		model.addAttribute("adSisterna",adSisterna);
		model.addAttribute("adBoshqa",adBoshqa + 113);

		// samarqand depo tamir hamma false vagonlar soni
		int sdKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)");
		int sdPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)");
		int sdPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)");
		int sdSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)");
		int sdBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)");
		int sdHamma = sdKriti + sdPlatforma+ sdPoluvagon+ sdSisterna + sdBoshqa;

		model.addAttribute("sdHamma",sdHamma + 393);
		model.addAttribute("sdKriti",sdKriti + 135);
		model.addAttribute("sdPlatforma",sdPlatforma + 8);
		model.addAttribute("sdPoluvagon",sdPoluvagon + 67);
		model.addAttribute("sdSisterna",sdSisterna + 23);
		model.addAttribute("sdBoshqa",sdBoshqa + 160);

		// depoli tamir itogo uchun
		int uvtdHamma =  adHamma + hdHamma+sdHamma;
		int uvtdKriti = sdKriti + hdKriti + adKriti;
		int uvtdPlatforma = adPlatforma + sdPlatforma + hdPlatforma;
		int uvtdPoluvagon  = adPoluvagon + sdPoluvagon + hdPoluvagon;
		int uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
		int uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

		model.addAttribute("uvtdHamma",uvtdHamma + 1487);
		model.addAttribute("uvtdKriti",uvtdKriti + 394);
		model.addAttribute("uvtdPlatforma",uvtdPlatforma + 56);
		model.addAttribute("uvtdPoluvagon",uvtdPoluvagon + 279);
		model.addAttribute("uvtdSisterna",uvtdSisterna + 58);
		model.addAttribute("uvtdBoshqa",uvtdBoshqa + 700);

		//Yolovchi Andijon fact
		int atYolovchi=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yo'lovchi vagon(пассжир)","TO-3");
		int adYolovchi=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yo'lovchi vagon(пассжир)","Depoli ta’mir(ДР)");

		model.addAttribute("atYolovchi",atYolovchi + 37);
		model.addAttribute("adYolovchi",adYolovchi + 24);


		// VCHD-3 kapital tamir hamma false vagonlar soni
		int hkKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Kapital ta’mir(КР)");
		int hkPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)");
		int hkPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)");
		int hkSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)");
		int hkBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)");
		int hkHamma = hkKriti + hkPlatforma+ hkPoluvagon+ hkSisterna + hkBoshqa;

		model.addAttribute("hkHamma",hkHamma + 227);
		model.addAttribute("hkKriti",hkKriti + 41);
		model.addAttribute("hkPlatforma",hkPlatforma + 32);
		model.addAttribute("hkPoluvagon",hkPoluvagon + 4);
		model.addAttribute("hkSisterna",hkSisterna + 51);
		model.addAttribute("hkBoshqa",hkBoshqa + 99);

		// VCHD-5 kapital tamir hamma false vagonlar soni
		int akKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)");
		int akPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)");
		int akPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)");
		int akSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)");
		int akBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)");
		int akHamma = akKriti + akPlatforma+ akPoluvagon+ akSisterna + akBoshqa;

		model.addAttribute("akHamma",akHamma + 28);
		model.addAttribute("akKriti",akKriti + 26);
		model.addAttribute("akPlatforma",akPlatforma);
		model.addAttribute("akPoluvagon",akPoluvagon + 2);
		model.addAttribute("akSisterna",akSisterna);
		model.addAttribute("akBoshqa",akBoshqa);
		
		// samarqand KApital tamir hamma false vagonlar soni
		int skKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)");
		int skPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)");
		int skPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)");
		int skSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)");
		int skBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)");
		int skHamma = skKriti + skPlatforma+ skPoluvagon+ skSisterna + skBoshqa;

		model.addAttribute("skHamma",skHamma + 284);
		model.addAttribute("skKriti",skKriti + 160);
		model.addAttribute("skPlatforma",skPlatforma + 44);
		model.addAttribute("skPoluvagon",skPoluvagon + 52);
		model.addAttribute("skSisterna",skSisterna + 9);
		model.addAttribute("skBoshqa",skBoshqa + 19);
		
		// Kapital tamir itogo uchun
		int uvtkHamma =  akHamma + hkHamma+skHamma;
		int uvtkKriti = skKriti + hkKriti + akKriti;
		int uvtkPlatforma = akPlatforma + skPlatforma + hkPlatforma;
		int uvtkPoluvagon  = akPoluvagon + skPoluvagon + hkPoluvagon;
		int uvtkSisterna = akSisterna + hkSisterna + skSisterna;
		int uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

		model.addAttribute("uvtkHamma",uvtkHamma + 539);
		model.addAttribute("uvtkKriti",uvtkKriti + 227);
		model.addAttribute("uvtkPlatforma",uvtkPlatforma + 76);
		model.addAttribute("uvtkPoluvagon",uvtkPoluvagon + 58);
		model.addAttribute("uvtkSisterna",uvtkSisterna + 60);
		model.addAttribute("uvtkBoshqa",uvtkBoshqa + 118);

		//**

		// VCHD-3 KRP tamir hamma false vagonlar soni
		int hkrKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","KRP(КРП)");
		int hkrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)");
		int hkrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)");
		int hkrSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)");
		int hkrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)");
		int hkrHamma = hkrKriti + hkrPlatforma+ hkrPoluvagon+ hkrSisterna + hkrBoshqa;

		model.addAttribute("hkrHamma",hkrHamma+83);
		model.addAttribute("hkrKriti",hkrKriti+83);
		model.addAttribute("hkrPlatforma",hkrPlatforma);
		model.addAttribute("hkrPoluvagon",hkrPoluvagon);
		model.addAttribute("hkrSisterna",hkrSisterna);
		model.addAttribute("hkrBoshqa",hkrBoshqa);

		// VCHD-5 KRP tamir hamma false vagonlar soni
		int akrKriti=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)");
		int akrPlatforma=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)");
		int akrPoluvagon=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)");
		int akrSisterna=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)");
		int akrBoshqa=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)");
		int akrHamma = akrKriti + akrPlatforma+ akrPoluvagon+ akrSisterna + akrBoshqa;

		model.addAttribute("akrHamma",akrHamma+61);
		model.addAttribute("akrKriti",akrKriti);
		model.addAttribute("akrPlatforma",akrPlatforma);
		model.addAttribute("akrPoluvagon",akrPoluvagon+61);
		model.addAttribute("akrSisterna",akrSisterna);
		model.addAttribute("akBoshqa",akrBoshqa);

		// samarqand KRP tamir hamma false vagonlar soni
		int skrKriti=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)");
		int skrPlatforma=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)");
		int skrPoluvagon=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)");
		int skrSisterna=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)");
		int skrBoshqa=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)");
		int skrHamma = skrKriti + skrPlatforma+ skrPoluvagon+ skrSisterna + skrBoshqa;

		model.addAttribute("skrHamma",skrHamma+89);
		model.addAttribute("skrKriti",skrKriti);
		model.addAttribute("skrPlatforma",skrPlatforma);
		model.addAttribute("skrPoluvagon",skrPoluvagon + 88);
		model.addAttribute("skrSisterna",skrSisterna + 1);
		model.addAttribute("skrBoshqa",skrBoshqa);
// Krp itogo uchun
		int uvtkrHamma =  akrHamma + hkrHamma+skrHamma;
		int uvtkrKriti = skrKriti + hkrKriti + akrKriti;
		int uvtkrPlatforma = akrPlatforma + skrPlatforma + hkrPlatforma;
		int uvtkrPoluvagon  = akrPoluvagon + skrPoluvagon + hkrPoluvagon;
		int uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
		int uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

		model.addAttribute("uvtkrHamma",uvtkrHamma + 233);
		model.addAttribute("uvtkrKriti",uvtkrKriti);
		model.addAttribute("uvtkrPlatforma",uvtkrPlatforma);
		model.addAttribute("uvtkrPoluvagon",uvtkrPoluvagon + 232);
		model.addAttribute("uvtkrSisterna",uvtkrSisterna + 1);
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
		vagonsToDownloadTable.add(uvtdHamma);
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
		vagonsToDownloadTable.add(uvtkHamma);
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
		vagonsToDownloadTable.add(uvtkrHamma);
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

		vagonsToDownloadTable.add(AndjToYolovchiPlan);
		vagonsToDownloadTable.add(atYolovchi);
		vagonsToDownloadTable.add(AndjDtYolovchiPlan);
		vagonsToDownloadTable.add(adYolovchi);


		vagonsToDownloadAllTable = vagonsToDownloadTable;

		// hamma false vagonlarni list qilib chiqarish
		vagonsToDownload = vagonTayyorService.findAll();
		model.addAttribute("vagons", vagonTayyorService.findAll());

    	return "planTableForMonths";
    }

    // wagon nomer orqali qidirish 1 oylida
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/searchTayyor")
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
			model.addAttribute("vagons", vagonTayyorService.findAll(oy));
			vagonsToDownload = vagonTayyorService.findAll(oy);
		}else {
			model.addAttribute("vagons", vagonTayyorService.searchByNomer(participant, oy));
			List<VagonTayyor> emptyList = new ArrayList<>();
			vagonsToDownload = emptyList;
			vagonsToDownload.add( vagonTayyorService.searchByNomer(participant, oy));
		}
		//vaqtni olib turadi
		model.addAttribute("samDate",vagonTayyorService.getSamDate());
		model.addAttribute("havDate", vagonTayyorService.getHavDate());
		model.addAttribute("andjDate",vagonTayyorService.getAndjDate());

		PlanBiznes planDto = vagonTayyorService.getPlanBiznes();
		//planlar kiritish

		//havos hamma plan
		int HavDtHammaPlan = planDto.getHavDtKritiPlanBiznes() + planDto.getHavDtPlatformaPlanBiznes() + planDto.getHavDtPoluvagonPlanBiznes() + planDto.getHavDtSisternaPlanBiznes() + planDto.getHavDtBoshqaPlanBiznes();
		int HavDtKritiPlan = planDto.getHavDtKritiPlanBiznes();
		int HavDtPlatformaPlan = planDto.getHavDtPlatformaPlanBiznes();
		int HavDtPoluvagonPlan = planDto.getHavDtPoluvagonPlanBiznes();
		int HavDtSisternaPlan = planDto.getHavDtSisternaPlanBiznes();
		int HavDtBoshqaPlan = planDto.getHavDtBoshqaPlanBiznes();

		model.addAttribute("HavDtHammaPlan", HavDtHammaPlan);
		model.addAttribute("HavDtKritiPlan", HavDtKritiPlan);
		model.addAttribute("HavDtPlatformaPlan", HavDtPlatformaPlan);
		model.addAttribute("HavDtPoluvagonPlan", HavDtPoluvagonPlan);
		model.addAttribute("HavDtSisternaPlan", HavDtSisternaPlan);
		model.addAttribute("HavDtBoshqaPlan", HavDtBoshqaPlan);

		//andijon hamma plan depo tamir
		int AndjDtHammaPlan = planDto.getAndjDtKritiPlanBiznes() + planDto.getAndjDtPlatformaPlanBiznes() + planDto.getAndjDtPoluvagonPlanBiznes() + planDto.getAndjDtSisternaPlanBiznes() + planDto.getAndjDtBoshqaPlanBiznes();
		int AndjDtKritiPlan =  planDto.getAndjDtKritiPlanBiznes();
		int AndjDtPlatformaPlan =  planDto.getAndjDtPlatformaPlanBiznes();
		int AndjDtPoluvagonPlan =  planDto.getAndjDtPoluvagonPlanBiznes();
		int AndjDtSisternaPlan =  planDto.getAndjDtSisternaPlanBiznes();
		int AndjDtBoshqaPlan =  planDto.getAndjDtBoshqaPlanBiznes();

		model.addAttribute("AndjDtHammaPlan", AndjDtHammaPlan);
		model.addAttribute("AndjDtKritiPlan", AndjDtKritiPlan);
		model.addAttribute("AndjDtPlatformaPlan", AndjDtPlatformaPlan);
		model.addAttribute("AndjDtPoluvagonPlan", AndjDtPoluvagonPlan);
		model.addAttribute("AndjDtSisternaPlan", AndjDtSisternaPlan);
		model.addAttribute("AndjDtBoshqaPlan", AndjDtBoshqaPlan);

		//samarqand depo tamir
		int SamDtHammaPlan=planDto.getSamDtKritiPlanBiznes() + planDto.getSamDtPlatformaPlanBiznes() + planDto.getSamDtPoluvagonPlanBiznes() + planDto.getSamDtSisternaPlanBiznes() + planDto.getSamDtBoshqaPlanBiznes();
		int SamDtKritiPlan =  planDto.getSamDtKritiPlanBiznes();
		int SamDtPlatformaPlan =  planDto.getSamDtPlatformaPlanBiznes();
		int SamDtPoluvagonPlan =  planDto.getSamDtPoluvagonPlanBiznes();
		int SamDtSisternaPlan =  planDto.getSamDtSisternaPlanBiznes();
		int SamDtBoshqaPlan =  planDto.getSamDtBoshqaPlanBiznes();

		model.addAttribute("SamDtHammaPlan",SamDtHammaPlan);
		model.addAttribute("SamDtKritiPlan", SamDtKritiPlan);
		model.addAttribute("SamDtPlatformaPlan", SamDtPlatformaPlan);
		model.addAttribute("SamDtPoluvagonPlan", SamDtPoluvagonPlan);
		model.addAttribute("SamDtSisternaPlan", SamDtSisternaPlan);
		model.addAttribute("SamDtBoshqaPlan", SamDtBoshqaPlan);

		// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = planDto.getAndjDtKritiPlanBiznes() + planDto.getHavDtKritiPlanBiznes() + planDto.getSamDtKritiPlanBiznes();
		int DtPlatformaPlan = planDto.getAndjDtPlatformaPlanBiznes() + planDto.getHavDtPlatformaPlanBiznes() + planDto.getSamDtPlatformaPlanBiznes();
		int DtPoluvagonPlan = planDto.getAndjDtPoluvagonPlanBiznes() + planDto.getHavDtPoluvagonPlanBiznes() + planDto.getSamDtPoluvagonPlanBiznes();
		int DtSisternaPlan = planDto.getAndjDtSisternaPlanBiznes() + planDto.getHavDtSisternaPlanBiznes() + planDto.getSamDtSisternaPlanBiznes();
		int DtBoshqaPlan = planDto.getAndjDtBoshqaPlanBiznes() + planDto.getHavDtBoshqaPlanBiznes() + planDto.getSamDtBoshqaPlanBiznes();

		model.addAttribute("DtHammaPlan", DtHammaPlan);
		model.addAttribute("DtKritiPlan", DtKritiPlan);
		model.addAttribute("DtPlatformaPlan", DtPlatformaPlan);
		model.addAttribute("DtPoluvagonPlan", DtPoluvagonPlan);
		model.addAttribute("DtSisternaPlan", DtSisternaPlan);
		model.addAttribute("DtBoshqaPlan", DtBoshqaPlan);

		//yolovchi vagonlar plan
		int AndjToYolovchiPlan = planDto.getAndjTYolovchiPlanBiznes();
		int AndjDtYolovchiPlan = planDto.getAndjDtYolovchiPlanBiznes();

		model.addAttribute("AndjToYolovchiPlan", AndjToYolovchiPlan);
		model.addAttribute("AndjDtYolovchiPlan", AndjDtYolovchiPlan);


		//havos kapital tamir uchun plan
		int HavKtHammaPlan = planDto.getHavKtKritiPlanBiznes() + planDto.getHavKtPlatformaPlanBiznes() + planDto.getHavKtPoluvagonPlanBiznes() + planDto.getHavKtSisternaPlanBiznes() + planDto.getHavKtBoshqaPlanBiznes();
		int HavKtKritiPlan = planDto.getHavKtKritiPlanBiznes();
		int HavKtPlatformaPlan = planDto.getHavKtPlatformaPlanBiznes();
		int HavKtPoluvagonPlan = planDto.getHavKtPoluvagonPlanBiznes();
		int HavKtSisternaPlan = planDto.getHavKtSisternaPlanBiznes();
		int HavKtBoshqaPlan = planDto.getHavKtBoshqaPlanBiznes();

		model.addAttribute("HavKtHammaPlan", HavKtHammaPlan);
		model.addAttribute("HavKtKritiPlan", HavKtKritiPlan);
		model.addAttribute("HavKtPlatformaPlan", HavKtPlatformaPlan);
		model.addAttribute("HavKtPoluvagonPlan", HavKtPoluvagonPlan);
		model.addAttribute("HavKtSisternaPlan", HavKtSisternaPlan);
		model.addAttribute("HavKtBoshqaPlan", HavKtBoshqaPlan);

		//VCHD-5 kapital tamir uchun plan
		int AndjKtHammaPlan = planDto.getAndjKtKritiPlanBiznes() + planDto.getAndjKtPlatformaPlanBiznes() + planDto.getAndjKtPoluvagonPlanBiznes() + planDto.getAndjKtSisternaPlanBiznes() + planDto.getAndjKtBoshqaPlanBiznes();
		int AndjKtKritiPlan = planDto.getAndjKtKritiPlanBiznes();
		int AndjKtPlatformaPlan = planDto.getAndjKtPlatformaPlanBiznes();
		int AndjKtPoluvagonPlan = planDto.getAndjKtPoluvagonPlanBiznes();
		int AndjKtSisternaPlan = planDto.getAndjKtSisternaPlanBiznes();
		int AndjKtBoshqaPlan = planDto.getAndjKtBoshqaPlanBiznes();

		model.addAttribute("AndjKtHammaPlan", AndjKtHammaPlan);
		model.addAttribute("AndjKtKritiPlan", AndjKtKritiPlan);
		model.addAttribute("AndjKtPlatformaPlan", AndjKtPlatformaPlan);
		model.addAttribute("AndjKtPoluvagonPlan", AndjKtPoluvagonPlan);
		model.addAttribute("AndjKtSisternaPlan", AndjKtSisternaPlan);
		model.addAttribute("AndjKtBoshqaPlan", AndjKtBoshqaPlan);

		//VCHD-6 kapital tamir uchun plan
		int SamKtHammaPlan = planDto.getSamKtKritiPlanBiznes() + planDto.getSamKtPlatformaPlanBiznes() + planDto.getSamKtPoluvagonPlanBiznes() + planDto.getSamKtSisternaPlanBiznes() + planDto.getSamKtBoshqaPlanBiznes();
		int SamKtKritiPlan = planDto.getSamKtKritiPlanBiznes();
		int SamKtPlatformaPlan = planDto.getSamKtPlatformaPlanBiznes();
		int SamKtPoluvagonPlan = planDto.getSamKtPoluvagonPlanBiznes();
		int SamKtSisternaPlan = planDto.getSamKtSisternaPlanBiznes();
		int SamKtBoshqaPlan = planDto.getSamKtBoshqaPlanBiznes();

		model.addAttribute("SamKtHammaPlan", SamKtHammaPlan);
		model.addAttribute("SamKtKritiPlan", SamKtKritiPlan);
		model.addAttribute("SamKtPlatformaPlan", SamKtPlatformaPlan);
		model.addAttribute("SamKtPoluvagonPlan", SamKtPoluvagonPlan);
		model.addAttribute("SamKtSisternaPlan", SamKtSisternaPlan);
		model.addAttribute("SamKtBoshqaPlan", SamKtBoshqaPlan);

		//kapital itogo
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = planDto.getAndjKtKritiPlanBiznes() + planDto.getHavKtKritiPlanBiznes() + planDto.getSamKtKritiPlanBiznes();
		int KtPlatformaPlan = planDto.getAndjKtPlatformaPlanBiznes() + planDto.getHavKtPlatformaPlanBiznes() + planDto.getSamKtPlatformaPlanBiznes();
		int KtPoluvagonPlan = planDto.getAndjKtPoluvagonPlanBiznes() + planDto.getHavKtPoluvagonPlanBiznes() + planDto.getSamKtPoluvagonPlanBiznes();
		int KtSisternaPlan = planDto.getAndjKtSisternaPlanBiznes() + planDto.getHavKtSisternaPlanBiznes() + planDto.getSamKtSisternaPlanBiznes();
		int KtBoshqaPlan = planDto.getAndjKtBoshqaPlanBiznes() + planDto.getHavKtBoshqaPlanBiznes() + planDto.getSamKtBoshqaPlanBiznes();

		model.addAttribute("KtHammaPlan", KtHammaPlan);
		model.addAttribute("KtKritiPlan", KtKritiPlan);
		model.addAttribute("KtPlatformaPlan", KtPlatformaPlan);
		model.addAttribute("KtPoluvagonPlan", KtPoluvagonPlan);
		model.addAttribute("KtSisternaPlan", KtSisternaPlan);
		model.addAttribute("KtBoshqaPlan", KtBoshqaPlan);

		//VCHD-3 KRP plan
		int HavKrpHammaPlan =  planDto.getHavKrpKritiPlanBiznes() + planDto.getHavKrpPlatformaPlanBiznes() + planDto.getHavKrpPoluvagonPlanBiznes() + planDto.getHavKrpSisternaPlanBiznes() + planDto.getHavKrpBoshqaPlanBiznes();
		int HavKrpKritiPlan = planDto.getHavKrpKritiPlanBiznes();
		int HavKrpPlatformaPlan = planDto.getHavKrpPlatformaPlanBiznes();
		int HavKrpPoluvagonPlan = planDto.getHavKrpPoluvagonPlanBiznes();
		int HavKrpSisternaPlan = planDto.getHavKrpSisternaPlanBiznes();
		int HavKrpBoshqaPlan = planDto.getHavKrpBoshqaPlanBiznes();

		model.addAttribute("HavKrpHammaPlan", HavKrpHammaPlan);
		model.addAttribute("HavKrpKritiPlan", HavKrpKritiPlan);
		model.addAttribute("HavKrpPlatformaPlan", HavKrpPlatformaPlan);
		model.addAttribute("HavKrpPoluvagonPlan", HavKrpPoluvagonPlan);
		model.addAttribute("HavKrpSisternaPlan", HavKrpSisternaPlan);
		model.addAttribute("HavKrpBoshqaPlan", HavKrpBoshqaPlan);

		//VCHD-5 Krp plan
		int AndjKrpHammaPlan =  planDto.getAndjKrpKritiPlanBiznes() + planDto.getAndjKrpPlatformaPlanBiznes() + planDto.getAndjKrpPoluvagonPlanBiznes() + planDto.getAndjKrpSisternaPlanBiznes() + planDto.getAndjKrpBoshqaPlanBiznes();
		int AndjKrpKritiPlan = planDto.getAndjKrpKritiPlanBiznes();
		int AndjKrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanBiznes();
		int AndjKrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanBiznes();
		int AndjKrpSisternaPlan = planDto.getAndjKrpSisternaPlanBiznes();
		int AndjKrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanBiznes();

		model.addAttribute("AndjKrpHammaPlan", AndjKrpHammaPlan);
		model.addAttribute("AndjKrpKritiPlan", AndjKrpKritiPlan);
		model.addAttribute("AndjKrpPlatformaPlan", AndjKrpPlatformaPlan);
		model.addAttribute("AndjKrpPoluvagonPlan", AndjKrpPoluvagonPlan);
		model.addAttribute("AndjKrpSisternaPlan", AndjKrpSisternaPlan);
		model.addAttribute("AndjKrpBoshqaPlan", AndjKrpBoshqaPlan);


		//samarqand KRP plan
		int SamKrpHammaPlan = planDto.getSamKrpKritiPlanBiznes() + planDto.getSamKrpPlatformaPlanBiznes() + planDto.getSamKrpPoluvagonPlanBiznes() + planDto.getSamKrpSisternaPlanBiznes() + planDto.getSamKrpBoshqaPlanBiznes();
		int SamKrpKritiPlan = planDto.getSamKrpKritiPlanBiznes();
		int SamKrpPlatformaPlan = planDto.getSamKrpPlatformaPlanBiznes();
		int SamKrpPoluvagonPlan = planDto.getSamKrpPoluvagonPlanBiznes();
		int SamKrpSisternaPlan = planDto.getSamKrpSisternaPlanBiznes();
		int SamKrpBoshqaPlan = planDto.getSamKrpBoshqaPlanBiznes();

		model.addAttribute("SamKrpHammaPlan", SamKrpHammaPlan);
		model.addAttribute("SamKrpKritiPlan", SamKrpKritiPlan);
		model.addAttribute("SamKrpPlatformaPlan", SamKrpPlatformaPlan);
		model.addAttribute("SamKrpPoluvagonPlan", SamKrpPoluvagonPlan);
		model.addAttribute("SamKrpSisternaPlan", SamKrpSisternaPlan);
		model.addAttribute("SamKrpBoshqaPlan", SamKrpBoshqaPlan);

		//Krp itogo plan
		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = planDto.getAndjKrpKritiPlanBiznes() + planDto.getHavKrpKritiPlanBiznes() + planDto.getSamKrpKritiPlanBiznes();
		int KrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanBiznes() + planDto.getHavKrpPlatformaPlanBiznes() + planDto.getSamKrpPlatformaPlanBiznes();
		int KrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanBiznes() + planDto.getHavKrpPoluvagonPlanBiznes() + planDto.getSamKrpPoluvagonPlanBiznes();
		int KrpSisternaPlan = planDto.getAndjKrpSisternaPlanBiznes() + planDto.getHavKrpSisternaPlanBiznes() + planDto.getSamKrpSisternaPlanBiznes();
		int KrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanBiznes() + planDto.getHavKrpBoshqaPlanBiznes() + planDto.getSamKrpBoshqaPlanBiznes();

		model.addAttribute("KrpHammaPlan", KrpHammaPlan);
		model.addAttribute("KrpKritiPlan", KrpKritiPlan);
		model.addAttribute("KrpPlatformaPlan", KrpPlatformaPlan);
		model.addAttribute("KrpPoluvagonPlan", KrpPoluvagonPlan);
		model.addAttribute("KrpSisternaPlan", KrpSisternaPlan);
		model.addAttribute("KrpBoshqaPlan", KrpBoshqaPlan);

		// factlar

		//VCHD-3 uchun depli tamir
		int hdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);
		int hdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy);
		int hdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)", oy);
		int hdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy);
		int hdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)", oy);
		int hdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		model.addAttribute("hdHamma",hdHamma);
		model.addAttribute("hdKriti", hdKriti);
		model.addAttribute("hdPlatforma", hdPlatforma);
		model.addAttribute("hdPoluvagon", hdPoluvagon);
		model.addAttribute("hdSisterna", hdSisterna);
		model.addAttribute("hdBoshqa", hdBoshqa);

		//VCHD-5 uchun depli tamir
		int adHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)", oy);
		int adKriti =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy);
		int adPlatforma =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)", oy);
		int adPoluvagon =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy);
		int adSisterna =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)", oy);
		int adBoshqa =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		model.addAttribute("adHamma",adHamma);
		model.addAttribute("adKriti", adKriti);
		model.addAttribute("adPlatforma", adPlatforma);
		model.addAttribute("adPoluvagon", adPoluvagon);
		model.addAttribute("adSisterna", adSisterna);
		model.addAttribute("adBoshqa", adBoshqa);

		//samarqand uchun depli tamir
		int sdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);
		int sdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy);
		int sdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)", oy);
		int sdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy);
		int sdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)", oy);
		int sdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy);

		model.addAttribute("sdHamma",sdHamma);
		model.addAttribute("sdKriti", sdKriti);
		model.addAttribute("sdPlatforma", sdPlatforma);
		model.addAttribute("sdPoluvagon", sdPoluvagon);
		model.addAttribute("sdSisterna", sdSisterna);
		model.addAttribute("sdBoshqa", sdBoshqa);

		// itogo Fact uchun depli tamir
		int uvtdHamma = sdHamma + hdHamma + adHamma;
		int uvtdKriti = sdKriti + hdKriti + adKriti;
		int uvtdPlatforma = sdPlatforma + adPlatforma + hdPlatforma;
		int uvtdPoluvagon = sdPoluvagon + hdPoluvagon + adPoluvagon;
		int uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
		int uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

		model.addAttribute("uvtdHamma",uvtdHamma);
		model.addAttribute("uvtdKriti",uvtdKriti);
		model.addAttribute("uvtdPlatforma",uvtdPlatforma);
		model.addAttribute("uvtdPoluvagon",uvtdPoluvagon);
		model.addAttribute("uvtdSisterna",uvtdSisterna);
		model.addAttribute("uvtdBoshqa",uvtdBoshqa);


		//Yolovchi vagon Fact
		int atYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yo'lovchi vagon(пассжир)","TO-3", oy);
		int adYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yo'lovchi vagon(пассжир)","Depoli ta’mir(ДР)", oy);

		model.addAttribute("atYolovchi", atYolovchi);
		model.addAttribute("adYolovchi", adYolovchi);

		//VCHD-3 uchun kapital tamir
		int hkHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);
		int hkKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy);
		int hkPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)", oy);
		int hkPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy);
		int hkSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)", oy);
		int hkBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		model.addAttribute("hkHamma",hkHamma);
		model.addAttribute("hkKriti", hkKriti);
		model.addAttribute("hkPlatforma", hkPlatforma);
		model.addAttribute("hkPoluvagon", hkPoluvagon);
		model.addAttribute("hkSisterna", hkSisterna);
		model.addAttribute("hkBoshqa", hkBoshqa);

		//VCHD-3 uchun kapital tamir
		int akHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)", oy);
		int akKriti =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy);
		int akPlatforma =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)", oy);
		int akPoluvagon =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy);
		int akSisterna =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)", oy);
		int akBoshqa =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

		model.addAttribute( "akHamma", akHamma);
		model.addAttribute( "akKriti", akKriti);
		model.addAttribute( "akPlatforma", akPlatforma);
		model.addAttribute( "akPoluvagon", akPoluvagon);
		model.addAttribute( "akSisterna", akSisterna);
		model.addAttribute( "akBoshqa", akBoshqa);

		//samarqand uchun Kapital tamir
		int skHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);
		int skKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy);
		int skPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)", oy);
		int skPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy);
		int skSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)", oy);
		int skBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy);

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


		//VCHD-3 uchun kapital tamir
		int hkrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)", oy);
		int hkrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","KRP(КРП)", oy);
		int hkrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)", oy);
		int hkrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)", oy);
		int hkrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)", oy);
		int hkrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)", oy);

		model.addAttribute("hkrHamma",hkrHamma);
		model.addAttribute("hkrKriti", hkrKriti);
		model.addAttribute("hkrPlatforma", hkrPlatforma);
		model.addAttribute("hkrPoluvagon", hkrPoluvagon);
		model.addAttribute("hkrSisterna", hkrSisterna);
		model.addAttribute("hkrBoshqa", hkrBoshqa);

		//VCHD-3 uchun kapital tamir
		int akrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)", oy);
		int akrKriti =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)", oy);
		int akrPlatforma =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)", oy);
		int akrPoluvagon =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)", oy);
		int akrSisterna =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)", oy);
		int akrBoshqa =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)", oy);

		model.addAttribute( "akrHamma", akrHamma);
		model.addAttribute( "akrKriti", akrKriti);
		model.addAttribute( "akrPlatforma", akrPlatforma);
		model.addAttribute( "akrPoluvagon", akrPoluvagon);
		model.addAttribute( "akrSisterna", akrSisterna);
		model.addAttribute( "akrBoshqa", akrBoshqa);

		//samarqand uchun Kapital tamir
		int skrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)", oy) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)", oy);
		int skrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)", oy);
		int skrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)", oy);
		int skrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)", oy);
		int skrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)", oy);
		int skrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)", oy);

		model.addAttribute("skrHamma",skrHamma);
		model.addAttribute("skrKriti", skrKriti);
		model.addAttribute("skrPlatforma", skrPlatforma);
		model.addAttribute("skrPoluvagon", skrPoluvagon);
		model.addAttribute("skrSisterna", skrSisterna);
		model.addAttribute("skrBoshqa", skrBoshqa);

		// itogo Fact uchun KRP tamir
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
		vagonsToDownloadTable.add(uvtdHamma);
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

		vagonsToDownloadTable.add(AndjToYolovchiPlan);
		vagonsToDownloadTable.add(atYolovchi);
		vagonsToDownloadTable.add(AndjDtYolovchiPlan);
		vagonsToDownloadTable.add(adYolovchi);

		vagonsToDownloadAllTable = vagonsToDownloadTable;

		return "AllPlanTable";
    }

    // wagon nomer orqali qidirish shu oygacha hammasidan
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/searchTayyorAllMonths")
	public String search(Model model,  @RequestParam(value = "participant", required = false) Integer participant) {
		if(participant==null  ) {
			model.addAttribute("vagons", vagonTayyorService.findAll());
			vagonsToDownload = vagonTayyorService.findAll();
		}else {
			model.addAttribute("vagons", vagonTayyorService.findByNomer(participant));
			List<VagonTayyor> emptyList = new ArrayList<>();
			vagonsToDownload = emptyList;
			vagonsToDownload.add( vagonTayyorService.findByNomer(participant));
		}
		PlanBiznes planDto = vagonTayyorService.getPlanBiznes();
		//planlar kiritish


		//havos depo tamir hamma plan
		int HavDtKritiPlan = planDto.getHavDtKritiPlanBiznesMonths();
		int HavDtPlatformaPlan = planDto.getHavDtPlatformaPlanBiznesMonths();
		int HavDtPoluvagonPlan = planDto.getHavDtPoluvagonPlanBiznesMonths();
		int HavDtSisternaPlan = planDto.getHavDtSisternaPlanBiznesMonths();
		int HavDtBoshqaPlan = planDto.getHavDtBoshqaPlanBiznesMonths();
		int HavDtHammaPlan = HavDtKritiPlan + HavDtPlatformaPlan + HavDtPoluvagonPlan + HavDtSisternaPlan + HavDtBoshqaPlan;

		model.addAttribute("HavDtHammaPlan", HavDtHammaPlan);
		model.addAttribute("HavDtKritiPlan", HavDtKritiPlan);
		model.addAttribute("HavDtPlatformaPlan", HavDtPlatformaPlan);
		model.addAttribute("HavDtPoluvagonPlan", HavDtPoluvagonPlan);
		model.addAttribute("HavDtSisternaPlan", HavDtSisternaPlan);
		model.addAttribute("HavDtBoshqaPlan", HavDtBoshqaPlan);


		//VCHD-5 depo tamir plan
		int AndjDtKritiPlan = planDto.getAndjDtKritiPlanBiznesMonths();
		int AndjDtPlatformaPlan =planDto.getAndjDtPlatformaPlanBiznesMonths();
		int AndjDtPoluvagonPlan =planDto.getAndjDtPoluvagonPlanBiznesMonths();
		int AndjDtSisternaPlan =planDto.getAndjDtSisternaPlanBiznesMonths();
		int AndjDtBoshqaPlan=planDto.getAndjDtBoshqaPlanBiznesMonths();
		int AndjDtHammaPlan = AndjDtKritiPlan + AndjDtPlatformaPlan + AndjDtPoluvagonPlan + AndjDtSisternaPlan + AndjDtBoshqaPlan;

		model.addAttribute("AndjDtHammaPlan", AndjDtHammaPlan);
		model.addAttribute("AndjDtKritiPlan", AndjDtKritiPlan);
		model.addAttribute("AndjDtPlatformaPlan",AndjDtPlatformaPlan);
		model.addAttribute("AndjDtPoluvagonPlan", AndjDtPoluvagonPlan);
		model.addAttribute("AndjDtSisternaPlan", AndjDtSisternaPlan);
		model.addAttribute("AndjDtBoshqaPlan", AndjDtBoshqaPlan);

		//samarqand depo tamir plan
		int SamDtKritiPlan = planDto.getSamDtKritiPlanBiznesMonths();
		int SamDtPlatformaPlan = planDto.getSamDtPlatformaPlanBiznesMonths();
		int SamDtPoluvagonPlan =  planDto.getSamDtPoluvagonPlanBiznesMonths();
		int SamDtSisternaPlan = planDto.getSamDtSisternaPlanBiznesMonths();
		int SamDtBoshqaPlan = planDto.getSamDtBoshqaPlanBiznesMonths();
		int SamDtHammaPlan=SamDtKritiPlan + SamDtPlatformaPlan + SamDtPoluvagonPlan + SamDtSisternaPlan + SamDtBoshqaPlan;

		model.addAttribute("SamDtHammaPlan",SamDtHammaPlan);
		model.addAttribute("SamDtKritiPlan", SamDtKritiPlan);
		model.addAttribute("SamDtPlatformaPlan", SamDtPlatformaPlan);
		model.addAttribute("SamDtPoluvagonPlan", SamDtPoluvagonPlan);
		model.addAttribute("SamDtSisternaPlan", SamDtSisternaPlan);
		model.addAttribute("SamDtBoshqaPlan", SamDtBoshqaPlan);


		// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = SamDtKritiPlan + HavDtKritiPlan + AndjDtKritiPlan;
		int DtPlatformaPlan = SamDtPlatformaPlan + HavDtPlatformaPlan + AndjDtPlatformaPlan;
		int DtPoluvagonPlan = SamDtPoluvagonPlan + HavDtPoluvagonPlan + AndjDtPoluvagonPlan;
		int DtSisternaPlan = SamDtSisternaPlan + HavDtSisternaPlan + AndjDtSisternaPlan;
		int DtBoshqaPlan = SamDtBoshqaPlan + HavDtBoshqaPlan + AndjDtBoshqaPlan;

		model.addAttribute("DtHammaPlan", DtHammaPlan);
		model.addAttribute("DtKritiPlan", DtKritiPlan);
		model.addAttribute("DtPlatformaPlan", DtPlatformaPlan);
		model.addAttribute("DtPoluvagonPlan",DtPoluvagonPlan);
		model.addAttribute("DtSisternaPlan", DtSisternaPlan);
		model.addAttribute("DtBoshqaPlan", DtBoshqaPlan);


		//Yolovchi vagon Plan
		int AndjToYolovchiPlan = planDto.getAndjTYolovchiPlanBiznesMonths();
		int AndjDtYolovchiPlan = planDto.getAndjDtYolovchiPlanBiznesMonths();

		model.addAttribute("AndjToYolovchiPlan", AndjToYolovchiPlan);
		model.addAttribute("AndjDtYolovchiPlan", AndjDtYolovchiPlan);

		//hovos kapital plan
		int HavKtKritiPlan = planDto.getHavKtKritiPlanBiznesMonths();
		int HavKtPlatformaPlan = planDto.getHavKtPlatformaPlanBiznesMonths();
		int HavKtPoluvagonPlan = planDto.getHavKtPoluvagonPlanBiznesMonths();
		int HavKtSisternaPlan = planDto.getHavKtSisternaPlanBiznesMonths();
		int HavKtBoshqaPlan = planDto.getHavKtBoshqaPlanBiznesMonths();
		int HavKtHammaPlan = HavKtKritiPlan + HavKtPlatformaPlan + HavKtPoluvagonPlan + HavKtSisternaPlan + HavKtBoshqaPlan;

		model.addAttribute("HavKtHammaPlan", HavKtHammaPlan);
		model.addAttribute("HavKtKritiPlan", HavKtKritiPlan);
		model.addAttribute("HavKtPlatformaPlan", HavKtPlatformaPlan);
		model.addAttribute("HavKtPoluvagonPlan", HavKtPoluvagonPlan);
		model.addAttribute("HavKtSisternaPlan", HavKtSisternaPlan);
		model.addAttribute("HavKtBoshqaPlan", HavKtBoshqaPlan);

		//ANDIJON kapital plan
		int AndjKtKritiPlan = planDto.getAndjKtKritiPlanBiznesMonths();
		int AndjKtPlatformaPlan=planDto.getAndjKtPlatformaPlanBiznesMonths();
		int AndjKtPoluvagonPlan=planDto.getAndjKtPoluvagonPlanBiznesMonths();
		int AndjKtSisternaPlan=planDto.getAndjKtSisternaPlanBiznesMonths();
		int AndjKtBoshqaPlan=planDto.getAndjKtBoshqaPlanBiznesMonths();
		int AndjKtHammaPlan = AndjKtKritiPlan + AndjKtPlatformaPlan + AndjKtPoluvagonPlan + AndjKtSisternaPlan + AndjKtBoshqaPlan;

		model.addAttribute("AndjKtHammaPlan", AndjKtHammaPlan);
		model.addAttribute("AndjKtKritiPlan", AndjKtKritiPlan);
		model.addAttribute("AndjKtPlatformaPlan", AndjKtPlatformaPlan);
		model.addAttribute("AndjKtPoluvagonPlan", AndjKtPoluvagonPlan);
		model.addAttribute("AndjKtSisternaPlan", AndjKtSisternaPlan);
		model.addAttribute("AndjKtBoshqaPlan", AndjKtBoshqaPlan);

		//Samrqand kapital plan
		int SamKtKritiPlan = planDto.getSamKtKritiPlanBiznesMonths();
		int SamKtPlatformaPlan = planDto.getSamKtPlatformaPlanBiznesMonths();
		int SamKtPoluvagonPlan = planDto.getSamKtPoluvagonPlanBiznesMonths();
		int SamKtSisternaPlan = planDto.getSamKtSisternaPlanBiznesMonths();
		int SamKtBoshqaPlan = planDto.getSamKtBoshqaPlanBiznesMonths();
		int SamKtHammaPlan = SamKtKritiPlan + SamKtPlatformaPlan + SamKtPoluvagonPlan + SamKtSisternaPlan +SamKtBoshqaPlan;

		model.addAttribute("SamKtHammaPlan",SamKtHammaPlan);
		model.addAttribute("SamKtKritiPlan", SamKtKritiPlan);
		model.addAttribute("SamKtPlatformaPlan", SamKtPlatformaPlan);
		model.addAttribute("SamKtPoluvagonPlan", SamKtPoluvagonPlan);
		model.addAttribute("SamKtSisternaPlan", SamKtSisternaPlan);
		model.addAttribute("SamKtBoshqaPlan", SamKtBoshqaPlan);


		//Itogo kapital plan
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = SamKtKritiPlan + HavKtKritiPlan + AndjKtKritiPlan;
		int KtPlatformaPlan = SamKtPlatformaPlan + HavKtPlatformaPlan + AndjKtPlatformaPlan;
		int KtPoluvagonPlan = SamKtPoluvagonPlan + HavKtPoluvagonPlan + AndjKtPoluvagonPlan;
		int KtSisternaPlan = SamKtSisternaPlan + HavKtSisternaPlan + AndjKtSisternaPlan;
		int KtBoshqaPlan = SamKtBoshqaPlan + HavKtBoshqaPlan + AndjKtBoshqaPlan;

		model.addAttribute("KtHammaPlan", KtHammaPlan);
		model.addAttribute("KtKritiPlan", KtKritiPlan);
		model.addAttribute("KtPlatformaPlan", KtPlatformaPlan);
		model.addAttribute("KtPoluvagonPlan",KtPoluvagonPlan);
		model.addAttribute("KtSisternaPlan", KtSisternaPlan);
		model.addAttribute("KtBoshqaPlan", KtBoshqaPlan);


		//Hovos krp plan
		int HavKrpKritiPlan = planDto.getHavKrpKritiPlanBiznesMonths();
		int HavKrpPlatformaPlan = planDto.getHavKrpPlatformaPlanBiznesMonths();
		int HavKrpPoluvagonPlan = planDto.getHavKrpPoluvagonPlanBiznesMonths();
		int HavKrpSisternaPlan = planDto.getHavKrpSisternaPlanBiznesMonths();
		int HavKrpBoshqaPlan = planDto.getHavKrpBoshqaPlanBiznesMonths();
		int HavKrpHammaPlan = HavKrpKritiPlan + HavKrpPlatformaPlan + HavKrpPoluvagonPlan + HavKrpSisternaPlan + HavKrpBoshqaPlan;

		model.addAttribute("HavKrpHammaPlan",HavKrpHammaPlan);
		model.addAttribute("HavKrpKritiPlan", HavKrpKritiPlan);
		model.addAttribute("HavKrpPlatformaPlan", HavKrpPlatformaPlan);
		model.addAttribute("HavKrpPoluvagonPlan", HavKrpPoluvagonPlan);
		model.addAttribute("HavKrpSisternaPlan", HavKrpSisternaPlan);
		model.addAttribute("HavKrpBoshqaPlan", HavKrpBoshqaPlan);

		//andijon krp plan
		int AndjKrpKritiPlan = planDto.getAndjKrpKritiPlanBiznesMonths();
		int AndjKrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanBiznesMonths();
		int AndjKrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanBiznesMonths();
		int AndjKrpSisternaPlan = planDto.getAndjKrpSisternaPlanBiznesMonths();
		int AndjKrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanBiznesMonths();
		int AndjKrpHammaPlan = AndjKrpKritiPlan + AndjKrpPlatformaPlan + AndjKrpPoluvagonPlan + AndjKrpSisternaPlan + AndjKrpBoshqaPlan;

		model.addAttribute("AndjKrpHammaPlan",AndjKrpHammaPlan);
		model.addAttribute("AndjKrpKritiPlan", AndjKrpKritiPlan);
		model.addAttribute("AndjKrpPlatformaPlan", AndjKrpPlatformaPlan);
		model.addAttribute("AndjKrpPoluvagonPlan", AndjKrpPoluvagonPlan);
		model.addAttribute("AndjKrpSisternaPlan", AndjKrpSisternaPlan);
		model.addAttribute("AndjKrpBoshqaPlan", AndjKrpBoshqaPlan);

		//Samarqankr Krp plan
		int SamKrpKritiPlan = planDto.getSamKrpKritiPlanBiznesMonths();
		int SamKrpPlatformaPlan = planDto.getSamKrpPlatformaPlanBiznesMonths();
		int SamKrpPoluvagonPlan = planDto.getSamKrpPoluvagonPlanBiznesMonths();
		int SamKrpSisternaPlan = planDto.getSamKrpSisternaPlanBiznesMonths();
		int SamKrpBoshqaPlan = planDto.getSamKrpBoshqaPlanBiznesMonths();
		int SamKrpHammaPlan = SamKrpKritiPlan + SamKrpPlatformaPlan + SamKrpPoluvagonPlan + SamKrpSisternaPlan + SamKrpBoshqaPlan;

		model.addAttribute("SamKrpHammaPlan", SamKrpHammaPlan);
		model.addAttribute("SamKrpKritiPlan", SamKrpKritiPlan);
		model.addAttribute("SamKrpPlatformaPlan", SamKrpPlatformaPlan);
		model.addAttribute("SamKrpPoluvagonPlan", SamKrpPoluvagonPlan);
		model.addAttribute("SamKrpSisternaPlan", SamKrpSisternaPlan);
		model.addAttribute("SamKrpBoshqaPlan", SamKrpBoshqaPlan);


		//itogo krp

		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = SamKrpKritiPlan + HavKrpKritiPlan + AndjKrpKritiPlan;
		int KrpPlatformaPlan = SamKrpPlatformaPlan + HavKrpPlatformaPlan + AndjKrpPlatformaPlan;
		int KrpPoluvagonPlan = SamKrpPoluvagonPlan + HavKrpPoluvagonPlan + AndjKrpPoluvagonPlan;
		int KrpSisternaPlan = SamKrpSisternaPlan + HavKrpSisternaPlan + AndjKrpSisternaPlan;
		int KrpBoshqaPlan = SamKrpBoshqaPlan + HavKrpBoshqaPlan + AndjKrpBoshqaPlan;

		model.addAttribute("KrpHammaPlan", KrpHammaPlan);
		model.addAttribute("KrpKritiPlan", KrpKritiPlan);
		model.addAttribute("KrpPlatformaPlan", KrpPlatformaPlan);
		model.addAttribute("KrpPoluvagonPlan",KrpPoluvagonPlan);
		model.addAttribute("KrpSisternaPlan", KrpSisternaPlan);
		model.addAttribute("KrpBoshqaPlan", KrpBoshqaPlan);

		//**//
		// VCHD-3 depo tamir hamma false vagonlar soni
		int hdKriti=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)");
		int hdPlatforma=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)");
		int hdPoluvagon=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)");
		int hdSisterna=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)");
		int hdBoshqa=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)");
		int hdHamma = hdKriti + hdPlatforma + hdPoluvagon + hdSisterna + hdBoshqa;

		model.addAttribute("hdHamma",hdHamma + 651);
		model.addAttribute("hdKriti",hdKriti + 35);
		model.addAttribute("hdPlatforma",hdPlatforma + 45);
		model.addAttribute("hdPoluvagon",hdPoluvagon + 109);
		model.addAttribute("hdSisterna",hdSisterna + 35);
		model.addAttribute("hdBoshqa",hdBoshqa + 427);

		// VCHD-5 depo tamir hamma false vagonlar soni
		int adKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)");
		int adPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)");
		int adPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)");
		int adSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)");
		int adBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)");
		int adHamma = adKriti + adPlatforma+ adPoluvagon+ adSisterna + adBoshqa;

		model.addAttribute("adHamma",adHamma + 443);
		model.addAttribute("adKriti",adKriti + 224);
		model.addAttribute("adPlatforma",adPlatforma + 3);
		model.addAttribute("adPoluvagon",adPoluvagon + 103);
		model.addAttribute("adSisterna",adSisterna);
		model.addAttribute("adBoshqa",adBoshqa + 113);

		// samarqand depo tamir hamma false vagonlar soni
		int sdKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)");
		int sdPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)");
		int sdPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)");
		int sdSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)");
		int sdBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)");
		int sdHamma = sdKriti + sdPlatforma+ sdPoluvagon+ sdSisterna + sdBoshqa;

		model.addAttribute("sdHamma",sdHamma + 393);
		model.addAttribute("sdKriti",sdKriti + 135);
		model.addAttribute("sdPlatforma",sdPlatforma + 8);
		model.addAttribute("sdPoluvagon",sdPoluvagon + 67);
		model.addAttribute("sdSisterna",sdSisterna + 23);
		model.addAttribute("sdBoshqa",sdBoshqa + 160);

		// depoli tamir itogo uchun
		int uvtdHamma =  adHamma + hdHamma+sdHamma;
		int uvtdKriti = sdKriti + hdKriti + adKriti;
		int uvtdPlatforma = adPlatforma + sdPlatforma + hdPlatforma;
		int uvtdPoluvagon  = adPoluvagon + sdPoluvagon + hdPoluvagon;
		int uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
		int uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

		model.addAttribute("uvtdHamma",uvtdHamma + 1487);
		model.addAttribute("uvtdKriti",uvtdKriti + 394);
		model.addAttribute("uvtdPlatforma",uvtdPlatforma + 56);
		model.addAttribute("uvtdPoluvagon",uvtdPoluvagon + 279);
		model.addAttribute("uvtdSisterna",uvtdSisterna + 58);
		model.addAttribute("uvtdBoshqa",uvtdBoshqa + 700);

		//Yolovchi Andijon fact
		int atYolovchi=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yo'lovchi vagon(пассжир)","TO-3");
		int adYolovchi=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yo'lovchi vagon(пассжир)","Depoli ta’mir(ДР)");

		model.addAttribute("atYolovchi",atYolovchi + 37);
		model.addAttribute("adYolovchi",adYolovchi + 24);


		// VCHD-3 kapital tamir hamma false vagonlar soni
		int hkKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Kapital ta’mir(КР)");
		int hkPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)");
		int hkPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)");
		int hkSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)");
		int hkBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)");
		int hkHamma = hkKriti + hkPlatforma+ hkPoluvagon+ hkSisterna + hkBoshqa;

		model.addAttribute("hkHamma",hkHamma + 227);
		model.addAttribute("hkKriti",hkKriti + 41);
		model.addAttribute("hkPlatforma",hkPlatforma + 32);
		model.addAttribute("hkPoluvagon",hkPoluvagon + 4);
		model.addAttribute("hkSisterna",hkSisterna + 51);
		model.addAttribute("hkBoshqa",hkBoshqa + 99);

		// VCHD-5 kapital tamir hamma false vagonlar soni
		int akKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)");
		int akPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)");
		int akPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)");
		int akSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)");
		int akBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)");
		int akHamma = akKriti + akPlatforma+ akPoluvagon+ akSisterna + akBoshqa;

		model.addAttribute("akHamma",akHamma + 28);
		model.addAttribute("akKriti",akKriti + 26);
		model.addAttribute("akPlatforma",akPlatforma);
		model.addAttribute("akPoluvagon",akPoluvagon + 2);
		model.addAttribute("akSisterna",akSisterna);
		model.addAttribute("akBoshqa",akBoshqa);

		// samarqand KApital tamir hamma false vagonlar soni
		int skKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)");
		int skPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)");
		int skPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)");
		int skSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)");
		int skBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)");
		int skHamma = skKriti + skPlatforma+ skPoluvagon+ skSisterna + skBoshqa;

		model.addAttribute("skHamma",skHamma + 284);
		model.addAttribute("skKriti",skKriti + 160);
		model.addAttribute("skPlatforma",skPlatforma + 44);
		model.addAttribute("skPoluvagon",skPoluvagon + 52);
		model.addAttribute("skSisterna",skSisterna + 9);
		model.addAttribute("skBoshqa",skBoshqa + 19);

		// Kapital tamir itogo uchun
		int uvtkHamma =  akHamma + hkHamma+skHamma;
		int uvtkKriti = skKriti + hkKriti + akKriti;
		int uvtkPlatforma = akPlatforma + skPlatforma + hkPlatforma;
		int uvtkPoluvagon  = akPoluvagon + skPoluvagon + hkPoluvagon;
		int uvtkSisterna = akSisterna + hkSisterna + skSisterna;
		int uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

		model.addAttribute("uvtkHamma",uvtkHamma + 539);
		model.addAttribute("uvtkKriti",uvtkKriti + 227);
		model.addAttribute("uvtkPlatforma",uvtkPlatforma + 76);
		model.addAttribute("uvtkPoluvagon",uvtkPoluvagon + 58);
		model.addAttribute("uvtkSisterna",uvtkSisterna + 60);
		model.addAttribute("uvtkBoshqa",uvtkBoshqa + 118);

		//**

		// VCHD-3 KRP tamir hamma false vagonlar soni
		int hkrKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","KRP(КРП)");
		int hkrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)");
		int hkrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)");
		int hkrSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)");
		int hkrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)");
		int hkrHamma = hkrKriti + hkrPlatforma+ hkrPoluvagon+ hkrSisterna + hkrBoshqa;

		model.addAttribute("hkrHamma",hkrHamma+83);
		model.addAttribute("hkrKriti",hkrKriti+83);
		model.addAttribute("hkrPlatforma",hkrPlatforma);
		model.addAttribute("hkrPoluvagon",hkrPoluvagon);
		model.addAttribute("hkrSisterna",hkrSisterna);
		model.addAttribute("hkrBoshqa",hkrBoshqa);

		// VCHD-5 KRP tamir hamma false vagonlar soni
		int akrKriti=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)");
		int akrPlatforma=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)");
		int akrPoluvagon=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)");
		int akrSisterna=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)");
		int akrBoshqa=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)");
		int akrHamma = akrKriti + akrPlatforma+ akrPoluvagon+ akrSisterna + akrBoshqa;

		model.addAttribute("akrHamma",akrHamma+61);
		model.addAttribute("akrKriti",akrKriti);
		model.addAttribute("akrPlatforma",akrPlatforma);
		model.addAttribute("akrPoluvagon",akrPoluvagon+61);
		model.addAttribute("akrSisterna",akrSisterna);
		model.addAttribute("akBoshqa",akrBoshqa);

		// samarqand KRP tamir hamma false vagonlar soni
		int skrKriti=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)");
		int skrPlatforma=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)");
		int skrPoluvagon=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)");
		int skrSisterna=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)");
		int skrBoshqa=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)");
		int skrHamma = skrKriti + skrPlatforma+ skrPoluvagon+ skrSisterna + skrBoshqa;

		model.addAttribute("skrHamma",skrHamma+89);
		model.addAttribute("skrKriti",skrKriti);
		model.addAttribute("skrPlatforma",skrPlatforma);
		model.addAttribute("skrPoluvagon",skrPoluvagon + 88);
		model.addAttribute("skrSisterna",skrSisterna + 1);
		model.addAttribute("skrBoshqa",skrBoshqa);
// Krp itogo uchun
		int uvtkrHamma =  akrHamma + hkrHamma+skrHamma;
		int uvtkrKriti = skrKriti + hkrKriti + akrKriti;
		int uvtkrPlatforma = akrPlatforma + skrPlatforma + hkrPlatforma;
		int uvtkrPoluvagon  = akrPoluvagon + skrPoluvagon + hkrPoluvagon;
		int uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
		int uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

		model.addAttribute("uvtkrHamma",uvtkrHamma + 233);
		model.addAttribute("uvtkrKriti",uvtkrKriti);
		model.addAttribute("uvtkrPlatforma",uvtkrPlatforma);
		model.addAttribute("uvtkrPoluvagon",uvtkrPoluvagon + 232);
		model.addAttribute("uvtkrSisterna",uvtkrSisterna + 1);
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
		vagonsToDownloadTable.add(uvtdHamma);
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
		vagonsToDownloadTable.add(uvtkHamma);
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
		vagonsToDownloadTable.add(uvtkrHamma);
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

		vagonsToDownloadTable.add(AndjToYolovchiPlan);
		vagonsToDownloadTable.add(atYolovchi);
		vagonsToDownloadTable.add(AndjDtYolovchiPlan);
		vagonsToDownloadTable.add(adYolovchi);


		vagonsToDownloadAllTable = vagonsToDownloadTable;


		return "planTableForMonths";
    }

    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
   	@GetMapping("/vagons/filterOneMonth")
   	public String filterByDepoNomi(Model model,  @RequestParam(value = "depoNomi", required = false) String depoNomi,
   												@RequestParam(value = "vagonTuri", required = false) String vagonTuri,
   												@RequestParam(value = "country", required = false) String country) {
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

   		if(!depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi") ) {
   			vagonsToDownload = vagonTayyorService.findAllByDepoNomiVagonTuriAndCountry(depoNomi, vagonTuri, country, oy);
   			model.addAttribute("vagons", vagonTayyorService.findAllByDepoNomiVagonTuriAndCountry(depoNomi, vagonTuri, country, oy));
   		}else if(depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi")){
   			vagonsToDownload = vagonTayyorService.findAllByVagonTuriAndCountry(vagonTuri , country, oy);
   			model.addAttribute("vagons", vagonTayyorService.findAllByVagonTuriAndCountry(vagonTuri , country, oy));
   		}else if(depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi")&& !country.equalsIgnoreCase("Hammasi")){
   			vagonsToDownload = vagonTayyorService.findAllBycountry(country, oy );
   			model.addAttribute("vagons", vagonTayyorService.findAllBycountry(country, oy ));
   		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi")){
   			vagonsToDownload = vagonTayyorService.findAllByDepoNomiAndVagonTuri(depoNomi, vagonTuri, oy);
   			model.addAttribute("vagons", vagonTayyorService.findAllByDepoNomiAndVagonTuri(depoNomi, vagonTuri, oy));
   		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi")){
   			vagonsToDownload = vagonTayyorService.findAllByDepoNomiAndCountry(depoNomi, country, oy);
   			model.addAttribute("vagons", vagonTayyorService.findAllByDepoNomiAndCountry(depoNomi, country, oy));
   		}else if(depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi")){
   			vagonsToDownload = vagonTayyorService.findAllByVagonTuri(vagonTuri, oy);
   			model.addAttribute("vagons", vagonTayyorService.findAllByVagonTuri(vagonTuri, oy));
   		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi")){
   			model.addAttribute("vagons", vagonTayyorService.findAllByDepoNomi(depoNomi, oy));
   			vagonsToDownload = vagonTayyorService.findAllByDepoNomi(depoNomi, oy );
   		}else {
   			model.addAttribute("vagons", vagonTayyorService.findAll(oy));
   			vagonsToDownload = vagonTayyorService.findAll(oy);
   		}

		//vaqtni olib turadi
		model.addAttribute("samDate",vagonTayyorService.getSamDate());
		model.addAttribute("havDate", vagonTayyorService.getHavDate());
		model.addAttribute("andjDate",vagonTayyorService.getAndjDate());

		PlanBiznes planDto = vagonTayyorService.getPlanBiznes();
		//planlar kiritish

		//havos hamma plan
		int HavDtHammaPlan = planDto.getHavDtKritiPlanBiznes() + planDto.getHavDtPlatformaPlanBiznes() + planDto.getHavDtPoluvagonPlanBiznes() + planDto.getHavDtSisternaPlanBiznes() + planDto.getHavDtBoshqaPlanBiznes();
		int HavDtKritiPlan = planDto.getHavDtKritiPlanBiznes();
		int HavDtPlatformaPlan = planDto.getHavDtPlatformaPlanBiznes();
		int HavDtPoluvagonPlan = planDto.getHavDtPoluvagonPlanBiznes();
		int HavDtSisternaPlan = planDto.getHavDtSisternaPlanBiznes();
		int HavDtBoshqaPlan = planDto.getHavDtBoshqaPlanBiznes();

		model.addAttribute("HavDtHammaPlan", HavDtHammaPlan);
		model.addAttribute("HavDtKritiPlan", HavDtKritiPlan);
		model.addAttribute("HavDtPlatformaPlan", HavDtPlatformaPlan);
		model.addAttribute("HavDtPoluvagonPlan", HavDtPoluvagonPlan);
		model.addAttribute("HavDtSisternaPlan", HavDtSisternaPlan);
		model.addAttribute("HavDtBoshqaPlan", HavDtBoshqaPlan);

		//andijon hamma plan depo tamir
		int AndjDtHammaPlan = planDto.getAndjDtKritiPlanBiznes() + planDto.getAndjDtPlatformaPlanBiznes() + planDto.getAndjDtPoluvagonPlanBiznes() + planDto.getAndjDtSisternaPlanBiznes() + planDto.getAndjDtBoshqaPlanBiznes();
		int AndjDtKritiPlan =  planDto.getAndjDtKritiPlanBiznes();
		int AndjDtPlatformaPlan =  planDto.getAndjDtPlatformaPlanBiznes();
		int AndjDtPoluvagonPlan =  planDto.getAndjDtPoluvagonPlanBiznes();
		int AndjDtSisternaPlan =  planDto.getAndjDtSisternaPlanBiznes();
		int AndjDtBoshqaPlan =  planDto.getAndjDtBoshqaPlanBiznes();

		model.addAttribute("AndjDtHammaPlan", AndjDtHammaPlan);
		model.addAttribute("AndjDtKritiPlan", AndjDtKritiPlan);
		model.addAttribute("AndjDtPlatformaPlan", AndjDtPlatformaPlan);
		model.addAttribute("AndjDtPoluvagonPlan", AndjDtPoluvagonPlan);
		model.addAttribute("AndjDtSisternaPlan", AndjDtSisternaPlan);
		model.addAttribute("AndjDtBoshqaPlan", AndjDtBoshqaPlan);

		//samarqand depo tamir
		int SamDtHammaPlan=planDto.getSamDtKritiPlanBiznes() + planDto.getSamDtPlatformaPlanBiznes() + planDto.getSamDtPoluvagonPlanBiznes() + planDto.getSamDtSisternaPlanBiznes() + planDto.getSamDtBoshqaPlanBiznes();
		int SamDtKritiPlan =  planDto.getSamDtKritiPlanBiznes();
		int SamDtPlatformaPlan =  planDto.getSamDtPlatformaPlanBiznes();
		int SamDtPoluvagonPlan =  planDto.getSamDtPoluvagonPlanBiznes();
		int SamDtSisternaPlan =  planDto.getSamDtSisternaPlanBiznes();
		int SamDtBoshqaPlan =  planDto.getSamDtBoshqaPlanBiznes();

		model.addAttribute("SamDtHammaPlan",SamDtHammaPlan);
		model.addAttribute("SamDtKritiPlan", SamDtKritiPlan);
		model.addAttribute("SamDtPlatformaPlan", SamDtPlatformaPlan);
		model.addAttribute("SamDtPoluvagonPlan", SamDtPoluvagonPlan);
		model.addAttribute("SamDtSisternaPlan", SamDtSisternaPlan);
		model.addAttribute("SamDtBoshqaPlan", SamDtBoshqaPlan);

		// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = planDto.getAndjDtKritiPlanBiznes() + planDto.getHavDtKritiPlanBiznes() + planDto.getSamDtKritiPlanBiznes();
		int DtPlatformaPlan = planDto.getAndjDtPlatformaPlanBiznes() + planDto.getHavDtPlatformaPlanBiznes() + planDto.getSamDtPlatformaPlanBiznes();
		int DtPoluvagonPlan = planDto.getAndjDtPoluvagonPlanBiznes() + planDto.getHavDtPoluvagonPlanBiznes() + planDto.getSamDtPoluvagonPlanBiznes();
		int DtSisternaPlan = planDto.getAndjDtSisternaPlanBiznes() + planDto.getHavDtSisternaPlanBiznes() + planDto.getSamDtSisternaPlanBiznes();
		int DtBoshqaPlan = planDto.getAndjDtBoshqaPlanBiznes() + planDto.getHavDtBoshqaPlanBiznes() + planDto.getSamDtBoshqaPlanBiznes();

		model.addAttribute("DtHammaPlan", DtHammaPlan);
		model.addAttribute("DtKritiPlan", DtKritiPlan);
		model.addAttribute("DtPlatformaPlan", DtPlatformaPlan);
		model.addAttribute("DtPoluvagonPlan", DtPoluvagonPlan);
		model.addAttribute("DtSisternaPlan", DtSisternaPlan);
		model.addAttribute("DtBoshqaPlan", DtBoshqaPlan);

		//yolovchi vagonlar plan
		int AndjToYolovchiPlan = planDto.getAndjTYolovchiPlanBiznes();
		int AndjDtYolovchiPlan = planDto.getAndjDtYolovchiPlanBiznes();

		model.addAttribute("AndjToYolovchiPlan", AndjToYolovchiPlan);
		model.addAttribute("AndjDtYolovchiPlan", AndjDtYolovchiPlan);


		//havos kapital tamir uchun plan
		int HavKtHammaPlan = planDto.getHavKtKritiPlanBiznes() + planDto.getHavKtPlatformaPlanBiznes() + planDto.getHavKtPoluvagonPlanBiznes() + planDto.getHavKtSisternaPlanBiznes() + planDto.getHavKtBoshqaPlanBiznes();
		int HavKtKritiPlan = planDto.getHavKtKritiPlanBiznes();
		int HavKtPlatformaPlan = planDto.getHavKtPlatformaPlanBiznes();
		int HavKtPoluvagonPlan = planDto.getHavKtPoluvagonPlanBiznes();
		int HavKtSisternaPlan = planDto.getHavKtSisternaPlanBiznes();
		int HavKtBoshqaPlan = planDto.getHavKtBoshqaPlanBiznes();

		model.addAttribute("HavKtHammaPlan", HavKtHammaPlan);
		model.addAttribute("HavKtKritiPlan", HavKtKritiPlan);
		model.addAttribute("HavKtPlatformaPlan", HavKtPlatformaPlan);
		model.addAttribute("HavKtPoluvagonPlan", HavKtPoluvagonPlan);
		model.addAttribute("HavKtSisternaPlan", HavKtSisternaPlan);
		model.addAttribute("HavKtBoshqaPlan", HavKtBoshqaPlan);

		//VCHD-5 kapital tamir uchun plan
		int AndjKtHammaPlan = planDto.getAndjKtKritiPlanBiznes() + planDto.getAndjKtPlatformaPlanBiznes() + planDto.getAndjKtPoluvagonPlanBiznes() + planDto.getAndjKtSisternaPlanBiznes() + planDto.getAndjKtBoshqaPlanBiznes();
		int AndjKtKritiPlan = planDto.getAndjKtKritiPlanBiznes();
		int AndjKtPlatformaPlan = planDto.getAndjKtPlatformaPlanBiznes();
		int AndjKtPoluvagonPlan = planDto.getAndjKtPoluvagonPlanBiznes();
		int AndjKtSisternaPlan = planDto.getAndjKtSisternaPlanBiznes();
		int AndjKtBoshqaPlan = planDto.getAndjKtBoshqaPlanBiznes();

		model.addAttribute("AndjKtHammaPlan", AndjKtHammaPlan);
		model.addAttribute("AndjKtKritiPlan", AndjKtKritiPlan);
		model.addAttribute("AndjKtPlatformaPlan", AndjKtPlatformaPlan);
		model.addAttribute("AndjKtPoluvagonPlan", AndjKtPoluvagonPlan);
		model.addAttribute("AndjKtSisternaPlan", AndjKtSisternaPlan);
		model.addAttribute("AndjKtBoshqaPlan", AndjKtBoshqaPlan);

		//VCHD-6 kapital tamir uchun plan
		int SamKtHammaPlan = planDto.getSamKtKritiPlanBiznes() + planDto.getSamKtPlatformaPlanBiznes() + planDto.getSamKtPoluvagonPlanBiznes() + planDto.getSamKtSisternaPlanBiznes() + planDto.getSamKtBoshqaPlanBiznes();
		int SamKtKritiPlan = planDto.getSamKtKritiPlanBiznes();
		int SamKtPlatformaPlan = planDto.getSamKtPlatformaPlanBiznes();
		int SamKtPoluvagonPlan = planDto.getSamKtPoluvagonPlanBiznes();
		int SamKtSisternaPlan = planDto.getSamKtSisternaPlanBiznes();
		int SamKtBoshqaPlan = planDto.getSamKtBoshqaPlanBiznes();

		model.addAttribute("SamKtHammaPlan", SamKtHammaPlan);
		model.addAttribute("SamKtKritiPlan", SamKtKritiPlan);
		model.addAttribute("SamKtPlatformaPlan", SamKtPlatformaPlan);
		model.addAttribute("SamKtPoluvagonPlan", SamKtPoluvagonPlan);
		model.addAttribute("SamKtSisternaPlan", SamKtSisternaPlan);
		model.addAttribute("SamKtBoshqaPlan", SamKtBoshqaPlan);

		//kapital itogo
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = planDto.getAndjKtKritiPlanBiznes() + planDto.getHavKtKritiPlanBiznes() + planDto.getSamKtKritiPlanBiznes();
		int KtPlatformaPlan = planDto.getAndjKtPlatformaPlanBiznes() + planDto.getHavKtPlatformaPlanBiznes() + planDto.getSamKtPlatformaPlanBiznes();
		int KtPoluvagonPlan = planDto.getAndjKtPoluvagonPlanBiznes() + planDto.getHavKtPoluvagonPlanBiznes() + planDto.getSamKtPoluvagonPlanBiznes();
		int KtSisternaPlan = planDto.getAndjKtSisternaPlanBiznes() + planDto.getHavKtSisternaPlanBiznes() + planDto.getSamKtSisternaPlanBiznes();
		int KtBoshqaPlan = planDto.getAndjKtBoshqaPlanBiznes() + planDto.getHavKtBoshqaPlanBiznes() + planDto.getSamKtBoshqaPlanBiznes();

		model.addAttribute("KtHammaPlan", KtHammaPlan);
		model.addAttribute("KtKritiPlan", KtKritiPlan);
		model.addAttribute("KtPlatformaPlan", KtPlatformaPlan);
		model.addAttribute("KtPoluvagonPlan", KtPoluvagonPlan);
		model.addAttribute("KtSisternaPlan", KtSisternaPlan);
		model.addAttribute("KtBoshqaPlan", KtBoshqaPlan);

		//VCHD-3 KRP plan
		int HavKrpHammaPlan =  planDto.getHavKrpKritiPlanBiznes() + planDto.getHavKrpPlatformaPlanBiznes() + planDto.getHavKrpPoluvagonPlanBiznes() + planDto.getHavKrpSisternaPlanBiznes() + planDto.getHavKrpBoshqaPlanBiznes();
		int HavKrpKritiPlan = planDto.getHavKrpKritiPlanBiznes();
		int HavKrpPlatformaPlan = planDto.getHavKrpPlatformaPlanBiznes();
		int HavKrpPoluvagonPlan = planDto.getHavKrpPoluvagonPlanBiznes();
		int HavKrpSisternaPlan = planDto.getHavKrpSisternaPlanBiznes();
		int HavKrpBoshqaPlan = planDto.getHavKrpBoshqaPlanBiznes();

		model.addAttribute("HavKrpHammaPlan", HavKrpHammaPlan);
		model.addAttribute("HavKrpKritiPlan", HavKrpKritiPlan);
		model.addAttribute("HavKrpPlatformaPlan", HavKrpPlatformaPlan);
		model.addAttribute("HavKrpPoluvagonPlan", HavKrpPoluvagonPlan);
		model.addAttribute("HavKrpSisternaPlan", HavKrpSisternaPlan);
		model.addAttribute("HavKrpBoshqaPlan", HavKrpBoshqaPlan);

		//VCHD-5 Krp plan
		int AndjKrpHammaPlan =  planDto.getAndjKrpKritiPlanBiznes() + planDto.getAndjKrpPlatformaPlanBiznes() + planDto.getAndjKrpPoluvagonPlanBiznes() + planDto.getAndjKrpSisternaPlanBiznes() + planDto.getAndjKrpBoshqaPlanBiznes();
		int AndjKrpKritiPlan = planDto.getAndjKrpKritiPlanBiznes();
		int AndjKrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanBiznes();
		int AndjKrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanBiznes();
		int AndjKrpSisternaPlan = planDto.getAndjKrpSisternaPlanBiznes();
		int AndjKrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanBiznes();

		model.addAttribute("AndjKrpHammaPlan", AndjKrpHammaPlan);
		model.addAttribute("AndjKrpKritiPlan", AndjKrpKritiPlan);
		model.addAttribute("AndjKrpPlatformaPlan", AndjKrpPlatformaPlan);
		model.addAttribute("AndjKrpPoluvagonPlan", AndjKrpPoluvagonPlan);
		model.addAttribute("AndjKrpSisternaPlan", AndjKrpSisternaPlan);
		model.addAttribute("AndjKrpBoshqaPlan", AndjKrpBoshqaPlan);


		//samarqand KRP plan
		int SamKrpHammaPlan = planDto.getSamKrpKritiPlanBiznes() + planDto.getSamKrpPlatformaPlanBiznes() + planDto.getSamKrpPoluvagonPlanBiznes() + planDto.getSamKrpSisternaPlanBiznes() + planDto.getSamKrpBoshqaPlanBiznes();
		int SamKrpKritiPlan = planDto.getSamKrpKritiPlanBiznes();
		int SamKrpPlatformaPlan = planDto.getSamKrpPlatformaPlanBiznes();
		int SamKrpPoluvagonPlan = planDto.getSamKrpPoluvagonPlanBiznes();
		int SamKrpSisternaPlan = planDto.getSamKrpSisternaPlanBiznes();
		int SamKrpBoshqaPlan = planDto.getSamKrpBoshqaPlanBiznes();

		model.addAttribute("SamKrpHammaPlan", SamKrpHammaPlan);
		model.addAttribute("SamKrpKritiPlan", SamKrpKritiPlan);
		model.addAttribute("SamKrpPlatformaPlan", SamKrpPlatformaPlan);
		model.addAttribute("SamKrpPoluvagonPlan", SamKrpPoluvagonPlan);
		model.addAttribute("SamKrpSisternaPlan", SamKrpSisternaPlan);
		model.addAttribute("SamKrpBoshqaPlan", SamKrpBoshqaPlan);

		//Krp itogo plan
		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = planDto.getAndjKrpKritiPlanBiznes() + planDto.getHavKrpKritiPlanBiznes() + planDto.getSamKrpKritiPlanBiznes();
		int KrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanBiznes() + planDto.getHavKrpPlatformaPlanBiznes() + planDto.getSamKrpPlatformaPlanBiznes();
		int KrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanBiznes() + planDto.getHavKrpPoluvagonPlanBiznes() + planDto.getSamKrpPoluvagonPlanBiznes();
		int KrpSisternaPlan = planDto.getAndjKrpSisternaPlanBiznes() + planDto.getHavKrpSisternaPlanBiznes() + planDto.getSamKrpSisternaPlanBiznes();
		int KrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanBiznes() + planDto.getHavKrpBoshqaPlanBiznes() + planDto.getSamKrpBoshqaPlanBiznes();

		model.addAttribute("KrpHammaPlan", KrpHammaPlan);
		model.addAttribute("KrpKritiPlan", KrpKritiPlan);
		model.addAttribute("KrpPlatformaPlan", KrpPlatformaPlan);
		model.addAttribute("KrpPoluvagonPlan", KrpPoluvagonPlan);
		model.addAttribute("KrpSisternaPlan", KrpSisternaPlan);
		model.addAttribute("KrpBoshqaPlan", KrpBoshqaPlan);

		//VCHD-3 uchun depli tamir
		int hdHamma = 0;
		int hdKriti = 0;
		int hdPlatforma = 0;
		int hdPoluvagon = 0;
		int hdSisterna = 0;
		int hdBoshqa = 0;

		//VCHD-5 uchun depli tamir
		int adHamma = 0;
		int adKriti = 0;
		int adPlatforma = 0;
		int adPoluvagon = 0;
		int adSisterna = 0;
		int adBoshqa = 0;

		//samarqand uchun depli tamir
		int sdHamma = 0;
		int sdKriti = 0;
		int sdPlatforma = 0;
		int sdPoluvagon = 0;
		int sdSisterna = 0;
		int sdBoshqa = 0;


		// itogo Fact uchun depli tamir
		int uvtdHamma = 0;
		int uvtdKriti = 0;
		int uvtdPlatforma = 0;
		int uvtdPoluvagon = 0;
		int uvtdSisterna = 0;
		int uvtdBoshqa = 0;

		//Yolovchi vagon Fact
		int atYolovchi = 0;
		int adYolovchi = 0;
		
		//VCHD-3 uchun kapital tamir
		int hkHamma = 0;
		int hkKriti = 0;
		int hkPlatforma = 0;
		int hkPoluvagon = 0;
		int hkSisterna = 0;
		int hkBoshqa = 0;

		//VCHD-3 uchun kapital tamir
		int akHamma = 0;
		int akKriti = 0;
		int akPlatforma = 0;
		int akPoluvagon = 0;
		int akSisterna = 0;
		int akBoshqa = 0;

		//samarqand uchun Kapital tamir
		int skHamma = 0;
		int skKriti = 0;
		int skPlatforma = 0;
		int skPoluvagon = 0;
		int skSisterna = 0;
		int skBoshqa = 0;

		// itogo Fact uchun kapital tamir
		int uvtkhamma = 0;
		int uvtkKriti = 0;
		int uvtkPlatforma = 0;
		int uvtkPoluvagon = 0;
		int uvtkSisterna = 0;
		int uvtkBoshqa = 0;

		//VCHD-3 uchun kapital tamir
		int hkrHamma = 0;
		int hkrKriti = 0;
		int hkrPlatforma = 0;
		int hkrPoluvagon = 0;
		int hkrSisterna = 0;
		int hkrBoshqa = 0;

		//VCHD-3 uchun kapital tamir
		int akrHamma = 0;
		int akrKriti = 0;
		int akrPlatforma = 0;
		int akrPoluvagon = 0;
		int akrSisterna = 0;
		int akrBoshqa = 0;

		//samarqand uchun Kapital tamir
		int skrHamma = 0;
		int skrKriti = 0;
		int skrPlatforma = 0;
		int skrPoluvagon = 0;
		int skrSisterna = 0;
		int skrBoshqa = 0;
		

		// itogo Fact uchun KRP tamir
		int uvtkrhamma = 0;
		int uvtkrKriti = 0;
		int uvtkrPlatforma = 0;
		int uvtkrPoluvagon = 0;
		int uvtkrSisterna = 0;
		int uvtkrBoshqa = 0;


		// factlar
		if (country.equalsIgnoreCase("Hammasi")) {
			//VCHD-3 uchun depli tamir
			hdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy);
			hdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy);
			hdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", oy);
			hdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy);
			hdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy);
			hdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy);

			model.addAttribute("hdHamma", hdHamma);
			model.addAttribute("hdKriti", hdKriti);
			model.addAttribute("hdPlatforma", hdPlatforma);
			model.addAttribute("hdPoluvagon", hdPoluvagon);
			model.addAttribute("hdSisterna", hdSisterna);
			model.addAttribute("hdBoshqa", hdBoshqa);

			//VCHD-5 uchun depli tamir
			adHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy);
			adKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy);
			adPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", oy);
			adPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy);
			adSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy);
			adBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy);

			model.addAttribute("adHamma", adHamma);
			model.addAttribute("adKriti", adKriti);
			model.addAttribute("adPlatforma", adPlatforma);
			model.addAttribute("adPoluvagon", adPoluvagon);
			model.addAttribute("adSisterna", adSisterna);
			model.addAttribute("adBoshqa", adBoshqa);

			//samarqand uchun depli tamir
			sdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy);
			sdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy);
			sdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", oy);
			sdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy);
			sdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy);
			sdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy);

			model.addAttribute("sdHamma", sdHamma);
			model.addAttribute("sdKriti", sdKriti);
			model.addAttribute("sdPlatforma", sdPlatforma);
			model.addAttribute("sdPoluvagon", sdPoluvagon);
			model.addAttribute("sdSisterna", sdSisterna);
			model.addAttribute("sdBoshqa", sdBoshqa);

			// itogo Fact uchun depli tamir
			uvtdHamma = sdHamma + hdHamma + adHamma;
			uvtdKriti = sdKriti + hdKriti + adKriti;
			uvtdPlatforma = sdPlatforma + adPlatforma + hdPlatforma;
			uvtdPoluvagon = sdPoluvagon + hdPoluvagon + adPoluvagon;
			uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
			uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

			model.addAttribute("uvtdHamma", uvtdHamma);
			model.addAttribute("uvtdKriti", uvtdKriti);
			model.addAttribute("uvtdPlatforma", uvtdPlatforma);
			model.addAttribute("uvtdPoluvagon", uvtdPoluvagon);
			model.addAttribute("uvtdSisterna", uvtdSisterna);
			model.addAttribute("uvtdBoshqa", uvtdBoshqa);


			//Yolovchi vagon Fact
			atYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yo'lovchi vagon(пассжир)", "TO-3", oy);
			adYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yo'lovchi vagon(пассжир)", "Depoli ta’mir(ДР)", oy);

			model.addAttribute("atYolovchi", atYolovchi);
			model.addAttribute("adYolovchi", adYolovchi);

			//VCHD-3 uchun kapital tamir
			hkHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy);
			hkKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy);
			hkPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", oy);
			hkPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy);
			hkSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", oy);
			hkBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy);

			model.addAttribute("hkHamma", hkHamma);
			model.addAttribute("hkKriti", hkKriti);
			model.addAttribute("hkPlatforma", hkPlatforma);
			model.addAttribute("hkPoluvagon", hkPoluvagon);
			model.addAttribute("hkSisterna", hkSisterna);
			model.addAttribute("hkBoshqa", hkBoshqa);

			//VCHD-3 uchun kapital tamir
			akHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", oy);
			akKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy);
			akPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", oy);
			akPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy);
			akSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", oy);
			akBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy);

			model.addAttribute("akHamma", akHamma);
			model.addAttribute("akKriti", akKriti);
			model.addAttribute("akPlatforma", akPlatforma);
			model.addAttribute("akPoluvagon", akPoluvagon);
			model.addAttribute("akSisterna", akSisterna);
			model.addAttribute("akBoshqa", akBoshqa);

			//samarqand uchun Kapital tamir
			skHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy);
			skKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy);
			skPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", oy);
			skPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy);
			skSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", oy);
			skBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy);

			model.addAttribute("skHamma", skHamma);
			model.addAttribute("skKriti", skKriti);
			model.addAttribute("skPlatforma", skPlatforma);
			model.addAttribute("skPoluvagon", skPoluvagon);
			model.addAttribute("skSisterna", skSisterna);
			model.addAttribute("skBoshqa", skBoshqa);

			// itogo Fact uchun kapital tamir
			uvtkhamma = skHamma + hkHamma + akHamma;
			uvtkKriti = skKriti + hkKriti + akKriti;
			uvtkPlatforma = skPlatforma + akPlatforma + hkPlatforma;
			uvtkPoluvagon = skPoluvagon + hkPoluvagon + akPoluvagon;
			uvtkSisterna = akSisterna + hkSisterna + skSisterna;
			uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

			model.addAttribute("uvtkhamma", uvtkhamma);
			model.addAttribute("uvtkKriti", uvtkKriti);
			model.addAttribute("uvtkPlatforma", uvtkPlatforma);
			model.addAttribute("uvtkPoluvagon", uvtkPoluvagon);
			model.addAttribute("uvtkSisterna", uvtkSisterna);
			model.addAttribute("uvtkBoshqa", uvtkBoshqa);


			//VCHD-3 uchun kapital tamir
			hkrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "KRP(КРП)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "KRP(КРП)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", oy);
			hkrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", oy);
			hkrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "KRP(КРП)", oy);
			hkrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy);
			hkrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "KRP(КРП)", oy);
			hkrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", oy);

			model.addAttribute("hkrHamma", hkrHamma);
			model.addAttribute("hkrKriti", hkrKriti);
			model.addAttribute("hkrPlatforma", hkrPlatforma);
			model.addAttribute("hkrPoluvagon", hkrPoluvagon);
			model.addAttribute("hkrSisterna", hkrSisterna);
			model.addAttribute("hkrBoshqa", hkrBoshqa);

			//VCHD-3 uchun kapital tamir
			akrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "KRP(КРП)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "KRP(КРП)", oy);
			akrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", oy);
			akrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "KRP(КРП)", oy);
			akrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy);
			akrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "KRP(КРП)", oy);
			akrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", oy);

			model.addAttribute("akrHamma", akrHamma);
			model.addAttribute("akrKriti", akrKriti);
			model.addAttribute("akrPlatforma", akrPlatforma);
			model.addAttribute("akrPoluvagon", akrPoluvagon);
			model.addAttribute("akrSisterna", akrSisterna);
			model.addAttribute("akrBoshqa", akrBoshqa);

			//samarqand uchun Kapital tamir
			skrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "KRP(КРП)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "KRP(КРП)", oy) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", oy);
			skrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", oy);
			skrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "KRP(КРП)", oy);
			skrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy);
			skrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "KRP(КРП)", oy);
			skrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", oy);

			model.addAttribute("skrHamma", skrHamma);
			model.addAttribute("skrKriti", skrKriti);
			model.addAttribute("skrPlatforma", skrPlatforma);
			model.addAttribute("skrPoluvagon", skrPoluvagon);
			model.addAttribute("skrSisterna", skrSisterna);
			model.addAttribute("skrBoshqa", skrBoshqa);

			// itogo Fact uchun KRP tamir
			uvtkrhamma = skrHamma + hkrHamma + akrHamma;
			uvtkrKriti = skrKriti + hkrKriti + akrKriti;
			uvtkrPlatforma = skrPlatforma + akrPlatforma + hkrPlatforma;
			uvtkrPoluvagon = skrPoluvagon + hkrPoluvagon + akrPoluvagon;
			uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
			uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

			model.addAttribute("uvtkrhamma", uvtkrhamma);
			model.addAttribute("uvtkrKriti", uvtkrKriti);
			model.addAttribute("uvtkrPlatforma", uvtkrPlatforma);
			model.addAttribute("uvtkrPoluvagon", uvtkrPoluvagon);
			model.addAttribute("uvtkrSisterna", uvtkrSisterna);
			model.addAttribute("uvtkrBoshqa", uvtkrBoshqa);
			
		}else if (country.equalsIgnoreCase("O'TY(ГАЖК)")) {
			
			//VCHD-3 uchun depli tamir
			hdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");
			hdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");
			hdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");
			hdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");
			hdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");
			hdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");

			model.addAttribute("hdHamma", hdHamma);
			model.addAttribute("hdKriti", hdKriti);
			model.addAttribute("hdPlatforma", hdPlatforma);
			model.addAttribute("hdPoluvagon", hdPoluvagon);
			model.addAttribute("hdSisterna", hdSisterna);
			model.addAttribute("hdBoshqa", hdBoshqa);

			//VCHD-5 uchun depli tamir
			adHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");
			adKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");
			adPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");
			adPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");
			adSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");
			adBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");

			model.addAttribute("adHamma", adHamma);
			model.addAttribute("adKriti", adKriti);
			model.addAttribute("adPlatforma", adPlatforma);
			model.addAttribute("adPoluvagon", adPoluvagon);
			model.addAttribute("adSisterna", adSisterna);
			model.addAttribute("adBoshqa", adBoshqa);

			//samarqand uchun depli tamir
			sdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");
			sdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");
			sdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");
			sdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");
			sdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");
			sdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");

			model.addAttribute("sdHamma", sdHamma);
			model.addAttribute("sdKriti", sdKriti);
			model.addAttribute("sdPlatforma", sdPlatforma);
			model.addAttribute("sdPoluvagon", sdPoluvagon);
			model.addAttribute("sdSisterna", sdSisterna);
			model.addAttribute("sdBoshqa", sdBoshqa);

			// itogo Fact uchun depli tamir
			uvtdHamma = sdHamma + hdHamma + adHamma;
			uvtdKriti = sdKriti + hdKriti + adKriti;
			uvtdPlatforma = sdPlatforma + adPlatforma + hdPlatforma;
			uvtdPoluvagon = sdPoluvagon + hdPoluvagon + adPoluvagon;
			uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
			uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

			model.addAttribute("uvtdHamma", uvtdHamma);
			model.addAttribute("uvtdKriti", uvtdKriti);
			model.addAttribute("uvtdPlatforma", uvtdPlatforma);
			model.addAttribute("uvtdPoluvagon", uvtdPoluvagon);
			model.addAttribute("uvtdSisterna", uvtdSisterna);
			model.addAttribute("uvtdBoshqa", uvtdBoshqa);


			//Yolovchi vagon Fact
			atYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yo'lovchi vagon(пассжир)", "TO-3", oy, "O'TY(ГАЖК)");
			adYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yo'lovchi vagon(пассжир)", "Depoli ta’mir(ДР)", oy, "O'TY(ГАЖК)");

			model.addAttribute("atYolovchi", atYolovchi);
			model.addAttribute("adYolovchi", adYolovchi);

			//VCHD-3 uchun kapital tamir
			hkHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)");
			hkKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)");
			hkPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)");
			hkPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)");
			hkSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)");
			hkBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)");

			model.addAttribute("hkHamma", hkHamma);
			model.addAttribute("hkKriti", hkKriti);
			model.addAttribute("hkPlatforma", hkPlatforma);
			model.addAttribute("hkPoluvagon", hkPoluvagon);
			model.addAttribute("hkSisterna", hkSisterna);
			model.addAttribute("hkBoshqa", hkBoshqa);

			//VCHD-3 uchun kapital tamir
			akHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)");
			akKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)");
			akPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)");
			akPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)");
			akSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)");
			akBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)");

			model.addAttribute("akHamma", akHamma);
			model.addAttribute("akKriti", akKriti);
			model.addAttribute("akPlatforma", akPlatforma);
			model.addAttribute("akPoluvagon", akPoluvagon);
			model.addAttribute("akSisterna", akSisterna);
			model.addAttribute("akBoshqa", akBoshqa);

			//samarqand uchun Kapital tamir
			skHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)");
			skKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)");
			skPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)");
			skPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)");
			skSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)");
			skBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy, "O'TY(ГАЖК)");

			model.addAttribute("skHamma", skHamma);
			model.addAttribute("skKriti", skKriti);
			model.addAttribute("skPlatforma", skPlatforma);
			model.addAttribute("skPoluvagon", skPoluvagon);
			model.addAttribute("skSisterna", skSisterna);
			model.addAttribute("skBoshqa", skBoshqa);

			// itogo Fact uchun kapital tamir
			uvtkhamma = skHamma + hkHamma + akHamma;
			uvtkKriti = skKriti + hkKriti + akKriti;
			uvtkPlatforma = skPlatforma + akPlatforma + hkPlatforma;
			uvtkPoluvagon = skPoluvagon + hkPoluvagon + akPoluvagon;
			uvtkSisterna = akSisterna + hkSisterna + skSisterna;
			uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

			model.addAttribute("uvtkhamma", uvtkhamma);
			model.addAttribute("uvtkKriti", uvtkKriti);
			model.addAttribute("uvtkPlatforma", uvtkPlatforma);
			model.addAttribute("uvtkPoluvagon", uvtkPoluvagon);
			model.addAttribute("uvtkSisterna", uvtkSisterna);
			model.addAttribute("uvtkBoshqa", uvtkBoshqa);


			//VCHD-3 uchun kapital tamir
			hkrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "KRP(КРП)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "KRP(КРП)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", oy, "O'TY(ГАЖК)");
			hkrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", oy, "O'TY(ГАЖК)");
			hkrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "KRP(КРП)", oy, "O'TY(ГАЖК)");
			hkrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy, "O'TY(ГАЖК)");
			hkrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "KRP(КРП)", oy, "O'TY(ГАЖК)");
			hkrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", oy, "O'TY(ГАЖК)");

			model.addAttribute("hkrHamma", hkrHamma);
			model.addAttribute("hkrKriti", hkrKriti);
			model.addAttribute("hkrPlatforma", hkrPlatforma);
			model.addAttribute("hkrPoluvagon", hkrPoluvagon);
			model.addAttribute("hkrSisterna", hkrSisterna);
			model.addAttribute("hkrBoshqa", hkrBoshqa);

			//VCHD-3 uchun kapital tamir
			akrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "KRP(КРП)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "KRP(КРП)", oy, "O'TY(ГАЖК)");
			akrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", oy, "O'TY(ГАЖК)");
			akrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "KRP(КРП)", oy, "O'TY(ГАЖК)");
			akrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy, "O'TY(ГАЖК)");
			akrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "KRP(КРП)", oy, "O'TY(ГАЖК)");
			akrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", oy, "O'TY(ГАЖК)");

			model.addAttribute("akrHamma", akrHamma);
			model.addAttribute("akrKriti", akrKriti);
			model.addAttribute("akrPlatforma", akrPlatforma);
			model.addAttribute("akrPoluvagon", akrPoluvagon);
			model.addAttribute("akrSisterna", akrSisterna);
			model.addAttribute("akrBoshqa", akrBoshqa);

			//samarqand uchun Kapital tamir
			skrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "KRP(КРП)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "KRP(КРП)", oy, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", oy, "O'TY(ГАЖК)");
			skrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", oy, "O'TY(ГАЖК)");
			skrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "KRP(КРП)", oy, "O'TY(ГАЖК)");
			skrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy, "O'TY(ГАЖК)");
			skrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "KRP(КРП)", oy, "O'TY(ГАЖК)");
			skrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", oy, "O'TY(ГАЖК)");

			model.addAttribute("skrHamma", skrHamma);
			model.addAttribute("skrKriti", skrKriti);
			model.addAttribute("skrPlatforma", skrPlatforma);
			model.addAttribute("skrPoluvagon", skrPoluvagon);
			model.addAttribute("skrSisterna", skrSisterna);
			model.addAttribute("skrBoshqa", skrBoshqa);

			// itogo Fact uchun KRP tamir
			uvtkrhamma = skrHamma + hkrHamma + akrHamma;
			uvtkrKriti = skrKriti + hkrKriti + akrKriti;
			uvtkrPlatforma = skrPlatforma + akrPlatforma + hkrPlatforma;
			uvtkrPoluvagon = skrPoluvagon + hkrPoluvagon + akrPoluvagon;
			uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
			uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

			model.addAttribute("uvtkrhamma", uvtkrhamma);
			model.addAttribute("uvtkrKriti", uvtkrKriti);
			model.addAttribute("uvtkrPlatforma", uvtkrPlatforma);
			model.addAttribute("uvtkrPoluvagon", uvtkrPoluvagon);
			model.addAttribute("uvtkrSisterna", uvtkrSisterna);
			model.addAttribute("uvtkrBoshqa", uvtkrBoshqa);
			
		}else if (country.equalsIgnoreCase("MDH(СНГ)")) {
			
			//VCHD-3 uchun depli tamir
			hdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");
			hdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");
			hdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");
			hdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");
			hdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");
			hdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");

			model.addAttribute("hdHamma", hdHamma);
			model.addAttribute("hdKriti", hdKriti);
			model.addAttribute("hdPlatforma", hdPlatforma);
			model.addAttribute("hdPoluvagon", hdPoluvagon);
			model.addAttribute("hdSisterna", hdSisterna);
			model.addAttribute("hdBoshqa", hdBoshqa);

			//VCHD-5 uchun depli tamir
			adHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");
			adKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");
			adPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");
			adPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");
			adSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");
			adBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");

			model.addAttribute("adHamma", adHamma);
			model.addAttribute("adKriti", adKriti);
			model.addAttribute("adPlatforma", adPlatforma);
			model.addAttribute("adPoluvagon", adPoluvagon);
			model.addAttribute("adSisterna", adSisterna);
			model.addAttribute("adBoshqa", adBoshqa);

			//samarqand uchun depli tamir
			sdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");
			sdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");
			sdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");
			sdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");
			sdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");
			sdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");

			model.addAttribute("sdHamma", sdHamma);
			model.addAttribute("sdKriti", sdKriti);
			model.addAttribute("sdPlatforma", sdPlatforma);
			model.addAttribute("sdPoluvagon", sdPoluvagon);
			model.addAttribute("sdSisterna", sdSisterna);
			model.addAttribute("sdBoshqa", sdBoshqa);

			// itogo Fact uchun depli tamir
			uvtdHamma = sdHamma + hdHamma + adHamma;
			uvtdKriti = sdKriti + hdKriti + adKriti;
			uvtdPlatforma = sdPlatforma + adPlatforma + hdPlatforma;
			uvtdPoluvagon = sdPoluvagon + hdPoluvagon + adPoluvagon;
			uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
			uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

			model.addAttribute("uvtdHamma", uvtdHamma);
			model.addAttribute("uvtdKriti", uvtdKriti);
			model.addAttribute("uvtdPlatforma", uvtdPlatforma);
			model.addAttribute("uvtdPoluvagon", uvtdPoluvagon);
			model.addAttribute("uvtdSisterna", uvtdSisterna);
			model.addAttribute("uvtdBoshqa", uvtdBoshqa);


			//Yolovchi vagon Fact
			atYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yo'lovchi vagon(пассжир)", "TO-3", oy, "MDH(СНГ)");
			adYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yo'lovchi vagon(пассжир)", "Depoli ta’mir(ДР)", oy, "MDH(СНГ)");

			model.addAttribute("atYolovchi", atYolovchi);
			model.addAttribute("adYolovchi", adYolovchi);

			//VCHD-3 uchun kapital tamir
			hkHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)");
			hkKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)");
			hkPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)");
			hkPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)");
			hkSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)");
			hkBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)");

			model.addAttribute("hkHamma", hkHamma);
			model.addAttribute("hkKriti", hkKriti);
			model.addAttribute("hkPlatforma", hkPlatforma);
			model.addAttribute("hkPoluvagon", hkPoluvagon);
			model.addAttribute("hkSisterna", hkSisterna);
			model.addAttribute("hkBoshqa", hkBoshqa);

			//VCHD-3 uchun kapital tamir
			akHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)");
			akKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)");
			akPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)");
			akPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)");
			akSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)");
			akBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)");

			model.addAttribute("akHamma", akHamma);
			model.addAttribute("akKriti", akKriti);
			model.addAttribute("akPlatforma", akPlatforma);
			model.addAttribute("akPoluvagon", akPoluvagon);
			model.addAttribute("akSisterna", akSisterna);
			model.addAttribute("akBoshqa", akBoshqa);

			//samarqand uchun Kapital tamir
			skHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)");
			skKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)");
			skPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)");
			skPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)");
			skSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)");
			skBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy, "MDH(СНГ)");

			model.addAttribute("skHamma", skHamma);
			model.addAttribute("skKriti", skKriti);
			model.addAttribute("skPlatforma", skPlatforma);
			model.addAttribute("skPoluvagon", skPoluvagon);
			model.addAttribute("skSisterna", skSisterna);
			model.addAttribute("skBoshqa", skBoshqa);

			// itogo Fact uchun kapital tamir
			uvtkhamma = skHamma + hkHamma + akHamma;
			uvtkKriti = skKriti + hkKriti + akKriti;
			uvtkPlatforma = skPlatforma + akPlatforma + hkPlatforma;
			uvtkPoluvagon = skPoluvagon + hkPoluvagon + akPoluvagon;
			uvtkSisterna = akSisterna + hkSisterna + skSisterna;
			uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

			model.addAttribute("uvtkhamma", uvtkhamma);
			model.addAttribute("uvtkKriti", uvtkKriti);
			model.addAttribute("uvtkPlatforma", uvtkPlatforma);
			model.addAttribute("uvtkPoluvagon", uvtkPoluvagon);
			model.addAttribute("uvtkSisterna", uvtkSisterna);
			model.addAttribute("uvtkBoshqa", uvtkBoshqa);


			//VCHD-3 uchun kapital tamir
			hkrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "KRP(КРП)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "KRP(КРП)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", oy, "MDH(СНГ)");
			hkrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", oy, "MDH(СНГ)");
			hkrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "KRP(КРП)", oy, "MDH(СНГ)");
			hkrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy, "MDH(СНГ)");
			hkrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "KRP(КРП)", oy, "MDH(СНГ)");
			hkrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", oy, "MDH(СНГ)");

			model.addAttribute("hkrHamma", hkrHamma);
			model.addAttribute("hkrKriti", hkrKriti);
			model.addAttribute("hkrPlatforma", hkrPlatforma);
			model.addAttribute("hkrPoluvagon", hkrPoluvagon);
			model.addAttribute("hkrSisterna", hkrSisterna);
			model.addAttribute("hkrBoshqa", hkrBoshqa);

			//VCHD-3 uchun kapital tamir
			akrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "KRP(КРП)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "KRP(КРП)", oy, "MDH(СНГ)");
			akrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", oy, "MDH(СНГ)");
			akrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "KRP(КРП)", oy, "MDH(СНГ)");
			akrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy, "MDH(СНГ)");
			akrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "KRP(КРП)", oy, "MDH(СНГ)");
			akrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", oy, "MDH(СНГ)");

			model.addAttribute("akrHamma", akrHamma);
			model.addAttribute("akrKriti", akrKriti);
			model.addAttribute("akrPlatforma", akrPlatforma);
			model.addAttribute("akrPoluvagon", akrPoluvagon);
			model.addAttribute("akrSisterna", akrSisterna);
			model.addAttribute("akrBoshqa", akrBoshqa);

			//samarqand uchun Kapital tamir
			skrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "KRP(КРП)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "KRP(КРП)", oy, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", oy, "MDH(СНГ)");
			skrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", oy, "MDH(СНГ)");
			skrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "KRP(КРП)", oy, "MDH(СНГ)");
			skrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy, "MDH(СНГ)");
			skrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "KRP(КРП)", oy, "MDH(СНГ)");
			skrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", oy, "MDH(СНГ)");

			model.addAttribute("skrHamma", skrHamma);
			model.addAttribute("skrKriti", skrKriti);
			model.addAttribute("skrPlatforma", skrPlatforma);
			model.addAttribute("skrPoluvagon", skrPoluvagon);
			model.addAttribute("skrSisterna", skrSisterna);
			model.addAttribute("skrBoshqa", skrBoshqa);

			// itogo Fact uchun KRP tamir
			uvtkrhamma = skrHamma + hkrHamma + akrHamma;
			uvtkrKriti = skrKriti + hkrKriti + akrKriti;
			uvtkrPlatforma = skrPlatforma + akrPlatforma + hkrPlatforma;
			uvtkrPoluvagon = skrPoluvagon + hkrPoluvagon + akrPoluvagon;
			uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
			uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

			model.addAttribute("uvtkrhamma", uvtkrhamma);
			model.addAttribute("uvtkrKriti", uvtkrKriti);
			model.addAttribute("uvtkrPlatforma", uvtkrPlatforma);
			model.addAttribute("uvtkrPoluvagon", uvtkrPoluvagon);
			model.addAttribute("uvtkrSisterna", uvtkrSisterna);
			model.addAttribute("uvtkrBoshqa", uvtkrBoshqa);
			
		}else if (country.equalsIgnoreCase("Sanoat(ПРОМ)")) {
			
			//VCHD-3 uchun depli tamir
			hdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");
			hdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");
			hdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");
			hdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");
			hdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");
			hdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");

			model.addAttribute("hdHamma", hdHamma);
			model.addAttribute("hdKriti", hdKriti);
			model.addAttribute("hdPlatforma", hdPlatforma);
			model.addAttribute("hdPoluvagon", hdPoluvagon);
			model.addAttribute("hdSisterna", hdSisterna);
			model.addAttribute("hdBoshqa", hdBoshqa);

			//VCHD-5 uchun depli tamir
			adHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");
			adKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");
			adPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");
			adPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");
			adSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");
			adBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");

			model.addAttribute("adHamma", adHamma);
			model.addAttribute("adKriti", adKriti);
			model.addAttribute("adPlatforma", adPlatforma);
			model.addAttribute("adPoluvagon", adPoluvagon);
			model.addAttribute("adSisterna", adSisterna);
			model.addAttribute("adBoshqa", adBoshqa);

			//samarqand uchun depli tamir
			sdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");
			sdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");
			sdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");
			sdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");
			sdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");
			sdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");

			model.addAttribute("sdHamma", sdHamma);
			model.addAttribute("sdKriti", sdKriti);
			model.addAttribute("sdPlatforma", sdPlatforma);
			model.addAttribute("sdPoluvagon", sdPoluvagon);
			model.addAttribute("sdSisterna", sdSisterna);
			model.addAttribute("sdBoshqa", sdBoshqa);

			// itogo Fact uchun depli tamir
			uvtdHamma = sdHamma + hdHamma + adHamma;
			uvtdKriti = sdKriti + hdKriti + adKriti;
			uvtdPlatforma = sdPlatforma + adPlatforma + hdPlatforma;
			uvtdPoluvagon = sdPoluvagon + hdPoluvagon + adPoluvagon;
			uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
			uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

			model.addAttribute("uvtdHamma", uvtdHamma);
			model.addAttribute("uvtdKriti", uvtdKriti);
			model.addAttribute("uvtdPlatforma", uvtdPlatforma);
			model.addAttribute("uvtdPoluvagon", uvtdPoluvagon);
			model.addAttribute("uvtdSisterna", uvtdSisterna);
			model.addAttribute("uvtdBoshqa", uvtdBoshqa);


			//Yolovchi vagon Fact
			atYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yo'lovchi vagon(пассжир)", "TO-3", oy, "Sanoat(ПРОМ)");
			adYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yo'lovchi vagon(пассжир)", "Depoli ta’mir(ДР)", oy, "Sanoat(ПРОМ)");

			model.addAttribute("atYolovchi", atYolovchi);
			model.addAttribute("adYolovchi", adYolovchi);

			//VCHD-3 uchun kapital tamir
			hkHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)");
			hkKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)");
			hkPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)");
			hkPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)");
			hkSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)");
			hkBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)");

			model.addAttribute("hkHamma", hkHamma);
			model.addAttribute("hkKriti", hkKriti);
			model.addAttribute("hkPlatforma", hkPlatforma);
			model.addAttribute("hkPoluvagon", hkPoluvagon);
			model.addAttribute("hkSisterna", hkSisterna);
			model.addAttribute("hkBoshqa", hkBoshqa);

			//VCHD-3 uchun kapital tamir
			akHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)");
			akKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)");
			akPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)");
			akPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)");
			akSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)");
			akBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)");

			model.addAttribute("akHamma", akHamma);
			model.addAttribute("akKriti", akKriti);
			model.addAttribute("akPlatforma", akPlatforma);
			model.addAttribute("akPoluvagon", akPoluvagon);
			model.addAttribute("akSisterna", akSisterna);
			model.addAttribute("akBoshqa", akBoshqa);

			//samarqand uchun Kapital tamir
			skHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)");
			skKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)");
			skPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)");
			skPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)");
			skSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)");
			skBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy, "Sanoat(ПРОМ)");

			model.addAttribute("skHamma", skHamma);
			model.addAttribute("skKriti", skKriti);
			model.addAttribute("skPlatforma", skPlatforma);
			model.addAttribute("skPoluvagon", skPoluvagon);
			model.addAttribute("skSisterna", skSisterna);
			model.addAttribute("skBoshqa", skBoshqa);

			// itogo Fact uchun kapital tamir
			uvtkhamma = skHamma + hkHamma + akHamma;
			uvtkKriti = skKriti + hkKriti + akKriti;
			uvtkPlatforma = skPlatforma + akPlatforma + hkPlatforma;
			uvtkPoluvagon = skPoluvagon + hkPoluvagon + akPoluvagon;
			uvtkSisterna = akSisterna + hkSisterna + skSisterna;
			uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

			model.addAttribute("uvtkhamma", uvtkhamma);
			model.addAttribute("uvtkKriti", uvtkKriti);
			model.addAttribute("uvtkPlatforma", uvtkPlatforma);
			model.addAttribute("uvtkPoluvagon", uvtkPoluvagon);
			model.addAttribute("uvtkSisterna", uvtkSisterna);
			model.addAttribute("uvtkBoshqa", uvtkBoshqa);


			//VCHD-3 uchun kapital tamir
			hkrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "KRP(КРП)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "KRP(КРП)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", oy, "Sanoat(ПРОМ)");
			hkrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", oy, "Sanoat(ПРОМ)");
			hkrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "KRP(КРП)", oy, "Sanoat(ПРОМ)");
			hkrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy, "Sanoat(ПРОМ)");
			hkrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "KRP(КРП)", oy, "Sanoat(ПРОМ)");
			hkrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", oy, "Sanoat(ПРОМ)");

			model.addAttribute("hkrHamma", hkrHamma);
			model.addAttribute("hkrKriti", hkrKriti);
			model.addAttribute("hkrPlatforma", hkrPlatforma);
			model.addAttribute("hkrPoluvagon", hkrPoluvagon);
			model.addAttribute("hkrSisterna", hkrSisterna);
			model.addAttribute("hkrBoshqa", hkrBoshqa);

			//VCHD-3 uchun kapital tamir
			akrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "KRP(КРП)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "KRP(КРП)", oy, "Sanoat(ПРОМ)");
			akrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", oy, "Sanoat(ПРОМ)");
			akrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "KRP(КРП)", oy, "Sanoat(ПРОМ)");
			akrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy, "Sanoat(ПРОМ)");
			akrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "KRP(КРП)", oy, "Sanoat(ПРОМ)");
			akrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", oy, "Sanoat(ПРОМ)");

			model.addAttribute("akrHamma", akrHamma);
			model.addAttribute("akrKriti", akrKriti);
			model.addAttribute("akrPlatforma", akrPlatforma);
			model.addAttribute("akrPoluvagon", akrPoluvagon);
			model.addAttribute("akrSisterna", akrSisterna);
			model.addAttribute("akrBoshqa", akrBoshqa);

			//samarqand uchun Kapital tamir
			skrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "KRP(КРП)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "KRP(КРП)", oy, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", oy, "Sanoat(ПРОМ)");
			skrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", oy, "Sanoat(ПРОМ)");
			skrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "KRP(КРП)", oy, "Sanoat(ПРОМ)");
			skrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy, "Sanoat(ПРОМ)");
			skrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "KRP(КРП)", oy, "Sanoat(ПРОМ)");
			skrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", oy, "Sanoat(ПРОМ)");

			model.addAttribute("skrHamma", skrHamma);
			model.addAttribute("skrKriti", skrKriti);
			model.addAttribute("skrPlatforma", skrPlatforma);
			model.addAttribute("skrPoluvagon", skrPoluvagon);
			model.addAttribute("skrSisterna", skrSisterna);
			model.addAttribute("skrBoshqa", skrBoshqa);

			// itogo Fact uchun KRP tamir
			uvtkrhamma = skrHamma + hkrHamma + akrHamma;
			uvtkrKriti = skrKriti + hkrKriti + akrKriti;
			uvtkrPlatforma = skrPlatforma + akrPlatforma + hkrPlatforma;
			uvtkrPoluvagon = skrPoluvagon + hkrPoluvagon + akrPoluvagon;
			uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
			uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

			model.addAttribute("uvtkrhamma", uvtkrhamma);
			model.addAttribute("uvtkrKriti", uvtkrKriti);
			model.addAttribute("uvtkrPlatforma", uvtkrPlatforma);
			model.addAttribute("uvtkrPoluvagon", uvtkrPoluvagon);
			model.addAttribute("uvtkrSisterna", uvtkrSisterna);
			model.addAttribute("uvtkrBoshqa", uvtkrBoshqa);
			
		}


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
		vagonsToDownloadTable.add(uvtdHamma);
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

		vagonsToDownloadTable.add(AndjToYolovchiPlan);
		vagonsToDownloadTable.add(atYolovchi);
		vagonsToDownloadTable.add(AndjDtYolovchiPlan);
		vagonsToDownloadTable.add(adYolovchi);

		vagonsToDownloadAllTable = vagonsToDownloadTable;
		
	   	 return "AllPlanTable";
    }

    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
   	@GetMapping("/vagons/filterAllMonth")
   	public String filterByDepoNomiForAllMonths(Model model,  @RequestParam(value = "depoNomi", required = false) String depoNomi,
   												@RequestParam(value = "vagonTuri", required = false) String vagonTuri,
   												@RequestParam(value = "country", required = false) String country) {
   		if(!depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi") ) {
   			vagonsToDownload = vagonTayyorService.findByDepoNomiVagonTuriAndCountry(depoNomi, vagonTuri, country);
   			model.addAttribute("vagons", vagonTayyorService.findByDepoNomiVagonTuriAndCountry(depoNomi, vagonTuri, country));
   		}else if(depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi")){
   			vagonsToDownload = vagonTayyorService.findByVagonTuriAndCountry(vagonTuri , country);
   			model.addAttribute("vagons", vagonTayyorService.findByVagonTuriAndCountry(vagonTuri , country));
   		}else if(depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi")&& !country.equalsIgnoreCase("Hammasi")){
   			vagonsToDownload = vagonTayyorService.findBycountry(country );
   			model.addAttribute("vagons", vagonTayyorService.findBycountry(country ));
   		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi")){
   			vagonsToDownload = vagonTayyorService.findByDepoNomiAndVagonTuri(depoNomi, vagonTuri);
   			model.addAttribute("vagons", vagonTayyorService.findByDepoNomiAndVagonTuri(depoNomi, vagonTuri));
   		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi")){
   			vagonsToDownload = vagonTayyorService.findByDepoNomiAndCountry(depoNomi, country);
   			model.addAttribute("vagons", vagonTayyorService.findByDepoNomiAndCountry(depoNomi, country));
   		}else if(depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi")){
   			vagonsToDownload = vagonTayyorService.findByVagonTuri(vagonTuri);
   			model.addAttribute("vagons", vagonTayyorService.findByVagonTuri(vagonTuri));
   		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi")){
   			vagonsToDownload = vagonTayyorService.findByDepoNomi(depoNomi );
   			model.addAttribute("vagons", vagonTayyorService.findByDepoNomi(depoNomi ));
   		}else {
   			vagonsToDownload = vagonTayyorService.findAll( );
   			model.addAttribute("vagons", vagonTayyorService.findAll());
   		}

		PlanBiznes planDto = vagonTayyorService.getPlanBiznes();
		//planlar kiritish


		//havos depo tamir hamma plan
		int HavDtKritiPlan = planDto.getHavDtKritiPlanBiznesMonths();
		int HavDtPlatformaPlan = planDto.getHavDtPlatformaPlanBiznesMonths();
		int HavDtPoluvagonPlan = planDto.getHavDtPoluvagonPlanBiznesMonths();
		int HavDtSisternaPlan = planDto.getHavDtSisternaPlanBiznesMonths();
		int HavDtBoshqaPlan = planDto.getHavDtBoshqaPlanBiznesMonths();
		int HavDtHammaPlan = HavDtKritiPlan + HavDtPlatformaPlan + HavDtPoluvagonPlan + HavDtSisternaPlan + HavDtBoshqaPlan;

		model.addAttribute("HavDtHammaPlan", HavDtHammaPlan);
		model.addAttribute("HavDtKritiPlan", HavDtKritiPlan);
		model.addAttribute("HavDtPlatformaPlan", HavDtPlatformaPlan);
		model.addAttribute("HavDtPoluvagonPlan", HavDtPoluvagonPlan);
		model.addAttribute("HavDtSisternaPlan", HavDtSisternaPlan);
		model.addAttribute("HavDtBoshqaPlan", HavDtBoshqaPlan);


		//VCHD-5 depo tamir plan
		int AndjDtKritiPlan = planDto.getAndjDtKritiPlanBiznesMonths();
		int AndjDtPlatformaPlan =planDto.getAndjDtPlatformaPlanBiznesMonths();
		int AndjDtPoluvagonPlan =planDto.getAndjDtPoluvagonPlanBiznesMonths();
		int AndjDtSisternaPlan =planDto.getAndjDtSisternaPlanBiznesMonths();
		int AndjDtBoshqaPlan=planDto.getAndjDtBoshqaPlanBiznesMonths();
		int AndjDtHammaPlan = AndjDtKritiPlan + AndjDtPlatformaPlan + AndjDtPoluvagonPlan + AndjDtSisternaPlan + AndjDtBoshqaPlan;

		model.addAttribute("AndjDtHammaPlan", AndjDtHammaPlan);
		model.addAttribute("AndjDtKritiPlan", AndjDtKritiPlan);
		model.addAttribute("AndjDtPlatformaPlan",AndjDtPlatformaPlan);
		model.addAttribute("AndjDtPoluvagonPlan", AndjDtPoluvagonPlan);
		model.addAttribute("AndjDtSisternaPlan", AndjDtSisternaPlan);
		model.addAttribute("AndjDtBoshqaPlan", AndjDtBoshqaPlan);

		//samarqand depo tamir plan
		int SamDtKritiPlan = planDto.getSamDtKritiPlanBiznesMonths();
		int SamDtPlatformaPlan = planDto.getSamDtPlatformaPlanBiznesMonths();
		int SamDtPoluvagonPlan =  planDto.getSamDtPoluvagonPlanBiznesMonths();
		int SamDtSisternaPlan = planDto.getSamDtSisternaPlanBiznesMonths();
		int SamDtBoshqaPlan = planDto.getSamDtBoshqaPlanBiznesMonths();
		int SamDtHammaPlan=SamDtKritiPlan + SamDtPlatformaPlan + SamDtPoluvagonPlan + SamDtSisternaPlan + SamDtBoshqaPlan;

		model.addAttribute("SamDtHammaPlan",SamDtHammaPlan);
		model.addAttribute("SamDtKritiPlan", SamDtKritiPlan);
		model.addAttribute("SamDtPlatformaPlan", SamDtPlatformaPlan);
		model.addAttribute("SamDtPoluvagonPlan", SamDtPoluvagonPlan);
		model.addAttribute("SamDtSisternaPlan", SamDtSisternaPlan);
		model.addAttribute("SamDtBoshqaPlan", SamDtBoshqaPlan);


		// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = SamDtKritiPlan + HavDtKritiPlan + AndjDtKritiPlan;
		int DtPlatformaPlan = SamDtPlatformaPlan + HavDtPlatformaPlan + AndjDtPlatformaPlan;
		int DtPoluvagonPlan = SamDtPoluvagonPlan + HavDtPoluvagonPlan + AndjDtPoluvagonPlan;
		int DtSisternaPlan = SamDtSisternaPlan + HavDtSisternaPlan + AndjDtSisternaPlan;
		int DtBoshqaPlan = SamDtBoshqaPlan + HavDtBoshqaPlan + AndjDtBoshqaPlan;

		model.addAttribute("DtHammaPlan", DtHammaPlan);
		model.addAttribute("DtKritiPlan", DtKritiPlan);
		model.addAttribute("DtPlatformaPlan", DtPlatformaPlan);
		model.addAttribute("DtPoluvagonPlan",DtPoluvagonPlan);
		model.addAttribute("DtSisternaPlan", DtSisternaPlan);
		model.addAttribute("DtBoshqaPlan", DtBoshqaPlan);


		//Yolovchi vagon Plan
		int AndjToYolovchiPlan = planDto.getAndjTYolovchiPlanBiznesMonths();
		int AndjDtYolovchiPlan = planDto.getAndjDtYolovchiPlanBiznesMonths();

		model.addAttribute("AndjToYolovchiPlan", AndjToYolovchiPlan);
		model.addAttribute("AndjDtYolovchiPlan", AndjDtYolovchiPlan);

		//hovos kapital plan
		int HavKtKritiPlan = planDto.getHavKtKritiPlanBiznesMonths();
		int HavKtPlatformaPlan = planDto.getHavKtPlatformaPlanBiznesMonths();
		int HavKtPoluvagonPlan = planDto.getHavKtPoluvagonPlanBiznesMonths();
		int HavKtSisternaPlan = planDto.getHavKtSisternaPlanBiznesMonths();
		int HavKtBoshqaPlan = planDto.getHavKtBoshqaPlanBiznesMonths();
		int HavKtHammaPlan = HavKtKritiPlan + HavKtPlatformaPlan + HavKtPoluvagonPlan + HavKtSisternaPlan + HavKtBoshqaPlan;

		model.addAttribute("HavKtHammaPlan", HavKtHammaPlan);
		model.addAttribute("HavKtKritiPlan", HavKtKritiPlan);
		model.addAttribute("HavKtPlatformaPlan", HavKtPlatformaPlan);
		model.addAttribute("HavKtPoluvagonPlan", HavKtPoluvagonPlan);
		model.addAttribute("HavKtSisternaPlan", HavKtSisternaPlan);
		model.addAttribute("HavKtBoshqaPlan", HavKtBoshqaPlan);

		//ANDIJON kapital plan
		int AndjKtKritiPlan = planDto.getAndjKtKritiPlanBiznesMonths();
		int AndjKtPlatformaPlan=planDto.getAndjKtPlatformaPlanBiznesMonths();
		int AndjKtPoluvagonPlan=planDto.getAndjKtPoluvagonPlanBiznesMonths();
		int AndjKtSisternaPlan=planDto.getAndjKtSisternaPlanBiznesMonths();
		int AndjKtBoshqaPlan=planDto.getAndjKtBoshqaPlanBiznesMonths();
		int AndjKtHammaPlan = AndjKtKritiPlan + AndjKtPlatformaPlan + AndjKtPoluvagonPlan + AndjKtSisternaPlan + AndjKtBoshqaPlan;

		model.addAttribute("AndjKtHammaPlan", AndjKtHammaPlan);
		model.addAttribute("AndjKtKritiPlan", AndjKtKritiPlan);
		model.addAttribute("AndjKtPlatformaPlan", AndjKtPlatformaPlan);
		model.addAttribute("AndjKtPoluvagonPlan", AndjKtPoluvagonPlan);
		model.addAttribute("AndjKtSisternaPlan", AndjKtSisternaPlan);
		model.addAttribute("AndjKtBoshqaPlan", AndjKtBoshqaPlan);

		//Samrqand kapital plan
		int SamKtKritiPlan = planDto.getSamKtKritiPlanBiznesMonths();
		int SamKtPlatformaPlan = planDto.getSamKtPlatformaPlanBiznesMonths();
		int SamKtPoluvagonPlan = planDto.getSamKtPoluvagonPlanBiznesMonths();
		int SamKtSisternaPlan = planDto.getSamKtSisternaPlanBiznesMonths();
		int SamKtBoshqaPlan = planDto.getSamKtBoshqaPlanBiznesMonths();
		int SamKtHammaPlan = SamKtKritiPlan + SamKtPlatformaPlan + SamKtPoluvagonPlan + SamKtSisternaPlan +SamKtBoshqaPlan;

		model.addAttribute("SamKtHammaPlan",SamKtHammaPlan);
		model.addAttribute("SamKtKritiPlan", SamKtKritiPlan);
		model.addAttribute("SamKtPlatformaPlan", SamKtPlatformaPlan);
		model.addAttribute("SamKtPoluvagonPlan", SamKtPoluvagonPlan);
		model.addAttribute("SamKtSisternaPlan", SamKtSisternaPlan);
		model.addAttribute("SamKtBoshqaPlan", SamKtBoshqaPlan);


		//Itogo kapital plan
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = SamKtKritiPlan + HavKtKritiPlan + AndjKtKritiPlan;
		int KtPlatformaPlan = SamKtPlatformaPlan + HavKtPlatformaPlan + AndjKtPlatformaPlan;
		int KtPoluvagonPlan = SamKtPoluvagonPlan + HavKtPoluvagonPlan + AndjKtPoluvagonPlan;
		int KtSisternaPlan = SamKtSisternaPlan + HavKtSisternaPlan + AndjKtSisternaPlan;
		int KtBoshqaPlan = SamKtBoshqaPlan + HavKtBoshqaPlan + AndjKtBoshqaPlan;

		model.addAttribute("KtHammaPlan", KtHammaPlan);
		model.addAttribute("KtKritiPlan", KtKritiPlan);
		model.addAttribute("KtPlatformaPlan", KtPlatformaPlan);
		model.addAttribute("KtPoluvagonPlan",KtPoluvagonPlan);
		model.addAttribute("KtSisternaPlan", KtSisternaPlan);
		model.addAttribute("KtBoshqaPlan", KtBoshqaPlan);


		//Hovos krp plan
		int HavKrpKritiPlan = planDto.getHavKrpKritiPlanBiznesMonths();
		int HavKrpPlatformaPlan = planDto.getHavKrpPlatformaPlanBiznesMonths();
		int HavKrpPoluvagonPlan = planDto.getHavKrpPoluvagonPlanBiznesMonths();
		int HavKrpSisternaPlan = planDto.getHavKrpSisternaPlanBiznesMonths();
		int HavKrpBoshqaPlan = planDto.getHavKrpBoshqaPlanBiznesMonths();
		int HavKrpHammaPlan = HavKrpKritiPlan + HavKrpPlatformaPlan + HavKrpPoluvagonPlan + HavKrpSisternaPlan + HavKrpBoshqaPlan;

		model.addAttribute("HavKrpHammaPlan",HavKrpHammaPlan);
		model.addAttribute("HavKrpKritiPlan", HavKrpKritiPlan);
		model.addAttribute("HavKrpPlatformaPlan", HavKrpPlatformaPlan);
		model.addAttribute("HavKrpPoluvagonPlan", HavKrpPoluvagonPlan);
		model.addAttribute("HavKrpSisternaPlan", HavKrpSisternaPlan);
		model.addAttribute("HavKrpBoshqaPlan", HavKrpBoshqaPlan);

		//andijon krp plan
		int AndjKrpKritiPlan = planDto.getAndjKrpKritiPlanBiznesMonths();
		int AndjKrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanBiznesMonths();
		int AndjKrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanBiznesMonths();
		int AndjKrpSisternaPlan = planDto.getAndjKrpSisternaPlanBiznesMonths();
		int AndjKrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanBiznesMonths();
		int AndjKrpHammaPlan = AndjKrpKritiPlan + AndjKrpPlatformaPlan + AndjKrpPoluvagonPlan + AndjKrpSisternaPlan + AndjKrpBoshqaPlan;

		model.addAttribute("AndjKrpHammaPlan",AndjKrpHammaPlan);
		model.addAttribute("AndjKrpKritiPlan", AndjKrpKritiPlan);
		model.addAttribute("AndjKrpPlatformaPlan", AndjKrpPlatformaPlan);
		model.addAttribute("AndjKrpPoluvagonPlan", AndjKrpPoluvagonPlan);
		model.addAttribute("AndjKrpSisternaPlan", AndjKrpSisternaPlan);
		model.addAttribute("AndjKrpBoshqaPlan", AndjKrpBoshqaPlan);

		//Samarqankr Krp plan
		int SamKrpKritiPlan = planDto.getSamKrpKritiPlanBiznesMonths();
		int SamKrpPlatformaPlan = planDto.getSamKrpPlatformaPlanBiznesMonths();
		int SamKrpPoluvagonPlan = planDto.getSamKrpPoluvagonPlanBiznesMonths();
		int SamKrpSisternaPlan = planDto.getSamKrpSisternaPlanBiznesMonths();
		int SamKrpBoshqaPlan = planDto.getSamKrpBoshqaPlanBiznesMonths();
		int SamKrpHammaPlan = SamKrpKritiPlan + SamKrpPlatformaPlan + SamKrpPoluvagonPlan + SamKrpSisternaPlan + SamKrpBoshqaPlan;

		model.addAttribute("SamKrpHammaPlan", SamKrpHammaPlan);
		model.addAttribute("SamKrpKritiPlan", SamKrpKritiPlan);
		model.addAttribute("SamKrpPlatformaPlan", SamKrpPlatformaPlan);
		model.addAttribute("SamKrpPoluvagonPlan", SamKrpPoluvagonPlan);
		model.addAttribute("SamKrpSisternaPlan", SamKrpSisternaPlan);
		model.addAttribute("SamKrpBoshqaPlan", SamKrpBoshqaPlan);


		//itogo krp

		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = SamKrpKritiPlan + HavKrpKritiPlan + AndjKrpKritiPlan;
		int KrpPlatformaPlan = SamKrpPlatformaPlan + HavKrpPlatformaPlan + AndjKrpPlatformaPlan;
		int KrpPoluvagonPlan = SamKrpPoluvagonPlan + HavKrpPoluvagonPlan + AndjKrpPoluvagonPlan;
		int KrpSisternaPlan = SamKrpSisternaPlan + HavKrpSisternaPlan + AndjKrpSisternaPlan;
		int KrpBoshqaPlan = SamKrpBoshqaPlan + HavKrpBoshqaPlan + AndjKrpBoshqaPlan;

		model.addAttribute("KrpHammaPlan", KrpHammaPlan);
		model.addAttribute("KrpKritiPlan", KrpKritiPlan);
		model.addAttribute("KrpPlatformaPlan", KrpPlatformaPlan);
		model.addAttribute("KrpPoluvagonPlan",KrpPoluvagonPlan);
		model.addAttribute("KrpSisternaPlan", KrpSisternaPlan);
		model.addAttribute("KrpBoshqaPlan", KrpBoshqaPlan);

		//VCHD-3 uchun depli tamir
		int hdHamma = 0;
		int hdKriti = 0;
		int hdPlatforma = 0;
		int hdPoluvagon = 0;
		int hdSisterna = 0;
		int hdBoshqa = 0;

		//VCHD-5 uchun depli tamir
		int adHamma = 0;
		int adKriti = 0;
		int adPlatforma = 0;
		int adPoluvagon = 0;
		int adSisterna = 0;
		int adBoshqa = 0;

		//samarqand uchun depli tamir
		int sdHamma = 0;
		int sdKriti = 0;
		int sdPlatforma = 0;
		int sdPoluvagon = 0;
		int sdSisterna = 0;
		int sdBoshqa = 0;


		// itogo Fact uchun depli tamir
		int uvtdHamma = 0;
		int uvtdKriti = 0;
		int uvtdPlatforma = 0;
		int uvtdPoluvagon = 0;
		int uvtdSisterna = 0;
		int uvtdBoshqa = 0;

		//Yolovchi vagon Fact
		int atYolovchi = 0;
		int adYolovchi = 0;

		//VCHD-3 uchun kapital tamir
		int hkHamma = 0;
		int hkKriti = 0;
		int hkPlatforma = 0;
		int hkPoluvagon = 0;
		int hkSisterna = 0;
		int hkBoshqa = 0;

		//VCHD-3 uchun kapital tamir
		int akHamma = 0;
		int akKriti = 0;
		int akPlatforma = 0;
		int akPoluvagon = 0;
		int akSisterna = 0;
		int akBoshqa = 0;

		//samarqand uchun Kapital tamir
		int skHamma = 0;
		int skKriti = 0;
		int skPlatforma = 0;
		int skPoluvagon = 0;
		int skSisterna = 0;
		int skBoshqa = 0;

		// itogo Fact uchun kapital tamir
		int uvtkHamma = 0;
		int uvtkKriti = 0;
		int uvtkPlatforma = 0;
		int uvtkPoluvagon = 0;
		int uvtkSisterna = 0;
		int uvtkBoshqa = 0;

		//VCHD-3 uchun kapital tamir
		int hkrHamma = 0;
		int hkrKriti = 0;
		int hkrPlatforma = 0;
		int hkrPoluvagon = 0;
		int hkrSisterna = 0;
		int hkrBoshqa = 0;

		//VCHD-3 uchun kapital tamir
		int akrHamma = 0;
		int akrKriti = 0;
		int akrPlatforma = 0;
		int akrPoluvagon = 0;
		int akrSisterna = 0;
		int akrBoshqa = 0;

		//samarqand uchun Kapital tamir
		int skrHamma = 0;
		int skrKriti = 0;
		int skrPlatforma = 0;
		int skrPoluvagon = 0;
		int skrSisterna = 0;
		int skrBoshqa = 0;


		// itogo Fact uchun KRP tamir
		int uvtkrHamma = 0;
		int uvtkrKriti = 0;
		int uvtkrPlatforma = 0;
		int uvtkrPoluvagon = 0;
		int uvtkrSisterna = 0;
		int uvtkrBoshqa = 0;
		
//		if (country.equalsIgnoreCase("Hammasi")) {
			//**//
			// VCHD-3 depo tamir hamma false vagonlar soni
			hdKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)");
			hdPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)");
			hdPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)");
			hdSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)");
			hdBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)");
			hdHamma = hdKriti + hdPlatforma + hdPoluvagon + hdSisterna + hdBoshqa;

			model.addAttribute("hdHamma", hdHamma + 651);
			model.addAttribute("hdKriti", hdKriti + 35);
			model.addAttribute("hdPlatforma", hdPlatforma + 45);
			model.addAttribute("hdPoluvagon", hdPoluvagon + 109);
			model.addAttribute("hdSisterna", hdSisterna + 35);
			model.addAttribute("hdBoshqa", hdBoshqa + 427);

			// VCHD-5 depo tamir hamma false vagonlar soni
			adKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)");
			adPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)");
			adPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)");
			adSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)");
			adBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)");
			adHamma = adKriti + adPlatforma + adPoluvagon + adSisterna + adBoshqa;

			model.addAttribute("adHamma", adHamma + 443);
			model.addAttribute("adKriti", adKriti + 224);
			model.addAttribute("adPlatforma", adPlatforma + 3);
			model.addAttribute("adPoluvagon", adPoluvagon + 103);
			model.addAttribute("adSisterna", adSisterna);
			model.addAttribute("adBoshqa", adBoshqa + 113);

			// samarqand depo tamir hamma false vagonlar soni
			sdKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)");
			sdPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)");
			sdPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)");
			sdSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)");
			sdBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)");
			sdHamma = sdKriti + sdPlatforma + sdPoluvagon + sdSisterna + sdBoshqa;

			model.addAttribute("sdHamma", sdHamma + 393);
			model.addAttribute("sdKriti", sdKriti + 135);
			model.addAttribute("sdPlatforma", sdPlatforma + 8);
			model.addAttribute("sdPoluvagon", sdPoluvagon + 67);
			model.addAttribute("sdSisterna", sdSisterna + 23);
			model.addAttribute("sdBoshqa", sdBoshqa + 160);

			// depoli tamir itogo uchun
			uvtdHamma = adHamma + hdHamma + sdHamma;
			uvtdKriti = sdKriti + hdKriti + adKriti;
			uvtdPlatforma = adPlatforma + sdPlatforma + hdPlatforma;
			uvtdPoluvagon = adPoluvagon + sdPoluvagon + hdPoluvagon;
			uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
			uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

			model.addAttribute("uvtdHamma", uvtdHamma + 1487);
			model.addAttribute("uvtdKriti", uvtdKriti + 394);
			model.addAttribute("uvtdPlatforma", uvtdPlatforma + 56);
			model.addAttribute("uvtdPoluvagon", uvtdPoluvagon + 279);
			model.addAttribute("uvtdSisterna", uvtdSisterna + 58);
			model.addAttribute("uvtdBoshqa", uvtdBoshqa + 700);

			//Yolovchi Andijon fact
			atYolovchi = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yo'lovchi vagon(пассжир)", "TO-3");
			adYolovchi = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yo'lovchi vagon(пассжир)", "Depoli ta’mir(ДР)");

			model.addAttribute("atYolovchi", atYolovchi + 37);
			model.addAttribute("adYolovchi", adYolovchi + 24);


			// VCHD-3 kapital tamir hamma false vagonlar soni
			hkKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)");
			hkPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)");
			hkPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)");
			hkSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)");
			hkBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)");
			hkHamma = hkKriti + hkPlatforma + hkPoluvagon + hkSisterna + hkBoshqa;

			model.addAttribute("hkHamma", hkHamma + 227);
			model.addAttribute("hkKriti", hkKriti + 41);
			model.addAttribute("hkPlatforma", hkPlatforma + 32);
			model.addAttribute("hkPoluvagon", hkPoluvagon + 4);
			model.addAttribute("hkSisterna", hkSisterna + 51);
			model.addAttribute("hkBoshqa", hkBoshqa + 99);

			// VCHD-5 kapital tamir hamma false vagonlar soni
			akKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)");
			akPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)");
			akPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)");
			akSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)");
			akBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)");
			akHamma = akKriti + akPlatforma + akPoluvagon + akSisterna + akBoshqa;

			model.addAttribute("akHamma", akHamma + 28);
			model.addAttribute("akKriti", akKriti + 26);
			model.addAttribute("akPlatforma", akPlatforma);
			model.addAttribute("akPoluvagon", akPoluvagon + 2);
			model.addAttribute("akSisterna", akSisterna);
			model.addAttribute("akBoshqa", akBoshqa);

			// samarqand KApital tamir hamma false vagonlar soni
			skKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)");
			skPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)");
			skPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)");
			skSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)");
			skBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)");
			skHamma = skKriti + skPlatforma + skPoluvagon + skSisterna + skBoshqa;

			model.addAttribute("skHamma", skHamma + 284);
			model.addAttribute("skKriti", skKriti + 160);
			model.addAttribute("skPlatforma", skPlatforma + 44);
			model.addAttribute("skPoluvagon", skPoluvagon + 52);
			model.addAttribute("skSisterna", skSisterna + 9);
			model.addAttribute("skBoshqa", skBoshqa + 19);

			// Kapital tamir itogo uchun
			uvtkHamma = akHamma + hkHamma + skHamma;
			uvtkKriti = skKriti + hkKriti + akKriti;
			uvtkPlatforma = akPlatforma + skPlatforma + hkPlatforma;
			uvtkPoluvagon = akPoluvagon + skPoluvagon + hkPoluvagon;
			uvtkSisterna = akSisterna + hkSisterna + skSisterna;
			uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

			model.addAttribute("uvtkHamma", uvtkHamma + 539);
			model.addAttribute("uvtkKriti", uvtkKriti + 227);
			model.addAttribute("uvtkPlatforma", uvtkPlatforma + 76);
			model.addAttribute("uvtkPoluvagon", uvtkPoluvagon + 58);
			model.addAttribute("uvtkSisterna", uvtkSisterna + 60);
			model.addAttribute("uvtkBoshqa", uvtkBoshqa + 118);

			//**

			// VCHD-3 KRP tamir hamma false vagonlar soni
			hkrKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)");
			hkrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "KRP(КРП)");
			hkrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)");
			hkrSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "KRP(КРП)");
			hkrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)");
			hkrHamma = hkrKriti + hkrPlatforma + hkrPoluvagon + hkrSisterna + hkrBoshqa;

			model.addAttribute("hkrHamma", hkrHamma + 83);
			model.addAttribute("hkrKriti", hkrKriti + 83);
			model.addAttribute("hkrPlatforma", hkrPlatforma);
			model.addAttribute("hkrPoluvagon", hkrPoluvagon);
			model.addAttribute("hkrSisterna", hkrSisterna);
			model.addAttribute("hkrBoshqa", hkrBoshqa);

			// VCHD-5 KRP tamir hamma false vagonlar soni
			akrKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)");
			akrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "KRP(КРП)");
			akrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)");
			akrSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "KRP(КРП)");
			akrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)");
			akrHamma = akrKriti + akrPlatforma + akrPoluvagon + akrSisterna + akrBoshqa;

			model.addAttribute("akrHamma", akrHamma + 61);
			model.addAttribute("akrKriti", akrKriti);
			model.addAttribute("akrPlatforma", akrPlatforma);
			model.addAttribute("akrPoluvagon", akrPoluvagon + 61);
			model.addAttribute("akrSisterna", akrSisterna);
			model.addAttribute("akBoshqa", akrBoshqa);

			// samarqand KRP tamir hamma false vagonlar soni
			skrKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)");
			skrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "KRP(КРП)");
			skrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)");
			skrSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "KRP(КРП)");
			skrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)");
			skrHamma = skrKriti + skrPlatforma + skrPoluvagon + skrSisterna + skrBoshqa;

			model.addAttribute("skrHamma", skrHamma + 89);
			model.addAttribute("skrKriti", skrKriti);
			model.addAttribute("skrPlatforma", skrPlatforma);
			model.addAttribute("skrPoluvagon", skrPoluvagon + 88);
			model.addAttribute("skrSisterna", skrSisterna + 1);
			model.addAttribute("skrBoshqa", skrBoshqa);
// Krp itogo uchun
			uvtkrHamma = akrHamma + hkrHamma + skrHamma;
			uvtkrKriti = skrKriti + hkrKriti + akrKriti;
			uvtkrPlatforma = akrPlatforma + skrPlatforma + hkrPlatforma;
			uvtkrPoluvagon = akrPoluvagon + skrPoluvagon + hkrPoluvagon;
			uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
			uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

			model.addAttribute("uvtkrHamma", uvtkrHamma + 233);
			model.addAttribute("uvtkrKriti", uvtkrKriti);
			model.addAttribute("uvtkrPlatforma", uvtkrPlatforma);
			model.addAttribute("uvtkrPoluvagon", uvtkrPoluvagon + 232);
			model.addAttribute("uvtkrSisterna", uvtkrSisterna + 1);
			model.addAttribute("uvtkrBoshqa", uvtkrBoshqa);

//			ishlaydi avvalgi sonlari nomalumligi uchun commentfa olindi
//		}else if (country.equalsIgnoreCase("O'TY(ГАЖК)")) {
//			//**//
//			// VCHD-3 depo tamir hamma false vagonlar soni
//			hdKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)");
//			hdPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)");
//			hdPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)");
//			hdSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)");
//			hdBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)");
//			hdHamma = hdKriti + hdPlatforma + hdPoluvagon + hdSisterna + hdBoshqa;
//
//			model.addAttribute("hdHamma", hdHamma + 651);
//			model.addAttribute("hdKriti", hdKriti + 35);
//			model.addAttribute("hdPlatforma", hdPlatforma + 45);
//			model.addAttribute("hdPoluvagon", hdPoluvagon + 109);
//			model.addAttribute("hdSisterna", hdSisterna + 35);
//			model.addAttribute("hdBoshqa", hdBoshqa + 427);
//
//			// VCHD-5 depo tamir hamma false vagonlar soni
//			adKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)");
//			adPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)");
//			adPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)");
//			adSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)");
//			adBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)");
//			adHamma = adKriti + adPlatforma + adPoluvagon + adSisterna + adBoshqa;
//
//			model.addAttribute("adHamma", adHamma + 443);
//			model.addAttribute("adKriti", adKriti + 224);
//			model.addAttribute("adPlatforma", adPlatforma + 3);
//			model.addAttribute("adPoluvagon", adPoluvagon + 103);
//			model.addAttribute("adSisterna", adSisterna);
//			model.addAttribute("adBoshqa", adBoshqa + 113);
//
//			// samarqand depo tamir hamma false vagonlar soni
//			sdKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)");
//			sdPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)");
//			sdPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)");
//			sdSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)");
//			sdBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)");
//			sdHamma = sdKriti + sdPlatforma + sdPoluvagon + sdSisterna + sdBoshqa;
//
//			model.addAttribute("sdHamma", sdHamma + 393);
//			model.addAttribute("sdKriti", sdKriti + 135);
//			model.addAttribute("sdPlatforma", sdPlatforma + 8);
//			model.addAttribute("sdPoluvagon", sdPoluvagon + 67);
//			model.addAttribute("sdSisterna", sdSisterna + 23);
//			model.addAttribute("sdBoshqa", sdBoshqa + 160);
//
//			// depoli tamir itogo uchun
//			uvtdHamma = adHamma + hdHamma + sdHamma;
//			uvtdKriti = sdKriti + hdKriti + adKriti;
//			uvtdPlatforma = adPlatforma + sdPlatforma + hdPlatforma;
//			uvtdPoluvagon = adPoluvagon + sdPoluvagon + hdPoluvagon;
//			uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
//			uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;
//
//			model.addAttribute("uvtdHamma", uvtdHamma + 1487);
//			model.addAttribute("uvtdKriti", uvtdKriti + 394);
//			model.addAttribute("uvtdPlatforma", uvtdPlatforma + 56);
//			model.addAttribute("uvtdPoluvagon", uvtdPoluvagon + 279);
//			model.addAttribute("uvtdSisterna", uvtdSisterna + 58);
//			model.addAttribute("uvtdBoshqa", uvtdBoshqa + 700);
//
//			//Yolovchi Andijon fact
//			atYolovchi = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yo'lovchi vagon(пассжир)", "TO-3", "O'TY(ГАЖК)");
//			adYolovchi = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yo'lovchi vagon(пассжир)", "Depoli ta’mir(ДР)","O'TY(ГАЖК)");
//
//			model.addAttribute("atYolovchi", atYolovchi + 37);
//			model.addAttribute("adYolovchi", adYolovchi + 24);
//
//
//			// VCHD-3 kapital tamir hamma false vagonlar soni
//			hkKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)");
//			hkPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)");
//			hkPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)");
//			hkSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)");
//			hkBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)");
//			hkHamma = hkKriti + hkPlatforma + hkPoluvagon + hkSisterna + hkBoshqa;
//
//			model.addAttribute("hkHamma", hkHamma + 227);
//			model.addAttribute("hkKriti", hkKriti + 41);
//			model.addAttribute("hkPlatforma", hkPlatforma + 32);
//			model.addAttribute("hkPoluvagon", hkPoluvagon + 4);
//			model.addAttribute("hkSisterna", hkSisterna + 51);
//			model.addAttribute("hkBoshqa", hkBoshqa + 99);
//
//			// VCHD-5 kapital tamir hamma false vagonlar soni
//			akKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)");
//			akPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)");
//			akPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)");
//			akSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)");
//			akBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)");
//			akHamma = akKriti + akPlatforma + akPoluvagon + akSisterna + akBoshqa;
//
//			model.addAttribute("akHamma", akHamma + 28);
//			model.addAttribute("akKriti", akKriti + 26);
//			model.addAttribute("akPlatforma", akPlatforma);
//			model.addAttribute("akPoluvagon", akPoluvagon + 2);
//			model.addAttribute("akSisterna", akSisterna);
//			model.addAttribute("akBoshqa", akBoshqa);
//
//			// samarqand KApital tamir hamma false vagonlar soni
//			skKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)");
//			skPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)");
//			skPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)");
//			skSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)");
//			skBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)");
//			skHamma = skKriti + skPlatforma + skPoluvagon + skSisterna + skBoshqa;
//
//			model.addAttribute("skHamma", skHamma + 284);
//			model.addAttribute("skKriti", skKriti + 160);
//			model.addAttribute("skPlatforma", skPlatforma + 44);
//			model.addAttribute("skPoluvagon", skPoluvagon + 52);
//			model.addAttribute("skSisterna", skSisterna + 9);
//			model.addAttribute("skBoshqa", skBoshqa + 19);
//
//			// Kapital tamir itogo uchun
//			uvtkHamma = akHamma + hkHamma + skHamma;
//			uvtkKriti = skKriti + hkKriti + akKriti;
//			uvtkPlatforma = akPlatforma + skPlatforma + hkPlatforma;
//			uvtkPoluvagon = akPoluvagon + skPoluvagon + hkPoluvagon;
//			uvtkSisterna = akSisterna + hkSisterna + skSisterna;
//			uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;
//
//			model.addAttribute("uvtkHamma", uvtkHamma + 539);
//			model.addAttribute("uvtkKriti", uvtkKriti + 227);
//			model.addAttribute("uvtkPlatforma", uvtkPlatforma + 76);
//			model.addAttribute("uvtkPoluvagon", uvtkPoluvagon + 58);
//			model.addAttribute("uvtkSisterna", uvtkSisterna + 60);
//			model.addAttribute("uvtkBoshqa", uvtkBoshqa + 118);
//
//			//**
//
//			// VCHD-3 KRP tamir hamma false vagonlar soni
//			hkrKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", "O'TY(ГАЖК)");
//			hkrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Platforma(пф)", "KRP(КРП)", "O'TY(ГАЖК)");
//			hkrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", "O'TY(ГАЖК)");
//			hkrSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Sisterna(цс)", "KRP(КРП)", "O'TY(ГАЖК)");
//			hkrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", "O'TY(ГАЖК)");
//			hkrHamma = hkrKriti + hkrPlatforma + hkrPoluvagon + hkrSisterna + hkrBoshqa;
//
//			model.addAttribute("hkrHamma", hkrHamma + 83);
//			model.addAttribute("hkrKriti", hkrKriti + 83);
//			model.addAttribute("hkrPlatforma", hkrPlatforma);
//			model.addAttribute("hkrPoluvagon", hkrPoluvagon);
//			model.addAttribute("hkrSisterna", hkrSisterna);
//			model.addAttribute("hkrBoshqa", hkrBoshqa);
//
//			// VCHD-5 KRP tamir hamma false vagonlar soni
//			akrKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", "O'TY(ГАЖК)");
//			akrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Platforma(пф)", "KRP(КРП)", "O'TY(ГАЖК)");
//			akrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", "O'TY(ГАЖК)");
//			akrSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Sisterna(цс)", "KRP(КРП)", "O'TY(ГАЖК)");
//			akrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", "O'TY(ГАЖК)");
//			akrHamma = akrKriti + akrPlatforma + akrPoluvagon + akrSisterna + akrBoshqa;
//
//			model.addAttribute("akrHamma", akrHamma + 61);
//			model.addAttribute("akrKriti", akrKriti);
//			model.addAttribute("akrPlatforma", akrPlatforma);
//			model.addAttribute("akrPoluvagon", akrPoluvagon + 61);
//			model.addAttribute("akrSisterna", akrSisterna);
//			model.addAttribute("akBoshqa", akrBoshqa);
//
//			// samarqand KRP tamir hamma false vagonlar soni
//			skrKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", "O'TY(ГАЖК)");
//			skrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Platforma(пф)", "KRP(КРП)", "O'TY(ГАЖК)");
//			skrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", "O'TY(ГАЖК)");
//			skrSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Sisterna(цс)", "KRP(КРП)", "O'TY(ГАЖК)");
//			skrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", "O'TY(ГАЖК)");
//			skrHamma = skrKriti + skrPlatforma + skrPoluvagon + skrSisterna + skrBoshqa;
//
//			model.addAttribute("skrHamma", skrHamma + 89);
//			model.addAttribute("skrKriti", skrKriti);
//			model.addAttribute("skrPlatforma", skrPlatforma);
//			model.addAttribute("skrPoluvagon", skrPoluvagon + 88);
//			model.addAttribute("skrSisterna", skrSisterna + 1);
//			model.addAttribute("skrBoshqa", skrBoshqa);
//// Krp itogo uchun
//			uvtkrHamma = akrHamma + hkrHamma + skrHamma;
//			uvtkrKriti = skrKriti + hkrKriti + akrKriti;
//			uvtkrPlatforma = akrPlatforma + skrPlatforma + hkrPlatforma;
//			uvtkrPoluvagon = akrPoluvagon + skrPoluvagon + hkrPoluvagon;
//			uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
//			uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;
//
//			model.addAttribute("uvtkrHamma", uvtkrHamma + 233);
//			model.addAttribute("uvtkrKriti", uvtkrKriti);
//			model.addAttribute("uvtkrPlatforma", uvtkrPlatforma);
//			model.addAttribute("uvtkrPoluvagon", uvtkrPoluvagon + 232);
//			model.addAttribute("uvtkrSisterna", uvtkrSisterna + 1);
//			model.addAttribute("uvtkrBoshqa", uvtkrBoshqa);
//
//		}else if (country.equalsIgnoreCase("MDH(СНГ)")) {
//			//**//
//			// VCHD-3 depo tamir hamma false vagonlar soni
//			hdKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", "MDH(СНГ)");
//			hdPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", "MDH(СНГ)");
//			hdPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", "MDH(СНГ)");
//			hdSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", "MDH(СНГ)");
//			hdBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", "MDH(СНГ)");
//			hdHamma = hdKriti + hdPlatforma + hdPoluvagon + hdSisterna + hdBoshqa;
//
//			model.addAttribute("hdHamma", hdHamma + 651);
//			model.addAttribute("hdKriti", hdKriti + 35);
//			model.addAttribute("hdPlatforma", hdPlatforma + 45);
//			model.addAttribute("hdPoluvagon", hdPoluvagon + 109);
//			model.addAttribute("hdSisterna", hdSisterna + 35);
//			model.addAttribute("hdBoshqa", hdBoshqa + 427);
//
//			// VCHD-5 depo tamir hamma false vagonlar soni
//			adKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", "MDH(СНГ)");
//			adPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", "MDH(СНГ)");
//			adPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", "MDH(СНГ)");
//			adSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", "MDH(СНГ)");
//			adBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", "MDH(СНГ)");
//			adHamma = adKriti + adPlatforma + adPoluvagon + adSisterna + adBoshqa;
//
//			model.addAttribute("adHamma", adHamma + 443);
//			model.addAttribute("adKriti", adKriti + 224);
//			model.addAttribute("adPlatforma", adPlatforma + 3);
//			model.addAttribute("adPoluvagon", adPoluvagon + 103);
//			model.addAttribute("adSisterna", adSisterna);
//			model.addAttribute("adBoshqa", adBoshqa + 113);
//
//			// samarqand depo tamir hamma false vagonlar soni
//			sdKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", "MDH(СНГ)");
//			sdPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", "MDH(СНГ)");
//			sdPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", "MDH(СНГ)");
//			sdSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", "MDH(СНГ)");
//			sdBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", "MDH(СНГ)");
//			sdHamma = sdKriti + sdPlatforma + sdPoluvagon + sdSisterna + sdBoshqa;
//
//			model.addAttribute("sdHamma", sdHamma + 393);
//			model.addAttribute("sdKriti", sdKriti + 135);
//			model.addAttribute("sdPlatforma", sdPlatforma + 8);
//			model.addAttribute("sdPoluvagon", sdPoluvagon + 67);
//			model.addAttribute("sdSisterna", sdSisterna + 23);
//			model.addAttribute("sdBoshqa", sdBoshqa + 160);
//
//			// depoli tamir itogo uchun
//			uvtdHamma = adHamma + hdHamma + sdHamma;
//			uvtdKriti = sdKriti + hdKriti + adKriti;
//			uvtdPlatforma = adPlatforma + sdPlatforma + hdPlatforma;
//			uvtdPoluvagon = adPoluvagon + sdPoluvagon + hdPoluvagon;
//			uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
//			uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;
//
//			model.addAttribute("uvtdHamma", uvtdHamma + 1487);
//			model.addAttribute("uvtdKriti", uvtdKriti + 394);
//			model.addAttribute("uvtdPlatforma", uvtdPlatforma + 56);
//			model.addAttribute("uvtdPoluvagon", uvtdPoluvagon + 279);
//			model.addAttribute("uvtdSisterna", uvtdSisterna + 58);
//			model.addAttribute("uvtdBoshqa", uvtdBoshqa + 700);
//
//			//Yolovchi Andijon fact
//			atYolovchi = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yo'lovchi vagon(пассжир)", "TO-3", "MDH(СНГ)");
//			adYolovchi = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yo'lovchi vagon(пассжир)", "Depoli ta’mir(ДР)", "MDH(СНГ)");
//
//			model.addAttribute("atYolovchi", atYolovchi + 37);
//			model.addAttribute("adYolovchi", adYolovchi + 24);
//
//
//			// VCHD-3 kapital tamir hamma false vagonlar soni
//			hkKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", "MDH(СНГ)");
//			hkPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", "MDH(СНГ)");
//			hkPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", "MDH(СНГ)");
//			hkSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", "MDH(СНГ)");
//			hkBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", "MDH(СНГ)");
//			hkHamma = hkKriti + hkPlatforma + hkPoluvagon + hkSisterna + hkBoshqa;
//
//			model.addAttribute("hkHamma", hkHamma + 227);
//			model.addAttribute("hkKriti", hkKriti + 41);
//			model.addAttribute("hkPlatforma", hkPlatforma + 32);
//			model.addAttribute("hkPoluvagon", hkPoluvagon + 4);
//			model.addAttribute("hkSisterna", hkSisterna + 51);
//			model.addAttribute("hkBoshqa", hkBoshqa + 99);
//
//			// VCHD-5 kapital tamir hamma false vagonlar soni
//			akKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", "MDH(СНГ)");
//			akPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", "MDH(СНГ)");
//			akPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", "MDH(СНГ)");
//			akSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", "MDH(СНГ)");
//			akBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", "MDH(СНГ)");
//			akHamma = akKriti + akPlatforma + akPoluvagon + akSisterna + akBoshqa;
//
//			model.addAttribute("akHamma", akHamma + 28);
//			model.addAttribute("akKriti", akKriti + 26);
//			model.addAttribute("akPlatforma", akPlatforma);
//			model.addAttribute("akPoluvagon", akPoluvagon + 2);
//			model.addAttribute("akSisterna", akSisterna);
//			model.addAttribute("akBoshqa", akBoshqa);
//
//			// samarqand KApital tamir hamma false vagonlar soni
//			skKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", "MDH(СНГ)");
//			skPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", "MDH(СНГ)");
//			skPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", "MDH(СНГ)");
//			skSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", "MDH(СНГ)");
//			skBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", "MDH(СНГ)");
//			skHamma = skKriti + skPlatforma + skPoluvagon + skSisterna + skBoshqa;
//
//			model.addAttribute("skHamma", skHamma + 284);
//			model.addAttribute("skKriti", skKriti + 160);
//			model.addAttribute("skPlatforma", skPlatforma + 44);
//			model.addAttribute("skPoluvagon", skPoluvagon + 52);
//			model.addAttribute("skSisterna", skSisterna + 9);
//			model.addAttribute("skBoshqa", skBoshqa + 19);
//
//			// Kapital tamir itogo uchun
//			uvtkHamma = akHamma + hkHamma + skHamma;
//			uvtkKriti = skKriti + hkKriti + akKriti;
//			uvtkPlatforma = akPlatforma + skPlatforma + hkPlatforma;
//			uvtkPoluvagon = akPoluvagon + skPoluvagon + hkPoluvagon;
//			uvtkSisterna = akSisterna + hkSisterna + skSisterna;
//			uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;
//
//			model.addAttribute("uvtkHamma", uvtkHamma + 539);
//			model.addAttribute("uvtkKriti", uvtkKriti + 227);
//			model.addAttribute("uvtkPlatforma", uvtkPlatforma + 76);
//			model.addAttribute("uvtkPoluvagon", uvtkPoluvagon + 58);
//			model.addAttribute("uvtkSisterna", uvtkSisterna + 60);
//			model.addAttribute("uvtkBoshqa", uvtkBoshqa + 118);
//
//			//**
//
//			// VCHD-3 KRP tamir hamma false vagonlar soni
//			hkrKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", "MDH(СНГ)");
//			hkrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Platforma(пф)", "KRP(КРП)", "MDH(СНГ)");
//			hkrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", "MDH(СНГ)");
//			hkrSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Sisterna(цс)", "KRP(КРП)", "MDH(СНГ)");
//			hkrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", "MDH(СНГ)");
//			hkrHamma = hkrKriti + hkrPlatforma + hkrPoluvagon + hkrSisterna + hkrBoshqa;
//
//			model.addAttribute("hkrHamma", hkrHamma + 83);
//			model.addAttribute("hkrKriti", hkrKriti + 83);
//			model.addAttribute("hkrPlatforma", hkrPlatforma);
//			model.addAttribute("hkrPoluvagon", hkrPoluvagon);
//			model.addAttribute("hkrSisterna", hkrSisterna);
//			model.addAttribute("hkrBoshqa", hkrBoshqa);
//
//			// VCHD-5 KRP tamir hamma false vagonlar soni
//			akrKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", "MDH(СНГ)");
//			akrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Platforma(пф)", "KRP(КРП)", "MDH(СНГ)");
//			akrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", "MDH(СНГ)");
//			akrSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Sisterna(цс)", "KRP(КРП)", "MDH(СНГ)");
//			akrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", "MDH(СНГ)");
//			akrHamma = akrKriti + akrPlatforma + akrPoluvagon + akrSisterna + akrBoshqa;
//
//			model.addAttribute("akrHamma", akrHamma + 61);
//			model.addAttribute("akrKriti", akrKriti);
//			model.addAttribute("akrPlatforma", akrPlatforma);
//			model.addAttribute("akrPoluvagon", akrPoluvagon + 61);
//			model.addAttribute("akrSisterna", akrSisterna);
//			model.addAttribute("akBoshqa", akrBoshqa);
//
//			// samarqand KRP tamir hamma false vagonlar soni
//			skrKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", "MDH(СНГ)");
//			skrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Platforma(пф)", "KRP(КРП)", "MDH(СНГ)");
//			skrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", "MDH(СНГ)");
//			skrSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Sisterna(цс)", "KRP(КРП)", "MDH(СНГ)");
//			skrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", "MDH(СНГ)");
//			skrHamma = skrKriti + skrPlatforma + skrPoluvagon + skrSisterna + skrBoshqa;
//
//			model.addAttribute("skrHamma", skrHamma + 89);
//			model.addAttribute("skrKriti", skrKriti);
//			model.addAttribute("skrPlatforma", skrPlatforma);
//			model.addAttribute("skrPoluvagon", skrPoluvagon + 88);
//			model.addAttribute("skrSisterna", skrSisterna + 1);
//			model.addAttribute("skrBoshqa", skrBoshqa);
//// Krp itogo uchun
//			uvtkrHamma = akrHamma + hkrHamma + skrHamma;
//			uvtkrKriti = skrKriti + hkrKriti + akrKriti;
//			uvtkrPlatforma = akrPlatforma + skrPlatforma + hkrPlatforma;
//			uvtkrPoluvagon = akrPoluvagon + skrPoluvagon + hkrPoluvagon;
//			uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
//			uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;
//
//			model.addAttribute("uvtkrHamma", uvtkrHamma + 233);
//			model.addAttribute("uvtkrKriti", uvtkrKriti);
//			model.addAttribute("uvtkrPlatforma", uvtkrPlatforma);
//			model.addAttribute("uvtkrPoluvagon", uvtkrPoluvagon + 232);
//			model.addAttribute("uvtkrSisterna", uvtkrSisterna + 1);
//			model.addAttribute("uvtkrBoshqa", uvtkrBoshqa);
//
//		}else if (country.equalsIgnoreCase("Sanoat(ПРОМ)")) {
//			//**//
//			// VCHD-3 depo tamir hamma false vagonlar soni
//			hdKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)");
//			hdPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)");
//			hdPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)");
//			hdSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)");
//			hdBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)");
//			hdHamma = hdKriti + hdPlatforma + hdPoluvagon + hdSisterna + hdBoshqa;
//
//			model.addAttribute("hdHamma", hdHamma + 651);
//			model.addAttribute("hdKriti", hdKriti + 35);
//			model.addAttribute("hdPlatforma", hdPlatforma + 45);
//			model.addAttribute("hdPoluvagon", hdPoluvagon + 109);
//			model.addAttribute("hdSisterna", hdSisterna + 35);
//			model.addAttribute("hdBoshqa", hdBoshqa + 427);
//
//			// VCHD-5 depo tamir hamma false vagonlar soni
//			adKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)");
//			adPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)");
//			adPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)");
//			adSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)");
//			adBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)");
//			adHamma = adKriti + adPlatforma + adPoluvagon + adSisterna + adBoshqa;
//
//			model.addAttribute("adHamma", adHamma + 443);
//			model.addAttribute("adKriti", adKriti + 224);
//			model.addAttribute("adPlatforma", adPlatforma + 3);
//			model.addAttribute("adPoluvagon", adPoluvagon + 103);
//			model.addAttribute("adSisterna", adSisterna);
//			model.addAttribute("adBoshqa", adBoshqa + 113);
//
//			// samarqand depo tamir hamma false vagonlar soni
//			sdKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)");
//			sdPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)");
//			sdPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)");
//			sdSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)");
//			sdBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)");
//			sdHamma = sdKriti + sdPlatforma + sdPoluvagon + sdSisterna + sdBoshqa;
//
//			model.addAttribute("sdHamma", sdHamma + 393);
//			model.addAttribute("sdKriti", sdKriti + 135);
//			model.addAttribute("sdPlatforma", sdPlatforma + 8);
//			model.addAttribute("sdPoluvagon", sdPoluvagon + 67);
//			model.addAttribute("sdSisterna", sdSisterna + 23);
//			model.addAttribute("sdBoshqa", sdBoshqa + 160);
//
//			// depoli tamir itogo uchun
//			uvtdHamma = adHamma + hdHamma + sdHamma;
//			uvtdKriti = sdKriti + hdKriti + adKriti;
//			uvtdPlatforma = adPlatforma + sdPlatforma + hdPlatforma;
//			uvtdPoluvagon = adPoluvagon + sdPoluvagon + hdPoluvagon;
//			uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
//			uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;
//
//			model.addAttribute("uvtdHamma", uvtdHamma + 1487);
//			model.addAttribute("uvtdKriti", uvtdKriti + 394);
//			model.addAttribute("uvtdPlatforma", uvtdPlatforma + 56);
//			model.addAttribute("uvtdPoluvagon", uvtdPoluvagon + 279);
//			model.addAttribute("uvtdSisterna", uvtdSisterna + 58);
//			model.addAttribute("uvtdBoshqa", uvtdBoshqa + 700);
//
//			//Yolovchi Andijon fact
//			atYolovchi = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yo'lovchi vagon(пассжир)", "TO-3", "Sanoat(ПРОМ)");
//			adYolovchi = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yo'lovchi vagon(пассжир)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)");
//
//			model.addAttribute("atYolovchi", atYolovchi + 37);
//			model.addAttribute("adYolovchi", adYolovchi + 24);
//
//
//			// VCHD-3 kapital tamir hamma false vagonlar soni
//			hkKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)");
//			hkPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)");
//			hkPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)");
//			hkSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)");
//			hkBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)");
//			hkHamma = hkKriti + hkPlatforma + hkPoluvagon + hkSisterna + hkBoshqa;
//
//			model.addAttribute("hkHamma", hkHamma + 227);
//			model.addAttribute("hkKriti", hkKriti + 41);
//			model.addAttribute("hkPlatforma", hkPlatforma + 32);
//			model.addAttribute("hkPoluvagon", hkPoluvagon + 4);
//			model.addAttribute("hkSisterna", hkSisterna + 51);
//			model.addAttribute("hkBoshqa", hkBoshqa + 99);
//
//			// VCHD-5 kapital tamir hamma false vagonlar soni
//			akKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)");
//			akPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)");
//			akPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)");
//			akSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)");
//			akBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)");
//			akHamma = akKriti + akPlatforma + akPoluvagon + akSisterna + akBoshqa;
//
//			model.addAttribute("akHamma", akHamma + 28);
//			model.addAttribute("akKriti", akKriti + 26);
//			model.addAttribute("akPlatforma", akPlatforma);
//			model.addAttribute("akPoluvagon", akPoluvagon + 2);
//			model.addAttribute("akSisterna", akSisterna);
//			model.addAttribute("akBoshqa", akBoshqa);
//
//			// samarqand KApital tamir hamma false vagonlar soni
//			skKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)");
//			skPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)");
//			skPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)");
//			skSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)");
//			skBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)");
//			skHamma = skKriti + skPlatforma + skPoluvagon + skSisterna + skBoshqa;
//
//			model.addAttribute("skHamma", skHamma + 284);
//			model.addAttribute("skKriti", skKriti + 160);
//			model.addAttribute("skPlatforma", skPlatforma + 44);
//			model.addAttribute("skPoluvagon", skPoluvagon + 52);
//			model.addAttribute("skSisterna", skSisterna + 9);
//			model.addAttribute("skBoshqa", skBoshqa + 19);
//
//			// Kapital tamir itogo uchun
//			uvtkHamma = akHamma + hkHamma + skHamma;
//			uvtkKriti = skKriti + hkKriti + akKriti;
//			uvtkPlatforma = akPlatforma + skPlatforma + hkPlatforma;
//			uvtkPoluvagon = akPoluvagon + skPoluvagon + hkPoluvagon;
//			uvtkSisterna = akSisterna + hkSisterna + skSisterna;
//			uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;
//
//			model.addAttribute("uvtkHamma", uvtkHamma + 539);
//			model.addAttribute("uvtkKriti", uvtkKriti + 227);
//			model.addAttribute("uvtkPlatforma", uvtkPlatforma + 76);
//			model.addAttribute("uvtkPoluvagon", uvtkPoluvagon + 58);
//			model.addAttribute("uvtkSisterna", uvtkSisterna + 60);
//			model.addAttribute("uvtkBoshqa", uvtkBoshqa + 118);
//
//			//**
//
//			// VCHD-3 KRP tamir hamma false vagonlar soni
//			hkrKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", "Sanoat(ПРОМ)");
//			hkrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Platforma(пф)", "KRP(КРП)", "Sanoat(ПРОМ)");
//			hkrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", "Sanoat(ПРОМ)");
//			hkrSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Sisterna(цс)", "KRP(КРП)", "Sanoat(ПРОМ)");
//			hkrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", "Sanoat(ПРОМ)");
//			hkrHamma = hkrKriti + hkrPlatforma + hkrPoluvagon + hkrSisterna + hkrBoshqa;
//
//			model.addAttribute("hkrHamma", hkrHamma + 83);
//			model.addAttribute("hkrKriti", hkrKriti + 83);
//			model.addAttribute("hkrPlatforma", hkrPlatforma);
//			model.addAttribute("hkrPoluvagon", hkrPoluvagon);
//			model.addAttribute("hkrSisterna", hkrSisterna);
//			model.addAttribute("hkrBoshqa", hkrBoshqa);
//
//			// VCHD-5 KRP tamir hamma false vagonlar soni
//			akrKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", "Sanoat(ПРОМ)");
//			akrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Platforma(пф)", "KRP(КРП)", "Sanoat(ПРОМ)");
//			akrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", "Sanoat(ПРОМ)");
//			akrSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Sisterna(цс)", "KRP(КРП)", "Sanoat(ПРОМ)");
//			akrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", "Sanoat(ПРОМ)");
//			akrHamma = akrKriti + akrPlatforma + akrPoluvagon + akrSisterna + akrBoshqa;
//
//			model.addAttribute("akrHamma", akrHamma + 61);
//			model.addAttribute("akrKriti", akrKriti);
//			model.addAttribute("akrPlatforma", akrPlatforma);
//			model.addAttribute("akrPoluvagon", akrPoluvagon + 61);
//			model.addAttribute("akrSisterna", akrSisterna);
//			model.addAttribute("akBoshqa", akrBoshqa);
//
//			// samarqand KRP tamir hamma false vagonlar soni
//			skrKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", "Sanoat(ПРОМ)");
//			skrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Platforma(пф)", "KRP(КРП)", "Sanoat(ПРОМ)");
//			skrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", "Sanoat(ПРОМ)");
//			skrSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Sisterna(цс)", "KRP(КРП)", "Sanoat(ПРОМ)");
//			skrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", "Sanoat(ПРОМ)");
//			skrHamma = skrKriti + skrPlatforma + skrPoluvagon + skrSisterna + skrBoshqa;
//
//			model.addAttribute("skrHamma", skrHamma + 89);
//			model.addAttribute("skrKriti", skrKriti);
//			model.addAttribute("skrPlatforma", skrPlatforma);
//			model.addAttribute("skrPoluvagon", skrPoluvagon + 88);
//			model.addAttribute("skrSisterna", skrSisterna + 1);
//			model.addAttribute("skrBoshqa", skrBoshqa);
//// Krp itogo uchun
//			uvtkrHamma = akrHamma + hkrHamma + skrHamma;
//			uvtkrKriti = skrKriti + hkrKriti + akrKriti;
//			uvtkrPlatforma = akrPlatforma + skrPlatforma + hkrPlatforma;
//			uvtkrPoluvagon = akrPoluvagon + skrPoluvagon + hkrPoluvagon;
//			uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
//			uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;
//
//			model.addAttribute("uvtkrHamma", uvtkrHamma + 233);
//			model.addAttribute("uvtkrKriti", uvtkrKriti);
//			model.addAttribute("uvtkrPlatforma", uvtkrPlatforma);
//			model.addAttribute("uvtkrPoluvagon", uvtkrPoluvagon + 232);
//			model.addAttribute("uvtkrSisterna", uvtkrSisterna + 1);
//			model.addAttribute("uvtkrBoshqa", uvtkrBoshqa);
//		}

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
		vagonsToDownloadTable.add(uvtdHamma);
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
		vagonsToDownloadTable.add(uvtkHamma);
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
		vagonsToDownloadTable.add(uvtkrHamma);
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

		vagonsToDownloadTable.add(AndjToYolovchiPlan);
		vagonsToDownloadTable.add(atYolovchi);
		vagonsToDownloadTable.add(AndjDtYolovchiPlan);
		vagonsToDownloadTable.add(adYolovchi);

		vagonsToDownloadAllTable = vagonsToDownloadTable;

		return "planTableForMonths";
    }

}
