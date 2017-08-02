package com.chua.evergrocery.database.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.ProductDetailDAO;
import com.chua.evergrocery.database.entity.ProductDetail;
import com.chua.evergrocery.database.service.ProductDetailService;

@Service
public class ProductDetailServiceImpl 
		extends AbstractService<ProductDetail, Long, ProductDetailDAO>
		implements ProductDetailService 
{
	@Autowired
	protected ProductDetailServiceImpl(ProductDetailDAO dao) {
		super(dao);
	}

	@Override
	public List<ProductDetail> findAllByProductId(Long productId) {
		return dao.findAllByProductId(productId);
	}

	@Override
	public ProductDetail findByBarcode(String barcode) {
		return dao.findByBarcode(barcode);
	}

	@Override
	public ProductDetail findByProductIdAndTitle(Long productId, String title) {
		return dao.findByProductIdAndTitle(productId, title);
	}
}
