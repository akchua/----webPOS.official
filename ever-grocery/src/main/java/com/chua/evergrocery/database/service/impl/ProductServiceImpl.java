package com.chua.evergrocery.database.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.ProductDAO;
import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.database.service.ProductService;
import com.chua.evergrocery.objects.ObjectList;

@Service
public class ProductServiceImpl
		extends AbstractService<Product, Long, ProductDAO>
		implements ProductService 
{
	@Autowired
	protected ProductServiceImpl(ProductDAO dao) {
		super(dao);
	}
	
	@Override
	public ObjectList<Product> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey, Long companyId) {
		return dao.findAllWithPaging(pageNumber, resultsPerPage, searchKey, companyId);
	}
	
	@Override
	public ObjectList<Product> findAllWithPagingOrderByName(int pageNumber, int resultsPerPage, String searchKey,
			Boolean promoOnly, Long companyId, Long categoryId) {
		if(categoryId != null) {
			return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, promoOnly, companyId, categoryId, new Order[] { Order.desc("mtdOfftake"), Order.asc("name") });
		} else {
			return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, promoOnly, companyId, categoryId, new Order[] { Order.asc("name") });
		}
		
	}
	
	@Override
	public ObjectList<Product> findAllWithPagingOrderByProfit(int pageNumber, int resultsPerPage,
			String searchKey, Long companyId) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, null, companyId, null, new Order[] { Order.desc("profitPercentage") });
	}
	
	@Override
	public ObjectList<Product> findAllWithPagingByCategoryOrderByProfit(int pageNumber, int resultsPerPage,
			String searchKey, Long categoryId) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, null, null, categoryId, new Order[] { Order.desc("categoryProfitPercentage") });
	}
	
	@Override
	public Boolean isExistsByName(String name) {
		return dao.findByName(StringUtils.trimToEmpty(name)) != null;
	}
	
	@Override
	public Boolean isExistsByCode(String code) {
		return dao.findByCode(StringUtils.trimToEmpty(code)) != null;
	}

	@Override
	public List<Product> findAllByCompanyOrderByName(Long companyId) {
		return dao.findAllByCompanyWithOrder(companyId, new Order[] { Order.asc("name") });
	}
	
	@Override
	public List<Product> findAllByCompanyOrderByCategoryAndName(Long companyId) {
		return dao.findAllByCompanyWithOrder(companyId, new Order[] { Order.asc("category.id"), Order.asc("name") });
	}

	@Override
	public List<Product> findAllByCompanyOrderByProfit(Long companyId) {
		return dao.findAllByCompanyWithOrder(companyId, new Order[] { Order.desc("profitPercentage") });
	}

	@Override
	public List<Product> findAllByCategoryOrderByName(Long categoryId) {
		return dao.findAllByCategoryWithOrder(categoryId, new Order[] { Order.asc("company.id"), Order.asc("name")});
	}
}
