package com.chua.evergrocery.database.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.CustomerDAO;
import com.chua.evergrocery.database.entity.Customer;
import com.chua.evergrocery.objects.ObjectList;

@Repository
public class CustomerDAOImpl
		extends AbstractDAO<Customer, Long>
		implements CustomerDAO {

	@Override
	public ObjectList<Customer> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(StringUtils.isNotBlank(searchKey))
		{
			for(String s : searchKey.split("\\s+")) {
				conjunction.add(Restrictions.ilike("firstName", s, MatchMode.ANYWHERE));
				conjunction.add(Restrictions.ilike("lastName", s, MatchMode.ANYWHERE));
			}
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, null, conjunction);
	}
	
	@Override
	public List<Customer> findAllWithOrder(Order[] orders) {
		return findAllByCriterionList(null, null, null, orders, Restrictions.eq("isValid", Boolean.TRUE));
	}
	
	@Override
	public Customer findByFullName(String firstName, String lastName) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("firstName", firstName));
		conjunction.add(Restrictions.eq("lastName", lastName));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public Customer findByCardId(String cardId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("cardId", cardId));
		
		return findUniqueResult(null, null, null, conjunction);
	}
}
