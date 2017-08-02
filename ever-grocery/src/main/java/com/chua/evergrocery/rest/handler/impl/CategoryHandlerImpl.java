package com.chua.evergrocery.rest.handler.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.beans.CategoryFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Category;
import com.chua.evergrocery.database.service.CategoryService;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.CategoryHandler;

@Transactional
@Component
public class CategoryHandlerImpl implements CategoryHandler {

	@Autowired
	private CategoryService categoryService;

	@Override
	public ObjectList<Category> getCategoryObjectList(Integer pageNumber, String searchKey) {
		return categoryService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), searchKey);
	}
	
	@Override
	public Category getCategory(Long categoryId) {
		return categoryService.find(categoryId);
	}
	
	@Override
	public ResultBean createCategory(CategoryFormBean categoryForm) {
		final ResultBean result;
		
		if(!categoryService.isExistsByName(categoryForm.getName())) {
			final Category category = new Category();
			setCategory(category, categoryForm);
			
			result = new ResultBean();
			result.setSuccess(categoryService.insert(category) != null);
			if(result.getSuccess()) {
				result.setMessage("Category successfully created.");
			} else {
				result.setMessage("Failed to create category.");
			}
		} else {
			result = new ResultBean(false, "Category \"" + categoryForm.getName() + "\" already exists!");
		}
		
		return result;
	}
	
	@Override
	public ResultBean updateCategory(CategoryFormBean categoryForm) {
		final ResultBean result;
		
		final Category category = categoryService.find(categoryForm.getId());
		if(category != null) {
			if(!(StringUtils.trimToEmpty(category.getName()).equalsIgnoreCase(categoryForm.getName())) &&
					categoryService.isExistsByName(categoryForm.getName())) {
				result = new ResultBean(false, "Category \"" + categoryForm.getName() + "\" already exists!");
			} else {
				setCategory(category, categoryForm);
				
				result = new ResultBean();
				result.setSuccess(categoryService.update(category));
				if(result.getSuccess()) {
					result.setMessage("Category successfully updated.");
				} else {
					result.setMessage("Failed to update category.");
				}
			}
		} else {
			result = new ResultBean(false, "Category not found.");
		}
		
		return result;
	}

	@Override
	public ResultBean removeCategory(Long categoryId) {
		final ResultBean result;
		
		final Category category = categoryService.find(categoryId);
		if(category != null) {
			result = new ResultBean();
			
			result.setSuccess(categoryService.delete(category));
			if(result.getSuccess()) {
				result.setMessage("Successfully removed Category \"" + category.getName() + "\".");
			} else {
				result.setMessage("Failed to remove Category \"" + category.getName() + "\".");
			}
		} else {
			result = new ResultBean(false, "Category not found.");
		}
		
		return result;
	}
	
	@Override
	public List<Category> getCategoryList() {
		return categoryService.findAllOrderByName();
	}
	
	private void setCategory(Category category, CategoryFormBean categoryForm) {
		category.setName(categoryForm.getName());
	}
}
