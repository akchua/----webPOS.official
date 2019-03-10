package com.chua.evergrocery.rest.handler;

import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.AuditLog;
import com.chua.evergrocery.enums.AuditLogType;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 10, 2019
 */
public interface AuditLogHandler {

	ObjectList<AuditLog> getAuditLogObjectList(Integer pageNumber, Long subjectId);
	
	ResultBean addLog(Long subjectOfAuditId, AuditLogType auditLogType, Float amount);
}
