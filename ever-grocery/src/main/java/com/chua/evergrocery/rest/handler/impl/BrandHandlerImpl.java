package com.chua.evergrocery.rest.handler.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.annotations.CheckAuthority;
import com.chua.evergrocery.beans.BrandFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Brand;
import com.chua.evergrocery.database.service.BrandService;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.ActivityLogHandler;
import com.chua.evergrocery.rest.handler.BrandHandler;

@Transactional
@Component
public class BrandHandlerImpl implements BrandHandler {

	@Autowired
	private BrandService brandService;
	
	@Autowired
	private ActivityLogHandler activityLogHandler;

	@Override
	public ObjectList<Brand> getBrandObjectList(Integer pageNumber, String searchKey) {
		return brandService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), searchKey);
	}
	
	@Override
	public Brand getBrand(Long brandId) {
		return brandService.find(brandId);
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 3)
	public ResultBean createBrand(BrandFormBean brandForm, String ip) {
		final ResultBean result;
		
		if(!brandService.isExistsByName(brandForm.getName())) {
			final Brand brand = new Brand();
			setBrand(brand, brandForm);
			
			result = new ResultBean();
			result.setSuccess(brandService.insert(brand) != null);
			if(result.getSuccess()) {
				result.setMessage("Brand successfully created.");
				activityLogHandler.myLog("created a brand : " + brand.getId() + " - " + brand.getName(), ip);
			} else {
				result.setMessage("Failed to create brand.");
			}
		} else {
			result = new ResultBean(false, "Brand \"" + brandForm.getName() + "\" already exists!");
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 3)
	public ResultBean updateBrand(BrandFormBean brandForm, String ip) {
		final ResultBean result;
		
		final Brand brand = brandService.find(brandForm.getId());
		if(brand != null) {
			if(!(StringUtils.trimToEmpty(brand.getName()).equalsIgnoreCase(brandForm.getName())) &&
					brandService.isExistsByName(brandForm.getName())) {
				result = new ResultBean(false, "Brand \"" + brandForm.getName() + "\" already exists!");
			} else {
				setBrand(brand, brandForm);
				
				result = new ResultBean();
				result.setSuccess(brandService.update(brand));
				if(result.getSuccess()) {
					result.setMessage("Brand successfully updated.");
					activityLogHandler.myLog("updated a brand : " + brand.getId() + " - " + brand.getName(), ip);
				} else {
					result.setMessage("Failed to update brand.");
				}
			}
		} else {
			result = new ResultBean(false, "Brand not found.");
		}
		
		return result;
	}

	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean removeBrand(Long brandId, String ip) {
		final ResultBean result;
		
		final Brand brand = brandService.find(brandId);
		if(brand != null) {
			result = new ResultBean();
			
			result.setSuccess(brandService.delete(brand));
			if(result.getSuccess()) {
				result.setMessage("Successfully removed Brand \"" + brand.getName() + "\".");
				activityLogHandler.myLog("removed a brand : " + brand.getId() + " - " + brand.getName(), ip);
			} else {
				result.setMessage("Failed to remove Brand \"" + brand.getName() + "\".");
			}
		} else {
			result = new ResultBean(false, "Brand not found.");
		}
		
		return result;
	}
	
	@Override
	public List<Brand> getBrandList() {
		return brandService.findAllOrderByName();
	}
	
	private void setBrand(Brand brand, BrandFormBean brandForm) {
		brand.setName(brandForm.getName());
	}
}
