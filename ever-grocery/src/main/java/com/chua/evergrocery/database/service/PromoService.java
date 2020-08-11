package com.chua.evergrocery.database.service;

import com.chua.evergrocery.database.entity.Promo;
import com.chua.evergrocery.database.prototype.PromoPrototype;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Aug 1, 2020
 */
public interface PromoService
		extends Service<Promo, Long>, PromoPrototype {
	
	ObjectList<Promo> findAllWithPagingOrderByLatest(int pageNumber, int resultsPerPage, Boolean showActiveOnly);
	
	ObjectList<Promo> findAllRecentlyEndedWithPagingOrderByLatest(int pageNumber, int resultsPerPage);
}
