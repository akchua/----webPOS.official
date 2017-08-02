package com.chua.evergrocery.database.prototype;

import java.util.List;

import com.chua.evergrocery.database.entity.ProductDetail;

public interface ProductDetailPrototype {

	List<ProductDetail> findAllByProductId(Long productId);
	
	ProductDetail findByProductIdAndTitle(Long productId, String title);
}
