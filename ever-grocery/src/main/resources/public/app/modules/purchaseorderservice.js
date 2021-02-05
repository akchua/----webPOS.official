define(['jquery'], function ($) {
	return {
		getPurchaseOrderList: function(pageNumber, companyId, showChecked) {
    		return $.ajax({
    			url: '/services/purchaseorder/list',
    			data: {
    				pageNumber: pageNumber - 1,
    				companyId: companyId,
    				showChecked: showChecked
    			}
    		});
    	},
    	
    	getPurchaseOrder: function(purchaseOrderId) {
    		return $.ajax({
    			url: '/services/purchaseorder/get',
    			data: {
    				purchaseOrderId: purchaseOrderId
    			}
    		});
    	},
    	
    	savePurchaseOrder: function(purchaseOrderFormData) {
    		return $.ajax({
    			url: '/services/purchaseorder/save',
    			method: 'POST',
    			data: {
    				purchaseOrderFormData: purchaseOrderFormData
    			}
    		});
    	},
    	
    	generatePurchaseOrder: function(companyId, daysToBook) {
    		return $.ajax({
    			url: '/services/purchaseorder/generate',
    			method: 'POST',
    			data: {
    				companyId: companyId,
    				daysToBook: daysToBook
    			}
    		});
    	},
    	
    	generateOfftake: function(companyId, offtakeDays, download, print) {
    		return $.ajax({
    			url: '/services/purchaseorder/offtake',
    			method: 'POST',
    			data: {
    				companyId: companyId,
    				offtakeDays: offtakeDays,
    				download: download,
    				print: print
    			}
    		});
    	},
    	
    	removePurchaseOrder: function(purchaseOrderId) {
    		return $.ajax({
    			url: '/services/purchaseorder/remove',
    			method: 'POST',
    			data: {
    				purchaseOrderId: purchaseOrderId
    			}
    		});
    	},
    	
    	refreshPurchaseOrder: function(purchaseOrderId) {
    		return $.ajax({
    			url: '/services/purchaseorder/refreshpurchaseorder',
    			method: 'POST',
    			data: {
    				purchaseOrderId: purchaseOrderId
    			}
    		});
    	},
    	
    	getPurchaseOrderDetailList: function(pageNumber, purchaseOrderId, async) {
    		return $.ajax({
    			url: '/services/purchaseorder/detaillist',
    			async: async,
    			data: {
    				pageNumber: pageNumber - 1,
    				purchaseOrderId: purchaseOrderId
    			}
    		});
    	},
    	
    	getPurchaseOrderDetailListByProduct: function(pageNumber, productId, async) {
    		return $.ajax({
    			url: '/services/purchaseorder/detaillistbyproduct',
    			async: async,
    			data: {
    				pageNumber: pageNumber - 1,
    				productId: productId
    			}
    		});
    	},
    	
    	addItem: function(productDetailId, purchaseOrderId, quantity) {
    		return $.ajax({
    			url: '/services/purchaseorder/additem',
    			method: 'POST',
    			data: {
    				productDetailId: productDetailId,
    				purchaseOrderId: purchaseOrderId,
    				quantity: quantity
    			}
    		});
    	},
    	
    	addItemByBarcode: function(barcode, purchaseOrderId) {
    		return $.ajax({
    			url: '/services/purchaseorder/additembybarcode',
    			method: 'POST',
    			data: {
    				barcode: barcode,
    				purchaseOrderId: purchaseOrderId
    			}
    		});
    	},
    	
    	removePurchaseOrderDetail: function(purchaseOrderDetailId) {
    		return $.ajax({
    			url: '/services/purchaseorder/removeitem',
    			method: 'POST',
    			data: {
    				purchaseOrderDetailId: purchaseOrderDetailId
    			}
    		});
    	},
    	
    	changeQuantity: function(purchaseOrderDetailId, quantity) {
    		return $.ajax({
    			url: '/services/purchaseorder/changequantity',
    			method: 'POST',
    			data: {
    				purchaseOrderDetailId: purchaseOrderDetailId,
    				quantity: quantity
    			}
    		});
    	},
    	
    	checkPurchaseOrder: function(purchaseOrderId) {
    		return $.ajax({
    			url: '/services/purchaseorder/checkpurchaseorder',
    			method: 'POST',
    			data: {
    				purchaseOrderId: purchaseOrderId
    			}
    		});
    	},
    	
    	printPurchaseOrderCopy: function(purchaseOrderId) {
    		return $.ajax({
    			url: '/services/purchaseorder/printordercopy',
    			method: 'POST',
    			data: {
    				purchaseOrderId: purchaseOrderId
    			}
    		});
    	}
	};
});