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
    	}
	};
});