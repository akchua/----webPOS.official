package com.chua.evergrocery.rest.handler;

import java.util.List;

import com.chua.evergrocery.beans.CategoryFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.Category;
import com.chua.evergrocery.objects.ObjectList;

public interface CategoryHandler {

	ObjectList<Category> getCategoryObjectList(Integer pageNumber, String searchKey);
	
	Category getCategory(Long categoryId);
	
	ResultBean createCategory(CategoryFormBean categoryForm, String ip);
	
	ResultBean updateCategory(CategoryFormBean categoryForm, String ip);
	
	ResultBean removeCategory(Long categoryId, String ip);
	
	List<Category> getCategoryList();
}
