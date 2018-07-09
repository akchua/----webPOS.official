package com.chua.evergrocery.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * This is a utility class used for writing in Portable Document Format (.pdf)
 * The default font used is Courier with font size 9
 * 
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 23, 2016
 */
public class SimplePdfWriter {
	
	/**
	 * This internal class is used for paging
	 */
	static class Pager extends PdfPageEventHelper {
		
		private String footnote;
		
		Font font;
        PdfTemplate t;
        Image total;
        
        public Pager(String footnote) {
        	super();
        	this.footnote = footnote;
        }
 
        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            t = writer.getDirectContent().createTemplate(30, 16);
            try {
                total = Image.getInstance(t);
                total.setRole(PdfName.ARTIFACT);
                font =  defaultFont;
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }
 
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable table = new PdfPTable(3);
            try {
                table.setWidths(new int[]{24, 24, 2});
                table.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
                table.getDefaultCell().setFixedHeight(20);
                table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                table.addCell(new Phrase(footnote, font));
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(new Phrase(String.format("Page %d of", writer.getPageNumber()), font));
                PdfPCell cell = new PdfPCell(total);
                cell.setBorder(Rectangle.BOTTOM);
                table.addCell(cell);
                PdfContentByte canvas = writer.getDirectContent();
                canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
                table.writeSelectedRows(0, -1, 36, 30, canvas);
                canvas.endMarkedContentSequence();
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }
 
        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
                new Phrase(String.valueOf(writer.getPageNumber()), font),
                2, 4, 0);
        }
    }
	
	/**
	 * The default font
	 * 
	 * Courier font is used for monospacing
	 * Font size 9 in landscape can contain at most 133 characters
	 * Font size 9 in portrait can contain at most 96 characters
	 */
	private static final Font defaultFont = new Font(Font.FontFamily.COURIER, 9, Font.NORMAL);
	
	@SuppressWarnings("unused")
	private static final Paragraph newLine = new Paragraph(" ");
	
	/**
	 * This class does not need to be instantiated
	 */
	private SimplePdfWriter() {
		
	}
	
	/**
	 * This static method is used to write a Java String to a pdf file
	 * 
	 * @param message The message to be written
	 * @param path The path where the file will be created
	 * @param landscape True if orientation is landscape
	 */
	public static boolean write(String message, String footnote, String path, boolean landscape) {
		boolean success = true;
		
		try {
			File file = new File(path);
			if(file.getParentFile() != null) file.getParentFile().mkdirs();
			
			Document document = new Document();
			if(landscape) document.setPageSize(PageSize.LETTER.rotate());
			
			PdfWriter writer;
				writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			
			Pager pager = new Pager(footnote);
			writer.setPageEvent(pager);
			
			document.open();
			
			Paragraph paragraph = new Paragraph(message, defaultFont);
			document.add(paragraph);
			
			document.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			success = false;
		} catch (DocumentException e) {
			e.printStackTrace();
			success = false;
		}
		
		return success;
	}
	
	/**
	 * This method checks if the given path specifies a pdf file.
	 * If not then it will append .pdf at the end of the path
	 * @param path The path to be checked
	 * @return The converted path
	 */
	public static String convertPathToPdf(String path) {
		String[] tokens = path.split("\\.");
		if(!tokens[tokens.length - 1].equals("pdf")) {
			path += ".pdf";
		}
		return path;
	}
}
