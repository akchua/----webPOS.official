package com.chua.evergrocery.database.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.PurchaseOrderDAO;
import com.chua.evergrocery.database.entity.PurchaseOrder;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.objects.ObjectList;

@Repository
public class PurchaseOrderDAOImpl
	extends AbstractDAO<PurchaseOrder, Long>
	implements PurchaseOrderDAO 
{
	@Override
	public ObjectList<PurchaseOrder> findAllWithPaging(int pageNumber, int resultsPerPage, Long companyId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(companyId != null) {
			conjunction.add(Restrictions.eq("company.id", companyId));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, null, conjunction);
	}

	@Override
	public ObjectList<PurchaseOrder> findAllWithPagingAndStatus(int pageNumber, int resultsPerPage, Long companyId,
			Status[] status) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		final Disjunction disjunction = Restrictions.disjunction();
		
		if(companyId != null) {
			conjunction.add(Restrictions.eq("company.id", companyId));
		}
		
		for(Status statuz : status) {
			disjunction.add(Restrictions.eq("status", statuz));
		}
		
		conjunction.add(disjunction);
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, null, conjunction);
	}

	@Override
	public List<PurchaseOrder> findAllByCompanyAndDaysWithOrder(Long companyId, Date cutOff, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("company.id", companyId));
		
		conjunction.add(Restrictions.between("deliveredOn", cutOff, new Date()));
		
		return findAllByCriterionList(null, null, null, orders, conjunction);
	}

	@Override
	public PurchaseOrder findLatestDeliveryByCompany(Long companyId) {
		final DetachedCriteria latestDelivery = DetachedCriteria.forClass(PurchaseOrder.class)
			    .setProjection( Projections.max("deliveredOn") );
		
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("company.id", companyId));
		conjunction.add(Restrictions.eq("deliveredOn", latestDelivery));
		
		return findUniqueResult(null, null, null, conjunction);
	}
}
