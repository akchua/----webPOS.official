define(['jquery'], function ($) {
	return {
    	generateInventory: function(companyId) {
    		return $.ajax({
    			url: '/services/inventory/bycompany',
    			method: 'POST',
    			data: {
    				companyId: companyId
    			}
    		});
    	}
	};
});