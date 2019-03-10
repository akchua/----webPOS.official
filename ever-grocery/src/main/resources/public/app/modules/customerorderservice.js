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
    	
    	getCashierCustomerOrderList: function(pageNumber, searchKey) {
    		return $.ajax({
    			url: '/services/customerorder/cashierlist',
    			data: {
    				pageNumber: pageNumber - 1,
    				searchKey: searchKey
    			}
    		});
    	},
    	
    	getListingCustomerOrderList: function(pageNumber, searchKey) {
    		return $.ajax({
    			url: '/services/customerorder/listinglist',
    			data: {
    				pageNumber: pageNumber - 1,
    				searchKey: searchKey
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
    	
    	getCustomerOrderBySIN: function(serialInvoiceNumber) {
    		return $.ajax({
    			url: '/services/customerorder/getbysin',
    			data: {
    				serialInvoiceNumber: serialInvoiceNumber
    			}
    		});
    	},
    	
    	createCustomerOrder: function() {
    		return $.ajax({
    			url: '/services/customerorder/create',
    			method: 'POST'
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
    	
    	applyDiscount: function(customerOrderId, discountType, grossAmountLimit) {
    		return $.ajax({
    			url: '/services/customerorder/applydiscount',
    			method: 'POST',
    			data: {
    				customerOrderId: customerOrderId,
    				discountType: discountType,
    				grossAmountLimit: grossAmountLimit
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
    	
    	submitCustomerOrder: function(customerOrderId) {
    		return $.ajax({
    			url: '/services/customerorder/submit',
    			method: 'POST',
    			data: {
    				customerOrderId: customerOrderId
    			}
    		});
    	},
    	
    	printCustomerOrderCopy: function(customerOrderId) {
    		return $.ajax({
    			url: '/services/customerorder/printordercopy',
    			method: 'POST',
    			data: {
    				customerOrderId: customerOrderId
    			}
    		});
    	},
    	
    	printReceipt: function(customerOrderId) {
    		return $.ajax({
    			url: '/services/customerorder/printreceipt',
    			method: 'POST',
    			data: {
    				customerOrderId: customerOrderId
    			}
    		});
    	},
    	
    	generateReport: function(salesReportQueryData) {
    		return $.ajax({
    			url: '/services/customerorder/generatereport',
    			method: 'POST',
    			data: {
    				salesReportQueryData: salesReportQueryData
    			}
    		});
    	}
    };
});