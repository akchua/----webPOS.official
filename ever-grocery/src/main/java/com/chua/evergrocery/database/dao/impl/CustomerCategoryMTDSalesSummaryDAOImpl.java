package com.chua.evergrocery.database.dao.impl;

import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.CustomerCategoryMTDSalesSummaryDAO;
import com.chua.evergrocery.database.entity.CustomerCategoryMTDSalesSummary;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
@Repository
public class CustomerCategoryMTDSalesSummaryDAOImpl
		extends AbstractDAO<CustomerCategoryMTDSalesSummary, Long>
		implements CustomerCategoryMTDSalesSummaryDAO {

	@Override
	public CustomerCategoryMTDSalesSummary findByCustomerCategoryAndMonthId(long customerCategoryId, int monthId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("customerCategory.id", customerCategoryId));
		conjunction.add(Restrictions.eq("monthId", monthId));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public List<CustomerCategoryMTDSalesSummary> findAllByCustomerCategoryWithOrder(Long customerCategoryId, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("customerCategory.id", customerCategoryId));
		
		return findAllByCriterionList(null, null, null, orders, conjunction);
	}
}
