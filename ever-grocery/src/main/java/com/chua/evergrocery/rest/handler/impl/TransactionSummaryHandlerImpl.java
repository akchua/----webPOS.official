package com.chua.evergrocery.rest.handler.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
			
			final Map<Long, PurchaseSummaryBean> companyPurchaseSummary = new HashMap<Long, PurchaseSummaryBean>();
			
			for(Company company : companies) {
				LOG.info("Processing company : " + company.getName());
				
				final List<ProductPurchaseSummaryBean> productPurchaseSummaries = purchaseOrderDetailService.getAllProductPurchaseSummaryByCompanyAndMonthId(company.getId(), monthId);
				
				final PurchaseSummaryBean tempCompanyPurchaseSummary = new PurchaseSummaryBean();
				
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
					
					tempCompanyPurchaseSummary.setGrossTotal(tempCompanyPurchaseSummary.getGrossTotal() + pps.getGrossTotal());
					tempCompanyPurchaseSummary.setNetTotal(tempCompanyPurchaseSummary.getNetTotal() + pps.getNetTotal());
				}
				
				// Check if processing last month, then update each product's purchase value percentage
				if(monthId == lastMonthId) {
					for(ProductPurchaseSummaryBean pps : productPurchaseSummaries) {
						final Float netTotal = pps.getNetTotal();
						final Product product = productService.find(pps.getProductId());
						product.setPurchaseValuePercentage(netTotal / tempCompanyPurchaseSummary.getNetTotal() * 100.0f);
					}
				}
				
				// Add company purchase summary to map of company purchase summaries
				companyPurchaseSummary.put(company.getId(), tempCompanyPurchaseSummary);
			}
			
			final PurchaseSummaryBean totalPurchaseSummary = new PurchaseSummaryBean();
			
			for(Map.Entry<Long, PurchaseSummaryBean> compPurchaseSummary : companyPurchaseSummary.entrySet()) {
				final Float grossTotal = compPurchaseSummary.getValue().getGrossTotal();
				final Float netTotal = compPurchaseSummary.getValue().getNetTotal();
				
				// Save or Update each company purchase summary
				CompanyMTDPurchaseSummary companyMTDPurchaseSummary = companyMTDPurchaseSummaryService.findByCompanyAndMonthId(compPurchaseSummary.getKey(), monthId);
				if(companyMTDPurchaseSummary == null) {
					companyMTDPurchaseSummary = new CompanyMTDPurchaseSummary();
					companyMTDPurchaseSummary.setCompany(companyService.find(compPurchaseSummary.getKey()));
					companyMTDPurchaseSummary.setMonthId(monthId);
					companyMTDPurchaseSummary.setGrossTotal(grossTotal);
					companyMTDPurchaseSummary.setNetTotal(netTotal);
					companyMTDPurchaseSummaryService.insert(companyMTDPurchaseSummary);
				} else {
					if(!(companyMTDPurchaseSummary.getGrossTotal().equals(grossTotal)
							&& companyMTDPurchaseSummary.getNetTotal().equals(netTotal))) {
						companyMTDPurchaseSummary.setGrossTotal(grossTotal);
						companyMTDPurchaseSummary.setNetTotal(netTotal);
						companyMTDPurchaseSummaryService.update(companyMTDPurchaseSummary);
					}
				}
				
				// Add company summary to grand total
				totalPurchaseSummary.setGrossTotal(totalPurchaseSummary.getGrossTotal() + grossTotal);
				totalPurchaseSummary.setNetTotal(totalPurchaseSummary.getNetTotal() + netTotal);
			}
			
			// Check if processing last month, then update each company's purchase value percentage
			if(monthId == lastMonthId) {
				for(Map.Entry<Long, PurchaseSummaryBean> compPurchaseSummary : companyPurchaseSummary.entrySet()) {
					final Float netTotal = compPurchaseSummary.getValue().getNetTotal();
					final Company company = companyService.find(compPurchaseSummary.getKey());
					company.setPurchaseValuePercentage(netTotal / totalPurchaseSummary.getNetTotal() * 100.0f);
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
