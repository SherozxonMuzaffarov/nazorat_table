package com.sms.serviceImp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.VerticalText;
import com.sms.model.LastActionTimes;
import com.sms.repository.TimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.security.web.savedrequest.SavedCookie;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.sms.model.VagonMalumot;
import com.sms.repository.VagonMalumotRepository;
import com.sms.service.VagonMalumotService;

@Service
public class VagonMalumotServiceImp implements VagonMalumotService{

	@Autowired
	VagonMalumotRepository malumotRepository;
	@Autowired
	private TimeRepository utyTimeRepository;

	String currentDate;
	String samDate;
	String havDate ;
	String andjDate ;
	
	public VagonMalumotRepository getMalumotRepository() {
		return malumotRepository;
	}

	public String getSamDate() {
		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		if (!optionalQoldiqTime.isPresent())
			return currentDate;
		return optionalQoldiqTime.get().getSamMalumotDate();
	}

	public String getHavDate() {
		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		if (!optionalQoldiqTime.isPresent())
			return currentDate;
		return optionalQoldiqTime.get().getHavMalumotDate();
	}

	public String getAndjDate() {
		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		if (!optionalQoldiqTime.isPresent())
			return currentDate;
		return optionalQoldiqTime.get().getAndjMalumotDate();
	}
	
	public void createPdf(List<VagonMalumot> vagons, HttpServletResponse response) throws IOException {

		String home = System.getProperty("user.home");
		  File file = new File(home + "/Downloads" + "/Ta'mirdan chiqgan yuk vagonlar haqida ma'lumot.pdf");
		  if (!file.getParentFile().exists())
		      file.getParentFile().mkdirs();
		  if (!file.exists())
		      file.createNewFile();
		List<VagonMalumot> allVagons = vagons;
		try {
			response.setHeader("Content-Disposition",
                    "attachment;fileName=\"" + "Ta'mirdan chiqgan yuk vagonlar haqida ma'lumot.pdf" +"\"");
			response.setContentType("application/pdf");


			PdfWriter writer = new PdfWriter(file.getAbsolutePath());
			PdfDocument pdfDoc = new PdfDocument(writer);
			pdfDoc.setDefaultPageSize(PageSize.A4.rotate());
			Document doc = new Document(pdfDoc);

			String FONT_FILENAME = "./src/main/resources/arial.ttf";
			PdfFont font = PdfFontFactory.createFont(FONT_FILENAME, PdfEncodings.IDENTITY_H);
			doc.setFont(font);

			Paragraph paragraph = new Paragraph("Ta'mirdan chiqgan yuk vagonlar haqida ma'lumot");
			paragraph.setBackgroundColor(Color.DARK_GRAY);
			paragraph.setFontColor(Color.WHITE);// Setting background color to cell1
			paragraph.setBorder(Border.NO_BORDER);            // Setting border to cell1
			paragraph.setTextAlignment(TextAlignment.CENTER); // Setting text alignment to cell1
			paragraph.setFontSize(16);

			float[] columnWidth = {30f,150f,150f,150f,150f, 150f,150f,150f,150f,150f,150f};
			Table table = new Table(columnWidth);
			table.setTextAlignment(TextAlignment.CENTER);
			table.addCell(new Cell().add("\n № "));
			table.addCell(new Cell().add("\n Nomeri"));
			table.addCell(new Cell().add("\n VCHD"));
			table.addCell(new Cell().add(" Oxirgi ta'mir sanasi"));
			table.addCell(new Cell().add("\n Ta'mir turi"));
			table.addCell(new Cell().add(" Ishlab chiqarilgan yili"));
			table.addCell(new Cell().add("\n Holat"));

			// yon ramaga nested table
			float [] pointColumnWidths2 = {150f};
			Table nestedTableRama = new Table(pointColumnWidths2);
			nestedTableRama.addCell(new Cell().add("Yon ramasi"));

			float [] pointColumnWidths3 = {75f,75f};
			Table nestedTableRamaTomon = new Table(pointColumnWidths3);
			nestedTableRamaTomon.addCell(new Cell().add("O'ng"));
			nestedTableRamaTomon.addCell(new Cell().add("Chap"));
			nestedTableRama.addCell(new Cell().add(nestedTableRamaTomon));

			table.addCell(new Cell().add(nestedTableRama));
			table.addCell(new Cell().add("Ressor usti balkasi"));

			// G'ildirak juftligi tegishliligiga nested table
			float [] pointColumnWidths6 = {150f};
			Table nestedTableGildirak = new Table(pointColumnWidths6);
			nestedTableGildirak.addCell(new Cell().add("G'ildirak juftligi tegishliligi (29)"));

			float [] pointColumnWidths7 = {75f/2,75f/2,75f/2,75f/2};
			Table nestedTableSoni = new Table(pointColumnWidths7);
			nestedTableSoni.addCell(new Cell().add("Birinchi"));
			nestedTableSoni.addCell(new Cell().add("Ikkinchi"));
			nestedTableSoni.addCell(new Cell().add("Uchinchi"));
			nestedTableSoni.addCell(new Cell().add("To'rtinchi"));
			nestedTableGildirak.addCell(new Cell().add(nestedTableSoni));

			table.addCell(new Cell().add(nestedTableGildirak));
			table.addCell(new Cell().add("\nIzoh"));
			int i=0;
			for(VagonMalumot vagon:allVagons) {
				i++;
				table.addCell(new Cell().add(String.valueOf(i)));
				table.addCell(new Cell().add(String.valueOf(vagon.getNomer())));
				table.addCell(new Cell().add(vagon.getDepoNomi()));
				table.addCell(new Cell().add(String.valueOf(vagon.getOxirgiTamirKuni())));
				table.addCell(new Cell().add(vagon.getRemontTuri()));
				table.addCell(new Cell().add(String.valueOf(vagon.getIshlabChiqarilganYili())));

				//holat
				float [] pointColumnWidths9 = {75f, 75f};
				Table nestedTableHolat = new Table(pointColumnWidths9);
				nestedTableHolat.addCell(new Cell().add("\n K:"));

				float [] pointColumnWidths10 = {75f};
				Table nestedTableHolatKirish = new Table(pointColumnWidths10);
				nestedTableHolatKirish.addCell(new Cell().add("yili"));
				nestedTableHolatKirish.addCell(new Cell().add("nomeri"));

				nestedTableHolat.addCell(new Cell().add(nestedTableHolatKirish));
				nestedTableHolat.addCell(new Cell().add("\n Ch:"));

				float [] pointColumnWidths11 = {75f};
				Table nestedTableHolatChiqish = new Table(pointColumnWidths11);
				nestedTableHolatChiqish.addCell(new Cell().add("yili"));
				nestedTableHolatChiqish.addCell(new Cell().add("nomeri"));

				nestedTableHolat.addCell(new Cell().add(nestedTableHolatChiqish));

				table.addCell(new Cell().add(nestedTableHolat));

				// yon ramaga nested table
				float [] pointColumnWidthsrama = {75f,75f};
				Table nestedTableRama1 = new Table(pointColumnWidthsrama);
				//ong tomon 1,2
				float [] pointColumnWidths4 = {75f,75f};
				Table nestedTable = new Table(pointColumnWidths4);
				nestedTable.addCell(new Cell().add(String.valueOf(vagon.getKramaOng1())));
				nestedTable.addCell(new Cell().add(String.valueOf(vagon.getKramaOng2())));
				nestedTable.addCell(new Cell().add(String.valueOf(vagon.getKramaOng1Nomeri())));
				nestedTable.addCell(new Cell().add(String.valueOf(vagon.getKramaOng2Nomeri())));
				nestedTable.addCell(new Cell().add(String.valueOf(vagon.getRamaOng1())));
				nestedTable.addCell(new Cell().add(String.valueOf(vagon.getRamaOng2())));
				nestedTable.addCell(new Cell().add(String.valueOf(vagon.getRamaOng1Nomeri())));
				nestedTable.addCell(new Cell().add(String.valueOf(vagon.getRamaOng2Nomeri())));

				nestedTableRama1.addCell(new Cell().add(nestedTable));

				//chap tomon 1,2
				float [] pointColumnWidthschap = {75f,75f};
				Table nestedTableChap = new Table(pointColumnWidthschap);
				nestedTableChap.addCell(new Cell().add(String.valueOf(vagon.getKramaChap1())));
				nestedTableChap.addCell(new Cell().add(String.valueOf(vagon.getKramaChap2())));
				nestedTableChap.addCell(new Cell().add(String.valueOf(vagon.getKramaChap1Nomeri())));
				nestedTableChap.addCell(new Cell().add(String.valueOf(vagon.getKramaChap2Nomeri())));
				nestedTableChap.addCell(new Cell().add(String.valueOf(vagon.getRamaChap1())));
				nestedTableChap.addCell(new Cell().add(String.valueOf(vagon.getRamaChap2())));
				nestedTableChap.addCell(new Cell().add(String.valueOf(vagon.getRamaChap1Nomeri())));
				nestedTableChap.addCell(new Cell().add(String.valueOf(vagon.getRamaChap2Nomeri())));

				nestedTableRama1.addCell(new Cell().add(nestedTableChap));

				table.addCell(new Cell().add(nestedTableRama1));

				//Ressor usti balkasiga nested table

				float [] pointColumnWidths5 = {75f,75f};
				Table nestedTableBalka = new Table(pointColumnWidths5);
				nestedTableBalka.addCell(new Cell().add(String.valueOf(vagon.getKbalka1())));
				nestedTableBalka.addCell(new Cell().add(String.valueOf(vagon.getKbalka2())));
				nestedTableBalka.addCell(new Cell().add(String.valueOf(vagon.getKbalka1Nomeri())));
				nestedTableBalka.addCell(new Cell().add(String.valueOf(vagon.getKbalka2Nomeri())));
				nestedTableBalka.addCell(new Cell().add(String.valueOf(vagon.getBalka1())));
				nestedTableBalka.addCell(new Cell().add(String.valueOf(vagon.getBalka2())));
				nestedTableBalka.addCell(new Cell().add(String.valueOf(vagon.getBalka1Nomeri())));
				nestedTableBalka.addCell(new Cell().add(String.valueOf(vagon.getBalka2Nomeri())));

				table.addCell(new Cell().add(nestedTableBalka));

				// G'ildirak juftligi tegishliligiga nested table
				float [] pointColumnWidths8 = {75f,75f,75f,75f};
				Table nestedTableTegisliligi = new Table(pointColumnWidths8);
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getKgildirak1())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getKgildirak2())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getKgildirak3())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getKgildirak4())));

				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getKgildirak1Nomeri())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getKgildirak2Nomeri())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getKgildirak3Nomeri())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getKgildirak4Nomeri())));

				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getGildirak1())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getGildirak2())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getGildirak3())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getGildirak4())));

				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getGildirak1Nomeri())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getGildirak2Nomeri())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getGildirak3Nomeri())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getGildirak4Nomeri())));

				table.addCell(new Cell().add(nestedTableTegisliligi));
				table.addCell(new Cell().add(vagon.getIzoh()));
			}

			doc.add(paragraph);
			doc.add(table);
			doc.close();
			FileInputStream in = new FileInputStream(file.getAbsoluteFile());
			FileCopyUtils.copy(in, response.getOutputStream());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<VagonMalumot> findAll() {
		return malumotRepository.findAll();
	}
	
	// bosh admin qoshadi
		@Override
		public VagonMalumot saveVagon(VagonMalumot vagon) {	
			if(vagon.getNomer() == null)
				return new VagonMalumot();
			Optional<VagonMalumot> exist=	malumotRepository.findByNomer(vagon.getNomer());
			if(exist.isPresent())
				return new VagonMalumot();
			VagonMalumot savedVagon = new VagonMalumot();
			savedVagon.setNomer(vagon.getNomer());
			savedVagon.setDepoNomi(vagon.getDepoNomi());
			savedVagon.setOxirgiTamirKuni(vagon.getOxirgiTamirKuni());
			savedVagon.setRemontTuri(vagon.getRemontTuri());
			savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			
			savedVagon.setKramaOng1(vagon.getKramaOng1());
			savedVagon.setKramaOng1Nomeri(vagon.getKramaOng1Nomeri());
			savedVagon.setRamaOng1(vagon.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());
			
			savedVagon.setKramaOng2(vagon.getKramaOng2());
			savedVagon.setKramaOng2Nomeri(vagon.getKramaOng2Nomeri());
			savedVagon.setRamaOng2(vagon.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());
			
			savedVagon.setKramaChap1(vagon.getKramaChap1());
			savedVagon.setKramaChap1Nomeri(vagon.getKramaChap1Nomeri());
			savedVagon.setRamaChap1(vagon.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());
			
			savedVagon.setKramaChap2(vagon.getKramaChap2());
			savedVagon.setKramaChap2Nomeri(vagon.getKramaChap2Nomeri());
			savedVagon.setRamaChap2(vagon.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());
			
			savedVagon.setKbalka1(vagon.getKbalka1());
			savedVagon.setKbalka1Nomeri(vagon.getKbalka1Nomeri());
			savedVagon.setBalka1(vagon.getBalka1());
			savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());
			
			savedVagon.setKbalka2(vagon.getKbalka2());
			savedVagon.setKbalka2Nomeri(vagon.getKbalka2Nomeri());
			savedVagon.setBalka2(vagon.getBalka2());
			savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());
			
			savedVagon.setKgildirak1(vagon.getKgildirak1());
			savedVagon.setKgildirak1Nomeri(vagon.getKgildirak1Nomeri());
			savedVagon.setGildirak1(vagon.getGildirak1());
			savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());
			
			savedVagon.setKgildirak2(vagon.getKgildirak2());
			savedVagon.setKgildirak2Nomeri(vagon.getKgildirak2Nomeri());
			savedVagon.setGildirak2(vagon.getGildirak2());
			savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());
			
			savedVagon.setKgildirak3(vagon.getKgildirak3());
			savedVagon.setKgildirak3Nomeri(vagon.getKgildirak3Nomeri());
			savedVagon.setGildirak3(vagon.getGildirak3());
			savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());
			
			savedVagon.setKgildirak4(vagon.getKgildirak4());
			savedVagon.setKgildirak4Nomeri(vagon.getKgildirak4Nomeri());
			savedVagon.setGildirak4(vagon.getGildirak4());
			savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());

			savedVagon.setCountry(vagon.getCountry());
			savedVagon.setIzoh(vagon.getIzoh());

			LocalDateTime today = LocalDateTime.now();
			LocalDateTime minusHours = today.plusHours(5);
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			currentDate = minusHours.format(myFormatObj);

			savedVagon.setSaqlanganVaqti(currentDate);
			
			return malumotRepository.save(savedVagon);	
		}
		@Override
		public VagonMalumot saveVagonSam(VagonMalumot vagon) {	
			if(vagon.getNomer() == null)
				return new VagonMalumot();
			Optional<VagonMalumot> exist=malumotRepository.findByNomer(vagon.getNomer());
			if(exist.isPresent() || !vagon.getDepoNomi().equalsIgnoreCase("VCHD-6"))
				return new VagonMalumot();

			VagonMalumot savedVagon = new VagonMalumot();
			savedVagon.setNomer(vagon.getNomer());
			savedVagon.setDepoNomi(vagon.getDepoNomi());
			savedVagon.setOxirgiTamirKuni(vagon.getOxirgiTamirKuni());
			savedVagon.setRemontTuri(vagon.getRemontTuri());
			savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());

			savedVagon.setKramaOng1(vagon.getKramaOng1());
			savedVagon.setKramaOng1Nomeri(vagon.getKramaOng1Nomeri());
			savedVagon.setRamaOng1(vagon.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());

			savedVagon.setKramaOng2(vagon.getKramaOng2());
			savedVagon.setKramaOng2Nomeri(vagon.getKramaOng2Nomeri());
			savedVagon.setRamaOng2(vagon.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());

			savedVagon.setKramaChap1(vagon.getKramaChap1());
			savedVagon.setKramaChap1Nomeri(vagon.getKramaChap1Nomeri());
			savedVagon.setRamaChap1(vagon.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());

			savedVagon.setKramaChap2(vagon.getKramaChap2());
			savedVagon.setKramaChap2Nomeri(vagon.getKramaChap2Nomeri());
			savedVagon.setRamaChap2(vagon.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());

			savedVagon.setKbalka1(vagon.getKbalka1());
			savedVagon.setKbalka1Nomeri(vagon.getKbalka1Nomeri());
			savedVagon.setBalka1(vagon.getBalka1());
			savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());

			savedVagon.setKbalka2(vagon.getKbalka2());
			savedVagon.setKbalka2Nomeri(vagon.getKbalka2Nomeri());
			savedVagon.setBalka2(vagon.getBalka2());
			savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());

			savedVagon.setKgildirak1(vagon.getKgildirak1());
			savedVagon.setKgildirak1Nomeri(vagon.getKgildirak1Nomeri());
			savedVagon.setGildirak1(vagon.getGildirak1());
			savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());

			savedVagon.setKgildirak2(vagon.getKgildirak2());
			savedVagon.setKgildirak2Nomeri(vagon.getKgildirak2Nomeri());
			savedVagon.setGildirak2(vagon.getGildirak2());
			savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());

			savedVagon.setKgildirak3(vagon.getKgildirak3());
			savedVagon.setKgildirak3Nomeri(vagon.getKgildirak3Nomeri());
			savedVagon.setGildirak3(vagon.getGildirak3());
			savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());

			savedVagon.setKgildirak4(vagon.getKgildirak4());
			savedVagon.setKgildirak4Nomeri(vagon.getKgildirak4Nomeri());
			savedVagon.setGildirak4(vagon.getGildirak4());
			savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());

			savedVagon.setCountry(vagon.getCountry());
			savedVagon.setIzoh(vagon.getIzoh());
			
			LocalDateTime today = LocalDateTime.now();
			LocalDateTime minusHours = today.plusHours(5);
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			samDate = minusHours.format(myFormatObj);

			savedVagon.setSaqlanganVaqti(samDate);

			Optional<LastActionTimes> optionalMalumotTime = utyTimeRepository.findById(1);
			optionalMalumotTime.get().setSamMalumotDate(samDate);
			utyTimeRepository.save(optionalMalumotTime.get());
	
			return malumotRepository.save(savedVagon);	
		}
		@Override
		public VagonMalumot saveVagonHav(VagonMalumot vagon) {
			if(vagon.getNomer() == null)
				return new VagonMalumot();
			Optional<VagonMalumot> exist=	malumotRepository.findByNomer(vagon.getNomer());
			if(exist.isPresent() || !vagon.getDepoNomi().equalsIgnoreCase("VCHD-3"))
				return new VagonMalumot();
	
			VagonMalumot savedVagon = new VagonMalumot();
			savedVagon.setNomer(vagon.getNomer());
			savedVagon.setDepoNomi(vagon.getDepoNomi());
			savedVagon.setOxirgiTamirKuni(vagon.getOxirgiTamirKuni());
			savedVagon.setRemontTuri(vagon.getRemontTuri());
			savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());

			savedVagon.setKramaOng1(vagon.getKramaOng1());
			savedVagon.setKramaOng1Nomeri(vagon.getKramaOng1Nomeri());
			savedVagon.setRamaOng1(vagon.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());

			savedVagon.setKramaOng2(vagon.getKramaOng2());
			savedVagon.setKramaOng2Nomeri(vagon.getKramaOng2Nomeri());
			savedVagon.setRamaOng2(vagon.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());

			savedVagon.setKramaChap1(vagon.getKramaChap1());
			savedVagon.setKramaChap1Nomeri(vagon.getKramaChap1Nomeri());
			savedVagon.setRamaChap1(vagon.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());

			savedVagon.setKramaChap2(vagon.getKramaChap2());
			savedVagon.setKramaChap2Nomeri(vagon.getKramaChap2Nomeri());
			savedVagon.setRamaChap2(vagon.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());

			savedVagon.setKbalka1(vagon.getKbalka1());
			savedVagon.setKbalka1Nomeri(vagon.getKbalka1Nomeri());
			savedVagon.setBalka1(vagon.getBalka1());
			savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());

			savedVagon.setKbalka2(vagon.getKbalka2());
			savedVagon.setKbalka2Nomeri(vagon.getKbalka2Nomeri());
			savedVagon.setBalka2(vagon.getBalka2());
			savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());

			savedVagon.setKgildirak1(vagon.getKgildirak1());
			savedVagon.setKgildirak1Nomeri(vagon.getKgildirak1Nomeri());
			savedVagon.setGildirak1(vagon.getGildirak1());
			savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());

			savedVagon.setKgildirak2(vagon.getKgildirak2());
			savedVagon.setKgildirak2Nomeri(vagon.getKgildirak2Nomeri());
			savedVagon.setGildirak2(vagon.getGildirak2());
			savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());

			savedVagon.setKgildirak3(vagon.getKgildirak3());
			savedVagon.setKgildirak3Nomeri(vagon.getKgildirak3Nomeri());
			savedVagon.setGildirak3(vagon.getGildirak3());
			savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());

			savedVagon.setKgildirak4(vagon.getKgildirak4());
			savedVagon.setKgildirak4Nomeri(vagon.getKgildirak4Nomeri());
			savedVagon.setGildirak4(vagon.getGildirak4());
			savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());

			savedVagon.setCountry(vagon.getCountry());
			savedVagon.setIzoh(vagon.getIzoh());
			
			LocalDateTime today = LocalDateTime.now();
			LocalDateTime minusHours = today.plusHours(5);
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			havDate = minusHours.format(myFormatObj);

			savedVagon.setSaqlanganVaqti(havDate);

			Optional<LastActionTimes> optionalMalumotTime = utyTimeRepository.findById(1);
			optionalMalumotTime.get().setHavMalumotDate(havDate);
			utyTimeRepository.save(optionalMalumotTime.get());

			return malumotRepository.save(savedVagon);
		}

		@Override
		public VagonMalumot saveVagonAndj(VagonMalumot vagon) {
			if(vagon.getNomer() == null)
				return new VagonMalumot();
			Optional<VagonMalumot> exist=	malumotRepository.findByNomer(vagon.getNomer());
			if(exist.isPresent() || !vagon.getDepoNomi().equalsIgnoreCase("VCHD-5"))
				return new VagonMalumot();

			VagonMalumot savedVagon = new VagonMalumot();
			savedVagon.setNomer(vagon.getNomer());
			savedVagon.setDepoNomi(vagon.getDepoNomi());
			savedVagon.setOxirgiTamirKuni(vagon.getOxirgiTamirKuni());
			savedVagon.setRemontTuri(vagon.getRemontTuri());
			savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());

			savedVagon.setKramaOng1(vagon.getKramaOng1());
			savedVagon.setKramaOng1Nomeri(vagon.getKramaOng1Nomeri());
			savedVagon.setRamaOng1(vagon.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());

			savedVagon.setKramaOng2(vagon.getKramaOng2());
			savedVagon.setKramaOng2Nomeri(vagon.getKramaOng2Nomeri());
			savedVagon.setRamaOng2(vagon.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());

			savedVagon.setKramaChap1(vagon.getKramaChap1());
			savedVagon.setKramaChap1Nomeri(vagon.getKramaChap1Nomeri());
			savedVagon.setRamaChap1(vagon.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());

			savedVagon.setKramaChap2(vagon.getKramaChap2());
			savedVagon.setKramaChap2Nomeri(vagon.getKramaChap2Nomeri());
			savedVagon.setRamaChap2(vagon.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());

			savedVagon.setKbalka1(vagon.getKbalka1());
			savedVagon.setKbalka1Nomeri(vagon.getKbalka1Nomeri());
			savedVagon.setBalka1(vagon.getBalka1());
			savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());

			savedVagon.setKbalka2(vagon.getKbalka2());
			savedVagon.setKbalka2Nomeri(vagon.getKbalka2Nomeri());
			savedVagon.setBalka2(vagon.getBalka2());
			savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());

			savedVagon.setKgildirak1(vagon.getKgildirak1());
			savedVagon.setKgildirak1Nomeri(vagon.getKgildirak1Nomeri());
			savedVagon.setGildirak1(vagon.getGildirak1());
			savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());

			savedVagon.setKgildirak2(vagon.getKgildirak2());
			savedVagon.setKgildirak2Nomeri(vagon.getKgildirak2Nomeri());
			savedVagon.setGildirak2(vagon.getGildirak2());
			savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());

			savedVagon.setKgildirak3(vagon.getKgildirak3());
			savedVagon.setKgildirak3Nomeri(vagon.getKgildirak3Nomeri());
			savedVagon.setGildirak3(vagon.getGildirak3());
			savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());

			savedVagon.setKgildirak4(vagon.getKgildirak4());
			savedVagon.setKgildirak4Nomeri(vagon.getKgildirak4Nomeri());
			savedVagon.setGildirak4(vagon.getGildirak4());
			savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());

			savedVagon.setCountry(vagon.getCountry());
			savedVagon.setIzoh(vagon.getIzoh());
	
			LocalDateTime today = LocalDateTime.now();
			LocalDateTime minusHours = today.plusHours(5);
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			andjDate = minusHours.format(myFormatObj);

			savedVagon.setSaqlanganVaqti(andjDate);

			Optional<LastActionTimes> optionalMalumotTime = utyTimeRepository.findById(1);
			optionalMalumotTime.get().setAndjMalumotDate(andjDate);
			utyTimeRepository.save(optionalMalumotTime.get());
			
			return malumotRepository.save(savedVagon);
		}
		

		@Override
		public VagonMalumot updateVagon(VagonMalumot vagon, long id) {	
			if(vagon.getNomer() == null)
				return new VagonMalumot();
			
			Optional<VagonMalumot> exist = malumotRepository.findById(id);
			 if(!exist.isPresent())
				 return new VagonMalumot();

			VagonMalumot savedVagon = exist.get();
			savedVagon.setId(id);
			savedVagon.setNomer(vagon.getNomer());
			savedVagon.setDepoNomi(vagon.getDepoNomi());
			savedVagon.setOxirgiTamirKuni(vagon.getOxirgiTamirKuni());
			savedVagon.setRemontTuri(vagon.getRemontTuri());
			savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());

			savedVagon.setKramaOng1(vagon.getKramaOng1());
			savedVagon.setKramaOng1Nomeri(vagon.getKramaOng1Nomeri());
			savedVagon.setRamaOng1(vagon.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());

			savedVagon.setKramaOng2(vagon.getKramaOng2());
			savedVagon.setKramaOng2Nomeri(vagon.getKramaOng2Nomeri());
			savedVagon.setRamaOng2(vagon.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());

			savedVagon.setKramaChap1(vagon.getKramaChap1());
			savedVagon.setKramaChap1Nomeri(vagon.getKramaChap1Nomeri());
			savedVagon.setRamaChap1(vagon.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());

			savedVagon.setKramaChap2(vagon.getKramaChap2());
			savedVagon.setKramaChap2Nomeri(vagon.getKramaChap2Nomeri());
			savedVagon.setRamaChap2(vagon.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());

			savedVagon.setKbalka1(vagon.getKbalka1());
			savedVagon.setKbalka1Nomeri(vagon.getKbalka1Nomeri());
			savedVagon.setBalka1(vagon.getBalka1());
			savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());

			savedVagon.setKbalka2(vagon.getKbalka2());
			savedVagon.setKbalka2Nomeri(vagon.getKbalka2Nomeri());
			savedVagon.setBalka2(vagon.getBalka2());
			savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());

			savedVagon.setKgildirak1(vagon.getKgildirak1());
			savedVagon.setKgildirak1Nomeri(vagon.getKgildirak1Nomeri());
			savedVagon.setGildirak1(vagon.getGildirak1());
			savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());

			savedVagon.setKgildirak2(vagon.getKgildirak2());
			savedVagon.setKgildirak2Nomeri(vagon.getKgildirak2Nomeri());
			savedVagon.setGildirak2(vagon.getGildirak2());
			savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());

			savedVagon.setKgildirak3(vagon.getKgildirak3());
			savedVagon.setKgildirak3Nomeri(vagon.getKgildirak3Nomeri());
			savedVagon.setGildirak3(vagon.getGildirak3());
			savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());

			savedVagon.setKgildirak4(vagon.getKgildirak4());
			savedVagon.setKgildirak4Nomeri(vagon.getKgildirak4Nomeri());
			savedVagon.setGildirak4(vagon.getGildirak4());
			savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());

			savedVagon.setCountry(vagon.getCountry());
			savedVagon.setIzoh(vagon.getIzoh());
	
			return malumotRepository.save(savedVagon);
		}
		
		@Override
		public VagonMalumot updateVagonSam(VagonMalumot vagon, long id) {
			if(vagon.getNomer() == null)
				return new VagonMalumot();
			 Optional<VagonMalumot> exist = malumotRepository.findById(id);
			 if(exist.get().getDepoNomi().equalsIgnoreCase("VCHD-6") && vagon.getDepoNomi().equalsIgnoreCase("VCHD-6")) {

				 VagonMalumot savedVagon = exist.get();
				savedVagon.setId(id);
				savedVagon.setNomer(vagon.getNomer());
				savedVagon.setDepoNomi(vagon.getDepoNomi());
				savedVagon.setOxirgiTamirKuni(vagon.getOxirgiTamirKuni());
				savedVagon.setRemontTuri(vagon.getRemontTuri());
				savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());

				 savedVagon.setKramaOng1(vagon.getKramaOng1());
				 savedVagon.setKramaOng1Nomeri(vagon.getKramaOng1Nomeri());
				 savedVagon.setRamaOng1(vagon.getRamaOng1());
				 savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());

				 savedVagon.setKramaOng2(vagon.getKramaOng2());
				 savedVagon.setKramaOng2Nomeri(vagon.getKramaOng2Nomeri());
				 savedVagon.setRamaOng2(vagon.getRamaOng2());
				 savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());

				 savedVagon.setKramaChap1(vagon.getKramaChap1());
				 savedVagon.setKramaChap1Nomeri(vagon.getKramaChap1Nomeri());
				 savedVagon.setRamaChap1(vagon.getRamaChap1());
				 savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());

				 savedVagon.setKramaChap2(vagon.getKramaChap2());
				 savedVagon.setKramaChap2Nomeri(vagon.getKramaChap2Nomeri());
				 savedVagon.setRamaChap2(vagon.getRamaChap2());
				 savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());

				 savedVagon.setKbalka1(vagon.getKbalka1());
				 savedVagon.setKbalka1Nomeri(vagon.getKbalka1Nomeri());
				 savedVagon.setBalka1(vagon.getBalka1());
				 savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());

				 savedVagon.setKbalka2(vagon.getKbalka2());
				 savedVagon.setKbalka2Nomeri(vagon.getKbalka2Nomeri());
				 savedVagon.setBalka2(vagon.getBalka2());
				 savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());

				 savedVagon.setKgildirak1(vagon.getKgildirak1());
				 savedVagon.setKgildirak1Nomeri(vagon.getKgildirak1Nomeri());
				 savedVagon.setGildirak1(vagon.getGildirak1());
				 savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());

				 savedVagon.setKgildirak2(vagon.getKgildirak2());
				 savedVagon.setKgildirak2Nomeri(vagon.getKgildirak2Nomeri());
				 savedVagon.setGildirak2(vagon.getGildirak2());
				 savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());

				 savedVagon.setKgildirak3(vagon.getKgildirak3());
				 savedVagon.setKgildirak3Nomeri(vagon.getKgildirak3Nomeri());
				 savedVagon.setGildirak3(vagon.getGildirak3());
				 savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());

				 savedVagon.setKgildirak4(vagon.getKgildirak4());
				 savedVagon.setKgildirak4Nomeri(vagon.getKgildirak4Nomeri());
				 savedVagon.setGildirak4(vagon.getGildirak4());
				 savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());

				 savedVagon.setCountry(vagon.getCountry());
				savedVagon.setIzoh(vagon.getIzoh());

				LocalDateTime today = LocalDateTime.now();
				LocalDateTime minusHours = today.plusHours(5);
				DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
				samDate = minusHours.format(myFormatObj);

				Optional<LastActionTimes> optionalMalumotTime = utyTimeRepository.findById(1);
				optionalMalumotTime.get().setSamMalumotDate(samDate);
				utyTimeRepository.save(optionalMalumotTime.get());
				
				return malumotRepository.save(savedVagon);
			 }else
				return new VagonMalumot();

		}

		@Override
		public VagonMalumot updateVagonHav(VagonMalumot vagon, long id) {
			if(vagon.getNomer() == null)
				return new VagonMalumot();
			 Optional<VagonMalumot> exist = malumotRepository.findById(id);
			 if(exist.get().getDepoNomi().equalsIgnoreCase("VCHD-3") && vagon.getDepoNomi().equalsIgnoreCase("VCHD-3")) {
				 
				 VagonMalumot savedVagon = exist.get();
				 savedVagon.setId(id);
				 savedVagon.setNomer(vagon.getNomer());
				 savedVagon.setDepoNomi(vagon.getDepoNomi());
				 savedVagon.setOxirgiTamirKuni(vagon.getOxirgiTamirKuni());
				 savedVagon.setRemontTuri(vagon.getRemontTuri());
				 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());

				 savedVagon.setKramaOng1(vagon.getKramaOng1());
				 savedVagon.setKramaOng1Nomeri(vagon.getKramaOng1Nomeri());
				 savedVagon.setRamaOng1(vagon.getRamaOng1());
				 savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());

				 savedVagon.setKramaOng2(vagon.getKramaOng2());
				 savedVagon.setKramaOng2Nomeri(vagon.getKramaOng2Nomeri());
				 savedVagon.setRamaOng2(vagon.getRamaOng2());
				 savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());

				 savedVagon.setKramaChap1(vagon.getKramaChap1());
				 savedVagon.setKramaChap1Nomeri(vagon.getKramaChap1Nomeri());
				 savedVagon.setRamaChap1(vagon.getRamaChap1());
				 savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());

				 savedVagon.setKramaChap2(vagon.getKramaChap2());
				 savedVagon.setKramaChap2Nomeri(vagon.getKramaChap2Nomeri());
				 savedVagon.setRamaChap2(vagon.getRamaChap2());
				 savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());

				 savedVagon.setKbalka1(vagon.getKbalka1());
				 savedVagon.setKbalka1Nomeri(vagon.getKbalka1Nomeri());
				 savedVagon.setBalka1(vagon.getBalka1());
				 savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());

				 savedVagon.setKbalka2(vagon.getKbalka2());
				 savedVagon.setKbalka2Nomeri(vagon.getKbalka2Nomeri());
				 savedVagon.setBalka2(vagon.getBalka2());
				 savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());

				 savedVagon.setKgildirak1(vagon.getKgildirak1());
				 savedVagon.setKgildirak1Nomeri(vagon.getKgildirak1Nomeri());
				 savedVagon.setGildirak1(vagon.getGildirak1());
				 savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());

				 savedVagon.setKgildirak2(vagon.getKgildirak2());
				 savedVagon.setKgildirak2Nomeri(vagon.getKgildirak2Nomeri());
				 savedVagon.setGildirak2(vagon.getGildirak2());
				 savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());

				 savedVagon.setKgildirak3(vagon.getKgildirak3());
				 savedVagon.setKgildirak3Nomeri(vagon.getKgildirak3Nomeri());
				 savedVagon.setGildirak3(vagon.getGildirak3());
				 savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());

				 savedVagon.setKgildirak4(vagon.getKgildirak4());
				 savedVagon.setKgildirak4Nomeri(vagon.getKgildirak4Nomeri());
				 savedVagon.setGildirak4(vagon.getGildirak4());
				 savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());

				 savedVagon.setCountry(vagon.getCountry());
				 savedVagon.setIzoh(vagon.getIzoh());

				 LocalDateTime today = LocalDateTime.now();
				 LocalDateTime minusHours = today.plusHours(5);
				 DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
				 havDate = minusHours.format(myFormatObj);

				 Optional<LastActionTimes> optionalMalumotTime = utyTimeRepository.findById(1);
				 optionalMalumotTime.get().setHavMalumotDate(havDate);
				 utyTimeRepository.save(optionalMalumotTime.get());

				 return malumotRepository.save(savedVagon);
			 }else
				 return new VagonMalumot();
		}

		@Override
		public VagonMalumot updateVagonAndj(VagonMalumot vagon, long id) {
			if(vagon.getNomer() == null)
				return new VagonMalumot();
			 Optional<VagonMalumot> exist = malumotRepository.findById(id);
			 if( exist.get().getDepoNomi().equalsIgnoreCase("VCHD-5") && vagon.getDepoNomi().equalsIgnoreCase("VCHD-5")){	

				 VagonMalumot savedVagon = exist.get();
				 savedVagon.setId(id);
				 savedVagon.setNomer(vagon.getNomer());
				 savedVagon.setDepoNomi(vagon.getDepoNomi());
				 savedVagon.setOxirgiTamirKuni(vagon.getOxirgiTamirKuni());
				 savedVagon.setRemontTuri(vagon.getRemontTuri());
				 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());

				 savedVagon.setKramaOng1(vagon.getKramaOng1());
				 savedVagon.setKramaOng1Nomeri(vagon.getKramaOng1Nomeri());
				 savedVagon.setRamaOng1(vagon.getRamaOng1());
				 savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());

				 savedVagon.setKramaOng2(vagon.getKramaOng2());
				 savedVagon.setKramaOng2Nomeri(vagon.getKramaOng2Nomeri());
				 savedVagon.setRamaOng2(vagon.getRamaOng2());
				 savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());

				 savedVagon.setKramaChap1(vagon.getKramaChap1());
				 savedVagon.setKramaChap1Nomeri(vagon.getKramaChap1Nomeri());
				 savedVagon.setRamaChap1(vagon.getRamaChap1());
				 savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());

				 savedVagon.setKramaChap2(vagon.getKramaChap2());
				 savedVagon.setKramaChap2Nomeri(vagon.getKramaChap2Nomeri());
				 savedVagon.setRamaChap2(vagon.getRamaChap2());
				 savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());

				 savedVagon.setKbalka1(vagon.getKbalka1());
				 savedVagon.setKbalka1Nomeri(vagon.getKbalka1Nomeri());
				 savedVagon.setBalka1(vagon.getBalka1());
				 savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());

				 savedVagon.setKbalka2(vagon.getKbalka2());
				 savedVagon.setKbalka2Nomeri(vagon.getKbalka2Nomeri());
				 savedVagon.setBalka2(vagon.getBalka2());
				 savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());

				 savedVagon.setKgildirak1(vagon.getKgildirak1());
				 savedVagon.setKgildirak1Nomeri(vagon.getKgildirak1Nomeri());
				 savedVagon.setGildirak1(vagon.getGildirak1());
				 savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());

				 savedVagon.setKgildirak2(vagon.getKgildirak2());
				 savedVagon.setKgildirak2Nomeri(vagon.getKgildirak2Nomeri());
				 savedVagon.setGildirak2(vagon.getGildirak2());
				 savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());

				 savedVagon.setKgildirak3(vagon.getKgildirak3());
				 savedVagon.setKgildirak3Nomeri(vagon.getKgildirak3Nomeri());
				 savedVagon.setGildirak3(vagon.getGildirak3());
				 savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());

				 savedVagon.setKgildirak4(vagon.getKgildirak4());
				 savedVagon.setKgildirak4Nomeri(vagon.getKgildirak4Nomeri());
				 savedVagon.setGildirak4(vagon.getGildirak4());
				 savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());

				 savedVagon.setCountry(vagon.getCountry());
				 savedVagon.setIzoh(vagon.getIzoh());

				LocalDateTime today = LocalDateTime.now();
				LocalDateTime minusHours = today.plusHours(5);
				DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
				andjDate = minusHours.format(myFormatObj);

				Optional<LastActionTimes> optionalMalumotTime = utyTimeRepository.findById(1);
				optionalMalumotTime.get().setAndjMalumotDate(andjDate);
				utyTimeRepository.save(optionalMalumotTime.get());

				return malumotRepository.save(savedVagon);

				}else {
					 return new VagonMalumot();
				}
		}


		@Override
		public VagonMalumot getVagonById(long id) {
		Optional<VagonMalumot> exist=	malumotRepository.findById(id);
		if(!exist.isPresent())
			return new VagonMalumot();
		return exist.get();
		}

		@Override
		public void deleteVagonById(long id) throws NotFoundException {
			Optional<VagonMalumot> exist=	malumotRepository.findById(id);
			if(exist.isPresent()) 
				malumotRepository.deleteById(id);

		}

		@Override
		public void deleteVagonSam(long id) throws NotFoundException {
			VagonMalumot exist=	malumotRepository.findById(id).get();	
			if(exist.getDepoNomi().equals("VCHD-6") ) {
				malumotRepository.deleteById(id);
				LocalDateTime today = LocalDateTime.now();
				LocalDateTime minusHours = today.plusHours(5);
				DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
				samDate = minusHours.format(myFormatObj);

				Optional<LastActionTimes> optionalMalumotTime = utyTimeRepository.findById(1);
				optionalMalumotTime.get().setSamMalumotDate(samDate);
				utyTimeRepository.save(optionalMalumotTime.get());
			}
		}

		@Override
		public void deleteVagonHav(long id) throws NotFoundException{
			VagonMalumot exist=	malumotRepository.findById(id).get();	
			if(exist.getDepoNomi().equals("VCHD-3") ) {
				malumotRepository.deleteById(id);
				LocalDateTime today = LocalDateTime.now();
				LocalDateTime minusHours = today.plusHours(5);
				DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
				havDate = minusHours.format(myFormatObj);

				Optional<LastActionTimes> optionalMalumotTime = utyTimeRepository.findById(1);
				optionalMalumotTime.get().setHavMalumotDate(havDate);
				utyTimeRepository.save(optionalMalumotTime.get());
			}
		}

		@Override
		public void deleteVagonAndj(long id) throws NotFoundException{
			VagonMalumot exist=	malumotRepository.findById(id).get();	
			if(exist.getDepoNomi().equals("VCHD-5") ) {
				malumotRepository.deleteById(id);
				LocalDateTime today = LocalDateTime.now();
				LocalDateTime minusHours = today.plusHours(5);
				DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
				andjDate = minusHours.format(myFormatObj);

				Optional<LastActionTimes> optionalMalumotTime = utyTimeRepository.findById(1);
				optionalMalumotTime.get().setAndjMalumotDate(andjDate);
				utyTimeRepository.save(optionalMalumotTime.get());
			}
		}

		@Override
		public VagonMalumot searchByNomer(Integer nomer) {
			Optional<VagonMalumot> exist=malumotRepository.findByNomer(nomer);
			if(exist.isPresent())
				return exist.get();
			else
				return null;
		}

//		filterniki
		@Override
		public List<VagonMalumot> filterByDate(String saqlanganVaqt) {
			return malumotRepository.filterByDate( saqlanganVaqt);
		}

		@Override
		public List<VagonMalumot> filterByCountry(String country) {
			return malumotRepository.filterByCountry( country);
		}

		@Override
		public List<VagonMalumot> filterByDepoNomi(String depoNomi) {
			return malumotRepository.filterByDepoNomi( depoNomi);
		}

		@Override
		public List<VagonMalumot> filterByCountryAndDate(String country, String saqlanganVaqt) {
			return malumotRepository.filterByCountryAndDate( country, saqlanganVaqt);
		}

		@Override
		public List<VagonMalumot> filterByDepoNomiAndCountry(String depoNomi, String country) {
			return malumotRepository.filterByDepoNomiAndCountry(depoNomi, country);
		}

		@Override
		public List<VagonMalumot> filterByDepoNomiAndDate(String depoNomi, String saqlanganVaqt) {
			return malumotRepository.filterByDepoNomiAndDate( depoNomi, saqlanganVaqt);
		}

		@Override
		public List<VagonMalumot> filterByDepoNomiCountryAndDate(String depoNomi, String country, String saqlanganVaqt) {
			return malumotRepository.filterByDepoNomiCountryAndDate( depoNomi, country, saqlanganVaqt);
		}

}
