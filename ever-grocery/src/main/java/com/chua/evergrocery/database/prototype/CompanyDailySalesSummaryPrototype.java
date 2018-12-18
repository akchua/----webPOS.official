package com.chua.evergrocery.database.prototype;

import java.util.Date;

import com.chua.evergrocery.database.entity.CompanyDailySalesSummary;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   18 Dec 2018
 */
public interface CompanyDailySalesSummaryPrototype {

	CompanyDailySalesSummary findByCompanyAndSalesDate(long companyId, Date salesDate);
}
