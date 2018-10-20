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

import com.chua.evergrocery.beans.PurchaseSummaryBean;
import com.chua.evergrocery.database.entity.Company;
import com.chua.evergrocery.database.entity.CompanyMTDPurchaseSummary;
import com.chua.evergrocery.database.entity.MTDPurchaseSummary;
import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.database.entity.ProductMTDPurchaseSummary;
import com.chua.evergrocery.database.entity.PurchaseOrderDetail;
import com.chua.evergrocery.database.service.CompanyMTDPurchaseSummaryService;
import com.chua.evergrocery.database.service.CompanyService;
import com.chua.evergrocery.database.service.MTDPurchaseSummaryService;
import com.chua.evergrocery.database.service.ProductMTDPurchaseSummaryService;
import com.chua.evergrocery.database.service.ProductService;
import com.chua.evergrocery.database.service.PurchaseOrderDetailService;
import com.chua.evergrocery.rest.handler.PurchaseStatisticsHandler;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
@Transactional
@Component
public class PurchaseStatisticsHandlerImpl implements PurchaseStatisticsHandler {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
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
	
	@Override
	public void updateAllPurchaseStatistics(int includedMonthsAgo) {
		final List<Company> companies = companyService.findAllList();
		
		final int lastMonthId = DateUtil.getMonthId(new Date()) - 1;
		
		for(int monthId = lastMonthId; monthId > lastMonthId - includedMonthsAgo; monthId--) {
			LOG.info("### Processing month of : " + DateFormatter.prettyMonthFormat(monthId));
			final Map<Long, PurchaseSummaryBean> companyPurchaseSummary = new HashMap<Long, PurchaseSummaryBean>();
			
			for(Company company : companies) {
				LOG.info("Processing company : " + company.getName());
				
				final Map<Long, PurchaseSummaryBean> productPurchaseSummary = new HashMap<Long, PurchaseSummaryBean>();
				
				final List<PurchaseOrderDetail> purchaseOrderDetails = purchaseOrderDetailService.findAllByCompanyAndMonthId(company.getId(), monthId);
				
				for(PurchaseOrderDetail pod : purchaseOrderDetails) {
					final Long productId = pod.getProductDetail().getProduct().getId();
					PurchaseSummaryBean pps = productPurchaseSummary.get(productId);
					if(pps == null) pps = new PurchaseSummaryBean();
					pps.setGrossTotal(pps.getGrossTotal() + (pod.getGrossPrice() * pod.getQuantity()));
					pps.setNetTotal(pps.getNetTotal() + pod.getTotalPrice());
					productPurchaseSummary.put(productId, pps);
				}
				
				final PurchaseSummaryBean tempCompanyPurchaseSummary = new PurchaseSummaryBean();
				
				for(Map.Entry<Long, PurchaseSummaryBean> prodPurchaseSummary : productPurchaseSummary.entrySet()) {
					final Float grossTotal = prodPurchaseSummary.getValue().getGrossTotal();
					final Float netTotal = prodPurchaseSummary.getValue().getNetTotal();
					
					ProductMTDPurchaseSummary productMTDPurchaseSummary = productMTDPurchaseSummaryService.findByProductAndMonthId(prodPurchaseSummary.getKey(), monthId);
					if(productMTDPurchaseSummary == null) {
						productMTDPurchaseSummary = new ProductMTDPurchaseSummary();
						productMTDPurchaseSummary.setProduct(productService.find(prodPurchaseSummary.getKey()));
						productMTDPurchaseSummary.setMonthId(monthId);
						productMTDPurchaseSummary.setGrossTotal(grossTotal);
						productMTDPurchaseSummary.setNetTotal(netTotal);
						
						productMTDPurchaseSummaryService.insert(productMTDPurchaseSummary);
					} else {
						if(!(productMTDPurchaseSummary.getGrossTotal().equals(grossTotal)
								&& productMTDPurchaseSummary.getNetTotal().equals(netTotal))) {
							productMTDPurchaseSummary.setGrossTotal(grossTotal);
							productMTDPurchaseSummary.setNetTotal(netTotal);
							productMTDPurchaseSummaryService.update(productMTDPurchaseSummary);
						}
					}
					
					tempCompanyPurchaseSummary.setGrossTotal(tempCompanyPurchaseSummary.getGrossTotal() + grossTotal);
					tempCompanyPurchaseSummary.setNetTotal(tempCompanyPurchaseSummary.getNetTotal() + netTotal);
				}
				
				if(monthId == lastMonthId) {
					for(Map.Entry<Long, PurchaseSummaryBean> prodPurchaseSummary : productPurchaseSummary.entrySet()) {
						final Float netTotal = prodPurchaseSummary.getValue().getNetTotal();
						final Product product = productService.find(prodPurchaseSummary.getKey());
						product.setPurchaseValuePercentage(netTotal / tempCompanyPurchaseSummary.getNetTotal() * 100.0f);
					}
				}
				
				companyPurchaseSummary.put(company.getId(), tempCompanyPurchaseSummary);
			}
			
			final PurchaseSummaryBean totalPurchaseSummary = new PurchaseSummaryBean();
			
			for(Map.Entry<Long, PurchaseSummaryBean> compPurchaseSummary : companyPurchaseSummary.entrySet()) {
				final Float grossTotal = compPurchaseSummary.getValue().getGrossTotal();
				final Float netTotal = compPurchaseSummary.getValue().getNetTotal();
				
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
				
				totalPurchaseSummary.setGrossTotal(totalPurchaseSummary.getGrossTotal() + grossTotal);
				totalPurchaseSummary.setNetTotal(totalPurchaseSummary.getNetTotal() + netTotal);
			}
			
			if(monthId == lastMonthId) {
				for(Map.Entry<Long, PurchaseSummaryBean> compPurchaseSummary : companyPurchaseSummary.entrySet()) {
					final Float netTotal = compPurchaseSummary.getValue().getNetTotal();
					final Company company = companyService.find(compPurchaseSummary.getKey());
					company.setPurchaseValuePercentage(netTotal / totalPurchaseSummary.getNetTotal() * 100.0f);
				}
			}
			
			MTDPurchaseSummary mtdPurchaseSummary = mtdPurchaseSummaryService.findByMonthId(monthId);
			if(mtdPurchaseSummary == null) {
				mtdPurchaseSummary = new MTDPurchaseSummary();
				mtdPurchaseSummary.setMonthId(monthId);
				mtdPurchaseSummary.setGrossTotal(totalPurchaseSummary.getGrossTotal());
				mtdPurchaseSummary.setNetTotal(totalPurchaseSummary.getNetTotal());
				
				mtdPurchaseSummaryService.insert(mtdPurchaseSummary);
			} else {
				if(!(mtdPurchaseSummary.getGrossTotal().equals(totalPurchaseSummary.getGrossTotal())
						&& mtdPurchaseSummary.getNetTotal().equals(totalPurchaseSummary.getNetTotal()))) {
					mtdPurchaseSummary.setGrossTotal(totalPurchaseSummary.getGrossTotal());
					mtdPurchaseSummary.setNetTotal(totalPurchaseSummary.getNetTotal());
					mtdPurchaseSummaryService.update(mtdPurchaseSummary);
				}
			}
		}
	}
}
