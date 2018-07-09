package com.chua.evergrocery.rest.handler;

import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.SalesReportQueryBean;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   9 Jul 2018
 */
public interface SalesReportHandler {

	ResultBean generateReport(SalesReportQueryBean salesReportQuery);
}
