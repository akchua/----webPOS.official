package com.chua.evergrocery.database.prototype;

import java.util.Date;

import com.chua.evergrocery.database.entity.Promo;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Aug 1, 2020
 */
public interface PromoPrototype {
	
	Promo findCurrentByProduct(Long productId);

	Promo findByProductAndDuration(Long productId, Date startDate, Date endDate, Long promoId);
}
