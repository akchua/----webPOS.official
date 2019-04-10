package com.chua.evergrocery.rest.handler;

import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.SalesReportQueryBean;
import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.database.entity.XReading;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   9 Jul 2018
 */
public interface SalesReportHandler {
	
	void updateZReading();
	
	XReading getXReadingByCashier(User cashier);
	
	ResultBean generateReport(SalesReportQueryBean salesReportQuery);
}
