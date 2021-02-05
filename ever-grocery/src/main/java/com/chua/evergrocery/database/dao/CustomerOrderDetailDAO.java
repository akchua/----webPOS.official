package com.chua.evergrocery.database.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.beans.ProductSalesSummaryBean;
import com.chua.evergrocery.beans.SalesSummaryBean;
import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.database.prototype.CustomerOrderDetailPrototype;
import com.chua.evergrocery.objects.ObjectList;

public interface CustomerOrderDetailDAO extends DAO<CustomerOrderDetail, Long>, CustomerOrderDetailPrototype {

	ObjectList<CustomerOrderDetail> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, long customerOrderId, Order[] orders);
	
	ObjectList<CustomerOrderDetail> findAllWithPagingAndOrderByProduct(int pageNumber, int resultsPerPage, long productId, Order[] orders);
	
	List<CustomerOrderDetail> findAllByCustomerOrderIdWithOrder(Long customerOrderId, Order[] orders);
	
	SalesSummaryBean getSalesSummaryByProductAndDatePaid(Long productId, Date datePaidStart, Date datePaidEnd);
	
	List<ProductSalesSummaryBean> getAllProductSalesSummaryByCompanyAndPaidDate(long companyId,
			Date datePaidStart, Date datePaidEnd);
}
