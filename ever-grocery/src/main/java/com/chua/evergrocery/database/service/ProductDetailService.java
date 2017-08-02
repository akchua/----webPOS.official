package com.chua.evergrocery.database.service;

import com.chua.evergrocery.database.entity.ProductDetail;
import com.chua.evergrocery.database.prototype.ProductDetailPrototype;

public interface ProductDetailService
		extends Service<ProductDetail, Long>, ProductDetailPrototype
{
	public ProductDetail findByBarcode(String barcode);
}
