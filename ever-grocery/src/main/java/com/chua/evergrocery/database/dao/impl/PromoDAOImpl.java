package com.chua.evergrocery.database.dao.impl;

import java.util.Date;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.PromoDAO;
import com.chua.evergrocery.database.entity.Promo;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.utility.DateUtil;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Aug 1, 2020
 */
@Repository
public class PromoDAOImpl 
		extends AbstractDAO<Promo, Long> 
		implements PromoDAO {

	@Override
	public Promo findCurrentByProduct(Long productId) {
		final Date today = DateUtil.floorDay(new Date());
		
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("product.id", productId));
		conjunction.add(Restrictions.le("startDate", today));
		conjunction.add(Restrictions.ge("endDate", today));
		
		return findUniqueResult(null, null, null, conjunction);
	}
	
	@Override
	public ObjectList<Promo> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Boolean showActiveOnly, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		final Date today = DateUtil.floorDay(new Date());
		
		if(showActiveOnly) {
			conjunction.add(Restrictions.le("startDate", today));
			conjunction.add(Restrictions.ge("endDate", today));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}
	
	@Override
	public Promo findByProductAndDuration(Long productId, Date startDate, Date endDate, Long promoId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("product.id", productId));
		conjunction.add(Restrictions.le("startDate", endDate));
		conjunction.add(Restrictions.ge("endDate", startDate));
		conjunction.add(Restrictions.ne("id", promoId));
		
		return findUniqueResult(null, null, null, conjunction);
	}
}
