package com.chua.evergrocery.database.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.PurchaseOrderDAO;
import com.chua.evergrocery.database.entity.PurchaseOrder;
import com.chua.evergrocery.database.service.PurchaseOrderService;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.objects.ObjectList;

@Service
public class PurchaseOrderServiceImpl
	extends AbstractService<PurchaseOrder, Long, PurchaseOrderDAO>
	implements PurchaseOrderService 
{
	@Autowired
	protected PurchaseOrderServiceImpl(PurchaseOrderDAO dao) {
		super(dao);
	}

	@Override
	public ObjectList<PurchaseOrder> findAllWithPaging(int pageNumber, int resultsPerPage, Long companyId) {
		return dao.findAllWithPaging(pageNumber, resultsPerPage, companyId);
	}

	@Override
	public ObjectList<PurchaseOrder> findAllWithPagingAndStatus(int pageNumber, int resultsPerPage, Long companyId,
			Status[] status) {
		return dao.findAllWithPagingAndStatus(pageNumber, resultsPerPage, companyId, status);
	}
}
