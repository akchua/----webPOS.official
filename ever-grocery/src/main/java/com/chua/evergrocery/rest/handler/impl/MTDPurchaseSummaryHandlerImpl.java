package com.chua.evergrocery.rest.handler.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.database.entity.CompanyMTDPurchaseSummary;
import com.chua.evergrocery.database.entity.MTDPurchaseSummary;
import com.chua.evergrocery.database.service.CompanyMTDPurchaseSummaryService;
import com.chua.evergrocery.database.service.MTDPurchaseSummaryService;
import com.chua.evergrocery.rest.handler.MTDPurchaseSummaryHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 21, 2018
 */
@Component
@Transactional
public class MTDPurchaseSummaryHandlerImpl implements MTDPurchaseSummaryHandler {

	@Autowired
	private MTDPurchaseSummaryService mtdPurchaseSummaryService;
	
	@Autowired
	private CompanyMTDPurchaseSummaryService companyMTDPurchaseSummaryService;
	
	@Override
	public List<MTDPurchaseSummary> getMTDPurchaseSummaryList() {
		return mtdPurchaseSummaryService.findAllOrderByMonthId();
	}
	
	@Override
	public List<CompanyMTDPurchaseSummary> getCompanyMTDPurchaseSummaryList(Long companyId) {
		return companyMTDPurchaseSummaryService.findAllByCompanyOrderByMonthId(companyId);
	}
}
