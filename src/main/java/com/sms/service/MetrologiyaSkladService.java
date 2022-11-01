package com.sms.service;

import com.sms.model.MetrologiyaModel;
import com.sms.model.MetrologiyaSkladModel;
import com.sms.model.MetrologiyaSkladModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface MetrologiyaSkladService {

    List<MetrologiyaSkladModel> findAll();

    List<MetrologiyaSkladModel> findAllByDepoNomi(String depoNomi);
    
    MetrologiyaSkladModel saveTemplate(MetrologiyaSkladModel metrologiyaModel);

    MetrologiyaSkladModel saveTemplateSam(MetrologiyaSkladModel metrologiyaModel);

    MetrologiyaSkladModel saveTemplateHav(MetrologiyaSkladModel metrologiyaModel);

    MetrologiyaSkladModel saveTemplateAndj(MetrologiyaSkladModel metrologiyaModel);

    MetrologiyaSkladModel getShablonById(Integer id);

    void updateShablon(MetrologiyaSkladModel metrologiyaModel, Integer id);

    void updateShablonSam(MetrologiyaSkladModel metrologiyaModel, Integer id);

    void updateShablonHav(MetrologiyaSkladModel metrologiyaModel, Integer id);

    void updateShablonAndj(MetrologiyaSkladModel metrologiyaModel, Integer id);

    void deleteTemplateById(Integer id);

    void deleteTemplateSam(Integer id);

    void deleteTemplateHav(Integer id);

    void deleteTemplateAndj(Integer id);

    void createPdf(List<MetrologiyaSkladModel> templatesToDownload, HttpServletResponse response) throws IOException;
}
