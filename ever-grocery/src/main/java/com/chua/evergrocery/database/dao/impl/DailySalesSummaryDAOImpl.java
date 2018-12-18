package com.chua.evergrocery.database.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.DailySalesSummaryDAO;
import com.chua.evergrocery.database.entity.DailySalesSummary;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   18 Dec 2018
 */
@Repository
public class DailySalesSummaryDAOImpl
		extends AbstractDAO<DailySalesSummary, Long>
		implements DailySalesSummaryDAO {

	@Override
	public List<DailySalesSummary> findAllWithOrder(Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("class", "DailySalesSummary"));
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		return findAllByCriterionList(null, null, null, orders, conjunction);
	}

	@Override
	public List<DailySalesSummary> findByRangeWithOrder(Date startDate, Date endDate, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("class", "DailySalesSummary"));
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.between("salesDay", startDate, endDate));
		
		return findAllByCriterionList(null, null, null, orders, conjunction);
	}

	@Override
	public DailySalesSummary findBySalesDate(Date salesDate) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("class", "DailySalesSummary"));
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("salesDate", salesDate));
		
		return findUniqueResult(null, null, null, conjunction);
	}
}
