define(['jquery'], function ($) {
    return {
    	getCustomerCategoryList: function(pageNumber, searchKey) {
    		return $.ajax({
    			url: '/services/custcategory/list',
    			data: {
    				pageNumber: pageNumber - 1,
    				searchKey: searchKey
    			}
    		});
    	},
    	
    	getCustomerCategory: function(customerCategoryId) {
    		return $.ajax({
    			url: '/services/custcategory/get',
    			data: {
    				customerCategoryId: customerCategoryId
    			}
    		});
    	},
    	
    	saveCustomerCategory: function(customerCategoryFormData) {
    		return $.ajax({
    			url: '/services/custcategory/save',
    			method: 'POST',
    			data: {
    				customerCategoryFormData: customerCategoryFormData
    			}
    		});
    	},
    	
    	removeCustomerCategory: function(customerCategoryId) {
    		return $.ajax({
    			url: '/services/custcategory/remove',
    			method: 'POST',
    			data: {
    				customerCategoryId: customerCategoryId
    			}
    		});
    	},
    	
    	getCustomerCategoryListByName: function() {
    		return $.ajax({
    			url: '/services/custcategory/listbyname'
    		});
    	}
    };
});