package com.chua.evergrocery.database.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.BrandDAO;
import com.chua.evergrocery.database.entity.Brand;
import com.chua.evergrocery.objects.ObjectList;

@Repository
public class BrandDAOImpl 
		extends AbstractDAO<Brand, Long>
		implements BrandDAO {

	@Override
	public ObjectList<Brand> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(StringUtils.isNotBlank(searchKey))
		{
			conjunction.add(Restrictions.disjunction()
					.add(Restrictions.ilike("name", searchKey, MatchMode.ANYWHERE)));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, null, conjunction);
	}

	@Override
	public List<Brand> findAllWithOrder(Order[] orders) {
		return findAllByCriterionList(null, null, null, orders, Restrictions.eq("isValid", Boolean.TRUE));
	}

	@Override
	public Brand findByName(String name) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("name", name));
		
		return findUniqueResult(null, null, null, conjunction);
	}
}
