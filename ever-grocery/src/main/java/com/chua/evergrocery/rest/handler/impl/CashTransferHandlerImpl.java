package com.chua.evergrocery.rest.handler.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.beans.AuditResultBean;
import com.chua.evergrocery.beans.CashTransferFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.CashTransfer;
import com.chua.evergrocery.database.entity.CustomerOrder;
import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.database.service.CashTransferService;
import com.chua.evergrocery.database.service.CustomerOrderService;
import com.chua.evergrocery.database.service.UserService;
import com.chua.evergrocery.enums.Color;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.CashTransferHandler;
import com.chua.evergrocery.rest.validator.CashTransferValidator;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.EncryptionUtil;
import com.chua.evergrocery.utility.Html;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Jul 3, 2018
 */
@Transactional
@Component
public class CashTransferHandlerImpl implements CashTransferHandler {

	@Autowired
	private CashTransferService cashTransferService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerOrderService customerOrderService;
	
	@Autowired
	private CashTransferValidator cashTransferValidator;

	@Override
	public CashTransfer getCashTransfer(Long cashTransferId) {
		return cashTransferService.find(cashTransferId);
	}
	
	@Override
	public ObjectList<CashTransfer> getMyCashTransfer(Integer pageNumber) {
		return cashTransferService.findRecentOrRequestingByRelatedUserWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), UserContextHolder.getUser().getId());
	}
	
	@Override
	public ObjectList<CashTransfer> getCashTransferObjectList(Integer pageNumber, Long userId, Status status) {
		return cashTransferService.findAllByRelatedUserAndStatusWithPagingOrderByLatest(pageNumber, UserContextHolder.getItemsPerPage(), userId, status != null ? new Status[] { status } : null);
	}

	@Override
	public ResultBean requestCashTransfer(CashTransferFormBean cashTransferForm) {
		final ResultBean result;
		final Map<String, String> errors = cashTransferValidator.validate(cashTransferForm);
		
		if(errors.isEmpty()) {
			final User cashTo = userService.find(cashTransferForm.getCashToId());
			
			if(cashTo.getId().equals(UserContextHolder.getUser().getId())) {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.line(Color.RED,  "Invalid!") + " Transfer to self."));
			} else {
				if(cashTo.getUserType().getAuthority() <= 2) {
					final CashTransfer cashTransfer = new CashTransfer();
					
					cashTransfer.setAmount(cashTransferForm.getAmount());
					cashTransfer.setCashFrom(UserContextHolder.getUser().getUserEntity());
					cashTransfer.setCashTo(cashTo);
					cashTransfer.setStatus(Status.REQUESTING);
					cashTransfer.setTransferredOn(DateUtil.getDefaultDate());
					
					result = new ResultBean();
					result.setSuccess(cashTransferService.insert(cashTransfer) != null);
					if(result.getSuccess()) {
						result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created request for cash transfer."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.line(Color.RED,  "Invalid!") + " You are only allowed to transfer cash to manager."));
				}
			}
		} else {
			result = new ResultBean(Boolean.FALSE, "");
			result.addToExtras("errors", errors);
		}
		
		return result;
	}

	@Override
	public ResultBean acceptCashTransfer(Long cashTransferId, String auth) {
		final ResultBean result;
		
		if(UserContextHolder.getUser().getPassword().equals(EncryptionUtil.getMd5(auth))) {
			final CashTransfer cashTransfer = cashTransferService.find(cashTransferId);
			
			if(cashTransfer != null) {
				if(cashTransfer.getCashTo().getId().equals(UserContextHolder.getUser().getId())) {
					if(cashTransfer.getStatus().equals(Status.REQUESTING)) {
						result = new ResultBean();
						
						cashTransfer.setStatus(Status.TRANSFERRED);
						cashTransfer.setTransferredOn(new Date());
						
						result.setSuccess(cashTransferService.update(cashTransfer));
						if(result.getSuccess()) {
							result.setMessage(Html.line(Html.text(Color.GREEN, "Success!") + " You have just received Php " + cashTransfer.getFormattedAmount() + " from " + cashTransfer.getCashFrom().getFormattedName() + "." ));
						} else {
							result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
						}
					} else {
						result = new ResultBean(Boolean.FALSE, Html.line("Cash transfer no longer available. Please refresh the page."));
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "DECLINED!") + " You are not the designated recipient of this cash transfer."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load cash transfer. Please refresh the page."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("Transfer " + Html.text(Color.RED, "DECLINED!") + " Invalid authorization."));
		}
		
		return result;
	}

	@Override
	public ResultBean cancelCashTransfer(Long cashTransferId) {
		final ResultBean result;
		final CashTransfer cashTransfer = cashTransferService.find(cashTransferId);
		
		if(cashTransfer != null) {
			final Long currentUserId = UserContextHolder.getUser().getId();
			result = new ResultBean();
			
			if(cashTransfer.getCashFrom().getId().equals(currentUserId)) {
				cashTransfer.setStatus(Status.CANCELLED);
				result.setSuccess(cashTransferService.delete(cashTransfer));
				if(result.getSuccess()){
					result.setMessage(Html.line(Html.text(Color.GREEN, "Success!") + " You have just canceled transfer of Php " + cashTransfer.getFormattedAmount() + " to " + cashTransfer.getCashTo().getFormattedName() + "." ));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else if(cashTransfer.getCashTo().getId().equals(currentUserId)) {
				cashTransfer.setStatus(Status.DECLINED);
				result.setSuccess(cashTransferService.update(cashTransfer));
				if(result.getSuccess()){
					result.setMessage(Html.line(Html.text(Color.GREEN, "Success!") + " You have just declined transfer of Php " + cashTransfer.getFormattedAmount() + " from " + cashTransfer.getCashFrom().getFormattedName() + "." ));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result.setSuccess(Boolean.FALSE);
				result.setMessage(Html.line(Html.text(Color.RED, "DECLINED!") + " You are not authorized to cancel this cash transfer."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load cash transfer. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	public ResultBean auditUser(Long userId, Boolean fullAudit) {
		final ResultBean result;
		
		if(userId != null) {
			final User user = userService.find(userId);
			
			if(user != null) {
				if(UserContextHolder.getUser().getUserType().getAuthority() <= user.getUserType().getAuthority()) {
					if(DateUtil.daysBetween(user.getLastFullAudit(), new Date()) > 13) {
						fullAudit = Boolean.TRUE;
					}
					
					final Date lastAudit = fullAudit ? DateUtil.getDefaultDate() : user.getLastAudit();
					if(fullAudit) user.setLastFullAudit(new Date());
					user.setLastAudit(new Date());
					
					final List<CashTransfer> cashTransfers = cashTransferService.findAllTransferredByUserAndDate(user.getId(), lastAudit);
					final List<CustomerOrder> customerOrders = customerOrderService.findAllPaidByCashierAndDateFromToNow(user.getId(), lastAudit);
					
					Float cashTransferOut = 0.0f;
					Float cashTransferReceived = 0.0f;
					for(CashTransfer cashTransfer : cashTransfers) {
						if(cashTransfer.getCashTo().equals(user.getId())) {
							cashTransferReceived += cashTransfer.getAmount();
						} else if(cashTransfer.getCashFrom().equals(user.getId())) {
							cashTransferOut += cashTransfer.getAmount();
						}
					}
					
					Float sales = 0.0f;
					for(CustomerOrder customerOrder : customerOrders) {
						if(customerOrder.getCashier().getId().equals(user.getId())) {
							sales += customerOrder.getTotalAmount();
						}
					}
					
					final Float previousWithheldCash = fullAudit ? 0 : user.getWithheldCash();
					
					final Float withheldCash = previousWithheldCash - cashTransferOut + cashTransferReceived + sales;
					
					result = new ResultBean();
					user.setWithheldCash(withheldCash);
					
					result.setSuccess(userService.update(user));
					if(result.getSuccess()) {
						final AuditResultBean auditResult = new AuditResultBean();
						auditResult.setCashTransferOut(cashTransferOut);
						auditResult.setCashTransferReceived(cashTransferReceived);
						auditResult.setLastAudit(lastAudit);
						auditResult.setSales(sales);
						auditResult.setWithheldCash(withheldCash);
						
						result.addToExtras("auditResult", auditResult);
						result.setMessage("");
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "DECLINED!") + " You are not authorized to audit this user."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load user. Please refresh the page."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, "");
		}
		
		return result;
	}
}
