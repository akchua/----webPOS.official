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
    	
    	getCustomerOrderListByCustomer: function(pageNumber, customerId, async) {
    		return $.ajax({
    			url: '/services/customerorder/listbycustomer',
    			async: async,
    			data: {
    				pageNumber: pageNumber - 1,
    				customerId: customerId
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
    	
    	setCustomer: function(customerOrderId, customerCardId) {
    		return $.ajax({
    			url: '/services/customerorder/setcustomer',
    			method: 'POST',
    			data: {
    				customerOrderId: customerOrderId,
    				customerCardId: customerCardId
    			}
    		});
    	},
    	
    	setCustomerByCode: function(customerOrderId, customerCode) {
    		return $.ajax({
    			url: '/services/customerorder/setcustomerbycode',
    			method: 'POST',
    			data: {
    				customerOrderId: customerOrderId,
    				customerCode: customerCode
    			}
    		});
    	},
    	
    	removeCustomer: function(customerOrderId) {
    		return $.ajax({
    			url: '/services/customerorder/removecustomer',
    			method: 'POST',
    			data: {
    				customerOrderId: customerOrderId
    			}
    		});
    	},
    	
    	applyDiscount: function(discountFormData) {
    		return $.ajax({
    			url: '/services/customerorder/applydiscount',
    			method: 'POST',
    			data: {
    				discountFormData: discountFormData
    			}
    		});
    	},
    	
    	payCustomerOrder: function(paymentsFormData) {
    		return $.ajax({
    			url: '/services/customerorder/paycustomerorder',
    			method: 'POST',
    			data: {
    				paymentsFormData: paymentsFormData
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
    	
    	getCustomerOrderDetailListByProduct: function(pageNumber, productId, async) {
    		return $.ajax({
    			url: '/services/customerorder/detaillistbyproduct',
    			async: async,
    			data: {
    				pageNumber: pageNumber - 1,
    				productId: productId
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
    	
    	returnCustomerOrder: function(customerOrderId) {
    		return $.ajax({
    			url: '/services/customerorder/return',
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
    	
    	printOriginalReceipt: function(customerOrderId, footer) {
    		return $.ajax({
    			url: '/services/customerorder/printoriginalreceipt',
    			method: 'POST',
    			data: {
    				customerOrderId: customerOrderId,
    				footer: footer
    			}
    		});
    	},
    	
    	printReceiptCopy: function(customerOrderId) {
    		return $.ajax({
    			url: '/services/customerorder/printreceiptcopy',
    			method: 'POST',
    			data: {
    				customerOrderId: customerOrderId
    			}
    		});
    	},
    	
    	printZReading: function(readingDate) {
    		return $.ajax({
    			url: '/services/customerorder/printzreading',
    			method: 'POST',
    			data: {
    				readingDate: readingDate
    			}
    		});
    	},
    	
    	endOfShift: function() {
    		return $.ajax({
    			url: '/services/customerorder/endofshift',
    			method: 'POST'
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
    	},
    	
    	generateBackendReport: function(dateFrom, dateTo) {
    		return $.ajax({
    			url: '/services/customerorder/backendreport',
    			method: 'POST',
    			data: {
    				dateFrom: dateFrom,
    				dateTo: dateTo
    			}
    		});
    	},
    	
    	updatePackageCount: function(customerOrderId, cartonCount, plasticCount, bagCount) {
    		return $.ajax({
    			url: '/services/customerorder/updatepackagecount',
    			method: 'POST',
    			data: {
    				customerOrderId: customerOrderId,
    				cartonCount: cartonCount,
    				plasticCount: plasticCount,
    				bagCount: bagCount
    			}
    		});
    	}
    };
});