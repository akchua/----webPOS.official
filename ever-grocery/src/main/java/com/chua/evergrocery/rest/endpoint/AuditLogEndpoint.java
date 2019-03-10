package com.chua.evergrocery.rest.endpoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.evergrocery.database.entity.AuditLog;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.AuditLogHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 10, 2019
 */
@Path("/auditlog")
public class AuditLogEndpoint {

	@Autowired
	private AuditLogHandler auditLogHandler;
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<AuditLog> getAuditLogObjectList(@QueryParam("pageNumber") Integer pageNumber, 
				@QueryParam("subjectId") Long subjectId) {
		return auditLogHandler.getAuditLogObjectList(pageNumber, subjectId);
	}
}
