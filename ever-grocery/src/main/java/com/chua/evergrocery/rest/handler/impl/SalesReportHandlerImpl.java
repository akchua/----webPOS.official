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
		
		final List<DiscountType> discountTypes = salesReportQuery.getDiscountType() != null ? new ArrayList<DiscountType>() : null;
		if(discountTypes != null) discountTypes.add(salesReportQuery.getDiscountType());
		final List<DailySalesReportBean> dailySalesReports = customerOrderService.getDailySalesReportByDateRangeAndDiscountType(salesReportQuery.getFrom(), salesReportQuery.getTo(), discountTypes);
				
		String fileName = "";
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
		
		return result;
	}
}
