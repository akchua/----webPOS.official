define(['jquery'], function ($) {
	return {
		getProductInventory: function(productId) {
    		return $.ajax({
    			url: '/services/inventory/byproduct',
    			data: {
    				productId: productId
    			}
    		});
    	},
		
    	generateInventory: function(companyId) {
    		return $.ajax({
    			url: '/services/inventory/bycompany',
    			method: 'POST',
    			data: {
    				companyId: companyId
    			}
    		});
    	},
    	
    	generateInventoryByCategoryName: function(categoryName) {
    		return $.ajax({
    			url: '/services/inventory/bycategoryname',
    			method: 'POST',
    			data: {
    				categoryName: categoryName
    			}
    		});
    	}
	};
});