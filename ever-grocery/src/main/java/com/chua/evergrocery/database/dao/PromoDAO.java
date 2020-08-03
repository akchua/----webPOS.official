package com.chua.evergrocery.database.dao;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.Promo;
import com.chua.evergrocery.database.prototype.PromoPrototype;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Aug 1, 2020
 */
public interface PromoDAO extends DAO<Promo, Long>, PromoPrototype {

	ObjectList<Promo> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Boolean showActiveOnly, Order[] orders);
}
