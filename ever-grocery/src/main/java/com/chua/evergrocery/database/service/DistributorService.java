package com.chua.evergrocery.database.service;

import java.util.List;

import com.chua.evergrocery.database.entity.Distributor;
import com.chua.evergrocery.database.prototype.DistributorPrototype;

public interface DistributorService 
		extends Service<Distributor, Long>, DistributorPrototype {
	/**
	 * Find all distributor ordered by name.
	 * 
	 * @return the list of distributor
	 */
	List<Distributor> findAllOrderByName();
	
	Boolean isExistsByName(String name);
}
