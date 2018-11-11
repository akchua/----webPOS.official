package com.chua.evergrocery.rest.handler.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.beans.CompanyPurchaseSummaryBean;
import com.chua.evergrocery.beans.ProductPurchaseSummaryBean;
import com.chua.evergrocery.beans.PurchaseSummaryBean;
import com.chua.evergrocery.database.entity.Company;
import com.chua.evergrocery.database.entity.CompanyMTDPurchaseSummary;
import com.chua.evergrocery.database.entity.MTDPurchaseSummary;
import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.database.entity.ProductMTDPurchaseSummary;
import com.chua.evergrocery.database.service.CompanyMTDPurchaseSummaryService;
import com.chua.evergrocery.database.service.CompanyService;
import com.chua.evergrocery.database.service.MTDPurchaseSummaryService;
import com.chua.evergrocery.database.service.ProductMTDPurchaseSummaryService;
import com.chua.evergrocery.database.service.ProductService;
import com.chua.evergrocery.database.service.PurchaseOrderDetailService;
import com.chua.evergrocery.rest.handler.TransactionSummaryHandler;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 21, 2018
 */
@Component
@Transactional
public class TransactionSummaryHandlerImpl implements TransactionSummaryHandler {

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private PurchaseOrderDetailService purchaseOrderDetailService;
	
	@Autowired
	private ProductMTDPurchaseSummaryService productMTDPurchaseSummaryService;
	
	@Autowired
	private CompanyMTDPurchaseSummaryService companyMTDPurchaseSummaryService;
	
	@Autowired
	private MTDPurchaseSummaryService mtdPurchaseSummaryService;
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public List<MTDPurchaseSummary> getMTDPurchaseSummaryList() {
		return mtdPurchaseSummaryService.findAllOrderByMonthId();
	}
	
	@Override
	public List<MTDPurchaseSummary> getMTDPurchaseSummaryListByYear(int year) {
		return mtdPurchaseSummaryService.findByYearOrderByMonthId(year);
	}
	
	@Override
	public List<CompanyMTDPurchaseSummary> getCompanyMTDPurchaseSummaryList(Long companyId) {
		return companyMTDPurchaseSummaryService.findAllByCompanyOrderByMonthId(companyId);
	}

	@Override
	public void updateAllPurchaseSummaries(int includedMonthsAgo) {
		final List<Company> companies = companyService.findAllList();
		
		final int lastMonthId = DateUtil.getMonthId(new Date()) - 1;
		
		for(int monthId = lastMonthId; monthId > lastMonthId - includedMonthsAgo; monthId--) {
			LOG.info("### Processing month of : " + DateFormatter.prettyMonthFormat(monthId));
			
			final List<CompanyPurchaseSummaryBean> companyPurchaseSummaries = new ArrayList<CompanyPurchaseSummaryBean>();
			
			for(Company company : companies) {
				LOG.info("Processing company : " + company.getName());
				
				final List<ProductPurchaseSummaryBean> productPurchaseSummaries = purchaseOrderDetailService.getAllProductPurchaseSummaryByCompanyAndMonthId(company.getId(), monthId);
				
				final CompanyPurchaseSummaryBean companyPurchaseSummary = new CompanyPurchaseSummaryBean();
				companyPurchaseSummary.setCompanyId(company.getId());
				
				// Process product of each company and add them to the company total
				for(ProductPurchaseSummaryBean pps : productPurchaseSummaries) {
					ProductMTDPurchaseSummary productMTDPurchaseSummary = productMTDPurchaseSummaryService.findByProductAndMonthId(pps.getProductId(), monthId);
					if(productMTDPurchaseSummary == null) {
						productMTDPurchaseSummary = new ProductMTDPurchaseSummary();
						productMTDPurchaseSummary.setProduct(productService.find(pps.getProductId()));
						productMTDPurchaseSummary.setMonthId(monthId);
						productMTDPurchaseSummary.setGrossTotal(pps.getGrossTotal());
						productMTDPurchaseSummary.setNetTotal(pps.getNetTotal());
						
						productMTDPurchaseSummaryService.insert(productMTDPurchaseSummary);
					} else {
						if(!(productMTDPurchaseSummary.getGrossTotal().equals(pps.getGrossTotal())
								&& productMTDPurchaseSummary.getNetTotal().equals(pps.getNetTotal()))) {
							productMTDPurchaseSummary.setGrossTotal(pps.getGrossTotal());
							productMTDPurchaseSummary.setNetTotal(pps.getNetTotal());
							productMTDPurchaseSummaryService.update(productMTDPurchaseSummary);
						}
					}
					
					companyPurchaseSummary.setGrossTotal(companyPurchaseSummary.getGrossTotal() + pps.getGrossTotal());
					companyPurchaseSummary.setNetTotal(companyPurchaseSummary.getNetTotal() + pps.getNetTotal());
				}
				
				// Check if processing last month, then update each product's purchase value percentage
				if(monthId == lastMonthId) {
					for(ProductPurchaseSummaryBean pps : productPurchaseSummaries) {
						final Float netTotal = pps.getNetTotal();
						final Product product = productService.find(pps.getProductId());
						product.setPurchaseValuePercentage(netTotal / companyPurchaseSummary.getNetTotal() * 100.0f);
					}
				}
				
				// Add company purchase summary to map of company purchase summaries
				companyPurchaseSummaries.add(companyPurchaseSummary);
			}
			
			final PurchaseSummaryBean totalPurchaseSummary = new PurchaseSummaryBean();
			
			for(CompanyPurchaseSummaryBean cps : companyPurchaseSummaries) {
				// Save or Update each company purchase summary
				CompanyMTDPurchaseSummary companyMTDPurchaseSummary = companyMTDPurchaseSummaryService.findByCompanyAndMonthId(cps.getCompanyId(), monthId);
				if(companyMTDPurchaseSummary == null) {
					companyMTDPurchaseSummary = new CompanyMTDPurchaseSummary();
					companyMTDPurchaseSummary.setCompany(companyService.find(cps.getCompanyId()));
					companyMTDPurchaseSummary.setMonthId(monthId);
					companyMTDPurchaseSummary.setGrossTotal(cps.getGrossTotal());
					companyMTDPurchaseSummary.setNetTotal(cps.getNetTotal());
					companyMTDPurchaseSummaryService.insert(companyMTDPurchaseSummary);
				} else {
					if(!(companyMTDPurchaseSummary.getGrossTotal().equals(cps.getGrossTotal())
							&& companyMTDPurchaseSummary.getNetTotal().equals(cps.getNetTotal()))) {
						companyMTDPurchaseSummary.setGrossTotal(cps.getGrossTotal());
						companyMTDPurchaseSummary.setNetTotal(cps.getNetTotal());
						companyMTDPurchaseSummaryService.update(companyMTDPurchaseSummary);
					}
				}
				
				// Add company summary to grand total
				totalPurchaseSummary.setGrossTotal(totalPurchaseSummary.getGrossTotal() + cps.getGrossTotal());
				totalPurchaseSummary.setNetTotal(totalPurchaseSummary.getNetTotal() + cps.getNetTotal());
			}
			
			// Check if processing last month, then update each company's purchase value percentage
			if(monthId == lastMonthId) {
				for(CompanyPurchaseSummaryBean cps : companyPurchaseSummaries) {
					final Company company = companyService.find(cps.getCompanyId());
					company.setPurchaseValuePercentage(cps.getNetTotal() / totalPurchaseSummary.getNetTotal() * 100.0f);
				}
			}
			
			// Save or Update the total purchase summary
			MTDPurchaseSummary mtdPurchaseSummary = mtdPurchaseSummaryService.findByMonthId(monthId);
			if(mtdPurchaseSummary == null) {
				mtdPurchaseSummary = new MTDPurchaseSummary();
				mtdPurchaseSummary.setMonthId(monthId);
				mtdPurchaseSummary.setGrossTotal(totalPurchaseSummary.getGrossTotal());
				mtdPurchaseSummary.setNetTotal(totalPurchaseSummary.getNetTotal());
				
				mtdPurchaseSummaryService.insert(mtdPurchaseSummary);
				LOG.info("## New Purchase Summary");
			} else {
				if(!(mtdPurchaseSummary.getGrossTotal().equals(totalPurchaseSummary.getGrossTotal())
						&& mtdPurchaseSummary.getNetTotal().equals(totalPurchaseSummary.getNetTotal()))) {
					mtdPurchaseSummary.setGrossTotal(totalPurchaseSummary.getGrossTotal());
					mtdPurchaseSummary.setNetTotal(totalPurchaseSummary.getNetTotal());
					mtdPurchaseSummaryService.update(mtdPurchaseSummary);
				}
				LOG.info("## Updated Purchase Summary");
			}
		}
	}
}
