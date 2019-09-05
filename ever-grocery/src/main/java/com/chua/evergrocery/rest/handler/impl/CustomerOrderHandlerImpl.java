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
import com.chua.evergrocery.annotations.CheckAuthority;
import com.chua.evergrocery.beans.DiscountFormBean;
import com.chua.evergrocery.beans.PaymentsFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.SalesReportQueryBean;
import com.chua.evergrocery.beans.UserBean;
import com.chua.evergrocery.constants.FileConstants;
import com.chua.evergrocery.constants.PrintConstants;
import com.chua.evergrocery.database.entity.Customer;
import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.database.entity.ProductDetail;
import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.database.entity.XReading;
import com.chua.evergrocery.database.entity.ZReading;
import com.chua.evergrocery.database.service.CustomerOrderDetailService;
import com.chua.evergrocery.database.service.CustomerOrderService;
import com.chua.evergrocery.database.service.CustomerService;
import com.chua.evergrocery.database.service.ProductDetailService;
import com.chua.evergrocery.database.service.SystemVariableService;
import com.chua.evergrocery.database.service.UserService;
import com.chua.evergrocery.database.service.ZReadingService;
import com.chua.evergrocery.enums.AuditLogType;
import com.chua.evergrocery.enums.Color;
import com.chua.evergrocery.enums.DiscountType;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.enums.SystemVariableTag;
import com.chua.evergrocery.enums.TaxType;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.ActivityLogHandler;
import com.chua.evergrocery.rest.handler.AuditLogHandler;
import com.chua.evergrocery.rest.handler.CustomerOrderHandler;
import com.chua.evergrocery.rest.handler.InventoryHandler;
import com.chua.evergrocery.rest.handler.ProductHandler;
import com.chua.evergrocery.rest.handler.SalesReportHandler;
import com.chua.evergrocery.rest.validator.DiscountFormValidator;
import com.chua.evergrocery.rest.validator.PaymentsFormValidator;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.EmailUtil;
import com.chua.evergrocery.utility.Html;
import com.chua.evergrocery.utility.TaxUtil;
import com.chua.evergrocery.utility.TextWriter;
import com.chua.evergrocery.utility.format.CurrencyFormatter;
import com.chua.evergrocery.utility.format.DateFormatter;
import com.chua.evergrocery.utility.print.Printer;
import com.chua.evergrocery.utility.template.CustomerOrderCopyTemplate;
import com.chua.evergrocery.utility.template.CustomerOrderReceiptTemplate;
import com.chua.evergrocery.utility.template.XReadingTemplate;
import com.chua.evergrocery.utility.template.ZReadingTemplate;

@Transactional
@Component
public class CustomerOrderHandlerImpl implements CustomerOrderHandler {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerOrderService customerOrderService;
	
	@Autowired
	private CustomerOrderDetailService customerOrderDetailService;
	
	@Autowired
	private ProductDetailService productDetailService;
	
	@Autowired
	private SystemVariableService systemVariableService;
	
	@Autowired
	private ZReadingService zReadingService;
	
	@Autowired
	private ProductHandler productHandler;
	
	@Autowired
	private AuditLogHandler auditLogHandler;
	
	@Autowired
	private InventoryHandler inventoryHandler;
	
	@Autowired
	private SalesReportHandler salesReportHandler;
	
	@Autowired
	private ActivityLogHandler activityLogHandler;
	
	@Autowired
	private DiscountFormValidator discountFormValidator;
	
	@Autowired
	private PaymentsFormValidator paymentsFormValidator;
	
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
	@CheckAuthority(minimumAuthority = 10)
	public ResultBean createCustomerOrder(String ip) {
		final ResultBean result;
		
		final CustomerOrder customerOrder = new CustomerOrder();
		customerOrder.setPaidOn(DateUtil.getDefaultDate());
		customerOrder.setSerialInvoiceNumber(0l);
		customerOrder.setReferenceSerialInvoiceNumber(0l);
		customerOrder.setRefundNumber(0l);
		customerOrder.setVatSales(0.0f);
		customerOrder.setVatExSales(0.0f);
		customerOrder.setZeroRatedSales(0.0f);
		customerOrder.setDiscountType(DiscountType.NO_DISCOUNT);
		customerOrder.setVatDiscount(0.0f);
		customerOrder.setVatExDiscount(0.0f);
		customerOrder.setZeroRatedDiscount(0.0f);
		customerOrder.setDiscountIdNumber("");
		customerOrder.setTotalItems(0.0f);
		customerOrder.setCash(0.0f);
		customerOrder.setCheckAmount(0.0f);
		customerOrder.setCardAmount(0.0f);
		customerOrder.setPointsAmount(0.0f);
		customerOrder.setPointsEarned(0.0f);
		customerOrder.setTaxAdjustment(0.0f);
		
		customerOrder.setCreator(userService.find(UserContextHolder.getUser().getId()));
		customerOrder.setStatus(Status.LISTING);
		
		result = new ResultBean();
		result.setSuccess(customerOrderService.insert(customerOrder) != null);
		if(result.getSuccess()) {
			Map<String, Object> extras = new HashMap<String, Object>();
			extras.put("customerOrderId", customerOrder.getId());
			result.setExtras(extras);
			
			result.setMessage("Customer order successfully created.");
			activityLogHandler.myLog("created a sales order : " + customerOrder.getId(), ip);
		} else {
			result.setMessage("Failed to create customer order.");
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 10)
	public ResultBean removeCustomerOrder(Long customerOrderId, String ip) {
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
					activityLogHandler.myLog("removed a sales order : " + customerOrder.getId(), ip);
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
	@CheckAuthority(minimumAuthority = 5)
	public ResultBean setCustomer(Long customerOrderId, String customerCardId, String ip) {
		final ResultBean result;
		final CustomerOrder customerOrder = customerOrderService.find(customerOrderId);
		
		if(customerOrder != null) {
			final Customer customer = customerService.findByCardId(customerCardId);
			
			if(customer != null) {
				result = new ResultBean();
				
				customerOrder.setCustomer(customer);
				result.setSuccess(customerOrderService.update(customerOrder));
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " set Mr./Mrs. " + customer.getFormattedName() + " as the customer for this transaction."));
					activityLogHandler.myLog("set customer on a sales order : " + customer.getId() + " - " + customer.getFormattedName() + " to " + customerOrder.getId(), ip);
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Invalid ") + " customer card."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load sales order. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ResultBean removeCustomer(Long customerOrderId, String ip) {
		final ResultBean result;
		final CustomerOrder customerOrder = customerOrderService.find(customerOrderId);
		
		if(customerOrder != null) {
			result = new ResultBean();
			final Customer customer = customerOrder.getCustomer();
			
			customerOrder.setCustomer(null);
			result.setSuccess(customerOrderService.update(customerOrder));
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed the customer for this transaction."));
				activityLogHandler.myLog("removed customer from a sales order : " + customer.getId() + " - " + customer.getFormattedName() + " to " + customerOrder.getId(), ip);
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load sales order. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ResultBean applyDiscount(DiscountFormBean discountForm, String ip) {
		final ResultBean result;
		final Map<String, String> errors = discountFormValidator.validate(discountForm);
		
		if(errors.isEmpty()) {
			final CustomerOrder customerOrder = customerOrderService.find(discountForm.getCustomerOrderId());
			
			if(customerOrder != null) {
				if(customerOrder.getTotalDiscountAmount().equals(0.0f) && customerOrder.getDiscountType().equals(DiscountType.NO_DISCOUNT)) {
					final DiscountType discountType = discountForm.getDiscountType();
					if(discountType.equals(DiscountType.SENIOR_DISCOUNT) || discountType.equals(DiscountType.PWD_DISCOUNT) || 
							(discountType.equals(DiscountType.EMPLOYEE_DISCOUNT) && UserContextHolder.getUser().getUserType().getAuthority() <= 3)) {
						// Apply hard cap to gross amount
						Float grossAmountLimit = Math.min(discountForm.getGrossAmountLimit(), discountType.getGrossHardCap());
						
						final List<CustomerOrderDetail> customerOrderDetails = customerOrderDetailService.findAllByCustomerOrderId(customerOrder.getId());
						Float totalDiscountableWithVatAmount = 0.0f;
						Float totalDiscountableVatExemptAmount = 0.0f;
						Float totalDiscountableZeroRatedAmount = 0.0f;
						
						for(CustomerOrderDetail customerOrderDetail : customerOrderDetails) {
							if((discountType.equals(DiscountType.EMPLOYEE_DISCOUNT) || 
									(discountType.equals(DiscountType.SENIOR_DISCOUNT) && customerOrderDetail.getProductDetail().getProduct().getAllowSeniorDiscount()) ||
									(discountType.equals(DiscountType.PWD_DISCOUNT) && customerOrderDetail.getProductDetail().getProduct().getAllowPWDDiscount())) && 
									customerOrderDetail.getTotalPrice() <= grossAmountLimit) {
								grossAmountLimit -= customerOrderDetail.getTotalPrice();
								/*customerOrderDetail.setTotalPrice(TaxUtil.convertTaxType(customerOrderDetail.getTotalPrice(), customerOrderDetail.getTaxType(), TaxType.VAT_EXEMPT));
								customerOrderDetail.setTaxType(TaxType.VAT_EXEMPT);*/
								
								switch(customerOrderDetail.getTaxType()) {
									case VAT:
										totalDiscountableWithVatAmount += customerOrderDetail.getTotalPrice();
										break;
									case VAT_EXEMPT:
										totalDiscountableVatExemptAmount += customerOrderDetail.getTotalPrice();
										break;
									case ZERO_RATED:
										totalDiscountableZeroRatedAmount += customerOrderDetail.getTotalPrice();
										break;
								}
							}
						}
						
						customerOrder.setVatDiscount(totalDiscountableWithVatAmount * (discountType.getPercentDiscount() / 100.0f));
						customerOrder.setVatExDiscount(totalDiscountableVatExemptAmount * (discountType.getPercentDiscount() / 100.0f));
						customerOrder.setZeroRatedDiscount(totalDiscountableZeroRatedAmount * (discountType.getPercentDiscount() / 100.0f));
						
						customerOrder.setDiscountType(discountType);
						setDiscount(customerOrder, discountForm);
						customerOrder.setStatus(Status.DISCOUNTED);
						
						result = new ResultBean();
						
						result.setSuccess(customerOrderService.update(customerOrder));
						if(result.getSuccess()) {
							result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " applied " + discountType.getDisplayName() + " to Customer Order #" + customerOrder.getOrderNumber() + "."));
						} else {
							result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
						}
					} else if(discountType.equals(DiscountType.ZERO_RATED)) {
						Float grossAmountLimit = Math.min(discountForm.getGrossAmountLimit(), discountType.getGrossHardCap());
						
						final List<CustomerOrderDetail> customerOrderDetails = customerOrderDetailService.findAllByCustomerOrderId(customerOrder.getId());
						
						for(CustomerOrderDetail customerOrderDetail : customerOrderDetails) {
							if(customerOrderDetail.getTotalPrice() <= grossAmountLimit) {
								grossAmountLimit -= customerOrderDetail.getTotalPrice();
								final Float oldTotalPrice = customerOrderDetail.getTotalPrice();
								
								customerOrderDetail.setTotalPrice(TaxUtil.convertTaxType(customerOrderDetail.getTotalPrice(), customerOrderDetail.getTaxType(), TaxType.ZERO_RATED));
								customerOrderDetail.setTaxType(TaxType.ZERO_RATED);
								
								customerOrderDetail.setTaxAdjustment(customerOrderDetail.getTotalPrice() - oldTotalPrice);
								customerOrder.setTaxAdjustment(customerOrder.getTaxAdjustment() + customerOrderDetail.getTaxAdjustment());
							}
						}
						
						customerOrder.setDiscountType(discountType);
						setDiscount(customerOrder, discountForm);
						customerOrder.setStatus(Status.DISCOUNTED);
						
						result = new ResultBean();
						
						result.setSuccess(customerOrderService.update(customerOrder));
						if(result.getSuccess()) {
							this.refreshCustomerOrder(customerOrder);
							result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " applied " + discountType.getDisplayName() + " to Customer Order #" + customerOrder.getOrderNumber() + "."));
						} else {
							result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
						}
					} else if(discountType.equals(DiscountType.PWD_MEDICINE_DISCOUNT) || discountType.equals(DiscountType.SENIOR_MEDICINE_DISCOUNT)) {
						Float grossAmountLimit = Math.min(discountForm.getGrossAmountLimit(), discountType.getGrossHardCap());
						
						final List<CustomerOrderDetail> customerOrderDetails = customerOrderDetailService.findAllByCustomerOrderId(customerOrder.getId());
						float discountableAmount = 0.0f;
						
						for(CustomerOrderDetail customerOrderDetail : customerOrderDetails) {
							if(customerOrderDetail.getTotalPrice() <= grossAmountLimit && customerOrderDetail.getProduct().getCategory().getName().equals("Medicine")) {
								grossAmountLimit -= customerOrderDetail.getTotalPrice();
								final Float oldTotalPrice = customerOrderDetail.getTotalPrice();
								
								customerOrderDetail.setTotalPrice(TaxUtil.convertTaxType(customerOrderDetail.getTotalPrice(), customerOrderDetail.getTaxType(), TaxType.VAT_EXEMPT));
								customerOrderDetail.setTaxType(TaxType.VAT_EXEMPT);
								discountableAmount += customerOrderDetail.getTotalPrice();
								
								customerOrderDetail.setTaxAdjustment(customerOrderDetail.getTotalPrice() - oldTotalPrice);
								customerOrder.setTaxAdjustment(customerOrder.getTaxAdjustment() + customerOrderDetail.getTaxAdjustment());
							}
						}
						
						customerOrder.setVatExDiscount(discountableAmount * (discountType.getPercentDiscount() / 100.0f));
						
						customerOrder.setDiscountType(discountType);
						setDiscount(customerOrder, discountForm);
						customerOrder.setStatus(Status.DISCOUNTED);
						
						result = new ResultBean();
						
						result.setSuccess(customerOrderService.update(customerOrder));
						if(result.getSuccess()) {
							this.refreshCustomerOrder(customerOrder);
							result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " applied " + discountType.getDisplayName() + " to Customer Order #" + customerOrder.getOrderNumber() + "."));
							activityLogHandler.myLog("applied discount on sales order : " + customerOrder.getId(), ip);
						} else {
							result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
						}
					} else {
						result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Declined!") + " you are not authorized to give " + discountType.getDisplayName() + "."));
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed!") + "Customer Order already discounted."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed!") + "Customer Order no longer exists. Please reload the page."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, "");
			result.addToExtras("errors", errors);
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ResultBean payCustomerOrder(PaymentsFormBean paymentsForm, String ip) {
		final ResultBean result;
		final Map<String, String> errors = paymentsFormValidator.validate(paymentsForm);
		
		if(errors.isEmpty()) {
			final CustomerOrder customerOrder = customerOrderService.find(paymentsForm.getCustomerOrderId());
			
			if(customerOrder != null) {
				//if(DateUtil.isToday(zReadingService.getLatestZReading().getReadingDate())) {
					//result = new ResultBean(false, "Cannot transact after end of day");
				//} else {
					if(customerOrder.getStatus().equals(Status.SUBMITTED) || customerOrder.getStatus().equals(Status.DISCOUNTED)) {
						if(customerOrder.getTotalAmount() > 0 &&
								(paymentsForm.getCardAmount() + paymentsForm.getPointsAmount() > customerOrder.getTotalAmount() + 0.01f)) {
							errors.put("cardAmount", "Card + Points amount must be less than the total payable.");
							errors.put("pointsAmount", "Card + Points amount must be less than the total payable");
						}
						
						if(customerOrder.getCustomer() != null && paymentsForm.getPointsAmount() > customerOrder.getCustomer().getAvailablePoints()) {
							errors.put("pointsAmount", "Insufficient points");
						}
						
						if(errors.isEmpty()) {
							if(customerOrder.getTotalAmount() <= paymentsForm.getTotalPayment()) {
								result = new ResultBean();
								if(customerOrder.getTotalAmount() < 0) {
									if(paymentsForm.getRefSIN() != null && paymentsForm.getRefSIN()	> 0l) {
										final CustomerOrder referenceCustomerOrder = customerOrderService.findBySerialInvoiceNumber(paymentsForm.getRefSIN());
										if(referenceCustomerOrder != null && referenceCustomerOrder.getTotalAmount() > 0) {
											Long RN = Long.valueOf(systemVariableService.findByTag(SystemVariableTag.REFUND_NUMBER.getTag()));
											customerOrder.setRefundNumber(RN);
											result.setSuccess(systemVariableService.updateByTag(SystemVariableTag.REFUND_NUMBER.getTag(), String.valueOf(RN + 1)));
											
											customerOrder.setReferenceSerialInvoiceNumber(paymentsForm.getRefSIN());
										} else {
											result.setSuccess(Boolean.FALSE);
											result.setMessage("Invalid reference invoice number : " + paymentsForm.getRefSIN());
										}
									} else {
										result.setSuccess(Boolean.FALSE);
										result.setMessage("Refund requires reference invoice number");
									}
								} else {
									Long SIN = Long.valueOf(systemVariableService.findByTag(SystemVariableTag.SERIAL_INVOICE_NUMBER.getTag()));
									customerOrder.setSerialInvoiceNumber(SIN);
									result.setSuccess(systemVariableService.updateByTag(SystemVariableTag.SERIAL_INVOICE_NUMBER.getTag(), String.valueOf(SIN + 1)));
								}
								
								if(result.getSuccess()) {
									customerOrder.setCashier(userService.find(UserContextHolder.getUser().getId()));
									customerOrder.setStatus(Status.PAID);
									customerOrder.setPaidOn(new Date());
									setCustomerOrderPayment(customerOrder, paymentsForm);
									
									if(customerOrder.getCustomer() != null) {
										setEarnedPoints(customerOrder);
										final Customer customer = customerOrder.getCustomer();
										customer.setTotalPoints(customer.getTotalPoints() + customerOrder.getPointsEarned());
										
										// add used points if any
										customer.setUsedPoints(customer.getUsedPoints() + customerOrder.getPointsAmount());
										customerService.update(customer);
									}
									
									result.setSuccess(customerOrderService.update(customerOrder));
									if(result.getSuccess()) {
										// UPDATE INVENTORY if any item is sold at old price
										inventoryHandler.checkForStockAdjustment(paymentsForm.getCustomerOrderId());
										
										// UPDATE AUDIT LOG
										auditLogHandler.addLog(UserContextHolder.getUser().getId(), AuditLogType.SALES, customerOrder.getTotalAmount());
										
										result.setMessage(Html.rightLine(Html.boldText("CHANGE: Php " + CurrencyFormatter.pesoFormat(customerOrder.getTotalPayment() - customerOrder.getTotalAmount())) +
												Html.newLine + Html.newLine + Html.text("Cash          : " + customerOrder.getFormattedCash()) +
												(customerOrder.getCheckAmount().equals(0.0f) ? "" : Html.newLine + Html.text("Check         : " + customerOrder.getFormattedCheckAmount())) + 
												Html.newLine + Html.text("Amount Due    : " + customerOrder.getFormattedTotalAmount())));
										activityLogHandler.myLog("paid sales order : " + customerOrder.getId() + " with amount due : Php" + customerOrder.getFormattedTotalAmount() + ", received total payment of : Php" + CurrencyFormatter.pesoFormat(paymentsForm.getTotalPayment()), ip);
									} else {
										result.setMessage("Failed to pay Customer order \"" + customerOrder.getOrderNumber() + "\".");
									}
								}
							} else {
								result = new ResultBean(false, "Insufficient cash.");
							}
						} else {
							result = new ResultBean(Boolean.FALSE, "");
							result.addToExtras("errors", errors);
						}
					} else if(customerOrder.getStatus().equals(Status.PAID)) {
						result = new ResultBean(false, "Customer order already paid.");
					} else {
						result = new ResultBean(Boolean.FALSE, "Customer Order not yet completed.");
					}
				//}
			} else {
				result = new ResultBean(false, "Customer order not found.");
			}
		} else {
			result = new ResultBean(Boolean.FALSE, "");
			result.addToExtras("errors", errors);
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
				if(customerOrder.getStatus().equals(Status.LISTING) || 
						(customerOrder.getStatus().equals(Status.SUBMITTED) && UserContextHolder.getUser().getUserType().getAuthority() <= 3)) {
					result = this.removeCustomerOrderDetail(customerOrderDetail);
				} else {
					result = new ResultBean(false, "You do not have permission to deleted this order.");
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
			String[] temp = barcode.split("\\*|;");
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
					if(quantity != null) {
						result = this.addItem(productDetail, customerOrder, quantity);
					} else {
						result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Invalid Quantity.")));
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line("Product not found."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Declined!") + " You are not authorized to make changes on a submitted order."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("Customer Order not found."));
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
	public ResultBean submitCustomerOrder(Long customerOrderId, String ip) {
		final ResultBean result;
		final CustomerOrder customerOrder = customerOrderService.find(customerOrderId);
		
		if(customerOrder != null) {
			if(customerOrder.getStatus().equals(Status.LISTING)) {
				if(customerOrder.getTotalAmount().equals(0.0f)) {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Empty Order!")));
				} else {
					result = new ResultBean();
					
					customerOrder.setStatus(Status.SUBMITTED);
					result.setSuccess(customerOrderService.update(customerOrder));
					
					if(result.getSuccess()) {
						// TEMPORARY
						PaymentsFormBean paymentsForm = new PaymentsFormBean();
						paymentsForm.setCustomerOrderId(customerOrderId);
						paymentsForm.setCash(customerOrder.getTotalAmount());
						paymentsForm.setCardAmount(0.0f);
						paymentsForm.setCheckAmount(0.0f);
						paymentsForm.setPointsAmount(0.0f);
						this.payCustomerOrder(paymentsForm, "192.168.0.2");
						//
						result.setMessage(Html.line(Color.GREEN, "Successfully") + " forwarded customer order #" + customerOrder.getOrderNumber() + " to cashier.");
						activityLogHandler.myLog("submitted sales order : " + customerOrder.getId(), ip);
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Error!") + " Please refresh the page."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load customer order. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean returnCustomerOrder(Long customerOrderId, String ip) {
		final ResultBean result;
		final CustomerOrder customerOrder = customerOrderService.find(customerOrderId);
		
		if(customerOrder != null) {
			if(customerOrder.getStatus().equals(Status.SUBMITTED)) {
				result = new ResultBean();
				
				customerOrder.setStatus(Status.LISTING);
				result.setSuccess(customerOrderService.update(customerOrder));
				
				if(result.getSuccess()) {
					result.setMessage(Html.line(Color.GREEN, "Successfully") + " returned customer order #" + customerOrder.getOrderNumber() + " to server " + customerOrder.getCreator().getFormattedName() + ".");
					activityLogHandler.myLog("returned sales order : " + customerOrder.getId(), ip);
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Error!") + " Please refresh the page."));
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
					printer.print(customerOrderCopy.merge(velocityEngine, DocType.PRINT), "Customer Order #" + customerOrder.getOrderNumber() + " (COPY)", PrintConstants.EVER_CASHIER_PRINTER);
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
	public ResultBean generateBackendReport(Date dateFrom, Date dateTo, String ip) {
		return salesReportHandler.generateBackendReport(dateFrom, dateTo, ip);
	}
	
	@Override
	public void printReceipt(Long customerOrderId, String footer, Boolean original, String ip) {
		final CustomerOrder customerOrder = customerOrderService.find(customerOrderId);
		
		if(customerOrder != null) {
			final List<CustomerOrderDetail> customerOrderItems = customerOrderDetailService.findAllByCustomerOrderIdOrderByProductName(customerOrder.getId());
			final CustomerOrderReceiptTemplate customerOrderReceipt = new CustomerOrderReceiptTemplate(customerOrder, customerOrderItems, customerOrder.getTotalAmount() < 0 ? "REFUND" : "Sales Invoice", original ? "" : "REPRINT", footer);
			
			Printer printer = new Printer();
			
			try {
				final String receipt = customerOrderReceipt.merge(velocityEngine, DocType.PRINT);
				TextWriter.write(receipt, fileConstants.getJournalFile(), Boolean.TRUE);
				printer.print(receipt, "Customer Order #" + customerOrder.getOrderNumber() + (original ? " (ORIG)" : " (COPY)"), PrintConstants.EVER_CASHIER_PRINTER);
				if(original) activityLogHandler.myLog("printed a receipt for sales order : " + customerOrder.getId(), ip);
				else activityLogHandler.myLog("printed a receipt copy for sales order : " + customerOrder.getId(), ip);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public ResultBean printZReading(Date readingDate, String ip) {
		final ResultBean result;
		
		salesReportHandler.updateZReading();
		
		final ZReading zReading = zReadingService.findByReadingDate(readingDate);
		
		if(zReading != null) {
			result = new ResultBean();
			final ZReadingTemplate zReadingTemplate = new ZReadingTemplate(zReading);
			
			Printer printer = new Printer();
			try {
				
				final String zReadingString = zReadingTemplate.merge(velocityEngine, DocType.PRINT);
				TextWriter.write(zReadingString, fileConstants.getJournalFile(), Boolean.TRUE);
				printer.print(zReadingString, "Z Reading " + DateFormatter.prettyFormat(readingDate), PrintConstants.EVER_ACCOUNTING_PRINTER);
				result.setSuccess(Boolean.TRUE);
				result.setMessage("");
				activityLogHandler.myLog("printed z-reading", ip);
			} catch (Exception e) {
				e.printStackTrace();
				result.setSuccess(Boolean.FALSE);
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed!") + " Invalid reading date : " + DateFormatter.prettyFormat(readingDate)));
		}
		
		return result;
	}
	
	@Override
	public void endOfShift(String ip) {
		final User cashier = UserContextHolder.getUser().getUserEntity();
		final XReading xReading = salesReportHandler.getXReadingByCashier(cashier);
		
		final XReadingTemplate xReadingTemplate = new XReadingTemplate(xReading);
		
		Printer printer = new Printer();
		try {
			
			final String xReadingString = xReadingTemplate.merge(velocityEngine, DocType.PRINT);
			TextWriter.write(xReadingString, fileConstants.getJournalFile(), Boolean.TRUE);
			printer.print(xReadingString, "X Reading " + cashier.getShortName(), PrintConstants.EVER_CASHIER_PRINTER);
			activityLogHandler.myLog("end of shift", ip);
		} catch (Exception e) {
			e.printStackTrace();
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
		customerOrderDetail.setTaxAdjustment(0.0f);
		customerOrderDetail.setOrigTaxType(productDetail.getProduct().getTaxType());
	}
	
	private void setCustomerOrderDetailQuantity(CustomerOrderDetail customerOrderDetail, float quantity) {
		customerOrderDetail.setQuantity(quantity);
		customerOrderDetail.setTotalPrice(quantity * customerOrderDetail.getUnitPrice());
	}
	
	private void setCustomerOrderPayment(CustomerOrder customerOrder, PaymentsFormBean paymentsForm) {
		customerOrder.setCash(paymentsForm.getCash());
		customerOrder.setCheckAccountNumber(paymentsForm.getCheckAccountNumber());
		customerOrder.setCheckNumber(paymentsForm.getCheckNumber());
		customerOrder.setCheckAmount(paymentsForm.getCheckAmount() != null ? paymentsForm.getCheckAmount() : 0.0f);
		customerOrder.setCardTransactionNumber(paymentsForm.getCardTransactionNumber());
		customerOrder.setCardAmount(paymentsForm.getCardAmount() != null ? paymentsForm.getCardAmount() : 0.0f);
		customerOrder.setPointsAmount(paymentsForm.getPointsAmount() != null ? paymentsForm.getPointsAmount() : 0.0f);
	}
	
	private void setEarnedPoints(CustomerOrder customerOrder) {
		final List<CustomerOrderDetail> orderItems = customerOrderDetailService.findAllByCustomerOrderId(customerOrder.getId());
		Float totalProfit = 0.0f;
		for(CustomerOrderDetail cod : orderItems) {
			totalProfit += cod.getTotalPrice() - (cod.getUnitPrice() / (1 + (cod.getMargin() / 100)) * cod.getQuantity());
		}
		
		// set points earned to the lower amount between (10% of total profit AND .5% of total amount)
		// points are eligible to earn points (200 points used = 1 new point) (40k worth = 201 points)
		customerOrder.setPointsEarned(Math.round(Math.min(totalProfit * .10f, customerOrder.getTotalAmount() * .005f) * 100.0f) / 100.0f);
	}
	
	private void setDiscount(CustomerOrder customerOrder, DiscountFormBean discountForm) {
		customerOrder.setDiscountIdNumber(discountForm.getDiscountIdNumber());
		customerOrder.setDiscountName(discountForm.getName());
		customerOrder.setDiscountAddress(discountForm.getAddress());
		customerOrder.setDiscountTin(discountForm.getTin());
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
