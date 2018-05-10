package com.chua.evergrocery.database.dao;

import java.util.Date;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.PriceHistory;
import com.chua.evergrocery.database.prototype.PriceHistoryPrototype;
import com.chua.evergrocery.enums.PriceHistoryType;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   May 10, 2018
 */
public interface PriceHistoryDAO extends DAO<PriceHistory, Long>, PriceHistoryPrototype {

	ObjectList<PriceHistory> findAllWithPagingAndOrderLimitByDate(int pageNumber, int resultsPerPage, PriceHistoryType priceHistoryType, Date until, Order[] orders);
}
