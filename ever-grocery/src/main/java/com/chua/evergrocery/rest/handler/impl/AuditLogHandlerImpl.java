package com.chua.evergrocery.rest.handler.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.AuditLog;
import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.database.service.AuditLogService;
import com.chua.evergrocery.database.service.UserService;
import com.chua.evergrocery.enums.AuditLogType;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.AuditLogHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 10, 2019
 */
@Transactional
@Component
public class AuditLogHandlerImpl implements AuditLogHandler {

	@Autowired
	private AuditLogService auditLogService;
	
	@Autowired
	private UserService userService;

	@Override
	public ObjectList<AuditLog> getAuditLogObjectList(Integer pageNumber, Long subjectId) {
		return auditLogService.findBySubjectOrderByLatest(pageNumber, UserContextHolder.getItemsPerPage(), subjectId);
	}
	
	@Override
	public ResultBean addLog(Long subjectOfAuditId, AuditLogType auditLogType, Float amount) {
		final ResultBean result;
		final User subjectOfAudit = userService.find(subjectOfAuditId);
		final AuditLog lastLogOfSubject = auditLogService.find(subjectOfAudit.getAuditLogId());
		
		final AuditLog auditLog;
		
		if(lastLogOfSubject != null &&
				lastLogOfSubject.getAuditLogType().equals(AuditLogType.SALES) &&
				auditLogType.equals(AuditLogType.SALES)) {
			auditLog = lastLogOfSubject;
			auditLog.setAuditEnd(new Date());
			auditLog.setAmount(auditLog.getAmount() + amount);
			auditLog.setTransactionCount(auditLog.getTransactionCount() + 1);
			auditLog.setWithheldCash(auditLog.getWithheldCash() + amount);
			auditLogService.update(auditLog);
		} else {
			auditLog = new AuditLog();
			auditLog.setSubjectOfAudit(subjectOfAudit);
			auditLog.setAuditStart(new Date());
			auditLog.setAuditEnd(new Date());
			auditLog.setAmount(amount);
			auditLog.setAuditLogType(auditLogType);
			auditLog.setTransactionCount(1);
			auditLog.setWithheldCash(lastLogOfSubject != null ? lastLogOfSubject.getWithheldCash() + amount : amount);
			auditLogService.insert(auditLog);
			
			subjectOfAudit.setAuditLogId(auditLog.getId());
			userService.update(subjectOfAudit);
		}
		
		result = new ResultBean(Boolean.TRUE, "");
		
		return result;
	}
}
