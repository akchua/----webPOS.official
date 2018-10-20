package com.chua.evergrocery.database.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.CompanyMTDPurchaseSummaryDAO;
import com.chua.evergrocery.database.entity.CompanyMTDPurchaseSummary;
import com.chua.evergrocery.database.service.CompanyMTDPurchaseSummaryService;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
@Service
public class CompanyMTDPurchaseSummaryServiceImpl
		extends AbstractService<CompanyMTDPurchaseSummary, Long, CompanyMTDPurchaseSummaryDAO>
		implements CompanyMTDPurchaseSummaryService {

	@Autowired
	protected CompanyMTDPurchaseSummaryServiceImpl(CompanyMTDPurchaseSummaryDAO dao) {
		super(dao);
	}

	@Override
	public CompanyMTDPurchaseSummary findByCompanyAndMonthId(long companyId, int monthId) {
		return dao.findByCompanyAndMonthId(companyId, monthId);
	}
}
