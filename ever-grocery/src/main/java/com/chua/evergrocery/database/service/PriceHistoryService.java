package com.chua.evergrocery.database.service;

import com.chua.evergrocery.database.entity.PriceHistory;
import com.chua.evergrocery.database.prototype.PriceHistoryPrototype;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   May 10, 2018
 */
public interface PriceHistoryService extends Service<PriceHistory, Long>, PriceHistoryPrototype {

	ObjectList<PriceHistory> findAllSaleTypeWithin30DaysOrderByCreatedOn(int pageNumber, int resultsPerPage);
}
