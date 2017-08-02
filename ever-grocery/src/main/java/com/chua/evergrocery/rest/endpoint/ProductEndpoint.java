package com.chua.evergrocery.rest.endpoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.evergrocery.beans.ProductDetailsFormBean;
import com.chua.evergrocery.beans.ProductFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.database.entity.ProductDetail;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.ProductHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/product")
public class ProductEndpoint {
	
	@Autowired
	private ProductHandler productHandler;
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<Product> getProductList(@QueryParam("pageNumber") Integer pageNumber, 
			@QueryParam("searchKey") String searchKey, @QueryParam("companyId") Long companyId) {
		return productHandler.getProductList(pageNumber, searchKey, companyId);
	}
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public Product getProduct(@QueryParam("productId") Long productId) {
		return productHandler.getProduct(productId);
	}
	
	@GET
	@Path("/getdetails")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<ProductDetail> getProductDetailList(@QueryParam("productId") Long productId) {
		return productHandler.getProductDetailList(productId);
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveProduct(@FormParam("productFormData") String productFormData) throws IOException {
		final ResultBean result;
		
		final ProductFormBean productForm = new ObjectMapper().readValue(productFormData, ProductFormBean.class);
		if(productForm.getId() != null) {
			result = productHandler.updateProduct(productForm);
		} else {
			result = productHandler.createProduct(productForm);
		}
		
		return result;
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeProduct(@FormParam("productId") Long productId) {
		return productHandler.removeProduct(productId);
	}
	
	@POST
	@Path("/savedetails")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveProductDetails(
			@FormParam("productId") Long productId,
			@FormParam("productDetailsWholeFormData") String productDetailsWholeFormData,
			@FormParam("productDetailsPieceFormData") String productDetailsPieceFormData,
			@FormParam("productDetailsInnerPieceFormData") String productDetailsInnerPieceFormData,
			@FormParam("productDetailsSecondInnerPieceFormData") String productDetailsSecondInnerPieceFormData) throws IOException {
		final List<ProductDetailsFormBean> productDetailsFormList = new ArrayList<>();
		final ObjectMapper objectMapper = new ObjectMapper();
		productDetailsFormList.add(objectMapper.readValue(productDetailsWholeFormData, ProductDetailsFormBean.class));
		productDetailsFormList.add(objectMapper.readValue(productDetailsPieceFormData, ProductDetailsFormBean.class));
		productDetailsFormList.add(objectMapper.readValue(productDetailsInnerPieceFormData, ProductDetailsFormBean.class));
		productDetailsFormList.add(objectMapper.readValue(productDetailsSecondInnerPieceFormData, ProductDetailsFormBean.class));
		
		return productHandler.saveProductDetails(productId, productDetailsFormList);
	}
	
	@GET
	@Path("/listunittype")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<String> getUnitTypeList() {
		return productHandler.getUnitTypeList();
	}
}
