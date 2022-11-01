package com.sms.controller;

import com.sms.model.MetrologiyaModel;
import com.sms.model.MetrologiyaSkladModel;
import com.sms.service.MetrologiyaService;
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
public class MetrologiyaController {
    @Autowired
    MetrologiyaService metrologiyaService;

    LocalDate today = LocalDate.now();
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    Date currentDate  = Date.valueOf(today.format(myFormatObj));

    //Yuklab olish uchun Malumot yigib beradi
    List<MetrologiyaModel> templatesToDownload  = new ArrayList<>();

    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
    @GetMapping("/metrologiya/createExcel")
    public void pdfFile(HttpServletResponse response) throws IOException {
        metrologiyaService.createPdf(templatesToDownload,response);
    }

    // hammasini olish
    @PreAuthorize(value = "hasAnyRole('DIRECTOR','SAMMETROLOGIYA','HAVMETROLOGIYA','ANDJMETROLOGIYA')")
    @GetMapping("/metrologiya")
    public String listVagon(Model model, HttpServletRequest request ) {

        if (request.isUserInRole("DIRECTOR")) {
            model.addAttribute("templates", metrologiyaService.findAll());
            templatesToDownload = metrologiyaService.findAll();
        }else if (request.isUserInRole("SAMMETROLOGIYA")) {
            model.addAttribute("templates", metrologiyaService.findAllByDepoNomi("VCHD-6"));
            templatesToDownload = metrologiyaService.findAllByDepoNomi("VCHD-6");
        }else if (request.isUserInRole("HAVMETROLOGIYA")) {
            model.addAttribute("templates", metrologiyaService.findAllByDepoNomi("VCHD-3"));
            templatesToDownload = metrologiyaService.findAllByDepoNomi("VCHD-3");
        }else if (request.isUserInRole("ANDJMETROLOGIYA")) {
            model.addAttribute("templates", metrologiyaService.findAllByDepoNomi("VCHD-5"));
            templatesToDownload = metrologiyaService.findAllByDepoNomi("VCHD-5");
        }

        if (request.isUserInRole("DIRECTOR")) {
            model.addAttribute("isAdmin", true);
            model.addAttribute("currentDate", currentDate);
        }else {
            model.addAttribute("isAdmin", false);
            model.addAttribute("currentDate", currentDate);
        }

        return "metrologiya";
    }

    //template qoshish uchun oyna
    @PreAuthorize(value = "hasAnyRole('DIRECTOR',  'SAMMETROLOGIYA','HAVMETROLOGIYA','ANDJMETROLOGIYA')")
    @GetMapping("/metrologiya/newTemplate")
    public String createForm(Model model) {
        MetrologiyaModel metrologiyaModel = new MetrologiyaModel();
        model.addAttribute("metrologiyaModel", metrologiyaModel);
        return "create_metrologiya";
    }

    //template qoshish
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAMMETROLOGIYA','HAVMETROLOGIYA','ANDJMETROLOGIYA')")
    @PostMapping("/metrologiya/template")
    public String saveTemplate(@ModelAttribute("metrologiyaModel") MetrologiyaModel metrologiyaModel, HttpServletRequest request ) {
        if (request.isUserInRole("DIRECTOR")) {
            metrologiyaService.saveTemplate(metrologiyaModel);
        }else if (request.isUserInRole("SAMMETROLOGIYA")) {
            metrologiyaService.saveTemplateSam(metrologiyaModel);
        }else if (request.isUserInRole("HAVMETROLOGIYA")) {
            metrologiyaService.saveTemplateHav(metrologiyaModel);
        }else if (request.isUserInRole("ANDJMETROLOGIYA")) {
            metrologiyaService.saveTemplateAndj(metrologiyaModel);
        }
        return "redirect:/metrologiya";
    }

    //tahrirlash uchun oyna
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAMMETROLOGIYA','HAVMETROLOGIYA','ANDJMETROLOGIYA')")
    @GetMapping("/metrologiya/editTemplate/{id}")
    public String editVagonForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("metrologiyaModel",metrologiyaService.getShablonById(id));
        return "edit_metrologiya";
    }

    //tahrirni saqlash
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAMMETROLOGIYA','HAVMETROLOGIYA','ANDJMETROLOGIYA')")
    @PostMapping("/metrologiya/updateTemplate/{id}")
    public String updateVagon(@ModelAttribute("vagon") MetrologiyaModel metrologiyaModel,@PathVariable Integer id,Model model, HttpServletRequest request) throws ChangeSetPersister.NotFoundException {
        if (request.isUserInRole("DIRECTOR")) {
            metrologiyaService.updateShablon(metrologiyaModel, id);
        }else if (request.isUserInRole("SAMMETROLOGIYA")) {
            metrologiyaService.updateShablonSam(metrologiyaModel, id);
        }else if (request.isUserInRole("HAVMETROLOGIYA")) {
            metrologiyaService.updateShablonHav(metrologiyaModel, id);
        }else if (request.isUserInRole("ANDJMETROLOGIYA")) {
            metrologiyaService.updateShablonAndj(metrologiyaModel, id);
        }
        return "redirect:/metrologiya";
    }

    //bazadan o'chirish
    @PreAuthorize(value = "hasAnyRole('DIRECTOR',  'SAMMETROLOGIYA','HAVMETROLOGIYA','ANDJMETROLOGIYA')")
    @GetMapping("/metrologiya/deleteTemplate/{id}")
    public String deleteVagonForm(@PathVariable("id") Integer id, HttpServletRequest request ) {
        if (request.isUserInRole("DIRECTOR")) {
            metrologiyaService.deleteTemplateById(id);
        }else if (request.isUserInRole("SAMMETROLOGIYA")) {
            metrologiyaService.deleteTemplateSam(id);
        }else if (request.isUserInRole("HAVMETROLOGIYA")) {
            metrologiyaService.deleteTemplateHav(id);
        }else if (request.isUserInRole("ANDJMETROLOGIYA")) {
            metrologiyaService.deleteTemplateAndj(id);
        }
        return "redirect:/metrologiya";
    }
    //Filter qilish
    @PreAuthorize(value = "hasAnyRole('DIRECTOR',  'SAMMETROLOGIYA','HAVMETROLOGIYA','ANDJMETROLOGIYA')")
    @GetMapping("/metrologiya/filterTemplate")
    public String filterByDate(Model model, HttpServletRequest request,  @RequestParam(value = "depoNomi") String depoNomi ) {

        if (request.isUserInRole("DIRECTOR")) {
            if (!depoNomi.equalsIgnoreCase("Hammasi")) {
                model.addAttribute("templates", metrologiyaService.findAllByDepoNomi(depoNomi));
                templatesToDownload = metrologiyaService.findAllByDepoNomi(depoNomi);
            } else {
                model.addAttribute("templates", metrologiyaService.findAll());
                templatesToDownload = metrologiyaService.findAll();
            }
            model.addAttribute("isAdmin", true);
            model.addAttribute("currentDate", currentDate);
        }else {
            model.addAttribute("isAdmin", false);
            model.addAttribute("currentDate", currentDate);
        }

        return "metrologiya";
    }
}
