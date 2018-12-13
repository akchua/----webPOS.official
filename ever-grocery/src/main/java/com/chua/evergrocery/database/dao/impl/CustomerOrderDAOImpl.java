package com.chua.evergrocery.database.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

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
		
		if(StringUtils.isNotBlank(searchKey))
		{
			for(String s : searchKey.split("\\s+")) {
				conjunction.add(Restrictions.ilike("name", s, MatchMode.ANYWHERE));
			}
		}
		
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
	public CustomerOrder findByNameAndStatus(String name, Status[] status) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("name", name));
		
		final Disjunction disjunction = Restrictions.disjunction();
		
		for(Status statuz : status) {
			disjunction.add(Restrictions.eq("status", statuz));
		}
		
		conjunction.add(disjunction);
		
		return findUniqueResult(null, null, null, conjunction);
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
		conjunction.add(Restrictions.between("paidOn", dateFrom, dateTo));
		
		final ProjectionList pList = Projections.projectionList();
		pList.add(Projections.min("serialInvoiceNumber"), "startSIN");
		pList.add(Projections.max("serialInvoiceNumber"), "endSIN");
		
		List<SINRangeBean> temp = findAllByCriterionProjection(SINRangeBean.class, null, null, null, pList, Transformers.aliasToBean(SINRangeBean.class), conjunction);
		
		return (temp != null && !temp.isEmpty()) ? temp.get(0) : new SINRangeBean();
	}
}
