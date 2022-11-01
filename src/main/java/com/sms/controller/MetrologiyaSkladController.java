package com.sms.controller;

import com.sms.model.MetrologiyaSkladModel;
import com.sms.service.MetrologiyaSkladService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MetrologiyaSkladController {
    @Autowired
    MetrologiyaSkladService skladService;

    LocalDate today = LocalDate.now();
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    Date currentDate  = Date.valueOf(today.format(myFormatObj));

    //Yuklab olish uchun Malumot yigib beradi
    List<MetrologiyaSkladModel> templatesToDownload  = new ArrayList<>();

    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
    @GetMapping("/metrologiyaSklad/createExcel")
    public void pdfFile(HttpServletResponse response) throws IOException {
        skladService.createPdf(templatesToDownload,response);
    }

    // hammasini olish
    @PreAuthorize(value = "hasAnyRole('DIRECTOR','SAMMETROLOGIYA','HAVMETROLOGIYA','ANDJMETROLOGIYA')")
    @GetMapping("/metrologiyaSklad")
    public String listVagon(Model model, HttpServletRequest request ) {

        if (request.isUserInRole("DIRECTOR")) {
            model.addAttribute("templates", skladService.findAll());
            templatesToDownload = skladService.findAll();
        }else if (request.isUserInRole("SAMMETROLOGIYA")) {
            model.addAttribute("templates", skladService.findAllByDepoNomi("VCHD-6"));
            templatesToDownload = skladService.findAllByDepoNomi("VCHD-6");
        }else if (request.isUserInRole("HAVMETROLOGIYA")) {
            model.addAttribute("templates", skladService.findAllByDepoNomi("VCHD-3"));
            templatesToDownload = skladService.findAllByDepoNomi("VCHD-3");
        }else if (request.isUserInRole("ANDJMETROLOGIYA")) {
            model.addAttribute("templates", skladService.findAllByDepoNomi("VCHD-5"));
            templatesToDownload = skladService.findAllByDepoNomi("VCHD-5");
        }

        if (request.isUserInRole("DIRECTOR")) {
            model.addAttribute("isAdmin", true);
            model.addAttribute("currentDate", currentDate);
        }else {
            model.addAttribute("isAdmin", false);
            model.addAttribute("currentDate", currentDate);
        }
        return "metrologiya_sklad";
    }


    //template qoshish uchun oyna
    @PreAuthorize(value = "hasAnyRole('DIRECTOR',  'SAMMETROLOGIYA','HAVMETROLOGIYA','ANDJMETROLOGIYA')")
    @GetMapping("/metrologiyaSklad/newTemplate")
    public String createForm(Model model) {
        MetrologiyaSkladModel skladModel = new MetrologiyaSkladModel();
        model.addAttribute("skladModel", skladModel);
        return "create_metrologiya_sklad";
    }

    //template qoshish
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAMMETROLOGIYA','HAVMETROLOGIYA','ANDJMETROLOGIYA')")
    @PostMapping("/metrologiyaSklad/template")
    public String saveTemplate(@ModelAttribute("metrologiyaModel") MetrologiyaSkladModel skladModel, HttpServletRequest request ) {
        if (request.isUserInRole("DIRECTOR")) {
            skladService.saveTemplate(skladModel);
        }else if (request.isUserInRole("SAMMETROLOGIYA")) {
            skladService.saveTemplateSam(skladModel);
        }else if (request.isUserInRole("HAVMETROLOGIYA")) {
            skladService.saveTemplateHav(skladModel);
        }else if (request.isUserInRole("ANDJMETROLOGIYA")) {
            skladService.saveTemplateAndj(skladModel);
        }
        return "redirect:/metrologiyaSklad";
    }

    //tahrirlash uchun oyna
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAMMETROLOGIYA','HAVMETROLOGIYA','ANDJMETROLOGIYA')")
    @GetMapping("/metrologiyaSklad/editTemplate/{id}")
    public String editVagonForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("skladModel",skladService.getShablonById(id));
        return "edit_metrologiya_sklad";
    }

    //tahrirni saqlash
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAMMETROLOGIYA','HAVMETROLOGIYA','ANDJMETROLOGIYA')")
    @PostMapping("/metrologiyaSklad/updateTemplate/{id}")
    public String updateVagon(@ModelAttribute("vagon") MetrologiyaSkladModel metrologiyaModel,@PathVariable Integer id,Model model, HttpServletRequest request) throws ChangeSetPersister.NotFoundException {
        if (request.isUserInRole("DIRECTOR")) {
            skladService.updateShablon(metrologiyaModel, id);
        }else if (request.isUserInRole("SAMMETROLOGIYA")) {
            skladService.updateShablonSam(metrologiyaModel, id);
        }else if (request.isUserInRole("HAVMETROLOGIYA")) {
            skladService.updateShablonHav(metrologiyaModel, id);
        }else if (request.isUserInRole("ANDJMETROLOGIYA")) {
            skladService.updateShablonAndj(metrologiyaModel, id);
        }
        return "redirect:/metrologiyaSklad";
    }


    //bazadan o'chirish
    @PreAuthorize(value = "hasAnyRole('DIRECTOR',  'SAMMETROLOGIYA','HAVMETROLOGIYA','ANDJMETROLOGIYA')")
    @GetMapping("/metrologiyaSklad/deleteTemplate/{id}")
    public String deleteVagonForm(@PathVariable("id") Integer id, HttpServletRequest request ) {
        if (request.isUserInRole("DIRECTOR")) {
            skladService.deleteTemplateById(id);
        }else if (request.isUserInRole("SAMMETROLOGIYA")) {
            skladService.deleteTemplateSam(id);
        }else if (request.isUserInRole("HAVMETROLOGIYA")) {
            skladService.deleteTemplateHav(id);
        }else if (request.isUserInRole("ANDJMETROLOGIYA")) {
            skladService.deleteTemplateAndj(id);
        }
        return "redirect:/metrologiyaSklad";
    }
    //Filter qilish
    @PreAuthorize(value = "hasAnyRole('DIRECTOR',  'SAMMETROLOGIYA','HAVMETROLOGIYA','ANDJMETROLOGIYA')")
    @GetMapping("/metrologiyaSklad/filterTemplate")
    public String filterByDate(Model model, HttpServletRequest request,  @RequestParam(value = "depoNomi") String depoNomi ) {

        if (request.isUserInRole("DIRECTOR")) {
            if (!depoNomi.equalsIgnoreCase("Hammasi")) {
                model.addAttribute("templates", skladService.findAllByDepoNomi(depoNomi));
                templatesToDownload = skladService.findAllByDepoNomi(depoNomi);
            } else {
                model.addAttribute("templates", skladService.findAll());
                templatesToDownload = skladService.findAll();
            }
            model.addAttribute("isAdmin", true);
            model.addAttribute("currentDate", currentDate);
        }else {
            model.addAttribute("isAdmin", false);
            model.addAttribute("currentDate", currentDate);
        }

        return "metrologiya_sklad";
    }

}
