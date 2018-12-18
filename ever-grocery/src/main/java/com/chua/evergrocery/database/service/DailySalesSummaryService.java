package com.chua.evergrocery.database.service;import java.util.Date;
import java.util.List;

import com.chua.evergrocery.database.entity.DailySalesSummary;
import com.chua.evergrocery.database.prototype.DailySalesSummaryPrototype;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   18 Dec 2018
 */
public interface DailySalesSummaryService
		extends Service<DailySalesSummary, Long>, DailySalesSummaryPrototype{

	List<DailySalesSummary> findAllOrderBySalesDate();
	
	List<DailySalesSummary> findByRangeOrderBySalesDate(Date startDate, Date endDate);
}
