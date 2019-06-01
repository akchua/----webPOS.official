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
import com.chua.evergrocery.database.dao.ProductDailySalesSummaryDAO;
import com.chua.evergrocery.database.entity.ProductDailySalesSummary;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   18 Dec 2018
 */
@Repository
public class ProductDailySalesSummaryDAOImpl
		extends AbstractDAO<ProductDailySalesSummary, Long>
		implements ProductDailySalesSummaryDAO {

	@Override
	public ProductDailySalesSummary findByProductAndSalesDate(long productId, Date salesDate) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("product.id", productId));
		conjunction.add(Restrictions.eq("salesDate", salesDate));
		
		return findUniqueResult(null, null, null, conjunction);
	}
	
	@Override
	public List<ProductDailySalesSummary> findByRangeWithOrder(Long productId, Date startDate, Date endDate, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("product.id", productId));
		conjunction.add(Restrictions.between("salesDate", startDate, endDate));
		
		return findAllByCriterionList(null, null, null, orders, conjunction);
	}
	
	@Override
	public List<ProductSalesSummaryBean> getAllProductSalesSummaryByCompanyAndPaidDate(long companyId,
			Date paidStart, Date paidEnd) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("p.company.id", companyId));
		conjunction.add(Restrictions.between("salesDate", paidStart, paidEnd));
		
		final ProjectionList pList = Projections.projectionList();
		pList.add(Projections.groupProperty("p.id"), "productId");
		pList.add(Projections.sqlProjection("sum(net_total) as netTotal", new String[] { "netTotal" }, new FloatType[] { new FloatType() }), "netTotal");
		pList.add(Projections.sqlProjection("sum(base_total) as baseTotal", new String[] { "baseTotal" }, new FloatType[] { new FloatType() }), "baseTotal");
		
		String[] associatedPaths = { "product" };
		String[] aliasNames = { "p" };
		JoinType[] joinTypes = { JoinType.INNER_JOIN };
		
		return findAllByCriterionProjection(ProductSalesSummaryBean.class, associatedPaths, aliasNames, joinTypes, pList, Transformers.aliasToBean(ProductSalesSummaryBean.class), conjunction);
	}
}
