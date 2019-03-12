package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.MTDSalesSummary;
import com.chua.evergrocery.database.prototype.MTDSalesSummaryPrototype;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 12, 2019
 */
public interface MTDSalesSummaryService extends Service<MTDSalesSummary, Long>, MTDSalesSummaryPrototype {

	List<MTDSalesSummary> findAllOrderByMonthId();
	
	List<MTDSalesSummary> findByYearOrderByMonthId(int year);
}
