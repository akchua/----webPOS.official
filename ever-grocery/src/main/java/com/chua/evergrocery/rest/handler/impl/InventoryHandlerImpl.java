package com.chua.evergrocery.rest.handler.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chua.evergrocery.beans.InventoryBean;
import com.chua.evergrocery.beans.PurchaseSummaryBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.SalesSummaryBean;
import com.chua.evergrocery.constants.FileConstants;
import com.chua.evergrocery.database.entity.Category;
import com.chua.evergrocery.database.entity.Company;
import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.database.entity.ProductDetail;
import com.chua.evergrocery.database.service.CategoryService;
import com.chua.evergrocery.database.service.CompanyService;
import com.chua.evergrocery.database.service.CustomerOrderDetailService;
import com.chua.evergrocery.database.service.ProductDetailService;
import com.chua.evergrocery.database.service.ProductService;
import com.chua.evergrocery.database.service.PurchaseOrderDetailService;
import com.chua.evergrocery.enums.UnitType;
import com.chua.evergrocery.rest.handler.InventoryHandler;
import com.chua.evergrocery.utility.Html;
import com.chua.evergrocery.utility.SimplePdfWriter;
import com.chua.evergrocery.utility.StringHelper;
import com.chua.evergrocery.utility.format.DateFormatter;
import com.chua.evergrocery.utility.template.InventoryTemplate;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 7, 2018
 */
@Transactional
@Component
public class InventoryHandlerImpl implements InventoryHandler {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductDetailService productDetailService;
	
	@Autowired
	private PurchaseOrderDetailService purchaseOrderDetailService;
	
	@Autowired
	private CustomerOrderDetailService customerOrderDetailService;
	
	@Autowired
	private FileConstants fileConstants;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public InventoryBean getProductInventory(Long productId) {
		return getProductInventory(productId, new Date());
	}
	
	@Override
	public InventoryBean getProductInventory(Long productId, Date upTo) {
		final InventoryBean inventory;
		final Product product = productService.find(productId);
		
		if(product != null) {
			LOG.info("## Processing inventory of " + product.getName());
			inventory = new InventoryBean();
			inventory.setProduct(product);
			
			// FOR FIRST INVENTORY
			//final PurchaseSummaryBean purchaseSummary = purchaseOrderDetailService.getPurchaseSummaryByProductAndDeliveryDate(productId, DateUtil.floorDay(product.getCompany().getLastPurchaseOrderDate()), upTo);
			final PurchaseSummaryBean purchaseSummary = purchaseOrderDetailService.getPurchaseSummaryByProductAndDeliveryDate(productId, product.getCompany().getLastPurchaseOrderDate(), upTo);
			inventory.setTotalNetPurchase(purchaseSummary.getNetTotal() != null ? purchaseSummary.getNetTotal() : 0.0d);
			LOG.info("Found total purchase : " + inventory.getTotalNetPurchase());
			final SalesSummaryBean salesSummary = customerOrderDetailService.getSalesSummaryByProductAndDatePaid(productId, product.getCompany().getLastPurchaseOrderDate(), upTo);
			inventory.setTotalBaseSales(salesSummary.getBaseTotal() != null ? salesSummary.getBaseTotal() : 0.0);
			LOG.info("Found total base sales : " + inventory.getTotalBaseSales());
			inventory.setStockBudget(product.getStockBudget() + inventory.getTotalNetPurchase() - inventory.getTotalBaseSales());
			
			final ProductDetail wholeProductDetail = productDetailService.findByProductIdAndTitle(product.getId(), "Whole");
			inventory.setWholePurchasePrice(wholeProductDetail != null ? wholeProductDetail.getNetPrice() : -1.0f);
			inventory.setWholeUnit(wholeProductDetail != null ? wholeProductDetail.getUnitType() : UnitType.DEFAULT);
			inventory.setWholeContent(wholeProductDetail != null && wholeProductDetail.getContent() != null ? wholeProductDetail.getContent() : 0);
			
			final ProductDetail pieceProductDetail = productDetailService.findByProductIdAndTitle(product.getId(), "Piece");
			inventory.setPiecePurchasePrice(pieceProductDetail != null ? pieceProductDetail.getNetPrice() : -1.0f);
			inventory.setPieceUnit(pieceProductDetail != null ? pieceProductDetail.getUnitType() : UnitType.DEFAULT);
			
			String inventoryMessage = "Inventory : ";
			if(inventory.getWholeUnit() != null) inventoryMessage += inventory.getWholeQuantity() + " " + inventory.getWholeUnit().getDisplayName();
			if(inventory.getWholeUnit() != null && inventory.getPieceUnit() != null) inventoryMessage += " AND ";
			if(inventory.getPieceUnit() != null) inventoryMessage += inventory.getPieceQuantity() + " " + inventory.getPieceUnit().getDisplayName();
			
			LOG.info(inventoryMessage);
			
		} else {
			LOG.info("Individual product inventory exited due to invalid productId : " + productId);
			inventory = null;
		}
		
		return inventory;
	}

	@Override
	public List<InventoryBean> getProductInventoryByCompany(Long companyId) {
		return getProductInventoryByCompany(companyId, new Date());
	}
	
	@Override
	public List<InventoryBean> getProductInventoryByCompany(Long companyId, Date upTo) {
		final List<InventoryBean> inventories;
		final Company company = companyService.find(companyId);
		
		if(company != null) {
			final Date start = new Date();
			LOG.info("#### Processing inventory of " + company.getName());
			LOG.info("Last Purchase Order Date : " + company.getFormattedLastPurchaseOrderDate());
			inventories = new ArrayList<InventoryBean>();
			for(Product product : productService.findAllByCompanyOrderByCategoryAndName(companyId)) {
				inventories.add(getProductInventory(product.getId(), upTo));
			}
			final Date end = new Date();
			final Float seconds = (end.getTime() - start.getTime()) / 1000.0f;
			LOG.info("Inventory of " + company.getName() + " complete in " + seconds + "s");
		} else {
			LOG.info("Product inventory by company exited due to invalid companyId : " + companyId);
			inventories = null;
		}
		
		return inventories;
	}
	
	@Override
	public List<InventoryBean> getProductInventoryByCategory(Long categoryId) {
		return getProductInventoryByCategory(categoryId, new Date());
	}
	
	@Override
	public List<InventoryBean> getProductInventoryByCategory(Long categoryId, Date upTo) {
		final List<InventoryBean> inventories;
		final Category category = categoryService.find(categoryId);
		
		if(category != null) {
			final Date start = new Date();
			LOG.info("#### Processing inventory of " + category.getName());
			inventories = new ArrayList<InventoryBean>();
			for(Product product : productService.findAllByCategoryOrderByName(categoryId)) {
				inventories.add(getProductInventory(product.getId(), upTo));
			}
			final Date end = new Date();
			final Float seconds = (end.getTime() - start.getTime()) / 1000.0f;
			LOG.info("Inventory of " + category.getName() + " complete in " + seconds + "s");
		} else {
			LOG.info("Product inventory by category exited due to invalid categoryId : " + categoryId);
			inventories = null;
		}
		
		return inventories;
	}


	@Override
	public ResultBean generateInventory(Long companyId) {
		final ResultBean result;
		final Company company = companyService.find(companyId);
		
		if(company != null) {
			final List<InventoryBean> inventories = getProductInventoryByCompany(companyId);
			
			// Generate text file of inventory
			final String fileName = StringHelper.convertToFileSafeFormat(company.getName()) + "_inventory_" + DateFormatter.fileSafeShortFormat(new Date()) + ".pdf";
			final String filePath = fileConstants.getInventoryHome() + fileName;
			final String temp = new InventoryTemplate(
					company.getName(),
					inventories)
			.merge(velocityEngine);
			SimplePdfWriter.write(temp, "Ever Bazar", filePath, false);
			
			final Map<String, Object> extras = new HashMap<String, Object>();
			extras.put("fileName", fileName);
			result = new ResultBean(Boolean.TRUE, "Done");
			result.setExtras(extras);
			LOG.info("File name : " + fileName);
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("Please select a company."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean generateInventoryByCategoryName(String categoryName) {
		final ResultBean result;
		final Category category = categoryService.findByName(categoryName);
		
		if(category != null) {
			final List<InventoryBean> inventories = getProductInventoryByCategory(category.getId());
			
			// Generate text file of inventory
			final String fileName = StringHelper.convertToFileSafeFormat(category.getName()) + "_inventory_" + DateFormatter.fileSafeShortFormat(new Date()) + ".pdf";
			final String filePath = fileConstants.getInventoryHome() + fileName;
			final String temp = new InventoryTemplate(
					category.getName(),
					inventories)
			.merge(velocityEngine);
			SimplePdfWriter.write(temp, "Ever Bazar", filePath, false);
			
			final Map<String, Object> extras = new HashMap<String, Object>();
			extras.put("fileName", fileName);
			result = new ResultBean(Boolean.TRUE, "Done");
			result.setExtras(extras);
			LOG.info("File name : " + fileName);
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("Please select a category."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean generateCashierInventory() {
		final ResultBean result;
		
		final List<InventoryBean> inventories = getProductInventoryByCategory(categoryService.findByName("Cigarette").getId());
		inventories.addAll(getProductInventoryByCategory(categoryService.findByName("Counter Item").getId()));
		
		// Generate text file of inventory
		final String fileName = "cashier_inventory_" + DateFormatter.fileSafeShortFormat(new Date()) + ".pdf";
		final String filePath = fileConstants.getInventoryHome() + fileName;
		final String temp = new InventoryTemplate(
				"Cashier",
				inventories)
		.merge(velocityEngine);
		SimplePdfWriter.write(temp, "Ever Bazar", filePath, false);
		
		final Map<String, Object> extras = new HashMap<String, Object>();
		extras.put("fileName", fileName);
		result = new ResultBean(Boolean.TRUE, "Done");
		result.setExtras(extras);
		LOG.info("File name : " + fileName);
		
		return result;
	}

	@Override
	public void checkForStockAdjustment(Long customerOrderId) {
		final List<CustomerOrderDetail> customerOrderDetails = customerOrderDetailService.findAllByCustomerOrderId(customerOrderId);
		
		for(CustomerOrderDetail cod : customerOrderDetails) {
			Float diff = cod.getProductDetail().getNetPrice() - (cod.getUnitPrice() / (1 + (cod.getMargin() / 100)));
			if(diff > 0.1f) {
				final Product product = cod.getProductDetail().getProduct();
				final Double newTotalBudget = product.getTotalBudget() - (diff * cod.getQuantity());
				LOG.info("Adjusting Stock budget of " + product.getName() + " from " + product.getStockBudget() + " to " + (newTotalBudget - product.getPurchaseBudget()));
				product.setTotalBudget(newTotalBudget);
				productService.update(product);
			}
		}
	}
}
