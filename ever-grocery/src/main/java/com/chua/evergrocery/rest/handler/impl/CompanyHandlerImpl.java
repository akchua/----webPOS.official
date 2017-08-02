package com.chua.evergrocery.rest.handler.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.beans.CompanyFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Company;
import com.chua.evergrocery.database.service.CompanyService;
import com.chua.evergrocery.enums.ReceiptType;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.CompanyHandler;

@Transactional
@Component
public class CompanyHandlerImpl implements CompanyHandler {

	@Autowired
	private CompanyService companyService;

	@Override
	public ObjectList<Company> getCompanyObjectList(Integer pageNumber, String searchKey) {
		return companyService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), searchKey);
	}
	
	@Override
	public Company getCompany(Long companyId) {
		return companyService.find(companyId);
	}
	
	@Override
	public ResultBean createCompany(CompanyFormBean companyForm) {
		final ResultBean result;
		
		if(!companyService.isExistsByName(companyForm.getName())) {
			final Company company = new Company();
			setCompany(company, companyForm);
			
			result = new ResultBean();
			result.setSuccess(companyService.insert(company) != null);
			if(result.getSuccess()) {
				result.setMessage("Company successfully created.");
			} else {
				result.setMessage("Failed to create company.");
			}
		} else {
			result = new ResultBean(false, "Company \"" + companyForm.getName() + "\" already exists!");
		}
		
		return result;
	}
	
	@Override
	public ResultBean updateCompany(CompanyFormBean companyForm) {
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
	public ResultBean removeCompany(Long companyId) {
		final ResultBean result;
		
		final Company company = companyService.find(companyId);
		if(company != null) {
			result = new ResultBean();
			
			result.setSuccess(companyService.delete(company));
			if(result.getSuccess()) {
				result.setMessage("Successfully removed Company \"" + company.getName() + "\".");
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
		company.setReceiptType(companyForm.getReceiptType());
	}
	
	@Override
	public List<String> getReceiptTypeList() {
		return Arrays.asList(ReceiptType.values()).stream().map(ReceiptType::name).collect(Collectors.toList());
	}
}
