package com.chua.evergrocery.database.prototype;

import com.chua.evergrocery.database.entity.PurchaseOrder;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.objects.ObjectList;

public interface PurchaseOrderPrototype {

	ObjectList<PurchaseOrder> findAllWithPaging(int pageNumber, int resultsPerPage, Long companyId);
	
	ObjectList<PurchaseOrder> findAllWithPagingAndStatus(int pageNumber, int resultsPerPage, Long companyId, Status[] status);
}
