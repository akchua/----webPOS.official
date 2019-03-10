package com.chua.evergrocery.rest.handler;

import com.chua.evergrocery.beans.CashTransferFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.CashTransfer;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Jul 3, 2018
 */
public interface CashTransferHandler {
	
	CashTransfer getCashTransfer(Long cashTransferId);

	ObjectList<CashTransfer> getMyCashTransfer(Integer pageNumber);
	
	ObjectList<CashTransfer> getCashTransferObjectList(Integer pageNumber, Long userId, Status status);
	
	ResultBean requestCashTransfer(CashTransferFormBean cashTransferForm);
	
	ResultBean acceptCashTransfer(Long cashTransferId, String auth);
	
	ResultBean cancelCashTransfer(Long cashTransferId);
	
	/*ResultBean auditUser(Long userId, Boolean fullAudit);*/
}
