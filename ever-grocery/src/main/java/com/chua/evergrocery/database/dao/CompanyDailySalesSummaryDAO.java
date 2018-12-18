package com.chua.evergrocery.database.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.CompanyDailySalesSummary;
import com.chua.evergrocery.database.prototype.CompanyDailySalesSummaryPrototype;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   18 Dec 2018
 */
public interface CompanyDailySalesSummaryDAO extends DAO<CompanyDailySalesSummary, Long>, CompanyDailySalesSummaryPrototype {

	List<CompanyDailySalesSummary> findAllByCompanyAndRangeWithOrder(Long companyId, Date startDate, Date endDate, Order[] orders);
}
