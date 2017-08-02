package com.chua.evergrocery.database.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.ProductDAO;
import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.objects.ObjectList;

@Repository
public class ProductDAOImpl
		extends AbstractDAO<Product, Long>
		implements ProductDAO {

	@Override
	public ObjectList<Product> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey, Long companyId)
	{
		return findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, companyId, null);
	}
	
	@Override
	public ObjectList<Product> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey,
			Long companyId, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(StringUtils.isNotBlank(searchKey))
		{
			conjunction.add(Restrictions.disjunction()
					.add(Restrictions.ilike("name", searchKey, MatchMode.ANYWHERE)));
		}
		
		if(companyId != null) {
			conjunction.add(Restrictions.eq("company.id", companyId));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}
	
	@Override
	public Product findByName(String name) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("name", name));
		
		return findUniqueResult(null, null, null, conjunction);
	}
}
