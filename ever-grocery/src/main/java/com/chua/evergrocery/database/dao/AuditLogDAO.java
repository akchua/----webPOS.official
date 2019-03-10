package com.chua.evergrocery.database.dao;

import org.hibernate.criterion.Order;

import com.chua.evergrocery.database.entity.AuditLog;
import com.chua.evergrocery.database.prototype.AuditLogPrototype;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 10, 2019
 */
public interface AuditLogDAO extends DAO<AuditLog, Long>, AuditLogPrototype {

	ObjectList<AuditLog> findBySubjectWithOrder(int pageNumber, int resultsPerPage, Long subjectId, Order[] orders);
}
