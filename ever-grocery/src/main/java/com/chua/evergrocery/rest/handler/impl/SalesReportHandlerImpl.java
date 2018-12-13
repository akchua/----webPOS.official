package com.chua.evergrocery.rest.handler.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.beans.DailySalesReportBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.SalesReportQueryBean;
import com.chua.evergrocery.constants.BusinessConstants;
import com.chua.evergrocery.constants.FileConstants;
import com.chua.evergrocery.database.service.CustomerOrderService;
import com.chua.evergrocery.enums.Color;
import com.chua.evergrocery.enums.DiscountType;
import com.chua.evergrocery.rest.handler.SalesReportHandler;
import com.chua.evergrocery.utility.Html;
import com.chua.evergrocery.utility.SimplePdfWriter;
import com.chua.evergrocery.utility.StringHelper;
import com.chua.evergrocery.utility.format.DateFormatter;
import com.chua.evergrocery.utility.template.SalesReportTemplate;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   9 Jul 2018
 */
@Component
@Transactional
public class SalesReportHandlerImpl implements SalesReportHandler {

	@Autowired
	private CustomerOrderService customerOrderService;
	
	@Autowired
	private FileConstants fileConstants;
	
	@Autowired
	private BusinessConstants businessConstants;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Override
	public ResultBean generateReport(SalesReportQueryBean salesReportQuery) {
		final ResultBean result;
		
		if(salesReportQuery.getReportType() != null) {
			final List<DailySalesReportBean> dailySalesReports;
			
			switch(salesReportQuery.getReportType()) {
				case FULL:
					dailySalesReports = customerOrderService.getDailySalesReportByDateRange(salesReportQuery.getFrom(), salesReportQuery.getTo());
					break;
				case REGULAR:
					dailySalesReports = customerOrderService.getDailySalesReportByDateRangeAndDiscountType(salesReportQuery.getFrom(), salesReportQuery.getTo(), null, false);
					break;
				case SCD:
					final List<DiscountType> discountTypes = new ArrayList<DiscountType>();
					discountTypes.add(DiscountType.SENIOR_DISCOUNT);
					dailySalesReports = customerOrderService.getDailySalesReportByDateRangeAndDiscountType(salesReportQuery.getFrom(), salesReportQuery.getTo(), discountTypes, null);
					break;
				case RETURNS:
					dailySalesReports = customerOrderService.getDailySalesReportByDateRangeAndDiscountType(salesReportQuery.getFrom(), salesReportQuery.getTo(), null, true);
					break;
				default:
					dailySalesReports = new ArrayList<DailySalesReportBean>();
			}
			
					
			String fileName = "";
			fileName += salesReportQuery.getReportType().getName() + "_";
			fileName += DateFormatter.fileSafeFormat(salesReportQuery.getFrom()) + "_to_";
			fileName += DateFormatter.fileSafeFormat(salesReportQuery.getTo());
			fileName += ".pdf";
			fileName = StringHelper.convertToFileSafeFormat(fileName);
			final String filePath = fileConstants.getSalesHome() + fileName;
			result = new ResultBean();
			result.setSuccess(
					SimplePdfWriter.write(
							new SalesReportTemplate(
									salesReportQuery,
									dailySalesReports).merge(velocityEngine),
							businessConstants.getBusinessShortName(),
							filePath,
							true)
					);
			if(result.getSuccess()) {
				final Map<String, Object> extras = new HashMap<String, Object>();
				extras.put("fileName", fileName);
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created sales report."));
				result.setExtras(extras);
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("Please select the type of report."));
		}
		
		return result;
	}
}
