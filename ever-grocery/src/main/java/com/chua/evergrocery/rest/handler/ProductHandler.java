package com.chua.evergrocery.rest.handler;

import java.util.List;

import com.chua.evergrocery.beans.ProductDetailsFormBean;
import com.chua.evergrocery.beans.ProductFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.PriceHistory;
import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.database.entity.ProductDetail;
import com.chua.evergrocery.enums.UnitType;
import com.chua.evergrocery.objects.ObjectList;

public interface ProductHandler {

	ObjectList<Product> getProductList(Integer pageNumber, String searchKey, Long companyId);
	
	ObjectList<Product> getProductListWithCategory(Integer pageNumber, String searchKey, Long companyId, Long categoryId);
	
	ObjectList<Product> getProductListByRank(Integer pageNumber, String searchKey, Long companyId);
	
	ObjectList<PriceHistory> getSalePriceHistoryList(Integer pageNumber);
	
	Product getProduct(Long productId);
	
	ProductDetail getProductWholeDetail(Long productId);
	
	List<ProductDetail> getProductDetailList(Long productId);
	
	ResultBean createProduct(ProductFormBean productForm, String ip);
	
	ResultBean updateProduct(ProductFormBean productForm, String ip);
	
	ResultBean removeProduct(Long productId, String ip);
	
	ResultBean saveProductDetails(Long productId, List<ProductDetailsFormBean> productDetailsFormList, String ip);
	
	ProductDetail getUpperProductDetail(Long productDetailId);
	
	List<UnitType> getUnitTypeList();
}
