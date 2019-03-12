package com.chua.evergrocery.database.prototype;

import com.chua.evergrocery.database.entity.CompanyMTDSalesSummary;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 12, 2019
 */
public interface CompanyMTDSalesSummaryPrototype {

	CompanyMTDSalesSummary findByCompanyAndMonthId(long companyId, int monthId);
}
