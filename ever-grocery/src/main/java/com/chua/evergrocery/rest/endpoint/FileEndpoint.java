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
		System.out.println("File name : " + fileName);
		File generatedPurchase = fileHandler.findGeneratedPurchaseByFileName(fileName);
		if(generatedPurchase.exists())
			return Response.ok(generatedPurchase, MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", "attachment; filename=\"" + generatedPurchase.getName() + "\"" )
				.build();
		else return null;
	}
	
	@GET
	@Path("/inventory/{fileName}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public Response getInventoryByFileName(@PathParam("fileName") String fileName) throws IOException {
		System.out.println("File name : " + fileName);
		File inventory = fileHandler.findInventoryByFileName(fileName);
		if(inventory.exists())
			return Response.ok(inventory, MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", "attachment; filename=\"" + inventory.getName() + "\"" )
				.build();
		else return null;
	}
}