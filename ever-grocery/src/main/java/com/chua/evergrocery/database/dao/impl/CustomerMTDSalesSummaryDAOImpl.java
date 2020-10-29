package com.chua.evergrocery.database.dao.impl;

import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.CustomerMTDSalesSummaryDAO;
import com.chua.evergrocery.database.entity.CustomerMTDSalesSummary;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
@Repository
public class CustomerMTDSalesSummaryDAOImpl
		extends AbstractDAO<CustomerMTDSalesSummary, Long>
		implements CustomerMTDSalesSummaryDAO {

	@Override
	public CustomerMTDSalesSummary findByCustomerAndMonthId(long customerId, int monthId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("customer.id", customerId));
		conjunction.add(Restrictions.eq("monthId", monthId));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public List<CustomerMTDSalesSummary> findAllByCustomerWithOrder(Long customerId, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("customer.id", customerId));
		
		return findAllByCriterionList(null, null, null, orders, conjunction);
	}
}
