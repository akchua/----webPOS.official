package com.chua.evergrocery.rest.handler;

import java.util.List;

import com.chua.evergrocery.beans.ProductDetailsFormBean;
import com.chua.evergrocery.beans.ProductFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.database.entity.ProductDetail;
import com.chua.evergrocery.objects.ObjectList;

public interface ProductHandler {

	ObjectList<Product> getProductList(Integer pageNumber, String searchKey, Long companyId);
	
	Product getProduct(Long productId);
	
	List<ProductDetail> getProductDetailList(Long productId);
	
	ResultBean createProduct(ProductFormBean productForm);
	
	ResultBean updateProduct(ProductFormBean productForm);
	
	ResultBean removeProduct(Long productId);
	
	ResultBean saveProductDetails(Long productId, List<ProductDetailsFormBean> productDetailsFormList);
	
	List<String> getUnitTypeList();
}
