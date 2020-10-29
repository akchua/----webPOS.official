define(['jquery'], function ($) {
    return {
    	getCustomerMTDSalesSummaryList: function(customerId) {
    		return $.ajax({
    			url: '/services/customersummary/customermtdsaleslist',
    			data: {
    				customerId: customerId
    			}
    		});
    	},
    	
    	getCustomerCategoryMTDSalesSummaryList: function(customerCategoryId) {
    		return $.ajax({
    			url: '/services/customersummary/customercategorymtdsaleslist',
    			data: {
    				customerCategoryId: customerCategoryId
    			}
    		});
    	}
    };
});