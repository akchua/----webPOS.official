package com.chua.evergrocery.database.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.PurchaseOrderDetailDAO;
import com.chua.evergrocery.database.entity.PurchaseOrderDetail;
import com.chua.evergrocery.database.service.PurchaseOrderDetailService;
import com.chua.evergrocery.objects.ObjectList;

@Service
public class PurchaseOrderDetailServiceImpl
	extends AbstractService<PurchaseOrderDetail, Long, PurchaseOrderDetailDAO>
	implements PurchaseOrderDetailService
{

	@Autowired
	protected PurchaseOrderDetailServiceImpl(PurchaseOrderDetailDAO dao) {
		super(dao);
	}

	@Override
	public ObjectList<PurchaseOrderDetail> findAllWithPaging(int pageNumber, int resultsPerPage, long purchaseOrderId) {
		return dao.findAllWithPaging(pageNumber, resultsPerPage, purchaseOrderId);
	}
	
	@Override
	public PurchaseOrderDetail findByOrderAndDetailId(long purchaseOrderId, long productDetailId) {
		return dao.findByOrderAndDetailId(purchaseOrderId, productDetailId);
	}

	@Override
	public List<PurchaseOrderDetail> findAllByPurchaseOrderId(Long purchaseOrderId) {
		return dao.findAllByPurchaseOrderId(purchaseOrderId);
	}
}
