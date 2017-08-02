package com.chua.evergrocery.database.dao.impl;

import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
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
}
