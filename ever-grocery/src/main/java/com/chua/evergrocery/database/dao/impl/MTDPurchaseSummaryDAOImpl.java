package com.chua.evergrocery.database.dao.impl;

import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.MTDPurchaseSummaryDAO;
import com.chua.evergrocery.database.entity.MTDPurchaseSummary;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
@Repository
public class MTDPurchaseSummaryDAOImpl 
		extends AbstractDAO<MTDPurchaseSummary, Long>
		implements MTDPurchaseSummaryDAO {

	@Override
	public MTDPurchaseSummary findByMonthId(int monthId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("class", "MTDPurchaseSummary"));
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("monthId", monthId));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public List<MTDPurchaseSummary> findAllWithOrder(Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("class", "MTDPurchaseSummary"));
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		return findAllByCriterionList(null, null, null, orders, conjunction);
	}

	@Override
	public List<MTDPurchaseSummary> findByYearWithOrder(int year, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("class", "MTDPurchaseSummary"));
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.between("monthId", year * 12, (year * 12) + 11));
		
		return findAllByCriterionList(null, null, null, orders, conjunction);
	}
}
