package com.chua.evergrocery.database.dao.impl;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.ProductMTDSalesSummaryDAO;
import com.chua.evergrocery.database.entity.ProductMTDSalesSummary;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 12, 2019
 */
@Repository
public class ProductMTDSalesSummaryDAOImpl
		extends AbstractDAO<ProductMTDSalesSummary, Long>
		implements ProductMTDSalesSummaryDAO {

	@Override
	public ProductMTDSalesSummary findByProductAndMonthId(long productId, int monthId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("product.id", productId));
		conjunction.add(Restrictions.eq("monthId", monthId));
		
		return findUniqueResult(null, null, null, conjunction);
	}
}
