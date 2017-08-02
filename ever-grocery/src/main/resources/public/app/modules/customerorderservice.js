define(['jquery'], function ($) {
    return {
    	getCustomerOrderList: function(pageNumber, searchKey, showPaid, daysAgo) {
    		return $.ajax({
    			url: '/services/customerorder/list',
    			data: {
    				pageNumber: pageNumber - 1,
    				searchKey: searchKey,
    				showPaid: showPaid,
    				daysAgo: daysAgo
    			}
    		});
    	},
    	
    	getCustomerOrder: function(customerOrderId) {
    		return $.ajax({
    			url: '/services/customerorder/get',
    			data: {
    				customerOrderId: customerOrderId
    			}
    		});
    	},
    	
    	saveCustomerOrder: function(customerOrderFormData) {
    		return $.ajax({
    			url: '/services/customerorder/save',
    			method: 'POST',
    			data: {
    				customerOrderFormData: customerOrderFormData
    			}
    		});
    	},
    	
    	removeCustomerOrder: function(customerOrderId) {
    		return $.ajax({
    			url: '/services/customerorder/remove',
    			method: 'POST',
    			data: {
    				customerOrderId: customerOrderId
    			}
    		});
    	},
    	
    	payCustomerOrder: function(customerOrderId, cash) {
    		return $.ajax({
    			url: '/services/customerorder/paycustomerorder',
    			method: 'POST',
    			data: {
    				customerOrderId: customerOrderId,
    				cash: cash
    			}
    		});
    	},
    	
    	refreshCustomerOrder: function(customerOrderId) {
    		return $.ajax({
    			url: '/services/customerorder/refreshcustomerorder',
    			method: 'POST',
    			data: {
    				customerOrderId: customerOrderId
    			}
    		});
    	},
    	
    	getCustomerOrderDetailList: function(pageNumber, customerOrderId, async) {
    		return $.ajax({
    			url: '/services/customerorder/detaillist',
    			async: async,
    			data: {
    				pageNumber: pageNumber - 1,
    				customerOrderId: customerOrderId
    			}
    		});
    	},
    	
    	addItemByBarcode: function(barcode, customerOrderId) {
    		return $.ajax({
    			url: '/services/customerorder/additembybarcode',
    			method: 'POST',
    			data: {
    				barcode: barcode,
    				customerOrderId: customerOrderId
    			}
    		});
    	},
    	
    	addItem: function(productDetailId, customerOrderId, quantity) {
    		return $.ajax({
    			url: '/services/customerorder/additem',
    			method: 'POST',
    			data: {
    				productDetailId: productDetailId,
    				customerOrderId: customerOrderId,
    				quantity: quantity
    			}
    		});
    	},
    	
    	removeCustomerOrderDetail: function(customerOrderDetailId) {
    		return $.ajax({
    			url: '/services/customerorder/removeitem',
    			method: 'POST',
    			data: {
    				customerOrderDetailId: customerOrderDetailId
    			}
    		});
    	},
    	
    	changeQuantity: function(customerOrderDetailId, quantity) {
    		return $.ajax({
    			url: '/services/customerorder/changequantity',
    			method: 'POST',
    			data: {
    				customerOrderDetailId: customerOrderDetailId,
    				quantity: quantity
    			}
    		});
    	},
    	
    	printCustomerOrderList: function(customerOrderId) {
    		return $.ajax({
    			url: '/services/customerorder/printorderlist',
    			method: 'POST',
    			data: {
    				customerOrderId: customerOrderId
    			}
    		});
    	},
    	
    	printReceipt: function(customerOrderId, cash) {
    		return $.ajax({
    			url: '/services/customerorder/printreceipt',
    			method: 'POST',
    			data: {
    				customerOrderId: customerOrderId,
    				cash: cash
    			}
    		});
    	}
    };
});