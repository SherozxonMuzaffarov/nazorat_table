package com.sms.serviceImp;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.sms.model.MetrologiyaModel;
import com.sms.model.MetrologiyaSkladModel;
import com.sms.repository.MetrologiyaRepository;
import com.sms.service.MetrologiyaService;
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
public class MetrologiyaServiceImp implements MetrologiyaService {
    @Autowired
    MetrologiyaRepository metrologiyaRepository;

    @Override
    public void createPdf(List<MetrologiyaModel> templatesToDownload, HttpServletResponse response) throws IOException {
        String home = System.getProperty("user.home");
        File file = new File(home + "/Downloads" + "/Ta'mirlash sexlardagi shablonlar.pdf");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        if (!file.exists())
            file.createNewFile();
        List<MetrologiyaModel> allTemplates = templatesToDownload;
        try {
            response.setHeader("Content-Disposition",
                    "attachment;fileName=\"" + "Ta'mirlash sexlardagi shablonlar.pdf" +"\"");
            response.setContentType("application/pdf");


            PdfWriter writer = new PdfWriter(file.getAbsolutePath());
            PdfDocument pdfDoc = new PdfDocument(writer);
            pdfDoc.setDefaultPageSize(PageSize.A4.rotate());
            Document doc = new Document(pdfDoc);

            String FONT_FILENAME = "./src/main/resources/arial.ttf";
            PdfFont font = PdfFontFactory.createFont(FONT_FILENAME, PdfEncodings.IDENTITY_H);
            doc.setFont(font);

            Paragraph paragraph = new Paragraph("Ta'mirlash sexlardagi mavjud shablonlar");
            paragraph.setBackgroundColor(Color.DARK_GRAY);
            paragraph.setFontColor(Color.WHITE);// Setting background color to cell1
            paragraph.setBorder(Border.NO_BORDER);            // Setting border to cell1
            paragraph.setTextAlignment(TextAlignment.CENTER); // Setting text alignment to cell1
            paragraph.setFontSize(16);

            float[] columnWidth = {20f,100f,100f,100f,100f,100f,100f,100f,100f,100f,100f,100f, 100f};
            Table table = new Table(columnWidth);
            table.setTextAlignment(TextAlignment.CENTER);
            table.setFontSize(10);
            table.addCell(new Cell().add("\n № "));
            table.addCell(new Cell().add("\n O'V va SU nomlari"));
            table.addCell(new Cell().add("\n Soni"));
            table.addCell(new Cell().add("\n Ishlab chiqarilgan yili"));
            table.addCell(new Cell().add("\n Zavodda ishlab chiqarilgan raqami"));
            table.addCell(new Cell().add("\n Turi"));
            table.addCell(new Cell().add("\n Bajaradigan ishi"));
            table.addCell(new Cell().add("\n O'V/SU ning saqlanish joyi"));

//            Qiyoslash / kalibrlash yili
            float [] pointColumnWidths1 = {100f};
            Table nestedTable1 = new Table(pointColumnWidths1);
            nestedTable1.addCell(new Cell().add("Qiyoslash / kalibrlash yili"));

            float [] pointColumnWidths2 = {25f, 25f, 25f, 25f};
            Table nestedTable2 = new Table(pointColumnWidths2);
            nestedTable2.addCell(new Cell().add("Sertifikatsiyalash, qiyoslash, kalibrlash raqami va sanasi"));
            nestedTable2.addCell(new Cell().add("Sertifikat kim tomonidan berilgan"));
            nestedTable2.addCell(new Cell().add("Sarflangan mablag'"));
            nestedTable2.addCell(new Cell().add("Keyingi qiyoslash va kalibrlash sanasi"));

            nestedTable1.addCell(new Cell().add(nestedTable2));

            table.addCell(new Cell().add(nestedTable1));
            table.addCell(new Cell().add("\n Qiyoslash va kalibrlashning davriyligi"));
            table.addCell(new Cell().add("\n Tuzilgan shartnomaning raqami va sanasi"));
            table.addCell(new Cell().add("\n VCHD"));
            table.addCell(new Cell().add("\n Izoh"));
            int i=0;
            for(MetrologiyaModel template:allTemplates) {
                i++;
                table.addCell(new Cell().add(String.valueOf(i)));
                table.addCell(new Cell().add(template.getNomi()));
                table.addCell(new Cell().add(String.valueOf(template.getSoni())));
                table.addCell(new Cell().add(String.valueOf(template.getIshlabChiqarilganYili())));
                table.addCell(new Cell().add(template.getRaqami()));
                table.addCell(new Cell().add(template.getTuri()));
                table.addCell(new Cell().add(template.getIshi()));
                table.addCell(new Cell().add(template.getSaqlanishJoyi()));

                float [] pointColumnWidths3 = {25f, 25f, 25f, 25f, };
                Table nestedTable3 = new Table(pointColumnWidths3);
                nestedTable3.addCell(new Cell().add(template.getSerRaqamiSanasi()));
                nestedTable3.addCell(new Cell().add(template.getSerBerganKorxona()));
                nestedTable3.addCell(new Cell().add(template.getSarflanganMablag()));
                nestedTable3.addCell(new Cell().add(String.valueOf(template.getSerKeyingiSanasi())));

                table.addCell(new Cell().add(nestedTable3));
                table.addCell(new Cell().add(String.valueOf(template.getSerDavriyligi())));
                table.addCell(new Cell().add(String.valueOf(template.getShartnomaRaqamiSanasi())));
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
    public List<MetrologiyaModel> findAll() {
        return metrologiyaRepository.findAll();
    }

    @Override
    public List<MetrologiyaModel> findAllByDepoNomi(String depoNomi) {
        return metrologiyaRepository.findAllByDepoNomi(depoNomi);
    }

    @Override
    public MetrologiyaModel saveTemplate(MetrologiyaModel metrologiyaModel) {
        Optional<MetrologiyaModel> exists = metrologiyaRepository.findById(metrologiyaModel.getId());
        if (exists.isPresent() ||  metrologiyaModel.getNomi().isEmpty())
            return new MetrologiyaModel();
        MetrologiyaModel savedShablon = new MetrologiyaModel();
        savedShablon.setNomi(metrologiyaModel.getNomi());
        savedShablon.setSoni(metrologiyaModel.getSoni());
        savedShablon.setRaqami(metrologiyaModel.getRaqami());
        savedShablon.setIshlabChiqarilganYili(metrologiyaModel.getIshlabChiqarilganYili());
        savedShablon.setTuri(metrologiyaModel.getTuri());
        savedShablon.setIshi(metrologiyaModel.getIshi());
        savedShablon.setSaqlanishJoyi(metrologiyaModel.getSaqlanishJoyi());
        savedShablon.setSerRaqamiSanasi(metrologiyaModel.getSerRaqamiSanasi());
        savedShablon.setSerBerganKorxona(metrologiyaModel.getSerBerganKorxona());
        savedShablon.setSarflanganMablag(metrologiyaModel.getSarflanganMablag());
        savedShablon.setSerKeyingiSanasi(metrologiyaModel.getSerKeyingiSanasi());
        savedShablon.setSerDavriyligi(metrologiyaModel.getSerDavriyligi());
        savedShablon.setShartnomaRaqamiSanasi(metrologiyaModel.getShartnomaRaqamiSanasi());
        savedShablon.setIzoh(metrologiyaModel.getIzoh());
        savedShablon.setDepoNomi(metrologiyaModel.getDepoNomi());

        return metrologiyaRepository.save(savedShablon);
    }

    @Override
    public MetrologiyaModel saveTemplateSam(MetrologiyaModel metrologiyaModel) {

        if(metrologiyaModel.getNomi().isEmpty())
            return new MetrologiyaModel();
        Optional<MetrologiyaModel> exists = metrologiyaRepository.findById(metrologiyaModel.getId());
        if (exists.isPresent() || !metrologiyaModel.getDepoNomi().equalsIgnoreCase("VCHD-6") || metrologiyaModel.getNomi().isEmpty())
            return new MetrologiyaModel();

        MetrologiyaModel savedShablon = new MetrologiyaModel();
        savedShablon.setNomi(metrologiyaModel.getNomi());
        savedShablon.setSoni(metrologiyaModel.getSoni());
        savedShablon.setRaqami(metrologiyaModel.getRaqami());
        savedShablon.setIshlabChiqarilganYili(metrologiyaModel.getIshlabChiqarilganYili());
        savedShablon.setTuri(metrologiyaModel.getTuri());
        savedShablon.setIshi(metrologiyaModel.getIshi());
        savedShablon.setSaqlanishJoyi(metrologiyaModel.getSaqlanishJoyi());
        savedShablon.setSerRaqamiSanasi(metrologiyaModel.getSerRaqamiSanasi());
        savedShablon.setSerBerganKorxona(metrologiyaModel.getSerBerganKorxona());
        savedShablon.setSarflanganMablag(metrologiyaModel.getSarflanganMablag());
        savedShablon.setSerKeyingiSanasi(metrologiyaModel.getSerKeyingiSanasi());
        savedShablon.setSerDavriyligi(metrologiyaModel.getSerDavriyligi());
        savedShablon.setShartnomaRaqamiSanasi(metrologiyaModel.getShartnomaRaqamiSanasi());
        savedShablon.setIzoh(metrologiyaModel.getIzoh());
        savedShablon.setDepoNomi(metrologiyaModel.getDepoNomi());

        return metrologiyaRepository.save(savedShablon);
    }

    @Override
    public MetrologiyaModel saveTemplateHav(MetrologiyaModel metrologiyaModel) {

        Optional<MetrologiyaModel> exists = metrologiyaRepository.findById(metrologiyaModel.getId());
        if (exists.isPresent() || !metrologiyaModel.getDepoNomi().equalsIgnoreCase("VCHD-3") || metrologiyaModel.getNomi().isEmpty())
            return new MetrologiyaModel();
        MetrologiyaModel savedShablon = new MetrologiyaModel();
        savedShablon.setNomi(metrologiyaModel.getNomi());
        savedShablon.setSoni(metrologiyaModel.getSoni());
        savedShablon.setRaqami(metrologiyaModel.getRaqami());
        savedShablon.setIshlabChiqarilganYili(metrologiyaModel.getIshlabChiqarilganYili());
        savedShablon.setTuri(metrologiyaModel.getTuri());
        savedShablon.setIshi(metrologiyaModel.getIshi());
        savedShablon.setSaqlanishJoyi(metrologiyaModel.getSaqlanishJoyi());
        savedShablon.setSerRaqamiSanasi(metrologiyaModel.getSerRaqamiSanasi());
        savedShablon.setSerBerganKorxona(metrologiyaModel.getSerBerganKorxona());
        savedShablon.setSarflanganMablag(metrologiyaModel.getSarflanganMablag());
        savedShablon.setSerKeyingiSanasi(metrologiyaModel.getSerKeyingiSanasi());
        savedShablon.setSerDavriyligi(metrologiyaModel.getSerDavriyligi());
        savedShablon.setShartnomaRaqamiSanasi(metrologiyaModel.getShartnomaRaqamiSanasi());
        savedShablon.setIzoh(metrologiyaModel.getIzoh());
        savedShablon.setDepoNomi(metrologiyaModel.getDepoNomi());

        return metrologiyaRepository.save(savedShablon);
    }

    @Override
    public MetrologiyaModel saveTemplateAndj(MetrologiyaModel metrologiyaModel) {

        Optional<MetrologiyaModel> exists = metrologiyaRepository.findById(metrologiyaModel.getId());
        if (exists.isPresent() || !metrologiyaModel.getDepoNomi().equalsIgnoreCase("VCHD-5") || metrologiyaModel.getNomi().isEmpty())
            return new MetrologiyaModel();
        MetrologiyaModel savedShablon = new MetrologiyaModel();
        savedShablon.setNomi(metrologiyaModel.getNomi());
        savedShablon.setSoni(metrologiyaModel.getSoni());
        savedShablon.setRaqami(metrologiyaModel.getRaqami());
        savedShablon.setIshlabChiqarilganYili(metrologiyaModel.getIshlabChiqarilganYili());
        savedShablon.setTuri(metrologiyaModel.getTuri());
        savedShablon.setIshi(metrologiyaModel.getIshi());
        savedShablon.setSaqlanishJoyi(metrologiyaModel.getSaqlanishJoyi());
        savedShablon.setSerRaqamiSanasi(metrologiyaModel.getSerRaqamiSanasi());
        savedShablon.setSerBerganKorxona(metrologiyaModel.getSerBerganKorxona());
        savedShablon.setSarflanganMablag(metrologiyaModel.getSarflanganMablag());
        savedShablon.setSerKeyingiSanasi(metrologiyaModel.getSerKeyingiSanasi());
        savedShablon.setSerDavriyligi(metrologiyaModel.getSerDavriyligi());
        savedShablon.setShartnomaRaqamiSanasi(metrologiyaModel.getShartnomaRaqamiSanasi());
        savedShablon.setIzoh(metrologiyaModel.getIzoh());
        savedShablon.setDepoNomi(metrologiyaModel.getDepoNomi());

        return metrologiyaRepository.save(savedShablon);
    }

    @Override
    public MetrologiyaModel getShablonById(Integer id) {
        Optional<MetrologiyaModel> optional = metrologiyaRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        return new MetrologiyaModel();

    }

    @Override
    public void updateShablon(MetrologiyaModel metrologiyaModel, Integer id) {
        Optional<MetrologiyaModel> exists = metrologiyaRepository.findById(id);
        if (exists.isPresent() && !metrologiyaModel.getNomi().isEmpty()) {

            MetrologiyaModel savedShablon = exists.get();
            savedShablon.setNomi(metrologiyaModel.getNomi());
            savedShablon.setSoni(metrologiyaModel.getSoni());
            savedShablon.setRaqami(metrologiyaModel.getRaqami());
            savedShablon.setIshlabChiqarilganYili(metrologiyaModel.getIshlabChiqarilganYili());
            savedShablon.setTuri(metrologiyaModel.getTuri());
            savedShablon.setIshi(metrologiyaModel.getIshi());
            savedShablon.setSaqlanishJoyi(metrologiyaModel.getSaqlanishJoyi());
            savedShablon.setSerRaqamiSanasi(metrologiyaModel.getSerRaqamiSanasi());
            savedShablon.setSerBerganKorxona(metrologiyaModel.getSerBerganKorxona());
            savedShablon.setSarflanganMablag(metrologiyaModel.getSarflanganMablag());
            savedShablon.setSerKeyingiSanasi(metrologiyaModel.getSerKeyingiSanasi());
            savedShablon.setSerDavriyligi(metrologiyaModel.getSerDavriyligi());
            savedShablon.setShartnomaRaqamiSanasi(metrologiyaModel.getShartnomaRaqamiSanasi());
            savedShablon.setIzoh(metrologiyaModel.getIzoh());
            savedShablon.setDepoNomi(metrologiyaModel.getDepoNomi());

            metrologiyaRepository.save(savedShablon);
        }
    }

    @Override
    public void updateShablonSam(MetrologiyaModel metrologiyaModel, Integer id) {
        Optional<MetrologiyaModel> exists = metrologiyaRepository.findById(id);
        if (exists.isPresent() && !metrologiyaModel.getNomi().isEmpty()&& metrologiyaModel.getDepoNomi().equalsIgnoreCase("VCHD-6")
                && exists.get().getDepoNomi().equalsIgnoreCase("VCHD-6") ) {

            MetrologiyaModel savedShablon = exists.get();
            savedShablon.setNomi(metrologiyaModel.getNomi());
            savedShablon.setSoni(metrologiyaModel.getSoni());
            savedShablon.setRaqami(metrologiyaModel.getRaqami());
            savedShablon.setIshlabChiqarilganYili(metrologiyaModel.getIshlabChiqarilganYili());
            savedShablon.setTuri(metrologiyaModel.getTuri());
            savedShablon.setIshi(metrologiyaModel.getIshi());
            savedShablon.setSaqlanishJoyi(metrologiyaModel.getSaqlanishJoyi());
            savedShablon.setSerRaqamiSanasi(metrologiyaModel.getSerRaqamiSanasi());
            savedShablon.setSerBerganKorxona(metrologiyaModel.getSerBerganKorxona());
            savedShablon.setSarflanganMablag(metrologiyaModel.getSarflanganMablag());
            savedShablon.setSerKeyingiSanasi(metrologiyaModel.getSerKeyingiSanasi());
            savedShablon.setSerDavriyligi(metrologiyaModel.getSerDavriyligi());
            savedShablon.setShartnomaRaqamiSanasi(metrologiyaModel.getShartnomaRaqamiSanasi());
            savedShablon.setIzoh(metrologiyaModel.getIzoh());
            savedShablon.setDepoNomi(metrologiyaModel.getDepoNomi());
            metrologiyaRepository.save(savedShablon);
        }
    }

    @Override
    public void updateShablonHav(MetrologiyaModel metrologiyaModel, Integer id) {
        Optional<MetrologiyaModel> exists = metrologiyaRepository.findById(id);
        if (exists.isPresent() && !metrologiyaModel.getNomi().isEmpty()&& metrologiyaModel.getDepoNomi().equalsIgnoreCase("VCHD-3")
                && exists.get().getDepoNomi().equalsIgnoreCase("VCHD-3") ) {

            MetrologiyaModel savedShablon = exists.get();
            savedShablon.setNomi(metrologiyaModel.getNomi());
            savedShablon.setSoni(metrologiyaModel.getSoni());
            savedShablon.setRaqami(metrologiyaModel.getRaqami());
            savedShablon.setIshlabChiqarilganYili(metrologiyaModel.getIshlabChiqarilganYili());
            savedShablon.setTuri(metrologiyaModel.getTuri());
            savedShablon.setIshi(metrologiyaModel.getIshi());
            savedShablon.setSaqlanishJoyi(metrologiyaModel.getSaqlanishJoyi());
            savedShablon.setSerRaqamiSanasi(metrologiyaModel.getSerRaqamiSanasi());
            savedShablon.setSerBerganKorxona(metrologiyaModel.getSerBerganKorxona());
            savedShablon.setSarflanganMablag(metrologiyaModel.getSarflanganMablag());
            savedShablon.setSerKeyingiSanasi(metrologiyaModel.getSerKeyingiSanasi());
            savedShablon.setSerDavriyligi(metrologiyaModel.getSerDavriyligi());
            savedShablon.setShartnomaRaqamiSanasi(metrologiyaModel.getShartnomaRaqamiSanasi());
            savedShablon.setIzoh(metrologiyaModel.getIzoh());
            savedShablon.setDepoNomi(metrologiyaModel.getDepoNomi());
            metrologiyaRepository.save(savedShablon);
        }
    }

    @Override
    public void updateShablonAndj(MetrologiyaModel metrologiyaModel, Integer id) {
        Optional<MetrologiyaModel> exists = metrologiyaRepository.findById(id);
        if (exists.isPresent() && !metrologiyaModel.getNomi().isEmpty()&& metrologiyaModel.getDepoNomi().equalsIgnoreCase("VCHD-5")
                && exists.get().getDepoNomi().equalsIgnoreCase("VCHD-5") ) {

            MetrologiyaModel savedShablon = exists.get();
            savedShablon.setNomi(metrologiyaModel.getNomi());
            savedShablon.setSoni(metrologiyaModel.getSoni());
            savedShablon.setRaqami(metrologiyaModel.getRaqami());
            savedShablon.setIshlabChiqarilganYili(metrologiyaModel.getIshlabChiqarilganYili());
            savedShablon.setTuri(metrologiyaModel.getTuri());
            savedShablon.setIshi(metrologiyaModel.getIshi());
            savedShablon.setSaqlanishJoyi(metrologiyaModel.getSaqlanishJoyi());
            savedShablon.setSerRaqamiSanasi(metrologiyaModel.getSerRaqamiSanasi());
            savedShablon.setSerBerganKorxona(metrologiyaModel.getSerBerganKorxona());
            savedShablon.setSarflanganMablag(metrologiyaModel.getSarflanganMablag());
            savedShablon.setSerKeyingiSanasi(metrologiyaModel.getSerKeyingiSanasi());
            savedShablon.setSerDavriyligi(metrologiyaModel.getSerDavriyligi());
            savedShablon.setShartnomaRaqamiSanasi(metrologiyaModel.getShartnomaRaqamiSanasi());
            savedShablon.setIzoh(metrologiyaModel.getIzoh());
            savedShablon.setDepoNomi(metrologiyaModel.getDepoNomi());
            metrologiyaRepository.save(savedShablon);
        }
    }

    @Override
    public void deleteTemplateById(Integer id) {
        metrologiyaRepository.deleteById(id);

    }

    @Override
    public void deleteTemplateSam(Integer id) {
        Optional<MetrologiyaModel> optional = metrologiyaRepository.findById(id);
        if (optional.isPresent() && optional.get().getDepoNomi().equals("VCHD-6"))
            metrologiyaRepository.deleteById(id);
    }

    @Override
    public void deleteTemplateHav(Integer id) {
        Optional<MetrologiyaModel> optional = metrologiyaRepository.findById(id);
        if (optional.isPresent() && optional.get().getDepoNomi().equals("VCHD-3"))
            metrologiyaRepository.deleteById(id);
    }

    @Override
    public void deleteTemplateAndj(Integer id) {
        Optional<MetrologiyaModel> optional = metrologiyaRepository.findById(id);
        if (optional.isPresent() && optional.get().getDepoNomi().equals("VCHD-5"))
            metrologiyaRepository.deleteById(id);
    }
}
