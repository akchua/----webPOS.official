package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.MTDPurchaseSummary;
import com.chua.evergrocery.database.prototype.MTDPurchaseSummaryPrototype;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
public interface MTDPurchaseSummaryService 
	extends Service<MTDPurchaseSummary, Long>, MTDPurchaseSummaryPrototype {

	List<MTDPurchaseSummary> findAllOrderByMonthId();
	
	List<MTDPurchaseSummary> findByYearOrderByMonthId(int year);
}
