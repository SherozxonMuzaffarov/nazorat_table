package com.sms.serviceImp;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.sms.model.*;
import com.sms.model.MetrologiyaSkladModel;
import com.sms.model.MetrologiyaSkladModel;
import com.sms.model.MetrologiyaSkladModel;
import com.sms.repository.MetrologiyaSkladRepository;
import com.sms.service.MetrologiyaSkladService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MetrologiyaSkladServiceImpl implements MetrologiyaSkladService {
    @Autowired
    MetrologiyaSkladRepository skladRepository;

    @Override
    public void createPdf(List<MetrologiyaSkladModel> templatesToDownload, HttpServletResponse response) throws IOException {
        String home = System.getProperty("user.home");
        File file = new File(home + "/Downloads" + "/Ombordagi shablonlar.pdf");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        if (!file.exists())
            file.createNewFile();
        List<MetrologiyaSkladModel> allTemplates = templatesToDownload;
        try {
            response.setHeader("Content-Disposition",
                    "attachment;fileName=\"" + "Ombordagi shablonlar.pdf" +"\"");
            response.setContentType("application/pdf");


            PdfWriter writer = new PdfWriter(file.getAbsolutePath());
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);

            String FONT_FILENAME = "./src/main/resources/arial.ttf";
            PdfFont font = PdfFontFactory.createFont(FONT_FILENAME, PdfEncodings.IDENTITY_H);
            doc.setFont(font);

            Paragraph paragraph = new Paragraph("Depo ombordagi mavjud shablonlar");
            paragraph.setBackgroundColor(Color.DARK_GRAY);
            paragraph.setFontColor(Color.WHITE);// Setting background color to cell1
            paragraph.setBorder(Border.NO_BORDER);            // Setting border to cell1
            paragraph.setTextAlignment(TextAlignment.CENTER); // Setting text alignment to cell1
            paragraph.setFontSize(16);

            float[] columnWidth = {30f,200f,200f,200f,200f,200f,200f,200f,200f};
            Table table = new Table(columnWidth);
            table.setTextAlignment(TextAlignment.CENTER);
            table.addCell(new Cell().add("\n № "));
            table.addCell(new Cell().add("\n O'V va SU nomlari"));
            table.addCell(new Cell().add("\n Soni"));
            table.addCell(new Cell().add(" Ishlab chiqarilgan yili"));
            table.addCell(new Cell().add(" Zavodda ishlab chiqarilgan raqami"));
            table.addCell(new Cell().add("\n Turi"));
            table.addCell(new Cell().add("Bajaradigan ishi"));
            table.addCell(new Cell().add("\n VCHD"));
            table.addCell(new Cell().add("\n Izoh"));
            int i=0;
            for(MetrologiyaSkladModel template:allTemplates) {
                i++;
                table.addCell(new Cell().add(String.valueOf(i)));
                table.addCell(new Cell().add(template.getNomi()));
                table.addCell(new Cell().add(String.valueOf(template.getSoni())));
                table.addCell(new Cell().add(String.valueOf(template.getIshlabChiqarilganYili())));
                table.addCell(new Cell().add(template.getRaqami()));
                table.addCell(new Cell().add(template.getTuri()));
                table.addCell(new Cell().add(template.getIshi()));
                table.addCell(new Cell().add(template.getDepoNomi()));
                table.addCell(new Cell().add(template.getIzoh()));
            }

            doc.add(paragraph);
            doc.add(table);
            doc.close();
            FileInputStream in = new FileInputStream(file.getAbsoluteFile());
            FileCopyUtils.copy(in, response.getOutputStream());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<MetrologiyaSkladModel> findAll() {
        return skladRepository.findAll();
    }

    @Override
    public List<MetrologiyaSkladModel> findAllByDepoNomi(String depoNomi) {
        return skladRepository.findAllByDepoNomi(depoNomi);
    }

    @Override
    public MetrologiyaSkladModel saveTemplate(MetrologiyaSkladModel metrologiyaModel) {
        Optional<MetrologiyaSkladModel> exists = skladRepository.findById(metrologiyaModel.getId());
        if (exists.isPresent() ||  metrologiyaModel.getNomi().isEmpty())
            return new MetrologiyaSkladModel();
        MetrologiyaSkladModel savedShablon = new MetrologiyaSkladModel();
        savedShablon.setNomi(metrologiyaModel.getNomi());
        savedShablon.setSoni(metrologiyaModel.getSoni());
        savedShablon.setRaqami(metrologiyaModel.getRaqami());
        savedShablon.setIshlabChiqarilganYili(metrologiyaModel.getIshlabChiqarilganYili());
        savedShablon.setTuri(metrologiyaModel.getTuri());
        savedShablon.setIshi(metrologiyaModel.getIshi());
        savedShablon.setIzoh(metrologiyaModel.getIzoh());
        savedShablon.setDepoNomi(metrologiyaModel.getDepoNomi());

        return skladRepository.save(savedShablon);
    }

    @Override
    public MetrologiyaSkladModel saveTemplateSam(MetrologiyaSkladModel metrologiyaModel) {

        if(metrologiyaModel.getNomi().isEmpty())
            return new MetrologiyaSkladModel();
        Optional<MetrologiyaSkladModel> exists = skladRepository.findById(metrologiyaModel.getId());
        if (exists.isPresent() || !metrologiyaModel.getDepoNomi().equalsIgnoreCase("VCHD-6") || metrologiyaModel.getNomi().isEmpty())
            return new MetrologiyaSkladModel();

        MetrologiyaSkladModel savedShablon = new MetrologiyaSkladModel();
        savedShablon.setNomi(metrologiyaModel.getNomi());
        savedShablon.setSoni(metrologiyaModel.getSoni());
        savedShablon.setRaqami(metrologiyaModel.getRaqami());
        savedShablon.setIshlabChiqarilganYili(metrologiyaModel.getIshlabChiqarilganYili());
        savedShablon.setTuri(metrologiyaModel.getTuri());
        savedShablon.setIshi(metrologiyaModel.getIshi());
        savedShablon.setIzoh(metrologiyaModel.getIzoh());
        savedShablon.setDepoNomi(metrologiyaModel.getDepoNomi());

        return skladRepository.save(savedShablon);
    }

    @Override
    public MetrologiyaSkladModel saveTemplateHav(MetrologiyaSkladModel metrologiyaModel) {

        Optional<MetrologiyaSkladModel> exists = skladRepository.findById(metrologiyaModel.getId());
        if (exists.isPresent() || !metrologiyaModel.getDepoNomi().equalsIgnoreCase("VCHD-3") || metrologiyaModel.getNomi().isEmpty())
            return new MetrologiyaSkladModel();
        MetrologiyaSkladModel savedShablon = new MetrologiyaSkladModel();
        savedShablon.setNomi(metrologiyaModel.getNomi());
        savedShablon.setSoni(metrologiyaModel.getSoni());
        savedShablon.setRaqami(metrologiyaModel.getRaqami());
        savedShablon.setIshlabChiqarilganYili(metrologiyaModel.getIshlabChiqarilganYili());
        savedShablon.setTuri(metrologiyaModel.getTuri());
        savedShablon.setIshi(metrologiyaModel.getIshi());
        savedShablon.setIzoh(metrologiyaModel.getIzoh());
        savedShablon.setDepoNomi(metrologiyaModel.getDepoNomi());

        return skladRepository.save(savedShablon);
    }

    @Override
    public MetrologiyaSkladModel saveTemplateAndj(MetrologiyaSkladModel metrologiyaModel) {

        Optional<MetrologiyaSkladModel> exists = skladRepository.findById(metrologiyaModel.getId());
        if (exists.isPresent() || !metrologiyaModel.getDepoNomi().equalsIgnoreCase("VCHD-5") || metrologiyaModel.getNomi().isEmpty())
            return new MetrologiyaSkladModel();
        MetrologiyaSkladModel savedShablon = new MetrologiyaSkladModel();
        savedShablon.setNomi(metrologiyaModel.getNomi());
        savedShablon.setSoni(metrologiyaModel.getSoni());
        savedShablon.setRaqami(metrologiyaModel.getRaqami());
        savedShablon.setIshlabChiqarilganYili(metrologiyaModel.getIshlabChiqarilganYili());
        savedShablon.setTuri(metrologiyaModel.getTuri());
        savedShablon.setIshi(metrologiyaModel.getIshi());
        savedShablon.setIzoh(metrologiyaModel.getIzoh());
        savedShablon.setDepoNomi(metrologiyaModel.getDepoNomi());

        return skladRepository.save(savedShablon);
    }

    @Override
    public MetrologiyaSkladModel getShablonById(Integer id) {
        Optional<MetrologiyaSkladModel> optional = skladRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        return new MetrologiyaSkladModel();

    }

    @Override
    public void updateShablon(MetrologiyaSkladModel metrologiyaModel, Integer id) {
        Optional<MetrologiyaSkladModel> exists = skladRepository.findById(id);
        if (exists.isPresent() && !metrologiyaModel.getNomi().isEmpty()) {

            MetrologiyaSkladModel savedShablon = exists.get();
            savedShablon.setNomi(metrologiyaModel.getNomi());
            savedShablon.setSoni(metrologiyaModel.getSoni());
            savedShablon.setRaqami(metrologiyaModel.getRaqami());
            savedShablon.setIshlabChiqarilganYili(metrologiyaModel.getIshlabChiqarilganYili());
            savedShablon.setTuri(metrologiyaModel.getTuri());
            savedShablon.setIshi(metrologiyaModel.getIshi());
            savedShablon.setIzoh(metrologiyaModel.getIzoh());
            savedShablon.setDepoNomi(metrologiyaModel.getDepoNomi());
            skladRepository.save(savedShablon);
        }
    }

    @Override
    public void updateShablonSam(MetrologiyaSkladModel metrologiyaModel, Integer id) {
        Optional<MetrologiyaSkladModel> exists = skladRepository.findById(id);
        if (exists.isPresent() && !metrologiyaModel.getNomi().isEmpty()&& metrologiyaModel.getDepoNomi().equalsIgnoreCase("VCHD-6")
                && exists.get().getDepoNomi().equalsIgnoreCase("VCHD-6") ) {

            MetrologiyaSkladModel savedShablon = exists.get();
            savedShablon.setNomi(metrologiyaModel.getNomi());
            savedShablon.setSoni(metrologiyaModel.getSoni());
            savedShablon.setRaqami(metrologiyaModel.getRaqami());
            savedShablon.setIshlabChiqarilganYili(metrologiyaModel.getIshlabChiqarilganYili());
            savedShablon.setTuri(metrologiyaModel.getTuri());
            savedShablon.setIshi(metrologiyaModel.getIshi());
            savedShablon.setIzoh(metrologiyaModel.getIzoh());
            savedShablon.setDepoNomi(metrologiyaModel.getDepoNomi());
            skladRepository.save(savedShablon);
        }
    }

    @Override
    public void updateShablonHav(MetrologiyaSkladModel metrologiyaModel, Integer id) {
        Optional<MetrologiyaSkladModel> exists = skladRepository.findById(id);
        if (exists.isPresent() && !metrologiyaModel.getNomi().isEmpty()&& metrologiyaModel.getDepoNomi().equalsIgnoreCase("VCHD-3")
                && exists.get().getDepoNomi().equalsIgnoreCase("VCHD-3") ) {

            MetrologiyaSkladModel savedShablon = exists.get();
            savedShablon.setNomi(metrologiyaModel.getNomi());
            savedShablon.setSoni(metrologiyaModel.getSoni());
            savedShablon.setRaqami(metrologiyaModel.getRaqami());
            savedShablon.setIshlabChiqarilganYili(metrologiyaModel.getIshlabChiqarilganYili());
            savedShablon.setTuri(metrologiyaModel.getTuri());
            savedShablon.setIshi(metrologiyaModel.getIshi());
            savedShablon.setIzoh(metrologiyaModel.getIzoh());
            savedShablon.setDepoNomi(metrologiyaModel.getDepoNomi());
            skladRepository.save(savedShablon);
        }
    }

    @Override
    public void updateShablonAndj(MetrologiyaSkladModel metrologiyaModel, Integer id) {
        Optional<MetrologiyaSkladModel> exists = skladRepository.findById(id);
        if (exists.isPresent() && metrologiyaModel.getNomi().isEmpty() && metrologiyaModel.getDepoNomi().equalsIgnoreCase("VCHD-5")
                && exists.get().getDepoNomi().equalsIgnoreCase("VCHD-5") ) {

            MetrologiyaSkladModel savedShablon = exists.get();
            savedShablon.setNomi(metrologiyaModel.getNomi());
            savedShablon.setSoni(metrologiyaModel.getSoni());
            savedShablon.setRaqami(metrologiyaModel.getRaqami());
            savedShablon.setIshlabChiqarilganYili(metrologiyaModel.getIshlabChiqarilganYili());
            savedShablon.setTuri(metrologiyaModel.getTuri());
            savedShablon.setIshi(metrologiyaModel.getIshi());
            savedShablon.setIzoh(metrologiyaModel.getIzoh());
            savedShablon.setDepoNomi(metrologiyaModel.getDepoNomi());
            skladRepository.save(savedShablon);
        }
    }


    @Override
    public void deleteTemplateById(Integer id) {
        skladRepository.deleteById(id);

    }

    @Override
    public void deleteTemplateSam(Integer id) {
        Optional<MetrologiyaSkladModel> optional = skladRepository.findById(id);
        if (optional.isPresent() && optional.get().getDepoNomi().equals("VCHD-6"))
            skladRepository.deleteById(id);
    }

    @Override
    public void deleteTemplateHav(Integer id) {
        Optional<MetrologiyaSkladModel> optional = skladRepository.findById(id);
        if (optional.isPresent() && optional.get().getDepoNomi().equals("VCHD-3"))
            skladRepository.deleteById(id);
    }

    @Override
    public void deleteTemplateAndj(Integer id) {
        Optional<MetrologiyaSkladModel> optional = skladRepository.findById(id);
        if (optional.isPresent() && optional.get().getDepoNomi().equals("VCHD-5"))
            skladRepository.deleteById(id);
    }

}
