package com.chua.evergrocery.database.service.impl;

import java.util.Date;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.PromoDAO;
import com.chua.evergrocery.database.entity.Promo;
import com.chua.evergrocery.database.service.PromoService;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Aug 1, 2020
 */
@Service
public class PromoServiceImpl 
		extends AbstractService<Promo, Long, PromoDAO>
		implements PromoService {

	@Autowired
	protected PromoServiceImpl(PromoDAO dao) {
		super(dao);
	}
	
	@Override
	public ObjectList<Promo> findAllWithPagingOrderByLatest(int pageNumber, int resultsPerPage, Boolean showActiveOnly) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, showActiveOnly, new Order[] { Order.desc("createdOn") });
	}
	
	@Override
	public ObjectList<Promo> findAllRecentlyEndedWithPagingOrderByLatest(int pageNumber, int resultsPerPage) {
		return dao.findAllRecentlyEndedWithPagingAndOrder(pageNumber, resultsPerPage, 4, new Order[] { Order.desc("endDate") });
	}

	@Override
	public Promo findByProductAndDuration(Long productId, Date startDate, Date endDate, Long promoId) {
		return dao.findByProductAndDuration(productId, startDate, endDate, promoId);
	}

	@Override
	public Promo findCurrentByProduct(Long productId) {
		return dao.findCurrentByProduct(productId);
	}
}
