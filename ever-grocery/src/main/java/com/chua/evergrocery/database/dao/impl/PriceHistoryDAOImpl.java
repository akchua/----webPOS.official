package com.chua.evergrocery.database.dao.impl;

import java.util.Date;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.PriceHistoryDAO;
import com.chua.evergrocery.database.entity.PriceHistory;
import com.chua.evergrocery.enums.PriceHistoryType;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   May 10, 2018
 */
@Repository
public class PriceHistoryDAOImpl
		extends AbstractDAO<PriceHistory, Long>
		implements PriceHistoryDAO {

	@Override
	public ObjectList<PriceHistory> findAllWithPagingAndOrderLimitByDate(int pageNumber, int resultsPerPage, PriceHistoryType priceHistoryType, Date until,
			Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		conjunction.add(Restrictions.eq("priceHistoryType", priceHistoryType));
		conjunction.add(Restrictions.ge("createdOn", new DateTime(until)));
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}
}
