package com.sms.serviceImp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.property.TextAlignment;
import com.sms.dto.PlanUtyDto;
import com.sms.model.PlanUty;
import com.sms.model.LastActionTimes;
import com.sms.repository.PlanUtyRepository;
import com.sms.repository.TimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.sms.model.VagonTayyorUty;
import com.sms.repository.VagonTayyorUtyRepository;
import com.sms.service.VagonTayyorUtyService;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class VagonTayyorUtyServiceImp implements VagonTayyorUtyService{

	@Autowired
	private VagonTayyorUtyRepository vagonTayyorUtyRepository;
	@Autowired
	private PlanUtyRepository planUtyRepository;
	@Autowired
	private TimeRepository utyTimeRepository;
	
	LocalDateTime today = LocalDateTime.now();
	LocalDateTime minusHours = today.plusHours(5);
	DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	String currentDate = minusHours.format(myFormatObj);
	String samDate ;
	String havDate ;
	String andjDate ;

	public String getCurrentDate() {
		return minusHours.format(myFormatObj);
	}

	public void createPdf(List<VagonTayyorUty> vagons, HttpServletResponse response) throws IOException {
		
		String home = System.getProperty("user.home");
		  File file = new File(home + "/Downloads" + "/O'TY rejasi boyicha ta'mir boyicha ma'lumot.pdf");
		  if (!file.getParentFile().exists())
		      file.getParentFile().mkdirs();
		  if (!file.exists())
		      file.createNewFile();
		List<VagonTayyorUty> allVagons = vagons;
		try {
			response.setHeader("Content-Disposition",
                    "attachment;fileName=\"" + "O'TY rejasi boyicha tamir boyicha malumot.pdf" +"\"");
			response.setContentType("application/pdf");
			
			PdfWriter writer = new PdfWriter(file.getAbsolutePath());
			PdfDocument pdfDoc = new PdfDocument(writer);
			Document doc = new Document(pdfDoc);

			String FONT_FILENAME = "./src/main/resources/arial.ttf";
			PdfFont font = PdfFontFactory.createFont(FONT_FILENAME, PdfEncodings.IDENTITY_H);
			doc.setFont(font);

			Paragraph paragraph = new Paragraph("Ta'mirdan chiqgan vagonlar(O'TY rejasi bo'yicha)");
			paragraph.setBackgroundColor(Color.DARK_GRAY);
			paragraph.setFontColor(Color.WHITE);// Setting background color to cell1
			paragraph.setBorder(Border.NO_BORDER);            // Setting border to cell1
			paragraph.setTextAlignment(TextAlignment.CENTER); // Setting text alignment to cell1
			paragraph.setFontSize(14);

			float[] columnWidth = {30f,200f,200f,200f,200f,200f,200f,200f,200f,200f};
			Table table = new Table(columnWidth);
			table.setTextAlignment(TextAlignment.CENTER);
			table.addCell(new Cell().add(" № "));
			table.addCell(new Cell().add("Nomeri"));
			table.addCell(new Cell().add("Vagon turi"));
			table.addCell(new Cell().add("VCHD"));
			table.addCell(new Cell().add("Ta'mir turi"));
			table.addCell(new Cell().add("Ishlab chiqarilgan yili"));
			table.addCell(new Cell().add("Ta'mirdan chiqgan vaqti"));
			table.addCell(new Cell().add("Saqlangan vaqti"));
			table.addCell(new Cell().add("Egasi"));
			table.addCell(new Cell().add("Izoh"));
			int i=0;
			for(VagonTayyorUty vagon:allVagons) {
				i++;
				table.addCell(new Cell().add(String.valueOf(i)));
				table.addCell(new Cell().add(String.valueOf(vagon.getNomer())));
				table.addCell(new Cell().add(vagon.getVagonTuri()));
				table.addCell(new Cell().add(vagon.getDepoNomi()));
				table.addCell(new Cell().add(vagon.getRemontTuri()));
				table.addCell(new Cell().add(String.valueOf(vagon.getIshlabChiqarilganYili())));
				table.addCell(new Cell().add(String.valueOf(vagon.getChiqganVaqti())));
				table.addCell(new Cell().add(String.valueOf(vagon.getCreatedDate())));
				table.addCell(new Cell().add(vagon.getCountry()));
				table.addCell(new Cell().add(vagon.getIzoh()));
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
	public void pdfFileTable(List<Integer> vagonsToDownloadAllTable, HttpServletResponse response) throws IOException {

		String home = System.getProperty("user.home");
		File file = new File(home + "/Downloads" + "/O'TY rejasi boyicha ta'mir boyicha ma'lumot (Jadval).pdf");
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		if (!file.exists())
			file.createNewFile();
		List<Integer> allVagons = vagonsToDownloadAllTable;
		try {
			response.setHeader("Content-Disposition",
					"attachment;fileName=\"" + "O'TY rejasi boyicha tamir boyicha malumot (Jadval).pdf" +"\"");
			response.setContentType("application/pdf");

			PdfWriter writer = new PdfWriter(file.getAbsolutePath());
			PdfDocument pdfDoc = new PdfDocument(writer);
			pdfDoc.setDefaultPageSize(PageSize.A4.rotate());
			Document doc = new Document(pdfDoc);

			String FONT_FILENAME = "./src/main/resources/arial.ttf";
			PdfFont font = PdfFontFactory.createFont(FONT_FILENAME, PdfEncodings.IDENTITY_H);
			doc.setFont(font);

			Paragraph paragraph = new Paragraph("Ta'mirdan chiqgan vagonlar(O'TY rejasi bo'yicha)");
			paragraph.setBackgroundColor(Color.DARK_GRAY);
			paragraph.setFontColor(Color.WHITE);// Setting background color to cell1
			paragraph.setBorder(Border.NO_BORDER);            // Setting border to cell1
			paragraph.setTextAlignment(TextAlignment.CENTER); // Setting text alignment to cell1
			paragraph.setFontSize(20);

			Paragraph paragraphDr = new Paragraph("Depo ta'mir(ДР)");
			paragraphDr.setTextAlignment(TextAlignment.CENTER); // Setting text alignment to cell1
			paragraphDr.setFontSize(16);



			float[] columnWidth = {200f,200f,200f,200f,200f,200f, 200f};
			Table table = new Table(columnWidth);
			table.setTextAlignment(TextAlignment.CENTER);

			table.addCell(new Cell().add("\n\n VCHD "));

//			Jami
			float[] columnWidth1 = {200f};
			Table table1 = new Table(columnWidth1);
			table1.addCell(new Cell().add("Jami \n . "));

			float[] columnWidth2 = {200f, 200f, 200f};
			Table table2 = new Table(columnWidth2);
			table2.addCell(new Cell().add("Plan"));
			table2.addCell(new Cell().add("Fact"));
			table2.addCell(new Cell().add("+/-"));

			table1.addCell(table2);
			table.addCell(table1);

//			Yopiq vagon
			Table table3 = new Table(columnWidth1);
			table3.addCell(new Cell().add("Yopiq  vagon (крыт)"));

			Table table4 = new Table(columnWidth2);
			table4.addCell(new Cell().add("Plan"));
			table4.addCell(new Cell().add("Fact"));
			table4.addCell(new Cell().add("+/-"));

			table3.addCell(table4);
			table.addCell(table3);

//			Platforma(пф)
			Table table5 = new Table(columnWidth1);
			table5.addCell(new Cell().add("Platforma \n (пф) "));

			Table table6 = new Table(columnWidth2);
			table6.addCell(new Cell().add("Plan"));
			table6.addCell(new Cell().add("Fact"));
			table6.addCell(new Cell().add("+/-"));

			table5.addCell(table6);
			table.addCell(table5);

//			Yarim ochiq vagon(пв)
			Table table7 = new Table(columnWidth1);
			table7.addCell(new Cell().add("Yarim ochiq vagon (пв)"));

			Table table8 = new Table(columnWidth2);
			table8.addCell(new Cell().add("Plan"));
			table8.addCell(new Cell().add("Fact"));
			table8.addCell(new Cell().add("+/-"));

			table7.addCell(table8);
			table.addCell(table7);

//			Sisterna(цс)
			Table table9 = new Table(columnWidth1);
			table9.addCell(new Cell().add("Sisterna \n (цс) "));

			Table table10 = new Table(columnWidth2);
			table10.addCell(new Cell().add("Plan"));
			table10.addCell(new Cell().add("Fact"));
			table10.addCell(new Cell().add("+/-"));

			table9.addCell(table10);
			table.addCell(table9);

//			Boshqa turdagi(проч)
			Table table11 = new Table(columnWidth1);
			table11.addCell(new Cell().add("Boshqa turdagi (проч)"));

			Table table12 = new Table(columnWidth2);
			table12.addCell(new Cell().add("Plan"));
			table12.addCell(new Cell().add("Fact"));
			table12.addCell(new Cell().add("+/-"));

			table11.addCell(table12);
			table.addCell(table11);

//VALUE LAR
			table.addCell("VCHD-3");
			//JAmi
			float[] columnWidth3 = {200f,200f,200f};
			Table table13 = new Table(columnWidth3);
			table13.addCell(new Cell().add(String.valueOf(allVagons.get(0))));
			table13.addCell(new Cell().add(String.valueOf(allVagons.get(1))));
			table13.addCell(new Cell().add(String.valueOf(allVagons.get(1) - allVagons.get(0))));

			table.addCell(table13);

			//Yopiq vagon (крыт)
			Table table14 = new Table(columnWidth3);
			table14.addCell(new Cell().add(String.valueOf(allVagons.get(2))));
			table14.addCell(new Cell().add(String.valueOf(allVagons.get(3))));
			table14.addCell(new Cell().add(String.valueOf(allVagons.get(3) - allVagons.get(2))));

			table.addCell(table14);

			//Platforma(пф)
			Table table15 = new Table(columnWidth3);
			table15.addCell(new Cell().add(String.valueOf(allVagons.get(4))));
			table15.addCell(new Cell().add(String.valueOf(allVagons.get(5))));
			table15.addCell(new Cell().add(String.valueOf(allVagons.get(5) - allVagons.get(4))));

			table.addCell(table15);
			
			//Yarim ochiq vagon(пв)
			Table table16 = new Table(columnWidth3);
			table16.addCell(new Cell().add(String.valueOf(allVagons.get(6))));
			table16.addCell(new Cell().add(String.valueOf(allVagons.get(7))));
			table16.addCell(new Cell().add(String.valueOf(allVagons.get(7) - allVagons.get(6))));

			table.addCell(table16);

			//Sisterna(цс)
			Table table17 = new Table(columnWidth3);
			table17.addCell(new Cell().add(String.valueOf(allVagons.get(8))));
			table17.addCell(new Cell().add(String.valueOf(allVagons.get(9))));
			table17.addCell(new Cell().add(String.valueOf(allVagons.get(9) - allVagons.get(8))));

			table.addCell(table17);

			//Boshqa turdagi(проч)
			Table table18 = new Table(columnWidth3);
			table18.addCell(new Cell().add(String.valueOf(allVagons.get(10))));
			table18.addCell(new Cell().add(String.valueOf(allVagons.get(11))));
			table18.addCell(new Cell().add(String.valueOf(allVagons.get(11) - allVagons.get(10))));

			table.addCell(table18);
			
			
			table.addCell("VCHD-5");
			//JAmi
			Table table19 = new Table(columnWidth3);
			table19.addCell(new Cell().add(String.valueOf(allVagons.get(12))));
			table19.addCell(new Cell().add(String.valueOf(allVagons.get(13))));
			table19.addCell(new Cell().add(String.valueOf(allVagons.get(13) - allVagons.get(12))));

			table.addCell(table19);

			//Yopiq vagon (крыт)
			Table table20 = new Table(columnWidth3);
			table20.addCell(new Cell().add(String.valueOf(allVagons.get(14))));
			table20.addCell(new Cell().add(String.valueOf(allVagons.get(15))));
			table20.addCell(new Cell().add(String.valueOf(allVagons.get(15) - allVagons.get(14))));

			table.addCell(table20);

			//Platforma(пф)
			Table table21 = new Table(columnWidth3);
			table21.addCell(new Cell().add(String.valueOf(allVagons.get(16))));
			table21.addCell(new Cell().add(String.valueOf(allVagons.get(17))));
			table21.addCell(new Cell().add(String.valueOf(allVagons.get(17) - allVagons.get(16))));

			table.addCell(table21);
			
			//Yarim ochiq vagon(пв)
			Table table22 = new Table(columnWidth3);
			table22.addCell(new Cell().add(String.valueOf(allVagons.get(18))));
			table22.addCell(new Cell().add(String.valueOf(allVagons.get(19))));
			table22.addCell(new Cell().add(String.valueOf(allVagons.get(19) - allVagons.get(18))));

			table.addCell(table22);

			//Sisterna(цс)
			Table table23 = new Table(columnWidth3);
			table23.addCell(new Cell().add(String.valueOf(allVagons.get(20))));
			table23.addCell(new Cell().add(String.valueOf(allVagons.get(21))));
			table23.addCell(new Cell().add(String.valueOf(allVagons.get(21) - allVagons.get(20))));

			table.addCell(table23);

			//Boshqa turdagi(проч)
			Table table24 = new Table(columnWidth3);
			table24.addCell(new Cell().add(String.valueOf(allVagons.get(22))));
			table24.addCell(new Cell().add(String.valueOf(allVagons.get(23))));
			table24.addCell(new Cell().add(String.valueOf(allVagons.get(23) - allVagons.get(22))));

			table.addCell(table24);


			table.addCell("VCHD-6");
			//JAmi
			Table table25 = new Table(columnWidth3);
			table25.addCell(new Cell().add(String.valueOf(allVagons.get(24))));
			table25.addCell(new Cell().add(String.valueOf(allVagons.get(25))));
			table25.addCell(new Cell().add(String.valueOf(allVagons.get(25) - allVagons.get(24))));

			table.addCell(table25);

			//Yopiq vagon (крыт)
			Table table26 = new Table(columnWidth3);
			table26.addCell(new Cell().add(String.valueOf(allVagons.get(26))));
			table26.addCell(new Cell().add(String.valueOf(allVagons.get(27))));
			table26.addCell(new Cell().add(String.valueOf(allVagons.get(27) - allVagons.get(26))));

			table.addCell(table26);

			//Platforma(пф)
			Table table27 = new Table(columnWidth3);
			table27.addCell(new Cell().add(String.valueOf(allVagons.get(28))));
			table27.addCell(new Cell().add(String.valueOf(allVagons.get(29))));
			table27.addCell(new Cell().add(String.valueOf(allVagons.get(29) - allVagons.get(28))));
			
			table.addCell(table27);

			//Yarim ochiq vagon(пв)
			Table table28 = new Table(columnWidth3);
			table28.addCell(new Cell().add(String.valueOf(allVagons.get(30))));
			table28.addCell(new Cell().add(String.valueOf(allVagons.get(31))));
			table28.addCell(new Cell().add(String.valueOf(allVagons.get(31) - allVagons.get(30))));

			table.addCell(table28);

			//Sisterna(цс)
			Table table29 = new Table(columnWidth3);
			table29.addCell(new Cell().add(String.valueOf(allVagons.get(32))));
			table29.addCell(new Cell().add(String.valueOf(allVagons.get(33))));
			table29.addCell(new Cell().add(String.valueOf(allVagons.get(33) - allVagons.get(32))));

			table.addCell(table29);

			//Boshqa turdagi(проч)
			Table table30 = new Table(columnWidth3);
			table30.addCell(new Cell().add(String.valueOf(allVagons.get(34))));
			table30.addCell(new Cell().add(String.valueOf(allVagons.get(35))));
			table30.addCell(new Cell().add(String.valueOf(allVagons.get(35) - allVagons.get(34))));

			table.addCell(table30);

			table.addCell("O'zvagonta'mir");
			//JAmi
			Table table31 = new Table(columnWidth3);
			table31.addCell(new Cell().add(String.valueOf(allVagons.get(36))));
			table31.addCell(new Cell().add(String.valueOf(allVagons.get(37))));
			table31.addCell(new Cell().add(String.valueOf(allVagons.get(37) - allVagons.get(36))));

			table.addCell(table31);

			//Yopiq vagon (крыт)
			Table table32 = new Table(columnWidth3);
			table32.addCell(new Cell().add(String.valueOf(allVagons.get(38))));
			table32.addCell(new Cell().add(String.valueOf(allVagons.get(39))));
			table32.addCell(new Cell().add(String.valueOf(allVagons.get(39) - allVagons.get(38))));

			table.addCell(table32);

			//Platforma(пф)
			Table table33 = new Table(columnWidth3);
			table33.addCell(new Cell().add(String.valueOf(allVagons.get(40))));
			table33.addCell(new Cell().add(String.valueOf(allVagons.get(41))));
			table33.addCell(new Cell().add(String.valueOf(allVagons.get(41) - allVagons.get(40))));
			
			table.addCell(table33);

			//Yarim ochiq vagon(пв)
			Table table34 = new Table(columnWidth3);
			table34.addCell(new Cell().add(String.valueOf(allVagons.get(42))));
			table34.addCell(new Cell().add(String.valueOf(allVagons.get(43))));
			table34.addCell(new Cell().add(String.valueOf(allVagons.get(43) - allVagons.get(42))));

			table.addCell(table34);

			//Sisterna(цс)
			Table table35 = new Table(columnWidth3);
			table35.addCell(new Cell().add(String.valueOf(allVagons.get(44))));
			table35.addCell(new Cell().add(String.valueOf(allVagons.get(45))));
			table35.addCell(new Cell().add(String.valueOf(allVagons.get(45) - allVagons.get(44))));

			table.addCell(table35);

			//Boshqa turdagi(проч)
			Table table36 = new Table(columnWidth3);
			table36.addCell(new Cell().add(String.valueOf(allVagons.get(46))));
			table36.addCell(new Cell().add(String.valueOf(allVagons.get(47))));
			table36.addCell(new Cell().add(String.valueOf(allVagons.get(47) - allVagons.get(46))));

			table.addCell(table36);


			Paragraph paragraphKr = new Paragraph("\n Kapital ta'mir(КР)");
			paragraphKr.setTextAlignment(TextAlignment.CENTER); // Setting text alignment to cell1
			paragraphKr.setFontSize(16);



			Table table37 = new Table(columnWidth);
			table37.setTextAlignment(TextAlignment.CENTER);
			table37.addCell(new Cell().add("\n\n VCHD "));

//			Jami
			Table table38 = new Table(columnWidth1);
			table38.addCell(new Cell().add("Jami \n . "));

			Table table39 = new Table(columnWidth2);
			table39.addCell(new Cell().add("Plan"));
			table39.addCell(new Cell().add("Fact"));
			table39.addCell(new Cell().add("+/-"));

			table38.addCell(table39);
			table37.addCell(table38);

//			Yopiq vagon
			Table table40 = new Table(columnWidth1);
			table40.addCell(new Cell().add("Yopiq  vagon (крыт)"));

			Table table41 = new Table(columnWidth2);
			table41.addCell(new Cell().add("Plan"));
			table41.addCell(new Cell().add("Fact"));
			table41.addCell(new Cell().add("+/-"));

			table40.addCell(table41);
			table37.addCell(table40);

//			Platforma(пф)
			Table table42 = new Table(columnWidth1);
			table42.addCell(new Cell().add("Platforma \n (пф) "));

			Table table43 = new Table(columnWidth2);
			table43.addCell(new Cell().add("Plan"));
			table43.addCell(new Cell().add("Fact"));
			table43.addCell(new Cell().add("+/-"));

			table42.addCell(table43);
			table37.addCell(table42);

//			Yarim ochiq vagon(пв)
			Table table44 = new Table(columnWidth1);
			table44.addCell(new Cell().add("Yarim ochiq vagon (пв)"));

			Table table45 = new Table(columnWidth2);
			table45.addCell(new Cell().add("Plan"));
			table45.addCell(new Cell().add("Fact"));
			table45.addCell(new Cell().add("+/-"));

			table44.addCell(table45);
			table37.addCell(table44);

//			Sisterna(цс)
			Table table46 = new Table(columnWidth1);
			table46.addCell(new Cell().add("Sisterna \n (цс) "));

			Table table47 = new Table(columnWidth2);
			table47.addCell(new Cell().add("Plan"));
			table47.addCell(new Cell().add("Fact"));
			table47.addCell(new Cell().add("+/-"));

			table46.addCell(table47);
			table37.addCell(table46);

//			Boshqa turdagi(проч)
			Table table48 = new Table(columnWidth1);
			table48.addCell(new Cell().add("Boshqa turdagi (проч)"));

			Table table49 = new Table(columnWidth2);
			table49.addCell(new Cell().add("Plan"));
			table49.addCell(new Cell().add("Fact"));
			table49.addCell(new Cell().add("+/-"));

			table48.addCell(table49);
			table37.addCell(table48);

//VALUE LAR
			table37.addCell("VCHD-3");
			//JAmi
			Table table50 = new Table(columnWidth3);
			table50.addCell(new Cell().add(String.valueOf(allVagons.get(48))));
			table50.addCell(new Cell().add(String.valueOf(allVagons.get(49))));
			table50.addCell(new Cell().add(String.valueOf(allVagons.get(49) - allVagons.get(48))));

			table37.addCell(table50);

			//Yopiq vagon (крыт)
			Table table51 = new Table(columnWidth3);
			table51.addCell(new Cell().add(String.valueOf(allVagons.get(50))));
			table51.addCell(new Cell().add(String.valueOf(allVagons.get(51))));
			table51.addCell(new Cell().add(String.valueOf(allVagons.get(51) - allVagons.get(50))));

			table37.addCell(table51);

			//Platforma(пф)
			Table table52 = new Table(columnWidth3);
			table52.addCell(new Cell().add(String.valueOf(allVagons.get(52))));
			table52.addCell(new Cell().add(String.valueOf(allVagons.get(53))));
			table52.addCell(new Cell().add(String.valueOf(allVagons.get(53) - allVagons.get(52))));

			table37.addCell(table52);

			//Yarim ochiq vagon(пв)
			Table table53 = new Table(columnWidth3);
			table53.addCell(new Cell().add(String.valueOf(allVagons.get(54))));
			table53.addCell(new Cell().add(String.valueOf(allVagons.get(55))));
			table53.addCell(new Cell().add(String.valueOf(allVagons.get(55) - allVagons.get(54))));

			table37.addCell(table53);

			//Sisterna(цс)
			Table table54 = new Table(columnWidth3);
			table54.addCell(new Cell().add(String.valueOf(allVagons.get(56))));
			table54.addCell(new Cell().add(String.valueOf(allVagons.get(57))));
			table54.addCell(new Cell().add(String.valueOf(allVagons.get(57) - allVagons.get(56))));

			table37.addCell(table54);

			//Boshqa turdagi(проч)
			Table table55 = new Table(columnWidth3);
			table55.addCell(new Cell().add(String.valueOf(allVagons.get(58))));
			table55.addCell(new Cell().add(String.valueOf(allVagons.get(59))));
			table55.addCell(new Cell().add(String.valueOf(allVagons.get(59) - allVagons.get(58))));

			table37.addCell(table55);


			table37.addCell("VCHD-5");
			//JAmi
			Table table56 = new Table(columnWidth3);
			table56.addCell(new Cell().add(String.valueOf(allVagons.get(60))));
			table56.addCell(new Cell().add(String.valueOf(allVagons.get(61))));
			table56.addCell(new Cell().add(String.valueOf(allVagons.get(61) - allVagons.get(60))));

			table37.addCell(table56);

			//Yopiq vagon (крыт)
			Table table57 = new Table(columnWidth3);
			table57.addCell(new Cell().add(String.valueOf(allVagons.get(62))));
			table57.addCell(new Cell().add(String.valueOf(allVagons.get(63))));
			table57.addCell(new Cell().add(String.valueOf(allVagons.get(63) - allVagons.get(62))));

			table37.addCell(table57);

			//Platforma(пф)
			Table table58 = new Table(columnWidth3);
			table58.addCell(new Cell().add(String.valueOf(allVagons.get(64))));
			table58.addCell(new Cell().add(String.valueOf(allVagons.get(65))));
			table58.addCell(new Cell().add(String.valueOf(allVagons.get(65) - allVagons.get(64))));

			table37.addCell(table58);

			//Yarim ochiq vagon(пв)
			Table table59 = new Table(columnWidth3);
			table59.addCell(new Cell().add(String.valueOf(allVagons.get(66))));
			table59.addCell(new Cell().add(String.valueOf(allVagons.get(67))));
			table59.addCell(new Cell().add(String.valueOf(allVagons.get(67) - allVagons.get(66))));

			table37.addCell(table59);

			//Sisterna(цс)
			Table table60 = new Table(columnWidth3);
			table60.addCell(new Cell().add(String.valueOf(allVagons.get(68))));
			table60.addCell(new Cell().add(String.valueOf(allVagons.get(69))));
			table60.addCell(new Cell().add(String.valueOf(allVagons.get(69) - allVagons.get(68))));

			table37.addCell(table60);

			//Boshqa turdagi(проч)
			Table table61 = new Table(columnWidth3);
			table61.addCell(new Cell().add(String.valueOf(allVagons.get(70))));
			table61.addCell(new Cell().add(String.valueOf(allVagons.get(71))));
			table61.addCell(new Cell().add(String.valueOf(allVagons.get(71) - allVagons.get(70))));

			table37.addCell(table61);


			table37.addCell("VCHD-6");
			
			//JAmi
			Table table62 = new Table(columnWidth3);
			table62.addCell(new Cell().add(String.valueOf(allVagons.get(72))));
			table62.addCell(new Cell().add(String.valueOf(allVagons.get(73))));
			table62.addCell(new Cell().add(String.valueOf(allVagons.get(73) - allVagons.get(72))));

			table37.addCell(table62);

			//Yopiq vagon (крыт)
			Table table63 = new Table(columnWidth3);
			table63.addCell(new Cell().add(String.valueOf(allVagons.get(74))));
			table63.addCell(new Cell().add(String.valueOf(allVagons.get(75))));
			table63.addCell(new Cell().add(String.valueOf(allVagons.get(75) - allVagons.get(74))));

			table37.addCell(table63);

			//Platforma(пф)
			Table table64 = new Table(columnWidth3);
			table64.addCell(new Cell().add(String.valueOf(allVagons.get(76))));
			table64.addCell(new Cell().add(String.valueOf(allVagons.get(77))));
			table64.addCell(new Cell().add(String.valueOf(allVagons.get(77) - allVagons.get(76))));

			table37.addCell(table64);

			//Yarim ochiq vagon(пв)
			Table table65 = new Table(columnWidth3);
			table65.addCell(new Cell().add(String.valueOf(allVagons.get(78))));
			table65.addCell(new Cell().add(String.valueOf(allVagons.get(79))));
			table65.addCell(new Cell().add(String.valueOf(allVagons.get(79) - allVagons.get(78))));

			table37.addCell(table65);

			//Sisterna(цс)
			Table table66 = new Table(columnWidth3);
			table66.addCell(new Cell().add(String.valueOf(allVagons.get(80))));
			table66.addCell(new Cell().add(String.valueOf(allVagons.get(81))));
			table66.addCell(new Cell().add(String.valueOf(allVagons.get(81) - allVagons.get(80))));

			table37.addCell(table66);

			//Boshqa turdagi(проч)
			Table table67 = new Table(columnWidth3);
			table67.addCell(new Cell().add(String.valueOf(allVagons.get(82))));
			table67.addCell(new Cell().add(String.valueOf(allVagons.get(83))));
			table67.addCell(new Cell().add(String.valueOf(allVagons.get(83) - allVagons.get(82))));

			table37.addCell(table67);

			table37.addCell("O'zvagonta'mir");
			
			//JAmi
			Table table68 = new Table(columnWidth3);
			table68.addCell(new Cell().add(String.valueOf(allVagons.get(84))));
			table68.addCell(new Cell().add(String.valueOf(allVagons.get(85))));
			table68.addCell(new Cell().add(String.valueOf(allVagons.get(85) - allVagons.get(84))));

			table37.addCell(table68);

			//Yopiq vagon (крыт)
			Table table69 = new Table(columnWidth3);
			table69.addCell(new Cell().add(String.valueOf(allVagons.get(86))));
			table69.addCell(new Cell().add(String.valueOf(allVagons.get(87))));
			table69.addCell(new Cell().add(String.valueOf(allVagons.get(87) - allVagons.get(86))));

			table37.addCell(table69);

			//Platforma(пф)
			Table table70 = new Table(columnWidth3);
			table70.addCell(new Cell().add(String.valueOf(allVagons.get(88))));
			table70.addCell(new Cell().add(String.valueOf(allVagons.get(89))));
			table70.addCell(new Cell().add(String.valueOf(allVagons.get(89) - allVagons.get(88))));

			table37.addCell(table70);

			//Yarim ochiq vagon(пв)
			Table table71 = new Table(columnWidth3);
			table71.addCell(new Cell().add(String.valueOf(allVagons.get(90))));
			table71.addCell(new Cell().add(String.valueOf(allVagons.get(91))));
			table71.addCell(new Cell().add(String.valueOf(allVagons.get(91) - allVagons.get(90))));

			table37.addCell(table71);

			//Sisterna(цс)
			Table table72 = new Table(columnWidth3);
			table72.addCell(new Cell().add(String.valueOf(allVagons.get(92))));
			table72.addCell(new Cell().add(String.valueOf(allVagons.get(93))));
			table72.addCell(new Cell().add(String.valueOf(allVagons.get(93) - allVagons.get(92))));

			table37.addCell(table72);

			//Boshqa turdagi(проч)
			Table table73 = new Table(columnWidth3);
			table73.addCell(new Cell().add(String.valueOf(allVagons.get(94))));
			table73.addCell(new Cell().add(String.valueOf(allVagons.get(95))));
			table73.addCell(new Cell().add(String.valueOf(allVagons.get(95) - allVagons.get(94))));

			table37.addCell(table73);

			Paragraph paragraphKrp = new Paragraph("KRP(КРП)");
			paragraphKrp.setTextAlignment(TextAlignment.CENTER); // Setting text alignment to cell1
			paragraphKrp.setFontSize(16);



			Table table74 = new Table(columnWidth);
			table74.setTextAlignment(TextAlignment.CENTER);
			table74.addCell(new Cell().add("\n\n VCHD "));

//			Jami
			Table table75 = new Table(columnWidth1);
			table75.addCell(new Cell().add("Jami \n . "));

			Table table76 = new Table(columnWidth2);
			table76.addCell(new Cell().add("Plan"));
			table76.addCell(new Cell().add("Fact"));
			table76.addCell(new Cell().add("+/-"));

			table75.addCell(table76);
			table74.addCell(table75);

//			Yopiq vagon
			Table table77 = new Table(columnWidth1);
			table77.addCell(new Cell().add("Yopiq  vagon (крыт)"));

			Table table78 = new Table(columnWidth2);
			table78.addCell(new Cell().add("Plan"));
			table78.addCell(new Cell().add("Fact"));
			table78.addCell(new Cell().add("+/-"));

			table77.addCell(table78);
			table74.addCell(table77);

//			Platforma(пф)
			Table table79 = new Table(columnWidth1);
			table79.addCell(new Cell().add("Platforma \n (пф) "));

			Table table80 = new Table(columnWidth2);
			table80.addCell(new Cell().add("Plan"));
			table80.addCell(new Cell().add("Fact"));
			table80.addCell(new Cell().add("+/-"));

			table79.addCell(table80);
			table74.addCell(table79);

//			Yarim ochiq vagon(пв)
			Table table81 = new Table(columnWidth1);
			table81.addCell(new Cell().add("Yarim ochiq vagon (пв)"));

			Table table82 = new Table(columnWidth2);
			table82.addCell(new Cell().add("Plan"));
			table82.addCell(new Cell().add("Fact"));
			table82.addCell(new Cell().add("+/-"));

			table81.addCell(table82);
			table74.addCell(table81);

//			Sisterna(цс)
			Table table83 = new Table(columnWidth1);
			table83.addCell(new Cell().add("Sisterna \n (цс) "));

			Table table84 = new Table(columnWidth2);
			table84.addCell(new Cell().add("Plan"));
			table84.addCell(new Cell().add("Fact"));
			table84.addCell(new Cell().add("+/-"));

			table83.addCell(table84);
			table74.addCell(table83);

//			Boshqa turdagi(проч)
			Table table85 = new Table(columnWidth1);
			table85.addCell(new Cell().add("Boshqa turdagi (проч)"));

			Table table86 = new Table(columnWidth2);
			table86.addCell(new Cell().add("Plan"));
			table86.addCell(new Cell().add("Fact"));
			table86.addCell(new Cell().add("+/-"));

			table85.addCell(table86);
			table74.addCell(table85);

//VALUE LAR
			table74.addCell("VCHD-3");
			//JAmi
			Table table87 = new Table(columnWidth3);
			table87.addCell(new Cell().add(String.valueOf(allVagons.get(96))));
			table87.addCell(new Cell().add(String.valueOf(allVagons.get(97))));
			table87.addCell(new Cell().add(String.valueOf(allVagons.get(97) - allVagons.get(96))));

			table74.addCell(table87);

			//Yopiq vagon (крыт)
			Table table88 = new Table(columnWidth3);
			table88.addCell(new Cell().add(String.valueOf(allVagons.get(98))));
			table88.addCell(new Cell().add(String.valueOf(allVagons.get(99))));
			table88.addCell(new Cell().add(String.valueOf(allVagons.get(99) - allVagons.get(98))));

			table74.addCell(table88);

			//Platforma(пф)
			Table table89 = new Table(columnWidth3);
			table89.addCell(new Cell().add(String.valueOf(allVagons.get(100))));
			table89.addCell(new Cell().add(String.valueOf(allVagons.get(101))));
			table89.addCell(new Cell().add(String.valueOf(allVagons.get(101) - allVagons.get(100))));

			table74.addCell(table89);

			//Yarim ochiq vagon(пв)
			Table table90 = new Table(columnWidth3);
			table90.addCell(new Cell().add(String.valueOf(allVagons.get(102))));
			table90.addCell(new Cell().add(String.valueOf(allVagons.get(103))));
			table90.addCell(new Cell().add(String.valueOf(allVagons.get(103) - allVagons.get(102))));

			table74.addCell(table90);

			//Sisterna(цс)
			Table table91 = new Table(columnWidth3);
			table91.addCell(new Cell().add(String.valueOf(allVagons.get(104))));
			table91.addCell(new Cell().add(String.valueOf(allVagons.get(105))));
			table91.addCell(new Cell().add(String.valueOf(allVagons.get(105) - allVagons.get(104))));

			table74.addCell(table91);

			//Boshqa turdagi(проч)
			Table table92 = new Table(columnWidth3);
			table92.addCell(new Cell().add(String.valueOf(allVagons.get(106))));
			table92.addCell(new Cell().add(String.valueOf(allVagons.get(107))));
			table92.addCell(new Cell().add(String.valueOf(allVagons.get(107) - allVagons.get(106))));

			table74.addCell(table92);


			table74.addCell("VCHD-5");
			//JAmi
			Table table93 = new Table(columnWidth3);
			table93.addCell(new Cell().add(String.valueOf(allVagons.get(108))));
			table93.addCell(new Cell().add(String.valueOf(allVagons.get(109))));
			table93.addCell(new Cell().add(String.valueOf(allVagons.get(109) - allVagons.get(108))));

			table74.addCell(table93);

			//Yopiq vagon (крыт)
			Table table94 = new Table(columnWidth3);
			table94.addCell(new Cell().add(String.valueOf(allVagons.get(110))));
			table94.addCell(new Cell().add(String.valueOf(allVagons.get(111))));
			table94.addCell(new Cell().add(String.valueOf(allVagons.get(111) - allVagons.get(110))));

			table74.addCell(table94);

			//Platforma(пф)
			Table table95 = new Table(columnWidth3);
			table95.addCell(new Cell().add(String.valueOf(allVagons.get(112))));
			table95.addCell(new Cell().add(String.valueOf(allVagons.get(113))));
			table95.addCell(new Cell().add(String.valueOf(allVagons.get(113) - allVagons.get(112))));

			table74.addCell(table95);

			//Yarim ochiq vagon(пв)
			Table table96 = new Table(columnWidth3);
			table96.addCell(new Cell().add(String.valueOf(allVagons.get(114))));
			table96.addCell(new Cell().add(String.valueOf(allVagons.get(115))));
			table96.addCell(new Cell().add(String.valueOf(allVagons.get(115) - allVagons.get(114))));

			table74.addCell(table96);

			//Sisterna(цс)
			Table table97 = new Table(columnWidth3);
			table97.addCell(new Cell().add(String.valueOf(allVagons.get(116))));
			table97.addCell(new Cell().add(String.valueOf(allVagons.get(117))));
			table97.addCell(new Cell().add(String.valueOf(allVagons.get(117) - allVagons.get(116))));

			table74.addCell(table97);

			//Boshqa turdagi(проч)
			Table table98 = new Table(columnWidth3);
			table98.addCell(new Cell().add(String.valueOf(allVagons.get(118))));
			table98.addCell(new Cell().add(String.valueOf(allVagons.get(119))));
			table98.addCell(new Cell().add(String.valueOf(allVagons.get(119) - allVagons.get(118))));

			table74.addCell(table98);


			table74.addCell("VCHD-6");

			//JAmi
			Table table99 = new Table(columnWidth3);
			table99.addCell(new Cell().add(String.valueOf(allVagons.get(120))));
			table99.addCell(new Cell().add(String.valueOf(allVagons.get(121))));
			table99.addCell(new Cell().add(String.valueOf(allVagons.get(121) - allVagons.get(120))));

			table74.addCell(table99);

			//Yopiq vagon (крыт)
			Table table100 = new Table(columnWidth3);
			table100.addCell(new Cell().add(String.valueOf(allVagons.get(122))));
			table100.addCell(new Cell().add(String.valueOf(allVagons.get(123))));
			table100.addCell(new Cell().add(String.valueOf(allVagons.get(123) - allVagons.get(122))));

			table74.addCell(table100);

			//Platforma(пф)
			Table table101 = new Table(columnWidth3);
			table101.addCell(new Cell().add(String.valueOf(allVagons.get(124))));
			table101.addCell(new Cell().add(String.valueOf(allVagons.get(125))));
			table101.addCell(new Cell().add(String.valueOf(allVagons.get(125) - allVagons.get(124))));

			table74.addCell(table101);

			//Yarim ochiq vagon(пв)
			Table table102 = new Table(columnWidth3);
			table102.addCell(new Cell().add(String.valueOf(allVagons.get(126))));
			table102.addCell(new Cell().add(String.valueOf(allVagons.get(127))));
			table102.addCell(new Cell().add(String.valueOf(allVagons.get(127) - allVagons.get(126))));

			table74.addCell(table102);

			//Sisterna(цс)
			Table table103 = new Table(columnWidth3);
			table103.addCell(new Cell().add(String.valueOf(allVagons.get(128))));
			table103.addCell(new Cell().add(String.valueOf(allVagons.get(129))));
			table103.addCell(new Cell().add(String.valueOf(allVagons.get(129) - allVagons.get(128))));

			table74.addCell(table103);

			//Boshqa turdagi(проч)
			Table table104 = new Table(columnWidth3);
			table104.addCell(new Cell().add(String.valueOf(allVagons.get(130))));
			table104.addCell(new Cell().add(String.valueOf(allVagons.get(131))));
			table104.addCell(new Cell().add(String.valueOf(allVagons.get(131) - allVagons.get(130))));

			table74.addCell(table104);

			table74.addCell("O'zvagonta'mir");

			//JAmi
			Table table105 = new Table(columnWidth3);
			table105.addCell(new Cell().add(String.valueOf(allVagons.get(132))));
			table105.addCell(new Cell().add(String.valueOf(allVagons.get(133))));
			table105.addCell(new Cell().add(String.valueOf(allVagons.get(133) - allVagons.get(132))));

			table74.addCell(table105);

			//Yopiq vagon (крыт)
			Table table106 = new Table(columnWidth3);
			table106.addCell(new Cell().add(String.valueOf(allVagons.get(134))));
			table106.addCell(new Cell().add(String.valueOf(allVagons.get(135))));
			table106.addCell(new Cell().add(String.valueOf(allVagons.get(135) - allVagons.get(134))));

			table74.addCell(table106);

			//Platforma(пф)
			Table table107 = new Table(columnWidth3);
			table107.addCell(new Cell().add(String.valueOf(allVagons.get(136))));
			table107.addCell(new Cell().add(String.valueOf(allVagons.get(137))));
			table107.addCell(new Cell().add(String.valueOf(allVagons.get(137) - allVagons.get(136))));

			table74.addCell(table107);

			//Yarim ochiq vagon(пв)
			Table table108 = new Table(columnWidth3);
			table108.addCell(new Cell().add(String.valueOf(allVagons.get(138))));
			table108.addCell(new Cell().add(String.valueOf(allVagons.get(139))));
			table108.addCell(new Cell().add(String.valueOf(allVagons.get(139) - allVagons.get(138))));

			table74.addCell(table108);

			//Sisterna(цс)
			Table table109 = new Table(columnWidth3);
			table109.addCell(new Cell().add(String.valueOf(allVagons.get(140))));
			table109.addCell(new Cell().add(String.valueOf(allVagons.get(141))));
			table109.addCell(new Cell().add(String.valueOf(allVagons.get(141) - allVagons.get(140))));

			table74.addCell(table109);

			//Boshqa turdagi(проч)
			Table table110 = new Table(columnWidth3);
			table110.addCell(new Cell().add(String.valueOf(allVagons.get(142))));
			table110.addCell(new Cell().add(String.valueOf(allVagons.get(143))));
			table110.addCell(new Cell().add(String.valueOf(allVagons.get(143) - allVagons.get(142))));

			table74.addCell(table110);

			doc.add(paragraph);
			doc.add(paragraphDr);
			doc.add(table);
			doc.add(paragraphKr);
			doc.add(table37);
			doc.add(paragraphKrp);
			doc.add(table74);
			doc.close();
			FileInputStream in = new FileInputStream(file.getAbsoluteFile());
			FileCopyUtils.copy(in, response.getOutputStream());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String getSamDate() {
		Optional<LastActionTimes> optionalUtyTime = utyTimeRepository.findById(1);
		if (!optionalUtyTime.isPresent())
			return getCurrentDate();
		return optionalUtyTime.get().getSamUtyDate();
	}

	public String getHavDate() {
		Optional<LastActionTimes> optionalUtyTime = utyTimeRepository.findById(1);
		if (!optionalUtyTime.isPresent())
			return getCurrentDate();
		return optionalUtyTime.get().getHavUtyDate();
	}

	public String getAndjDate() {
		Optional<LastActionTimes> optionalUtyTime = utyTimeRepository.findById(1);
		if (!optionalUtyTime.isPresent())
			return getCurrentDate();
		return optionalUtyTime.get().getAndjUtyDate();
	}

	// bosh admin qoshadi
	@Override
	public VagonTayyorUty saveVagon(VagonTayyorUty vagon) {	
		if(vagon.getNomer() == null)
			return new VagonTayyorUty();
		Optional<VagonTayyorUty> exist=	vagonTayyorUtyRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent())
			return new VagonTayyorUty();
		VagonTayyorUty savedVagon = new VagonTayyorUty();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setVagonTuri(vagon.getVagonTuri());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setIzoh(vagon.getIzoh());
		savedVagon.setCountry(vagon.getCountry());
		savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());
		savedVagon.setCountry("O'TY(ГАЖК)");
		savedVagon.setActive(true);

		String currentDate = minusHours.format(myFormatObj);
		savedVagon.setCreatedDate(currentDate);

		return vagonTayyorUtyRepository.save(savedVagon);	
	}

	@Override
	public VagonTayyorUty saveVagonSam(VagonTayyorUty vagon) {
		if(vagon.getNomer() == null)
			return new VagonTayyorUty();
		Optional<VagonTayyorUty> exist=	vagonTayyorUtyRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent() || !vagon.getDepoNomi().equalsIgnoreCase("VCHD-6"))
			return new VagonTayyorUty();
		VagonTayyorUty savedVagon = new VagonTayyorUty();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setVagonTuri(vagon.getVagonTuri());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setIzoh(vagon.getIzoh());
		savedVagon.setCountry(vagon.getCountry());
		savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());
		savedVagon.setCountry("O'TY(ГАЖК)");
		savedVagon.setActive(true);

		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		samDate = minusHours.format(myFormatObj);

		savedVagon.setCreatedDate(samDate);

		Optional<LastActionTimes> optionalUtyTime = utyTimeRepository.findById(1);
		optionalUtyTime.get().setSamUtyDate(samDate);
		utyTimeRepository.save(optionalUtyTime.get());

		return vagonTayyorUtyRepository.save(savedVagon);
	}
	
	@Override
	public VagonTayyorUty saveVagonHav(VagonTayyorUty vagon) {
		if(vagon.getNomer() == null)
			return new VagonTayyorUty();
		Optional<VagonTayyorUty> exist=	vagonTayyorUtyRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent() || !vagon.getDepoNomi().equalsIgnoreCase("VCHD-3"))
			return new VagonTayyorUty();
		VagonTayyorUty savedVagon = new VagonTayyorUty();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setVagonTuri(vagon.getVagonTuri());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setIzoh(vagon.getIzoh());
		savedVagon.setCountry(vagon.getCountry());
		savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());
		savedVagon.setCountry("O'TY(ГАЖК)");
		savedVagon.setActive(true);

		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		havDate = minusHours.format(myFormatObj);

		Optional<LastActionTimes> optionalUtyTime = utyTimeRepository.findById(1);
		savedVagon.setCreatedDate(havDate);
		optionalUtyTime.get().setHavUtyDate(havDate);
		utyTimeRepository.save(optionalUtyTime.get());

		return vagonTayyorUtyRepository.save(savedVagon);
	}

	@Override
	public VagonTayyorUty saveVagonAndj(VagonTayyorUty vagon) {
		if(vagon.getNomer() == null)
			return new VagonTayyorUty();
		Optional<VagonTayyorUty> exist= vagonTayyorUtyRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent() || !vagon.getDepoNomi().equalsIgnoreCase("VCHD-5"))
			return new VagonTayyorUty();
		VagonTayyorUty savedVagon = new VagonTayyorUty();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setVagonTuri(vagon.getVagonTuri());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setIzoh(vagon.getIzoh());
		savedVagon.setCountry(vagon.getCountry());
		savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());
		savedVagon.setCountry("O'TY(ГАЖК)");
		savedVagon.setActive(true);

		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		andjDate = minusHours.format(myFormatObj);

		Optional<LastActionTimes> optionalUtyTime = utyTimeRepository.findById(1);
		savedVagon.setCreatedDate(andjDate);
		optionalUtyTime.get().setAndjUtyDate(andjDate);
		utyTimeRepository.save(optionalUtyTime.get());

		return vagonTayyorUtyRepository.save(savedVagon);
}

	@Override
	public VagonTayyorUty updateVagon(VagonTayyorUty vagon, long id) {	
		if(vagon.getNomer() == null)
			return new VagonTayyorUty();
		 Optional<VagonTayyorUty> exist = vagonTayyorUtyRepository.findById(id);
		 if(!exist.isPresent() )
			 return new VagonTayyorUty();
		 VagonTayyorUty savedVagon = exist.get();
		 savedVagon.setId(id);
		 savedVagon.setNomer(vagon.getNomer());
		 savedVagon.setVagonTuri(vagon.getVagonTuri());
		 savedVagon.setDepoNomi(vagon.getDepoNomi());
		 savedVagon.setRemontTuri(vagon.getRemontTuri());
		 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		 savedVagon.setIzoh(vagon.getIzoh());
		 savedVagon.setCountry(vagon.getCountry());
		 savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());
		 savedVagon.setCountry("O'TY(ГАЖК)");

		 return vagonTayyorUtyRepository.save(savedVagon);
	}
	
	@Override
	public VagonTayyorUty updateVagonSam(VagonTayyorUty vagon, long id) {
		if(vagon.getNomer() == null)
			return new VagonTayyorUty();
		 Optional<VagonTayyorUty> exist = vagonTayyorUtyRepository.findById(id);
		 if(exist.get().getDepoNomi().equalsIgnoreCase("VCHD-6") && vagon.getDepoNomi().equalsIgnoreCase("VCHD-6")) {
			 VagonTayyorUty savedVagon = exist.get();
			 savedVagon.setId(id);
			 savedVagon.setNomer(vagon.getNomer());
			 savedVagon.setVagonTuri(vagon.getVagonTuri());
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setIzoh(vagon.getIzoh());
			 savedVagon.setCountry(vagon.getCountry());
			 savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());
   			 savedVagon.setCountry("O'TY(ГАЖК)");

			 LocalDateTime today = LocalDateTime.now();
			 LocalDateTime minusHours = today.plusHours(5);
			 DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			 samDate = minusHours.format(myFormatObj);

			 Optional<LastActionTimes> optionalUtyTime = utyTimeRepository.findById(1);
			 optionalUtyTime.get().setSamUtyDate(samDate);
			 utyTimeRepository.save(optionalUtyTime.get());

			 return vagonTayyorUtyRepository.save(savedVagon);
		 }else
			return new VagonTayyorUty();

	}

	@Override
	public VagonTayyorUty updateVagonHav(VagonTayyorUty vagon, long id) {
		if(vagon.getNomer() == null)
			return new VagonTayyorUty();
		 Optional<VagonTayyorUty> exist = vagonTayyorUtyRepository.findById(id);
		 if(exist.get().getDepoNomi().equalsIgnoreCase("VCHD-3") && vagon.getDepoNomi().equalsIgnoreCase("VCHD-3") ) {
			 
			 VagonTayyorUty savedVagon = exist.get();
			 savedVagon.setId(id);
			 savedVagon.setNomer(vagon.getNomer());
			 savedVagon.setVagonTuri(vagon.getVagonTuri());
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setIzoh(vagon.getIzoh());
			 savedVagon.setCountry(vagon.getCountry());
			 savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());
			 savedVagon.setCountry("O'TY(ГАЖК)");

			 Optional<LastActionTimes> optionalUtyTime = utyTimeRepository.findById(1);

			 LocalDateTime today = LocalDateTime.now();
			 LocalDateTime minusHours = today.plusHours(5);
			 DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			 havDate = minusHours.format(myFormatObj);
			 optionalUtyTime.get().setHavUtyDate(havDate);
			 utyTimeRepository.save(optionalUtyTime.get());

			 return vagonTayyorUtyRepository.save(savedVagon);
		 }else
			 return new VagonTayyorUty();
	}

	@Override
	public VagonTayyorUty updateVagonAndj(VagonTayyorUty vagon, long id) {
		if(vagon.getNomer() == null)
			return new VagonTayyorUty();
		 Optional<VagonTayyorUty> exist = vagonTayyorUtyRepository.findById(id);
		 if( exist.get().getDepoNomi().equalsIgnoreCase("VCHD-5") && vagon.getDepoNomi().equalsIgnoreCase("VCHD-5") ){	
			 VagonTayyorUty savedVagon = exist.get();
			 savedVagon.setId(id);
			 savedVagon.setNomer(vagon.getNomer());
			 savedVagon.setVagonTuri(vagon.getVagonTuri());
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setIzoh(vagon.getIzoh());
			 savedVagon.setCountry(vagon.getCountry());
			 savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());
			 savedVagon.setCountry("O'TY(ГАЖК)");

			 Optional<LastActionTimes> optionalUtyTime = utyTimeRepository.findById(1);

			 LocalDateTime today = LocalDateTime.now();
			 LocalDateTime minusHours = today.plusHours(5);
			 DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			 andjDate = minusHours.format(myFormatObj);
			 optionalUtyTime.get().setAndjUtyDate(andjDate);
			 utyTimeRepository.save(optionalUtyTime.get());

			 return vagonTayyorUtyRepository.save(savedVagon);
		}else {
				 return new VagonTayyorUty();
		}
	}

	//*update JAmi oylarniki
	@Override
	public VagonTayyorUty updateVagonMonths(VagonTayyorUty vagon, long id) {	
		if(vagon.getNomer() == null)
			return new VagonTayyorUty();
		 Optional<VagonTayyorUty> exist = vagonTayyorUtyRepository.findById(id);
		 if(!exist.isPresent() )
			 return new VagonTayyorUty();
		 VagonTayyorUty savedVagon = exist.get();
		 savedVagon.setId(id);
		 savedVagon.setNomer(vagon.getNomer());
		 savedVagon.setVagonTuri(vagon.getVagonTuri());
		 savedVagon.setDepoNomi(vagon.getDepoNomi());
		 savedVagon.setRemontTuri(vagon.getRemontTuri());
		 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		 savedVagon.setIzoh(vagon.getIzoh());
		 savedVagon.setCountry(vagon.getCountry());
		 savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());
		 savedVagon.setCountry("O'TY(ГАЖК)");

		 return vagonTayyorUtyRepository.save(savedVagon);
	}
	
	@Override
	public VagonTayyorUty updateVagonSamMonths(VagonTayyorUty vagon, long id) {
		if(vagon.getNomer() == null)
			return new VagonTayyorUty();
		 Optional<VagonTayyorUty> exist = vagonTayyorUtyRepository.findById(id);
		 if(exist.get().getDepoNomi().equalsIgnoreCase("VCHD-6") && vagon.getDepoNomi().equalsIgnoreCase("VCHD-6")) {
			 VagonTayyorUty savedVagon = exist.get();
			 savedVagon.setId(id);
			 savedVagon.setNomer(vagon.getNomer());
			 savedVagon.setVagonTuri(vagon.getVagonTuri());
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setIzoh(vagon.getIzoh());
			 savedVagon.setCountry(vagon.getCountry());
			 savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());
   			 savedVagon.setCountry("O'TY(ГАЖК)");

			 return vagonTayyorUtyRepository.save(savedVagon);
		 }else
			return new VagonTayyorUty();

	}

	@Override
	public VagonTayyorUty updateVagonHavMonths(VagonTayyorUty vagon, long id) {
		if(vagon.getNomer() == null)
			return new VagonTayyorUty();
		 Optional<VagonTayyorUty> exist = vagonTayyorUtyRepository.findById(id);
		 if(exist.get().getDepoNomi().equalsIgnoreCase("VCHD-3") && vagon.getDepoNomi().equalsIgnoreCase("VCHD-3") ) {
			 
			 VagonTayyorUty savedVagon = exist.get();
			 savedVagon.setId(id);
			 savedVagon.setNomer(vagon.getNomer());
			 savedVagon.setVagonTuri(vagon.getVagonTuri());
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setIzoh(vagon.getIzoh());
			 savedVagon.setCountry(vagon.getCountry());
			 savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());
			 savedVagon.setCountry("O'TY(ГАЖК)");

			 return vagonTayyorUtyRepository.save(savedVagon);
		 }else
			 return new VagonTayyorUty();
	}

	@Override
	public VagonTayyorUty updateVagonAndjMonths(VagonTayyorUty vagon, long id) {
		if(vagon.getNomer() == null)
			return new VagonTayyorUty();
		 Optional<VagonTayyorUty> exist = vagonTayyorUtyRepository.findById(id);
		 if( exist.get().getDepoNomi().equalsIgnoreCase("VCHD-5") && vagon.getDepoNomi().equalsIgnoreCase("VCHD-5") ){	
			 VagonTayyorUty savedVagon = exist.get();
			 savedVagon.setId(id);
			 savedVagon.setNomer(vagon.getNomer());
			 savedVagon.setVagonTuri(vagon.getVagonTuri());
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setIzoh(vagon.getIzoh());
			 savedVagon.setCountry(vagon.getCountry());
			 savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());
			 savedVagon.setCountry("O'TY(ГАЖК)");

			 return vagonTayyorUtyRepository.save(savedVagon);
		}else {
				 return new VagonTayyorUty();
		}
	}

	@Override
	public VagonTayyorUty getVagonById(long id) {
	Optional<VagonTayyorUty> exist=	vagonTayyorUtyRepository.findById(id);
	if(!exist.isPresent())
		return new VagonTayyorUty();
	return exist.get();
	}

	@Override
	public void deleteVagonById(long id) throws NotFoundException {
		Optional<VagonTayyorUty> exist=	vagonTayyorUtyRepository.findById(id);
		if(exist.isPresent()) 
			vagonTayyorUtyRepository.deleteById(id);

	}

	@Override
	public void deleteVagonSam(long id) throws NotFoundException {
		VagonTayyorUty exist=	vagonTayyorUtyRepository.findById(id).get();	
		if(exist.getDepoNomi().equals("VCHD-6") ) {
			vagonTayyorUtyRepository.deleteById(id);

			LocalDateTime today = LocalDateTime.now();
			LocalDateTime minusHours = today.plusHours(5);
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			samDate = minusHours.format(myFormatObj);

			Optional<LastActionTimes> optionalUtyTime = utyTimeRepository.findById(1);
			optionalUtyTime.get().setSamUtyDate(samDate);
			utyTimeRepository.save(optionalUtyTime.get());
		}
	}

	@Override
	public void deleteVagonHav(long id) throws NotFoundException{
		VagonTayyorUty exist=	vagonTayyorUtyRepository.findById(id).get();	
		if(exist.getDepoNomi().equals("VCHD-3") ) {
			vagonTayyorUtyRepository.deleteById(id);


			LocalDateTime today = LocalDateTime.now();
			LocalDateTime minusHours = today.plusHours(5);
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			Optional<LastActionTimes> optionalUtyTime = utyTimeRepository.findById(1);
			optionalUtyTime.get().setHavUtyDate(havDate);
			utyTimeRepository.save(optionalUtyTime.get());
		}
	}

	@Override
	public void deleteVagonAndj(long id) throws NotFoundException{
		VagonTayyorUty exist=	vagonTayyorUtyRepository.findById(id).get();	
		if(exist.getDepoNomi().equals("VCHD-5") ) {
			vagonTayyorUtyRepository.deleteById(id);


			LocalDateTime today = LocalDateTime.now();
			LocalDateTime minusHours = today.plusHours(5);
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			andjDate = minusHours.format(myFormatObj);
			Optional<LastActionTimes> optionalUtyTime = utyTimeRepository.findById(1);
			optionalUtyTime.get().setAndjUtyDate(andjDate);
			utyTimeRepository.save(optionalUtyTime.get());
		}
	}

	@Override
	public int countByDepoNomiVagonTuriAndTamirTuri(String depoNomi, String vagonTuri, String tamirTuri) {
		return vagonTayyorUtyRepository.countByDepoNomiVagonTuriAndTamirTuri(depoNomi, vagonTuri, tamirTuri);
	}

	@Override
	public List<VagonTayyorUty> findAll() {
		return vagonTayyorUtyRepository.findAll();
	}
	@Override
	public List<VagonTayyorUty> findAll(String oy) {
		Optional<LastActionTimes> byId = utyTimeRepository.findById(1);
		if (!byId.isPresent()) {
			LastActionTimes times = new LastActionTimes();

			times.setId(1);
			times.setSamQoldiqDate(getCurrentDate());
			times.setHavQoldiqDate(getCurrentDate());
			times.setAndjQoldiqDate(getCurrentDate());

			times.setSamMalumotDate(getCurrentDate());
			times.setHavMalumotDate(getCurrentDate());
			times.setAndjMalumotDate(getCurrentDate());

			times.setSamUtyDate(getCurrentDate());
			times.setHavUtyDate(getCurrentDate());
			times.setAndjUtyDate(getCurrentDate());

			times.setSamBiznesDate(getCurrentDate());
			times.setHavBiznesDate(getCurrentDate());
			times.setAndjBiznesDate(getCurrentDate());

			utyTimeRepository.save(times);
		}
		return vagonTayyorUtyRepository.findAll(oy);
	}
	
	@Override
	public int countAllActiveByDepoNomiVagonTuriAndTamirTuri(String depoNomi, String vagnTuri, 
			String tamirTuri, String oy) {
		return vagonTayyorUtyRepository.countAllActiveByDepoNomiVagonTuriAndTamirTuri(depoNomi, vagnTuri, tamirTuri, oy);
	}
	
	@Override
	public VagonTayyorUty searchByNomer(Integer nomer, String oy) {
		return vagonTayyorUtyRepository.searchByNomer(nomer, oy);
	}

	@Override
	public VagonTayyorUty findByNomer(Integer nomer) {
		Optional<VagonTayyorUty> optional = vagonTayyorUtyRepository.findByNomer(nomer);
		if (optional.isPresent())
			return optional.get();
		return new VagonTayyorUty();
	}
	
	// filterniki	
	@Override
	public List<VagonTayyorUty> findAllByDepoNomiAndVagonTuri(String depoNomi, String vagonTuri, String oy) {
		return vagonTayyorUtyRepository.findAllByDepoNomiAndVagonTuri(depoNomi, vagonTuri, oy);
	}

	@Override
	public List<VagonTayyorUty> findAllByDepoNomi(String depoNomi, String oy) {
		return vagonTayyorUtyRepository.findAllByDepoNomi(depoNomi, oy);
	}

	@Override
	public List<VagonTayyorUty> findAllByVagonTuri(String vagonTuri, String oy) {
		return vagonTayyorUtyRepository.findAllByVagonTuri(vagonTuri, oy);
	}

	//hamma oylarini filter uchn

	@Override
	public List<VagonTayyorUty> findByDepoNomiAndVagonTuri(String depoNomi, String vagonTuri) {
		return vagonTayyorUtyRepository.findByDepoNomiAndVagonTuri(depoNomi, vagonTuri);
	}

	@Override
	public List<VagonTayyorUty> findByDepoNomi(String depoNomi) {
		return vagonTayyorUtyRepository.findByDepoNomi(depoNomi);
	}

	@Override
	public List<VagonTayyorUty> findByVagonTuri(String vagonTuri) {
		return vagonTayyorUtyRepository.findByVagonTuri(vagonTuri);
	}
	

	@Override
	public void savePlan(PlanUtyDto planDto) {
		
		Optional<PlanUty> existsPlan = planUtyRepository.findById(1);

		if (!existsPlan.isPresent()) {

			PlanUty utyPlan = new PlanUty();
			utyPlan.setId(1);

			//bir oy uchun
			//Depoli tamir 
			utyPlan.setSamDtKritiPlanUty(planDto.getSamDtKritiPlanUty());
			utyPlan.setSamDtPlatformaPlanUty(planDto.getSamDtPlatformaPlanUty());
			utyPlan.setSamDtPoluvagonPlanUty(planDto.getSamDtPoluvagonPlanUty());
			utyPlan.setSamDtSisternaPlanUty(planDto.getSamDtSisternaPlanUty());
			utyPlan.setSamDtBoshqaPlanUty(planDto.getSamDtBoshqaPlanUty());
			
			utyPlan.setHavDtKritiPlanUty(planDto.getHavDtKritiPlanUty());
			utyPlan.setHavDtPlatformaPlanUty(planDto.getHavDtPlatformaPlanUty());
			utyPlan.setHavDtPoluvagonPlanUty(planDto.getHavDtPoluvagonPlanUty());
			utyPlan.setHavDtSisternaPlanUty(planDto.getHavDtSisternaPlanUty());
			utyPlan.setHavDtBoshqaPlanUty(planDto.getHavDtBoshqaPlanUty());
			
			utyPlan.setAndjDtKritiPlanUty(planDto.getAndjDtKritiPlanUty());
			utyPlan.setAndjDtPlatformaPlanUty(planDto.getAndjDtPlatformaPlanUty());
			utyPlan.setAndjDtPoluvagonPlanUty(planDto.getAndjDtPoluvagonPlanUty());
			utyPlan.setAndjDtSisternaPlanUty(planDto.getAndjDtSisternaPlanUty());
			utyPlan.setAndjDtBoshqaPlanUty(planDto.getAndjDtBoshqaPlanUty());
			
			//kapital tamir 
			utyPlan.setSamKtKritiPlanUty(planDto.getSamKtKritiPlanUty());
			utyPlan.setSamKtPlatformaPlanUty(planDto.getSamKtPlatformaPlanUty());
			utyPlan.setSamKtPoluvagonPlanUty(planDto.getSamKtPoluvagonPlanUty());
			utyPlan.setSamKtSisternaPlanUty(planDto.getSamKtSisternaPlanUty());
			utyPlan.setSamKtBoshqaPlanUty(planDto.getSamKtBoshqaPlanUty());
			
			utyPlan.setHavKtKritiPlanUty(planDto.getHavKtKritiPlanUty());
			utyPlan.setHavKtPlatformaPlanUty(planDto.getHavKtPlatformaPlanUty());
			utyPlan.setHavKtPoluvagonPlanUty(planDto.getHavKtPoluvagonPlanUty());
			utyPlan.setHavKtSisternaPlanUty(planDto.getHavKtSisternaPlanUty());
			utyPlan.setHavKtBoshqaPlanUty(planDto.getHavKtBoshqaPlanUty());
			
			utyPlan.setAndjKtKritiPlanUty(planDto.getAndjKtKritiPlanUty());
			utyPlan.setAndjKtPlatformaPlanUty(planDto.getAndjKtPlatformaPlanUty());
			utyPlan.setAndjKtPoluvagonPlanUty(planDto.getAndjKtPoluvagonPlanUty());
			utyPlan.setAndjKtSisternaPlanUty(planDto.getAndjKtSisternaPlanUty());
			utyPlan.setAndjKtBoshqaPlanUty(planDto.getAndjKtBoshqaPlanUty());
			
			//KRP tamir 
			utyPlan.setSamKrpKritiPlanUty(planDto.getSamKrpKritiPlanUty());
			utyPlan.setSamKrpPlatformaPlanUty(planDto.getSamKrpPlatformaPlanUty());
			utyPlan.setSamKrpPoluvagonPlanUty(planDto.getSamKrpPoluvagonPlanUty());
			utyPlan.setSamKrpSisternaPlanUty(planDto.getSamKrpSisternaPlanUty());
			utyPlan.setSamKrpBoshqaPlanUty(planDto.getSamKrpBoshqaPlanUty());
			
			utyPlan.setHavKrpKritiPlanUty(planDto.getHavKrpKritiPlanUty());
			utyPlan.setHavKrpPlatformaPlanUty(planDto.getHavKrpPlatformaPlanUty());
			utyPlan.setHavKrpPoluvagonPlanUty(planDto.getHavKrpPoluvagonPlanUty());
			utyPlan.setHavKrpSisternaPlanUty(planDto.getHavKrpSisternaPlanUty());
			utyPlan.setHavKrpBoshqaPlanUty(planDto.getHavKrpBoshqaPlanUty());
			
			utyPlan.setAndjKrpKritiPlanUty(planDto.getAndjKrpKritiPlanUty());
			utyPlan.setAndjKrpPlatformaPlanUty(planDto.getAndjKrpPlatformaPlanUty());
			utyPlan.setAndjKrpPoluvagonPlanUty(planDto.getAndjKrpPoluvagonPlanUty());
			utyPlan.setAndjKrpSisternaPlanUty(planDto.getAndjKrpSisternaPlanUty());
			utyPlan.setAndjKrpBoshqaPlanUty(planDto.getAndjKrpBoshqaPlanUty());

			//Jami oy uchun
			//Depoli tamir
			utyPlan.setSamDtKritiPlanUtyMonths(planDto.getSamDtKritiPlanUty());
			utyPlan.setSamDtPlatformaPlanUtyMonths(planDto.getSamDtPlatformaPlanUty());
			utyPlan.setSamDtPoluvagonPlanUtyMonths(planDto.getSamDtPoluvagonPlanUty());
			utyPlan.setSamDtSisternaPlanUtyMonths(planDto.getSamDtSisternaPlanUty());
			utyPlan.setSamDtBoshqaPlanUtyMonths(planDto.getSamDtBoshqaPlanUty());

			utyPlan.setHavDtKritiPlanUtyMonths(planDto.getHavDtKritiPlanUty());
			utyPlan.setHavDtPlatformaPlanUtyMonths(planDto.getHavDtPlatformaPlanUty());
			utyPlan.setHavDtPoluvagonPlanUtyMonths(planDto.getHavDtPoluvagonPlanUty());
			utyPlan.setHavDtSisternaPlanUtyMonths(planDto.getHavDtSisternaPlanUty());
			utyPlan.setHavDtBoshqaPlanUtyMonths(planDto.getHavDtBoshqaPlanUty());

			utyPlan.setAndjDtKritiPlanUtyMonths(planDto.getAndjDtKritiPlanUty());
			utyPlan.setAndjDtPlatformaPlanUtyMonths(planDto.getAndjDtPlatformaPlanUty());
			utyPlan.setAndjDtPoluvagonPlanUtyMonths(planDto.getAndjDtPoluvagonPlanUty());
			utyPlan.setAndjDtSisternaPlanUtyMonths(planDto.getAndjDtSisternaPlanUty());
			utyPlan.setAndjDtBoshqaPlanUtyMonths(planDto.getAndjDtBoshqaPlanUty());

			//kapital tamir
			utyPlan.setSamKtKritiPlanUtyMonths(planDto.getSamKtKritiPlanUty());
			utyPlan.setSamKtPlatformaPlanUtyMonths(planDto.getSamKtPlatformaPlanUty());
			utyPlan.setSamKtPoluvagonPlanUtyMonths(planDto.getSamKtPoluvagonPlanUty());
			utyPlan.setSamKtSisternaPlanUtyMonths(planDto.getSamKtSisternaPlanUty());
			utyPlan.setSamKtBoshqaPlanUtyMonths(planDto.getSamKtBoshqaPlanUty());

			utyPlan.setHavKtKritiPlanUtyMonths(planDto.getHavKtKritiPlanUty());
			utyPlan.setHavKtPlatformaPlanUtyMonths(planDto.getHavKtPlatformaPlanUty());
			utyPlan.setHavKtPoluvagonPlanUtyMonths(planDto.getHavKtPoluvagonPlanUty());
			utyPlan.setHavKtSisternaPlanUtyMonths(planDto.getHavKtSisternaPlanUty());
			utyPlan.setHavKtBoshqaPlanUtyMonths(planDto.getHavKtBoshqaPlanUty());

			utyPlan.setAndjKtKritiPlanUtyMonths(planDto.getAndjKtKritiPlanUty());
			utyPlan.setAndjKtPlatformaPlanUtyMonths(planDto.getAndjKtPlatformaPlanUty());
			utyPlan.setAndjKtPoluvagonPlanUtyMonths(planDto.getAndjKtPoluvagonPlanUty());
			utyPlan.setAndjKtSisternaPlanUtyMonths(planDto.getAndjKtSisternaPlanUty());
			utyPlan.setAndjKtBoshqaPlanUtyMonths(planDto.getAndjKtBoshqaPlanUty());

			//KRP tamir
			utyPlan.setSamKrpKritiPlanUtyMonths(planDto.getSamKrpKritiPlanUty());
			utyPlan.setSamKrpPlatformaPlanUtyMonths(planDto.getSamKrpPlatformaPlanUty());
			utyPlan.setSamKrpPoluvagonPlanUtyMonths(planDto.getSamKrpPoluvagonPlanUty());
			utyPlan.setSamKrpSisternaPlanUtyMonths(planDto.getSamKrpSisternaPlanUty());
			utyPlan.setSamKrpBoshqaPlanUtyMonths(planDto.getSamKrpBoshqaPlanUty());

			utyPlan.setHavKrpKritiPlanUtyMonths(planDto.getHavKrpKritiPlanUty());
			utyPlan.setHavKrpPlatformaPlanUtyMonths(planDto.getHavKrpPlatformaPlanUty());
			utyPlan.setHavKrpPoluvagonPlanUtyMonths(planDto.getHavKrpPoluvagonPlanUty());
			utyPlan.setHavKrpSisternaPlanUtyMonths(planDto.getHavKrpSisternaPlanUty());
			utyPlan.setHavKrpBoshqaPlanUtyMonths(planDto.getHavKrpBoshqaPlanUty());

			utyPlan.setAndjKrpKritiPlanUtyMonths(planDto.getAndjKrpKritiPlanUty());
			utyPlan.setAndjKrpPlatformaPlanUtyMonths(planDto.getAndjKrpPlatformaPlanUty());
			utyPlan.setAndjKrpPoluvagonPlanUtyMonths(planDto.getAndjKrpPoluvagonPlanUty());
			utyPlan.setAndjKrpSisternaPlanUtyMonths(planDto.getAndjKrpSisternaPlanUty());
			utyPlan.setAndjKrpBoshqaPlanUtyMonths(planDto.getAndjKrpBoshqaPlanUty());

			planUtyRepository.save(utyPlan);

		}else {
			PlanUty utyPlan = existsPlan.get();

			//bir oy uchun
			//Depoli tamir
			utyPlan.setSamDtKritiPlanUty(planDto.getSamDtKritiPlanUty());
			utyPlan.setSamDtPlatformaPlanUty(planDto.getSamDtPlatformaPlanUty());
			utyPlan.setSamDtPoluvagonPlanUty(planDto.getSamDtPoluvagonPlanUty());
			utyPlan.setSamDtSisternaPlanUty(planDto.getSamDtSisternaPlanUty());
			utyPlan.setSamDtBoshqaPlanUty(planDto.getSamDtBoshqaPlanUty());

			utyPlan.setHavDtKritiPlanUty(planDto.getHavDtKritiPlanUty());
			utyPlan.setHavDtPlatformaPlanUty(planDto.getHavDtPlatformaPlanUty());
			utyPlan.setHavDtPoluvagonPlanUty(planDto.getHavDtPoluvagonPlanUty());
			utyPlan.setHavDtSisternaPlanUty(planDto.getHavDtSisternaPlanUty());
			utyPlan.setHavDtBoshqaPlanUty(planDto.getHavDtBoshqaPlanUty());

			utyPlan.setAndjDtKritiPlanUty(planDto.getAndjDtKritiPlanUty());
			utyPlan.setAndjDtPlatformaPlanUty(planDto.getAndjDtPlatformaPlanUty());
			utyPlan.setAndjDtPoluvagonPlanUty(planDto.getAndjDtPoluvagonPlanUty());
			utyPlan.setAndjDtSisternaPlanUty(planDto.getAndjDtSisternaPlanUty());
			utyPlan.setAndjDtBoshqaPlanUty(planDto.getAndjDtBoshqaPlanUty());

			//kapital tamir
			utyPlan.setSamKtKritiPlanUty(planDto.getSamKtKritiPlanUty());
			utyPlan.setSamKtPlatformaPlanUty(planDto.getSamKtPlatformaPlanUty());
			utyPlan.setSamKtPoluvagonPlanUty(planDto.getSamKtPoluvagonPlanUty());
			utyPlan.setSamKtSisternaPlanUty(planDto.getSamKtSisternaPlanUty());
			utyPlan.setSamKtBoshqaPlanUty(planDto.getSamKtBoshqaPlanUty());

			utyPlan.setHavKtKritiPlanUty(planDto.getHavKtKritiPlanUty());
			utyPlan.setHavKtPlatformaPlanUty(planDto.getHavKtPlatformaPlanUty());
			utyPlan.setHavKtPoluvagonPlanUty(planDto.getHavKtPoluvagonPlanUty());
			utyPlan.setHavKtSisternaPlanUty(planDto.getHavKtSisternaPlanUty());
			utyPlan.setHavKtBoshqaPlanUty(planDto.getHavKtBoshqaPlanUty());

			utyPlan.setAndjKtKritiPlanUty(planDto.getAndjKtKritiPlanUty());
			utyPlan.setAndjKtPlatformaPlanUty(planDto.getAndjKtPlatformaPlanUty());
			utyPlan.setAndjKtPoluvagonPlanUty(planDto.getAndjKtPoluvagonPlanUty());
			utyPlan.setAndjKtSisternaPlanUty(planDto.getAndjKtSisternaPlanUty());
			utyPlan.setAndjKtBoshqaPlanUty(planDto.getAndjKtBoshqaPlanUty());

			//KRP tamir
			utyPlan.setSamKrpKritiPlanUty(planDto.getSamKrpKritiPlanUty());
			utyPlan.setSamKrpPlatformaPlanUty(planDto.getSamKrpPlatformaPlanUty());
			utyPlan.setSamKrpPoluvagonPlanUty(planDto.getSamKrpPoluvagonPlanUty());
			utyPlan.setSamKrpSisternaPlanUty(planDto.getSamKrpSisternaPlanUty());
			utyPlan.setSamKrpBoshqaPlanUty(planDto.getSamKrpBoshqaPlanUty());

			utyPlan.setHavKrpKritiPlanUty(planDto.getHavKrpKritiPlanUty());
			utyPlan.setHavKrpPlatformaPlanUty(planDto.getHavKrpPlatformaPlanUty());
			utyPlan.setHavKrpPoluvagonPlanUty(planDto.getHavKrpPoluvagonPlanUty());
			utyPlan.setHavKrpSisternaPlanUty(planDto.getHavKrpSisternaPlanUty());
			utyPlan.setHavKrpBoshqaPlanUty(planDto.getHavKrpBoshqaPlanUty());

			utyPlan.setAndjKrpKritiPlanUty(planDto.getAndjKrpKritiPlanUty());
			utyPlan.setAndjKrpPlatformaPlanUty(planDto.getAndjKrpPlatformaPlanUty());
			utyPlan.setAndjKrpPoluvagonPlanUty(planDto.getAndjKrpPoluvagonPlanUty());
			utyPlan.setAndjKrpSisternaPlanUty(planDto.getAndjKrpSisternaPlanUty());
			utyPlan.setAndjKrpBoshqaPlanUty(planDto.getAndjKrpBoshqaPlanUty());

			//Jami oy uchun
			//Depoli tamir
			utyPlan.setSamDtKritiPlanUtyMonths(utyPlan.getSamDtKritiPlanUtyMonths() + planDto.getSamDtKritiPlanUty());
			utyPlan.setSamDtPlatformaPlanUtyMonths(utyPlan.getSamDtPlatformaPlanUtyMonths() +planDto.getSamDtPlatformaPlanUty());
			utyPlan.setSamDtPoluvagonPlanUtyMonths(utyPlan.getSamDtPoluvagonPlanUtyMonths() +planDto.getSamDtPoluvagonPlanUty());
			utyPlan.setSamDtSisternaPlanUtyMonths(utyPlan.getSamDtSisternaPlanUtyMonths() +planDto.getSamDtSisternaPlanUty());
			utyPlan.setSamDtBoshqaPlanUtyMonths(utyPlan.getSamDtBoshqaPlanUtyMonths() +planDto.getSamDtBoshqaPlanUty());

			utyPlan.setHavDtKritiPlanUtyMonths(utyPlan.getHavDtKritiPlanUtyMonths() + planDto.getHavDtKritiPlanUty());
			utyPlan.setHavDtPlatformaPlanUtyMonths(utyPlan.getHavDtPlatformaPlanUtyMonths() +planDto.getHavDtPlatformaPlanUty());
			utyPlan.setHavDtPoluvagonPlanUtyMonths(utyPlan.getHavDtPoluvagonPlanUtyMonths() +planDto.getHavDtPoluvagonPlanUty());
			utyPlan.setHavDtSisternaPlanUtyMonths(utyPlan.getHavDtSisternaPlanUtyMonths() +planDto.getHavDtSisternaPlanUty());
			utyPlan.setHavDtBoshqaPlanUtyMonths(utyPlan.getHavDtBoshqaPlanUtyMonths() +planDto.getHavDtBoshqaPlanUty());

			utyPlan.setAndjDtKritiPlanUtyMonths(utyPlan.getAndjDtKritiPlanUtyMonths() + planDto.getAndjDtKritiPlanUty());
			utyPlan.setAndjDtPlatformaPlanUtyMonths(utyPlan.getAndjDtPlatformaPlanUtyMonths() +planDto.getAndjDtPlatformaPlanUty());
			utyPlan.setAndjDtPoluvagonPlanUtyMonths(utyPlan.getAndjDtPoluvagonPlanUtyMonths() +planDto.getAndjDtPoluvagonPlanUty());
			utyPlan.setAndjDtSisternaPlanUtyMonths(utyPlan.getAndjDtSisternaPlanUtyMonths() +planDto.getAndjDtSisternaPlanUty());
			utyPlan.setAndjDtBoshqaPlanUtyMonths(utyPlan.getAndjDtBoshqaPlanUtyMonths() +planDto.getAndjDtBoshqaPlanUty());

			//kapital tamir
			utyPlan.setSamKtKritiPlanUtyMonths(utyPlan.getSamKtKritiPlanUtyMonths() + planDto.getSamKtKritiPlanUty());
			utyPlan.setSamKtPlatformaPlanUtyMonths(utyPlan.getSamKtPlatformaPlanUtyMonths() +planDto.getSamKtPlatformaPlanUty());
			utyPlan.setSamKtPoluvagonPlanUtyMonths(utyPlan.getSamKtPoluvagonPlanUtyMonths() +planDto.getSamKtPoluvagonPlanUty());
			utyPlan.setSamKtSisternaPlanUtyMonths(utyPlan.getSamKtSisternaPlanUtyMonths() +planDto.getSamKtSisternaPlanUty());
			utyPlan.setSamKtBoshqaPlanUtyMonths(utyPlan.getSamKtBoshqaPlanUtyMonths() +planDto.getSamKtBoshqaPlanUty());

			utyPlan.setHavKtKritiPlanUtyMonths(utyPlan.getHavKtKritiPlanUtyMonths() + planDto.getHavKtKritiPlanUty());
			utyPlan.setHavKtPlatformaPlanUtyMonths(utyPlan.getHavKtPlatformaPlanUtyMonths() +planDto.getHavKtPlatformaPlanUty());
			utyPlan.setHavKtPoluvagonPlanUtyMonths(utyPlan.getHavKtPoluvagonPlanUtyMonths() +planDto.getHavKtPoluvagonPlanUty());
			utyPlan.setHavKtSisternaPlanUtyMonths(utyPlan.getHavKtSisternaPlanUtyMonths() +planDto.getHavKtSisternaPlanUty());
			utyPlan.setHavKtBoshqaPlanUtyMonths(utyPlan.getHavKtBoshqaPlanUtyMonths() +planDto.getHavKtBoshqaPlanUty());

			utyPlan.setAndjKtKritiPlanUtyMonths(utyPlan.getAndjKtKritiPlanUtyMonths() + planDto.getAndjKtKritiPlanUty());
			utyPlan.setAndjKtPlatformaPlanUtyMonths(utyPlan.getAndjKtPlatformaPlanUtyMonths() +planDto.getAndjKtPlatformaPlanUty());
			utyPlan.setAndjKtPoluvagonPlanUtyMonths(utyPlan.getAndjKtPoluvagonPlanUtyMonths() +planDto.getAndjKtPoluvagonPlanUty());
			utyPlan.setAndjKtSisternaPlanUtyMonths(utyPlan.getAndjKtSisternaPlanUtyMonths() +planDto.getAndjKtSisternaPlanUty());
			utyPlan.setAndjKtBoshqaPlanUtyMonths(utyPlan.getAndjKtBoshqaPlanUtyMonths() +planDto.getAndjKtBoshqaPlanUty());

			//KRP tamir
			utyPlan.setSamKrpKritiPlanUtyMonths(utyPlan.getSamKrpKritiPlanUtyMonths() + planDto.getSamKrpKritiPlanUty());
			utyPlan.setSamKrpPlatformaPlanUtyMonths(utyPlan.getSamKrpPlatformaPlanUtyMonths() +planDto.getSamKrpPlatformaPlanUty());
			utyPlan.setSamKrpPoluvagonPlanUtyMonths(utyPlan.getSamKrpPoluvagonPlanUtyMonths() +planDto.getSamKrpPoluvagonPlanUty());
			utyPlan.setSamKrpSisternaPlanUtyMonths(utyPlan.getSamKrpSisternaPlanUtyMonths() +planDto.getSamKrpSisternaPlanUty());
			utyPlan.setSamKrpBoshqaPlanUtyMonths(utyPlan.getSamKrpBoshqaPlanUtyMonths() +planDto.getSamKrpBoshqaPlanUty());

			utyPlan.setHavKrpKritiPlanUtyMonths(utyPlan.getHavKrpKritiPlanUtyMonths() + planDto.getHavKrpKritiPlanUty());
			utyPlan.setHavKrpPlatformaPlanUtyMonths(utyPlan.getHavKrpPlatformaPlanUtyMonths() +planDto.getHavKrpPlatformaPlanUty());
			utyPlan.setHavKrpPoluvagonPlanUtyMonths(utyPlan.getHavKrpPoluvagonPlanUtyMonths() +planDto.getHavKrpPoluvagonPlanUty());
			utyPlan.setHavKrpSisternaPlanUtyMonths(utyPlan.getHavKrpSisternaPlanUtyMonths() +planDto.getHavKrpSisternaPlanUty());
			utyPlan.setHavKrpBoshqaPlanUtyMonths(utyPlan.getHavKrpBoshqaPlanUtyMonths() +planDto.getHavKrpBoshqaPlanUty());

			utyPlan.setAndjKrpKritiPlanUtyMonths(utyPlan.getAndjKrpKritiPlanUtyMonths() + planDto.getAndjKrpKritiPlanUty());
			utyPlan.setAndjKrpPlatformaPlanUtyMonths(utyPlan.getAndjKrpPlatformaPlanUtyMonths() +planDto.getAndjKrpPlatformaPlanUty());
			utyPlan.setAndjKrpPoluvagonPlanUtyMonths(utyPlan.getAndjKrpPoluvagonPlanUtyMonths() +planDto.getAndjKrpPoluvagonPlanUty());
			utyPlan.setAndjKrpSisternaPlanUtyMonths(utyPlan.getAndjKrpSisternaPlanUtyMonths() +planDto.getAndjKrpSisternaPlanUty());
			utyPlan.setAndjKrpBoshqaPlanUtyMonths(utyPlan.getAndjKrpBoshqaPlanUtyMonths() +planDto.getAndjKrpBoshqaPlanUty());

			planUtyRepository.save(utyPlan);
		}
	}

	@Override
	public PlanUty getPlanuty() {
		Optional<PlanUty> optionalPlanUty = planUtyRepository.findById(1);
		if (optionalPlanUty.isPresent())
			return optionalPlanUty.get();
		return new PlanUty();
	}


	@Override
	public VagonTayyorUty findById(Long id) {
		return vagonTayyorUtyRepository.findById(id).get();
	}
}
