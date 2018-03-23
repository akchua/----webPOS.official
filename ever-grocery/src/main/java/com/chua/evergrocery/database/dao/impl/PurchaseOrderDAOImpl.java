package com.chua.evergrocery.database.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.PurchaseOrderDAO;
import com.chua.evergrocery.database.entity.PurchaseOrder;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.utility.DateUtil;

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
	public List<PurchaseOrder> findAllByCompanyAndDaysWithOrder(Long companyId, int days, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("company.id", companyId));
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, -days);
		
		conjunction.add(Restrictions.between("deliveredOn", (cal.getTime().before(DateUtil.getOrderCutoffDate()) ? DateUtil.getOrderCutoffDate() : cal.getTime()), new Date()));
		
		return findAllByCriterionList(null, null, null, orders, conjunction);
	}
}
