package com.chua.evergrocery.database.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.beans.ProductPurchaseSummaryBean;
import com.chua.evergrocery.beans.PurchaseSummaryBean;
import com.chua.evergrocery.database.dao.PurchaseOrderDetailDAO;
import com.chua.evergrocery.database.entity.PurchaseOrderDetail;
import com.chua.evergrocery.database.service.PurchaseOrderDetailService;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.utility.DateUtil;

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
	public ObjectList<PurchaseOrderDetail> findAllWithPagingOrderByLastUpdate(int pageNumber, int resultsPerPage,
			long purchaseOrderId) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, purchaseOrderId, new Order[] { Order.desc("updatedOn") });
	}
	
	@Override
	public PurchaseOrderDetail findByOrderAndDetailId(long purchaseOrderId, long productDetailId) {
		return dao.findByOrderAndDetailId(purchaseOrderId, productDetailId);
	}

	@Override
	public List<PurchaseOrderDetail> findAllByPurchaseOrderId(Long purchaseOrderId) {
		return dao.findAllByPurchaseOrderId(purchaseOrderId);
	}

	@Override
	public List<PurchaseOrderDetail> findAllByCompanyAndMonthId(long companyId, int monthId) {
		return dao.findAllByCompanyAndDeliveryDate(companyId, DateUtil.monthIdToDate(monthId), DateUtil.monthIdToDate(monthId + 1));
	}

	@Override
	public List<PurchaseOrderDetail> findAllByProductAndDeliveryDate(long productId, Date deliveryStart,
			Date deliveryEnd) {
		return dao.findAllByProductAndDeliveryDate(productId, deliveryStart, deliveryEnd);
	}
	
	@Override
	public List<PurchaseOrderDetail> findAllByPurchaseOrderIdOrderByProductName(Long purchaseOrderId) {
		return dao.findAllByPurchaseOrderIdWithOrder(purchaseOrderId, new Order[] { Order.asc("productName") });
	}

	@Override
	public PurchaseSummaryBean getPurchaseSummaryByProductAndDeliveryDate(long productId, Date deliveryStart,
			Date deliveryEnd) {
		return dao.getPurchaseSummaryByProductAndDeliveryDate(productId, deliveryStart, deliveryEnd);
	}

	@Override
	public List<ProductPurchaseSummaryBean> getAllProductPurchaseSummaryByCompanyAndMonthId(long companyId,
			int monthId) {
		return dao.getAllProductPurchaseSummaryByCompanyAndDeliveryDate(companyId, DateUtil.monthIdToDate(monthId), DateUtil.monthIdToDate(monthId + 1));
	}
}
