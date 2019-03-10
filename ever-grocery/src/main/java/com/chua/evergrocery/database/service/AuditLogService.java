package com.chua.evergrocery.database.service;

import com.chua.evergrocery.database.entity.AuditLog;
import com.chua.evergrocery.database.prototype.AuditLogPrototype;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 10, 2019
 */
public interface AuditLogService extends Service<AuditLog, Long>, AuditLogPrototype {

	ObjectList<AuditLog> findBySubjectOrderByLatest(int pageNumber, int resultsPerPage, Long subjectId);
}
