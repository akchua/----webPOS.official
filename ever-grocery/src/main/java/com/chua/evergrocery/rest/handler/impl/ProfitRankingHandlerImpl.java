package com.chua.evergrocery.rest.handler.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.database.entity.Company;
import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.database.service.CompanyService;
import com.chua.evergrocery.database.service.ProductService;
import com.chua.evergrocery.rest.handler.ProfitRankingHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 2, 2019
 */
@Transactional
@Component
public class ProfitRankingHandlerImpl implements ProfitRankingHandler {

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ProductService productService;

	@Override
	public void updateAllProductProfitRankings() {
		final List<Company> companies = companyService.findAllList();
		
		for(Company company : companies) {
			final List<Product> products = productService.findAllByCompanyOrderByProfit(company.getId());
			
			for(int i = 0; i < products.size(); i++) {
				final Product product = products.get(i);
				product.setPreviousProfitRank(product.getCurrentProfitRank());
				product.setCurrentProfitRank(i + 1);
			}
			
			productService.batchUpdate(products);
		}
	}

	@Override
	public void updateAllCompanyProfitRankings() {
		final List<Company> companies = companyService.findAllOrderByProfit();
		
		for(int i = 0; i < companies.size(); i++) {
			final Company company = companies.get(i);
			company.setPreviousProfitRank(company.getCurrentProfitRank());
			company.setCurrentProfitRank(i + 1);
		}
		
		companyService.batchUpdate(companies);
	}
}
