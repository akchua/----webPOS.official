package com.chua.evergrocery.rest.handler.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.beans.CustomerCategorySalesSummaryBean;
import com.chua.evergrocery.beans.CustomerSalesSummaryBean;
import com.chua.evergrocery.database.entity.Category;
import com.chua.evergrocery.database.entity.Customer;
import com.chua.evergrocery.database.entity.CustomerCategory;
import com.chua.evergrocery.database.entity.CustomerCategoryMTDSalesSummary;
import com.chua.evergrocery.database.entity.CustomerMTDSalesSummary;
import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.database.entity.MTDSalesSummary;
import com.chua.evergrocery.database.service.CustomerCategoryMTDSalesSummaryService;
import com.chua.evergrocery.database.service.CustomerCategoryService;
import com.chua.evergrocery.database.service.CustomerMTDSalesSummaryService;
import com.chua.evergrocery.database.service.CustomerOrderDetailService;
import com.chua.evergrocery.database.service.CustomerOrderService;
import com.chua.evergrocery.database.service.CustomerService;
import com.chua.evergrocery.database.service.MTDSalesSummaryService;
import com.chua.evergrocery.rest.handler.CustomerSummaryHandler;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
@Component
@Transactional
public class CustomerSummaryHandlerImpl implements CustomerSummaryHandler {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerOrderService customerOrderService;
	
	@Autowired
	private CustomerCategoryService customerCategoryService;
	
	@Autowired
	private CustomerOrderDetailService customerOrderDetailService;
	
	@Autowired
	private CustomerMTDSalesSummaryService customerMTDSalesSummaryService;
	
	@Autowired
	private CustomerCategoryMTDSalesSummaryService customerCategoryMTDSalesSummaryService;
	
	@Autowired
	private MTDSalesSummaryService mtdSalesSummaryService;
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public List<CustomerMTDSalesSummary> getCustomerMTDSalesSummaryList(Long customerId) {
		return customerMTDSalesSummaryService.findAllByCustomerOrderByMonthId(customerId);
	}

	@Override
	public List<CustomerCategoryMTDSalesSummary> getCustomerCategoryMTDSalesSummaryList(Long customerCategoryId) {
		return customerCategoryMTDSalesSummaryService.findAllByCustomerCategoryOrderByMonthId(customerCategoryId);
	}

	@Override
	public void updateMonthlyCustomerSummaries(int includedMonthsAgo) {
		final List<Customer> customers = customerService.findAllList();
		
		// remove previous sales and profit percentages
		this.clearAllSalesAndProfitValuePercentage();
		
		final int lastMonthId = DateUtil.getMonthId(new Date()) - 1;
		
		for(int monthId = lastMonthId; monthId > lastMonthId - includedMonthsAgo; monthId--) {
			LOG.info("### Processing month of : " + DateFormatter.prettyMonthFormat(monthId));
			
			final MTDSalesSummary grandTotal = mtdSalesSummaryService.findByMonthId(monthId);
			final Map<Long, CustomerCategorySalesSummaryBean> customerCategorySalesSummaries = new HashMap<Long, CustomerCategorySalesSummaryBean>();
			
			for(Customer customer : customers) {
				LOG.info("Processing customer : " + customer.getName());
				
				final List<CustomerOrderDetail> cods = customerOrderDetailService.findAllByCustomerAndDatePaid(customer.getId(), DateUtil.monthIdToDate(monthId), DateUtil.monthIdToDate(monthId + 1));
				
				final CustomerSalesSummaryBean css = new CustomerSalesSummaryBean();
				css.setCustomerId(customer.getId());
				css.setLuxuryTotal(0.0f);
				css.setNetTotal(0.0f);
				css.setBaseTotal(0.0f);
				
				for(CustomerOrderDetail cod : cods) {
					Category category = cod.getProduct().getCategory();
					if(category.getName().equals("Liquors") || category.getName().equals("Cigarette")) {
						css.setLuxuryTotal(css.getLuxuryTotal() + cod.getTotalPrice());
					}
					css.setNetTotal(css.getNetTotal() + cod.getTotalPrice() - cod.getPromoDiscountAmount());
					css.setBaseTotal(css.getBaseTotal() + (cod.getUnitPrice() / (1 + (cod.getMargin() / 100)) * cod.getQuantity()) - cod.getPromoDiscountAmount());
				}
				
				// add sales of customer to customer category summary
				CustomerCategorySalesSummaryBean ccss = customerCategorySalesSummaries.get(customer.getCustomerCategory().getId());
				if(ccss == null) {
					ccss = new CustomerCategorySalesSummaryBean();
					ccss.setCustomerCategoryId(customer.getCustomerCategory().getId());
					ccss.setBaseTotal(0.0f);
					ccss.setLuxuryTotal(0.0f);
					ccss.setNetTotal(0.0f);
				}
				ccss.setLuxuryTotal(ccss.getLuxuryTotal() + css.getLuxuryTotal());
				ccss.setBaseTotal(ccss.getBaseTotal() + css.getBaseTotal());
				ccss.setNetTotal(ccss.getNetTotal() + css.getNetTotal());
				customerCategorySalesSummaries.put(ccss.getCustomerCategoryId(), ccss);
				
				// save customer sales summary
				CustomerMTDSalesSummary customerMTDSalesSummary = customerMTDSalesSummaryService.findByCustomerAndMonthId(customer.getId(), monthId);
				if(customerMTDSalesSummary == null) {
					customerMTDSalesSummary = new CustomerMTDSalesSummary();
					customerMTDSalesSummary.setCustomer(customer);
					customerMTDSalesSummary.setMonthId(monthId);
					customerMTDSalesSummary.setLuxuryTotal(css.getLuxuryTotal());
					customerMTDSalesSummary.setBaseTotal(css.getBaseTotal());
					customerMTDSalesSummary.setNetTotal(css.getNetTotal());
					customerMTDSalesSummaryService.insert(customerMTDSalesSummary);
				} else {
					customerMTDSalesSummary.setLuxuryTotal(css.getLuxuryTotal());
					customerMTDSalesSummary.setBaseTotal(css.getBaseTotal());
					customerMTDSalesSummary.setNetTotal(css.getNetTotal());
					customerMTDSalesSummaryService.update(customerMTDSalesSummary);
				}
				
				// check if processing last month, then update customer's sales and profit percentage
				if(monthId == lastMonthId) {
					customer.setSaleValuePercentage(css.getNetTotal().equals(0.0f) ? 0.0f : css.getNetTotal() / grandTotal.getNetTotal() * 100.0f);
					customer.setProfitPercentage(css.getTotalProfit().equals(0.0f) ? 0.0f : css.getTotalProfit() / grandTotal.getProfit() * 100.0f);
					customerService.update(customer);
				}
			}
			
			// save each customer category sales summary
			for(Map.Entry<Long, CustomerCategorySalesSummaryBean> entry : customerCategorySalesSummaries.entrySet()) {
				CustomerCategorySalesSummaryBean ccss = entry.getValue();
				CustomerCategory customerCategory = customerCategoryService.find(ccss.getCustomerCategoryId());
				CustomerCategoryMTDSalesSummary customerCategoryMTDSalesSummary = customerCategoryMTDSalesSummaryService.findByCustomerCategoryAndMonthId(entry.getKey(), monthId);
				
				if(customerCategoryMTDSalesSummary == null) {
					customerCategoryMTDSalesSummary = new CustomerCategoryMTDSalesSummary();
					customerCategoryMTDSalesSummary.setCustomerCategory(customerCategory);
					customerCategoryMTDSalesSummary.setMonthId(monthId);
					customerCategoryMTDSalesSummary.setLuxuryTotal(ccss.getLuxuryTotal());
					customerCategoryMTDSalesSummary.setBaseTotal(ccss.getBaseTotal());
					customerCategoryMTDSalesSummary.setNetTotal(ccss.getNetTotal());
					customerCategoryMTDSalesSummaryService.insert(customerCategoryMTDSalesSummary);
				} else {
					customerCategoryMTDSalesSummary.setLuxuryTotal(ccss.getLuxuryTotal());
					customerCategoryMTDSalesSummary.setBaseTotal(ccss.getBaseTotal());
					customerCategoryMTDSalesSummary.setNetTotal(ccss.getNetTotal());
					customerCategoryMTDSalesSummaryService.update(customerCategoryMTDSalesSummary);
				}
				
				// check if processing last month, then update customer category's sales and profit percentage
				if(monthId == lastMonthId) {
					customerCategory.setSaleValuePercentage(ccss.getNetTotal().equals(0.0f) ? 0.0f : ccss.getNetTotal() / grandTotal.getNetTotal() * 100.0f);
					customerCategory.setProfitPercentage(ccss.getTotalProfit().equals(0.0f) ? 0.0f : ccss.getTotalProfit() / grandTotal.getProfit() * 100.0f);
					customerCategoryService.update(customerCategory);
				}
			}
		}
	}
	
	@Override
	public void updateMonthlyCustomerSchedule() {
		final List<Customer> customers = customerService.findAllList();
		
		for(Customer customer : customers) {
			LOG.info("Processing customer : " + customer.getName());
			
			final List<CustomerOrder> customerOrders = customerOrderService.findAllWithPagingByCustomerOrderByLatest(0, 20, customer.getId()).getList();
			
			if(customerOrders != null && customerOrders.size() > 5) {
				float averageSchedule = 0.0f;
				int sameDayOrderOffset = 0;
				for(int i = 0; i < customerOrders.size() - 1; i++) {
					float daysDiff = DateUtil.daysBetween(customerOrders.get(i + 1).getPaidOn(), customerOrders.get(i).getPaidOn());
					averageSchedule += daysDiff;
					if(daysDiff <= 0.0f) sameDayOrderOffset++;
				}
				averageSchedule /= customerOrders.size() - 1 - sameDayOrderOffset;
				customer.setAverageSchedule(Math.abs(averageSchedule));
			}
		}
	}

	@Override
	public void updateDailyCustomerOOSFlag() {
		final List<Customer> customers = customerService.findAllList();
		
		for(Customer customer : customers) {
			LOG.info("Processing customer : " + customer.getName());
			
			if(!customer.getAverageSchedule().equals(0.0f) && 
					DateUtil.daysBetween(customer.getLastPurchase(), new Date()) > customer.getAverageSchedule() * 2) {
				if(!customer.getOosFlag()) {
					customer.setOosFlag(Boolean.TRUE);
					customer.setOosLastFlag(new Date());
				}
			} else {
				customer.setOosFlag(Boolean.FALSE);
			}
		}
		
		customerService.batchUpdate(customers);
	}
	
	private void clearAllSalesAndProfitValuePercentage() {
		LOG.info("Cleaning up customer sales and profit value percentages .... ");
		
		final List<Customer> allCustomers = customerService.findAllList();
		for(Customer customer : allCustomers) {
			customer.setSaleValuePercentage(0.0f);
			customer.setProfitPercentage(0.0f);
		}
		customerService.batchUpdate(allCustomers);
		
		final List<CustomerCategory> allCustomerCategories = customerCategoryService.findAllList();
		for(CustomerCategory customerCategory : allCustomerCategories) {
			customerCategory.setSaleValuePercentage(0.0f);
			customerCategory.setProfitPercentage(0.0f);
		}
		customerCategoryService.batchUpdate(allCustomerCategories);
		
		LOG.info("Clean up complete .... ");
	}
}
