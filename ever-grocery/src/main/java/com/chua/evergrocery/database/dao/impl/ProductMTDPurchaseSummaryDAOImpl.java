package com.chua.evergrocery.database.dao.impl;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.ProductMTDPurchaseSummaryDAO;
import com.chua.evergrocery.database.entity.ProductMTDPurchaseSummary;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
@Repository
public class ProductMTDPurchaseSummaryDAOImpl 
		extends AbstractDAO<ProductMTDPurchaseSummary, Long>
		implements ProductMTDPurchaseSummaryDAO {

	@Override
	public ProductMTDPurchaseSummary findByProductAndMonthId(long productId, int monthId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("product.id", productId));
		conjunction.add(Restrictions.eq("monthId", monthId));
		
		return findUniqueResult(null, null, null, conjunction);
	}
}
