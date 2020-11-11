package com.chua.evergrocery.rest.handler;

import com.chua.evergrocery.beans.PromoFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Promo;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Aug 1, 2020
 */
public interface PromoHandler {
	
	ObjectList<Promo> getPromoList(Integer pageNumber, Boolean showActiveOnly);
	
	ObjectList<Promo> getRecentlyEndedPromoList(Integer pageNumber);
	
	Promo getPromo(Long promoId);

	ResultBean createPromo(PromoFormBean promoForm, String ip);
	
	ResultBean updatePromo(PromoFormBean promoForm, String ip);
	
	ResultBean removePromo(Long promoId, String ip);
	
	ResultBean finalizeUsedPromos(Long customerOrderId);
	
	ResultBean generateCurrentPromoPDF();
}
