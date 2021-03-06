define(['jquery'], function ($) {
    return {
    	getCustomerList: function(pageNumber, searchKey) {
    		return $.ajax({
    			url: '/services/customer/list',
    			data: {
    				pageNumber: pageNumber - 1,
    				searchKey: searchKey
    			}
    		});
    	},
    	
    	getCustomerListByCategory: function(pageNumber, customerCategoryId, async) {
    		return $.ajax({
    			url: '/services/customer/listbycategory',
    			async: async,
    			data: {
    				pageNumber: pageNumber - 1,
    				customerCategoryId: customerCategoryId
    			}
    		});
    	},
    	
    	getOutOfScheduleCustomerList: function(pageNumber) {
    		return $.ajax({
    			url: '/services/customer/outofschedulelist',
    			data: {
    				pageNumber: pageNumber - 1
    			}
    		});
    	},
    	
    	getCustomer: function(customerId) {
    		return $.ajax({
    			url: '/services/customer/get',
    			data: {
    				customerId: customerId
    			}
    		});
    	},
    	
    	saveCustomer: function(customerFormData) {
    		return $.ajax({
    			url: '/services/customer/save',
    			method: 'POST',
    			data: {
    				customerFormData: customerFormData
    			}
    		});
    	},
    	
    	removeCustomer: function(customerId) {
    		return $.ajax({
    			url: '/services/customer/remove',
    			method: 'POST',
    			data: {
    				customerId: customerId
    			}
    		});
    	},
    	
    	getCustomerListByLastName: function() {
    		return $.ajax({
    			url: '/services/customer/listbylastname'
    		});
    	}
    };
});