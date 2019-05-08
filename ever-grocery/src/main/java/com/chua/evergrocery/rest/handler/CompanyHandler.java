package com.chua.evergrocery.rest.handler;

import java.util.List;

import com.chua.evergrocery.beans.CompanyFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Company;
import com.chua.evergrocery.enums.ReceiptType;
import com.chua.evergrocery.objects.ObjectList;

public interface CompanyHandler {

	ObjectList<Company> getCompanyObjectList(Integer pageNumber, String searchKey);
	
	ObjectList<Company> getCompanyListByRank(Integer pageNumber, String searchKey);
	
	Company getCompany(Long companyId);
	
	ResultBean createCompany(CompanyFormBean companyForm, String ip);
	
	ResultBean updateCompany(CompanyFormBean companyForm, String ip);
	
	ResultBean removeCompany(Long companyId, String ip);
	
	List<Company> getCompanyList();
	
	List<ReceiptType> getReceiptTypeList();
}
