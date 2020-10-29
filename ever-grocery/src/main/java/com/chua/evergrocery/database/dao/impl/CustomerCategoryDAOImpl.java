package com.chua.evergrocery.database.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.CustomerCategoryDAO;
import com.chua.evergrocery.database.entity.CustomerCategory;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
@Repository
public class CustomerCategoryDAOImpl
		extends AbstractDAO<CustomerCategory, Long>
		implements CustomerCategoryDAO {

	@Override
	public ObjectList<CustomerCategory> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey) {
		return findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, null);
	}
	
	@Override
	public ObjectList<CustomerCategory> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey,
			Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(StringUtils.isNotBlank(searchKey))
		{
			for(String s : searchKey.split("\\s+")) {
				conjunction.add(Restrictions.ilike("name", s, MatchMode.ANYWHERE));
			}
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}
	
	@Override
	public List<CustomerCategory> findAllWithOrder(Order[] orders) {
		return findAllByCriterionList(null, null, null, orders, Restrictions.eq("isValid", Boolean.TRUE));
	}
	
	@Override
	public CustomerCategory findByName(String name) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("name", name));
		
		return findUniqueResult(null, null, null, conjunction);
	}
}
