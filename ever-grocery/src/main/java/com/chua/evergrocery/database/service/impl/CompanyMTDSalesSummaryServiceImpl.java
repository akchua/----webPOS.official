package com.chua.evergrocery.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.CompanyMTDSalesSummaryDAO;
import com.chua.evergrocery.database.entity.CompanyMTDSalesSummary;
import com.chua.evergrocery.database.service.CompanyMTDSalesSummaryService;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 12, 2019
 */
@Service
public class CompanyMTDSalesSummaryServiceImpl
		extends AbstractService<CompanyMTDSalesSummary, Long, CompanyMTDSalesSummaryDAO>
		implements CompanyMTDSalesSummaryService{

	@Autowired
	protected CompanyMTDSalesSummaryServiceImpl(CompanyMTDSalesSummaryDAO dao) {
		super(dao);
	}
	
	@Override
	public CompanyMTDSalesSummary findByCompanyAndMonthId(long companyId, int monthId) {
		return dao.findByCompanyAndMonthId(companyId, monthId);
	}

	@Override
	public List<CompanyMTDSalesSummary> findAllByCompanyOrderByMonthId(Long companyId) {
		return dao.findAllByCompanyWithOrder(companyId, new Order[] { Order.asc("monthId") });
	}
}
