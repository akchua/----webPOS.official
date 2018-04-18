define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/productservice', 'modules/purchaseorderservice', 'viewmodels/purchase-order/quantityform'], 
		function (dialog, app, ko, productService, purchaseOrderService, QuantityForm) {
    var SearchDetails = function(product, purchaseOrder, receiptType) {
    	this.product = product;
    	this.purchaseOrder = purchaseOrder;
    	this.receiptType = receiptType;
    	
    	this.beforeVatAndDiscount = ko.observable(false);
    	this.beforeVatAfterDiscount = ko.observable(false);
    	this.afterVatBeforeDiscount = ko.observable(false);
    	
    	this.productName = ko.observable();
    	
    	this.productDetailList = ko.observable();
    };
    
    SearchDetails.prototype.activate = function() {
    	var self = this;
    	
    	self.productName(self.product.name);
    	
    	productService.getProductDetailList(self.product.id, false).done(function (data) {
    		self.productDetailList(data);
    	});
    	
    	switch(self.receiptType) {
	    	case 'Before VAT and Discount':
	    		self.beforeVatAndDiscount(true);
	    		break;
	    	case 'Before VAT, After Discount':
	    		self.beforeVatAfterDiscount(true);
	    		break;
	    	case 'After VAT, Before Discount':
	    		self.afterVatBeforeDiscount(true);
	    		break;
		}
    };
    
    SearchDetails.show = function(product, purchaseOrder, receiptType) {
    	return dialog.show(new SearchDetails(product, purchaseOrder, receiptType));
    };
    
    SearchDetails.prototype.add = function(productDetailId, productUnitType) {
    	var self = this;
    	
    	QuantityForm.show(productDetailId, productUnitType, self.purchaseOrder.id, self.productName()).then(function() {
    		dialog.close(self);
    	});
    };
    
    SearchDetails.prototype.cancel = function() {
    	dialog.close(this);
    };
    
    return SearchDetails;
});