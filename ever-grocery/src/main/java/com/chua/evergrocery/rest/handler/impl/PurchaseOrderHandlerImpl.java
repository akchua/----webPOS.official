package com.chua.evergrocery.rest.handler.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.beans.PurchaseOrderFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.ProductDetail;
import com.chua.evergrocery.database.entity.PurchaseOrder;
import com.chua.evergrocery.database.entity.PurchaseOrderDetail;
import com.chua.evergrocery.database.service.CompanyService;
import com.chua.evergrocery.database.service.ProductDetailService;
import com.chua.evergrocery.database.service.PurchaseOrderDetailService;
import com.chua.evergrocery.database.service.PurchaseOrderService;
import com.chua.evergrocery.database.service.UserService;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.PurchaseOrderHandler;
import com.chua.evergrocery.utility.DateUtil;

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
	private ProductDetailService productDetailService;

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
			result = new ResultBean(false, "Select a company.");
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
				result = new ResultBean(false, "Purchase order cannot be removed right now.");
			}
		} else {
			result = new ResultBean(false, "Purchase order not found.");
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
