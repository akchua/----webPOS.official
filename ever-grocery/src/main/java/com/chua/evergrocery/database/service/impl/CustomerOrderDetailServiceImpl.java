package com.chua.evergrocery.database.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.beans.ProductSalesSummaryBean;
import com.chua.evergrocery.beans.SalesSummaryBean;
import com.chua.evergrocery.database.dao.CustomerOrderDetailDAO;
import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.database.service.CustomerOrderDetailService;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.utility.DateUtil;

@Service
public class CustomerOrderDetailServiceImpl
		extends AbstractService<CustomerOrderDetail, Long, CustomerOrderDetailDAO>
		implements CustomerOrderDetailService {

	@Autowired
	protected CustomerOrderDetailServiceImpl(CustomerOrderDetailDAO dao) {
		super(dao);
	}
	
	@Override
	public ObjectList<CustomerOrderDetail> findAllWithPaging(int pageNumber, int resultsPerPage, long customerOrderId) {
		return dao.findAllWithPaging(pageNumber, resultsPerPage, customerOrderId);
	}
	
	@Override
	public ObjectList<CustomerOrderDetail> findAllWithPagingOrderByLastUpdate(int pageNumber, int resultsPerPage,
			long customerOrderId) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, customerOrderId, new Order[] { Order.desc("updatedOn") });
	}

	@Override
	public CustomerOrderDetail findByOrderAndDetailId(long customerOrderId, long productDetailId) {
		return dao.findByOrderAndDetailId(customerOrderId, productDetailId);
	}
	
	@Override
	public List<CustomerOrderDetail> findAllByCustomerOrderId(Long customerOrderId) {
		return dao.findAllByCustomerOrderId(customerOrderId);
	}

	@Override
	public List<CustomerOrderDetail> findAllByCustomerOrderIdOrderByProductName(Long customerOrderId) {
		return dao.findAllByCustomerOrderIdWithOrder(customerOrderId, new Order[] { Order.asc("productDisplayName") });
	}

	@Override
	public List<CustomerOrderDetail> findAllByProductLimitByDate(Long productId, Date start) {
		return dao.findAllByProductLimitByDate(productId, start);
	}

	@Override
	public SalesSummaryBean getSalesSummaryByProductAndDatePaid(long productId, Date datePaidStart, Date datePaidEnd) {
		return dao.getSalesSummaryByProductAndDatePaid(productId, datePaidStart, datePaidEnd);
	}

	@Override
	public List<ProductSalesSummaryBean> getAllProductSalesSummaryByCompanyAndDate(long companyId, Date salesDay) {
		return dao.getAllProductSalesSummaryByCompanyAndPaidDate(companyId, DateUtil.floorDay(salesDay), DateUtil.ceilDay(salesDay));
	}
}
