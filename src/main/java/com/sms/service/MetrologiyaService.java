package com.sms.service;

import com.sms.model.MetrologiyaModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface MetrologiyaService {

    List<MetrologiyaModel> findAll();

    List<MetrologiyaModel> findAllByDepoNomi(String depoNomi);

    MetrologiyaModel saveTemplate(MetrologiyaModel metrologiyaModel);

    MetrologiyaModel saveTemplateSam(MetrologiyaModel metrologiyaModel);

    MetrologiyaModel saveTemplateHav(MetrologiyaModel metrologiyaModel);

    MetrologiyaModel saveTemplateAndj(MetrologiyaModel metrologiyaModel);

    void deleteTemplateById(Integer id);

    void deleteTemplateSam(Integer id);

    void deleteTemplateHav(Integer id);

    void deleteTemplateAndj(Integer id);

    MetrologiyaModel getShablonById(Integer id);

    void updateShablon(MetrologiyaModel metrologiyaModel, Integer id);

    void updateShablonSam(MetrologiyaModel metrologiyaModel, Integer id);

    void updateShablonHav(MetrologiyaModel metrologiyaModel, Integer id);

    void updateShablonAndj(MetrologiyaModel metrologiyaModel, Integer id);

    void createPdf(List<MetrologiyaModel> templatesToDownload, HttpServletResponse response) throws IOException;
}
