package com.chua.evergrocery.rest.handler.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.beans.InventoryBean;
import com.chua.evergrocery.beans.ProductStatisticsBean;
import com.chua.evergrocery.beans.PurchaseOrderFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.constants.FileConstants;
import com.chua.evergrocery.database.entity.Company;
import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.database.entity.ProductDetail;
import com.chua.evergrocery.database.entity.PurchaseOrder;
import com.chua.evergrocery.database.entity.PurchaseOrderDetail;
import com.chua.evergrocery.database.service.CompanyService;
import com.chua.evergrocery.database.service.CustomerOrderDetailService;
import com.chua.evergrocery.database.service.ProductDetailService;
import com.chua.evergrocery.database.service.ProductService;
import com.chua.evergrocery.database.service.PurchaseOrderDetailService;
import com.chua.evergrocery.database.service.PurchaseOrderService;
import com.chua.evergrocery.database.service.UserService;
import com.chua.evergrocery.enums.Color;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.enums.UnitType;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.PurchaseOrderHandler;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.Html;
import com.chua.evergrocery.utility.StringHelper;
import com.chua.evergrocery.utility.TextWriter;
import com.chua.evergrocery.utility.format.DateFormatter;
import com.chua.evergrocery.utility.template.GeneratedPurchaseTemplate;
import com.chua.evergrocery.utility.template.InventoryTemplate;

@Transactional
@Component
public class PurchaseOrderHandlerImpl implements PurchaseOrderHandler {

	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@Autowired
	private PurchaseOrderDetailService purchaseOrderDetailService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductDetailService productDetailService;
	
	@Autowired
	private CustomerOrderDetailService customerOrderDetailService;
	
	@Autowired
	private FileConstants fileConstants;
	
	@Autowired
	private VelocityEngine velocityEngine;

	@Override
	public ObjectList<PurchaseOrder> getPurchaseOrderList(Integer pageNumber, Long companyId, Boolean showChecked) {
		if(showChecked) {
			return purchaseOrderService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), companyId);
		} else {
			return this.getActivePurchaseOrderList(pageNumber, companyId);
		}
	}
	
	private ObjectList<PurchaseOrder> getActivePurchaseOrderList(Integer pageNumber, Long companyId) {
		return purchaseOrderService.findAllWithPagingAndStatus(pageNumber, UserContextHolder.getItemsPerPage(), companyId, new Status[] { Status.LISTING });
	}

	@Override
	public PurchaseOrder getPurchaseOrder(Long purchaseOrderId) {
		return (purchaseOrderId != null && purchaseOrderId > 0l) ? purchaseOrderService.find(purchaseOrderId) : null;
	}

	@Override
	public ResultBean createPurchaseOrder(PurchaseOrderFormBean purchaseOrderForm) {
		final ResultBean result;
		
		if(purchaseOrderForm.getCompanyId() != null) {
			final PurchaseOrder purchaseOrder = new PurchaseOrder();
			setPurchaseOrder(purchaseOrder, purchaseOrderForm);
			purchaseOrder.setTotalAmount(0.0f);
			purchaseOrder.setTotalItems(0);
			
			purchaseOrder.setCreator(userService.find(UserContextHolder.getUser().getId()));
			purchaseOrder.setStatus(Status.LISTING);
			purchaseOrder.setDeliveredOn(purchaseOrderForm.getDeliveredOn());
			purchaseOrder.setCheckedOn(DateUtil.getDefaultDate());
			
			result = new ResultBean();
			result.setSuccess(purchaseOrderService.insert(purchaseOrder) != null);
			if(result.getSuccess()) {
				Map<String, Object> extras = new HashMap<String, Object>();
				extras.put("purchaseOrderId", purchaseOrder.getId());
				result.setExtras(extras);
				
				result.setMessage("Purchase order successfully created.");
			} else {
				result.setMessage("Failed to create purchase order.");
			}
		} else {
			result = new ResultBean(Boolean.FALSE, "Select a company.");
		}
		
		return result;
	}
	
	@Override
	public ResultBean generatePurchaseOrder(Long companyId, Float daysToBook) {
		// Storing start time of this method
		final Date generateStartTime = new Date();
		
		final ResultBean result;
		final Company company = companyService.find(companyId);
		
		if(company != null) {
		if(daysToBook >= 3) {
			// Check if last purchase order date is more than 3 days ago
			if(Days.daysBetween(new DateTime(company.getLastPurchaseOrderDate()), new DateTime()).getDays() > 3) {
				// Retrieve purchase orders delivered within ninety days from now
				final List<PurchaseOrder> deliveredAfterCutoff = purchaseOrderService.findDeliveredAfterCutoffByCompanyOrderByDeliveryDate(company.getId());
				System.out.println("# of deliveries after " + DateFormatter.prettyFormat(DateUtil.getOrderCutoffDate()) + " : " + deliveredAfterCutoff.size());
				
				if(!deliveredAfterCutoff.isEmpty()) {
					/*// Computing for delivery rate
					Float totalDeliveryTime = 0.0f;
					int deliveryCount = 0;
					for(int i = 1; i < deliveredAfterCutoff.size(); i++) {
						int deliveryTime = Days.daysBetween(new DateTime(deliveredAfterCutoff.get(i).getDeliveredOn()), new DateTime(deliveredAfterCutoff.get(i - 1).getDeliveredOn())).getDays();
						totalDeliveryTime += deliveryTime;
						if(deliveryTime > 0) deliveryCount++;
					}
					final Float deliveryRate = deliveryCount > 0 ? totalDeliveryTime / deliveryCount : 7.0f;
					System.out.println("Computed delivery rate : " + deliveryRate);*/
					
					// Determining last purchase order date
					Calendar lastPODate = Calendar.getInstance();
					lastPODate.setTime(company.getLastPurchaseOrderDate());
					if(lastPODate.getTimeInMillis() == DateUtil.getDefaultDateInMillis()) {
						lastPODate.setTime(deliveredAfterCutoff.get(0).getDeliveredOn());
					}
					System.out.println("Last PO Date : " + lastPODate.getTime());
					
					// Determining last booked days
					if(company.getDaysBooked().equals(0.0f)) {
						company.setDaysBooked(Days.daysBetween(new DateTime(lastPODate.getTime()), new DateTime()).getDays() / 1.0f);
					}
					System.out.println("Last Booked Days : " + company.getDaysBooked());
					
					// Separating deliveries after last purchase order
					final List<PurchaseOrder> deliveriesAfterLastPO = new ArrayList<PurchaseOrder>();
					for(PurchaseOrder po : deliveredAfterCutoff) {
						Calendar deliveredOn = Calendar.getInstance();
						deliveredOn.setTime(po.getDeliveredOn());
						if(deliveredOn.get(Calendar.YEAR) >= lastPODate.get(Calendar.YEAR) &&
								deliveredOn.get(Calendar.DAY_OF_YEAR) >= lastPODate.get(Calendar.DAY_OF_YEAR)) {
							deliveriesAfterLastPO.add(po);
						}
					}
					System.out.println("# of deliveries after last PO : " + deliveriesAfterLastPO.size());
					
					// Mapping the net amount to the product id of all products included in "deliveriesAfterLastPO"
					final Map<Long, Float> lastPurchaseNetAmount = new HashMap<Long, Float>();
					for(PurchaseOrder po : deliveriesAfterLastPO) {
						System.out.println("Processing PO #" + po.getId());
						final List<PurchaseOrderDetail> poDetails = purchaseOrderDetailService.findAllByPurchaseOrderId(po.getId());
						for(PurchaseOrderDetail poDetail : poDetails) {
							System.out.println("Found : " + poDetail.getProductName() + " worth " + poDetail.getFormattedNetPrice());
							final Long productId = poDetail.getProductDetail().getProduct().getId();
							Float netPurchaseAmount = (lastPurchaseNetAmount.get(productId) == null) ? 0.0f : lastPurchaseNetAmount.get(productId);
							netPurchaseAmount += poDetail.getTotalPrice();
							lastPurchaseNetAmount.put(productId, netPurchaseAmount);
							System.out.println("Updating purchase net amount to : " + netPurchaseAmount);
						}
					}
					
					// Retrieve all products of the company
					final List<Product> products = productService.findAllByCompanyOrderByName(companyId);
					
					// Storage for printout data
					final List<ProductStatisticsBean> productStats = new ArrayList<ProductStatisticsBean>();
					
					for(Product product : products) {
						System.out.println("######## Processing product : " + product.getName() + " ########");
						
						// Check if any last purchase net amount
						final Float netPurchaseAmount = (lastPurchaseNetAmount.get(product.getId()) == null) ? 0.0f : lastPurchaseNetAmount.get(product.getId());
						
						// Adjust total and purchase budget (only on over purchase)
						if(netPurchaseAmount > product.getPurchaseBudget()) {
							final Float previousStockBudget = product.getStockBudget();
							System.out.println("Adjusting product purchase budget to : " + netPurchaseAmount);
							product.setPurchaseBudget(netPurchaseAmount);
							System.out.println("Adjusting product total budget to : " + (netPurchaseAmount + previousStockBudget));
							product.setTotalBudget(netPurchaseAmount + previousStockBudget);
						}
						
						// Storing available printout data
						final ProductStatisticsBean productStat = new ProductStatisticsBean();
						productStat.setProductId(product.getId());
						productStat.setProductName(product.getName());
						productStat.setPreviousSaleRate(product.getSaleRate());
						productStat.setPreviousTotalBudget(product.getTotalBudget());
						
						// Compute for ACTUAL BUDGET (Adjusted Stock Budget + Net Purchase Amount)
						final Float actualBudget = product.getStockBudget() + netPurchaseAmount;
						System.out.println("Computed actual budget : " + actualBudget);
						
						// Retrieve all sales on or after last PO date
						final List<CustomerOrderDetail> customerOrderDetails = customerOrderDetailService.findAllByProductLimitByDate(product.getId(), lastPODate.getTime());
						Float netSalesAmount = 0.0f;
						Float netProfitAmount = 0.0f;
						for(CustomerOrderDetail coDetail : customerOrderDetails) {
							Float salesAmount = coDetail.getTotalPrice() / (1 + (coDetail.getMargin() / 100));
							netSalesAmount += salesAmount;
							netProfitAmount += coDetail.getTotalPrice() - salesAmount;
						}
						System.out.println("Total sales : " + netSalesAmount);
						System.out.println("Total profit : " + netProfitAmount);
						
						// Compute for sale rate (net sales amount / actual budget * 100) [cap value between 0-100%]
						Float tempRate = actualBudget.equals(0.0f) ? 0 : netSalesAmount / actualBudget * 100;
						final Float saleRate = tempRate > 100.0f ? 100.0f : (tempRate < 0.0f ? 0.0f : tempRate);
						System.out.println("Computed sale rate : " + saleRate);
						
						// Compute adjustment rate {actual value to be multiplied to budget for simplicity}
						// 20/30 Maximum increase of 25% at 70-100% sales AND 30/70 Maximum decrease of 30% at 0-70% sales
						Float tempAdjustmentRate = (saleRate - 70.0f) * (saleRate >= 70.0f ? (25.0f / 30.0f) : (30.0f / 70.0f)) / 100.0f;
						if((saleRate >= 95.0f && product.getSaleRate() >= 95.0f) || (saleRate <= 30 && product.getSaleRate() <= 30)) {
							tempAdjustmentRate *= 2.0f;
						}
						final Float adjustmentRate = 1.0f + tempAdjustmentRate;
						System.out.println("Computed adjustment rate : " + adjustmentRate);
						
						// Compute new total budget (total budget * adjustment rate)
						Float tempTotalBudget = actualBudget * adjustmentRate;
						tempTotalBudget = saleRate >= 70 ? Math.max(tempTotalBudget, product.getTotalBudget()) : Math.min(tempTotalBudget, product.getTotalBudget());
						/*// apply delivery rate ratio
						if(!company.getDeliveryRate().equals(0.0f)) tempTotalBudget = (tempTotalBudget / company.getDeliveryRate()) * deliveryRate;*/
						// adjust to number of days to book
						if(!company.getDaysBooked().equals(0.0f)) tempTotalBudget = (tempTotalBudget / company.getDaysBooked()) * daysToBook;
						product.setTotalBudget(tempTotalBudget);
						System.out.println("Computed new total budget : " + product.getTotalBudget());
						
						// Compute new purchase budget (total budget - (actual budget - sales))
						final Float actualStock = actualBudget - netSalesAmount;
						product.setPurchaseBudget(product.getTotalBudget() - (actualStock > 0.0f ? actualStock : 0.0f));
						System.out.println("Computed new purchase budget : " + product.getPurchaseBudget());
						
						// Applying new sale rate to product
						product.setSaleRate(saleRate);
						
						// Storing available printout data
						productStat.setSales(netSalesAmount);
						productStat.setProfit(netProfitAmount);
						productStat.setCurrentPurchaseBudget(product.getPurchaseBudget());
						productStat.setCurrentTotalBudget(product.getTotalBudget());
						productStat.setCurrentSaleRate(product.getSaleRate());
						
						// Allocate budget
						final ProductDetail wholeProductDetail = productDetailService.findByProductIdAndTitle(product.getId(), "Whole");
						final ProductDetail pieceProductDetail = productDetailService.findByProductIdAndTitle(product.getId(), "Piece");
						
						final Float wholePurchasePrice = wholeProductDetail != null ? wholeProductDetail.getNetPrice() : -1.0f;
						final Float piecePurchasePrice = pieceProductDetail != null ? pieceProductDetail.getNetPrice() : -1.0f;
						
						Integer quantity = 0;
						UnitType unit;
						
						if(Math.abs(product.getPurchaseBudget() / wholePurchasePrice) < 2.0f) {
							quantity = Math.round(product.getPurchaseBudget() / piecePurchasePrice);
							unit = pieceProductDetail.getUnitType();
						} else if(product.getPurchaseBudget() / wholePurchasePrice > 100.0f) {
							// if quantity is more than 100 wholes round to the nearest 10s
							quantity = Math.round(product.getPurchaseBudget() / piecePurchasePrice / 10) * 10;
							unit = wholeProductDetail.getUnitType();
						} else if(product.getPurchaseBudget() / wholePurchasePrice > 30.0f) {
							// if quantity is more than 30 wholes round to the nearest 5s
							quantity = Math.round(product.getPurchaseBudget() / piecePurchasePrice / 5) * 5;
							unit = wholeProductDetail.getUnitType();
						} else {
							quantity = Math.round(product.getPurchaseBudget() / wholePurchasePrice);
							unit = wholeProductDetail.getUnitType();
						}
						
						productStat.setQuantity(quantity);
						productStat.setUnit(unit != null ? unit : UnitType.DEFAULT);
						if(quantity != 0) {
							productStats.add(productStat);
						}
						System.out.println("Allocated purchase : " + quantity + " " + unit);
					}
					
					// Computing expected delivery date (30% of previous days booked)
					Calendar expectedDeliveryDate = Calendar.getInstance();
					int maxDaysToDeliver = company.getDaysBooked().equals(0.0f) ? 2 : (int) Math.floor(company.getDaysBooked() * 0.30f);
					expectedDeliveryDate.add(Calendar.DAY_OF_MONTH, maxDaysToDeliver);
					
					// Applying changes to company
					/*company.setDeliveryRate(deliveryRate);*/
					company.setDaysBooked(daysToBook);
					company.setLastPurchaseOrderDate(generateStartTime);
					
					// Saving changes
					productService.batchUpdate(products);
					companyService.update(company);
								
					// Generate text file of generated purchase order
					final String fileName = StringHelper.convertToFileSafeFormat(company.getName()) + "_" + DateFormatter.fileSafeFormat(new Date()) + ".txt";
					final String filePath = fileConstants.getGeneratePurchasesHome() + fileName;
					final String temp = new GeneratedPurchaseTemplate(
							company.getName(),
							lastPODate.getTime(),
							generateStartTime,
							expectedDeliveryDate.getTime(),
							daysToBook,
							productStats)
					.merge(velocityEngine);
					TextWriter.write(
							temp, filePath);
					final Map<String, Object> extras = new HashMap<String, Object>();
					extras.put("fileName", fileName);
					result = new ResultBean(Boolean.TRUE, "Done");
					result.setExtras(extras);
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line("No purchase record within 90 days."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line("Last purchase order was genearted on " + DateFormatter.longFormat(company.getLastPurchaseOrderDate()) + ". You can generate another after a minimum of 3 days have passed."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("You must book for at least " + Html.text(Color.BLUE, "3 days.")));
		}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("Please select a company."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean generateInventory(Long companyId) {
		final ResultBean result;
		final Company company = companyService.find(companyId);
		// Determining last purchase order date
		if(company != null) {
			Calendar lastPODate = Calendar.getInstance();
			lastPODate.setTime(company.getLastPurchaseOrderDate());
		if(lastPODate.getTimeInMillis() != DateUtil.getDefaultDateInMillis()) {
			final List<PurchaseOrder> deliveredAfterCutoff = purchaseOrderService.findDeliveredAfterCutoffByCompanyOrderByDeliveryDate(company.getId());
			System.out.println("# of deliveries after " + DateFormatter.prettyFormat(DateUtil.getOrderCutoffDate()) + " : " + deliveredAfterCutoff.size());
			
			// Separating deliveries after last purchase order
			final List<PurchaseOrder> deliveriesAfterLastPO = new ArrayList<PurchaseOrder>();
			for(PurchaseOrder po : deliveredAfterCutoff) {
				Calendar deliveredOn = Calendar.getInstance();
				deliveredOn.setTime(po.getDeliveredOn());
				if(deliveredOn.get(Calendar.YEAR) >= lastPODate.get(Calendar.YEAR) &&
						deliveredOn.get(Calendar.DAY_OF_YEAR) >= lastPODate.get(Calendar.DAY_OF_YEAR)) {
					deliveriesAfterLastPO.add(po);
				}
			}
			System.out.println("# of deliveries after last PO : " + deliveriesAfterLastPO.size());
			
			// Mapping the net amount to the product id of all products included in "deliveriesAfterLastPO"
			final Map<Long, Float> lastPurchaseNetAmount = new HashMap<Long, Float>();
			for(PurchaseOrder po : deliveriesAfterLastPO) {
				System.out.println("Processing PO #" + po.getId());
				final List<PurchaseOrderDetail> poDetails = purchaseOrderDetailService.findAllByPurchaseOrderId(po.getId());
				for(PurchaseOrderDetail poDetail : poDetails) {
					System.out.println("Found : " + poDetail.getProductName() + " worth " + poDetail.getFormattedNetPrice());
					final Long productId = poDetail.getProductDetail().getProduct().getId();
					Float netPurchaseAmount = (lastPurchaseNetAmount.get(productId) == null) ? 0.0f : lastPurchaseNetAmount.get(productId);
					netPurchaseAmount += poDetail.getTotalPrice();
					lastPurchaseNetAmount.put(productId, netPurchaseAmount);
					System.out.println("Updating purchase net amount to : " + netPurchaseAmount);
				}
			}
			
			// Retrieve all products of the company
			final List<Product> products = productService.findAllByCompanyOrderByName(companyId);
			
			// Storage for printout data
			final List<InventoryBean> inventories = new ArrayList<InventoryBean>();
			
			for(Product product : products) {
				System.out.println("######## Processing product : " + product.getName() + " ########");
				
				// Check if any last purchase net amount
				final Float netPurchaseAmount = (lastPurchaseNetAmount.get(product.getId()) == null) ? 0.0f : lastPurchaseNetAmount.get(product.getId());
				
				// Retrieve all sales on or after last PO date
				final List<CustomerOrderDetail> customerOrderDetails = customerOrderDetailService.findAllByProductLimitByDate(product.getId(), lastPODate.getTime());
				Float netSalesAmount = 0.0f;
				for(CustomerOrderDetail coDetail : customerOrderDetails) {
					netSalesAmount += coDetail.getTotalPrice() / (1 + (coDetail.getMargin() / 100));
				}
				
				final Float stockBudget = product.getStockBudget() + netPurchaseAmount - netSalesAmount;
				System.out.println("Calculated stock budget : " + product.getStockBudget());
				
				// Storing all printout data
				final InventoryBean inventory = new InventoryBean();
				inventory.setProductId(product.getId());
				inventory.setProductName(product.getName());
				inventory.setProductDisplayName(product.getDisplayName());
				
				final ProductDetail wholeProductDetail = productDetailService.findByProductIdAndTitle(product.getId(), "Whole");
				final ProductDetail pieceProductDetail = productDetailService.findByProductIdAndTitle(product.getId(), "Piece");
				
				final Float wholePurchasePrice = wholeProductDetail != null ? wholeProductDetail.getNetPrice() : -1.0f;
				final Float piecePurchasePrice = pieceProductDetail != null ? pieceProductDetail.getNetPrice() : -1.0f;
				
				inventory.setWholeQuantity((int) Math.floor(stockBudget / wholePurchasePrice));
				inventory.setPieceQuantity((stockBudget % wholePurchasePrice) / piecePurchasePrice);
				
				inventory.setWholeUnit((wholeProductDetail.getUnitType() != null) ? wholeProductDetail.getUnitType() : UnitType.DEFAULT);
				inventory.setPieceUnit((pieceProductDetail.getUnitType() != null) ? pieceProductDetail.getUnitType() : UnitType.DEFAULT);
				
				inventories.add(inventory);
			}
			
			// Generate text file of inventory
			final String fileName = StringHelper.convertToFileSafeFormat(company.getName()) + "_" + DateFormatter.fileSafeFormat(new Date()) + ".txt";
			final String filePath = fileConstants.getInventoryHome() + fileName;
			final String temp = new InventoryTemplate(
					company.getName(),
					inventories)
			.merge(velocityEngine);
			TextWriter.write(
					temp, filePath);
			final Map<String, Object> extras = new HashMap<String, Object>();
			extras.put("fileName", fileName);
			result = new ResultBean(Boolean.TRUE, "Done");
			result.setExtras(extras);
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("Inventory data is only available after the first purchase order has been generated."));
		}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("Please select a company."));
		}
		
		return result;
	}

	@Override
	public ResultBean removePurchaseOrder(Long purchaseOrderId) {
		final ResultBean result;
		
		final PurchaseOrder purchaseOrder = purchaseOrderService.find(purchaseOrderId);
		if(purchaseOrder != null) {
			if(purchaseOrder.getStatus().equals(Status.LISTING)) {
				result = new ResultBean();
				
				purchaseOrder.setStatus(Status.CANCELLED);
				
				result.setSuccess(purchaseOrderService.delete(purchaseOrder));
				if(result.getSuccess()) {
					result.setMessage("Successfully removed Purchase order \"" + purchaseOrder.getId() + "(" + purchaseOrder.getCompany().getName() + ")\".");
				} else {
					result.setMessage("Failed to remove Purchase order \"" + purchaseOrder.getId() + " of " + purchaseOrder.getCompany().getName() + "\".");
				}
			} else {
				result = new ResultBean(Boolean.FALSE, "Purchase order cannot be removed right now.");
			}
		} else {
			result = new ResultBean(Boolean.FALSE, "Purchase order not found.");
		}
		
		return result;
	}
	
	private void setPurchaseOrder(PurchaseOrder purchaseOrder, PurchaseOrderFormBean purchaseOrderForm) {
		purchaseOrder.setCompany(companyService.find(purchaseOrderForm.getCompanyId()));
	}

	@Override
	public void refreshPurchaseOrder(Long purchaseOrderId) {
		this.refreshPurchaseOrder(purchaseOrderService.find(purchaseOrderId));
	}
	
	private void refreshPurchaseOrder(PurchaseOrder purchaseOrder) {
		float totalAmount = 0l;
		int totalItems = 0;
		
		List<PurchaseOrderDetail> purchaseOrderDetails = purchaseOrderDetailService.findAllByPurchaseOrderId(purchaseOrder.getId());
		
		for(PurchaseOrderDetail purchaseOrderDetail : purchaseOrderDetails) {
			totalAmount += purchaseOrderDetail.getTotalPrice();
			totalItems += purchaseOrderDetail.getQuantity();
		}
		
		purchaseOrder.setTotalAmount(totalAmount);
		purchaseOrder.setTotalItems(totalItems);
		purchaseOrderService.update(purchaseOrder);
	}
	
	@Override
	public ObjectList<PurchaseOrderDetail> getPurchaseOrderDetailList(Integer pageNumber, Long purchaseOrderId) {
		return purchaseOrderDetailService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), purchaseOrderId);
	}
	
	@Override
	public ResultBean addItemByProductDetailId(Long productDetailId, Long purchaseOrderId, Integer quantity) {
		final ResultBean result;
		
		final ProductDetail productDetail = productDetailService.find(productDetailId);
		final PurchaseOrder purchaseOrder = purchaseOrderService.find(purchaseOrderId);
		
		if(purchaseOrder != null) {
			if(purchaseOrder.getStatus().equals(Status.LISTING)) {
				if(productDetail != null) {
					result = this.addItem(productDetail, purchaseOrder, quantity);
				} else {
					result = new ResultBean(false, "Product not found.");
				}
			} else {
				result = new ResultBean(false, "Purchase order cannot be edited right now.");
			}
		} else {
			result = new ResultBean(false, "Purchase order not found.");
		}
		
		return result;
	}
	
	private ResultBean addItem(ProductDetail productDetail, PurchaseOrder purchaseOrder, Integer quantity) {
		final ResultBean result;
		
		final PurchaseOrderDetail purchaseOrderDetail = purchaseOrderDetailService.findByOrderAndDetailId(purchaseOrder.getId(), productDetail.getId());
		
		if(purchaseOrderDetail == null) {
			result = new ResultBean();
			
			final PurchaseOrderDetail newPurchaseOrderDetail = new PurchaseOrderDetail();
			setPurchaseOrderDetail(newPurchaseOrderDetail, purchaseOrder, productDetail);
			
			result.setSuccess(purchaseOrderDetailService.insert(newPurchaseOrderDetail) != null && 
					this.changePurchaseOrderDetailQuantity(newPurchaseOrderDetail, quantity).getSuccess());
			
			if(result.getSuccess()) {
				result.setMessage("Successfully added item.");
			} else {
				result.setMessage("Failed to add item.");
			}
		} else {
			result = this.changePurchaseOrderDetailQuantity(purchaseOrderDetail, purchaseOrderDetail.getQuantity() + quantity);
		}
		
		return result;
	}
	
	@Override
	public ResultBean removePurchaseOrderDetail(Long purchaseOrderDetailId) {
		final ResultBean result;
		final PurchaseOrderDetail purchaseOrderDetail = purchaseOrderDetailService.find(purchaseOrderDetailId);
		
		if(purchaseOrderDetail != null) {
			final PurchaseOrder purchaseOrder = purchaseOrderDetail.getPurchaseOrder();
			if(purchaseOrder != null) {
				if(purchaseOrder.getStatus().equals(Status.LISTING)) {
					result = this.removePurchaseOrderDetail(purchaseOrderDetail);
				} else {
					result = new ResultBean(false, "Purchase order cannot be edited right now.");
				}
			} else {
				result = new ResultBean(false, "Purchase order not found.");
			}
		} else {
			result = new ResultBean(false, "Item not found.");
		}
		
		return result;
	}
	
	private ResultBean removePurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail) {
		final ResultBean result;
		
		final PurchaseOrder purchaseOrder = purchaseOrderDetail.getPurchaseOrder();
		result = new ResultBean();
		
		purchaseOrder.setTotalAmount(purchaseOrder.getTotalAmount() - purchaseOrderDetail.getTotalPrice());
		purchaseOrder.setTotalItems(purchaseOrder.getTotalItems() - purchaseOrderDetail.getQuantity());
		result.setSuccess(purchaseOrderDetailService.erase(purchaseOrderDetail));
		if(result.getSuccess()) {
			purchaseOrderService.update(purchaseOrder);
			result.setMessage("Successfully removed item \"" + purchaseOrderDetail.getProductName() + " (" + purchaseOrderDetail.getUnitType() + ")\".");
		} else {
			result.setMessage("Failed to remove Purchase order \"" + purchaseOrderDetail.getProductName() + " (" + purchaseOrderDetail.getUnitType() + ")\".");
		}
		
		return result;
	}
	
	@Override
	public ResultBean changePurchaseOrderDetailQuantity(Long purchaseOrderDetailId, Integer quantity) {
		final ResultBean result;
		
		final PurchaseOrderDetail purchaseOrderDetail = purchaseOrderDetailService.find(purchaseOrderDetailId);
		
		if(purchaseOrderDetail != null) {
			final PurchaseOrder purchaseOrder = purchaseOrderDetail.getPurchaseOrder();
			if(purchaseOrder != null) {
				if(purchaseOrder.getStatus().equals(Status.LISTING)) {
					result = this.changePurchaseOrderDetailQuantity(purchaseOrderDetail, quantity);
				} else {
					result =  new ResultBean(false, "Purchase order cannot be edited right now.");
				}
			} else {
				result = new ResultBean(false, "Purchase order not found.");
			}
		} else {
			result = new ResultBean(false, "Purchase order detail not found.");
		}
		
		return result;
	}
	
	private ResultBean changePurchaseOrderDetailQuantity(PurchaseOrderDetail purchaseOrderDetail, Integer quantity) {
		final ResultBean result;
		
		if(quantity != null && quantity != 0) {
			// Limit maximum quantity to 999
			if(quantity > 999) {
				quantity = 999;
			}
			
			result = new ResultBean();
			
			final PurchaseOrder purchaseOrder = purchaseOrderDetail.getPurchaseOrder();
			
			purchaseOrder.setTotalAmount(purchaseOrder.getTotalAmount() - purchaseOrderDetail.getTotalPrice());
			purchaseOrder.setTotalItems(purchaseOrder.getTotalItems() - purchaseOrderDetail.getQuantity());
			
			setPurchaseOrderDetailQuantity(purchaseOrderDetail, quantity);
			result.setSuccess(purchaseOrderDetailService.update(purchaseOrderDetail));
			
			if(result.getSuccess()) {
				purchaseOrder.setTotalAmount(purchaseOrder.getTotalAmount() + purchaseOrderDetail.getTotalPrice());
				purchaseOrder.setTotalItems(purchaseOrder.getTotalItems() + purchaseOrderDetail.getQuantity());
				purchaseOrderService.update(purchaseOrder);
				
				result.setMessage("Quantity successfully updated.");
			} else {
				result.setMessage("Failed to update quantity.");
			}
		} else {
			result = this.removePurchaseOrderDetail(purchaseOrderDetail);
		}
		
		return result;
	}
	
	@Override
	public ResultBean checkPurchaseOrder(Long purchaseOrderId) {
		final ResultBean result;
		final PurchaseOrder purchaseOrder = purchaseOrderService.find(purchaseOrderId);
		
		if(purchaseOrder != null) {
			if(purchaseOrder.getStatus() != Status.CHECKED && purchaseOrder.getStatus() != Status.CANCELLED) {
				result = new ResultBean();
				
				purchaseOrder.setManagerInCharge(userService.find(UserContextHolder.getUser().getId()));
				purchaseOrder.setStatus(Status.CHECKED);
				purchaseOrder.setCheckedOn(new Date());
				
				result.setSuccess(purchaseOrderService.update(purchaseOrder));
				if(result.getSuccess()) {
					//#############################################################################################		add to stock!!!
					
					result.setMessage("Successfully checked Purchase order \"" + purchaseOrder.getId() + "\".");
				} else {
					result.setMessage("Failed to check Purchase order \"" + purchaseOrder.getId() + "\".");
				}
			} else {
				result = new ResultBean(false, "Purchase order already checked or cancelled.");
			}
		} else {
			result = new ResultBean(false, "Purchase order not found.");
		}
		
		return result;
	}
	
	private void setPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail, PurchaseOrder purchaseOrder, ProductDetail productDetail) {
		purchaseOrderDetail.setPurchaseOrder(purchaseOrder);
		purchaseOrderDetail.setProductDetail(productDetail);
		purchaseOrderDetail.setProductName(productDetail.getProduct().getName());
		purchaseOrderDetail.setUnitType(productDetail.getUnitType());
		purchaseOrderDetail.setGrossPrice(productDetail.getGrossPrice());
		purchaseOrderDetail.setNetPrice(productDetail.getNetPrice());
		purchaseOrderDetail.setQuantity(0);
		purchaseOrderDetail.setTotalPrice(0.0f);
	}
	
	private void setPurchaseOrderDetailQuantity(PurchaseOrderDetail purchaseOrderDetail, int quantity) {
		purchaseOrderDetail.setQuantity(quantity);
		purchaseOrderDetail.setTotalPrice(quantity * purchaseOrderDetail.getNetPrice());
	}
}