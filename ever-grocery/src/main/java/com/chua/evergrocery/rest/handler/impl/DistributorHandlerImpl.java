package com.chua.evergrocery.rest.handler.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.beans.DistributorFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Distributor;
import com.chua.evergrocery.database.service.DistributorService;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.DistributorHandler;

@Transactional
@Component
public class DistributorHandlerImpl implements DistributorHandler {

	@Autowired
	private DistributorService distributorService;

	@Override
	public ObjectList<Distributor> getDistributorObjectList(Integer pageNumber, String searchKey) {
		return distributorService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), searchKey);
	}
	
	@Override
	public Distributor getDistributor(Long distributorId) {
		return distributorService.find(distributorId);
	}
	
	@Override
	public ResultBean createDistributor(DistributorFormBean distributorForm) {
		final ResultBean result;
		
		if(!distributorService.isExistsByName(distributorForm.getName())) {
			final Distributor distributor = new Distributor();
			setDistributor(distributor, distributorForm);
			
			result = new ResultBean();
			result.setSuccess(distributorService.insert(distributor) != null);
			if(result.getSuccess()) {
				result.setMessage("Distributor successfully created.");
			} else {
				result.setMessage("Failed to create distributor.");
			}
		} else {
			result = new ResultBean(false, "Distributor \"" + distributorForm.getName() + "\" already exists!");
		}
		
		return result;
	}
	
	@Override
	public ResultBean updateDistributor(DistributorFormBean distributorForm) {
		final ResultBean result;
		
		final Distributor distributor = distributorService.find(distributorForm.getId());
		if(distributor != null) {
			if(!(StringUtils.trimToEmpty(distributor.getName()).equalsIgnoreCase(distributorForm.getName())) &&
					distributorService.isExistsByName(distributorForm.getName())) {
				result = new ResultBean(false, "Distributor \"" + distributorForm.getName() + "\" already exists!");
			} else {
				setDistributor(distributor, distributorForm);
				
				result = new ResultBean();
				result.setSuccess(distributorService.update(distributor));
				if(result.getSuccess()) {
					result.setMessage("Distributor successfully updated.");
				} else {
					result.setMessage("Failed to update distributor.");
				}
			}
		} else {
			result = new ResultBean(false, "Distributor not found.");
		}
		
		return result;
	}

	@Override
	public ResultBean removeDistributor(Long distributorId) {
		final ResultBean result;
		
		final Distributor distributor = distributorService.find(distributorId);
		if(distributor != null) {
			result = new ResultBean();
			
			result.setSuccess(distributorService.delete(distributor));
			if(result.getSuccess()) {
				result.setMessage("Successfully removed Distributor \"" + distributor.getName() + "\".");
			} else {
				result.setMessage("Failed to remove Distributor \"" + distributor.getName() + "\".");
			}
		} else {
			result = new ResultBean(false, "Distributor not found.");
		}
		
		return result;
	}
	
	@Override
	public List<Distributor> getDistributorList() {
		return distributorService.findAllOrderByName();
	}
	
	private void setDistributor(Distributor distributor, DistributorFormBean distributorForm) {
		distributor.setName(distributorForm.getName());
		distributor.setAddress(distributorForm.getAddress());
		distributor.setAgent(distributorForm.getAgent());
		distributor.setPhoneNumber(distributorForm.getPhoneNumber());
	}
}
