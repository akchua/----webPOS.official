package com.chua.evergrocery.rest.handler.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.beans.CompanyPurchaseSummaryBean;
import com.chua.evergrocery.beans.CompanySalesSummaryBean;
import com.chua.evergrocery.beans.ProductPurchaseSummaryBean;
import com.chua.evergrocery.beans.ProductSalesSummaryBean;
import com.chua.evergrocery.beans.PurchaseSummaryBean;
import com.chua.evergrocery.beans.SalesSummaryBean;
import com.chua.evergrocery.database.entity.Company;
import com.chua.evergrocery.database.entity.CompanyDailySalesSummary;
import com.chua.evergrocery.database.entity.CompanyMTDPurchaseSummary;
import com.chua.evergrocery.database.entity.CompanyMTDSalesSummary;
import com.chua.evergrocery.database.entity.DailySalesSummary;
import com.chua.evergrocery.database.entity.MTDPurchaseSummary;
import com.chua.evergrocery.database.entity.MTDSalesSummary;
import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.database.entity.ProductDailySalesSummary;
import com.chua.evergrocery.database.entity.ProductMTDPurchaseSummary;
import com.chua.evergrocery.database.entity.ProductMTDSalesSummary;
import com.chua.evergrocery.database.service.CompanyDailySalesSummaryService;
import com.chua.evergrocery.database.service.CompanyMTDPurchaseSummaryService;
import com.chua.evergrocery.database.service.CompanyMTDSalesSummaryService;
import com.chua.evergrocery.database.service.CompanyService;
import com.chua.evergrocery.database.service.CustomerOrderDetailService;
import com.chua.evergrocery.database.service.DailySalesSummaryService;
import com.chua.evergrocery.database.service.MTDPurchaseSummaryService;
import com.chua.evergrocery.database.service.MTDSalesSummaryService;
import com.chua.evergrocery.database.service.ProductDailySalesSummaryService;
import com.chua.evergrocery.database.service.ProductMTDPurchaseSummaryService;
import com.chua.evergrocery.database.service.ProductMTDSalesSummaryService;
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
	private CustomerOrderDetailService customerOrderDetailService;
	
	@Autowired
	private PurchaseOrderDetailService purchaseOrderDetailService;
	
	@Autowired
	private ProductMTDPurchaseSummaryService productMTDPurchaseSummaryService;
	
	@Autowired
	private CompanyMTDPurchaseSummaryService companyMTDPurchaseSummaryService;
	
	@Autowired
	private MTDPurchaseSummaryService mtdPurchaseSummaryService;
	
	@Autowired
	private ProductMTDSalesSummaryService productMTDSalesSummaryService;
	
	@Autowired
	private CompanyMTDSalesSummaryService companyMTDSalesSummaryService;
	
	@Autowired
	private MTDSalesSummaryService mtdSalesSummaryService;
	
	@Autowired
	private DailySalesSummaryService dailySalesSummaryService;
	
	@Autowired
	private ProductDailySalesSummaryService productDailySalesSummaryService;
	
	@Autowired
	private CompanyDailySalesSummaryService companyDailySalesSummaryService;
	
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
	public List<MTDSalesSummary> getMTDSalesSummaryList() {
		return mtdSalesSummaryService.findAllOrderByMonthId();
	}

	@Override
	public List<MTDSalesSummary> getMTDSalesSummaryListByYear(int year) {
		return mtdSalesSummaryService.findByYearOrderByMonthId(year);
	}

	@Override
	public List<CompanyMTDSalesSummary> getCompanyMTDSalesSummaryList(Long companyId) {
		return companyMTDSalesSummaryService.findAllByCompanyOrderByMonthId(companyId);
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
						final Product product = productService.find(pps.getProductId());
						product.setPurchaseValuePercentage(companyPurchaseSummary.getNetTotal().equals(0.0f) ? 0.0f : pps.getNetTotal() / companyPurchaseSummary.getNetTotal() * 100.0f);
						productService.update(product);
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
					company.setPurchaseValuePercentage(totalPurchaseSummary.getNetTotal().equals(0.0f) ? 0.0f : cps.getNetTotal() / totalPurchaseSummary.getNetTotal() * 100.0f);
					companyService.update(company);
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
	
	@Override
	public void updateMonthlySalesSummaries(int includedMonthsAgo) {
		final List<Company> companies = companyService.findAllList();
		
		final int lastMonthId = DateUtil.getMonthId(new Date()) - 1;
		
		for(int monthId = lastMonthId; monthId > lastMonthId - includedMonthsAgo; monthId--) {
			LOG.info("### Processing month of : " + DateFormatter.prettyMonthFormat(monthId));
			
			final List<CompanySalesSummaryBean> companySalesSummaries = new ArrayList<CompanySalesSummaryBean>();
			
			for(Company company : companies) {
				LOG.info("Processing company : " + company.getName());
				
				final List<ProductSalesSummaryBean> productSalesSummaries = productDailySalesSummaryService.getAllProductSalesSummaryByCompanyAndMonthId(company.getId(), monthId);
				
				final CompanySalesSummaryBean companySalesSummary = new CompanySalesSummaryBean();
				companySalesSummary.setCompanyId(company.getId());
				
				// Process product of each company and add them to the company total
				for(ProductSalesSummaryBean pss : productSalesSummaries) {
					ProductMTDSalesSummary productMTDSalesSummary = productMTDSalesSummaryService.findByProductAndMonthId(pss.getProductId(), monthId);
					if(productMTDSalesSummary == null) {
						productMTDSalesSummary = new ProductMTDSalesSummary();
						productMTDSalesSummary.setProduct(productService.find(pss.getProductId()));
						productMTDSalesSummary.setMonthId(monthId);
						productMTDSalesSummary.setNetTotal(pss.getNetTotal());
						productMTDSalesSummary.setBaseTotal(pss.getBaseTotal());
						
						productMTDSalesSummaryService.insert(productMTDSalesSummary);
					} else {
						if(!(productMTDSalesSummary.getNetTotal().equals(pss.getNetTotal())
								&& productMTDSalesSummary.getBaseTotal().equals(pss.getBaseTotal()))) {
							productMTDSalesSummary.setNetTotal(pss.getNetTotal());
							productMTDSalesSummary.setBaseTotal(pss.getBaseTotal());
							productMTDSalesSummaryService.update(productMTDSalesSummary);
						}
					}
					
					companySalesSummary.setNetTotal(companySalesSummary.getNetTotal() + pss.getNetTotal());
					companySalesSummary.setBaseTotal(companySalesSummary.getBaseTotal() + pss.getBaseTotal());
				}
				
				// Check if processing last month, then update each product's sales value and profit percentage
				if(monthId == lastMonthId) {
					for(ProductSalesSummaryBean pss : productSalesSummaries) {
						final Product product = productService.find(pss.getProductId());
						product.setSaleValuePercentage(companySalesSummary.getNetTotal().equals(0.0f) ? 0.0f : pss.getNetTotal() / companySalesSummary.getNetTotal() * 100.0f);
						product.setProfitPercentage(companySalesSummary.getBaseTotal().equals(0.0f) ? 0.0f : pss.getBaseTotal() / companySalesSummary.getBaseTotal() * 100.0f);
						productService.update(product);
					}
				}
				
				// Add company purchase summary to map of company sales summaries
				companySalesSummaries.add(companySalesSummary);
			}
			
			final SalesSummaryBean totalSalesSummary = new SalesSummaryBean();
			
			for(CompanySalesSummaryBean css : companySalesSummaries) {
				// Save or Update each company sales summary
				CompanyMTDSalesSummary companyMTDSalesSummary = companyMTDSalesSummaryService.findByCompanyAndMonthId(css.getCompanyId(), monthId);
				if(companyMTDSalesSummary == null) {
					companyMTDSalesSummary = new CompanyMTDSalesSummary();
					companyMTDSalesSummary.setCompany(companyService.find(css.getCompanyId()));
					companyMTDSalesSummary.setMonthId(monthId);
					companyMTDSalesSummary.setNetTotal(css.getNetTotal());
					companyMTDSalesSummary.setBaseTotal(css.getBaseTotal());
					companyMTDSalesSummaryService.insert(companyMTDSalesSummary);
				} else {
					if(!(companyMTDSalesSummary.getNetTotal().equals(css.getNetTotal())
							&& companyMTDSalesSummary.getBaseTotal().equals(css.getBaseTotal()))) {
						companyMTDSalesSummary.setNetTotal(css.getNetTotal());
						companyMTDSalesSummary.setBaseTotal(css.getBaseTotal());
						companyMTDSalesSummaryService.update(companyMTDSalesSummary);
					}
				}
				
				// Add company summary to grand total
				totalSalesSummary.setNetTotal(totalSalesSummary.getNetTotal() + css.getNetTotal());
				totalSalesSummary.setBaseTotal(totalSalesSummary.getBaseTotal() + css.getBaseTotal());
			}
			
			// Check if processing last month, then update each company's sales value and profit percentage
			if(monthId == lastMonthId) {
				for(CompanySalesSummaryBean css : companySalesSummaries) {
					final Company company = companyService.find(css.getCompanyId());
					company.setSaleValuePercentage(totalSalesSummary.getNetTotal().equals(0.0f) ? 0.0f : css.getNetTotal() / totalSalesSummary.getNetTotal() * 100.0f);
					company.setProfitPercentage(totalSalesSummary.getBaseTotal().equals(0.0f) ? 0.0f : css.getBaseTotal() / totalSalesSummary.getBaseTotal() * 100.0f);
					companyService.update(company);
				}
			}
			
			// Save or Update the total sales summary
			MTDSalesSummary mtdSalesSummary = mtdSalesSummaryService.findByMonthId(monthId);
			if(mtdSalesSummary == null) {
				mtdSalesSummary = new MTDSalesSummary();
				mtdSalesSummary.setMonthId(monthId);
				mtdSalesSummary.setNetTotal(totalSalesSummary.getNetTotal());
				mtdSalesSummary.setBaseTotal(totalSalesSummary.getBaseTotal());
				
				mtdSalesSummaryService.insert(mtdSalesSummary);
				LOG.info("## New Sales Summary");
			} else {
				if(!(mtdSalesSummary.getNetTotal().equals(totalSalesSummary.getNetTotal())
						&& mtdSalesSummary.getBaseTotal().equals(totalSalesSummary.getBaseTotal()))) {
					mtdSalesSummary.setNetTotal(totalSalesSummary.getNetTotal());
					mtdSalesSummary.setBaseTotal(totalSalesSummary.getBaseTotal());
					mtdSalesSummaryService.update(mtdSalesSummary);
				}
				LOG.info("## Updated Sales Summary");
			}
		}
	}
	
	@Override
	public void updateDailySalesSummaries(int includedDaysAgo) {
		final List<Company> companies = companyService.findAllList();
		
		final Calendar currentDay = Calendar.getInstance();
		
		for(int i = 0; i < includedDaysAgo; i++) {
			LOG.info("### Processing date : " + DateFormatter.prettyFormat(currentDay.getTime()));
			
			final List<CompanySalesSummaryBean> companySalesSummaries = new ArrayList<CompanySalesSummaryBean>();
			
			for(Company company : companies) {
				LOG.info("Processing company : " + company.getName());
				
				final List<ProductSalesSummaryBean> productSalesSummaries = customerOrderDetailService.getAllProductSalesSummaryByCompanyAndDate(company.getId(), currentDay.getTime());
				
				final CompanySalesSummaryBean companySalesSummary = new CompanySalesSummaryBean();
				companySalesSummary.setCompanyId(company.getId());
				
				// Process product of each company and add them to the company total
				for(ProductSalesSummaryBean pss : productSalesSummaries) {
					ProductDailySalesSummary productDailySalesSummary = productDailySalesSummaryService.findByProductAndSalesDate(pss.getProductId(), currentDay.getTime());
					if(productDailySalesSummary == null) {
						productDailySalesSummary = new ProductDailySalesSummary();
						productDailySalesSummary.setProduct(productService.find(pss.getProductId()));
						productDailySalesSummary.setSalesDate(DateUtil.floorDay(currentDay.getTime()));
						productDailySalesSummary.setNetTotal(pss.getNetTotal());
						productDailySalesSummary.setBaseTotal(pss.getBaseTotal());
						
						productDailySalesSummaryService.insert(productDailySalesSummary);
					} else {
						if(!(productDailySalesSummary.getNetTotal().equals(pss.getNetTotal())
								&& productDailySalesSummary.getBaseTotal().equals(pss.getBaseTotal()))) {
							productDailySalesSummary.setNetTotal(pss.getNetTotal());
							productDailySalesSummary.setBaseTotal(pss.getBaseTotal());
							productDailySalesSummaryService.update(productDailySalesSummary);
						}
					}
					System.out.println("Product " + productDailySalesSummary.getProduct().getName() + " Company " + productDailySalesSummary.getProduct().getCompany().getName() + " Found " + productDailySalesSummary.getNetTotal());
					
					companySalesSummary.setNetTotal(companySalesSummary.getNetTotal() + pss.getNetTotal());
					companySalesSummary.setBaseTotal(companySalesSummary.getBaseTotal() + pss.getBaseTotal());
				}
				
				// Add company purchase summary to map of company purchase summaries
				companySalesSummaries.add(companySalesSummary);
			}
			
			final SalesSummaryBean totalSalesSummary = new SalesSummaryBean();
			
			for(CompanySalesSummaryBean css : companySalesSummaries) {
				// Save or Update each company purchase summary
				CompanyDailySalesSummary companyDailySalesSummary = companyDailySalesSummaryService.findByCompanyAndSalesDate(css.getCompanyId(), currentDay.getTime());
				if(companyDailySalesSummary == null) {
					companyDailySalesSummary = new CompanyDailySalesSummary();
					companyDailySalesSummary.setCompany(companyService.find(css.getCompanyId()));
					companyDailySalesSummary.setSalesDate(DateUtil.floorDay(currentDay.getTime()));
					companyDailySalesSummary.setNetTotal(css.getNetTotal());
					companyDailySalesSummary.setBaseTotal(css.getBaseTotal());
					companyDailySalesSummaryService.insert(companyDailySalesSummary);
				} else {
					if(!(companyDailySalesSummary.getNetTotal().equals(css.getNetTotal())
							&& companyDailySalesSummary.getBaseTotal().equals(css.getBaseTotal()))) {
						companyDailySalesSummary.setNetTotal(css.getNetTotal());
						companyDailySalesSummary.setBaseTotal(css.getBaseTotal());
						companyDailySalesSummaryService.update(companyDailySalesSummary);
					}
				}
				
				// Add company summary to grand total
				totalSalesSummary.setNetTotal(totalSalesSummary.getNetTotal() + css.getNetTotal());
				totalSalesSummary.setBaseTotal(totalSalesSummary.getBaseTotal() + css.getBaseTotal());
			}
			
			// Save or Update the total purchase summary
			DailySalesSummary dailySalesSummary = dailySalesSummaryService.findBySalesDate(currentDay.getTime());
			if(dailySalesSummary == null) {
				dailySalesSummary = new DailySalesSummary();
				dailySalesSummary.setSalesDate(DateUtil.floorDay(currentDay.getTime()));
				dailySalesSummary.setNetTotal(totalSalesSummary.getNetTotal());
				dailySalesSummary.setBaseTotal(totalSalesSummary.getBaseTotal());
				
				dailySalesSummaryService.insert(dailySalesSummary);
				LOG.info("## New Daily Sales Summary");
			} else {
				if(!(dailySalesSummary.getNetTotal().equals(totalSalesSummary.getNetTotal())
						&& dailySalesSummary.getBaseTotal().equals(totalSalesSummary.getBaseTotal()))) {
					dailySalesSummary.setNetTotal(totalSalesSummary.getNetTotal());
					dailySalesSummary.setBaseTotal(totalSalesSummary.getBaseTotal());
					dailySalesSummaryService.update(dailySalesSummary);
				}
				LOG.info("## Updated Daily Sales Summary");
			}
			
			currentDay.add(Calendar.DAY_OF_MONTH, -1);
		}
	}
}
