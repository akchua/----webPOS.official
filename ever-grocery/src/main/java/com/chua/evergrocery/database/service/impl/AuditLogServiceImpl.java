package com.chua.evergrocery.database.service.impl;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.AuditLogDAO;
import com.chua.evergrocery.database.entity.AuditLog;
import com.chua.evergrocery.database.service.AuditLogService;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 10, 2019
 */
@Service
public class AuditLogServiceImpl
		extends AbstractService<AuditLog, Long, AuditLogDAO>
		implements AuditLogService {

	@Autowired
	protected AuditLogServiceImpl(AuditLogDAO dao) {
		super(dao);
	}

	@Override
	public ObjectList<AuditLog> findBySubjectOrderByLatest(int pageNumber, int resultsPerPage, Long subjectId) {
		return dao.findBySubjectWithOrder(pageNumber, resultsPerPage, subjectId, new Order[] { Order.desc("auditEnd") });
	}
}
