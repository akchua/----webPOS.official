package com.chua.evergrocery.database.dao.impl;

import java.util.List;

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
			final Junction disjunction = Restrictions.disjunction();
			final Junction conjunction2 = Restrictions.conjunction();
			for(String s : searchKey.split("\\s+")) {
				conjunction2.add(Restrictions.ilike("name", s, MatchMode.ANYWHERE));
			}
			disjunction.add(conjunction2);
			disjunction.add(Restrictions.ilike("code", searchKey, MatchMode.ANYWHERE));
			conjunction.add(disjunction);
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
	
	@Override
	public Product findByCode(String code) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("code", code));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public List<Product> findAllByCompanyWithOrder(Long companyId, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("company.id", companyId));
		
		return findAllByCriterionList(null, null, null, orders, conjunction);
	}
}
