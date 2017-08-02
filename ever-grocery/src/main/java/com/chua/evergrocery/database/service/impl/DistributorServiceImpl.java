package com.chua.evergrocery.database.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.DistributorDAO;
import com.chua.evergrocery.database.entity.Distributor;
import com.chua.evergrocery.database.service.DistributorService;
import com.chua.evergrocery.objects.ObjectList;

@Service
public class DistributorServiceImpl
		extends AbstractService<Distributor, Long, DistributorDAO>
		implements DistributorService {

	@Autowired
	protected DistributorServiceImpl(DistributorDAO dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ObjectList<Distributor> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey) {
		return dao.findAllWithPaging(pageNumber, resultsPerPage, searchKey);
	}
	
	@Override
	public List<Distributor> findAllOrderByName() {
		return dao.findAllWithOrder(new Order[] { Order.asc("name") });
	}
	
	@Override
	public Boolean isExistsByName(String name) {
		return dao.findByName(StringUtils.trimToEmpty(name)) != null;
	}
}
