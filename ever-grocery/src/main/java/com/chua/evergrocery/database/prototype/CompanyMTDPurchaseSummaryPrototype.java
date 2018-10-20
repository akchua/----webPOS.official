package com.chua.evergrocery.database.prototype;

import com.chua.evergrocery.database.entity.CompanyMTDPurchaseSummary;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
public interface CompanyMTDPurchaseSummaryPrototype {

	CompanyMTDPurchaseSummary findByCompanyAndMonthId(long companyId, int monthId);
}
