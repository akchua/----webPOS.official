package com.chua.evergrocery.rest.handler.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.beans.CustomerOrderFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.UserBean;
import com.chua.evergrocery.database.entity.Customer;
import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.database.entity.ProductDetail;
import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.database.service.CustomerOrderDetailService;
import com.chua.evergrocery.database.service.CustomerOrderService;
import com.chua.evergrocery.database.service.CustomerService;
import com.chua.evergrocery.database.service.ProductDetailService;
import com.chua.evergrocery.database.service.UserService;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.enums.UserType;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.CustomerOrderHandler;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.Html;
import com.chua.evergrocery.utility.print.OrderItem;
import com.chua.evergrocery.utility.print.OrderList;
import com.chua.evergrocery.utility.print.OrderReceipt;
import com.chua.evergrocery.utility.print.OrderReceiptConfig;

@Transactional
@Component
public class CustomerOrderHandlerImpl implements CustomerOrderHandler {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerOrderService customerOrderService;
	
	@Autowired
	private CustomerOrderDetailService customerOrderDetailService;
	
	@Autowired
	private ProductDetailService productDetailService;
	
	@Autowired
	private VelocityEngine velocityEngine;

	@Override
	public ObjectList<CustomerOrder> getCustomerOrderList(Integer pageNumber, String searchKey, Boolean showPaid, Integer daysAgo) {
		if(showPaid) {
			return customerOrderService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), searchKey, null, daysAgo);
		} else {
			return customerOrderService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), searchKey, new Status[] { Status.LISTING,  Status.PRINTED }, daysAgo);
		}
	}
	
	@Override
	public CustomerOrder getCustomerOrder(Long customerOrderId) {
		return customerOrderService.find(customerOrderId);
	}
	
	@Override
	public ResultBean createCustomerOrder(CustomerOrderFormBean customerOrderForm) {
		final ResultBean result;
		
		if(customerOrderForm.getName() != null) {
			if(!customerOrderService.isExistsByNameAndStatus(customerOrderForm.getName(), new Status[] { Status.LISTING, Status.PRINTED })) {
				final CustomerOrder customerOrder = new CustomerOrder();
				setCustomerOrder(customerOrder, customerOrderForm);
				customerOrder.setPaidOn(DateUtil.getDefaultDate());
				customerOrder.setTotalAmount(0.0f);
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
			} else {
				result = new ResultBean(false, "Customer order \"" + customerOrderForm.getName() + "\" already exists!");
			}
		} else {
			result = new ResultBean(false, "Input a name for the customer order.");
		}
		
		return result;
	}
	
	@Override
	public ResultBean updateCustomerOrder(CustomerOrderFormBean customerOrderForm) {
		final ResultBean result;
		
		final CustomerOrder customerOrder = customerOrderService.find(customerOrderForm.getId());
		if(customerOrder != null) {
			final UserBean currentUser = UserContextHolder.getUser();
			
			if(customerOrder.getStatus() == Status.LISTING && customerOrder.getCreator().getId() == currentUser.getId()) {
				if(!(StringUtils.trimToEmpty(customerOrder.getName()).equalsIgnoreCase(customerOrderForm.getName())) &&
						customerOrderService.isExistsByNameAndStatus(customerOrderForm.getName(), new Status[] { Status.LISTING, Status.PRINTED })) {
					result = new ResultBean(false, "Customer order \"" + customerOrderForm.getName() + "\" already exists!");
				} else {
					setCustomerOrder(customerOrder, customerOrderForm);
					
					result = new ResultBean();
					result.setSuccess(customerOrderService.update(customerOrder));
					if(result.getSuccess()) {
						result.setMessage("Customer order successfully updated.");
					} else {
						result.setMessage("Failed to update customer order.");
					}
				}
			} else {
				result = new ResultBean(false, "Customer order cannot be edited right now.");
			}
		} else {
			result = new ResultBean(false, "Customer order not found.");
		}
		
		return result;
	}

	@Override
	public ResultBean removeCustomerOrder(Long customerOrderId) {
		final ResultBean result;
		
		final CustomerOrder customerOrder = customerOrderService.find(customerOrderId);
		if(customerOrder != null) {
			final UserBean currentUser = UserContextHolder.getUser();
			
			if(customerOrder.getStatus() != Status.PAID && (currentUser.getUserType() == UserType.ADMINISTRATOR ||
					currentUser.getUserType() == UserType.MANAGER || currentUser.getUserType() == UserType.ASSISTANT_MANAGER)) {
				result = new ResultBean();
				
				customerOrder.setStatus(Status.CANCELLED);
				
				result.setSuccess(customerOrderService.delete(customerOrder));
				if(result.getSuccess()) {
					result.setMessage("Successfully removed Customer order \"" + customerOrder.getName() + "\".");
				} else {
					result.setMessage("Failed to remove Customer order \"" + customerOrder.getName() + "\".");
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
	public ResultBean payCustomerOrder(Long customerOrderId, Float cash) {
		final ResultBean result;
		final DecimalFormat df = new DecimalFormat("#.##");
		final CustomerOrder customerOrder = customerOrderService.find(customerOrderId);
		
		if(customerOrder != null) {
			if(customerOrder.getStatus() == Status.PRINTED) {
				if(customerOrder.getTotalAmount() <= cash) {
					result = new ResultBean();
					
					customerOrder.setCashier(userService.find(UserContextHolder.getUser().getId()));
					customerOrder.setStatus(Status.PAID);
					customerOrder.setPaidOn(new Date());
					
					result.setSuccess(customerOrderService.update(customerOrder));
					if(result.getSuccess()) {
						//#############################################################################################		remove from stock!!!
						result.setMessage("CHANGE: Php " + df.format(cash - customerOrder.getTotalAmount()));
					} else {
						result.setMessage("Failed to pay Customer order \"" + customerOrder.getName() + "\".");
					}
				} else {
					result = new ResultBean(false, "Insufficient cash.");
				}
			} else {
				result = new ResultBean(false, "Customer order already paid or not yet printed.");
			}
		} else {
			result = new ResultBean(false, "Customer order not found.");
		}
		
		return result;
	}
	
	private void setCustomerOrder(CustomerOrder customerOrder, CustomerOrderFormBean customerOrderForm) {
		customerOrder.setName(customerOrderForm.getName());
		customerOrder.setCustomer(customerService.find(customerOrderForm.getCustomerId()));
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
				if(customerOrder.getStatus() == Status.LISTING) {
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
		
		customerOrder.setTotalAmount(customerOrder.getTotalAmount() - customerOrderDetail.getTotalPrice());
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
			if(customerOrder.getStatus() == Status.LISTING) {
				if(productDetail != null) {
					result = this.addItem(productDetail, customerOrder, quantity);
				} else {
					result = new ResultBean(false, "Product not found.");
				}
			} else {
				result = new ResultBean(false, "Customer order cannot be edited right now.");
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
				result = new ResultBean();
				
				final CustomerOrderDetail newCustomerOrderDetail = new CustomerOrderDetail();
				setCustomerOrderDetail(newCustomerOrderDetail, customerOrder, productDetail);
				
				result.setSuccess(customerOrderDetailService.insert(newCustomerOrderDetail) != null &&
						this.changeCustomerOrderDetailQuantity(newCustomerOrderDetail, quantity).getSuccess());
				
				if(result.getSuccess()) {
					//result message used instead as boolean to check if item is new
					final String isNew = "NEW";
					
					result.setMessage(isNew);
				} else {
					result.setMessage("Failed to add item.");
				}
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
				if(customerOrder.getStatus() == Status.LISTING) {
					result = this.changeCustomerOrderDetailQuantity(customerOrderDetail, quantity);
				} else {
					result =  new ResultBean(false, "Customer order cannot be edited right now.");
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
		
		if(quantity > 0) {
			result = new ResultBean();
			final CustomerOrder customerOrder = customerOrderDetail.getCustomerOrder();
			
			customerOrder.setTotalAmount(customerOrder.getTotalAmount() - customerOrderDetail.getTotalPrice());
			customerOrder.setTotalItems(customerOrder.getTotalItems() - customerOrderDetail.getQuantity());
			
			setCustomerOrderDetailQuantity(customerOrderDetail, quantity);
			result.setSuccess(customerOrderDetailService.update(customerOrderDetail));
			
			if(result.getSuccess()) {
				customerOrder.setTotalAmount(customerOrder.getTotalAmount() + customerOrderDetail.getTotalPrice());
				customerOrder.setTotalItems(customerOrder.getTotalItems() + customerOrderDetail.getQuantity());
				customerOrderService.update(customerOrder);
				
				result.setMessage("Successfully updated quantity.");
			} else {
				result.setMessage("Failed to update quantity.");
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
		final ProductDetail upperProductDetail;
		final ProductDetail lowerProductDetail;
		switch(productDetail.getTitle()) {
		case "Whole":
			upperProductDetail = null;
			lowerProductDetail = productDetailService.findByProductIdAndTitle(productDetail.getProduct().getId(), "Piece");
			break;
		case "Piece":
			upperProductDetail = productDetailService.findByProductIdAndTitle(productDetail.getProduct().getId(), "Whole");
			lowerProductDetail = productDetailService.findByProductIdAndTitle(productDetail.getProduct().getId(), "Inner Piece");
			break;
		case "Inner Piece":
			upperProductDetail = productDetailService.findByProductIdAndTitle(productDetail.getProduct().getId(), "Piece");
			lowerProductDetail = productDetailService.findByProductIdAndTitle(productDetail.getProduct().getId(), "2nd Inner Piece");
			break;
		case "2nd Inner Piece":
			upperProductDetail = productDetailService.findByProductIdAndTitle(productDetail.getProduct().getId(), "Inner Piece");
			lowerProductDetail = null;
			break;
		default:
			upperProductDetail = null;
			lowerProductDetail = null;
		}
		
		final float minQuantity;
		if(lowerProductDetail != null && !lowerProductDetail.getQuantity().equals(0) && lowerProductDetail.getQuantity() % 2 == 0) {
			minQuantity = 0.5f;
		} else {
			minQuantity = 1.0f;
		}
		
		// trim off values not divisible by minimum quantity
		quantity -= (quantity % minQuantity);
		
		if(upperProductDetail != null) {
			this.addItem(upperProductDetail, customerOrderDetail.getCustomerOrder(), quantity / productDetail.getQuantity());
			result = quantity % (productDetail.getQuantity() * (productDetail.getQuantity() % 2 == 0 ? 0.5f : 1.0f));
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
		float totalAmount = 0;
		float totalItems = 0;
		
		List<CustomerOrderDetail> customerOrderDetails = customerOrderDetailService.findAllByCustomerOrderId(customerOrder.getId());
		
		for(CustomerOrderDetail customerOrderDetail : customerOrderDetails) {
			totalAmount += customerOrderDetail.getTotalPrice();
			totalItems += customerOrderDetail.getQuantity();
		}
		
		customerOrder.setTotalAmount(totalAmount);
		customerOrder.setTotalItems(totalItems);
		customerOrderService.update(customerOrder);
	}
	
	@Override
	public ResultBean printCustomerOrderList(Long customerOrderId) {
		final ResultBean result;
		
		final CustomerOrder customerOrder = customerOrderService.find(customerOrderId);
		
		/*if(customerOrder != null) {
			final UserBean currentUser = UserContextHolder.getUser();
			
			if(customerOrder.getStatus() == Status.LISTING || currentUser.getUserType() == UserType.ADMINISTRATOR ||
					currentUser.getUserType() == UserType.MANAGER || currentUser.getUserType() == UserType.ASSISTANT_MANAGER) {
				result = new ResultBean();
				
				if(customerOrder.getStatus() == Status.LISTING) {
					customerOrder.setStatus(Status.PRINTED);
					result.setSuccess(customerOrderService.update(customerOrder));
				} else {
					result.setSuccess(true);
				}
				
				if(result.getSuccess()) {
					this.printOrderList(customerOrder);
					
					result.setMessage("Successfully printed Customer order \"" + customerOrder.getName() + "\".");
				} else {
					result.setMessage("Failed to print Customer order \"" + customerOrder.getName() + "\".");
				}
			} else {
				result = new ResultBean(false, "You are not authorized to print a copy.");
			}
		} else {
			result = new ResultBean(false, "Customer order not found.");
		}*/
		customerOrder.setStatus(Status.PRINTED);
		customerOrderService.update(customerOrder);
		result = new ResultBean(Boolean.TRUE, "print service temporarily stopped.");
		
		return result;
	}
	
	private void printOrderList(CustomerOrder customerOrder) {
		final String creatorName;
		final User creator = customerOrder.getCreator();
		
		if(creator != null) {
			creatorName = creator.getUsername();
		} else {
			creatorName = "";
		}
		
		final List<CustomerOrderDetail> customerOrderDetails = customerOrderDetailService.findAllByCustomerOrderId(customerOrder.getId());
		final List<OrderItem> orderItems = new ArrayList<OrderItem>();
		
		for(CustomerOrderDetail orderDetail: customerOrderDetails) {
			final String displayName;
			
			if(orderDetail.getProductDisplayName() != null && orderDetail.getProductDisplayName().trim() != "") {
				displayName = orderDetail.getProductDisplayName();
			} else {
				displayName = orderDetail.getProductName();
			}
			
			orderItems.add(new OrderItem(displayName, orderDetail.getUnitType().getShorthand(), orderDetail.getTotalPrice(), orderDetail.getQuantity()));
		}
		
		final OrderList orderList = new OrderList(creatorName, customerOrder.getId() + "", orderItems);
		
		try {
			orderList.print(velocityEngine);
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	@Override
	public void printReceipt(Long customerOrderId, Float cash) {
		final CustomerOrder customerOrder = customerOrderService.find(customerOrderId);
		
		if(customerOrder != null) {
			this.printReceipt(customerOrder, cash);
		}
	}
	
	private void printReceipt(CustomerOrder customerOrder, Float cash) {
		
		final String customerName;
		final Customer customer = customerOrder.getCustomer();
		
		if(customer != null) {
			customerName = customer.getFirstName() + " " + customer.getLastName();
		} else {
			customerName = "";
		}
		
		final String cashierName;
		final User cashier = customerOrder.getCashier();
		
		if(cashier != null) {
			cashierName = cashier.getFirstName() + " " + cashier.getLastName();
		} else {
			cashierName = "";
		}
		
		final OrderReceipt orderReceipt = new OrderReceipt(new DateTime(), cashierName, customerOrder.getId() + "", customerName,
				customerOrder.getTotalAmount(), new OrderReceiptConfig("Ever Grocery"), "", cash);
		try {
			orderReceipt.print(velocityEngine);
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	private void setCustomerOrderDetail(CustomerOrderDetail customerOrderDetail, CustomerOrder customerOrder, ProductDetail productDetail) {
		customerOrderDetail.setCustomerOrder(customerOrder);
		customerOrderDetail.setProductDetail(productDetail);
		customerOrderDetail.setProductName(productDetail.getProduct().getName());
		customerOrderDetail.setProductDisplayName(productDetail.getProduct().getDisplayName());
		customerOrderDetail.setUnitType(productDetail.getUnitType());
		customerOrderDetail.setUnitPrice(productDetail.getSellingPrice());
		customerOrderDetail.setQuantity(0.0f);
		customerOrderDetail.setTotalPrice(0.0f);
		customerOrderDetail.setMargin(productDetail.getPercentProfit());
	}
	
	private void setCustomerOrderDetailQuantity(CustomerOrderDetail customerOrderDetail, float quantity) {
		customerOrderDetail.setQuantity(quantity);
		customerOrderDetail.setTotalPrice(quantity * customerOrderDetail.getUnitPrice());
	}
}
