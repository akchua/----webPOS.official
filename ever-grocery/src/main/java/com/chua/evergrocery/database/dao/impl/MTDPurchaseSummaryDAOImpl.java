package com.chua.evergrocery.database.dao.impl;

import org.hibernate.criterion.Junction;
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
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("monthId", monthId));
		
		return findUniqueResult(null, null, null, conjunction);
	}
}
