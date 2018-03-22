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
			return purchaseOrderService.findAllWithPaging(pageNumber,  UserContextHolder.getItemsPerPage(), companyId);
		} else {
			return this.getActivePurchaseOrderList(pageNumber, companyId);
		}
	}
	
	private ObjectList<PurchaseOrder> getActivePurchaseOrderList(Integer pageNumber, Long companyId) {
		return purchaseOrderService.findAllWithPagingAndStatus(pageNumber, UserContextHolder.getItemsPerPage(), companyId, new Status[] { Status.LISTING });
	}

	@Override
	public PurchaseOrder getPurchaseOrder(Long purchaseOrderId) {
		return purchaseOrderService.find(purchaseOrderId);
	}

	@Override
	public ResultBean createPurchaseOrder(PurchaseOrderFormBean purchaseOrderForm) {
		final ResultBean result;
		
		if(purchaseOrderForm.getCompanyId() != null) {
			final PurchaseOrder purchaseOrder = new PurchaseOrder();
			setPurchaseOrder(purchaseOrder, purchaseOrderForm);
			purchaseOrder.setTotalAmount(0.0f);
			purchaseOrder.setTotalItems(0);
			
			purchaseOrder.setCreator(userService.find(UserContextHolder.getUser().getUserId()));
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
	public ResultBean generatePurchaseOrder(Long companyId) {
		final ResultBean result;
		final Company company = companyService.find(companyId);
		
		if(company != null) {
			final Date generateStartTime = new Date();
			
			// GETTING PURCHASES
			final Map<Long, Float> lastPurchaseValues = new HashMap<Long, Float>();
			final List<PurchaseOrder> lastPurchases = new ArrayList<PurchaseOrder>();
			
			final List<PurchaseOrder> deliveredWithinNinety = purchaseOrderService.findDeliveredWithinNinetyDaysByCompanyOrderByDeliveryDate(companyId);
			if(!deliveredWithinNinety.isEmpty()) {
				// getting last purchase order date
				Calendar lastPO = Calendar.getInstance();
				lastPO.setTime(company.getLastPurchaseOrderDate());
				final Date lastPurchaseOrderDate = lastPO.getTimeInMillis() == DateUtil.getDefaultDateInMillis() ? lastPurchases.get(0).getDeliveredOn() : company.getLastPurchaseOrderDate();
				
				// separating last orders
				for(int i = 0; i < deliveredWithinNinety.size(); i++) {
					if(DateUtil.isSameDay(deliveredWithinNinety.get(i).getDeliveredOn(), deliveredWithinNinety.get(0).getDeliveredOn())){
						lastPurchases.add(deliveredWithinNinety.get(i));
					} else {
						break;
					}
				}
				
				// computing purchase values of last delivery, only if delivery is after or on the last PO
				if(!lastPurchases.get(0).getDeliveredOn().before(lastPO.getTime())) {
					for(PurchaseOrder lastOrder : lastPurchases) {
						final List<PurchaseOrderDetail> lastOrderItems = purchaseOrderDetailService.findAllByPurchaseOrderId(lastOrder.getId());
						for(PurchaseOrderDetail lastOrderItem : lastOrderItems) {
							Long lastOrderItemProductId = lastOrderItem.getProductDetail().getProduct().getId();
							Float lastOrderValue = lastPurchaseValues.get(lastOrderItemProductId);
							lastOrderValue = lastOrderItem.getTotalPrice() + (lastOrderValue == null ? 0 : lastOrderValue);
							lastPurchaseValues.put(lastOrderItemProductId, lastOrderValue);
						}
					}
				}
				
				// GETTING CURRENT DELIVERY RATE
				Float currentDeliveryRate = 0.0f;
				int count = 0;
				for(int i = 1; i < deliveredWithinNinety.size(); i++) {
					if(DateUtil.isSameDay(deliveredWithinNinety.get(i).getDeliveredOn(), deliveredWithinNinety.get(i - 1).getDeliveredOn())) {
						currentDeliveryRate += Days.daysBetween(new DateTime(deliveredWithinNinety.get(i).getDeliveredOn()), new DateTime(deliveredWithinNinety.get(i - 1).getDeliveredOn())).getDays();
						count++;
					}
				}
				
				currentDeliveryRate = (count == 0) ? 0 : currentDeliveryRate / count;
				
				// UPDATING BUDGET (TB & PB)
				for(Map.Entry<Long, Float> lastPurchase : lastPurchaseValues.entrySet()) {
					final Product product = productService.find(lastPurchase.getKey());
					if(product.getPurchaseBudget() < lastPurchase.getValue()) {
						product.setTotalBudget(product.getTotalBudget() + (lastPurchase.getValue() - product.getPurchaseBudget()));
						product.setPurchaseBudget(lastPurchase.getValue());
					}
				}
				
				// GETTING SALES, COMPUTING NEXT BUDGET, CREATING STATISTICS
				final List<ProductStatisticsBean> productStats = new ArrayList<ProductStatisticsBean>();
				
				final List<Product> products = productService.findAllByCompanyOrderByName(companyId);
				
				for(Product product : products) {
					final ProductStatisticsBean productStat = new ProductStatisticsBean();
					productStat.setProductId(product.getId());
					productStat.setProductName(product.getName());
					productStat.setPreviousSaleRate(product.getSaleRate());
					productStat.setPreviousTotalBudget(product.getTotalBudget());
					
					// computing sales since last Purchase Order
					final List<CustomerOrderDetail> customerOrderDetails = customerOrderDetailService.findAllByProductLimitByDate(product.getId(), lastPurchaseOrderDate);
					Float lastSaleValue = 0.0f;
					for(CustomerOrderDetail custOrder : customerOrderDetails) {
						lastSaleValue += custOrder.getTotalPrice();
					}
					
					// computing previous actual budget
					final Float lastPurchaseValue = (lastPurchaseValues.containsKey(product.getId()) ? lastPurchaseValues.get(product.getId()) : 0);
					final Float previousActualBudget = product.getStockBudget() + lastPurchaseValue;
					
					// computing current sale rate
					final Float currentSaleRate = (lastSaleValue > previousActualBudget) ? 100.0f : lastSaleValue / previousActualBudget * 100;
					
					// computing budget adjustment
					// 20/30 Maximum increase of 20% at 70-100% sales AND 30/70 Maximum decrease of 30% at 0-70% sales
					Float budgetAdjustmentRate = (currentSaleRate - 70.0f) * (currentSaleRate >= 70.0f ? 20 / 30 : 30 / 70) / 100;
					// double up if twice greater than equal 95% sale rate AND double down if twice less than equal 30% sale rate
					if((currentSaleRate >= 95.0f && product.getSaleRate() >= 95.0f) || (currentSaleRate <= 30 && product.getSaleRate() <= 30)) {
						budgetAdjustmentRate *= 2;
					}
					
					// SETTING NEW TOTAL BUDGET
					// computing ratio of previous delivery rate and current delivery rate
					final Float deliveryRateAdjustment = (company.getDeliveryRate().equals(0.0f)) ? 1 : currentDeliveryRate / company.getDeliveryRate();
					final Float newTotalBudget = previousActualBudget * (1 + budgetAdjustmentRate) * deliveryRateAdjustment;
					product.setTotalBudget(currentSaleRate >= 70 ? Math.max(newTotalBudget, product.getTotalBudget()) : Math.min(newTotalBudget, product.getTotalBudget()));
					productStat.setCurrentTotalBudget(product.getTotalBudget());
					
					// SETTING NEW PURCHASE BUDGET
					final Float newPurchaseBudget = product.getTotalBudget() - (previousActualBudget - lastSaleValue);
					product.setPurchaseBudget(newPurchaseBudget > 0.0f ? newPurchaseBudget : 0.0f);
					productStat.setCurrentPurchaseBudget(product.getPurchaseBudget());
					
					// SETTING NEW SALE RATE
					product.setSaleRate(currentSaleRate);
					productStat.setCurrentSaleRate(product.getSaleRate());
					
					// SAVE CHANGES ON PRODUCT
					productService.update(product);
					
					// ALLOCATE BUDGET
					final ProductDetail wholeProductDetail = productDetailService.findByProductIdAndTitle(product.getId(), "Whole");
					final ProductDetail pieceProductDetail = productDetailService.findByProductIdAndTitle(product.getId(), "Piece");
					
					final Float wholePurchasePrice = wholeProductDetail != null ? wholeProductDetail.getNetPrice() : -1.0f;
					final Float piecePurchasePrice = pieceProductDetail != null ? pieceProductDetail.getNetPrice() : -1.0f;
					
					Integer quantity = 0;
					UnitType unit;
					
					if(product.getPurchaseBudget() / wholePurchasePrice < 1.0f) {
						quantity = Math.round(product.getPurchaseBudget() / piecePurchasePrice);
						unit = pieceProductDetail.getUnitType();
					} else if(product.getPurchaseBudget() / wholePurchasePrice > 100.0f) {
						// if quantity is more than 100 wholes round to the nearest 10s
						quantity = Math.round(product.getPurchaseBudget() / piecePurchasePrice / 10) * 10;
						unit = wholeProductDetail.getUnitType();
					} else if(product.getPurchaseBudget() / wholePurchasePrice > 50.0f) {
						// if quantity is more than 50 wholes round to the nearest 5s
						quantity = Math.round(product.getPurchaseBudget() / piecePurchasePrice / 5) * 5;
						unit = wholeProductDetail.getUnitType();
					} else {
						quantity = Math.round(product.getPurchaseBudget() / wholePurchasePrice);
						unit = wholeProductDetail.getUnitType();
					}
					
					productStat.setQuantity(quantity);
					productStat.setUnit(unit);
					
					// include only those with budget
					if(!productStat.getFormattedCurrentTotalBudget().equals(0.0f) && !productStat.getPreviousTotalBudget().equals(0.0f)) {
						productStats.add(productStat);
					}
				}
				
				// computing expected delivery date
				Calendar expectedDeliveryDate = Calendar.getInstance();
				expectedDeliveryDate.add(Calendar.DAY_OF_MONTH, Math.round(currentDeliveryRate));
				
				// SAVE CHANGES ON COMPANY
				company.setDeliveryRate(currentDeliveryRate);
				company.setLastPurchaseOrderDate(generateStartTime);
				companyService.update(company);
				
				// GENERATE TEXT FILE OF GENERATED PURCHASE ORDER
				final String fileName = StringHelper.convertToFileSafeFormat(company.getName()) + "_" + DateFormatter.fileSafeFormat(new Date()) + ".txt";
				final String filePath = fileConstants.getGeneratePurchasesHome() + fileName;
				
				TextWriter.write(
						new GeneratedPurchaseTemplate(
								company.getName(),
								lastPurchaseOrderDate,
								generateStartTime,
								expectedDeliveryDate.getTime(),
								productStats)
						.merge(velocityEngine), filePath);
				
				final Map<String, Object> extras = new HashMap<String, Object>();
				extras.put("fileName", filePath);
				result = new ResultBean(Boolean.TRUE, "Done");
				result.setExtras(extras);
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line("No purchase record within 90 days."));
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
			if(purchaseOrder.getStatus() == Status.LISTING) {
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
			if(purchaseOrder.getStatus() == Status.LISTING) {
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
				if(purchaseOrder.getStatus() == Status.LISTING) {
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
				if(purchaseOrder.getStatus() == Status.LISTING) {
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
				
				purchaseOrder.setManagerInCharge(userService.find(UserContextHolder.getUser().getUserId()));
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