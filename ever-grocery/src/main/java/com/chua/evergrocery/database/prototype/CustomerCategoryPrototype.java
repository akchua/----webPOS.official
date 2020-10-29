package com.chua.evergrocery.database.prototype;

import com.chua.evergrocery.database.entity.CustomerCategory;
import com.chua.evergrocery.objects.ObjectList;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Oct 28, 2020
 */
public interface CustomerCategoryPrototype extends Prototype<CustomerCategory, Long> {

	ObjectList<CustomerCategory> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey);
}
