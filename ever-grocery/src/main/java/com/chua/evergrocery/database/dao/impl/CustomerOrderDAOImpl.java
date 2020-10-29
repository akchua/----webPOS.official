package com.chua.evergrocery.database.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.beans.BIRSalesSummaryBean;
import com.chua.evergrocery.beans.CashierSalesSummaryBean;
import com.chua.evergrocery.beans.SINRangeBean;
import com.chua.evergrocery.database.dao.CustomerOrderDAO;
import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.enums.DiscountType;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.objects.ObjectList;

@Repository
public class CustomerOrderDAOImpl 
		extends AbstractDAO<CustomerOrder, Long>
		implements CustomerOrderDAO {
	
	@Override
	public ObjectList<CustomerOrder> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey, Status[] status, Integer daysAgo)
	{
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		/*if(StringUtils.isNotBlank(searchKey))
		{
			for(String s : searchKey.split("\\s+")) {
				conjunction.add(Restrictions.ilike("id", s, MatchMode.ANYWHERE));
			}
		}*/
		
		if(status != null && status.length > 0) {
			final Disjunction disjunction = Restrictions.disjunction();
			
			for(Status statuz : status) {
				disjunction.add(Restrictions.eq("status", statuz));
			}
			
			conjunction.add(disjunction);
		}
		
		if(daysAgo != null) {
			final DateTime date = new DateTime();

			conjunction.add(Restrictions.ge("createdOn", date.minusDays(daysAgo.intValue())));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, null, conjunction);
	}
	
	@Override
	public ObjectList<CustomerOrder> findAllWithPagingByCreator(int pageNumber, int resultsPerPage, String searchKey,
			Status[] status, Long creatorId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		/*if(StringUtils.isNotBlank(searchKey))
		{
			for(String s : searchKey.split("\\s+")) {
				conjunction.add(Restrictions.ilike("id", s, MatchMode.ANYWHERE));
			}
		}*/
		
		if(status != null && status.length > 0) {
			final Disjunction disjunction = Restrictions.disjunction();
			
			for(Status statuz : status) {
				disjunction.add(Restrictions.eq("status", statuz));
			}
			
			conjunction.add(disjunction);
		}
		
		if(creatorId != null) {
			conjunction.add(Restrictions.eq("creator.id", creatorId));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, null, conjunction);
	}
	
	@Override
	public ObjectList<CustomerOrder> findAllWithPagingByCustomerWithOrder(int pageNumber, int resultsPerPage,
			Long customerId, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(customerId != null) {
			conjunction.add(Restrictions.eq("customer.id", customerId));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}
	
	@Override
	public List<CustomerOrder> findAllByCashierStatusAndDatePaidWithOrder(Long cashierId, Status[] status, Date dateFrom, Date dateTo, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(cashierId != null) {
			conjunction.add(Restrictions.eq("cashier.id", cashierId));
		}
		
		if(status != null) {
			Junction disjunction = Restrictions.disjunction();
			for(Status stats : status) {
				disjunction.add(Restrictions.eq("status", stats));
			}
			conjunction.add(disjunction);
		}
		
		conjunction.add(Restrictions.between("paidOn", dateFrom, dateTo));
		
		return findAllByCriterionList(null, null, null, orders, conjunction);
	}

	@Override
	public CustomerOrder findBySerialInvoiceNumber(Long serialInvoiceNumber) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("serialInvoiceNumber", serialInvoiceNumber));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public List<CashierSalesSummaryBean> findAllCashierSalesSummaryByDatePaidAndDiscountType(Date dateFrom, Date dateTo,
			List<DiscountType> discountTypes, Boolean returnsOnly) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("status", Status.PAID));
		conjunction.add(Restrictions.between("paidOn", dateFrom, dateTo));
		
		if(discountTypes != null && !discountTypes.isEmpty()) {
			final Junction disjunction = Restrictions.disjunction();
			for(DiscountType dt : discountTypes) {
				disjunction.add(Restrictions.eq("discountType", dt));
			}
			conjunction.add(disjunction);
		}
		
		if(returnsOnly != null) {
			if(returnsOnly) {
				conjunction.add(Restrictions.lt("totalItems", 0.0f));
			} else {
				conjunction.add(Restrictions.gt("totalItems", 0.0f));
			}
		}
		
		final ProjectionList pList = Projections.projectionList();
		pList.add(Projections.groupProperty("c.id"), "cashierId");
		pList.add(Projections.max("c.firstName"), "cashierFirstName");
		pList.add(Projections.max("c.lastName"), "cashierLastName");
		pList.add(Projections.sqlProjection("sum(vat_sales) as vatSales", new String[] { "vatSales" }, new FloatType[] { new FloatType() }), "vatSales");
		pList.add(Projections.sqlProjection("sum(vat_ex_sales) as vatExSales", new String[] { "vatExSales" }, new FloatType[] { new FloatType() }), "vatExSales");
		pList.add(Projections.sqlProjection("sum(zero_rated_sales) as zeroRatedSales", new String[] { "zeroRatedSales" }, new FloatType[] { new FloatType() }), "zeroRatedSales");
		pList.add(Projections.sqlProjection("sum(discount_amount) as discountAmount", new String[] { "discountAmount" }, new FloatType[] { new FloatType() }), "discountAmount");
		
		String[] associatedPaths = { "cashier" };
		String[] aliasNames = { "c" };
		JoinType[] joinTypes = { JoinType.INNER_JOIN };
		
		return findAllByCriterionProjection(CashierSalesSummaryBean.class, associatedPaths, aliasNames, joinTypes, pList, Transformers.aliasToBean(CashierSalesSummaryBean.class), conjunction);
	}
	
	@Override
	public SINRangeBean getSINRangeByDate(Date dateFrom, Date dateTo) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("status", Status.PAID));
		conjunction.add(Restrictions.gt("serialInvoiceNumber", 0l));
		conjunction.add(Restrictions.between("paidOn", dateFrom, dateTo));
		
		final ProjectionList pList = Projections.projectionList();
		pList.add(Projections.min("serialInvoiceNumber"), "startSIN");
		pList.add(Projections.max("serialInvoiceNumber"), "endSIN");
		
		List<SINRangeBean> temp = findAllByCriterionProjection(SINRangeBean.class, null, null, null, pList, Transformers.aliasToBean(SINRangeBean.class), conjunction);
		
		return (temp != null && !temp.isEmpty()) ? temp.get(0) : new SINRangeBean();
	}
	
	@Override
	public BIRSalesSummaryBean getSalesSummaryByDatePaidAndCashier(Date dateFrom, Date dateTo,
			Long cashierId, Boolean exceptThisCashier, Boolean refundOnly) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("status", Status.PAID));
		conjunction.add(Restrictions.between("paidOn", dateFrom, dateTo));
		
		if(refundOnly) {
			conjunction.add(Restrictions.gt("refundNumber", 0l));
		} else {
			conjunction.add(Restrictions.gt("serialInvoiceNumber", 0l));
		}
		
		if(cashierId != null) {
			if(exceptThisCashier) {
				conjunction.add(Restrictions.ne("cashier.id", cashierId));
			} else {
				conjunction.add(Restrictions.eq("cashier.id", cashierId));
			}
		}
		
		final ProjectionList pList = Projections.projectionList();
		pList.add(Projections.min("serialInvoiceNumber"), "beginningSIN");
		pList.add(Projections.max("serialInvoiceNumber"), "endingSIN");
		pList.add(Projections.min("refundNumber"), "beginningRefundNumber");
		pList.add(Projections.max("refundNumber"), "endingRefundNumber");
		pList.add(Projections.sqlProjection("sum(vat_sales) as vatSales", new String[] { "vatSales" }, new FloatType[] { new FloatType() }), "vatSales");
		pList.add(Projections.sqlProjection("sum(vat_ex_sales) as vatExSales", new String[] { "vatExSales" }, new FloatType[] { new FloatType() }), "vatExSales");
		pList.add(Projections.sqlProjection("sum(zero_rated_sales) as zeroRatedSales", new String[] { "zeroRatedSales" }, new FloatType[] { new FloatType() }), "zeroRatedSales");
		pList.add(Projections.sqlProjection("sum(vat_discount) as vatDiscount", new String[] { "vatDiscount" }, new FloatType[] { new FloatType() }), "vatDiscount");
		pList.add(Projections.sqlProjection("sum(vat_ex_discount) as vatExDiscount", new String[] { "vatExDiscount" }, new FloatType[] { new FloatType() }), "vatExDiscount");
		pList.add(Projections.sqlProjection("sum(zero_rated_discount) as zeroRatedDiscount", new String[] { "zeroRatedDiscount" }, new FloatType[] { new FloatType() }), "zeroRatedDiscount");
		pList.add(Projections.sqlProjection("sum(check_amount) as checkAmount", new String[] { "checkAmount" }, new FloatType[] { new FloatType() }), "checkAmount");
		pList.add(Projections.sqlProjection("sum(card_amount) as cardAmount", new String[] { "cardAmount" }, new FloatType[] { new FloatType() }), "cardAmount");
		pList.add(Projections.sqlProjection("sum(points_amount) as pointsAmount", new String[] { "pointsAmount" }, new FloatType[] { new FloatType() }), "pointsAmount");
		
		List<BIRSalesSummaryBean> temp = findAllByCriterionProjection(BIRSalesSummaryBean.class, null, null, null, pList, Transformers.aliasToBean(BIRSalesSummaryBean.class), conjunction);
		return (temp != null && !temp.isEmpty()) ? temp.get(0) : new BIRSalesSummaryBean();
	}
}
