package com.chua.evergrocery.database.dao.impl;

import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.UserImageDAO;
import com.chua.evergrocery.database.entity.UserImage;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 30, 2018
 */
@Repository
public class UserImageDAOImpl
		extends AbstractDAO<UserImage, Long> 
		implements UserImageDAO {
	
	@Override
	public List<UserImage> findAllByUserId(Long userId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("user.id", userId));
		
		return findAllByCriterionList(null, null, null, null, conjunction);
	}
}
