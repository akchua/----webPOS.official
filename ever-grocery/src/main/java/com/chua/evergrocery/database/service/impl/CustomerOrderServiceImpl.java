package com.chua.evergrocery.database.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.CustomerOrderDAO;
import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.service.CustomerOrderService;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.objects.ObjectList;

@Service
public class CustomerOrderServiceImpl
		extends AbstractService<CustomerOrder, Long, CustomerOrderDAO>
		implements CustomerOrderService 
{
	@Autowired
	protected CustomerOrderServiceImpl(CustomerOrderDAO dao) {
	super(dao);
	}
	
	@Override
	public ObjectList<CustomerOrder> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey, Status[] status, Integer daysAgo) {
	return dao.findAllWithPaging(pageNumber, resultsPerPage, searchKey, status, daysAgo);
	}
	
	@Override
	public Boolean isExistsByNameAndStatus(String name, Status[] status) {
	return dao.findByNameAndStatus(StringUtils.trimToEmpty(name), status) != null;
	}

	@Override
	public List<CustomerOrder> findAllPaidByCashierAndDate(Long cashierId, Date dateFrom) {
		return dao.findAllByCashierStatusAndDatePaid(cashierId, new Status[] { Status.PAID }, dateFrom);
	}
}
