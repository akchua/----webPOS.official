package com.chua.evergrocery.rest.handler.impl;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.annotations.CheckAuthority;
import com.chua.evergrocery.beans.PromoFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.database.entity.Promo;
import com.chua.evergrocery.database.service.CustomerOrderDetailService;
import com.chua.evergrocery.database.service.ProductService;
import com.chua.evergrocery.database.service.PromoService;
import com.chua.evergrocery.enums.Color;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.ActivityLogHandler;
import com.chua.evergrocery.rest.handler.PromoHandler;
import com.chua.evergrocery.rest.validator.PromoFormValidator;
import com.chua.evergrocery.utility.Html;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Aug 1, 2020
 */
@Transactional
@Component
public class PromoHandlerImpl implements PromoHandler {

	@Autowired
	private PromoService promoService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ActivityLogHandler activityLogHandler;
	
	@Autowired
	private PromoFormValidator promoFormValidator;
	
	@Autowired
	private CustomerOrderDetailService customerOrderDetailService;
	
	@Override
	public ObjectList<Promo> getPromoList(Integer pageNumber, Boolean showActiveOnly) {
		return promoService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), showActiveOnly);
	}
	
	@Override
	public Promo getPromo(Long promoId) {
		return promoService.find(promoId);
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean createPromo(PromoFormBean promoForm, String ip) {
		final ResultBean result;
		final Map<String, String> errors = promoFormValidator.validate(promoForm);
		
		if(errors.isEmpty()) {
			final Promo existingPromo = promoService.findByProductAndDuration(promoForm.getProductId(), promoForm.getStartDate(), promoForm.getEndDate(), 0l);
			
			if(existingPromo != null) {
				errors.put("startDate", "Overlapping promo exists!");
			}
			
			if(errors.isEmpty()) {
				final Promo promo = new Promo();
				setPromo(promo, promoForm);
				promo.setCreator(UserContextHolder.getUser().getUserEntity());
				
				result = new ResultBean();
				result.setSuccess(promoService.insert(promo) != null);
				
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created Promo for Product " + Html.text(Color.BLUE, promo.getProduct().getDisplayName()) + "."));
					activityLogHandler.myLog("created a promo for product : " + promo.getProduct().getId() + " - " + promo.getProduct().getName(), ip);
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, "");
				result.addToExtras("errors", errors);
			}
		} else {
			result = new ResultBean(Boolean.FALSE, "");
			result.addToExtras("errors", errors);
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean updatePromo(PromoFormBean promoForm, String ip) {
		final ResultBean result;
		
		final Promo promo = promoService.find(promoForm.getId());
		if(promo != null) {
			final Map<String, String> errors = promoFormValidator.validate(promoForm);
			final Promo existingPromo = promoService.findByProductAndDuration(promoForm.getProductId(), promoForm.getStartDate(), promoForm.getEndDate(), promoForm.getId());
			
			if(existingPromo != null && !existingPromo.getId().equals(promoForm.getId())) {
				errors.put("startDate", "Overlapping promo exists!");
			}
				
			if(errors.isEmpty()) {
				setPromo(promo, promoForm);
				
				result = new ResultBean();
				result.setSuccess(promoService.update(promo));
				if(result.getSuccess()) {
					result.setMessage(Html.line("Promo for product " + Html.text(Color.BLUE, promo.getProduct().getDisplayName()) + " has been successfully " + Html.text(Color.GREEN, "updated") + "."));
					activityLogHandler.myLog("updated a promo for product : " + promo.getProduct().getId() + " - " + promo.getProduct().getName(), ip);
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, "");
				result.addToExtras("errors", errors);
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load promo. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean removePromo(Long promoId, String ip) {
		final ResultBean result;
		
		final Promo promo = promoService.find(promoId);
		if(promo != null) {
			result = new ResultBean();
			
			result.setSuccess(promoService.delete(promo));
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed Promo for Product " + Html.text(Color.BLUE, promo.getProduct().getName()) + "."));
				activityLogHandler.myLog("removed a promo for product : " + promo.getProduct().getId() + " - " + promo.getProduct().getName(), ip);
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load promo. Please refresh the page."));
		}
		
		return result;
	}
	
	private void setPromo(Promo promo, PromoFormBean promoForm) {
		promo.setProduct(productService.find(promoForm.getProductId()));
		promo.setStartDate(promoForm.getStartDate());
		promo.setEndDate(promoForm.getEndDate());
		promo.setBudget(promoForm.getBudget());
		promo.setUsedBudget(promoForm.getUsedBudget());
		promo.setDiscountPercent(promoForm.getDiscountPercent());
		promo.setPromoType(promoForm.getPromoType());
	}

	@Override
	public ResultBean finalizeUsedPromos(Long customerOrderId) {
		final ResultBean result;
		final List<CustomerOrderDetail> customerOrderDetails = customerOrderDetailService.findAllByCustomerOrderId(customerOrderId);
		
		for(CustomerOrderDetail orderDetail : customerOrderDetails) {
			if(orderDetail.getPromoType() != null) {
				Promo promo = promoService.get(orderDetail.getPromoId());
				promo.setUsedBudget(promo.getUsedBudget() + orderDetail.getPromoDiscountAmount());
				promoService.update(promo);
			}
		}
		
		result = new ResultBean(Boolean.TRUE, "");
		return result;
	}
}
