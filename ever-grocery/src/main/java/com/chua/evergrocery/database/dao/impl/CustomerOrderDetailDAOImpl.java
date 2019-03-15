package com.chua.evergrocery.database.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.beans.ProductSalesSummaryBean;
import com.chua.evergrocery.beans.SalesSummaryBean;
import com.chua.evergrocery.database.dao.CustomerOrderDetailDAO;
import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.utility.DateUtil;

@Repository
public class CustomerOrderDetailDAOImpl 
		extends AbstractDAO<CustomerOrderDetail, Long>
		implements CustomerOrderDetailDAO 
{

	@Override
	public ObjectList<CustomerOrderDetail> findAllWithPaging(int pageNumber, int resultsPerPage, long customerOrderId) {
		return findAllWithPagingAndOrder(pageNumber, resultsPerPage, customerOrderId, null);
	}
	
	@Override
	public ObjectList<CustomerOrderDetail> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage,
			long customerOrderId, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("customerOrder.id", customerOrderId));
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}
	
	@Override
	public List<CustomerOrderDetail> findAllByCustomerOrderId(Long customerOrderId) {
		return findAllByCustomerOrderIdWithOrder(customerOrderId, null);
	}
	
	@Override
	public List<CustomerOrderDetail> findAllByCustomerOrderIdWithOrder(Long customerOrderId, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("customerOrder.id", customerOrderId));
		
		return findAllByCriterionList(null, null, null, orders, conjunction);
	}
	
	@Override
	public List<CustomerOrderDetail> findAllByProductLimitByDate(Long productId, Date start) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));

		String[] associatedPaths = { "productDetail", "customerOrder" };
		String[] aliasNames = { "pd", "co" };
		JoinType[] joinTypes = { JoinType.INNER_JOIN, JoinType.INNER_JOIN };
		
		conjunction.add(Restrictions.eq("pd.product.id", productId));
		conjunction.add(Restrictions.eq("co.isValid", Boolean.TRUE));
		conjunction.add(Restrictions.ge("co.paidOn", start.before(DateUtil.getOrderCutoffDate()) ? DateUtil.getOrderCutoffDate() : start));
		conjunction.add(Restrictions.eq("co.status", Status.PAID));
		
		return findAllByCriterionList(associatedPaths, aliasNames, joinTypes, null, conjunction);
	}

	@Override
	public CustomerOrderDetail findByOrderAndDetailId(long customerOrderId, long productDetailId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("customerOrder.id", customerOrderId));
		conjunction.add(Restrictions.eq("productDetail.id", productDetailId));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public SalesSummaryBean getSalesSummaryByProductAndDatePaid(Long productId, Date datePaidStart, Date datePaidEnd) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("co.isValid", Boolean.TRUE));
		conjunction.add(Restrictions.between("co.paidOn", datePaidStart, datePaidEnd));
		
		if(productId != null) {
			conjunction.add(Restrictions.eq("product.id", productId));
		}
		
		final ProjectionList pList = Projections.projectionList();
		pList.add(Projections.sqlProjection("sum(total_price) as netTotal", new String[] { "netTotal" }, new FloatType[] { new FloatType() }), "netTotal");
		pList.add(Projections.sqlProjection("sum(unit_price / (1 + (margin / 100)) * quantity) as baseTotal", new String[] { "baseTotal" }, new FloatType[] { new FloatType() }), "baseTotal");
		
		String[] associatedPaths = { "customerOrder" };
		String[] aliasNames = { "co" };
		JoinType[] joinTypes = { JoinType.INNER_JOIN };
		
		final List<SalesSummaryBean> salesSummaries = findAllByCriterionProjection(SalesSummaryBean.class, associatedPaths, aliasNames, joinTypes, pList, Transformers.aliasToBean(SalesSummaryBean.class), conjunction);
		if(salesSummaries.isEmpty()) return new SalesSummaryBean();
		else return salesSummaries.get(0);
	}
	
	@Override
	public List<ProductSalesSummaryBean> getAllProductSalesSummaryByCompanyAndPaidDate(long companyId,
			Date datePaidStart, Date datePaidEnd) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("p.company.id", companyId));
		conjunction.add(Restrictions.eq("co.isValid", Boolean.TRUE));
		conjunction.add(Restrictions.between("co.paidOn", datePaidStart, datePaidEnd));
		
		final ProjectionList pList = Projections.projectionList();
		pList.add(Projections.groupProperty("p.id"), "productId");
		pList.add(Projections.sqlProjection("sum(total_price) as netTotal", new String[] { "netTotal" }, new FloatType[] { new FloatType() }), "netTotal");
		pList.add(Projections.sqlProjection("sum(unit_price / (1 + (margin / 100)) * quantity) as baseTotal", new String[] { "baseTotal" }, new FloatType[] { new FloatType() }), "baseTotal");
		
		String[] associatedPaths = { "customerOrder", "product" };
		String[] aliasNames = { "co", "p" };
		JoinType[] joinTypes = { JoinType.INNER_JOIN, JoinType.INNER_JOIN };
		
		return findAllByCriterionProjection(ProductSalesSummaryBean.class, associatedPaths, aliasNames, joinTypes, pList, Transformers.aliasToBean(ProductSalesSummaryBean.class), conjunction);
	}
}
