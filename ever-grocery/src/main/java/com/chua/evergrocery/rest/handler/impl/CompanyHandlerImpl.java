package com.chua.evergrocery.rest.handler.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.annotations.CheckAuthority;
import com.chua.evergrocery.beans.CompanyFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Company;
import com.chua.evergrocery.database.service.CompanyService;
import com.chua.evergrocery.database.service.DistributorService;
import com.chua.evergrocery.enums.ReceiptType;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.ActivityLogHandler;
import com.chua.evergrocery.rest.handler.CompanyHandler;
import com.chua.evergrocery.utility.DateUtil;

@Transactional
@Component
public class CompanyHandlerImpl implements CompanyHandler {

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private DistributorService distributorService;
	
	@Autowired
	private ActivityLogHandler activityLogHandler;

	@Override
	public ObjectList<Company> getCompanyObjectList(Integer pageNumber, String searchKey) {
		return companyService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), searchKey);
	}
	
	@Override
	public ObjectList<Company> getCompanyListByRank(Integer pageNumber, String searchKey) {
		return companyService.findAllWithPagingOrderByProfit(pageNumber, UserContextHolder.getItemsPerPage(), searchKey);
	}
	
	@Override
	public Company getCompany(Long companyId) {
		return companyService.find(companyId);
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 3)
	public ResultBean createCompany(CompanyFormBean companyForm, String ip) {
		final ResultBean result;
		
		if(!companyService.isExistsByName(companyForm.getName())) {
			final Company company = new Company();
			setCompany(company, companyForm);
			company.setDaysBooked(0.0f);
			company.setLastPurchaseOrderDate(DateUtil.getDefaultDate());
			company.setPurchaseValuePercentage(0.0f);
			company.setSaleValuePercentage(0.0f);
			company.setProfitPercentage(0.0f);
			
			result = new ResultBean();
			result.setSuccess(companyService.insert(company) != null);
			if(result.getSuccess()) {
				result.setMessage("Company successfully created.");
				activityLogHandler.myLog("created a company : " + company.getId() + " - " + company.getName(), ip);
			} else {
				result.setMessage("Failed to create company.");
			}
		} else {
			result = new ResultBean(false, "Company \"" + companyForm.getName() + "\" already exists!");
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 3)
	public ResultBean updateCompany(CompanyFormBean companyForm, String ip) {
		final ResultBean result;
		
		final Company company = companyService.find(companyForm.getId());
		if(company != null) {
			if(!(StringUtils.trimToEmpty(company.getName()).equalsIgnoreCase(companyForm.getName())) &&
					companyService.isExistsByName(companyForm.getName())) {
				result = new ResultBean(false, "Company \"" + companyForm.getName() + "\" already exists!");
			} else {
				setCompany(company, companyForm);
				
				result = new ResultBean();
				result.setSuccess(companyService.update(company));
				if(result.getSuccess()) {
					result.setMessage("Company successfully updated.");
					activityLogHandler.myLog("updated a company : " + company.getId() + " - " + company.getName(), ip);
				} else {
					result.setMessage("Failed to update company.");
				}
			}
		} else {
			result = new ResultBean(false, "Company not found.");
		}
		
		return result;
	}

	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean removeCompany(Long companyId, String ip) {
		final ResultBean result;
		
		final Company company = companyService.find(companyId);
		if(company != null) {
			result = new ResultBean();
			
			result.setSuccess(companyService.delete(company));
			if(result.getSuccess()) {
				result.setMessage("Successfully removed Company \"" + company.getName() + "\".");
				activityLogHandler.myLog("removed a company : " + company.getId() + " - " + company.getName(), ip);
			} else {
				result.setMessage("Failed to remove Company \"" + company.getName() + "\".");
			}
		} else {
			result = new ResultBean(false, "Company not found.");
		}
		
		return result;
	}
	
	@Override
	public List<Company> getCompanyList() {
		return companyService.findAllOrderByName();
	}
	
	private void setCompany(Company company, CompanyFormBean companyForm) {
		company.setName(companyForm.getName());
		company.setAddress(companyForm.getAddress());
		company.setAgent(companyForm.getAgent());
		company.setPhoneNumber(companyForm.getPhoneNumber());
		company.setReceiptType(companyForm.getReceiptType() != null ? companyForm.getReceiptType() : ReceiptType.AFTER_VAT_AND_DISCOUNT);
		company.setDistributor(distributorService.find(companyForm.getDistributorId()));
	}
	
	@Override
	public List<ReceiptType> getReceiptTypeList() {
		return Stream.of(ReceiptType.values())
					.collect(Collectors.toList());
	}
}
