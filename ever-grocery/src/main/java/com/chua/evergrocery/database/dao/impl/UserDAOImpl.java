package com.chua.evergrocery.database.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.UserDAO;
import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.enums.UserType;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 30, 2017
 */
@Repository
public class UserDAOImpl
		extends AbstractDAO<User, Long> 
		implements UserDAO {
	
	@Override
	public ObjectList<User> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey) {
		return findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, null);
	}
	
	@Override
	public ObjectList<User> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey,
			Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(StringUtils.isNotBlank(searchKey))
		{
			for(String s : searchKey.split("\\s+")) {
				conjunction.add(Restrictions.disjunction()
						.add(Restrictions.ilike("firstName", s, MatchMode.ANYWHERE))
						.add(Restrictions.ilike("lastName", s, MatchMode.ANYWHERE)));
			}
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}

	@Override
	public User findByUsernameAndPassword(String username, String password) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("username", username));
		conjunction.add(Restrictions.eq("password", password));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public User findByUsername(String username) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("username", username));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public User findByUsernameOrEmail(String username, String emailAddress) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.disjunction()
				.add(Restrictions.eq("username", username))
				.add(Restrictions.eq("emailAddress", emailAddress)));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public List<User> findAllWithOrder(Order[] orders) {
		return findAllByUserTypeWithOrder(null, orders);
	}

	@Override
	public List<User> findAllByUserTypeWithOrder(UserType[] userTypes, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		Junction disjunction = Restrictions.disjunction();
		if(userTypes != null) {
			for(UserType userType : userTypes) {
				disjunction.add(Restrictions.eq("userType", userType));
			}
		}
		conjunction.add(disjunction);
		
		return findAllByCriterionList(null, null, null, orders, conjunction);
	}
}
