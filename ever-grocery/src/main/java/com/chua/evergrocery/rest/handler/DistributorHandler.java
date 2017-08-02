package com.chua.evergrocery.rest.handler;

import java.util.List;

import com.chua.evergrocery.beans.DistributorFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Distributor;
import com.chua.evergrocery.objects.ObjectList;

public interface DistributorHandler {

	ObjectList<Distributor> getDistributorObjectList(Integer pageNumber, String searchKey);
	
	Distributor getDistributor(Long distributorId);
	
	ResultBean createDistributor(DistributorFormBean distributorForm);
	
	ResultBean updateDistributor(DistributorFormBean distributorForm);
	
	ResultBean removeDistributor(Long distributorId);
	
	List<Distributor> getDistributorList();
}
