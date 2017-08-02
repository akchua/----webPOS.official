package com.chua.evergrocery.utility.print;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Arrays;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.chua.evergrocery.constants.PrintConstants;

/**
 * @author Kevin Roy K. Chua
 *
 * @version 1.0, Jul 9, 2016
 * @since 1.0, Jul 9, 2016
 *
 */
public class Printer
{
	private DocFlavor serviceDocFlavor;
	private DocFlavor printDocFlavor;
	private PrintRequestAttributeSet printRequestAttributeSet;
	private DocAttributeSet docAttributeSet;
	private String serviceName;			
	
	public Printer() 
	{
		this.setServiceDocFlavor(DocFlavor.SERVICE_FORMATTED.PRINTABLE);
		this.setPrintDocFlavor(DocFlavor.BYTE_ARRAY.AUTOSENSE);
		this.setPrintRequestAttributeSet(new HashPrintRequestAttributeSet());
		this.setDocAttributeSet(new HashDocAttributeSet());
		this.setServiceName(PrintConstants.LOCAL_PRINTER_SERVICE);		
	}
	
	public Printer(HttpServletRequest request, ServletContext servletContext)
	{
		this.setServiceDocFlavor(DocFlavor.SERVICE_FORMATTED.PRINTABLE);
		this.setPrintDocFlavor(DocFlavor.BYTE_ARRAY.AUTOSENSE);
		this.setPrintRequestAttributeSet(new HashPrintRequestAttributeSet());
		this.setDocAttributeSet(new HashDocAttributeSet());
		
		String serviceName = "";		
		/*try {
			List<PrinterBean> printerList = PrinterUtil.getConfigurations(new File(servletContext.getRealPath(""), PrintConstants.PRINTER_CONFIG));
			String remoteHost = request.getRemoteHost();			
			for(PrinterBean printer : printerList)
			{
				if(printer.getIpAddress().equalsIgnoreCase(remoteHost) || printer.getComputerName().equalsIgnoreCase(remoteHost))
				{
					serviceName = printer.getPrintService();
					break;
				}
			}
		} catch(Exception e) {}*/		
		//this.setServiceName("\\\\" + RequestUtil.getRemoteComputerName(request) + "\\" + PrintConstant.HI_PRECISION_PRINTER_SERVICE);
		this.setServiceName(serviceName);
	}
	
	public Printer(DocFlavor serviceDocFlavor, DocFlavor printDocFlavor, PrintRequestAttributeSet printRequestAttributeSet, DocAttributeSet docAttributeSet, String serviceName)
	{
		this.setServiceDocFlavor(serviceDocFlavor);
		this.setPrintDocFlavor(printDocFlavor);
		this.setPrintRequestAttributeSet(printRequestAttributeSet);
		this.setDocAttributeSet(docAttributeSet);
		this.setServiceName(serviceName);
	}
	
	/**
	 * @param serviceDocFlavor the serviceDocFlavor to set
	 */
	public void setServiceDocFlavor(DocFlavor serviceDocFlavor)
	{
		this.serviceDocFlavor = serviceDocFlavor;
	}

	/**
	 * @return the serviceDocFlavor
	 */
	public DocFlavor getServiceDocFlavor()
	{
		return serviceDocFlavor;
	}

	/**
	 * @param printDocFlavor the printDocFlavor to set
	 */
	public void setPrintDocFlavor(DocFlavor printDocFlavor)
	{
		this.printDocFlavor = printDocFlavor;
	}

	/**
	 * @return the printDocFlavor
	 */
	public DocFlavor getPrintDocFlavor()
	{
		return printDocFlavor;
	}

	/**
	 * @param printRequestAttributeSet the printRequestAttributeSet to set
	 */
	public void setPrintRequestAttributeSet(PrintRequestAttributeSet printRequestAttributeSet)
	{
		this.printRequestAttributeSet = printRequestAttributeSet;
	}

	/**
	 * @return the printRequestAttributeSet
	 */
	public PrintRequestAttributeSet getPrintRequestAttributeSet()
	{
		return printRequestAttributeSet;
	}

	/**
	 * @param docAttributeSet the docAttributeSet to set
	 */
	public void setDocAttributeSet(DocAttributeSet docAttributeSet)
	{
		this.docAttributeSet = docAttributeSet;
	}

	/**
	 * @return the docAttributeSet
	 */
	public DocAttributeSet getDocAttributeSet()
	{
		return docAttributeSet;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName)
	{		
		this.serviceName = serviceName;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName()
	{
		return serviceName;
	}
	
	private List<PrintService> getPrintServices()
	{
		return Arrays.asList(PrintServiceLookup.lookupPrintServices(getServiceDocFlavor(), getPrintRequestAttributeSet()));
	}
	
	private PrintService getPrintService(DocFlavor serviceDocFlavor) throws Exception
	{
		PrintService printService = null;
		//System.out.println("service name: " + getServiceName());
		for(PrintService ps : getPrintServices())
		{			
			//System.out.println("print service: " + ps.getName());
			if(getServiceName().equalsIgnoreCase(ps.getName()))
			{
				printService = ps;
			}
		}
		return printService;
	}
	
	private DocPrintJob getDocPrintJob(DocFlavor serviceDocFlavor) throws Exception
	{
		return getPrintService(serviceDocFlavor).createPrintJob();
	}
	
	private Doc getDoc(DocFlavor printDocFlavor, byte [] printByte) throws Exception
	{
		return new SimpleDoc(printByte, printDocFlavor, null);
	}	
	
	public void print(String text, String labNumber, String jobName) throws Exception
	{
		PrintableDoc doc = new PrintableDoc(text, labNumber);
		PrinterJob pj = PrinterJob.getPrinterJob();
		
		try {
			
			pj.setPrintService(getPrintService(getServiceDocFlavor()));
		} catch (PrinterException ex) {        
		    throw new RuntimeException("Unable to set printer to \"" + pj.getPrintService().toString() + "\" because " + ex.getMessage());
		}
		
		double margin = doc.getMargin(), pageWidth = doc.getPageWidth(), pageHeight = doc.getPageHeight();        
		PageFormat pf = new PageFormat();
		Paper paper = new Paper();
		paper.setSize(pageWidth, pageHeight);
		paper.setImageableArea(margin, margin, pageWidth - 2 * margin, pageHeight - 2 * margin);
		pf.setPaper(paper);
		pj.setPrintable(doc, pf);
		pj.setJobName(jobName);
		try {        	
		    pj.print();
		} catch (PrinterException ex) {
		    throw new RuntimeException("Unable to print \"" + jobName + "\" because " + ex.getMessage());
		}
	}
	
	public void print(byte [] printByte, Boolean cutPaper) throws Exception
	{
		DocPrintJob docPrintJob = getDocPrintJob(getServiceDocFlavor());
		if(cutPaper) printByte = addCutPaper(printByte);
		Doc doc = getDoc(getPrintDocFlavor(), printByte);		
		docPrintJob.print(doc, null);		
	}
	
	private byte [] addCutPaper(byte [] printByte) throws Exception
	{
		byte [] addCutPaper = new byte[printByte.length + PrintConstants.CUTTER_CODE.length];
		System.arraycopy(printByte, 0, addCutPaper, 0, printByte.length);
		for(int c = 0; c < PrintConstants.CUTTER_CODE.length; c++)
		{
			addCutPaper[printByte.length + c] = PrintConstants.CUTTER_CODE[c];
		}
		return addCutPaper;
	}
}
