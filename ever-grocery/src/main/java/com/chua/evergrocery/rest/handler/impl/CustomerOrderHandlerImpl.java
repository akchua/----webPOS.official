package com.chua.evergrocery.rest.handler.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.SalesReportQueryBean;
import com.chua.evergrocery.beans.UserBean;
import com.chua.evergrocery.constants.FileConstants;
import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.database.entity.ProductDetail;
import com.chua.evergrocery.database.service.CustomerOrderDetailService;
import com.chua.evergrocery.database.service.CustomerOrderService;
import com.chua.evergrocery.database.service.ProductDetailService;
import com.chua.evergrocery.database.service.SystemVariableService;
import com.chua.evergrocery.database.service.UserService;
import com.chua.evergrocery.enums.AuditLogType;
import com.chua.evergrocery.enums.Color;
import com.chua.evergrocery.enums.DiscountType;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.enums.SystemVariableTag;
import com.chua.evergrocery.enums.TaxType;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.AuditLogHandler;
import com.chua.evergrocery.rest.handler.CustomerOrderHandler;
import com.chua.evergrocery.rest.handler.InventoryHandler;
import com.chua.evergrocery.rest.handler.ProductHandler;
import com.chua.evergrocery.rest.handler.SalesReportHandler;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.EmailUtil;
import com.chua.evergrocery.utility.Html;
import com.chua.evergrocery.utility.TaxUtil;
import com.chua.evergrocery.utility.TextWriter;
import com.chua.evergrocery.utility.format.CurrencyFormatter;
import com.chua.evergrocery.utility.print.Printer;
import com.chua.evergrocery.utility.template.CustomerOrderCopyTemplate;
import com.chua.evergrocery.utility.template.CustomerOrderReceiptTemplate;

@Transactional
@Component
public class CustomerOrderHandlerImpl implements CustomerOrderHandler {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerOrderService customerOrderService;
	
	@Autowired
	private CustomerOrderDetailService customerOrderDetailService;
	
	@Autowired
	private ProductDetailService productDetailService;
	
	@Autowired
	private SystemVariableService systemVariableService;
	
	@Autowired
	private ProductHandler productHandler;
	
	@Autowired
	private AuditLogHandler auditLogHandler;
	
	@Autowired
	private InventoryHandler inventoryHandler;
	
	@Autowired
	private SalesReportHandler salesReportHandler;
	
	@Autowired
	private FileConstants fileConstants;
	
	@Autowired
	private EmailUtil emailUtil;
	
	@Autowired
	private VelocityEngine velocityEngine;

	@Override
	public ObjectList<CustomerOrder> getCustomerOrderList(Integer pageNumber, String searchKey, Boolean showPaid, Integer daysAgo) {
		if(showPaid) {
			return customerOrderService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), searchKey, null, daysAgo);
		} else {
			return customerOrderService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), searchKey, new Status[] { Status.LISTING,  Status.SUBMITTED }, daysAgo);
		}
	}
	
	@Override
	public ObjectList<CustomerOrder> getCashierCustomerOrderList(Integer pageNumber, String searchKey) {
		return customerOrderService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), searchKey, new Status[] { Status.SUBMITTED, Status.DISCOUNTED}, null);
	}

	@Override
	public ObjectList<CustomerOrder> getListingCustomerOrderList(Integer pageNumber, String searchKey) {
		return customerOrderService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), searchKey, new Status[] { Status.LISTING}, null);
	}
	
	@Override
	public CustomerOrder getCustomerOrder(Long customerOrderId) {
		return customerOrderService.find(customerOrderId);
	}
	
	@Override
	public CustomerOrder getCustomerOrderBySIN(Long serialInvoiceNumber) {
		return (serialInvoiceNumber != null && serialInvoiceNumber > 0l) ? customerOrderService.findBySerialInvoiceNumber(serialInvoiceNumber) : null;
	}
	
	@Override
	public ResultBean createCustomerOrder() {
		final ResultBean result;
		
		final CustomerOrder customerOrder = new CustomerOrder();
		customerOrder.setPaidOn(DateUtil.getDefaultDate());
		customerOrder.setSerialInvoiceNumber(0l);
		customerOrder.setVatSales(0.0f);
		customerOrder.setVatExSales(0.0f);
		customerOrder.setZeroRatedSales(0.0f);
		customerOrder.setDiscountType(DiscountType.NO_DISCOUNT);
		customerOrder.setTotalDiscountAmount(0.0f);
		customerOrder.setTotalItems(0.0f);
		
		customerOrder.setCreator(userService.find(UserContextHolder.getUser().getId()));
		customerOrder.setStatus(Status.LISTING);
		
		result = new ResultBean();
		result.setSuccess(customerOrderService.insert(customerOrder) != null);
		if(result.getSuccess()) {
			Map<String, Object> extras = new HashMap<String, Object>();
			extras.put("customerOrderId", customerOrder.getId());
			result.setExtras(extras);
			
			result.setMessage("Customer order successfully created.");
		} else {
			result.setMessage("Failed to create customer order.");
		}
		
		return result;
	}
	
	@Override
	public ResultBean removeCustomerOrder(Long customerOrderId) {
		final ResultBean result;
		
		final CustomerOrder customerOrder = customerOrderService.find(customerOrderId);
		if(customerOrder != null) {
			final UserBean currentUser = UserContextHolder.getUser();
			
			if(!customerOrder.getStatus().equals(Status.PAID) && currentUser.getUserType().getAuthority() <= 3) {
				result = new ResultBean();
				
				customerOrder.setStatus(Status.CANCELLED);
				
				result.setSuccess(customerOrderService.delete(customerOrder));
				if(result.getSuccess()) {
					result.setMessage("Successfully removed Customer order \"" + customerOrder.getOrderNumber() + "\".");
				} else {
					result.setMessage("Failed to remove Customer order \"" + customerOrder.getOrderNumber() + "\".");
				}
			} else {
				result = new ResultBean(false, "Customer order cannot be removed right now.");
			}
		} else {
			result = new ResultBean(false, "Customer order not found.");
		}
		
		return result;
	}
	
	@Override
	public ResultBean applyDiscount(Long customerOrderId, DiscountType discountType, Float grossAmountLimit) {
		final ResultBean result;
		final CustomerOrder customerOrder = customerOrderService.find(customerOrderId);
		
		if(customerOrder != null) {
			if(customerOrder.getTotalDiscountAmount().equals(0.0f) && customerOrder.getDiscountType().equals(DiscountType.NO_DISCOUNT)) {
				if(discountType.equals(DiscountType.SENIOR_DISCOUNT)) {
					// Apply hard cap to gross amount
					grossAmountLimit = Math.min(grossAmountLimit, discountType.getGrossHardCap());
					
					final List<CustomerOrderDetail> customerOrderDetails = customerOrderDetailService.findAllByCustomerOrderId(customerOrderId);
					Float totalDiscounted = 0.0f;
					
					for(CustomerOrderDetail customerOrderDetail : customerOrderDetails) {
						if(customerOrderDetail.getProductDetail().getProduct().getAllowSeniorDiscount() && customerOrderDetail.getTotalPrice() <= grossAmountLimit) {
							grossAmountLimit -= customerOrderDetail.getTotalPrice();
							customerOrderDetail.setTotalPrice(TaxUtil.convertTaxType(customerOrderDetail.getTotalPrice(), customerOrderDetail.getTaxType(), TaxType.ZERO_RATED));
							customerOrderDetail.setTaxType(TaxType.ZERO_RATED);
							
							totalDiscounted += customerOrderDetail.getTotalPrice();
						}
					}
					
					customerOrderDetailService.batchUpdate(customerOrderDetails);
					customerOrder.setDiscountType(discountType);
					customerOrder.setStatus(Status.DISCOUNTED);
					customerOrder.setTotalDiscountAmount(totalDiscounted * (discountType.getPercentDiscount() / 100));
					
					result = new ResultBean();
					
					result.setSuccess(customerOrderService.update(customerOrder));
					if(result.getSuccess()) {
						result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " applied " + discountType.getDisplayName() + " to Customer Order #" + customerOrder.getOrderNumber() + "."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed!") + " please select a discount type."));
				}
				
				refreshCustomerOrder(customerOrder);
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed!") + "Customer Order already discounted."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed!") + "Customer Order no longer exists. Please reload the page."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean payCustomerOrder(Long customerOrderId, Float cash) {
		final ResultBean result;
		final CustomerOrder customerOrder = customerOrderService.find(customerOrderId);
		
		if(customerOrder != null) {
			if(customerOrder.getStatus().equals(Status.SUBMITTED) || customerOrder.getStatus().equals(Status.DISCOUNTED)) {
				if(customerOrder.getTotalAmount() <= cash) {
					Long SIN = Long.valueOf(systemVariableService.findByTag(SystemVariableTag.SERIAL_INVOICE_NUMBER.getTag()));
					
					result = new ResultBean();
					
					customerOrder.setCashier(userService.find(UserContextHolder.getUser().getId()));
					customerOrder.setStatus(Status.PAID);
					customerOrder.setPaidOn(new Date());
					customerOrder.setCash(cash);
					customerOrder.setSerialInvoiceNumber(SIN);
					
					result.setSuccess(customerOrderService.update(customerOrder) && systemVariableService.updateByTag(SystemVariableTag.SERIAL_INVOICE_NUMBER.getTag(), String.valueOf(SIN + 1)));
					if(result.getSuccess()) {
						// UPDATE INVENTORY if any item is sold at old price
						inventoryHandler.checkForStockAdjustment(customerOrderId);
						
						// UPDATE AUDIT LOG
						auditLogHandler.addLog(UserContextHolder.getUser().getId(), AuditLogType.SALES, customerOrder.getTotalAmount());
						
						result.setMessage(Html.rightLine(Html.boldText("CHANGE: Php " + CurrencyFormatter.pesoFormat(cash - customerOrder.getTotalAmount())) +
								Html.newLine + Html.newLine + Html.text("Cash          : " + CurrencyFormatter.pesoFormat(cash)) +
								Html.newLine + Html.text("Amount Due    : " + CurrencyFormatter.pesoFormat(customerOrder.getTotalAmount()))));
					} else {
						result.setMessage("Failed to pay Customer order \"" + customerOrder.getOrderNumber() + "\".");
					}
				} else {
					result = new ResultBean(false, "Insufficient cash.");
				}
			} else if(customerOrder.getStatus().equals(Status.PAID)) {
				result = new ResultBean(false, "Customer order already paid.");
			} else {
				result = new ResultBean(Boolean.FALSE, "Customer Order not yet completed.");
			}
		} else {
			result = new ResultBean(false, "Customer order not found.");
		}
		
		return result;
	}
	
	@Override
	public ObjectList<CustomerOrderDetail> getCustomerOrderDetailList(Integer pageNumber, Long customerOrderId) {
		return customerOrderDetailService.findAllWithPagingOrderByLastUpdate(pageNumber, UserContextHolder.getItemsPerPage(), customerOrderId);
	}
	
	@Override
	public ResultBean removeCustomerOrderDetail(Long customerOrderDetailId) {
		final ResultBean result;
		final CustomerOrderDetail customerOrderDetail = customerOrderDetailService.find(customerOrderDetailId);
		
		if(customerOrderDetail != null) {
			final CustomerOrder customerOrder = customerOrderDetail.getCustomerOrder();
			if(customerOrder != null) {
				if(customerOrder.getStatus().equals(Status.LISTING)) {
					result = this.removeCustomerOrderDetail(customerOrderDetail);
				} else {
					result = new ResultBean(false, "Customer order cannot be edited right now.");
				}
			} else {
				result = new ResultBean(false, "Customer order not found.");
			}
		} else {
			result = new ResultBean(false, "Item not found.");
		}
		
		return result;
	}
	
	private ResultBean removeCustomerOrderDetail(CustomerOrderDetail customerOrderDetail) {
		final ResultBean result;
		
		final CustomerOrder customerOrder = customerOrderDetail.getCustomerOrder();
		result = new ResultBean();
		
		addAmountToOrder(-customerOrderDetail.getTotalPrice(), customerOrderDetail.getTaxType(), customerOrder);
		customerOrder.setTotalItems(customerOrder.getTotalItems() - customerOrderDetail.getQuantity());
		result.setSuccess(customerOrderDetailService.erase(customerOrderDetail));
		if(result.getSuccess()) {
			customerOrderService.update(customerOrder);
			result.setMessage("Successfully removed item \"" + customerOrderDetail.getProductName() + " (" + customerOrderDetail.getUnitType() + ")\".");
		} else {
			result.setMessage("Failed to remove Customer order \"" + customerOrderDetail.getProductName() + " (" + customerOrderDetail.getUnitType() + ")\".");
		}
		
		return result;
	}

	@Override
	public ResultBean addItemByBarcode(String barcode, Long customerOrderId) {
		final ResultBean result;
		
		if(barcode != null && barcode.length() > 4) {
			final ProductDetail productDetail;
			String[] temp = barcode.split("\\*");
			if(temp.length == 2) {
				productDetail = productDetailService.findByBarcode(temp[1]);
			} else if(temp.length == 1) {
				productDetail = productDetailService.findByBarcode(temp[0]);
			} else {
				productDetail = null;
			}
			
			if(productDetail != null) {
				if(temp.length == 2) {
					result = this.addItemByProductDetailId(productDetail.getId(), customerOrderId, Float.parseFloat(temp[0]));
				} else if(temp.length == 1) {
					result = this.addItemByProductDetailId(productDetail.getId(), customerOrderId, 1.0f);
				} else {
					result = new ResultBean(false, "Invalid barcode.");
				}
			} else {
				result = new ResultBean(false, "Barcode not found.");
			}
		} else {
			result = new ResultBean(false, "Invalid barcode.");
		}
		
		return result;
	}
	
	@Override
	public ResultBean addItemByProductDetailId(Long productDetailId, Long customerOrderId, Float quantity) {
		final ResultBean result;
		
		final ProductDetail productDetail = productDetailService.find(productDetailId);
		final CustomerOrder customerOrder = customerOrderService.find(customerOrderId);
		
		if(customerOrder != null) {
			if(customerOrder.getStatus().equals(Status.LISTING) 
					|| (customerOrder.getStatus().equals(Status.SUBMITTED) && UserContextHolder.getUser().getUserType().getAuthority() <= 3)) {
				if(productDetail != null) {
					result = this.addItem(productDetail, customerOrder, quantity);
				} else {
					result = new ResultBean(false, "Product not found.");
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Declined!") + " You are not authorized to make changes on a submitted order."));
			}
		} else {
			result = new ResultBean(false, "Customer order not found.");
		}
		
		return result;
	}
	
	private ResultBean addItem(ProductDetail productDetail, CustomerOrder customerOrder, Float quantity) {
		final ResultBean result;
		
		if(!quantity.equals(0.0f)) {
			final CustomerOrderDetail customerOrderDetail = customerOrderDetailService.findByOrderAndDetailId(customerOrder.getId(), productDetail.getId());
			
			if(customerOrderDetail == null) {
				final CustomerOrderDetail newCustomerOrderDetail = new CustomerOrderDetail();
				setCustomerOrderDetail(newCustomerOrderDetail, customerOrder, productDetail);
				
				customerOrderDetailService.insert(newCustomerOrderDetail);
				
				result = this.changeCustomerOrderDetailQuantity(newCustomerOrderDetail, quantity);
			} else {
				result = this.changeCustomerOrderDetailQuantity(customerOrderDetail, customerOrderDetail.getQuantity() + quantity);
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("Tried to add 0 quantity."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean changeCustomerOrderDetailQuantity(Long customerOrderDetailId, Float quantity) {
		final ResultBean result;
		
		final CustomerOrderDetail customerOrderDetail = customerOrderDetailService.find(customerOrderDetailId);
		
		if(customerOrderDetail != null) {
			final CustomerOrder customerOrder = customerOrderDetail.getCustomerOrder();
			if(customerOrder != null) {
				if(customerOrder.getStatus().equals(Status.LISTING)
						|| (customerOrder.getStatus().equals(Status.SUBMITTED) && UserContextHolder.getUser().getUserType().getAuthority() <= 3)) {
					result = this.changeCustomerOrderDetailQuantity(customerOrderDetail, quantity);
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Declined!") + " You are not authorized to make changes on a submitted order."));
				}
			} else {
				result = new ResultBean(false, "Customer order not found.");
			}
		} else {
			result = new ResultBean(false, "Customer order detail not found.");
		}
		
		return result;
	}
	
	private ResultBean changeCustomerOrderDetailQuantity(CustomerOrderDetail customerOrderDetail, Float quantity) {
		final ResultBean result;
		
		quantity = resolveCustomerOrderDetailUnitType(customerOrderDetail, quantity);
		
		if(quantity > 0 || (quantity < 0 && UserContextHolder.getUser().getUserType().getAuthority() <= 2)) {
			final Float tempTotalItems = customerOrderDetail.getCustomerOrder().getTotalItems();
			if((quantity > 0 && tempTotalItems >= 0) || (quantity < 0 && tempTotalItems <= 0)) {
				// Limit maximum quantity to 999
				if(quantity > 999) {
					quantity = 999.0f;
				}
				
				result = new ResultBean();
				final CustomerOrder customerOrder = customerOrderDetail.getCustomerOrder();
				
				addAmountToOrder(-customerOrderDetail.getTotalPrice(), customerOrderDetail.getTaxType(), customerOrder);
				customerOrder.setTotalItems(customerOrder.getTotalItems() - customerOrderDetail.getQuantity());
				
				setCustomerOrderDetailQuantity(customerOrderDetail, quantity);
				result.setSuccess(customerOrderDetailService.update(customerOrderDetail));
				
				if(result.getSuccess()) {
					addAmountToOrder(customerOrderDetail.getTotalPrice(), customerOrderDetail.getTaxType(), customerOrder);
					customerOrder.setTotalItems(customerOrder.getTotalItems() + customerOrderDetail.getQuantity());
					customerOrderService.update(customerOrder);
					
					result.setMessage("Successfully updated quantity.");
				} else {
					result.setMessage("Failed to update quantity.");
				}
			} else {
				result = new ResultBean(Boolean.FALSE, "Returns must be on a separate transaction.");
			}
		} else {
			result = this.removeCustomerOrderDetail(customerOrderDetail);
		}
		
		return result;
	}
	
	private float resolveCustomerOrderDetailUnitType(CustomerOrderDetail customerOrderDetail, Float quantity) {
		final float result;
		final ProductDetail productDetail = customerOrderDetail.getProductDetail();
		
		if(quantity == null) quantity = 0.0f;
		
		// sets minimum possible quantity (1 or 0.5) (0.5 is not allowed for odd quantity)
		final ProductDetail upperProductDetail = productHandler.getUpperProductDetail(productDetail.getId());
		
		final float minQuantity;
		if(productDetail.getContent() != null && !productDetail.getContent().equals(0) && productDetail.getContent() % 2 == 0) {
			minQuantity = 0.5f;
		} else {
			minQuantity = 1.0f;
		}
		
		// trim off values not divisible by minimum quantity
		quantity -= (quantity % minQuantity);
		
		// get min quantity of upper product detail
		final Float upperMinQuantity = productDetail.getQuantity() % 2 == 0 ? 0.5f : 1.0f;
		
		if(upperProductDetail != null && quantity / productDetail.getQuantity() >= upperMinQuantity) {
			this.addItem(upperProductDetail, customerOrderDetail.getCustomerOrder(), quantity / productDetail.getQuantity());
			result = quantity % (productDetail.getQuantity() * upperMinQuantity);
		} else {
			result = quantity;
		}
			
		return result;
	}
	
	@Override
	public void refreshCustomerOrder(Long customerOrderId) {
		this.refreshCustomerOrder(customerOrderService.find(customerOrderId));
	}
	
	private void refreshCustomerOrder(CustomerOrder customerOrder) {
		float vatSales = 0;
		float vatExSales = 0;
		float zeroRatedSales = 0;
		float totalItems = 0;
		
		List<CustomerOrderDetail> customerOrderDetails = customerOrderDetailService.findAllByCustomerOrderId(customerOrder.getId());
		
		for(CustomerOrderDetail customerOrderDetail : customerOrderDetails) {
			switch(customerOrderDetail.getTaxType()) {
			case VAT:
				vatSales += customerOrderDetail.getTotalPrice();
				break;
			case VAT_EXEMPT:
				vatExSales += customerOrderDetail.getTotalPrice();
				break;
			case ZERO_RATED:
				zeroRatedSales += customerOrderDetail.getTotalPrice();
				break;
			}
			totalItems += customerOrderDetail.getQuantity();
		}
		
		customerOrder.setVatSales(vatSales);
		customerOrder.setVatExSales(vatExSales);
		customerOrder.setZeroRatedSales(zeroRatedSales);
		customerOrder.setTotalItems(totalItems);
		customerOrderService.update(customerOrder);
	}
	
	@Override
	public ResultBean submitCustomerOrder(Long customerOrderId) {
		final ResultBean result;
		final CustomerOrder customerOrder = customerOrderService.find(customerOrderId);
		
		if(customerOrder != null) {
			result = new ResultBean();
			
			customerOrder.setStatus(Status.SUBMITTED);
			result.setSuccess(customerOrderService.update(customerOrder));
			
			if(result.getSuccess()) {
				result.setMessage(Html.line(Color.GREEN, "Successfully") + " submitted customer order #" + customerOrder.getOrderNumber());
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load customer order. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean printCustomerOrderCopy(Long customerOrderId) {
		final ResultBean result;
		final CustomerOrder customerOrder = customerOrderService.find(customerOrderId);
		
		if(customerOrder != null) {
			final UserBean currentUser = UserContextHolder.getUser();
			
			if(customerOrder.getStatus().equals(Status.LISTING) || currentUser.getUserType().getAuthority() <= 3) {
				final List<CustomerOrderDetail> customerOrderItems = customerOrderDetailService.findAllByCustomerOrderIdOrderByProductName(customerOrder.getId());
				final CustomerOrderCopyTemplate customerOrderCopy = new CustomerOrderCopyTemplate(customerOrder, customerOrderItems);
				
				Printer printer = new Printer();
				try {
					printer.print(customerOrderCopy.merge(velocityEngine, DocType.PRINT), "Customer Order #" + customerOrder.getOrderNumber() + " (COPY)");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				result = new ResultBean(Boolean.TRUE, "Successfully printed Customer order \"" + customerOrder.getOrderNumber() + "\".");
			} else {
				result = new ResultBean(false, "You are not authorized to print a copy.");
			}
		} else {
			result = new ResultBean(false, "Customer order not found.");
		}
		
		return result;
	}
	
	@Override
	public ResultBean generateReport(SalesReportQueryBean salesReportQuery) {
		final ResultBean result = salesReportHandler.generateReport(salesReportQuery);
		if (result.getSuccess() && salesReportQuery.getSendMail()) {
			emailUtil.send(UserContextHolder.getUser().getEmailAddress(), 
					"Sales Report",
					"Sales Report for " + salesReportQuery.getFrom() + " - " + salesReportQuery.getTo() + ".",
					new String[] { fileConstants.getSalesHome() + (String) result.getExtras().get("fileName") });
		}
		return result;
	}
	
	@Override
	public void printReceipt(Long customerOrderId) {
		final CustomerOrder customerOrder = customerOrderService.find(customerOrderId);
		
		if(customerOrder != null) {
			final List<CustomerOrderDetail> customerOrderItems = customerOrderDetailService.findAllByCustomerOrderIdOrderByProductName(customerOrder.getId());
			final CustomerOrderReceiptTemplate customerOrderReceipt = new CustomerOrderReceiptTemplate(customerOrder, customerOrderItems);
			
			Printer printer = new Printer();
			try {
				final String receipt = customerOrderReceipt.merge(velocityEngine, DocType.PRINT);
				TextWriter.write(receipt, fileConstants.getReceiptHome() + customerOrder.getSerialInvoiceNumber() + ".txt");
				printer.print(receipt, "Customer Order #" + customerOrder.getOrderNumber() + " (ORIG)");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setCustomerOrderDetail(CustomerOrderDetail customerOrderDetail, CustomerOrder customerOrder, ProductDetail productDetail) {
		customerOrderDetail.setCustomerOrder(customerOrder);
		customerOrderDetail.setProductDetail(productDetail);
		customerOrderDetail.setProduct(productDetail.getProduct());
		customerOrderDetail.setProductName(productDetail.getProduct().getName());
		customerOrderDetail.setProductDisplayName((productDetail.getProduct().getDisplayName() != null && !productDetail.getProduct().getDisplayName().isEmpty()) ? productDetail.getProduct().getDisplayName() : productDetail.getProduct().getName());
		customerOrderDetail.setProductCode(productDetail.getProduct().getCode());
		customerOrderDetail.setUnitType(productDetail.getUnitType());
		customerOrderDetail.setContent(productDetail.getContent());
		customerOrderDetail.setContentUnit(productDetail.getContentUnit());
		customerOrderDetail.setUnitPrice(productDetail.getSellingPrice());
		customerOrderDetail.setQuantity(0.0f);
		customerOrderDetail.setTotalPrice(0.0f);
		customerOrderDetail.setMargin(productDetail.getActualPercentProfit());
		customerOrderDetail.setTaxType(productDetail.getProduct().getTaxType());
	}
	
	private void setCustomerOrderDetailQuantity(CustomerOrderDetail customerOrderDetail, float quantity) {
		customerOrderDetail.setQuantity(quantity);
		customerOrderDetail.setTotalPrice(quantity * customerOrderDetail.getUnitPrice());
	}
	
	private void addAmountToOrder(Float amount, TaxType taxType, CustomerOrder customerOrder) {
		switch(taxType) {
		case VAT:
			customerOrder.setVatSales(customerOrder.getVatSales() + amount);
			break;
		case VAT_EXEMPT:
			customerOrder.setVatExSales(customerOrder.getVatExSales() + amount);
			break;
		case ZERO_RATED:
			customerOrder.setZeroRatedSales(customerOrder.getZeroRatedSales() + amount);
			break;
		}
	}
}
