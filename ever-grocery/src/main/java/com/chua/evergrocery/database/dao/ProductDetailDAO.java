package com.chua.evergrocery.database.dao;

import com.chua.evergrocery.database.entity.ProductDetail;
import com.chua.evergrocery.database.prototype.ProductDetailPrototype;

public interface ProductDetailDAO extends DAO<ProductDetail, Long>, ProductDetailPrototype {

	ProductDetail findByBarcode(String barcode);
	
	
}
