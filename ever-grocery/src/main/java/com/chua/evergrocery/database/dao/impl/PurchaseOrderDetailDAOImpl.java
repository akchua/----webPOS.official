package com.chua.evergrocery.database.dao.impl;

import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

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
}
