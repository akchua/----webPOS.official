package com.chua.evergrocery.database.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.beans.ProductPurchaseSummaryBean;
import com.chua.evergrocery.beans.PurchaseSummaryBean;
import com.chua.evergrocery.database.dao.PurchaseOrderDetailDAO;
import com.chua.evergrocery.database.entity.PurchaseOrderDetail;
import com.chua.evergrocery.objects.ObjectList;

@Repository
public class PurchaseOrderDetailDAOImpl
	extends AbstractDAO<PurchaseOrderDetail, Long>
	implements PurchaseOrderDetailDAO
{

	@Override
	public ObjectList<PurchaseOrderDetail> findAllWithPaging(int pageNumber, int resultsPerPage, long purchaseOrderId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("purchaseOrder.id", purchaseOrderId));
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, null, conjunction);
	}
	
	@Override
	public PurchaseOrderDetail findByOrderAndDetailId(long purchaseOrderId, long productDetailId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("purchaseOrder.id", purchaseOrderId));
		conjunction.add(Restrictions.eq("productDetail.id", productDetailId));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public List<PurchaseOrderDetail> findAllByPurchaseOrderId(Long purchaseOrderId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("purchaseOrder.id", purchaseOrderId));
		
		return findAllByCriterionList(null, null, null, null, conjunction);
	}
	
	@Override
	public List<PurchaseOrderDetail> findAllByProductAndDeliveryDate(long productId, Date deliveryStart,
			Date deliveryEnd) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("pd.product.id", productId));
		conjunction.add(Restrictions.eq("po.isValid", Boolean.TRUE));
		conjunction.add(Restrictions.between("po.deliveredOn", deliveryStart, deliveryEnd));
		
		String[] associatedPaths = { "productDetail", "purchaseOrder" };
		String[] aliasNames = { "pd", "po" };
		JoinType[] joinTypes = { JoinType.INNER_JOIN, JoinType.INNER_JOIN };
		
		return findAllByCriterionList(associatedPaths, aliasNames, joinTypes, null, conjunction);
	}

	@Override
	public List<PurchaseOrderDetail> findAllByCompanyAndDeliveryDate(long companyId, Date deliveryStart,
			Date deliveryEnd) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("po.isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("po.company.id", companyId));
		conjunction.add(Restrictions.between("po.deliveredOn", deliveryStart, deliveryEnd));
		
		String[] associatedPaths = { "purchaseOrder" };
		String[] aliasNames = { "po" };
		JoinType[] joinTypes = { JoinType.INNER_JOIN };
		
		return findAllByCriterionList(associatedPaths, aliasNames, joinTypes, null, conjunction);
	}

	@Override
	public PurchaseSummaryBean getPurchaseSummaryByProductAndDeliveryDate(long productId, Date deliveryStart,
			Date deliveryEnd) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("productId", productId));
		conjunction.add(Restrictions.eq("po.isValid", Boolean.TRUE));
		conjunction.add(Restrictions.between("po.deliveredOn", deliveryStart, deliveryEnd));
		
		final ProjectionList pList = Projections.projectionList();
		pList.add(Projections.sqlProjection("sum(total_price) as netTotal", new String[] { "netTotal" }, new FloatType[] { new FloatType() }), "netTotal");
		pList.add(Projections.sqlProjection("sum(gross_price*quantity) as grossTotal", new String[] { "grossTotal" }, new FloatType[] { new FloatType() }), "grossTotal");
		
		String[] associatedPaths = { "purchaseOrder" };
		String[] aliasNames = { "po" };
		JoinType[] joinTypes = { JoinType.INNER_JOIN };
		
		final List<PurchaseSummaryBean> purchaseSummaries = findAllByCriterionProjection(PurchaseSummaryBean.class, associatedPaths, aliasNames, joinTypes, pList, Transformers.aliasToBean(PurchaseSummaryBean.class), conjunction);
		if(purchaseSummaries.isEmpty()) return new PurchaseSummaryBean();
		else return purchaseSummaries.get(0);
	}

	@Override
	public List<ProductPurchaseSummaryBean> getAllProductPurchaseSummaryByCompanyAndDeliveryDate(long companyId,
			Date deliveryStart, Date deliveryEnd) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("p.company.id", companyId));
		conjunction.add(Restrictions.eq("po.isValid", Boolean.TRUE));
		conjunction.add(Restrictions.between("po.deliveredOn", deliveryStart, deliveryEnd));
		
		final ProjectionList pList = Projections.projectionList();
		pList.add(Projections.groupProperty("p.id"), "productId");
		pList.add(Projections.sqlProjection("sum(total_price) as netTotal", new String[] { "netTotal" }, new FloatType[] { new FloatType() }), "netTotal");
		pList.add(Projections.sqlProjection("sum(gross_price*quantity) as grossTotal", new String[] { "grossTotal" }, new FloatType[] { new FloatType() }), "grossTotal");
		
		String[] associatedPaths = { "purchaseOrder", "product" };
		String[] aliasNames = { "po", "p" };
		JoinType[] joinTypes = { JoinType.INNER_JOIN, JoinType.INNER_JOIN };
		
		return findAllByCriterionProjection(ProductPurchaseSummaryBean.class, associatedPaths, aliasNames, joinTypes, pList, Transformers.aliasToBean(ProductPurchaseSummaryBean.class), conjunction);
	}
}
