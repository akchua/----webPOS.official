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