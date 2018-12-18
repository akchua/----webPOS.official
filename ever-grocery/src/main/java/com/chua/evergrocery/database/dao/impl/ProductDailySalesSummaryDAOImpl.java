package com.chua.evergrocery.database.dao.impl;

import java.util.Date;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.ProductDailySalesSummaryDAO;
import com.chua.evergrocery.database.entity.ProductDailySalesSummary;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   18 Dec 2018
 */
@Repository
public class ProductDailySalesSummaryDAOImpl
		extends AbstractDAO<ProductDailySalesSummary, Long>
		implements ProductDailySalesSummaryDAO {

	@Override
	public ProductDailySalesSummary findByProductAndSalesDate(long productId, Date salesDate) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("product.id", productId));
		conjunction.add(Restrictions.eq("salesDate", salesDate));
		
		return findUniqueResult(null, null, null, conjunction);
	}
}
