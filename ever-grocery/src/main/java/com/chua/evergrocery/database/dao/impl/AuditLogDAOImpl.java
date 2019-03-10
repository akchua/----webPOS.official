package com.chua.evergrocery.database.dao.impl;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.AuditLogDAO;
import com.chua.evergrocery.database.entity.AuditLog;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 10, 2019
 */
@Repository
public class AuditLogDAOImpl
		extends AbstractDAO<AuditLog, Long>
		implements AuditLogDAO {

	@Override
	public ObjectList<AuditLog> findBySubjectWithOrder(int pageNumber, int resultsPerPage, Long subjectId,
			Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(subjectId != null) {
			conjunction.add(Restrictions.eq("subjectOfAudit.id", subjectId));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}
}
