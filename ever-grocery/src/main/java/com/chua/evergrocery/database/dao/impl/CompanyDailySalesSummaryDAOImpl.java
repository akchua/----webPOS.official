package com.chua.evergrocery.database.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.CompanyDailySalesSummaryDAO;
import com.chua.evergrocery.database.entity.CompanyDailySalesSummary;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   18 Dec 2018
 */
@Repository
public class CompanyDailySalesSummaryDAOImpl
		extends AbstractDAO<CompanyDailySalesSummary, Long>
		implements CompanyDailySalesSummaryDAO {

	@Override
	public CompanyDailySalesSummary findByCompanyAndSalesDate(long companyId, Date salesDate) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("company.id", companyId));
		conjunction.add(Restrictions.eq("salesDate", salesDate));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public List<CompanyDailySalesSummary> findAllByCompanyAndRangeWithOrder(Long companyId, Date startDate,
			Date endDate, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("company.id", companyId));
		conjunction.add(Restrictions.between("salesDate", startDate, endDate));
		
		return findAllByCriterionList(null, null, null, orders, conjunction);
	}
}
