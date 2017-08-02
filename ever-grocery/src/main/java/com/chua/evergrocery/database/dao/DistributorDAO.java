package com.chua.evergrocery.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.Distributor;
import com.chua.evergrocery.database.prototype.DistributorPrototype;

public interface DistributorDAO extends DAO<Distributor, Long>, DistributorPrototype {

	/**
	 * Find all using the given orders.
	 * 
	 * @param orders the orders
	 * 
	 * @return the list of distributor
	 */
	List<Distributor> findAllWithOrder(Order[] orders);
	
	Distributor findByName(String name);
}
