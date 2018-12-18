package com.chua.evergrocery.database.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.CompanyDailySalesSummaryDAO;
import com.chua.evergrocery.database.entity.CompanyDailySalesSummary;
import com.chua.evergrocery.database.service.CompanyDailySalesSummaryService;
import com.chua.evergrocery.utility.DateUtil;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   18 Dec 2018
 */
@Service
public class CompanyDailySalesSummaryServiceImpl
		extends AbstractService<CompanyDailySalesSummary, Long, CompanyDailySalesSummaryDAO>
		implements CompanyDailySalesSummaryService {

	@Autowired
	protected CompanyDailySalesSummaryServiceImpl(CompanyDailySalesSummaryDAO dao) {
		super(dao);
	}

	@Override
	public CompanyDailySalesSummary findByCompanyAndSalesDate(long companyId, Date salesDate) {
		return dao.findByCompanyAndSalesDate(companyId, DateUtil.floorDay(salesDate));
	}

	@Override
	public List<CompanyDailySalesSummary> findAllByCompanyAndRangeOrderBySalesDate(Long companyId, Date startDate,
			Date endDate) {
		return dao.findAllByCompanyAndRangeWithOrder(companyId, startDate, endDate, new Order[] { Order.asc("salesDate") });
	}
}
