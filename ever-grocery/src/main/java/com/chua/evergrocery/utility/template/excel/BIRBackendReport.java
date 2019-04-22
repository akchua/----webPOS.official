package com.chua.evergrocery.utility.template.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.chua.evergrocery.database.entity.ZReading;
import com.chua.evergrocery.utility.format.CurrencyFormatter;
import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Apr 17, 2019
 */
public class BIRBackendReport {

	private XSSFWorkbook workbook;
	
	private XSSFSheet report;
	
	private XSSFCellStyle bold_center;
	
	private XSSFCellStyle absolute_center;
	
	private XSSFCellStyle right;
	
	private List<ZReading> zReadingList;
	
	public BIRBackendReport(List<ZReading> zReadingList) {
		this.workbook = new XSSFWorkbook();
		this.report = workbook.createSheet("Report");
		this.zReadingList = zReadingList;
		
		this.initStyles();
		this.fillUpWorkbook();
	}
	
	public void write(String path) {
		File file = new File(path);
		if(file.getParentFile() != null) file.getParentFile().mkdirs();
		
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			workbook.write(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initStyles() {
		XSSFFont bold = workbook.createFont();
		bold.setBold(true);
		bold_center = workbook.createCellStyle();
		bold_center.setAlignment(HorizontalAlignment.CENTER);
		bold_center.setFont(bold);
		
		XSSFFont calibri10 = workbook.createFont();
		calibri10.setFontHeight((short) 200);
		absolute_center = workbook.createCellStyle();
		absolute_center.setAlignment(HorizontalAlignment.CENTER);
		absolute_center.setVerticalAlignment(VerticalAlignment.CENTER);
		absolute_center.setFont(calibri10);
		absolute_center.setWrapText(true);
		
		right = workbook.createCellStyle();
		right.setAlignment(HorizontalAlignment.RIGHT);
		right.setFont(calibri10);
		right.setWrapText(true);
	}
	
	private void fillUpWorkbook() {
		
		createBasicCell(0, 0, "Saturnino O. Chua");
		createBasicCell(1, 0, "VPKC Grocery");
		createBasicCell(2, 0, "Abadilla St. Brgy. 17 Laoag City, Ilocos Norte");
		createBasicCell(3, 0, "102-198-471-0000");
		createBasicCell(4, 0, "Server");
		
		Cell r7c1 = report.createRow(6).createCell(0);
		r7c1.setCellValue("BIR Sales Summary Report");
		r7c1.setCellStyle(bold_center);
		report.addMergedRegion(new CellRangeAddress(6, 6, 0, 27));
		
		createHeaderRow();
		createDataRow();
	}
	
	private void createDataRow() {
		int dataRowStart = 10;
		
		for(int i = 0; i < zReadingList.size(); i++) {
			final ZReading zReading = zReadingList.get(i);
			final Row row = report.createRow(dataRowStart + i);
			
			createCenteredCell(row, 0, DateFormatter.shortFormat(zReading.getReadingDate()));
			createCenteredCell(row, 1, zReading.getBeginningSIN() + "");
			createCenteredCell(row, 2, zReading.getEndingSIN() + "");
			createRightAlignedCell(row, 3, zReading.getFormattedEndingBalance());
			createRightAlignedCell(row, 4, zReading.getFormattedBeginningBalance());
			createRightAlignedCell(row, 5, zReading.getFormattedGrossSales());
			createRightAlignedCell(row, 6, "0.00");
			createRightAlignedCell(row, 7, zReading.getFormattedGrossSales());
			createRightAlignedCell(row, 8, zReading.getFormattedNetVatableSales());
			createRightAlignedCell(row, 9, zReading.getFormattedNetVatAmount());
			createRightAlignedCell(row, 10, zReading.getFormattedVatExSales());
			createRightAlignedCell(row, 11, zReading.getFormattedZeroRatedSales());
			createRightAlignedCell(row, 12, zReading.getFormattedRegularDiscountAmount());
			createRightAlignedCell(row, 13, zReading.getFormattedTotalSpecialDiscountAmount());
			createRightAlignedCell(row, 14, CurrencyFormatter.pesoFormat(-(zReading.getRefundAmount())));
			createRightAlignedCell(row, 15, "0.00");
			createRightAlignedCell(row, 16, zReading.getFormattedTotalDeductions());
			createRightAlignedCell(row, 17, "0.00");
			createRightAlignedCell(row, 18, "0.00");
			createRightAlignedCell(row, 19, "0.00");
			createRightAlignedCell(row, 20, "0.00");
			createRightAlignedCell(row, 21, zReading.getFormattedNetVatAmount());
			createRightAlignedCell(row, 22, zReading.getFormattedNetOfVatSales());
			createRightAlignedCell(row, 23, "0.00");
			createRightAlignedCell(row, 24, "0.00");
			createRightAlignedCell(row, 25, zReading.getFormattedNetOfVatSales());
			createCenteredCell(row, 26, "n/a");
			createCenteredCell(row, 27, "");
		}
	}
	
	private void createHeaderRow() {
		Row r9 = report.createRow(8);
		Row r10 = report.createRow(9);
		r10.setHeight((short) 960);
		
		for(int i = 0; i < 28; i++) {
			if(i < 12 || i > 20) report.addMergedRegion(new CellRangeAddress(8, 9, i, i));
		}
		
		report.addMergedRegion(new CellRangeAddress(8, 8, 12, 16));
		report.addMergedRegion(new CellRangeAddress(8, 8, 17, 20));
		
		createCenteredCell(r9, 0, "Date");
		createCenteredCell(r9, 1, "Beginning SI/OR No.");
		createCenteredCell(r9, 2, "Ending SI/OR No.");
		createCenteredCell(r9, 3, "Grand Accum. Sales Ending Balance");
		createCenteredCell(r9, 4, "Grand Accum. Sales Beginning Balance");
		createCenteredCell(r9, 5, "Gross Sales for the Day");
		createCenteredCell(r9, 6, "Sales Issued with Manual SI/OR (per RR 16-2018)");
		createCenteredCell(r9, 7, "Gross Sales From POS");
		createCenteredCell(r9, 8, "VATable Sales");
		createCenteredCell(r9, 9, "VAT Amount");
		createCenteredCell(r9, 10, "VAT-Exempt Sales");
		createCenteredCell(r9, 11, "Zero Rated Sales");
		createCenteredCell(r9, 12, "Deductions");
		createCenteredCell(r10, 12, "Regular Discount");
		createCenteredCell(r10, 13, "Special Discount (SC/PWD)");
		createCenteredCell(r10, 14, "Returns");
		createCenteredCell(r10, 15, "Void");
		createCenteredCell(r10, 16, "Total Deductions");
		createCenteredCell(r9, 17, "Adjustments on VAT");
		createCenteredCell(r10, 17, "VAT on Special Discounts");
		createCenteredCell(r10, 18, "VAT on Returns");
		createCenteredCell(r10, 19, "Others");
		createCenteredCell(r10, 20, "Total VAT Adj.");
		createCenteredCell(r9, 21, "VAT Payable");
		createCenteredCell(r9, 22, "Net Sales");
		createCenteredCell(r9, 23, "Other Income");
		createCenteredCell(r9, 24, "Sales Overrun/ Overflow");
		createCenteredCell(r9, 25, "Total Net Sales");
		createCenteredCell(r9, 26, "Reset Counter");
		createCenteredCell(r9, 27, "Remarks");
	}
	
	private void createBasicCell(int row, int column, String text) {
		Cell basicCell = report.createRow(row).createCell(column);
		basicCell.setCellValue(text);
	}
	
	private void createCenteredCell(Row row, int column, String text) {
		Cell centeredCell = row.createCell(column);
		centeredCell.setCellValue(text);
		centeredCell.setCellStyle(absolute_center);
	}
	
	private void createRightAlignedCell(Row row, int column, String text) {
		Cell rightAlignedCell = row.createCell(column);
		rightAlignedCell.setCellValue(text);
		rightAlignedCell.setCellStyle(right);
	}
}
