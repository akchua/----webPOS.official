package com.chua.evergrocery.database.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.beans.ProductSalesSummaryBean;
import com.chua.evergrocery.database.dao.ProductDailySalesSummaryDAO;
import com.chua.evergrocery.database.entity.ProductDailySalesSummary;
import com.chua.evergrocery.database.service.ProductDailySalesSummaryService;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   18 Dec 2018
 */
@Service
public class ProductDailySalesSummaryServiceImpl
		extends AbstractService<ProductDailySalesSummary, Long, ProductDailySalesSummaryDAO>
		implements ProductDailySalesSummaryService {

	@Autowired
	protected ProductDailySalesSummaryServiceImpl(ProductDailySalesSummaryDAO dao) {
		super(dao);
	}

	@Override
	public ProductDailySalesSummary findByProductAndSalesDate(long productId, Date salesDate) {
		return dao.findByProductAndSalesDate(productId, DateUtil.floorDay(salesDate));
	}
	
	@Override
	public List<ProductDailySalesSummary> findByRangeOrderBySalesDate(Long productId, Date startDate, Date endDate) {
		return dao.findByRangeWithOrder(productId, DateUtil.floorDay(startDate), DateUtil.floorDay(endDate), new Order[] { Order.asc("salesDate") });
	}
	
	@Override
	public List<ProductSalesSummaryBean> getAllProductSalesSummaryByCompanyAndMonthId(long companyId,
			int monthId) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtil.monthIdToDate(monthId));
		cal.add(Calendar.SECOND, -1);
		
		System.out.println(DateFormatter.longFormat(cal.getTime()) + " - " + DateFormatter.longFormat(DateUtil.monthIdToDate(monthId + 1)));
		
		return dao.getAllProductSalesSummaryByCompanyAndPaidDate(companyId, cal.getTime(), DateUtil.monthIdToDate(monthId + 1));
	}
}
