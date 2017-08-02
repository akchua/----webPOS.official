package com.chua.evergrocery.utility.print;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Kevin Roy K. Chua
 *
 * @version 1.0, Jul 9, 2016
 * @since 1.0, Jul 9, 2016
 *
 */
public class PrintableDoc
		implements Printable
{	
	private static final String TOTAL_AMOUNT = "Total Amount";
	
	private static final String CHANGE = "Change";
	
	public final static double DEFAULT_MARGIN = 0.5 * 40.0; //points
	
	public final static double DEFAULT_PAGE_WIDTH = 8.5 * 40.0; //points
  
	public final static double DEFAULT_PAGE_HEIGHT = 11.0 * 40.0; //points
	
	public static final String DEFAULT_FONT_NAME = "Lucida Console";
	
	public static final int FONT_SIZE_11 = 11 ;
	
	public static final int FONT_SIZE_13 = 13;
	
	public static final int FONT_SIZE_15 = 15;
	
	/** This is the hard coded and measured lines per page on a Lucida Console Font 11  */
	public static final int LINES_PER_PAGE = 76;
	
	private final double margin;
	
	private final double pageWidth;
	
	private final double pageHeight;
	
	private final String fontName;
	
	private final String text;
	
	private String labNumber;
	
	/**
	 * Create a new printable doc with the given text to display.
	 * 
	 * @param text the text
	 */
	public PrintableDoc(String text, String labNumber)
	{
		this.margin = DEFAULT_MARGIN;
		this.pageWidth = DEFAULT_PAGE_WIDTH;
		if(StringUtils.isNotBlank(text))
		{
			this.pageHeight = 11.0 * (text.split("\n").length + 25);
		}
		else
		{
			this.pageHeight = 0;
		}
		this.fontName = DEFAULT_FONT_NAME;
		this.text = text;
		this.labNumber = labNumber;
	}    		
	
	/**
	 * Create a new printable doc with the given parameters.
	 * 
	 * @param margin the margin
	 * @param pageWidth the page width
	 * @param pageHeight the page height
	 * @param fontName the font name
	 * @param text the text
	 */
	public PrintableDoc(double margin, double pageWidth, double pageHeight, String fontName, String text)
	{
		this.margin = margin;
		this.pageWidth = pageWidth;
		this.pageHeight = pageHeight;
		this.fontName = fontName;
		this.text = text;
	}
	
  ArrayList<TextLayout> textLayouts = new ArrayList<TextLayout>();

  public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
	  String [] splitArray = text.split("\n");     
      int length = splitArray.length;
      int totalPageCount = length / LINES_PER_PAGE;
      int remainder = length % LINES_PER_PAGE;
      if(totalPageCount > 0 && remainder == 0) {
    	  totalPageCount--;  
      }      
      
      //System.out.println("page index: " + pageIndex);
      if (pageIndex > totalPageCount) {
          return Printable.NO_SUCH_PAGE;
      }

      Graphics2D g2d = (Graphics2D) graphics;
            
      g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
      FontRenderContext frc = g2d.getFontRenderContext();
      textLayouts = new ArrayList<TextLayout>();
      //if (textLayouts.isEmpty()) {  //only build text Layouts the first time                              
          int lowerLimit = pageIndex * LINES_PER_PAGE;
          int upperLimit = (pageIndex + 1) * LINES_PER_PAGE;            
          for(int c = lowerLimit; c < (length > upperLimit ? upperLimit : length); c++)
          {
          	String split = splitArray[c];
          	if(c == 0)
          	{
          		buildTextLayouts(frc, fontName, FONT_SIZE_15, split + " ", true, false);
          	}
          	else if(split.contains(TOTAL_AMOUNT) || split.contains(CHANGE))
          	{
          		buildTextLayouts(frc, fontName, FONT_SIZE_13, split + " ", true, false);
          	}
          	else
          	{
          		buildTextLayouts(frc, fontName, FONT_SIZE_11, split + " ", false, false);
          	}
          }          
      //}
          
	  drawText(g2d);
      return Printable.PAGE_EXISTS;
  }

  private float drawText(Graphics2D g2d) {
      //float leftMargin = (float) (1.0 * 72);
      float yOffset = 0; // 1" top margin
      for (TextLayout tl : textLayouts) {
          yOffset += tl.getAscent();
          tl.draw(g2d, 0, yOffset);
          yOffset += tl.getDescent();
          yOffset += tl.getLeading();
      }
      return yOffset;
  }

  private void buildTextLayouts(FontRenderContext frc, String fontName, int fontSize, String text, boolean bold, boolean italic) {
      //actually build the layout if it does not exist
      AttributedString attrStr = makeAttributedString(
              fontName,
              fontSize,
              text,
              bold,
              italic);
      AttributedCharacterIterator paragraph = attrStr.getIterator();
      LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);
      lineMeasurer.setPosition(paragraph.getBeginIndex());
      int end = paragraph.getEndIndex();
      while (lineMeasurer.getPosition() < end) {
          //compute line width to account for indents            	
          double lineWidth = pageWidth - 2 * margin;          
          TextLayout l = lineMeasurer.nextLayout((float) lineWidth);
          textLayouts.add(l);
      }
  }

  private AttributedString makeAttributedString(String fontName, int fontSize, String text, boolean bold, boolean italic) {
      AttributedString str = new AttributedString(text);      
      //now apply attributes for each block
      int start = 0;
      int end = text.length();      
      Map attributes = getFontAttributes(fontName, fontSize, bold, italic);
      str.addAttributes(attributes, start, end);
      return str;
  }

  private Map getFontAttributes(String fontname, int size, boolean bold, boolean italic) {
      //if CODE, override font name
      //set attributes
      Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
      map.put(TextAttribute.FAMILY, fontname);
      map.put(TextAttribute.SIZE, size);
      map.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
      map.put(TextAttribute.WIDTH, TextAttribute.WIDTH_CONDENSED);
      
      if (bold) {
          map.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
      }

      if (italic) {
          map.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
      }
      
      return map;
  }
  
  public double getMargin()
  {
  	return margin;
  }  
  
  public double getPageWidth()
  {
  	return pageWidth;
  }
  
  public double getPageHeight()
  {
  	return pageHeight;
  }
  
  public String getText()
  {
  	return text;
  }
  
  public String getLabNumber()
  {
	  return labNumber;
  }
}