package com.chua.evergrocery.rest.endpoint;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.evergrocery.beans.PromoFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Promo;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.PromoHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Aug 1, 2020
 */
@Path("/promo")
public class PromoEndpoint {

	@Autowired
	private PromoHandler promoHandler;
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<Promo> getPromoList(@QueryParam("pageNumber") Integer pageNumber, 
			@QueryParam("showActiveOnly") Boolean showActiveOnly) {
		return promoHandler.getPromoList(pageNumber, showActiveOnly);
	}
	
	@GET
	@Path("/recentlyended")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<Promo> getRecentlyEndedPromoList(@QueryParam("pageNumber") Integer pageNumber) {
		return promoHandler.getRecentlyEndedPromoList(pageNumber);
	}
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public Promo getPromo(@QueryParam("promoId") Long promoId) {
		return promoHandler.getPromo(promoId);
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean savePromo(@FormParam("promoFormData") String promoFormData,
				@Context HttpServletRequest request) throws IOException {
		final ResultBean result;
		
		final PromoFormBean promoForm = new ObjectMapper().readValue(promoFormData, PromoFormBean.class);
		if(promoForm.getId() != null) {
			result = promoHandler.updatePromo(promoForm, request.getRemoteAddr());
		} else {
			result = promoHandler.createPromo(promoForm, request.getRemoteAddr());
		}
		
		return result;
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removePromo(@FormParam("promoId") Long promoId,
				@Context HttpServletRequest request) {
		return promoHandler.removePromo(promoId, request.getRemoteAddr());
	}
	
	@POST
	@Path("/currentpromo")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean generateCurrentPromoPDF() {
		return promoHandler.generateCurrentPromoPDF();
	}
}
