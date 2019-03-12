package com.chua.evergrocery.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.CompanyMTDSalesSummary;
import com.chua.evergrocery.database.prototype.CompanyMTDSalesSummaryPrototype;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 12, 2019
 */
public interface CompanyMTDSalesSummaryDAO extends DAO<CompanyMTDSalesSummary, Long>, CompanyMTDSalesSummaryPrototype {

	List<CompanyMTDSalesSummary> findAllByCompanyWithOrder(Long companyId, Order[] orders);
}
