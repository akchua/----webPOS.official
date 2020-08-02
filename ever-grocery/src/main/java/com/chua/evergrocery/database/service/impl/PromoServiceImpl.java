package com.chua.evergrocery.database.service.impl;

import java.util.Date;

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
	public ObjectList<Promo> findAllWithPaging(int pageNumber, int resultsPerPage, Boolean showActiveOnly) {
		return dao.findAllWithPaging(pageNumber, resultsPerPage, showActiveOnly);
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
