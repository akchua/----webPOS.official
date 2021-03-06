package com.chua.evergrocery.rest.endpoint;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.evergrocery.rest.handler.FileHandler;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	9 Feb 2017
 */
@Path("/file")
public class FileEndpoint {

	@Autowired
	private FileHandler fileHandler;
	
	@GET
	@Path("/generatedpurchase/{fileName}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public Response getGeneratedPurchaseByFileName(@PathParam("fileName") String fileName) throws IOException {
		File generatedPurchase = fileHandler.findGeneratedPurchaseByFileName(fileName);
		if(generatedPurchase.exists())
			return Response.ok(generatedPurchase, MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", "attachment; filename=\"" + generatedPurchase.getName() + "\"" )
				.build();
		else return null;
	}
	
	@GET
	@Path("/generatedofftake/{fileName}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public Response getGeneratedOfftakeByFileName(@PathParam("fileName") String fileName) throws IOException {
		File generatedOfftake = fileHandler.findGeneratedOfftakeByFileName(fileName);
		if(generatedOfftake.exists())
			return Response.ok(generatedOfftake, MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", "attachment; filename=\"" + generatedOfftake.getName() + "\"" )
				.build();
		else return null;
	}
	
	@GET
	@Path("/currentpromo/{fileName}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public Response getCurrentPromoByFileName(@PathParam("fileName") String fileName) throws IOException {
		File currentPromo = fileHandler.findCurrentPromoByFileName(fileName);
		if(currentPromo.exists())
			return Response.ok(currentPromo, MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", "attachment; filename=\"" + currentPromo.getName() + "\"" )
				.build();
		else return null;
	}
	
	@GET
	@Path("/inventory/{fileName}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public Response getInventoryByFileName(@PathParam("fileName") String fileName) throws IOException {
		File inventory = fileHandler.findInventoryByFileName(fileName);
		if(inventory.exists())
			return Response.ok(inventory, MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", "attachment; filename=\"" + inventory.getName() + "\"" )
				.build();
		else return null;
	}
	
	@GET
	@Path("/salesreport/{fileName}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public Response getSalesReportByFileName(@PathParam("fileName") String fileName) throws IOException {
		File salesReport = fileHandler.findSalesReportByFileName(fileName);
		if(salesReport.exists())
			return Response.ok(salesReport, MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", "attachment; filename=\"" + salesReport.getName() + "\"" )
				.build();
		else return null;
	}
	
	@GET
	@Path("/backendreport/{fileName}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public Response getBackendReportByFileName(@PathParam("fileName") String fileName) throws IOException {
		File backendReport = fileHandler.findBackendReportByFileName(fileName);
		if(backendReport.exists())
			return Response.ok(backendReport, MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", "attachment; filename=\"" + backendReport.getName() + "\"" )
				.build();
		else return null;
	}
	
	@GET
	@Path("/journal")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public Response getJournal() throws IOException {
		File journal = fileHandler.getJournal();
		if(journal.exists())
			return Response.ok(journal, MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", "attachment; filename=\"" + journal.getName() + "\"" )
				.build();
		else return null;
	}
}
